package com.jdc.laboratorio.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "categoria")
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idCategoria")
    private Integer idCategoria;

    @Column(name = "nombre", nullable = false, length = 255)
    private String nombre;

    // Relación con subcategorías
    @OneToMany(mappedBy = "categoria", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SubCategoria> subcategorias = new ArrayList<>();

    @Lob
    @Column(name = "imagen")
    private byte[] imagen;  // ahora almacena la imagen como bytes en PostgreSQL (BYTEA)

    public Categoria() {}

    public Categoria(Integer idCategoria, String nombre, byte[] imagen) {
        this.idCategoria = idCategoria;
        this.nombre = nombre;
        this.imagen = imagen;
    }

    // Getters y Setters
    public Integer getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(Integer idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<SubCategoria> getSubcategorias() {
        return subcategorias;
    }

    public void setSubcategorias(List<SubCategoria> subcategorias) {
        this.subcategorias = subcategorias;
    }

    public byte[] getImagen() {
        return imagen;
    }

    public void setImagen(byte[] imagen) {
        this.imagen = imagen;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Categoria categoria)) return false;
        return Objects.equals(idCategoria, categoria.idCategoria);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idCategoria);
    }
}
