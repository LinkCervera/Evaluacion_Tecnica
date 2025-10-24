package com.castores.inventario.service;

import com.castores.inventario.model.TipoMovimiento;
import com.castores.inventario.repository.TipoMovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class TipoMovimientoService {

    private final TipoMovimientoRepository tipoMovimientoRepository;

    public TipoMovimientoService(TipoMovimientoRepository tipoMovimientoRepository) {
        this.tipoMovimientoRepository = tipoMovimientoRepository;
    }

    public Optional<TipoMovimiento> buscarPorNombre(String nombreTipo) {
        return tipoMovimientoRepository.findByNombreTipo(nombreTipo);
    }

    public TipoMovimiento guardar(TipoMovimiento tipoMovimiento) {
        return tipoMovimientoRepository.save(tipoMovimiento);
    }
}
