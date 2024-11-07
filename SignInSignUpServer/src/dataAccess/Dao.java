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
import modelo.ActionUsers;
import modelo.Signable;
import modelo.Usuario;
import utils.Errores;

/**
 *
 * @author 2dam
 */
public class Dao implements Signable {

    private ConnectionPool conexion;

    public Dao() throws Errores.PropertiesFileException {
        try {
            conexion = new ConnectionPool();
        } catch (SQLException e) {
            throw new Errores.PropertiesFileException("");
        }
    }

    @Override
    public ActionUsers registrar(ActionUsers user)
            throws Errores.UserAlreadyExistsException, Errores.DatabaseConnectionException {
        Connection connection = conexion.obtenerConexion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int idGenerado = 0;

        try {
            String sql = "SELECT login from res_users where login =?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUser().getEmail());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                throw new Errores.UserAlreadyExistsException("");
            }

            sql = "insert into res_partner(name, street, zip, city, email, phone) values(?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUser().getNombre() + "-" + user.getUser().getApellido());
            preparedStatement.setString(2, user.getUser().getCalle());
            preparedStatement.setString(3, user.getUser().getCodigoPostal());
            preparedStatement.setString(4, user.getUser().getCiudad());
            preparedStatement.setString(5, user.getUser().getEmail());
            preparedStatement.setString(6, user.getUser().getTelefono());
            preparedStatement.executeUpdate();

            sql = "select id from res_partner order by id desc limit 1";
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                idGenerado = resultSet.getInt(1);
            }

            sql = "insert into res_users (company_id, partner_id, active, login, password, notification_type) values (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, 1);
            preparedStatement.setInt(2, idGenerado);
            preparedStatement.setBoolean(3, user.getUser().getActivo());
            preparedStatement.setString(4, user.getUser().getEmail());
            preparedStatement.setString(5, user.getUser().getContrasena());
            preparedStatement.setString(6, "email");
            preparedStatement.executeUpdate();

            conexion.devolverConexion(connection);
        } catch (SQLException ex) {
            throw new Errores.DatabaseConnectionException("");
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
        return user;
    }

    @Override
    public ActionUsers login(ActionUsers user) throws Errores.DatabaseConnectionException, Errores.AuthenticationFailedException {
        Connection connection = conexion.obtenerConexion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int partnerid = 0;
        try {

            // Consulta SQL para buscar el usuario por email y contraseña
            String sql = "SELECT * FROM res_users WHERE login = ? AND password = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getUser().getEmail());
            preparedStatement.setString(2, user.getUser().getContrasena());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                partnerid = resultSet.getInt("partner_id");
                user.getUser().setActivo(resultSet.getBoolean("active"));
            } else {
                throw new Errores.AuthenticationFailedException("");
            }
            sql = "SELECT * FROM res_partner WHERE id = ?";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, partnerid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                String fullName = resultSet.getString("name");

                String[] nameParts = fullName.split(" ", 2);

                if (nameParts.length >= 2) {
                    user.getUser().setNombre(nameParts[0]);
                    user.getUser().setApellido(nameParts[1]);
                } else {
                    user.getUser().setNombre(nameParts[0]);
                    user.getUser().setApellido("");
                }
                user.getUser().setTelefono(resultSet.getString("phone"));
                user.getUser().setCalle(resultSet.getString("street"));
                user.getUser().setCodigoPostal(resultSet.getString("zip"));
                user.getUser().setCiudad(resultSet.getString("city"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
            throw new Errores.DatabaseConnectionException("Error al buscar el usuario en la bas de datos.");
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
        return user;
    }

}
