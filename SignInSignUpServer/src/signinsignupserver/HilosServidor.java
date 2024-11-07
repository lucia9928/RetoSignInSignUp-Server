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

public class HilosServidor extends Thread {

    private Socket socket;
    private ObjectInputStream entrada;
    private ObjectOutputStream salida;
    private ServidorSocket servidorSocket;  

    
    public HilosServidor(Socket socket, ServidorSocket servidorSocket) {
        this.socket = socket;
        this.servidorSocket = servidorSocket;
    }

    @Override
    public void run() {
        try {
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            ActionUsers user = (ActionUsers) entrada.readObject();
            procesarComando(user);
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error con el cliente: " + e.getMessage());
        } finally {
            cerrarConexion();
        }
    }

    private void procesarComando(ActionUsers user) {
        try {
            if (Actions.REGISTER_REQUEST.equals(user.getAction())) {

                FactorySignableServer.getSignable().registrar(user);
                user.setAction(Actions.REGISTER_OK);

            }
            if (Actions.LOGGING_REQUEST.equals(user.getAction())) {

                FactorySignableServer.getSignable().login(user);
                user.setAction(Actions.LOGGING_OK);
            }
        } catch (Errores.UserAlreadyExistsException e) {
            user.setAction(Actions.RESGISTER_FAILED);
        } catch (Errores.DatabaseConnectionException e) {
            user.setAction(Actions.DATABASE_FAILED);
        } catch (Errores.AuthenticationFailedException e) {
            user.setAction(Actions.LOGGING_FAILED);
        } catch (Errores.PropertiesFileException e) {
            user.setAction(Actions.PROPERTIESFILE_FAILED);
        } catch (Errores.ServerConnectionException e) {
            user.setAction(Actions.SERVER_FAILED);
        } catch (Exception e) {
            enviarMensajeError(e);
        } finally {
            try {
                salida.writeObject(user);
            } catch (IOException ex) {
                Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
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
            Logger.getLogger(HilosServidor.class.getName()).log(Level.SEVERE, "Error al cerrar las conexiones: " + e.getMessage(), e);
        }

        servidorSocket.clienteDesconectado();
    }
}