package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.repository.MovimientoRepository;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SustanciaServiceImpl implements SustanciasService {

    private final SustanciaRepository sustanciaRepository;
    private final MovimientoRepository movimientoRepository;

    public SustanciaServiceImpl(SustanciaRepository sustanciaRepository,
                                MovimientoRepository movimientoRepository) {
        this.sustanciaRepository = sustanciaRepository;
        this.movimientoRepository = movimientoRepository;
    }

    @Override
    public List<Sustancia> listarTodas() {
        return sustanciaRepository.findAll();
    }

    @Override
    public Optional<Sustancia> buscarPorId(Long id) {
        return sustanciaRepository.findById(id);
    }

    @Override
    public Sustancia guardar(Sustancia sustancia) {
        return sustanciaRepository.save(sustancia);
    }

    // 🔹 Método con validación normal (no elimina si hay movimientos)
    @Override
    public void eliminar(Long id) {
        Sustancia s = sustanciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));
        if (!movimientoRepository.findBySustanciaIdSustancia(s.getIdSustancia()).isEmpty()) {
            throw new IllegalStateException("No se puede eliminar porque tiene movimientos.");
        }
        sustanciaRepository.delete(s);
    }

    // 🔹 Método forzado desde el dashboard (sí elimina movimientos)
    @Override
    @Transactional
    public void eliminarDirecto(Long id) {
        Sustancia s = sustanciaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));
        movimientoRepository.deleteAllBySustancia(s);
        sustanciaRepository.delete(s);
    }

    @Override
    public List<Sustancia> buscarPorSubcategoria(Integer idSubCategoria) {
        return sustanciaRepository.findBySubcategorias_IdSubCategoria(idSubCategoria);
    }

    @Override
    public List<Sustancia> buscarPorSubcategoriaYLaboratorio(Integer idSubCategoria, Integer idLaboratorio) {
        return sustanciaRepository.findBySubcategorias_IdSubCategoriaAndLaboratorio_IdLaboratorio(idSubCategoria, idLaboratorio);
    }

    @Override
    public long contarSustancias() {
        return sustanciaRepository.count();
    }

    @Override
    public long contarProximasAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusMonths(1);
        return sustanciaRepository.countByFechaVencimientoBetween(hoy, limite);
    }

    @Override
    public long contarAgotadas() {
        return sustanciaRepository.countByStockLessThanEqual(0);
    }

    @Override
    public List<Object[]> contarPorCategoria() {
        return sustanciaRepository.contarPorCategoria();
    }

    @Override
    public List<Sustancia> listarProximasAVencer() {
        LocalDate hoy = LocalDate.now();
        LocalDate limite = hoy.plusMonths(1);

        // Incluye vencidas y próximas (hasta dentro de 1 mes)
        return sustanciaRepository.findAll().stream()
                .filter(s -> s.getFechaVencimiento() != null &&
                        !s.getFechaVencimiento().isAfter(limite))
                .sorted((a, b) -> a.getFechaVencimiento().compareTo(b.getFechaVencimiento()))
                .toList();
    }

    @Override
    public List<Sustancia> listarAgotadas() {
        return sustanciaRepository.findAgotadas();
    }

    @Override
    public List<Sustancia> buscarPorLaboratorio(Integer idLaboratorio) {
        return sustanciaRepository.findByLaboratorio_IdLaboratorio(idLaboratorio);
    }
}
