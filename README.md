# Proyecto Coches

## Descripción
Este proyecto en Java gestiona una base de datos de coches y sus propietarios utilizando **MySQL**.  
Permite realizar operaciones de **consulta, inserción, actualización y borrado** de coches y propietarios, demostrando el uso de **JDBC** con `PreparedStatement` para mayor seguridad frente a inyecciones SQL.

El proyecto fue desarrollado como práctica académica y está organizado en las siguientes clases:

- **AccesoDatos**:  
  Maneja todas las operaciones con la base de datos:
  - Abrir y cerrar conexión.
  - Mostrar todos los coches ordenados por precio descendente.
  - Insertar, modificar y borrar coches.
  - Insertar y borrar propietarios.
  - Mostrar un propietario junto con todos sus coches.
  - Verificar la existencia de un propietario.

- **PruebaAccesoDatos**:  
  Clase con el método `main` para probar todas las funcionalidades de `AccesoDatos`.

---

## Requisitos

- Java 25 o superior
- Maven
- MySQL con una base de datos llamada `DatosCoches`
- Dos tablas:
  - **Propietarios**: `dni` (PK), `nombre`, `edad`
  - **Coches**: `matricula` (PK), `marca`, `precio`, `dni` (FK de Propietarios)

---

Clonar el repositorio:

```bash
git clone https://github.com/tu-usuario/Coches.git
