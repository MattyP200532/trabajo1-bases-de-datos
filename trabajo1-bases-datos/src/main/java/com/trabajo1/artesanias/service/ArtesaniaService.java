
package com.trabajo1.artesanias.service;

import com.trabajo1.artesanias.model.Artesania;
import java.util.ArrayList;
import java.util.List;

public class ArtesaniaService {

    private final JsonStorageService storage = new JsonStorageService();
    private final String archivo = "data/artesanias.json";
    private final List<Artesania> artesanias;

    public ArtesaniaService() {
        artesanias = storage.cargar(archivo, Artesania.class);
    }

    public void registrar(Artesania artesania) {
        if (artesania == null || buscarPorId(artesania.getId()) != null) {
            throw new IllegalArgumentException("Artesanía inválida o ID duplicado.");
        }
        artesanias.add(artesania);
        guardar();
    }

    public List<Artesania> listar() {
        return new ArrayList<>(artesanias);
    }

    public Artesania buscarPorId(String id) {
        return artesanias.stream()
                .filter(a -> a.getId().equals(id))
                .findFirst().orElse(null);
    }

    private void guardar() {
        storage.guardar(artesanias, archivo);
    }
}
