package com.jdc.laboratorio.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;
@Entity
@Table(name = "notificacion")
public class Notificacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idNotificacion")
    private Integer idNotificacion;

    @Column(name = "mensaje", nullable = false, length = 500)
    private String mensaje;

    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    // Relación con usuario
    @ManyToOne
    @JoinColumn(name = "idUsuario", nullable = false)
    private Usuario usuario;

    // Relación opcional con sustancia (ejemplo: alerta de stock bajo)
    @ManyToOne
    @JoinColumn(name = "idSustancia")
    private Sustancia sustancia;

    public Notificacion() {}

    public Notificacion(Integer idNotificacion, String mensaje, LocalDate fecha, Usuario usuario, Sustancia sustancia) {
        this.idNotificacion = idNotificacion;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.usuario = usuario;
        this.sustancia = sustancia;
    }

    // Getters & Setters
    public Integer getIdNotificacion() {
        return idNotificacion;
    }

    public void setIdNotificacion(Integer idNotificacion) {
        this.idNotificacion = idNotificacion;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
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
        if (!(o instanceof Notificacion that)) return false;
        return Objects.equals(idNotificacion, that.idNotificacion);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idNotificacion);
    }

    @Override
    public String toString() {
        return "Notificacion{" +
                "idNotificacion=" + idNotificacion +
                ", mensaje='" + mensaje + '\'' +
                ", fecha=" + fecha +
                '}';
    }
}
