package com.jdc.laboratorio.service;

import com.jdc.laboratorio.model.Movimiento;
import com.jdc.laboratorio.model.Sustancia;
import com.jdc.laboratorio.model.TipoMovimiento;
import com.jdc.laboratorio.repository.MovimientoRepository;
import com.jdc.laboratorio.repository.SustanciaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final SustanciaRepository sustanciaRepository;

    public MovimientoServiceImpl(MovimientoRepository movimientoRepository,
                                 SustanciaRepository sustanciaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.sustanciaRepository = sustanciaRepository;
    }

    @Override
    @Transactional
    public Movimiento registrarMovimiento(Movimiento movimiento) {

        Sustancia sustancia = sustanciaRepository.findById(movimiento.getSustancia().getIdSustancia())
                .orElseThrow(() -> new IllegalArgumentException("Sustancia no encontrada"));

        if (movimiento.getCantidad() <= 0) {
            throw new IllegalArgumentException("La cantidad debe ser mayor que cero");
        }

        if (movimiento.getTipoMovimiento() == TipoMovimiento.ENTRADA) {
            sustancia.setStock(sustancia.getStock() + movimiento.getCantidad());
        } else if (movimiento.getTipoMovimiento() == TipoMovimiento.SALIDA) {
            if (sustancia.getStock() < movimiento.getCantidad()) {
                throw new IllegalArgumentException("No hay suficiente stock para realizar la salida");
            }
            sustancia.setStock(sustancia.getStock() - movimiento.getCantidad());
        }

        movimiento.setFechaMovimiento(LocalDate.now());
        sustanciaRepository.save(sustancia);

        return movimientoRepository.save(movimiento);
    }

    @Override
    public List<Movimiento> listarMovimientos() {
        return movimientoRepository.findAll();
    }

    @Override
    public List<Movimiento> listarPorSustancia(Long idSustancia) {
        return movimientoRepository.findBySustanciaIdSustancia(idSustancia);
    }
}
