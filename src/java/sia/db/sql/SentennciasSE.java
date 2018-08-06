package sia.db.sql;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import sia.libs.formato.Error;

import sia.xml.Dml;
import sia.xml.DmlSE;

public class SentennciasSE extends Sentencias {
  
  private Map parametros;
  
  public SentennciasSE(int tipoConexion) {
    this(tipoConexion, PROPIEDADES);
  }

  public SentennciasSE(int tipoConexion, int formato) {
    super(tipoConexion, formato);
  }
  
  public void addParamVal(String k, String cad, Object v)
  {
      if(cad != null && !cad.equals("") && v != null && !v.equals("") && !v.equals("null"))
          addParam(k, cad.replaceAll(":param", v.toString()));
      else
          addParam(k, "");
  }
  
  public void addParamVal(String k, String cad, Object ... v) {
    String param = null;
    if(v != null) {
      for (int i = 0; i < v.length; i++)  {
        if(cad!=null && !cad.equals("") && v[i] != null && !v[i].equals("") && !v[i].equals("null")) {
          param = v.length == 1?":param":":param".concat(String.valueOf(i));
          cad = cad.replaceAll(param,v[i].toString());
        } else {
          cad = "";
          break;
        }
      }
    } else {
      cad = "";
    }
    addParam(k,cad);
  }
  
  public void addParam(String k, Object v) {
    if(parametros == null)
      parametros = new HashMap();
    if(k != null && v != null)
      parametros.put(k,v);
  }
  
  private String getCommandXml(String propiedad, String parametros, char token) {
    String regresar   = "";
    String[] atributos= propiedad.split("[.]");
    if(atributos.length>= 3) {
      DmlSE dml= null;
      try  {
        dml= new DmlSE(getTipoConexion());
        switch(getOperation(atributos[1])) {
          case Dml.INSERT:
            regresar= dml.getInsert(atributos[0], atributos[2], parametros, token);
            break;
          case Dml.DELETE:
            regresar= dml.getDelete(atributos[0], atributos[2], parametros, token);
            break;
          case Dml.UPDATE:
            regresar= dml.getUpdate(atributos[0], atributos[2], parametros, token);
            break;
          case Dml.SELECT:
            regresar= dml.getSelect(atributos[0], atributos[2], parametros, token);
            break;
        } // switch
      } 
      catch (Exception e)  {
        Error.mensaje(e, "SIAFM");
      } 
      dml= null;
    } // if
    return regresar;
  }
  
  public String getCommand(String propiedad) {
    return getCommand(propiedad,parametros);
  }
  
  public List<Vista> registros(Connection con, String propiedad) {
    return registros(getCommand(propiedad, parametros), con);
  }
  
  public String getCommand(String propiedad, Map parametros) {
    String regresar   = "";
    String[] atributos= propiedad.split("[.]");
    if(atributos.length>= 3) {
      DmlSE dml= null;
      try  {
        if(atributos.length==3)
          dml= new DmlSE(getTipoConexion());
        if(atributos.length==4)
          dml= new DmlSE(getTipoConexion(),atributos[3]);
        switch(getOperation(atributos[1])) {
          case Dml.INSERT:
            regresar= dml.getInsert(atributos[0], atributos[2], parametros);
            break;
          case Dml.DELETE:
            regresar= dml.getDelete(atributos[0], atributos[2], parametros);
            break;
          case Dml.UPDATE:
            regresar= dml.getUpdate(atributos[0], atributos[2], parametros);
            break;
          case Dml.SELECT:
            regresar= dml.getSelect(atributos[0], atributos[2], parametros);
            break;
        } // switch
      } 
      catch (Exception e)  {
        Error.mensaje(e, "CILCI");
      } 
      dml= null;
    } // if
   // System.out.println("SeenntenciasSE. atributos[0]: "+atributos[0]+ "atributos[2]:"+atributos[2]+"Parametros: "+parametros+"\nregresar: " + regresar);
    return regresar;
  }
  
  public void liberaParametros() {
    Iterator it=parametros.keySet().iterator();
    List keys = new ArrayList();
    while(it.hasNext()) {
      keys.add(it.next());
    }
    for(Object key:keys) 
      parametros.remove(key);
    parametros = null;
  }
}
