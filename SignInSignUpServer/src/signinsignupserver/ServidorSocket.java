/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signinsignupserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import modelo.Usuario;





/**
 *
 * @author 2dam
*/

public class ServidorSocket {

    private final int PUERTO = 9000;

    public void iniciarServidor() {
        ServerSocket servidor = null;

        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Esperando conexiones del cliente...");

            // Bucle infinito para aceptar múltiples conexiones de clientes
            while (true) {
                Socket socket = servidor.accept();
                System.out.println("Cliente conectado");

                // Pasar el socket a un nuevo hilo para manejar la conexión del cliente
                HilosServidor cliente = new HilosServidor(socket);
                cliente.start();
            }

        } catch (IOException e) {
            System.out.println("Error en el servidor: " + e.getMessage());
        } finally {
            try {
                if (servidor != null) {
                    servidor.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Servidor cerrado.");
        }
    }

    public static void main(String[] args) {
        ServidorSocket servidor = new ServidorSocket();
        servidor.iniciarServidor();
    }
}

