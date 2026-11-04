
package com.trabajo1.Transporte1;

import com.trabajo1.transporte.model.*;
import com.trabajo1.transporte.service.GestionTransporte;
import com.trabajo1.transporte.reports.ReporteTransporte;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class AuditoriaTransporteTest {

    @Test
    public void detectarAsientosDuplicados() {

        Viaje viaje = new Viaje(99, "Ibarra-Quito",
            "2026-11-25T09:00:00", 10, 10, "DIRECTO");

        Pasajero pasajero1 = new Pasajero(
            "1000000009", "Juan", "NORMAL");

        Pasajero pasajero2 = new Pasajero(
            "1000000010", "Pedro", "NORMAL");

        Boleto boleto1 = new Boleto(
            1, 99, pasajero1, 5, 10, "2026-11-03T10:00:00");

        Boleto boleto2 = new Boleto(
            2, 99, pasajero2, 5, 10, "2026-11-03T10:05:00");

        GestionTransporte gestion = new GestionTransporte(
            List.of(viaje), List.of(boleto1, boleto2));

        ReporteTransporte reporte = new ReporteTransporte(gestion);

        String resultado = reporte.auditoriaAsientos();

        System.out.println(resultado);

        assertTrue(resultado.contains("Estado: REVISAR"));
    }
}
