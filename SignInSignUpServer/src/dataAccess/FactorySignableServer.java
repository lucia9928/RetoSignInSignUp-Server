/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataAccess;

import modelo.Signable;
import utils.Errores;

/**
 *
 * @author 2dam
 */
public class FactorySignableServer {
    
     public static Signable getSignable() throws Errores.PropertiesFileException{
        return new Dao();
       
    }
    
}
