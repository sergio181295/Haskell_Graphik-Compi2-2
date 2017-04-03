package Compilador.Haskell;
import java_cup.runtime.Symbol;
import java_cup.runtime.*;
import java.io.Reader;
import java.awt.Color;
import javax.swing.JOptionPane;

%% //opciones

%class ScannerH 
%cupsym symH
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
%}

Salto = \r|\n|\r\n
Espacio = {Salto} | [ \t\f]  
Entero = [0-9]+
Decimal = [0-9]+"."[0-9]+
Id  = [A-Za-z][_0-9A-Za-z]*

%% //fin de opciones

 <YYINITIAL> {
//PROPIAS DE HASKELL
"$"         	  {return new Symbol(symH.DOLAR,yychar,yyline,"$");}
"\'MOD\'"         {return new Symbol(symH.MOD,yychar,yyline,"MOD");}
"\'SQRT\'"        {return new Symbol(symH.SQRT,yychar,yyline,"SQRT");}
"\'POT\'"         {return new Symbol(symH.POT,yychar,yyline,"POT");}
"LET"             {return new Symbol(symH.LET,yychar,yyline,"LET");}
"CALCULAR"        {return new Symbol(symH.CALCULAR,yychar,yyline,"CALCULAR");}
"SUCC"         	  {return new Symbol(symH.SUCC,yychar,yyline,"SUCC");}
"DECC"         	  {return new Symbol(symH.DECC,yychar,yyline,"DECC");}
"MIN"         	  {return new Symbol(symH.MIN,yychar,yyline,"MIN");}
"MAX"         	  {return new Symbol(symH.MAX,yychar,yyline,"MAX");}
"SUM"         	  {return new Symbol(symH.SUM,yychar,yyline,"SUM");}
"PRODUCT"         {return new Symbol(symH.PRODUCT,yychar,yyline,"PRODUCT");}
"REVERS"          {return new Symbol(symH.REVERS,yychar,yyline,"REVERS");}
"IMPR"         	  {return new Symbol(symH.IMPR,yychar,yyline,"IMPR");}
"PAR"         	  {return new Symbol(symH.PAR,yychar,yyline,"PAR");}
"ASC"         	  {return new Symbol(symH.ASC,yychar,yyline,"ASC");}
"DESC"         	  {return new Symbol(symH.DESC,yychar,yyline,"DESC");}
"LENGTH"          {return new Symbol(symH.LENGTH,yychar,yyline,"LENGTH");}
"END"         	  {return new Symbol(symH.END,yychar,yyline,"END");}
"%"         	  {return new Symbol(symH.PORCENTAJE,yychar,yyline,"%");}
"++"         	  {return new Symbol(symH.MASMAS,yychar,yyline,"++");}
"!!"         	  {return new Symbol(symH.EXCLAMACION,yychar,yyline,"!!");}
"IF"         	  {return new Symbol(symH.SI,yychar,yyline,"IF");}
"THEN"         	  {return new Symbol(symH.ENTONCES,yychar,yyline,"THEN");}
"ELSE"         	  {return new Symbol(symH.SINO,yychar,yyline,"ELSE");}
"CASE"         	  {return new Symbol(symH.SELECCIONA,yychar,yyline,"CASE");}
":"         	  {return new Symbol(symH.DOSPUNTOS,yychar,yyline,":");}
"TRUE"         	  {return new Symbol(symH.TRUE,yychar,yyline,"TRUE");}
"FALSE"           {return new Symbol(symH.FALSE,yychar,yyline,"FALSE");}
"+"         	  {return new Symbol(symH.MAS,yychar,yyline,"+");}
"-"         	  {return new Symbol(symH.MENOS,yychar,yyline,"-");}
"*"         	  {return new Symbol(symH.POR,yychar,yyline,"*");}
"/"         	  {return new Symbol(symH.DIV,yychar,yyline,"/");}
">"         	  {return new Symbol(symH.MAYOR,yychar,yyline,">");}
"<"         	  {return new Symbol(symH.MENOR,yychar,yyline,"<");}
">="         	  {return new Symbol(symH.MAYORIGUAL,yychar,yyline,">=");}
"<="         	  {return new Symbol(symH.MENORIGUAL,yychar,yyline,"<=");}
"=="         	  {return new Symbol(symH.IGUALIGUAL,yychar,yyline,"==");}
"!="         	  {return new Symbol(symH.NOIGUAL,yychar,yyline,"!=");}
"||"         	  {return new Symbol(symH.OR,yychar,yyline,"||");}
"&&"         	  {return new Symbol(symH.AND,yychar,yyline,"&&");}
"="         	  {return new Symbol(symH.IGUAL,yychar,yyline,"=");}
"["         	  {return new Symbol(symH.COR1,yychar,yyline,"[");}
"]"         	  {return new Symbol(symH.COR2,yychar,yyline,"]");}
"{"         	  {return new Symbol(symH.LLA1,yychar,yyline,"{");}
"}"         	  {return new Symbol(symH.LLA2,yychar,yyline,"}");}
"("         	  {return new Symbol(symH.PAR1,yychar,yyline,"(");}
")"         	  {return new Symbol(symH.PAR2,yychar,yyline,")");}
","         	  {return new Symbol(symH.COMA,yychar,yyline,",");}
";"         	  {return new Symbol(symH.PUNTOYCOMA,yychar,yyline,";");}
//""         	  {return new Symbol(symH.,yychar,yyline,null);}
{Id}    		  {return new Symbol(symH.ID,yychar, yyline, new String(yytext()));}
{Entero}     	  {return new Symbol(symH.NUMERO,yychar, yyline, new String(yytext()));} 
{Decimal}     	  {return new Symbol(symH.NUMERO,yychar, yyline, new String(yytext()));} 
{Espacio}         { /* ignora el espacio */ } 
"//"            {yybegin(COMENTARIO1);}
"#*"            {yybegin(COMENTARIO2);}
[\"]            {cadena = ""; yybegin(CADENA);}
[\']            {cadena = ""; yybegin(CARACTER);}
}

<CADENA> {
[^\"]   {cadena+=yytext();}
[\"]    {String temporal=cadena; cadena=""; yybegin(YYINITIAL);return new Symbol(symH.CADENA,yychar,yyline, temporal);}
}

<CARACTER> {
[^\']	{cadena += yytext();}
[\']	{String temporal = cadena; cadena = ""; yybegin(YYINITIAL);return new  Symbol(symH.CARACTER,yychar,yyline,temporal);}
}

<COMENTARIO1> {
[^\n]+  {/**/} 
[\n]    {yybegin(YYINITIAL);}
}

<COMENTARIO2> {
[^*#]+  {/**/}
"*#"    {yybegin(YYINITIAL);}   
}

. {
        String errLex = "Error léxico, caracter irreconocible: '"+yytext()+"' en la línea: "+(yyline+1)+" y columna: "+yycolumn;
        System.err.println(errLex);
}