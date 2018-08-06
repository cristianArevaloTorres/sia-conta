package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

import sia.libs.formato.Error;

public class Numerico implements Function{

  private static final int INDEX_CADENA    = 0;

  public Numerico() {
  }
  
  public String getName() {
    return "VAL";
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

  private Double convert(String value) {
    Double regresar= 0D;
    try  {
      regresar= Double.parseDouble(value);
    } 
    catch (Exception e)  {
      Error.mensaje(e, "SIAFM");
    } 
    return regresar;
  }
  
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    return convert((String)parametersValue[INDEX_CADENA]);
  }

}
