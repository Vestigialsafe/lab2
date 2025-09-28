package com.jdc.laboratorio.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "sustancia")
public class Sustancia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSustancia")
    private Long idSustancia;

    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @Column(name = "presentacion")
    private Integer presentacion;

    @Column(name = "estado", length = 255)
    private String estado;

    @Column(name = "riesgo", length = 255)
    private String riesgo;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "Cas", length = 255)
    private String cas;

    @Column(name = "Pureza", length = 255)
    private String pureza;

    @Column(name = "Marca", length = 255)
    private String marca;

    @Column(name = "Embase")
    private Boolean embace;

    @Column(name = "Documentacion", length = 500)
    private String documentacion;

    @ManyToOne
    @JoinColumn(name = "idLaboratorio", nullable = false)
    private Laboratorio laboratorio;

    // 🔗 Relación N:M con SubCategoria
    @ManyToMany
    @JoinTable(
            name = "sustancia_subcategoria",
            joinColumns = @JoinColumn(name = "idSustancia"),
            inverseJoinColumns = @JoinColumn(name = "idSubCategoria")
    )
    private List<SubCategoria> subcategorias = new ArrayList<>();

    public Sustancia() {
    }

    public Sustancia(Long idSustancia, String nombre, Integer presentacion, String estado, String riesgo,
                     LocalDate fechaVencimiento, Integer stock, String cas, String pureza, String marca,
                     Boolean embace, String documentacion, Laboratorio laboratorio) {
        this.idSustancia = idSustancia;
        this.nombre = nombre;
        this.presentacion = presentacion;
        this.estado = estado;
        this.riesgo = riesgo;
        this.fechaVencimiento = fechaVencimiento;
        this.stock = stock;
        this.cas = cas;
        this.pureza = pureza;
        this.marca = marca;
        this.embace = embace;
        this.documentacion = documentacion;
        this.laboratorio = laboratorio;
    }

    // Getters & Setters
    public Long getIdSustancia() { return idSustancia; }
    public void setIdSustancia(Long idSustancia) { this.idSustancia = idSustancia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getPresentacion() { return presentacion; }
    public void setPresentacion(Integer presentacion) { this.presentacion = presentacion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getRiesgo() { return riesgo; }
    public void setRiesgo(String riesgo) { this.riesgo = riesgo; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getCas() { return cas; }
    public void setCas(String cas) { this.cas = cas; }

    public String getPureza() { return pureza; }
    public void setPureza(String pureza) { this.pureza = pureza; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public Boolean getEmbace() { return embace; }
    public void setEmbace(Boolean embace) { this.embace = embace; }

    public String getDocumentacion() { return documentacion; }
    public void setDocumentacion(String documentacion) { this.documentacion = documentacion; }

    public Laboratorio getLaboratorio() { return laboratorio; }
    public void setLaboratorio(Laboratorio laboratorio) { this.laboratorio = laboratorio; }

    public List<SubCategoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<SubCategoria> subcategorias) { this.subcategorias = subcategorias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sustancia sustancia = (Sustancia) o;
        return Objects.equals(idSustancia, sustancia.idSustancia);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(idSustancia);
    }

    @Override
    public String toString() {
        return "Sustancia{" +
                "idSustancia=" + idSustancia +
                ", nombre='" + nombre + '\'' +
                ", presentacion=" + presentacion +
                ", estado='" + estado + '\'' +
                ", riesgo='" + riesgo + '\'' +
                ", fechaVencimiento=" + fechaVencimiento +
                ", stock=" + stock +
                ", cas='" + cas + '\'' +
                ", pureza='" + pureza + '\'' +
                ", marca='" + marca + '\'' +
                ", embace=" + embace +
                ", documentacion='" + documentacion + '\'' +
                ", laboratorio=" + laboratorio +
                '}';
    }
}
