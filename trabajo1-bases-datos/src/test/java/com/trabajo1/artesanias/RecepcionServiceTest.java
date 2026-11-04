
package com.trabajo1.artesanias;

import com.trabajo1.artesanias.model.Recepcion;
import com.trabajo1.artesanias.model.DetalleVenta;
import com.trabajo1.artesanias.service.RecepcionService;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class RecepcionServiceTest {

    @Before
    public void limpiarDatos() throws Exception {
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/recepciones.json"), "[]");
        Files.writeString(Path.of("data/detalles-venta.json"), "[]");
        Files.writeString(Path.of("data/devoluciones.json"), "[]");
    }

    @Test
    public void debeRegistrarUnaRecepcion() {
        RecepcionService service = new RecepcionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 10, 1)
        );

        service.registrar(recepcion);

        assertEquals(1, service.listar().size());
    }

    @Test
    public void debeCalcularUnidadesDisponibles() {
        RecepcionService service = new RecepcionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 10, 1)
        );

        service.registrar(recepcion);

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                3, new BigDecimal("15.00")
        );

        service.registrarDetalleVenta(detalle);

        assertEquals(7, service.unidadesDisponibles("R001"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebePermitirVenderMasUnidadesDeLasDisponibles() {
        RecepcionService service = new RecepcionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 5,
                LocalDate.of(2026, 10, 1)
        );

        service.registrar(recepcion);

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                6, new BigDecimal("15.00")
        );

        service.registrarDetalleVenta(detalle);
    }
}
