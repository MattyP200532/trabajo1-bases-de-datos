package com.trabajo1.biblioteca.model;

import java.time.LocalDate;

public class Prestamo {

    private String id;
    private String usuarioId;
    private String ejemplarId;
    private LocalDate fechaPrestamo;
    private LocalDate fechaVencimiento;
    private LocalDate fechaDevolucion;

    public Prestamo(
            String id,
            String usuarioId,
            String ejemplarId,
            LocalDate fechaPrestamo,
            int diasPrestamo) {

        this.id = id;
        this.usuarioId = usuarioId;
        this.ejemplarId = ejemplarId;
        this.fechaPrestamo = fechaPrestamo;
        this.fechaVencimiento = fechaPrestamo.plusDays(diasPrestamo);
        this.fechaDevolucion = null;
    }

    public String getId() {
        return id;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public String getEjemplarId() {
        return ejemplarId;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public LocalDate getFechaDevolucion() {
        return fechaDevolucion;
    }

    public boolean estaActivo() {
        return fechaDevolucion == null;
    }

    public void registrarDevolucion(LocalDate fechaDevolucion) {

        if (!estaActivo()) {
            throw new IllegalStateException(
                    "Este préstamo ya fue devuelto.");
        }

        if (fechaDevolucion.isBefore(fechaPrestamo)) {
            throw new IllegalArgumentException(
                    "La devolución no puede ser anterior al préstamo.");
        }

        this.fechaDevolucion = fechaDevolucion;
    }
}