package com.trabajo1.artesanias.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JsonStorageService {

    private final Gson gson;

    public JsonStorageService() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(
                        LocalDate.class,
                        (com.google.gson.JsonSerializer<LocalDate>)
                                (fecha, tipo, contexto) ->
                                        new com.google.gson.JsonPrimitive(fecha.toString())
                )
                .registerTypeAdapter(
                        LocalDate.class,
                        (com.google.gson.JsonDeserializer<LocalDate>)
                                (json, tipo, contexto) ->
                                        LocalDate.parse(json.getAsString())
                )
                .setPrettyPrinting()
                .create();
    }

    // Guardar una lista de objetos en un archivo JSON
    public <T> void guardar(List<T> datos, String ruta) {
        try {
            Path archivo = Path.of(ruta);

            if (archivo.getParent() != null) {
                Files.createDirectories(archivo.getParent());
            }

            String json = gson.toJson(datos);
            Files.writeString(archivo, json);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar el archivo: " + ruta, e
            );
        }
    }

    // Cargar una lista de objetos desde un archivo JSON
    public <T> List<T> cargar(String ruta, Class<T> clase) {
        try {
            Path archivo = Path.of(ruta);

            if (!Files.exists(archivo)) {
                return new ArrayList<>();
            }

            String json = Files.readString(archivo);

            if (json.isBlank()) {
                return new ArrayList<>();
            }

            Type tipoLista = TypeToken.getParameterized(
                    List.class, clase
            ).getType();

            List<T> datos = gson.fromJson(json, tipoLista);

            return datos != null ? datos : new ArrayList<>();

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al cargar el archivo: " + ruta, e
            );
        }
    }
}