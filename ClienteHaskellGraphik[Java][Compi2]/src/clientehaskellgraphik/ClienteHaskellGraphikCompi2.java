package clientehaskellgraphik;

import Tabla_Simbolos.Dato;
import java.io.File;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.CharsetEncoder;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;


import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;
import java.util.Collections;

public class ClienteHaskellGraphikCompi2 {
    
    public static void main(String[] args) {    
        Conexion c = new Conexion();
        Ventana v = new Ventana();
        v.setVisible(true);
        
        Dato d1 = new Dato("num");
        Dato d2 = new Dato("num");
        Dato d3 = new Dato("num");
        Dato d4 = new Dato("10");
        Dato d5 = new Dato("2");
        Dato d6 = new Dato("3");
        Dato d7 = new Dato("2");
        d2.addDato(d4);
        d2.addDato(d5);
        d3.addDato(d6);
        d3.addDato(d7);
        d1.addDato(d2);
        d1.addDato(d3);
    }
}
