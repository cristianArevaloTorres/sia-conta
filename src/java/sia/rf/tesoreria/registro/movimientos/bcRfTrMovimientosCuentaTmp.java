package sia.rf.tesoreria.registro.movimientos;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentenciasCRS;

public class bcRfTrMovimientosCuentaTmp extends BeanBase {
		
  private String fechaHora;
	private String numCuenta;
	private String idTipoTrans;
	private String claveTrans;
	private String sucursal;
	private String monto;
	private String numeroCheque;
	private String numeroFolio;
	private String operador;
	private String descripcion;
	private String saldo;
	private String tipoSaldo;
	private String referencia;
	private String idMovimiento;
	private String referencia2;
	private String referencia3;
	private String referencia4;
	private String referencia5;
	private String saldocal;
	private String saldoinicial;
  
	public bcRfTrMovimientosCuentaTmp() {
		limpia();
	}
	
  public void setFechaHora(String fechaHora) {
		this.fechaHora = fechaHora;
	}

	public String getFechaHora() {
		return this.fechaHora;
	}

	public void setNumCuenta(String numCuenta) {
		this.numCuenta = numCuenta;
	}

	public String getNumCuenta() {
		return this.numCuenta;
	}

	public void setIdTipoTrans(String idTipoTrans) {
		this.idTipoTrans = idTipoTrans;
	}

	public String getIdTipoTrans() {
		return this.idTipoTrans;
	}

	public void setClaveTrans(String claveTrans) {
		this.claveTrans = claveTrans;
	}

	public String getClaveTrans() {
		return this.claveTrans;
	}

	public void setSucursal(String sucursal) {
		this.sucursal = sucursal;
	}

	public String getSucursal() {
		return this.sucursal;
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

	public void setIdMovimiento(String idMovimiento) {
		this.idMovimiento = idMovimiento;
	}

	public String getIdMovimiento() {
		return this.idMovimiento;
	}

	public void setReferencia2(String referencia2) {
		this.referencia2 = referencia2;
	}

	public String getReferencia2() {
		return this.referencia2;
	}

	public void setReferencia3(String referencia3) {
		this.referencia3 = referencia3;
	}

	public String getReferencia3() {
		return this.referencia3;
	}

	public void setReferencia4(String referencia4) {
		this.referencia4 = referencia4;
	}

	public String getReferencia4() {
		return this.referencia4;
	}

	public void setReferencia5(String referencia5) {
		this.referencia5 = referencia5;
	}

	public String getReferencia5() {
		return this.referencia5;
	}

	public void setSaldocal(String saldocal) {
		this.saldocal = saldocal;
	}

	public String getSaldocal() {
		return this.saldocal;
	}

	public void setSaldoinicial(String saldoinicial) {
		this.saldoinicial = saldoinicial;
	}

	public String getSaldoinicial() {
		return this.saldoinicial;
	}
  
  private void limpia(){
		fechaHora = "";
		numCuenta = "";
		idTipoTrans = "";
		claveTrans = "";
		sucursal = "";
		monto = "";
		numeroCheque = "";
		numeroFolio = "";
		operador = "";
		descripcion = "";
		saldo = "";
		tipoSaldo = "";
		referencia = "";
		idMovimiento = "";
		referencia2 = "";
		referencia3 = "";
		referencia4 = "";
		referencia5 = "";
		saldocal = "";
		saldoinicial = "";

  }
  
  /*private void adecuaCampos() {

  }*/
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_MOVIMIENTOS_CUENTA_TMP.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idMovimiento = rsQuerySeq.getString("valoractual");
      } //del while
      if(idMovimiento == null)
        idMovimiento = "1";

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
			SQL.append("  to_char(FECHA_HORA,'dd/mm/yyyy') FECHA_HORA, \n");
			SQL.append("  NUM_CUENTA, \n");
			SQL.append("  ID_TIPO_TRANS, \n");
			SQL.append("  CLAVE_TRANS, \n");
			SQL.append("  SUCURSAL, \n");
			SQL.append("  MONTO, \n");
			SQL.append("  NUMERO_CHEQUE, \n");
			SQL.append("  NUMERO_FOLIO, \n");
			SQL.append("  OPERADOR, \n");
			SQL.append("  DESCRIPCION, \n");
			SQL.append("  SALDO, \n");
			SQL.append("  TIPO_SALDO, \n");
			SQL.append("  REFERENCIA, \n");
			SQL.append("  ID_MOVIMIENTO, \n");
			SQL.append("  REFERENCIA2, \n");
			SQL.append("  REFERENCIA3, \n");
			SQL.append("  REFERENCIA4, \n");
			SQL.append("  REFERENCIA5, \n");
			SQL.append("  SALDOCAL, \n");
			SQL.append("  SALDOINICIAL \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_MOVIMIENTOS_CUENTA_TMP\n");
			SQL.append("where \n");
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
        
