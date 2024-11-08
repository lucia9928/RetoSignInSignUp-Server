package signinsignupserver;

import dataAccess.FactorySignableServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.ActionUsers;
import modelo.Usuario;
import utils.Errores;
import utils.Actions;
import static utils.Actions.REGISTER_REQUEST;

/**
 * Hilo que maneja la comunicación con un cliente en el servidor.
 * Procesa las solicitudes de registro y login, y responde con los resultados
 * correspondientes.
 * 
 * @author Oscar
 * @author Markel
 */
public class HilosServidor extends Thread {

    private Socket socket;  // Socket de la conexión con el cliente
    private ObjectInputStream entrada;  // Canal de entrada de datos del cliente
    private ObjectOutputStream salida;  // Canal de salida de datos hacia el cliente
    private ServidorSocket servidorSocket;  // Servidor que gestiona la conexión

    /**
     * Constructor que recibe el socket y el servidor asociado.
     * 
     * @param socket Socket para la conexión con el cliente.
     * @param servidorSocket Instancia del servidor que gestiona la conexión.
     */
    public HilosServidor(Socket socket, ServidorSocket servidorSocket) {
        this.socket = socket;
        this.servidorSocket = servidorSocket;
    }

    /**
     * Método principal del hilo. Se encarga de leer los datos enviados por el cliente,
     * procesar el comando y devolver la respuesta adecuada.
     */
    @Override
    public void run() {
        try {
            // Inicializa los flujos de entrada y salida con el cliente
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            // Lee el objeto ActionUsers enviado por el cliente
            ActionUsers user = (ActionUsers) entrada.readObject();
            // Procesa el comando recibido del cliente
            procesarComando(user);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            // Cierra la conexión después de procesar la solicitud
            cerrarConexion();
        }
    }

    /**
     * Procesa la solicitud recibida del cliente.
     * Dependiendo de la acción, llama al servicio adecuado para registrar o hacer login.
     * 
     * @param user Objeto ActionUsers que contiene la acción y datos del usuario.
     */
    private void procesarComando(ActionUsers user) {
        try {
            // Si la acción es de registro, se llama al método de registro
            if (Actions.REGISTER_REQUEST.equals(user.getAction())) {
                FactorySignableServer.getSignable().registrar(user);
                user.setAction(Actions.REGISTER_OK);  // Responde con éxito de registro
            }
            // Si la acción es de login, se llama al método de login
            if (Actions.LOGGING_REQUEST.equals(user.getAction())) {
                FactorySignableServer.getSignable().login(user);
                user.setAction(Actions.LOGGING_OK);  // Responde con éxito de login
            }
        } catch (Errores.UserAlreadyExistsException e) {
            user.setAction(Actions.RESGISTER_FAILED);  // Si el usuario ya existe, responde con error
        } catch (Errores.DatabaseConnectionException e) {
            user.setAction(Actions.DATABASE_FAILED);  // Error de conexión con la base de datos
        } catch (Errores.AuthenticationFailedException e) {
            user.setAction(Actions.LOGGING_FAILED);  // Error de autenticación al hacer login
        } catch (Errores.PropertiesFileException e) {
            user.setAction(Actions.PROPERTIESFILE_FAILED);  // Error en el archivo de propiedades
        } catch (Errores.ServerConnectionException e) {
            user.setAction(Actions.SERVER_FAILED);  // Error general en el servidor
        } catch (Exception e) {
            enviarMensajeError(e);  // Enviar un mensaje de error si ocurre una excepción no controlada
        } finally {
            try {
                // Envía el objeto ActionUsers de vuelta al cliente con la respuesta
                salida.writeObject(user);
            } catch (IOException ex) {
                Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Envía un mensaje de error al cliente si ocurre una excepción.
     * 
     * @param e La excepción que se ha producido.
     */
    private void enviarMensajeError(Exception e) {
        try {
            // Enviar el mensaje de error al cliente
            salida.writeObject("Error: " + e.getMessage());
        } catch (IOException ioException) {
            Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, "Error enviando mensaje de error al cliente", ioException);
        }
    }

    /**
     * Cierra los recursos utilizados para la conexión con el cliente.
     */
    private void cerrarConexion() {
        try {
            // Cierra los flujos de entrada y salida
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            // Cierra el socket de conexión con el cliente
            if (socket != null) {
                socket.close();
            }
            System.out.println("Conexión cerrada para el cliente.");
        } catch (IOException e) {
            Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, "Error al cerrar las conexiones: " + e.getMessage(), e);
        }

        // Notifica al servidor que el cliente se ha desconectado
        servidorSocket.clienteDesconectado();
    }
}
