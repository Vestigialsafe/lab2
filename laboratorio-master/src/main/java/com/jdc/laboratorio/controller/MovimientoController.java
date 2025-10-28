package com.jdc.laboratorio.controller;

import com.jdc.laboratorio.model.Movimiento;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.Enums.TipoMovimiento;
import com.jdc.laboratorio.service.MovimientoService;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

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

    // 📋 Vista general de todos los movimientos
    @GetMapping("/vista")
    public String listar(Model model) {
        model.addAttribute("movimientos", movimientoService.listarMovimientos());
        return "movimiento";
    }

    // 🧾 Formulario de nuevo movimiento (general)
    @GetMapping("/nuevo")
    public String nuevo(Model model) {
        model.addAttribute("movimiento", new Movimiento());
        model.addAttribute("sustancias", sustanciaRepository.findAll());
        model.addAttribute("tipos", TipoMovimiento.values());
        return "crearMovimiento";
    }

    // 🧾 Nuevo movimiento para una sustancia específica
    @GetMapping("/nuevo/{idSustancia}")
    public String nuevoMovimientoPorSustancia(@PathVariable("idSustancia") Long idSustancia, Model model) {
        Sustancia sustancia = sustanciaRepository.findById(idSustancia)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));

        Movimiento movimiento = new Movimiento();
        movimiento.setSustancia(sustancia);

        model.addAttribute("movimiento", movimiento);
        model.addAttribute("sustancia", sustancia);
        model.addAttribute("tipos", TipoMovimiento.values());
        return "crearMovimiento";
    }

    // 💾 Guardar movimiento nuevo
    @PostMapping("/guardar")
    public String guardar(@ModelAttribute Movimiento movimiento) {
        movimientoService.registrarMovimiento(movimiento);

        Long idSustancia = movimiento.getSustancia() != null
                ? movimiento.getSustancia().getIdSustancia()
                : null;

        return (idSustancia != null)
                ? "redirect:/movimientos/sustancia/" + idSustancia
                : "redirect:/movimientos/vista";
    }

    // ✏️ Editar movimiento
    @GetMapping("/editar/{idMovimiento}")
    public String editarMovimiento(@PathVariable("idMovimiento") Long idMovimiento, Model model) {
        Movimiento movimiento = movimientoService.obtenerPorId(idMovimiento)
                .orElseThrow(() -> new IllegalArgumentException("Movimiento no encontrado"));

        model.addAttribute("movimiento", movimiento);
        model.addAttribute("sustancias", sustanciaRepository.findAll());
        model.addAttribute("tipos", TipoMovimiento.values());
        return "editarMovimiento";
    }

    // 💾 Actualizar movimiento existente (y stock)
    @PostMapping("/actualizar")
    public String actualizarMovimiento(@ModelAttribute Movimiento movimiento) {
        movimientoService.actualizarMovimiento(movimiento);

        Long idSustancia = movimiento.getSustancia() != null
                ? movimiento.getSustancia().getIdSustancia()
                : null;

        return (idSustancia != null)
                ? "redirect:/movimientos/sustancia/" + idSustancia
                : "redirect:/movimientos/vista";
    }

    // 🔍 Ver movimientos de una sustancia específica
    @GetMapping("/sustancia/{idSustancia}")
    public String listarPorSustancia(@PathVariable("idSustancia") Long idSustancia, Model model) {
        Sustancia sustancia = sustanciaRepository.findById(idSustancia)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));
        model.addAttribute("sustancia", sustancia);
        model.addAttribute("movimientos", movimientoService.listarPorSustancia(idSustancia));
        return "movimiento";
    }

    // 📤 Exportar movimientos a Excel
    @GetMapping("/exportar/{idSustancia}")
    public void exportarMovimientosAExcel(@PathVariable("idSustancia") Long idSustancia,
                                          HttpServletResponse response) throws IOException {

        response.setContentType("application/octet-stream");
        String nombreArchivo = "reporte_movimientos_sustancia_" + idSustancia + ".xlsx";
        response.setHeader("Content-Disposition", "attachment; filename=" + nombreArchivo);

        Sustancia sustancia = sustanciaRepository.findById(idSustancia)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));
        List<Movimiento> movimientos = movimientoService.listarPorSustancia(idSustancia);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Movimientos");

            Row titulo = sheet.createRow(0);
            Cell cellTitulo = titulo.createCell(0);
            cellTitulo.setCellValue("Reporte de Movimientos - " + sustancia.getNombre());

            CellStyle tituloStyle = workbook.createCellStyle();
            Font tituloFont = workbook.createFont();
            tituloFont.setBold(true);
            tituloFont.setFontHeightInPoints((short) 14);
            tituloStyle.setFont(tituloFont);
            cellTitulo.setCellStyle(tituloStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 5));

            Row header = sheet.createRow(2);
            String[] columnas = {"Fecha", "Tipo", "Cantidad", "Stock", "Procesos", "Descripción"};
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            for (int i = 0; i < columnas.length; i++) {
                Cell cell = header.createCell(i);
                cell.setCellValue(columnas[i]);
                cell.setCellStyle(headerStyle);
            }

            int fila = 3;
            for (Movimiento m : movimientos) {
                Row dataRow = sheet.createRow(fila++);
                dataRow.createCell(0).setCellValue(m.getFechaMovimiento() != null ? m.getFechaMovimiento().toString() : "-");
                dataRow.createCell(1).setCellValue(m.getTipoMovimiento() != null ? m.getTipoMovimiento().name() : "-");
                dataRow.createCell(2).setCellValue(m.getCantidad());
                dataRow.createCell(3).setCellValue(m.getStockPosterior() != null ? m.getStockPosterior() : 0);
                dataRow.createCell(4).setCellValue(m.getProcesos() != null ? m.getProcesos() : 0);
                dataRow.createCell(5).setCellValue(m.getDescripcion() != null ? m.getDescripcion() : "-");
            }

            for (int i = 0; i < columnas.length; i++) sheet.autoSizeColumn(i);

            workbook.write(response.getOutputStream());
        }
    }
}
