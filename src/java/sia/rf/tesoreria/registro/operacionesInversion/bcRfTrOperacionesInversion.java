package sia.rf.tesoreria.registro.operacionesInversion;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentenciasCRS;

import sia.libs.formato.Numero;

public class bcRfTrOperacionesInversion extends BeanBase {
		
  private String idOperacion;
	private String idCuentaInversion;
	private String idTipoOperacion;
	private String fecha;
	private String importe;
	private String estatus;
	private String saldoAnterior;
	private String saldoActual;
	private String fechaRegistro;
	private String saldoReal;
    
    private String afectacion;
    private int afectaSaldoReal;
  
	public bcRfTrOperacionesInversion() {
		limpia();
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

	public void setIdTipoOperacion(String idTipoOperacion) {
		this.idTipoOperacion = idTipoOperacion;
	}

	public String getIdTipoOperacion() {
		return this.idTipoOperacion;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setImporte(double importe) {
		this.importe = Numero.formatear(Numero.NUMERO_DECIMALES,importe) ;
	}
    
    public void setImporte(String importe) {
        this.importe = quitarComas(importe);
    }

	public String getImporte() {
		return this.importe;
	}

	public void setEstatus(String estatus) {
		this.estatus = estatus;
	}

	public String getEstatus() {
		return this.estatus;
	}

	public void setSaldoAnterior(double saldoAnterior) {
		this.saldoAnterior = Numero.formatear(Numero.NUMERO_DECIMALES,saldoAnterior);
	}

	public String getSaldoAnterior() {
		return this.saldoAnterior;
	}

	public void setSaldoActual(double saldoActual) {
		this.saldoActual = Numero.formatear(Numero.NUMERO_DECIMALES,saldoActual);
	}

	public String getSaldoActual() {
		return this.saldoActual;
	}

	public void setFechaRegistro(String fechaRegistro) {
		this.fechaRegistro = fechaRegistro.replaceAll("\\.",":");
	}

	public String getFechaRegistro() {
		return this.fechaRegistro;
	}

	public void setSaldoReal(double saldoReal) {
		this.saldoReal = Numero.formatear(Numero.NUMERO_DECIMALES,saldoReal);
	}

	public String getSaldoReal() {
		return this.saldoReal;
	}
  
  private void limpia(){
		idOperacion = "";
		idCuentaInversion = "";
		idTipoOperacion = "";
		fecha = "";
		importe = "";
		estatus = "";
		saldoAnterior = "";
		saldoActual = "";
		fechaRegistro = "";
		saldoReal = "";

  }
  
  private void adecuaCampos() {
		idOperacion = ((idOperacion==null)||(idOperacion.equals("")))? "null" : idOperacion;
		idCuentaInversion = ((idCuentaInversion==null)||(idCuentaInversion.equals("")))? "null" : idCuentaInversion;
		idTipoOperacion = ((idTipoOperacion==null)||(idTipoOperacion.equals("")))? "null" : idTipoOperacion;
		fecha = ((fecha==null)||(fecha.equals("")))? "null" : "to_date('" + fecha + "','dd/mm/yyyy')";
		importe = ((importe==null)||(importe.equals("")))? "null" : importe;
		estatus = ((estatus==null)||(estatus.equals("")))? "null" : estatus;
		saldoAnterior = ((saldoAnterior==null)||(saldoAnterior.equals("")))? "null" : saldoAnterior;
		saldoActual = ((saldoActual==null)||(saldoActual.equals("")))? "null" : saldoActual;
		fechaRegistro = ((fechaRegistro==null)||(fechaRegistro.equals("")))? "null" : "to_date('" + fechaRegistro + "','dd/mm/yyyy hh24:mi:ss')";
		saldoReal = ((saldoReal==null)||(saldoReal.equals("")))? "null" : saldoReal;

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select SEQ_TR_OPERACIONES_INVERSION.nextval valoractual from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
      while (rsQuerySeq.next()){
        idOperacion = rsQuerySeq.getString("valoractual");
      } //del while
      if(idOperacion == null)
        idOperacion = "1";

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
			SQL.append("  ID_OPERACION, \n");
			SQL.append("  ID_CUENTA_INVERSION, \n");
			SQL.append("  ID_TIPO_OPERACION, \n");
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  IMPORTE, \n");
			SQL.append("  ESTATUS, \n");
			SQL.append("  SALDO_ANTERIOR, \n");
			SQL.append("  SALDO_ACTUAL, \n");
			SQL.append("  to_char(FECHA_REGISTRO,'dd/mm/yyyy hh24:mi:ss') FECHA_REGISTRO, \n");
			SQL.append("  SALDO_REAL \n");
			SQL.append("from \n"); 
			SQL.append("  rf_tr_operaciones_inversion\n");
			SQL.append("where \n");
			SQL.append("  ID_OPERACION = " + idOperacion + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              idOperacion = rsQuery.getString("ID_OPERACION")==null?"":rsQuery.getString("ID_OPERACION");
              idCuentaInversion = rsQuery.getString("ID_CUENTA_INVERSION")==null?"":rsQuery.getString("ID_CUENTA_INVERSION");
              idTipoOperacion = rsQuery.getString("ID_TIPO_OPERACION")==null?"":rsQuery.getString("ID_TIPO_OPERACION");
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              importe = rsQuery.getString("IMPORTE")==null?"":rsQuery.getString("IMPORTE");
              estatus = rsQuery.getString("ESTATUS")==null?"":rsQuery.getString("ESTATUS");
              saldoAnterior = rsQuery.getString("SALDO_ANTERIOR")==null?"":rsQuery.getString("SALDO_ANTERIOR");
              saldoActual = rsQuery.getString("SALDO_ACTUAL")==null?"":rsQuery.getString("SALDO_ACTUAL");
              fechaRegistro = rsQuery.getString("FECHA_REGISTRO")==null?"":rsQuery.getString("FECHA_REGISTRO");
              saldoReal = rsQuery.getString("SALDO_REAL")==null?"":rsQuery.getString("SALDO_REAL");

      }
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrOperacionesInversion.select: " + SQL.toString());
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
      SQL.append("insert into rf_tr_operaciones_inversion ( ");
      SQL.append("ID_OPERACION, ");
      SQL.append("ID_CUENTA_INVERSION, ");
      SQL.append("ID_TIPO_OPERACION, ");
      SQL.append("FECHA, ");
      SQL.append("IMPORTE, ");
      SQL.append("ESTATUS, ");
      SQL.append("SALDO_ANTERIOR, ");
      SQL.append("SALDO_ACTUAL, ");
      SQL.append("FECHA_REGISTRO, ");
      SQL.append("SALDO_REAL ");
      SQL.append("\n) values (");
      SQL.append(idOperacion + ", ");
      SQL.append(idCuentaInversion + ", ");
      SQL.append(idTipoOperacion + ", ");
      SQL.append(fecha + ", ");
      SQL.append(importe + ", ");
      SQL.append(estatus + ", ");
      SQL.append(saldoAnterior + ", ");
      SQL.append(saldoActual + ", ");
      SQL.append(fechaRegistro + ", ");
      SQL.append(saldoReal + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrOperacionesInversion.insert: " + SQL);
      idOperacion = "-1";

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
       SQL.append("update rf_tr_operaciones_inversion set \n");
       SQL.append("  ID_OPERACION = " + idOperacion + " , \n");
       SQL.append("  ID_CUENTA_INVERSION = " + idCuentaInversion + " , \n");
       SQL.append("  ID_TIPO_OPERACION = " + idTipoOperacion + " , \n");
       SQL.append("  FECHA = " + fecha + " , \n");
       SQL.append("  IMPORTE = " + importe + " , \n");
       SQL.append("  ESTATUS = " + estatus + " , \n");
       SQL.append("  SALDO_ANTERIOR = " + saldoAnterior + " , \n");
       SQL.append("  SALDO_ACTUAL = " + saldoActual + " , \n");
       SQL.append("  FECHA_REGISTRO = " + fechaRegistro + " , \n");
       SQL.append("  SALDO_REAL = " + saldoReal + "  \n");
       SQL.append("where \n");
       SQL.append("  ID_OPERACION = " + idOperacion + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrOperacionesInversion.update: "+SQL);
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
        SQL = "delete from rf_tr_operaciones_inversion where ID_OPERACION = " + idOperacion + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrOperacionesInversion.delete: "+SQL);         
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
		sb.append(getIdOperacion());
		sb.append(",");
		sb.append(getSaldoAnterior());
		sb.append(",");
		sb.append(getIdTipoOperacion());
		sb.append(",");
		sb.append(getFechaRegistro());
		sb.append(",");
		sb.append(getEstatus());
		sb.append(",");
		sb.append(getIdCuentaInversion());
		sb.append(",");
		sb.append(getSaldoReal());
		sb.append(",");
		sb.append(getImporte());
		sb.append(",");
		sb.append(getSaldoActual());
    return sb.toString();
  }
  
  private int getTipoOper() {
    int regresa = 1;
    if(getAfectacion().equals("A"))
      regresa = -1;
    return regresa;
  }
  
  private boolean getAfectaSReal() {
    boolean regresa = false;
    if(getAfectaSaldoReal() == 1)
      regresa = true;
    return regresa;
  }
  
  public String getDetalleMensaje() {
    StringBuffer sb = new StringBuffer();
    sb.append("<br>Sobrepasado en la fecha:").append(getFecha());
    sb.append("<br>El saldo disponible quedaria en:").append(Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES, Double.parseDouble(getSaldoActual())));
    sb.append("<br>El saldo real quedaria en:").append(Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES, Double.parseDouble(getSaldoReal())));
    return sb.toString();
  }
  
  public double addImporte(double saldoRealAnt) throws Exception {
    
    double saldoAct = (red(getSaldoAnterior(),3) + red((red(importe,3))*(getTipoOper()),3) );
    double saldoRea = getAfectaSReal() ? (saldoRealAnt + ((Double.parseDouble(importe))*(getTipoOper())) ) : saldoRealAnt ;
    setSaldoActual(saldoAct);
    setSaldoReal(saldoRea);
    //if(saldoAct < 0)
      //throw new Exception("El saldo disponible no puede ser menor a 0".concat(getDetalleMensaje()));    
    if(saldoRea < 0)
      throw new Exception("El saldo real no puede ser menor a 0".concat(getDetalleMensaje())); 
    return saldoRea;
  }

  public void setAfectacion(String afectacion) {
    this.afectacion = afectacion;
  }

  public String getAfectacion() {
    return afectacion;
  }

  public void setAfectaSaldoReal(int afectaSaldoReal) {
    this.afectaSaldoReal = afectaSaldoReal;
  }

  public int getAfectaSaldoReal() {
    return afectaSaldoReal;
  }
}
