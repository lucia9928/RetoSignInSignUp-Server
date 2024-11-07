package signinsignupserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorSocket {

    private final int PUERTO = 5000;
    private final int MAX_CONEXIONES = 10;  // Límite de conexiones activas
    private int conexionesActivas = 0;      // Contador de conexiones activas

    // Método para iniciar el servidor
    public void iniciarServidor() {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Esperando conexiones del cliente...");

            while (true) {
                Socket socket = servidor.accept();

                synchronized (this) {
                    // Verifica si el número de conexiones activas ya alcanzó el límite
                    if (conexionesActivas >= MAX_CONEXIONES) {
                        System.out.println("Conexión rechazada: límite máximo de clientes alcanzado.");
                        socket.close(); // Rechaza la conexión cerrando el socket
                        continue;
                    }

                    // Incrementa el contador de conexiones activas
                    conexionesActivas++;
                    System.out.println("Cliente conectado. Conexiones activas: " + conexionesActivas);
                }

                // Inicia un nuevo hilo para manejar la conexión del cliente
                HilosServidor hiloCliente = new HilosServidor(socket, this);
                hiloCliente.start();  // Lanza el hilo

            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        }
    }

    // Método sincronizado para decrementar el contador de conexiones
    public synchronized void clienteDesconectado() {
        conexionesActivas--;
        System.out.println("Cliente desconectado. Conexiones activas: " + conexionesActivas);
    }

    public static void main(String[] args) {
        ServidorSocket servidor = new ServidorSocket();
        servidor.iniciarServidor();
    }
}
