package com.trabajo1.artesanias.service;

import java.nio.file.Path;

public final class RutasDatos {

    private RutasDatos() {
    }

    public static String archivo(String nombre) {
        String directorio = System.getProperty(
                "artesanias.data.dir",
                "data/artesanias"
        );

        return Path.of(directorio, nombre).toString();
    }
}
