/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package signinsignupserver;

import dataAccess.FactorySignableServer;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
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

            salida.writeObject("mensaje del servidor");
            Usuario user= (Usuario) entrada.readObject();
            System.out.println(user.getApellido());
            
            FactorySignableServer.getSignable().registrar(user);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } catch (Exception ex) {
            Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, null, ex);
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
