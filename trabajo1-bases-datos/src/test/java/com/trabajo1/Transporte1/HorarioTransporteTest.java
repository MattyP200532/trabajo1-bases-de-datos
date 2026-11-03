
package com.trabajo1.Transporte1;

import com.trabajo1.transporte.model.*;
import com.trabajo1.transporte.service.GestionTransporte;
import java.time.LocalDateTime;
import org.junit.Test;
import static org.junit.Assert.*;

public class HorarioTransporteTest {

    @Test
    public void rechazarAnulacionFueraDeTiempo() {

        GestionTransporte gestion = new GestionTransporte();

        Viaje viaje = new Viaje(99, "Ibarra-Quito",
            LocalDateTime.now().plusHours(1).toString(),
            10, 10, "DIRECTO");

        gestion.registrarViaje(viaje);

        Pasajero pasajero = new Pasajero(
            "1000000007", "Prueba", "NORMAL");

        Boleto boleto = gestion.venderBoleto(99, pasajero, 1);

        try {
            gestion.anularBoleto(boleto.getId());
            fail("La anulacion debia rechazarse");

        } catch (IllegalArgumentException error) {
            assertTrue(error.getMessage().contains("2 horas"));
        }

        assertEquals("ACTIVO", boleto.getEstado());
    }

    @Test
    public void permitirAnulacionConAnticipacion() {

        GestionTransporte gestion = new GestionTransporte();

        Viaje viaje = new Viaje(100, "Ibarra-Quito",
            LocalDateTime.now().plusHours(3).toString(),
            10, 10, "DIRECTO");

        gestion.registrarViaje(viaje);

        Pasajero pasajero = new Pasajero(
            "1000000008", "Prueba", "NORMAL");

        Boleto boleto = gestion.venderBoleto(100, pasajero, 1);

        gestion.anularBoleto(boleto.getId());

        assertEquals("ANULADO", boleto.getEstado());
        assertEquals(10, gestion.asientosLibres(100));
    }
}
