
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.MovimientoInventario;
import com.trabajo1.cafeteria.service.MovimientoInventarioService;

import java.util.List;
import java.util.Scanner;

public class MovimientoInventarioController {

    private final MovimientoInventarioService movimientoService;
    private final Scanner scanner;

    public MovimientoInventarioController(Scanner scanner) {
        this.scanner = scanner;
        this.movimientoService = new MovimientoInventarioService();
    }

    public void listarMovimientos() {

        List<MovimientoInventario> movimientos =
                movimientoService.obtenerTodos();

        System.out.println("\n--- HISTORIAL DE MOVIMIENTOS ---");

        if (movimientos.isEmpty()) {
            System.out.println("No existen movimientos registrados.");
            return;
        }

        for (MovimientoInventario movimiento : movimientos) {
            System.out.println("--------------------------------");
            System.out.println("ID movimiento: "
                    + movimiento.getIdMovimiento());
            System.out.println("ID ingrediente: "
                    + movimiento.getIdIngrediente());
            System.out.println("Tipo: "
                    + movimiento.getTipoMovimiento());
            System.out.println("Cantidad: "
                    + movimiento.getCantidad());
            System.out.println("Fecha: "
                    + movimiento.getFecha());
            System.out.println("Descripción: "
                    + movimiento.getDescripcion());
        }
    }

    public void buscarMovimientosPorIngrediente() {

        try {
            System.out.print("\nIngrese el ID del ingrediente: ");
            int idIngrediente =
                    Integer.parseInt(scanner.nextLine());

            List<MovimientoInventario> movimientos =
                    movimientoService.obtenerPorIngrediente(idIngrediente);

            System.out.println("\n--- MOVIMIENTOS DEL INGREDIENTE ---");

            if (movimientos.isEmpty()) {
                System.out.println(
                        "No existen movimientos para ese ingrediente."
                );
                return;
            }

            for (MovimientoInventario movimiento : movimientos) {
                System.out.println("--------------------------------");
                System.out.println("ID movimiento: "
                        + movimiento.getIdMovimiento());
                System.out.println("Tipo: "
                        + movimiento.getTipoMovimiento());
                System.out.println("Cantidad: "
                        + movimiento.getCantidad());
                System.out.println("Fecha: "
                        + movimiento.getFecha());
                System.out.println("Descripción: "
                        + movimiento.getDescripcion());
            }

        } catch (NumberFormatException e) {
    System.out.println("Debe ingresar un ID numérico.");
} catch (IllegalArgumentException e) {
    System.out.println(e.getMessage());
}
    }
}
