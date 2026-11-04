# Trabajo 1 - Bases de Datos

## Descripción general

Proyecto grupal desarrollado para la asignatura de Bases de Datos. Consiste en cinco sistemas de gestión implementados en Java, aplicando programación orientada a objetos y persistencia de información mediante archivos JSON.

Los módulos contemplados en el proyecto son:

- Biblioteca.
- Cooperativa.
- Transporte.
- Cafetería.
- Artesanías.

## Tecnologías utilizadas

- **Lenguaje:** Java 17.
- **Gestor de dependencias y construcción:** Apache Maven.
- **Persistencia:** archivos JSON mediante Gson.
- **Pruebas unitarias:** JUnit 4.13.2.
- **Entorno de desarrollo:** Visual Studio Code.

## Estructura del proyecto

```text
trabajo1-bases-de-datos/
│
├── README.md
├── .gitignore
│
└── trabajo1-bases-datos/
    ├── pom.xml
    ├── data/
    │   ├── biblioteca/
    │   ├── cooperativa/
    │   ├── transporte/
    │   ├── cafeteria/
    │   └── artesanias/
    │
    ├── docs/
    │   └── diagramas/
    │
    ├── informe/
    │
    └── src/
        ├── main/java/com/trabajo1/
        │   ├── biblioteca/
        │   ├── cooperativa/
        │   ├── transporte/
        │   ├── cafeteria/
        │   └── artesanias/
        │
        └── test/java/com/trabajo1/
            └── biblioteca/
```

Cada módulo organiza su código en paquetes según las responsabilidades de la aplicación:

- `app`: puntos de entrada e interacción con el usuario.
- `model`: entidades y clases del dominio.
- `service`: lógica de negocio y reglas del sistema.
- `repository`: persistencia y recuperación de información.
- `reports`: generación de consultas e informes.

## Requisitos previos

Para compilar y ejecutar el proyecto se necesita:

- JDK 17 o superior.
- Apache Maven.
- Git (para clonar y actualizar el repositorio).

## Instalación y ejecución

### 1. Clonar el repositorio

```bash
git clone URL_DEL_REPOSITORIO
```

### 2. Ingresar al proyecto Maven

```bash
cd trabajo1-bases-datos
```

### 3. Compilar y ejecutar las pruebas

```bash
mvn clean test
```

### 4. Ejecutar el módulo Biblioteca

```bash
mvn exec:java "-Dexec.mainClass=com.trabajo1.biblioteca.app.Main"
```

El programa permite interactuar con el sistema de biblioteca mediante un menú de consola.

## Módulos del proyecto

### Biblioteca

Sistema de gestión de préstamos de libros y revistas.

Incluye:

- Registro de usuarios, materiales y ejemplares.
- Registro de préstamos y devoluciones.
- Control de disponibilidad de ejemplares.
- Control de multas por retrasos.
- Restricción de préstamos según las reglas del sistema.
- Historial de préstamos.
- Reportes de préstamos vencidos, materiales más prestados y multas.
- Auditoría de ejemplares disponibles.
- Persistencia de información mediante archivos JSON.

### Cooperativa

Módulo contemplado en el proyecto grupal. Su documentación se completará conforme se integre su implementación.

### Transporte

Módulo contemplado en el proyecto grupal. Su documentación se completará conforme se integre su implementación.

### Cafetería

Módulo contemplado en el proyecto grupal. Su documentación se completará conforme se integre su implementación.

### Artesanías

Módulo contemplado en el proyecto grupal. Su documentación se completará conforme se integre su implementación.

## Pruebas

Las pruebas automatizadas se encuentran en:

```text
src/test/java/com/trabajo1/
```

Para ejecutarlas:

```bash
mvn clean test
```

Los resultados de las pruebas se generan en:

```text
target/surefire-reports/
```

## Diagramas UML

<WritingBlock id="82517" variant="standard">## Diagramas UML

Los diagramas de clases del proyecto se almacenan en `docs/diagramas/`.

### Módulo Biblioteca

El siguiente diagrama representa la estructura de clases del sistema de gestión de préstamos de libros y revistas.

![Diagrama UML del módulo Biblioteca](trabajo1-bases-datos/docs/diagramas/diagrama-biblioteca.png)</WritingBlock>

## Informe del proyecto

El informe técnico y los documentos complementarios se almacenarán en:

```text
informe/
```

## Estado del proyecto

El proyecto se encuentra en desarrollo e integración grupal. Cada módulo será documentado y actualizado conforme se incorporen las implementaciones de sus respectivos integrantes.