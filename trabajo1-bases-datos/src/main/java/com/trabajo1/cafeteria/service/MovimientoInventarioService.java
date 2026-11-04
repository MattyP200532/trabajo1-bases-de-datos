
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.MovimientoInventario;
import com.trabajo1.cafeteria.repository.MovimientoInventarioRepository;

import java.util.List;
import java.util.Optional;

public class MovimientoInventarioService {

    private final MovimientoInventarioRepository movimientoRepository;

    public MovimientoInventarioService() {
        this.movimientoRepository = new MovimientoInventarioRepository();
    }

    public List<MovimientoInventario> obtenerTodos() {
        return movimientoRepository.obtenerTodos();
    }

    public Optional<MovimientoInventario> buscarPorId(int id) {
        return movimientoRepository.buscarPorId(id);
    }

    public List<MovimientoInventario> obtenerPorIngrediente(int idIngrediente) {
        return movimientoRepository.obtenerTodos()
                .stream()
                .filter(m -> m.getIdIngrediente() == idIngrediente)
                .toList();
    }
}
