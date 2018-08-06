/*
 * Blancos.java
 *
 * Created on 2 de diciembre de 2007, 11:45 PM
 * Write by, alejandro.jimenez
 *
 */

package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

/**
 *
 * @author alejandro.jimenez
 */
public class Blancos implements Function {
  
  private static final int INDEX_CADENA= 0;
  
  /** Creates a new instance of Search */
  public Blancos() {
  }
  
  public String getName() {
    return "ALLTRIM";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1; 
  }
  
  private void checkParameters(Object [] parametersValue) {
    for(Object cadena: parametersValue) {
      if(!(cadena instanceof String))
        throw new IllegalArgumentException(cadena.toString().concat(" no es una cadena. !"));
    } // for
  }
  
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    String cadena= (String)parametersValue[INDEX_CADENA];
    return new String(cadena.trim());
  }
  
}
