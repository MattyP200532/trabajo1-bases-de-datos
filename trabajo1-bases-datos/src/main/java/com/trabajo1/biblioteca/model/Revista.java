package com.trabajo1.biblioteca.model;
import java.math.BigDecimal;

public class Revista extends Material {

    public Revista(String id, String titulo, String autor) {
        super(id, titulo, autor);
    }

    @Override
    public int getDiasPrestamo() {
        return 3;
    }

    @Override
    public BigDecimal getMultaPorDia() {
        return new BigDecimal("0.50");
    }
}