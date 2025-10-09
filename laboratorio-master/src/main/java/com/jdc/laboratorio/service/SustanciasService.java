package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;

import java.util.List;
import java.util.Optional;

public interface SustanciasService {

    List<Sustancia> listarTodas();
    Optional<Sustancia> buscarPorId(Long id);
    Sustancia guardar(Sustancia sustancia);
    void eliminar(Long id);

    // 🔹 Filtros y estadísticas
    List<Sustancia> buscarPorSubcategoria(Integer idSubCategoria);
    long contarSustancias();
    long contarProximasAVencer();
    long contarAgotadas();
    List<Object[]> contarPorCategoria();

    // 🔹 Listados específicos
    List<Sustancia> listarProximasAVencer();
    List<Sustancia> listarAgotadas();
}
