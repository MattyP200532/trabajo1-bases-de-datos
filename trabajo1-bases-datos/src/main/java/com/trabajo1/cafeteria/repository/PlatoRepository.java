
package com.trabajo1.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Plato;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlatoRepository {

    private final Path archivo;
    private final Gson gson;

    public PlatoRepository() {
        this("data/cafeteria/platos.json");
    }

    public PlatoRepository(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public List<Plato> obtenerTodos() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);

            Type tipoLista = new TypeToken<List<Plato>>() {
            }.getType();

            List<Plato> platos = gson.fromJson(json, tipoLista);

            return platos == null ? new ArrayList<>() : platos;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al leer los platos", e);
        }
    }

    public Optional<Plato> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(p -> p.getIdPlato() == id)
                .findFirst();
    }

    public void guardar(Plato plato) {
        List<Plato> platos = obtenerTodos();

        boolean existe = platos.stream()
                .anyMatch(p -> p.getIdPlato() == plato.getIdPlato());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un plato con ese identificador");
        }

        platos.add(plato);
        guardarTodos(platos);
    }

    public void actualizar(Plato plato) {
        List<Plato> platos = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < platos.size(); i++) {
            if (platos.get(i).getIdPlato() == plato.getIdPlato()) {
                platos.set(i, plato);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Plato no encontrado");
        }

        guardarTodos(platos);
    }

    private void guardarTodos(List<Plato> platos) {
        try {
            Files.createDirectories(archivo.getParent());

            String json = gson.toJson(platos);

            Files.writeString(archivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar los platos", e);
        }
    }
}