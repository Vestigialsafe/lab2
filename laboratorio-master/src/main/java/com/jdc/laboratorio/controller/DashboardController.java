package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.service.LaboratorioService;
import com.jdc.laboratorio.service.SustanciasService;
import com.jdc.laboratorio.service.SubCategoriaService;
import com.jdc.laboratorio.service.UsuarioService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final SustanciasService sustanciaService;
    private final SubCategoriaService subCategoriaService;
    private final UsuarioService usuarioService;
    private final LaboratorioService laboratorioService;

    public DashboardController(SustanciasService sustanciaService,
                               SubCategoriaService subCategoriaService,
                               UsuarioService usuarioService,
                               LaboratorioService laboratorioService) {
        this.sustanciaService = sustanciaService;
        this.subCategoriaService = subCategoriaService;
        this.usuarioService = usuarioService;
        this.laboratorioService = laboratorioService;
    }

    @GetMapping
    public String mostrarDashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        long totalSustancias;
        long proximasAVencer;
        long agotadas;
        long totalSubCategorias;
        List<String> categorias = new ArrayList<>();
        List<Long> cantidades = new ArrayList<>();
        List<Sustancia> listaProximas;
        List<Sustancia> listaAgotadas;

        // 🔹 Si el usuario es SUPERADMIN → estadísticas globales
        if ("SUPERADMIN".equals(usuario.getRol())) {
            totalSustancias = sustanciaService.contarSustancias();
            proximasAVencer = sustanciaService.contarProximasAVencer();
            agotadas = sustanciaService.contarAgotadas();
            totalSubCategorias = subCategoriaService.listarTodas().size();

            List<Object[]> resultados = sustanciaService.contarPorCategoria();
            for (Object[] fila : resultados) {
                categorias.add((String) fila[0]);
                cantidades.add(((Number) fila[1]).longValue());
            }

            listaProximas = sustanciaService.listarProximasAVencer();
            listaAgotadas = sustanciaService.listarAgotadas();
        }

        // 🔹 Si el usuario es ADMIN → estadísticas solo de su laboratorio
        else {
            Laboratorio lab = usuario.getLaboratorio();
            if (lab == null) {
                totalSustancias = proximasAVencer = agotadas = totalSubCategorias = 0;
                listaProximas = new ArrayList<>();
                listaAgotadas = new ArrayList<>();
            } else {
                List<Sustancia> sustanciasLab = sustanciaService.buscarPorLaboratorio(lab.getIdLaboratorio());

                totalSustancias = sustanciasLab.size();
                listaProximas = sustanciaService.listarProximasAVencer().stream()
                        .filter(s -> s.getLaboratorio() != null && s.getLaboratorio().getIdLaboratorio().equals(lab.getIdLaboratorio()))
                        .collect(Collectors.toList());

                listaAgotadas = sustanciaService.listarAgotadas().stream()
                        .filter(s -> s.getLaboratorio() != null && s.getLaboratorio().getIdLaboratorio().equals(lab.getIdLaboratorio()))
                        .collect(Collectors.toList());

                proximasAVencer = listaProximas.size();
                agotadas = listaAgotadas.size();

                totalSubCategorias = subCategoriaService.listarTodas().stream()
                        .filter(sub -> sustanciasLab.stream()
                                .anyMatch(s -> s.getSubcategorias().contains(sub)))
                        .count();

                // Agrupar sustancias por categoría dentro del laboratorio
                Map<String, Long> conteo = sustanciasLab.stream()
                        .flatMap(s -> s.getSubcategorias().stream()
                                .map(sub -> sub.getCategoria().getNombre()))
                        .collect(Collectors.groupingBy(c -> c, Collectors.counting()));

                categorias.addAll(conteo.keySet());
                cantidades.addAll(conteo.values());
            }
        }

        // 📊 Enviar datos a la vista
        model.addAttribute("totalSustancias", totalSustancias);
        model.addAttribute("proximasAVencer", proximasAVencer);
        model.addAttribute("agotadas", agotadas);
        model.addAttribute("totalSubCategorias", totalSubCategorias);
        model.addAttribute("categorias", categorias);
        model.addAttribute("cantidades", cantidades);
        model.addAttribute("listaProximas", listaProximas);
        model.addAttribute("listaAgotadas", listaAgotadas);
        model.addAttribute("usuario", usuario);

        return "dashboard";
    }
    // 🚀 Nuevo: eliminar directamente desde el dashboard (sin validaciones)
    @PostMapping("/eliminarDashboard/{id}")
    public String eliminarDesdeDashboard(@PathVariable Long id, RedirectAttributes redirect) {
        try {
            sustanciaService.eliminarDirecto(id);
            redirect.addFlashAttribute("mensaje", "🗑 Sustancia vencida eliminada correctamente.");
        } catch (Exception e) {
            redirect.addFlashAttribute("error", "⚠️ No se pudo eliminar la sustancia vencida.");
        }
        return "redirect:/dashboard";
    }

}
