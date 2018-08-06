package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrMensajesMenu extends BeanBase {
		
  private String idMensaje;
	private String consecutivo;
  
	public bcIjTrMensajesMenu() {
		limpia();
	}
	
  public void setIdMensaje(String idMensaje) {
		this.idMensaje = idMensaje;
	}

	public String getIdMensaje() {
		return this.idMensaje;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return this.consecutivo;
	}
  
  private void limpia(){
		idMensaje = "";
		consecutivo = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  ID_MENSAJE, \n");
			SQL.append("  CONSECUTIVO \n");
			SQL.append("from \n"); 
			SQL.append("  SG_TR_MENSAJES_MENU\n");
			SQL.append("where \n");
			SQL.append("  CONSECUTIVO = " + consecutivo + " and ");
			SQL.append("  CONSECUTIVO = " + consecutivo + " and ");
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
              consecutivo = rsQuery.getString("CONSECUTIVO")==null?"":rsQuery.getString("CONSECUTIVO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcSGTrMensajesMenu.select: " + SQL.toString());
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
      SQL = new StringBuffer();
      SQL.setLength(0);
      SQL.append("insert into SG_TR_MENSAJES_MENU ( ");
      SQL.append("ID_MENSAJE, ");
      SQL.append("CONSECUTIVO ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idMensaje)+", ");
      SQL.append(getCampo(consecutivo)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcSGTrMensajesMenu.insert: " + SQL);
      consecutivo = "-1";
      consecutivo = "-1";
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
       SQL.append("update SG_TR_MENSAJES_MENU set \n");
       SQL.append("  ID_MENSAJE = " + getCampo(idMensaje)+", \n");
       SQL.append("  CONSECUTIVO = " + getCampo(consecutivo)+" \n");
       SQL.append("where \n");
       SQL.append("  CONSECUTIVO = " + consecutivo + "  and " );
       SQL.append("  CONSECUTIVO = " + consecutivo + "  and " );
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
          System.out.println("bcSGTrMensajesMenu.update: "+SQL);
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
        SQL = "delete from SG_TR_MENSAJES_MENU where CONSECUTIVO = " + consecutivo + " and CONSECUTIVO = " + consecutivo + " and ID_MENSAJE = " + idMensaje + " and ID_MENSAJE = " + idMensaje + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcSGTrMensajesMenu.delete: "+SQL);         
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
		sb.append(getConsecutivo());
		sb.append(",");
		sb.append(getIdMensaje());
    return sb.toString();
  }
  
}
