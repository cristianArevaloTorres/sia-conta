package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Igual implements Function {
  
  private static final int INDEX_CADENA_1= 0;
  private static final int INDEX_CADENA_2= 1;
  
  /** Creates a new instance of Search */
  public Igual() {
  }
  
  public String getName() {
    return "EQUALS";
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
    String cadena= (String)parametersValue[INDEX_CADENA_1];
    String igual = (String)parametersValue[INDEX_CADENA_2];
    return cadena.equalsIgnoreCase(igual);
  }
  
}
