
package com.trabajo1;

import com.trabajo1.artesanias.model.*;
import com.trabajo1.artesanias.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class AppArtesanias {

    private static final Scanner scanner = new Scanner(System.in);

    private static final ComunidadService comunidadService = new ComunidadService();
    private static final ArtesanoService artesanoService = new ArtesanoService();
    private static final ArtesaniaService artesaniaService = new ArtesaniaService();
    private static final RecepcionService recepcionService = new RecepcionService();
    private static final VentaService ventaService = new VentaService();
    private static final DevolucionService devolucionService = new DevolucionService();

    public static void main(String[] args) {

        int opcion;

        do {
            System.out.println("\n======================================");
            System.out.println(" SISTEMA DE CONSIGNACION DE ARTESANIAS");
            System.out.println("======================================");
            System.out.println("1. Registrar comunidad");
            System.out.println("2. Registrar artesano");
            System.out.println("3. Registrar artesania");
            System.out.println("4. Registrar recepcion");
            System.out.println("5. Registrar venta");
            System.out.println("6. Registrar devolucion");
            System.out.println("7. Listar comunidades");
            System.out.println("8. Listar artesanos");
            System.out.println("9. Listar artesanias");
            System.out.println("10. Listar recepciones");
            System.out.println("11. Listar ventas");
            System.out.println("12. Listar devoluciones");
            System.out.println("13. Consultar inventario");
            System.out.println("0. Salir");
            System.out.print("Seleccione una opcion: ");

            opcion = leerEntero();

            try {
                switch (opcion) {
                    case 1 -> registrarComunidad();
                    case 2 -> registrarArtesano();
                    case 3 -> registrarArtesania();
                    case 4 -> registrarRecepcion();
                    case 5 -> registrarVenta();
                    case 6 -> registrarDevolucion();
                    case 7 -> listarComunidades();
                    case 8 -> listarArtesanos();
                    case 9 -> listarArtesanias();
                    case 10 -> listarRecepciones();
                    case 11 -> listarVentas();
                    case 12 -> listarDevoluciones();
                    case 13 -> consultarInventario();
                    case 0 -> System.out.println("Gracias por utilizar el sistema.");
                    default -> System.out.println("Opcion no valida.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }

        } while (opcion != 0);

        // No cerrar System.in
    }

    private static void registrarComunidad() {
        System.out.println("\n--- REGISTRAR COMUNIDAD ---");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        comunidadService.registrar(new Comunidad(id, nombre));

        System.out.println("Comunidad registrada correctamente.");
    }

    private static void registrarArtesano() {
        System.out.println("\n--- REGISTRAR ARTESANO ---");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("ID de la comunidad: ");
        String comunidadId = scanner.nextLine();

        artesanoService.registrar(new Artesano(id, nombre, comunidadId));

        System.out.println("Artesano registrado correctamente.");
    }

    private static void registrarArtesania() {
        System.out.println("\n--- REGISTRAR ARTESANIA ---");

        System.out.print("ID: ");
        String id = scanner.nextLine();

        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.println("Tipo de artesania:");
        System.out.println("1. TEXTIL");
        System.out.println("2. TALLA_MADERA");
        System.out.print("Seleccione: ");

        int tipoOpcion = leerEntero();

        TipoArtesania tipo;

        if (tipoOpcion == 1) {
            tipo = TipoArtesania.TEXTIL;
        } else if (tipoOpcion == 2) {
            tipo = TipoArtesania.TALLA_MADERA;
        } else {
            throw new IllegalArgumentException("Tipo de artesania no valido.");
        }

        System.out.print("Precio de venta: ");
        BigDecimal precio = leerDecimal();

        System.out.print("ID del artesano: ");
        String artesanoId = scanner.nextLine();

        artesaniaService.registrar(
                new Artesania(id, nombre, tipo, precio, artesanoId)
        );

        System.out.println("Artesania registrada correctamente.");
    }

    private static void registrarRecepcion() {
        System.out.println("\n--- REGISTRAR RECEPCION ---");

        System.out.print("ID de recepcion: ");
        String id = scanner.nextLine();

        System.out.print("ID del artesano: ");
        String artesanoId = scanner.nextLine();

        System.out.print("ID de la artesania: ");
        String artesaniaId = scanner.nextLine();

        System.out.print("Cantidad recibida: ");
        int cantidad = leerEntero();

        System.out.print("Fecha (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        recepcionService.registrar(
                new Recepcion(id, artesanoId, artesaniaId, cantidad, fecha)
        );

        System.out.println("Recepcion registrada correctamente.");
    }

    private static void registrarVenta() {
        System.out.println("\n--- REGISTRAR VENTA ---");

        System.out.print("ID de venta: ");
        String ventaId = scanner.nextLine();

        System.out.print("ID del detalle: ");
        String detalleId = scanner.nextLine();

        System.out.print("ID de recepcion: ");
        String recepcionId = scanner.nextLine();

        System.out.print("ID de artesania: ");
        String artesaniaId = scanner.nextLine();

        System.out.print("Cantidad vendida: ");
        int cantidad = leerEntero();

        System.out.print("Precio unitario: ");
        BigDecimal precio = leerDecimal();

        System.out.print("Fecha de venta (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        Venta venta = new Venta(ventaId, fecha, BigDecimal.ZERO);

        DetalleVenta detalle = new DetalleVenta(
                detalleId,
                ventaId,
                recepcionId,
                artesaniaId,
                cantidad,
                precio
        );

        ventaService.registrar(
                venta,
                List.of(detalle),
                recepcionService,
                artesaniaService
        );

        System.out.println("Venta registrada correctamente.");
        System.out.println("Total: $" + venta.getTotal());
    }

    private static void registrarDevolucion() {
        System.out.println("\n--- REGISTRAR DEVOLUCION ---");

        System.out.print("ID de devolucion: ");
        String id = scanner.nextLine();

        System.out.print("ID de recepcion: ");
        String recepcionId = scanner.nextLine();

        System.out.print("ID del artesano propietario: ");
        String artesanoId = scanner.nextLine();

        System.out.print("Cantidad a devolver: ");
        int cantidad = leerEntero();

        System.out.print("Fecha de devolucion (AAAA-MM-DD): ");
        LocalDate fecha = LocalDate.parse(scanner.nextLine());

        Devolucion devolucion = new Devolucion(
                id, recepcionId, artesanoId, cantidad, fecha
        );

        devolucionService.registrar(devolucion, recepcionService);

        System.out.println("Devolucion registrada correctamente.");
    }

    private static void listarComunidades() {
        System.out.println("\n--- COMUNIDADES ---");

        for (Comunidad c : comunidadService.listar()) {
            System.out.println(c.getId() + " - " + c.getNombre());
        }
    }

    private static void listarArtesanos() {
        System.out.println("\n--- ARTESANOS ---");

        for (Artesano a : artesanoService.listar()) {
            System.out.println(
                    a.getId() + " - " + a.getNombre()
                    + " | Comunidad: " + a.getComunidadId()
            );
        }
    }

    private static void listarArtesanias() {
        System.out.println("\n--- ARTESANIAS ---");

        for (Artesania a : artesaniaService.listar()) {
            System.out.println(
                    a.getId() + " - " + a.getNombre()
                    + " | Tipo: " + a.getTipo()
                    + " | Precio: $" + a.getPrecioVenta()
                    + " | Artesano: " + a.getArtesanoId()
            );
        }
    }

    private static void listarRecepciones() {
        System.out.println("\n--- RECEPCIONES ---");

        for (Recepcion r : recepcionService.listar()) {
            System.out.println(
                    r.getId()
                    + " | Artesania: " + r.getArtesaniaId()
                    + " | Cantidad: " + r.getCantidad()
                    + " | Fecha: " + r.getFechaRecepcion()
            );
        }
    }

    private static void listarVentas() {
        System.out.println("\n--- VENTAS ---");

        for (Venta v : ventaService.listar()) {
            System.out.println(
                    v.getId() + " | Fecha: " + v.getFecha()
                    + " | Total: $" + v.getTotal()
            );
        }
    }

    private static void listarDevoluciones() {
        System.out.println("\n--- DEVOLUCIONES ---");

        for (Devolucion d : devolucionService.listar()) {
            System.out.println(
                    d.getId()
                    + " | Recepcion: " + d.getRecepcionId()
                    + " | Cantidad: " + d.getCantidad()
                    + " | Fecha: " + d.getFechaDevolucion()
            );
        }
    }

    private static void consultarInventario() {
        System.out.println("\n--- INVENTARIO DISPONIBLE ---");

        for (Recepcion r : recepcionService.listar()) {
            System.out.println(
                    "Recepcion " + r.getId()
                    + " | Artesania: " + r.getArtesaniaId()
                    + " | Unidades disponibles: "
                    + recepcionService.unidadesDisponibles(r.getId())
            );
        }
    }

    private static int leerEntero() {
        int valor = Integer.parseInt(scanner.nextLine().trim());
        return valor;
    }

    private static BigDecimal leerDecimal() {
        return new BigDecimal(scanner.nextLine().trim().replace(",", "."));
    }
}