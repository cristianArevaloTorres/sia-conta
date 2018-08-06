package sia.rf.tesoreria.registro.comprasInversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrComprasInversion extends BeanBase {
		
  private String idCompraInversion;
	private String idCuentaInversion;
	private String idTipoInversion;
	private String importeGlobal;
	private String fecha;
	private String plazo;
	private String tasa;
	private String vencimiento;
	private String numOperacion;
	private String estatus;
  
	public bcRfTrComprasInversion() {
		limpia();
	}
	
  public void setIdCompraInversion(String idCompraInversion) {
		this.idCompraInversion = idCompraInversion;
	}

	public String getIdCompraInversion() {
		return this.idCompraInversion;
	}

	public void setIdCuentaInversion(String idCuentaInversion) {
		this.idCuentaInversion = idCuentaInversion;
	}

	public String getIdCuentaInversion() {
		return this.idCuentaInversion;
	}

	public void setIdTipoInversion(String idTipoInversion) {
		this.idTipoInversion = idTipoInversion;
	}

	public String getIdTipoInversion() {
		return this.idTipoInversion;
	}

	public void setImporteGlobal(String importeGlobal) {
		this.importeGlobal = importeGlobal;
	}

	public String getImporteGlobal() {
		return this.importeGlobal;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
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

	public void setVencimiento(String vencimiento) {
		this.vencimiento = vencimiento;
	}

	public String getVencimiento() {
		return this.vencimiento;
	}

	public void setNumOperacion(String numOperacion) {
		this.numOperacion = numOperacion;
	}

	public String getNumOperacion() {
		return this.numOperacion;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}
  
  private void limpia(){
		idCompraInversion = "";
		idCuentaInversion = "";
		idTipoInversion = "";
		importeGlobal = "";
		fecha = "";
		plazo = "";
		tasa = "";
		vencimiento = "";
		numOperacion = "";
		estatus = "";

  }
  
  private void adecuaCampos() {
		idCompraInversion = ((idCompraInversion==null)||(idCompraInversion.equals("")))? "null" : idCompraInversion;
		idCuentaInversion = ((idCuentaInversion==null)||(idCuentaInversion.equals("")))? "null" : idCuentaInversion;
		idTipoInversion = ((idTipoInversion==null)||(idTipoInversion.equals("")))? "null" : idTipoInversion;
		importeGlobal = ((importeGlobal==null)||(importeGlobal.equals("")))? "null" : importeGlobal;
		fecha = ((fecha==null)||(fecha.equals("")))? "null" : "to_date('" + fecha + "','dd/mm/yyyy')";
		plazo = ((plazo==null)||(plazo.equals("")))? "null" : plazo;
		tasa = ((tasa==null)||(tasa.equals("")))? "null" : tasa;
		vencimiento = ((vencimiento==null)||(vencimiento.equals("")))? "null" : "to_date('" + vencimiento + "','dd/mm/yyyy')";
		numOperacion = ((numOperacion==null)||(numOperacion.equals("")))? "null" : "'" +numOperacion+ "'";
		estatus = ((estatus==null)||(estatus.equals("")))? "null" : estatus;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_COMPRAS_INVERSION.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idCompraInversion = rsQuerySeq.getString("valoractual");
      } //del while
      if(idCompraInversion == null)
        idCompraInversion = "1";

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
			SQL.append("  ID_COMPRA_INVERSION, \n");
			SQL.append("  ID_CUENTA_INVERSION, \n");
			SQL.append("  ID_TIPO_INVERSION, \n");
			SQL.append("  IMPORTE_GLOBAL, \n");
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  PLAZO, \n");
			SQL.append("  TASA, \n");
			SQL.append("  to_char(VENCIMIENTO,'dd/mm/yyyy') VENCIMIENTO, \n");
			SQL.append("  NUM_OPERACION, \n");
			SQL.append("  ESTATUS \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_compras_inversion\n");
			SQL.append("where \n");
			SQL.append("  ID_COMPRA_INVERSION = " + idCompraInversion + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idCompraInversion = rsQuery.getString("ID_COMPRA_INVERSION")==null?"":rsQuery.getString("ID_COMPRA_INVERSION");
              idCuentaInversion = rsQuery.getString("ID_CUENTA_INVERSION")==null?"":rsQuery.getString("ID_CUENTA_INVERSION");
              idTipoInversion = rsQuery.getString("ID_TIPO_INVERSION")==null?"":rsQuery.getString("ID_TIPO_INVERSION");
              importeGlobal = rsQuery.getString("IMPORTE_GLOBAL")==null?"":rsQuery.getString("IMPORTE_GLOBAL");
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              plazo = rsQuery.getString("PLAZO")==null?"":rsQuery.getString("PLAZO");
              tasa = rsQuery.getString("TASA")==null?"":rsQuery.getString("TASA");
              vencimiento = rsQuery.getString("VENCIMIENTO")==null?"":rsQuery.getString("VENCIMIENTO");
              numOperacion = rsQuery.getString("NUM_OPERACION")==null?"":rsQuery.getString("NUM_OPERACION");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrComprasInversion.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_compras_inversion ( ");
      SQL.append("ID_COMPRA_INVERSION, ");
      SQL.append("ID_CUENTA_INVERSION, ");
      SQL.append("ID_TIPO_INVERSION, ");
      SQL.append("IMPORTE_GLOBAL, ");
      SQL.append("FECHA, ");
      SQL.append("PLAZO, ");
      SQL.append("TASA, ");
      SQL.append("VENCIMIENTO, ");
      SQL.append("NUM_OPERACION, ");
      SQL.append("ESTATUS ");
      SQL.append("\n) values (");
      SQL.append(idCompraInversion + ", ");
      SQL.append(idCuentaInversion + ", ");
      SQL.append(idTipoInversion + ", ");
      SQL.append(importeGlobal + ", ");
      SQL.append(fecha + ", ");
      SQL.append(plazo + ", ");
      SQL.append(tasa + ", ");
      SQL.append(vencimiento + ", ");
      SQL.append(numOperacion + ", ");
      SQL.append(estatus + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrComprasInversion.insert: " + SQL);
      idCompraInversion = "-1";

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
       SQL.append("update rf_tr_compras_inversion set \n");
       SQL.append("  ID_COMPRA_INVERSION = " + idCompraInversion + " , \n");
       SQL.append("  ID_CUENTA_INVERSION = " + idCuentaInversion + " , \n");
       SQL.append("  ID_TIPO_INVERSION = " + idTipoInversion + " , \n");
       SQL.append("  IMPORTE_GLOBAL = " + importeGlobal + " , \n");
       SQL.append("  FECHA = " + fecha + " , \n");
       SQL.append("  PLAZO = " + plazo + " , \n");
       SQL.append("  TASA = " + tasa + " , \n");
       SQL.append("  VENCIMIENTO = " + vencimiento + " , \n");
       SQL.append("  NUM_OPERACION = " + numOperacion + " , \n");
       SQL.append("  ESTATUS = " + estatus + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_COMPRA_INVERSION = " + idCompraInversion + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrComprasInversion.update: "+SQL);
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
        SQL = "delete from rf_tr_compras_inversion where ID_COMPRA_INVERSION = " + idCompraInversion + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrComprasInversion.delete: "+SQL);         
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
		sb.append(getIdTipoInversion());
		sb.append(",");
		sb.append(getVencimiento());
		sb.append(",");
		sb.append(getFecha());
		sb.append(",");
		sb.append(getImporteGlobal());
		sb.append(",");
		sb.append(getTasa());
		sb.append(",");
		sb.append(getNumOperacion());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdCuentaInversion());
		sb.append(",");
		sb.append(getIdCompraInversion());
		sb.append(",");
		sb.append(getPlazo());
    return sb.toString();
  }
  
}
