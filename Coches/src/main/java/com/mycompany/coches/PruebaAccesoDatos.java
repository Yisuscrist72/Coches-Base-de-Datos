package com.mycompany.coches;

import java.sql.SQLException;

public class PruebaAccesoDatos {

    public static void main(String[] args) throws SQLException {
        // Crear instancia del acceso a datos
        AccesoDatos AD = new AccesoDatos();

        // Abrir conexión con la base de datos
        AD.abrirConexion();

        // Mostrar todos los coches (ordenados por precio DESC)
        AD.mostrarDatosCoches();

        // Modificar el precio de un coche existente
        AD.modificarCoche("BA-3333", 5000);

        // Borrar un coche por matrícula
        AD.borrarCoche("MA");

        // Insertar un coche (asegúrate que el DNI "1A" existe en Propietarios)
        AD.insertarCoche("AA-0005", "Ford", 4500, "1A");

        // Insertar un propietario nuevo
        AD.insertarPropietario("X25", "Jose", 54);

        // Cerrar la conexión
        AD.cerrarConexion();
    }
}

