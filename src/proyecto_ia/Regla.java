/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proyecto_ia;

/**
 *
 * @author Jose
 */
public class Regla {
    String a1, a2, a3, a4, c;
    int n;
    boolean disparada;

    public Regla(int n, String a1, String a2, String a3, String a4, String c) {
        this.n = n;
        this.a1 = a1;
        this.a2 = a2;
        this.a3 = a3;
        this.a4 = a4;
        this.c = c;
        this.disparada = false;
    }
    
    @Override
    public String toString()
    {
        return "{Regla numero: " + this.n + ", A1 : " + this.a1 + ", A2 : " + this.a2 + ", A3 : " + this.a3 + ", A4 : " + this.a4 +  ", C : " + this.c + "}";
    }
    
    public Regla(){}
    
    public void setDisparada(boolean disparada){
        this.disparada = disparada;
    }
    
    public boolean getDisparada(){
        return this.disparada;
    }
    
    public int getN(){
        return n;
    }
    
    public void setN(int n)
    {
        this.n = n;
    }

    public String getA1() {
        return a1;
    }

    public void setA1(String a1) {
        this.a1 = a1;
    }

    public String getA2() {
        return a2;
    }

    public void setA2(String a2) {
        this.a2 = a2;
    }

    public String getA3() {
        return a3;
    }

    public void setA3(String a3) {
        this.a3 = a3;
    }

    public String getA4() {
        return a4;
    }

    public void setA4(String a4) {
        this.a4 = a4;
    }

    public String getC() {
        return c;
    }

    public void setC(String c) {
        this.c = c;
    }
    
    
}
