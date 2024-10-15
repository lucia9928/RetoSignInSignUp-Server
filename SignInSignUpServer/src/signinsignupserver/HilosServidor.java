/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signinsignupserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import modelo.Usuario;

/**
 *
 * @author 2dam
 */
public class HilosServidor extends Thread{
    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    public HilosServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            // Crear los streams de entrada y salida
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            // Comunicación con el cliente
            salida.writeObject("Introduce la contraseña:");
            Usuario user = (Usuario) entrada.readObject();
            System.out.println("Usuario registrado: " + user.getApellido());

            // Puedes procesar los datos del usuario, por ejemplo, guardar en una base de datos
            salida.writeObject("Registro completado exitosamente");

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            try {
                if (socket != null) {
                    socket.close();
                }
                if (entrada != null) {
                    entrada.close();
                }
                if (salida != null) {
                    salida.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Conexión con el cliente cerrada.");
        }
    }
    
}
