package Compilador;

import Compilador.Haskell.EjecucionH;
import Compilador.Haskell.RecoleccionH;
import Compilador.Haskell.ParserHC;
import Compilador.Haskell.ScannerH;
import Compilador.Haskell.ParserH;
import Tabla_Simbolos.TablaSimbolos;
import java.io.*;
import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;
import Compilador.Graphik.*;

public class Analizador {
    public static NodoParser raiz;
    public static TablaSimbolos tabla = new TablaSimbolos("Tabla de Simbolos");
    public static TablaSimbolos tablaH = new TablaSimbolos("Tabla de Haskell");
    public static TablaSimbolos tablaImport = new TablaSimbolos("Tabla de Importaciones");
    public static String textoRaiz = "";
    public static boolean debuguear = false;
    
    public static boolean compilarH(String codigo){
        StringReader mireader = new StringReader(codigo);
        ScannerH scanner = new ScannerH(mireader);
        ParserH parser = new ParserH(scanner);
        try {
            parser.parse();
            if(raiz != null){
                RecoleccionH.Recolectar(raiz);
                pintar("H");
                return true;
            }
            
        } catch (Exception ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean compilarHC(String codigo){
        StringReader mireader = new StringReader(codigo);
        ScannerH scanner = new ScannerH(mireader);
        ParserHC parser = new ParserHC(scanner);
        try {
            parser.parse();
            if(raiz != null){
                pintar("HC");
                EjecucionH.ambitos.add("global");
                EjecucionH.Ejecutar(raiz);
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static boolean compilarG(String codigo,int idTabla, String nomArch, boolean ejecutar){
        if(ejecutar){
            LimpiarG();
        }
        StringReader mireader = new StringReader(codigo);
        ScannerG scanner = new ScannerG(mireader);
        ParserG parser = new ParserG(scanner);
        parser.nombreArchivo = nomArch;
        try {
            parser.parse();
            if(raiz != null){
                pintar("G");
                RecoleccionG rg = new RecoleccionG(idTabla);
                rg.Recolectar(raiz);
                if(debuguear && ejecutar){
                    tablaImport.GenerarTablaHTML();
                }
                if(ejecutar){
                    EjecucionG.Ejecutar(raiz);
                }
                if(debuguear && ejecutar){
                    tabla.GenerarTablaHTML();
                }
                return true;
            }
        } catch (Exception ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
    public static void soloGetRizG(){
        StringReader mireader = new StringReader(textoRaiz);
        ScannerG scanner = new ScannerG(mireader);
        ParserG parser = new ParserG(scanner);
        try {
            parser.parse();
            if(raiz != null){
                pintar("G");
            }
        } catch (Exception ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private static int contador;
    private static String grafo;
    
    public static void pintar(String tipo){
        String dot = getDOT(raiz);
        File archivo = new File("Reportes/arbol"+tipo+".txt");
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(dot);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(Analizador.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        try {
          String dotPath = "C:\\Program Files (x86)\\Graphviz2.38\\bin\\dot.exe";
          String fileInputPath = "C:\\Users\\Sergio\\Dropbox\\Proyectos USAC\\Haskell_Graphik[Compi2]\\ClienteHaskellGraphik[Java][Compi2]\\Reportes\\arbol"+tipo+".txt";
          String fileOutputPath = "C:\\Users\\Sergio\\Desktop\\arbol"+tipo+".jpg";
          String tParam = "-Tjpg";
          String tOParam = "-o";
          String[] cmd = new String[5];
          cmd[0] = dotPath;
          cmd[1] = tParam;
          cmd[2] = fileInputPath;
          cmd[3] = tOParam;
          cmd[4] = fileOutputPath;
          Runtime rt = Runtime.getRuntime();
          rt.exec( cmd );
        } catch (Exception ex) {
          ex.printStackTrace();
        }
    }
    
    

    public static String getDOT(NodoParser raiz) {
        grafo = "digraph g{";
        grafo += "node[shape = \"box\"];";
        grafo += "nodo0[label = \""+raiz.nombre+";" + raiz.valor + "\"];\n";
        contador = 1;
        reccorerAST("nodo0", raiz);
        grafo += "}";
        return grafo;
    }

    public static void reccorerAST(String padre, NodoParser nodoPadre) {
        for (NodoParser nodoHijo : nodoPadre.hijos) {
            String hijo = "nodo" + contador;
            grafo += hijo + "[label = \""+nodoHijo.nombre+";" + nodoHijo.valor + "\"];\n";
            grafo += padre + "->" + hijo + ";\n";
            contador++;
            reccorerAST(hijo, nodoHijo);
        }
    }
    
    public static void LimpiarG(){
        raiz = null;
        tabla.Limpiar();
        tablaImport.Limpiar();
    }
    
    public static void LimpiarH(){
        raiz = null;
        tablaH.Limpiar();
    }
}
