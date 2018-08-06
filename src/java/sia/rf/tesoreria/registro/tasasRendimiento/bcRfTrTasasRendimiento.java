package sia.rf.tesoreria.registro.tasasRendimiento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrTasasRendimiento extends BeanBase {
		
  private String fecha;
	private String idBanco;
	private String idRendimiento;
  
	public bcRfTrTasasRendimiento() {
		limpia();
	}
	
  public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
	}

	public String getIdBanco() {
		return this.idBanco;
	}

	public void setIdRendimiento(String idRendimiento) {
		this.idRendimiento = idRendimiento;
	}

	public String getIdRendimiento() {
		return this.idRendimiento;
	}
  
  private void limpia(){
		fecha = "";
		idBanco = "";
		idRendimiento = "";

  }
  
  private void adecuaCampos() {
		fecha = ((fecha==null)||(fecha.equals("")))? "null" : "to_date('" + fecha + "','dd/mm/yyyy')";
		idBanco = ((idBanco==null)||(idBanco.equals("")))? "null" : idBanco;
		idRendimiento = ((idRendimiento==null)||(idRendimiento.equals("")))? "null" : idRendimiento;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(ID_RENDIMIENTO)+1 valoractual from rf_tr_tasas_rendimiento");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idRendimiento = rsQuerySeq.getString("valoractual");
      } //del while
      if(idRendimiento == null)
        idRendimiento = "1";

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
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  ID_BANCO, \n");
			SQL.append("  ID_RENDIMIENTO \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_tasas_rendimiento\n");
			SQL.append("where \n");
			SQL.append("  ID_RENDIMIENTO = " + idRendimiento + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              idBanco = rsQuery.getString("ID_BANCO")==null?"":rsQuery.getString("ID_BANCO");
              idRendimiento = rsQuery.getString("ID_RENDIMIENTO")==null?"":rsQuery.getString("ID_RENDIMIENTO");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrTasasRendimiento.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_tasas_rendimiento ( ");
      SQL.append("FECHA, ");
      SQL.append("ID_BANCO, ");
      SQL.append("ID_RENDIMIENTO ");
      SQL.append("\n) values (");
      SQL.append(fecha + ", ");
      SQL.append(idBanco + ", ");
      SQL.append(idRendimiento + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrTasasRendimiento.insert: " + SQL);
      idRendimiento = "-1";

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
       SQL.append("update rf_tr_tasas_rendimiento set \n");
       SQL.append("  FECHA = " + fecha + " , \n");
       SQL.append("  ID_BANCO = " + idBanco + " , \n");
       SQL.append("  ID_RENDIMIENTO = " + idRendimiento + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_RENDIMIENTO = " + idRendimiento + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrTasasRendimiento.update: "+SQL);
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
        SQL = "delete from rf_tr_tasas_rendimiento where ID_RENDIMIENTO = " + idRendimiento + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrTasasRendimiento.delete: "+SQL);         
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
		sb.append(getFecha());
		sb.append(",");
		sb.append(getIdBanco());
		sb.append(",");
		sb.append(getIdRendimiento());
    return sb.toString();
  }
  
}
