package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    public UsuarioController(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "crearUsuario";
    }

    // ✅ Al guardar, el servicio cifrará automáticamente la contraseña
    @PostMapping
    public String guardarUsuario(@ModelAttribute Usuario usuario) {
        usuarioService.guardar(usuario);
        return "redirect:/usuarios/vista";
    }

    @GetMapping("/{id}")
    public ResponseEntity<Usuario> obtenerPorId(@PathVariable Integer id) {
        return usuarioService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar")
    public ResponseEntity<Usuario> obtenerPorUserName(@RequestParam String userName) {
        return usuarioService.buscarPorUserName(userName)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Usuario> listarTodos() {
        return usuarioService.listarTodos();
    }

    // ✅ Mantenemos POST para eliminar (seguro)
    @PostMapping("/eliminar/{id}")
    public String eliminarUsuario(@PathVariable Integer id) {
        usuarioService.eliminar(id);
        return "redirect:/usuarios/vista";
    }

    @GetMapping("/vista")
    public String vistaUsuarios(Model model) {
        List<Usuario> usuarios = usuarioService.listarTodos();
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("totalUsuarios", usuarios.size());
        return "usuario";
    }
}
