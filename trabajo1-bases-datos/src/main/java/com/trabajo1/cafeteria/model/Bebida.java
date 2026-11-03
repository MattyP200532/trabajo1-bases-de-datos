
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;

public class Bebida {

    private int idBebida;
    private String nombre;
    private int stockActual;
    private BigDecimal precioFijo;
    private boolean activa;

    public Bebida() {
    }

    public Bebida(int idBebida, String nombre,
                  int stockActual, BigDecimal precioFijo) {

        this.idBebida = idBebida;
        this.nombre = nombre;
        this.stockActual = stockActual;
        this.precioFijo = precioFijo;
        this.activa = true;
    }

    public int getIdBebida() {
        return idBebida;
    }

    public String getNombre() {
        return nombre;
    }

    public int getStockActual() {
        return stockActual;
    }

    public BigDecimal getPrecioFijo() {
        return precioFijo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStockActual(int stockActual) {
        this.stockActual = stockActual;
    }

    public void setPrecioFijo(BigDecimal precioFijo) {
        this.precioFijo = precioFijo;
    }

    public void setActiva(boolean activa) {
        this.activa = activa;
    }

    @Override
    public String toString() {
        return "Bebida{" +
                "idBebida=" + idBebida +
                ", nombre='" + nombre + '\'' +
                ", stockActual=" + stockActual +
                ", precioFijo=" + precioFijo +
                ", activa=" + activa +
                '}';
    }
}