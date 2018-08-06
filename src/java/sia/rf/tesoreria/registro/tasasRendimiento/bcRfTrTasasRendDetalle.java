package sia.rf.tesoreria.registro.tasasRendimiento;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrTasasRendDetalle extends BeanBase {
		
  private String idRendimiento;
	private String plazo;
	private String tasa;
  
	public bcRfTrTasasRendDetalle() {
		limpia();
	}
	
  public void setIdRendimiento(String idRendimiento) {
		this.idRendimiento = idRendimiento;
	}

	public String getIdRendimiento() {
		return this.idRendimiento;
	}

	public void setPlazo(String plazo) {
		this.plazo = plazo;
	}

	public String getPlazo() {
		return this.plazo;
	}

	public void setTasa(String tasa) {
		this.tasa = tasa;
	}

	public String getTasa() {
		return this.tasa;
	}
  
  private void limpia(){
		idRendimiento = "";
		plazo = "";
		tasa = "";

  }
  
  private void adecuaCampos() {
		idRendimiento = ((idRendimiento==null)||(idRendimiento.equals("")))? "null" : idRendimiento;
		plazo = ((plazo==null)||(plazo.equals("")))? "null" : plazo;
		tasa = ((tasa==null)||(tasa.equals("")))? "null" : tasa;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
//      while (rsQuerySeq.next()){
//        TODO:// Asignar llave compuesta
//      } //del while

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
			SQL.append("  ID_RENDIMIENTO, \n");
			SQL.append("  PLAZO, \n");
			SQL.append("  TASA \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_tasas_rend_detalle\n");
			SQL.append("where \n");
			SQL.append("  ID_RENDIMIENTO = " + idRendimiento + " and ");
			SQL.append("  PLAZO = " + plazo + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idRendimiento = rsQuery.getString("ID_RENDIMIENTO")==null?"":rsQuery.getString("ID_RENDIMIENTO");
              plazo = rsQuery.getString("PLAZO")==null?"":rsQuery.getString("PLAZO");
              tasa = rsQuery.getString("TASA")==null?"":rsQuery.getString("TASA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrTasasRendDetalle.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_tasas_rend_detalle ( ");
      SQL.append("ID_RENDIMIENTO, ");
      SQL.append("PLAZO, ");
      SQL.append("TASA ");
      SQL.append("\n) values (");
      SQL.append(idRendimiento + ", ");
      SQL.append(plazo + ", ");
      SQL.append(tasa + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrTasasRendDetalle.insert: " + SQL);
      idRendimiento = "-1";
      plazo = "-1";

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
       SQL.append("update rf_tr_tasas_rend_detalle set \n");
       SQL.append("  ID_RENDIMIENTO = " + idRendimiento + " , \n");
       SQL.append("  PLAZO = " + plazo + " , \n");
       SQL.append("  TASA = " + tasa + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_RENDIMIENTO = " + idRendimiento + "  and " );
       SQL.append("  PLAZO = " + plazo + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrTasasRendDetalle.update: "+SQL);
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
        SQL = "delete from rf_tr_tasas_rend_detalle where ID_RENDIMIENTO = " + idRendimiento + " and PLAZO = " + plazo + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrTasasRendDetalle.delete: "+SQL);         
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
		sb.append(getTasa());
		sb.append(",");
		sb.append(getIdRendimiento());
		sb.append(",");
		sb.append(getPlazo());
    return sb.toString();
  }
  
}
