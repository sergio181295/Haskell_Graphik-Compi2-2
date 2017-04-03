package Compilador;

public class GenerarCompilador {
    public static void main(String[] args) {
        //Generar("H","src/Compilador/Haskell");
        //Generar("HC","src/Compilador/Haskell");
        Generar("G","src/Compilador/Graphik");
    }
    
    public static void Generar(String tipo, String ruta){
        try {
            String opcFlex[] = {"FlexYCup/Lexico"+tipo+".jflex", "-d", ruta };
            jflex.Main.generate(opcFlex);
            String opcCUP[] = { "-destdir", ruta, "-parser", "Parser"+tipo, "-symbols", "sym"+tipo, "FlexYCup/Sintactico"+tipo+".cup" };
            java_cup.Main.main(opcCUP);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
