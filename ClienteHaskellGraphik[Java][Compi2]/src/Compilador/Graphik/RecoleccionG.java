package Compilador.Graphik;

import Compilador.Analizador;
import Compilador.NodoParser;
import Compilador.token;
import Tabla_Simbolos.Dato;
import Tabla_Simbolos.NodoSimbolo;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RecoleccionG {
    public int idTabla; // 0 == tabla, 1 == tablaImport
    public RecoleccionG(int idTabla) {
        this.idTabla = idTabla;
    }
  
    public static String rutaProyecto; 
    
    //VARIABLES AUXILIARES PARA LA RECOLECCION
     String nombreClase;
    String idParametros = "", sobrecarga = "";
    String tipoActivo = "", visActiva = "";
    boolean heredo = false;
    ArrayList<Integer> dimensiones = new ArrayList<>();
    ArrayList<Dato> lista = new ArrayList<>();
    public  Dato Recolectar(NodoParser nodo){
        switch (nodo.nombre) {
            case "inicio":{
                for (NodoParser n : nodo.hijos) {
                    Recolectar(n);
                }
                break;
            }
            case "imporGra":{
                for (NodoParser n : nodo.hijos) {
                    String ruta = rutaProyecto+"/" + n.valor + ".gk";
                    File archivo = new File(ruta);
                    FileReader fr;
                    try {
                        fr = new FileReader (archivo.getPath());
                        BufferedReader br = new BufferedReader(fr);
                        String texto = "";
                        String aux;
                        while((aux=br.readLine())!=null){
                            texto += aux + "\n";
                        }
                        br.close();
                        Analizador.compilarG(texto,1,n.valor+".gk",false);
                        
                    }catch (FileNotFoundException ex) {
                        Logger.getLogger(RecoleccionG.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RecoleccionG.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                if(idTabla == 0){
                    Analizador.soloGetRizG();
                }
                break;
            }
            case "imporHash":{
                for (NodoParser n : nodo.hijos) {
                    String nombre = n.valor;
                    NodoSimbolo ns = Analizador.tablaH.getHaskell(nombre);
                    if(ns != null) Analizador.tabla.insertar(ns);
                }
                break;
            }
            case "clases":{
                for (NodoParser n : nodo.hijos) {
                    Recolectar(n);
                }
                break;
            }
            case "ALS":{
                String vis = token.publico;
                for (NodoParser n : nodo.hijos) {
                    if(n.nombre.equals("ID")){
                        nombreClase = n.valor;
                        EjecucionG.clases.add(nombreClase);
                    }else if(n.nombre.equals("VISIBILIDAD")){
                        vis = n.valor;
                    }else if(n.nombre.equals("HEREDA")){
                        ArrayList<NodoSimbolo> lista = Analizador.tablaImport.getNodo(n.valor);
                        if(lista.isEmpty() || heredo){
                            System.out.println("No se pudo heredar o ya se heredo una vez");
                            break;
                        }
                        for (NodoSimbolo n1 : lista) {
                            insertar(n1);
                        }
                        heredo = true;
                    }else{
                        Recolectar(n);
                    }
                }
                NodoSimbolo ns = new NodoSimbolo(nombreClase, "ALS", "global", null, nombreClase, vis);
                insertar(ns);
                break;
            }
            case "codigoClase":{
                for (NodoParser n : nodo.hijos) {
                    Recolectar(n);
                }
                break;
            }
            case "funcion":{
                String nom = "", tipo = "", vis = token.publico;
                idParametros = "nul";
                sobrecarga = "nul";
                for (NodoParser n : nodo.hijos) {
                    if(n.nombre.equals("ID")){
                        nom = n.valor;
                        if(nom.equals("inicio")) EjecucionG.claseInicio = nombreClase;
                    }else if(n.nombre.equals("VISIBILIDAD")){
                        vis = n.valor;
                    }else if(n.nombre.equals("TIPO")){
                        tipo = n.valor;
                    }else if(n.nombre.equals("parametros")){
                        Recolectar(n);
                    }else{
                        NodoSimbolo n2 = new NodoSimbolo(nom, tipo, "global", idParametros, n, sobrecarga, nombreClase,vis);
                        insertar(n2);
                    }
                }
                break;
            }
            case "parametros":{
                idParametros = "";
                sobrecarga = "";
                for (NodoParser n : nodo.hijos) {
                    if(!n.valor.equals("[")){
                        if(n.nombre.equals("TIPO")){
                            sobrecarga += n.valor + ",";
                        }else{
                            idParametros += n.valor + ",";
                        }
                    }
                }
                idParametros = idParametros.substring(0, idParametros.length()-1);
                sobrecarga = sobrecarga.substring(0,sobrecarga.length()-1);
                break;
            }
            case "declaracion":{
                for (NodoParser n : nodo.hijos) {
                    if(n.nombre.equals("TIPO") || n.nombre.equals("ID")){
                        if(n.nombre.equals("ID")){
                            //FALTA: VERIFICAR QUE EL ALS EXISTA EN LA TABLA DE SIMBOLOS OSEA SI YA FUE DECLARADO
                        }else{
                            
                        }
                        tipoActivo = n.valor;
                    }else{
                        Recolectar(n);
                    }
                }
                break;
            }
            case "variable":{
                String vis = token.publico, nombre = "", nuevo = "";
                boolean esArr = false;
                Dato val = new Dato();
                dimensiones.clear();
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
                            dimensiones.add(dim);
                            break;
                        }   
                        case "exp":{
                            val = EjecucionG.Ejecutar(n);
                            break;
                        }
                        case "NUEVO":{
                            nuevo = nodo.hijos.get(i+1).valor;
                            if(tipoActivo.equals(nuevo)){
                                ArrayList<NodoSimbolo> atrs = Analizador.tablaImport.getAtributos(nuevo);
                                if(esArr){
                                    int totalObjs = 1;
                                    for (Integer e : dimensiones) {
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
                                        aux.tipo = nuevo;
                                        val.addDato(aux);
                                    }
                                    val.valor = nombre;
                                    val.tipo = nuevo;
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
                            esArr = false;
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
                if(!dimensiones.isEmpty()){
                    val.dimensiones.clear();
                    for (Integer c : dimensiones) {
                        val.dimensiones.add(c);
                    }
                }
                lista.clear();
                dimensiones.clear();
                val.nombre = nombre;
                NodoSimbolo ns = new NodoSimbolo(nombre, tipoActivo, "global", val, nombreClase, vis);
                insertar(ns);
                break;
            }
            default:
                throw new AssertionError();
        }
        
        return new Dato("nul");
    }
    
    public void insertar(NodoSimbolo n){
        if(idTabla == 0){
            Analizador.tabla.insertar(n);
        }else {
            Analizador.tablaImport.insertar(n);
        }
    }
}
