# Informe de pruebas – Módulo Biblioteca

## 1. Información general

**Proyecto:** Trabajo 1 de Bases de Datos  
**Módulo:** Sistema de gestión de biblioteca  
**Lenguaje de programación:** Java 17  
**Persistencia:** Archivos JSON  
**Herramientas:** Apache Maven, Gson y JUnit 4.13.2  
**Fecha de validación:** 4 de noviembre de 2026

### Integrantes del equipo

1. Matias Nicolas Perez Chamorro
2. Yaguapaz Obando Dilan Alexis
3. Suarez Bravo Damaris Lisbeth
4. Quichinguango Amaguaña Jared Moroni
5. Sinchico Anrango Yurak Sisay

## 2. Objetivo de las pruebas

Verificar el correcto funcionamiento del módulo Biblioteca, comprobando el cumplimiento de las reglas de negocio relacionadas con el registro de préstamos, devoluciones, disponibilidad de ejemplares, generación de multas, persistencia de datos y elaboración de reportes.

## 3. Entorno de pruebas

Las pruebas se realizaron en un entorno local con las siguientes herramientas:

- Java 17.
- Apache Maven.
- Gson 2.13.2 para la persistencia JSON.
- JUnit 4.13.2 para las pruebas automatizadas.
- Sistema operativo Windows.
- Visual Studio Code como entorno de desarrollo.

Para ejecutar las pruebas se utilizó:

```bash
mvn clean test
```

## 4. Casos de prueba funcionales

| ID | Caso de prueba | Resultado esperado | Resultado obtenido |
|---|---|---|---|
| CP-01 | Registrar un préstamo de un libro. | Crear el préstamo y calcular el vencimiento a 7 días. | Correcto. |
| CP-02 | Devolver un libro con retraso de 3 días. | Generar una multa de $0.75. | Correcto. |
| CP-03 | Intentar prestar a un usuario con multa pendiente. | Rechazar el préstamo. | Correcto. |
| CP-04 | Registrar tres préstamos activos para un usuario. | Permitir los tres préstamos. | Correcto. |
| CP-05 | Intentar registrar un cuarto préstamo activo. | Rechazar el préstamo. | Correcto. |
| CP-06 | Prestar un ejemplar que ya está prestado. | Rechazar el préstamo. | Correcto. |
| CP-07 | Consultar préstamos vencidos a una fecha determinada. | Mostrar los préstamos activos vencidos. | Correcto. |
| CP-08 | Consultar el historial de préstamos de un usuario. | Mostrar sus préstamos activos y devueltos. | Correcto. |
| CP-09 | Consultar los materiales más prestados. | Mostrar los materiales ordenados por cantidad de préstamos. | Correcto. |
| CP-10 | Consultar las multas generadas durante un período. | Mostrar las multas correspondientes al intervalo. | Correcto. |
| CP-11 | Auditar los ejemplares disponibles. | Mostrar ejemplares totales, prestados y disponibles por material. | Correcto. |

## 5. Pruebas automatizadas

Se ejecutó la suite automatizada mediante Maven.

**Resultado:** 20 pruebas superadas, sin errores.

Las pruebas cubren los siguientes componentes:

- `PrestamoServiceTest`: reglas de negocio de préstamos y devoluciones.
- `JsonRepositoryTest`: persistencia y recuperación de información JSON.
- `BibliotecaRepositoryTest`: integración y persistencia de las entidades de Biblioteca.
- `ReporteBibliotecaTest`: funcionamiento de los reportes.

## 6. Verificación de persistencia

Se comprobó que la información del sistema se almacena correctamente en archivos JSON.

Los archivos utilizados son:

- `usuarios.json`
- `libros.json`
- `revistas.json`
- `ejemplares.json`
- `prestamos.json`
- `multas.json`

También se verificó que los datos registrados mediante la aplicación se conservaran al almacenarse en los archivos correspondientes.

## 7. Declaración de uso de inteligencia artificial

Durante el desarrollo del módulo Biblioteca se utilizó **ChatGPT, de OpenAI**, como herramienta de asistencia para apoyar el proceso de diseño, implementación, depuración y documentación del software.

### Actividades en las que se utilizó

- Orientación sobre la arquitectura y organización del proyecto Java.
- Asistencia en el diseño de clases, atributos, métodos y relaciones UML.
- Apoyo en la implementación de las reglas de negocio para préstamos, devoluciones y multas.
- Orientación para implementar la persistencia de información mediante archivos JSON utilizando Gson.
- Apoyo en la elaboración de consultas y reportes.
- Asistencia en la identificación y corrección de errores de programación.
- Orientación para diseñar y ejecutar pruebas automatizadas con JUnit.
- Apoyo en la elaboración de documentación técnica.

### Verificación de los resultados

Las sugerencias y soluciones proporcionadas por ChatGPT fueron revisadas y contrastadas con los requisitos del taller antes de incorporarse al proyecto.

La verificación se realizó mediante:

- Compilación del proyecto utilizando Apache Maven.
- Ejecución de las pruebas automatizadas mediante `mvn clean test`, obteniendo 20 pruebas superadas.
- Ejecución manual de la aplicación para comprobar el registro de préstamos y devoluciones.
- Comprobación de las restricciones de préstamos y generación de multas.
- Inspección de los archivos JSON para verificar la persistencia de los datos.
- Ejecución de los reportes y comparación de sus resultados con los valores esperados.

La inteligencia artificial se utilizó como herramienta de apoyo y no como sustituto de la revisión, comprensión y validación del trabajo realizado por el estudiante.

**Herramienta utilizada:** ChatGPT (OpenAI).

**Responsabilidad sobre la entrega:** los integrantes del equipo son responsables de revisar, comprender y validar el código y los resultados presentados en el proyecto.

## 8. Conclusiones

Las pruebas realizadas permiten comprobar que el módulo Biblioteca cumple satisfactoriamente las reglas de negocio implementadas.

Se verificó el registro de préstamos y devoluciones, el control de disponibilidad, la restricción de préstamos por usuario, la generación de multas, la persistencia de información y el funcionamiento de los reportes.

Las 20 pruebas automatizadas finalizaron satisfactoriamente y los casos de prueba funcionales documentados obtuvieron resultados correctos.

## 9. Repositorio del proyecto

**GitHub:** https://github.com/MattyP200532/trabajo1-bases-de-datos

**Rama de desarrollo del módulo:** `feature/biblioteca`

---

*Este documento corresponde a los resultados verificados del módulo Biblioteca. Los resultados de los módulos Cooperativa, Transporte, Cafetería y Artesanías deberán incorporarse cuando sus respectivas implementaciones hayan sido finalizadas y probadas.*