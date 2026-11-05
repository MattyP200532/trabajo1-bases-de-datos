package com.trabajo1.artesanias.model;

public class Comunidad {

    private String id;
    private String nombre;

    public Comunidad(String id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}