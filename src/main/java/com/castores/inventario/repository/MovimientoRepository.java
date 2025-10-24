package com.castores.inventario.repository;

import com.castores.inventario.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {

    // Para mostrar historial completo (Administrador)
    List<Movimiento> findAllByOrderByFechaHoraDesc();

    // Para filtrar historial por tipo (ENTRADA o SALIDA)
    @Query("SELECT m FROM Movimiento m WHERE m.tipoMovimiento.nombreTipo = :nombreTipo ORDER BY m.fechaHora DESC")
    List<Movimiento> findByTipoMovimientoNombre(@Param("nombreTipo") String nombreTipo);
}

