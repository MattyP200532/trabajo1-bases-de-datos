
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Bebida;
import com.trabajo1.cafeteria.model.DetalleVentaBebidas;
import com.trabajo1.cafeteria.model.VentaBebidas;
import com.trabajo1.cafeteria.repository.BebidaRepository;
import com.trabajo1.cafeteria.repository.VentaBebidasRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.*;

public class VentaBebidasService {

    private static final BigDecimal IVA =
            new BigDecimal("0.15");

    private final BebidaRepository bebidaRepository;
    private final VentaBebidasRepository ventaRepository;

    public VentaBebidasService() {
        this.bebidaRepository = new BebidaRepository();
        this.ventaRepository = new VentaBebidasRepository();
    }

    public VentaBebidasService(
            BebidaRepository bebidaRepository,
            VentaBebidasRepository ventaRepository) {

        this.bebidaRepository = bebidaRepository;
        this.ventaRepository = ventaRepository;
    }

    public VentaBebidas registrarVenta(
            int idVenta,
            List<DetalleVentaBebidas> detalles) {

        if (idVenta <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser positivo");
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "Debe incluir al menos una bebida");
        }

        if (ventaRepository.buscarPorId(idVenta).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una venta con ese identificador");
        }

        // Acumular cantidades por bebida.
        Map<Integer, Integer> cantidades = new LinkedHashMap<>();

        for (DetalleVentaBebidas detalle : detalles) {

            if (detalle == null || detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero");
            }

            cantidades.merge(
                    detalle.getIdBebida(),
                    detalle.getCantidad(),
                    Integer::sum);
        }

        // Obtener y validar todas las bebidas.
        Map<Integer, Bebida> bebidas =
                new LinkedHashMap<>();

        for (Map.Entry<Integer, Integer> entrada :
                cantidades.entrySet()) {

            Bebida bebida = bebidaRepository
                    .buscarPorId(entrada.getKey())
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "Bebida no encontrada: "
                                            + entrada.getKey()));

            if (!bebida.isActiva()) {
                throw new IllegalArgumentException(
                        "Bebida inactiva: " + bebida.getNombre());
            }

            if (bebida.getStockActual() < entrada.getValue()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para: "
                                + bebida.getNombre());
            }

            bebidas.put(bebida.getIdBebida(), bebida);
        }

        // Construir la venta.
        VentaBebidas venta = new VentaBebidas(
                idVenta,
                LocalDateTime.now());

        for (DetalleVentaBebidas detalle : detalles) {

            Bebida bebida = bebidas.get(detalle.getIdBebida());

            BigDecimal precioConIVA =
                    calcularPrecioConIVA(bebida.getPrecioFijo());

            DetalleVentaBebidas nuevoDetalle =
                    new DetalleVentaBebidas(
                            detalle.getIdDetalleVenta(),
                            bebida.getIdBebida(),
                            detalle.getCantidad(),
                            precioConIVA);

            venta.agregarDetalle(nuevoDetalle);
        }

        // Preparar los nuevos stocks.
        Map<Integer, Integer> stocksAnteriores =
                new LinkedHashMap<>();

        for (Map.Entry<Integer, Integer> entrada :
                cantidades.entrySet()) {

            Bebida bebida = bebidas.get(entrada.getKey());

            stocksAnteriores.put(
                    bebida.getIdBebida(),
                    bebida.getStockActual());

            bebida.setStockActual(
                    bebida.getStockActual() - entrada.getValue());
        }

        // Guardar la venta y actualizar los stocks.
        // Si ocurre un error, intentar restaurar los stocks.
        try {
            for (Bebida bebida : bebidas.values()) {
                bebidaRepository.actualizar(bebida);
            }

            ventaRepository.guardar(venta);

        } catch (RuntimeException e) {

            for (Map.Entry<Integer, Integer> entrada :
                    stocksAnteriores.entrySet()) {

                Bebida bebida = bebidas.get(entrada.getKey());

                bebida.setStockActual(entrada.getValue());

                try {
                    bebidaRepository.actualizar(bebida);
                } catch (RuntimeException errorRestauracion) {
                    e.addSuppressed(errorRestauracion);
                }
            }

            throw e;
        }

        return venta;
    }

    public List<VentaBebidas> obtenerVentas() {
        return ventaRepository.obtenerTodos();
    }

    public BigDecimal calcularIVA(BigDecimal precioFijo) {

        if (precioFijo == null ||
                precioFijo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo");
        }

        return precioFijo.multiply(IVA)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPrecioConIVA(
            BigDecimal precioFijo) {

        return precioFijo.add(calcularIVA(precioFijo))
                .setScale(2, RoundingMode.HALF_UP);
    }
}