
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Recepcion;
import com.trabajo1.artesanias.model.DetalleVenta;
import com.trabajo1.artesanias.model.Devolucion;

import java.util.ArrayList;
import java.util.List;

public class RecepcionService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = RutasDatos.archivo("recepciones.json");
    private final List<Recepcion> recepciones;
    private final List<DetalleVenta> detallesVenta;
    private final List<Devolucion> devoluciones;

    public RecepcionService() {
        recepciones = storage.cargar(archivo, Recepcion.class);
        detallesVenta = storage.cargar(RutasDatos.archivo("detalles-venta.json"), DetalleVenta.class);
        devoluciones = storage.cargar(RutasDatos.archivo("devoluciones.json"), Devolucion.class);
    }

    public void registrar(Recepcion recepcion) {
        if (recepcion == null || recepcion.getCantidad() <= 0
                || buscarPorId(recepcion.getId()) != null) {
            throw new IllegalArgumentException("Recepción inválida o ID duplicado.");
        }
        recepciones.add(recepcion);
        guardarRecepciones();
    }

    public List<Recepcion> listar() {
        return new ArrayList<>(recepciones);
    }

    public Recepcion buscarPorId(String id) {
        return recepciones.stream()
                .filter(r -> r.getId().equals(id))
                .findFirst().orElse(null);
    }

    public int unidadesVendidas(String id) {
        return detallesVenta.stream()
                .filter(d -> d.getRecepcionId().equals(id))
                .mapToInt(DetalleVenta::getCantidad).sum();
    }

    public int unidadesDevueltas(String id) {
        return devoluciones.stream()
                .filter(d -> d.getRecepcionId().equals(id))
                .mapToInt(Devolucion::getCantidad).sum();
    }

    public int unidadesDisponibles(String id) {
        Recepcion r = buscarPorId(id);
        if (r == null) throw new IllegalArgumentException("La recepción no existe.");
        return r.getCantidad() - unidadesVendidas(id) - unidadesDevueltas(id);
    }

    public void registrarDetalleVenta(DetalleVenta detalle) {
        if (detalle == null) throw new IllegalArgumentException("Detalle nulo.");

        Recepcion r = buscarPorId(detalle.getRecepcionId());

        if (r == null || !r.getArtesaniaId().equals(detalle.getArtesaniaId())) {
            throw new IllegalArgumentException("Recepción o artesanía incorrecta.");
        }

        if (detalle.getCantidad() <= 0
                || detalle.getCantidad() > unidadesDisponibles(r.getId())) {
            throw new IllegalArgumentException("No hay suficientes unidades disponibles.");
        }

        detallesVenta.add(detalle);
        storage.guardar(detallesVenta, RutasDatos.archivo("detalles-venta.json"));
    }

    public void registrarDevolucion(Devolucion devolucion) {
        if (devolucion == null) throw new IllegalArgumentException("Devolución nula.");

        Recepcion r = buscarPorId(devolucion.getRecepcionId());

        if (r == null || !r.getArtesanoId().equals(devolucion.getArtesanoId())) {
            throw new IllegalArgumentException("La devolución debe ser al artesano propietario.");
        }

        if (devolucion.getCantidad() <= 0
                || devolucion.getCantidad() > unidadesDisponibles(r.getId())) {
            throw new IllegalArgumentException("Cantidad de devolución inválida.");
        }

        devoluciones.add(devolucion);
        storage.guardar(devoluciones, RutasDatos.archivo("devoluciones.json"));
    }

    public List<DetalleVenta> listarDetallesVenta() {
        return new ArrayList<>(detallesVenta);
    }

    public List<Devolucion> listarDevoluciones() {
        return new ArrayList<>(devoluciones);
    }

    private void guardarRecepciones() {
        storage.guardar(recepciones, archivo);
    }
}
