/*
 * Clase: Cadena.java
 *
 * Creado: 21 de mayo de 2007, 12:16 AM
 *
 * Write by: alejandro.jimenez
 */

package sia.libs.formato;

import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Cadena {

 

  private Cadena() {
  }
  
  public static String eliminaCaracter(String msg, char caracter) {
    StringBuffer resultado= new StringBuffer(msg);
    int x= 0; 
    while(x< resultado.length()) {
      if (resultado.charAt(x)== caracter) 
        resultado.deleteCharAt(x);
      else   
        x++;
    } // while
    return resultado.toString();
  }  
  
  public static String eliminar(String campos, char caracter) {
    StringBuffer sb= new StringBuffer();
    for(int x= 0; x< campos.length(); x++) {
      if (campos.charAt(x)!= caracter) {
        sb.append(campos.charAt(x));
			};
    };
  	return sb.toString();
  }

  public static String nombrePersona(String nombre) {
    StringBuffer sb= new StringBuffer();
    boolean letraCapital= true;
  	for(int x= 0; x< nombre.length(); x++) {
  	  sb.append(letraCapital? nombre.substring(x, x+ 1).toUpperCase(): nombre.substring(x, x+ 1).toLowerCase());
    	letraCapital= nombre.charAt(x)== ' ';
    }; // for
   return sb.toString();
  }; // nombrePersona

  public static String letraCapital(String nombre) {
  	if(nombre.length()!=0)
  	  return nombre.substring(0,1).toUpperCase()+ nombre.substring(1).toLowerCase();
	  else
	    return nombre;
  };

  public static String soloLetras(String msg)  {
    StringBuffer sb= new StringBuffer();
    for(int x= 0; x< msg.length(); x++) {
	  if("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_".indexOf(msg.charAt(x))>=0)
  	   sb.append(msg.charAt(x));
	  }
	  return sb.toString();
  }
  
  public static String comillas(String cadena) {
    return "'".concat(cadena== null? "": cadena).concat("'");
  }

  public static int contar(String cadena, char caracter) {
    int regresar= 0;
    for(int x= 0; x< cadena.length(); x++) {
      if(cadena.charAt(x)== caracter)
        regresar++;
    } // for x
    return regresar;
  }

  public static String rellenar(String cadena, int cuantos, char caracter, boolean alInicio) {
    StringBuffer regresar= new StringBuffer(cadena);
    for (int x= cadena.length(); x<cuantos; x++)  {
      if(alInicio)
        regresar.insert(0, caracter);
      else
        regresar.append(caracter);
    }
    return regresar.toString();
  } // rellenar
  /**
     * Remplaza todos los no alfanumericos por un guion de piso "_"
     * @param cadena cadena a estandarizar
     * @return 
     */
  public static String formatearNombreArchivo(String cadena){
      Pattern patron = Pattern.compile("\\W");
      Matcher match = patron.matcher(cadena);
      if (match.find()) {
        cadena = match.replaceAll("_");
      }
      return cadena;
  }

  public static String getCadenaConAcute(String cadena) {
    String[] letrasAcentuadas =
      new String[] { "�", "�", "�", "�", "�", "�", "�", "�", "�", "�" };
    String primero = "";
    int k = cadena.length();
    int x = 0;
    int i = 0;
    while (x < cadena.length()) {
      for (i=x; i <cadena.length(); i++) {
        for (int j = 0; j < 10; j++) {
          k=cadena.substring(i,i+1).indexOf((letrasAcentuadas[j]));
          if (k >= 0)
            break;
        }
        if (k >= 0) {
          break;
        }
        primero+=cadena.substring(i, i+1);
      }
      if (k >= 0) {
        primero+=cadena.substring(i, k + i);
        primero+=getRespectivoAcute(cadena.substring(k + i, k + i + 1).toCharArray()[0]);
      }
//      else
//        if (k==-1)
//          primero+= cadena.substring(x);
//        else
//          primero+=cadena.substring(i, k + i);
      x=k + i + 1;
      k=-1;
    }
    return primero;
  }

  public static String getRespectivoAcute(char letra) {
    switch (letra) {
    case 'á':
      return "&aacute;";
    case 'é':
      return "&eacute;";
    case 'í':
      return "&iacute;";
    case 'ó':
      return "&oacute;";
    case 'ú':
      return "&uacute;";
    case 'Á':
      return "&Aacute;";
    case 'É':
      return "&Eacute;";
    case 'Í':
      return "&Iacute;";
    case 'Ó':
      return "&Oacute;";
    case 'Ú':
      return "&Uacute;";
    default:
      return null;
    }
  }
 
 /* 
  * recibe una cadena sim importar si viene en Mayuscula, minusculas o combinado
  * regresa la cadena en formato oracion 
  * ejemplo: 
  *    RECIBE:  "diRECcion gENERAL de programaci�n, Organizacion y PRESUPUESTO"
  *    REGRESA: "Direccion General de Programaci�n, Organizacion y Presupuesto"
  * */
 
  public static String getFormatoOracion(String cadena){
    String regresa = "";
    String[] articulos  = new String[]{"DE","E","Y","LA","O","PARA","ES","UNA","DEL"};
    boolean existe = false;
    String palabra = null;
    StringTokenizer tokens=new StringTokenizer(cadena.toUpperCase());
    while(tokens.hasMoreTokens()){
      palabra = tokens.nextToken();
      for (int i = 0; i < articulos.length; i++)  {
        if (palabra.equals(articulos[i])){
          regresa = regresa + palabra.toLowerCase() + " ";
          existe = true;
          i= articulos.length +1;
        }
      }
      if (!existe){
        if(palabra.contains("."))
          regresa = regresa + palabra + " ";
        else
          regresa = regresa + palabra.substring(0,1).toUpperCase() + palabra.substring(1).toLowerCase() + " ";
      }
      existe = false;
    }
    return regresa;
  }
  
   /* 
  * recibe una cadena con los rangos permitidos
  * regresa todos los rangos permitidos
  * ejemplo: 
  *    RECIBE:  0010,0020,0030.0040,5000
  *    REGRESA: 0010|0020|0030|0031|0032|0033|0034|0035|0036|0037|0038|0039|0040|5000
  * */
   public static String getRango(String cadena){
     String regresa= "";
     String[] valores = cadena.split(",");
     int valorFinal = -1;
     int valorInicial = -1;
     int suma = -1;
     for(int i=0; i<valores.length; i++){
       if(valores[i].indexOf(".")!=-1){
         //regresa = valores[i].replace(".", ",");
         String[] temporal = valores[i].replace(".", ",").split(",");
         regresa = regresa.concat(temporal[0]).concat("|");
         valorFinal = Integer.parseInt(temporal[1]);
         valorInicial = Integer.parseInt(temporal[0]);
         //Calcular todo el rango
         do{
           suma = valorInicial+1;
           regresa = regresa.concat(rellenar(String.valueOf(suma),4,'0',true)).concat("|");
           valorInicial = suma;
         } while(suma < valorFinal);
       }else{
         regresa = regresa.concat(valores[i]).concat("|");
       }
     }
     return regresa;
   }
 
  public static void main (String args[]) {
    //System.out.println(getCadenaConAcute("�Agre�gar�"));
    System.out.println(getRango("0010,0020,0030.0040,5000"));
}

}