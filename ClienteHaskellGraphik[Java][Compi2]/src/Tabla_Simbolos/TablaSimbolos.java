package Tabla_Simbolos;

import Compilador.Analizador;
import Compilador.Graphik.EjecucionG;
import Compilador.NodoParser;
import Compilador.token;
import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class TablaSimbolos {
    String nombre;
    int tamaño, elementos;
    double factorCarga;
    NodoSimbolo[] tabla;

    public TablaSimbolos(String nombre) {
        this.nombre = nombre;
        tamaño = 1000;
        elementos = 0;
        factorCarga = 0;
        tabla = new NodoSimbolo[tamaño];
        for (int i = 0; i < tamaño; i++) {
            tabla[i] = null;
        }
    }

    public void Limpiar(){
        for (int i = 0; i < tamaño; i++) {
                tabla[i] = null;
            }
    }
    public double equivalente(String id) {
        String valor = "";
        int x = id.length();
        for (int i = 0; i < x; i++) {
            valor = valor + (int)id.charAt(i);
        }
        double salida = Double.parseDouble(valor);
        return salida;
    }
    public int posicion(String id) {
        double pos;
        double eq;
        eq = equivalente(id);
        pos = eq % tamaño;
        int col = 0;
        double c = pos;
        while (tabla[(int)c] != null) {//&& !tabla[(int)c].nombre.equals(id)
            col++;
            c = pos + (col * col);
            c = c % tamaño;
        }
        return (int)c;
    }
    public void insertar(NodoSimbolo nodo) {
        int pos = existe(nodo.nombre, nodo.ambito, nodo.sobrecarga, nodo.rol, nodo.clase);
        if (pos == -1) {
            pos = posicion(nodo.nombre);
            tabla[pos] = nodo;
            elementos++;
            factorCarga = (double)(elementos / tamaño);
            if(EjecucionG.debuguear){
                this.GenerarTablaHTML();
            }
        } else {
        }
    }

    public int existe(String nombre, String ambito, String sobrecarga, String rol, String clase) {
        int salida = -1;
        for (int i = 0; i < tamaño; i++) {
            if (tabla[i] != null) {
                if (!tabla[i].eliminado && tabla[i].nombre.equals(nombre) && tabla[i].ambito.equals(ambito) && tabla[i].sobrecarga.equals(sobrecarga) && tabla[i].rol.equals(rol) && tabla[i].clase.equals(clase)) {
                    salida = i;
                }
            }
        }
        return salida;
    }
    
    public String getTipo(String nombre, String ambito, String sobrecarga, String rol, String clase) {
        String salida = "-";
        int pos = existe(nombre, ambito, sobrecarga, rol, clase);
        if (pos != -1) {
            salida = tabla[pos].tipo;
        } else {
            System.out.println(">> La variable no existe");
        }
        return salida;
    }

    public String getRol(String nombre, String ambito, String clase) {
        for (NodoSimbolo n : tabla) {
            if(n != null){
                if(n.nombre.equals(nombre) && n.ambito.equals(ambito) && n.clase.equals(clase)){
                    return n.rol;
                }
            }
        }
        return null;
    }
    
    public Dato getValor(String nombre, String ambito, String clase) {
        int pos = existe(nombre, ambito, "nul", "variable", clase);
        if (pos != -1) {
            return tabla[pos].dato;
        }
        return null;
    }

    public NodoParser getInstrucciones(String nombre, String ambito, String sobrecarga, String clase) {
        NodoParser salida = null;
        int pos = existe(nombre, ambito, sobrecarga, "funcion", clase);
        if (pos != -1) {
            salida = tabla[pos].instrucciones;
        }
        return salida;
    }

    public String getParametros(String nombre, String ambito, String sobrecarga, String clase) {
        String salida = null;
        int pos = existe(nombre, ambito, sobrecarga, "funcion", clase);
        if (pos != -1) {
            salida = tabla[pos].parametros;
        }
        return salida;
    }

    public String getRol(String nombre, String clase) {
        for(int i = 0; i < tamaño; i++) {
            if(tabla[i] != null) {
                if(tabla[i].nombre.equals(nombre)&&tabla[i].clase.equals(clase)) {
                    return tabla[i].rol;
                }
            }
        }
        return "-";
    }
    public boolean modificarValor(String nombre, String ambito, Dato dato, String clase) {
        boolean Salida = false;
        int pos = existe(nombre, ambito, "nul","variable", clase);
        if (pos != -1) {
            if(EjecucionG.debuguear){
                this.GenerarTablaHTML();
            }
            tabla[pos].dato = dato;
            Salida = true;
        }
        return Salida;
    }
    
    public void eliminarPorAmbito(String ambito){
        for (NodoSimbolo n : tabla) {
            if(n != null){
                if(!n.eliminado && n.ambito.equals(ambito)){
                    n.eliminado = true;
                }
            }
        }
    }
    
    public NodoSimbolo getHaskell(String nombre){
        for (NodoSimbolo n : tabla) {
            if(n != null){
                if(n.nombre.equals(nombre)){
                    return n;
                }
            }
        }
        return null;
    }
    
    public ArrayList<NodoSimbolo> getNodo(String clase){
        ArrayList<NodoSimbolo> salida = new ArrayList<>();
        for (NodoSimbolo n : tabla) {
            if(n != null){
                if(n.clase.equals(clase) && !n.visibilidad.equals(token.protegido)){
                    salida.add(n);
                }
            }
        }
        return salida;
    }
    
    public ArrayList<NodoSimbolo> getAtributos(String clase){
        ArrayList<NodoSimbolo> salida = new ArrayList<>();
        for (NodoSimbolo n : tabla) {
            if(n != null){
                if(n.clase.equals(clase) && !n.tipo.equals(token.als)){
                    salida.add(n);
                }
            }
        }
        return salida;
    }
    
    public void GenerarTablaHTML() {
        String html = "<HTML> <HEAD> <TITLE> "+nombre+" </TITLE> </HEAD> <BODY>\n";
        html += "<H1 ALIGN=CENTER>"+nombre+" </H1> \n";
        html += "<H3 ALIGN=LEFT>&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;&emsp;Fecha: </H3> ";
        html += "<br>";

            html += "<table width=\"100%\" border=\"2\" align=\"center\" cellspacing=\"0\" bordercolor=\"#000000\" bgcolor=\"#FFCC99\">";
            html += "<tr>";
            html += "   <th><strong> No. </th>";
            html += "   <th><strong> Nombre </th>";
            html += "   <th><strong> Tipo </th>";
            html += "   <th><strong> Ambito </th>";
            html += "   <th><strong> Valor </th>";
            html += "   <th><strong> Rol </th>";
            html += "   <th><strong> Parametros </th>";
            html += "   <th><strong> Seobrecarga </th>";
            html += "   <th><strong> Clase </th>";
            html += "</tr>";

            for (int i = 0;i<this.tamaño;i++) {
                NodoSimbolo n = tabla[i];
                if(n != null && !n.eliminado) {
                    html += "<tr>";
                    html += "   <td align=\"center\">"+i+"</td>";
                    html += "   <td align=\"center\">" + n.nombre + "</td>";
                    html += "   <td align=\"center\">" + n.tipo + "</td>";
                    html += "   <td align=\"center\">" + n.ambito + "</td>";
                    if(n.dato != null){
                        html += "   <td align=\"center\">" + n.dato.toString() + "</td>";
                    }else{
                        html += "   <td align=\"center\">null</td>";
                    }
                    html += "   <td align=\"center\">" + n.rol + "</td>";
                    html += "   <td align=\"center\">" + n.parametros + "</td>";
                    html += "   <td align=\"center\">" + n.sobrecarga + "</td>";
                    html += "   <td align=\"center\">" + n.clase + "</td>";
                    html += "</tr>";
                }
            }
            html += "</table>";

        html += "</BODY></HTML>";
        File archivo = new File("Reportes/"+nombre+".html");
        BufferedWriter bw;
        try {
            bw = new BufferedWriter(new FileWriter(archivo));
            bw.write(html);
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(TablaSimbolos.class.getName()).log(Level.SEVERE, null, ex);
        }
        Desktop desktop;
        if (Desktop.isDesktopSupported()){// si éste Host soporta esta API 
            desktop = Desktop.getDesktop();//objtengo una instancia del Desktop(Escritorio)de mi host 
            try {            
                desktop.open(archivo);//abro el archivo con el programa predeterminado
            } catch (IOException ex) {
                Logger.getLogger(TablaSimbolos.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       else{ 
            System.out.println("Lo lamento,no se puede abrir el archivo; ésta Maquina no soporta la API Desktop");
       }

    }
}
