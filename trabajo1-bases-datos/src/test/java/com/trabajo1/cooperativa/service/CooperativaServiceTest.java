
package com.trabajo1.cooperativa.service;

import com.trabajo1.cooperativa.model.*;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CooperativaServiceTest {

    private CooperativaService servicio;

    @Before
    public void configurar() {
        servicio = new CooperativaService();

        servicio.registrarSocio(
            new Socio("S001", "Ana Lopez", "1000000000")
        );

        servicio.registrarCuenta(
            new CuentaAhorros(
                "A001", "S001",
                new BigDecimal("100.00"))
        );

        servicio.registrarCuenta(
            new CuentaCorriente(
                "C001", "S001",
                new BigDecimal("0.00"))
        );
    }

    @Test
    public void depositoValido() {
        servicio.depositar("A001", new BigDecimal("20.00"));

        assertEquals(0, servicio.consultarSaldo("A001")
                .compareTo(new BigDecimal("120.00")));
    }

    @Test
    public void retiroRespetaSaldoMinimo() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.retirar(
                "A001", new BigDecimal("96.00")));

        assertEquals(0, servicio.consultarSaldo("A001")
                .compareTo(new BigDecimal("100.00")));
    }

    @Test
    public void transferenciaValida() {
        servicio.transferir(
            "A001", "C001", new BigDecimal("25.00"));

        assertEquals(0, servicio.consultarSaldo("A001")
                .compareTo(new BigDecimal("75.00")));

        assertEquals(0, servicio.consultarSaldo("C001")
                .compareTo(new BigDecimal("25.00")));
    }

    @Test
    public void transferenciaInvalidaNoCambiaDatos() {
        int movimientosAntes =
                servicio.listarMovimientos().size();

        assertThrows(IllegalArgumentException.class,
            () -> servicio.transferir(
                "A001", "C001", new BigDecimal("200.00")));

        assertEquals(0, servicio.consultarSaldo("A001")
                .compareTo(new BigDecimal("100.00")));

        assertEquals(0, servicio.consultarSaldo("C001")
                .compareTo(BigDecimal.ZERO));

        assertEquals(movimientosAntes,
                servicio.listarMovimientos().size());
    }

    @Test
    public void sobregiroMaximo() {
        servicio.retirar("C001", new BigDecimal("200.00"));

        assertEquals(0, servicio.consultarSaldo("C001")
                .compareTo(new BigDecimal("-200.00")));

        assertThrows(IllegalArgumentException.class,
            () -> servicio.retirar(
                "C001", new BigDecimal("0.01")));
    }

    @Test
    public void cierreCuentaConSaldoCero() {
        servicio.cerrarCuenta("C001");

        assertThrows(IllegalStateException.class,
            () -> servicio.depositar(
                "C001", new BigDecimal("10.00")));
    }

    @Test
    public void noPermitirTransferenciaMismaCuenta() {
        assertThrows(IllegalArgumentException.class,
            () -> servicio.transferir(
                "A001", "A001", new BigDecimal("10.00")));
    }

    @Test
    public void noDuplicarInteresesMensuales() {
        servicio.aplicarInteresMensual("A001");

        assertThrows(IllegalStateException.class,
            () -> servicio.aplicarInteresMensual("A001"));
    }
}
