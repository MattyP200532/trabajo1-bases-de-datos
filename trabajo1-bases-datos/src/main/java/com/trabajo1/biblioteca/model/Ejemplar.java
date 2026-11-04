package com.trabajo1.biblioteca.model;

public class Ejemplar {

    private String id;
    private String materialId;

    public Ejemplar(String id, String materialId) {
        this.id = id;
        this.materialId = materialId;
    }

    public String getId() {
        return id;
    }

    public String getMaterialId() {
        return materialId;
    }
}