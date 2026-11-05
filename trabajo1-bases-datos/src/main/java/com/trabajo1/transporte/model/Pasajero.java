package com.trabajo1.transporte.model;

public class Pasajero {

    private String cedula;
    private String nombre;
    private String tipo;

    public Pasajero() {
    }

    public Pasajero(String cedula, String nombre, String tipo) {
        this.cedula = cedula;
        this.nombre = nombre;
        this.tipo = tipo;
    }

    public String getCedula() {
        return cedula;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipo() {
        return tipo;
    }
}