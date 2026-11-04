
package com.trabajo1.cafeteria.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.VentaBebidas;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaBebidasRepository {

    private final JsonDataManager dataManager;
    private static final String ARCHIVO = "ventas_bebidas";

    public VentaBebidasRepository() {
        this.dataManager = new JsonDataManager();
    }

    public List<VentaBebidas> obtenerTodos() {
        Type tipo = new TypeToken<List<VentaBebidas>>() {
        }.getType();

        return dataManager.cargar(
                ARCHIVO,
                tipo,
                new ArrayList<>()
        );
    }

    public Optional<VentaBebidas> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(v -> v.getIdVenta() == id)
                .findFirst();
    }

    public void guardar(VentaBebidas venta) {
        List<VentaBebidas> lista = obtenerTodos();

        boolean existe = lista.stream()
                .anyMatch(v -> v.getIdVenta() == venta.getIdVenta());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una venta de bebidas con ese identificador"
            );
        }

        lista.add(venta);
        dataManager.guardar(ARCHIVO, lista);
    }
}