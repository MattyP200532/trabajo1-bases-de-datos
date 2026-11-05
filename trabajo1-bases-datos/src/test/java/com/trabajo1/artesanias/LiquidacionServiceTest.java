
package com.trabajo1.artesanias;

import com.trabajo1.artesanias.model.*;
import com.trabajo1.artesanias.service.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LiquidacionServiceTest {

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
    public void debeCalcularComisionTextilDel20PorCiento() {
        ArtesaniaService artesaniaService = new ArtesaniaService();
        RecepcionService recepcionService = new RecepcionService();
        VentaService ventaService = new VentaService();
        LiquidacionService liquidacionService = new LiquidacionService();

        Artesania artesania = new Artesania(
                "AR001", "Mantel", TipoArtesania.TEXTIL,
                new BigDecimal("100.00"), "A001"
        );

        artesaniaService.registrar(artesania);

        recepcionService.registrar(new Recepcion(
                "R001", "A001", "AR001", 5,
                LocalDate.of(2026, 1, 1)
        ));

        Venta venta = new Venta(
                "V001", LocalDate.of(2026, 4, 5), BigDecimal.ZERO
        );

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                1, new BigDecimal("100.00")
        );

        ventaService.registrar(
                venta, Arrays.asList(detalle),
                recepcionService, artesaniaService
        );

        BigDecimal comision = liquidacionService.calcularComision(
                "A001",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                recepcionService.listarDetallesVenta(),
                artesaniaService.listar(),
                ventaService
        );

        assertEquals(new BigDecimal("20.0000"), comision);
    }

    @Test
    public void debeCalcularComisionTallaMaderaDel25PorCiento() {
        ArtesaniaService artesaniaService = new ArtesaniaService();
        RecepcionService recepcionService = new RecepcionService();
        VentaService ventaService = new VentaService();
        LiquidacionService liquidacionService = new LiquidacionService();

        Artesania artesania = new Artesania(
                "AR002", "Figura de madera", TipoArtesania.TALLA_MADERA,
                new BigDecimal("100.00"), "A002"
        );

        artesaniaService.registrar(artesania);

        recepcionService.registrar(new Recepcion(
                "R002", "A002", "AR002", 5,
                LocalDate.of(2026, 1, 1)
        ));

        Venta venta = new Venta(
                "V002", LocalDate.of(2026, 4, 5), BigDecimal.ZERO
        );

        DetalleVenta detalle = new DetalleVenta(
                "D002", "V002", "R002", "AR002",
                1, new BigDecimal("100.00")
        );

        ventaService.registrar(
                venta, Arrays.asList(detalle),
                recepcionService, artesaniaService
        );

        BigDecimal comision = liquidacionService.calcularComision(
                "A002",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                recepcionService.listarDetallesVenta(),
                artesaniaService.listar(),
                ventaService
        );

        assertEquals(new BigDecimal("25.0000"), comision);
    }

    @Test
    public void debeCalcularPagoNetoDelArtesano() {
        ArtesaniaService artesaniaService = new ArtesaniaService();
        RecepcionService recepcionService = new RecepcionService();
        VentaService ventaService = new VentaService();
        LiquidacionService liquidacionService = new LiquidacionService();

        artesaniaService.registrar(new Artesania(
                "AR001", "Mantel", TipoArtesania.TEXTIL,
                new BigDecimal("100.00"), "A001"
        ));

        recepcionService.registrar(new Recepcion(
                "R001", "A001", "AR001", 5,
                LocalDate.of(2026, 1, 1)
        ));

        Venta venta = new Venta(
                "V001", LocalDate.of(2026, 4, 5), BigDecimal.ZERO
        );

        DetalleVenta detalle = new DetalleVenta(
                "D001", "V001", "R001", "AR001",
                1, new BigDecimal("100.00")
        );

        ventaService.registrar(
                venta, Arrays.asList(detalle),
                recepcionService, artesaniaService
        );

        BigDecimal pago = liquidacionService.calcularPagoArtesano(
                "A001",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                recepcionService.listarDetallesVenta(),
                artesaniaService.listar(),
                ventaService
        );

        assertEquals(new BigDecimal("80.0000"), pago);
    }

    @Test
    public void debeRetornarCeroCuandoNoHayVentas() {
        LiquidacionService service = new LiquidacionService();
        VentaService ventaService = new VentaService();

        BigDecimal total = service.calcularTotalVendido(
                "A001",
                LocalDate.of(2026, 4, 1),
                LocalDate.of(2026, 4, 30),
                Collections.emptyList(),
                Collections.emptyList(),
                ventaService
        );

        assertEquals(BigDecimal.ZERO, total);
    }
}
