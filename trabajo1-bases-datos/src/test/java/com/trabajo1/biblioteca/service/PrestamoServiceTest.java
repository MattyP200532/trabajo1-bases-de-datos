package com.trabajo1.biblioteca.service;

import com.trabajo1.biblioteca.model.*;

import org.junit.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class PrestamoServiceTest {

    @Test
    public void debeRegistrarPrestamoCorrectamente() {

        Usuario usuario = new Usuario(
                "U1", "Matias", "123456"
        );

        Libro libro = new Libro(
                "M1", "Clean Code", "Robert C. Martin"
        );

        Ejemplar ejemplar = new Ejemplar("E1", "M1");

        List<Usuario> usuarios = new ArrayList<>();
        List<Material> materiales = new ArrayList<>();
        List<Ejemplar> ejemplares = new ArrayList<>();
        List<Prestamo> prestamos = new ArrayList<>();
        List<Multa> multas = new ArrayList<>();

        usuarios.add(usuario);
        materiales.add(libro);
        ejemplares.add(ejemplar);

        PrestamoService service = new PrestamoService(
                usuarios, materiales, ejemplares, prestamos, multas
        );

        Prestamo prestamo = service.registrarPrestamo(
                "U1", "E1", LocalDate.of(2026, 11, 3)
        );

        assertNotNull(prestamo);
        assertEquals("U1", prestamo.getUsuarioId());
        assertEquals("E1", prestamo.getEjemplarId());

        assertEquals(
                LocalDate.of(2026, 11, 10),
                prestamo.getFechaVencimiento()
        );
    }

    @Test
    public void debeGenerarMultaPorDevolucionTardia() {

        Usuario usuario = new Usuario(
                "U1", "Matias", "123456"
        );

        Libro libro = new Libro(
                "M1", "Clean Code", "Robert C. Martin"
        );

        Ejemplar ejemplar = new Ejemplar("E1", "M1");

        List<Usuario> usuarios = new ArrayList<>();
        List<Material> materiales = new ArrayList<>();
        List<Ejemplar> ejemplares = new ArrayList<>();
        List<Prestamo> prestamos = new ArrayList<>();
        List<Multa> multas = new ArrayList<>();

        usuarios.add(usuario);
        materiales.add(libro);
        ejemplares.add(ejemplar);

        PrestamoService service = new PrestamoService(
                usuarios, materiales, ejemplares, prestamos, multas
        );

        Prestamo prestamo = service.registrarPrestamo(
                "U1", "E1", LocalDate.of(2026, 11, 3)
        );

        Multa multa = service.registrarDevolucion(
                prestamo.getId(),
                LocalDate.of(2026, 11, 13)
        );

        assertNotNull(multa);

        assertEquals(
                0,
                new BigDecimal("0.75").compareTo(multa.getMonto())
        );

        assertFalse(multa.isPagada());
    }
}