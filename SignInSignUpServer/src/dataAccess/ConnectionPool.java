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
 * Clase que maneja un pool de conexiones a la base de datos.
 * Utiliza una pila para gestionar un número limitado de conexiones que pueden ser reutilizadas, optimizando el acceso a la base de datos.
 * 
 * @author Markel
 */
public class ConnectionPool {

    // Carga la configuración de la base de datos desde el archivo de propiedades
    ResourceBundle fichConfig = ResourceBundle.getBundle("propiedades.connectionDB");

    // Datos necesarios para la conexión con la base de datos
    private final String url = fichConfig.getString("URL");
    private final String user = fichConfig.getString("USER");
    private final String password = fichConfig.getString("PASSWORD");

    // Cantidad de conexiones que se crearán en el pool
    private final int conexiones = Integer.valueOf(fichConfig.getString("CONEXIONES"));

    // Pila que almacena las conexiones disponibles
    private final Stack<Connection> conexionesDisponibles = new Stack<>();

    /**
     * Constructor que inicializa el pool de conexiones.
     * Crea las conexiones necesarias y las almacena en la pila.
     * 
     * @throws SQLException Si ocurre un error al crear una conexión
     */
    public ConnectionPool() throws SQLException {
        // Crea y guarda las conexiones en el pool
        for (int i = 0; i < conexiones; i++) {
            conexionesDisponibles.push(crearConexion());
        }
    }

    /**
     * Crea una nueva conexión a la base de datos utilizando los parámetros de configuración.
     * 
     * @return La conexión creada
     * @throws SQLException Si ocurre un error al establecer la conexión con la base de datos
     */
    private Connection crearConexion() throws SQLException {
        // Utiliza el DriverManager para crear la conexión a la base de datos
        return DriverManager.getConnection(url, user, password);
    }

    /**
     * Obtiene una conexión del pool.
     * Si hay una conexión disponible, se la devuelve; si no, lanza una excepción.
     * 
     * @return Una conexión disponible en el pool
     * @throws RuntimeException Si no hay conexiones disponibles
     */
    public synchronized Connection obtenerConexion() {
        if (!conexionesDisponibles.isEmpty()) {
            return conexionesDisponibles.pop();
        } else {
            // Si no hay conexiones disponibles, se lanza una excepción
            throw new RuntimeException("No hay conexiones disponibles");
        }
    }

    /**
     * Devuelve una conexión al pool para que pueda ser reutilizada.
     * 
     * @param conexion La conexión que se va a devolver al pool
     */
    public synchronized void devolverConexion(Connection conexion) {
        conexionesDisponibles.push(conexion);  // Vuelve a añadir la conexión al pool
    }

    /**
     * Cierra todas las conexiones del pool.
     * Este método es utilizado para liberar recursos cuando ya no se necesita el pool.
     * 
     * @throws SQLException Si ocurre un error al cerrar las conexiones
     */
    public synchronized void cerrarConexiones() throws SQLException {
        while (!conexionesDisponibles.isEmpty()) {
            conexionesDisponibles.pop().close(); // Cierra cada conexión en el pool
        }
    }
}
