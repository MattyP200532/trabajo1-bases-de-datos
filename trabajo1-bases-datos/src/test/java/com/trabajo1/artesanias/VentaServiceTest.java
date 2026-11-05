
package com.trabajo1.artesanias;

import com.trabajo1.artesanias.model.*;
import com.trabajo1.artesanias.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class VentaServiceTest {

    @Before
    public void limpiarDatos() throws Exception {
        Files.createDirectories(Path.of("target/test-data/artesanias"));
        Files.writeString(Path.of("target/test-data/artesanias/artesanias.json"), "[]");
        Files.writeString(Path.of("target/test-data/artesanias/recepciones.json"), "[]");
        Files.writeString(Path.of("target/test-data/artesanias/detalles-venta.json"), "[]");
        Files.writeString(Path.of("target/test-data/artesanias/devoluciones.json"), "[]");
        Files.writeString(Path.of("target/test-data/artesanias/ventas.json"), "[]");
    }

    @Test
    public void debeRegistrarVentaYCalcularTotal() {
        ArtesaniaService artesaniaService = new ArtesaniaService();
        RecepcionService recepcionService = new RecepcionService();
        VentaService ventaService = new VentaService();

        Artesania artesania = new Artesania(
                "AR001", "Pulsera", TipoArtesania.TEXTIL,
                new BigDecimal("15.00"), "A001"
        );

        artesaniaService.registrar(artesania);

        recepcionService.registrar(new Recepcion(
                "R001", "A001", "AR001", 10,
                LocalDate.of(2026, 1, 1)
        ));

        Venta venta = new Venta(
                "V001", LocalDate.of(2026, 4, 5), BigDecimal.ZERO
        );

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                2, new BigDecimal("15.00")
        );

        ventaService.registrar(
                venta,
                java.util.Arrays.asList(detalle),
                recepcionService,
                artesaniaService
        );

        assertEquals(
                new BigDecimal("30.00"),
                ventaService.buscarPorId("V001").getTotal()
        );
    }

    @Test(expected = IllegalArgumentException.class)
    public void noDebePermitirVentaSinInventarioSuficiente() {
        ArtesaniaService artesaniaService = new ArtesaniaService();
        RecepcionService recepcionService = new RecepcionService();
        VentaService ventaService = new VentaService();

        artesaniaService.registrar(new Artesania(
                "AR001", "Pulsera", TipoArtesania.TEXTIL,
                new BigDecimal("15.00"), "A001"
        ));

        recepcionService.registrar(new Recepcion(
                "R001", "A001", "AR001", 3,
                LocalDate.of(2026, 1, 1)
        ));

        Venta venta = new Venta(
                "V001", LocalDate.of(2026, 4, 5), BigDecimal.ZERO
        );

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                5, new BigDecimal("15.00")
        );

        ventaService.registrar(
                venta,
                java.util.Arrays.asList(detalle),
                recepcionService,
                artesaniaService
        );
    }
}
