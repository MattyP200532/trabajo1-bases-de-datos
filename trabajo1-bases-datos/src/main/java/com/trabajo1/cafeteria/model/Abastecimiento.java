
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Abastecimiento {

    private int idAbastecimiento;
    private int idIngrediente;
    private BigDecimal cantidad;
    private BigDecimal costoUnitario;
    private LocalDateTime fecha;

    public Abastecimiento() {
    }

    public Abastecimiento(int idAbastecimiento,
                          int idIngrediente,
                          BigDecimal cantidad,
                          BigDecimal costoUnitario,
                          LocalDateTime fecha) {
        this.idAbastecimiento = idAbastecimiento;
        this.idIngrediente = idIngrediente;
        this.cantidad = cantidad;
        this.costoUnitario = costoUnitario;
        this.fecha = fecha;
    }

    public int getIdAbastecimiento() {
        return idAbastecimiento;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public BigDecimal getCantidad() {
        return cantidad;
    }

    public BigDecimal getCostoUnitario() {
        return costoUnitario;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setCantidad(BigDecimal cantidad) {
        this.cantidad = cantidad;
    }

    public void setCostoUnitario(BigDecimal costoUnitario) {
        this.costoUnitario = costoUnitario;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }
    
@Override
public String toString() {
    return "Abastecimiento{" +
            "idAbastecimiento=" + idAbastecimiento +
            ", idIngrediente=" + idIngrediente +
            ", cantidad=" + cantidad +
            ", costoUnitario=" + costoUnitario +
            ", fecha=" + fecha +
            '}';
}
}