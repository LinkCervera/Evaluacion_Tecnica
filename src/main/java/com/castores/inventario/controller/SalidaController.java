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

@Controller
@RequestMapping("/salida")
public class SalidaController {

    private final ProductoService productoService;
    private final MovimientoService movimientoService;
    private final AuthUtils authUtils;

    public SalidaController(ProductoService productoService,
                            MovimientoService movimientoService,
                            AuthUtils authUtils) {
        this.productoService = productoService;
        this.movimientoService = movimientoService;
        this.authUtils = authUtils;
    }

    // Ver mÃ³dulo de salida - Solo productos activos (Solo Almacenista)
    @GetMapping
    public String verSalida(Model model) {
        model.addAttribute("productos", productoService.listarActivos());
        return "salida/listado";
    }

    // Formulario para sacar inventario (Solo Almacenista)
    @GetMapping("/sacar/{id}")
    public String formularioSacar(@PathVariable Integer id, Model model) {
        Producto producto = productoService.buscarPorId(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        model.addAttribute("producto", producto);
        return "salida/sacar";
    }

    // Sacar inventario - Salida (Solo Almacenista)
    @PostMapping("/sacar/{id}")
    public String sacarInventario(@PathVariable Integer id,
                                  @RequestParam Integer cantidad,
                                  RedirectAttributes redirectAttributes) {
        try {
            Usuario usuario = authUtils.getUsuarioAutenticado();
            movimientoService.registrarSalida(id, cantidad, usuario);

            redirectAttributes.addFlashAttribute("success", "Salida de inventario registrada correctamente");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/salida";
    }
}