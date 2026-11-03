
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class VentaBebidas {

    private int idVenta;
    private LocalDateTime fecha;
    private List<DetalleVentaBebidas> detalles;
    private BigDecimal total;

    public VentaBebidas() {
        this.detalles = new ArrayList<>();
        this.total = BigDecimal.ZERO;
    }

    public VentaBebidas(int idVenta, LocalDateTime fecha) {
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

    public List<DetalleVentaBebidas> getDetalles() {
        return detalles;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void agregarDetalle(DetalleVentaBebidas detalle) {
        detalles.add(detalle);
        calcularTotal();
    }

    public void calcularTotal() {
        total = BigDecimal.ZERO;

        for (DetalleVentaBebidas detalle : detalles) {
            total = total.add(detalle.getSubtotal());
        }
    }
}