
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Bebida;
import com.trabajo1.cafeteria.model.DetalleVentaBebidas;
import com.trabajo1.cafeteria.model.VentaBebidas;
import com.trabajo1.cafeteria.repository.BebidaRepository;
import com.trabajo1.cafeteria.repository.VentaBebidasRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

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
                    "El identificador de venta debe ser positivo");
        }

        if (detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException(
                    "La venta debe contener al menos una bebida");
        }

        if (ventaRepository.buscarPorId(idVenta).isPresent()) {
            throw new IllegalArgumentException(
                    "Ya existe una venta con ese identificador");
        }

        // Validar todas las bebidas antes de modificar el stock.
        for (DetalleVentaBebidas detalle : detalles) {

            if (detalle.getCantidad() <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad debe ser mayor que cero");
            }

            Bebida bebida = bebidaRepository
                    .buscarPorId(detalle.getIdBebida())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "Bebida no encontrada: "
                                    + detalle.getIdBebida()));

            if (!bebida.isActiva()) {
                throw new IllegalArgumentException(
                        "La bebida no está activa: "
                                + bebida.getNombre());
            }

            if (bebida.getStockActual() < detalle.getCantidad()) {
                throw new IllegalArgumentException(
                        "Stock insuficiente para: "
                                + bebida.getNombre());
            }
        }

        VentaBebidas venta = new VentaBebidas(
                idVenta,
                LocalDateTime.now()
        );

        BigDecimal total = BigDecimal.ZERO;

        // Calcular precios con IVA.
        for (DetalleVentaBebidas detalle : detalles) {

            Bebida bebida = bebidaRepository
                    .buscarPorId(detalle.getIdBebida())
                    .orElseThrow();

            BigDecimal precioConIVA = bebida.getPrecioFijo()
                    .multiply(BigDecimal.ONE.add(IVA))
                    .setScale(2, RoundingMode.HALF_UP);

            DetalleVentaBebidas nuevoDetalle =
                    new DetalleVentaBebidas(
                            detalle.getIdDetalleVenta(),
                            bebida.getIdBebida(),
                            detalle.getCantidad(),
                            precioConIVA
                    );

            venta.agregarDetalle(nuevoDetalle);

            total = total.add(nuevoDetalle.getSubtotal());
        }

        // Actualizar stock.
        for (DetalleVentaBebidas detalle : detalles) {

            Bebida bebida = bebidaRepository
                    .buscarPorId(detalle.getIdBebida())
                    .orElseThrow();

            bebida.setStockActual(
                    bebida.getStockActual() - detalle.getCantidad()
            );

            bebidaRepository.actualizar(bebida);
        }

        venta.calcularTotal();

        // Registrar venta.
        ventaRepository.guardar(venta);

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

    public BigDecimal calcularPrecioConIVA(BigDecimal precioFijo) {
        return precioFijo.add(calcularIVA(precioFijo))
                .setScale(2, RoundingMode.HALF_UP);
    }
}