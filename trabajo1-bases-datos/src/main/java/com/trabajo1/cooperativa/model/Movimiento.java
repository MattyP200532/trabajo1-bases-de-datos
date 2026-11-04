
package com.trabajo1.cooperativa.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Movimiento {

    private final String id;
    private final String numeroCuenta;
    private final TipoMovimiento tipo;
    private final BigDecimal monto;
    private final BigDecimal saldoPosterior;
    private final String fechaHora;
    private final String cuentaRelacionada;

    public Movimiento(
            String numeroCuenta,
            TipoMovimiento tipo,
            BigDecimal monto,
            BigDecimal saldoPosterior,
            String cuentaRelacionada) {

        if (numeroCuenta == null ||
                numeroCuenta.isBlank()) {
            throw new IllegalArgumentException(
                    "El numero de cuenta es obligatorio");
        }

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de movimiento es obligatorio");
        }

        if (monto == null ||
                monto.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException(
                    "El monto debe ser mayor que cero");
        }

        if (saldoPosterior == null) {
            throw new IllegalArgumentException(
                    "El saldo posterior es obligatorio");
        }

        this.id = UUID.randomUUID().toString();
        this.numeroCuenta = numeroCuenta;
        this.tipo = tipo;
        this.monto = monto;
        this.saldoPosterior = saldoPosterior;
        this.fechaHora = LocalDateTime.now().toString();
        this.cuentaRelacionada = cuentaRelacionada;
    }

    public String getId() {
        return id;
    }

    public String getNumeroCuenta() {
        return numeroCuenta;
    }

    public TipoMovimiento getTipo() {
        return tipo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public BigDecimal getSaldoPosterior() {
        return saldoPosterior;
    }

    public String getFechaHora() {
        return fechaHora;
    }

    public String getCuentaRelacionada() {
        return cuentaRelacionada;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "id='" + id + '\'' +
                ", cuenta='" + numeroCuenta + '\'' +
                ", tipo=" + tipo +
                ", monto=" + monto +
                ", saldoPosterior=" + saldoPosterior +
                ", fechaHora='" + fechaHora + '\'' +
                '}';
    }
}
