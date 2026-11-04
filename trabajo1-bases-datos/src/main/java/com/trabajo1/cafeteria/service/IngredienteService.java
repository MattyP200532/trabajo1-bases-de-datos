
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Ingrediente;
import com.trabajo1.cafeteria.repository.IngredienteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IngredienteService {

    private final IngredienteRepository ingredienteRepository;

    public IngredienteService() {
        this.ingredienteRepository = new IngredienteRepository();
    }

    public void registrarIngrediente(Ingrediente ingrediente) {

        validarIngrediente(ingrediente);

        ingredienteRepository.guardar(ingrediente);
    }

    public List<Ingrediente> obtenerTodos() {

        return ingredienteRepository.obtenerTodos()
                .stream()
                .filter(Ingrediente::isActivo)
                .collect(Collectors.toList());
    }

    public Optional<Ingrediente> buscarPorId(int id) {

        return ingredienteRepository.buscarPorId(id)
                .filter(Ingrediente::isActivo);
    }

    public void actualizarIngrediente(Ingrediente ingrediente) {

        validarIngrediente(ingrediente);

        Optional<Ingrediente> existente =
                ingredienteRepository.buscarPorId(
                        ingrediente.getIdIngrediente()
                );

        if (existente.isEmpty()) {
            throw new IllegalArgumentException(
                    "El ingrediente no existe"
            );
        }

        ingredienteRepository.actualizar(ingrediente);
    }

    public void desactivarIngrediente(int id) {

        Ingrediente ingrediente = buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Ingrediente no encontrado"
                        )
                );

        ingrediente.setActivo(false);

        ingredienteRepository.actualizar(ingrediente);
    }

    public List<Ingrediente> obtenerIngredientesBajoStock() {

        return obtenerTodos()
                .stream()
                .filter(i ->
                        i.getStockActual().compareTo(
                                i.getStockMinimo()
                        ) < 0
                )
                .collect(Collectors.toList());
    }

    private void validarIngrediente(Ingrediente ingrediente) {

        if (ingrediente == null) {
            throw new IllegalArgumentException(
                    "El ingrediente no puede ser nulo"
            );
        }

        if (ingrediente.getNombre() == null ||
                ingrediente.getNombre().isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre es obligatorio"
            );
        }

        if (ingrediente.getUnidadMedida() == null) {
            throw new IllegalArgumentException(
                    "La unidad de medida es obligatoria"
            );
        }

        validarCantidad(
                ingrediente.getStockActual(),
                "El stock actual"
        );

        validarCantidad(
                ingrediente.getStockMinimo(),
                "El stock mínimo"
        );

        validarCantidad(
                ingrediente.getCostoUnitario(),
                "El costo unitario"
        );
    }

    private void validarCantidad(BigDecimal valor, String campo) {

        if (valor == null || valor.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(
                    campo + " no puede ser nulo ni negativo"
            );
        }
    }
}