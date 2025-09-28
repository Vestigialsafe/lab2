package com.jdc.laboratorio.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "laboratorio")
public class Laboratorio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idLaboratorio;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String sede;

    @ManyToOne
    @JoinColumn(name = "encargado", nullable = false)
    private Usuario encargado;

    public Laboratorio() {
    }

    public Laboratorio(Integer idLaboratorio, String nombre, String sede, Usuario encargado) {
        this.idLaboratorio = idLaboratorio;
        this.nombre = nombre;
        this.sede = sede;
        this.encargado = encargado;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Integer idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }

    public Usuario getEncargado() {
        return encargado;
    }

    public void setEncargado(Usuario encargado) {
        this.encargado = encargado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laboratorio that = (Laboratorio) o;
        return Objects.equals(idLaboratorio, that.idLaboratorio);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idLaboratorio);
    }

    @Override
    public String toString() {
        return "Laboratorio{" +
                "idLaboratorio=" + idLaboratorio +
                ", nombre='" + nombre + '\'' +
                ", sede='" + sede + '\'' +
                ", encargado=" + encargado +
                '}';
    }
}
