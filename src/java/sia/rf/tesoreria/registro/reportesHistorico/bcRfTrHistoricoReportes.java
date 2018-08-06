package sia.rf.tesoreria.registro.reportesHistorico;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.List;
import sia.libs.formato.Error;
import sia.configuracion.BeanBase;

public class bcRfTrHistoricoReportes extends BeanBase {
		
  private String reporte;
	private String fecha;
	private String consecutivo;
	private String informacion;
        
        /// datos historicos
        private String hisFecha;
        private String hisImporte;
        private String hisTipoFolio;
        private String hisIdDirige;
        private String hisConcepto;
        private List<bcRfTrHistoricoReportesMovimiento> movimientos;
        private String hisOnservaciones;
        private String hisIdAutorizo;
        private String hisIdReviso;
        private String hisIdElaboro;
        private String hisFolio;
        private String hisValida;
  
	public bcRfTrHistoricoReportes() {
		limpia();
	}
	
  public void setReporte(String reporte) {
		this.reporte = reporte;
	}

	public String getReporte() {
		return this.reporte;
	}

	public void setFecha(String fecha) {
		this.fecha = fecha;
	}

	public String getFecha() {
		return this.fecha;
	}

	public void setConsecutivo(String consecutivo) {
		this.consecutivo = consecutivo;
	}

	public String getConsecutivo() {
		return this.consecutivo;
	}

	public void setInformacion(String informacion) {
		this.informacion = informacion;
	}

	public String getInformacion() {
		return this.informacion;
	}
  
  private void limpia(){
		reporte = "";
		fecha = "";
		consecutivo = "";
		informacion = "";

  }
  
  private void adecuaCampos() {
		reporte = ((reporte==null)||(reporte.equals("")))? "null" : reporte;
		fecha = ((fecha==null)||(fecha.equals("")))? "null" : "to_date('" + fecha + "','dd/mm/yyyy')";
		consecutivo = ((consecutivo==null)||(consecutivo.equals("")))? "null" : "'" +consecutivo+ "'";
		informacion = ((informacion==null)||(informacion.equals("")))? "null" : "'" +informacion+ "'";

  }
  
