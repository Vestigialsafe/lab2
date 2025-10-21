package com.jdc.laboratorio.model;

import com.jdc.laboratorio.Enums.TipoMovimiento;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "Movimientos")
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovimiento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TipoMovimiento tipoMovimiento; // ENTRADA o SALIDA

    @DecimalMin(value = "0.0", inclusive = true)
    @Column(nullable = false)
    private double cantidad;

    @Column(nullable = false)
    private LocalDate fechaMovimiento;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idSustancia", nullable = false)
    private Sustancia sustancia;

    public Movimiento() {}

    public Movimiento(Long idMovimiento, TipoMovimiento tipoMovimiento, Double cantidad,
                      LocalDate fechaMovimiento, String descripcion, Sustancia sustancia) {
        this.idMovimiento = idMovimiento;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fechaMovimiento = fechaMovimiento;
        this.descripcion = descripcion;
        this.sustancia = sustancia;
    }

    public Long getIdMovimiento() {
        return idMovimiento;
    }

    public void setIdMovimiento(Long idMovimiento) {
        this.idMovimiento = idMovimiento;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setTipoMovimiento(TipoMovimiento tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public LocalDate getFechaMovimiento() {
        return fechaMovimiento;
    }

    public void setFechaMovimiento(LocalDate fechaMovimiento) {
        this.fechaMovimiento = fechaMovimiento;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Sustancia getSustancia() {
        return sustancia;
    }

    public void setSustancia(Sustancia sustancia) {
        this.sustancia = sustancia;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Movimiento that)) return false;
        return Objects.equals(idMovimiento, that.idMovimiento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idMovimiento);
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "idMovimiento=" + idMovimiento +
                ", tipoMovimiento=" + tipoMovimiento +
                ", cantidad=" + cantidad +
                ", fechaMovimiento=" + fechaMovimiento +
                ", descripcion='" + descripcion + '\'' +
                ", sustancia=" + (sustancia != null ? sustancia.getNombre() : "null") +
                '}';
    }
}
