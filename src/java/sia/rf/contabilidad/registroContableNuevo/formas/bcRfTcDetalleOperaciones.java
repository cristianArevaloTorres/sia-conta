package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTcDetalleOperaciones extends BeanBase {
		
  private String maestroOperacionId;
	private String cuentaContableId;
	private String operacionContableId;
	private String detalleOperacionId;
  
	public bcRfTcDetalleOperaciones() {
		limpia();
	}
	
  public void setMaestroOperacionId(String maestroOperacionId) {
		this.maestroOperacionId = maestroOperacionId;
	}

	public String getMaestroOperacionId() {
		return this.maestroOperacionId;
	}

	public void setCuentaContableId(String cuentaContableId) {
		this.cuentaContableId = cuentaContableId;
	}

	public String getCuentaContableId() {
		return this.cuentaContableId;
	}

	public void setOperacionContableId(String operacionContableId) {
		this.operacionContableId = operacionContableId;
	}

	public String getOperacionContableId() {
		return this.operacionContableId;
	}

	public void setDetalleOperacionId(String detalleOperacionId) {
		this.detalleOperacionId = detalleOperacionId;
	}

	public String getDetalleOperacionId() {
		return this.detalleOperacionId;
	}
  
  private void limpia(){
		maestroOperacionId = "";
		cuentaContableId = "";
		operacionContableId = "";
		detalleOperacionId = "";

  }
  
  private void adecuaCampos() {
		maestroOperacionId = ((maestroOperacionId==null)||(maestroOperacionId.equals("")))? "null" : maestroOperacionId;
		cuentaContableId = ((cuentaContableId==null)||(cuentaContableId.equals("")))? "null" : cuentaContableId;
		operacionContableId = ((operacionContableId==null)||(operacionContableId.equals("")))? "null" : operacionContableId;
		detalleOperacionId = ((detalleOperacionId==null)||(detalleOperacionId.equals("")))? "null" : detalleOperacionId;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_RF_TC_DETALLE_OPERACIONES.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        detalleOperacionId = rsQuerySeq.getString("valoractual");
      } //del while
      if(detalleOperacionId == null)
        detalleOperacionId = "1";

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
			SQL.append("  CUENTA_CONTABLE_ID, \n");
			SQL.append("  OPERACION_CONTABLE_ID, \n");
			SQL.append("  DETALLE_OPERACION_ID \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TC_DETALLE_OPERACIONES\n");
			SQL.append("where \n");
			SQL.append("  DETALLE_OPERACION_ID = " + detalleOperacionId + "  ");

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
              cuentaContableId = rsQuery.getString("CUENTA_CONTABLE_ID")==null?"":rsQuery.getString("CUENTA_CONTABLE_ID");
              operacionContableId = rsQuery.getString("OPERACION_CONTABLE_ID")==null?"":rsQuery.getString("OPERACION_CONTABLE_ID");
              detalleOperacionId = rsQuery.getString("DETALLE_OPERACION_ID")==null?"":rsQuery.getString("DETALLE_OPERACION_ID");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTcDetalleOperaciones.select: " + SQL.toString());
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
      SQL.append("insert into RF_TC_DETALLE_OPERACIONES ( ");
      SQL.append("MAESTRO_OPERACION_ID, ");
      SQL.append("CUENTA_CONTABLE_ID, ");
      SQL.append("OPERACION_CONTABLE_ID, ");
      SQL.append("DETALLE_OPERACION_ID ");
      SQL.append("\n) values (");
      SQL.append(maestroOperacionId + ", ");
      SQL.append(cuentaContableId + ", ");
      SQL.append(operacionContableId + ", ");
      SQL.append(detalleOperacionId + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTcDetalleOperaciones.insert: " + SQL);
      detalleOperacionId = "-1";

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
       SQL.append("update RF_TC_DETALLE_OPERACIONES set \n");
       SQL.append("  MAESTRO_OPERACION_ID = " + maestroOperacionId + " , \n");
       SQL.append("  CUENTA_CONTABLE_ID = " + cuentaContableId + " , \n");
       SQL.append("  OPERACION_CONTABLE_ID = " + operacionContableId + " , \n");
       SQL.append("  DETALLE_OPERACION_ID = " + detalleOperacionId + "  \n");
       SQL.append("where \n");
       SQL.append("  DETALLE_OPERACION_ID = " + detalleOperacionId + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTcDetalleOperaciones.update: "+SQL);
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
        SQL = "delete from RF_TC_DETALLE_OPERACIONES where DETALLE_OPERACION_ID = " + detalleOperacionId + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTcDetalleOperaciones.delete: "+SQL);         
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
		sb.append(getOperacionContableId());
		sb.append(",");
		sb.append(getCuentaContableId());
		sb.append(",");
		sb.append(getDetalleOperacionId());
    return sb.toString();
  }
  
}
