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

    @Column(name = "stock")
    private Integer stock;

    @Column(name = "unidad", length = 50)
    private String unidad;

    @Column(name = "envase_original")
    private Boolean envaseOriginal;

    @Column(name = "fecha_vencimiento")
    private LocalDate fechaVencimiento;

    @Column(name = "cas", length = 255)
    private String cas;

    @Column(name = "pureza", length = 255)
    private String pureza;

    @Column(name = "marca", length = 255)
    private String marca;

    // Documentación guardada en la BD como archivo binario (ej. PDF, DOC)
    @Lob
    @Column(name = "documentacion")
    private byte[] documentacion;  // ⚡ aquí se guarda como bytea en Postgres

    // Relación con laboratorio (Muchas sustancias -> Un laboratorio)
    @ManyToOne
    @JoinColumn(name = "idLaboratorio", nullable = false)
    private Laboratorio laboratorio;

    // Relación N:M con subcategorías
    @ManyToMany
    @JoinTable(
            name = "sustancia_subcategoria",
            joinColumns = @JoinColumn(name = "idSustancia"),
            inverseJoinColumns = @JoinColumn(name = "idSubCategoria")
    )
    private List<SubCategoria> subcategorias = new ArrayList<>();

    public Sustancia() {
    }

    public Sustancia(Long idSustancia, String nombre, Integer stock, String unidad, Boolean envaseOriginal,
                     LocalDate fechaVencimiento, String cas, String pureza, String marca,
                     byte[] documentacion, Laboratorio laboratorio) {
        this.idSustancia = idSustancia;
        this.nombre = nombre;
        this.stock = stock;
        this.unidad = unidad;
        this.envaseOriginal = envaseOriginal;
        this.fechaVencimiento = fechaVencimiento;
        this.cas = cas;
        this.pureza = pureza;
        this.marca = marca;
        this.documentacion = documentacion;
        this.laboratorio = laboratorio;
    }

    // Getters & Setters
    public Long getIdSustancia() { return idSustancia; }
    public void setIdSustancia(Long idSustancia) { this.idSustancia = idSustancia; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }

    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }

    public Boolean getEnvaseOriginal() { return envaseOriginal; }
    public void setEnvaseOriginal(Boolean envaseOriginal) { this.envaseOriginal = envaseOriginal; }

    public LocalDate getFechaVencimiento() { return fechaVencimiento; }
    public void setFechaVencimiento(LocalDate fechaVencimiento) { this.fechaVencimiento = fechaVencimiento; }

    public String getCas() { return cas; }
    public void setCas(String cas) { this.cas = cas; }

    public String getPureza() { return pureza; }
    public void setPureza(String pureza) { this.pureza = pureza; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public byte[] getDocumentacion() { return documentacion; }
    public void setDocumentacion(byte[] documentacion) { this.documentacion = documentacion; }

    public Laboratorio getLaboratorio() { return laboratorio; }
    public void setLaboratorio(Laboratorio laboratorio) { this.laboratorio = laboratorio; }

    public List<SubCategoria> getSubcategorias() { return subcategorias; }
    public void setSubcategorias(List<SubCategoria> subcategorias) { this.subcategorias = subcategorias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Sustancia sustancia)) return false;
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
                ", stock=" + stock +
                ", unidad='" + unidad + '\'' +
                ", envaseOriginal=" + envaseOriginal +
                ", fechaVencimiento=" + fechaVencimiento +
                ", cas='" + cas + '\'' +
                ", pureza='" + pureza + '\'' +
                ", marca='" + marca + '\'' +
                ", laboratorio=" + (laboratorio != null ? laboratorio.getIdLaboratorio() : null) +
                ", subcategorias=" + subcategorias.size() +
                '}';
    }
}