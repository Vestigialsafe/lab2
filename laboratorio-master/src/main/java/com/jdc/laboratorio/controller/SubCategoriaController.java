package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.SubCategoria;
import com.jdc.laboratorio.model.Categoria;
import com.jdc.laboratorio.service.SubCategoriaService;
import com.jdc.laboratorio.service.CategoriaService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/subcategorias")
public class SubCategoriaController {

    @Autowired
    private SubCategoriaService subCategoriaService;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/vista/{idCategoria}")
    public String listarSubCategoriasPorCategoria(@PathVariable Integer idCategoria, Model model) {
        model.addAttribute("subcategorias", subCategoriaService.listarPorCategoria(idCategoria));
        Categoria categoria = categoriaService.buscarPorId(idCategoria)
                .orElseThrow(() -> new IllegalArgumentException("ID de categoría inválido: " + idCategoria));
        model.addAttribute("categoria", categoria);
        return "subcategorias";
    }

    @GetMapping("/crear")
    public String crearSubCategoriaForm(Model model) {
        SubCategoria subCategoria = new SubCategoria();
        subCategoria.setCategoria(new Categoria()); // 👈 evita el null
        model.addAttribute("subCategoria", subCategoria);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "crearSubcategoria";
    }


    @PostMapping("/guardar")
    public String guardarSubCategoria(@ModelAttribute SubCategoria subCategoria,
                                      @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {

        if (!imagenFile.isEmpty()) {
            subCategoria.setImagen(imagenFile.getBytes()); // Guardar en base de datos
        }

        subCategoriaService.guardar(subCategoria);
        return "redirect:/subcategorias/vista/" + subCategoria.getCategoria().getIdCategoria();
    }

    @GetMapping("/editar/{id}")
    public String editarSubCategoriaForm(@PathVariable Integer id, Model model) {
        SubCategoria subCategoria = subCategoriaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        model.addAttribute("subCategoria", subCategoria);
        model.addAttribute("categorias", categoriaService.listarTodas());
        return "editarSubcategoria";
    }

    @PostMapping("/actualizar/{id}")
    public String actualizarSubCategoria(@PathVariable Integer id,
                                         @ModelAttribute SubCategoria subCategoria,
                                         @RequestParam("imagenFile") MultipartFile imagenFile) throws IOException {
        SubCategoria existente = subCategoriaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        existente.setNombre(subCategoria.getNombre());
        existente.setCategoria(subCategoria.getCategoria());

        if (!imagenFile.isEmpty()) {
            existente.setImagen(imagenFile.getBytes());
        }

        subCategoriaService.guardar(existente);
        return "redirect:/subcategorias/vista/" + subCategoria.getCategoria().getIdCategoria();
    }

    @PostMapping("/eliminar/{id}")
    public String eliminarSubCategoria(@PathVariable Integer id) {
        SubCategoria subCategoria = subCategoriaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));

        Integer idCategoria = subCategoria.getCategoria().getIdCategoria();
        subCategoriaService.eliminar(id);
        return "redirect:/subcategorias/vista/" + idCategoria;
    }

    // Endpoint para mostrar la imagen
    @GetMapping("/imagen/{id}")
    @ResponseBody
    public byte[] mostrarImagen(@PathVariable Integer id) {
        SubCategoria sub = subCategoriaService.buscarPorId(id)
                .orElseThrow(() -> new IllegalArgumentException("ID inválido: " + id));
        return sub.getImagen();
    }
}
