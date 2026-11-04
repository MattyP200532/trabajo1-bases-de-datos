package com.trabajo1.biblioteca.model;
import java.math.BigDecimal;

public abstract class Material {

    private String id;
    private String titulo;
    private String autor;

    public Material(String id, String titulo, String autor) {
        this.id = id;
        this.titulo = titulo;
        this.autor = autor;
    }

    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getAutor() {
        return autor;
    }

    public abstract int getDiasPrestamo();

    public abstract BigDecimal getMultaPorDia();
}