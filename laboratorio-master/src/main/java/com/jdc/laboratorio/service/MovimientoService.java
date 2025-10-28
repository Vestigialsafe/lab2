package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Movimiento;
import java.util.List;
import java.util.Optional;

public interface MovimientoService {

    Movimiento registrarMovimiento(Movimiento movimiento);

    List<Movimiento> listarMovimientos();

    List<Movimiento> listarPorSustancia(Long idSustancia);

    Optional<Movimiento> obtenerPorId(Long id);

    Movimiento actualizarMovimiento(Movimiento movimiento);
}
