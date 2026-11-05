
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.repository.CooperativaJsonRepository;
import com.trabajo1.cooperativa.reports.CooperativaReports;
import com.trabajo1.cooperativa.service.CooperativaService;

import java.math.BigDecimal;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.Scanner;

public class CooperativaMenuApp {

    private static final Scanner entrada = new Scanner(System.in);

    private static final Path ARCHIVO = Path.of(
            "data", "cooperativa", "estado.json");

    public static void main(String[] args) {

        CooperativaJsonRepository repositorio =
                new CooperativaJsonRepository();

        CooperativaService servicio;

        // Recuperar datos existentes, si los hay.
        try {
            if (Files.isRegularFile(ARCHIVO)) {
                servicio = repositorio.cargar();
                System.out.println(
                        "Datos recuperados correctamente.");
            } else {
                servicio = new CooperativaService();
                System.out.println(
                        "Iniciando cooperativa sin datos previos.");
            }
        } catch (RuntimeException e) {
            System.out.println(
                    "Error al cargar los datos: " + e.getMessage());
            return;
        }

        CooperativaReports reportes =
                new CooperativaReports();

        while (true) {

            mostrarMenu();
            String opcion = leer("Seleccione una opcion: ");
            boolean modificar = false;

            try {
                switch (opcion) {

                    case "1" -> {
                        Socio socio = new Socio(
                                leer("ID del socio: "),
                                leer("Nombre: "),
                                leer("Cedula: ")
                        );
                        servicio.registrarSocio(socio);
                        modificar = true;
                        System.out.println("Socio registrado.");
                    }

                    case "2", "3" -> {
                        String numero = leer("Numero de cuenta: ");
                        String idSocio = leer("ID del socio: ");
                        BigDecimal saldo = leerMonto(
                                "Saldo inicial: ", true);

                        Cuenta cuenta;

                        if (opcion.equals("2")) {
                            cuenta = new CuentaAhorros(
                                    numero, idSocio, saldo);
                        } else {
                            cuenta = new CuentaCorriente(
                                    numero, idSocio, saldo);
                        }

                        servicio.registrarCuenta(cuenta);
                        modificar = true;
                        System.out.println("Cuenta registrada.");
                    }

                    case "4" -> {
                        String numero = leer("Cuenta: ");
                        BigDecimal monto = leerMonto(
                                "Monto a depositar: ", false);
                        servicio.depositar(numero, monto);
                        modificar = true;
                        System.out.println("Deposito realizado.");
                    }

                    case "5" -> {
                        String numero = leer("Cuenta: ");
                        BigDecimal monto = leerMonto(
                                "Monto a retirar: ", false);
                        servicio.retirar(numero, monto);
                        modificar = true;
                        System.out.println("Retiro realizado.");
                    }

                    case "6" -> {
                        String origen = leer("Cuenta origen: ");
                        String destino = leer("Cuenta destino: ");
                        BigDecimal monto = leerMonto(
                                "Monto a transferir: ", false);

                        servicio.transferir(origen, destino, monto);
                        modificar = true;
                        System.out.println("Transferencia realizada.");
                    }

                    case "7" -> {
                        String numero = leer("Cuenta de ahorros: ");
                        servicio.aplicarInteresMensual(numero);
                        modificar = true;
                        System.out.println("Interes acreditado.");
                    }

                    case "8" -> {
                        String numero = leer("Cuenta a cerrar: ");
                        servicio.cerrarCuenta(numero);
                        modificar = true;
                        System.out.println("Cuenta cerrada.");
                    }

                    case "9" -> {
                        String numero = leer(
                                "Cuenta de ahorros a liquidar: ");

                        BigDecimal entregado =
                                servicio.liquidarCuentaAhorros(numero);

                        modificar = true;
                        System.out.println(
                                "Monto entregado: $" + entregado);
                    }

                    case "10" -> {
                        String numero = leer("Cuenta: ");
                        System.out.println("Saldo actual: $" +
                                servicio.consultarSaldo(numero));
                    }

                    case "11" -> {
                        String numero = leer("Cuenta: ");
                        reportes.estadoCuenta(servicio, numero);
                    }

                    case "12" -> {
                        YearMonth mes = YearMonth.parse(
                                leer("Mes (AAAA-MM): "));
                        reportes.interesesMensuales(servicio, mes);
                    }

                    case "13" -> {
                        reportes.cuentasEnSobregiro(servicio);
                    }

                    case "14" -> {
                        LocalDate desde = LocalDate.parse(
                                leer("Desde (AAAA-MM-DD): "));
                        LocalDate hasta = LocalDate.parse(
                                leer("Hasta (AAAA-MM-DD): "));

                        reportes.movimientosPorPeriodo(
                                servicio, desde, hasta);
                    }

                    case "15" -> {
                        reportes.auditoriaSaldos(servicio);
                    }

                    case "0" -> {
                        System.out.println("Saliendo del sistema.");
                        return;
                    }

                    default -> {
                        System.out.println("Opcion no valida.");
                    }
                }

                if (modificar) {
                    repositorio.guardar(servicio);
                    System.out.println(
                            "Cambios guardados en JSON.");
                }

            } catch (UncheckedIOException e) {
                System.out.println(
                        "ERROR al guardar los datos: "
                                + e.getMessage());
                System.out.println(
                        "Se detendra el programa para evitar "
                                + "mas cambios sin guardar.");
                return;

            } catch (RuntimeException e) {
                System.out.println(
                        "Operacion rechazada: " + e.getMessage());
            }
        }
    }

    private static void mostrarMenu() {

        System.out.println("\n================================");
        System.out.println("   COOPERATIVA DE AHORRO UTN");
        System.out.println("================================");
        System.out.println("1. Registrar socio");
        System.out.println("2. Crear cuenta de ahorros");
        System.out.println("3. Crear cuenta corriente");
        System.out.println("4. Depositar");
        System.out.println("5. Retirar");
        System.out.println("6. Transferir");
        System.out.println("7. Aplicar interes mensual");
        System.out.println("8. Cerrar cuenta con saldo cero");
        System.out.println("9. Liquidar cuenta de ahorros");
        System.out.println("10. Consultar saldo");
        System.out.println("11. Estado de cuenta");
        System.out.println("12. Reporte de intereses");
        System.out.println("13. Cuentas en sobregiro");
        System.out.println("14. Depositos y retiros");
        System.out.println("15. Auditoria de saldos");
        System.out.println("0. Salir");
        System.out.println("================================");
    }

    private static String leer(String mensaje) {
        System.out.print(mensaje);
        return entrada.nextLine().trim();
    }

    private static BigDecimal leerMonto(
            String mensaje, boolean permitirCero) {

        BigDecimal monto = new BigDecimal(leer(mensaje));

        if (monto.signum() < 0 ||
                (!permitirCero && monto.signum() == 0) ||
                monto.stripTrailingZeros().scale() > 2) {
            throw new IllegalArgumentException(
                    "Monto invalido. Use maximo dos decimales.");
        }

        return monto.setScale(2);
    }
}
