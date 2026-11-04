package com.trabajo1.transporte.model;

public class Viaje {

    private int id;
    private String ruta;
    private String fechaSalida;
    private int capacidad;
    private double tarifaBase;
    private String tipo;

    public Viaje() {
    }

    public Viaje(int id, String ruta, String fechaSalida, int capacidad, double tarifaBase, String tipo) {
        this.id = id;
        this.ruta = ruta;
        this.fechaSalida = fechaSalida;
        this.capacidad = capacidad;
        this.tarifaBase = tarifaBase;
        this.tipo = tipo;
    }

    public int getId() {
        return id;
    }

    public String getRuta() {
        return ruta;
    }

    public String getFechaSalida() {
        return fechaSalida;
    }

    public int getCapacidad() {
        return capacidad;
    }

    public double getTarifaBase() {
        return tarifaBase;
    }

    public String getTipo() {
        return tipo;
    }
}