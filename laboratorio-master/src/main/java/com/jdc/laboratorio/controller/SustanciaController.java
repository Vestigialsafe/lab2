package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.model.SubCategoria;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.model.Usuario;
import com.jdc.laboratorio.service.LaboratorioService;
import com.jdc.laboratorio.service.SubCategoriaService;
import com.jdc.laboratorio.service.SustanciasService;
import com.jdc.laboratorio.service.UsuarioService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final UsuarioService usuarioService;

    public SustanciaController(SustanciasService sustanciaService,
                               LaboratorioService laboratorioService,
                               SubCategoriaService subCategoriaService,
                               UsuarioService usuarioService) {
        this.sustanciaService = sustanciaService;
        this.laboratorioService = laboratorioService;
        this.subCategoriaService = subCategoriaService;
        this.usuarioService = usuarioService;
    }

    // 📌 Listar todas las sustancias (según rol)
    @GetMapping("/vista")
    public String listar(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Sustancia> sustancias;

        if ("SUPERADMIN".equals(usuario.getRol())) {
            sustancias = sustanciaService.listarTodas();
        } else {
            Laboratorio lab = usuario.getLaboratorio();
            sustancias = (lab != null)
                    ? sustanciaService.buscarPorLaboratorio(lab.getIdLaboratorio())
                    : List.of();
        }

        model.addAttribute("sustancias", sustancias);
        model.addAttribute("titulo", "Listado de Sustancias");
        return "sustancias";
    }

    // 📌 Mostrar formulario para crear nueva sustancia
    @GetMapping("/crear")
    public String mostrarFormularioCrear(
            @RequestParam(value = "idSubCategoria", required = false) Integer idSubCategoria,
            Model model) {

        // Obtener usuario autenticado
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // Crear nueva sustancia
        Sustancia nueva = new Sustancia();
        model.addAttribute("sustancia", nueva);

        // Listar todas las subcategorías
        List<SubCategoria> subcategorias = subCategoriaService.listarTodas();
        model.addAttribute("subcategorias", subcategorias);
        model.addAttribute("idSubCategoriaContexto", idSubCategoria);

        // Configurar laboratorios según rol
        List<Laboratorio> laboratorios;
        if ("SUPERADMIN".equals(usuario.getRol())) {
            laboratorios = laboratorioService.listarTodos();
        } else {
            Laboratorio lab = usuario.getLaboratorio();
            laboratorios = (lab != null) ? List.of(lab) : List.of(); // seguro ante null
        }
        model.addAttribute("laboratorios", laboratorios);

        return "crearSustancia";
    }


    // 📌 Guardar sustancia
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Sustancia sustancia,
                          @RequestParam(value = "documentoFile", required = false) MultipartFile documentoFile,
                          @RequestParam("idLaboratorio") Integer idLaboratorio,
                          @RequestParam(value = "subcategoriasSeleccionadas", required = false)
                          List<Integer> subcategoriasSeleccionadas,
                          @RequestParam(value = "idSubCategoriaContexto", required = false) Integer idSubCategoriaContexto,
                          RedirectAttributes redirectAttrs) throws IOException {

        if (documentoFile != null && !documentoFile.isEmpty()) {
            sustancia.setDocumentacion(documentoFile.getBytes());
        }

        laboratorioService.buscarPorId(idLaboratorio).ifPresent(sustancia::setLaboratorio);

        if (subcategoriasSeleccionadas != null && !subcategoriasSeleccionadas.isEmpty()) {
            List<SubCategoria> seleccionadas = subcategoriasSeleccionadas.stream()
                    .map(id -> subCategoriaService.buscarPorId(id).orElse(null))
                    .filter(sc -> sc != null)
                    .toList();
            sustancia.setSubcategorias(seleccionadas);
        }

        sustanciaService.guardar(sustancia);
        redirectAttrs.addFlashAttribute("mensaje", "✅ Sustancia guardada correctamente");

        if (idSubCategoriaContexto != null) {
            return "redirect:/sustancias/subcategoria/" + idSubCategoriaContexto;
        } else {
            return "redirect:/sustancias/vista";
        }
    }

    // 📌 Eliminar sustancia
    @GetMapping("/eliminar/{id}")
    public String eliminar(@PathVariable Long id,
                           @RequestParam(value = "subcat", required = false) Integer idSubCategoria,
                           RedirectAttributes redirectAttrs) {
        try {
            sustanciaService.eliminar(id);
            redirectAttrs.addFlashAttribute("mensaje", "❌ Sustancia eliminada correctamente");
        } catch (Exception e) {
            redirectAttrs.addFlashAttribute("error",
                    "⚠️ No se puede eliminar la sustancia porque tiene movimientos registrados.");
        }

        if (idSubCategoria != null) {
            return "redirect:/sustancias/subcategoria/" + idSubCategoria;
        }
        return "redirect:/sustancias/vista";
    }

    // 📌 Descargar documento PDF
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

    // 📌 Listar sustancias según subcategoría (filtradas por laboratorio si aplica)
    @GetMapping("/subcategoria/{idSubCategoria}")
    public String listarPorSubcategoria(@PathVariable Integer idSubCategoria, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioService.buscarPorUserName(auth.getName())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Sustancia> sustancias;

        if ("SUPERADMIN".equals(usuario.getRol())) {
            sustancias = sustanciaService.buscarPorSubcategoria(idSubCategoria);
        } else {
            Laboratorio lab = usuario.getLaboratorio();
            sustancias = (lab != null)
                    ? sustanciaService.buscarPorSubcategoriaYLaboratorio(idSubCategoria, lab.getIdLaboratorio())
                    : List.of();
        }

        model.addAttribute("sustancias", sustancias);
        model.addAttribute("idSubCategoria", idSubCategoria);

        subCategoriaService.buscarPorId(idSubCategoria).ifPresent(
                sub -> model.addAttribute("titulo", "Sustancias de la subcategoría: " + sub.getNombre())
        );

        return "sustancias";
    }
}
