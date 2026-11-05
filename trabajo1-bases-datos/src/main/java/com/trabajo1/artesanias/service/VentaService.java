
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Venta;
import com.trabajo1.artesanias.model.DetalleVenta;
import com.trabajo1.artesanias.model.Artesania;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class VentaService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = RutasDatos.archivo("ventas.json");
    private final List<Venta> ventas;

    public VentaService() {
        ventas = storage.cargar(archivo, Venta.class);
    }

    public void registrar(Venta venta, List<DetalleVenta> detalles,
            RecepcionService recepcionService, ArtesaniaService artesaniaService) {

        if (venta == null || detalles == null || detalles.isEmpty()) {
            throw new IllegalArgumentException("La venta debe tener detalles.");
        }

        if (buscarPorId(venta.getId()) != null) {
            throw new IllegalArgumentException("La venta ya existe.");
        }

        BigDecimal total = BigDecimal.ZERO;

        for (DetalleVenta detalle : detalles) {
            if (!venta.getId().equals(detalle.getVentaId())
                    || detalle.getCantidad() <= 0
                    || detalle.getPrecioUnitario() == null
                    || detalle.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException("Detalle de venta inválido.");
            }

            Artesania artesania = artesaniaService.buscarPorId(detalle.getArtesaniaId());
            if (artesania == null) {
                throw new IllegalArgumentException("La artesanía no existe.");
            }

            total = total.add(detalle.getPrecioUnitario()
                    .multiply(BigDecimal.valueOf(detalle.getCantidad())));
        }

        for (DetalleVenta detalle : detalles) {
            recepcionService.registrarDetalleVenta(detalle);
        }

        venta.setTotal(total);
        ventas.add(venta);
        storage.guardar(ventas, archivo);
    }

    public List<Venta> listar() {
        return new ArrayList<>(ventas);
    }

    public Venta buscarPorId(String id) {
        return ventas.stream()
                .filter(v -> v.getId().equals(id))
                .findFirst().orElse(null);
    }
}
