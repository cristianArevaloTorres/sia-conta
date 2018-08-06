package sia.libs.parser.funciones;

import com.eteks.parser.Function;
import com.eteks.parser.Interpreter;

import java.util.HashMap;
import java.util.List;
import java.util.ListResourceBundle;
import java.util.Map;
import java.util.ResourceBundle;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.libs.formato.Formatos;

public class Concepto implements Function{
  
  private static final int INDEX_NUMERO    = 0;
  
  public Concepto() {
  }
  
  public String getName() {
    return "CONCEPTO";
  }
  
  public boolean isValidParameterCount(int parameterCount) {
    // At least one parameter
    return parameterCount== 1; 
  }

  private void checkParameters(Object[] parametersValue) {
    for (Object cadena: parametersValue) {
      if (!(cadena instanceof Integer) && !(cadena instanceof Long) && !(cadena instanceof Short) && !(cadena instanceof Byte))
        throw new IllegalArgumentException(cadena.toString().concat(" no es un numero valido. !"));
    } // for
  }

  public Object computeFunction(Interpreter interpreter, Object [] parametersValue) {
    checkParameters(parametersValue);
    return getConcepto(parametersValue[INDEX_NUMERO]);
  }
  

  
  private int getConcepto(Object parametro){
    int concepto= Integer.valueOf(parametro.toString())/100;
    return concepto*100;
  }
  
}
