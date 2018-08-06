package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Cadena implements Function{

  private static final int INDEX_NUMERO    = 0;

  public Cadena() {
  }
  
  public String getName() {
    return "STR";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1; 
  }

  private void checkParameters(Object[] parametersValue) {
    for (Object cadena: parametersValue) {
      if (!(cadena instanceof Double) && !(cadena instanceof Float) && !(cadena instanceof Integer) &&
          !(cadena instanceof Long) && !(cadena instanceof Short) && !(cadena instanceof Byte))
        throw new IllegalArgumentException(cadena.toString().concat(" no es un numero valido. !"));
    } // for
  }

  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    return String.valueOf(parametersValue[INDEX_NUMERO]);
  }

}
