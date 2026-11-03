package com.trabajo1.transporte.app;

import com.trabajo1.transporte.model.*;
import com.trabajo1.transporte.service.GestionTransporte;
import com.trabajo1.transporte.repository.RepositorioTransporte;
import com.trabajo1.transporte.reports.ReporteTransporte;

import java.time.LocalDate;
import java.util.Scanner;

public class MainTransporte {

    private static Scanner teclado = new Scanner(System.in);
    private static RepositorioTransporte archivos = new RepositorioTransporte();
    private static GestionTransporte gestion;
    private static ReporteTransporte reportes;

    public static void main(String[] args) {

        try {
            gestion = new GestionTransporte(
                archivos.cargarViajes(),
                archivos.cargarBoletos()
            );
        } catch (RuntimeException error) {
            System.out.println("Error al cargar datos: " + error.getMessage());
            return;
        }

        reportes = new ReporteTransporte(gestion);

        int opcion = -1;

        while (opcion != 0) {
            mostrarMenu();

            try {
                opcion = leerEntero("Seleccione una opcion: ");

                switch (opcion) {
                    case 1: registrarViaje(); break;
                    case 2: mostrarViajes(); break;
                    case 3: venderBoleto(); break;
                    case 4: anularBoleto(); break;
                    case 5: eliminarViaje(); break;
                    case 6: mostrarBoletos(); break;
                    case 7: mapaAsientos(); break;
                    case 8: recaudacion(); break;
                    case 9: System.out.println(reportes.porcentajeOcupacion()); break;
                    case 10: boletosAnulados(); break;
                    case 11: System.out.println(reportes.auditoriaAsientos()); break;
                    case 0: System.out.println("Programa finalizado"); break;
                    default: System.out.println("Opcion incorrecta");
                }

            } catch (RuntimeException error) {
                System.out.println("Error: " + error.getMessage());
            }
        }
    }

    // Mostrar el menu principal
    private static void mostrarMenu() {
        System.out.println("\n===== COOPERATIVA DE TRANSPORTE =====");
        System.out.println("1. Registrar viaje");
        System.out.println("2. Mostrar viajes");
        System.out.println("3. Vender boleto");
        System.out.println("4. Anular boleto");
        System.out.println("5. Eliminar viaje");
        System.out.println("6. Mostrar boletos");
        System.out.println("7. Mapa de asientos");
        System.out.println("8. Recaudacion por ruta");
        System.out.println("9. Porcentaje de ocupacion");
        System.out.println("10. Boletos anulados");
        System.out.println("11. Auditoria de asientos");
        System.out.println("0. Salir");
    }

    // Registrar un nuevo viaje
    private static void registrarViaje() {
        int id = leerEntero("ID del viaje: ");
        String ruta = leerTexto("Ruta: ");
        String fecha = leerTexto("Salida (AAAA-MM-DDTHH:MM): ");
        int capacidad = leerEntero("Capacidad: ");
        double tarifa = leerDecimal("Tarifa base: ");
        String tipo = leerTexto("Tipo (DIRECTO/CON_PARADAS): ");

        Viaje viaje = new Viaje(id, ruta, fecha, capacidad, tarifa, tipo);
        gestion.registrarViaje(viaje);

        archivos.guardarViajes(gestion.getViajes());
        System.out.println("Viaje registrado correctamente");
    }

    // Mostrar los viajes disponibles
    private static void mostrarViajes() {
        if (gestion.getViajes().isEmpty()) {
            System.out.println("No hay viajes registrados");
            return;
        }

        for (Viaje viaje : gestion.getViajes()) {
            System.out.println("\nViaje: " + viaje.getId());
            System.out.println("Ruta: " + viaje.getRuta());
            System.out.println("Salida: " + viaje.getFechaSalida());
            System.out.println("Tipo: " + viaje.getTipo());
            System.out.println("Tarifa: $" + viaje.getTarifaBase());
            System.out.println("Asientos libres: "
                + gestion.asientosLibres(viaje.getId()));
        }
    }

    // Vender un boleto
    private static void venderBoleto() {
        int idViaje = leerEntero("ID del viaje: ");
        String cedula = leerTexto("Cedula: ");
        String nombre = leerTexto("Nombre: ");
        String tipo = leerTexto("Pasajero (NORMAL/TERCERA_EDAD/DISCAPACIDAD): ");
        int asiento = leerEntero("Numero de asiento: ");

        Pasajero pasajero = new Pasajero(cedula, nombre, tipo);
        Boleto boleto = gestion.venderBoleto(idViaje, pasajero, asiento);

        archivos.guardarBoletos(gestion.getBoletos());

        System.out.println("Boleto vendido correctamente");
        System.out.println("ID del boleto: " + boleto.getId());
        System.out.println("Precio final: $" + boleto.getPrecio());
    }

    // Anular un boleto
    private static void anularBoleto() {
        int id = leerEntero("ID del boleto: ");
        gestion.anularBoleto(id);

        archivos.guardarBoletos(gestion.getBoletos());
        System.out.println("Boleto anulado correctamente");
    }

    // Eliminar un viaje
    private static void eliminarViaje() {
        int id = leerEntero("ID del viaje: ");
        gestion.eliminarViaje(id);

        archivos.guardarViajes(gestion.getViajes());
        System.out.println("Viaje eliminado correctamente");
    }

    // Mostrar todos los boletos
    private static void mostrarBoletos() {
        if (gestion.getBoletos().isEmpty()) {
            System.out.println("No hay boletos registrados");
            return;
        }

        for (Boleto boleto : gestion.getBoletos()) {
            System.out.println("\nBoleto: " + boleto.getId());
            System.out.println("Viaje: " + boleto.getIdViaje());
            System.out.println("Pasajero: " + boleto.getPasajero().getNombre());
            System.out.println("Cedula: " + boleto.getPasajero().getCedula());
            System.out.println("Asiento: " + boleto.getAsiento());
            System.out.println("Precio: $" + boleto.getPrecio());
            System.out.println("Estado: " + boleto.getEstado());
        }
    }

    // Reporte del mapa de asientos
    private static void mapaAsientos() {
        int id = leerEntero("ID del viaje: ");
        System.out.println(reportes.mapaAsientos(id));
    }

    // Reporte de recaudacion
    private static void recaudacion() {
        LocalDate inicio = leerFecha("Fecha inicial (AAAA-MM-DD): ");
        LocalDate fin = leerFecha("Fecha final (AAAA-MM-DD): ");

        System.out.println(reportes.recaudacionPorRuta(inicio, fin));
    }

    // Reporte de boletos anulados
    private static void boletosAnulados() {
        LocalDate inicio = leerFecha("Fecha inicial (AAAA-MM-DD): ");
        LocalDate fin = leerFecha("Fecha final (AAAA-MM-DD): ");

        System.out.println(reportes.boletosAnulados(inicio, fin));
    }

    // Leer un texto
    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return teclado.nextLine().trim();
    }

    // Leer un numero entero
    private static int leerEntero(String mensaje) {
        return Integer.parseInt(leerTexto(mensaje));
    }

    // Leer un numero decimal
    private static double leerDecimal(String mensaje) {
        return Double.parseDouble(leerTexto(mensaje).replace(',', '.'));
    }

    // Leer una fecha
    private static LocalDate leerFecha(String mensaje) {
        return LocalDate.parse(leerTexto(mensaje));
    }
}