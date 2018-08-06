package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

public class Vacio implements Function {

  private static final int INDEX_CADENA = 0;

  public Vacio() {
  }

  public String getName() {
    return "EMPTY";
  }

  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount == 1;
  }

  private void checkParameters(Object[] parametersValue) {
    for (Object cadena: parametersValue) {
      if (!(cadena instanceof String))
        throw new IllegalArgumentException(cadena.toString().concat(" no es una cadena. !"));
    } // for
  }

  public Object computeFunction(Interpreter interpreter, Object[] parametersValue) {
    checkParameters(parametersValue);
    String cadena = (String)parametersValue[INDEX_CADENA];
    return cadena.trim().length()== 0;
  }


}
