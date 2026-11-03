
package com.trabajo1.cooperativa.model;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class CuentaAhorros extends Cuenta {

    public static final BigDecimal SALDO_MINIMO =
            new BigDecimal("5.00");

    public static final BigDecimal TASA_ANUAL =
            new BigDecimal("0.04");

    public CuentaAhorros(String numeroCuenta,
                         String idSocio,
                         BigDecimal saldoInicial) {

        super(numeroCuenta, idSocio, saldoInicial);

        if (saldoInicial.compareTo(SALDO_MINIMO) < 0) {
            throw new IllegalArgumentException(
                "El saldo inicial minimo es $5.00");
        }
    }

    @Override
    protected boolean saldoPermitido(BigDecimal nuevoSaldo) {
        return nuevoSaldo.compareTo(SALDO_MINIMO) >= 0;
    }

    public BigDecimal calcularInteresMensual() {
        return getSaldo()
                .multiply(TASA_ANUAL)
                .divide(new BigDecimal("12"),
                        2, RoundingMode.HALF_UP);
    }

    private CuentaAhorros(String numeroCuenta,
                        String idSocio,
                        BigDecimal saldoGuardado,
                        boolean activa) {

        super(numeroCuenta, idSocio, saldoGuardado, activa);

        if (activa &&
                saldoGuardado.compareTo(SALDO_MINIMO) < 0) {
            throw new IllegalArgumentException(
                    "Saldo de ahorros guardado invalido");
        }
    }

    public static CuentaAhorros desdeArchivo(
            String numeroCuenta,
            String idSocio,
            BigDecimal saldoGuardado,
            boolean activa) {

        return new CuentaAhorros(
                numeroCuenta, idSocio, saldoGuardado, activa);
    }

}
