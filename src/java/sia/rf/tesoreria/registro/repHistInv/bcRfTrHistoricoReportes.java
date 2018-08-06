package sia.rf.tesoreria.registro.repHistInv;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import sia.configuracion.BeanBase;

public class bcRfTrHistoricoReportes extends BeanBase {

    private String reporte;
    private String fecha;
    private String consecutivo;
    private String informacion;
    
    
    /// resumen de inversion 
    private String idCuentaInversion;
    private String idCompraInversion;
    private String valorLeyanda;
    

    /// disponibilidad financiera
    private String valorBanco;
    private String conEgreCP;
    private String conIngInv;
    private String vencCP;
    private String conEgeInv;
    private String vencInv;
    private String compEgre;
    
    /// DF, TF, CT
    private String fechaRep;
    
    /// RI, DF, TF
    private String observacion;
    private String numEmpleadoA;
    private String numEmpleadoVB;
    private String numEmpleadoE;
    private String[] datos;
    
     // cotizacion 
    private String tabla;
    private String retiro;
    private String fondoReserva;
    private String inversionRec;

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

    private void limpia() {
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

    private void getNext(Connection con) throws Exception {
        Statement stQuery = null;
        StringBuffer SQL = null;
        ResultSet rsQuerySeq = null;
        try {
            stQuery =
                    con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = new StringBuffer("select 1 valoracutal from dual");
            rsQuerySeq = stQuery.executeQuery(SQL.toString());
            while (rsQuerySeq.next()) { // Asignar llave compuesta
            } //del while

        } catch (Exception e) {
            throw new Exception(SQL.toString(), e);
        } finally {
            SQL.setLength(0);
            if (rsQuerySeq != null)
                rsQuerySeq.close();
            rsQuerySeq = null;
            if (stQuery != null)
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
        SQL.append("  CONSECUTIVO = " + consecutivo + " and ");
        SQL.append(" trunc(FECHA) = to_date('" + fecha + "','dd/mm/yyyy') and ");
        SQL.append("  REPORTE = " + reporte + "  ");

        Statement stQuery = null;
        ResultSet rsQuery = null;
        int resultado = 0;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());

            rsQuery.beforeFirst();
            if (rsQuery.next()) {

                limpia();

                reporte = rsQuery.getString("REPORTE") == null ? "" : rsQuery.getString("REPORTE");
                fecha = rsQuery.getString("FECHA") == null ? "" : rsQuery.getString("FECHA");
                consecutivo = rsQuery.getString("CONSECUTIVO") == null ? "" : rsQuery.getString("CONSECUTIVO");
                informacion = rsQuery.getString("INFORMACION") == null ? "" : rsQuery.getString("INFORMACION");

            }
            divideInformacion();
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo select");
            System.out.println("bcRfTrHistoricoReportes.select: " +   SQL.toString());
            throw new Exception(SQL.toString(), e);
        } finally {
            if (rsQuery != null)
                rsQuery.close();
            rsQuery = null;
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
        if(informacion!=null && !informacion.equals("")) {
          campos = informacion.split("~");
          if (reporte.equals("4")){
            setIdCuentaInversion(campos[0]);
            setIdCompraInversion(campos[1]);
            setObservacion(campos[2]);
            setNumEmpleadoA(campos[3]);
            setNumEmpleadoVB(campos[4]);
            setNumEmpleadoE(campos[5]);
            setValorLeyanda(campos[6]);
          }
          if (reporte.equals("3")){
            setFecha(campos[0]);
            setValorBanco(campos[1]);
            setObservacion(campos[2]);
            setNumEmpleadoA(campos[3]);
            setNumEmpleadoVB(campos[4]);
            setNumEmpleadoE(campos[5]);
            ///setVencCP(campos[6]);
            setVencCP(campos[7]);
            setConIngInv(campos[8]);
            setConEgeInv(campos[9]);
            setVencInv(campos[10]);
            setConEgreCP(campos[11]);
          }
           if (reporte.equals("1")){
              setFecha(campos[0]);
              setTabla(campos[1]);
              setObservacion(campos[2]);
              setNumEmpleadoE(campos[3]);
              setNumEmpleadoVB(campos[4]);
              setRetiro(campos[6]);
              setFondoReserva(campos[7]);
              setInversionRec(campos[8]);
           }
        }
    }

    public void insert(Connection con) throws Exception {
        ResultSet rsQuerySeq = null;
        Statement stQuery = null;
        Statement stQuery2 = null;
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
            stQuery2 = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            stQuery2.executeUpdate(SQL.toString());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo insert");
            System.out.println("bcRfTrHistoricoReportes.insert: " + SQL);
            consecutivo = "-1";
            fecha = "-1";
            reporte = "-1";

            throw new Exception(SQL.toString(), e);
        } finally {
            if (rsQuerySeq != null) {
                rsQuerySeq.close();
                rsQuerySeq = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
            if (stQuery2 != null) {
                stQuery2.close();
                stQuery2 = null;
            }
            if (SQL != null) {
                SQL.setLength(0);
                SQL = null;
            }
        }
        //return (Integer.parseInt(idMetaIndicador));
    }

    public void update(Connection con) throws Exception {
        adecuaCampos();
        StringBuffer SQL = new StringBuffer();
        SQL.append("update RF_TR_HISTORICO_REPORTES set \n");
        SQL.append("  REPORTE = " + reporte + ", \n");
        SQL.append("  FECHA = " + fecha + ", \n");
        SQL.append("  CONSECUTIVO = " + consecutivo + ", \n");
        SQL.append("  INFORMACION = " + informacion + " \n");
        SQL.append("where \n");
        SQL.append("  CONSECUTIVO = " + consecutivo + " and ");
        SQL.append("  FECHA =  " + fecha + " and " );
        SQL.append("  REPORTE = " + reporte + "   ");
        //10
        int regsAfectados = -1;
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            regsAfectados = stQuery.executeUpdate(SQL.toString());
        } catch (Exception e) {
            System.out.println("Ha ocurrido un error en el metodo update");
            System.out.println("bcRfTrHistoricoReportes.update: " + SQL);
            throw new Exception(SQL.toString(), e);
        } finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        }
    }

    public void delete(Connection con) throws Exception {
        int rs = -1;
        Statement stQuery = null;
        String SQL = "";
        try {
            stQuery =
                    con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL = "delete from RF_TR_HISTORICO_REPORTES where CONSECUTIVO = " + consecutivo + " and FECHA = " + fecha + " and REPORTE = " + reporte + " ";
            rs = stQuery.executeUpdate(SQL);
        } catch (Exception e) {
            System.out.println("Ha occurrido un error en el metodo delete");
            System.out.println("bcRfTrHistoricoReportes.delete: " + SQL);
            throw new Exception(SQL.toString(), e);
        } finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
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

  
    public void setValorLeyanda(String valorLeyanda) {
        this.valorLeyanda = valorLeyanda;
    }

    public String getValorLeyanda() {
        return valorLeyanda;
    }

    public void setValorBanco(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public String getValorBanco() {
        return valorBanco;
    }

  

    public void setCompEgre(String compEgre) {
        this.compEgre = compEgre;
    }

    public String getCompEgre() {
        return compEgre;
    }

    public void setFechaRep(String fechaRep) {
        this.fechaRep = fechaRep;
    }

    public String getFechaRep() {
        return fechaRep;
    }

    public void setObservacion(String observacion) {
        this.observacion = observacion;
    }

    public String getObservacion() {
        return observacion;
    }
    
    public void setDatos(String[] datos) {
        this.datos = datos;
    }

    public String[] getDatos() {
        return datos;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public String getTabla() {
        return tabla;
    }

    public void setRetiro(String retiro) {
        this.retiro = retiro;
    }

    public String getRetiro() {
        return retiro;
    }

    public void setIdCuentaInversion(String idCuentaInversion) {
        this.idCuentaInversion = idCuentaInversion;
    }

    public String getIdCuentaInversion() {
        return idCuentaInversion;
    }

    public void setIdCompraInversion(String idCompraInversion) {
        this.idCompraInversion = idCompraInversion;
    }

    public String getIdCompraInversion() {
        return idCompraInversion;
    }

    public void setNumEmpleadoA(String numEmpleadoA) {
        this.numEmpleadoA = numEmpleadoA;
    }

    public String getNumEmpleadoA() {
        return numEmpleadoA;
    }

    public void setNumEmpleadoVB(String numEmpleadoVB) {
        this.numEmpleadoVB = numEmpleadoVB;
    }

    public String getNumEmpleadoVB() {
        return numEmpleadoVB;
    }

    public void setNumEmpleadoE(String numEmpleadoE) {
        this.numEmpleadoE = numEmpleadoE;
    }

    public String getNumEmpleadoE() {
        return numEmpleadoE;
    }

    public void setConEgreCP(String conEgreCP) {
        this.conEgreCP = conEgreCP;
    }

    public String getConEgreCP() {
        return conEgreCP;
    }

    public void setConIngInv(String conIngInv) {
        this.conIngInv = conIngInv;
    }

    public String getConIngInv() {
        return conIngInv;
    }

    public void setVencCP(String vencCP) {
        this.vencCP = vencCP;
    }

    public String getVencCP() {
        return vencCP;
    }

    public void setConEgeInv(String conEgeInv) {
        this.conEgeInv = conEgeInv;
    }

    public String getConEgeInv() {
        return conEgeInv;
    }

    public void setVencInv(String vencInv) {
        this.vencInv = vencInv;
    }

    public String getVencInv() {
        return vencInv;
    }

    public void setFondoReserva(String fondoReserva) {
        this.fondoReserva = fondoReserva;
    }

    public String getFondoReserva() {
        return fondoReserva;
    }

    public void setInversionRec(String inversionRec) {
        this.inversionRec = inversionRec;
    }

    public String getInversionRec() {
        return inversionRec;
    }
}
