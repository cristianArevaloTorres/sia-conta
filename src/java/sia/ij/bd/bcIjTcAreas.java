package sia.ij.bd;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;


public class bcIjTcAreas extends BeanBase {
		
  private String idareas;
	private String descripcion;
	private String estatus;
  
	public bcIjTcAreas() {
		limpia();
	}
	
  public void setIdareas(String idareas) {
		this.idareas = idareas;
	}

	public String getIdareas() {
		return this.idareas;
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
		idareas = "";
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
      SQL = new StringBuffer("select max(IDAREAS) + 1 valoractual from IJ_TC_AREAS");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idareas = rsQuerySeq.getString("valoractual");
      } //del while
      if(idareas == null)
        idareas = "1";

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
			SQL.append("  IDAREAS, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  ESTATUS \n");
			SQL.append("from \n"); 
			SQL.append("  IJ_TC_AREAS\n");
			SQL.append("where \n");
			SQL.append("  IDAREAS = " + idareas + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idareas = rsQuery.getString("IDAREAS")==null?"":rsQuery.getString("IDAREAS");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcIjTcAreas.select: " + SQL.toString());
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
      SQL.append("insert into IJ_TC_AREAS ( ");
      SQL.append("IDAREAS, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("ESTATUS ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idareas)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getString(estatus)+")  ");
System.out.println("insertar areas "+SQL.toString());
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcIjTcAreas.insert: " + SQL);
      idareas = "-1";

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
       SQL.append("update IJ_TC_AREAS set \n");
       SQL.append("  IDAREAS = " + getCampo(idareas)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  ESTATUS = " + getString(estatus)+" \n");
       SQL.append("where \n");
       SQL.append("  IDAREAS = " + idareas + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcIjTcAreas.update: "+SQL);
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
        SQL = "delete from IJ_TC_AREAS where IDAREAS = " + idareas + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcIjTcAreas.delete: "+SQL);         
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
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getIdareas());
    return sb.toString();
  }
  
}
