
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class MovimientoInventario {

    private int idMovimiento;
    private int idIngrediente;
    private TipoMovimiento tipoMovimiento;
    private BigDecimal cantidad;
    private LocalDateTime fecha;
    private String descripcion;

    public MovimientoInventario() {
    }

    public MovimientoInventario(int idMovimiento,
                                int idIngrediente,
                                TipoMovimiento tipoMovimiento,
                                BigDecimal cantidad,
                                LocalDateTime fecha,
                                String descripcion) {
        this.idMovimiento = idMovimiento;
        this.idIngrediente = idIngrediente;
        this.tipoMovimiento = tipoMovimiento;
        this.cantidad = cantidad;
        this.fecha = fecha;
        this.descripcion = descripcion;
    }

    public int getIdMovimiento() {
        return idMovimiento;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public TipoMovimiento getTipoMovimiento() {
        return tipoMovimiento;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}