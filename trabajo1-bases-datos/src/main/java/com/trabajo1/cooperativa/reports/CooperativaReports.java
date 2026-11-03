
package com.trabajo1.cooperativa.reports;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.LinkedHashMap;
import java.util.Map;

public class CooperativaReports {

    // REPORTE 1: ESTADO DE CUENTA
    public void estadoCuenta(CooperativaService servicio,
                             String numeroCuenta) {

        Cuenta cuenta = buscarCuenta(servicio, numeroCuenta);

        System.out.println("\n=== ESTADO DE CUENTA ===");
        System.out.println("Cuenta: " + numeroCuenta);
        System.out.println("Tipo: " +
                (cuenta instanceof CuentaAhorros
                        ? "AHORROS" : "CORRIENTE"));

        for (Movimiento m :
                servicio.consultarMovimientos(numeroCuenta)) {

            System.out.printf(
                    "%s | %s | Monto: $%s | Saldo: $%s%n",
                    m.getFechaHora(),
                    m.getTipo(),
                    m.getMonto(),
                    m.getSaldoPosterior()
            );
        }

        System.out.println("Saldo actual: $" +
                cuenta.getSaldo());
    }

    // REPORTE 2: INTERESES MENSUALES
    public void interesesMensuales(CooperativaService servicio,
                                   YearMonth mes) {

        System.out.println(
                "\n=== INTERESES DEL MES " + mes + " ===");

        for (Cuenta cuenta : servicio.listarCuentas()) {

            if (!(cuenta instanceof CuentaAhorros)) {
                continue;
            }

            BigDecimal total = BigDecimal.ZERO;

            for (Movimiento m :
                    servicio.consultarMovimientos(
                            cuenta.getNumeroCuenta())) {

                if (m.getTipo() == TipoMovimiento.INTERES &&
                        YearMonth.from(LocalDateTime.parse(
                                m.getFechaHora())).equals(mes)) {

                    total = total.add(m.getMonto());
                }
            }

            System.out.printf(
                    "Cuenta %s | Interes acreditado: $%s%n",
                    cuenta.getNumeroCuenta(),
                    total
            );
        }
    }

    // REPORTE 3: CUENTAS EN SOBREGIRO
    public void cuentasEnSobregiro(CooperativaService servicio) {

        System.out.println("\n=== CUENTAS EN SOBREGIRO ===");

        int cantidad = 0;

        for (Cuenta cuenta : servicio.listarCuentas()) {

            if (cuenta instanceof CuentaCorriente &&
                    cuenta.getSaldo().signum() < 0) {

                System.out.printf(
                        "Cuenta: %s | Saldo: $%s | Deuda: $%s%n",
                        cuenta.getNumeroCuenta(),
                        cuenta.getSaldo(),
                        cuenta.getSaldo().negate()
                );

                cantidad++;
            }
        }

        if (cantidad == 0) {
            System.out.println(
                    "No existen cuentas en sobregiro");
        }
    }

    // REPORTE 4: DEPOSITOS Y RETIROS POR PERIODO
    public void movimientosPorPeriodo(
            CooperativaService servicio,
            LocalDate desde,
            LocalDate hasta) {

        if (desde == null || hasta == null ||
                desde.isAfter(hasta)) {
            throw new IllegalArgumentException(
                    "Periodo de fechas invalido");
        }

        BigDecimal depositos = BigDecimal.ZERO;
        BigDecimal retiros = BigDecimal.ZERO;

        for (Movimiento m : servicio.listarMovimientos()) {

            LocalDate fecha = LocalDateTime.parse(
                    m.getFechaHora()).toLocalDate();

            if (fecha.isBefore(desde) ||
                    fecha.isAfter(hasta)) {
                continue;
            }

            if (m.getTipo() == TipoMovimiento.DEPOSITO) {
                depositos = depositos.add(m.getMonto());
            }

            if (m.getTipo() == TipoMovimiento.RETIRO) {
                retiros = retiros.add(m.getMonto());
            }
        }

        System.out.println(
                "\n=== DEPOSITOS Y RETIROS ===");
        System.out.println("Desde: " + desde);
        System.out.println("Hasta: " + hasta);
        System.out.println("Total depositado: $" + depositos);
        System.out.println("Total retirado: $" + retiros);
    }

    // REPORTE 5: AUDITORIA DE SALDOS
    public boolean auditoriaSaldos(CooperativaService servicio) {

        System.out.println("\n=== AUDITORIA DE SALDOS ===");

        Map<String, Cuenta> cuentas = new LinkedHashMap<>();
        Map<String, BigDecimal> saldos = new LinkedHashMap<>();

        for (Cuenta cuenta : servicio.listarCuentas()) {
            cuentas.put(cuenta.getNumeroCuenta(), cuenta);
            saldos.put(cuenta.getNumeroCuenta(),
                    BigDecimal.ZERO);
        }

        boolean correcto = true;

        for (Movimiento m : servicio.listarMovimientos()) {

            String numero = m.getNumeroCuenta();

            if (!cuentas.containsKey(numero)) {
                System.out.println(
                        "ERROR: movimiento de cuenta inexistente "
                                + numero);
                correcto = false;
                continue;
            }

            BigDecimal cambio = switch (m.getTipo()) {
                case DEPOSITO, TRANSFERENCIA_RECIBIDA,
                     INTERES -> m.getMonto();

                case RETIRO, TRANSFERENCIA_ENVIADA ->
                        m.getMonto().negate();
            };

            BigDecimal saldoCalculado =
                    saldos.get(numero).add(cambio);

            saldos.put(numero, saldoCalculado);

            if (saldoCalculado.compareTo(
                    m.getSaldoPosterior()) != 0) {

                System.out.println(
                        "ERROR en movimiento " + m.getId());
                correcto = false;
            }
        }

        for (Cuenta cuenta : servicio.listarCuentas()) {

            String numero = cuenta.getNumeroCuenta();
            BigDecimal calculado = saldos.get(numero);

            if (calculado.compareTo(cuenta.getSaldo()) == 0) {
                System.out.println(
                        "OK - Cuenta " + numero +
                        " | Saldo: $" + calculado);
            } else {
                System.out.println(
                        "ERROR - Cuenta " + numero +
                        " | Esperado: $" + calculado +
                        " | Registrado: $" + cuenta.getSaldo());
                correcto = false;
            }
        }

        System.out.println(correcto
                ? "AUDITORIA CORRECTA"
                : "AUDITORIA CON DIFERENCIAS");

        return correcto;
    }

    // METODO AUXILIAR
    private Cuenta buscarCuenta(CooperativaService servicio,
                                String numeroCuenta) {

        for (Cuenta cuenta : servicio.listarCuentas()) {

            if (cuenta.getNumeroCuenta()
                    .equals(numeroCuenta)) {
                return cuenta;
            }
        }

        throw new IllegalArgumentException(
                "La cuenta no existe");
    }
}
