
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Comunidad;
import java.util.ArrayList;
import java.util.List;

public class ComunidadService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = RutasDatos.archivo("comunidades.json");
    private final List<Comunidad> comunidades;

    public ComunidadService() {
        comunidades = storage.cargar(archivo, Comunidad.class);
    }

    public void registrar(Comunidad comunidad) {
        if (comunidad == null || buscarPorId(comunidad.getId()) != null) {
            throw new IllegalArgumentException("Comunidad inválida o ID duplicado.");
        }
        comunidades.add(comunidad);
        guardar();
    }

    public List<Comunidad> listar() {
        return new ArrayList<>(comunidades);
    }

    public Comunidad buscarPorId(String id) {
        return comunidades.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        comunidades.removeIf(c -> c.getId().equals(id));
        guardar();
    }

    private void guardar() {
        storage.guardar(comunidades, archivo);
    }
}
