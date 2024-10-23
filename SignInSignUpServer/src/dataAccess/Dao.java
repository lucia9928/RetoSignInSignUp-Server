/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import modelo.Signable;
import modelo.Usuario;

/**
 *
 * @author 2dam
 */
public class Dao implements Signable {

    private static final String URL = "jdbc:postgresql://192.168.20.69:5432/odoo";
    private static final String USER = "odoo";
    private static final String PASSWORD = "abcd*1234";

    @Override
    public void registrar(Usuario user) {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a la base de datos de Odoo");

            // Incluir todos los campos en la consulta SQL
            String sql = "INSERT INTO res_partner (name, email) VALUES (?, ?)";
            preparedStatement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);

            // Asignar los valores del usuario al PreparedStatement
            preparedStatement.setString(1, user.getNombre() + " " + user.getApellido());
            preparedStatement.setString(2, user.getEmail());
            //preparedStatement.setDate(3, java.sql.Date.valueOf(user.getFechaNacimiento()));  // Convertir LocalDate a Date

            int rowsAffected = preparedStatement.executeUpdate();
            System.out.println("Filas afectadas: " + rowsAffected);

            if (rowsAffected > 0) {
                resultSet = preparedStatement.getGeneratedKeys();
                if (resultSet.next()) {
                    System.out.println("ID generado: " + resultSet.getInt(1));
                }
            }

        } catch (SQLException ex) {
            System.out.println("Error al insertar en la base de datos: " + ex.getMessage());
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                }
                if (connection != null) {
                    connection.close();
                }
            } catch (SQLException ex) {
                Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void login(Usuario user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
