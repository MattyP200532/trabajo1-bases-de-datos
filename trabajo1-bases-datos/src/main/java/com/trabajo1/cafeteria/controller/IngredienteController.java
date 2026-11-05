
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Ingrediente;
import com.trabajo1.cafeteria.model.UnidadMedida;
import com.trabajo1.cafeteria.service.IngredienteService;
import com.trabajo1.cafeteria.repository.IngredienteRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class IngredienteController {

    private final IngredienteService ingredienteService;
    private final IngredienteRepository ingredienteRepository;
    private final Scanner scanner;

    public IngredienteController(Scanner scanner) {
        this.scanner = scanner;
        this.ingredienteService = new IngredienteService();
        this.ingredienteRepository = new IngredienteRepository();
    }

    public void registrarIngrediente() {

        try {
            System.out.println("\n--- REGISTRAR INGREDIENTE ---");

            int id = ingredienteRepository.obtenerSiguienteId();

            System.out.println("ID asignado: " + id);

            System.out.print("Nombre del ingrediente: ");
            String nombre = scanner.nextLine();

            System.out.println("Unidad de medida:");
            System.out.println("1. KILOGRAMO");
            System.out.println("2. LITRO");
            System.out.print("Seleccione: ");

            int opcionUnidad = Integer.parseInt(scanner.nextLine());

            UnidadMedida unidad;

            switch (opcionUnidad) {
                case 1:
                    unidad = UnidadMedida.KILOGRAMO;
                    break;
                case 2:
                    unidad = UnidadMedida.LITRO;
                    break;
                default:
                    throw new IllegalArgumentException(
                            "Unidad de medida no válida"
                    );
            }

            System.out.print("Stock actual: ");
            BigDecimal stockActual =
                    new BigDecimal(scanner.nextLine());

            System.out.print("Stock mínimo: ");
            BigDecimal stockMinimo =
                    new BigDecimal(scanner.nextLine());

            System.out.print("Costo unitario: ");
            BigDecimal costoUnitario =
                    new BigDecimal(scanner.nextLine());

            Ingrediente ingrediente = new Ingrediente(
                    id,
                    nombre,
                    unidad,
                    stockActual,
                    stockMinimo,
                    costoUnitario
            );

            ingredienteService.registrarIngrediente(ingrediente);

            System.out.println("\nIngrediente registrado correctamente.");

        } catch (NumberFormatException e) {
            System.out.println("Error: ingrese valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarIngredientes() {

        List<Ingrediente> ingredientes =
                ingredienteService.obtenerTodos();

        System.out.println("\n--- LISTA DE INGREDIENTES ---");

        if (ingredientes.isEmpty()) {
            System.out.println("No hay ingredientes registrados.");
            return;
        }

        for (Ingrediente ingrediente : ingredientes) {
            System.out.println(ingrediente);
        }
    }
}
