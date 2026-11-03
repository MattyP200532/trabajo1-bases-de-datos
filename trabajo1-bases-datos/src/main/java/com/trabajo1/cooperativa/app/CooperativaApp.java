
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;
import java.math.BigDecimal;

public class CooperativaApp {

    public static void main(String[] args) {

        CooperativaService servicio =
                new CooperativaService();

        Socio socio = new Socio(
                "S001", "Ana Lopez", "1000000000");

        servicio.registrarSocio(socio);

        servicio.registrarCuenta(
                new CuentaAhorros(
                        "A001", "S001",
                        new BigDecimal("100.00")));

        servicio.registrarCuenta(
                new CuentaCorriente(
                        "C001", "S001",
                        new BigDecimal("0.00")));

        servicio.transferir(
                "A001", "C001",
                new BigDecimal("25.00"));

        System.out.println("Saldo ahorros: $" +
                servicio.consultarSaldo("A001"));

        System.out.println("Saldo corriente: $" +
                servicio.consultarSaldo("C001"));

        System.out.println("Movimientos de ahorros:");

        for (Movimiento m :
                servicio.consultarMovimientos("A001")) {
            System.out.println(m);
        }
    }
}
