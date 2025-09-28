package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Laboratorio;

import java.util.List;
import java.util.Optional;

public interface LaboratorioService {
    Laboratorio guardar(Laboratorio laboratorio);
    Optional<Laboratorio> buscarPorId(Integer id);
    List<Laboratorio> listarTodos();
    void eliminar(Integer id);
}
