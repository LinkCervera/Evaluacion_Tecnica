package com.castores.inventario.controller;

import com.castores.inventario.model.Producto;
import com.castores.inventario.model.Usuario;
import com.castores.inventario.service.MovimientoService;
import com.castores.inventario.service.ProductoService;
import com.castores.inventario.utils.AuthUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;

@Controller
@RequestMapping("/inventario")
public class InventarioController {

    private final ProductoService productoService;
    private final MovimientoService movimientoService;
    private final AuthUtils authUtils;

    public InventarioController(ProductoService productoService,
                                MovimientoService movimientoService,
                                AuthUtils authUtils) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
        this.authUtils = authUtils;
    }

    // Ver modulo de inventario (Ambos roles)
    @GetMapping
    public String verInventario(Model model) {
        model.addAttribute("productos", productoService.listarTodos());
        return "inventario/listado";
    }

    // Formulario para agregar nuevo producto (Solo Administrador)
    @GetMapping("/agregar")
    public String formularioAgregar(Model model) {
        model.addAttribute("producto", new Producto());
        return "inventario/agregar";
    }

    // Guardar nuevo producto (Solo Administrador)
    @PostMapping("/agregar")
    public String agregarProducto(@RequestParam String nombreProducto,
                                  @RequestParam(required = false) String descripcion,
                                  RedirectAttributes redirectAttributes) {
        try {
            Producto producto = new Producto();
            producto.setNombreProducto(nombreProducto);
            producto.setDescripcion(descripcion);

            productoService.crearProducto(producto);

            redirectAttributes.addFlashAttribute("success", "Producto agregado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error al agregar producto: " + e.getMessage());
        }

        return "redirect:/inventario";
    }

    // Formulario para aumentar inventario (Solo Administrador)
    @GetMapping("/aumentar/{id}")
    public String formularioAumentar(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "inventario/aumentar";
    }

    // Aumentar inventario - Entrada (Solo Administrador)
    @PostMapping("/aumentar/{id}")
    public String aumentarInventario(@PathVariable Integer id,
                                     @RequestParam Integer cantidad,
                                     RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = authUtils.getUsuarioAutenticado();
            movimientoService.registrarEntrada(id, cantidad, usuario);

            redirectAttributes.addFlashAttribute("success", "Inventario aumentado correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/inventario";
    }

    // Dar de baja producto (Solo Administrador)
    @PostMapping("/baja/{id}")
    public String darDeBaja(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.darDeBaja(id);
            redirectAttributes.addFlashAttribute("success", "Producto dado de baja");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/inventario";
    }

    // Reactivar producto (Solo Administrador)
    @PostMapping("/reactivar/{id}")
    public String reactivar(@PathVariable Integer id, RedirectAttributes redirectAttributes) {
        try {
            productoService.reactivarProducto(id);
            redirectAttributes.addFlashAttribute("success", "Producto reactivado");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/inventario";
    }
}