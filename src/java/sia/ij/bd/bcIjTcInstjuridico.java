package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class bcIjTcInstjuridico extends BeanBase {
		
  private String idinstjuridico;
	private String descripcion;
	private String siglas;
	private String estatus;
  
	public bcIjTcInstjuridico() {
		limpia();
	}
	
  public void setIdinstjuridico(String idinstjuridico) {
		this.idinstjuridico = idinstjuridico;
	}

	public String getIdinstjuridico() {
		return this.idinstjuridico;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setSiglas(String siglas) {
		this.siglas = siglas;
	}

	public String getSiglas() {
		return this.siglas;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}
  
  private void limpia(){
		idinstjuridico = "";
		descripcion = "";
		siglas = "";
		estatus = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(IDINSTJURIDICO) + 1 valoractual from IJ_TC_INSTJURIDICO");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idinstjuridico = rsQuerySeq.getString("valoractual");
      } //del while
      if(idinstjuridico == null)
        idinstjuridico = "1";

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
			SQL.append("  IDINSTJURIDICO, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  SIGLAS, \n");
			SQL.append("  ESTATUS \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_INSTJURIDICO\n");
			SQL.append("where \n");
			SQL.append("  IDINSTJURIDICO = " + idinstjuridico + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idinstjuridico = rsQuery.getString("IDINSTJURIDICO")==null?"":rsQuery.getString("IDINSTJURIDICO");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              siglas = rsQuery.getString("SIGLAS")==null?"":rsQuery.getString("SIGLAS");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcInstjuridico.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TC_INSTJURIDICO ( ");
      SQL.append("IDINSTJURIDICO, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("SIGLAS, ");
      SQL.append("ESTATUS ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idinstjuridico)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getString(siglas)+", ");
      SQL.append(getString(estatus)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcInstjuridico.insert: " + SQL);
      idinstjuridico = "-1";

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
       SQL.append("update IJ_TC_INSTJURIDICO set \n");
       SQL.append("  IDINSTJURIDICO = " + getCampo(idinstjuridico)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  SIGLAS = " + getString(siglas)+", \n");
       SQL.append("  ESTATUS = " + getString(estatus)+" \n");
       SQL.append("where \n");
       SQL.append("  IDINSTJURIDICO = " + idinstjuridico + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcInstjuridico.update: "+SQL);
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
        SQL = "delete from IJ_TC_INSTJURIDICO where IDINSTJURIDICO = " + idinstjuridico + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcInstjuridico.delete: "+SQL);         
        throw new Exception(SQL.toString(),e);
      }
      finally {                  
        if (stQuery != null) {
          stQuery.close();
          stQuery=null;
        }                         
      }
  }
  
 public String selectSiglasIj(Connection con) throws Exception{
     StringBuffer SQL;
     SQL = new StringBuffer();
     SQL.append("Select IJTCINSTJURIDICO.SIGLAS From IJ_TC_INSTJURIDICO IJTCINSTJURIDICO \n");
     SQL.append(" Where IJTCINSTJURIDICO.IDINSTJURIDICO = "+this.idinstjuridico+" \n");
     Statement stQuery = null;
     ResultSet rsQuery = null;
     String regresa = "";
     try {
       stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
       rsQuery = stQuery.executeQuery(SQL.toString());
       
       rsQuery.beforeFirst();
       if (rsQuery.next() )  {
               regresa = rsQuery.getString("SIGLAS")==null?"":rsQuery.getString("SIGLAS");
       }
       
       return regresa;
     }
     catch (Exception e) {
       System.out.println("Ha ocurrido un error en el metodo selectSiglasIj");
       System.out.println("bcIjTcInstjuridico.selectSiglasIj: " + SQL.toString());
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
    }
  
  
  public String toString() { 
    StringBuffer sb = new StringBuffer();
		sb.append(getSiglas());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getIdinstjuridico());
    return sb.toString();
  }
  
}
