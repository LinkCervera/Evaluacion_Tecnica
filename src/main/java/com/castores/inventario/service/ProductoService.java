package com.castores.inventario.service;

import com.castores.inventario.model.Producto;
import com.castores.inventario.repository.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    // Crear nuevo producto (cantidad inicial = 0)
    public Producto crearProducto(Producto producto) {
        producto.setCantidadActual(0);
        producto.setActivo(true);
        return productoRepository.save(producto);
    }

    // Aumentar inventario (Entrada de productos)
    public Producto aumentarInventario(Integer idProducto, Integer cantidad) throws Exception {
        if (cantidad <= 0) {
            throw new Exception("No se puede disminuir el inventario desde esta opciÃ³n");
        }

        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        producto.setCantidadActual(producto.getCantidadActual() + cantidad);
        return productoRepository.save(producto);
    }

    // Disminuir inventario (Salida de productos)
    public Producto disminuirInventario(Integer idProducto, Integer cantidad) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));

        if (producto.getCantidadActual() < cantidad) {
            throw new Exception("No hay suficiente inventario disponible");
        }

        producto.setCantidadActual(producto.getCantidadActual() - cantidad);
        return productoRepository.save(producto);
    }

    // Dar de baja un producto
    public void darDeBaja(Integer idProducto) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
        producto.setActivo(false);
        productoRepository.save(producto);
    }

    // Reactivar un producto
    public void reactivarProducto(Integer idProducto) throws Exception {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new Exception("Producto no encontrado"));
        producto.setActivo(true);
        productoRepository.save(producto);
    }

    // Listar todos los productos (activos e inactivos)
    public List<Producto> listarTodos() {
        return productoRepository.findAllByOrderByNombreProductoAsc();
    }

    // Listar solo productos activos (para salida)
    public List<Producto> listarActivos() {
        return productoRepository.findByActivo(true);
    }

    // Buscar por ID
    public Optional<Producto> buscarPorId(Integer idProducto) {
        return productoRepository.findById(idProducto);
    }
}
