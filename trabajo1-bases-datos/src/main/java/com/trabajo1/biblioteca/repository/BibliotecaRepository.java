
package com.trabajo1.biblioteca.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.biblioteca.model.*;

import java.lang.reflect.Type;
import java.util.List;

public class BibliotecaRepository {

    private final JsonRepository<Usuario> usuarios;
    private final JsonRepository<Libro> libros;
    private final JsonRepository<Revista> revistas;
    private final JsonRepository<Ejemplar> ejemplares;
    private final JsonRepository<Prestamo> prestamos;
    private final JsonRepository<Multa> multas;

    public BibliotecaRepository() {
        this("../data/biblioteca");
    }

    public BibliotecaRepository(String directorioBase) {

        usuarios = new JsonRepository<>(
                directorioBase + "/usuarios.json",
                new TypeToken<List<Usuario>>() {}.getType()
        );

        libros = new JsonRepository<>(
                directorioBase + "/libros.json",
                new TypeToken<List<Libro>>() {}.getType()
        );

        revistas = new JsonRepository<>(
                directorioBase + "/revistas.json",
                new TypeToken<List<Revista>>() {}.getType()
        );

        ejemplares = new JsonRepository<>(
                directorioBase + "/ejemplares.json",
                new TypeToken<List<Ejemplar>>() {}.getType()
        );

        prestamos = new JsonRepository<>(
                directorioBase + "/prestamos.json",
                new TypeToken<List<Prestamo>>() {}.getType()
        );

        multas = new JsonRepository<>(
                directorioBase + "/multas.json",
                new TypeToken<List<Multa>>() {}.getType()
        );
    }

    public List<Usuario> cargarUsuarios() {
        return usuarios.cargar();
    }

    public void guardarUsuarios(List<Usuario> datos) {
        usuarios.guardar(datos);
    }

    public List<Libro> cargarLibros() {
        return libros.cargar();
    }

    public void guardarLibros(List<Libro> datos) {
        libros.guardar(datos);
    }

    public List<Revista> cargarRevistas() {
        return revistas.cargar();
    }

    public void guardarRevistas(List<Revista> datos) {
        revistas.guardar(datos);
    }

    public List<Ejemplar> cargarEjemplares() {
        return ejemplares.cargar();
    }

    public void guardarEjemplares(List<Ejemplar> datos) {
        ejemplares.guardar(datos);
    }

    public List<Prestamo> cargarPrestamos() {
        return prestamos.cargar();
    }

    public void guardarPrestamos(List<Prestamo> datos) {
        prestamos.guardar(datos);
    }

    public List<Multa> cargarMultas() {
        return multas.cargar();
    }

    public void guardarMultas(List<Multa> datos) {
        multas.guardar(datos);
    }
}
