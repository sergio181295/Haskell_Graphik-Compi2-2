package clientehaskellgraphik;

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

public class Conexion {
    /**
     * LA CONEXION ES POR MEDIO DE SOCKETS, ENVINADO ARREGLOS DE BYTES, CON UN FORMATO EN ESPECIFICO, 
     * EL ARREGLO TIENE DE PRIMERO EL TAMÑO DE LA CADENA SEGUIDO DE LA CADENA
     * AL RECIBIR LA RESPUESTA SE LEE PRIMERO EL ENTERO QUE DEFINE EL TAMAÑO Y SE UTULIZA 
     * PARA LEER EL RESTO DE LA CADENA
     */
    public Conexion(){
        
    }
    public static String Conectar(String cadena){
        //CREAMOS EL ENCONDER PARA PASAR STRINS A BYTES, Y EL DECODER PARA PASAR DE BYTES A STRING
        Charset charset = Charset.forName("UTF-8");
        CharsetDecoder decoder = charset.newDecoder();
        CharsetEncoder encoder = charset.newEncoder();
        byte [] underlyingBuffer = new byte[1024];
        ByteBuffer buffer = ByteBuffer.wrap(underlyingBuffer);
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        try{
            Socket client = new Socket("192.168.43.229", 8081);
            //ENVIAMOS DATOS
            OutputStream oStream = client.getOutputStream();
            InputStream iStream = client.getInputStream();
            serializar(buffer, cadena, encoder);
            buffer.flip();
            int dataToSend = buffer.remaining();
            int remaining = dataToSend;
            while(remaining > 0){
                oStream.write(buffer.get());
                -- remaining;
            }
            //RECIBIMOS RESPUESTA
            ByteBuffer readBuffer = ByteBuffer.allocate(1024);
            readBuffer.order(ByteOrder.LITTLE_ENDIAN);

            int db = iStream.read();
            while(db != -1){
                readBuffer.put((byte)db);
                db = iStream.read();
            }
            readBuffer.flip();
            int length = readBuffer.getInt();
            byte [] stringBuffer = new byte[length];
            readBuffer.get(stringBuffer);
            String respuesta = decoder.decode(ByteBuffer.wrap(stringBuffer)).toString();

//            System.out.println("Respuesta: \n" + respuesta);
            System.out.flush();
            client.close();
            return respuesta;
        } catch(Exception e) {
            e.printStackTrace(System.err);
        }
        return "error";
    }
    //METODO ENCARGADO DE PASAR LA CADENA A UN BUFFER DE BYTS POR MEDIO DEL ENCONDER
    private static void serializar(ByteBuffer buffer, String cad, CharsetEncoder encoder){
        CharBuffer nameBuffer = CharBuffer.wrap(cad.toCharArray());
        ByteBuffer nbBuffer = null;
        //LONGITUD DE CADENA
        try
        {
            nbBuffer = encoder.encode(nameBuffer);
            System.out.println(String.format("String [%1$s] #bytes = %2$s", cad, nbBuffer.limit()));
        } 
        catch(CharacterCodingException e)
        {
            throw new ArithmeticException();
        }
        buffer.putInt(nbBuffer.limit());
        buffer.put(nbBuffer);
    }
}
