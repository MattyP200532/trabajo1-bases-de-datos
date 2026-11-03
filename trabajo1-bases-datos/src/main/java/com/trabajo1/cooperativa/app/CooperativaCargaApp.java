
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.repository.CooperativaJsonRepository;
import com.trabajo1.cooperativa.service.CooperativaService;
import com.trabajo1.cooperativa.model.Movimiento;

public class CooperativaCargaApp {

    public static void main(String[] args) {

        CooperativaJsonRepository repositorio =
                new CooperativaJsonRepository();

        CooperativaService servicio =
                repositorio.cargar();

        System.out.println("DATOS RECUPERADOS DEL JSON");

        System.out.println("Cuenta de ahorros A001: $" +
                servicio.consultarSaldo("A001"));

        System.out.println("Cuenta corriente C001: $" +
                servicio.consultarSaldo("C001"));

        System.out.println("Movimientos recuperados:");

        for (Movimiento movimiento :
                servicio.consultarMovimientos("A001")) {

            System.out.println(movimiento);
        }
    }
}
