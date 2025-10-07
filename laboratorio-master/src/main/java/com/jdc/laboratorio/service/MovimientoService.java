package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Movimiento;
import java.util.List;

public interface MovimientoService {

    Movimiento registrarMovimiento(Movimiento movimiento);

    List<Movimiento> listarMovimientos();

    List<Movimiento> listarPorSustancia(Long idSustancia);
}
