package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.service.LaboratorioService;
import com.jdc.laboratorio.service.UsuarioService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/laboratorios")
public class LaboratorioController {

    private final LaboratorioService laboratorioService;
    private final UsuarioService usuarioService;

    public LaboratorioController(LaboratorioService laboratorioService, UsuarioService usuarioService) {
        this.laboratorioService = laboratorioService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("laboratorio", new Laboratorio());
        model.addAttribute("usuarios", usuarioService.listarTodos());
        return "crearLaboratorio";
    }

    @PostMapping
    public String guardarLaboratorio(@ModelAttribute Laboratorio laboratorio,
                                     @RequestParam("encargadoId") Integer encargadoId) {

        Usuario encargado = usuarioService.buscarPorId(encargadoId)
                .orElseThrow(() -> new IllegalArgumentException("Encargado no válido"));

        laboratorio.setEncargado(encargado);
        Laboratorio laboratorioGuardado = laboratorioService.guardar(laboratorio);

        encargado.setLaboratorio(laboratorioGuardado);
        usuarioService.guardar(encargado);

        return "redirect:/laboratorios/vista";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Laboratorio> obtenerPorId(@PathVariable Integer id) {
        return laboratorioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Laboratorio> listarTodos() {
        return laboratorioService.listarTodos();
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarLaboratorio(@PathVariable Integer id) {
        try {
            laboratorioService.eliminar(id);
            return "redirect:/laboratorios/vista?success=true";
        } catch (DataIntegrityViolationException e) {
            return "redirect:/laboratorios/vista?error=asociado";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/laboratorios/vista?error=general";
        }
    }

    @GetMapping("/vista")
    public String vistaLaboratorios(Model model) {
        model.addAttribute("laboratorios", laboratorioService.listarTodos());
        return "laboratorio";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormularioEditar(@PathVariable Integer id, Model model) {
        Laboratorio laboratorio = laboratorioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));
        List<Usuario> usuarios = usuarioService.listarTodos();

        model.addAttribute("laboratorio", laboratorio);
        model.addAttribute("usuarios", usuarios);
        return "editarLaboratorio";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarLaboratorio(@PathVariable Integer id,
                                        @ModelAttribute Laboratorio laboratorioActualizado,
                                        @RequestParam("encargadoId") Integer encargadoId) {

        Laboratorio laboratorio = laboratorioService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("Laboratorio no encontrado"));

        Usuario encargado = usuarioService.buscarPorId(encargadoId)
                .orElseThrow(() -> new IllegalArgumentException("Encargado no válido"));

        laboratorio.setNombre(laboratorioActualizado.getNombre());
        laboratorio.setSede(laboratorioActualizado.getSede());
        laboratorio.setEncargado(encargado);

        Laboratorio laboratorioActual = laboratorioService.guardar(laboratorio);

        encargado.setLaboratorio(laboratorioActual);
        usuarioService.guardar(encargado);

        return "redirect:/laboratorios/vista?editado=true";
    }
}
