
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Plato;
import com.trabajo1.cafeteria.model.Receta;
import com.trabajo1.cafeteria.model.DetalleReceta;
import com.trabajo1.cafeteria.model.Ingrediente;

import com.trabajo1.cafeteria.repository.PlatoRepository;
import com.trabajo1.cafeteria.repository.RecetaRepository;
import com.trabajo1.cafeteria.repository.IngredienteRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

public class PlatoService {

    private final PlatoRepository platoRepository;
    private final RecetaRepository recetaRepository;
    private final IngredienteRepository ingredienteRepository;

    public PlatoService() {
        this.platoRepository = new PlatoRepository();
        this.recetaRepository = new RecetaRepository();
        this.ingredienteRepository = new IngredienteRepository();
    }

    public PlatoService(
            PlatoRepository platoRepository,
            RecetaRepository recetaRepository,
            IngredienteRepository ingredienteRepository) {

        this.platoRepository = platoRepository;
        this.recetaRepository = recetaRepository;
        this.ingredienteRepository = ingredienteRepository;
    }

    public List<Plato> obtenerTodos() {
        return platoRepository.obtenerTodos();
    }

    public Optional<Plato> buscarPorId(int id) {

        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de plato inválido");
        }

        return platoRepository.buscarPorId(id);
    }

    public void guardar(Plato plato) {

        validarPlato(plato);
        platoRepository.guardar(plato);
    }

    public void actualizar(Plato plato) {

        validarPlato(plato);
        platoRepository.actualizar(plato);
    }

    public BigDecimal calcularCosto(int idPlato) {

        Receta receta = obtenerReceta(idPlato);

        BigDecimal costoTotal = BigDecimal.ZERO;

        for (DetalleReceta detalle : receta.getDetalles()) {

            Ingrediente ingrediente =
                    ingredienteRepository.buscarPorId(
                            detalle.getIdIngrediente())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Ingrediente no encontrado"));

            if (ingrediente.getCostoUnitario() == null) {
                throw new IllegalArgumentException(
                        "El ingrediente no tiene costo registrado");
            }

            BigDecimal costo = ingrediente.getCostoUnitario()
                    .multiply(detalle.getCantidadNecesaria());

            costoTotal = costoTotal.add(costo);
        }

        return costoTotal.setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calcularPrecioVenta(int idPlato) {

        return calcularCosto(idPlato)
                .multiply(new BigDecimal("2"))
                .setScale(2, RoundingMode.HALF_UP);
    }

    public int calcularUnidadesDisponibles(int idPlato) {

        Receta receta = obtenerReceta(idPlato);

        int unidadesPosibles = Integer.MAX_VALUE;

        for (DetalleReceta detalle : receta.getDetalles()) {

            Ingrediente ingrediente =
                    ingredienteRepository.buscarPorId(
                            detalle.getIdIngrediente())
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "Ingrediente no encontrado"));

            BigDecimal stock = ingrediente.getStockActual();
            BigDecimal necesario = detalle.getCantidadNecesaria();

            if (stock == null || stock.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException(
                        "El stock del ingrediente no es válido");
            }

            if (necesario == null
                    || necesario.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException(
                        "La cantidad necesaria no es válida");
            }

            int unidades = stock
                    .divideToIntegralValue(necesario)
                    .intValue();

            unidadesPosibles = Math.min(
                    unidadesPosibles, unidades);
        }

        return unidadesPosibles == Integer.MAX_VALUE
                ? 0 : unidadesPosibles;
    }

    private Receta obtenerReceta(int idPlato) {

        if (idPlato <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de plato inválido");
        }

        return recetaRepository.obtenerTodos()
                .stream()
                .filter(r -> r.getIdPlato() == idPlato)
                .findFirst()
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El plato no tiene receta registrada"));
    }

    private void validarPlato(Plato plato) {

        if (plato == null) {
            throw new IllegalArgumentException(
                    "El plato no puede ser nulo");
        }

        if (plato.getIdPlato() <= 0) {
            throw new IllegalArgumentException(
                    "Identificador de plato inválido");
        }
    }
}