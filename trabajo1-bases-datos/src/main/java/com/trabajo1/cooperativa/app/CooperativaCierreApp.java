
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;
import java.math.BigDecimal;

public class CooperativaCierreApp {

    public static void main(String[] args) {

        CooperativaService servicio =
                new CooperativaService();

        // Registrar socio de prueba
        Socio socio = new Socio(
                "S002",
                "Carlos Perez",
                "1000000001");

        servicio.registrarSocio(socio);

        // Cuenta corriente sin saldo
        CuentaCorriente corriente =
                new CuentaCorriente(
                        "C002",
                        "S002",
                        new BigDecimal("0.00"));

        servicio.registrarCuenta(corriente);

        // Cuenta de ahorros con saldo
        CuentaAhorros ahorros =
                new CuentaAhorros(
                        "A002",
                        "S002",
                        new BigDecimal("100.00"));

        servicio.registrarCuenta(ahorros);

        System.out.println("PRUEBA DE CIERRE DE CUENTAS");

        // PRUEBA 1: Cierre valido
        servicio.cerrarCuenta("C002");

        System.out.println(
                "Cuenta C002 cerrada correctamente");

        System.out.println(
                "Cuenta C002 activa: " + corriente.isActiva());

        // PRUEBA 2: Cierre con saldo positivo
        try {
            servicio.cerrarCuenta("A002");

        } catch (IllegalStateException e) {
            System.out.println(
                    "Cierre rechazado: " + e.getMessage());
        }

        // PRUEBA 3: Deposito en cuenta cerrada
        try {
            servicio.depositar(
                    "C002", new BigDecimal("50.00"));

        } catch (IllegalStateException e) {
            System.out.println(
                    "Deposito rechazado: " + e.getMessage());
        }

        // PRUEBA 4: Cerrar nuevamente la misma cuenta
        try {
            servicio.cerrarCuenta("C002");

        } catch (IllegalStateException e) {
            System.out.println(
                    "Segundo cierre rechazado: " + e.getMessage());
        }
    }
}
