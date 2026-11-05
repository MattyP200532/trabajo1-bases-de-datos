package com.trabajo1.transporte.reports;

import com.trabajo1.transporte.model.*;
import com.trabajo1.transporte.service.GestionTransporte;
import java.time.LocalDate;
import java.time.LocalDateTime;

public class ReporteTransporte {

    private GestionTransporte gestion;

    public ReporteTransporte(GestionTransporte gestion) {
        this.gestion = gestion;
    }

    // 1. Mapa de asientos
    public String mapaAsientos(int idViaje) {
        Viaje viaje = gestion.buscarViaje(idViaje);

        if (viaje == null)
            throw new IllegalArgumentException("Viaje no encontrado");

        String reporte = "MAPA DE ASIENTOS - " + viaje.getRuta() + "\n";

        for (int i = 1; i <= viaje.getCapacidad(); i++) {
            if (gestion.asientoOcupado(idViaje, i))
                reporte += String.format("[%02d:X] ", i);
            else
                reporte += String.format("[%02d: ] ", i);

            if (i % 4 == 0) reporte += "\n";
        }

        reporte += "\nX = Ocupado, espacio = Libre\n";
        reporte += "Asientos libres: " + gestion.asientosLibres(idViaje);
        return reporte;
    }

    // 2. Recaudacion por ruta
    public String recaudacionPorRuta(LocalDate inicio, LocalDate fin) {
        validarPeriodo(inicio, fin);

        String[] rutas = {
            "Ibarra-Quito", "Ibarra-Tulcán", "Ibarra-Otavalo"
        };

        String reporte = "RECAUDACION POR RUTA\n";

        for (String ruta : rutas) {
            double total = 0;

            for (Boleto boleto : gestion.getBoletos()) {
                Viaje viaje = gestion.buscarViaje(boleto.getIdViaje());

                if (viaje != null && viaje.getRuta().equalsIgnoreCase(ruta)
                    && "ACTIVO".equals(boleto.getEstado())) {

                    LocalDate fecha = LocalDateTime.parse(
                        boleto.getFechaVenta()).toLocalDate();

                    if (!fecha.isBefore(inicio) && !fecha.isAfter(fin))
                        total += boleto.getPrecio();
                }
            }

            reporte += ruta + ": $" + String.format("%.2f", total) + "\n";
        }

        return reporte;
    }

    // 3. Porcentaje de ocupacion
    public String porcentajeOcupacion() {
        String reporte = "PORCENTAJE DE OCUPACION\n";

        for (Viaje viaje : gestion.getViajes()) {
            int libres = gestion.asientosLibres(viaje.getId());
            int ocupados = viaje.getCapacidad() - libres;
            double porcentaje = ocupados * 100.0 / viaje.getCapacidad();

            reporte += "Viaje " + viaje.getId() + " - " + viaje.getRuta();
            reporte += ": " + String.format("%.2f", porcentaje) + "%\n";
        }

        return reporte;
    }

    // 4. Boletos anulados
    public String boletosAnulados(LocalDate inicio, LocalDate fin) {
        validarPeriodo(inicio, fin);

        String reporte = "BOLETOS ANULADOS\n";
        int cantidad = 0;

        for (Boleto boleto : gestion.getBoletos()) {
            if ("ANULADO".equals(boleto.getEstado())
                && boleto.getFechaAnulacion() != null) {

                LocalDate fecha = LocalDateTime.parse(
                    boleto.getFechaAnulacion()).toLocalDate();

                if (!fecha.isBefore(inicio) && !fecha.isAfter(fin)) {
                    reporte += "Boleto " + boleto.getId()
                        + " - Viaje " + boleto.getIdViaje()
                        + " - Asiento " + boleto.getAsiento() + "\n";
                    cantidad++;
                }
            }
        }

        reporte += "Total anulados: " + cantidad;
        return reporte;
    }

    // 5. Auditoria de asientos libres
    public String auditoriaAsientos() {
        String reporte = "AUDITORIA DE ASIENTOS\n";

        for (Viaje viaje : gestion.getViajes()) {
            boolean[] ocupados = new boolean[viaje.getCapacidad() + 1];
            int contador = 0;
            boolean correcto = true;

            for (Boleto boleto : gestion.getBoletos()) {
                if (boleto.getIdViaje() == viaje.getId()
                    && "ACTIVO".equals(boleto.getEstado())) {

                    int asiento = boleto.getAsiento();

                    if (asiento < 1 || asiento > viaje.getCapacidad()) {
                        correcto = false;
                    } else if (ocupados[asiento]) {
                        correcto = false;
                    } else {
                        ocupados[asiento] = true;
                        contador++;
                    }
                }
            }

            int libres = viaje.getCapacidad() - contador;

            if (libres != gestion.asientosLibres(viaje.getId()))
                correcto = false;

            reporte += "Viaje " + viaje.getId()
                + " | Libres: " + libres
                + " | Estado: " + (correcto ? "CORRECTO" : "REVISAR")
                + "\n";
        }

        for (Boleto boleto : gestion.getBoletos()) {
            if (gestion.buscarViaje(boleto.getIdViaje()) == null)
                reporte += "REVISAR: boleto " + boleto.getId()
                    + " sin viaje registrado\n";
        }

        return reporte;
    }

    // Verificar las fechas de los reportes
    private void validarPeriodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null || inicio.isAfter(fin))
            throw new IllegalArgumentException("Periodo incorrecto");
    }
}