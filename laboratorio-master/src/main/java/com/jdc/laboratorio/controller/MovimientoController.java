package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Movimiento;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.Enums.TipoMovimiento;
import com.jdc.laboratorio.service.MovimientoService;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/movimientos")
public class MovimientoController {

    private final MovimientoService movimientoService;
    private final SustanciaRepository sustanciaRepository;

    public MovimientoController(MovimientoService movimientoService,
                                SustanciaRepository sustanciaRepository) {
        this.movimientoService = movimientoService;
        this.sustanciaRepository = sustanciaRepository;
    }

    // 📋 Listar todos los movimientos
    @GetMapping("/vista")
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoService.listarMovimientos());
        return "movimiento"; // ✅ este es el que tienes
    }

    // 🧾 Crear movimiento general (sin sustancia específica)
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("movimiento", new Movimiento());
        model.addAttribute("sustancias", sustanciaRepository.findAll());
        model.addAttribute("tipos", TipoMovimiento.values());
        return "crearMovimiento"; // ✅ asegúrate de tener este formulario
    }

    // 🧾 Crear movimiento para una sustancia específica
    @GetMapping("/nuevo/{idSustancia}")
    public String nuevoMovimientoPorSustancia(@PathVariable("idSustancia") Long idSustancia, Model model) {
        Sustancia sustancia = sustanciaRepository.findById(idSustancia)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setSustancia(sustancia);

        model.addAttribute("movimiento", movimiento);
        model.addAttribute("sustancia", sustancia);
        model.addAttribute("tipos", TipoMovimiento.values());

        return "crearMovimiento"; // ✅ reutiliza el mismo formulario
    }

    // 💾 Guardar movimiento
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Movimiento movimiento) {
        movimientoService.registrarMovimiento(movimiento);

        // Redirige según si viene de una sustancia específica
        Long idSustancia = movimiento.getSustancia() != null ? movimiento.getSustancia().getIdSustancia() : null;
        if (idSustancia != null) {
            return "redirect:/movimientos/sustancia/" + idSustancia;
        }

        return "redirect:/movimientos/vista";
    }

    // 🔍 Listar movimientos de una sustancia específica
    @GetMapping("/sustancia/{idSustancia}")
    public String listarPorSustancia(@PathVariable("idSustancia") Long idSustancia, Model model) {
        Sustancia sustancia = sustanciaRepository.findById(idSustancia)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));

        model.addAttribute("sustancia", sustancia);
        model.addAttribute("movimientos", movimientoService.listarPorSustancia(idSustancia));

        return "movimiento"; // ✅ usamos la vista que sí existe
    }
}
