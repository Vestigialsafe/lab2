package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MovimientoRepository extends JpaRepository<Movimiento, Integer> {
}
