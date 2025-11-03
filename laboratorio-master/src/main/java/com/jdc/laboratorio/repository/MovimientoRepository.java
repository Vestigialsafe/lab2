package com.jdc.laboratorio.repository;

import com.jdc.laboratorio.model.Movimiento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MovimientoRepository extends JpaRepository<Movimiento, Long> {

    List<Movimiento> findBySustanciaIdSustancia(Long idSustancia);

    // ✅ Nuevo: eliminar todos los movimientos de una sustancia
    void deleteAllBySustancia(com.jdc.laboratorio.model.Sustancia sustancia);
}
