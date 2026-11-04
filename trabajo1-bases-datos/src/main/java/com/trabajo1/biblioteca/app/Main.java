
package com.trabajo1.biblioteca.app;

import com.trabajo1.biblioteca.model.*;
import com.trabajo1.biblioteca.repository.BibliotecaRepository;
import com.trabajo1.biblioteca.service.PrestamoService;
import com.trabajo1.biblioteca.reports.ReporteBiblioteca;

import java.time.LocalDate;
import java.util.Map;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);

    private static void mostrarMenu() {

        System.out.println("\n==================================");
        System.out.println("       BIBLIOTECA UTN");
        System.out.println("==================================");
        System.out.println("1. Registrar préstamo");
        System.out.println("2. Registrar devolución");
        System.out.println("3. Consultar préstamos vencidos");
        System.out.println("4. Consultar historial de usuario");
        System.out.println("5. Consultar materiales más prestados");
        System.out.println("6. Consultar multas por período");
        System.out.println("7. Auditar ejemplares disponibles");
        System.out.println("8. Salir");
        System.out.println("----------------------------------");
        System.out.print("Seleccione una opción: ");
    }

    public static void main(String[] args) {

        // 1. Inicializar el repositorio
        BibliotecaRepository repositorio =
                new BibliotecaRepository();

        // 2. Cargar los datos desde JSON
        List<Usuario> usuarios =
                repositorio.cargarUsuarios();

        List<Libro> libros =
                repositorio.cargarLibros();

        List<Revista> revistas =
                repositorio.cargarRevistas();

        List<Ejemplar> ejemplares =
                repositorio.cargarEjemplares();

        List<Prestamo> prestamos =
                repositorio.cargarPrestamos();

        List<Multa> multas =
                repositorio.cargarMultas();

        // 3. Unificar libros y revistas
        List<Material> materiales = new ArrayList<>();

        materiales.addAll(libros);
        materiales.addAll(revistas);

        // 4. Inicializar el servicio de préstamos
        PrestamoService servicio = new PrestamoService(
                usuarios,
                materiales,
                ejemplares,
                prestamos,
                multas
        );

        ReporteBiblioteca reportes = new ReporteBiblioteca(prestamos, multas);
        
        boolean ejecutando = true;

        while (ejecutando) {

            mostrarMenu();

            String opcion = scanner.nextLine().trim();

            try {

                switch (opcion) {

                    case "1":
                        System.out.println("\n--- REGISTRAR PRÉSTAMO ---");

                        System.out.print("ID del usuario: ");
                        String usuarioId = scanner.nextLine().trim();

                        System.out.print("ID del ejemplar: ");
                        String ejemplarId = scanner.nextLine().trim();

                        System.out.print("Fecha del préstamo (AAAA-MM-DD): ");
                        LocalDate fechaPrestamo =
                                LocalDate.parse(scanner.nextLine().trim());

                        Prestamo nuevoPrestamo =
                                servicio.registrarPrestamo(
                                        usuarioId,
                                        ejemplarId,
                                        fechaPrestamo
                                );

                        repositorio.guardarPrestamos(prestamos);

                        System.out.println("\n¡Préstamo registrado correctamente!");
                        System.out.println("ID del préstamo: "
                                + nuevoPrestamo.getId());
                        System.out.println("Fecha de vencimiento: "
                                + nuevoPrestamo.getFechaVencimiento());

                        break;

                    case "2":
                        System.out.println("\n--- REGISTRAR DEVOLUCIÓN ---");

                        System.out.print("ID del préstamo: ");
                        String prestamoId = scanner.nextLine().trim();

                        System.out.print("Fecha de devolución (AAAA-MM-DD): ");
                        LocalDate fechaDevolucion =
                                LocalDate.parse(scanner.nextLine().trim());

                        Multa nuevaMulta = servicio.registrarDevolucion(
                                prestamoId,
                                fechaDevolucion
                        );

                        repositorio.guardarPrestamos(prestamos);
                        repositorio.guardarMultas(multas);

                        System.out.println("\n¡Devolución registrada correctamente!");

                        if (nuevaMulta != null) {
                            System.out.println("Se generó una multa de: $"
                                    + nuevaMulta.getMonto());
                        } else {
                            System.out.println("No se generaron multas.");
                        }

                        break;

                    case "3": 
                        System.out.println("\n--- PRÉSTAMOS VENCIDOS ---");

                        System.out.print("Fecha de consulta (AAAA-MM-DD): ");
                        LocalDate fechaConsulta =
                                LocalDate.parse(scanner.nextLine().trim());

                        List<Prestamo> vencidos =
                                reportes.obtenerPrestamosVencidos(fechaConsulta);

                        if (vencidos.isEmpty()) {
                            System.out.println("No hay préstamos vencidos.");
                        } else {
                            for (Prestamo p : vencidos) {
                                System.out.println("-------------------------");
                                System.out.println("Préstamo: " + p.getId());
                                System.out.println("Usuario: " + p.getUsuarioId());
                                System.out.println("Ejemplar: " + p.getEjemplarId());
                                System.out.println("Vencimiento: " + p.getFechaVencimiento());
                            }
                        }
                        break;

                    case "4": 
                        System.out.println("\n--- HISTORIAL DE PRÉSTAMOS ---");

                        System.out.print("ID del usuario: ");
                        String usuarioIdHistorial = scanner.nextLine().trim();

                        List<Prestamo> historial =
                                reportes.obtenerHistorialPrestamos(usuarioIdHistorial);

                        if (historial.isEmpty()) {
                            System.out.println("El usuario no tiene préstamos registrados.");
                        } else {
                            for (Prestamo p : historial) {
                                System.out.println("-------------------------");
                                System.out.println("Préstamo: " + p.getId());
                                System.out.println("Ejemplar: " + p.getEjemplarId());
                                System.out.println("Fecha de préstamo: " + p.getFechaPrestamo());
                                System.out.println("Fecha de vencimiento: " + p.getFechaVencimiento());
                                System.out.println("Fecha de devolución: " + p.getFechaDevolucion());
                                System.out.println("Estado: " + (p.estaActivo() ? "Activo" : "Devuelto"));
                            }
                        }
                        break;

                    case "5": 
                        System.out.println("\n--- MATERIALES MÁS PRESTADOS ---");

                        System.out.print("Cantidad de resultados que desea consultar: ");
                        int limite = Integer.parseInt(scanner.nextLine().trim());

                        Map<String, Integer> materialesMasPrestados =
                                reportes.obtenerMaterialesMasPrestados(ejemplares, limite);

                        if (materialesMasPrestados.isEmpty()) {
                            System.out.println("Todavía no hay préstamos registrados.");
                        } else {
                            for (Map.Entry<String, Integer> entrada : materialesMasPrestados.entrySet()) {
                                System.out.println("Material: " + entrada.getKey());
                                System.out.println("Cantidad de préstamos: " + entrada.getValue());
                                System.out.println("-------------------------");
                            }
                        }
                        break;

                    case "6": 
                        System.out.println("\n--- MULTAS POR PERÍODO ---");

                        System.out.print("Fecha inicial (AAAA-MM-DD): ");
                        LocalDate fechaInicio =
                                LocalDate.parse(scanner.nextLine().trim());

                        System.out.print("Fecha final (AAAA-MM-DD): ");
                        LocalDate fechaFin =
                                LocalDate.parse(scanner.nextLine().trim());

                        List<Multa> multasPeriodo =
                                reportes.obtenerMultasPorPeriodo(fechaInicio, fechaFin);

                        if (multasPeriodo.isEmpty()) {
                            System.out.println("No se encontraron multas en ese período.");
                        } else {
                            for (Multa m : multasPeriodo) {
                                System.out.println("-------------------------");
                                System.out.println("Préstamo: " + m.getPrestamoId());
                                System.out.println("Monto: $" + m.getMonto());
                                System.out.println("Fecha de generación: " + m.getFechaGeneracion());
                                System.out.println("Estado: " + (m.isPagada() ? "Pagada" : "Pendiente"));
                            }
                        }
                        break;

                    case "7": 
                        System.out.println("\n--- AUDITORÍA DE EJEMPLARES ---");

                        Map<String, Map<String, Integer>> auditoria =
                                reportes.auditarEjemplaresDisponibles(ejemplares);

                        if (auditoria.isEmpty()) {
                            System.out.println("No hay ejemplares registrados.");
                        } else {
                            for (Map.Entry<String, Map<String, Integer>> entrada : auditoria.entrySet()) {
                                System.out.println("-------------------------");
                                System.out.println("Material: " + entrada.getKey());
                                System.out.println("Total: " + entrada.getValue().get("total"));
                                System.out.println("Prestados: " + entrada.getValue().get("prestados"));
                                System.out.println("Disponibles: " + entrada.getValue().get("disponibles"));
                            }
                        }
                        break;
                    
                    case "8":
                        ejecutando = false;
                        System.out.println("\nCerrando el sistema...");
                        break;

                    default:
                        System.out.println(
                                "\nOpción no válida o todavía no implementada."
                        );
                        break;
                }

            } catch (Exception e) {

                System.out.println("\nError: " + e.getMessage());
            }
        }

        // 5. Mostrar información inicial
        System.out.println("==================================");
        System.out.println("     SISTEMA DE BIBLIOTECA UTN");
        System.out.println("==================================");

        System.out.println("Usuarios registrados: "
                + usuarios.size());

        System.out.println("Libros registrados: "
                + libros.size());

        System.out.println("Revistas registradas: "
                + revistas.size());

        System.out.println("Ejemplares registrados: "
                + ejemplares.size());

        System.out.println("Préstamos registrados: "
                + prestamos.size());

        System.out.println("Multas registradas: "
                + multas.size());

        System.out.println("----------------------------------");
        System.out.println("Sistema inicializado correctamente.");
    }
}
