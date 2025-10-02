package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.springframework.stereotype.Service;

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
}
