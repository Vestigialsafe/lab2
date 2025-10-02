package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;

import java.util.List;
import java.util.Optional;

public interface SustanciasService {
    List<Sustancia> listarTodas();
    Optional<Sustancia> buscarPorId(Long id);
    Sustancia guardar(Sustancia sustancia);
    void eliminar(Long id);

    // 👇 Nuevo
    List<Sustancia> buscarPorSubcategoria(Integer idSubCategoria);
}
