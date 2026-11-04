
package com.trabajo1.cafeteria.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Ingrediente;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IngredienteRepository {

    private final JsonDataManager dataManager;
    private static final String ARCHIVO = "ingredientes";

    public IngredienteRepository() {
        this.dataManager = new JsonDataManager();
    }

    public List<Ingrediente> obtenerTodos() {

        Type tipo = new TypeToken<List<Ingrediente>>() {
        }.getType();

        List<Ingrediente> ingredientes = dataManager.cargar(
                ARCHIVO,
                tipo,
                new ArrayList<>()
        );

        return ingredientes == null
                ? new ArrayList<>()
                : ingredientes;
    }

    public Optional<Ingrediente> buscarPorId(int id) {

        return obtenerTodos().stream()
                .filter(i -> i.getIdIngrediente() == id)
                .findFirst();
    }

    public int obtenerSiguienteId() {

    List<Ingrediente> ingredientes = obtenerTodos();

    if (ingredientes.isEmpty()) {
        return 1;
    }

    return ingredientes.stream()
            .mapToInt(Ingrediente::getIdIngrediente)
            .max()
            .orElse(0) + 1;
    }

    public void guardar(Ingrediente ingrediente) {

        List<Ingrediente> ingredientes = obtenerTodos();

        boolean existe = ingredientes.stream()
                .anyMatch(i ->
                        i.getIdIngrediente()
                                == ingrediente.getIdIngrediente()
                );

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un ingrediente con ese identificador"
            );
        }

        ingredientes.add(ingrediente);

        dataManager.guardar(ARCHIVO, ingredientes);
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
                    "Ingrediente no encontrado"
            );
        }

        dataManager.guardar(ARCHIVO, ingredientes);
    }
}
