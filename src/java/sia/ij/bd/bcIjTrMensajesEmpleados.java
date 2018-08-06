package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrMensajesEmpleados extends BeanBase {
		
  private String idMensaje;
	private String numEmpleado;
  
	public bcIjTrMensajesEmpleados() {
		limpia();
	}
	
  public void setIdMensaje(String idMensaje) {
		this.idMensaje = idMensaje;
	}

	public String getIdMensaje() {
		return this.idMensaje;
	}

	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNumEmpleado() {
		return this.numEmpleado;
	}
  
  private void limpia(){
		idMensaje = "";
		numEmpleado = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
    
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  ID_MENSAJE, \n");
			SQL.append("  NUM_EMPLEADO \n");
			SQL.append("from \n"); 
			SQL.append("  SG_TR_MENSAJES_EMPLEADOS\n");
			SQL.append("where \n");
			SQL.append("  ID_MENSAJE = " + idMensaje + " and ");
			SQL.append("  ID_MENSAJE = " + idMensaje + " and ");
			SQL.append("  NUM_EMPLEADO = " + numEmpleado + " and ");
			SQL.append("  NUM_EMPLEADO = " + numEmpleado + "  ");

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
              numEmpleado = rsQuery.getString("NUM_EMPLEADO")==null?"":rsQuery.getString("NUM_EMPLEADO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcSGTrMensajesEmpleados.select: " + SQL.toString());
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
      //adecuaCampos();   
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into SG_TR_MENSAJES_EMPLEADOS ( ");
      SQL.append("ID_MENSAJE, ");
      SQL.append("NUM_EMPLEADO ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idMensaje)+", ");
      SQL.append(getCampo(numEmpleado)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcSGTrMensajesEmpleados.insert: " + SQL);
      idMensaje = "-1";
      idMensaje = "-1";
      numEmpleado = "-1";
      numEmpleado = "-1";

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
       SQL.append("update SG_TR_MENSAJES_EMPLEADOS set \n");
       SQL.append("  ID_MENSAJE = " + getCampo(idMensaje)+", \n");
       SQL.append("  NUM_EMPLEADO = " + getCampo(numEmpleado)+" \n");
       SQL.append("where \n");
       SQL.append("  ID_MENSAJE = " + idMensaje + "  and " );
       SQL.append("  ID_MENSAJE = " + idMensaje + "  and " );
       SQL.append("  NUM_EMPLEADO = " + numEmpleado + "  and " );
       SQL.append("  NUM_EMPLEADO = " + numEmpleado + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcSGTrMensajesEmpleados.update: "+SQL);
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
        SQL = "delete from SG_TR_MENSAJES_EMPLEADOS where ID_MENSAJE = " + idMensaje + " and ID_MENSAJE = " + idMensaje + " and NUM_EMPLEADO = " + numEmpleado + " and NUM_EMPLEADO = " + numEmpleado + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcSGTrMensajesEmpleados.delete: "+SQL);         
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
		sb.append(getNumEmpleado());
		sb.append(",");
		sb.append(getIdMensaje());
    return sb.toString();
  }
  
}
