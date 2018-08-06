package sia.scriptlets;

import java.io.PrintWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;
import sia.scriptlets.Firmante;
import sia.libs.formato.Numero;

import sia.rf.contabilidad.reportes.SubFirmas;

public class SaldosLibrosDiario extends SubFirmas {

    private int currentRecord = 0;
    private int recordCount = 1;
    private String form = null;
    private boolean showPercent = false;
    private boolean htmlPercent = false;
    private boolean debug = false;
    private Connection connection = null;
    private PrintWriter out = null;
    private String[] nombreCampos = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL", "AGO", "SEP", "OCT", "NOV", "DIC"};

    public SaldosLibrosDiario() {
    }

    public void beforeReportInit() throws JRScriptletException {
        recordCount = 0;
        String queryReport = null;
        try {
            connection = (Connection) this.getParameterValue("REPORT_CONNECTION");
            if (checkParameter("SIA_QUERY_REPORT") != null) {
                queryReport = (String) checkParameter("SIA_QUERY_REPORT");
            }
            if (checkParameter("SIA_RECORD_COUNT") != null) {
                recordCount = ((Integer) this.getParameterValue("SIA_RECORD_COUNT")).intValue();
            }
            if (recordCount <= 0 && connection != null && queryReport != null) {
                Statement stm = connection.createStatement();
                ResultSet rst = stm.executeQuery(queryReport);
                while (rst.next()) {
                    recordCount++;
                } // while
                stm.close();
                stm = null;
                rst = null;
                if (debug) {
                    System.out.println("total de registro: " + recordCount);
                }
            }
            ; // if
        } catch (Exception e) {
            System.err.println(Error.getMensaje(this, "SIAFM", "beforeReportInit", e));
        }
        ; // try
        if (checkParameter("SIA_OUT_REPORT") != null) {
            out = new PrintWriter((Writer) checkParameter("SIA_OUT_REPORT"));
        }
        if (checkParameter("SIA_FORM_REPORT") != null) {
            form = (String) checkParameter("SIA_FORM_REPORT");
        }
        showPercent = recordCount > 0;
        htmlPercent = out != null && form != null;
    }

    public void beforeDetailEval() throws JRScriptletException {
    }

