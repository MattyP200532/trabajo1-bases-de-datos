
package com.trabajo1.cafeteria.model;

public class Plato {

    private int idPlato;
    private String nombre;
    private boolean activo;

    public Plato() {
    }

    public Plato(int idPlato, String nombre) {
        this.idPlato = idPlato;
        this.nombre = nombre;
        this.activo = true;
    }

    public int getIdPlato() {
        return idPlato;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isActivo() {
        return activo;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }

    @Override
    public String toString() {
        return "Plato{" +
                "idPlato=" + idPlato +
                ", nombre='" + nombre + '\'' +
                ", activo=" + activo +
                '}';
    }
}
