
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Plato;
import com.trabajo1.cafeteria.model.VentaPlatos;
import com.trabajo1.cafeteria.repository.PlatoRepository;
import com.trabajo1.cafeteria.repository.VentaPlatosRepository;
import com.trabajo1.cafeteria.service.VentaPlatosService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class VentaPlatosController {

    private final VentaPlatosService ventaService;
    private final PlatoRepository platoRepository;
    private final VentaPlatosRepository ventaRepository;
    private final Scanner scanner;

    public VentaPlatosController(Scanner scanner) {
        this.scanner = scanner;
        this.ventaService = new VentaPlatosService();
        this.platoRepository = new PlatoRepository();
        this.ventaRepository = new VentaPlatosRepository();
    }

    public void registrarVenta() {

        try {
            List<Plato> platos = platoRepository.obtenerTodos();

            System.out.println("\n--- PLATOS DISPONIBLES ---");

            boolean existenPlatos = false;

            for (Plato plato : platos) {
                if (plato.isActivo()) {
                    System.out.println(
                            "ID: " + plato.getIdPlato()
                            + " | Nombre: " + plato.getNombre()
                    );
                    existenPlatos = true;
                }
            }

            if (!existenPlatos) {
                System.out.println("No existen platos activos.");
                return;
            }

            Map<Integer, Integer> platosVendidos = new HashMap<>();

            int continuar;

            do {
                System.out.print("\nIngrese el ID del plato: ");
                int idPlato = Integer.parseInt(scanner.nextLine());

                Plato plato = platoRepository.buscarPorId(idPlato)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe ese plato."));

                if (!plato.isActivo()) {
                    throw new IllegalArgumentException(
                            "El plato está inactivo.");
                }

                System.out.print("Ingrese la cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                if (cantidad <= 0) {
                    throw new IllegalArgumentException(
                            "La cantidad debe ser mayor que cero.");
                }

                platosVendidos.merge(idPlato, cantidad, Integer::sum);

                System.out.print(
                        "¿Desea agregar otro plato? (1 = Si, 0 = No): ");
                continuar = Integer.parseInt(scanner.nextLine());

            } while (continuar == 1);

            int idVenta = ventaRepository.obtenerTodos()
                    .stream()
                    .mapToInt(VentaPlatos::getIdVenta)
                    .max()
                    .orElse(0) + 1;

            VentaPlatos venta = ventaService.registrarVenta(
                    idVenta,
                    platosVendidos
            );

            System.out.println("\n¡Venta registrada correctamente!");
            System.out.println("ID de venta: " + venta.getIdVenta());
            System.out.println("Fecha: " + venta.getFecha());
            System.out.println("Total: $" + venta.getTotal());

        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar un número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al registrar la venta: "
                    + e.getMessage());
        }
    }

    public void listarVentas() {

        List<VentaPlatos> ventas = ventaService.obtenerTodasLasVentas();

        System.out.println("\n--- HISTORIAL DE VENTAS DE PLATOS ---");

        if (ventas.isEmpty()) {
            System.out.println("No existen ventas registradas.");
            return;
        }

        for (VentaPlatos venta : ventas) {
            System.out.println("--------------------------------");
            System.out.println("ID venta: " + venta.getIdVenta());
            System.out.println("Fecha: " + venta.getFecha());

            for (var detalle : venta.getDetalles()) {
                System.out.println(
                        "ID plato: " + detalle.getIdPlato()
                        + " | Cantidad: " + detalle.getCantidad()
                        + " | Precio unitario: $" + detalle.getPrecioUnitario()
                        + " | Subtotal: $" + detalle.getSubtotal()
                );
            }

            System.out.println("TOTAL: $" + venta.getTotal());
        }
    }
}
