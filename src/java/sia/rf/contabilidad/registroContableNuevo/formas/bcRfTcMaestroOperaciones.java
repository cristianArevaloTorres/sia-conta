package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;

import sia.configuracion.BeanBase;
import sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcDetalleOperacionesPojo;

import sia.db.dao.DaoFactory;
import sia.db.sql.Vista;

import sia.libs.formato.Fecha;

public class bcRfTcMaestroOperaciones extends BeanBase {
		
  private String maestroOperacionId;
	private String unidadEjecutora;
	private String ambito;
	private String pais;
	private String entidad;
	private String consecutivo;
	private String descripcion;
	private String aplicacion;
	private String registro;
	private String fechaVigIni;
	private String fechaVigFin;
	private String idCatalogoCuenta;
  
	public bcRfTcMaestroOperaciones() {
		limpia();
	}
	
  public void setMaestroOperacionId(String maestroOperacionId) {
		this.maestroOperacionId = maestroOperacionId;
	}

	public String getMaestroOperacionId() {
		return this.maestroOperacionId;
	}

	public void setUnidadEjecutora(String unidadEjecutora) {
		this.unidadEjecutora = unidadEjecutora;
	}

	public String getUnidadEjecutora() {
		return this.unidadEjecutora;
	}

	public void setAmbito(String ambito) {
		this.ambito = ambito;
	}

	public String getAmbito() {
		return this.ambito;
	}

	public void setPais(String pais) {
		this.pais = pais;
	}

	public String getPais() {
		return this.pais;
	}

	public void setEntidad(String entidad) {
		this.entidad = entidad;
	}