  private void getNext(Connection con) throws  Exception {
    Statement stQuery=null;
    StringBuffer SQL = null;
    ResultSet rsQuerySeq = null;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      SQL = new StringBuffer("select 1 valoracutal from dual");
      rsQuerySeq = stQuery.executeQuery(SQL.toString());
   /*   while (rsQuerySeq.next()){
        TODO:// Asignar llave compuesta
      } //del while*/

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
			SQL.append("  REPORTE, \n");
			SQL.append("  to_char(FECHA,'dd/mm/yyyy') FECHA, \n");
			SQL.append("  CONSECUTIVO, \n");
			SQL.append("  INFORMACION \n");
			SQL.append("from \n"); 
			SQL.append("  RF_TR_HISTORICO_REPORTES\n");
			SQL.append("where \n");
			SQL.append("  CONSECUTIVO = '" + consecutivo + "' and ");
			SQL.append(" trunc(FECHA) = to_date('" + fecha + "','dd/mm/yyyy') and ");
			SQL.append("  REPORTE = " + reporte + "  ");

    Statement stQuery = null;
    ResultSet rsQuery = null;
    int resultado = 0;
    try {
      stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      rsQuery = stQuery.executeQuery(SQL.toString());
      
      rsQuery.beforeFirst();
      if (rsQuery.next() )  {
        
        limpia();
        
              reporte = rsQuery.getString("REPORTE")==null?"":rsQuery.getString("REPORTE");
              fecha = rsQuery.getString("FECHA")==null?"":rsQuery.getString("FECHA");
              consecutivo = rsQuery.getString("CONSECUTIVO")==null?"":rsQuery.getString("CONSECUTIVO");
              informacion = rsQuery.getString("INFORMACION")==null?"":rsQuery.getString("INFORMACION");

      }
      divideInformacion();
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo select");
      System.out.println("bcRfTrHistoricoReportes.select: " + SQL.toString());
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
  
  private void divideInformacion() {
    String[] campos    = null; 
    String[] renglones = null;
    String[] camposMovimiento = null;
    bcRfTrHistoricoReportesMovimiento movimiento = null;
    movimientos = new ArrayList();
      if(informacion!=null && !informacion.equals("")) {
        campos = informacion.split("~");
        setHisFecha(campos[0]);
        setHisImporte(campos[1]);
        setHisTipoFolio(campos[2]);
        setHisIdDirige(campos[3]);
        setHisConcepto(campos[4]);
        renglones = campos[5].split("\\|");
        for(String renglon : renglones) {
          movimiento = new bcRfTrHistoricoReportesMovimiento();
          camposMovimiento = renglon.split(",");
          movimiento.setNumTransferencia(camposMovimiento[0]);
          movimiento.setTipoMovimiento(camposMovimiento[1]);
          movimiento.setIdCuentaBancaria(camposMovimiento[2]);
          movimiento.setMonto(camposMovimiento[3]);
          movimiento.setReferencia(camposMovimiento[4]);
          movimiento.setTipoCuenta(camposMovimiento[5]);
          movimientos.add(movimiento);
        }
        setHisOnservaciones(campos[6]);
        setHisIdAutorizo(campos[7]);
        setHisIdReviso(campos[8]);  
        setHisIdElaboro(campos[9]);
        setHisFolio(campos[10]);
             try {
                 SimpleDateFormat sdf= new SimpleDateFormat("dd/MM/yyyy");
                 java.util.Date fechaDate = sdf.parse(getFecha());
                 java.util.Date fechaModificacion = sdf.parse("31/03/2012");
                 if (fechaDate.after(fechaModificacion))
                     setHisValida(campos[11]);
                 else 
                    setHisValida("1");
             }
             catch (Exception e) {
                 Error.mensaje(e,"Tesoreria");
             }
        
      }
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
      SQL.append("insert into RF_TR_HISTORICO_REPORTES ( ");
      SQL.append("REPORTE, ");
      SQL.append("FECHA, ");
      SQL.append("CONSECUTIVO, ");
      SQL.append("INFORMACION ");
      SQL.append("\n) values (");
      SQL.append(reporte + ", ");
      SQL.append(fecha + ", ");
      SQL.append(consecutivo + ", ");
      SQL.append(informacion + ")  ");
//3
      stQuery2=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stQuery2.executeUpdate(SQL.toString());
    }
    catch (Exception e) {
      System.out.println("Ha ocurrido un error en el metodo insert");
      System.out.println("bcRfTrHistoricoReportes.insert: " + SQL);
      consecutivo = "-1";
      fecha = "-1";
      reporte = "-1";

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
       SQL.append("update RF_TR_HISTORICO_REPORTES set \n");
       SQL.append("  REPORTE = " + reporte + " , \n");
       SQL.append("  FECHA = " + fecha + " , \n");
       SQL.append("  CONSECUTIVO = " + consecutivo + " , \n");
       SQL.append("  INFORMACION = " + informacion + "  \n");
       SQL.append("where \n");
       SQL.append("  CONSECUTIVO = " + consecutivo + " and " );
       SQL.append("  FECHA =  " + fecha + " and " );
       SQL.append("  REPORTE = " + reporte + "   " );
//10
       int regsAfectados =-1;
       Statement stQuery=null;
       try {
          stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          regsAfectados=stQuery.executeUpdate(SQL.toString());
       }
       catch (Exception e) {
          System.out.println("Ha ocurrido un error en el metodo update");
          System.out.println("bcRfTrHistoricoReportes.update: "+SQL);
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
        SQL = "delete from RF_TR_HISTORICO_REPORTES where CONSECUTIVO = " + consecutivo + " and FECHA = " + fecha + " and REPORTE = " + reporte + " " ;
        rs=stQuery.executeUpdate(SQL);
      }
     catch (Exception e) {
        System.out.println("Ha occurrido un error en el metodo delete");
        System.out.println("bcRfTrHistoricoReportes.delete: "+SQL);         
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
		sb.append(getReporte());
		sb.append(",");
		sb.append(getInformacion());
		sb.append(",");
		sb.append(getConsecutivo());
    return sb.toString();
  }

    public String get_reporte() {
        return reporte;
    }

    public String get_fecha() {
        return fecha;
    }

    public String get_consecutivo() {
        return consecutivo;
    }

    public String get_informacion() {
        return informacion;
    }

    public void setHisFecha(String hisFecha) {
        this.hisFecha = hisFecha;
    }

    public String getHisFecha() {
        return hisFecha;
    }

    public void setHisImporte(String hisImporte) {
        this.hisImporte = hisImporte;
    }

    public String getHisImporte() {
        return hisImporte;
    }

    public void setHisIdDirige(String hisIdDirige) {
        this.hisIdDirige = hisIdDirige;
    }

    public String getHisIdDirige() {
        return hisIdDirige;
    }

    public void setHisConcepto(String hisConcepto) {
        this.hisConcepto = hisConcepto;
    }

    public String getHisConcepto() {
        return hisConcepto;
    }

    public void setMovimientos(List<bcRfTrHistoricoReportesMovimiento> movimientos) {
        this.movimientos = movimientos;
    }

    public List<bcRfTrHistoricoReportesMovimiento> getMovimientos() {
        return movimientos;
    }

    public void setHisOnservaciones(String hisOnservaciones) {
        this.hisOnservaciones = hisOnservaciones;
    }

    public String getHisOnservaciones() {
        return hisOnservaciones;
    }

    public void setHisIdAutorizo(String hisIdAutorizo) {
        this.hisIdAutorizo = hisIdAutorizo;
    }

    public String getHisIdAutorizo() {
        return hisIdAutorizo;
    }

    public void setHisIdReviso(String hisIdReviso) {
        this.hisIdReviso = hisIdReviso;
    }

    public String getHisIdReviso() {
        return hisIdReviso;
    }

    public void setHisIdElaboro(String hisIdElaboro) {
        this.hisIdElaboro = hisIdElaboro;
    }

    public String getHisIdElaboro() {
        return hisIdElaboro;
    }

  public void setHisTipoFolio(String hisTipoFolio) {
    this.hisTipoFolio = hisTipoFolio;
  }

  public String getHisTipoFolio() {
    return hisTipoFolio;
  }

  public void setHisFolio(String hisFolio) {
    this.hisFolio = hisFolio;
  }

  public String getHisFolio() {
    return hisFolio;
  }

    public void setHisValida(String hisValida) {
        this.hisValida = hisValida;
    }

    public String getHisValida() {
        return hisValida;
    }
}
