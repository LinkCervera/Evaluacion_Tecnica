package com.castores.inventario.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Table(name = "tipos_movimiento")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TipoMovimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "nombre_tipo", nullable = false, unique = true, length = 20)
    private String nombreTipo;

    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @OneToMany(mappedBy = "tipoMovimiento", fetch = FetchType.LAZY)
    private Set<Movimiento> movimientos;

    // Constructor personalizado sin relaciones
    public TipoMovimiento(Integer idTipo, String nombreTipo, String descripcion) {
        this.idTipo = idTipo;
        this.nombreTipo = nombreTipo;
        this.descripcion = descripcion;
    }
}
