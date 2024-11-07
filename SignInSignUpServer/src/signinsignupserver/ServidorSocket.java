/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signinsignupserver;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ServidorSocket {

    private final int PUERTO = 5000;
    private final ExecutorService threadPool;

    public ServidorSocket(int maxConexiones) {
        this.threadPool = Executors.newFixedThreadPool(maxConexiones);
    }

    public void iniciarServidor() {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Esperando conexiones del cliente...");

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    Socket socket = servidor.accept();
                    System.out.println("Cliente conectado");
                    threadPool.submit(new HilosServidor(socket)); // Usa ExecutorService para manejar hilos
                } catch (IOException e) {
                    System.out.println("Error al aceptar conexión: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            cerrarServidor();
        }
    }

    private void cerrarServidor() {
        threadPool.shutdownNow();
        System.out.println("Todos los hilos del pool han sido cerrados.");
    }

    public static void main(String[] args) {
        int maxHilos = 2;
        ServidorSocket servidor = new ServidorSocket(maxHilos);
        servidor.iniciarServidor();
    }
}
