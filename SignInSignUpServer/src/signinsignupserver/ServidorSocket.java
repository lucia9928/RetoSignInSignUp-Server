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
import java.net.ServerSocket;
import java.net.Socket;
import modelo.Usuario;




/**
 *
 * @author 2dam
 */
public class ServidorSocket {

    private final int PUERTO = 5000;
    
    

    public void registrar() {
        java.net.ServerSocket servidor = null;
        Socket socket = null;
        ObjectInputStream entrada = null;
        ObjectOutputStream salida = null;
        try {
            servidor = new ServerSocket(PUERTO);
            System.out.println("Esperando conexiones del cliente...");
            socket = servidor.accept();
            System.out.println("Cliente conectado");
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());
            salida.writeObject("Conectado con el servidor");
            
            
            Usuario user= (Usuario) entrada.readObject();
            System.out.println(user.getApellido());
                    
            FactorySignableServer.getSignable().registrar(user);
          
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            try {
                if (servidor != null) {
                    servidor.close();
                }
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
            System.out.println("Fin servidor");
        }
    }

    public static void main(String[] args) {
        ServidorSocket servidor = new ServidorSocket();
        servidor.registrar();
    }
}

