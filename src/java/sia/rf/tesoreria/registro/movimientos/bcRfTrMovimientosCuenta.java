package sia.rf.tesoreria.registro.movimientos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrMovimientosCuenta extends BeanBase {
		
  private String idCuenta;
	private String idMovimiento;
	private String fechaHora;
	private String sucursal;
	private String claveTrans;
	private String monto;
	private String numeroCheque;
	private String numeroFolio;
	private String operador;
	private String descripcion;
	private String saldo;
	private String tipoSaldo;
	private String referencia;
	private String idTipoTrans;
	private String claveTransRecla;
	private String idClaveTrans;
	private String idClaveTransRecla;
	private String borrar;
	private String saldocalculado;
	private String stcontable;
	private String referenciaAnt;
  
	public bcRfTrMovimientosCuenta() {
		limpia();
	}
	
  public void setIdCuenta(String idCuenta) {
		this.idCuenta = idCuenta;
	}

	public String getIdCuenta() {
		return this.idCuenta;
	}

	public void setIdMovimiento(String idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getIdMovimiento() {
		return this.idMovimiento;
	}

	public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getFechaHora() {
		return this.fechaHora;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getSucursal() {
		return this.sucursal;
	}

	public void setClaveTrans(String claveTrans) {
		this.claveTrans = claveTrans;
	}

	public String getClaveTrans() {
		return this.claveTrans;
	}

	public void setMonto(String monto) {
		this.monto = monto;
	}

	public String getMonto() {
		return this.monto;
	}

	public void setNumeroCheque(String numeroCheque) {
		this.numeroCheque = numeroCheque;
	}

	public String getNumeroCheque() {
		return this.numeroCheque;
	}

	public void setNumeroFolio(String numeroFolio) {
		this.numeroFolio = numeroFolio;
	}

	public String getNumeroFolio() {
		return this.numeroFolio;
	}

	public void setOperador(String operador) {
		this.operador = operador;
	}

	public String getOperador() {
		return this.operador;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getDescripcion() {
		return this.descripcion;
	}

	public void setSaldo(String saldo) {
		this.saldo = saldo;
	}

	public String getSaldo() {
		return this.saldo;
	}

	public void setTipoSaldo(String tipoSaldo) {
		this.tipoSaldo = tipoSaldo;
	}

	public String getTipoSaldo() {
		return this.tipoSaldo;
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getReferencia() {
		return this.referencia;
	}

	public void setIdTipoTrans(String idTipoTrans) {
		this.idTipoTrans = idTipoTrans;
	}

	public String getIdTipoTrans() {
		return this.idTipoTrans;
	}

	public void setClaveTransRecla(String claveTransRecla) {
		this.claveTransRecla = claveTransRecla;
	}

	public String getClaveTransRecla() {
		return this.claveTransRecla;
	}

	public void setIdClaveTrans(String idClaveTrans) {
		this.idClaveTrans = idClaveTrans;
	}

	public String getIdClaveTrans() {
		return this.idClaveTrans;
	}

	public void setIdClaveTransRecla(String idClaveTransRecla) {
		this.idClaveTransRecla = idClaveTransRecla;
	}

	public String getIdClaveTransRecla() {
		return this.idClaveTransRecla;
	}

	public void setBorrar(String borrar) {
		this.borrar = borrar;
	}

	public String getBorrar() {
		return this.borrar;
	}

	public void setSaldocalculado(String saldocalculado) {
		this.saldocalculado = saldocalculado;
	}

	public String getSaldocalculado() {
		return this.saldocalculado;
	}

	public void setStcontable(String stcontable) {
		this.stcontable = stcontable;
	}

	public String getStcontable() {
		return this.stcontable;
	}

	public void setReferenciaAnt(String referenciaAnt) {
		this.referenciaAnt = referenciaAnt;
	}

	public String getReferenciaAnt() {
		return this.referenciaAnt;
	}
  
  private void limpia(){
		idCuenta = "";
		idMovimiento = "";
		fechaHora = "";
		sucursal = "";
		claveTrans = "";
		monto = "";
		numeroCheque = "";
		numeroFolio = "";
		operador = "";
		descripcion = "";
		saldo = "";
		tipoSaldo = "";
		referencia = "";
		idTipoTrans = "";
		claveTransRecla = "";
		idClaveTrans = "";
		idClaveTransRecla = "";
		borrar = "";
		saldocalculado = "";
		stcontable = "";
		referenciaAnt = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_MOVIMIENTOS_CUENTA.NEXTVAL valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
          setIdMovimiento(rsQuerySeq.getString("valoractual"));
      } //del while

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
			SQL.append("  ID_MOVIMIENTO, \n");
			SQL.append("  to_char(FECHA_HORA,'dd/mm/yyyy') FECHA_HORA, \n");
			SQL.append("  SUCURSAL, \n");
			SQL.append("  CLAVE_TRANS, \n");
			SQL.append("  MONTO, \n");
			SQL.append("  NUMERO_CHEQUE, \n");
			SQL.append("  NUMERO_FOLIO, \n");
			SQL.append("  OPERADOR, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  SALDO, \n");
			SQL.append("  TIPO_SALDO, \n");
			SQL.append("  REFERENCIA, \n");
			SQL.append("  ID_TIPO_TRANS, \n");
			SQL.append("  CLAVE_TRANS_RECLA, \n");
			SQL.append("  ID_CLAVE_TRANS, \n");
			SQL.append("  ID_CLAVE_TRANS_RECLA, \n");
			SQL.append("  BORRAR, \n");
			SQL.append("  SALDOCALCULADO, \n");
			SQL.append("  STCONTABLE, \n");
			SQL.append("  REFERENCIA_ANT \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_MOVIMIENTOS_CUENTA\n");
			SQL.append("where \n");
			SQL.append("  ID_CUENTA = " + idCuenta + " and ");
			SQL.append("  ID_MOVIMIENTO = " + idMovimiento + "  ");

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
              idMovimiento = rsQuery.getString("ID_MOVIMIENTO")==null?"":rsQuery.getString("ID_MOVIMIENTO");
              fechaHora = rsQuery.getString("FECHA_HORA")==null?"":rsQuery.getString("FECHA_HORA");
              sucursal = rsQuery.getString("SUCURSAL")==null?"":rsQuery.getString("SUCURSAL");
              claveTrans = rsQuery.getString("CLAVE_TRANS")==null?"":rsQuery.getString("CLAVE_TRANS");
              monto = rsQuery.getString("MONTO")==null?"":rsQuery.getString("MONTO");
              numeroCheque = rsQuery.getString("NUMERO_CHEQUE")==null?"":rsQuery.getString("NUMERO_CHEQUE");
              numeroFolio = rsQuery.getString("NUMERO_FOLIO")==null?"":rsQuery.getString("NUMERO_FOLIO");
              operador = rsQuery.getString("OPERADOR")==null?"":rsQuery.getString("OPERADOR");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              saldo = rsQuery.getString("SALDO")==null?"":rsQuery.getString("SALDO");
              tipoSaldo = rsQuery.getString("TIPO_SALDO")==null?"":rsQuery.getString("TIPO_SALDO");
              referencia = rsQuery.getString("REFERENCIA")==null?"":rsQuery.getString("REFERENCIA");
              idTipoTrans = rsQuery.getString("ID_TIPO_TRANS")==null?"":rsQuery.getString("ID_TIPO_TRANS");
              claveTransRecla = rsQuery.getString("CLAVE_TRANS_RECLA")==null?"":rsQuery.getString("CLAVE_TRANS_RECLA");
              idClaveTrans = rsQuery.getString("ID_CLAVE_TRANS")==null?"":rsQuery.getString("ID_CLAVE_TRANS");
              idClaveTransRecla = rsQuery.getString("ID_CLAVE_TRANS_RECLA")==null?"":rsQuery.getString("ID_CLAVE_TRANS_RECLA");
              borrar = rsQuery.getString("BORRAR")==null?"":rsQuery.getString("BORRAR");
              saldocalculado = rsQuery.getString("SALDOCALCULADO")==null?"":rsQuery.getString("SALDOCALCULADO");
              stcontable = rsQuery.getString("STCONTABLE")==null?"":rsQuery.getString("STCONTABLE");
              referenciaAnt = rsQuery.getString("REFERENCIA_ANT")==null?"":rsQuery.getString("REFERENCIA_ANT");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrMovimientosCuenta.select: " + SQL.toString());
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
      SQL.append("insert into RF_TR_MOVIMIENTOS_CUENTA ( ");
      SQL.append("ID_CUENTA, ");
      SQL.append("ID_MOVIMIENTO, ");
      SQL.append("FECHA_HORA, ");
      SQL.append("SUCURSAL, ");
      SQL.append("CLAVE_TRANS, ");
      SQL.append("MONTO, ");
      SQL.append("NUMERO_CHEQUE, ");
      SQL.append("NUMERO_FOLIO, ");
      SQL.append("OPERADOR, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("SALDO, ");
      SQL.append("TIPO_SALDO, ");
      SQL.append("REFERENCIA, ");
      SQL.append("ID_TIPO_TRANS, ");
      SQL.append("CLAVE_TRANS_RECLA, ");
      SQL.append("ID_CLAVE_TRANS, ");
      SQL.append("ID_CLAVE_TRANS_RECLA, ");
      SQL.append("BORRAR, ");
      SQL.append("SALDOCALCULADO, ");
      SQL.append("STCONTABLE, ");
      SQL.append("REFERENCIA_ANT ");
      SQL.append("\n) values (");
      SQL.append(getCampo(idCuenta)+", ");
      SQL.append(getCampo(idMovimiento)+", ");
      SQL.append(getDate(fechaHora)+", ");
      SQL.append(getString(sucursal)+", ");
      SQL.append(getString(claveTrans)+", ");
      SQL.append(getCampo(monto)+", ");
      SQL.append(getCampo(numeroCheque)+", ");
      SQL.append(getCampo(numeroFolio)+", ");
      SQL.append(getString(operador)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getCampo(saldo)+", ");
      SQL.append(getString(tipoSaldo)+", ");
      SQL.append(getString(referencia)+", ");
      SQL.append(getString(idTipoTrans)+", ");
      SQL.append(getString(claveTransRecla)+", ");
      SQL.append(getCampo(idClaveTrans)+", ");
      SQL.append(getCampo(idClaveTransRecla)+", ");
      SQL.append(getCampo(borrar)+", ");
      SQL.append(getCampo(saldocalculado)+", ");
      SQL.append(getCampo(stcontable)+", ");
      SQL.append(getString(referenciaAnt)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrMovimientosCuenta.insert: " + SQL);
      idCuenta = "-1";
      idMovimiento = "-1";

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
       SQL.append("update RF_TR_MOVIMIENTOS_CUENTA set \n");
       SQL.append("  ID_CUENTA = " + getCampo(idCuenta)+", \n");
       SQL.append("  ID_MOVIMIENTO = " + getCampo(idMovimiento)+", \n");
       SQL.append("  FECHA_HORA = " + getDate(fechaHora)+", \n");
       SQL.append("  SUCURSAL = " + getString(sucursal)+", \n");
       SQL.append("  CLAVE_TRANS = " + getString(claveTrans)+", \n");
       SQL.append("  MONTO = " + getCampo(monto)+", \n");
       SQL.append("  NUMERO_CHEQUE = " + getCampo(numeroCheque)+", \n");
       SQL.append("  NUMERO_FOLIO = " + getCampo(numeroFolio)+", \n");
       SQL.append("  OPERADOR = " + getString(operador)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  SALDO = " + getCampo(saldo)+", \n");
       SQL.append("  TIPO_SALDO = " + getString(tipoSaldo)+", \n");
       SQL.append("  REFERENCIA = " + getString(referencia)+", \n");
       SQL.append("  ID_TIPO_TRANS = " + getString(idTipoTrans)+", \n");
       SQL.append("  CLAVE_TRANS_RECLA = " + getString(claveTransRecla)+", \n");
       SQL.append("  ID_CLAVE_TRANS = " + getCampo(idClaveTrans)+", \n");
       SQL.append("  ID_CLAVE_TRANS_RECLA = " + getCampo(idClaveTransRecla)+", \n");
       SQL.append("  BORRAR = " + getCampo(borrar)+", \n");
       SQL.append("  SALDOCALCULADO = " + getCampo(saldocalculado)+", \n");
       SQL.append("  STCONTABLE = " + getCampo(stcontable)+", \n");
       SQL.append("  REFERENCIA_ANT = " + getString(referenciaAnt)+" \n");
       SQL.append("where \n");
       SQL.append("  ID_CUENTA = " + idCuenta + "  and " );
       SQL.append("  ID_MOVIMIENTO = " + idMovimiento + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrMovimientosCuenta.update: "+SQL);
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
        SQL = "delete from RF_TR_MOVIMIENTOS_CUENTA where ID_CUENTA = " + idCuenta + " and ID_MOVIMIENTO = " + idMovimiento + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrMovimientosCuenta.delete: "+SQL);         
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
		sb.append(getIdTipoTrans());
		sb.append(",");
		sb.append(getIdCuenta());
		sb.append(",");
		sb.append(getTipoSaldo());
		sb.append(",");
		sb.append(getStcontable());
		sb.append(",");
		sb.append(getSucursal());
		sb.append(",");
		sb.append(getClaveTrans());
		sb.append(",");
		sb.append(getClaveTransRecla());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getSaldo());
		sb.append(",");
		sb.append(getBorrar());
		sb.append(",");
		sb.append(getSaldocalculado());
		sb.append(",");
		sb.append(getFechaHora());
		sb.append(",");
		sb.append(getOperador());
		sb.append(",");
		sb.append(getIdClaveTrans());
		sb.append(",");
		sb.append(getNumeroCheque());
		sb.append(",");
		sb.append(getNumeroFolio());
		sb.append(",");
		sb.append(getReferencia());
		sb.append(",");
		sb.append(getIdMovimiento());
		sb.append(",");
		sb.append(getReferenciaAnt());
		sb.append(",");
		sb.append(getIdClaveTransRecla());
		sb.append(",");
		sb.append(getMonto());
    return sb.toString();
  }
  
}
