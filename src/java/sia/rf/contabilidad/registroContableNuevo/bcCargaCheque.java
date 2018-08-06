package sia.rf.contabilidad.registroContableNuevo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class bcCargaCheque {

    public bcCargaCheque() {
    }
    private String consecutivocheques;
    private String unidad;
    private String ambito;
    private String fechacheque;
    private String cuentabancaria;
    private String operaciontipo;
    private String importe;
    private String concepto;
    private String beneficiario;
    private String fechacarga;
    private String fechaaplicacion;
    private String estatus;
    private String numempleado;
    private String mensaje;
    private String operacion_pago;
    private String operacion_pago_sup;
    private String origen_operacion;

    /**
     * consecutivocheques
     *
     * @return consecutivocheques
     */
    public String getConsecutivocheques() {
        return consecutivocheques;
    }

    /**
     * consecutivocheques
     *
     * @param consecutivocheques
     */
    public void setConsecutivocheques(String consecutivocheques) {
        this.consecutivocheques = consecutivocheques;
    }

    /**
     * unidad
     *
     * @return unidad
     */
    public String getUnidad() {
        return unidad;
    }

    /**
     * unidad
     *
     * @param unidad
     */
    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    /**
     * ambito
     *
     * @return ambito
     */
    public String getAmbito() {
        return ambito;
    }

    /**
     * ambito
     *
     * @param ambito
     */
    public void setAmbito(String ambito) {
        this.ambito = ambito;
    }

    /**
     * fechacheque
     *
     * @return fechacheque
     */
    public String getFechacheque() {
        return fechacheque.substring(0, 10);
    }

    /**
     * fechacheque
     *
     * @param fechacheque
     */
    public void setFechacheque(String fechacheque) {
        this.fechacheque = fechacheque;
    }

    /**
     * cuentabancaria
     *
     * @return cuentabancaria
     */
    public String getCuentabancaria() {
        return cuentabancaria;
    }

    /**
     * cuentabancaria
     *
     * @param cuentabancaria
     */
    public void setCuentabancaria(String cuentabancaria) {
        this.cuentabancaria = cuentabancaria;
    }

    /**
     * operaciontipo
     *
     * @return operaciontipo
     */
    public String getOperaciontipo() {
        return operaciontipo;
    }

    /**
     * operaciontipo
     *
     * @param operaciontipo
     */
    public void setOperaciontipo(String operaciontipo) {
        if (operaciontipo.length() == 1) {
            operaciontipo = "0" + operaciontipo;
        }
        this.operaciontipo = operaciontipo;
    }

    /**
     * importe
     *
     * @return importe
     */
    public String getImporte() {
        return importe;
    }

    /**
     * importe
     *
     * @param importe
     */
    public void setImporte(String importe) {
        this.importe = importe;
    }

    /**
     * concepto
     *
     * @return concepto
     */
    public String getConcepto() {
        return concepto;
    }

    /**
     * concepto
     *
     * @param concepto
     */
    public void setConcepto(String concepto) {
        this.concepto = concepto;
    }

    public String getBeneficiario() {
        return beneficiario;
    }

    public void setBeneficiario(String beneficiario) {
        this.beneficiario = beneficiario;
    }

    /**
     * fechacarga
     *
     * @return fechacarga
     */
    public String getFechacarga() {
        return fechacarga;
    }

    /**
     * fechacarga
     *
     * @param fechacarga
     */
    public void setFechacarga(String fechacarga) {
        this.fechacarga = fechacarga;
    }

    /**
     * fechaaplicacion
     *
     * @return fechaaplicacion
     */
    public String getFechaaplicacion() {
        return fechaaplicacion;
    }

    /**
     * fechaaplicacion
     *
     * @param fechaaplicacion
     */
    public void setFechaaplicacion(String fechaaplicacion) {
        this.fechaaplicacion = fechaaplicacion;
    }

    /**
     * estatus
     *
     * @return estatus
     */
    public String getEstatus() {
        return estatus;
    }

    /**
     * estatus
     *
     * @param estatus
     */
    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    /**
     * numempleado
     *
     * @return numempleado
     */
    public String getNumempleado() {
        return numempleado;
    }

    /**
     * numempleado
     *
     * @param numempleado
     */
    public void setNumempleado(String numempleado) {
        this.numempleado = numempleado;
    }

    /**
     * mensaje
     *
     * @return mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * mensaje
     *
     * @param mensaje
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public void setOperacion_pago(String operacion_pago) {
        this.operacion_pago = operacion_pago;
    }

    public String getOperacion_pago() {
        return operacion_pago;
    }

    public void setOperacion_pago_sup(String operacion_pago_sup) {
        this.operacion_pago_sup = operacion_pago_sup;
    }

    public String getOperacion_pago_sup() {
        return operacion_pago_sup;
    }

    public void setOrigen_operacion(String origen_operacion) {
        this.origen_operacion = origen_operacion;
    }

    public String getOrigen_operacion() {
        return origen_operacion;
    }

    /**
     * Metodo que lee la sequencia para la poliza que se va a generar fechAlta
     * de creacion:08/06/2009 Autor:Jorge Luis Perez N. fechAlta de
     * modificacion: Modificado por:
     */
    public String select_SEQ_rf_tr_CargaCheque(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        String SidPoliza = "";
        try {
            String SQL2 = "select seq_rf_tr_carga_cheque.nextval valoractual from dual";
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL2);
            //  secuencia para poliza_id
            while (rsQuery.next()) {
                SidPoliza = rsQuery.getString("valoractual");
            } //del while
        } //Fin try
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo seq_rf_tr_carga_cheque " + e.getMessage());
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
        return SidPoliza;
    } //fin Select 

    /**
     * Metodo que lee el nombre de la persona que carga el cheque fechAlta de
     * creacion:08/06/2009 Autor:Jorge Luis Perez N. fechAlta de modificacion:
     * Modificado por:
     */
    public String select_nombre_carga_cheque(Connection con, String pNumEmpleado) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        String nombre = "";
        try {
            String SQL2 = "select t.nombres || ' ' || t.apellido_pat || ' ' || t.apellido_mat valorActual from sia_admin.rh_tr_empleados where t.num_empleado=" + pNumEmpleado;
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL2);
            //  secuencia para poliza_id
            while (rsQuery.next()) {
                nombre = rsQuery.getString("valoractual");
            } //del while
        } //Fin try
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_nombre_carga_cheque " + e.getMessage());
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
        return nombre;
    } //fin Select 

    /**
     * Metodo que lee la informacion de RF_TR_CHEQUES_CARGA Fecha de creacion:
     * Autor: Fecha de modificacion: Modificado por:
     */
    public void select_RF_TR_CHEQUES_CARGA(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("SELECT a.consecutivocheques,a.unidad,a.ambito,to_char(a.fechacheque,'dd/mm/yyyyy hh24:mi:ss') fechaCheque,a.cuentabancaria,a.operaciontipo,a.importe,a.concepto,a.beneficiario,a.fechacarga,a.fechaaplicacion,a.estatus,a.numempleado,a.mensaje, a.origen_operacion, a.operacion_pago, a.operacion_pago_sup");
            SQL.append(" FROM RF_TR_CHEQUES_CARGA a ");
            SQL.append(" WHERE a.consecutivoCheques=").append(clave).append(" ");
            System.out.println(SQL.toString());
            rsQuery = stQuery.executeQuery(SQL.toString());
            while (rsQuery.next()) {
                consecutivocheques = (rsQuery.getString("consecutivocheques") == null) ? "" : rsQuery.getString("consecutivocheques");
                unidad = (rsQuery.getString("unidad") == null) ? "" : rsQuery.getString("unidad");
                ambito = (rsQuery.getString("ambito") == null) ? "" : rsQuery.getString("ambito");
                fechacheque = (rsQuery.getString("fechacheque") == null) ? "" : rsQuery.getString("fechacheque");
                cuentabancaria = (rsQuery.getString("cuentabancaria") == null) ? "" : rsQuery.getString("cuentabancaria");
                operaciontipo = (rsQuery.getString("operaciontipo") == null) ? "" : rsQuery.getString("operaciontipo");
                importe = (rsQuery.getString("importe") == null) ? "" : rsQuery.getString("importe");
                concepto = (rsQuery.getString("concepto") == null) ? "" : rsQuery.getString("concepto");
                beneficiario = (rsQuery.getString("beneficiario") == null) ? "" : rsQuery.getString("beneficiario");
                fechacarga = (rsQuery.getString("fechacarga") == null) ? "" : rsQuery.getString("fechacarga");
                fechaaplicacion = (rsQuery.getString("fechaaplicacion") == null) ? "" : rsQuery.getString("fechaaplicacion");
                estatus = (rsQuery.getString("estatus") == null) ? "" : rsQuery.getString("estatus");
                numempleado = (rsQuery.getString("numempleado") == null) ? "" : rsQuery.getString("numempleado");
                mensaje = (rsQuery.getString("mensaje") == null) ? "" : rsQuery.getString("mensaje");
                origen_operacion = (rsQuery.getString("origen_operacion") == null) ? "" : rsQuery.getString("origen_operacion");
                operacion_pago = (rsQuery.getString("operacion_pago") == null) ? "" : rsQuery.getString("operacion_pago");
                operacion_pago_sup = (rsQuery.getString("operacion_pago_sup") == null) ? "" : rsQuery.getString("operacion_pago_sup");
            } // Fin while 
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo select_RF_TR_CHEQUES_CARGA " + e.getMessage());
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
    } //Fin metodo select_RF_TR_CHEQUES_CARGA 

    /**
     * Metodo que inserta la informacion de RF_TR_CHEQUES_CARGA Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void insert_RF_TR_CHEQUES_CARGA(Connection con) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("INSERT INTO RF_TR_CHEQUES_CARGA( consecutivocheques,unidad,ambito,fechacheque,cuentabancaria,operaciontipo,importe,concepto, beneficiario,fechacarga,fechaaplicacion,estatus,numempleado,mensaje,origen_operacion,operacion_pago,operacion_pago_sup) ");
            SQL.append("VALUES(");
            SQL.append(consecutivocheques).append(",");
            SQL.append("'").append(unidad).append("',");
            SQL.append("'").append(ambito).append("',");
            SQL.append("to_date('").append(fechacheque).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("'").append(cuentabancaria).append("',");
            SQL.append("'").append(operaciontipo).append("',");
            SQL.append(importe).append(",");
            SQL.append("'").append(concepto).append("',");
            SQL.append("'").append(beneficiario).append("',");
            SQL.append("sysdate,");
            SQL.append("null,");
            SQL.append(estatus).append(",");
            SQL.append(numempleado).append(",");
            SQL.append("'").append(mensaje).append("',");
            SQL.append(origen_operacion).append(",");
            SQL.append("'").append(operacion_pago).append("',");
            SQL.append("'").append(operacion_pago_sup).append("')");
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo insert_RF_TR_CHEQUES_CARGA " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo insert_RF_TR_CHEQUES_CARGA 

    /**
     * Metodo que modifica la informacion de RF_TR_CHEQUES_CARGA Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void update_RF_TR_CHEQUES_CARGA(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("UPDATE RF_TR_CHEQUES_CARGA");
            SQL.append(" SET consecutivocheques=").append(consecutivocheques).append(",");
            SQL.append("unidad=").append("'").append(unidad).append("',");
            SQL.append("ambito=").append("'").append(ambito).append("',");
            SQL.append("fechacheque=to_date('").append(fechacheque).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("cuentabancaria=").append("'").append(cuentabancaria).append("',");
            SQL.append("operaciontipo=").append("'").append(operaciontipo).append("',");
            SQL.append("importe=").append(importe).append(",");
            SQL.append("concepto=").append("'").append(concepto).append("',");
            SQL.append("beneficiario=").append("'").append(beneficiario).append("',");
            SQL.append("fechacarga=to_date('").append(fechacarga).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("fechaaplicacion=to_date('").append(fechaaplicacion).append("','dd/mm/yyyy hh24:mi:ss'),");
            SQL.append("estatus=").append(estatus).append(",");
            SQL.append("numempleado=").append(numempleado).append(",");
            SQL.append("mensaje=").append("'").append(mensaje);
            SQL.append(" WHERE LLAVE='").append(clave).append("'");
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_RF_TR_CHEQUES_CARGA " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo update_RF_TR_CHEQUES_CARGA 

    /**
     * Metodo que modifica la informacion de RF_TR_CHEQUES_CARGA Fecha de
     * creacion: Autor: Fecha de modificacion: Modificado por:
     */
    public void update_RF_TR_CHEQUES_CARGA_FECHA(Connection con, String clave) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("UPDATE RF_TR_CHEQUES_CARGA ");
            SQL.append("SET fechaaplicacion=").append("sysdate,");
            SQL.append("estatus=1");
            SQL.append(" WHERE consecutivocheques=").append(clave).append(" ");
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo update_RF_TR_CHEQUES_CARGA_FECHA " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo update_RF_TR_CHEQUES_CARGA_FECHA 

    /**
     * Metodo que borra la informacion de RF_TR_CHEQUES_CARGA Fecha de creacion:
     * Autor: Fecha de modificacion: Modificado por:
     */
    /* public void delete_RF_TR_CHEQUES_CARGA(Connection con, String clave)throws SQLException, Exception{ 
     Statement stQuery=null; 
     try{ 
     stQuery=con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE); 
     StringBuilder SQL=new StringBuilder("DELETE FROM RF_TR_CHEQUES_CARGA a "); 
     SQL.append("WHERE a.LLAVE='").append(clave).append("'"); 
     System.out.println(SQL.toString()); 
     int rs=-1; 
     rs=stQuery.executeUpdate(SQL.toString()); 
     } //Fin try 
     catch(Exception e){ 
     System.out.println("Ocurrio un error al accesar al metodo delete_RF_TR_CHEQUES_CARGA "+e.getMessage()); 
     throw e; 
     } //Fin catch 
     finally{ 
     if (stQuery!=null){ 
     stQuery.close(); 
     stQuery=null; 
     } 
     } //Fin finally 
     } //Fin metodo delete_RF_TR_CHEQUES_CARGA */
    public void delete_RF_TR_CHEQUES_CARGA(Connection con, String condicion) throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuilder SQL = new StringBuilder("DELETE FROM RF_TR_CHEQUES_CARGA a ");
            SQL.append(condicion);
            System.out.println(SQL.toString());
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo delete_RF_TR_CHEQUES_CARGA " + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
            }
        } //Fin finally 
    } //Fin metodo delete_RF_TR_CHEQUES_CARGA 

    /**
     * Metodo que verifica que exista el rango de pago_opearcioin y
     * pago_operacion_sup sapfin_pa.RF_TR_OPERACIONES_CHEQUES Fecha de creacion:
     * 13/MARZO/2012 Autor: Yadhira Fecha de modificacion: Modificado por:
     */
    public String select_origen_rf_presupuesto_s2_rf_tr_operaciones_cheques_CargaCheque(Connection con, String tipo, String inferior, String superior) throws SQLException, Exception {
        Statement stQuery = null;
        ResultSet rsQuery = null;
        String origen = null;
        StringBuilder SQL2 = new StringBuilder("");

        try {
            SQL2.append("select t.origen from sapfin_pa.rf_tr_operaciones_cheques t ");
            SQL2.append("WHERE t.consecutivo like ('%").append(tipo).append("%') ");
            SQL2.append(" and SUBSTR(t.consecutivo,10,5)>=").append(inferior.substring(9, 14));
            SQL2.append(" and SUBSTR(t.consecutivo,10,5)<=").append(superior.substring(9, 14));
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL2.toString());
            //  secuencia para poliza_id
            if (rsQuery.first()) {
                origen = rsQuery.getString("origen");
            } else {
                throw new Exception("No existe existe el rango especificado.");
            }
        } //Fin try
        catch (Exception e) {
            System.out.println("select_origen_rf_presupuesto_s2_rf_tr_operaciones_cheques_CargaCheque " + e.getMessage());
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
        return origen;
    } //fin Select
} //Fin clase bcCargaCheque 
