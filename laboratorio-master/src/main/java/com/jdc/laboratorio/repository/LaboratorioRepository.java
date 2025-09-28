package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Laboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaboratorioRepository extends JpaRepository<Laboratorio, Integer> {
}
