package com.jdc.laboratorio.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "subcategoria")
public class SubCategoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idSubCategoria")
    private Integer idSubCategoria;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    // Imagen guardada como bytea en PostgreSQL
    @Column(name = "imagen", columnDefinition = "bytea")
    private byte[] imagen;

    // Relación con categoría
    @ManyToOne
    @JoinColumn(name = "idCategoria", nullable = false)
    private Categoria categoria;

    // Relación N:M con Sustancia
    @ManyToMany(mappedBy = "subcategorias")
    private List<Sustancia> sustancias = new ArrayList<>();

    public SubCategoria() {}

    public SubCategoria(Integer idSubCategoria, String nombre, byte[] imagen, Categoria categoria, List<Sustancia> sustancias) {
        this.idSubCategoria = idSubCategoria;
        this.nombre = nombre;
        this.imagen = imagen;
        this.categoria = categoria;
        this.sustancias = sustancias;
    }

    // Getters & Setters
    public Integer getIdSubCategoria() { return idSubCategoria; }
    public void setIdSubCategoria(Integer idSubCategoria) { this.idSubCategoria = idSubCategoria; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public byte[] getImagen() { return imagen; }
    public void setImagen(byte[] imagen) { this.imagen = imagen; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public List<Sustancia> getSustancias() { return sustancias; }
    public void setSustancias(List<Sustancia> sustancias) { this.sustancias = sustancias; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SubCategoria that)) return false;
        return Objects.equals(idSubCategoria, that.idSubCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idSubCategoria);
    }

    @Override
    public String toString() {
        return "SubCategoria{" +
                "idSubCategoria=" + idSubCategoria +
                ", nombre='" + nombre + '\'' +
                ", categoria=" + (categoria != null ? categoria.getIdCategoria() : null) +
                ", sustancias=" + sustancias.size() +
                '}';
    }
}
