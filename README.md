# Trabajo 1 - Bases de Datos

## Descripción general

Este proyecto corresponde al Trabajo 1 de la asignatura de Bases de Datos. Consiste en el desarrollo grupal de cinco sistemas utilizando el lenguaje de programación Java, aplicando los principios de la Programación Orientada a Objetos (POO) y almacenamiento persistente de información mediante archivos JSON.

## Objetivo

Desarrollar sistemas que permitan gestionar información y operaciones de diferentes escenarios, aplicando conceptos de programación, organización del código, persistencia de datos y buenas prácticas de desarrollo de software.

## Tecnologías utilizadas

- **Lenguaje de programación:** Java.
- **Gestión del proyecto:** Apache Maven.
- **Almacenamiento de información:** Archivos JSON.
- **Biblioteca de procesamiento JSON:** Gson.
- **Control de versiones:** Git y GitHub.

## Sistemas desarrollados

El proyecto está compuesto por cinco ejercicios:

1. Sistema correspondiente al ejercicio 1.
2. Sistema correspondiente al ejercicio 2.
3. Sistema correspondiente al ejercicio 3.
4. **Sistema de gestión de Cafetería UTN.**
5. Sistema correspondiente al ejercicio 5.

## Ejercicio 4: Cafetería UTN

Este módulo permite gestionar los ingredientes, platos, recetas, abastecimientos, ventas e inventario de una cafetería, además de generar reportes relacionados con sus operaciones.

## Requisitos previos

Para ejecutar el proyecto se necesita:

- JDK 17 o superior.
- Apache Maven.
- Git para clonar el repositorio.

## Estructura del proyecto

La estructura principal del proyecto es:

```text
trabajo1-bases-de-datos/
│
├── README.md
├── .gitignore
│
└── trabajo1-bases-datos/
    ├── pom.xml
    ├── src/
    │   ├── main/
    │   │   └── java/
    │   └── test/
    ├── data/
    ├── docs/
    └── informe/
```

## Compilación y ejecución

1. Clonar el repositorio:

```bash
git clone https://github.com/MattyP200532/trabajo1-bases-datos.git
```

2. Ingresar a la carpeta del proyecto Maven:

```bash
cd trabajo1-bases-datos/trabajo1-bases-datos
```

3. Compilar el proyecto:

```bash
mvn clean compile
```

4. Ejecutar la aplicación:

```bash
mvn exec:java -Dexec.mainClass=com.trabajo1.App
```

La aplicación presenta un menú interactivo en consola para acceder a las opciones disponibles.

## Persistencia de datos

La información de los sistemas se almacena mediante archivos JSON ubicados en la carpeta `data`, permitiendo conservar los registros entre ejecuciones.
