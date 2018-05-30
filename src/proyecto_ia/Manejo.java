/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_ia;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.LinkedList;
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
                    id = archi.length() / 304;
                    System.out.println("Clave del registro");
                    Llave = entrada.next();
                    clave = new StringBuffer(Llave);
                    clave.setLength(2);
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
                    
                    System.out.println("Ingrese el cosecuente de la regla");
                    Nodo = entrada.next();
                    clave = new StringBuffer(Nodo);
                    clave.setLength(30);
                    archi.writeChars(clave.toString());
                    System.out.println(archi.length());
                    
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
                System.out.println(leer_archi.readChar() + "" + leer_archi.readChar());
                prettyPrint("Antecedente 1", leer_archi);
                prettyPrint("Antecedente 2", leer_archi);
                prettyPrint("Antecedente 3", leer_archi);
                prettyPrint("Antecedente 4", leer_archi);
                prettyPrint("Consecuente", leer_archi);
            }
            leer_archi.close();
        }
    }
    
    public void prettyPrint(String n, RandomAccessFile archivo)
    {
        System.out.println(n);
        for(int i = 0; i < 30; i++)
        {
            try {
                String str = Character.toString(archivo.readChar());
                if(!str.equals(""))
                    System.out.println(str.replaceAll("\\s+", ""));
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
    
    public void forwardChaining() throws IOException
    {
        Manejo m = new Manejo();
        try {
            LinkedList<String> bh;
            LinkedList<Regla> bc;
            LinkedList<Regla> cc;
            String seleccion = "";
            String meta = "Erliquiosis";
            //m.escribir_Maestro();
            //System.out.println("Leyendo maestro");
            //m.Leer_Secuencial_Maestro();
            //m.guardaHechos();
            bh = m.extraeHechos();
            String bhs = "{";
            for(int i = 0; i < bh.size(); i++)
            {
                bhs += bh.get(i) + ", ";
            }
            bhs += "}";
            System.out.println("Base de hechos: \n" + bhs);
            bc = m.extraerReglas();
            cc = m.generaconjuntoConflicto(bh, bc);
            String ccs = "[";
            for(int j = 0; j < cc.size(); j++)
            {
                ccs += cc.get(j).toString() + "\n";
            }
            ccs += "]";
            boolean primerLanzamiento = true;
            boolean contenida = contenida(bh, meta);
            
            while(cc.size()!= 0 && !contenida)
            {
                cc = m.generaconjuntoConflicto(bh, bc);
                String ccss = "[";
                for(int j = 0; j < cc.size(); j++)
                {
                    ccs += cc.get(j).toString() + "\n";
                }
                ccss += "]";
                if(cc.size() != 0)
                {
                    if(primerLanzamiento)
                    {
                        seleccion = m.seleccionaRegla(cc, primerLanzamiento);
                        primerLanzamiento = false;
                    }
                    else seleccion = m.seleccionaRegla(cc, primerLanzamiento);
                    bh.add(seleccion);
                    String bhss = "{";
                    for(int i = 0; i < bh.size(); i++)
                    {
                        bhs += bh.get(i) + ", ";
                    }
                    bhs += "}";
                    System.out.println("Base de hechos: \n" + bhs);
                    contenida = contenida(bh, meta);
                }
            }
            if(contenida)
                System.out.println("El paciente padece erliquiosis");
        } catch (IOException ex) {
            Logger.getLogger(Proyecto_IA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public boolean contenida(LinkedList<String> bh, String meta)
    {
        if(bh.size() != 0)
        {
            for(int i = 0; i < bh.size(); i++)
            {
                if(meta.indexOf(bh.get(i)) == 0)
                    return true;
            }
        }
        return false;
    }
    
    public LinkedList<Regla> generaconjuntoConflicto(LinkedList<String> bh, LinkedList<Regla> bc)
    {
        LinkedList<Regla> cc = new LinkedList<>();
        for(int i = 0; i < bc.size(); i++)
        {
            int antecedentes = 0;
            int coincidencias = 0;
            if(bc.get(i).getA1().indexOf("xxx") == -1)
                antecedentes++;
            if(bc.get(i).getA2().indexOf("xxx") == -1)
                antecedentes++;
            if(bc.get(i).getA3().indexOf("xxx") == -1)
                antecedentes++;
            if(bc.get(i).getA4().indexOf("xxx") == -1)
                antecedentes++;
            
            for(int j = 0; j < bh.size(); j++)
            {
                String h;
                String a;
                h = bh.get(j);
                a = bc.get(i).getA1();
                
                if(h.indexOf(a) == 0)
                    coincidencias++;
                a = bc.get(i).getA2();
                if(h.indexOf(a) == 0)
                    coincidencias++;
                a = bc.get(i).getA3();
                if(h.indexOf(a) == 0)
                    coincidencias++;
                a = bc.get(i).getA4();
                if(h.indexOf(a) == 0)
                    coincidencias++;
            }
            if(antecedentes == coincidencias)
                cc.add(bc.get(i));
        }
        return cc;
    }
    
    public LinkedList<Regla> extraerReglas() throws IOException
    {
        LinkedList<Regla> cc = new LinkedList<>();
        try(RandomAccessFile reader = new RandomAccessFile("Maestro", "r"))
        {
            Regla regla = new Regla();
            int pos = 0;
            int n = 1;
            String s = "";
            long ap_actual, ap_final;
            reader.seek(0);
            reader.readChar();
            reader.readChar();
            while((ap_actual = reader.getFilePointer()) != (ap_final = reader.length()))
            {
                s = readRule(reader);
                //System.out.println(s);
                switch(pos)
                {
                    case 0:
                        regla.setA1(s);
                        break;
                    case 1:
                        regla.setA2(s);
                        break;
                    case 2:
                        regla.setA3(s);
                        break;
                    case 3:
                        regla.setA4(s);
                        break;
                    case 4:
                        regla.setC(s);
                        regla.setN(n);
                        n++;
                        cc.add(regla);
                        break;
                }
                if(pos < 4)
                    pos++;
                else
                {
                    pos = 0;
                    long last = reader.getFilePointer();
                    if(last != reader.length())
                    {
                        reader.readChar();
                        reader.readChar();
                    }
                    regla = new Regla();
                }
            }
            reader.close();
        }
        return cc;
    }
    
    public String seleccionaRegla(LinkedList<Regla> cc, boolean primerLanzamiento)
    {
        if(cc.size() > 0)
        {
            if(cc.size() == 1)
            {
                Regla seleccion = cc.get(0);
                cc.get(0).setDisparada(true);
                
                return seleccion.getC();
            }
            else
            {
                Regla seleccion = cc.get(0);
                if(primerLanzamiento)
                    cc.get(0).setDisparada(true);
                for(int i = 1; i < cc.size(); i++)
                {
                    if(seleccion.getN() < cc.get(i).getN() && !cc.get(i).getDisparada())
                    seleccion = cc.get(i);
                    seleccion.setDisparada(true);
                }
                return seleccion.getC();
            }
        }
        return null;
    }
    
    public String readRule(RandomAccessFile reader)
    {
        String str = "";
        for(int i = 0; i < 30; i++)
        {
            try {
                str += Character.toString(reader.readChar());
            } catch (IOException ex) {
                Logger.getLogger(Manejo.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return str;
    }
    
    public void guardaHechos() throws IOException
    {
        try(RandomAccessFile hechos = new RandomAccessFile("Hechos", "rw"))
        {
            int decide = 1;
            Scanner teclado = new Scanner(System.in);
            do
            {
                System.out.println("Ingrese los síntomas del paciente");
                String hecho = teclado.next();
                StringBuffer registro = new StringBuffer(hecho);
                registro.setLength(30);
                hechos.writeChars(registro.toString());
                System.out.println("¿Desea agregar más síntomas?\n1 - SI\n2 - NO");
                decide = teclado.nextInt();
            }while(decide == 1);
        }
    }
    
    public LinkedList extraeHechos() throws IOException
    {
        LinkedList<String> lista_hechos = new LinkedList<>();
        long ap_actual, ap_final;
        
        try(RandomAccessFile leer_bh = new RandomAccessFile("Hechos", "r"))
        {
            while((ap_actual = leer_bh.getFilePointer()) != (ap_final = leer_bh.length()))
            {
                String hecho = readRule(leer_bh);
                lista_hechos.add(hecho);
            }
        }
        return lista_hechos;
    }
}
