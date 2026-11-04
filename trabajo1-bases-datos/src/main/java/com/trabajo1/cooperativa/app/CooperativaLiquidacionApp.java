
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;
import java.math.BigDecimal;

public class CooperativaLiquidacionApp {

    public static void main(String[] args) {

        CooperativaService servicio =
                new CooperativaService();

        servicio.registrarSocio(
                new Socio("S003", "Maria Lopez", "1000000002")
        );

        CuentaAhorros cuenta = new CuentaAhorros(
                "A003",
                "S003",
                new BigDecimal("100.00")
        );

        servicio.registrarCuenta(cuenta);

        System.out.println("PRUEBA DE LIQUIDACION");

        System.out.println("Saldo inicial: $" +
                servicio.consultarSaldo("A003"));

        BigDecimal montoEntregado =
                servicio.liquidarCuentaAhorros("A003");

        System.out.println("Monto entregado: $" +
                montoEntregado);

        System.out.println("Saldo final: $" +
                servicio.consultarSaldo("A003"));

        System.out.println("Cuenta activa: " +
                cuenta.isActiva());

        System.out.println("Movimientos registrados:");

        for (Movimiento movimiento :
                servicio.consultarMovimientos("A003")) {
            System.out.println(movimiento);
        }

        // Comprobar que no se pueda depositar
        // despues del cierre.
        try {
            servicio.depositar(
                    "A003", new BigDecimal("10.00"));

        } catch (IllegalStateException e) {
            System.out.println(
                    "Operacion rechazada: " + e.getMessage());
        }
    }
}
