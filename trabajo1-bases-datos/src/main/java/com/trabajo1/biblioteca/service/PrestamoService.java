package com.trabajo1.biblioteca.service;

import com.trabajo1.biblioteca.model.*;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;

public class PrestamoService {

    private List<Usuario> usuarios;
    private List<Material> materiales;
    private List<Ejemplar> ejemplares;
    private List<Prestamo> prestamos;
    private List<Multa> multas;

    public PrestamoService(
            List<Usuario> usuarios,
            List<Material> materiales,
            List<Ejemplar> ejemplares,
            List<Prestamo> prestamos,
            List<Multa> multas) {

        this.usuarios = usuarios;
        this.materiales = materiales;
        this.ejemplares = ejemplares;
        this.prestamos = prestamos;
        this.multas = multas;
    }

    public Prestamo registrarPrestamo(
            String usuarioId,
            String ejemplarId,
            LocalDate fechaPrestamo) {

        // 1. Verificar que el usuario exista
        Usuario usuario = buscarUsuario(usuarioId);

        if (usuario == null) {
            throw new IllegalArgumentException(
                    "El usuario no existe.");
        }

        // 2. Verificar el limite de prestamos activos
        if (contarPrestamosActivos(usuarioId) >= 3) {
            throw new IllegalStateException(
                    "El usuario ya tiene 3 prestamos activos.");
        }

        // 3. Verificar si tiene multas pendientes
        if (tieneMultasPendientes(usuarioId)) {
            throw new IllegalStateException(
                    "El usuario tiene multas pendientes.");
        }

        // 4. Verificar que el ejemplar exista
        Ejemplar ejemplar = buscarEjemplar(ejemplarId);

        if (ejemplar == null) {
            throw new IllegalArgumentException(
                    "El ejemplar no existe.");
        }

        // 5. Verificar disponibilidad
        if (!estaDisponible(ejemplarId)) {
            throw new IllegalStateException(
                    "El ejemplar no esta disponible.");
        }

        // 6. Buscar el material asociado
        Material material = buscarMaterial(ejemplar.getMaterialId());

        if (material == null) {
            throw new IllegalStateException(
                    "No se encontro el material del ejemplar.");
        }

        // 7. Crear y registrar el prestamo
        Prestamo nuevoPrestamo = new Prestamo(
                UUID.randomUUID().toString(),
                usuarioId,
                ejemplarId,
                fechaPrestamo,
                material.getDiasPrestamo()
        );

        prestamos.add(nuevoPrestamo);

        return nuevoPrestamo;
    }

    public Multa registrarDevolucion(
            String prestamoId,
            LocalDate fechaDevolucion) {

        // 1. Validar la fecha
        if (fechaDevolucion == null) {
            throw new IllegalArgumentException(
                    "La fecha de devolucion es obligatoria.");
        }

        // 2. Buscar el prestamo
        Prestamo prestamo = buscarPrestamo(prestamoId);

        if (prestamo == null) {
            throw new IllegalArgumentException(
                    "El prestamo no existe.");
        }

        // 3. Verificar que el prestamo siga activo
        if (!prestamo.estaActivo()) {
            throw new IllegalStateException(
                    "Este prestamo ya fue devuelto.");
        }

        // 4. Buscar el ejemplar y su material
        Ejemplar ejemplar = buscarEjemplar(prestamo.getEjemplarId());

        if (ejemplar == null) {
            throw new IllegalStateException(
                    "No se encontro el ejemplar.");
        }

        Material material = buscarMaterial(ejemplar.getMaterialId());

        if (material == null) {
            throw new IllegalStateException(
                    "No se encontro el material.");
        }

        // 5. Calcular los dias de retraso
        long diasRetraso = ChronoUnit.DAYS.between(
                prestamo.getFechaVencimiento(),
                fechaDevolucion
        );

        // 6. Registrar la devolucion
        prestamo.registrarDevolucion(fechaDevolucion);

        // 7. Si no existe retraso, no generar multa
        if (diasRetraso <= 0) {
            return null;
        }

        // 8. Calcular el monto de la multa
        BigDecimal monto = material.getMultaPorDia()
                .multiply(BigDecimal.valueOf(diasRetraso));

        // 9. Crear y registrar la multa
        Multa nuevaMulta = new Multa(
                UUID.randomUUID().toString(),
                prestamoId,
                monto,
                fechaDevolucion
        );

        multas.add(nuevaMulta);

        return nuevaMulta;
    }

    private Usuario buscarUsuario(String usuarioId) {
        for (Usuario usuario : usuarios) {
            if (usuario.getId().equals(usuarioId)) {
                return usuario;
            }
        }

        return null;
    }

    private Ejemplar buscarEjemplar(String ejemplarId) {
        for (Ejemplar ejemplar : ejemplares) {
            if (ejemplar.getId().equals(ejemplarId)) {
                return ejemplar;
            }
        }

        return null;
    }

    private Material buscarMaterial(String materialId) {
        for (Material material : materiales) {
            if (material.getId().equals(materialId)) {
                return material;
            }
        }

        return null;
    }

    private Prestamo buscarPrestamo(String prestamoId) {
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getId().equals(prestamoId)) {
                return prestamo;
            }
        }

        return null;
    }

    private int contarPrestamosActivos(String usuarioId) {
        int cantidad = 0;

        for (Prestamo prestamo : prestamos) {
            if (prestamo.getUsuarioId().equals(usuarioId)
                    && prestamo.estaActivo()) {
                cantidad++;
            }
        }

        return cantidad;
    }

    private boolean estaDisponible(String ejemplarId) {
        for (Prestamo prestamo : prestamos) {
            if (prestamo.getEjemplarId().equals(ejemplarId)
                    && prestamo.estaActivo()) {
                return false;
            }
        }

        return true;
    }

    private boolean tieneMultasPendientes(String usuarioId) {
        for (Multa multa : multas) {

            if (!multa.isPagada()) {

                for (Prestamo prestamo : prestamos) {

                    if (prestamo.getId().equals(multa.getPrestamoId())
                            && prestamo.getUsuarioId().equals(usuarioId)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }
}