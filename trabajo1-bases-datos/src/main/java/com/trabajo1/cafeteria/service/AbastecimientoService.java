
package com.trabajo1.cafeteria.service;

import com.trabajo1.cafeteria.model.Abastecimiento;
import com.trabajo1.cafeteria.model.Ingrediente;
import com.trabajo1.cafeteria.model.MovimientoInventario;
import com.trabajo1.cafeteria.model.TipoMovimiento;
import com.trabajo1.cafeteria.repository.AbastecimientoRepository;
import com.trabajo1.cafeteria.repository.IngredienteRepository;
import com.trabajo1.cafeteria.repository.MovimientoInventarioRepository;

import java.math.BigDecimal;
import java.util.List;

public class AbastecimientoService {

    private final AbastecimientoRepository abastecimientoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final MovimientoInventarioRepository movimientoRepository;

    public AbastecimientoService() {
        this.abastecimientoRepository = new AbastecimientoRepository();
        this.ingredienteRepository = new IngredienteRepository();
        this.movimientoRepository = new MovimientoInventarioRepository();
    }

    public void registrarAbastecimiento(Abastecimiento abastecimiento) {

        validarAbastecimiento(abastecimiento);

        // Buscar el ingrediente
        Ingrediente ingrediente = ingredienteRepository
                .buscarPorId(abastecimiento.getIdIngrediente())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "El ingrediente no existe"
                        )
                );

        // Verificar si el ingrediente está activo
        if (!ingrediente.isActivo()) {
            throw new IllegalArgumentException(
                    "No se puede abastecer un ingrediente inactivo"
            );
        }

        // Calcular el nuevo stock
        BigDecimal nuevoStock = ingrediente.getStockActual()
                .add(abastecimiento.getCantidad());

        // Actualizar los datos del ingrediente
        ingrediente.setCostoUnitario(
                abastecimiento.getCostoUnitario()
        );

        ingrediente.setStockActual(nuevoStock);

        // Generar identificador independiente del movimiento
        int idMovimiento = movimientoRepository.obtenerSiguienteId();

        // Crear movimiento de inventario
        MovimientoInventario movimiento = new MovimientoInventario(
                idMovimiento,
                abastecimiento.getIdIngrediente(),
                TipoMovimiento.ABASTECIMIENTO,
                abastecimiento.getCantidad(),
                abastecimiento.getFecha(),
                "Ingreso por abastecimiento"
        );

        // Guardar abastecimiento
        abastecimientoRepository.guardar(abastecimiento);

        // Actualizar ingrediente
        ingredienteRepository.actualizar(ingrediente);

        // Guardar movimiento de inventario
        movimientoRepository.guardar(movimiento);
    }

    public List<Abastecimiento> obtenerTodos() {
        return abastecimientoRepository.obtenerTodos();
    }

    public Abastecimiento buscarPorId(int id) {
        return abastecimientoRepository.buscarPorId(id)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Abastecimiento no encontrado"
                        )
                );
    }

    public List<Abastecimiento> obtenerPorIngrediente(int idIngrediente) {
        return abastecimientoRepository.obtenerTodos()
                .stream()
                .filter(a ->
                        a.getIdIngrediente() == idIngrediente
                )
                .toList();
    }

    private void validarAbastecimiento(Abastecimiento abastecimiento) {

        if (abastecimiento == null) {
            throw new IllegalArgumentException(
                    "El abastecimiento no puede ser nulo"
            );
        }
            if (abastecimiento.getIdAbastecimiento() <= 0) {
        throw new IllegalArgumentException(
                "El identificador del abastecimiento debe ser positivo"
        );
    }

        if (abastecimiento.getCantidad() == null ||
                abastecimiento.getCantidad().compareTo(
                        BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "La cantidad debe ser mayor que cero"
            );
        }

        if (abastecimiento.getCostoUnitario() == null ||
                abastecimiento.getCostoUnitario().compareTo(
                        BigDecimal.ZERO) <= 0) {

            throw new IllegalArgumentException(
                    "El costo unitario debe ser mayor que cero"
            );
        }

        if (abastecimiento.getFecha() == null) {
            throw new IllegalArgumentException(
                    "La fecha es obligatoria"
            );
        }
    }
}
