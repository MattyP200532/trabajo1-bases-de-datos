
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.*;
import com.trabajo1.cafeteria.repository.*;
import com.trabajo1.cafeteria.service.RecetaService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class RecetaController {

    private final Scanner scanner;
    private final RecetaService recetaService;
    private final PlatoRepository platoRepository;
    private final IngredienteRepository ingredienteRepository;
    private final RecetaRepository recetaRepository;

    public RecetaController(Scanner scanner) {
        this.scanner = scanner;
        this.recetaService = new RecetaService();
        this.platoRepository = new PlatoRepository();
        this.ingredienteRepository = new IngredienteRepository();
        this.recetaRepository = new RecetaRepository();
    }

    public void registrarReceta() {
        try {
            System.out.println("\n--- REGISTRAR RECETA ---");

            List<Plato> platos = platoRepository.obtenerTodos();

            if (platos.isEmpty()) {
                System.out.println("Primero debe registrar un plato.");
                return;
            }

            System.out.println("\n--- PLATOS DISPONIBLES ---");

            for (Plato plato : platos) {
                if (plato.isActivo()) {
                    System.out.println(
                            "ID: " + plato.getIdPlato()
                            + " | Nombre: " + plato.getNombre());
                }
            }

            System.out.print("\nIngrese el ID del plato: ");
            int idPlato = Integer.parseInt(scanner.nextLine());

            Plato platoSeleccionado = platoRepository.buscarPorId(idPlato)
                    .orElseThrow(() ->
                            new IllegalArgumentException(
                                    "El plato no existe."));

            if (!platoSeleccionado.isActivo()) {
                throw new IllegalArgumentException(
                        "El plato está inactivo.");
            }

            if (recetaService.buscarPorPlato(idPlato).isPresent()) {
                throw new IllegalArgumentException(
                        "Este plato ya tiene una receta.");
            }

            List<Ingrediente> ingredientes =
                    ingredienteRepository.obtenerTodos();

            ingredientes.removeIf(i -> !i.isActivo());

            if (ingredientes.isEmpty()) {
                System.out.println(
                        "No existen ingredientes activos registrados.");
                return;
            }

            int siguienteId = recetaRepository.obtenerTodos()
                    .stream()
                    .mapToInt(Receta::getIdReceta)
                    .max()
                    .orElse(0) + 1;

            Receta receta = new Receta(siguienteId, idPlato);

            System.out.println("\n--- INGREDIENTES DISPONIBLES ---");

            for (Ingrediente ingrediente : ingredientes) {
                System.out.println(
                        "ID: " + ingrediente.getIdIngrediente()
                        + " | Nombre: " + ingrediente.getNombre()
                        + " | Unidad: " + ingrediente.getUnidadMedida());
            }

            String continuar;

            do {
                System.out.print("\nIngrese el ID del ingrediente: ");
                int idIngrediente =
                        Integer.parseInt(scanner.nextLine());

                System.out.print("Ingrese la cantidad necesaria: ");
                BigDecimal cantidad =
                        new BigDecimal(scanner.nextLine());

                boolean existe = ingredientes.stream()
                        .anyMatch(i ->
                                i.getIdIngrediente() == idIngrediente);

                if (!existe) {
                    throw new IllegalArgumentException(
                            "Ingrediente inexistente o inactivo.");
                }

                boolean repetido = receta.getDetalles()
                        .stream()
                        .anyMatch(d ->
                                d.getIdIngrediente() == idIngrediente);

                if (repetido) {
                    throw new IllegalArgumentException(
                            "No puede repetir un ingrediente.");
                }

                int idDetalle = receta.getDetalles().size() + 1;

                receta.agregarDetalle(new DetalleReceta(
                        idDetalle,
                        idIngrediente,
                        cantidad
                ));

                System.out.print(
                        "¿Desea agregar otro ingrediente? (S/N): ");
                continuar = scanner.nextLine().trim();

            } while (continuar.equalsIgnoreCase("S"));

            recetaService.guardar(receta);

            System.out.println("\n¡Receta registrada correctamente!");
            System.out.println("ID de receta: " + siguienteId);
            System.out.println("Plato: " + platoSeleccionado.getNombre());
            System.out.println("Cantidad de ingredientes: "
                    + receta.getDetalles().size());

        } catch (NumberFormatException e) {
            System.out.println("Error: ingrese un número válido.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al registrar receta: "
                    + e.getMessage());
        }
    }

    public void listarRecetas() {
        List<Receta> recetas = recetaService.obtenerTodas();

        System.out.println("\n--- LISTADO DE RECETAS ---");

        if (recetas.isEmpty()) {
            System.out.println("No existen recetas registradas.");
            return;
        }

        for (Receta receta : recetas) {
            Optional<Plato> plato =
                    platoRepository.buscarPorId(receta.getIdPlato());

            System.out.println("\n------------------------------");
            System.out.println("ID receta: " + receta.getIdReceta());
            System.out.println("ID plato: " + receta.getIdPlato());
            System.out.println("Plato: "
                    + plato.map(Plato::getNombre).orElse("No encontrado"));

            for (DetalleReceta detalle : receta.getDetalles()) {
                Optional<Ingrediente> ingrediente =
                        ingredienteRepository.buscarPorId(
                                detalle.getIdIngrediente());

                System.out.println(
                        "Ingrediente: "
                        + ingrediente.map(Ingrediente::getNombre)
                                .orElse("No encontrado")
                        + " | Cantidad: "
                        + detalle.getCantidadNecesaria());
            }
        }
    }
}
