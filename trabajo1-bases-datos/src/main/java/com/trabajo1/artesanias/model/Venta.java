
package com.trabajo1.artesanias.model;

import java.time.LocalDate;
import java.math.BigDecimal;

public class Venta {

    private String id;
    private LocalDate fecha;
    private BigDecimal total;

    public Venta(String id, LocalDate fecha, BigDecimal total) {
        this.id = id;
        this.fecha = fecha;
        this.total = total;
    }

    public String getId() {
        return id;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }
}
