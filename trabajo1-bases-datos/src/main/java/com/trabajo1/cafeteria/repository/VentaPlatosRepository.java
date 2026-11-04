
package com.trabajo1.cafeteria.repository;

import com.google.gson.reflect.TypeToken;
import com.trabajo1.cafeteria.model.VentaPlatos;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VentaPlatosRepository {

    private final JsonDataManager jsonManager;
    private static final String ARCHIVO = "ventas_platos";

    public VentaPlatosRepository() {
        this.jsonManager = new JsonDataManager();
    }

    public List<VentaPlatos> obtenerTodos() {
        Type tipo = new TypeToken<List<VentaPlatos>>() {}.getType();

        List<VentaPlatos> lista = jsonManager.cargar(
                ARCHIVO, tipo, new ArrayList<>()
        );

        return lista == null ? new ArrayList<>() : lista;
    }

    public Optional<VentaPlatos> buscarPorId(int id) {
        return obtenerTodos().stream()
                .filter(v -> v.getIdVenta() == id)
                .findFirst();
    }

    public void guardar(VentaPlatos venta) {
        List<VentaPlatos> lista = obtenerTodos();

        boolean existe = lista.stream()
                .anyMatch(v -> v.getIdVenta() == venta.getIdVenta());

        if (existe) {
            throw new IllegalArgumentException(
                    "Ya existe una venta de platos con ese identificador"
            );
        }

        lista.add(venta);
        jsonManager.guardar(ARCHIVO, lista);
    }

    public void actualizar(VentaPlatos venta) {
        List<VentaPlatos> lista = obtenerTodos();

        boolean encontrado = false;

        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getIdVenta() == venta.getIdVenta()) {
                lista.set(i, venta);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            throw new IllegalArgumentException(
                    "Venta de platos no encontrada"
            );
        }

        jsonManager.guardar(ARCHIVO, lista);
    }
}