
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;

public class Ingrediente {

    private int idIngrediente;
    private String nombre;
    private UnidadMedida unidadMedida;
    private BigDecimal stockActual;
    private BigDecimal stockMinimo;
    private BigDecimal costoUnitario;
    private boolean activo;

    public Ingrediente() {
    }

    public Ingrediente(int idIngrediente, String nombre,
                       UnidadMedida unidadMedida,
                       BigDecimal stockActual,
                       BigDecimal stockMinimo,
                       BigDecimal costoUnitario) {

        this.idIngrediente = idIngrediente;
        this.nombre = nombre;
        this.unidadMedida = unidadMedida;
        this.stockActual = stockActual;
        this.stockMinimo = stockMinimo;
        this.costoUnitario = costoUnitario;
        this.activo = true;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public String getNombre() {
        return nombre;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public BigDecimal getStockActual() {
        return stockActual;
    }

    public BigDecimal getStockMinimo() {
        return stockMinimo;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setStockActual(BigDecimal stockActual) {
        this.stockActual = stockActual;
    }

    public void setStockMinimo(BigDecimal stockMinimo) {
        this.stockMinimo = stockMinimo;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "idIngrediente=" + idIngrediente +
                ", nombre='" + nombre + '\'' +
                ", unidadMedida=" + unidadMedida +
                ", stockActual=" + stockActual +
                ", stockMinimo=" + stockMinimo +
                ", costoUnitario=" + costoUnitario +
                ", activo=" + activo +
                '}';
    }
}