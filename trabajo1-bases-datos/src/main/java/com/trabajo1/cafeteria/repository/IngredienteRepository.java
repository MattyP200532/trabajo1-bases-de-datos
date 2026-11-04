
package com.trabajo1.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Ingrediente;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredienteRepository {

    private final Path archivo;
    private final Gson gson;

    public IngredienteRepository() {
        this("data/cafeteria/ingredientes.json");
    }

    public IngredienteRepository(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public List<Ingrediente> obtenerTodos() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);

            Type tipoLista = new TypeToken<List<Ingrediente>>() {
            }.getType();

            List<Ingrediente> ingredientes =
                    gson.fromJson(json, tipoLista);

            return ingredientes == null
                    ? new ArrayList<>()
                    : ingredientes;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al leer los ingredientes", e);
        }
    }

    public Optional<Ingrediente> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(i -> i.getIdIngrediente() == id)
                .findFirst();
    }

    public void guardar(Ingrediente ingrediente) {
        List<Ingrediente> ingredientes = obtenerTodos();

        boolean existe = ingredientes.stream()
                .anyMatch(i ->
                        i.getIdIngrediente()
                                == ingrediente.getIdIngrediente());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un ingrediente con ese identificador");
        }

        ingredientes.add(ingrediente);
        guardarTodos(ingredientes);
    }

    public void actualizar(Ingrediente ingrediente) {
        List<Ingrediente> ingredientes = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < ingredientes.size(); i++) {
            if (ingredientes.get(i).getIdIngrediente()
                    == ingrediente.getIdIngrediente()) {

                ingredientes.set(i, ingrediente);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Ingrediente no encontrado");
        }

        guardarTodos(ingredientes);
    }

    private void guardarTodos(List<Ingrediente> ingredientes) {
        try {
            Files.createDirectories(archivo.getParent());

            String json = gson.toJson(ingredientes);

            Files.writeString(archivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar los ingredientes", e);
        }
    }
}