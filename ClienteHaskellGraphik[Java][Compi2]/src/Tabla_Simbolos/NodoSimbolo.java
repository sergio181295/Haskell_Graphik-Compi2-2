package Tabla_Simbolos;

import Compilador.NodoParser;
import Compilador.token;
import java.util.ArrayList;

public class NodoSimbolo {
    public String tipo = "nul";
    public String nombre = "nul";
    public String ambito = "nul";
    public String clase = "nul";
    public String visibilidad = "publico";
    public String valor = null;
    public String rol = "nul";
    public String parametros = "nul";
    public String sobrecarga = "nul";
    public NodoParser instrucciones = null;
    public boolean eliminado = false;

    public boolean esLista = false;
    public ArrayList<NodoSimbolo> lista = new ArrayList<>();
    
    public ArrayList<Integer> dimensiones = new ArrayList<>();
    public int contDim = 0;

    //CONSTRUCTOR PARA VARIABLES CON VALOR INICIAL
    public NodoSimbolo(String xnombre,  String xtipo, String xambito, String xdato, String clase, String visibilidad) {
        this.nombre = xnombre;
        this.ambito = xambito;
        this.tipo = xtipo;
        this.valor = xdato;
        this.rol = "variable";
        this.clase = clase;
        this.visibilidad = visibilidad;
    }

    //CONSTRUCTOR PARA METODOS
    public NodoSimbolo(String xnombre, String xtipo, String xambito, String xparametros, NodoParser xinstrucciones, String xsobrecarga, String clase, String visibilidad) {
        this.nombre = xnombre;
        this.tipo = xtipo;
        this.ambito = xambito;
        this.parametros = xparametros;
        this.sobrecarga = xsobrecarga;
        this.instrucciones = xinstrucciones;
        this.rol = "funcion";
        this.clase = clase;
        this.visibilidad = visibilidad;
    }
    
    public NodoSimbolo() {
    }
    
    public NodoSimbolo clonar(){
        NodoSimbolo d = new NodoSimbolo();
        d.tipo = this.tipo;
        d.nombre = this.nombre;
        d.ambito = this.ambito;
        d.clase = this.clase; 
        d.visibilidad = this.visibilidad;
        d.valor = this.valor;
        d.esLista = this.esLista;
        d.contDim = this.contDim;
        if(!lista.isEmpty()){
            for (NodoSimbolo d1 : this.lista) {
                NodoSimbolo aux = d1.clonar();
                d.lista.add(aux);
            }
        }
        if(!dimensiones.isEmpty()){
            for (Integer dim : this.dimensiones) {
                d.dimensiones.add(dim);
            }
        }
        return d;
    }

    public NodoSimbolo(String valor) {
        this.valor = valor;
    }
    
    public NodoSimbolo(float valor) {
        this.valor = String.valueOf(valor);
    }
    
    public NodoSimbolo(boolean valor) {
        if(valor){
            this.valor = token.verdadero;
        }else{
            this.valor = token.falso;
        }
    }
    
    public void addDato(NodoSimbolo d){
        esLista = true;
        this.tipo = d.tipo;
        lista.add(d);
    }
    
    public boolean esNumero(){
        if(this.esLista){
            for (NodoSimbolo dato : this.lista) {
                if(!dato.esNumero()){
                    return false;
                }
            }
        }else{
            if(!this.tipo.equals(token.decimal) && !this.tipo.equals(token.entero)  && !this.tipo.equals("numero")){
                    return false;
            }
        }
        return true;
    }
    
    public boolean esCadena(){
        if(this.esLista){
            for (NodoSimbolo dato : this.lista) {
                if(!dato.esCadena()){
                    return false;
                }
            }
        }else{
            if(!this.tipo.equals(token.cadena)){
                    return false;
            }
        }
        return true;
    }
    
    public boolean esCaracter(){
        if(this.esLista){
            for (NodoSimbolo dato : this.lista) {
                if(!dato.esCaracter()){
                    return false;
                }
            }
        }else{
            if(!this.tipo.equals(token.caracter)){
                    return false;
            }
        }
        return true;
    }
    
