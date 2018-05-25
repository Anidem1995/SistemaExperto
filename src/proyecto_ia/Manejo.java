/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_ia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOException;

/**
 *
 * @author Jose
 */
public class Manejo {
     String Llave;
    String Nodo;
    int peso_toal = 0;

    public void escribir_Maestro() throws IOException {
        int n, antecedentes = 0, relleno = 0;
        long id;
        StringBuffer clave;
        System.out.println("Archivo Maestro");
        try (RandomAccessFile indice = new RandomAccessFile("Indice", "rw")) {
            try (RandomAccessFile archi = new RandomAccessFile("Maestro", "rw") //Instancia la creacion de una Archivo de acceso aleatorio con Read and Write permissions
                    ) {
                Scanner entrada = new Scanner(System.in);
                do {
                    relleno = 4;
                    id = archi.length() / 242;
                    System.out.println("Clave del registro");
                    Llave = entrada.next();
                    clave = new StringBuffer(Llave);
                    clave.setLength(1);
                    archi.writeChars(clave.toString());

                    indice.writeChars(clave.toString());
                    indice.writeLong(id);

                    do {
                        System.out.println("Cuantas relaciones tendra? ( 1 - 4 )");
                        antecedentes = entrada.nextInt();
                    } while (antecedentes < 1 || antecedentes > 4);

                    relleno = relleno - antecedentes;

                    while (antecedentes > 0) {
                        System.out.println("Antecedente no. " + antecedentes);
                        Nodo = entrada.next();
                        clave = new StringBuffer(Nodo);
                        clave.setLength(30);
                        archi.writeChars(clave.toString());

                        antecedentes--;
                    }

                    while (relleno > 0) {
                        Nodo = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";
                        clave = new StringBuffer(Nodo);
                        clave.setLength(30);
                        archi.writeChars(clave.toString());
                        relleno--;
                    }
                    
                    

                    do {
                        System.out.println("¿DESEA AGREGAR OTRO REGISTRO?\n1.- SI\n2.- NO");
                        n = entrada.nextInt();
                    } while (n < 0 || n > 2);
                } while (n == 1);
            }
        }

    }

    public void Leer_Secuencial_Maestro() throws IOException {
        long ap_actual, ap_final; // 8 bytes (64 bits)
        try (RandomAccessFile leer_archi = new RandomAccessFile("Maestro", "r")) {
            while ((ap_actual = leer_archi.getFilePointer()) != (ap_final = leer_archi.length())) {
                System.out.println("Clave del registro");
                System.out.println(leer_archi.readChar());
                prettyPrint(1, leer_archi);
                prettyPrint(2, leer_archi);
                prettyPrint(3, leer_archi);
                prettyPrint(4, leer_archi);
            }
            leer_archi.close();
        }
    }
    
    public void prettyPrint(int n, RandomAccessFile archivo)
    {
        System.out.println("Antecedente " + n);
        for(int i = 0; i < 30; i++)
        {
            try {
                System.out.println(archivo.readChar());
            } catch (IOException ex) {
                Logger.getLogger(Manejo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void Leer_Indice() throws IOException {
        long ap_actual, ap_final;
        try (RandomAccessFile read = new RandomAccessFile("Indice", "r")) {
            while ((ap_actual = read.getFilePointer()) != (ap_final = read.length())) {
                System.out.println(read.readChar());
                System.out.println(read.readLong());
            }
        }
    }
    
    public void forwardChaining()
    {
        
    }
    
    public void guardaHechos() throws IOException
    {
        try(RandomAccessFile hechos = new RandomAccessFile("Hechos", "rw"))
        {
            String decide;
            Scanner teclado = new Scanner(System.in);
            System.out.println("Ingrese los síntomas del paciente");
            do
            {
                String hecho = teclado.next();
                StringBuffer registro = new StringBuffer(hecho);
                registro.setLength(30);
                hechos.writeChars(hecho.toString());
                System.out.println("¿Desea agregar más síntomas?\n 1 - SI\n2 - NO");
                decide = teclado.next();
            }while(!decide.equals("1"));
        }
    }
    
    public void extraeHechos()
    {
        long ap_actual, ap_final;
    }
}
