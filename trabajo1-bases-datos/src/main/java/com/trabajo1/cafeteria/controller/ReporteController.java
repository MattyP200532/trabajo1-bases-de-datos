
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.service.ReporteCafeteriaService;
import com.trabajo1.cafeteria.model.Ingrediente;
import com.trabajo1.cafeteria.model.MovimientoInventario;
import com.trabajo1.cafeteria.model.TipoMovimiento;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

public class ReporteController {

    private final Scanner scanner;
    private final ReporteCafeteriaService reporteService;

    public ReporteController(Scanner scanner) {
        this.scanner = scanner;
        this.reporteService = new ReporteCafeteriaService();
    }

    public void mostrarMenu() {

        int opcion;

        do {
            System.out.println("\n--- REPORTES DE CAFETERIA ---");
            System.out.println("1. Costo, precio y margen de platos");
            System.out.println("2. Ingredientes con stock bajo");
            System.out.println("3. Platos que se pueden preparar");
            System.out.println("4. Platos mas vendidos por periodo");
            System.out.println("5. Auditoria de stock");
            System.out.println("0. Volver al menu principal");
            System.out.print("Seleccione una opcion: ");

            try {
                opcion = Integer.parseInt(scanner.nextLine());

                switch (opcion) {
                    case 1:
                        mostrarCostoPrecioMargen();
                        break;
                    case 2:
                        mostrarStockBajo();
                        break;
                    case 3:
                        mostrarPlatosPreparables();
                        break;
                    case 4:
                        mostrarPlatosMasVendidos();
                        break;
                    case 5:
                        mostrarAuditoriaStock();
                        break;
                    case 0:
                        System.out.println("Volviendo al menu principal...");
                        break;
                    default:
                        System.out.println("Opcion no valida.");
                }

            } catch (NumberFormatException e) {
                opcion = -1;
                System.out.println("Debe ingresar un numero.");
            } catch (IllegalArgumentException e) {
                opcion = -1;
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);
    }

    private void mostrarCostoPrecioMargen() {

        System.out.println("\n--- COSTO, PRECIO Y MARGEN DE PLATOS ---");

        Map<String, Map<String, BigDecimal>> reporte =
                reporteService.reporteCostoPrecioMargen();

        if (reporte.isEmpty()) {
            System.out.println("No hay platos disponibles.");
            return;
        }

        reporte.forEach((nombre, valores) -> {
            System.out.println("\nPlato: " + nombre);
            System.out.println("Costo: $" + valores.get("costo"));
            System.out.println("Precio: $" + valores.get("precio"));
            System.out.println("Margen: $" + valores.get("margen"));
        });
    }

    private void mostrarStockBajo() {

        System.out.println("\n--- INGREDIENTES CON STOCK BAJO ---");

        List<Ingrediente> ingredientes =
                reporteService.reporteIngredientesStockBajo();

        if (ingredientes.isEmpty()) {
            System.out.println("No hay ingredientes con stock bajo.");
            return;
        }

        for (Ingrediente ingrediente : ingredientes) {
            System.out.println(
                    "Ingrediente: " + ingrediente.getNombre()
                    + " | Stock actual: " + ingrediente.getStockActual()
                    + " | Stock minimo: " + ingrediente.getStockMinimo()
            );
        }
    }

    private void mostrarPlatosPreparables() {

        System.out.println("\n--- PLATOS QUE SE PUEDEN PREPARAR ---");

        Map<String, Integer> reporte =
                reporteService.reportePlatosPreparables();

        if (reporte.isEmpty()) {
            System.out.println("No hay platos con recetas validas.");
            return;
        }

        reporte.forEach((nombre, cantidad) ->
                System.out.println(
                        "Plato: " + nombre
                        + " | Unidades posibles: " + cantidad
                )
        );
    }

    private void mostrarPlatosMasVendidos() {

        System.out.println("\n--- PLATOS MAS VENDIDOS ---");

        try {
            System.out.print(
                    "Fecha inicial (AAAA-MM-DDTHH:MM): ");
            LocalDateTime inicio =
                    LocalDateTime.parse(scanner.nextLine());

            System.out.print(
                    "Fecha final (AAAA-MM-DDTHH:MM): ");
            LocalDateTime fin =
                    LocalDateTime.parse(scanner.nextLine());

            Map<String, Integer> reporte =
                    reporteService.reportePlatosMasVendidos(
                            inicio, fin);

            if (reporte.isEmpty()) {
                System.out.println(
                        "No hay ventas de platos en ese periodo.");
                return;
            }

            reporte.forEach((nombre, cantidad) ->
                    System.out.println(
                            "Plato: " + nombre
                            + " | Cantidad vendida: " + cantidad
                    )
            );

        } catch (DateTimeParseException e) {
            System.out.println(
                    "Formato de fecha incorrecto. "
                    + "Ejemplo: 2026-11-04T00:00");
        }
    }

    private void mostrarAuditoriaStock() {

    System.out.println("\n--- AUDITORIA DE STOCK ---");

    Map<Ingrediente, List<MovimientoInventario>> reporte =
            reporteService.reporteAuditoriaStock();

    if (reporte.isEmpty()) {
        System.out.println("No hay ingredientes registrados.");
        return;
    }

    reporte.forEach((ingrediente, movimientos) -> {

        System.out.println("\nIngrediente: "
                + ingrediente.getNombre());

        System.out.println("Stock actual: "
                + ingrediente.getStockActual());

        System.out.println("\nHISTORIAL DE MOVIMIENTOS:");

        if (movimientos.isEmpty()) {
            System.out.println(
                    "No hay movimientos registrados para este ingrediente.");
            return;
        }

        for (MovimientoInventario movimiento : movimientos) {

            String signo = "";

            if (movimiento.getTipoMovimiento()
                    == TipoMovimiento.ABASTECIMIENTO) {
                signo = "+";
            } else if (movimiento.getTipoMovimiento()
                    == TipoMovimiento.VENTA_PLATO) {
                signo = "-";
            }

            System.out.println("--------------------------------");

            System.out.println("Fecha: "
                    + movimiento.getFecha());

            System.out.println("Tipo: "
                    + movimiento.getTipoMovimiento());

            System.out.println("Cantidad: "
                    + signo + movimiento.getCantidad());

            System.out.println("Descripcion: "
                    + movimiento.getDescripcion());
        }

        System.out.println("--------------------------------");
    });
}
}
