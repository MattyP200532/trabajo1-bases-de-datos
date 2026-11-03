
package com.trabajo1.cooperativa.repository;

import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;
import com.trabajo1.cooperativa.reports.CooperativaReports;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class CooperativaJsonRepositoryTest {

    @Rule
    public TemporaryFolder temporal =
            new TemporaryFolder();

    @Test
    public void guardarYRecuperarSinPerderInformacion()
            throws Exception {

        Path archivo = temporal.getRoot()
                .toPath()
                .resolve("estado.json");

        CooperativaJsonRepository repositorio =
                new CooperativaJsonRepository(archivo);

        CooperativaService original =
                new CooperativaService();

        original.registrarSocio(
                new Socio("S910", "Ana Prueba",
                        "1000000910"));

        original.registrarCuenta(
                new CuentaAhorros(
                        "A910", "S910",
                        new BigDecimal("100.00")));

        original.registrarCuenta(
                new CuentaCorriente(
                        "C910", "S910",
                        BigDecimal.ZERO));

        // Realizar una transferencia
        original.transferir(
                "A910", "C910",
                new BigDecimal("20.00"));

        // Acreditar interes mensual
        original.aplicarInteresMensual("A910");

        // Guardar los datos
        repositorio.guardar(original);

        assertTrue(Files.exists(archivo));

        // Recuperar desde el archivo JSON
        CooperativaService recuperado =
                repositorio.cargar();

        // Comprobar saldos
        assertEquals(0,
                recuperado.consultarSaldo("A910")
                        .compareTo(new BigDecimal("80.27")));

        assertEquals(0,
                recuperado.consultarSaldo("C910")
                        .compareTo(new BigDecimal("20.00")));

        // Comprobar que se conservaron movimientos
        assertEquals(4,
                recuperado.listarMovimientos().size());

        // Comprobar auditoria
        CooperativaReports reportes =
                new CooperativaReports();

        assertTrue(reportes.auditoriaSaldos(recuperado));

        // Volver a cargar no debe duplicar movimientos
        CooperativaService segundaCarga =
                repositorio.cargar();

        assertEquals(4,
                segundaCarga.listarMovimientos().size());
    }
}