    public boolean esBool(){
        if(this.esLista){
            for (NodoSimbolo dato : this.lista) {
                if(!dato.esBool()){
                    return false;
                }
            }
        }else{
            if(!this.tipo.equals(token.bool)){
                    return false;
            }
        }
        return true;
    }
    
    public boolean esCadyCaracter(){
        if(this.esCadena() && this.esCaracter()){
            return true;
        }else{
            return false;
        }
    }
    
    public Float tryParseFloat() {
        Float retVal;
        try {
            retVal = Float.parseFloat(this.valor);
        } catch (NumberFormatException nfe) {
            retVal = null;
        }
        return retVal;
    }
    
    public Float getMin(float val){
        if(!this.esNumero()){
            return null;
        }
        if(this.esLista){
            for (NodoSimbolo d2 : this.lista) {
                val = d2.getMin(val);
            }
            return val;
        }else{
            float v2 = Float.parseFloat(this.valor);
            if(v2<val){
                return v2;
            }else{
                return val;
            }
        }
    }
    
    public Float getMax(float val){
        if(!this.esNumero()){
            return null;
        }
        if(this.esLista){
            for (NodoSimbolo d2 : this.lista) {
                val = d2.getMax(val);
            }
            return val;
        }else{
            float v2 = Float.parseFloat(this.valor);
            if(v2>val){
                return v2;
            }else{
                return val;
            }
        }
    }
    
    public Float Sum(){
        if(!this.esNumero()){
            return null;
        }
        float salida = 0;
        if(this.esLista){
            for (NodoSimbolo d2 : this.lista) {
                salida += d2.Sum();
            }
            return salida;
        }else{
            float v2 = Float.parseFloat(this.valor);
            return v2;
        }
    }
    
    public Float Mult(){
        if(!this.esNumero()){
            return null;
        }
        float salida = 1;
        if(this.esLista){
            for (NodoSimbolo d2 : this.lista) {
                salida *= d2.Mult();
            }
            return salida;
        }else{
            float v2 = Float.parseFloat(this.valor);
            return v2;
        }
    }
    
    public String toString(){
        String salida;
        if(this.esLista){
            salida = "";
            if(this.esCaracter()){
                for (NodoSimbolo dato : this.lista) {
                    salida += dato.toString();
                }
                return salida;
            }
            for (NodoSimbolo dato : this.lista) {
                salida += ",";
                salida += dato.toString();
            }
            salida += "]";
            salida = salida.replaceFirst(",", "[");
        }else{
            if(this.esCadena()){
                salida = this.valor;
                return salida;
            }
            salida = this.valor;
        }
        return salida;
    }
    
    public String toStringH(){
        String salida;
        if(this.esLista){
            salida = "";
            if(this.esCaracter()){
                salida += "\"";
                for (NodoSimbolo dato : this.lista) {
                    salida += dato.toString();
                }
                salida += "\"";
                return salida;
            }
            for (NodoSimbolo dato : this.lista) {
                salida += ",";
                salida += dato.toString();
            }
            salida += "]";
            salida = salida.replaceFirst(",", "[");
        }else{
            if(this.esCadena()){
                salida = "\""+this.valor+"\"";
                return salida;
            }
            salida = this.valor;
        }
        return salida;
    }
    
    public NodoSimbolo getAtributo(String nombre){
        for (NodoSimbolo d : lista) {
            if(d.nombre.equals(nombre)){
                return d;
            }
        }
        return null;
    }
    
    public NodoSimbolo getDeLista(ArrayList<Integer> coordenadas){
        int tam = dimensiones.size()-1;
        int pos = coordenadas.get(tam);
        for (int i = tam-1; i >= 0; i--) {
            pos = pos * dimensiones.get(i);
            pos = pos + coordenadas.get(i);
        }
        return lista.get(pos);
    }
}
