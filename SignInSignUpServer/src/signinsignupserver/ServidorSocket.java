package signinsignupserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ResourceBundle;

/**
 * Servidor que maneja conexiones de clientes y les asigna un hilo para
 * gestionar cada solicitud. Controla el límite de conexiones activas y el
 * cierre de las conexiones cuando los clientes se desconectan.
 * 
 * @author Oscar
 */
public class ServidorSocket {

    // Carga el archivo de configuración de propiedades para la conexión con el cliente
    ResourceBundle fichConfig = ResourceBundle.getBundle("propiedades.connectionWithClient");
    private final int PUERTO = Integer.valueOf(fichConfig.getString("PUERTO"));  // Puerto de conexión del servidor
    private final int MAX_CONEXIONES = 10;  // Límite de conexiones activas simultáneas
    private int conexionesActivas = 0;  // Contador de conexiones activas

    /**
     * Método para iniciar el servidor. Establece un `ServerSocket` que escucha
     * en el puerto configurado y acepta conexiones entrantes.
     */
    public void iniciarServidor() {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {  // Se crea un servidor en el puerto configurado
            System.out.println("Esperando conexiones del cliente...");

            while (true) {
                // Acepta una nueva conexión entrante
                Socket socket = servidor.accept();

                synchronized (this) {
                    // Verifica si el número de conexiones activas ha alcanzado el máximo permitido
                    if (conexionesActivas >= MAX_CONEXIONES) {
                        System.out.println("Conexión rechazada: límite máximo de clientes alcanzado.");
                        socket.close();  // Si el límite se alcanza, rechaza la conexión cerrando el socket
                        continue;  // Continúa esperando nuevas conexiones
                    }

                    // Incrementa el contador de conexiones activas
                    conexionesActivas++;
                    System.out.println("Cliente conectado. Conexiones activas: " + conexionesActivas);
                }

                // Crea y lanza un nuevo hilo para manejar la conexión del cliente
                HilosServidor hiloCliente = new HilosServidor(socket, this);
                hiloCliente.start();  // Inicia el hilo para procesar las solicitudes del cliente
            }
        } catch (IOException e) {
            // Si ocurre un error al aceptar conexiones o en el servidor, lo maneja aquí
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    /**
     * Método sincronizado que se llama cuando un cliente se desconecta.
     * Decrementa el contador de conexiones activas.
     */
    public synchronized void clienteDesconectado() {
        conexionesActivas--;  // Decrementa el número de conexiones activas
        System.out.println("Cliente desconectado. Conexiones activas: " + conexionesActivas);
    }

    /**
     * Método principal que inicia el servidor.
     */
    public static void main(String[] args) {
        ServidorSocket servidor = new ServidorSocket();  // Crea una instancia del servidor
        servidor.iniciarServidor();  // Llama al método para iniciar el servidor
    }
}
