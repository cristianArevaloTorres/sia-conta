package sia.rf.tesoreria.registro.cuentasInversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrCuentasInversion extends BeanBase {
		
  private String idCuentaInversion;
	private String idBanco;
	private String estatus;
	private String contratoCuenta;
	private String fechaApertura;
  
	public bcRfTrCuentasInversion() {
		limpia();
	}
	
  public void setIdCuentaInversion(String idCuentaInversion) {
		this.idCuentaInversion = idCuentaInversion;
	}

	public String getIdCuentaInversion() {
		return this.idCuentaInversion;
	}

	public void setIdBanco(String idBanco) {
		this.idBanco = idBanco;
	}

	public String getIdBanco() {
		return this.idBanco;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setContratoCuenta(String contratoCuenta) {
		this.contratoCuenta = contratoCuenta;
	}

	public String getContratoCuenta() {
		return this.contratoCuenta;
	}

	public void setFechaApertura(String fechaApertura) {
		this.fechaApertura = fechaApertura;
	}

	public String getFechaApertura() {
		return this.fechaApertura;
	}
  
  private void limpia(){
		idCuentaInversion = "";
		idBanco = "";
		estatus = "";
		contratoCuenta = "";
		fechaApertura = "";

  }
  
  private void adecuaCampos() {
		idCuentaInversion = ((idCuentaInversion==null)||(idCuentaInversion.equals("")))? "null" : idCuentaInversion;
		idBanco = ((idBanco==null)||(idBanco.equals("")))? "null" : idBanco;
		estatus = ((estatus==null)||(estatus.equals("")))? "null" : estatus;
		contratoCuenta = ((contratoCuenta==null)||(contratoCuenta.equals("")))? "null" : "'" +contratoCuenta+ "'";
		fechaApertura = ((fechaApertura==null)||(fechaApertura.equals("")))? "null" : "to_date('" + fechaApertura + "','dd/mm/yyyy')";

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_CUENTAS_INVERSION.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idCuentaInversion = rsQuerySeq.getString("valoractual");
      } //del while
      if(idCuentaInversion == null)
        idCuentaInversion = "1";

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
			SQL.append("  ID_CUENTA_INVERSION, \n");
			SQL.append("  ID_BANCO, \n");
			SQL.append("  ESTATUS, \n");
			SQL.append("  CONTRATO_CUENTA, \n");
			SQL.append("  to_char(FECHA_APERTURA,'dd/mm/yyyy') FECHA_APERTURA \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_CUENTAS_INVERSION\n");
			SQL.append("where \n");
			SQL.append("  ID_CUENTA_INVERSION = " + idCuentaInversion + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idCuentaInversion = rsQuery.getString("ID_CUENTA_INVERSION")==null?"":rsQuery.getString("ID_CUENTA_INVERSION");
              idBanco = rsQuery.getString("ID_BANCO")==null?"":rsQuery.getString("ID_BANCO");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              contratoCuenta = rsQuery.getString("CONTRATO_CUENTA")==null?"":rsQuery.getString("CONTRATO_CUENTA");
              fechaApertura = rsQuery.getString("FECHA_APERTURA")==null?"":rsQuery.getString("FECHA_APERTURA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrCuentasInversion.select: " + SQL.toString());
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
      SQL.append("insert into RF_TR_CUENTAS_INVERSION ( ");
      SQL.append("ID_CUENTA_INVERSION, ");
      SQL.append("ID_BANCO, ");
      SQL.append("ESTATUS, ");
      SQL.append("CONTRATO_CUENTA, ");
      SQL.append("FECHA_APERTURA ");
      SQL.append("\n) values (");
      SQL.append(idCuentaInversion + ", ");
      SQL.append(idBanco + ", ");
      SQL.append(estatus + ", ");
      SQL.append(contratoCuenta + ", ");
      SQL.append(fechaApertura + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrCuentasInversion.insert: " + SQL);
      idCuentaInversion = "-1";

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
       SQL.append("update RF_TR_CUENTAS_INVERSION set \n");
       SQL.append("  ID_CUENTA_INVERSION = " + idCuentaInversion + " , \n");
       SQL.append("  ID_BANCO = " + idBanco + " , \n");
       SQL.append("  ESTATUS = " + estatus + " , \n");
       SQL.append("  CONTRATO_CUENTA = " + contratoCuenta + " , \n");
       SQL.append("  FECHA_APERTURA = " + fechaApertura + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_CUENTA_INVERSION = " + idCuentaInversion + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrCuentasInversion.update: "+SQL);
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
        SQL = "delete from RF_TR_CUENTAS_INVERSION where ID_CUENTA_INVERSION = " + idCuentaInversion + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrCuentasInversion.delete: "+SQL);         
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
		sb.append(getIdBanco());
		sb.append(",");
		sb.append(getContratoCuenta());
		sb.append(",");
		sb.append(getFechaApertura());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdCuentaInversion());
    return sb.toString();
  }
  
}
