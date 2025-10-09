package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.service.SustanciasService;
import com.jdc.laboratorio.service.SubCategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private SustanciasService sustanciaService;

    @Autowired
    private SubCategoriaService subCategoriaService;

    @GetMapping
    public String mostrarDashboard(Model model) {

        // --- Datos principales ---
        long totalSustancias = sustanciaService.contarSustancias();
        long proximasAVencer = sustanciaService.contarProximasAVencer();
        long agotadas = sustanciaService.contarAgotadas();
        long totalSubCategorias = subCategoriaService.listarTodas().size();

        // --- Sustancias agrupadas por categoría ---
        Map<String, Long> sustanciasPorCategoria = new HashMap<>();
        List<Object[]> resultados = sustanciaService.contarPorCategoria();

        List<String> categorias = new ArrayList<>();
        List<Long> cantidades = new ArrayList<>();

        for (Object[] fila : resultados) {
            String categoria = (String) fila[0];
            Long cantidad = ((Number) fila[1]).longValue();
            sustanciasPorCategoria.put(categoria, cantidad);
            categorias.add(categoria);
            cantidades.add(cantidad);
        }

        // --- Sustancias próximas a vencer ---
        List<Sustancia> listaProximas = sustanciaService.listarProximasAVencer();

        // --- Sustancias agotadas ---
        List<Sustancia> listaAgotadas = sustanciaService.listarAgotadas();

        // --- Enviar datos a la vista ---
        model.addAttribute("totalSustancias", totalSustancias);
        model.addAttribute("proximasAVencer", proximasAVencer);
        model.addAttribute("agotadas", agotadas);
        model.addAttribute("totalSubCategorias", totalSubCategorias);
        model.addAttribute("categorias", categorias);
        model.addAttribute("cantidades", cantidades);
        model.addAttribute("listaProximas", listaProximas);
        model.addAttribute("listaAgotadas", listaAgotadas);

        return "dashboard";
    }
}
