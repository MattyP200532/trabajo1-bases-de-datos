
package com.trabajo1.cooperativa.model;

import java.math.BigDecimal;

public class CuentaCorriente extends Cuenta {

    public static final BigDecimal LIMITE_SOBREGIRO =
            new BigDecimal("200.00");

    public CuentaCorriente(String numeroCuenta,
                          String idSocio,
                          BigDecimal saldoInicial) {

        super(numeroCuenta, idSocio, saldoInicial);
    }

    @Override
    protected boolean saldoPermitido(BigDecimal nuevoSaldo) {
        return nuevoSaldo.compareTo(
                LIMITE_SOBREGIRO.negate()) >= 0;
    }
}
