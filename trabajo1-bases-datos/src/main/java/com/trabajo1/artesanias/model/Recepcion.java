
package com.trabajo1.artesanias.model;

import java.time.LocalDate;

public class Recepcion {

    private String id;
    private String artesanoId;
    private String artesaniaId;
    private int cantidad;
    private LocalDate fechaRecepcion;

    public Recepcion(String id, String artesanoId, String artesaniaId,
                     int cantidad, LocalDate fechaRecepcion) {
        this.id = id;
        this.artesanoId = artesanoId;
        this.artesaniaId = artesaniaId;
        this.cantidad = cantidad;
        this.fechaRecepcion = fechaRecepcion;
    }

    public String getId() {
        return id;
    }

    public String getArtesanoId() {
        return artesanoId;
    }

    public String getArtesaniaId() {
        return artesaniaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDate getFechaRecepcion() {
        return fechaRecepcion;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }
}

