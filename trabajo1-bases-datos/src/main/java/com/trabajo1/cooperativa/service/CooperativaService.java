
package com.trabajo1.cooperativa.service;

import com.trabajo1.cooperativa.model.*;
import java.math.BigDecimal;
import java.util.*;

public class CooperativaService {

    private final Map<String, Socio> socios =
            new LinkedHashMap<>();

    private final Map<String, Cuenta> cuentas =
            new LinkedHashMap<>();

    private final List<Movimiento> movimientos =
            new ArrayList<>();

    // REGISTRAR SOCIOS
    public void registrarSocio(Socio socio) {

        if (socio == null) {
            throw new IllegalArgumentException(
                    "El socio no puede ser nulo");
        }

        if (socios.containsKey(socio.getId())) {
            throw new IllegalArgumentException(
                    "El socio ya esta registrado");
        }

        for (Socio existente : socios.values()) {
            if (existente.getCedula()
                    .equals(socio.getCedula())) {
                throw new IllegalArgumentException(
                        "La cedula ya esta registrada");
            }
        }

        socios.put(socio.getId(), socio);
    }

    // REGISTRAR CUENTAS
    public void registrarCuenta(Cuenta cuenta) {

        if (cuenta == null) {
            throw new IllegalArgumentException(
                    "La cuenta no puede ser nula");
        }

        if (cuentas.containsKey(cuenta.getNumeroCuenta())) {
            throw new IllegalArgumentException(
                    "El numero de cuenta ya existe");
        }

        if (!socios.containsKey(cuenta.getIdSocio())) {
            throw new IllegalArgumentException(
                    "El socio no esta registrado");
        }

        if (!cuenta.isActiva()) {
            throw new IllegalArgumentException(
                    "No se puede registrar una cuenta inactiva");
        }

        cuentas.put(cuenta.getNumeroCuenta(), cuenta);

        // Registrar el saldo inicial para la auditoria.
        if (cuenta.getSaldo().signum() > 0) {
            registrarMovimiento(cuenta,
                    TipoMovimiento.DEPOSITO,
                    cuenta.getSaldo(), null);
        }
    }

    // DEPOSITAR DINERO
    public void depositar(String numeroCuenta,
                          BigDecimal monto) {

        Cuenta cuenta = buscarCuenta(numeroCuenta);
        cuenta.depositar(monto);

        registrarMovimiento(cuenta,
                TipoMovimiento.DEPOSITO,
                monto, null);
    }

    // RETIRAR DINERO
    public void retirar(String numeroCuenta,
                        BigDecimal monto) {

        Cuenta cuenta = buscarCuenta(numeroCuenta);
        cuenta.retirar(monto);

        registrarMovimiento(cuenta,
                TipoMovimiento.RETIRO,
                monto, null);
    }

    // TRANSFERIR ENTRE CUENTAS
    public void transferir(String origen,
                           String destino,
                           BigDecimal monto) {

        Cuenta cuentaOrigen = buscarCuenta(origen);
        Cuenta cuentaDestino = buscarCuenta(destino);

        // Valida ambas cuentas antes de cambiar saldos.
        cuentaOrigen.transferirA(cuentaDestino, monto);

        registrarMovimiento(cuentaOrigen,
                TipoMovimiento.TRANSFERENCIA_ENVIADA,
                monto, destino);

        registrarMovimiento(cuentaDestino,
                TipoMovimiento.TRANSFERENCIA_RECIBIDA,
                monto, origen);
    }

    // CONSULTAR SALDO
    public BigDecimal consultarSaldo(String numeroCuenta) {
        return buscarCuenta(numeroCuenta).getSaldo();
    }

    // CONSULTAR HISTORIAL
    public List<Movimiento> consultarMovimientos(
            String numeroCuenta) {

        buscarCuenta(numeroCuenta);

        List<Movimiento> resultado = new ArrayList<>();

        for (Movimiento movimiento : movimientos) {
            if (movimiento.getNumeroCuenta()
                    .equals(numeroCuenta)) {
                resultado.add(movimiento);
            }
        }

        return Collections.unmodifiableList(resultado);
    }

    // BUSCAR UNA CUENTA
    private Cuenta buscarCuenta(String numeroCuenta) {

        Cuenta cuenta = cuentas.get(numeroCuenta);

        if (cuenta == null) {
            throw new IllegalArgumentException(
                    "La cuenta no existe");
        }

        return cuenta;
    }

    // REGISTRAR UN MOVIMIENTO
    private void registrarMovimiento(
            Cuenta cuenta,
            TipoMovimiento tipo,
            BigDecimal monto,
            String cuentaRelacionada) {

        Movimiento movimiento = new Movimiento(
                cuenta.getNumeroCuenta(),
                tipo,
                monto,
                cuenta.getSaldo(),
                cuentaRelacionada);

        movimientos.add(movimiento);
    }

    public List<Socio> listarSocios() {
        return List.copyOf(socios.values());
    }

    public List<Cuenta> listarCuentas() {
        return List.copyOf(cuentas.values());
    }

    public List<Movimiento> listarMovimientos() {
        return List.copyOf(movimientos);
    }   
    
    
    public static CooperativaService desdeArchivo(
            List<Socio> sociosGuardados,
            List<Cuenta> cuentasGuardadas,
            List<Movimiento> movimientosGuardados) {

        if (sociosGuardados == null ||
                cuentasGuardadas == null ||
                movimientosGuardados == null) {
            throw new IllegalArgumentException(
                    "Los datos guardados estan incompletos");
        }

        CooperativaService servicio = new CooperativaService();

        // Recuperar socios
        for (Socio socio : sociosGuardados) {
            servicio.registrarSocio(socio);
        }

        // Recuperar cuentas sin crear depositos nuevos
        for (Cuenta cuenta : cuentasGuardadas) {
            if (cuenta == null ||
                    !servicio.socios.containsKey(cuenta.getIdSocio())) {
                throw new IllegalArgumentException(
                        "Cuenta sin socio valido");
            }

            if (servicio.cuentas.putIfAbsent(
                    cuenta.getNumeroCuenta(), cuenta) != null) {
                throw new IllegalArgumentException(
                        "Cuenta duplicada en el archivo");
            }
        }

        // Recuperar movimientos originales
        for (Movimiento movimiento : movimientosGuardados) {
            if (movimiento == null ||
                    !servicio.cuentas.containsKey(
                            movimiento.getNumeroCuenta())) {
                throw new IllegalArgumentException(
                        "Movimiento con cuenta invalida");
            }

            servicio.movimientos.add(movimiento);
        }

        return servicio;
    }


}
