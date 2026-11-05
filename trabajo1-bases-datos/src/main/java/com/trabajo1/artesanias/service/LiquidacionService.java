package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Artesania;
import com.trabajo1.artesanias.model.DetalleVenta;
import com.trabajo1.artesanias.model.TipoArtesania;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class LiquidacionService {

    public BigDecimal calcularTotalVendido(
            String artesanoId,
            LocalDate desde,
            LocalDate hasta,
            List<DetalleVenta> detalles,
            List<Artesania> artesanias,
            VentaService ventaService) {

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta detalle : detalles) {
            var venta = ventaService.buscarPorId(detalle.getVentaId());

            if (venta == null
                    || venta.getFecha().isBefore(desde)
                    || venta.getFecha().isAfter(hasta)) {
                continue;
            }

            for (Artesania artesania : artesanias) {
                if (artesania.getId().equals(detalle.getArtesaniaId())
                        && artesania.getArtesanoId().equals(artesanoId)) {

                    BigDecimal importe = detalle.getPrecioUnitario()
                            .multiply(BigDecimal.valueOf(detalle.getCantidad()));

                    total = total.add(importe);
                }
            }
        }

        return total;
    }

    public BigDecimal calcularComision(
            String artesanoId,
            LocalDate desde,
            LocalDate hasta,
            List<DetalleVenta> detalles,
            List<Artesania> artesanias,
            VentaService ventaService) {

        BigDecimal comision = BigDecimal.ZERO;

        for (DetalleVenta detalle : detalles) {
            var venta = ventaService.buscarPorId(detalle.getVentaId());

            if (venta == null
                    || venta.getFecha().isBefore(desde)
                    || venta.getFecha().isAfter(hasta)) {
                continue;
            }

            for (Artesania artesania : artesanias) {
                if (!artesania.getId().equals(detalle.getArtesaniaId())
                        || !artesania.getArtesanoId().equals(artesanoId)) {
                    continue;
                }

                BigDecimal porcentaje;

                if (artesania.getTipo() == TipoArtesania.TEXTIL) {
                    porcentaje = new BigDecimal("0.20");
                } else {
                    porcentaje = new BigDecimal("0.25");
                }

                BigDecimal importe = detalle.getPrecioUnitario()
                        .multiply(BigDecimal.valueOf(detalle.getCantidad()));

                comision = comision.add(importe.multiply(porcentaje));
            }
        }

        return comision;
    }

    public BigDecimal calcularPagoArtesano(
            String artesanoId,
            LocalDate desde,
            LocalDate hasta,
            List<DetalleVenta> detalles,
            List<Artesania> artesanias,
            VentaService ventaService) {

        BigDecimal total = calcularTotalVendido(
                artesanoId, desde, hasta, detalles, artesanias, ventaService
        );

        BigDecimal comision = calcularComision(
                artesanoId, desde, hasta, detalles, artesanias, ventaService
        );

        return total.subtract(comision);
    }
}