package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.SubCategoria;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.service.LaboratorioService;
import com.jdc.laboratorio.service.SubCategoriaService;
import com.jdc.laboratorio.service.SustanciasService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/sustancias")
public class SustanciaController {

    private final SustanciasService sustanciaService;
    private final LaboratorioService laboratorioService;
    private final SubCategoriaService subCategoriaService;

    public SustanciaController(SustanciasService sustanciaService,
                               LaboratorioService laboratorioService,
                               SubCategoriaService subCategoriaService) {
        this.sustanciaService = sustanciaService;
        this.laboratorioService = laboratorioService;
        this.subCategoriaService = subCategoriaService;
    }

    // 📌 Listar todas
    @GetMapping("/vista")
    public String listar(Model model) {
        model.addAttribute("sustancias", sustanciaService.listarTodas());
        return "sustancias";
    }

    // 📌 Formulario crear
    @GetMapping("/crear")
    public String mostrarFormularioCrear(
            @RequestParam(value = "idSubCategoria", required = false) Integer idSubCategoria,
            Model model) {

        model.addAttribute("sustancia", new Sustancia());
        model.addAttribute("laboratorios", laboratorioService.listarTodos());
        model.addAttribute("subcategorias", subCategoriaService.listarTodas());

        // Guardamos el idSubCategoria en contexto para el formulario
        model.addAttribute("idSubCategoriaContexto", idSubCategoria);

        return "crearSustancia";
    }


    // 📌 Guardar
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Sustancia sustancia,
                          @RequestParam(value = "documentoFile", required = false) MultipartFile documentoFile,
                          @RequestParam("idLaboratorio") Integer idLaboratorio,
                          @RequestParam(value = "subcategoriasSeleccionadas", required = false)
                          List<Integer> subcategoriasSeleccionadas,
                          @RequestParam(value = "idSubCategoriaContexto", required = false) Integer idSubCategoriaContexto,
                          RedirectAttributes redirectAttrs) throws IOException {

        // 📌 Guardar documento si viene cargado
        if (documentoFile != null && !documentoFile.isEmpty()) {
            sustancia.setDocumentacion(documentoFile.getBytes());
        }

        // 📌 Asignar laboratorio
        laboratorioService.buscarPorId(idLaboratorio).ifPresent(sustancia::setLaboratorio);

        // 📌 Asignar subcategorías si hay seleccionadas
        if (subcategoriasSeleccionadas != null && !subcategoriasSeleccionadas.isEmpty()) {
            List<SubCategoria> seleccionadas = subcategoriasSeleccionadas.stream()
                    .map(id -> subCategoriaService.buscarPorId(id).orElse(null))
                    .filter(sc -> sc != null)
                    .toList();
            sustancia.setSubcategorias(seleccionadas);
        }

        // 📌 Guardar sustancia
        sustanciaService.guardar(sustancia);

        // 📌 Mensaje de confirmación
        redirectAttrs.addFlashAttribute("mensaje", "Sustancia guardada ✅");

        // 📌 Redirigir según contexto
        if (idSubCategoriaContexto != null) {
            return "redirect:/sustancias/subcategoria/" + idSubCategoriaContexto;
        } else {
            return "redirect:/sustancias/vista";
        }
    }


    // 📌 Eliminar
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(value = "subcat", required = false) Integer idSubCategoria,
                           RedirectAttributes redirectAttrs) {
        sustanciaService.eliminar(id);
        redirectAttrs.addFlashAttribute("mensaje", "Sustancia eliminada ❌");

        // Si viene desde una subcategoría, lo redirigimos allá
        if (idSubCategoria != null) {
            return "redirect:/sustancias/subcategoria/" + idSubCategoria;
        }

        // Si no, volvemos a la vista general
        return "redirect:/sustancias/vista";
    }


    // 📌 Descargar documento
    @GetMapping("/documento/{id}")
    public ResponseEntity<byte[]> verDocumento(@PathVariable Long id) {
        return sustanciaService.buscarPorId(id)
                .filter(s -> s.getDocumentacion() != null)
                .map(s -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=documento.pdf")
                        .contentType(MediaType.APPLICATION_PDF)
                        .body(s.getDocumentacion()))
                .orElse(ResponseEntity.notFound().build());
    }

    // 📌 Listar sustancias de una subcategoría
    @GetMapping("/subcategoria/{idSubCategoria}")
    public String listarPorSubcategoria(@PathVariable Integer idSubCategoria, Model model) {
        List<Sustancia> sustancias = sustanciaService.buscarPorSubcategoria(idSubCategoria);

        model.addAttribute("sustancias", sustancias);
        model.addAttribute("idSubCategoria", idSubCategoria);

        subCategoriaService.buscarPorId(idSubCategoria).ifPresent(
                sub -> model.addAttribute("titulo", "Sustancias de la subcategoría: " + sub.getNombre())
        );

        return "sustancias";
    }

}
