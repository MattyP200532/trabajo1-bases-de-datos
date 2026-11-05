
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Artesano;
import com.trabajo1.artesanias.model.Artesania;
import java.util.ArrayList;
import java.util.List;

public class ArtesanoService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = RutasDatos.archivo("artesanos.json");
    private final List<Artesano> artesanos;
    private final List<Artesania> artesanias;

    public ArtesanoService() {
        artesanos = storage.cargar(archivo, Artesano.class);
        artesanias = storage.cargar(RutasDatos.archivo("artesanias.json"), Artesania.class);
    }

    public void registrar(Artesano artesano) {
        if (artesano == null || buscarPorId(artesano.getId()) != null) {
            throw new IllegalArgumentException("Artesano inválido o ID duplicado.");
        }
        artesanos.add(artesano);
        guardar();
    }

    public List<Artesano> listar() {
        return new ArrayList<>(artesanos);
    }

    public Artesano buscarPorId(String id) {
        return artesanos.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    public void eliminar(String id) {
        boolean tieneArtesanias = artesanias.stream()
                .anyMatch(a -> a.getArtesanoId().equals(id));

        if (tieneArtesanias) {
            throw new IllegalStateException(
                    "No se puede eliminar un artesano con artesanías registradas."
            );
        }

        artesanos.removeIf(a -> a.getId().equals(id));
        guardar();
    }

    public void agregarArtesania(Artesania artesania) {
        if (artesania == null) {
            throw new IllegalArgumentException("La artesanía no puede ser nula.");
        }
        artesanias.add(artesania);
    }

    private void guardar() {
        storage.guardar(artesanos, archivo);
    }
}
