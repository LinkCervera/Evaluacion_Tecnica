-- Crear base de datos
CREATE DATABASE IF NOT EXISTS sistema_inventario;
USE sistema_inventario;

-- TABLA: ROLES
CREATE TABLE roles (
    id_rol INT AUTO_INCREMENT PRIMARY KEY,
    nombre_rol VARCHAR(50) NOT NULL UNIQUE,
    descripcion VARCHAR(255),
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA: USUARIOS
CREATE TABLE usuarios (
    id_usuario INT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    username VARCHAR(50) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    id_rol INT NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (id_rol) REFERENCES roles(id_rol)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA: PRODUCTOS
CREATE TABLE productos (
    id_producto INT AUTO_INCREMENT PRIMARY KEY,
    nombre_producto VARCHAR(100) NOT NULL,
    descripcion TEXT,
    cantidad_actual INT NOT NULL DEFAULT 0,
    activo BOOLEAN DEFAULT TRUE,
    fecha_creacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    fecha_modificacion TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CHECK (cantidad_actual >= 0)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA: TIPOS_MOVIMIENTO
CREATE TABLE tipos_movimiento (
    id_tipo INT AUTO_INCREMENT PRIMARY KEY,
    nombre_tipo VARCHAR(20) NOT NULL UNIQUE,
    descripcion VARCHAR(100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- TABLA: MOVIMIENTOS
CREATE TABLE movimientos (
    id_movimiento INT AUTO_INCREMENT PRIMARY KEY,
    id_producto INT NOT NULL,
    id_usuario INT NOT NULL,
    id_tipo INT NOT NULL,
    cantidad INT NOT NULL,
    fecha_hora TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    comentario TEXT,
    CHECK (cantidad > 0),
    FOREIGN KEY (id_producto) REFERENCES productos(id_producto),
    FOREIGN KEY (id_usuario) REFERENCES usuarios(id_usuario),
    FOREIGN KEY (id_tipo) REFERENCES tipos_movimiento(id_tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ÍNDICES PARA OPTIMIZACIÓN
CREATE INDEX idx_productos_activo ON productos(activo);
CREATE INDEX idx_movimientos_fecha ON movimientos(fecha_hora);
CREATE INDEX idx_movimientos_tipo ON movimientos(id_tipo);
CREATE INDEX idx_usuarios_username ON usuarios(username);

-- DATOS INICIALES
-- Insertar roles
INSERT INTO roles (nombre_rol, descripcion) VALUES 
('Administrador', 'Acceso completo al sistema excepto salida de productos'),
('Almacenista', 'Puede gestionar salida de productos y ver inventario');

-- Insertar tipos de movimiento
INSERT INTO tipos_movimiento (nombre_tipo, descripcion) VALUES 
('ENTRADA', 'Entrada de productos al inventario'),
('SALIDA', 'Salida de productos del inventario');

-- Insertar usuarios de ejemplo (password: 'admin123' y 'almacen123')
INSERT INTO usuarios (nombre, username, password_hash, id_rol) VALUES 
('Administrador Sistema', 'admin', '$2a$12$JKP/T2tWhZc8LDAQMIv.r.na5VTAEr67vfd6Uh.WkkTckcgKLmxLy', 1),
('Almacenista Principal', 'almacenista', '$2a$12$KKDbvVTE86GXkf0GVtvuZuce.H1ylfWiZFO/kiaTyInMY56a1FtMG', 2);

-- TRIGGER: Actualizar inventario en movimientos
DELIMITER //

CREATE TRIGGER trg_actualizar_inventario_entrada
AFTER INSERT ON movimientos
FOR EACH ROW
BEGIN
    DECLARE tipo_mov VARCHAR(20);
    
    SELECT nombre_tipo INTO tipo_mov 
    FROM tipos_movimiento 
    WHERE id_tipo = NEW.id_tipo;
    
    IF tipo_mov = 'ENTRADA' THEN
        UPDATE productos 
        SET cantidad_actual = cantidad_actual + NEW.cantidad
        WHERE id_producto = NEW.id_producto;
    ELSEIF tipo_mov = 'SALIDA' THEN
        UPDATE productos 
        SET cantidad_actual = cantidad_actual - NEW.cantidad
        WHERE id_producto = NEW.id_producto;
    END IF;
END//

DELIMITER ;

-- STORED PROCEDURE: Registrar movimiento
DELIMITER //

CREATE PROCEDURE sp_registrar_movimiento(
    IN p_id_producto INT,
    IN p_id_usuario INT,
    IN p_tipo_movimiento VARCHAR(20),
    IN p_cantidad INT,
    IN p_comentario TEXT
)
BEGIN
    DECLARE v_id_tipo INT;
    DECLARE v_cantidad_actual INT;
    
    -- Obtener id del tipo de movimiento
    SELECT id_tipo INTO v_id_tipo 
    FROM tipos_movimiento 
    WHERE nombre_tipo = p_tipo_movimiento;
    
    -- Verificar cantidad actual si es salida
    IF p_tipo_movimiento = 'SALIDA' THEN
        SELECT cantidad_actual INTO v_cantidad_actual
        FROM productos
        WHERE id_producto = p_id_producto;
        
        IF v_cantidad_actual < p_cantidad THEN
            SIGNAL SQLSTATE '45000'
            SET MESSAGE_TEXT = 'Cantidad insuficiente en inventario';
        END IF;
    END IF;
    
    -- Insertar movimiento
    INSERT INTO movimientos (id_producto, id_usuario, id_tipo, cantidad, comentario)
    VALUES (p_id_producto, p_id_usuario, v_id_tipo, p_cantidad, p_comentario);
    
END//

DELIMITER ;

-- VISTA: Resumen de inventario
CREATE VIEW vista_inventario AS
SELECT 
    p.id_producto,
    p.nombre_producto,
    p.descripcion,
    p.cantidad_actual,
    p.activo,
    COALESCE(SUM(CASE WHEN tm.nombre_tipo = 'ENTRADA' THEN m.cantidad ELSE 0 END), 0) as total_entradas,
    COALESCE(SUM(CASE WHEN tm.nombre_tipo = 'SALIDA' THEN m.cantidad ELSE 0 END), 0) as total_salidas
FROM productos p
LEFT JOIN movimientos m ON p.id_producto = m.id_producto
LEFT JOIN tipos_movimiento tm ON m.id_tipo = tm.id_tipo
GROUP BY p.id_producto, p.nombre_producto, p.descripcion, p.cantidad_actual, p.activo;