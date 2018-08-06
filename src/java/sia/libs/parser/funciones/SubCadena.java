/*
 * SubCadena.java
 *
 * Created on 09 de diciembre de 2007, 02:27 PM
 * Write by, alejandro.jimenez
 *
 */

package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;
import sia.libs.formato.Error;

public class SubCadena implements Function {

  private static final int INDEX_CADENA = 0;
  private static final int INDEX_INICIO = 1;
  private static final int INDEX_TERMINO= 2;
  
  /** Creates a new instance of Search */
  public SubCadena() {
  }
  
  public String getName() {
    return "SUBSTRING";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 3; 
  }
  
  private void checkParameters(Object [] parametersValue) {
    if(!(parametersValue[INDEX_CADENA] instanceof String))
      throw new IllegalArgumentException(parametersValue[INDEX_CADENA].toString().concat(" no es una cadena. !"));
    if(!(parametersValue[INDEX_INICIO] instanceof Long))
      throw new IllegalArgumentException(parametersValue[INDEX_INICIO].toString().concat(" no es una numero. !"));
    if(!(parametersValue[INDEX_TERMINO] instanceof Long))
      throw new IllegalArgumentException(parametersValue[INDEX_TERMINO].toString().concat(" no es una numero. !"));
  }
  
  private int getInteger(Object value) {
    int regresar= 0;
    try  {
      regresar= Integer.parseInt(value.toString());
    } 
    catch (Exception e)  {
      Error.mensaje(e, "SIAFM");
    } 
    return regresar;   
  }
  
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    String cadena= (String)parametersValue[INDEX_CADENA];
    int menor    = getInteger(parametersValue[INDEX_INICIO]);
    int mayor    = getInteger(parametersValue[INDEX_TERMINO]);
    int inicio   = Math.min(menor, mayor);
    inicio       = inicio<= 0? 0: inicio- 1;
    int termino  = Math.max(menor, mayor);
    termino      = termino>= cadena.length()? cadena.length(): termino;
    return new String(cadena.substring(inicio, termino));
  }
  
}
