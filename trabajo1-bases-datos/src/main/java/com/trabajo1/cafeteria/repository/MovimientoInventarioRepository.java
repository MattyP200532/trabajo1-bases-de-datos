
package com.trabajo1.cafeteria.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.MovimientoInventario;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MovimientoInventarioRepository {

    private final JsonDataManager dataManager;
    private static final String ARCHIVO = "movimientos_inventario";

    public MovimientoInventarioRepository() {
        this.dataManager = new JsonDataManager();
    }

    public List<MovimientoInventario> obtenerTodos() {
        Type tipo = new TypeToken<List<MovimientoInventario>>() {
        }.getType();

        return dataManager.cargar(
                ARCHIVO,
                tipo,
                new ArrayList<>()
        );
    }

    public Optional<MovimientoInventario> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(m -> m.getIdMovimiento() == id)
                .findFirst();
    }

    public void guardar(MovimientoInventario movimiento) {
        List<MovimientoInventario> lista = obtenerTodos();

        boolean existe = lista.stream()
                .anyMatch(m -> m.getIdMovimiento()
                        == movimiento.getIdMovimiento());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe un movimiento con ese identificador"
            );
        }

        lista.add(movimiento);
        dataManager.guardar(ARCHIVO, lista);
    }
}