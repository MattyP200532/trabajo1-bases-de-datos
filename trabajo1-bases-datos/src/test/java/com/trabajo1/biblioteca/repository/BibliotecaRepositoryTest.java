
package com.trabajo1.biblioteca.repository;

import com.trabajo1.biblioteca.model.*;
import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class BibliotecaRepositoryTest {

    @Test
    public void debeGuardarYCargarTodosLosDatos() {

        BibliotecaRepository repositorio =
                new BibliotecaRepository("target/test-data/biblioteca");

        // Usuarios
        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(new Usuario("U1", "Matias", "123456"));

        // Libros
        List<Libro> libros = new ArrayList<>();
        libros.add(new Libro("L1", "Java", "Autor"));

        // Revistas
        List<Revista> revistas = new ArrayList<>();
        revistas.add(new Revista("R1", "Tecnologia", "Editorial"));

        // Ejemplares
        List<Ejemplar> ejemplares = new ArrayList<>();
        ejemplares.add(new Ejemplar("E1", "L1"));

        // Prestamos
        List<Prestamo> prestamos = new ArrayList<>();
        prestamos.add(new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 3), 7
        ));

        // Multas
        List<Multa> multas = new ArrayList<>();
        multas.add(new Multa(
                "M1", "P1",
                new BigDecimal("0.75"),
                LocalDate.of(2026, 11, 13)
        ));

        // Guardar
        repositorio.guardarUsuarios(usuarios);
        repositorio.guardarLibros(libros);
        repositorio.guardarRevistas(revistas);
        repositorio.guardarEjemplares(ejemplares);
        repositorio.guardarPrestamos(prestamos);
        repositorio.guardarMultas(multas);

        // Recuperar y verificar
        assertEquals(1, repositorio.cargarUsuarios().size());
        assertEquals("Matias", repositorio.cargarUsuarios().get(0).getNombre());

        assertEquals(1, repositorio.cargarLibros().size());
        assertEquals("Java", repositorio.cargarLibros().get(0).getTitulo());

        assertEquals(1, repositorio.cargarRevistas().size());
        assertEquals("Tecnologia", repositorio.cargarRevistas().get(0).getTitulo());

        assertEquals(1, repositorio.cargarEjemplares().size());
        assertEquals("E1", repositorio.cargarEjemplares().get(0).getId());

        assertEquals(1, repositorio.cargarPrestamos().size());
        assertEquals("P1", repositorio.cargarPrestamos().get(0).getId());

        assertEquals(1, repositorio.cargarMultas().size());
        assertEquals(
                new BigDecimal("0.75"),
                repositorio.cargarMultas().get(0).getMonto()
        );
    }
}
