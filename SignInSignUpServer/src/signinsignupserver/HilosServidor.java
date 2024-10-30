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
import utils.Errores;
import utils.UserAction;

public class HilosServidor extends Thread {

    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;

    public HilosServidor(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            UserAction comando = (UserAction) entrada.readObject();
            procesarComando(comando);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    private void procesarComando(UserAction comando) {
        try {
            switch (comando) {
                case REGISTER_REQUEST:
                    registrarUsuario();
                    break;
                case LOGIN_REQUEST:
                    iniciarSesion();
                    break;
                default:
                    salida.writeObject("Comando no reconocido.");
            }
        } catch (Exception e) {
            enviarMensajeError(e);
        }
    }

    private void registrarUsuario() throws IOException, ClassNotFoundException, Errores.UserAlreadyExistsException, Errores.DatabaseConnectionException, Exception {
        salida.writeObject("Ingrese sus datos para registrarse.");
        Usuario user = (Usuario) entrada.readObject();
        FactorySignableServer.getSignable().registrar(user);
        salida.writeObject("Registro exitoso. Ahora puede iniciar sesión.");
    }

    private void iniciarSesion() throws IOException, ClassNotFoundException, Errores.AuthenticationFailedException, Errores.DatabaseConnectionException, Exception {
        salida.writeObject("Ingrese sus credenciales para iniciar sesión.");
        Usuario user = (Usuario) entrada.readObject();
        Usuario usuarioAutenticado = FactorySignableServer.getSignable().login(user);
        salida.writeObject("Inicio de sesión exitoso. Bienvenido " + usuarioAutenticado.getNombre() + "!");
    }

    private void enviarMensajeError(Exception e) {
        try {
            salida.writeObject("Error: " + e.getMessage());
        } catch (IOException ioException) {
            Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, "Error enviando mensaje de error al cliente", ioException);
        }
    }

    private void cerrarConexion() {
        try {
            if (entrada != null) {
                entrada.close();
            }
            if (salida != null) {
                salida.close();
            }
            if (socket != null) {
                socket.close();
            }
            System.out.println("Conexión cerrada para el cliente.");
        } catch (IOException e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
