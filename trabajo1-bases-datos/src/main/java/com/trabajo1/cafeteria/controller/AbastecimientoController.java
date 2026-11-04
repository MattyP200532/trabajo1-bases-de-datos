
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Abastecimiento;
import com.trabajo1.cafeteria.model.Ingrediente;
import com.trabajo1.cafeteria.repository.AbastecimientoRepository;
import com.trabajo1.cafeteria.service.AbastecimientoService;
import com.trabajo1.cafeteria.service.IngredienteService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class AbastecimientoController {

    private final AbastecimientoService abastecimientoService;
    private final IngredienteService ingredienteService;
    private final AbastecimientoRepository abastecimientoRepository;
    private final Scanner scanner;

    public AbastecimientoController(Scanner scanner) {
        this.scanner = scanner;
        this.abastecimientoService = new AbastecimientoService();
        this.ingredienteService = new IngredienteService();
        this.abastecimientoRepository = new AbastecimientoRepository();
    }

    public void registrarAbastecimiento() {

        try {
            System.out.println("\n--- REGISTRAR ABASTECIMIENTO ---");

            List<Ingrediente> ingredientes =
                    ingredienteService.obtenerTodos();

            if (ingredientes.isEmpty()) {
                System.out.println("No existen ingredientes registrados.");
                return;
            }

            System.out.println("\n--- INGREDIENTES DISPONIBLES ---");

            for (Ingrediente ingrediente : ingredientes) {
                System.out.println(
                        "ID: " + ingrediente.getIdIngrediente()
                        + " | Nombre: " + ingrediente.getNombre()
                        + " | Stock: " + ingrediente.getStockActual()
                        + " | Unidad: " + ingrediente.getUnidadMedida()
                );
            }

            System.out.print("\nIngrese el ID del ingrediente: ");
            int idIngrediente = Integer.parseInt(scanner.nextLine());

            Ingrediente ingrediente = ingredienteService.buscarPorId(idIngrediente)
                    .orElseThrow(() ->
                            new IllegalArgumentException("Ingrediente no encontrado")
                    );

            System.out.println("Ingrediente seleccionado: "
                    + ingrediente.getNombre());

            System.out.print("Cantidad que ingresa: ");
            BigDecimal cantidad = new BigDecimal(scanner.nextLine());

            System.out.print("Nuevo costo unitario: ");
            BigDecimal costoUnitario = new BigDecimal(scanner.nextLine());

            int idAbastecimiento =
                    abastecimientoRepository.obtenerSiguienteId();

            Abastecimiento abastecimiento = new Abastecimiento(
                    idAbastecimiento,
                    idIngrediente,
                    cantidad,
                    costoUnitario,
                    LocalDateTime.now()
            );

            abastecimientoService.registrarAbastecimiento(abastecimiento);

            System.out.println("\nAbastecimiento registrado correctamente.");
            System.out.println("ID del abastecimiento: " + idAbastecimiento);
            System.out.println("Ingrediente: " + ingrediente.getNombre());
            System.out.println("Cantidad ingresada: " + cantidad);
            System.out.println("Nuevo stock: "
                    + ingrediente.getStockActual().add(cantidad));

        } catch (NumberFormatException e) {
            System.out.println("Error: debe ingresar valores numéricos válidos.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public void listarAbastecimientos() {

        List<Abastecimiento> abastecimientos =
                abastecimientoService.obtenerTodos();

        System.out.println("\n--- LISTA DE ABASTECIMIENTOS ---");

        if (abastecimientos.isEmpty()) {
            System.out.println("No hay abastecimientos registrados.");
            return;
        }

        for (Abastecimiento abastecimiento : abastecimientos) {
            System.out.println(abastecimiento);
        }
    }
}
