# Evidencia de pruebas – Sistema de Cooperativa

## 1. Información general

- **Proyecto:** Trabajo 1 – Bases de Datos
- **Módulo:** Cooperativa de Ahorro y Crédito
- **Responsable:** Quinchiguango Jared
- **Lenguaje:** Java
- **Herramienta de pruebas:** JUnit 4 mediante Maven
- **Almacenamiento:** JSON
- **Fecha de ejecución:** 03/11/2026
- **Hora de finalización:** 23:10:42 (UTC-05:00)
- **Resultado general:** BUILD SUCCESS

## 2. Objetivo

Verificar el correcto funcionamiento de las operaciones bancarias, las restricciones del sistema y la persistencia de información en archivos JSON.

Las pruebas permiten comprobar que las operaciones válidas se ejecuten correctamente y que las operaciones inválidas sean rechazadas sin alterar indebidamente los saldos de las cuentas.

Además, se verifica la recuperación de información almacenada, la conservación del historial de movimientos y la consistencia de los saldos mediante una auditoría.

## 3. Procedimiento

Se ejecutaron las pruebas automatizadas mediante Apache Maven y JUnit, utilizando el siguiente comando:

```powershell
mvn clean test
```

Los resultados se consultaron en la terminal de Visual Studio Code y en los reportes generados dentro de la carpeta `target/surefire-reports`.

Las pruebas se ejecutaron mediante las siguientes clases:

| Clase de prueba | Pruebas ejecutadas | Fallos | Errores |
|---|---:|---:|---:|
| `AppTest` | 1 | 0 | 0 |
| `CooperativaServiceTest` | 11 | 0 | 0 |
| `CooperativaJsonRepositoryTest` | 1 | 0 | 0 |
| **Total** | **13** | **0** | **0** |

La prueba `AppTest` corresponde a una verificación general del proyecto Maven. Las otras 12 pruebas corresponden directamente al módulo de cooperativa.

## 4. Resultados de las pruebas

| N.º | Caso de prueba | Resultado esperado | Resultado real |
|---|---|---|---|
| 1 | Depósito válido | El saldo aumenta correctamente | **Aprobado:** se depositaron $20.00 y el saldo aumentó de $100.00 a $120.00. |
| 2 | Retiro superior al permitido | Se rechaza sin modificar el saldo | **Aprobado:** se rechazó un retiro de $96.00 en una cuenta de ahorros con $100.00, porque incumplía el saldo mínimo de $5.00. El saldo permaneció en $100.00. |
| 3 | Transferencia válida | Se actualizan ambas cuentas | **Aprobado:** se transfirieron $25.00 desde A001 hacia C001. Los saldos finales fueron $75.00 y $25.00, respectivamente. |
| 4 | Transferencia inválida | Los saldos y movimientos permanecen iguales | **Aprobado:** se rechazó una transferencia de $200.00 desde A001. Los saldos de ambas cuentas y la cantidad de movimientos permanecieron sin cambios. |
| 5 | Sobregiro máximo | No permite superar los $200.00 | **Aprobado:** se permitió un sobregiro de $200.00 en una cuenta corriente y se rechazó un retiro adicional de $0.01. |
| 6 | Cierre con saldo cero | La cuenta queda inactiva | **Aprobado:** se cerró una cuenta corriente con saldo cero y se rechazó un depósito posterior. |
| 7 | Transferencia a la misma cuenta | Se rechaza la operación | **Aprobado:** se rechazó una transferencia desde A001 hacia la misma cuenta A001. |
| 8 | Intereses duplicados | Se rechaza la segunda acreditación mensual | **Aprobado:** se aplicó el interés mensual de una cuenta de ahorros y se rechazó una segunda acreditación durante el mismo mes. |
| 9 | Persistencia JSON | Se recuperan saldos y movimientos sin duplicarlos | **Aprobado:** se recuperaron los saldos de $80.27 y $20.00, se conservaron cuatro movimientos y la auditoría de saldos fue correcta. Una segunda carga mantuvo la misma cantidad de movimientos. |
| 10 | Depósito con tres decimales | Se rechaza el monto inválido | **Aprobado:** se rechazó un depósito de $10.123 y el saldo permaneció en $100.00. |
| 11 | Transferencia con tres decimales | Se rechaza el monto inválido | **Aprobado:** se rechazó una transferencia de $5.123 y los saldos permanecieron sin modificaciones. |
| 12 | Saldo inicial con tres decimales | Se rechaza la creación de la cuenta | **Aprobado:** se rechazó la creación de una cuenta de ahorros con saldo inicial de $50.123. |

Los resultados anteriores corresponden a las verificaciones implementadas en las clases de pruebas y confirmadas por la ejecución satisfactoria de JUnit.

### 4.1. Evidencia de auditoría de saldos

Durante la prueba de persistencia JSON, el sistema presentó el siguiente resultado:

```text
=== AUDITORIA DE SALDOS ===
OK - Cuenta A910 | Saldo: $80.27
OK - Cuenta C910 | Saldo: $20.00
AUDITORIA CORRECTA
```

Este resultado confirma que los saldos recuperados coincidieron con los calculados a partir de los movimientos registrados en ese escenario de prueba.

### 4.2. Evidencia de ejecución de Maven

La terminal mostró el siguiente resumen:

```text
Running com.trabajo1.AppTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

Running com.trabajo1.cooperativa.repository.CooperativaJsonRepositoryTest
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

Running com.trabajo1.cooperativa.service.CooperativaServiceTest
Tests run: 11, Failures: 0, Errors: 0, Skipped: 0

Results:
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0

BUILD SUCCESS
```

## 5. Resumen de ejecución

- **Total de pruebas ejecutadas:** 13
- **Pruebas aprobadas:** 13
- **Pruebas fallidas:** 0
- **Errores:** 0
- **Pruebas omitidas:** 0
- **Pruebas específicas del módulo:** 12
- **Prueba general del proyecto Maven:** 1
- **Tiempo total de ejecución:** 4.475 segundos
- **Resultado general:** BUILD SUCCESS

La ejecución terminó satisfactoriamente el 3 de noviembre de 2026 a las 23:10:42, sin presentar fallos ni errores en las pruebas ejecutadas.

## 6. Conclusión

Las pruebas automatizadas del Sistema de Cooperativa de Ahorro y Crédito finalizaron satisfactoriamente, con un total de 13 pruebas ejecutadas, 13 aprobadas, cero fallos y cero errores.

Se verificó el funcionamiento de las principales operaciones financieras, incluyendo depósitos, retiros, transferencias, intereses mensuales, sobregiros y cierre de cuentas.

También se comprobó el cumplimiento de restricciones importantes, como el saldo mínimo de las cuentas de ahorro, el límite de sobregiro de las cuentas corrientes, la prohibición de transferencias a una misma cuenta y el rechazo de montos monetarios con más de dos decimales.

En cuanto a la persistencia de información, se verificó que los datos almacenados en JSON pudieran recuperarse conservando los saldos y movimientos originales. La auditoría ejecutada durante las pruebas confirmó la consistencia de los saldos del escenario evaluado.

**En conclusión, los resultados obtenidos demuestran que las funcionalidades evaluadas cumplen satisfactoriamente los escenarios de prueba implementados para el módulo de cooperativa.**

No obstante, estas pruebas no garantizan la ausencia de errores en situaciones no evaluadas, como fallos imprevistos del sistema durante la escritura de archivos o accesos simultáneos. La implementación tiene fines académicos y no constituye una certificación de seguridad para operaciones bancarias reales.