
package com.trabajo1.cafeteria.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class JsonDataManager {

    private static final String RUTA_BASE = "data/cafeteria/";

    private final Gson gson;

    public JsonDataManager() {

        JsonSerializer<LocalDateTime> serializador =
                (fecha, tipo, contexto) ->
                        contexto.serialize(
                                fecha.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
                        );

        JsonDeserializer<LocalDateTime> deserializador =
                (json, tipo, contexto) ->
                        LocalDateTime.parse(
                                json.getAsString(),
                                DateTimeFormatter.ISO_LOCAL_DATE_TIME
                        );

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, serializador)
                .registerTypeAdapter(LocalDateTime.class, deserializador)
                .create();
    }

    public void guardar(String nombreArchivo, Object datos) {

        try {
            Path directorio = Paths.get(RUTA_BASE);
            Files.createDirectories(directorio);

            Path archivo = directorio.resolve(nombreArchivo + ".json");

            String json = gson.toJson(datos);

            Files.writeString(
                    archivo,
                    json,
                    StandardCharsets.UTF_8
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al guardar el archivo JSON: " + nombreArchivo,
                    e
            );
        }
    }

    public <T> T cargar(
            String nombreArchivo,
            Type tipo,
            T valorPorDefecto) {

        try {
            Path archivo = Paths.get(
                    RUTA_BASE + nombreArchivo + ".json"
            );

            if (!Files.exists(archivo)) {
                return valorPorDefecto;
            }

            String json = Files.readString(
                    archivo,
                    StandardCharsets.UTF_8
            );

            return gson.fromJson(json, tipo);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Error al leer el archivo JSON: " + nombreArchivo,
                    e
            );
        }
    }
}