package Compilador.Graphik;

import Compilador.Analizador;
import Compilador.Haskell.EjecucionH;
import Compilador.NodoParser;
import Compilador.token;
import Tabla_Simbolos.Dato;
import Tabla_Simbolos.NodoSimbolo;
import clientehaskellgraphik.Ventana;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.data.xy.XYSeries;

public class EjecucionG {
    public static boolean debuguear = false;
    public static String claseInicio;
    //LISTAS AUXILIARES
    static ArrayList<Integer> coordenadas = new ArrayList<>();
    static ArrayList<String> ambitos = new ArrayList<>();
    static ArrayList<String> clases = new ArrayList<>();
    static ArrayList<String> accesos = new ArrayList<>();
    public static ArrayList<NodoSimbolo> datos = new ArrayList<>();
    static ArrayList<NodoSimbolo> resultados = new ArrayList<>();
    //STRINGS AUXILIARES
    static String tipoActivo = "", valorSwitch = "";
    public static String imprimir = "";
    static String enviados = "", sobrecarga = "";
    static String accesoActivo = "", claseActiva ="";
    static String funHaskell = "";
    //ENTEROS AUXILIARES
    static int contador = 0;
    static int contadorGraf = 0;
    static int columna = 0;
    //BOOLEANAS AUXILIARES
    static boolean salir = false;
    static boolean continuar = false;
    static boolean retornar = false;
    static boolean procesar = false, llamadaH = false, llamadaG = false;
    //DATOS AUXILIARES
    static NodoSimbolo retornable = null;
    static NodoParser nodoAcceso;
    static ArrayList<NodoSimbolo> lista = new ArrayList<>();
    public static Dato Ejecutar(NodoParser nodo){
        switch (nodo.nombre) {
            case "inicio":{//YA ESTUVO
                // <editor-fold desc="inicio">
                ambitos.add("global");
                ambitos.add("inicio");
                NodoParser inicio = Analizador.tabla.getInstrucciones("inicio", "global","nul", claseInicio);
                if(inicio == null){
                    try{
                        inicio = Analizador.tabla.getInstrucciones("Inicio", "global","nul", claseInicio);
                        Ejecutar(inicio);
                    }catch(Exception e){
                        imprimir += "Falta declaracion del metodo 'Inicio'";
                    }
                }
                Ejecutar(inicio);
                break;
                // </editor-fold>
            }
            case "sentencias":{//YA ESTUVO
                // <editor-fold desc="sentencias">
                for (NodoParser n : nodo.hijos) {
                    Ejecutar(n);
                    if(salir || retornar){
                        salir = false;
                        break;
                    }
                }
                break;
                // </editor-fold>
            }
            case "declaracion":{//YA ESTUVO
                // <editor-fold desc="declaracion">
                for (NodoParser n : nodo.hijos) {
                    if(n.nombre.equals("TIPO") || n.nombre.equals("ID")){
                        if(n.nombre.equals("ID")){
                            //FALTA: VERIFICAR QUE EL ALS EXISTA EN LA TABLA DE SIMBOLOS OSEA SI YA FUE DECLARADO
                        }else{
                            
                        }
                        tipoActivo = n.valor;
                    }else{
                        Ejecutar(n);
                    }
                }
                break;
                // </editor-fold>
            }
            case "variable":{//YA ESTUVO
                // <editor-fold desc="variable">
                String vis = token.publico, nombre = "", nuevo = "";
                boolean esArr = false;
                Dato val = new Dato();
                val.clase = claseInicio;
                coordenadas.clear();
                for (int i = 0;i<nodo.contador;i++) {
                    NodoParser n = nodo.hijos.get(i);
                    switch (n.nombre) {
                        case "ID":{
                            nombre = n.valor;
                            break;
                        }
                        case "VISIBILIDAD":{
                            vis = n.valor;
                            break;
                        }
                        case "DIMENSION":{
                            esArr = true;
                            float f = Float.parseFloat(EjecucionG.Ejecutar(n.hijos.get(0)).toString());
                            int dim = Math.round(f);
                            coordenadas.add(dim);
                            break;
                        }   
                        case "exp":{
                            val = Ejecutar(n);
//                            val = new Dato("prueba");
                            break;
                        }
                        case "NUEVO":{
                            nuevo = nodo.hijos.get(i+1).valor;
                            if(tipoActivo.equals(nuevo)){
                                ArrayList<NodoSimbolo> atrs = Analizador.tablaImport.getAtributos(nuevo);
                                if(esArr){
                                    int totalObjs = 1;
                                    for (Integer e : coordenadas) {
                                        totalObjs *= e;
                                    }
                                    for (int j = 0; j < totalObjs; j++) {
                                        Dato aux = new Dato();
                                        for (NodoSimbolo a : atrs) {
                                            Dato aux2; 
                                            if(a.rol.equals("variable")){
                                                aux2 = a.dato.clonar();
                                            }else{
                                                aux2 = new Dato("fucion");
                                            }
                                            aux2.nombre = a.nombre;
                                            aux2.tipo = a.tipo;
                                            aux.addDato(aux2);
                                        }
                                        val.addDato(aux);
                                    }
                                    val.valor = nombre;
                                }else{
                                    for (NodoSimbolo a : atrs) {
                                        Dato aux; 
                                        if(a.rol.equals("variable")){
                                            aux = a.dato.clonar();
                                        }else{
                                            aux = new Dato("funcion");
                                        }
                                        aux.nombre = a.nombre;
                                        aux.tipo = a.tipo;
                                        val.addDato(aux);
                                    }
                                }
                                i++;//para terminar y salir de ciclo for
                            }else{
                                System.out.println("Error: nuevo =! tipo");
                            }
                            break;
                        }
                        default:
                            throw new AssertionError();
                    }
                }
                if(esArr){
                    val.lista.clear();
                    for (Dato d : lista) {
                        val.addDato(d);
                    }
                    esArr = false;
                }
                val.tipo = tipoActivo;
                if(!coordenadas.isEmpty()){
                    val.dimensiones.clear();
                    for (Integer c : coordenadas) {
                        val.dimensiones.add(c);
                    }
                }
                lista.clear();
                coordenadas.clear();
                val.nombre = nombre;
                String amb = ambitos.get(ambitos.size()-1);
                NodoSimbolo ns = new NodoSimbolo(nombre, tipoActivo, amb, val, claseInicio, vis);
                insertar(ns);
                break;
                // </editor-fold>
            }
            case "asignacion":{//YA ESTUVO
                // <editor-fold desc="asignacion">
                Dato atr = Ejecutar(nodo.hijos.get(0));
                String nombre = atr.nombre;
                Dato val = Ejecutar(nodo.hijos.get(1));
                atr.valor = val.toString();
                boolean modificado = false;
                int tam = ambitos.size()-1;
                for (int i = tam; i>=0; i--) {
                    String amb = ambitos.get(i);
                    int tam2 = clases.size();
                    for (int i2 = 0; i2<tam2; i2++) {
                        String cls = clases.get(i2);
                        if(Analizador.tabla.modificarValor(nombre, amb, atr, cls)){
                            modificado = true;
                            break;
                        }
                    }
                    if(modificado) break;
                }
                break;
                // </editor-fold>
            }
            case "retornar":{//YA ESTUVO
                // <editor-fold desc="retornar">
                retornable = Ejecutar(nodo.hijos.get(0));
                retornar = true;
                return retornable;
                // </editor-fold>
            }
            case "si":{//YA ESTUVO
                // <editor-fold desc="si">
                String val = Ejecutar(nodo.hijos.get(0)).toString();
                boolean b = parsearBool(val);
                if(b){
                    contador++;
                    addAmbito("si"+contador);
                    Ejecutar(nodo.hijos.get(1));
                    eliminarAmbito();
                }else if(nodo.contador == 3){
                    contador++;
                    addAmbito("else"+contador);
                    Ejecutar(nodo.hijos.get(2));
                    eliminarAmbito();
                }
                break;
                // </editor-fold>
            }
            case "grafica":{
                // <editor-fold desc="grafica">
                Dato d1 = Ejecutar(nodo.hijos.get(0));
                Dato d2 = Ejecutar(nodo.hijos.get(1));
                int lim = d1.lista.size(); 
                contadorGraf++;
                XYSeries serie = new XYSeries("Funcion"+contadorGraf);
                for (int i = 0; i < lim; i++) {
                    serie.add(Double.parseDouble(d1.lista.get(i).toString()),Double.parseDouble(d2.lista.get(i).toString()));
                }
                Ventana.lineas.addSeries(serie);
                
                break;
                // </editor-fold>
            }
            case "imprimir":{//YA ESTUVO 
                // <editor-fold desc="imprimir">
                Dato d = Ejecutar(nodo.hijos.get(0));
                String val = d.toString();
                System.out.println(">>"+val);
                imprimir += ">>"+val+"\n";
                break;
                // </editor-fold>
            }
            case "seleccion":{//YA ESTUVO
                // <editor-fold desc="seleccion">
                valorSwitch = Ejecutar(nodo.hijos.get(0)).toString();
                Ejecutar(nodo.hijos.get(1));
                break;
                // </editor-fold>
            }
            case "casos":{//YA ESTUVO
                // <editor-fold desc="casos">
                for (NodoParser n : nodo.hijos) {
                    Ejecutar(n);
                    if(salir  || retornar){
                        salir = false;
                        break;
                    }
                }
                break;
                // </editor-fold>
            }
            case "caso":{//YA ESTUVO
                // <editor-fold desc="caso">
                if(nodo.contador == 2){
                    String val = Ejecutar(nodo.hijos.get(0)).toString();
                    if(val.equals(valorSwitch)){
                        contador++;
                        addAmbito("caso"+contador);
                        for (NodoParser n : nodo.hijos.get(1).hijos) {
                            Ejecutar(n);
                            if(salir || retornar){
                                break;
                            }
                            if(continuar){
                                continuar = false;
                                break;
                            }
                        }
                        if(salir || retornar){
                            salir = false;
                            break;
                        }
                        eliminarAmbito();
                    }
                }else{
                    contador++;
                    addAmbito("caso"+contador);
                    for (NodoParser n : nodo.hijos.get(0).hijos) {
                        Ejecutar(n);
                        if(salir || retornar){
                            break;
                        }
                        if(continuar){
                            continuar = false;
                            break;
                        }
                    }
                    if(salir || retornar){
                        salir = false;
                        break;
                    }
                    eliminarAmbito();
                }
                break;
                // </editor-fold>
            }
            case "para":{//YA ESTUVO
                // <editor-fold desc="para">
                contador++;
                addAmbito("for"+contador);
                Ejecutar(nodo.hijos.get(0));
                String val = Ejecutar(nodo.hijos.get(1)).toString();
                boolean b = parsearBool(val);
                while(b){
                    //System.out.println("para "+ contador);
                    contador++;
                    addAmbito("for"+contador);
                    for (NodoParser n : nodo.hijos.get(3).hijos) {
                        Ejecutar(n);
                        if(salir || retornar){
                            break;
                        }
                        if(continuar){
                            continuar = false;
                            break;
                        }
                    }
                    if(salir || retornar){
                        salir = false;
                        break;
                    }
                    Ejecutar(nodo.hijos.get(2));
                    val = Ejecutar(nodo.hijos.get(1)).toString();
                    b = parsearBool(val);
                    eliminarAmbito();
                }
                eliminarAmbito();
                break;
                // </editor-fold>
            }
            case "mientras":{//YA ESTUVO
                // <editor-fold desc="mientras">                
                String val = Ejecutar(nodo.hijos.get(0)).toString();
                boolean b = parsearBool(val);
                while(b){
                    contador++;
                    addAmbito("while"+contador);
                    System.out.println("while");
                    for (NodoParser n : nodo.hijos.get(1).hijos) {
                        Ejecutar(n);
                        if(salir || retornar){
                            break;
                        }
                        if(continuar){
                            continuar = false;
                            break;
                        }
                    }
                    if(salir || retornar){
                        salir = false;
                        break;
                    }
                    val = Ejecutar(nodo.hijos.get(0)).toString();
                    b = parsearBool(val);
                    eliminarAmbito();
                }
                break;
                // </editor-fold>
            }
            case "hacer":{//YA ESTUVO
                // <editor-fold desc="hacer">
                contador++;
                addAmbito("hacer"+contador);
                for (NodoParser n : nodo.hijos.get(1).hijos) {
                        Ejecutar(n);
                        if(salir || retornar){
                            break;
                        }
                        if(continuar){
                            continuar = false;
                            break;
                        }
                }
                if(salir || retornar){
                    salir = false;
                    break;
                }
                eliminarAmbito();
                String val = Ejecutar(nodo.hijos.get(0)).toString();
                boolean b = parsearBool(val);
                while(b){
                    contador++;
                    addAmbito("hacer"+contador);
                    for (NodoParser n : nodo.hijos.get(1).hijos) {
                        Ejecutar(n);
                        if(salir || retornar){
                            break;
                        }
                        if(continuar){
                            continuar = false;
                            break;
                        }
                    }
                    if(salir || retornar){
                        salir = false;
                        break;
                    }
                    val = Ejecutar(nodo.hijos.get(0)).toString();
                    b = parsearBool(val);
                    eliminarAmbito();
                }
                break;
                // </editor-fold>
            }
            case "TERMINAR":{//YA ESTUVO
                // <editor-fold desc="TERMINAR">
                salir = true;
                break;
                // </editor-fold>
            }
            case "CONTINUAR":{//YA ESTUVO
                // <editor-fold desc="CONTINUAR">
                continuar = true;
                break;
                // </editor-fold>
            }
            case "acceso":{//YA ESTUVO
                // <editor-fold desc="acceso">
                if(nodo.contador == 1){
                    String nom = nodo.hijos.get(0).valor;
                    int tam = ambitos.size()-1;
                    Dato d;
                    for (int i = tam;i>=0 ;i--) {
                        String amb = ambitos.get(i);
                        if((d = Analizador.tabla.getValor(nom, amb, claseInicio))!=null){
                            return d;
                        }
                    }
                }else{
                    //retornar = false;
                    coordenadas.clear();
                    Dato auxActual = new Dato();
                    Dato auxAnterior = new Dato();
                    boolean esArreglo = false, esFun = false, primero = true;
                    for (int i = 0;i<nodo.contador;i++) {
                        NodoParser actual = nodo.hijos.get(i);
                        String s = actual.nombre;
                        if(s.equals("ID")) {
                            accesoActivo = actual.valor;
                            //auxActual.nombre = accesoActivo;
                            //auxActual.tipo = claseInicio;
                            claseActiva = claseInicio;
                        }else if(s.equals("PUNTO")){
                            //<editor-fold desc="PUNTO">
                            String nomNuevo = actual.valor;
                            int tam = ambitos.size()-1;
                            int tam2 = clases.size();
                            if(esArreglo){
                                esArreglo = false;
                                if(primero){
                                    for (int i2 = tam;i2>=0 ;i2--) {
                                        String amb = ambitos.get(i2);
                                        for (int j = 0; j < tam2; j++) {
                                            String clase = clases.get(j);
                                            Dato aux2 = Analizador.tabla.getValor(accesoActivo, amb, clase);
                                            if(aux2!=null){
                                                auxAnterior = aux2;
                                                break;
                                            }
                                        }
                                    }
                                    auxActual = auxAnterior.getDeLista(coordenadas);
                                    primero  = false;
                                }else{
                                    //auxAnterior = auxActual;
                                    auxActual = auxActual.getDeLista(coordenadas);
                                }
                                coordenadas.clear();
                            }else{
                            }
                            boolean encontrado = false;
                            String nom =accesoActivo;
                            String rol = "rol";
                            
                            for (int i2 = tam;i2>=0 ;i2--) {
                                String amb = ambitos.get(i2);
                                for (int j = 0; j < tam2; j++) {
                                    String clase = clases.get(j);
                                    if((rol = Analizador.tablaImport.getRol(nomNuevo, amb, clase))!=null){
                                        encontrado = true;
                                        break;
                                    }
                                }
                                if(encontrado){
                                    break;
                                }
                            }
                            encontrado = false;
                            if(!rol.equals("variable")){
                                if(primero){
                                    for (int i2 = tam;i2>=0 ;i2--) {
                                        String amb = ambitos.get(i2);
                                        for (int j = 0; j < tam2; j++) {
                                            String clase = clases.get(j);
                                            Dato aux2 = Analizador.tabla.getValor(nom, amb, clase);
                                            if(aux2!=null){
                                                auxActual = aux2;
                                                encontrado = true;
                                                break;
                                            }
                                        }
                                    }
                                    primero  = false;
                                }
                                auxAnterior = auxActual;
                                accesoActivo = actual.valor;
                                //auxActual.nombre = accesoActivo;
                                //auxActual.valor = "funcion";
                            }else{
                                if(primero){
                                    for (int i2 = tam;i2>=0 ;i2--) {
                                        String amb = ambitos.get(i2);
                                        for (int j = 0; j < tam2; j++) {
                                            String clase = clases.get(j);
                                            Dato aux2 = Analizador.tabla.getValor(nom, amb, clase);
                                            if(aux2!=null){
                                                auxAnterior = aux2;
                                                encontrado = true;
                                                break;
                                            }
                                        }
                                    }
                                    primero  = false;
                                }else{
                                    auxAnterior = auxActual;
                                    encontrado = true;
                                }
                                if(encontrado){
                                    auxActual = auxAnterior.getAtributo(nomNuevo);
                                }else{
                                    //REPORTAR 
                                }
                                accesoActivo = nomNuevo;
                            }
                            //</editor-fold>
                        }else if(s.equals("DIMENSION")){
                            //<editor-fold desc="DIMENSION">
                            NodoParser n2 = actual.hijos.get(0);
                            float f = Float.parseFloat(Ejecutar(n2).toString());
                            int dim = Math.round(f);
                            coordenadas.add(dim);
                            esArreglo = true;
                            //</editor-fold>
                        }else{
                            //<editor-fold desc="SI ES FUNCION">
                            int tam = ambitos.size()-1;
                            int tam2 = clases.size();
                            if(esArreglo){
                                esArreglo = false;
                                if(primero){
                                    for (int i2 = tam;i2>=0 ;i2--) {
                                        String amb = ambitos.get(i2);
                                        for (int j = 0; j < tam2; j++) {
                                            String clase = clases.get(j);
                                            Dato aux2 = Analizador.tabla.getValor(accesoActivo, amb, clase);
                                            if(aux2!=null){
                                                auxAnterior = aux2;
                                                //auxAnterior.tipo = aux2.tipo;
                                                break;
                                            }
                                        }
                                    }
                                    auxActual = auxAnterior.getDeLista(coordenadas);
                                }else{
                                    auxActual = auxActual.getDeLista(coordenadas);
                                }
                                coordenadas.clear();
                            }  
                            
                            contador++;
                            addAmbito("fun"+contador);
//                            String nom = auxActual.nombre;
                            if(actual.nombre.equals("enviados")){
                                if(procesar){
                                    nodoAcceso = nodo.clonar();
                                    llamadaG = true;
                                    funHaskell = accesoActivo;
                                    Ejecutar(actual);
                                    
                                    break;
                                }
                                Ejecutar(actual);
                                String parametros;
                                if(primero){
                                    parametros = Analizador.tabla.getParametros(accesoActivo, "global", sobrecarga, claseInicio);
                                }else{
                                    parametros = Analizador.tablaImport.getParametros(accesoActivo, "global", sobrecarga, auxAnterior.tipo);
                                }
                                String[] so = sobrecarga.split(",");
                                String[] p = parametros.split(",");
                                String[] e = enviados.split(",");
                                String amb = ambitos.get(ambitos.size()-1);
                                for(int i2 = 0;i2 < p.length;i2++){
                                    Dato d = new Dato(e[i2]);
                                    d.tipo = so[i2];
                                    NodoSimbolo n = new NodoSimbolo(p[i2], so[i2], amb, d, claseInicio,"publico");
                                    Analizador.tabla.insertar(n);
                                }
                            }else{
                                sobrecarga = "nul";
                            }
                            if(!primero){
                                String amb = auxAnterior.nombre;
                                addAmbito(amb);
                                agregarAtributosTemporles(auxActual.lista, amb);
                            }
                            NodoParser ins;
                            if(primero){
                                ins = Analizador.tabla.getInstrucciones(accesoActivo, "global", sobrecarga, claseInicio);
                            }else{
                                ins = Analizador.tablaImport.getInstrucciones(accesoActivo, "global", sobrecarga, auxAnterior.tipo);
                            }
                            Ejecutar(ins);
                            auxActual = retornable;
                            retornar = false;
                            if(!primero){
                                eliminarAmbito();
                            }
                            
                            eliminarAmbito();
                            esFun = true;
                            //</editor-fold>
                        }
                    }
                    if(esArreglo){
                        esArreglo = false;
                        String am = ambitos.get(ambitos.size()-1);
                        Dato d;
                        int tam = ambitos.size()-1;
                        for (int i = tam;i>=0 ;i--) {
                            String amb = ambitos.get(i);
                            if((d = Analizador.tabla.getValor(accesoActivo, amb, claseInicio))!=null){
                                auxActual = d .getDeLista(coordenadas);
                            }
                        }
                    }else if(esFun){
                        
                    }
                    return auxActual;
                    //TAMBIEN HAY QUE AGREGAR A LA TABLA LOS ATRIBUTOS DE CADA CLASE AL CREAR U OBJETO COMO AMBITO EL ONMBRE DEL OBJETO
                }
                break;
                // </editor-fold>
            }
            case "listaExp":{//YA ESTUVO
                // <editor-fold desc="listaExp">
                Dato d = new Dato();
                for (NodoParser n : nodo.hijos) {
                    Dato aux = Ejecutar(n);
                    d.addDato(aux);
                    if(n.hijos.get(0).nombre != "listaExp"){
                        lista.add(aux);
                    }
                    
                }
                return d;
                // </editor-fold>
            }
            case "llamadaH":{//YA ESTUVO
                // <editor-fold desc="llamadaH">
                if(procesar){
                    funHaskell = nodo.hijos.get(0).valor;
                    llamadaH = true;
                    Ejecutar(nodo.hijos.get(1));
                    break;
                }
                contador++;
                addAmbito("funH"+contador);
                String nom = nodo.hijos.get(0).valor;
                //String amb = ambitos.get(ambitos.size()-1);
                String amb = "global1";
                if(nodo.contador == 2){
                    Ejecutar(nodo.hijos.get(1));
                    String parametros = Analizador.tablaH.getParametros(nom, "global", "nul", "haskell");
                    String[] p = parametros.split(",");
                    String[] e = enviados.split(",");


                    for(int i = 0;i < p.length;i++){
                        Dato d = new Dato(e[i]);
                        NodoSimbolo n = new NodoSimbolo(p[i], "number", amb, d,"haskell","publico");
                        Analizador.tablaH.insertar(n);
                    }
                }
                EjecucionH.ambitos.add(amb);
                NodoParser ins = Analizador.tablaH.getInstrucciones(nom, "global", "nul", "haskell");
                EjecucionH.Ejecutar(ins);
                String s = EjecucionH.retornableD.toStringH();
                Dato d = new Dato(s);
                d.tipo = token.decimal;
                Analizador.tablaH.eliminarPorAmbito(amb);
                EjecucionH.ambitos.remove(amb);
                eliminarAmbito();
                return d;
                // </editor-fold>
            }
            case "enviados":{//YA ESTUVO
                // <editor-fold desc="enviados">
                if(procesar && llamadaH){
                    //<editor-fold desc="haskell">
                    int x = Ejecutar(nodo.hijos.get(0)).lista.size();
                    for (int i = 0; i < x; i++) {
                                contador++;
                                addAmbito("funH"+contador);
                                String amb = "global1";
                                    String parametros = Analizador.tablaH.getParametros(funHaskell, "global", "nul", "haskell");
                                    String[] p = parametros.split(",");
                                    for(int i2 = 0;i2 < p.length;i2++){
                                        Dato col = Ejecutar(nodo.hijos.get(i2));
                                        Dato d;
                                        if(col.esLista){
                                            d = col.lista.get(i);
                                        }else{
                                            d = col;
                                        }
                                        NodoSimbolo n = new NodoSimbolo(p[i2], "number", amb, d,"haskell","publico");
                                        Analizador.tablaH.insertar(n);
                                    }
                                EjecucionH.ambitos.add(amb);
                                NodoParser ins = Analizador.tablaH.getInstrucciones(funHaskell, "global", "nul", "haskell");
                                EjecucionH.Ejecutar(ins);
                                String s = EjecucionH.retornableD.toStringH();
                                Dato d = new Dato(s);
                                d.tipo = token.decimal;
                                Analizador.tablaH.eliminarPorAmbito(amb);
                                EjecucionH.ambitos.remove(amb);
                                eliminarAmbito();
                                resultados.add(d);
                    }
                    procesar = false;
                    llamadaH = false;
                    break;
                    //</editor-fold>
                }else if(procesar && llamadaG){
                    // <editor-fold desc="acceso">
                    int x = Ejecutar(nodo.hijos.get(0)).lista.size();
                    for (int ii = 0; ii < x; ii++) {
                        if(nodo.contador == 1){
                            String nom = nodo.hijos.get(0).valor;
                            int tam = ambitos.size()-1;
                            Dato d;
                            for (int i = tam;i>=0 ;i--) {
                                String amb = ambitos.get(i);
                                if((d = Analizador.tabla.getValor(nom, amb, claseInicio))!=null){
                                    return d;
                                }
                            }
                        }else{
                            //retornar = false;
                            coordenadas.clear();
                            Dato auxActual = new Dato();
                            Dato auxAnterior = new Dato();
                            boolean esArreglo = false, esFun = false, primero = true;
                            for (int i = 0;i<nodoAcceso.contador;i++) {
                                NodoParser actual = nodoAcceso.hijos.get(i);
                                String s = actual.nombre;
                                if(s.equals("ID")) {
                                    accesoActivo = actual.valor;
                                    //auxActual.nombre = accesoActivo;
                                    //auxActual.tipo = claseInicio;
                                    claseActiva = claseInicio;
                                }else if(s.equals("PUNTO")){
                                    //<editor-fold desc="PUNTO">
                                    String nomNuevo = actual.valor;
                                    int tam = ambitos.size()-1;
                                    int tam2 = clases.size();
                                    if(esArreglo){
                                        esArreglo = false;
                                        if(primero){
                                            for (int i2 = tam;i2>=0 ;i2--) {
                                                String amb = ambitos.get(i2);
                                                for (int j = 0; j < tam2; j++) {
                                                    String clase = clases.get(j);
                                                    Dato aux2 = Analizador.tabla.getValor(accesoActivo, amb, clase);
                                                    if(aux2!=null){
                                                        auxAnterior = aux2;
                                                        break;
                                                    }
                                                }
                                            }
                                            auxActual = auxAnterior.getDeLista(coordenadas);
                                            primero  = false;
                                        }else{
                                            //auxAnterior = auxActual;
                                            auxActual = auxActual.getDeLista(coordenadas);
                                        }
                                        coordenadas.clear();
                                    }else{
                                    }
                                    boolean encontrado = false;
                                    String nom =accesoActivo;
                                    String rol = "rol";

                                    for (int i2 = tam;i2>=0 ;i2--) {
                                        String amb = ambitos.get(i2);
                                        for (int j = 0; j < tam2; j++) {
                                            String clase = clases.get(j);
                                            if((rol = Analizador.tablaImport.getRol(nomNuevo, amb, clase))!=null){
                                                encontrado = true;
                                                break;
                                            }
                                        }
                                        if(encontrado){
                                            break;
                                        }
                                    }
                                    encontrado = false;
                                    if(!rol.equals("variable")){
                                        if(primero){
                                            for (int i2 = tam;i2>=0 ;i2--) {
                                                String amb = ambitos.get(i2);
                                                for (int j = 0; j < tam2; j++) {
                                                    String clase = clases.get(j);
                                                    Dato aux2 = Analizador.tabla.getValor(nom, amb, clase);
                                                    if(aux2!=null){
                                                        auxActual = aux2;
                                                        encontrado = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            primero  = false;
                                        }
                                        auxAnterior = auxActual;
                                        accesoActivo = actual.valor;
                                        //auxActual.nombre = accesoActivo;
                                        //auxActual.valor = "funcion";
                                    }else{
                                        if(primero){
                                            for (int i2 = tam;i2>=0 ;i2--) {
                                                String amb = ambitos.get(i2);
                                                for (int j = 0; j < tam2; j++) {
                                                    String clase = clases.get(j);
                                                    Dato aux2 = Analizador.tabla.getValor(nom, amb, clase);
                                                    if(aux2!=null){
                                                        auxAnterior = aux2;
                                                        encontrado = true;
                                                        break;
                                                    }
                                                }
                                            }
                                            primero  = false;
                                        }else{
                                            auxAnterior = auxActual;
                                            encontrado = true;
                                        }
                                        if(encontrado){
                                            auxActual = auxAnterior.getAtributo(nomNuevo);
                                        }else{
                                            //REPORTAR 
                                        }
                                        accesoActivo = nomNuevo;
                                    }
                                    //</editor-fold>
                                }else if(s.equals("DIMENSION")){
                                    //<editor-fold desc="DIMENSION">
                                    NodoParser n2 = actual.hijos.get(0);
                                    float f = Float.parseFloat(Ejecutar(n2).toString());
                                    int dim = Math.round(f);
                                    coordenadas.add(dim);
                                    esArreglo = true;
                                    //</editor-fold>
                                }else{
                                    //<editor-fold desc="SI ES FUNCION">
                                    int tam = ambitos.size()-1;
                                    int tam2 = clases.size();
                                    if(esArreglo){
                                        esArreglo = false;
                                        if(primero){
                                            for (int i2 = tam;i2>=0 ;i2--) {
                                                String amb = ambitos.get(i2);
                                                for (int j = 0; j < tam2; j++) {
                                                    String clase = clases.get(j);
                                                    Dato aux2 = Analizador.tabla.getValor(accesoActivo, amb, clase);
                                                    if(aux2!=null){
                                                        auxAnterior = aux2;
                                                        //auxAnterior.tipo = aux2.tipo;
                                                        break;
                                                    }
                                                }
                                            }
                                            auxActual = auxAnterior.getDeLista(coordenadas);
                                        }else{
                                            auxActual = auxActual.getDeLista(coordenadas);
                                        }
                                        coordenadas.clear();
                                    }  

                                    contador++;
                                    addAmbito("fun"+contador);
        //                            String nom = auxActual.nombre;
                                    if(actual.nombre.equals("enviados")){
                                        procesar = false;
                                        Ejecutar(actual);
                                        procesar = true;
                                        String parametros;
                                        if(primero){
                                            parametros = Analizador.tabla.getParametros(accesoActivo, "global", sobrecarga, claseInicio);
                                        }else{
                                            parametros = Analizador.tablaImport.getParametros(accesoActivo, "global", sobrecarga, auxAnterior.tipo);
                                        }
                                        String[] so = sobrecarga.split(",");
                                        String[] p = parametros.split(",");
                                        
                                        //String[] e = enviados.split(",");
                                        String amb = ambitos.get(ambitos.size()-1);
                                        for(int i2 = 0;i2 < p.length;i2++){
                                            Dato col = Ejecutar(nodoAcceso.hijos.get(i2));
                                            Dato d;
                                            if(col.esLista){
                                                d = col.lista.get(ii);
                                            }else{
                                                d = col;
                                            }
                                            NodoSimbolo n = new NodoSimbolo(p[i2], so[i2], amb, d,claseInicio,"publico");
                                            Analizador.tablaH.insertar(n);
                                        }

                                    }else{
                                        sobrecarga = "nul";
                                    }
                                    if(!primero){
                                        String amb = auxAnterior.nombre;
                                        addAmbito(amb);
                                        agregarAtributosTemporles(auxActual.lista, amb);
                                    }
                                    NodoParser ins;
                                    if(primero){
                                        ins = Analizador.tabla.getInstrucciones(accesoActivo, "global", sobrecarga, claseInicio);
                                    }else{
                                        ins = Analizador.tablaImport.getInstrucciones(accesoActivo, "global", sobrecarga, auxAnterior.tipo);
                                    }
                                    Ejecutar(ins);
                                    auxActual = retornable;
                                    Dato res = retornable.clonar();
                                    resultados.add(res);
                                    retornar = false;
                                    if(!primero){
                                        eliminarAmbito();
                                    }

                                    eliminarAmbito();
                                    esFun = true;
                                    //</editor-fold>
                                }
                            }
                            if(esArreglo){
                                esArreglo = false;
                                String am = ambitos.get(ambitos.size()-1);
                                Dato d;
                                int tam = ambitos.size()-1;
                                for (int i = tam;i>=0 ;i--) {
                                    String amb = ambitos.get(i);
                                    if((d = Analizador.tabla.getValor(accesoActivo, amb, claseInicio))!=null){
                                        auxActual = d .getDeLista(coordenadas);
                                    }
                                }
                            }else if(esFun){

                            }
                            return auxActual;
                            //TAMBIEN HAY QUE AGREGAR A LA TABLA LOS ATRIBUTOS DE CADA CLASE AL CREAR U OBJETO COMO AMBITO EL ONMBRE DEL OBJETO
                        }
                        procesar = false;
                        llamadaG = false;
                        
                        // </editor-fold>
                    }
                    break;
                }else{
                    enviados = "";
                    sobrecarga = "";
                    for (NodoParser n : nodo.hijos) {
                        Dato d = Ejecutar(n);
                        enviados += d.valor + ",";
                        sobrecarga += d.tipo + ",";
                    }
                    enviados = enviados.substring(0,enviados.length()-1);
                    sobrecarga = sobrecarga.substring(0,sobrecarga.length()-1);
                    
                }
                break;
                // </editor-fold>
            }
            // <editor-fold desc="PROCESO DE DATOS">
            case "columna":{
                // <editor-fold desc="ACCCIONES">
                Dato d = Ejecutar(nodo.hijos.get(0));
                float f = Float.parseFloat(d.toString());
                columna = Math.round(f);
                return datos.get(columna);
                // </editor-fold>
            }
            case "procesar":{
                // <editor-fold desc="ACCCIONES">
                procesar = true;
                resultados.clear();
                Ejecutar(nodo.hijos.get(0));
                break;
                // </editor-fold>
            }
            case "donde":{
                // <editor-fold desc="ACCCIONES">
                float f = Float.parseFloat(Ejecutar(nodo.hijos.get(0)).toString());
                columna = Math.round(f);
                Dato d = Ejecutar(nodo.hijos.get(1));
                String html = "<HTML> <HEAD> <TITLE> Resultados Donde </TITLE> </HEAD> <BODY>\n";
                html += "<br>";
                html += "<table width=\"60%\" border=\"2\" align=\"center\" cellspacing=\"0\" bordercolor=\"#000000\" bgcolor=\"#FFCC99\">";
                html += "<tr>";
                html += "   <th><strong> Donde </th>";
                html += "   <th><strong> "+funHaskell+"</th>";
                html += "</tr>";
                int l = datos.get(columna).lista.size();
                for (int i = 0;i<l;i++) {
                    Dato dato = datos.get(columna).lista.get(i);
                    if(dato.toString().equals(d.toString())){
                        html += "<tr>";
                        html += "   <td align=\"center\">"+dato.toString()+"</td>";
                        html += "   <td align=\"center\">" + resultados.get(i).toString() + "</td>";
                        html += "</tr>";
                    }
                }
                html += "</table>";
                html += "</BODY></HTML>";
                
                File archivo = new File("tablaDonde.html");
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(html);
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Desktop desktop;
                if (Desktop.isDesktopSupported()){// si éste Host soporta esta API 
                    desktop = Desktop.getDesktop();//objtengo una instancia del Desktop(Escritorio)de mi host 
                    try {            
                        desktop.open(archivo);//abro el archivo con el programa predeterminado
                    } catch (IOException ex) {
                        Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                // </editor-fold>
            }
            case "dondeCada":{
                // <editor-fold desc="ACCCIONES">
                float f = Float.parseFloat(Ejecutar(nodo.hijos.get(0)).toString());
                columna = Math.round(f);
                String html = "<HTML> <HEAD> <TITLE> Resultados DondeCada</TITLE> </HEAD> <BODY>\n";
                html += "<br>";
                html += "<table width=\"60%\" border=\"2\" align=\"center\" cellspacing=\"0\" bordercolor=\"#000000\" bgcolor=\"#FFCC99\">";
                html += "<tr>";
                html += "   <th><strong> Donde </th>";
                html += "   <th><strong> "+funHaskell+"</th>";
                html += "</tr>";
                int l = datos.get(columna).lista.size();
                for (int i = 0;i<l;i++) {
                    Dato dato = datos.get(columna).lista.get(i);
                        html += "<tr>";
                        html += "   <td align=\"center\">"+dato.toString()+"</td>";
                        html += "   <td align=\"center\">" + resultados.get(i).toString() + "</td>";
                        html += "</tr>";
                }
                html += "</table>";
                html += "</BODY></HTML>";
                
                File archivo = new File("tablaDondeCada.html");
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(html);
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Desktop desktop;
                if (Desktop.isDesktopSupported()){// si éste Host soporta esta API 
                    desktop = Desktop.getDesktop();//objtengo una instancia del Desktop(Escritorio)de mi host 
                    try {            
                        desktop.open(archivo);//abro el archivo con el programa predeterminado
                    } catch (IOException ex) {
                        Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                // </editor-fold>
            }
            case "dondeTodo":{
                // <editor-fold desc="ACCCIONES">
                float f = Float.parseFloat(Ejecutar(nodo.hijos.get(0)).toString());
                columna = Math.round(f);
                String html = "<HTML> <HEAD> <TITLE> Resultados DondeTodo</TITLE> </HEAD> <BODY>\n";
                html += "<br>";
                html += "<table width=\"60%\" border=\"2\" align=\"center\" cellspacing=\"0\" bordercolor=\"#000000\" bgcolor=\"#FFCC99\">";
                html += "<tr>";
                html += "   <th><strong> Donde </th>";
                html += "   <th><strong> "+funHaskell+"</th>";
                html += "</tr>";
                float sum = 0;
                for (Dato r : resultados) {
                    sum += Float.parseFloat(r.toString());
                }
                        html += "<tr>";
                        html += "   <td align=\"center\"> Todo</td>";
                        html += "   <td align=\"center\">" + sum+ "</td>";
                        html += "</tr>";
                html += "</table>";
                html += "</BODY></HTML>";
                
                File archivo = new File("tablaDondeTod.html");
                BufferedWriter bw;
                try {
                    bw = new BufferedWriter(new FileWriter(archivo));
                    bw.write(html);
                    bw.close();
                } catch (IOException ex) {
                    Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                Desktop desktop;
                if (Desktop.isDesktopSupported()){// si éste Host soporta esta API 
                    desktop = Desktop.getDesktop();//objtengo una instancia del Desktop(Escritorio)de mi host 
                    try {            
                        desktop.open(archivo);//abro el archivo con el programa predeterminado
                    } catch (IOException ex) {
                        Logger.getLogger(EjecucionG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
                // </editor-fold>
            }
            // </editor-fold>
            case "exp":{//YA ESTUVO
                // <editor-fold desc="ACCCIONES">
                switch (nodo.contador) {
                    case 3:{
                        // <editor-fold desc="OPERACIONES DE 2">
                        Dato a1 = Ejecutar(nodo.hijos.get(0));
                        Dato val1 = a1.clonar();
                        Dato a2 = Ejecutar(nodo.hijos.get(2));
                        Dato val2 = a2.clonar();
                        String op = nodo.hijos.get(1).valor;
                        
                        switch (op) {
                            case "+":{//YAS ESTUVO
                                // <editor-fold desc="MAS">
                                if(val1.esBool() && val2.esBool()){
                                    Dato d;
                                    if(val1.toString().equals(token.falso) && val2.toString().equals(token.falso)){
                                        d = new Dato(token.falso);
                                    }else{
                                        d = new Dato(token.verdadero);
                                    }
                                    d.tipo = token.bool;
                                    return d;
                                }
                                val1 = parsearSiBool(val1);
                                val2 = parsearSiBool(val2);
                                if(val1.esNumero() && val2.esNumero()){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato n = new Dato(n1+n2);
                                    n.tipo = token.entero;
                                    return n;
                                }else{
                                    String res = val1.toString() + val2.toString();
                                    Dato d = new Dato(res);
                                    d.tipo = token.cadena;
                                    return d;
                                }
                                // </editor-fold>
                            }
                            case "-":{//YA ESTUVO 
                                //<editor-fold desc="RESTAS">
                                val1 = parsearSiBool(val1);
                                val2 = parsearSiBool(val2);
                                if(val1.esNumero() && val2.esNumero()){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato n = new Dato(n1-n2);
                                    n.tipo = token.entero;
                                    return n;
                                }else{
                                    //FALTA MANDAR ERROR DE RESTA DE CADENAS
                                }
                                break;
                                //</editor-fold>
                            }
                            case "*":{
                                // <editor-fold desc="MULTIPLICACION">
                                if(val1.esBool() && val2.esBool()){
                                    Dato d;
                                    if(val1.toString().equals(token.verdadero) && val2.toString().equals(token.verdadero)){
                                        d = new Dato(token.verdadero);
                                    }else{
                                        d = new Dato(token.falso);
                                    }
                                    d.tipo = token.bool;
                                    return d;
                                }
                                val1 = parsearSiBool(val1);
                                val2 = parsearSiBool(val2);
                                if(val1.esNumero() && val2.esNumero()){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato n = new Dato(n1*n2);
                                    n.tipo = token.entero;
                                    return n;
                                }else{
                                    //FALTA ERPORTAR ERROR DE MULTIPLICACION DE CADENAS
                                }
                                break;
                                // </editor-fold>
                            }    
                            case "/":{
                                // <editor-fold desc="DIVISION">
                                val1 = parsearSiBool(val1);
                                val2 = parsearSiBool(val2);
                                if(val1.esNumero() && val2.esNumero()){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato n = new Dato(n1/n2);
                                    n.tipo = token.entero;
                                    return n;
                                }else{
                                    //FALTA ERROR SEMANTICO DE DIVISION
                                }
                                break;
                                // </editor-fold>
                            }
                            case "^":{
                                // <editor-fold desc="POTENCIA">
                                val1 = parsearSiBool(val1);
                                val2 = parsearSiBool(val2);
                                if(val1.esNumero() && val2.esNumero()){
                                    double n1 = Float.parseFloat(val1.toString());
                                    double n2 = Float.parseFloat(val2.toString());
                                    float res = Float.parseFloat(String.valueOf(Math.pow(n1, n2)));
                                    Dato n = new Dato(res);
                                    n.tipo = token.entero;
                                    return n;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                // </editor-fold>
                            }
                            case "||":{
                                // <editor-fold desc="OR">
                                if(val1.esBool() && val2.esBool()){
                                    Dato d;
                                    if(val1.toString().equals(token.falso) && val2.toString().equals(token.falso)){
                                        d = new Dato(token.falso);
                                    }else{
                                        d = new Dato(token.verdadero);
                                    }
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                // </editor-fold> 
                            }
                            case "&&":{
                                // <editor-fold desc="AND">
                                if(val1.esBool() && val2.esBool()){
                                    Dato d;
                                    if(val1.toString().equals(token.verdadero) && val2.toString().equals(token.verdadero)){
                                        d = new Dato(token.verdadero);
                                    }else{
                                        d = new Dato(token.falso);
                                    }
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                // </editor-fold>
                            }
                            case "&|":{
                                // <editor-fold desc="XOR">
                                Dato d;
                                if(val1.toString().equals(val2.toString())){
                                    d = new Dato(token.falso);
                                }else{
                                    d = new Dato(token.verdadero);
                                }
                                d.tipo = token.bool;
                                return d;
                                // </editor-fold>
                            }
                            case "<":{
                                // <editor-fold desc="MAYOR">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1<n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            case ">":{
                                // <editor-fold desc="MENOR">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1>n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            case "<=":{
                                // <editor-fold desc="MENORIGUAL">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1<=n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            case ">=":{
                                // <editor-fold desc="MAYORIGUAL">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1>=n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            case "==":{
                                // <editor-fold desc="IGUALIGUAL">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1==n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            case "!=":{
                                // <editor-fold desc="NOIGUAL">
                                if((val1.esNumero() || val1.esBool()) && (val2.esNumero()||val2.esBool())){
                                    float n1 = Float.parseFloat(val1.toString());
                                    float n2 = Float.parseFloat(val2.toString());
                                    Dato d = new Dato(n1!=n2);
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO O COMPARACION LEXICOGRAFICA
                                }
                                // </editor-fold>
                            }
                            default:
                                throw new AssertionError();
                        }
                        break;
                        // </editor-fold>
                    }
                    case 2:{
                        //<editor-fold desc="OPERACION DE 1">
                        Dato val1 = Ejecutar(nodo.hijos.get(0)).clonar();
                        String op = nodo.hijos.get(1).valor;
                        val1 = parsearSiBool(val1);
                        switch (op) {
                            case "!":{
                                //<editor-fold desc="NOT">
                                if(val1.esNumero()){
                                    Dato d;
                                    if(val1.toString().equals("1")){
                                        d = new Dato(token.falso);
                                    }else{
                                        d = new Dato(token.verdadero);
                                    }
                                    d.tipo = token.bool;
                                    return d;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                //</editor-fold>
                            }
                            case "++":{
                                //<editor-fold desc="MASMAS">
                                if(val1.esNumero()){
                                    String nombre = val1.nombre;
                                    float res = Float.parseFloat(val1.toString())+1;
                                    Dato d = new Dato(res);
                                    d.nombre = val1.nombre;
                                    d.tipo = val1.tipo;
                                    
                                    boolean modificado = false;
                                    int tam = ambitos.size()-1;
                                    for (int i = tam; i>=0; i--) {
                                        String amb = ambitos.get(i);
                                        int tam2 = clases.size();
                                        for (int i2 = 0; i2<tam2; i2++) {
                                            String cls = clases.get(i2);
                                            if(Analizador.tabla.modificarValor(nombre, amb, d, cls)){
                                                modificado = true;
                                                break;
                                            }
                                        }
                                        if(modificado) break;
                                    }
                                   return d;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                //</editor-fold>
                            }
                            case "--":{
                                //<editor-fold desc="MENOSMENOS">
                                if(val1.esNumero()){
                                   String nombre = val1.nombre;
                                    float res = Float.parseFloat(val1.toString())-1;
                                    Dato d = new Dato(res);
                                    d.nombre = val1.nombre;
                                    d.tipo = val1.tipo;
                                    
                                    boolean modificado = false;
                                    int tam = ambitos.size()-1;
                                    for (int i = tam; i>=0; i--) {
                                        String amb = ambitos.get(i);
                                        int tam2 = clases.size();
                                        for (int i2 = 0; i2<tam2; i2++) {
                                            String cls = clases.get(i2);
                                            if(Analizador.tabla.modificarValor(nombre, amb, d, cls)){
                                                modificado = true;
                                                break;
                                            }
                                        }
                                        if(modificado) break;
                                    }
                                   return d;
                                }else{
                                    //FALTA ERROR SEMANTICO
                                }
                                break;
                                //</editor-fold>
                            }
                            default:
                                throw new AssertionError();
                        }
                        break;
                        //</editor-fold>
                    }
                    case 1:{
                        //<editor-fold desc="VALOR O LLAMADA">
                        String s = nodo.hijos.get(0).nombre;
                        if(s.equals("CADENA")||s.equals("ENTERO")||s.equals("DECIMAL")||s.equals("CARACTER")||s.equals("BOOL")){
                            Dato n = new Dato(nodo.hijos.get(0).valor);
                            n.tipo = s.toLowerCase();
                            return n;
                        }
                        return Ejecutar(nodo.hijos.get(0));
                        //</editor-fold>
                    }                    
                    default:
                        break;
                }
                    break;
                    // </editor-fold>
            }
            case "unario":{
                Dato d = Ejecutar(nodo.hijos.get(0));
                float f = Float.parseFloat(d.toString());
                Dato r = new Dato(f*-1);
                r.tipo = token.entero;
                return r;
            }
            default:
                throw new AssertionError();
        }
        
        return new Dato("salio de sw");
    }
    
    public static void agregarAtributosTemporles(ArrayList<Dato> lista, String ambito){
        for (Dato d : lista) {
            NodoSimbolo nuevo = new NodoSimbolo(d.nombre, d.tipo, ambito, d, claseInicio, "publico");
            insertar(nuevo);
        }
    }
    
    public static Dato parsearSiBool(Dato n){
        Dato salida = n;
        if(salida.esBool()){
            if(salida.toString().equals(token.verdadero)){
                salida.valor = "1";
                
            }else if(salida.toString().equals(token.falso)){
                salida.valor = "0";
            }
            salida.tipo = token.entero;
        }
        return salida;
    }
    
    public static boolean parsearBool(String val){
        if(val.equals(token.verdadero)){
            return true;
        }
        return false;
    }
    
    public static void insertar(NodoSimbolo n){
        Analizador.tabla.insertar(n);
    }
    
    public static void addAmbito(String amb){
        ambitos.add(amb);
    }
    public static void eliminarAmbito(){
        Analizador.tabla.eliminarPorAmbito(ambitos.get(ambitos.size()-1));
        ambitos.remove(ambitos.size()-1);
        //contador--;
    }
    public static void eliminarAmbitoH(){
        Analizador.tablaH.eliminarPorAmbito(EjecucionH.ambitos.get(EjecucionH.ambitos.size()-1));
        EjecucionH.ambitos.remove(EjecucionH.ambitos.size()-1);
        //contador--;
    }
}
