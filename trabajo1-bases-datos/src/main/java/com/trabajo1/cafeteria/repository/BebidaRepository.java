
package com.trabajo1.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Bebida;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class BebidaRepository {

    private final Path archivo;
    private final Gson gson;

    public BebidaRepository() {
        this("data/cafeteria/bebidas.json");
    }

    public BebidaRepository(String rutaArchivo) {
        this.archivo = Path.of(rutaArchivo);
        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
    }

    public List<Bebida> obtenerTodos() {
        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);

            Type tipoLista = new TypeToken<List<Bebida>>() {
            }.getType();

            List<Bebida> bebidas = gson.fromJson(json, tipoLista);

            return bebidas == null
                    ? new ArrayList<>()
                    : bebidas;

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al leer las bebidas", e);
        }
    }

    public Optional<Bebida> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(b -> b.getIdBebida() == id)
                .findFirst();
    }

    public void guardar(Bebida bebida) {
        List<Bebida> bebidas = obtenerTodos();

        boolean existe = bebidas.stream()
                .anyMatch(b ->
                        b.getIdBebida() == bebida.getIdBebida());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una bebida con ese identificador");
        }

        bebidas.add(bebida);
        guardarTodos(bebidas);
    }

    public void actualizar(Bebida bebida) {
        List<Bebida> bebidas = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < bebidas.size(); i++) {
            if (bebidas.get(i).getIdBebida()
                    == bebida.getIdBebida()) {

                bebidas.set(i, bebida);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Bebida no encontrada");
        }

        guardarTodos(bebidas);
    }

    private void guardarTodos(List<Bebida> bebidas) {
        try {
            Files.createDirectories(archivo.getParent());

            String json = gson.toJson(bebidas);

            Files.writeString(archivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar las bebidas", e);
        }
    }
}