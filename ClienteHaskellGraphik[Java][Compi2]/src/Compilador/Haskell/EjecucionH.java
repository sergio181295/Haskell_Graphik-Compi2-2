package Compilador.Haskell;

import Compilador.Analizador;
import Compilador.NodoParser;
import Tabla_Simbolos.Dato;
import Tabla_Simbolos.NodoSimbolo;
import java.util.ArrayList;
import java.util.Collections;

public class EjecucionH {
    
    public static String pactivos = "";
    public static String imprimir = "";
    public static String retornable = "";
    static int cif = 0;
    static int celse = 0;
    static int ccase = 0;
    static int cfun = 0;
    
    //NUEVAS
    public static ArrayList<String> ambitos = new ArrayList<String>();
    static String valorCase = "";
    static boolean salir = false;
    public static String ultimoValor = "";
    public static Dato Ejecutar(NodoParser nodo){
        switch (nodo.nombre) {
            case "codigo":
                for (NodoParser n : nodo.hijos) {
                    Ejecutar(n);
                }
                break;
            case "SI":{
                cif++;
                String si = "si" + cif;
                ambitos.add(si);
                boolean condicion = Boolean.valueOf(Ejecutar(nodo.hijos.get(0)).toStringH());
                if(condicion){
                    Ejecutar(nodo.hijos.get(1));
                }else if(nodo.contador == 3){
                    celse++;
                    ambitos.add("else"+celse);
                    Ejecutar(nodo.hijos.get(2));
                    ambitos.remove(ambitos.size()-1);
                }
                ambitos.remove(ambitos.size()-1);
                break;
            }
            case "SELECCIONA":
                valorCase = Ejecutar(nodo.hijos.get(0)).toStringH();
                Ejecutar(nodo.hijos.get(1));
                break;
            case "casos":
                for (NodoParser n : nodo.hijos) {
                    Ejecutar(n);
                    if(salir){
                        salir = false;
                        break;
                    }
                    
                }
                break;
            case "caso":{
                ccase++;
                ambitos.add("case"+ccase);
                String valor = Ejecutar(nodo.hijos.get(0)).toStringH();
                if(valor.equals(valorCase)){
                    Ejecutar(nodo.hijos.get(1));
                    salir = true;
                }
                ambitos.remove(ambitos.size()-1);
                break;
            }
            case "lista":{
                Dato d = Ejecutar(nodo.hijos.get(0));
                imprimir = d.toStringH();
                return d;
            }
            case "listaExp":{
                Dato d = new Dato();
                for (NodoParser n : nodo.hijos) {
                    d.addDato(Ejecutar(n));
                }
                return d;
            }    
            case "llamada":{
                String nombre = nodo.hijos.get(0).valor;
                cfun++;
                String fun = nombre+cfun;
                ambitos.add(fun);
                pactivos = Analizador.tablaH.getParametros(nombre, "global","nul","haskell");
                Ejecutar(nodo.hijos.get(1));//ENVIAMOS PARAMETROS
                NodoParser ins = Analizador.tablaH.getInstrucciones(nombre, "global", "nul", "haskell");
                Ejecutar(ins);
                Analizador.tablaH.eliminarPorAmbito(fun);
                ambitos.remove(ambitos.size()-1);
                return new Dato(imprimir);
            }
            case "enviados":{
                String[] p = pactivos.split(",");
                String ambito = ambitos.get(ambitos.size()-1);
                for(int i = 0;i < nodo.contador;i++){
                    Dato d = Ejecutar(nodo.hijos.get(i));
                    NodoSimbolo n = new NodoSimbolo(p[i], "number", ambito, d,"haskell","publico");
                    Analizador.tablaH.insertar(n);
                }
                return new Dato("nul");
            }
            case "funPropias":{
                String fun = nodo.hijos.get(0).nombre;
                if(fun.equals("CALCULAR")){
                    Dato d = Ejecutar(nodo.hijos.get(1));
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("SUCC")){
                    float val = Float.parseFloat(Ejecutar(nodo.hijos.get(1)).valor)+1;
                    Dato d = new Dato(String.valueOf(val));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("DECC")){
                    float val = Float.parseFloat(Ejecutar(nodo.hijos.get(1)).valor)-1;
                    Dato d = new Dato(String.valueOf(val));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("MIN")){
                    float v1 = Ejecutar(nodo.hijos.get(1)).getMin(320000);
                    Dato d = new Dato(String.valueOf(v1));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("MAX")){
                    float v1 = Ejecutar(nodo.hijos.get(1)).getMax(-320000);
                    Dato d = new Dato(String.valueOf(v1));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("SUM")){
                    float v1 = Ejecutar(nodo.hijos.get(1)).Sum();
                    Dato d = new Dato(String.valueOf(v1));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("PRODUCT")){
                    float v1 = Ejecutar(nodo.hijos.get(1)).Mult();
                    Dato d = new Dato(String.valueOf(v1));
                    d.tipo = "numero";
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("REVERS")){
                    Dato d = new Dato();
                    Dato v = Ejecutar(nodo.hijos.get(1));
                    if(v.esCadena()){
                        Dato aux = new Dato();
                        int tam = v.valor.length();
                        for (int i = 0; i < tam; i++) {
                            Dato aux2 = new Dato(String.valueOf(v.valor.charAt(i)));
                            aux2.tipo = "caracter";
                            aux.addDato(aux2);
                        }
                        int x = aux.lista.size()-1;
                        for(int i = x;i>=0;i--){
                            d.addDato(aux.lista.get(i));
                        }
                    }else if(v.esLista){
                        int x = v.lista.size()-1;
                        for(int i = x;i>=0;i--){
                            d.addDato(v.lista.get(i));
                        }
                    }
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("IMPR")){
                    Dato d = new Dato();
                    Dato v = Ejecutar(nodo.hijos.get(1));
                    if(v.esCadena()){
                        Dato aux = new Dato();
                        int tam = v.valor.length();
                        for (int i = 0; i < tam; i++) {
                            Dato aux2 = new Dato(String.valueOf(v.valor.charAt(i)));
                            aux2.tipo = "caracter";
                            aux.addDato(aux2);
                        }
                        int x = aux.lista.size();
                        for(int i = 0;i<x;i++){
                            d.addDato(aux.lista.get(i));
                            i++;
                        }
                    }else if(v.esLista){
                        int x = v.lista.size();
                        for(int i = 0;i<x;i++){
                            d.addDato(v.lista.get(i));
                            i++;
                        }
                    }
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("PAR")){
                    Dato d = new Dato();
                    Dato v = Ejecutar(nodo.hijos.get(1));
                    if(v.esCadena()){
                        Dato aux = new Dato();
                        int tam = v.valor.length();
                        for (int i = 0; i < tam; i++) {
                            Dato aux2 = new Dato(String.valueOf(v.valor.charAt(i)));
                            aux2.tipo = "caracter";
                            aux.addDato(aux2);
                        }
                        int x = aux.lista.size();
                        for(int i = 1;i<x;i++){
                            d.addDato(aux.lista.get(i));
                            i++;
                        }
                    }else if(v.esLista){
                        int x = v.lista.size();
                        for(int i = 1;i<x;i++){
                            d.addDato(v.lista.get(i));
                            i++;
                        }
                    }
                    imprimir = d.toStringH();
                    return d;
                }else if(fun.equals("ASC")){
                    Dato d = Ejecutar(nodo.hijos.get(1));
                    if(d.esNumero()){
                        ArrayList<Float> l = new ArrayList<>();
                        for (Dato n : d.lista) {
                            l.add(n.tryParseFloat());
                        }
                        Collections.sort(l);
                        Dato nuevo = new Dato();
                        for (Float s : l) {
                            Dato da = new Dato(String.valueOf(s));
                            nuevo.addDato(da);
                        }
                        imprimir = nuevo.toStringH();
                        return nuevo;
                    }else if(d.esCadena()){
                        ArrayList<String> l = new ArrayList<>();
                        int tam = d.valor.length();
                        for (int i = 0; i < tam; i++) {
                            l.add(String.valueOf(d.valor.charAt(i)));
                        }
                        Collections.sort(l);
                        Dato nuevo = new Dato();
                        for (String s : l) {
                            Dato da = new Dato(s);
                            nuevo.addDato(da);
                        }
                        imprimir = nuevo.toStringH();
                        return nuevo;
                    }
                    ArrayList<String> l = new ArrayList<>();
                    for (Dato n : d.lista) {
                        l.add(n.valor);
                    }
                    Collections.sort(l);
                    Dato nuevo = new Dato();
                    for (String s : l) {
                        Dato da = new Dato(s);
                        nuevo.addDato(da);
                    }
                    imprimir = nuevo.toStringH();
                    return nuevo;
                }else if(fun.equals("DESC")){
                    Dato d = Ejecutar(nodo.hijos.get(1));
                    if(d.esNumero()){
                        ArrayList<Float> l = new ArrayList<>();
                        for (Dato n : d.lista) {
                            l.add(n.tryParseFloat());
                        }
                        Collections.sort(l);
                        Dato nuevo = new Dato();
                        int x = l.size()-1;
                        for (int i = x; i >= 0; i--) {
                            Dato da = new Dato(String.valueOf(l.get(i)));
                            nuevo.addDato(da);
                        }
                        imprimir = nuevo.toStringH();
                        return nuevo;
                    }else if(d.esCadena()){
                        ArrayList<String> l = new ArrayList<>();
                        int tam = d.valor.length();
                        for (int i = 0; i < tam; i++) {
                            l.add(String.valueOf(d.valor.charAt(i)));
                        }
                        Collections.sort(l);
                        Dato nuevo = new Dato();
                        int x = l.size()-1;
                        for (int i = x; i >= 0; i--) {
                        Dato da = new Dato(l.get(i));
                            nuevo.addDato(da);
                        }
                        imprimir = nuevo.toStringH();
                        return nuevo;
                    }
                    ArrayList<String> l = new ArrayList<>();
                    for (Dato n : d.lista) {
                        l.add(n.valor);
                    }
                    Collections.sort(l);
                    Dato nuevo = new Dato();
                    int x = l.size()-1;
                    for (int i = x; i >= 0; i--) {
                        Dato da = new Dato(l.get(i));
                        nuevo.addDato(da);
                    }
                    imprimir = nuevo.toStringH();
                    return nuevo;
                }else if(fun.equals("LENGTH")){
                    Dato d = Ejecutar(nodo.hijos.get(1));
                    Dato n;
                    if(d.esCadena()){
                        n = new Dato(String.valueOf(d.valor.length()));
                    }else{
                        n = new Dato(String.valueOf(d.lista.size()));
                    }
                    n.tipo = "numero";
                    imprimir = n.toStringH();
                    return n;
                }else if(fun.equals("LET")){
                    String nombre = nodo.hijos.get(1).valor;
                    Dato va = Ejecutar(nodo.hijos.get(2));
                    NodoSimbolo n = new NodoSimbolo(nombre,"lista", "global", va,"haskell","publco");
                    Analizador.tablaH.insertar(n);
                    imprimir = va.toStringH();
                    return va;
                }
                break;
            }
            case "exp":{
                if(nodo.contador == 3){
                    Dato v1 = Ejecutar(nodo.hijos.get(0));
                    Dato v2 = Ejecutar(nodo.hijos.get(2));
                    String op = nodo.hijos.get(1).valor;
                    if(op == "++"){
                        Dato res = new Dato();
                        if(v1.esCadena() || v1.esCaracter()){
                            if(v2.esCadena() || v2.esCaracter()){
                                String s1 = v1.toStringH();
                                String s2 = v2.toStringH();
                                String s = s1.substring(1,s1.length()-1) + s2.substring(1,s2.length()-1);
                                //s = s.replaceAll("\"", "");
                                //s = "\""+s+"\"";
                                res.valor = s;
                                res.tipo = "cadena";
                                imprimir = res.toStringH();
                                return res;
                            }
                        }
                        if(v1.esLista){
                            for (Dato dato : v1.lista) {
                                res.addDato(dato);
                            }
                        }else{
                            res.addDato(new Dato(v1.valor));
                        }
                        if(v2.esLista){
                            for (Dato dato : v2.lista) {
                                res.addDato(dato);
                            }
                        }else{
                            res.addDato(new Dato(v2.valor));
                        }
                        imprimir = res.toStringH();
                        return res;
                    }else if(op == "!!"){
                        float fl = Float.parseFloat(v2.valor);
                        int pos = Math.round(fl);
                        Dato d = v1.lista.get(pos);
                        d.tipo = v1.tipo;
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "||"){
                        if(Boolean.parseBoolean(v1.valor) || Boolean.parseBoolean(v2.valor)){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "&&"){
                        if(Boolean.parseBoolean(v1.valor) && Boolean.parseBoolean(v2.valor)){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }
                    //FALTA AGREGAR EL LEXICOGRAFICO
//                    float a = tryParse(v1);
//                    float b = tryParse(v2);
                    float a = Float.parseFloat(v1.valor);
                    float b = Float.parseFloat(v2.valor);
                    if(op == ">"){
                        if(a>b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "<"){
                        if(a<b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == ">="){
                        if(a>=b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "<="){
                        if(a<=b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "=="){
                        if(a==b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "!="){
                        if(a!=b){
                            imprimir = "True";
                            return new Dato("true");
                        }else{
                            imprimir = "False";
                            return new Dato("false");
                        }
                    }else if(op == "+"){
                        Dato d =  new Dato(String.valueOf(a+b));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "-"){
                        Dato d =  new Dato(String.valueOf(a-b));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "*"){
                        Dato d =  new Dato(String.valueOf(a*b));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "/"){
                        Dato d =  new Dato(String.valueOf(a/b));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "mod"){
                        Dato d =  new Dato(String.valueOf(a%b));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "sqrt"){
                        Dato d =  new Dato(String.valueOf(Math.pow(a,1/b)));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(op == "pot"){
                        Dato d =  new Dato(String.valueOf(Math.pow(a,b)));
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }
                }else{
                    String nom = nodo.hijos.get(0).nombre;
                    if(nom.equals("CADENA")){
                        Dato d = new Dato(nodo.hijos.get(0).valor);
                        d.tipo = "cadena";
                        imprimir = d.toStringH();
                        return d;
                    }else if(nom.equals("NUMERO")){
                        Dato d = new Dato(nodo.hijos.get(0).valor);
                        d.tipo = "numero";
                        imprimir = d.toStringH();
                        return d;
                    }else if(nom.equals("CARACTER")){
                        Dato d = new Dato(nodo.hijos.get(0).valor);
                        d.tipo = "caracter";
                        imprimir = d.toStringH();
                        return d;
                    }else if(nom.equals("TRUE") || nom.equals("FALSE")){
                        Dato d = new Dato(nodo.hijos.get(0).valor);
                        d.tipo = "bool";
                        imprimir = d.toStringH();
                        return d;
                    }else if(nom.equals("%")){
                        imprimir = new Dato(ultimoValor).toStringH();
                        return new Dato(ultimoValor);
                    }else{
                        Dato d = Ejecutar(nodo.hijos.get(0));
                        imprimir = d.toStringH();
                        return d;
                    }
                }
                break;
            }
            case "ID":{
                String nombre = nodo.valor;
                int x = ambitos.size();
                for (int i = x-1; i >= 0; i--) {
                    String ambito = ambitos.get(i);
                    Dato d;
                    if ((d = Analizador.tablaH.getValor(nombre, ambito, "haskell")) != null) {
                        return d;
                    }
                }
                break;   
            }
            case "unario":{
                float v = Float.parseFloat(Ejecutar(nodo.hijos.get(0)).valor);
                Dato d = new Dato(String.valueOf(v*-1));
                d.tipo = "numero";
                imprimir = d.toStringH();
                return d;
            }
            
            default:
                throw new AssertionError();
        }
        return new Dato("Salio del Case");
    }
    
}
