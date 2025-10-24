package com.castores.inventario.controller;

import com.castores.inventario.model.Movimiento;
import com.castores.inventario.service.MovimientoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/historial")
public class HistorialController {

    private final MovimientoService movimientoService;

    public HistorialController(MovimientoService movimientoService) {
        this.movimientoService = movimientoService;
    }

    // Ver historial completo o filtrado (Solo Administrador)
    @GetMapping
    public String verHistorial(@RequestParam(required = false) String filtro, Model model) {
        List<Movimiento> movimientos;

        if (filtro != null && !filtro.isEmpty() && !filtro.equals("TODOS")) {
            movimientos = movimientoService.filtrarPorTipo(filtro);
            model.addAttribute("filtroActual", filtro);
        } else {
            movimientos = movimientoService.listarHistorial();
            model.addAttribute("filtroActual", "TODOS");
        }

        model.addAttribute("movimientos", movimientos);
        return "historial/listado";
    }
}