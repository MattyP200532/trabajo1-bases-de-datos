package com.trabajo1.biblioteca.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class JsonRepository<T> {

    private final Gson gson;
    private final String rutaArchivo;
    private final Type tipoLista;

    public JsonRepository(String rutaArchivo, Type tipoLista) {
        this.rutaArchivo = rutaArchivo;
        this.tipoLista = tipoLista;

        this.gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(
                        LocalDate.class,
                        new TypeAdapter<LocalDate>() {

                            @Override
                            public void write(
                                    JsonWriter out,
                                    LocalDate fecha) throws IOException {

                                out.value(fecha.toString());
                            }

                            @Override
                            public LocalDate read(
                                    JsonReader in) throws IOException {

                                return LocalDate.parse(in.nextString());
                            }
                        }.nullSafe()
                )
                .create();
    }

    public void guardar(List<T> datos) {

        File archivo = new File(rutaArchivo);

        File directorio = archivo.getParentFile();

        if (directorio != null) {
            directorio.mkdirs();
        }

        try (FileWriter writer = new FileWriter(archivo)) {

            gson.toJson(datos, tipoLista, writer);

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al guardar el archivo JSON: " + rutaArchivo,
                    e
            );
        }
    }

    public List<T> cargar() {

        File archivo = new File(rutaArchivo);

        if (!archivo.exists()) {
            return new ArrayList<>();
        }

        try (FileReader reader = new FileReader(archivo)) {

            List<T> datos = gson.fromJson(reader, tipoLista);

            return datos != null ? datos : new ArrayList<>();

        } catch (IOException e) {

            throw new RuntimeException(
                    "Error al leer el archivo JSON: " + rutaArchivo,
                    e
            );
        }
    }
}