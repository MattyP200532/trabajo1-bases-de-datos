
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Bebida;
import com.trabajo1.cafeteria.model.DetalleVentaBebidas;
import com.trabajo1.cafeteria.model.VentaBebidas;
import com.trabajo1.cafeteria.repository.BebidaRepository;
import com.trabajo1.cafeteria.repository.VentaBebidasRepository;
import com.trabajo1.cafeteria.service.BebidaService;
import com.trabajo1.cafeteria.service.VentaBebidasService;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class VentaBebidasController {

    private final VentaBebidasService ventaService;
    private final BebidaService bebidaService;
    private final BebidaRepository bebidaRepository;
    private final VentaBebidasRepository ventaRepository;
    private final Scanner scanner;

    public VentaBebidasController(Scanner scanner) {
        this.scanner = scanner;
        this.ventaService = new VentaBebidasService();
        this.bebidaService = new BebidaService();
        this.bebidaRepository = new BebidaRepository();
        this.ventaRepository = new VentaBebidasRepository();
    }

    public void registrarVenta() {

        try {
            List<Bebida> bebidas = bebidaRepository.obtenerTodos();

            System.out.println("\n--- BEBIDAS DISPONIBLES ---");

            boolean existenBebidas = false;

            for (Bebida bebida : bebidas) {
                if (bebida.isActiva()) {
                    System.out.println(
                            "ID: " + bebida.getIdBebida()
                            + " | Nombre: " + bebida.getNombre()
                            + " | Stock: " + bebida.getStockActual()
                            + " | Precio: $" + bebida.getPrecioFijo()
                    );
                    existenBebidas = true;
                }
            }

            if (!existenBebidas) {
                System.out.println("No existen bebidas activas.");
                return;
            }

            List<DetalleVentaBebidas> detalles = new ArrayList<>();

            int continuar;

            do {
                System.out.print("\nIngrese el ID de la bebida: ");
                int idBebida = Integer.parseInt(scanner.nextLine());

                Bebida bebida = bebidaRepository.buscarPorId(idBebida)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "No existe esa bebida."));

                if (!bebida.isActiva()) {
                    throw new IllegalArgumentException(
                            "La bebida está inactiva.");
                }

                System.out.println(
                        "Precio con IVA: $"
                        + bebidaService.calcularPrecioConIVA(idBebida)
                );

                System.out.print("Ingrese la cantidad: ");
                int cantidad = Integer.parseInt(scanner.nextLine());

                if (cantidad <= 0) {
                    throw new IllegalArgumentException(
                            "La cantidad debe ser mayor que cero.");
                }

                int idDetalle = detalles.size() + 1;

                detalles.add(new DetalleVentaBebidas(
                        idDetalle,
                        idBebida,
                        cantidad,
                        bebidaService.calcularPrecioConIVA(idBebida)
                ));

                System.out.print(
                        "¿Desea agregar otra bebida? (1 = Si, 0 = No): ");
                continuar = Integer.parseInt(scanner.nextLine());

                if (continuar != 0 && continuar != 1) {
                    throw new IllegalArgumentException(
                            "Debe ingresar 1 para continuar o 0 para finalizar.");
                }

            } while (continuar == 1);

            int idVenta = ventaRepository.obtenerTodos()
                    .stream()
                    .mapToInt(VentaBebidas::getIdVenta)
                    .max()
                    .orElse(0) + 1;

            VentaBebidas venta = ventaService.registrarVenta(
                    idVenta,
                    detalles
            );

            System.out.println("\n¡Venta de bebidas registrada correctamente!");
            System.out.println("ID de venta: " + venta.getIdVenta());
            System.out.println("Fecha: " + venta.getFecha());

            for (DetalleVentaBebidas detalle : venta.getDetalles()) {
                System.out.println(
                        "ID bebida: " + detalle.getIdBebida()
                        + " | Cantidad: " + detalle.getCantidad()
                        + " | Precio unitario con IVA: $"
                        + detalle.getPrecioUnitario()
                        + " | Subtotal: $" + detalle.getSubtotal()
                );
            }

            System.out.println("TOTAL: $" + venta.getTotal());

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

        List<VentaBebidas> ventas = ventaService.obtenerVentas();

        System.out.println("\n--- HISTORIAL DE VENTAS DE BEBIDAS ---");

        if (ventas.isEmpty()) {
            System.out.println("No existen ventas registradas.");
            return;
        }

        for (VentaBebidas venta : ventas) {
            System.out.println("--------------------------------");
            System.out.println("ID venta: " + venta.getIdVenta());
            System.out.println("Fecha: " + venta.getFecha());

            for (DetalleVentaBebidas detalle : venta.getDetalles()) {
                System.out.println(
                        "ID bebida: " + detalle.getIdBebida()
                        + " | Cantidad: " + detalle.getCantidad()
                        + " | Precio unitario: $"
                        + detalle.getPrecioUnitario()
                        + " | Subtotal: $" + detalle.getSubtotal()
                );
            }

            System.out.println("TOTAL: $" + venta.getTotal());
        }
    }
}
