package sia.ij.bd;

import sia.configuracion.BeanBase;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;



public class bcIjTcDregional extends BeanBase {
		
  private String iddregional;
	private String descripcion;
	private String estatus;
  
	public bcIjTcDregional() {
		limpia();
	}
	
  public void setIddregional(String iddregional) {
		this.iddregional = iddregional;
	}

	public String getIddregional() {
		return this.iddregional;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}
  
  private void limpia(){
		iddregional = "";
		descripcion = "";
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
      SQL = new StringBuffer("select max(IDDREGIONAL) + 1 valoractual from IJ_TC_DREGIONAL");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        iddregional = rsQuerySeq.getString("valoractual");
      } //del while
      if(iddregional == null)
        iddregional = "1";

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
			SQL.append("  IDDREGIONAL, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  ESTATUS \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_DREGIONAL\n");
			SQL.append("where \n");
			SQL.append("  IDDREGIONAL = " + iddregional + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              iddregional = rsQuery.getString("IDDREGIONAL")==null?"":rsQuery.getString("IDDREGIONAL");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcDregional.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TC_DREGIONAL ( ");
      SQL.append("IDDREGIONAL, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("ESTATUS ");
      SQL.append("\n) values (");
      SQL.append(getCampo(iddregional)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getString(estatus)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcDregional.insert: " + SQL);
      iddregional = "-1";

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
       SQL.append("update IJ_TC_DREGIONAL set \n");
       SQL.append("  IDDREGIONAL = " + getCampo(iddregional)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  ESTATUS = " + getString(estatus)+" \n");
       SQL.append("where \n");
       SQL.append("  IDDREGIONAL = " + iddregional + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcDregional.update: "+SQL);
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
        SQL = "delete from IJ_TC_DREGIONAL where IDDREGIONAL = " + iddregional + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcDregional.delete: "+SQL);         
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
		sb.append(getIddregional());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getDescripcion());
    return sb.toString();
  }
  
}
