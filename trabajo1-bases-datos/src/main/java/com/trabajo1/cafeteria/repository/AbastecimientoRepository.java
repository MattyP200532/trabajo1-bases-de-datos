
package com.trabajo1.cafeteria.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.Abastecimiento;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AbastecimientoRepository {

    private final JsonDataManager jsonManager;
    private static final String ARCHIVO = "abastecimientos";

    public AbastecimientoRepository() {
        this.jsonManager = new JsonDataManager();
    }

    public List<Abastecimiento> obtenerTodos() {
        Type tipo = new TypeToken<List<Abastecimiento>>() {}.getType();

        List<Abastecimiento> lista = jsonManager.cargar(
                ARCHIVO, tipo, new ArrayList<>()
        );

        return lista == null ? new ArrayList<>() : lista;
    }

    public Optional<Abastecimiento> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(a -> a.getIdAbastecimiento() == id)
                .findFirst();
    }

    public void guardar(Abastecimiento abastecimiento) {
        List<Abastecimiento> lista = obtenerTodos();

        boolean existe = lista.stream()
                .anyMatch(a -> a.getIdAbastecimiento()
                        == abastecimiento.getIdAbastecimiento());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un abastecimiento con ese identificador"
            );
        }

        lista.add(abastecimiento);
        jsonManager.guardar(ARCHIVO, lista);
    }

    public void actualizar(Abastecimiento abastecimiento) {
        List<Abastecimiento> lista = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdAbastecimiento()
                    == abastecimiento.getIdAbastecimiento()) {

                lista.set(i, abastecimiento);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Abastecimiento no encontrado"
            );
        }

        jsonManager.guardar(ARCHIVO, lista);
    }
}