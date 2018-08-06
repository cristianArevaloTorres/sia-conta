package sia.rf.contabilidad.registroContableNuevo.formas;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.util.List;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;
import sia.db.sql.Vista;

public class bcRfTrFormasFunciones extends BeanBase {
		
  private String formaContableId;
	private String idfuncion;
  
	public bcRfTrFormasFunciones() {
		limpia();
	}
	
  public void setFormaContableId(String formaContableId) {
		this.formaContableId = formaContableId;
	}

	public String getFormaContableId() {
		return this.formaContableId;
	}

	public void setIdfuncion(String idfuncion) {
		this.idfuncion = idfuncion;
	}

	public String getIdfuncion() {
		return this.idfuncion;
	}
  
  private void limpia(){
		formaContableId = "";
		idfuncion = "";

  }
  
  private void adecuaCampos() {
		formaContableId = ((formaContableId==null)||(formaContableId.equals("")))? "null" : formaContableId;
		idfuncion = ((idfuncion==null)||(idfuncion.equals("")))? "null" : idfuncion;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
//      rsQuerySeq = stQuery.executeQuery(SQL.toString());
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
			SQL.append("  FORMA_CONTABLE_ID, \n");
			SQL.append("  IDFUNCION \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_formas_funciones\n");
			SQL.append("where \n");
			SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + " and ");
			SQL.append("  IDFUNCION = " + idfuncion + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              formaContableId = rsQuery.getString("FORMA_CONTABLE_ID")==null?"":rsQuery.getString("FORMA_CONTABLE_ID");
              idfuncion = rsQuery.getString("IDFUNCION")==null?"":rsQuery.getString("IDFUNCION");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrFormasFunciones.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_formas_funciones ( ");
      SQL.append("FORMA_CONTABLE_ID, ");
      SQL.append("IDFUNCION ");
      SQL.append("\n) values (");
      SQL.append(formaContableId + ", ");
      SQL.append(idfuncion + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrFormasFunciones.insert: " + SQL);
      formaContableId = "-1";
      idfuncion = "-1";

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
       SQL.append("update rf_tr_formas_funciones set \n");
       SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + " , \n");
       SQL.append("  IDFUNCION = " + idfuncion + "  \n");
       SQL.append("where \n");
       SQL.append("  FORMA_CONTABLE_ID = " + formaContableId + "  and " );
       SQL.append("  IDFUNCION = " + idfuncion + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrFormasFunciones.update: "+SQL);
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
        SQL = "delete from rf_tr_formas_funciones where FORMA_CONTABLE_ID = " + formaContableId + " and IDFUNCION = " + idfuncion + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrFormasFunciones.delete: "+SQL);         
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
    sb.append(getIdfuncion());
    sb.append(",");
    sb.append(getFormaContableId());
    return sb.toString();
  }
  
  public void insertInGroup(Connection con, String forma) throws Exception {
    try  {
      addParamVal("forma","and f.forma = ':param'",forma);
      List<Vista> regs = consultar(DaoFactory.CONEXION_CONTABILIDAD,"eventos.select.RfTrFormacontable-getFormas.Formas");
      for(Vista reg : regs) {
        setFormaContableId(reg.getField("idforma"));
        insert(con);
        System.out.println(getFormaContableId() + '-' + getIdfuncion());
      }
      liberaParametros();  
    } catch (Exception ex)  {
      throw new Exception("Ocurrio un error al hacer la insercion en grupo",ex);
    } finally  {
    }
  }
  
}
