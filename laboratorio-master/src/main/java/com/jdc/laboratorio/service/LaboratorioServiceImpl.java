package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Laboratorio;
import com.jdc.laboratorio.repository.LaboratorioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LaboratorioServiceImpl implements LaboratorioService {

    private final LaboratorioRepository laboratorioRepository;

    public LaboratorioServiceImpl(LaboratorioRepository laboratorioRepository) {
        this.laboratorioRepository = laboratorioRepository;
    }

    @Override
    public Laboratorio guardar(Laboratorio laboratorio) {
        return laboratorioRepository.save(laboratorio);
    }

    @Override
    public Optional<Laboratorio> buscarPorId(Integer id) {
        return laboratorioRepository.findById(id);
    }

    @Override
    public List<Laboratorio> listarTodos() {
        return laboratorioRepository.findAll();
    }

    @Override
    public void eliminar(Integer id) {
        laboratorioRepository.deleteById(id);
    }
}
