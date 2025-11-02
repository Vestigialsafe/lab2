package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class SustanciaServiceImpl implements SustanciasService {

    private final SustanciaRepository sustanciaRepository;

    public SustanciaServiceImpl(SustanciaRepository sustanciaRepository) {
        this.sustanciaRepository = sustanciaRepository;
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

    @Override
    public void eliminar(Long id) {
        sustanciaRepository.deleteById(id);
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
        return sustanciaRepository.findProximasAVencer(hoy, limite);
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
