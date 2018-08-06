package sia.db.sql;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import sia.db.dao.DaoFactory;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;
import sia.libs.formato.Variables;
import sia.libs.recurso.Adquisiciones;
import sia.libs.recurso.Contabilidad;
import sia.libs.recurso.Presupuesto;
import sia.libs.recurso.Tesoreria;
import sia.xml.Dml;

public class Sentencias {

  public static final int PROPIEDADES= 0;
  public static final int XML        = 1;  
  
  private int tipoConexion;
  private int formato;
  private List<String> resmd = null;
  
  public Sentencias(int tipoConexion) {
    this(tipoConexion, PROPIEDADES);
  }

  public Sentencias(int tipoConexion, int formato) {
    setTipoConexion(tipoConexion);
    setFormato(formato);
  }
  
  public Object consultar(String sentencia) {
    Object regresar      = null;
    Connection connection= null;
    Statement statement  = null;
    ResultSet resultSet  = null;
    try {
      connection= DaoFactory.getConnection(getTipoConexion());
      statement = connection.createStatement();
      resultSet = statement.executeQuery(sentencia);
      if (resultSet.next()) {
            regresar= resultSet.getObject(1);
        }
    }
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    finally {
      try {
        if (resultSet != null) {
              resultSet.close();
          }
        if (statement != null) {
              statement.close();
          }
        if (connection != null) {
              connection.close();
          }
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
    }
    return regresar;
  }
  
  //JLPN
    public Object consultar(Connection connection, String sentencia) {
      Object regresar      = null;
      //Connection connection= null;
      Statement statement  = null;
      ResultSet resultSet  = null;
      try {
        //connection= DaoFactory.getConnection(getTipoConexion());
        statement = connection.createStatement();
        resultSet = statement.executeQuery(sentencia);
        if (resultSet.next()) {
              regresar= resultSet.getObject(1);
          }
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
      finally {
        try {
          if (resultSet != null) {
                resultSet.close();
            }
          if (statement != null) {
                statement.close();
            }
     //     if (connection != null)
      //      connection.close();
      //    connection = null;
        }
        catch (Exception e) {
          Error.mensaje(e, "SIAFM");
        }
      }
      return regresar;
    }  
  protected int getOperation(String name) {
    int regresar= Dml.SELECT;
    if(name.equalsIgnoreCase("insert")) {
          regresar= Dml.INSERT;
      }
    else  
      if(name.equalsIgnoreCase("delete")) {
          regresar= Dml.DELETE;
      }
      else  
        if(name.equalsIgnoreCase("update")) {
          regresar= Dml.UPDATE;
      }
        else  
          if(name.equalsIgnoreCase("select")) {
          regresar= Dml.SELECT;
      }
    return regresar;
  }
  
  private String getCommandXml(String propiedad, String parametros, char token) {
    String regresar   = "";
    String[] atributos= propiedad.split("[.]");
    if(atributos.length== 3) {
      Dml dml;
      try  {
        dml= new Dml(getTipoConexion());
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
    } // if
    return regresar;
  }

  public String getCommand(String propiedad, Map parametros) {
    String regresar   = "";
    String[] atributos= propiedad.split("[.]");
    if(atributos.length== 3) {
      Dml dml= null;
      try  {
        dml= new Dml(getTipoConexion());
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
    } // if
    return regresar;
  }
  
  private String getCommandProperties(String propiedad, String parametros, char token) {
    Variables variables = new Variables(parametros, token);
    Formatos formatos = new Formatos(getPropiedad(propiedad), variables.getMap());
    String regresar = formatos.getSentencia();
    return regresar;    
  }
  
  public String getComando(String propiedad, String parametros, char token) {
    String regresar= null;
    switch(getFormato()) {
      case PROPIEDADES:
        regresar= getCommandProperties(propiedad, parametros, token);
        break;
      case XML:
        regresar= getCommandXml(propiedad, parametros, token);
        break;
    } // switch
    return regresar;
  }

  public String getComando(String propiedad, String parametros) {
    return getComando(propiedad, parametros, '|');
  }

  public String getComando(String propiedad, Map parametros) {
    return getCommand(propiedad, parametros);
  }
  
  public Object consultar(String propiedad, String parametros, char token) {
    return consultar(getComando(propiedad, parametros, token));    
  }
  
    public Object consultar(Connection con,String propiedad, String parametros, char token) {
      return consultar(con,getComando(propiedad, parametros, token));    
    }

  public Object consultar(String propiedad, String parametros) {
    return consultar(propiedad, parametros, '|');
  }
  
  public Object consultar(Connection con, String propiedad, String parametros) {
    return consultar(con, propiedad, parametros, '|');
  }


  public Object consultar(String propiedad, Map parametros) {
    return consultar(getCommand(propiedad, parametros));
  }
  
  public Object consultar(Connection con, String propiedad, Map parametros) {
    return consultar(con,getCommand(propiedad, parametros));
  }

  public int ejecutar(Connection connection, String sentencia) throws Exception{
    int regresar         = -1;
    Statement statement  = null;
    ResultSet resultSet  = null;
    try {
      statement = connection.createStatement();
      regresar  = statement.executeUpdate(sentencia);
      //System.out.println("sentencia ejecutar"+ sentencia);
    }
    catch (Exception e) {
      System.out.println("Error en metodo ejecutar: "+e.getMessage()+"Conexion "+connection+ "Sentencia: "+sentencia+e.getMessage());
      Error.mensaje(e, "SIAFM");
      throw e; //SE AGREGO THROW ESTO HACE QUE ESTO METODO SE AGREGUE THROWS Y A QUIEN LO LLAMA
    }
    finally {
      try {
        if (resultSet != null) {
              resultSet.close();
          }
        if (statement != null) {
              statement.close();
          }
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
    }
    return regresar;
  }
  
  public int ejecutar(Connection connection, String propiedad, String parametros, char token) throws Exception {
    return ejecutar(connection, getComando(propiedad, parametros, token));
  }

  public int ejecutar(Connection connection, String propiedad, String parametros) throws Exception{
    return ejecutar(connection, propiedad, parametros, '|');
  }
  
  public int ejecutar(Connection connection, String propiedad, Map parametros) throws Exception{
    return ejecutar(connection, getCommand(propiedad, parametros));
  }
  
  public int ejecutar(String sentencia) {
    int regresar         = -1;
    Connection connection= null;
    try {
      connection= DaoFactory.getConnection(getTipoConexion());
      connection.setAutoCommit(true);
      regresar= ejecutar(connection, sentencia);
    }
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    finally {
      try {
        if (connection != null) {
              connection.close();
          }
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
    }
    return regresar;
  }

  public int ejecutar(String propiedad, String parametros, char token) {
    return ejecutar(getComando(propiedad, parametros, token));
  }

  public int ejecutar(String propiedad, String parametros) {
    return ejecutar(propiedad, parametros, '|');
  }

  public int ejecutar(String propiedad, Map parametros) {
    return ejecutar(getCommand(propiedad, parametros));
  }

    public int ejecutarAgrupadas(Connection connection, ArrayList lista) throws Exception{
      int regresar         = -1;
      Statement statement  = null;
      String sentencia="";
      try {
        statement = connection.createStatement();
        for (int i = 0; i < lista.size(); i++) {
          sentencia=lista.get(i).toString();
          regresar  = statement.executeUpdate(sentencia);          
         // System.out.println("sentencia "+ sentencia);            
        }
      }
      catch (Exception e) {
        System.out.println("Error en metodo ejecutarAgrupadas: "+e.getMessage()+"Conexion "+connection+ "Sentencia: "+sentencia+e.getMessage());
        Error.mensaje(e, "SIAFM");
        throw e; //SE AGREGO THROW ESTO HACE QUE ESTO METODO SE AGREGUE THROWS Y A QUIEN LO LLAMA
      }
      finally {
        try {
          if (statement != null) {
                statement.close();
            }
        }
        catch (Exception e) {
          Error.mensaje(e, "SIAFM");
        }
      }
      return regresar;
    }

  private String getPropiedad(String propiedad) {
    String regresar= "";
    switch(getTipoConexion()) {
      case DaoFactory.CONEXION_CONTABILIDAD:
        regresar= Contabilidad.getPropiedad(propiedad);
        break;
      case DaoFactory.CONEXION_TESORERIA:
        regresar= Tesoreria.getPropiedad(propiedad);
        break;
      case DaoFactory.CONEXION_PRESUPUESTO:
        regresar= Presupuesto.getPropiedad(propiedad);
        break;
      case DaoFactory.CONEXION_ADQUISICIONES:
        regresar= Adquisiciones.getPropiedad(propiedad);
        break;
    }
    return regresar;
  }
  
  private void iniciarEncabezado(ResultSetMetaData rsmd) {
    try  {
      if(resmd == null) {
        resmd = new ArrayList();
        for (int i = 1; i <= rsmd.getColumnCount(); i++)  {
          resmd.add(rsmd.getColumnName(i));
        }
      }  
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    
    
    
  }
  
  private Vista datos(ResultSet rst) throws Exception {
    Vista regresar= new Vista();
    ResultSetMetaData rsmd= rst.getMetaData();
    iniciarEncabezado(rsmd);
    for (int x= 1; x<= rsmd.getColumnCount(); x++)  {
      regresar.put(rsmd.getColumnName(x).toUpperCase(), rst.getObject(x));
    } // for x
    return regresar;
  }
  
  public List <Vista> registros(String sentencia) {
    List <Vista> regresar= null;
    Connection connection= null;
    Statement statement  = null;
    ResultSet resultSet  = null;
    try {
      connection= DaoFactory.getConnection(getTipoConexion());
      statement = connection.createStatement();
      //System.out.println(sentencia);
      resultSet = statement.executeQuery(sentencia);
      
      if (resultSet.next()) {
        regresar= new ArrayList <Vista> ();
        regresar.add(datos(resultSet));
        while(resultSet.next()) {
          regresar.add(datos(resultSet));
        } // while
      } // if  
    }
    catch (Exception e) {
      Error.mensaje(e, "SIAFM");
    }
    finally {
      try {
        if (resultSet != null) {
              resultSet.close();
          }
        if (statement != null) {
              statement.close();
          }
        if (connection != null) {
              connection.close();
          }
      }
      catch (Exception e) {
        Error.mensaje(e, "SIAFM");
      }
    }
    return regresar;
  }
  
  ////////////////////////
   public List <Vista> registros(String sentencia, Connection connection) {
     List <Vista> regresar= null;
  ///   Connection connection= null;
     Statement statement  = null;
     ResultSet resultSet  = null;
     try {
   ///    connection= DaoFactory.getConnection(getTipoConexion());
       statement = connection.createStatement();
       //System.out.println(sentencia);
       resultSet = statement.executeQuery(sentencia);
       
       if (resultSet.next()) {
         regresar= new ArrayList <Vista> ();
         regresar.add(datos(resultSet));
         while(resultSet.next()) {
           regresar.add(datos(resultSet));
         } // while
       } // if  
     }
     catch (Exception e) {
       Error.mensaje(e, "SIAFM");
     }
     finally {
       try {
         if (resultSet != null) {
               resultSet.close();
           }
         if (statement != null) {
               statement.close();
           }
   //      if (connection != null)
     ///      connection.close();
      ///   connection = null;
       }
       catch (Exception e) {
         Error.mensaje(e, "SIAFM");
       }
     }
     return regresar;
   }
  ///////////////////////
  

  public List <Vista> registros(String propiedad, String parametros, char token) {
    return registros(getComando(propiedad, parametros, token));
  }

  public List <Vista> registros(String propiedad, String parametros) {
    return registros(propiedad, parametros, '|');
  }
  
  public List <Vista> registros(String propiedad, Map parametros) {
    return registros(getCommand(propiedad, parametros));
  }
  
  public void setTipoConexion(int tipoConexion) {
    if(DaoFactory.isConexionValida(tipoConexion)) {
          this.tipoConexion = tipoConexion;
      }
    else {
          throw new RuntimeException("La conexion no es valida, para este proyecto ["+ tipoConexion+ "]. !" );
      }
  }

  public int getTipoConexion() {
    return tipoConexion;
  }

  public void setFormato(int formato) {
    this.formato = formato;
  }

  public int getFormato() {
    return formato;
  }

  public void setResmd(List<String> resmd) {
    this.resmd = resmd;
  }

  public List<String> getResmd() {
    return resmd;
  }
}
