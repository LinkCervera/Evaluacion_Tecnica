package com.castores.inventario.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "movimientos")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_movimiento")
    private Integer idMovimiento;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_producto", nullable = false)
    private Producto producto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_tipo", nullable = false)
    private TipoMovimiento tipoMovimiento;

    @Min(value = 1, message = "La cantidad debe ser mayor a 0")
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;

    @Column(name = "comentario", columnDefinition = "TEXT")
    private String comentario;

    // Constructor personalizado
    public Movimiento(Producto producto, Usuario usuario, TipoMovimiento tipoMovimiento,
                      Integer cantidad, String comentario) {
        this.producto = producto;
        this.usuario = usuario;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.comentario = comentario;
    }
}
