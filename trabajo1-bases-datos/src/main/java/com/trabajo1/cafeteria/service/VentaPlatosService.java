
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.*;
import com.trabajo1.cafeteria.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

public class VentaPlatosService {

    private final VentaPlatosRepository ventaRepository;
    private final PlatoRepository platoRepository;
    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;

    public VentaPlatosService() {
        this.ventaRepository = new VentaPlatosRepository();
        this.platoRepository = new PlatoRepository();
        this.recetaRepository = new RecetaRepository();
        this.ingredienteRepository = new IngredienteRepository();
    }

    public VentaPlatos registrarVenta(
            int idVenta,
            Map<Integer, Integer> platosVendidos) {

        if (idVenta <= 0) {
            throw new IllegalArgumentException(
                "El identificador de venta debe ser positivo");
        }

        if (platosVendidos == null || platosVendidos.isEmpty()) {
            throw new IllegalArgumentException(
                "Debe incluir al menos un plato");
        }

        if (ventaRepository.buscarPorId(idVenta).isPresent()) {
            throw new IllegalArgumentException(
                "Ya existe una venta con ese identificador");
        }

        List<Ingrediente> ingredientes =
                ingredienteRepository.obtenerTodos();

        Map<Integer, BigDecimal> consumoTotal = new HashMap<>();
        VentaPlatos venta = new VentaPlatos(
                idVenta, LocalDateTime.now());

        int idDetalle = 1;

        // 1. Validar platos y calcular ingredientes necesarios.
        for (Map.Entry<Integer, Integer> entrada :
                platosVendidos.entrySet()) {

            int idPlato = entrada.getKey();
            int cantidad = entrada.getValue();

            if (cantidad <= 0) {
                throw new IllegalArgumentException(
                    "La cantidad vendida debe ser mayor que cero");
            }

            Plato plato = platoRepository.buscarPorId(idPlato)
                    .orElseThrow(() ->
                        new IllegalArgumentException(
                            "Plato no encontrado: " + idPlato));

            if (!plato.isActivo()) {
                throw new IllegalArgumentException(
                    "El plato no está activo: " + plato.getNombre());
            }

            Receta receta = recetaRepository.buscarPorId(
                    plato.getIdReceta())
                    .orElseThrow(() ->
                        new IllegalArgumentException(
                            "Receta no encontrada para el plato: "
                            + plato.getNombre()));

            if (receta.getDetalles() == null ||
                    receta.getDetalles().isEmpty()) {
                throw new IllegalArgumentException(
                    "El plato no tiene ingredientes en su receta");
            }

            BigDecimal costoPlato = BigDecimal.ZERO;

            for (DetalleReceta detalle : receta.getDetalles()) {

                BigDecimal necesario = detalle
                        .getCantidadNecesaria()
                        .multiply(BigDecimal.valueOf(cantidad));

                Ingrediente ingrediente = ingredientes.stream()
                        .filter(i -> i.getIdIngrediente()
                                == detalle.getIdIngrediente())
                        .findFirst()
                        .orElseThrow(() ->
                            new IllegalArgumentException(
                                "Ingrediente no encontrado: "
                                + detalle.getIdIngrediente()));

                if (!ingrediente.isActivo()) {
                    throw new IllegalArgumentException(
                        "Ingrediente inactivo: "
                        + ingrediente.getNombre());
                }

                consumoTotal.merge(
                        ingrediente.getIdIngrediente(),
                        necesario,
                        BigDecimal::add);

                costoPlato = costoPlato.add(
                        ingrediente.getCostoUnitario()
                                .multiply(detalle.getCantidadNecesaria()));
            }

            // El precio de venta es el doble del costo.
            BigDecimal precioUnitario =
                    costoPlato.multiply(BigDecimal.valueOf(2));

            DetalleVentaPlatos detalleVenta =
                    new DetalleVentaPlatos(
                            idDetalle++,
                            idPlato,
                            cantidad,
                            precioUnitario);

            venta.agregarDetalle(detalleVenta);
        }

        // 2. Comprobar TODO el stock antes de modificarlo.
        for (Map.Entry<Integer, BigDecimal> consumo :
                consumoTotal.entrySet()) {

            Ingrediente ingrediente = ingredientes.stream()
                    .filter(i -> i.getIdIngrediente()
                            == consumo.getKey())
                    .findFirst()
                    .orElseThrow();

            if (ingrediente.getStockActual().compareTo(
                    consumo.getValue()) < 0) {

                throw new IllegalArgumentException(
                    "Stock insuficiente del ingrediente: "
                    + ingrediente.getNombre());
            }
        }

        // 3. Descontar los ingredientes.
        for (Map.Entry<Integer, BigDecimal> consumo :
                consumoTotal.entrySet()) {

            Ingrediente ingrediente = ingredientes.stream()
                    .filter(i -> i.getIdIngrediente()
                            == consumo.getKey())
                    .findFirst()
                    .orElseThrow();

            ingrediente.setStockActual(
                    ingrediente.getStockActual()
                            .subtract(consumo.getValue()));

            ingredienteRepository.actualizar(ingrediente);
        }

        // 4. Guardar la venta.
        ventaRepository.guardar(venta);

        return venta;
    }

    public List<VentaPlatos> obtenerTodasLasVentas() {
        return ventaRepository.obtenerTodos();
    }

    public Optional<VentaPlatos> buscarVentaPorId(int id) {
        return ventaRepository.buscarPorId(id);
    }
}