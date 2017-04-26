package clientehaskellgraphik;

import Compilador.Haskell.EjecucionH;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.io.*;      
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import Compilador.*;
import Compilador.Graphik.EjecucionG;
import Compilador.Graphik.RecoleccionG;
import Tabla_Simbolos.Dato;
import java.awt.Color;
import java.awt.event.KeyEvent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardXYItemLabelGenerator;
import org.jfree.chart.labels.XYItemLabelGenerator;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
        
public class Ventana extends javax.swing.JFrame {
    
    //VARIABLES PARA LA GRAFICA
    public static XYSeriesCollection lineas = new XYSeriesCollection();
    
    public Ventana() {
        initComponents();
        this.setLocationRelativeTo(null);
        File archivo = new File("C:\\Users\\pipaa\\Desktop\\hp_sergio\\prueba.gk");//C:\\Users\\pipaa\\Desktop
        FileReader fr;
        try {
            fr = new FileReader (archivo.getPath());
            BufferedReader br = new BufferedReader(fr);
            String texto = "";
            String aux;
            while((aux=br.readLine())!=null){
                texto += aux + "\n";
            }
            texto = texto.replaceAll("\t", "    ");
            br.close();
            JTextArea ta = new JTextArea();
            ta.setTabSize(4);
            ta.setText(texto);
            JScrollPane sp = new JScrollPane(ta);
            pestañas.addTab(archivo.getName(), sp);
            pestañas.setSelectedIndex(pestañas.getComponentCount()-1);
            Rutas.add(archivo.getPath());
            RecoleccionG.rutaProyecto = archivo.getParent();
            Nombres.add(archivo.getName());
            TextAreas.add(ta);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pestañas = new javax.swing.JTabbedPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        consola = new javax.swing.JTextArea();
        btnlogin = new javax.swing.JButton();
        nuevoProyecto = new javax.swing.JButton();
        impoexp = new javax.swing.JButton();
        nuevo = new javax.swing.JButton();
        abrir = new javax.swing.JButton();
        guardar = new javax.swing.JButton();
        guardarComo = new javax.swing.JButton();
        cerrar = new javax.swing.JButton();
        ejecutar = new javax.swing.JButton();
        reportes = new javax.swing.JButton();
        btnTablaSimbolos = new javax.swing.JButton();
        entradaComando = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        btnTablaSimbolos1 = new javax.swing.JButton();
        ejecutar1 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        importarDatos = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        consola.setColumns(20);
        consola.setRows(5);
        consola.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane1.setViewportView(consola);

        btnlogin.setText("Login");
        btnlogin.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnloginActionPerformed(evt);
            }
        });

        nuevoProyecto.setText("Nuevo Proyecto");
        nuevoProyecto.setEnabled(false);
        nuevoProyecto.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoProyectoActionPerformed(evt);
            }
        });

        impoexp.setText("Imp/Exp");
        impoexp.setEnabled(false);
        impoexp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                impoexpActionPerformed(evt);
            }
        });

        nuevo.setText("Nuevo");
        nuevo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nuevoActionPerformed(evt);
            }
        });

        abrir.setText("Abrir");
        abrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                abrirActionPerformed(evt);
            }
        });

        guardar.setText("Guardar");
        guardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                guardarActionPerformed(evt);
            }
        });

        guardarComo.setText("Guardar Como");

        cerrar.setText("Cerrar");
        cerrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cerrarActionPerformed(evt);
            }
        });

        ejecutar.setText("Ejecutar Haskell");
        ejecutar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutarActionPerformed(evt);
            }
        });

        reportes.setText("Reportes");

        btnTablaSimbolos.setText("Tabla de Simbolos");
        btnTablaSimbolos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTablaSimbolosActionPerformed(evt);
            }
        });

        entradaComando.setText("$ConjuntoFuncPolinomial {3,286}$");
        entradaComando.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                entradaComandoKeyPressed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 16)); // NOI18N
        jLabel1.setText("Consola");

        btnTablaSimbolos1.setText("Registrarse");
        btnTablaSimbolos1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTablaSimbolos1ActionPerformed(evt);
            }
        });

        ejecutar1.setText("Ejecutar Graphik");
        ejecutar1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ejecutar1ActionPerformed(evt);
            }
        });

        jButton1.setText("Grafica");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        importarDatos.setText("Cargar Datos");
        importarDatos.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                importarDatosActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 660, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(entradaComando, javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 660, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(reportes, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTablaSimbolos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnTablaSimbolos1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nuevoProyecto, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(impoexp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(nuevo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(abrir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guardar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(guardarComo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(cerrar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ejecutar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnlogin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ejecutar1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(importarDatos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(60, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pestañas, javax.swing.GroupLayout.PREFERRED_SIZE, 423, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 140, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(entradaComando, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnlogin, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(nuevoProyecto, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(impoexp, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(nuevo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(abrir, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guardar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(guardarComo, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cerrar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ejecutar, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ejecutar1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(5, 5, 5)
                        .addComponent(reportes, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTablaSimbolos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTablaSimbolos1, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(importarDatos, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    ArrayList<JTextArea> TextAreas = new ArrayList<JTextArea>();
    ArrayList<String> Nombres = new ArrayList<String>();
    ArrayList<String> Rutas = new ArrayList<String>();
    public static String listaProyectos;
    private void btnloginActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnloginActionPerformed
        Login_Form l = new Login_Form();
        l.setVisible(true);
    }//GEN-LAST:event_btnloginActionPerformed
    
    int contadorPestañas = 0;
    private void nuevoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoActionPerformed
        JTextArea ta = new JTextArea();
        JScrollPane sp = new JScrollPane(ta);
        pestañas.addTab("Nuevo-"+String.valueOf(contadorPestañas+1), sp);
        TextAreas.add(ta);
        Nombres.add("Nuevo-"+String.valueOf(contadorPestañas+1));
        Rutas.add("nul");
        contadorPestañas++;
    }//GEN-LAST:event_nuevoActionPerformed

    private void cerrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cerrarActionPerformed
        int pos = pestañas.getSelectedIndex();
        pestañas.removeTabAt(pos);
        Rutas.remove(pos);
        Nombres.remove(pos);
        TextAreas.remove(pos);
    }//GEN-LAST:event_cerrarActionPerformed

    private void abrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_abrirActionPerformed
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\pipaa\\Desktop"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            FileReader fr;
            try {
                fr = new FileReader (archivo.getPath());
                BufferedReader br = new BufferedReader(fr);
                String texto = "";
                String aux;
                while((aux=br.readLine())!=null){
                    texto += aux + "\n";
                }
                texto = texto.replaceAll("\t", "    ");
                br.close();
                JTextArea ta = new JTextArea();
                ta.setTabSize(4);
                ta.setText(texto);
                JScrollPane sp = new JScrollPane(ta);
                pestañas.addTab(archivo.getName(), sp);
                pestañas.setSelectedIndex(pestañas.getComponentCount()-1);
                Rutas.add(archivo.getPath());
                RecoleccionG.rutaProyecto = archivo.getParent();
                Nombres.add(archivo.getName());
                TextAreas.add(ta);
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_abrirActionPerformed

    private void guardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_guardarActionPerformed

    }//GEN-LAST:event_guardarActionPerformed

    private void nuevoProyectoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nuevoProyectoActionPerformed
        NuevoProyecto_Form np = new NuevoProyecto_Form();
        np.setVisible(true);
    }//GEN-LAST:event_nuevoProyectoActionPerformed

    private void ejecutarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutarActionPerformed
        String texto = getTextBox(Nombres.get(pestañas.getSelectedIndex())).getText();
        Analizador.compilarH(texto,true);
    }//GEN-LAST:event_ejecutarActionPerformed

    private void entradaComandoKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_entradaComandoKeyPressed
        if(evt.getKeyCode() == KeyEvent.VK_ENTER) {
            String entrada = entradaComando.getText();
            if(Analizador.compilarHC(entrada)){
                consola.append(">>"+entrada+"\n");
                consola.append(">>"+EjecucionH.imprimir+"\n");
                EjecucionH.ultimoValor = EjecucionH.imprimir;
            }
        }
    }//GEN-LAST:event_entradaComandoKeyPressed

    private void btnTablaSimbolosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTablaSimbolosActionPerformed
        Analizador.tabla.GenerarTablaHTML();
        Analizador.tablaH.GenerarTablaHTML();
        Analizador.tablaImport.GenerarTablaHTML();
    }//GEN-LAST:event_btnTablaSimbolosActionPerformed

    private void btnTablaSimbolos1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTablaSimbolos1ActionPerformed
        Registro_Form r = new Registro_Form();
        r.setVisible(true);
    }//GEN-LAST:event_btnTablaSimbolos1ActionPerformed

    private void ejecutar1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ejecutar1ActionPerformed
        String texto = getTextBox(Nombres.get(pestañas.getSelectedIndex())).getText();
        Analizador.textoRaiz = texto;
        if(Analizador.compilarG(texto,0,"actual",true)){
            consola.setText(EjecucionG.imprimir);
            EjecucionG.imprimir = "";
        }
    }//GEN-LAST:event_ejecutar1ActionPerformed

    private void impoexpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_impoexpActionPerformed
        // TODO add your handling code here:
        ImportarExportar_Form ie = new ImportarExportar_Form();
        ie.setVisible(true);
    }//GEN-LAST:event_impoexpActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        JFreeChart grafica = ChartFactory.createXYLineChart(
        "Grafica" , "X", "Y", lineas, PlotOrientation.VERTICAL,  true, true, false);               

        //personalización del grafico
        XYPlot xyplot = (XYPlot) grafica.getPlot();
        xyplot.setBackgroundPaint( Color.white );
        xyplot.setDomainGridlinePaint( Color.BLACK );
        xyplot.setRangeGridlinePaint( Color.BLACK );        
        // -> Pinta Shapes en los puntos dados por el XYDataset
        XYLineAndShapeRenderer xylineandshaperenderer = (XYLineAndShapeRenderer) xyplot.getRenderer();
        xylineandshaperenderer.setBaseShapesVisible(true);
        //--> muestra los valores de cada punto XY
        XYItemLabelGenerator xy = new StandardXYItemLabelGenerator();
        xylineandshaperenderer.setBaseItemLabelGenerator( xy );
        xylineandshaperenderer.setBaseItemLabelsVisible(true);
         xylineandshaperenderer.setBaseLinesVisible(true);
         xylineandshaperenderer.setBaseItemLabelsVisible(true);
         
         ChartFrame graf = new ChartFrame("Grafica",grafica);
         graf.pack();
         graf.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
         graf.setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void importarDatosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_importarDatosActionPerformed
        // TODO add your handling code here:
        boolean esEncabezado = true;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File("C:\\Users\\pipaa\\Desktop"));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File archivo = fileChooser.getSelectedFile();
            FileReader fr;
            try {
                fr = new FileReader (archivo.getPath());
                BufferedReader br = new BufferedReader(fr);
                String texto = "";
                String linea;
                int contador = 0;
                while((linea=br.readLine())!=null){
                    if(esEncabezado){
                        esEncabezado = false;
                        linea = linea.replace('[', ' ');
                        linea = linea.replace(']', ' ');
                        linea = linea.replace('"', ' ');
                        String[] aux = linea.split(",");
                        for (String s : aux) {
                            Dato d = new Dato(s.trim());
                            EjecucionG.datos.add(d);
                        }
                    }else{
                        linea = linea.replace('{', ' ');
                        linea = linea.replace('}', ' ');
                        linea = linea.replace('"', ' ');
                        String[] aux = linea.split(",");
                        int x = aux.length;
                        for (int i = 0; i < x; i++) {
                            Dato d = new Dato(aux[i].trim());
                            EjecucionG.datos.get(i).addDato(d);
                        }
                    }
                }
                for (Dato dato : EjecucionG.datos) {
                    System.out.println(dato.toString());
                }
                br.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_importarDatosActionPerformed
    
    
    public JTextArea getTextBox(String nombre) {
        int x = Nombres.size();
        for(int i = 0;i < x;i++) {
            if(Nombres.get(i) == nombre) {
                return TextAreas.get(i);
            }
        }
        return null;
    }
    public String getRutaActual(String nombre) {
        int x = Nombres.size();
        for(int i = 0;i < x;i++) {
            if(Nombres.get(i)== nombre) {
                return Rutas.get(i);
            }
        }
        return "nul";
    }
    public void cambiarRuta(String nombre, String ruta) {
        int x = Nombres.size();
        for(int i = 0;i < x;i++) {
            if(Nombres.get(i) == nombre) {
                Rutas.set(i, ruta);
            }
        }
    }
    //public static boolean login = false;
    
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Ventana().setVisible(true);
            }
        });
    }
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton abrir;
    private javax.swing.JButton btnTablaSimbolos;
    private javax.swing.JButton btnTablaSimbolos1;
    private javax.swing.JButton btnlogin;
    private javax.swing.JButton cerrar;
    private javax.swing.JTextArea consola;
    private javax.swing.JButton ejecutar;
    private javax.swing.JButton ejecutar1;
    private javax.swing.JTextField entradaComando;
    private javax.swing.JButton guardar;
    private javax.swing.JButton guardarComo;
    public static javax.swing.JButton impoexp;
    private javax.swing.JButton importarDatos;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton nuevo;
    public static javax.swing.JButton nuevoProyecto;
    private javax.swing.JTabbedPane pestañas;
    private javax.swing.JButton reportes;
    // End of variables declaration//GEN-END:variables
}
