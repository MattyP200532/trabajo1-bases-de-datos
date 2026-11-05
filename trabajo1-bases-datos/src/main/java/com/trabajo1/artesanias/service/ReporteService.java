package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

public class ReporteService {

    private final LiquidacionService liquidacionService =
            new LiquidacionService();

    // 1. Reporte de liquidación de un artesano.
    public Map<String, BigDecimal> reporteLiquidacion(
            String artesanoId,
            LocalDate desde,
            LocalDate hasta,
            List<DetalleVenta> detalles,
            List<Artesania> artesanias,
            VentaService ventaService) {

        BigDecimal vendido = liquidacionService.calcularTotalVendido(
                artesanoId, desde, hasta,
                detalles, artesanias, ventaService
        );

        BigDecimal comision = liquidacionService.calcularComision(
                artesanoId, desde, hasta,
                detalles, artesanias, ventaService
        );

        BigDecimal pago = vendido.subtract(comision);

        Map<String, BigDecimal> resultado = new LinkedHashMap<>();
        resultado.put("totalVendido", vendido);
        resultado.put("comision", comision);
        resultado.put("totalPagar", pago);

        return resultado;
    }

    // 2. Artesanías que llevan más de 90 días en tienda.
    public List<Recepcion> artesaniasParaDevolver(
            List<Recepcion> recepciones,
            LocalDate fechaActual) {

        return recepciones.stream()
                .filter(r -> ChronoUnit.DAYS.between(
                        r.getFechaRecepcion(), fechaActual) > 90)
                .collect(Collectors.toList());
    }

    // 3. Comunidades con más ventas.
    public Map<String, Integer> comunidadesConMasVentas(
            List<DetalleVenta> detalles,
            List<Artesania> artesanias,
            List<Artesano> artesanos) {

        Map<String, Integer> ventasPorComunidad = new HashMap<>();

        for (DetalleVenta detalle : detalles) {
            for (Artesania artesania : artesanias) {
                if (artesania.getId().equals(detalle.getArtesaniaId())) {
                    for (Artesano artesano : artesanos) {
                        if (artesano.getId().equals(artesania.getArtesanoId())) {
                            String comunidadId = artesano.getComunidadId();

                            ventasPorComunidad.put(
                                    comunidadId,
                                    ventasPorComunidad.getOrDefault(comunidadId, 0)
                                            + detalle.getCantidad()
                            );
                        }
                    }
                }
            }
        }

        return ventasPorComunidad.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // 4. Artesanías más vendidas.
    public Map<String, Integer> artesaniasMasVendidas(
            List<DetalleVenta> detalles) {

        Map<String, Integer> ventas = new HashMap<>();

        for (DetalleVenta detalle : detalles) {
            ventas.put(
                    detalle.getArtesaniaId(),
                    ventas.getOrDefault(detalle.getArtesaniaId(), 0)
                            + detalle.getCantidad()
            );
        }

        return ventas.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue()
                        .reversed())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (a, b) -> a,
                        LinkedHashMap::new
                ));
    }

    // 5. Auditoría de unidades disponibles.
    public Map<String, Integer> auditoriaInventario(
            List<Recepcion> recepciones,
            RecepcionService recepcionService) {

        Map<String, Integer> inventario = new LinkedHashMap<>();

        for (Recepcion recepcion : recepciones) {
            inventario.put(
                    recepcion.getId(),
                    recepcionService.unidadesDisponibles(recepcion.getId())
            );
        }

        return inventario;
    }
}