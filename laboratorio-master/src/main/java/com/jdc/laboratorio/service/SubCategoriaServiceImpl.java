package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.SubCategoria;
import com.jdc.laboratorio.repository.SubCategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubCategoriaServiceImpl implements SubCategoriaService {

    @Autowired
    private SubCategoriaRepository repository;

    @Override
    public SubCategoria guardar(SubCategoria subCategoria) {
        return repository.save(subCategoria);
    }

    @Override
    public List<SubCategoria> listarTodas() {
        return repository.findAll();
    }

    @Override
    public Optional<SubCategoria> buscarPorId(Integer id) {
        return repository.findById(id);
    }

    @Override
    public void eliminar(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public List<SubCategoria> listarPorCategoria(Integer idCategoria) {
        return repository.findByCategoriaIdCategoria(idCategoria);
    }
}
