
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;

public class DetalleVentaBebidas {

    private int idDetalleVenta;
    private int idBebida;
    private int cantidad;
    private BigDecimal precioUnitario;
    private BigDecimal subtotal;

    public DetalleVentaBebidas() {
    }

    public DetalleVentaBebidas(int idDetalleVenta,
                               int idBebida,
                               int cantidad,
                               BigDecimal precioUnitario) {
        this.idDetalleVenta = idDetalleVenta;
        this.idBebida = idBebida;
        this.cantidad = cantidad;
        this.precioUnitario = precioUnitario;

        this.subtotal = precioUnitario.multiply(
            BigDecimal.valueOf(cantidad)
        );
    }

    public int getIdDetalleVenta() {
        return idDetalleVenta;
    }

    public int getIdBebida() {
        return idBebida;
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