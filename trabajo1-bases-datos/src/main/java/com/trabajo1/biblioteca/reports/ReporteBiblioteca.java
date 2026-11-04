package com.trabajo1.biblioteca.reports;

import com.trabajo1.biblioteca.model.Prestamo;
import com.trabajo1.biblioteca.model.Ejemplar;
import com.trabajo1.biblioteca.model.Material;
import com.trabajo1.biblioteca.model.Multa;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Set;

public class ReporteBiblioteca {

    private final List<Prestamo> prestamos;
    private final List<Multa> multas;

    public ReporteBiblioteca(
            List<Prestamo> prestamos,
            List<Multa> multas) {

        this.prestamos = prestamos;
        this.multas = multas;
    }

    public List<Prestamo> obtenerPrestamosVencidos(LocalDate fechaConsulta) {

        if (fechaConsulta == null) {
            throw new IllegalArgumentException(
                    "La fecha de consulta no puede ser nula."
            );
        }

        List<Prestamo> vencidos = new ArrayList<>();

        for (Prestamo prestamo : prestamos) {

            if (prestamo.estaActivo()
                    && prestamo.getFechaVencimiento().isBefore(fechaConsulta)) {

                vencidos.add(prestamo);
            }
        }

        return vencidos;
    }

    public List<Prestamo> obtenerHistorialPrestamos(String usuarioId) {

        if (usuarioId == null || usuarioId.isBlank()) {
            throw new IllegalArgumentException(
                    "El ID del usuario no puede estar vacío."
            );
        }

        List<Prestamo> historial = new ArrayList<>();

        for (Prestamo prestamo : prestamos) {

            if (prestamo.getUsuarioId().equals(usuarioId)) {
                historial.add(prestamo);
            }
        }

        return historial;
    }

    public Map<String, Integer> obtenerMaterialesMasPrestados(
            List<Ejemplar> ejemplares,
            int limite) {

        if (ejemplares == null) {
            throw new IllegalArgumentException(
                    "La lista de ejemplares no puede ser nula."
            );
        }

        if (limite <= 0) {
            throw new IllegalArgumentException(
                    "El límite debe ser mayor que cero."
            );
        }

        Map<String, Integer> cantidadPrestamos = new HashMap<>();
        Map<String, Ejemplar> ejemplaresPorId = new HashMap<>();

        for (Ejemplar ejemplar : ejemplares) {
            ejemplaresPorId.put(ejemplar.getId(), ejemplar);
        }

        for (Prestamo prestamo : prestamos) {

            Ejemplar ejemplar = ejemplaresPorId.get(
                    prestamo.getEjemplarId()
            );

            if (ejemplar != null) {
                String materialId = ejemplar.getMaterialId();

                cantidadPrestamos.put(
                        materialId,
                        cantidadPrestamos.getOrDefault(materialId, 0) + 1
                );
            }
        }

        Map<String, Integer> resultado = new java.util.LinkedHashMap<>();

        cantidadPrestamos.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limite)
                .forEach(entry -> resultado.put(
                        entry.getKey(),
                        entry.getValue()
                ));

        return resultado;
    }

    public List<Multa> obtenerMultasPorPeriodo(
            LocalDate fechaInicio,
            LocalDate fechaFin) {

        if (fechaInicio == null || fechaFin == null) {
            throw new IllegalArgumentException(
                    "Las fechas no pueden ser nulas."
            );
        }

        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior a la fecha final."
            );
        }

        List<Multa> resultado = new ArrayList<>();

        for (Multa multa : multas) {

            LocalDate fecha = multa.getFechaGeneracion();

            if (!fecha.isBefore(fechaInicio)
                    && !fecha.isAfter(fechaFin)) {

                resultado.add(multa);
            }
        }

        return resultado;
    }

    public Map<String, Map<String, Integer>> auditarEjemplaresDisponibles(
            List<Ejemplar> ejemplares) {

        if (ejemplares == null) {
            throw new IllegalArgumentException(
                    "La lista de ejemplares no puede ser nula."
            );
        }

        // Identificar los ejemplares que tienen préstamos activos.
        Set<String> ejemplaresPrestados = new HashSet<>();

        for (Prestamo prestamo : prestamos) {
            if (prestamo.estaActivo()) {
                ejemplaresPrestados.add(prestamo.getEjemplarId());
            }
        }

        // Agrupar las cantidades por material.
        Map<String, Map<String, Integer>> resultado =
                new LinkedHashMap<>();

        for (Ejemplar ejemplar : ejemplares) {

            String materialId = ejemplar.getMaterialId();

            Map<String, Integer> cantidades =
                    resultado.computeIfAbsent(
                            materialId,
                            id -> {
                                Map<String, Integer> datos =
                                        new LinkedHashMap<>();

                                datos.put("total", 0);
                                datos.put("prestados", 0);
                                datos.put("disponibles", 0);

                                return datos;
                            }
                    );

            cantidades.put(
                    "total",
                    cantidades.get("total") + 1
            );

            if (ejemplaresPrestados.contains(ejemplar.getId())) {

                cantidades.put(
                        "prestados",
                        cantidades.get("prestados") + 1
                );

            } else {

                cantidades.put(
                        "disponibles",
                        cantidades.get("disponibles") + 1
                );
            }
        }

        return resultado;
    }
}
