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

    public Dao() {
        try {
            conexion = new ConnectionPool();
        } catch (SQLException e) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, "Error al crear el pool de conexiones", e);
        }
    }

    @Override
    public ActionUsers registrar(ActionUsers user) {
        ActionUsers userr = null;
        Connection connection = conexion.obtenerConexion();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        int idGenerado = 0;

        try {

            System.out.println("Conexion exitosa a la base de datos de odoo");

            String sql = "insert into res_partner(name, street, zip, city, email, phone) values(?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setString(1, userr.getUser().getNombre() + "-" + userr.getUser().getApellido());
            preparedStatement.setString(2, userr.getUser().getCalle());
            preparedStatement.setString(3, userr.getUser().getCodigoPostal());
            preparedStatement.setString(4, userr.getUser().getCiudad());
            preparedStatement.setString(5, userr.getUser().getEmail());
            preparedStatement.setString(6, userr.getUser().getTelefono());
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
            preparedStatement.setBoolean(3, userr.getUser().getActivo());
            preparedStatement.setString(4, user.getUser().getEmail());
            preparedStatement.setString(5, userr.getUser().getContrasena());
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
        return userr;
    }

    @Override
    public ActionUsers login(Usuario user) throws Exception {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
