package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Sustancia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SustanciaRepository extends JpaRepository<Sustancia, Long> {
    // 👇 Nuevo método
    List<Sustancia> findBySubcategorias_IdSubCategoria(Integer idSubCategoria);
}