    public void afterDetailEval() throws JRScriptletException {
        BigDecimal cargoInicial = new BigDecimal(0);
        BigDecimal abonoInicial = new BigDecimal(0);
        BigDecimal totalAbono = new BigDecimal(0);
        BigDecimal totalCargo = new BigDecimal(0);
        BigDecimal finalHaber = new BigDecimal(0);
        BigDecimal finalDebe = new BigDecimal(0);
        BigDecimal Haber = new BigDecimal(0);
        BigDecimal Debe = new BigDecimal(0);

        int imprimir = 0;
        String mesAcum = null;
        String mes = null;
        StringBuffer sb = null;
        Statement stm = null;
        ResultSet rs = null;

        Firmante firmante = null;

        try {
            if (checkParameter("ELABORO") != null) {
                firmante = getFirma("ELB", getParameterValue("UNIDAD_EJECUTORA").toString(), getParameterValue("ENTIDAD").toString(), getParameterValue("AMBITO").toString(), getParameterValue("EJERCICIO").toString(), getParameterValue("MES_FIRMAS").toString(), getParameterValue("DOCUMENTO").toString(), getParameterValue("ELABORO").toString());
                setVariableValue("ELABORO_EMPLEADO", firmante == null ? "" : firmante.getNombreEmpleado());
                setVariableValue("ELABORO_AREA", firmante == null ? "" : firmante.getAreaAdscripcion());
            }
            if (checkParameter("REVISO") != null) {
                firmante = getFirma("REV", getParameterValue("UNIDAD_EJECUTORA").toString(), getParameterValue("ENTIDAD").toString(), getParameterValue("AMBITO").toString(), getParameterValue("EJERCICIO").toString(), getParameterValue("MES_FIRMAS").toString(), getParameterValue("DOCUMENTO").toString(), getParameterValue("REVISO").toString());
                setVariableValue("REVISO_EMPLEADO", firmante == null ? "" : firmante.getNombreEmpleado());
                setVariableValue("REVISO_AREA", firmante == null ? "" : firmante.getAreaAdscripcion());
            }
            if (checkParameter("AUTORIZO") != null) {
                firmante = getFirma("AUT", getParameterValue("UNIDAD_EJECUTORA").toString(), getParameterValue("ENTIDAD").toString(), getParameterValue("AMBITO").toString(), getParameterValue("EJERCICIO").toString(), getParameterValue("MES_FIRMAS").toString(), getParameterValue("DOCUMENTO").toString(), getParameterValue("AUTORIZO").toString());
                setVariableValue("AUTORIZO_EMPLEADO", firmante == null ? "" : firmante.getNombreEmpleado());
                setVariableValue("AUTORIZO_AREA", firmante == null ? "" : firmante.getAreaAdscripcion());
            }


            mes = nombreCampos[Integer.parseInt((((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(5, 7)) - 1];
            if (Integer.parseInt((((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(5, 7)) > 1) {
                mesAcum = nombreCampos[Integer.parseInt((((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(5, 7)) - 2];
            }
            stm = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            sb = new StringBuffer();
            if (!(((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(8).equals("01")) {

                sb.append("select substr(cc.cuenta_contable,1,4) cuenta, ");
                sb.append(" cc.cuenta_mayor_id, ");
                sb.append("( ");
                sb.append("select sum(dp1.importe) haber ");
                //sb.append("from rf_tr_cuentas_contables cc1, rf_tr_detalle_poliza dp1 "); 
                sb.append("from rf_tr_cuentas_contables cc1, RF_TR_DETALLE_POLIZA_TEMP_LIB dp1 ");
                sb.append("where (dp1.fecha_afectacion>=to_date('" + (((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(0, 7) + "-01','yyyy-mm-dd') and dp1.fecha_afectacion<=to_date('" + ((java.sql.Date) checkField("FECHA_AFECTACION")).toString() + "','yyyy-mm-dd')-1 ) ");
                sb.append("and cc1.cuenta_contable_id=dp1.cuenta_contable_id ");
                sb.append("and dp1.operacion_contable_id=1 ");
                sb.append("and cc1.id_catalogo_cuenta=1 ");
                sb.append("and cc.cuenta_mayor_id=cc1.cuenta_mayor_id ");
                sb.append(")haber, ");
                sb.append("( ");
                sb.append("select sum(dp1.importe) haber ");
                //sb.append("from rf_tr_cuentas_contables cc1, rf_tr_detalle_poliza dp1 "); 
                sb.append("from rf_tr_cuentas_contables cc1, RF_TR_DETALLE_POLIZA_TEMP_LIB dp1 ");
                sb.append("where (dp1.fecha_afectacion>=to_date('" + (((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(0, 7) + "-01','yyyy-mm-dd') and dp1.fecha_afectacion<=to_date('" + ((java.sql.Date) checkField("FECHA_AFECTACION")).toString() + "','yyyy-mm-dd')-1 ) ");
                sb.append("and cc1.cuenta_contable_id=dp1.cuenta_contable_id ");
                sb.append("and dp1.operacion_contable_id=0 ");
                sb.append("and cc1.id_catalogo_cuenta=1 ");
                sb.append("and cc.cuenta_mayor_id=cc1.cuenta_mayor_id ");
                sb.append(")debe ");
                //sb.append("from rf_tr_cuentas_contables cc, rf_tr_detalle_poliza dp ");
                sb.append("from rf_tr_cuentas_contables cc, RF_TR_DETALLE_POLIZA_TEMP_LIB dp ");
                sb.append("where (dp.fecha_afectacion>=to_date('" + (((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(0, 7) + "-01','yyyy-mm-dd') and dp.fecha_afectacion<=to_date('" + ((java.sql.Date) checkField("FECHA_AFECTACION")).toString() + "','yyyy-mm-dd')-1 ) ");
                sb.append("and cc.cuenta_contable_id=dp.cuenta_contable_id ");
                sb.append("and cc.id_catalogo_cuenta=1 ");
                sb.append("and substr(cc.cuenta_contable,1,4)='" + ((String) checkField("CUENTA")) + "' ");
                sb.append("group by substr(cc.cuenta_contable,1,4), cc.cuenta_mayor_id ");
                System.out.print(sb.toString());
                rs = stm.executeQuery(sb.toString());
                if (rs.first()) {
                    cargoInicial = cargoInicial.add(rs.getString("DEBE") != null ? rs.getBigDecimal("DEBE") : new BigDecimal(0));
                    abonoInicial = abonoInicial.add(rs.getString("HABER") != null ? rs.getBigDecimal("HABER") : new BigDecimal(0));
                }
            }
            sb.delete(0, sb.length());
            if (mes.equals("ENE")) {
                sb.append("select cc.naturaleza, t." + mes + "_CARGO_INI_ELI, t." + mes + "_ABONO_INI_ELI ");
            } else {
                sb.append("select cc.naturaleza, t." + mesAcum + "_CARGO_ACUM_ELI, t." + mesAcum + "_ABONO_ACUM_ELI ");
            }
            sb.append("from rf_tr_cuentas_contables t, RF_TC_CLASIFICADOR_CUENTAS cc ");
            sb.append("where t.nivel=1 and extract(year from t.fecha_vig_ini)=" + (((java.sql.Date) checkField("FECHA_AFECTACION")).toString()).substring(0, 4) + " and t.id_catalogo_cuenta=1 and t.cuenta_mayor_id= cc.cuenta_mayor_id ");
            sb.append("and t.cuenta_contable like '" + ((String) checkField("CUENTA")) + "%' ");
            //sb.append("and t.fecha_vig_ini<='01/01/2011' and t.fecha_vig_fin>='31/03/2011' ");
            //System.out.print(sb.toString());
            rs = stm.executeQuery(sb.toString());

            if (rs.first()) {
                if (mes.equals("ENE")) {
                    cargoInicial = cargoInicial.add(rs.getBigDecimal(mes + "_CARGO_INI_ELI"));
                    abonoInicial = abonoInicial.add(rs.getBigDecimal(mes + "_ABONO_INI_ELI"));
                } else {
                    cargoInicial = cargoInicial.add(rs.getBigDecimal(mesAcum + "_CARGO_ACUM_ELI"));
                    abonoInicial = abonoInicial.add(rs.getBigDecimal(mesAcum + "_ABONO_ACUM_ELI"));
                }
                if (((Double) checkField("DEBE")) != null) {
                    Debe = new BigDecimal(((Double) checkField("DEBE")).doubleValue());
                }
                if (((Double) checkField("HABER")) != null) {
                    Haber = new BigDecimal(((Double) checkField("HABER")).doubleValue());
                }

                if (rs.getString("NATURALEZA").equals("D")) {
                    totalCargo = cargoInicial.subtract(abonoInicial);
                    totalAbono = new BigDecimal(0);
                    finalDebe = totalCargo.add(Debe.subtract(Haber));
                    if (finalDebe.setScale(2, 2).abs().doubleValue() == 0.00 && finalDebe.signum() == -1) {
                        finalDebe = new BigDecimal(0);
                    }
                    finalHaber = new BigDecimal(0);

                } else {
                    totalCargo = new BigDecimal(0);
                    totalAbono = abonoInicial.subtract(cargoInicial);
                    finalHaber = totalAbono.add(Haber.subtract(Debe));
                    if (finalHaber.setScale(2, 2).abs().doubleValue() == 0.00 && finalHaber.signum() == -1) {
                        finalHaber = new BigDecimal(0);
                    }
                    finalDebe = new BigDecimal(0);
                }
                if (cargoInicial.doubleValue() == 0 & abonoInicial.doubleValue() == 0 & totalCargo.doubleValue() == 0 & totalAbono.doubleValue() == 0 & Haber.doubleValue() == 0 & Debe.doubleValue() == 0) {
                    imprimir = 1;
                }
            }

        } catch (Exception e) {
            System.out.println("Error en obtencion de inicial " + e.getMessage());
        } finally {
            java.util.Date fecha = new java.util.Date();
            java.text.DateFormat df = new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stm != null) {
                    stm.close();
                    stm = null;
                }
                if (sb != null) {
                    sb.delete(0, sb.length());
                    sb = null;
                }
            } catch (Exception ex) {
                System.out.println("Error cerrar statement...");
            }
        }

        this.setVariableValue("TOTAL_ABONO", totalAbono);
        this.setVariableValue("TOTAL_CARGO", totalCargo);
        this.setVariableValue("TOTAL_CARGO_DIA", ((BigDecimal) this.getVariableValue("TOTAL_CARGO_DIA")).add(totalCargo));
        this.setVariableValue("TOTAL_ABONO_DIA", ((BigDecimal) this.getVariableValue("TOTAL_ABONO_DIA")).add(totalAbono));
        if (finalDebe.doubleValue() == 0) {
            this.setVariableValue("FINAL_DEBE", new BigDecimal(0));
        } else {
            this.setVariableValue("FINAL_DEBE", finalDebe);
        }
        if (finalHaber.doubleValue() == 0) {
            this.setVariableValue("FINAL_HABER", new BigDecimal(0));
        } else {
            this.setVariableValue("FINAL_HABER", finalHaber);
        }
        this.setVariableValue("FINAL_CARGO_DIA", ((BigDecimal) this.getVariableValue("FINAL_CARGO_DIA")).add(finalDebe));
        this.setVariableValue("FINAL_ABONO_DIA", ((BigDecimal) this.getVariableValue("FINAL_ABONO_DIA")).add(finalHaber));
        this.setVariableValue("IMPRIMIR", imprimir);
        /*System.out.println("-----------------------------------"); 
         System.out.println("CAMPO DEBE: "+ this.getFieldValue("DEBE"));
         System.out.println("VARIABLE ACUMULADO DEBE: "+ this.getVariableValue("TOTAL_DEBE"));
         System.out.println("CAMPO HABER: "+ this.getFieldValue("HABER"));
         System.out.println("VARIABLE ACUMULADO HABER: "+ this.getVariableValue("TOTAL_HABER") );
         System.out.println("valor Imprimir: "+ imprimir);*/


        if (showPercent) {
            progress();
        }
    }
}
