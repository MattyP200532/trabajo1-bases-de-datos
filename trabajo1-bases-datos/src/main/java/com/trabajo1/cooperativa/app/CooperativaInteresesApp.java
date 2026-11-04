
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.repository.CooperativaJsonRepository;
import com.trabajo1.cooperativa.service.CooperativaService;

public class CooperativaInteresesApp {

    public static void main(String[] args) {

        CooperativaJsonRepository repositorio =
                new CooperativaJsonRepository();

        // Recuperar informacion del JSON
        CooperativaService servicio =
                repositorio.cargar();

        System.out.println("SISTEMA DE INTERESES");

        System.out.println("Saldo anterior: $" +
                servicio.consultarSaldo("A001"));

        // Aplicar interes mensual
        servicio.aplicarInteresMensual("A001");

        System.out.println("Saldo con intereses: $" +
                servicio.consultarSaldo("A001"));

        // Guardar el nuevo saldo y movimiento
        repositorio.guardar(servicio);

        System.out.println(
                "Intereses aplicados y guardados correctamente");
    }
}
