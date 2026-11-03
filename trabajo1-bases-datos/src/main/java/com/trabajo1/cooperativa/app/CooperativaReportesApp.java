
package com.trabajo1.cooperativa.app;

import com.trabajo1.cooperativa.repository.CooperativaJsonRepository;
import com.trabajo1.cooperativa.service.CooperativaService;
import com.trabajo1.cooperativa.reports.CooperativaReports;

import java.time.LocalDate;
import java.time.YearMonth;

public class CooperativaReportesApp {

    public static void main(String[] args) {

        CooperativaJsonRepository repositorio =
                new CooperativaJsonRepository();

        CooperativaService servicio =
                repositorio.cargar();

        CooperativaReports reportes =
                new CooperativaReports();

        YearMonth mes = YearMonth.now();

        // REPORTE 1
        reportes.estadoCuenta(servicio, "A001");

        // REPORTE 2
        reportes.interesesMensuales(servicio, mes);

        // REPORTE 3
        reportes.cuentasEnSobregiro(servicio);

        // REPORTE 4
        reportes.movimientosPorPeriodo(
                servicio,
                mes.atDay(1),
                LocalDate.now()
        );

        // REPORTE 5
        reportes.auditoriaSaldos(servicio);
    }
}
