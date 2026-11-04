
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Bebida;
import com.trabajo1.cafeteria.service.BebidaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class BebidaController {

    private final BebidaService bebidaService;
    private final Scanner scanner;

    public BebidaController(Scanner scanner) {
        this.scanner = scanner;
        this.bebidaService = new BebidaService();
    }

    public void registrarBebida() {

        try {
            List<Bebida> bebidas = bebidaService.obtenerTodas();

            int idBebida = bebidas.stream()
                    .mapToInt(Bebida::getIdBebida)
                    .max()
                    .orElse(0) + 1;

            System.out.println("\n--- REGISTRAR BEBIDA ---");

            System.out.print("Ingrese el nombre de la bebida: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese el stock inicial (unidades): ");
            int stock = Integer.parseInt(scanner.nextLine());

            System.out.print("Ingrese el precio fijo: ");
            BigDecimal precio = new BigDecimal(scanner.nextLine());

            Bebida bebida = new Bebida(
                    idBebida,
                    nombre,
                    stock,
                    precio
            );

            bebidaService.registrarBebida(bebida);

            System.out.println("\n¡Bebida registrada correctamente!");
            System.out.println("ID: " + bebida.getIdBebida());
            System.out.println("Nombre: " + bebida.getNombre());
            System.out.println("Stock: " + bebida.getStockActual());
            System.out.println("Precio fijo: $" + bebida.getPrecioFijo());
            System.out.println(
                    "Precio con IVA (15 %): $"
                    + bebidaService.calcularPrecioConIVA(idBebida)
            );

        } catch (NumberFormatException e) {
            System.out.println(
                    "Error: debe ingresar valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(
                    "Error al registrar la bebida: " + e.getMessage());
        }
    }

    public void listarBebidas() {

        List<Bebida> bebidas = bebidaService.obtenerTodas();

        System.out.println("\n--- LISTA DE BEBIDAS ---");

        if (bebidas.isEmpty()) {
            System.out.println("No existen bebidas registradas.");
            return;
        }

        for (Bebida bebida : bebidas) {
            System.out.println("--------------------------------");

            System.out.println("ID: " + bebida.getIdBebida());
            System.out.println("Nombre: " + bebida.getNombre());
            System.out.println("Stock: " + bebida.getStockActual());
            System.out.println("Precio fijo: $" + bebida.getPrecioFijo());
            System.out.println(
                    "Precio con IVA: $"
                    + bebidaService.calcularPrecioConIVA(
                            bebida.getIdBebida())
            );
            System.out.println(
                    "Estado: " + (bebida.isActiva()
                            ? "Activa" : "Inactiva")
            );
        }
    }

    public void desactivarBebida() {

        try {
            listarBebidas();

            System.out.print("\nIngrese el ID de la bebida: ");
            int idBebida = Integer.parseInt(scanner.nextLine());

            bebidaService.desactivarBebida(idBebida);

            System.out.println("Bebida desactivada correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar un ID numérico.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println(
                    "Error al desactivar la bebida: " + e.getMessage());
        }
    }
}
