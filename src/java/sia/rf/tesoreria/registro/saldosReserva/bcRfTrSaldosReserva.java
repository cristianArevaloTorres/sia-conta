package sia.rf.tesoreria.registro.saldosReserva;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrSaldosReserva extends BeanBase {
		
  private String idCuenta;
	private String banco;
	private String tipoPertenencia;
	private String numSucursal;
	private String moneda;
	private String saldoCierre;
	private String fecha;
  
	public bcRfTrSaldosReserva() {
		limpia();
	}
	
  public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getIdCuenta() {
		return this.idCuenta;
	}

	public void setBanco(String banco) {
		this.banco = banco;
	}

	public String getBanco() {
		return this.banco;
	}

	public void setTipoPertenencia(String tipoPertenencia) {
		this.tipoPertenencia = tipoPertenencia;
	}

	public String getTipoPertenencia() {
		return this.tipoPertenencia;
	}

	public void setNumSucursal(String numSucursal) {
		this.numSucursal = numSucursal;
	}

	public String getNumSucursal() {
		return this.numSucursal;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getMoneda() {
		return this.moneda;
	}

	public void setSaldoCierre(String saldoCierre) {
		this.saldoCierre = saldoCierre;
	}

	public String getSaldoCierre() {
		return this.saldoCierre;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}
  
  private void limpia(){
		idCuenta = "";
		banco = "";
		tipoPertenencia = "";
		numSucursal = "";
		moneda = "";
		saldoCierre = "";
		fecha = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
   //   while (rsQuerySeq.next()){
     //   TODO: // Asignar llave compuesta
    //  } //del while

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
			SQL.append("  ID_CUENTA, \n");
			SQL.append("  BANCO, \n");
			SQL.append("  TIPO_PERTENENCIA, \n");
			SQL.append("  NUM_SUCURSAL, \n");
			SQL.append("  MONEDA, \n");
			SQL.append("  SALDO_CIERRE, \n");
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_SALDOS_RESERVA\n");
			SQL.append("where \n");
			SQL.append("  FECHA = to_date('" + fecha + "','dd/mm/yyyy') and ");
			SQL.append("  ID_CUENTA = " + idCuenta + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idCuenta = rsQuery.getString("ID_CUENTA")==null?"":rsQuery.getString("ID_CUENTA");
              banco = rsQuery.getString("BANCO")==null?"":rsQuery.getString("BANCO");
              tipoPertenencia = rsQuery.getString("TIPO_PERTENENCIA")==null?"":rsQuery.getString("TIPO_PERTENENCIA");
              numSucursal = rsQuery.getString("NUM_SUCURSAL")==null?"":rsQuery.getString("NUM_SUCURSAL");
              moneda = rsQuery.getString("MONEDA")==null?"":rsQuery.getString("MONEDA");
              saldoCierre = rsQuery.getString("SALDO_CIERRE")==null?"":rsQuery.getString("SALDO_CIERRE");
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrSaldosReserva.select: " + SQL.toString());
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
      SQL.append("insert into RF_TR_SALDOS_RESERVA ( ");
      SQL.append("ID_CUENTA, ");
      SQL.append("BANCO, ");
      SQL.append("TIPO_PERTENENCIA, ");
      SQL.append("NUM_SUCURSAL, ");
      SQL.append("MONEDA, ");
      SQL.append("SALDO_CIERRE, ");
      SQL.append("FECHA ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idCuenta)+", ");
      SQL.append(getString(banco)+", ");
      SQL.append(getString(tipoPertenencia)+", ");
      SQL.append(getString(numSucursal)+", ");
      SQL.append(getString(moneda)+", ");
      SQL.append(getCampo(saldoCierre)+", ");
      SQL.append(getDate(fecha)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrSaldosReserva.insert: " + SQL);
      fecha = "-1";
      idCuenta = "-1";

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
       SQL.append("update RF_TR_SALDOS_RESERVA set \n");
       SQL.append("  ID_CUENTA = " + getCampo(idCuenta)+", \n");
       SQL.append("  BANCO = " + getString(banco)+", \n");
       SQL.append("  TIPO_PERTENENCIA = " + getString(tipoPertenencia)+", \n");
       SQL.append("  NUM_SUCURSAL = " + getString(numSucursal)+", \n");
       SQL.append("  MONEDA = " + getString(moneda)+", \n");
       SQL.append("  SALDO_CIERRE = " + getCampo(saldoCierre)+", \n");
       SQL.append("  FECHA = " + getDate(fecha)+" \n");
       SQL.append("where \n");
       SQL.append("  FECHA =  to_date('" + fecha + "','dd/mm/yyyy') and " );
       SQL.append("  ID_CUENTA = " + idCuenta + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrSaldosReserva.update: "+SQL);
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
        SQL = "delete from RF_TR_SALDOS_RESERVA where FECHA = " + fecha + " and ID_CUENTA = " + idCuenta + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrSaldosReserva.delete: "+SQL);         
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
		sb.append(getTipoPertenencia());
		sb.append(",");
		sb.append(getIdCuenta());
		sb.append(",");
		sb.append(getNumSucursal());
		sb.append(",");
		sb.append(getFecha());
		sb.append(",");
		sb.append(getBanco());
		sb.append(",");
		sb.append(getSaldoCierre());
		sb.append(",");
		sb.append(getMoneda());
    return sb.toString();
  }
  
}
