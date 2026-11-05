package com.trabajo1.artesanias.model;

public class Artesano {

    private String id;
    private String nombre;
    private String comunidadId;

    public Artesano(String id, String nombre, String comunidadId) {
        this.id = id;
        this.nombre = nombre;
        this.comunidadId = comunidadId;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getComunidadId() {
        return comunidadId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setComunidadId(String comunidadId) {
        this.comunidadId = comunidadId;
    }
}
