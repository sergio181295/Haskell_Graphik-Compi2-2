package Compilador.Graphik;
import java_cup.runtime.Symbol;
import java_cup.runtime.*;
import java.io.Reader;
import java.awt.Color;
import javax.swing.JOptionPane;

%% //opciones

%class ScannerG 
%cupsym symG
%cup
%public
%unicode
%line
%char
%ignorecase
%full
%state COMENTARIO1 COMENTARIO2 CADENA CARACTER

%{
    String cadena="";
    public static int linea = 0;
    public static int columna = 0;
%}

Salto = \r|\n|\r\n
Espacio = " " | [ \t\f]  
Entero = [0-9]+
Decimal = [0-9]+"."[0-9]+
Id  = [A-Za-z][_0-9A-Za-z]*

%% //fin de opciones

 <YYINITIAL> {
//PROPIAS DE HASKELL
"ENTERO"         	  
{columna += 6;return new Symbol(symG.ENTERO,columna,linea,"ENTERO");}
"DECIMAL"         	  
{columna += 7;return new Symbol(symG.DECIMAL,columna,linea,"DECIMAL");}                                                     
"CARACTER"
{columna += 8;return new Symbol(symG.CARACTER,columna,linea,"CARACTER");}                                                     
"CADENA"         	  
{columna += 6;return new Symbol(symG.CADENA,columna,linea,"CADENA");}                                                     
"BOOL"         	  
{columna += 4;return new Symbol(symG.BOOL,columna,linea,"BOOL");}                                                     
"ALS"         	  
{columna += 3;return new Symbol(symG.ALS,columna,linea,"ALS");}                                                     
"VACIO"                                                              	  
{columna += 5;return new Symbol(symG.VACIO,columna,linea,"VACIO");}                                                     
"VERDADERO"                                                                            	  
{columna += 9;return new Symbol(symG.TRUE,columna,linea,"VERDADERO");}                       
"FALSO"                                                                            	  
{columna += 5;return new Symbol(symG.FALSE,columna,linea,"FALSO");}                       
"VAR"                                                                            	  
{columna += 3;return new Symbol(symG.VAR,columna,linea,"VAR");}                       
"SI"                                                                            	  
{columna += 2;return new Symbol(symG.SI,columna,linea,"SI");}                       
"SINO"                                                                            	  
{columna += 4;return new Symbol(symG.SINO,columna,linea,new String(yytext()));}                       
"PUBLICO"                                                                            	  
{columna += 7;return new Symbol(symG.PUBLICO,columna,linea,new String(yytext()));}                       
"PRIVADO"                                                                            	  
{columna += 7;return new Symbol(symG.PRIVADO,columna,linea,new String(yytext()));}                       
"PROTEGIDO"                                                                            	  
{columna += 10;return new Symbol(symG.PROTEGIDO,columna,linea,new String(yytext()));}                       
"IMPORTAR"                                                                            	  
{columna += 8;return new Symbol(symG.IMPORTAR,columna,linea,new String(yytext()));}                       
"HEREDA"                                                                            	  
{columna += 6;return new Symbol(symG.HEREDA,columna,linea,new String(yytext()));}                       
"NUEVO"                                                                            	  
{columna += 5;return new Symbol(symG.NUEVO,columna,linea,new String(yytext()));}                       
"RETORNAR"                                                                            	  
{columna += 8;return new Symbol(symG.RETORNAR,columna,linea,new String(yytext()));}      
"LLAMAR"                                                                            	  
{columna += 6;return new Symbol(symG.LLAMAR,columna,linea,new String(yytext()));}
"INCLUIR_HK"                                                                            	  
{columna += 10;return new Symbol(symG.INCLUIR_HK,columna,linea,new String(yytext()));}
"LLAMARHK"                                                                            	  
{columna += 8;return new Symbol(symG.LLAMARHK,columna,linea,new String(yytext()));}
"SELECCION"                                                                            	  
{columna += 9;return new Symbol(symG.SELECCION,columna,linea,new String(yytext()));}
"CASO"                                                                            	  
{columna += 4;return new Symbol(symG.CASO,columna,linea,new String(yytext()));}
"DEFECTO"                                                                            	  
{columna += 7;return new Symbol(symG.DEFECTO,columna,linea,new String(yytext()));}
"PARA"                                                                            	  
{columna += 4;return new Symbol(symG.PARA,columna,linea,new String(yytext()));}
"MIENTRAS"                                                                            	  
{columna += 8;return new Symbol(symG.MIENTRAS,columna,linea,new String(yytext()));}
"HACER"                                                                            	  
{columna += 5;return new Symbol(symG.HACER,columna,linea,new String(yytext()));}
"CONTINUAR"                                                                            	  
{columna += 9;return new Symbol(symG.CONTINUAR,columna,linea,new String(yytext()));}
"TERMINAR"                                                                            	  
{columna += 8;return new Symbol(symG.TERMINAR,columna,linea,new String(yytext()));}
"GRAPHIKAR_FUNCION"                                                                            	  
{columna += 17;return new Symbol(symG.GRAPHIKAR_FUNCION,columna,linea,new String(yytext()));}
"COLUMNA"                                                                            	  
{columna += 7;return new Symbol(symG.COLUMNA,columna,linea,new String(yytext()));}
"PROCESAR"                                                                            	  
{columna += 8;return new Symbol(symG.PROCESAR,columna,linea,new String(yytext()));}
"DONDE"                                                                            	  
{columna += 5;return new Symbol(symG.DONDE,columna,linea,new String(yytext()));}
"DONDECADA"                                                                            	  
{columna += 9;return new Symbol(symG.DONDECADA,columna,linea,new String(yytext()));}
"DONDETODO"                                                                            	  
{columna += 9;return new Symbol(symG.DONDETODO,columna,linea,new String(yytext()));}
"IMPRIMIR"       
{columna += 9;return new Symbol(symG.IMPRIMIRK,columna,linea,new String(yytext()));}                                                                     	  
//{columna += ;return new Symbol(symG.IMPRIMIRK,columna,linea,new String(yytext()));}
//""                                                                            	     
"+"         	  {columna += 1;return new Symbol(symG.MAS,columna,linea,"+");}
"-"         	  {columna += 1;return new Symbol(symG.MENOS,columna,linea,"-");}
"*"         	  {columna += 1;return new Symbol(symG.POR,columna,linea,"*");}
"/"         	  {columna += 1;return new Symbol(symG.DIV,columna,linea,"/");}
"^"         	  {columna += 1;return new Symbol(symG.POT,columna,linea,"^");}
"++"         	  {columna += 2;return new Symbol(symG.MASMAS,columna,linea,"++");}
"--"         	  {columna += 2;return new Symbol(symG.MENOSMENOS,columna,linea,"--");}
">"         	  {columna += 1;return new Symbol(symG.MAYOR,columna,linea,">");}
"<"         	  {columna += 1;return new Symbol(symG.MENOR,columna,linea,"<");}
">="         	  {columna += 2;return new Symbol(symG.MAYORIGUAL,columna,linea,">=");}
"<="         	  {columna += 2;return new Symbol(symG.MENORIGUAL,columna,linea,"<=");}
"=="         	  {columna += 2;return new Symbol(symG.IGUALIGUAL,columna,linea,"==");}
"!="         	  {columna += 2;return new Symbol(symG.NOIGUAL,columna,linea,"!=");}
"||"         	  {columna += 2;return new Symbol(symG.OR,columna,linea,"||");}
"&&"         	  {columna += 2;return new Symbol(symG.AND,columna,linea,"&&");}
"&|"         	  {columna += 2;return new Symbol(symG.XOR,columna,linea,"&|");}
"!"         	  {columna += 1;return new Symbol(symG.NOT,columna,linea,"!");}
"="         	  {columna += 1;return new Symbol(symG.IGUAL,columna,linea,"=");}
"["         	  {columna += 1;return new Symbol(symG.COR1,columna,linea,"[");}
"]"         	  {columna += 1;return new Symbol(symG.COR2,columna,linea,"]");}
"{"         	  {columna += 1;return new Symbol(symG.LLA1,columna,linea,"{");}
"}"         	  {columna += 1;return new Symbol(symG.LLA2,columna,linea,"}");}
"("         	  {columna += 1;return new Symbol(symG.PAR1,columna,linea,"(");}
")"         	  {columna += 1;return new Symbol(symG.PAR2,columna,linea,")");}
"."         	  {columna += 1;return new Symbol(symG.PUNTO,columna,linea,".");}
","         	  {columna += 1;return new Symbol(symG.COMA,columna,linea,",");}
"?"         	  {columna += 1;return new Symbol(symG.PUNTOYCOMA,columna,linea,"?");}
":"         	  {columna += 1;return new Symbol(symG.DOSPUNTOS,columna,linea,":");}
".GK"         	  {columna += 3;return new Symbol(symG.PGK,columna,linea,".GK");}
//""         	  {columna += ;return new Symbol(symG.,columna,linea,new String(yytext()));}
{Id}    		  {columna += new String(yytext()).length();return new Symbol(symG.ID,columna, linea, new String(yytext()));}
{Entero}     	  {columna += new String(yytext()).length();return new Symbol(symG.ENTERO,columna, linea, new String(yytext()));} 
{Decimal}     	  {columna += new String(yytext()).length();return new Symbol(symG.DECIMAL,columna, linea, new String(yytext()));} 
" "               {columna += 1; }
"\t" 			  {columna += 4; }
"\n"     		  {columna = 0; linea += 1;}
"\r"     		  {columna = 0; linea += 1;}
"\r\n"     		  {columna = 0; linea += 1;}
"#"               {cadena = "";yybegin(COMENTARIO1);}
"#/"              {cadena = "";yybegin(COMENTARIO2);}
[\"]              {cadena = ""; yybegin(CADENA);}
[\']              {cadena = ""; yybegin(CARACTER);}
}  

<CADENA> {
[^\"]   {cadena+=yytext();}
[\"]    {String temporal=cadena; cadena=""; yybegin(YYINITIAL);columna += temporal.length()+2;return new Symbol(symG.CADENA,columna,linea, temporal);}
}

<CARACTER> {
[^\']	{cadena += yytext();}
[\']	{String temporal = cadena; cadena = ""; yybegin(YYINITIAL);columna += 3;return new  Symbol(symG.CARACTER,columna,linea,temporal);}
}

<COMENTARIO1> {
[^\n]+  {/**/} 
[\n]    {columna = 0; linea += 1; yybegin(YYINITIAL);}
}

<COMENTARIO2> {
[^/#]+  {cadena += yytext();}
"/#"    {
			int x = cadena.length();
            for(int i = 0;i<x;i++){
                if(cadena.charAt(i) == '\n'){
                    linea += 1; 
                }
            }
			yybegin(YYINITIAL);
		}   
}

. {
        String errLex = "Error léxico, caracter irreconocible: '"+yytext()+"' en la línea: "+(linea+1)+" y columna: "+yycolumn;
        System.err.println(errLex);
}