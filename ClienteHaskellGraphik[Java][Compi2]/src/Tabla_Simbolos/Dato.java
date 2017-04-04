package Tabla_Simbolos;

import Compilador.token;
import java.util.ArrayList;

public class Dato {
    public String tipo = "nul";
    public String valor = "nul";
    public String nombre = "nul";
    public String clase = "nul";
    
    public boolean esLista = false;
    public ArrayList<Dato> lista = new ArrayList<>();
    
    public ArrayList<Integer> dimensiones = new ArrayList<>();
    public int contDim = 0;
    
    public Dato() {
    }
    
    public Dato clonar(){
        Dato d = new Dato();
        d.tipo = this.tipo;
        d.valor = this.valor;
        d.nombre = this.nombre;
        d.esLista = this.esLista;
        d.contDim = this.contDim;
        d.clase = this.clase;
        if(!lista.isEmpty()){
            for (Dato d1 : this.lista) {
                Dato aux = d1.clonar();
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

    public Dato(String valor) {
        this.valor = valor;
    }
    
    public Dato(float valor) {
        this.valor = String.valueOf(valor);
    }
    
    public Dato(boolean valor) {
        if(valor){
            this.valor = token.verdadero;
        }else{
            this.valor = token.falso;
        }
    }
    
    public void addDato(Dato d){
        esLista = true;
        this.tipo = d.tipo;
        lista.add(d);
    }
    
    public boolean esNumero(){
        if(this.esLista){
            for (Dato dato : this.lista) {
                if(!dato.esNumero()){
                    return false;
                }
            }
        }else{
            if(!this.tipo.equals(token.decimal) && !this.tipo.equals(token.entero)){
                    return false;
            }
        }
        return true;
    }
    
    public boolean esCadena(){
        if(this.esLista){
            for (Dato dato : this.lista) {
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
            for (Dato dato : this.lista) {
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
            for (Dato dato : this.lista) {
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
            for (Dato d2 : this.lista) {
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
            for (Dato d2 : this.lista) {
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
            for (Dato d2 : this.lista) {
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
            for (Dato d2 : this.lista) {
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
                for (Dato dato : this.lista) {
                    salida += dato.toString();
                }
                return salida;
            }
            for (Dato dato : this.lista) {
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
                for (Dato dato : this.lista) {
                    salida += dato.toString();
                }
                salida += "\"";
                return salida;
            }
            for (Dato dato : this.lista) {
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
    
    public Dato getAtributo(String nombre){
        for (Dato d : lista) {
            if(d.nombre.equals(nombre)){
                return d;
            }
        }
        return null;
    }
    
    public Dato getDeLista(ArrayList<Integer> coordenadas){
        int tam = dimensiones.size()-1;
        int pos = coordenadas.get(tam);
        for (int i = tam-1; i >= 0; i--) {
            pos = pos * dimensiones.get(i);
            pos = pos + coordenadas.get(i);
        }
        return lista.get(pos);
    }
}
