/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_ia;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Jose
 */
public class Proyecto_IA {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Manejo m = new Manejo();
        try {
           m.escribir_Maestro();
            System.out.println("Leyendo maestro");
            m.Leer_Secuencial_Maestro();
        } catch (IOException ex) {
            Logger.getLogger(Proyecto_IA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
