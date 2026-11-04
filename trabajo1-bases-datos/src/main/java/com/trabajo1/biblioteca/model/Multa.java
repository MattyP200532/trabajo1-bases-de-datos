package com.trabajo1.biblioteca.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Multa {

    private String id;
    private String prestamoId;
    private BigDecimal monto;
    private LocalDate fechaGeneracion;
    private boolean pagada;

    public Multa(
            String id,
            String prestamoId,
            BigDecimal monto,
            LocalDate fechaGeneracion) {

        this.id = id;
        this.prestamoId = prestamoId;
        this.monto = monto;
        this.fechaGeneracion = fechaGeneracion;
        this.pagada = false;
    }

    public String getId() {
        return id;
    }

    public String getPrestamoId() {
        return prestamoId;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public LocalDate getFechaGeneracion() {
        return fechaGeneracion;
    }

    public boolean isPagada() {
        return pagada;
    }

    public void registrarPago() {
        if (pagada) {
            throw new IllegalStateException(
                    "Esta multa ya fue pagada.");
        }

        this.pagada = true;
    }
}