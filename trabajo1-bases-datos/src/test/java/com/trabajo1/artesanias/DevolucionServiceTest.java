
package com.trabajo1.artesanias;

import com.trabajo1.artesanias.model.Recepcion;
import com.trabajo1.artesanias.model.Devolucion;
import com.trabajo1.artesanias.service.RecepcionService;
import com.trabajo1.artesanias.service.DevolucionService;

import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class DevolucionServiceTest {

    @Before
    public void limpiarDatos() throws Exception {
        Files.createDirectories(Path.of("data"));
        Files.writeString(Path.of("data/recepciones.json"), "[]");
        Files.writeString(Path.of("data/detalles-venta.json"), "[]");
        Files.writeString(Path.of("data/devoluciones.json"), "[]");
    }

    @Test
    public void debeRegistrarDevolucionDespuesDe90Dias() {
        RecepcionService recepcionService = new RecepcionService();
        DevolucionService devolucionService = new DevolucionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 1, 1)
        );

        recepcionService.registrar(recepcion);

        Devolucion devolucion = new Devolucion(
                "DEV001", "R001", "A001", 3,
                LocalDate.of(2026, 4, 1)
        );

        devolucionService.registrar(devolucion, recepcionService);

        assertEquals(1, devolucionService.listar().size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebePermitirDevolucionAntesDe90Dias() {
        RecepcionService recepcionService = new RecepcionService();
        DevolucionService devolucionService = new DevolucionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 1, 1)
        );

        recepcionService.registrar(recepcion);

        Devolucion devolucion = new Devolucion(
                "DEV001", "R001", "A001", 3,
                LocalDate.of(2026, 2, 1)
        );

        devolucionService.registrar(devolucion, recepcionService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebePermitirDevolverAOtroArtesano() {
        RecepcionService recepcionService = new RecepcionService();
        DevolucionService devolucionService = new DevolucionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 1, 1)
        );

        recepcionService.registrar(recepcion);

        Devolucion devolucion = new Devolucion(
                "DEV001", "R001", "A002", 3,
                LocalDate.of(2026, 4, 1)
        );

        devolucionService.registrar(devolucion, recepcionService);
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebePermitirDevolverMasUnidadesDisponibles() {
        RecepcionService recepcionService = new RecepcionService();
        DevolucionService devolucionService = new DevolucionService();

        Recepcion recepcion = new Recepcion(
                "R001", "A001", "AR001", 5,
                LocalDate.of(2026, 1, 1)
        );

        recepcionService.registrar(recepcion);

        Devolucion devolucion = new Devolucion(
                "DEV001", "R001", "A001", 6,
                LocalDate.of(2026, 4, 1)
        );

        devolucionService.registrar(devolucion, recepcionService);
    }
}
