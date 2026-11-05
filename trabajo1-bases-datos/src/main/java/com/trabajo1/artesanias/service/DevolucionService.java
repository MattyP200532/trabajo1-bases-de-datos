
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Devolucion;
import com.trabajo1.artesanias.model.Recepcion;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class DevolucionService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = RutasDatos.archivo("devoluciones.json");
    private final List<Devolucion> devoluciones;

    public DevolucionService() {
        devoluciones = storage.cargar(archivo, Devolucion.class);
    }

    public void registrar(Devolucion devolucion, RecepcionService recepcionService) {
        if (devolucion == null || devolucion.getCantidad() <= 0) {
            throw new IllegalArgumentException("Devolución inválida.");
        }

        Recepcion recepcion = recepcionService.buscarPorId(devolucion.getRecepcionId());

        if (recepcion == null
                || !recepcion.getArtesanoId().equals(devolucion.getArtesanoId())) {
            throw new IllegalArgumentException("La devolución debe ser al artesano propietario.");
        }

        long dias = ChronoUnit.DAYS.between(
                recepcion.getFechaRecepcion(), devolucion.getFechaDevolucion());

        if (dias < 90) {
            throw new IllegalArgumentException(
                    "La artesanía debe permanecer más de 90 días en tienda.");
        }

        recepcionService.registrarDevolucion(devolucion);
        devoluciones.add(devolucion);
        storage.guardar(devoluciones, archivo);
    }

    public List<Devolucion> listar() {
        return new ArrayList<>(devoluciones);
    }
}
