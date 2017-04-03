package Compilador.Haskell;

import Compilador.Analizador;
import Compilador.NodoParser;
import Tabla_Simbolos.NodoSimbolo;

public class RecoleccionH {
    
    public static String listaID = "";
    public static String ambito = "";
    public static Object Recolectar(NodoParser nodo){
        switch (nodo.nombre) {
            case "inicio":
                for (NodoParser n : nodo.hijos) {
                    Recolectar(n);
                }
                break;
            case "funcion":
                String nombre = nodo.hijos.get(0).valor;
                ambito = nombre;
                Recolectar(nodo.hijos.get(1));
                NodoSimbolo n = new NodoSimbolo(nombre, "number", "global",listaID,nodo.hijos.get(2),"nul","haskell","publico");
                Analizador.tablaH.insertar(n);
                break;
            case "ids":
                listaID = "";
                for (NodoParser n2 : nodo.hijos) {
                    listaID += n2.valor+",";
                }
                listaID = listaID.substring(0, listaID.length()-1); 
                break;
            default:
                throw new AssertionError();
        }
        return "nul";
    }
}
