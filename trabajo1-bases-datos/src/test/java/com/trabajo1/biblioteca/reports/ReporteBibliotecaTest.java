
package com.trabajo1.biblioteca.reports;

import com.trabajo1.biblioteca.model.Prestamo;
import com.trabajo1.biblioteca.model.Ejemplar;
import com.trabajo1.biblioteca.model.Multa;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Map;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class ReporteBibliotecaTest {

    @Test
    public void debeIdentificarPrestamosVencidos() {

        List<Prestamo> prestamos = new ArrayList<>();

        // Préstamo vencido y todavía activo.
        Prestamo vencido = new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 3), 7
        );

        // Préstamo cuyo vencimiento coincide con la fecha consultada.
        Prestamo vigente = new Prestamo(
                "P2", "U2", "E2",
                LocalDate.of(2026, 11, 8), 7
        );

        // Préstamo vencido, pero que ya fue devuelto.
        Prestamo devuelto = new Prestamo(
                "P3", "U3", "E3",
                LocalDate.of(2026, 11, 3), 7
        );
        devuelto.registrarDevolucion(LocalDate.of(2026, 11, 12));

        prestamos.add(vencido);
        prestamos.add(vigente);
        prestamos.add(devuelto);

        ReporteBiblioteca reporte = new ReporteBiblioteca(
            prestamos,
            new ArrayList<>()
            );

        List<Prestamo> resultado = reporte.obtenerPrestamosVencidos(
                LocalDate.of(2026, 11, 15)
        );

        assertEquals(1, resultado.size());
        assertEquals("P1", resultado.get(0).getId());
    }

    @Test
    public void noDebeMostrarPrestamosVencidosEnLaFechaDeConsulta() {

        List<Prestamo> prestamos = new ArrayList<>();

        Prestamo prestamo = new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 8), 7
        );

        prestamos.add(prestamo);

        ReporteBiblioteca reporte = new ReporteBiblioteca(
            prestamos,
            new ArrayList<>()
            );

        List<Prestamo> resultado = reporte.obtenerPrestamosVencidos(
                LocalDate.of(2026, 11, 15)
        );

        assertTrue(resultado.isEmpty());
    }

    @Test
    public void debeObtenerHistorialCompletoDeUnUsuario() {

        List<Prestamo> prestamos = new ArrayList<>();

        Prestamo activo = new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 3), 7
        );

        Prestamo devuelto = new Prestamo(
                "P2", "U1", "E2",
                LocalDate.of(2026, 11, 5), 7
        );
        devuelto.registrarDevolucion(LocalDate.of(2026, 11, 10));

        Prestamo otroUsuario = new Prestamo(
                "P3", "U2", "E3",
                LocalDate.of(2026, 11, 3), 7
        );

        prestamos.add(activo);
        prestamos.add(devuelto);
        prestamos.add(otroUsuario);

        ReporteBiblioteca reporte = new ReporteBiblioteca(
            prestamos,
            new ArrayList<>()
            );

        List<Prestamo> resultado =
                reporte.obtenerHistorialPrestamos("U1");

        assertEquals(2, resultado.size());
        assertEquals("P1", resultado.get(0).getId());
        assertEquals("P2", resultado.get(1).getId());

        assertTrue(resultado.get(0).estaActivo());
        assertFalse(resultado.get(1).estaActivo());
    }

    @Test
    public void debeDevolverListaVaciaSiElUsuarioNoTienePrestamos() {

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(),
                new ArrayList<>()
        );

        List<Prestamo> resultado =
                reporte.obtenerHistorialPrestamos("U1");

        assertTrue(resultado.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void debeRechazarIdentificadorDeUsuarioVacio() {

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(),
                new ArrayList<>()
        );

        reporte.obtenerHistorialPrestamos("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void debeRechazarFechaDeConsultaNula() {

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(),
                new ArrayList<>()
        );

        reporte.obtenerPrestamosVencidos(null);
    }

    @Test
    public void debeOrdenarMaterialesPorCantidadDePrestamos() {

        List<Prestamo> prestamos = new ArrayList<>();

        // El material L1 tendrá 3 préstamos.
        prestamos.add(new Prestamo(
                "P1", "U1", "E1", LocalDate.of(2026, 11, 1), 7
        ));
        prestamos.add(new Prestamo(
                "P2", "U2", "E1", LocalDate.of(2026, 11, 2), 7
        ));
        prestamos.add(new Prestamo(
                "P3", "U3", "E1", LocalDate.of(2026, 11, 3), 7
        ));

        // El material R1 tendrá 2 préstamos.
        prestamos.add(new Prestamo(
                "P4", "U1", "E2", LocalDate.of(2026, 11, 4), 3
        ));
        prestamos.add(new Prestamo(
                "P5", "U2", "E2", LocalDate.of(2026, 11, 5), 3
        ));

        List<Ejemplar> ejemplares = new ArrayList<>();
        ejemplares.add(new Ejemplar("E1", "L1"));
        ejemplares.add(new Ejemplar("E2", "R1"));

        ReporteBiblioteca reporte = new ReporteBiblioteca(
            prestamos,
            new ArrayList<>()
            );

        Map<String, Integer> resultado =
                reporte.obtenerMaterialesMasPrestados(ejemplares, 2);

        assertEquals(2, resultado.size());

        assertEquals(Integer.valueOf(3), resultado.get("L1"));
        assertEquals(Integer.valueOf(2), resultado.get("R1"));
    }

    @Test
    public void debeRespetarElLimiteDeMateriales() {

        List<Prestamo> prestamos = new ArrayList<>();

        prestamos.add(new Prestamo(
                "P1", "U1", "E1", LocalDate.of(2026, 11, 1), 7
        ));
        prestamos.add(new Prestamo(
                "P2", "U2", "E2", LocalDate.of(2026, 11, 2), 7
        ));
        prestamos.add(new Prestamo(
                "P3", "U3", "E3", LocalDate.of(2026, 11, 3), 7
        ));

        List<Ejemplar> ejemplares = new ArrayList<>();
        ejemplares.add(new Ejemplar("E1", "L1"));
        ejemplares.add(new Ejemplar("E2", "L2"));
        ejemplares.add(new Ejemplar("E3", "L3"));

        ReporteBiblioteca reporte = new ReporteBiblioteca(
            prestamos,
            new ArrayList<>()
            );

        Map<String, Integer> resultado =
                reporte.obtenerMaterialesMasPrestados(ejemplares, 2);

        assertEquals(2, resultado.size());
    }

    @Test(expected = IllegalArgumentException.class)
    public void debeRechazarLimiteInvalido() {

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(),
                new ArrayList<>()
        );

        reporte.obtenerMaterialesMasPrestados(new ArrayList<>(), 0);
    }

    @Test
    public void debeObtenerMultasDentroDelPeriodo() {
        List<Prestamo> prestamos = new ArrayList<>();

        List<Multa> multas = new ArrayList<>();
        multas.add(new Multa(
                "M1", "P1", new BigDecimal("0.75"),
                LocalDate.of(2026, 11, 5)
        ));
        multas.add(new Multa(
                "M2", "P2", new BigDecimal("1.00"),
                LocalDate.of(2026, 11, 10)
        ));
        multas.add(new Multa(
                "M3", "P3", new BigDecimal("0.50"),
                LocalDate.of(2026, 11, 20)
        ));

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                prestamos, multas
        );

        List<Multa> resultado = reporte.obtenerMultasPorPeriodo(
                LocalDate.of(2026, 11, 5),
                LocalDate.of(2026, 11, 10)
        );

        assertEquals(2, resultado.size());
    }

    @Test
    public void debeRetornarListaVaciaSiNoHayMultasEnElPeriodo() {
        List<Multa> multas = new ArrayList<>();
        multas.add(new Multa(
                "M1", "P1", new BigDecimal("0.75"),
                LocalDate.of(2026, 11, 5)
        ));

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(), multas
        );

        List<Multa> resultado = reporte.obtenerMultasPorPeriodo(
                LocalDate.of(2026, 11, 10),
                LocalDate.of(2026, 11, 15)
        );

        assertTrue(resultado.isEmpty());
    }

    @Test(expected = IllegalArgumentException.class)
    public void debeRechazarPeriodoConFechasInvertidas() {
        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(), new ArrayList<>()
        );

        reporte.obtenerMultasPorPeriodo(
                LocalDate.of(2026, 11, 15),
                LocalDate.of(2026, 11, 10)
        );
    }

    @Test
    public void debeAuditarEjemplaresDisponiblesCorrectamente() {

        List<Ejemplar> ejemplares = new ArrayList<>();

        ejemplares.add(new Ejemplar("E1", "L1"));
        ejemplares.add(new Ejemplar("E2", "L1"));
        ejemplares.add(new Ejemplar("E3", "L1"));
        ejemplares.add(new Ejemplar("E4", "R1"));
        ejemplares.add(new Ejemplar("E5", "R1"));

        List<Prestamo> prestamos = new ArrayList<>();

        Prestamo p1 = new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 1), 7
        );

        Prestamo p2 = new Prestamo(
                "P2", "U2", "E2",
                LocalDate.of(2026, 11, 1), 7
        );

        Prestamo p3 = new Prestamo(
                "P3", "U3", "E4",
                LocalDate.of(2026, 11, 1), 3
        );

        prestamos.add(p1);
        prestamos.add(p2);
        prestamos.add(p3);

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                prestamos, new ArrayList<>()
        );

        Map<String, Map<String, Integer>> resultado =
                reporte.auditarEjemplaresDisponibles(ejemplares);

        assertEquals(3, (int) resultado.get("L1").get("total"));
        assertEquals(2, (int) resultado.get("L1").get("prestados"));
        assertEquals(1, (int) resultado.get("L1").get("disponibles"));

        assertEquals(2, (int) resultado.get("R1").get("total"));
        assertEquals(1, (int) resultado.get("R1").get("prestados"));
        assertEquals(1, (int) resultado.get("R1").get("disponibles"));
    }

    @Test
    public void debeConsiderarDisponibleUnEjemplarDevuelto() {

        List<Ejemplar> ejemplares = new ArrayList<>();
        ejemplares.add(new Ejemplar("E1", "L1"));

        List<Prestamo> prestamos = new ArrayList<>();

        Prestamo prestamo = new Prestamo(
                "P1", "U1", "E1",
                LocalDate.of(2026, 11, 1), 7
        );

        prestamo.registrarDevolucion(
                LocalDate.of(2026, 11, 5)
        );

        prestamos.add(prestamo);

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                prestamos, new ArrayList<>()
        );

        Map<String, Map<String, Integer>> resultado =
                reporte.auditarEjemplaresDisponibles(ejemplares);

        assertEquals(0, (int) resultado.get("L1").get("prestados"));
        assertEquals(1, (int) resultado.get("L1").get("disponibles"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void debeRechazarListaDeEjemplaresNula() {

        ReporteBiblioteca reporte = new ReporteBiblioteca(
                new ArrayList<>(), new ArrayList<>()
        );

        reporte.auditarEjemplaresDisponibles(null);
    }
}
