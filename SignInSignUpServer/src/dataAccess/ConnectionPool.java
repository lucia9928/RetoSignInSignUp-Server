/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.Stack;

/**
 *
 * @author 2dam
 */
public class ConnectionPool {

    private final String url = ResourceBundle.getBundle("config").getString("url");
    private final String user = ResourceBundle.getBundle("config").getString("user");
    private final String password = ResourceBundle.getBundle("config").getString("password");
    private final int conexiones =10;

    private final Stack<Connection> conexionesDisponibles = new Stack<>();

    public ConnectionPool() throws SQLException {

        for (int i = 0; i < conexiones; i++) {

            conexionesDisponibles.push(crearConexion());

        }

    }

    //Crear y guardar conexiones
    private Connection crearConexion() throws SQLException {

        return DriverManager.getConnection(url, user, password);

    }

    // Obtener una conexión del pool
    public synchronized Connection obtenerConexion() {
        if (!conexionesDisponibles.isEmpty()) {
            return conexionesDisponibles.pop();
        } else {
            throw new RuntimeException("No hay conexiones disponibles");
        }

    }

    // Devolver una conexión al pool
    public synchronized void devolverConexion(Connection conexion) {
        conexionesDisponibles.push(conexion);  // Añade la conexión de vuelta al pool
    }

    // Cerrar todas las conexiones
    public synchronized void cerrarConexiones() throws SQLException {
        while (!conexionesDisponibles.isEmpty()) {
            conexionesDisponibles.pop().close();
        }
    }

}
