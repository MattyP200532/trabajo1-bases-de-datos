
package com.trabajo1.artesanias.model;

import java.math.BigDecimal;

public class DetalleVenta {

    private String id;
    private String ventaId;
    private String recepcionId;
    private String artesaniaId;
    private int cantidad;
    private BigDecimal precioUnitario;

    public DetalleVenta(String id, String ventaId, String recepcionId,
                        String artesaniaId, int cantidad,
                        BigDecimal precioUnitario) {
        this.id = id;
        this.ventaId = ventaId;
        this.recepcionId = recepcionId;
        this.artesaniaId = artesaniaId;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
    }

    public String getId() {
        return id;
    }

    public String getVentaId() {
        return ventaId;
    }

    public String getRecepcionId() {
        return recepcionId;
    }

    public String getArtesaniaId() {
        return artesaniaId;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }
}
