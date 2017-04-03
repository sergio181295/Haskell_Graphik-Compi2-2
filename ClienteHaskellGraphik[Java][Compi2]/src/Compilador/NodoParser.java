package Compilador;

import java.util.ArrayList;

public class NodoParser {
    public String nombre;
    public ArrayList<NodoParser> hijos = new ArrayList<NodoParser>();
    public String valor;
    public int contador = 0;
    
    //CONSTRUCTOR PARA NODOS HOJA
    public NodoParser(String nombre, String valor) {
        this.nombre = nombre;
        this.valor = valor;
    }
    //CONSTRUCTOR PARA NODOS INTERMEDIOS
    public NodoParser(String nombre, String valor, ArrayList<NodoParser> hijos) {
        this.nombre = nombre;
        this.valor = valor;
        this.hijos = hijos;
    }
    
    public void AgregarHijo(NodoParser n){
        this.hijos.add(n);
        contador++;
    }
    
    
}
