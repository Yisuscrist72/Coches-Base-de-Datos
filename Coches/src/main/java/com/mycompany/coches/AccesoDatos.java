package com.mycompany.coches;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccesoDatos {

  private static final String URL = "jdbc:mysql://localhost:3306/DatosCoches";
  private static final String USER = "root";
  private static final String PASSWORD = "root";

  private Connection cn = null;

  // Abrir conexión
  public void abrirConexion() {
    try {
      cn = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("Conectado a la base de datos DatosCoches");
    } catch (SQLException ex) {
      System.err.println("ERROR de Conexión. Verifica MySQL y la BD 'DatosCoches'.");
      ex.printStackTrace();
    }
  }

  // Cerrar conexión
  public void cerrarConexion() {
    try {
      if (cn != null && !cn.isClosed()) {
        cn.close();
        System.out.println("Se ha cerrado la conexión.");
      }
    } catch (SQLException ex) {
      System.err.println("ERROR al cerrar la conexión.");
      ex.printStackTrace();
    }
  }

  // Mostrar coches
  public void mostrarDatosCoches() {
    String sql = "SELECT * FROM Coches ORDER BY precio DESC";
    try (PreparedStatement ps = cn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

      System.out.println("\n LISTADO DE COCHES (ORDENADOS POR PRECIO DESC)");
      System.out.printf("%-10s | %-15s | %-10s | %-10s%n", "Matrícula", "Marca", "Precio", "DNI");
      System.out.println("-----------------------------------------------------------");

      boolean hay = false;
      while (rs.next()) {
        hay = true;
        System.out.printf("%-10s | %-15s | %-10d | %-10s%n",
          rs.getString("matricula"),
          rs.getString("marca"),
          rs.getInt("precio"),
          rs.getString("dni"));
      }

      if (!hay) {
        System.out.println("No hay coches registrados.");
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al consultar los coches.");
      ex.printStackTrace();
    }
  }

  // Actualiza el precio de un coche existente
  public void modificarCoche(String matricula, int nuevoPrecio) {
    String sql = "UPDATE Coches SET precio = ? WHERE matricula = ?";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setInt(1, nuevoPrecio);
      ps.setString(2, matricula);

      int filas = ps.executeUpdate();

      if (filas > 0) {
        System.out.println("Precio del coche actualizado. Matrícula: " + matricula);
      } else {
        System.out.println("No se encontró un coche con matrícula: " + matricula);
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al modificar el coche.");
      ex.printStackTrace();
    }
  }

  // Insertar propietario
  public void insertarPropietario(String dni, String nombre, int edad) {
    String sql = "INSERT INTO Propietarios (dni, nombre, edad) VALUES (?, ?, ?)";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, dni);
      ps.setString(2, nombre);
      ps.setInt(3, edad);

      int filas = ps.executeUpdate();
      if (filas > 0) {
        System.out.println("Propietario insertado correctamente. DNI: " + dni);
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al insertar el propietario.");
      ex.printStackTrace();
    }
  }

  // Verificar propietario
  private boolean existePropietario(String dni) {
    String sql = "SELECT dni FROM Propietarios WHERE dni = ?";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, dni);
      try (ResultSet rs = ps.executeQuery()) {
        return rs.next();
      }
    } catch (SQLException ex) {
      System.err.println("ERROR al verificar el propietario.");
      ex.printStackTrace();
      return false;
    }
  }

  // Insertar coche
  public void insertarCoche(String matricula, String marca, int precio, String dni) {
    if (!existePropietario(dni)) {
      System.err.println("ERROR: El DNI " + dni + " no existe en Propietarios.");
      return;
    }

    String sql = "INSERT INTO Coches (matricula, marca, precio, dni) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
      ps.setString(1, matricula);
      ps.setString(2, marca);
      ps.setInt(3, precio);
      ps.setString(4, dni);

      int filas = ps.executeUpdate();
      if (filas > 0) {
        System.out.println("Coche insertado correctamente. Matrícula: " + matricula);
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al insertar el coche.");
      ex.printStackTrace();
    }
  }

  // Borrar coche
  public void borrarCoche(String prefijo) {
    String sql = "DELETE FROM Coches WHERE matricula LIKE ?";
    try (PreparedStatement ps = cn.prepareStatement(sql)) {
        ps.setString(1, prefijo + "%"); // El símbolo % indica “cualquier cosa después de MA”
        int filas = ps.executeUpdate();

        if (filas > 0) {
            System.out.println("Se borraron " + filas + " coche(s) con prefijo: " + prefijo);
        } else {
            System.out.println("No se encontraron coches con prefijo: " + prefijo);
        }

    } catch (SQLException ex) {
        System.err.println("ERROR al borrar coche(s) por prefijo.");
        ex.printStackTrace();
    }
}

  // Mostrar propietario y sus coches
  public void mostrarPropietarioYCoches(String dni) {
    String sqlProp = "SELECT * FROM Propietarios WHERE dni = ?";
    try (PreparedStatement psProp = cn.prepareStatement(sqlProp)) {
      psProp.setString(1, dni);
      try (ResultSet rsProp = psProp.executeQuery()) {
        if (!rsProp.next()) {
          System.out.println("No se encontró un propietario con DNI: " + dni);
          return;
        }

        System.out.println("\n--- DATOS DEL PROPIETARIO ---");
        System.out.println("DNI: " + dni);
        System.out.println("Nombre: " + rsProp.getString("nombre"));
        System.out.println("Edad: " + rsProp.getInt("edad"));
      }

      String sqlCoches = "SELECT * FROM Coches WHERE dni = ? ORDER BY precio DESC";
      try (PreparedStatement psCoches = cn.prepareStatement(sqlCoches)) {
        psCoches.setString(1, dni);
        try (ResultSet rsCoches = psCoches.executeQuery()) {
          System.out.println("\n--- COCHES DEL PROPIETARIO ---");
          System.out.printf("%-10s | %-15s | %-10s%n", "Matrícula", "Marca", "Precio");
          System.out.println("------------------------------------------");

          boolean hay = false;
          while (rsCoches.next()) {
            hay = true;
            System.out.printf("%-10s | %-15s | %-10d%n",
              rsCoches.getString("matricula"),
              rsCoches.getString("marca"),
              rsCoches.getInt("precio"));
          }

          if (!hay) {
            System.out.println("Este propietario no tiene coches registrados.");
          }
        }
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al consultar el propietario y sus coches.");
      ex.printStackTrace();
    }
  }

  // Borrar propietario (y coches asociados)
  public void borrarPropietario(String dni) {
    if (!existePropietario(dni)) {
      System.out.println("No se encontró un propietario con DNI: " + dni);
      return;
    }

    String sqlCoches = "DELETE FROM Coches WHERE dni = ?";
    String sqlProp = "DELETE FROM Propietarios WHERE dni = ?";

    try (PreparedStatement psCoches = cn.prepareStatement(sqlCoches); PreparedStatement psProp = cn.prepareStatement(sqlProp)) {

      psCoches.setString(1, dni);
      int cochesBorrados = psCoches.executeUpdate();

      psProp.setString(1, dni);
      int propBorrado = psProp.executeUpdate();

      if (propBorrado > 0) {
        System.out.println("Propietario borrado correctamente. DNI: " + dni);
        if (cochesBorrados > 0) {
          System.out.println("Se han borrado " + cochesBorrados + " coche(s) asociado(s).");
        }
      }

    } catch (SQLException ex) {
      System.err.println("ERROR al borrar el propietario.");
      ex.printStackTrace();
    }
  }

  // Obtener conexión
  public Connection getConexion() {
    return cn;
  }

  public boolean estaConectado() {
    try {
      return cn != null && !cn.isClosed();
    } catch (SQLException ex) {
      return false;
    }
  }
}