	public String getEntidad() {
		return this.entidad;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setAplicacion(String aplicacion) {
		this.aplicacion = aplicacion;
	}

	public String getAplicacion() {
		return this.aplicacion;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getRegistro() {
		return this.registro;
	}

	public void setFechaVigIni(String fechaVigIni) {
		this.fechaVigIni = fechaVigIni;
	}

	public String getFechaVigIni() {
		return this.fechaVigIni;
	}

	public void setFechaVigFin(String fechaVigFin) {
		this.fechaVigFin = fechaVigFin;
	}

	public String getFechaVigFin() {
		return this.fechaVigFin;
	}

	public void setIdCatalogoCuenta(String idCatalogoCuenta) {
		this.idCatalogoCuenta = idCatalogoCuenta;
	}

	public String getIdCatalogoCuenta() {
		return this.idCatalogoCuenta;
	}
  
  private void limpia(){
		maestroOperacionId = "";
		unidadEjecutora = "";
		ambito = "";
		pais = "";
		entidad = "";
		consecutivo = "";
		descripcion = "";
		aplicacion = "";
		registro = "";
		fechaVigIni = "";
		fechaVigFin = "";
		idCatalogoCuenta = "";

  }
  
  private void adecuaCampos() {
		maestroOperacionId = ((maestroOperacionId==null)||(maestroOperacionId.equals("")))? "null" : maestroOperacionId;
		unidadEjecutora = ((unidadEjecutora==null)||(unidadEjecutora.equals("")))? "null" : "'" +unidadEjecutora+ "'";
		ambito = ((ambito==null)||(ambito.equals("")))? "null" : "'" +ambito+ "'";
		pais = ((pais==null)||(pais.equals("")))? "null" : "'" +pais+ "'";
		entidad = ((entidad==null)||(entidad.equals("")))? "null" : entidad;
		consecutivo = ((consecutivo==null)||(consecutivo.equals("")))? "null" : "'" +consecutivo+ "'";
		descripcion = ((descripcion==null)||(descripcion.equals("")))? "null" : "'" +descripcion+ "'";
		aplicacion = ((aplicacion==null)||(aplicacion.equals("")))? "null" : "'" +aplicacion+ "'";
		registro = ((registro==null)||(registro.equals("")))? "null" : "to_date('" + registro + "','dd/mm/yyyy')";
		fechaVigIni = ((fechaVigIni==null)||(fechaVigIni.equals("")))? "null" : "to_date('" + fechaVigIni + "','dd/mm/yyyy')";
		fechaVigFin = ((fechaVigFin==null)||(fechaVigFin.equals("")))? "null" : "to_date('" + fechaVigFin + "','dd/mm/yyyy')";
		idCatalogoCuenta = ((idCatalogoCuenta==null)||(idCatalogoCuenta.equals("")))? "null" : idCatalogoCuenta;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_MAESTRO_OPERACIONES.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        maestroOperacionId = rsQuerySeq.getString("valoractual");
      } //del while
      if(maestroOperacionId == null)
        maestroOperacionId = "1";

    }
        catch(Exception e) {
      throw e;
    }
    finally {
      SQL.setLength(0);
      if(rsQuerySeq != null)
        rsQuerySeq.close();
      rsQuerySeq = null;
      if(stQuery != null)
        stQuery.close();
      stQuery = null;
      
    }
  }
  
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  MAESTRO_OPERACION_ID, \n");
			SQL.append("  UNIDAD_EJECUTORA, \n");
			SQL.append("  AMBITO, \n");
			SQL.append("  PAIS, \n");
			SQL.append("  ENTIDAD, \n");
			SQL.append("  CONSECUTIVO, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  APLICACION, \n");
			SQL.append("  to_char(REGISTRO,'dd/mm/yyyy') REGISTRO, \n");
			SQL.append("  to_char(FECHA_VIG_INI,'dd/mm/yyyy') FECHA_VIG_INI, \n");
			SQL.append("  to_char(FECHA_VIG_FIN,'dd/mm/yyyy') FECHA_VIG_FIN, \n");
			SQL.append("  ID_CATALOGO_CUENTA \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TC_MAESTRO_OPERACIONES\n");
			SQL.append("where \n");
			SQL.append("  MAESTRO_OPERACION_ID = " + maestroOperacionId + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              maestroOperacionId = rsQuery.getString("MAESTRO_OPERACION_ID")==null?"":rsQuery.getString("MAESTRO_OPERACION_ID");
              unidadEjecutora = rsQuery.getString("UNIDAD_EJECUTORA")==null?"":rsQuery.getString("UNIDAD_EJECUTORA");
              ambito = rsQuery.getString("AMBITO")==null?"":rsQuery.getString("AMBITO");
              pais = rsQuery.getString("PAIS")==null?"":rsQuery.getString("PAIS");
              entidad = rsQuery.getString("ENTIDAD")==null?"":rsQuery.getString("ENTIDAD");
              consecutivo = rsQuery.getString("CONSECUTIVO")==null?"":rsQuery.getString("CONSECUTIVO");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              aplicacion = rsQuery.getString("APLICACION")==null?"":rsQuery.getString("APLICACION");
              registro = rsQuery.getString("REGISTRO")==null?"":rsQuery.getString("REGISTRO");
              fechaVigIni = rsQuery.getString("FECHA_VIG_INI")==null?"":rsQuery.getString("FECHA_VIG_INI");
              fechaVigFin = rsQuery.getString("FECHA_VIG_FIN")==null?"":rsQuery.getString("FECHA_VIG_FIN");
              idCatalogoCuenta = rsQuery.getString("ID_CATALOGO_CUENTA")==null?"":rsQuery.getString("ID_CATALOGO_CUENTA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcMaestroOperaciones.select: " + SQL.toString());
      throw e;
    }
    finally {
      if (rsQuery != null) 
        rsQuery.close(); 
      rsQuery=null;
      if (stQuery != null)
        stQuery.close();
      stQuery = null;
      SQL.setLength(0);
      SQL = null;
    }         
    return resultado;
  }
  
  public void insert(Connection con) throws  Exception {
    ResultSet rsQuerySeq= null;
    Statement stQuery=null;
    Statement stQuery2=null;  
    StringBuffer SQL = null;
    try {
      getNext(con);
      adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into RF_TC_MAESTRO_OPERACIONES ( ");
      SQL.append("MAESTRO_OPERACION_ID, ");
      SQL.append("UNIDAD_EJECUTORA, ");
      SQL.append("AMBITO, ");
      SQL.append("PAIS, ");
      SQL.append("ENTIDAD, ");
      SQL.append("CONSECUTIVO, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("APLICACION, ");
      SQL.append("REGISTRO, ");
      SQL.append("FECHA_VIG_INI, ");
      SQL.append("FECHA_VIG_FIN, ");
      SQL.append("ID_CATALOGO_CUENTA ");
      SQL.append("\n) values (");
      SQL.append(maestroOperacionId + ", ");
      SQL.append(unidadEjecutora + ", ");
      SQL.append(ambito + ", ");
      SQL.append(pais + ", ");
      SQL.append(entidad + ", ");
      SQL.append("lpad("+consecutivo + ",2,0), ");
      SQL.append(descripcion + ", ");
      SQL.append(aplicacion + ", ");
      SQL.append(registro + ", ");
      SQL.append(fechaVigIni + ", ");
      SQL.append(fechaVigFin + ", ");
      SQL.append(idCatalogoCuenta + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcMaestroOperaciones.insert: " + SQL);
      maestroOperacionId = "-1";

      throw e;
    }
    finally {
      if (rsQuerySeq != null) {
        rsQuerySeq.close();
        rsQuerySeq=null;
      }
      if (stQuery != null) {
        stQuery.close();
        stQuery=null;
      }
      if (stQuery2 != null) {
        stQuery2.close();
        stQuery2=null;
      }
      if (SQL != null) {
        SQL.setLength(0);
        SQL = null;
      }
    }         
    //return (Integer.parseInt(idMetaIndicador));
  }
  
  public void update(Connection con) throws  Exception {
       adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update RF_TC_MAESTRO_OPERACIONES set \n");
       SQL.append("  MAESTRO_OPERACION_ID = " + maestroOperacionId + " , \n");
       SQL.append("  UNIDAD_EJECUTORA = " + unidadEjecutora + " , \n");
       SQL.append("  AMBITO = " + ambito + " , \n");
       SQL.append("  PAIS = " + pais + " , \n");
       SQL.append("  ENTIDAD = " + entidad + " , \n");
       SQL.append("  CONSECUTIVO = lpad(" + consecutivo + ",2,0) , \n");
       SQL.append("  DESCRIPCION = " + descripcion + " , \n");
       SQL.append("  APLICACION = " + aplicacion + " , \n");
       SQL.append("  REGISTRO = " + registro + " , \n");
       SQL.append("  FECHA_VIG_INI = " + fechaVigIni + " , \n");
       SQL.append("  FECHA_VIG_FIN = " + fechaVigFin + " , \n");
       SQL.append("  ID_CATALOGO_CUENTA = " + idCatalogoCuenta + "  \n");
       SQL.append("where \n");
       SQL.append("  MAESTRO_OPERACION_ID = " + maestroOperacionId + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcMaestroOperaciones.update: "+SQL);
          throw e;
       }
       finally {
          if (stQuery != null) {
             stQuery.close();
             stQuery=null;
          }           
       }
  }
  
  public void delete(Connection con) throws  Exception {
      int rs=-1;
      Statement stQuery=null;
      String SQL = "";
      try {
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = "delete from RF_TC_MAESTRO_OPERACIONES where MAESTRO_OPERACION_ID = " + maestroOperacionId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcMaestroOperaciones.delete: "+SQL);         
        throw e;
      }
      finally {                  
        if (stQuery != null) {
          stQuery.close();
          stQuery=null;
        }                         
      }
  }
  
  public String toString() { 
    StringBuffer sb = new StringBuffer();
		sb.append(getMaestroOperacionId());
		sb.append(",");
		sb.append(getPais());
		sb.append(",");
		sb.append(getAplicacion());
		sb.append(",");
		sb.append(getAmbito());
		sb.append(",");
		sb.append(getIdCatalogoCuenta());
		sb.append(",");
		sb.append(getFechaVigFin());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getConsecutivo());
		sb.append(",");
		sb.append(getEntidad());
		sb.append(",");
		sb.append(getUnidadEjecutora());
		sb.append(",");
		sb.append(getFechaVigIni());
		sb.append(",");
		sb.append(getRegistro());
    return sb.toString();
  }
  
  public void borraEnCascada(Connection con) throws Exception {
    if(getMaestroOperacionId() != null && !getMaestroOperacionId().equals("")) {
      addParametro("maestroOper",getMaestroOperacionId());
      ejecuta(con,"formasContables.delete.detalleDeMaestroOper",DaoFactory.CONEXION_CONTABILIDAD);
      delete(con);
      liberaParametros();
    }
  }
  
  public List<bcRfTcDetalleOperacionesPojo> getDetalleOperaciones() {
    List<bcRfTcDetalleOperacionesPojo> regresa = null;
    try  {
      //List<Vista>consultar(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.detalleFormulario");
      
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    return regresa;
    
  }
  
  public static void main(String[] args) {
    String var = "12";
    System.out.println(String.format("%04s",var));
  }
  
  public void validaUniqueOperacion(String forma, String unidad, String entidad, String ambito, String idCatalogoCuenta, String fecha) throws Exception {
    addParametro("operacion",forma.toUpperCase());
    addParametro("unidad",unidad);
    addParametro("entidad",entidad);
    addParametro("ambito",ambito);
    addParametro("idCatalogo",idCatalogoCuenta);
    addParametro("fecha",fecha.substring(6,10));
    List<Vista> registros =  consultar(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.validarUniqueOperacion");
    liberaParametros();
    if(registros!= null && registros.size() > 0)
      throw new Exception("Ya existe otro registro con el numero de operacion asignado");
  }
  
}
