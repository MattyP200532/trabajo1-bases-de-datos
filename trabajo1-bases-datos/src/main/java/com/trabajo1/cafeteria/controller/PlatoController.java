
package com.trabajo1.cafeteria.controller;

import com.trabajo1.cafeteria.model.Plato;
import com.trabajo1.cafeteria.repository.PlatoRepository;

import java.util.List;
import java.util.Scanner;

public class PlatoController {

    private final PlatoRepository platoRepository;
    private final Scanner scanner;

    public PlatoController(Scanner scanner) {
        this.scanner = scanner;
        this.platoRepository = new PlatoRepository();
    }

    public void registrarPlato() {
        try {
            System.out.println("\n--- REGISTRAR PLATO ---");

            List<Plato> platos = platoRepository.obtenerTodos();

            int siguienteId = platos.stream()
                    .mapToInt(Plato::getIdPlato)
                    .max()
                    .orElse(0) + 1;

            System.out.println("ID del nuevo plato: " + siguienteId);

            System.out.print("Ingrese el nombre del plato: ");
            String nombre = scanner.nextLine().trim();

            if (nombre.isEmpty()) {
                throw new IllegalArgumentException(
                        "El nombre del plato no puede estar vacio.");
            }

            Plato plato = new Plato(siguienteId, nombre);

            platoRepository.guardar(plato);

            System.out.println("\n¡Plato registrado correctamente!");
            System.out.println(plato);

        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error al registrar el plato: "
                    + e.getMessage());
        }
    }

    public void listarPlatos() {
        List<Plato> platos = platoRepository.obtenerTodos();

        System.out.println("\n--- LISTADO DE PLATOS ---");

        if (platos.isEmpty()) {
            System.out.println("No existen platos registrados.");
            return;
        }

        for (Plato plato : platos) {
            System.out.println("------------------------------");
            System.out.println("ID: " + plato.getIdPlato());
            System.out.println("Nombre: " + plato.getNombre());
            System.out.println("Estado: "
                    + (plato.isActivo() ? "Activo" : "Inactivo"));
        }
    }
}
