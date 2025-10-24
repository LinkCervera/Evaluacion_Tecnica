package com.castores.inventario.repository;

import com.castores.inventario.model.TipoMovimiento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TipoMovimientoRepository extends JpaRepository<TipoMovimiento, Integer> {

    // Para obtener el tipo ENTRADA o SALIDA al registrar movimientos
    Optional<TipoMovimiento> findByNombreTipo(String nombreTipo);
}
