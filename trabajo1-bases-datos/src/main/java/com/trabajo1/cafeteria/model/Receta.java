
package com.trabajo1.cafeteria.model;

import java.util.ArrayList;
import java.util.List;

public class Receta {

    private int idReceta;
    private int idPlato;
    private List<DetalleReceta> detalles;

    public Receta() {
        this.detalles = new ArrayList<>();
    }

    public Receta(int idReceta, int idPlato) {
        this.idReceta = idReceta;
        this.idPlato = idPlato;
        this.detalles = new ArrayList<>();
    }

    public int getIdReceta() {
        return idReceta;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public List<DetalleReceta> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<DetalleReceta> detalles) {
        this.detalles = detalles;
    }

    public void agregarDetalle(DetalleReceta detalle) {
        detalles.add(detalle);
    }

    public void eliminarDetalle(int idDetalleReceta) {
        detalles.removeIf(
            detalle -> detalle.getIdDetalleReceta() == idDetalleReceta
        );
    }
}