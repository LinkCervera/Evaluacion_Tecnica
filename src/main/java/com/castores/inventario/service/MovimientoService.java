package com.castores.inventario.service;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.model.Producto;
import com.castores.inventario.model.TipoMovimiento;
import com.castores.inventario.model.Usuario;
import com.castores.inventario.repository.MovimientoRepository;
import com.castores.inventario.repository.TipoMovimientoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final TipoMovimientoRepository tipoMovimientoRepository;
    private final ProductoService productoService;

    public MovimientoService(MovimientoRepository movimientoRepository,
                             TipoMovimientoRepository tipoMovimientoRepository,
                             ProductoService productoService) {
        this.movimientoRepository = movimientoRepository;
        this.tipoMovimientoRepository = tipoMovimientoRepository;
        this.productoService = productoService;
    }

    // Registrar entrada
    public void registrarEntrada(Integer idProducto, Integer cantidad, Usuario usuario) throws Exception {
        Producto producto = productoService.aumentarInventario(idProducto, cantidad);

        TipoMovimiento tipoEntrada = tipoMovimientoRepository.findByNombreTipo("ENTRADA")
                .orElseThrow(() -> new Exception("Tipo ENTRADA no encontrado"));

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoEntrada);
        movimiento.setCantidad(cantidad);
        movimiento.setUsuario(usuario);
        movimiento.setFechaHora(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }

    // Registrar salida
    public void registrarSalida(Integer idProducto, Integer cantidad, Usuario usuario) throws Exception {
        Producto producto = productoService.disminuirInventario(idProducto, cantidad);

        TipoMovimiento tipoSalida = tipoMovimientoRepository.findByNombreTipo("SALIDA")
                .orElseThrow(() -> new Exception("Tipo SALIDA no encontrado"));

        Movimiento movimiento = new Movimiento();
        movimiento.setProducto(producto);
        movimiento.setTipoMovimiento(tipoSalida);
        movimiento.setCantidad(cantidad);
        movimiento.setUsuario(usuario);
        movimiento.setFechaHora(LocalDateTime.now());

        movimientoRepository.save(movimiento);
    }

    // Listar historial completo
    public List<Movimiento> listarHistorial() {
        return movimientoRepository.findAllByOrderByFechaHoraDesc();
    }

    // Filtrar por tipo (ENTRADA o SALIDA)
    public List<Movimiento> filtrarPorTipo(String nombreTipo) {
        return movimientoRepository.findByTipoMovimientoNombre(nombreTipo);
    }
}