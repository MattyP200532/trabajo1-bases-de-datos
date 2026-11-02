package com.trabajo1.transporte.model;

public class Boleto {

    private int id;
    private int idViaje;
    private Pasajero pasajero;
    private int asiento;
    private double precio;
    private String fechaVenta;
    private String fechaAnulacion;
    private String estado;

    public Boleto() {
    }

    public Boleto(int id, int idViaje, Pasajero pasajero, int asiento,
                  double precio, String fechaVenta) {
        this.id = id;
        this.idViaje = idViaje;
        this.pasajero = pasajero;
        this.asiento = asiento;
        this.precio = precio;
        this.fechaVenta = fechaVenta;
        this.fechaAnulacion = null;
        this.estado = "ACTIVO";
    }

    public void anular(String fechaAnulacion) {
        this.estado = "ANULADO";
        this.fechaAnulacion = fechaAnulacion;
    }

    public int getId() {
        return id;
    }

    public int getIdViaje() {
        return idViaje;
    }

    public Pasajero getPasajero() {
        return pasajero;
    }

    public int getAsiento() {
        return asiento;
    }

    public double getPrecio() {
        return precio;
    }

    public String getFechaVenta() {
        return fechaVenta;
    }

    public String getFechaAnulacion() {
        return fechaAnulacion;
    }

    public String getEstado() {
        return estado;
    }
}