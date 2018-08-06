package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class bcIjTrMensajesGrupo extends BeanBase {
		
  private String idMensaje;
	private String cveGrupo;
  
	public bcIjTrMensajesGrupo() {
		limpia();
	}
	
  public void setIdMensaje(String idMensaje) {
		this.idMensaje = idMensaje;
	}

	public String getIdMensaje() {
		return this.idMensaje;
	}

	public void setCveGrupo(String cveGrupo) {
		this.cveGrupo = cveGrupo;
	}

	public String getCveGrupo() {
		return this.cveGrupo;
	}
  
  private void limpia(){
		idMensaje = "";
		cveGrupo = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  public int select(Connection con) throws Exception {
    StringBuffer SQL;
    SQL = new StringBuffer();
			SQL.append("select \n");
			SQL.append("  ID_MENSAJE, \n");
			SQL.append("  CVE_GRUPO \n");
			SQL.append("from \n"); 
			SQL.append("  SG_TR_MENSAJES_GRUPO\n");
			SQL.append("where \n");
			SQL.append("  CVE_GRUPO = " + cveGrupo + " and ");
			SQL.append("  CVE_GRUPO = " + cveGrupo + " and ");
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
              cveGrupo = rsQuery.getString("CVE_GRUPO")==null?"":rsQuery.getString("CVE_GRUPO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcSGTrMensajesGrupo.select: " + SQL.toString());
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
      SQL.append("insert into SG_TR_MENSAJES_GRUPO ( ");
      SQL.append("ID_MENSAJE, ");
      SQL.append("CVE_GRUPO ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idMensaje)+", ");
      SQL.append(getCampo(cveGrupo)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcSGTrMensajesGrupo.insert: " + SQL);
      cveGrupo = "-1";
      cveGrupo = "-1";
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
       SQL.append("update SG_TR_MENSAJES_GRUPO set \n");
       SQL.append("  ID_MENSAJE = " + getCampo(idMensaje)+", \n");
       SQL.append("  CVE_GRUPO = " + getCampo(cveGrupo)+" \n");
       SQL.append("where \n");
       SQL.append("  CVE_GRUPO = " + cveGrupo + "  and " );
       SQL.append("  CVE_GRUPO = " + cveGrupo + "  and " );
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
          System.out.println("bcSGTrMensajesGrupo.update: "+SQL);
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
        SQL = "delete from SG_TR_MENSAJES_GRUPO where CVE_GRUPO = " + cveGrupo + " and CVE_GRUPO = " + cveGrupo + " and ID_MENSAJE = " + idMensaje + " and ID_MENSAJE = " + idMensaje + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcSGTrMensajesGrupo.delete: "+SQL);         
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
		sb.append(getIdMensaje());
		sb.append(",");
		sb.append(getCveGrupo());
    return sb.toString();
  }
  
}
