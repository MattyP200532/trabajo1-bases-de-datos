
package com.trabajo1.cafeteria.model;

import java.math.BigDecimal;

public class DetalleReceta {

    private int idDetalleReceta;
    private int idIngrediente;
    private BigDecimal cantidadNecesaria;

    public DetalleReceta() {
    }

    public DetalleReceta(int idDetalleReceta,
                         int idIngrediente,
                         BigDecimal cantidadNecesaria) {
        this.idDetalleReceta = idDetalleReceta;
        this.idIngrediente = idIngrediente;
        this.cantidadNecesaria = cantidadNecesaria;
    }

    public int getIdDetalleReceta() {
        return idDetalleReceta;
    }

    public int getIdIngrediente() {
        return idIngrediente;
    }

    public BigDecimal getCantidadNecesaria() {
        return cantidadNecesaria;
    }

    public void setCantidadNecesaria(BigDecimal cantidadNecesaria) {
        this.cantidadNecesaria = cantidadNecesaria;
    }
}