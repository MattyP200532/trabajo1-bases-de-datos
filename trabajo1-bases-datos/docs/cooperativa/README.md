
# Sistema de Cooperativa de Ahorro y Crédito

## 1. Descripción

Sistema desarrollado en Java mediante programación orientada
a objetos, que permite administrar socios, cuentas de ahorro,
cuentas corrientes y operaciones financieras.

La información se almacena en archivos JSON.

## 2. Tecnologías utilizadas

- Java 17 como versión de compilación
- Maven
- Gson
- JUnit 4
- Git y GitHub

## 3. Funcionalidades

- Registro de socios.
- Creación de cuentas de ahorro y corrientes.
- Depósitos y retiros.
- Transferencias entre cuentas.
- Cálculo y acreditación de intereses mensuales.
- Control de sobregiros.
- Cierre y liquidación de cuentas.
- Registro de movimientos financieros.
- Persistencia mediante JSON.
- Generación de reportes administrativos.

## 4. Reglas de negocio

- Las cuentas de ahorro generan un interés anual del 4 %.
- Las cuentas de ahorro mantienen un saldo mínimo de $5.
- Las cuentas corrientes permiten sobregiros de hasta $200.
- Los depósitos y retiros deben ser mayores que cero.
- No se permiten transferencias entre la misma cuenta.
- Las cuentas inactivas no permiten operaciones normales.
- Las cuentas solo se cierran con saldo cero.
- Los movimientos se conservan para auditoría.

## 5. Estructura del sistema

- model: entidades y clases bancarias.
- service: reglas de negocio y operaciones financieras.
- repository: almacenamiento y recuperación JSON.
- reports: reportes y auditoría.
- app: menú interactivo y aplicaciones de prueba.

## 6. Ejecución

Desde la carpeta que contiene pom.xml:

Compilar:

    mvn clean compile

Ejecutar pruebas:

    mvn clean test

Iniciar el menú:

    mvn exec:java "-Dexec.mainClass=com.trabajo1.cooperativa.app.CooperativaMenuApp"

## 7. Reportes disponibles

1. Estado de cuenta con saldos posteriores.
2. Intereses mensuales acreditados.
3. Cuentas corrientes en sobregiro.
4. Depósitos y retiros durante un período.
5. Auditoría de saldos.

## 8. Pruebas

Se implementaron pruebas con JUnit para verificar
depósitos, retiros, transferencias, saldo mínimo,
sobregiros, cierre de cuentas, intereses y persistencia JSON.

Los resultados reales deben verificarse ejecutando
mvn clean test.

## 9. Almacenamiento

El archivo data/cooperativa/estado.json conserva
socios, cuentas y movimientos.

El sistema recupera estos datos al iniciarse y guarda
el estado después de las operaciones válidas.

## 10. Limitaciones

El sistema es una implementación académica.

No está preparado para operaciones bancarias reales.
Se requiere reforzar la atomicidad duradera de las
transferencias, la recuperación ante fallos de escritura
y la validación integral de los importes monetarios.

## 11. Diagrama de clases UML

```mermaid
classDiagram
direction TB

class Socio {
    -String id
    -String nombre
    -String cedula
    +getId()
    +getNombre()
    +getCedula()
}

class Cuenta {
    <<abstract>>
    -String numeroCuenta
    -String idSocio
    -BigDecimal saldo
    -boolean activa
    +depositar(monto)
    +retirar(monto)
    +transferirA(destino, monto)
    +cerrarCuenta()
    +liquidarYCerrar()
    #saldoPermitido(nuevoSaldo)
}

class CuentaAhorros {
    +BigDecimal SALDO_MINIMO
    +BigDecimal TASA_ANUAL
    +calcularInteresMensual()
}

class CuentaCorriente {
    +BigDecimal LIMITE_SOBREGIRO
}

class TipoMovimiento {
    <<enumeration>>
    DEPOSITO
    RETIRO
    TRANSFERENCIA_ENVIADA
    TRANSFERENCIA_RECIBIDA
    INTERES
}

class Movimiento {
    -String id
    -String numeroCuenta
    -TipoMovimiento tipo
    -BigDecimal monto
    -BigDecimal saldoPosterior
    -String fechaHora
    -String cuentaRelacionada
}

class CooperativaService {
    -Map socios
    -Map cuentas
    -List movimientos
    +registrarSocio()
    +registrarCuenta()
    +depositar()
    +retirar()
    +transferir()
    +aplicarInteresMensual()
    +cerrarCuenta()
    +liquidarCuentaAhorros()
    +desdeArchivo()
}

class CooperativaJsonRepository {
    -Path archivo
    -Gson gson
    +guardar()
    +cargar()
}

class CooperativaReports {
    +estadoCuenta()
    +interesesMensuales()
    +cuentasEnSobregiro()
    +movimientosPorPeriodo()
    +auditoriaSaldos()
}

class CooperativaMenuApp {
    +main(args)
}

Cuenta <|-- CuentaAhorros
Cuenta <|-- CuentaCorriente
Socio "1" --> "0..*" Cuenta : posee
Cuenta "1" --> "0..*" Movimiento : historial
Movimiento --> TipoMovimiento : utiliza
CooperativaService o-- Socio
CooperativaService o-- Cuenta
CooperativaService o-- Movimiento
CooperativaJsonRepository ..> CooperativaService
CooperativaReports ..> CooperativaService
CooperativaMenuApp ..> CooperativaService
CooperativaMenuApp ..> CooperativaJsonRepository
CooperativaMenuApp ..> CooperativaReports
```