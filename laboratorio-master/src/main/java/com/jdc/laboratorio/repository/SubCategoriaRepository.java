package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.SubCategoria;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubCategoriaRepository extends JpaRepository<SubCategoria, Integer> {
    List<SubCategoria> findByCategoriaIdCategoria(Integer idCategoria);

}
