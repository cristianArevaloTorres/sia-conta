package sia.configuracion;

import java.sql.Connection;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import java.util.Map;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentennciasSE;
import sia.db.sql.Vista;

public class BeanBase {

  private Map parametros;
  private int tipoConexion;

  public BeanBase() {
  }
  
  protected List<Vista> consultar(int conexion, String propiedad) {
    SentennciasSE sentencia = null;
    List<Vista> registros = null;
    try  {
      sentencia = new SentennciasSE(conexion);
      registros = sentencia.registros(propiedad,parametros);
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
      sentencia = null;
    }
    return registros;
  }
  
  protected List<Vista> consultar(String consulta, int conexion) {
    SentennciasSE sentencia = null;
    List<Vista> registros = null;
    try  {
      sentencia = new SentennciasSE(conexion);
      registros = sentencia.registros(consulta);
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
      sentencia = null;
    }
    return registros;
  }
  
  protected int ejecuta(Connection con, String propiedad, int tipoConexion) {
    SentennciasSE sentencia = null;
    int regresa = 0;
    try  {
      sentencia = new SentennciasSE(tipoConexion);
      regresa = sentencia.ejecutar(con,propiedad,parametros);
    } catch (Exception ex)  {
      System.err.println("Ocurrio un error al ejecutar la sentencia");
    } finally  {
      sentencia = null;
    }
    return regresa;
  }
  
  protected int ejecuta(Connection con, int tipoConexion, String query) throws Exception {
    SentennciasSE sentencia = null;
    int regresa = 0;
    try  {
      sentencia = new SentennciasSE(tipoConexion);
      regresa = sentencia.ejecutar(con,query);
    } catch (Exception ex)  {
      System.err.println("Ocurrio un error al ejecutar la sentencia");
      throw (ex);
    } finally  {
      sentencia = null;
    }
    return regresa;
  }
  
  protected void addParametro(String key, Object valor) {
    if(parametros == null)
      parametros = new HashMap();
    parametros.put(key,valor);
  }
  
  public void addParamVal(String k, String cad, Object ... v) {
      String param = null;
      if(v != null) {
        for (int i = 0; i < v.length; i++)  {
          if(cad!=null && !cad.equals("") && v[i] != null && !v[i].equals("")) {
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
      addParametro(k,cad);
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
  
  public void select(int con){
    Connection conn = null;
    try  {
      conn = DaoFactory.getConnection(con);
      select(conn);
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
      DaoFactory.closeConnection(conn);
    }
  }
  
  public int select(Connection conn) throws Exception {
    return 0;
  }
  
  protected double red(double monto, int dec) {
    return Math.round(monto*Math.pow(10,dec))/Math.pow(10,dec);
  }
  
  protected double red(String monto, int dec) {
    return Math.round(Double.parseDouble(monto)*Math.pow(10,dec))/Math.pow(10,dec);
  }
  
  protected String quitarComas(String valor) {
    return valor.replaceAll(",","");
  }
  
  private String formatearCampo(String campo) {
    campo = campo.replace("'","''");
    return campo;
  }
  
  protected String getString(String campo) {
    return campo==null || campo.equals("")?"null":"'"+formatearCampo(campo)+"'";
  }
  
  protected String getDate(String campo) {
    return campo==null || campo.equals("")?"null":"to_date('"+campo+"','dd/mm/yyyy')";
  }
  
  protected String getTimeStamp(String campo) {
    return campo==null || campo.equals("")?"null":"to_date('"+campo+"','dd/mm/yyyy hh24:mi:ss')";
  }
  
  protected String getCampo(String campo) {
    return campo==null || campo.equals("")?"null":campo;
  }

}
