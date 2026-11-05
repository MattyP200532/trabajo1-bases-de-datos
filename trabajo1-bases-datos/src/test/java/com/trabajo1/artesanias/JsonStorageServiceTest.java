package com.trabajo1.artesanias;

import com.trabajo1.artesanias.service.JsonStorageService;
import org.junit.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class JsonStorageServiceTest {

    @Test
    public void debeGuardarYCargarDatos() throws Exception {

        JsonStorageService storage = new JsonStorageService();

        Path archivo = Path.of("target", "prueba-comunidades.json");

        List<String> datosOriginales = Arrays.asList(
                "Comunidad A",
                "Comunidad B"
        );

        storage.guardar(datosOriginales, archivo.toString());

        assertTrue(Files.exists(archivo));

        List<String> datosCargados =
                storage.cargar(archivo.toString(), String.class);

        assertEquals(datosOriginales, datosCargados);
    }
}