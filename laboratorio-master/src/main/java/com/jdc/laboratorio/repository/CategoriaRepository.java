package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
}
