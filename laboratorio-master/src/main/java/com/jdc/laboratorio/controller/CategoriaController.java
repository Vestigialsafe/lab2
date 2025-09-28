package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Categoria;
import com.jdc.laboratorio.service.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Controller
@RequestMapping("/categorias")
public class CategoriaController {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/vista")
    public String listarCategorias(Model model) {
        model.addAttribute("categorias", categoriaService.listarCategorias());
        return "categorias";
    }

    @GetMapping("/crear")
    public String mostrarFormulario(Model model) {
        model.addAttribute("categoria", new Categoria());
        return "crearCategoria";
    }

    @PostMapping("/guardar")
    public String guardarCategoria(@ModelAttribute Categoria categoria,
                                   @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {
        if (!imagenFile.isEmpty()) {
            categoria.setImagen(imagenFile.getBytes()); // ✅ guarda los bytes directo en la DB
        }
        categoriaService.guardarCategoria(categoria);
        return "redirect:/categorias/vista";
    }


    // Endpoint para mostrar la imagen desde la DB
    @GetMapping("/imagen/{id}")
    @ResponseBody
    public byte[] mostrarImagen(@PathVariable Integer id) {
        Categoria cat = categoriaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        return cat.getImagen();
    }
}
