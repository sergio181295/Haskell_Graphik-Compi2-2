package Compilador.Graphik;

import Compilador.Analizador;
import Compilador.Haskell.EjecucionH;
import Compilador.NodoParser;
import Compilador.token;
import Tabla_Simbolos.Dato;
import Tabla_Simbolos.NodoSimbolo;
import java.util.ArrayList;

public class EjecucionG {
    public static boolean debuguear = false;
    public static String claseInicio;
    //LISTAS AUXILIARES
    static ArrayList<Integer> coordenadas = new ArrayList<>();
    static ArrayList<String> ambitos = new ArrayList<>();
    static ArrayList<String> clases = new ArrayList<>();
    static ArrayList<String> accesos = new ArrayList<>();
    //STRINGS AUXILIARES
    static String tipoActivo = "", valorSwitch = "";
    public static String imprimir = "";
    static String enviados = "", sobrecarga = "";
    static String accesoActivo = "";
    //ENTEROS AUXILIARES
    static int contador = 0;
    //BOOLEANAS AUXILIARES
    static boolean salir = false;
    static boolean continuar = false;
    static boolean retornar = false;
    //DATOS AUXILIARES
    static Dato retornable = null;
    static ArrayList<Dato> lista = new ArrayList<Dato>();
    public static Dato Ejecutar(NodoParser nodo){
        switch (nodo.nombre) {
            case "inicio":{//YA ESTUVO
                // <editor-fold desc="inicio">
                ambitos.add("global");
                ambitos.add("inicio");
                NodoParser inicio = Analizador.tabla.getInstrucciones("inicio", "global","nul", claseInicio);
                Ejecutar(inicio);
                break;
                // </editor-fold>
            }
            case "sentencias":{//YA ESTUVO
                // <editor-fold desc="sentencias">
                for (NodoParser n : nodo.hijos) {
                    Ejecutar(n);
//                    if(salir || retornar){
//                        salir = false;
//                        break;
//                    }
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
                                            Dato aux2 = a.dato.clonar();
                                            aux.nombre = a.nombre;
                                            aux.tipo = a.tipo;
                                            aux.addDato(aux2);
                                        }
                                        val.addDato(aux);
                                    }
                                }else{
                                    for (NodoSimbolo a : atrs) {
                                        Dato aux = a.dato.clonar();
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
                
                break;
                // </editor-fold>
            }
            case "imprimir":{//YA ESTUVO 
                // <editor-fold desc="imprimir">
                String val = Ejecutar(nodo.hijos.get(0)).toString();
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
                    coordenadas.clear();
                    Dato auxActual = new Dato();
                    Dato auxAnterior = new Dato();
                    boolean esArreglo = false, esFun = false, primero = true;
                    for (int i = 0;i<nodo.contador;i++) {
                        NodoParser actual = nodo.hijos.get(i);
                        String s = actual.nombre;
                        if(s.equals("ID")) {
                            accesoActivo = actual.valor;
                            auxActual.nombre = accesoActivo;
                            auxActual.tipo = claseInicio;
                        }else if(s.equals("PUNTO")){
                            //<editor-fold desc="PUNTO">
                            
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
                            }
                            boolean encontrado = false;
                            String nomNuevo = actual.valor;
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
                                accesoActivo = actual.valor;
                                auxActual.nombre = accesoActivo;
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
                            retornar = false;
                            contador++;
                            addAmbito("fun"+contador);
                            String nom = auxActual.nombre;
                            if(actual.nombre.equals("enviados")){
                                Ejecutar(actual);
                                String parametros;
                                if(auxActual.tipo.equals(claseInicio)){
                                    parametros = Analizador.tabla.getParametros(nom, "global", sobrecarga, auxActual.tipo);
                                }else{
                                    parametros = Analizador.tablaImport.getParametros(nom, "global", sobrecarga, auxActual.tipo);
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
                                agregarAtributosTemporles(auxAnterior.lista, amb);
                            }
                            NodoParser ins;
                            if(auxActual.tipo.equals(claseInicio)){
                                ins = Analizador.tabla.getInstrucciones(nom, "global", sobrecarga, auxActual.tipo);
                            }else{
                                ins = Analizador.tablaImport.getInstrucciones(nom, "global", sobrecarga, auxActual.tipo);
                            }
                            Ejecutar(ins);
                            auxActual = retornable;
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
                contador++;
                addAmbito("funH"+contador);
                String nom = nodo.hijos.get(0).valor;
                if(nodo.contador == 2){
                    Ejecutar(nodo.hijos.get(1));
                }
                String parametros = Analizador.tablaH.getParametros(nom, "global", "nul", "haskell");
                String[] p = parametros.split(",");
                String[] e = enviados.split(",");
                String amb = ambitos.get(ambitos.size()-1);
                for(int i = 0;i < p.length;i++){
                    Dato d = new Dato(e[i]);
                    NodoSimbolo n = new NodoSimbolo(p[i], "number", amb, d,"haskell","publico");
                    Analizador.tablaH.insertar(n);
                }
                NodoParser ins = Analizador.tablaH.getInstrucciones(nom, "global", "nul", "haskell");
                Dato d = EjecucionH.Ejecutar(ins);
                eliminarAmbito();
                return d;
                // </editor-fold>
            }
            case "enviados":{//YA ESTUVO
                // <editor-fold desc="enviados">
                enviados = "";
                sobrecarga = "";
                for (NodoParser n : nodo.hijos) {
                    Dato d = Ejecutar(n);
                    enviados += d.valor + ",";
                    sobrecarga += d.tipo + ",";
                }
                enviados = enviados.substring(0,enviados.length()-1);
                sobrecarga = sobrecarga.substring(0,sobrecarga.length()-1);
                break;
                // </editor-fold>
            }
            // <editor-fold desc="PROCESO DE DATOS">
            case "columna":{
                // <editor-fold desc="ACCCIONES">
                
                break;
                // </editor-fold>
            }
            case "procesar":{
                // <editor-fold desc="ACCCIONES">
                
                break;
                // </editor-fold>
            }
            case "donde":{
                // <editor-fold desc="ACCCIONES">
                
                break;
                // </editor-fold>
            }
            case "dondeCada":{
                // <editor-fold desc="ACCCIONES">
                
                break;
                // </editor-fold>
            }
            case "dondeTodo":{
                // <editor-fold desc="ACCCIONES">
                
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
            default:
                throw new AssertionError();
        }
        
        return new Dato("salio de sw");
    }
    
    public static void agregarAtributosTemporles(ArrayList<Dato> lista, String ambito){
        for (Dato d : lista) {
            if(d.esLista){
                agregarAtributosTemporles(d.lista, ambito);
            }else{
                NodoSimbolo nuevo = new NodoSimbolo(d.nombre, d.tipo, ambito, d, claseInicio, "publico");
                insertar(nuevo);
            }
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
}
