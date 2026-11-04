package com.trabajo1.biblioteca.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.biblioteca.model.Usuario;
import com.trabajo1.biblioteca.model.Prestamo;

import org.junit.Test;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

import static org.junit.Assert.*;

public class JsonRepositoryTest {

    @Test
    public void debeGuardarYCargarUsuarios() {

        Type tipoUsuarios =
                new TypeToken<List<Usuario>>() {}.getType();

        JsonRepository<Usuario> repositorio =
                new JsonRepository<>(
                        "target/test-data/usuarios.json",
                        tipoUsuarios
                );

        List<Usuario> usuarios = new ArrayList<>();

        usuarios.add(new Usuario(
                "U1",
                "Matias",
                "123456"
        ));

        // Guardar usuarios
        repositorio.guardar(usuarios);

        // Recuperar usuarios
        List<Usuario> recuperados = repositorio.cargar();

        assertEquals(1, recuperados.size());
        assertEquals("Matias", recuperados.get(0).getNombre());
        assertEquals("123456", recuperados.get(0).getIdentificacion());
    }

    @Test
    public void debeGuardarYCargarFechasDePrestamo() {
        Type tipoPrestamos = new TypeToken<List<Prestamo>>() {}.getType();

        JsonRepository<Prestamo> repositorio = new JsonRepository<>(
                "target/test-data/prestamos.json", tipoPrestamos);

        List<Prestamo> prestamos = new ArrayList<>();

        LocalDate fechaPrestamo = LocalDate.of(2026, 11, 3);

        Prestamo prestamo = new Prestamo(
                "P1", "U1", "E1", fechaPrestamo, 7
        );

        prestamos.add(prestamo);

        repositorio.guardar(prestamos);

        List<Prestamo> recuperados = repositorio.cargar();

        assertEquals(1, recuperados.size());
        assertEquals(fechaPrestamo, recuperados.get(0).getFechaPrestamo());
        assertEquals(
                LocalDate.of(2026, 11, 10),
                recuperados.get(0).getFechaVencimiento()
        );
        assertNull(recuperados.get(0).getFechaDevolucion());
    }
}