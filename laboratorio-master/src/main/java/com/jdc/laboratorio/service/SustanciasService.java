package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Sustancia;
import java.util.List;
import java.util.Optional;

public interface SustanciasService {

    List<Sustancia> listarTodas();
    Optional<Sustancia> buscarPorId(Long id);
    Sustancia guardar(Sustancia sustancia);

    void eliminar(Long id);              // Validado
    void eliminarDirecto(Long id);       // Forzado

    List<Sustancia> buscarPorSubcategoria(Integer idSubCategoria);
    List<Sustancia> buscarPorSubcategoriaYLaboratorio(Integer idSubCategoria, Integer idLaboratorio);
    long contarSustancias();
    long contarProximasAVencer();
    long contarAgotadas();
    List<Object[]> contarPorCategoria();

    List<Sustancia> listarProximasAVencer();
    List<Sustancia> listarAgotadas();

    List<Sustancia> buscarPorLaboratorio(Integer idLaboratorio);
}
