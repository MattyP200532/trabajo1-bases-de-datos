
package com.trabajo1.cooperativa.model;

import java.math.BigDecimal;

public abstract class Cuenta {

    private final String numeroCuenta;
    private final String idSocio;
    private BigDecimal saldo;
    private boolean activa;

    public Cuenta(String numeroCuenta, String idSocio,
                  BigDecimal saldoInicial) {

        if (numeroCuenta == null || numeroCuenta.isBlank()
                || idSocio == null || idSocio.isBlank()
                || saldoInicial == null
                || saldoInicial.signum() < 0) {
            throw new IllegalArgumentException(
                "Datos de cuenta invalidos");
        }

        this.numeroCuenta = numeroCuenta;
        this.idSocio = idSocio;
        this.saldo = saldoInicial;
        this.activa = true;
    }


    protected Cuenta(String numeroCuenta,
                    String idSocio,
                    BigDecimal saldoGuardado,
                    boolean activa) {

        if (numeroCuenta == null || numeroCuenta.isBlank()
                || idSocio == null || idSocio.isBlank()
                || saldoGuardado == null) {
            throw new IllegalArgumentException(
                    "Datos de cuenta invalidos");
        }

        if (!activa &&
                saldoGuardado.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalArgumentException(
                    "Una cuenta inactiva debe tener saldo cero");
        }

        this.numeroCuenta = numeroCuenta;
        this.idSocio = idSocio;
        this.saldo = saldoGuardado;
        this.activa = activa;
    }


    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public String getIdSocio() {
        return idSocio;
    }

    public BigDecimal getSaldo() {
        return saldo;
    }

    public boolean isActiva() {
        return activa;
    }

    public void depositar(BigDecimal monto) {
        validarOperacion(monto);
        saldo = saldo.add(monto);
    }

    public void retirar(BigDecimal monto) {
        validarOperacion(monto);

        BigDecimal nuevoSaldo = saldo.subtract(monto);

        if (!saldoPermitido(nuevoSaldo)) {
            throw new IllegalArgumentException(
                "La operacion supera el saldo permitido");
        }

        saldo = nuevoSaldo;
    }

    public void cerrarCuenta() {
        if (!activa) {
            throw new IllegalStateException(
                "La cuenta ya esta inactiva");
        }

        if (saldo.compareTo(BigDecimal.ZERO) != 0) {
            throw new IllegalStateException(
                "Solo se pueden cerrar cuentas con saldo cero");
        }

        activa = false;
    }

    protected void validarOperacion(BigDecimal monto) {
        if (!activa) {
            throw new IllegalStateException(
                "La cuenta esta inactiva");
        }

        if (monto == null || monto.signum() <= 0) {
            throw new IllegalArgumentException(
                "El monto debe ser mayor que cero");
        }
    }

    protected abstract boolean saldoPermitido(
            BigDecimal nuevoSaldo);

    
    public void transferirA(Cuenta destino, BigDecimal monto) {

        if (destino == null) {
            throw new IllegalArgumentException(
                    "La cuenta destino no existe");
        }

        if (this == destino ||
                numeroCuenta.equals(destino.numeroCuenta)) {
            throw new IllegalArgumentException(
                    "No se puede transferir a la misma cuenta");
        }

        validarOperacion(monto);
        destino.validarOperacion(monto);

        BigDecimal nuevoSaldoOrigen = saldo.subtract(monto);

        if (!saldoPermitido(nuevoSaldoOrigen)) {
            throw new IllegalArgumentException(
                    "Saldo insuficiente para transferir");
        }

        BigDecimal nuevoSaldoDestino = destino.saldo.add(monto);

        if (!destino.saldoPermitido(nuevoSaldoDestino)) {
            throw new IllegalArgumentException(
                    "Saldo destino no permitido");
        }

        // Ambos saldos se modifican despues de validar todo.
        this.saldo = nuevoSaldoOrigen;
        destino.saldo = nuevoSaldoDestino;
    }

    public BigDecimal liquidarYCerrar() {

        if (!activa) {
            throw new IllegalStateException(
                    "La cuenta ya esta inactiva");
        }

        if (saldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalStateException(
                    "No se puede liquidar una cuenta con deuda");
        }

        BigDecimal montoLiquidado = saldo;

        // Se retira todo el dinero para el cierre definitivo.
        saldo = BigDecimal.ZERO;
        activa = false;

        return montoLiquidado;
    }

}
