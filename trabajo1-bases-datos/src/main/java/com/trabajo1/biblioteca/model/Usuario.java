package com.trabajo1.biblioteca.model;

public class Usuario {

    private String id;
    private String nombre;
    private String identificacion;

    public Usuario(String id, String nombre, String identificacion) {
        this.id = id;
        this.nombre = nombre;
        this.identificacion = identificacion;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getIdentificacion() {
        return identificacion;
    }
}