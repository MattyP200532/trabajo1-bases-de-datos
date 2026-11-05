package com.trabajo1.transporte.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.trabajo1.transporte.model.Viaje;
import com.trabajo1.transporte.model.Boleto;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RepositorioTransporte {

    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private Path carpeta = Path.of("data", "transporte");

    // Guardar los viajes
    public void guardarViajes(List<Viaje> viajes) {
        guardar("viajes.json", viajes);
    }

    // Guardar los boletos
    public void guardarBoletos(List<Boleto> boletos) {
        guardar("boletos.json", boletos);
    }

    // Guardar informacion en JSON
    private void guardar(String nombre, Object datos) {
        try {
            Files.createDirectories(carpeta);
            Files.writeString(carpeta.resolve(nombre), gson.toJson(datos));
        } catch (IOException error) {
            throw new IllegalStateException("Error al guardar " + nombre, error);
        }
    }

    // Recuperar los viajes
    public List<Viaje> cargarViajes() {
        Path archivo = carpeta.resolve("viajes.json");

        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);
            Type tipo = new TypeToken<List<Viaje>>() {}.getType();
            List<Viaje> viajes = gson.fromJson(json, tipo);

            if (viajes == null) {
                return new ArrayList<>();
            }

            return viajes;

        } catch (IOException | JsonParseException error) {
            throw new IllegalStateException("Error al leer los viajes", error);
        }
    }

    // Recuperar los boletos
    public List<Boleto> cargarBoletos() {
        Path archivo = carpeta.resolve("boletos.json");

        if (!Files.exists(archivo)) {
            return new ArrayList<>();
        }

        try {
            String json = Files.readString(archivo);
            Type tipo = new TypeToken<List<Boleto>>() {}.getType();
            List<Boleto> boletos = gson.fromJson(json, tipo);

            if (boletos == null) {
                return new ArrayList<>();
            }

            return boletos;

        } catch (IOException | JsonParseException error) {
            throw new IllegalStateException("Error al leer los boletos", error);
        }
    }
}