              fechaHora = rsQuery.getString("FECHA_HORA")==null?"":rsQuery.getString("FECHA_HORA");
              numCuenta = rsQuery.getString("NUM_CUENTA")==null?"":rsQuery.getString("NUM_CUENTA");
              idTipoTrans = rsQuery.getString("ID_TIPO_TRANS")==null?"":rsQuery.getString("ID_TIPO_TRANS");
              claveTrans = rsQuery.getString("CLAVE_TRANS")==null?"":rsQuery.getString("CLAVE_TRANS");
              sucursal = rsQuery.getString("SUCURSAL")==null?"":rsQuery.getString("SUCURSAL");
              monto = rsQuery.getString("MONTO")==null?"":rsQuery.getString("MONTO");
              numeroCheque = rsQuery.getString("NUMERO_CHEQUE")==null?"":rsQuery.getString("NUMERO_CHEQUE");
              numeroFolio = rsQuery.getString("NUMERO_FOLIO")==null?"":rsQuery.getString("NUMERO_FOLIO");
              operador = rsQuery.getString("OPERADOR")==null?"":rsQuery.getString("OPERADOR");
              descripcion = rsQuery.getString("DESCRIPCION")==null?"":rsQuery.getString("DESCRIPCION");
              saldo = rsQuery.getString("SALDO")==null?"":rsQuery.getString("SALDO");
              tipoSaldo = rsQuery.getString("TIPO_SALDO")==null?"":rsQuery.getString("TIPO_SALDO");
              referencia = rsQuery.getString("REFERENCIA")==null?"":rsQuery.getString("REFERENCIA");
              idMovimiento = rsQuery.getString("ID_MOVIMIENTO")==null?"":rsQuery.getString("ID_MOVIMIENTO");
              referencia2 = rsQuery.getString("REFERENCIA2")==null?"":rsQuery.getString("REFERENCIA2");
              referencia3 = rsQuery.getString("REFERENCIA3")==null?"":rsQuery.getString("REFERENCIA3");
              referencia4 = rsQuery.getString("REFERENCIA4")==null?"":rsQuery.getString("REFERENCIA4");
              referencia5 = rsQuery.getString("REFERENCIA5")==null?"":rsQuery.getString("REFERENCIA5");
              saldocal = rsQuery.getString("SALDOCAL")==null?"":rsQuery.getString("SALDOCAL");
              saldoinicial = rsQuery.getString("SALDOINICIAL")==null?"":rsQuery.getString("SALDOINICIAL");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrMovimientosCuentaTmp.select: " + SQL.toString());
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
      SQL.append("insert into RF_TR_MOVIMIENTOS_CUENTA_TMP ( ");
      SQL.append("FECHA_HORA, ");
      SQL.append("NUM_CUENTA, ");
      SQL.append("ID_TIPO_TRANS, ");
      SQL.append("CLAVE_TRANS, ");
      SQL.append("SUCURSAL, ");
      SQL.append("MONTO, ");
      SQL.append("NUMERO_CHEQUE, ");
      SQL.append("NUMERO_FOLIO, ");
      SQL.append("OPERADOR, ");
      SQL.append("DESCRIPCION, ");
      SQL.append("SALDO, ");
      SQL.append("TIPO_SALDO, ");
      SQL.append("REFERENCIA, ");
      SQL.append("ID_MOVIMIENTO, ");
      SQL.append("REFERENCIA2, ");
      SQL.append("REFERENCIA3, ");
      SQL.append("REFERENCIA4, ");
      SQL.append("REFERENCIA5, ");
      SQL.append("SALDOCAL, ");
      SQL.append("SALDOINICIAL ");
      SQL.append("\n) values (");
      SQL.append(getDate(fechaHora)+", ");
      SQL.append(getString(numCuenta)+", ");
      SQL.append(getString(idTipoTrans)+", ");
      SQL.append(getString(claveTrans)+", ");
      SQL.append(getString(sucursal)+", ");
      SQL.append(getCampo(monto)+", ");
      SQL.append(getCampo(numeroCheque)+", ");
      SQL.append(getCampo(numeroFolio)+", ");
      SQL.append(getString(operador)+", ");
      SQL.append(getString(descripcion)+", ");
      SQL.append(getCampo(saldo)+", ");
      SQL.append(getString(tipoSaldo)+", ");
      SQL.append(getString(referencia)+", ");
      SQL.append(getCampo(idMovimiento)+", ");
      SQL.append(getString(referencia2)+", ");
      SQL.append(getString(referencia3)+", ");
      SQL.append(getString(referencia4)+", ");
      SQL.append(getString(referencia5)+", ");
      SQL.append(getCampo(saldocal)+", ");
      SQL.append(getCampo(saldoinicial)+")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrMovimientosCuentaTmp.insert: " + SQL);
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
       SQL.append("update RF_TR_MOVIMIENTOS_CUENTA_TMP set \n");
       SQL.append("  FECHA_HORA = " + getDate(fechaHora)+", \n");
       SQL.append("  NUM_CUENTA = " + getString(numCuenta)+", \n");
       SQL.append("  ID_TIPO_TRANS = " + getString(idTipoTrans)+", \n");
       SQL.append("  CLAVE_TRANS = " + getString(claveTrans)+", \n");
       SQL.append("  SUCURSAL = " + getString(sucursal)+", \n");
       SQL.append("  MONTO = " + getCampo(monto)+", \n");
       SQL.append("  NUMERO_CHEQUE = " + getCampo(numeroCheque)+", \n");
       SQL.append("  NUMERO_FOLIO = " + getCampo(numeroFolio)+", \n");
       SQL.append("  OPERADOR = " + getString(operador)+", \n");
       SQL.append("  DESCRIPCION = " + getString(descripcion)+", \n");
       SQL.append("  SALDO = " + getCampo(saldo)+", \n");
       SQL.append("  TIPO_SALDO = " + getString(tipoSaldo)+", \n");
       SQL.append("  REFERENCIA = " + getString(referencia)+", \n");
       SQL.append("  ID_MOVIMIENTO = " + getCampo(idMovimiento)+", \n");
       SQL.append("  REFERENCIA2 = " + getString(referencia2)+", \n");
       SQL.append("  REFERENCIA3 = " + getString(referencia3)+", \n");
       SQL.append("  REFERENCIA4 = " + getString(referencia4)+", \n");
       SQL.append("  REFERENCIA5 = " + getString(referencia5)+", \n");
       SQL.append("  SALDOCAL = " + getCampo(saldocal)+", \n");
       SQL.append("  SALDOINICIAL = " + getCampo(saldoinicial)+" \n");
       SQL.append("where \n");
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
          System.out.println("bcRfTrMovimientosCuentaTmp.update: "+SQL);
          throw new Exception(SQL.toString(),e);
       }
       finally {
          if (stQuery != null) {
             stQuery.close();
             stQuery=null;
          }           
       }
  }
  
  public void eliminaRegDuplicados(Connection con) throws  Exception {
      SentenciasCRS sentenciasCRS = null;
      try  {
        sentenciasCRS =  new SentenciasCRS();
        sentenciasCRS.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.idRegDuplicados.bancas");
        sentenciasCRS.next();
        while (sentenciasCRS.next()){
            setIdMovimiento(sentenciasCRS.getStr("id_movimiento"));
            delete(con);
        }
      } catch (Exception ex)  {
          ex.printStackTrace();
          throw new Exception("Error al eliminar registros duplicados en la tabla tmp de movimientos",ex);
      } finally  {
          sentenciasCRS = null;
      }
  }
  
  public void delete(Connection con) throws  Exception {
      int rs=-1;
      Statement stQuery=null;
      String SQL = "";
      try {
        stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        SQL = "delete from RF_TR_MOVIMIENTOS_CUENTA_TMP where ID_MOVIMIENTO = " + idMovimiento + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrMovimientosCuentaTmp.delete: "+SQL);         
        throw new Exception(SQL.toString(),e);
      }
      finally {                  
        if (stQuery != null) {
          stQuery.close();
          stQuery=null;
        }                         
      }
  }
  
    public void deleteInicio(Connection con) throws  Exception {
        int rs=-1;
        Statement stQuery=null;
        String SQL = "";
        try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          SQL = "delete from RF_TR_MOVIMIENTOS_CUENTA_TMP ";
          rs=stQuery.executeUpdate(SQL);
        }
       catch (Exception e) {
          System.out.println("Ha occurrido un error en el metodo deleteInicio");
          System.out.println("bcRfTrMovimientosCuentaTmp.deleteInicio: "+SQL);         
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
		sb.append(getReferencia3());
		sb.append(",");
		sb.append(getIdTipoTrans());
		sb.append(",");
		sb.append(getReferencia2());
		sb.append(",");
		sb.append(getReferencia5());
		sb.append(",");
		sb.append(getNumCuenta());
		sb.append(",");
		sb.append(getTipoSaldo());
		sb.append(",");
		sb.append(getReferencia4());
		sb.append(",");
		sb.append(getClaveTrans());
		sb.append(",");
		sb.append(getSucursal());
		sb.append(",");
		sb.append(getDescripcion());
		sb.append(",");
		sb.append(getSaldo());
		sb.append(",");
		sb.append(getFechaHora());
		sb.append(",");
		sb.append(getOperador());
		sb.append(",");
		sb.append(getNumeroCheque());
		sb.append(",");
		sb.append(getNumeroFolio());
		sb.append(",");
		sb.append(getReferencia());
		sb.append(",");
		sb.append(getSaldocal());
		sb.append(",");
		sb.append(getIdMovimiento());
		sb.append(",");
		sb.append(getMonto());
		sb.append(",");
		sb.append(getSaldoinicial());
    return sb.toString();
  }
  
}
