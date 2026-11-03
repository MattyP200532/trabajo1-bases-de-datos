
package com.trabajo1.cooperativa.repository;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.trabajo1.cooperativa.model.*;
import com.trabajo1.cooperativa.service.CooperativaService;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

import java.io.Reader;
import java.math.BigDecimal;

public class CooperativaJsonRepository {

    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    private final Path archivo = Path.of(
            "data", "cooperativa", "estado.json");

    // Representa una cuenta en formato JSON.
    private record CuentaDato(
            String tipo,
            String numeroCuenta,
            String idSocio,
            String saldo,
            boolean activa
    ) {}

    // Representa el estado completo de la cooperativa.
    private record Estado(
            List<Socio> socios,
            List<CuentaDato> cuentas,
            List<Movimiento> movimientos
    ) {}

    public void guardar(CooperativaService servicio) {

        if (servicio == null) {
            throw new IllegalArgumentException(
                    "El servicio no puede ser nulo");
        }

        List<CuentaDato> datosCuentas =
                new ArrayList<>();

        for (Cuenta cuenta : servicio.listarCuentas()) {

            String tipo;

            if (cuenta instanceof CuentaAhorros) {
                tipo = "AHORROS";
            } else if (cuenta instanceof CuentaCorriente) {
                tipo = "CORRIENTE";
            } else {
                throw new IllegalArgumentException(
                        "Tipo de cuenta desconocido");
            }

            datosCuentas.add(new CuentaDato(
                    tipo,
                    cuenta.getNumeroCuenta(),
                    cuenta.getIdSocio(),
                    cuenta.getSaldo().toPlainString(),
                    cuenta.isActiva()
            ));
        }

        Estado estado = new Estado(
                servicio.listarSocios(),
                datosCuentas,
                servicio.listarMovimientos()
        );

        try {
            Files.createDirectories(archivo.getParent());

            Path temporal = archivo.resolveSibling(
                    "estado.json.tmp");

            try (Writer escritor = Files.newBufferedWriter(
                    temporal, StandardCharsets.UTF_8)) {

                gson.toJson(estado, escritor);
            }

            try {
                Files.move(temporal, archivo,
                        StandardCopyOption.REPLACE_EXISTING,
                        StandardCopyOption.ATOMIC_MOVE);

            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporal, archivo,
                        StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error al guardar estado JSON", e);
        }
    }

    public CooperativaService cargar() {

        if (!Files.isRegularFile(archivo)) {
            throw new IllegalStateException(
                    "No existe el archivo: " + archivo);
        }

        try (Reader lector = Files.newBufferedReader(
                archivo, StandardCharsets.UTF_8)) {

            Estado estado = gson.fromJson(lector, Estado.class);

            if (estado == null ||
                    estado.socios() == null ||
                    estado.cuentas() == null ||
                    estado.movimientos() == null) {
                throw new IllegalStateException(
                        "El archivo JSON esta incompleto");
            }

            List<Cuenta> cuentasRecuperadas =
                    new ArrayList<>();

            for (CuentaDato dato : estado.cuentas()) {

                if (dato == null || dato.tipo() == null ||
                        dato.saldo() == null) {
                    throw new IllegalStateException(
                            "Datos de cuenta incompletos");
                }

                BigDecimal saldo =
                        new BigDecimal(dato.saldo());

                Cuenta cuenta;

                switch (dato.tipo()) {

                    case "AHORROS":
                        cuenta = CuentaAhorros.desdeArchivo(
                                dato.numeroCuenta(),
                                dato.idSocio(),
                                saldo,
                                dato.activa());
                        break;

                    case "CORRIENTE":
                        cuenta = CuentaCorriente.desdeArchivo(
                                dato.numeroCuenta(),
                                dato.idSocio(),
                                saldo,
                                dato.activa());
                        break;

                    default:
                        throw new IllegalStateException(
                                "Tipo de cuenta desconocido");
                }

                cuentasRecuperadas.add(cuenta);
            }

            return CooperativaService.desdeArchivo(
                    estado.socios(),
                    cuentasRecuperadas,
                    estado.movimientos());

        } catch (IOException e) {
            throw new UncheckedIOException(
                    "Error al leer el archivo JSON", e);
        }
    }

}
