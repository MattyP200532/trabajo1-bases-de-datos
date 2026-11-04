package com.trabajo1.biblioteca.model;
import java.math.BigDecimal;

public class Libro extends Material {

    public Libro(String id, String titulo, String autor) {
        super(id, titulo, autor);
    }

    @Override
    public int getDiasPrestamo() {
        return 7;
    }

    @Override
    public BigDecimal getMultaPorDia() {
        return new BigDecimal("0.25");
    }
}