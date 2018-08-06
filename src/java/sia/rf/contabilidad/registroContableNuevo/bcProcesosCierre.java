package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

public class bcProcesosCierre {

    public bcProcesosCierre() {
    }
    private String ejercicio;
    private String mes;
    private String verificacion;
    private String fecha_verificacion;
    private String reafectacion;
    private String fecha_reafectacion;
    private String congruencia;
    private String fecha_congruencia;
    private String cierre_definitivo;
    private String fecha_cierre_def;
    private String eliminacion;
    private String fecha_eliminacion;
    private String proceso;

    public String getEjercicio() {
        return ejercicio;
    }

    public void setEjercicio(String ejercicio) {
        this.ejercicio = ejercicio;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getVerificacion() {
        return verificacion;
    }

    public void setVerificacion(String verificacion) {
        this.verificacion = verificacion;
    }

    public String getFecha_verificacion() {
        return fecha_verificacion;
    }

    public void setFecha_verificacion(String fecha_verificacion) {
        this.fecha_verificacion = fecha_verificacion;
    }

    public String getReafectacion() {
        return reafectacion;
    }

    public void setReafectacion(String reafectacion) {
        this.reafectacion = reafectacion;
    }

    public String getFecha_reafectacion() {
        return fecha_reafectacion;
    }

    public void setFecha_reafectacion(String fecha_reafectacion) {
        this.fecha_reafectacion = fecha_reafectacion;
    }

    public String getCongruencia() {
        return congruencia;
    }

    public void setCongruencia(String cngruencia) {
        this.congruencia = cngruencia;
    }

    public String getFecha_congruencia() {
        return fecha_congruencia;
    }

    public void setFecha_congruencia(String fecha_congruencia) {
        this.fecha_congruencia = fecha_congruencia;
    }

    public String getCierre_definitivo() {
        return (cierre_definitivo == null) ? "" : cierre_definitivo;
    }

    public void setCierre_definitivo(String cierre_definitivo) {
        this.cierre_definitivo = cierre_definitivo;
    }

    public String getFecha_cierre_def() {
        return fecha_cierre_def;
    }

    public void setFecha_cierre_def(String fecha_cierre_def) {
        this.fecha_cierre_def = fecha_cierre_def;
    }

    public String getEliminacion() {
        return eliminacion;
    }

    public void setEliminacion(String eliminacion) {
        this.eliminacion = eliminacion;
    }

    public String getFecha_eliminacion() {
        return fecha_eliminacion;
    }

    public void setFecha_eliminacion(String fecha_eliminacion) {
        this.fecha_eliminacion = fecha_eliminacion;
    }

    public String getProceso() {
        return proceso;
    }

    public void setProceso(String proceso) {
        this.proceso = proceso;
    }

    /**
     * Metodo que lee la informacion de rf_tr_procesos_cierre Fecha de creacion:
     * 31/05/2012 Autor: Jorge Luis Pérez N. Fecha de modificacion: Modificado
     * por:
     */
    public void select_rf_tr_procesos_cierre(Connection con, String pEjercicio, String pMes) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT a.ejercicio,a.mes,a.verificacion,to_char(a.fecha_verificacion,'dd/mm/yyyy hh24:mi:ss') fecha_verificacion,a.reafectacion, to_char(a.fecha_reafectacion,'dd/mm/yyyy hh24:mi:ss') fecha_reafectacion, congruencia, to_char(a.fecha_congruencia,'dd/mm/yyyy hh24:mi:ss') fecha_congruencia, cierre_definitivo,to_char(a.fecha_cierre_def,'dd/mm/yyyy') fecha_cierre_def, a.eliminacion, to_char(a.fecha_eliminacion,'dd/mm/yyyy hh24:mi:ss') fecha_eliminacion ");
        SQL.append(" FROM rf_tr_procesos_cierre a ");
        SQL.append(" WHERE a.ejercicio=").append(pEjercicio).append(" and a.mes=").append(pMes);
        // System.out.println(SQL.toString());       
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                ejercicio = (rsQuery.getString("ejercicio") == null) ? "" : rsQuery.getString("ejercicio");
                mes = (rsQuery.getString("mes") == null) ? "" : rsQuery.getString("mes");
                verificacion = (rsQuery.getString("verificacion") == null) ? "" : rsQuery.getString("verificacion");
                fecha_verificacion = (rsQuery.getString("fecha_verificacion") == null) ? "" : rsQuery.getString("fecha_verificacion");
                reafectacion = (rsQuery.getString("reafectacion") == null) ? "" : rsQuery.getString("reafectacion");
                fecha_reafectacion = (rsQuery.getString("fecha_reafectacion") == null) ? "" : rsQuery.getString("fecha_reafectacion");
                congruencia = (rsQuery.getString("congruencia") == null) ? "" : rsQuery.getString("congruencia");
                fecha_congruencia = (rsQuery.getString("fecha_congruencia") == null) ? "" : rsQuery.getString("fecha_congruencia");
                cierre_definitivo = (rsQuery.getString("cierre_definitivo") == null) ? "" : rsQuery.getString("cierre_definitivo");
                fecha_cierre_def = (rsQuery.getString("fecha_cierre_def") == null) ? "" : rsQuery.getString("fecha_cierre_def");
                eliminacion = (rsQuery.getString("eliminacion") == null) ? "" : rsQuery.getString("eliminacion");
                fecha_eliminacion = (rsQuery.getString("fecha_eliminacion") == null) ? "" : rsQuery.getString("fecha_eliminacion");

            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_rf_tr_procesos_cierre " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo select_rf_tr_procesos_cierre 

    /**
     * Metodo que inserta la informacion de rf_tr_procesos_cierre Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void insert_rf_tr_procesos_cierre(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        StringBuilder SQL = new StringBuilder("INSERT INTO rf_tr_procesos_cierre( ejercicio,mes,verificacion,fecha_verificacion,reafectacion,fecha_reafectacion, congruencia, fecha_congruencia, cierre_definitivo,fecha_cierre_def, eliminacion,fecha_eliminacion) ");
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL.append("VALUES(");
            SQL.append(ejercicio).append(",");
            SQL.append(mes).append(",");
            SQL.append(verificacion).append(",");
            SQL.append(" sysdate,");
            SQL.append(reafectacion).append(",");
            SQL.append(" null,");
            SQL.append(congruencia).append(",");
            SQL.append(" null,");
            SQL.append(cierre_definitivo).append(",");
            SQL.append(" null,");
            SQL.append(eliminacion).append(",");
            SQL.append(" null)");
            // System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo insert_rf_tr_procesos_cierre " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo insert_rf_tr_procesos_cierre 

    /**
     * Metodo que modifica la informacion de rf_tr_procesos_cierre Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void update_rf_tr_procesos_cierre(Connection con, String pEjercicio, String pMes) throws SQLException, Exception {
        Statement stQuery = null;
        StringBuilder SQL = new StringBuilder("UPDATE rf_tr_procesos_cierre");
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL.append(" SET ejercio=").append(ejercicio).append(",");
            SQL.append("mes=").append(mes).append(",");
            SQL.append("verificacion=").append(verificacion).append(",");
            SQL.append("fecha_verificacion=to_date('").append(fecha_verificacion).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("reafectacion=").append(reafectacion).append(",");
            SQL.append("fecha_reafectacion=to_date('").append(fecha_reafectacion).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("congruencia=").append(congruencia).append(",");
            SQL.append("fecha_congruencia=to_date('").append(fecha_congruencia).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("cierre_definitivo=").append(cierre_definitivo).append(",");
            SQL.append("fecha_cierre_def=to_date('").append(fecha_cierre_def).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("eliminacion=").append(eliminacion).append(",");
            SQL.append("fecha_eliminacion=to_date('").append(fecha_eliminacion).append("','dd/mm/yyyy hh24:mi:ss')");
            SQL.append(" WHERE ejercicio=").append(pEjercicio).append(" and mes=").append(pMes);
            // System.out.println(SQL.toString());  
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_procesos_cierre " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo update_rf_tr_procesos_cierre 

    /**
     * Metodo que borra la informacion de rf_tr_procesos_cierre Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void delete_rf_tr_procesos_cierre(Connection con, String pEjercicio, String pMes) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("DELETE FROM rf_tr_procesos_cierre a ");
            SQL.append("WHERE a.ejercicio=").append(pEjercicio).append(" and mes=").append(pMes);
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo delete_rf_tr_procesos_cierre " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo delete_rf_tr_procesos_cierre 

    /**
     * Metodo que modifica la informacion de rf_tr_procesos_cierre Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void update_rf_tr_procesos_cierre_estatus(Connection con, String pEjercicio, String pMes, String pProceso, String pEstatus) throws SQLException, Exception {
        Statement stQuery = null;
        StringBuilder SQL = new StringBuilder("UPDATE rf_tr_procesos_cierre ");
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL.append(" SET ");
            if (pProceso.equals("1")) {
                SQL.append("verificacion=").append(pEstatus).append(",");
                SQL.append("fecha_verificacion=sysdate ");
            }
            if (pProceso.equals("2")) {
                SQL.append("reafectacion=").append(pEstatus).append(",");
                SQL.append("fecha_reafectacion=sysdate ");
            }
            if (pProceso.equals("3")) {
                SQL.append("congruencia=").append(pEstatus).append(",");
                SQL.append("fecha_congruencia=sysdate ");
            }
            if (pProceso.equals("4")) {
                SQL.append("cierre_definitivo=").append(pEstatus).append(",");
                SQL.append("fecha_cierre_def=sysdate ");
            }
            if (pProceso.equals("5")) {
                SQL.append("eliminacion=").append(pEstatus).append(",");
                SQL.append("fecha_eliminacion=sysdate ");
            }
            SQL.append(" WHERE ejercicio=").append(pEjercicio).append(" and mes=").append(pMes);
            // System.out.println(SQL.toString());  
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_rf_tr_procesos_cierre_estatus " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo update_rf_tr_procesos_cierre 

    /**
     * Metodo que lee la informacion del proceso de verificacion Fecha de
     * creacion: Fecha de modificacion: Modificado por:
     */
    public String select_proceso_verificacion(Connection con, String pMes, String pEjercicio) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT F_VerContable('" + pMes + "','" + pEjercicio + "',1) proceso");
        SQL.append(" FROM dual ");
        // System.out.println(SQL.toString());       
        proceso = "-1";
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                proceso = (rsQuery.getString("proceso") == null) ? "" : rsQuery.getString("proceso");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_proceso_verificacion " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
            }
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
        return proceso;
    } //Fin metodo select_proceso_verificacion 

    /**
     * Metodo que lee la informacion del proceso de reafectacion Fecha de
     * creacion: Fecha de modificacion: Modificado por:
     */
    public String select_proceso_reafectacion(Connection con, String pMes, String pMesLetra, String pEjercicio) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT F_AfeContable('" + pMes + "','" + pMesLetra + "','" + pEjercicio + "',1) proceso");
        SQL.append(" FROM dual ");
        // System.out.println(SQL.toString());       
        proceso = "-1";
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                proceso = (rsQuery.getString("proceso") == null) ? "" : rsQuery.getString("proceso");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_proceso_reafectacion " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
        return proceso;
    } //Fin metodo select_proceso_reafectacion 

    /**
     * Metodo que lee la informacion del proceso de congruencia Fecha de
     * creacion: Fecha de modificacion: Modificado por:
     */
    public String select_proceso_congruencia(Connection con, String pMesLetra, String pEjercicio) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        StringBuilder SQL = new StringBuilder("SELECT F_ConContable('" + pMesLetra + "','" + pEjercicio + "',1) proceso");
        SQL.append(" FROM dual ");
        //System.out.println("bcProcesosCierre.select_proceso_congruencia.sql "+SQL.toString());       
        proceso = "-1";
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                proceso = (rsQuery.getString("proceso") == null) ? "" : rsQuery.getString("proceso");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_proceso_congruencia " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (rsQuery != null) {
                rsQuery.close();
                rsQuery = null;
            }
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
        return proceso;
    } //Fin metodo select_proceso_congruencia 

    /**
     * Metodo que modifica la informacion para abrir un mes ya cerrado quita los
     * estatus de cierres definitivo y eliminacion Fecha de creacion: 08/01/2013
     * Autor: Claudia Luz Hernandez Lopez Fecha de modificacion: Modificado por:
     */
    public void update_abrir_procesos_cierre(Connection con, String pEjercicio, String pMes) throws SQLException, Exception {
        Statement stQuery = null;
        StringBuilder SQL = new StringBuilder("");
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            SQL.append("UPDATE rf_tr_procesos_cierre ");
            SQL.append(" SET ");
            SQL.append("cierre_definitivo=0,");
            SQL.append("fecha_cierre_def=null, ");
            SQL.append("eliminacion=0, ");
            SQL.append("fecha_eliminacion=null ");
            SQL.append(" WHERE ejercicio=").append(pEjercicio).append(" and mes=").append(pMes);
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_abrir_procesos_cierre " + e.getMessage());
            System.out.println(SQL.toString());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    } //Fin metodo update_abrir_procesos_cierre 
} //Fin clase bcProcesosCierre 

