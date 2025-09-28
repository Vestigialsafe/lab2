package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Categoria;

import java.util.List;
import java.util.Optional;

public interface CategoriaService {
    Categoria guardarCategoria(Categoria categoria);
    List<Categoria> listarCategorias();
    List<Categoria> listarTodas();
    Optional<Categoria> buscarPorId(Integer id);
}
