package sia.rf.contabilidad.registroContable.formas.servicios;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;
import sia.libs.formato.Formatos;
import sia.libs.parser.Expresion;
import sia.libs.recurso.Contabilidad;

public class VariableForma {
  
  private Map variables;
  
  public VariableForma(Map variables) {
    setVariables(variables);
  }

  private String toVariables() {
    StringBuffer sb= new StringBuffer();
    for (Object objeto: getVariables().keySet())  {
      sb.append(getVariables().get(objeto.toString()).toString());
      sb.append(",");
    } // for
    sb.delete(sb.length()- 1, sb.length());
    return sb.toString();
  }
  
  public String toString() {
    StringBuffer sb= new StringBuffer();
    sb.append("sia.rf.contabilidad.registroContable.formas.servicios.VariableForma[");
    sb.append(toVariables());
    sb.append("]");
    return sb.toString();
  }
  
  private String ejecutar(String criterio) {
    String regresar= "";
    Sentencias sentencias= new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
    List<Vista>secuencias= sentencias.registros(criterio);
    if(secuencias!= null) {
      Vista registro= (Vista)secuencias.get(0);
      for (Object nombre: registro.keySet())  {
        getVariables().put(nombre.toString().toLowerCase(), registro.get(nombre.toString()));
        if(regresar== null || regresar.length()== 0)
          regresar= registro.get(nombre.toString()).toString();
      } // for
      registro.clear();
      registro= null;
      secuencias.clear();
    } // if  
    secuencias= null;
    sentencias= null;
    return regresar;
  }

  private String evaluar(String criterio) {
    String regresar= null;
    Expresion expresion= new Expresion(criterio);
    regresar = expresion.evaluar().toString();
    if(regresar.indexOf("=")>= 0) {
      String nombre= regresar.substring(0, regresar.indexOf("="));
      regresar= regresar.substring(regresar.indexOf("=")+ 1);
      getVariables().put(nombre, regresar);
    } // if
    expresion= null;
    return regresar;
  }
  
  public String getCodigo() {
    String regresar      = "";
    String sentencia     = Contabilidad.getPropiedad("cuenta.variables.forma.proceso");
    Map parametros       = new HashMap();
    parametros.put("areas_formas_id", getVariables().get("areas_formas_id"));
    parametros.put("nombre", getVariables().get("nombre"));
    Formatos formatos    = new Formatos(sentencia, parametros);
    String consulta      = formatos.getSentencia();
    Sentencias sentencias= new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
    List<Vista>registros = sentencias.registros(consulta);
    if(registros!= null) {
      for (Vista registro: registros) {
        // si el valor es cero entonces es una expresion algebraica si no entonces es una sentencia
        formatos.setCodigos(registro.getField("SENTENCIA"));
        formatos.setVariables(variables);
        String criterio= formatos.getSentencia();
        if(registro.getField("TIPO").equalsIgnoreCase("0")) 
          regresar= evaluar(criterio);
        else 
          regresar= ejecutar(criterio);
      } // for 
      registros.clear();
    } // if
    registros = null;
    sentencias= null;
    formatos  = null;
    parametros.clear();
    parametros= null;
    return regresar;
  }

  public void setVariables(Map variables) {
    this.variables = variables;
  }

  public Map getVariables() {
    return variables;
  }
  
}
