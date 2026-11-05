
package com.trabajo1.artesanias.model;

import java.time.LocalDate;

public class Devolucion {

    private String id;
    private String recepcionId;
    private String artesanoId;
    private int cantidad;
    private LocalDate fechaDevolucion;

    public Devolucion(String id, String recepcionId, String artesanoId,
                      int cantidad, LocalDate fechaDevolucion) {
        this.id = id;
        this.recepcionId = recepcionId;
        this.artesanoId = artesanoId;
        this.cantidad = cantidad;
        this.fechaDevolucion = fechaDevolucion;
    }

    public String getId() {
        return id;
    }

    public String getRecepcionId() {
        return recepcionId;
    }

    public String getArtesanoId() {
        return artesanoId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }
}
