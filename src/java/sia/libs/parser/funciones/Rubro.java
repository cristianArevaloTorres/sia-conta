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

public class Rubro implements Function{
  
  private static final int INDEX_NUMERO    = 0;
  
  public Rubro() {
  }
  
  public String getName() {
    return "RUBRO";
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
    return getRubro(parametersValue[INDEX_NUMERO]);
  }
  
  private String getPropiedad(String sentencia) {
    String regresar= "";
    ResourceBundle propiedades= ListResourceBundle.getBundle("contabilidad");
    regresar= propiedades.getString(sentencia);
    propiedades= null;
    return regresar== null? "": regresar;
  }

  
  private int getRubro(Object parametro){
    String sentencia     = getPropiedad("funciones.query.obtenerRubro");
    Map parametros       = new HashMap();
    parametros.put("partida", parametro);
    Formatos formatos    = new Formatos(sentencia, parametros);
    String consulta      = formatos.getSentencia();
    Sentencias sentencias= new Sentencias(DaoFactory.CONEXION_TESORERIA);
    int capitulo         = -1;
    List<Vista>registros = sentencias.registros(consulta);
    if(registros!= null) {
      Vista registro= (Vista)registros.get(0);
      capitulo=Integer.valueOf(registro.getField("RUBRO_ID"));
    }
    return capitulo;
  }
  
}
