package com.trabajo1.transporte.service;

import com.trabajo1.transporte.model.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class GestionTransporte {

    private ArrayList<Viaje> viajes = new ArrayList<>();
    private ArrayList<Boleto> boletos = new ArrayList<>();

    public GestionTransporte() {
    }

    public GestionTransporte(List<Viaje> viajes, List<Boleto> boletos) {
        if (viajes != null) this.viajes.addAll(viajes);
        if (boletos != null) this.boletos.addAll(boletos);
    }

    // Registrar un viaje
    public void registrarViaje(Viaje viaje) {
        if (viaje == null || viaje.getId() <= 0)
            throw new IllegalArgumentException("Datos del viaje incorrectos");

        if (buscarViaje(viaje.getId()) != null)
            throw new IllegalArgumentException("El viaje ya existe");

        String ruta = viaje.getRuta();

        if (!"Ibarra-Quito".equalsIgnoreCase(ruta) &&
            !"Ibarra-Tulcán".equalsIgnoreCase(ruta) &&
            !"Ibarra-Otavalo".equalsIgnoreCase(ruta))
            throw new IllegalArgumentException("Ruta no permitida");

        if (viaje.getCapacidad() <= 0 ||
            !Double.isFinite(viaje.getTarifaBase()) ||
            viaje.getTarifaBase() <= 0)
            throw new IllegalArgumentException("Capacidad o tarifa incorrecta");

        String tipo = viaje.getTipo();
        
        if (!"DIRECTO".equalsIgnoreCase(tipo) &&
            !"CON_PARADAS".equalsIgnoreCase(tipo))
            throw new IllegalArgumentException("Tipo de viaje incorrecto");

        if (viaje.getFechaSalida() == null)
            throw new IllegalArgumentException("Fecha de salida obligatoria");

        LocalDateTime.parse(viaje.getFechaSalida());
        viajes.add(viaje);
    }

    // Buscar un viaje
    public Viaje buscarViaje(int id) {
        for (Viaje viaje : viajes) {
            if (viaje.getId() == id) return viaje;
        }
        return null;
    }

    // Buscar un boleto
    public Boleto buscarBoleto(int id) {
        for (Boleto boleto : boletos) {
            if (boleto.getId() == id) return boleto;
        }
        return null;
    }

    // Comprobar si un asiento esta ocupado
    public boolean asientoOcupado(int idViaje, int asiento) {
        for (Boleto boleto : boletos) {
            if (boleto.getIdViaje() == idViaje &&
                boleto.getAsiento() == asiento &&
                "ACTIVO".equals(boleto.getEstado()))
                return true;
        }
        return false;
    }

    // Calcular los asientos libres
    public int asientosLibres(int idViaje) {
        Viaje viaje = buscarViaje(idViaje);
        if (viaje == null)
            throw new IllegalArgumentException("Viaje no encontrado");

        int ocupados = 0;
        for (Boleto boleto : boletos) {
            if (boleto.getIdViaje() == idViaje &&
                "ACTIVO".equals(boleto.getEstado()))
                ocupados++;
        }
        return viaje.getCapacidad() - ocupados;
    }

    // Vender un boleto
    public Boleto venderBoleto(int idViaje, Pasajero pasajero, int asiento) {
        Viaje viaje = buscarViaje(idViaje);

        if (viaje == null)
            throw new IllegalArgumentException("Viaje no encontrado");

        if (!LocalDateTime.now().isBefore(
                LocalDateTime.parse(viaje.getFechaSalida())))
            throw new IllegalArgumentException("El viaje ya salio");

        if (asiento < 1 || asiento > viaje.getCapacidad())
            throw new IllegalArgumentException("Asiento fuera de rango");

        if (asientoOcupado(idViaje, asiento))
            throw new IllegalArgumentException("El asiento esta ocupado");

        if (pasajero == null || pasajero.getCedula() == null ||
            pasajero.getCedula().isBlank() || pasajero.getNombre() == null ||
            pasajero.getNombre().isBlank())
            throw new IllegalArgumentException("Datos del pasajero incorrectos");

        double precio = viaje.getTarifaBase();

        if ("CON_PARADAS".equalsIgnoreCase(viaje.getTipo()))
            precio = precio * 0.85;

        String tipo = pasajero.getTipo();
        if ("TERCERA_EDAD".equalsIgnoreCase(tipo) ||
            "DISCAPACIDAD".equalsIgnoreCase(tipo))
            precio = precio * 0.50;
        else if (!"NORMAL".equalsIgnoreCase(tipo))
            throw new IllegalArgumentException("Tipo de pasajero incorrecto");

        precio = Math.round(precio * 100.0) / 100.0;

        int nuevoId = 1;
        for (Boleto boleto : boletos) {
            if (boleto.getId() >= nuevoId)
                nuevoId = boleto.getId() + 1;
        }

        Boleto nuevo = new Boleto(nuevoId, idViaje, pasajero,
            asiento, precio, LocalDateTime.now().toString());

        boletos.add(nuevo);
        return nuevo;
    }

    // Anular un boleto
    public void anularBoleto(int idBoleto) {
        Boleto boleto = buscarBoleto(idBoleto);

        if (boleto == null)
            throw new IllegalArgumentException("Boleto no encontrado");

        if ("ANULADO".equals(boleto.getEstado()))
            throw new IllegalArgumentException("El boleto ya esta anulado");

        Viaje viaje = buscarViaje(boleto.getIdViaje());
        if (viaje == null)
            throw new IllegalArgumentException("Viaje no encontrado");

        LocalDateTime salida = LocalDateTime.parse(viaje.getFechaSalida());

        if (LocalDateTime.now().isAfter(salida.minusHours(2)))
            throw new IllegalArgumentException(
                "Solo se puede anular hasta 2 horas antes de la salida");

        boleto.anular(LocalDateTime.now().toString());
    }

    // Eliminar un viaje
    public void eliminarViaje(int idViaje) {
        Viaje viaje = buscarViaje(idViaje);

        if (viaje == null)
            throw new IllegalArgumentException("Viaje no encontrado");

        for (Boleto boleto : boletos) {
            if (boleto.getIdViaje() == idViaje)
                throw new IllegalArgumentException(
                    "No se puede eliminar un viaje con boletos vendidos");
        }
        viajes.remove(viaje);
    }

    public List<Viaje> getViajes() {
        return new ArrayList<>(viajes);
    }

    public List<Boleto> getBoletos() {
        return new ArrayList<>(boletos);
    }
}