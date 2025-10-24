package com.castores.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "productos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    @Column(name = "nombre_producto", nullable = false, length = 100)
    private String nombreProducto;

    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;

    @Min(value = 0, message = "La cantidad actual no puede ser negativa")
    @Column(name = "cantidad_actual", nullable = false)
    private Integer cantidadActual = 0;

    @Column(name = "activo", nullable = false)
    private Boolean activo = true;

    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;

    @UpdateTimestamp
    @Column(name = "fecha_modificacion")
    private LocalDateTime fechaModificacion;

    @OneToMany(mappedBy = "producto", fetch = FetchType.LAZY)
    private Set<Movimiento> movimientos;

    // Constructor personalizado sin relaciones
    public Producto(String nombreProducto, String descripcion, Integer cantidadActual, Boolean activo) {
        this.nombreProducto = nombreProducto;
        this.descripcion = descripcion;
        this.cantidadActual = cantidadActual;
        this.activo = activo;
    }
}
