
package com.trabajo1.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Receta;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecetaRepository {

    private final Path archivo;
    private final Gson gson;

    public RecetaRepository() {
        this("data/cafeteria/recetas.json");
    }

    public RecetaRepository(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public List<Receta> obtenerTodos() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);

            Type tipoLista = new TypeToken<List<Receta>>() {
            }.getType();

            List<Receta> recetas = gson.fromJson(json, tipoLista);

            return recetas == null
                    ? new ArrayList<>()
                    : recetas;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al leer las recetas", e);
        }
    }

    public Optional<Receta> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(r -> r.getIdReceta() == id)
                .findFirst();
    }

    public void guardar(Receta receta) {
        List<Receta> recetas = obtenerTodos();

        boolean existe = recetas.stream()
                .anyMatch(r ->
                        r.getIdReceta() == receta.getIdReceta());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una receta con ese identificador");
        }

        recetas.add(receta);
        guardarTodos(recetas);
    }

    public void actualizar(Receta receta) {
        List<Receta> recetas = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < recetas.size(); i++) {
            if (recetas.get(i).getIdReceta()
                    == receta.getIdReceta()) {

                recetas.set(i, receta);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Receta no encontrada");
        }

        guardarTodos(recetas);
    }

    private void guardarTodos(List<Receta> recetas) {
        try {
            Files.createDirectories(archivo.getParent());

            String json = gson.toJson(recetas);

            Files.writeString(archivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar las recetas", e);
        }
    }
}