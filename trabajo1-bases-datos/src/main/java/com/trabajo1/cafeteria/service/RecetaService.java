
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Receta;
import com.trabajo1.cafeteria.model.DetalleReceta;
import com.trabajo1.cafeteria.repository.RecetaRepository;
import com.trabajo1.cafeteria.repository.IngredienteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class RecetaService {

    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;

    public RecetaService() {
        this.recetaRepository = new RecetaRepository();
        this.ingredienteRepository = new IngredienteRepository();
    }

    public RecetaService(
            RecetaRepository recetaRepository,
            IngredienteRepository ingredienteRepository) {

        this.recetaRepository = recetaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<Receta> obtenerTodas() {
        return recetaRepository.obtenerTodos();
    }

    public Optional<Receta> buscarPorId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "El identificador debe ser mayor que cero");
        }

        return recetaRepository.buscarPorId(id);
    }

    public Optional<Receta> buscarPorPlato(int idPlato) {
        if (idPlato <= 0) {
            throw new IllegalArgumentException(
                    "El identificador del plato no es válido");
        }

        return recetaRepository.obtenerTodos()
                .stream()
                .filter(r -> r.getIdPlato() == idPlato)
                .findFirst();
    }

    public void guardar(Receta receta) {

        validarReceta(receta);

        if (buscarPorPlato(receta.getIdPlato()).isPresent()) {
            throw new IllegalArgumentException(
                    "Este plato ya tiene una receta registrada");
        }

        recetaRepository.guardar(receta);
    }

    public void actualizar(Receta receta) {

        validarReceta(receta);

        Optional<Receta> existente =
                buscarPorPlato(receta.getIdPlato());

        if (existente.isPresent()
                && existente.get().getIdReceta()
                != receta.getIdReceta()) {

            throw new IllegalArgumentException(
                    "El plato ya tiene otra receta registrada");
        }

        recetaRepository.actualizar(receta);
    }

    public void agregarIngrediente(
            int idReceta,
            int idIngrediente,
            BigDecimal cantidad) {

        Receta receta = buscarPorId(idReceta)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receta no encontrada"));

        validarIngrediente(idIngrediente, cantidad);

        boolean repetido = receta.getDetalles()
                .stream()
                .anyMatch(d ->
                        d.getIdIngrediente() == idIngrediente);

        if (repetido) {
            throw new IllegalArgumentException(
                    "El ingrediente ya existe en esta receta");
        }

        int nuevoId = receta.getDetalles()
                .stream()
                .mapToInt(DetalleReceta::getIdDetalleReceta)
                .max()
                .orElse(0) + 1;

        DetalleReceta detalle = new DetalleReceta(
                nuevoId,
                idIngrediente,
                cantidad
        );

        receta.agregarDetalle(detalle);

        recetaRepository.actualizar(receta);
    }

    public void eliminarIngrediente(
            int idReceta,
            int idDetalleReceta) {

        Receta receta = buscarPorId(idReceta)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Receta no encontrada"));

        int cantidadAnterior = receta.getDetalles().size();

        receta.eliminarDetalle(idDetalleReceta);

        if (receta.getDetalles().size() == cantidadAnterior) {
            throw new IllegalArgumentException(
                    "Detalle de receta no encontrado");
        }

        recetaRepository.actualizar(receta);
    }

    private void validarReceta(Receta receta) {

        if (receta == null) {
            throw new IllegalArgumentException(
                    "La receta no puede ser nula");
        }

        if (receta.getIdReceta() <= 0
                || receta.getIdPlato() <= 0) {

            throw new IllegalArgumentException(
                    "Los identificadores deben ser mayores que cero");
        }

        if (receta.getDetalles() == null
                || receta.getDetalles().isEmpty()) {

            throw new IllegalArgumentException(
                    "La receta debe tener al menos un ingrediente");
        }

        for (DetalleReceta detalle : receta.getDetalles()) {

            validarIngrediente(
                    detalle.getIdIngrediente(),
                    detalle.getCantidadNecesaria()
            );
        }
    }

    private void validarIngrediente(
            int idIngrediente,
            BigDecimal cantidad) {

        if (idIngrediente <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de ingrediente inválido");
        }

        if (cantidad == null
                || cantidad.compareTo(BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero");
        }

        if (ingredienteRepository.buscarPorId(
                idIngrediente).isEmpty()) {

            throw new IllegalArgumentException(
                    "El ingrediente no existe");
        }
    }
}