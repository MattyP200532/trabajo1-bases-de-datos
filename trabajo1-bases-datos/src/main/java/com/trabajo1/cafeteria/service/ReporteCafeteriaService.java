
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.*;
import com.trabajo1.cafeteria.repository.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteCafeteriaService {

    private final IngredienteRepository ingredienteRepository;
    private final PlatoRepository platoRepository;
    private final RecetaRepository recetaRepository;
    private final VentaPlatosRepository ventaRepository;

    public ReporteCafeteriaService() {
        this.ingredienteRepository = new IngredienteRepository();
        this.platoRepository = new PlatoRepository();
        this.recetaRepository = new RecetaRepository();
        this.ventaRepository = new VentaPlatosRepository();
    }

    // REPORTE 1:
    // Costo, precio y margen de cada plato.

    public Map<String, Map<String, BigDecimal>>
    reporteCostoPrecioMargen() {

        Map<String, Map<String, BigDecimal>> reporte =
                new LinkedHashMap<>();

        for (Plato plato : platoRepository.obtenerTodos()) {

            if (!plato.isActivo()) {
                continue;
            }

           Receta receta = recetaRepository.obtenerTodos()
        .stream()
        .filter(r -> r.getIdPlato() == plato.getIdPlato())
        .findFirst()
        .orElseThrow(() ->
                new IllegalArgumentException(
                        "Receta no encontrada para el plato: "
                                + plato.getNombre()));

            BigDecimal costo = BigDecimal.ZERO;

            for (DetalleReceta detalle : receta.getDetalles()) {

                Ingrediente ingrediente = ingredienteRepository
                        .buscarPorId(detalle.getIdIngrediente())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Ingrediente no encontrado"));

                costo = costo.add(
                        ingrediente.getCostoUnitario()
                                .multiply(
                                        detalle.getCantidadNecesaria()));
            }

            BigDecimal precio = costo.multiply(
                    BigDecimal.valueOf(2));

            BigDecimal margen = precio.subtract(costo);

            Map<String, BigDecimal> valores =
                    new LinkedHashMap<>();

            valores.put("costo", costo);
            valores.put("precio", precio);
            valores.put("margen", margen);

            reporte.put(plato.getNombre(), valores);
        }

        return reporte;
    }

    // REPORTE 2:
    // Ingredientes por debajo del stock mínimo.

    public List<Ingrediente> reporteIngredientesStockBajo() {

        return ingredienteRepository.obtenerTodos()
                .stream()
                .filter(Ingrediente::isActivo)
                .filter(i -> i.getStockActual()
                        .compareTo(i.getStockMinimo()) < 0)
                .collect(Collectors.toList());
    }

    // REPORTE 3:
    // Platos que se pueden preparar con el stock actual.

    public Map<String, Integer>
    reportePlatosPreparables() {

        Map<String, Integer> reporte =
                new LinkedHashMap<>();

        List<Ingrediente> ingredientes =
                ingredienteRepository.obtenerTodos();

        for (Plato plato : platoRepository.obtenerTodos()) {

            if (!plato.isActivo()) {
                continue;
            }

            Optional<Receta> recetaOpcional =
        recetaRepository.obtenerTodos()
                .stream()
                .filter(r -> r.getIdPlato() == plato.getIdPlato())
                .findFirst();

            if (recetaOpcional.isEmpty()) {
                continue;
            }

            Receta receta = recetaOpcional.get();

            if (receta.getDetalles() == null ||
                    receta.getDetalles().isEmpty()) {
                continue;
            }

            int unidadesPosibles = Integer.MAX_VALUE;

            boolean recetaValida = true;

            for (DetalleReceta detalle : receta.getDetalles()) {

                Optional<Ingrediente> ingredienteOpcional =
                        ingredienteRepository.buscarPorId(
                                detalle.getIdIngrediente());

                if (ingredienteOpcional.isEmpty()) {
                    recetaValida = false;
                    break;
                }

                Ingrediente ingrediente =
                        ingredienteOpcional.get();

                if (!ingrediente.isActivo() ||
                        detalle.getCantidadNecesaria() == null ||
                        detalle.getCantidadNecesaria()
                                .compareTo(BigDecimal.ZERO) <= 0) {
                    recetaValida = false;
                    break;
                }

                int disponibles = ingrediente.getStockActual()
                        .divide(
                                detalle.getCantidadNecesaria(),
                                0,
                                java.math.RoundingMode.DOWN)
                        .intValue();

                unidadesPosibles = Math.min(
                        unidadesPosibles, disponibles);
            }

            if (recetaValida) {
                reporte.put(
                        plato.getNombre(),
                        unidadesPosibles);
            }
        }

        return reporte;
    }

    // REPORTE 4:
    // Platos más vendidos en un período.

    public Map<String, Integer> reportePlatosMasVendidos(
            LocalDateTime fechaInicio,
            LocalDateTime fechaFin) {

        validarPeriodo(fechaInicio, fechaFin);

        Map<Integer, Integer> cantidades =
                new HashMap<>();

        List<VentaPlatos> ventas =
                ventaRepository.obtenerTodos();

        for (VentaPlatos venta : ventas) {

            if (venta.getFecha() == null ||
                    venta.getFecha().isBefore(fechaInicio) ||
                    venta.getFecha().isAfter(fechaFin)) {
                continue;
            }

            for (DetalleVentaPlatos detalle :
                    venta.getDetalles()) {

                cantidades.merge(
                        detalle.getIdPlato(),
                        detalle.getCantidad(),
                        Integer::sum);
            }
        }

        Map<String, Integer> reporte =
                new LinkedHashMap<>();

        cantidades.entrySet().stream()
                .sorted(Map.Entry.<Integer, Integer>comparingByValue()
                        .reversed())
                .forEach(entrada -> {

                    platoRepository.buscarPorId(entrada.getKey())
                            .ifPresent(plato ->
                                    reporte.put(
                                            plato.getNombre(),
                                            entrada.getValue()));
                });

        return reporte;
    }

    // REPORTE 5:
    // Auditoría del stock de ingredientes.

    public Map<String, BigDecimal> reporteAuditoriaStock() {

        Map<String, BigDecimal> reporte =
                new LinkedHashMap<>();

        for (Ingrediente ingrediente :
                ingredienteRepository.obtenerTodos()) {

            if (ingrediente.isActivo()) {
                reporte.put(
                        ingrediente.getNombre(),
                        ingrediente.getStockActual());
            }
        }

        return reporte;
    }

    private void validarPeriodo(
            LocalDateTime inicio,
            LocalDateTime fin) {

        if (inicio == null || fin == null) {
            throw new IllegalArgumentException(
                    "Las fechas son obligatorias");
        }

        if (inicio.isAfter(fin)) {
            throw new IllegalArgumentException(
                    "La fecha inicial no puede ser posterior "
                            + "a la fecha final");
        }
    }
}