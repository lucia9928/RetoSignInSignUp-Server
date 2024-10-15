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
    private static final String URL="jdbc:postgresql://192.168.20.72:5432/odoo";
    private static final String USER="odoo";
    private static final String PASSWORD="abcd*1234";
    
    
    @Override
    public void registrar(Usuario user){
        Connection connection=null;
        PreparedStatement preparedStatement=null;
        ResultSet resultSet=null;
        
        try {
            connection=DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexion exitosa a la base de datos de odoo");
            
            String sql="insert into res_partner(name) values(?)";
            preparedStatement=connection.prepareStatement(sql);
            preparedStatement.setString(1, user.getNombre());
            preparedStatement.executeUpdate();
            
            
            
            
            //while(resultSet.next()){
            //    String name=resultSet.getString("login");
            //    System.out.println("Nombre del Partner" + name);
          
            //}
            
            
            
        } catch (SQLException ex) {
            Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            try {
                if(resultSet!=null) resultSet.close();
                if(preparedStatement!=null)preparedStatement.close();
                if(connection!=null)connection.close();
            } catch (SQLException ex) {
                Logger.getLogger(Dao.class.getName()).log(Level.SEVERE, null, ex);
            }
 
        }
      
        
    }
    
}
