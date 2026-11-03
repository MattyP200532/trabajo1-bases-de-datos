
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaPlatos {

    private int idVenta;
    private LocalDateTime fecha;
    private List<DetalleVentaPlatos> detalles;
    private BigDecimal total;

    public VentaPlatos() {
        this.detalles = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    public VentaPlatos(int idVenta, LocalDateTime fecha) {
        this.idVenta = idVenta;
        this.fecha = fecha;
        this.detalles = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    public int getIdVenta() {
        return idVenta;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public List<DetalleVentaPlatos> getDetalles() {
        return detalles;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void agregarDetalle(DetalleVentaPlatos detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    public void calcularTotal() {
        total = BigDecimal.ZERO;

        for (DetalleVentaPlatos detalle : detalles) {
            total = total.add(detalle.getSubtotal());
        }
    }
}