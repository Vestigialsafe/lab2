package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.service.LaboratorioService;
import com.jdc.laboratorio.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/laboratorios")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;
    private final UsuarioService usuarioService; // ✅ Nuevo

    public LaboratorioController(LaboratorioService laboratorioService, UsuarioService usuarioService) {
        this.laboratorioService = laboratorioService;
        this.usuarioService = usuarioService;
    }

    // Formulario para crear un nuevo laboratorio
    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("laboratorio", new Laboratorio());

        // ✅ Agregamos lista de usuarios para seleccionar encargado
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);

        return "crearLaboratorio"; // debes crear la vista crearLaboratorio.html
    }

    // Guardar un nuevo laboratorio
    @PostMapping
    public String guardarLaboratorio(@ModelAttribute Laboratorio laboratorio,
                                     @RequestParam("encargadoId") Integer encargadoId) {
        Usuario encargado = usuarioService.buscarPorId(encargadoId)
                .orElseThrow(() -> new IllegalArgumentException("Encargado no válido"));
        laboratorio.setEncargado(encargado);

        laboratorioService.guardar(laboratorio);
        return "redirect:/laboratorios/vista";
    }


    // Buscar laboratorio por ID
    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> obtenerPorId(@PathVariable Integer id) {
        return laboratorioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Listar todos los laboratorios (JSON)
    @GetMapping
    public List<Laboratorio> listarTodos() {
        return laboratorioService.listarTodos();
    }

    // Eliminar laboratorio
    @PostMapping("/eliminar/{id}")
    public String eliminarLaboratorio(@PathVariable Integer id) {
        laboratorioService.eliminar(id);
        return "redirect:/laboratorios/vista";
    }

    // Vista en Thymeleaf con todos los laboratorios
    @GetMapping("/vista")
    public String vistaLaboratorios(Model model) {
        model.addAttribute("laboratorios", laboratorioService.listarTodos());
        return "laboratorio"; // debes crear laboratorio.html en /templates
    }
}
