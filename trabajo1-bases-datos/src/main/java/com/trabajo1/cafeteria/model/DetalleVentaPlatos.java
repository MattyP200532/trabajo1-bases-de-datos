
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;

public class DetalleVentaPlatos {

    private int idDetalleVenta;
    private int idPlato;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleVentaPlatos() {
    }

    public DetalleVentaPlatos(int idDetalleVenta,
                              int idPlato,
                              int cantidad,
                              BigDecimal precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.idPlato = idPlato;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;
        this.subtotal = precioUnitario.multiply(
            BigDecimal.valueOf(cantidad)
        );
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public int getCantidad() {
        return cantidad;
    }

    public BigDecimal getPrecioUnitario() {
        return precioUnitario;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }
}