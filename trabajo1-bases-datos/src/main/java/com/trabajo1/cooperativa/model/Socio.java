
package com.trabajo1.cooperativa.model;

public class Socio {

    private String id;
    private String nombre;
    private String cedula;

    public Socio(String id, String nombre, String cedula) {
        if (id == null || id.isBlank() ||
            nombre == null || nombre.isBlank() ||
            cedula == null || cedula.isBlank()) {
            throw new IllegalArgumentException(
                "Los datos del socio son obligatorios"
            );
        }

        this.id = id;
        this.nombre = nombre;
        this.cedula = cedula;
    }

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCedula() {
        return cedula;
    }

    @Override
    public String toString() {
        return "Socio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", cedula='" + cedula + '\'' +
                '}';
    }
}
