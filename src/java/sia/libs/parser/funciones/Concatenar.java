/*
 * Concatenar.java
 *
 * Created on 2 de diciembre de 2007, 11:26 PM
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
public class Concatenar implements Function {
  
  private static final int INDEX_CADENA= 0;
  
  /** Creates a new instance of Search */
  public Concatenar() {
  }
  
  public String getName() {
    return "CONCAT";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount> 0; 
  }
  
  private void checkParameters(Object [] parametersValue) {
    for(Object cadena: parametersValue) {
      if(!(cadena instanceof String))
        throw new IllegalArgumentException(cadena.toString().concat(" no es una cadena. !"));
    } // for
  }
  
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    StringBuffer sb= new StringBuffer();
    for(Object cadena: parametersValue) {
      sb.append(cadena.toString());
    } // for
    return new String(sb.toString());
  }
  
}
