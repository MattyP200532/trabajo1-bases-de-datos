package com.trabajo1.artesanias.model;

import java.math.BigDecimal;

public class Artesania {

    private String id;
    private String nombre;
    private TipoArtesania tipo;
    private BigDecimal precioVenta;
    private String artesanoId;

    public Artesania(String id, String nombre, TipoArtesania tipo,
                     BigDecimal precioVenta, String artesanoId) {
        this.id = id;
        this.nombre = nombre;
        this.tipo = tipo;
        this.precioVenta = precioVenta;
        this.artesanoId = artesanoId;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoArtesania getTipo() {
        return tipo;
    }

    public BigDecimal getPrecioVenta() {
        return precioVenta;
    }

    public String getArtesanoId() {
        return artesanoId;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setTipo(TipoArtesania tipo) {
        this.tipo = tipo;
    }

    public void setPrecioVenta(BigDecimal precioVenta) {
        this.precioVenta = precioVenta;
    }
}