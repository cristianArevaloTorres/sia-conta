package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrMensajes extends BeanBase {
		
  private String idMensaje;
	private String descripcion;
	private String fechaVigIni;
	private String fechaVigFin;
	private String idTipoMensaje;
	private String numEmpleado;
	private String idArea;
	private String urlSeguimiento;
	private String registro;
	private String descripcion1;
	private String descripcionCorta;
  
	public bcIjTrMensajes() {
		limpia();
	}
	
  public void setIdMensaje(String idMensaje) {
		this.idMensaje = idMensaje;
	}

	public String getIdMensaje() {
		return this.idMensaje;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
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

	public void setIdTipoMensaje(String idTipoMensaje) {
		this.idTipoMensaje = idTipoMensaje;
	}

	public String getIdTipoMensaje() {
		return this.idTipoMensaje;
	}

	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNumEmpleado() {
		return this.numEmpleado;
	}

	public void setIdArea(String idArea) {
		this.idArea = idArea;
	}

	public String getIdArea() {
		return this.idArea;
	}

	public void setUrlSeguimiento(String urlSeguimiento) {
		this.urlSeguimiento = urlSeguimiento;
	}

	public String getUrlSeguimiento() {
		return this.urlSeguimiento;
	}

	public void setRegistro(String registro) {
		this.registro = registro;
	}

	public String getRegistro() {
		return this.registro;
	}

	public void setDescripcion1(String descripcion1) {
		this.descripcion1 = descripcion1;
	}

	public String getDescripcion1() {
		return this.descripcion1;
	}

	public void setDescripcionCorta(String descripcionCorta) {
		this.descripcionCorta = descripcionCorta;
	}

	public String getDescripcionCorta() {
		return this.descripcionCorta;
	}
  
  private void limpia(){
		idMensaje = "";
		descripcion = "";
		fechaVigIni = "";
		fechaVigFin = "";
		idTipoMensaje = "";
		numEmpleado = "";
		idArea = "";
		urlSeguimiento = "";
		registro = "";
		descripcion1 = "";
		descripcionCorta = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select decode(max(id_mensaje),null,0,max(id_mensaje))+1 valoractual from SG_tr_mensajes");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        setIdMensaje(rsQuerySeq.getString("valoractual"));
      } //del while

    }
    catch(Exception e) {
        throw new Exception(SQL.toString(),e);
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
			SQL.append("  ID_MENSAJE, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  to_char(FECHA_VIG_INI,'dd/mm/yyyy hh24:mi') FECHA_VIG_INI, \n");
			SQL.append("  to_char(FECHA_VIG_FIN,'dd/mm/yyyy hh24:mi') FECHA_VIG_FIN, \n");
			SQL.append("  ID_TIPO_MENSAJE, \n");
			SQL.append("  NUM_EMPLEADO, \n");
			SQL.append("  ID_AREA, \n");
			SQL.append("  URL_SEGUIMIENTO, \n");
			SQL.append("  to_char(REGISTRO,'dd/mm/yyyy hh24:mi:ss') REGISTRO, \n");
			SQL.append("  DESCRIPCION_1, \n");
			SQL.append("  DESCRIPCION_CORTA \n");
			SQL.append("from \n"); 
			SQL.append("  SG_TR_MENSAJES\n");
			SQL.append("where \n");
			SQL.append("  ID_MENSAJE = " + idMensaje + " and ");
			SQL.append("  ID_MENSAJE = " + idMensaje + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idMensaje = rsQuery.getString("ID_MENSAJE")==null?"":rsQuery.getString("ID_MENSAJE");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              fechaVigIni = rsQuery.getString("FECHA_VIG_INI")==null?"":rsQuery.getString("FECHA_VIG_INI");
              fechaVigFin = rsQuery.getString("FECHA_VIG_FIN")==null?"":rsQuery.getString("FECHA_VIG_FIN");
              idTipoMensaje = rsQuery.getString("ID_TIPO_MENSAJE")==null?"":rsQuery.getString("ID_TIPO_MENSAJE");
              numEmpleado = rsQuery.getString("NUM_EMPLEADO")==null?"":rsQuery.getString("NUM_EMPLEADO");
              idArea = rsQuery.getString("ID_AREA")==null?"":rsQuery.getString("ID_AREA");
              urlSeguimiento = rsQuery.getString("URL_SEGUIMIENTO")==null?"":rsQuery.getString("URL_SEGUIMIENTO");
              registro = rsQuery.getString("REGISTRO")==null?"":rsQuery.getString("REGISTRO");
              descripcion1 = rsQuery.getClob("DESCRIPCION_1")==null?"":rsQuery.getClob("DESCRIPCION_1").getSubString(1,(int)rsQuery.getClob("DESCRIPCION_1").length());
              descripcionCorta = rsQuery.getString("DESCRIPCION_CORTA")==null?"":rsQuery.getString("DESCRIPCION_CORTA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcSGTrMensajes.select: " + SQL.toString());
      throw new Exception(SQL.toString(),e);
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
      //adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into SG_TR_MENSAJES ( ");
      SQL.append("ID_MENSAJE, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("FECHA_VIG_INI, ");
      SQL.append("FECHA_VIG_FIN, ");
      SQL.append("ID_TIPO_MENSAJE, ");
      SQL.append("NUM_EMPLEADO, ");
      SQL.append("ID_AREA, ");
      SQL.append("URL_SEGUIMIENTO, ");
      SQL.append("REGISTRO, ");
      SQL.append("DESCRIPCION_1, ");
      SQL.append("DESCRIPCION_CORTA ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idMensaje)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getTimeStamp(fechaVigIni)+", ");
      SQL.append(getTimeStamp(fechaVigFin)+", ");
      SQL.append(getCampo(idTipoMensaje)+", ");
      SQL.append(getCampo(numEmpleado)+", ");
      SQL.append(getCampo(idArea)+", ");
      SQL.append(getString(urlSeguimiento)+", ");
      SQL.append(getTimeStamp(registro)+", ");
      SQL.append(getString(descripcion1)+", ");
      SQL.append(getString(descripcionCorta)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcSGTrMensajes.insert: " + SQL);
      idMensaje = "-1";
      idMensaje = "-1";

      throw new Exception(SQL.toString(),e);
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
       //adecuaCampos();
       StringBuffer SQL = new StringBuffer();
       SQL.append("update SG_TR_MENSAJES set \n");
       SQL.append("  ID_MENSAJE = " + getCampo(idMensaje)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  FECHA_VIG_INI = " + getTimeStamp(fechaVigIni)+", \n");
       SQL.append("  FECHA_VIG_FIN = " + getTimeStamp(fechaVigFin)+", \n");
       SQL.append("  ID_TIPO_MENSAJE = " + getCampo(idTipoMensaje)+", \n");
       SQL.append("  NUM_EMPLEADO = " + getCampo(numEmpleado)+", \n");
       SQL.append("  ID_AREA = " + getCampo(idArea)+", \n");
       SQL.append("  URL_SEGUIMIENTO = " + getString(urlSeguimiento)+", \n");
       SQL.append("  REGISTRO = " + getTimeStamp(registro)+", \n");
       SQL.append("  DESCRIPCION_1 = " + getString(descripcion1)+", \n");
       SQL.append("  DESCRIPCION_CORTA = " + getString(descripcionCorta)+" \n");
       SQL.append("where \n");
       SQL.append("  ID_MENSAJE = " + idMensaje + "  and " );
       SQL.append("  ID_MENSAJE = " + idMensaje + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcSGTrMensajes.update: "+SQL);
          throw new Exception(SQL.toString(),e);
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
        SQL = "delete from SG_TR_MENSAJES where ID_MENSAJE = " + idMensaje + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcSGTrMensajes.delete: "+SQL);         
        throw new Exception(SQL.toString(),e);
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
		sb.append(getUrlSeguimiento());
		sb.append(",");
		sb.append(getNumEmpleado());
		sb.append(",");
		sb.append(getIdTipoMensaje());
		sb.append(",");
		sb.append(getDescripcion1());
		sb.append(",");
		sb.append(getFechaVigFin());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getIdArea());
		sb.append(",");
		sb.append(getIdMensaje());
		sb.append(",");
		sb.append(getDescripcionCorta());
		sb.append(",");
		sb.append(getFechaVigIni());
		sb.append(",");
		sb.append(getRegistro());
    return sb.toString();
  }
  
  public String getFechaInicio(){
    if(fechaVigIni!=null && !fechaVigIni.equals(""))
      return getFechaVigIni().split(" ")[0];
    else
      return "";
  }
  
  public String getFechaFin(){
    if(fechaVigFin!=null && !fechaVigFin.equals(""))
      return getFechaVigFin().split(" ")[0];
    else
      return "";
  }
  
  public String getHoraInicio(){
    if(fechaVigIni!=null && !fechaVigIni.equals(""))
      return getFechaVigIni().split(" ")[1];
    else
      return "";
  }
  
  public String getHoraFin(){
    if(fechaVigFin!=null && !fechaVigFin.equals(""))
      return getFechaVigFin().split(" ")[1];
    else
      return "";
  }
  
}
