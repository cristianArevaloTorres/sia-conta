/*
 * Redondea.java
 *
 * Created on 2 de diciembre de 2007, 08:53 PM
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
public class Redondea implements Function {
  
  /** Creates a new instance of Redondea */
  public Redondea() {
  }
  
  public String getName() {
    return "ROUND";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    return parameterCount== 1; // At least one parameter
  }
  
  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    double value= 0;
    if (parametersValue[0] instanceof Double)
      value= ((Double)parametersValue[0]).doubleValue();
    else
      if (parametersValue[0] instanceof Long)
        value= ((Long)parametersValue[0]).longValue();
      else
        throw new IllegalArgumentException(String.valueOf(parametersValue[0]) + " no es un numero. !");
    if(value!= 0) {
/*
       int operador= value< 0? -1: 1;
       value= Math.abs(value)* 100.0;
       value= Math.rint(value+ 0.5001)/100;
       value*= operador;
 
 */
      // propusta de rolando la rola 06/01/2004
      int operador= value< 0? -1: 1;
      value= operador* (Math.floor(Math.abs(value)*100+ 0.5001)/100.0);
    } 
    else
      value= 0;
    return new Double(value);
  }
  
}
