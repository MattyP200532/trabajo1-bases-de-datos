
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Bebida;
import com.trabajo1.cafeteria.repository.BebidaRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class BebidaService {

    private final BebidaRepository bebidaRepository;

    private static final BigDecimal IVA =
            new BigDecimal("0.15");

    public BebidaService() {
        this.bebidaRepository = new BebidaRepository();
    }

    public BebidaService(BebidaRepository bebidaRepository) {
        this.bebidaRepository = bebidaRepository;
    }

    // Registrar una bebida
    public void registrarBebida(Bebida bebida) {

        if (bebida == null) {
            throw new IllegalArgumentException(
                    "La bebida no puede ser nula");
        }

        if (bebida.getIdBebida() <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo");
        }

        if (bebida.getNombre() == null ||
                bebida.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre de la bebida es obligatorio");
        }

        if (bebida.getStockActual() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo");
        }

        if (bebida.getPrecioFijo() == null ||
                bebida.getPrecioFijo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero");
        }

        bebidaRepository.guardar(bebida);
    }

    // Obtener todas las bebidas
    public List<Bebida> obtenerTodas() {
        return bebidaRepository.obtenerTodos();
    }

    // Buscar bebida por identificador
    public Optional<Bebida> buscarPorId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo");
        }

        return bebidaRepository.buscarPorId(id);
    }

    // Actualizar una bebida
    public void actualizarBebida(Bebida bebida) {

        if (bebida == null) {
            throw new IllegalArgumentException(
                    "La bebida no puede ser nula");
        }

        if (bebida.getNombre() == null ||
                bebida.getNombre().trim().isEmpty()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio");
        }

        if (bebida.getStockActual() < 0) {
            throw new IllegalArgumentException(
                    "El stock no puede ser negativo");
        }

        if (bebida.getPrecioFijo() == null ||
                bebida.getPrecioFijo().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El precio debe ser mayor que cero");
        }

        bebidaRepository.actualizar(bebida);
    }

    // Calcular precio con IVA del 15 %
    public BigDecimal calcularPrecioConIVA(int idBebida) {

        Bebida bebida = bebidaRepository.buscarPorId(idBebida)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bebida no encontrada"));

        return bebida.getPrecioFijo()
                .multiply(BigDecimal.ONE.add(IVA))
                .setScale(2, RoundingMode.HALF_UP);
    }

    // Aumentar stock por abastecimiento
    public void aumentarStock(int idBebida, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        Bebida bebida = bebidaRepository.buscarPorId(idBebida)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bebida no encontrada"));

        if (!bebida.isActiva()) {
            throw new IllegalArgumentException(
                    "No se puede abastecer una bebida inactiva");
        }

        bebida.setStockActual(
                Math.addExact(bebida.getStockActual(), cantidad));

        bebidaRepository.actualizar(bebida);
    }

    // Descontar stock
    public void descontarStock(int idBebida, int cantidad) {

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        Bebida bebida = bebidaRepository.buscarPorId(idBebida)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bebida no encontrada"));

        if (!bebida.isActiva()) {
            throw new IllegalArgumentException(
                    "La bebida está inactiva");
        }

        if (bebida.getStockActual() < cantidad) {
            throw new IllegalArgumentException(
                    "Stock insuficiente de la bebida: "
                            + bebida.getNombre());
        }

        bebida.setStockActual(
                bebida.getStockActual() - cantidad);

        bebidaRepository.actualizar(bebida);
    }

    // Desactivar bebida sin eliminar su historial
    public void desactivarBebida(int idBebida) {

        Bebida bebida = bebidaRepository.buscarPorId(idBebida)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Bebida no encontrada"));

        bebida.setActiva(false);
        bebidaRepository.actualizar(bebida);
    }
}