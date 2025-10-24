package com.castores.inventario.repository;

import com.castores.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    // Para mostrar productos activos (Almacenista en salida)
    List<Producto> findByActivo(Boolean activo);

    // Para mostrar todos los productos ordenados (mÃ³dulo inventario)
    List<Producto> findAllByOrderByNombreProductoAsc();
}