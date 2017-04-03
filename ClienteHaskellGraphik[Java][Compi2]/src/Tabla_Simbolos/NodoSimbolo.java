package Tabla_Simbolos;

import Compilador.NodoParser;
import java.util.ArrayList;

public class NodoSimbolo {
    public String tipo = "nul";
    public String nombre = "nul";
    public String ambito = "nul";
    public String clase = "nul";
    public String visibilidad = "publico";
    public Dato dato = null;
    public String rol = "nul";
    public String parametros = "nul";
    public String sobrecarga = "nul";
    public NodoParser instrucciones = null;
    public boolean eliminado = false;

    //CONSTRUCTOR PARA VARIABLES CON VALOR INICIAL
    public NodoSimbolo(String xnombre,  String xtipo, String xambito, Dato xdato, String clase, String visivilidad) {
        this.nombre = xnombre;
        this.ambito = xambito;
        this.tipo = xtipo;
        this.dato = xdato;
        this.rol = "variable";
        this.clase = clase;
        this.visibilidad = visivilidad;
    }

    //CONSTRUCTOR PARA METODOS
    public NodoSimbolo(String xnombre, String xtipo, String xambito, String xparametros, NodoParser xinstrucciones, String xsobrecarga, String clase, String visivilidad) {
        this.nombre = xnombre;
        this.tipo = xtipo;
        this.ambito = xambito;
        this.parametros = xparametros;
        this.sobrecarga = xsobrecarga;
        this.instrucciones = xinstrucciones;
        this.rol = "funcion";
        this.clase = clase;
        this.visibilidad = visivilidad;
    }
    
}
