package sia.rf.contabilidad.registroContableEvento;

import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/**
 *
 * @author jesus.urraca y salvador.munoz
 */
public class Cadena {
  public Cadena() {
  }
  public static String construyeCadena(HashMap m){
    StringBuffer cadena = new StringBuffer();
    String st;
    if (m.isEmpty()) {
          return null;
      }
    Set sKey = m.keySet();
    Iterator i = sKey.iterator();
    while(i.hasNext()){
       st = (String) i.next();
       cadena.append(st).append("=").append(m.get(st)).append("|");
    }
    return cadena.toString();
  }  
  
  public static String sustituyeCadena(String sentencia, String parametros, String separadorParametros){
    String [] arregloParametros = parametros.split(separadorParametros);
    String [] sentenciaPartes = sentencia.split(separadorParametros);
    StringBuffer sustitucion = new StringBuffer(); 
    boolean bandera=false;
    for (int counter = 0; counter < sentenciaPartes.length; ++counter){
      if (bandera == false){
        sustitucion.append(sentenciaPartes[counter]);
        bandera=true;
      }    
      else{
        sustitucion.append(arregloParametros[Integer.parseInt(sentenciaPartes[counter])]);
        bandera=false;
      }
    }  
    return sustitucion.toString();
  }
}
