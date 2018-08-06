package sia.rf.tesoreria.registro.efectivoInversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrEfectivoInversion extends BeanBase {
		
  private String idEfectivoInversion;
	private String idOperacion;
	private String idCuentaInversion;
	private String importe;
	private String fecha;
	private String vincimiento;
	private String rendimiento;
	private String idOperacionRecupera;
	private String idOperacionDeposito;
	private String idComprasInversion;
  
	public bcRfTrEfectivoInversion() {
		limpia();
	}
	
  public void setIdEfectivoInversion(String idEfectivoInversion) {
		this.idEfectivoInversion = idEfectivoInversion;
	}

	public String getIdEfectivoInversion() {
		return this.idEfectivoInversion;
	}

	public void setIdOperacion(String idOperacion) {
		this.idOperacion = idOperacion;
	}

	public String getIdOperacion() {
		return this.idOperacion;
	}

	public void setIdCuentaInversion(String idCuentaInversion) {
		this.idCuentaInversion = idCuentaInversion;
	}

	public String getIdCuentaInversion() {
		return this.idCuentaInversion;
	}

	public void setImporte(String importe) {
		this.importe = importe;
	}

	public String getImporte() {
		return this.importe;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setVincimiento(String vincimiento) {
		this.vincimiento = vincimiento;
	}

	public String getVincimiento() {
		return this.vincimiento;
	}

	public void setRendimiento(String rendimiento) {
		this.rendimiento = rendimiento;
	}

	public String getRendimiento() {
		return this.rendimiento;
	}

	public void setIdOperacionRecupera(String idOperacionRecupera) {
		this.idOperacionRecupera = idOperacionRecupera;
	}

	public String getIdOperacionRecupera() {
		return this.idOperacionRecupera;
	}

	public void setIdOperacionDeposito(String idOperacionDeposito) {
		this.idOperacionDeposito = idOperacionDeposito;
	}

	public String getIdOperacionDeposito() {
		return this.idOperacionDeposito;
	}

	public void setIdComprasInversion(String idComprasInversion) {
		this.idComprasInversion = idComprasInversion;
	}

	public String getIdComprasInversion() {
		return this.idComprasInversion;
	}
  
  private void limpia(){
		idEfectivoInversion = "";
		idOperacion = "";
		idCuentaInversion = "";
		importe = "";
		fecha = "";
		vincimiento = "";
		rendimiento = "";
		idOperacionRecupera = "";
		idOperacionDeposito = "";
		idComprasInversion = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select max(ID_EFECTIVO_INVERSION) + 1 valoractual from RF_TR_EFECTIVO_INVERSION");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idEfectivoInversion = rsQuerySeq.getString("valoractual");
      } //del while
      if(idEfectivoInversion == null)
        idEfectivoInversion = "1";

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
			SQL.append("  ID_EFECTIVO_INVERSION, \n");
			SQL.append("  ID_OPERACION, \n");
			SQL.append("  ID_CUENTA_INVERSION, \n");
			SQL.append("  IMPORTE, \n");
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  to_char(VINCIMIENTO,'dd/mm/yyyy') VINCIMIENTO, \n");
			SQL.append("  RENDIMIENTO, \n");
			SQL.append("  ID_OPERACION_RECUPERA, \n");
			SQL.append("  ID_OPERACION_DEPOSITO, \n");
			SQL.append("  ID_COMPRAS_INVERSION \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_EFECTIVO_INVERSION\n");
			SQL.append("where \n");
			SQL.append("  ID_EFECTIVO_INVERSION = " + idEfectivoInversion + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idEfectivoInversion = rsQuery.getString("ID_EFECTIVO_INVERSION")==null?"":rsQuery.getString("ID_EFECTIVO_INVERSION");
              idOperacion = rsQuery.getString("ID_OPERACION")==null?"":rsQuery.getString("ID_OPERACION");
              idCuentaInversion = rsQuery.getString("ID_CUENTA_INVERSION")==null?"":rsQuery.getString("ID_CUENTA_INVERSION");
              importe = rsQuery.getString("IMPORTE")==null?"":rsQuery.getString("IMPORTE");
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              vincimiento = rsQuery.getString("VINCIMIENTO")==null?"":rsQuery.getString("VINCIMIENTO");
              rendimiento = rsQuery.getString("RENDIMIENTO")==null?"":rsQuery.getString("RENDIMIENTO");
              idOperacionRecupera = rsQuery.getString("ID_OPERACION_RECUPERA")==null?"":rsQuery.getString("ID_OPERACION_RECUPERA");
              idOperacionDeposito = rsQuery.getString("ID_OPERACION_DEPOSITO")==null?"":rsQuery.getString("ID_OPERACION_DEPOSITO");
              idComprasInversion = rsQuery.getString("ID_COMPRAS_INVERSION")==null?"":rsQuery.getString("ID_COMPRAS_INVERSION");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrEfectivoInversion.select: " + SQL.toString());
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
      SQL.append("insert into RF_TR_EFECTIVO_INVERSION ( ");
      SQL.append("ID_EFECTIVO_INVERSION, ");
      SQL.append("ID_OPERACION, ");
      SQL.append("ID_CUENTA_INVERSION, ");
      SQL.append("IMPORTE, ");
      SQL.append("FECHA, ");
      SQL.append("VINCIMIENTO, ");
      SQL.append("RENDIMIENTO, ");
      SQL.append("ID_OPERACION_RECUPERA, ");
      SQL.append("ID_OPERACION_DEPOSITO, ");
      SQL.append("ID_COMPRAS_INVERSION ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idEfectivoInversion)+", ");
      SQL.append(getCampo(idOperacion)+", ");
      SQL.append(getCampo(idCuentaInversion)+", ");
      SQL.append(getCampo(importe)+", ");
      SQL.append(getDate(fecha)+", ");
      SQL.append(getDate(vincimiento)+", ");
      SQL.append(getCampo(rendimiento)+", ");
      SQL.append(getCampo(idOperacionRecupera)+", ");
      SQL.append(getCampo(idOperacionDeposito)+", ");
      SQL.append(getCampo(idComprasInversion)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrEfectivoInversion.insert: " + SQL);
      idEfectivoInversion = "-1";

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
       SQL.append("update RF_TR_EFECTIVO_INVERSION set \n");
       SQL.append("  ID_EFECTIVO_INVERSION = " + getCampo(idEfectivoInversion)+", \n");
       SQL.append("  ID_OPERACION = " + getCampo(idOperacion)+", \n");
       SQL.append("  ID_CUENTA_INVERSION = " + getCampo(idCuentaInversion)+", \n");
       SQL.append("  IMPORTE = " + getCampo(importe)+", \n");
       SQL.append("  FECHA = " + getDate(fecha)+", \n");
       SQL.append("  VINCIMIENTO = " + getDate(vincimiento)+", \n");
       SQL.append("  RENDIMIENTO = " + getCampo(rendimiento)+", \n");
       SQL.append("  ID_OPERACION_RECUPERA = " + getCampo(idOperacionRecupera)+", \n");
       SQL.append("  ID_OPERACION_DEPOSITO = " + getCampo(idOperacionDeposito)+", \n");
       SQL.append("  ID_COMPRAS_INVERSION = " + getCampo(idComprasInversion)+" \n");
       SQL.append("where \n");
       SQL.append("  ID_EFECTIVO_INVERSION = " + idEfectivoInversion + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrEfectivoInversion.update: "+SQL);
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
        SQL = "delete from RF_TR_EFECTIVO_INVERSION where ID_EFECTIVO_INVERSION = " + idEfectivoInversion + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrEfectivoInversion.delete: "+SQL);         
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
		sb.append(getIdEfectivoInversion());
		sb.append(",");
		sb.append(getFecha());
		sb.append(",");
		sb.append(getIdOperacion());
		sb.append(",");
		sb.append(getIdComprasInversion());
		sb.append(",");
		sb.append(getRendimiento());
		sb.append(",");
		sb.append(getIdOperacionDeposito());
		sb.append(",");
		sb.append(getIdCuentaInversion());
		sb.append(",");
		sb.append(getImporte());
		sb.append(",");
		sb.append(getIdOperacionRecupera());
		sb.append(",");
		sb.append(getVincimiento());
    return sb.toString();
  }
  
}
