/*
 * Search.java
 *
 * Created on 2 de diciembre de 2007, 08:42 PM
 *
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
public class Search implements Function {
  
  private static final int INDEX_SUB_CADENA= 0;
  private static final int INDEX_CADENA    = 1;
  
  /** Creates a new instance of Search */
  public Search() {
  }
  
  public String getName() {
    return "SEARCH";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 2; 
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
    String subCadena= (String)parametersValue[INDEX_SUB_CADENA];
    return new Boolean(cadena.indexOf(subCadena)>= 0);
  }
  
}
