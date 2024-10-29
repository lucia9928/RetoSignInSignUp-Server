/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import java.sql.Connection;
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

    private ConnectionPool conexion;

    public Dao() {
        try {
            conexion = new ConnectionPool();
        } catch (SQLException e) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, "Error al crear el pool de conexiones", e);
        }
    }

    @Override
    public void registrar(Usuario user) {
        Connection connection = conexion.obtenerConexion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int idGenerado = 0;

        try {

            System.out.println("Conexion exitosa a la base de datos de odoo");

            String sql = "insert into res_partner(name, street, zip, city, email, phone) values(?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getNombre() + "-" + user.getApellido());
            preparedStatement.setString(2, user.getCalle());
            preparedStatement.setString(3, user.getCodPostal());
            preparedStatement.setString(4, user.getCiudad());
            preparedStatement.setString(5, user.getEmail());
            preparedStatement.setString(6, user.getTelefono());
            preparedStatement.executeUpdate();

            sql = "select id from res_partner order by id desc limit 1";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idGenerado = resultSet.getInt(1);
            }

            sql = "insert into res_users (company_id, partner_id, active, login, password, notification_type) values (?, ?, ?, ?, ?,?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, idGenerado);
            preparedStatement.setBoolean(3, user.getActivo());
            preparedStatement.setString(4, user.getEmail());
            preparedStatement.setString(5, user.getContrasena());
            preparedStatement.setString(6, "email");
            preparedStatement.executeUpdate();

            conexion.devolverConexion(connection);
        } catch (SQLException ex) {
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
    public Usuario login(Usuario user) throws Exception {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = conexion.obtenerConexion();

            String sql = "SELECT login, password FROM res_users WHERE login = ? AND password = ?;";
            preparedStatement = connection.prepareStatement(sql);

            preparedStatement.setString(1, user.getEmail());
            preparedStatement.setString(2, user.getContrasena());

            resultSet = preparedStatement.executeQuery();

        } catch (SQLException e) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, "Error al autenticar el usuario", e);
            throw e;

        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }

        return user;
    }

}
