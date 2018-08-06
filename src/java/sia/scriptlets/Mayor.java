package sia.scriptlets;

import java.io.PrintWriter;
import java.io.Writer;

import java.math.BigDecimal;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;
import sia.libs.formato.Numero;
import sia.scriptlets.Firmante;

import sia.rf.contabilidad.reportes.SubFirmas;

public class Mayor extends SubFirmas {

    private int currentRecord = 0;
    private int recordCount = 1;
    private String form = null;
    private boolean showPercent = false;
    private int porcentaje = 0;
    private int avancePorcetaje = 0;
    private boolean htmlPercent = false;
    private boolean debug = false;
    private Connection connection = null;
    private PrintWriter out = null;
    private BigDecimal SaldoAnt = null;
    private BigDecimal SaldoFin = null;
    private BigDecimal subTotalDebe, subTotalHaber = null;

    public Mayor() {
        super();
    }

    public void beforePageInit() throws JRScriptletException {
        subTotalDebe = new BigDecimal(0);
        subTotalHaber = new BigDecimal(0);
        //System.out.println("SubTotalDebe: " + subTotalDebe);
        //this.setVariableValue("SUBTOTALHABER",(BigDecimal)checkField("HABER"));
    }

    public void beforeReportInit() throws JRScriptletException {
        SaldoAnt = new BigDecimal(0);
        SaldoFin = new BigDecimal(0);
        recordCount = 0;
        recordCount = 0;
        porcentaje = 0;
        avancePorcetaje = 0;
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
                }
                ; // while
                this.setVariableValue("SALDO_TOTAL", SaldoFin);
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

    public void afterDetailEval() throws JRScriptletException {
        int imprimir = 0;
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
        }//try
        catch (JRScriptletException e) {
            Error.mensaje(e, "SIAFM");
        }//catch

        //System.out.print("En DetailEval :");
        //System.out.println((BigDecimal)checkVariable("SUBTOTALHABER"));
        if (checkField("ORDEN").equals("1")) {
            SaldoFin = new BigDecimal(0);
            if (checkField("NATURALEZA").equals("D")) //SaldoFin=(BigDecimal)checkField("DEBE")-(Double)checkField("HABER");
            {
                SaldoFin = ((BigDecimal) checkField("DEBE")).subtract((BigDecimal) checkField("HABER"));
            } else //SaldoFin=(Double)checkField("HABER")-(Double)checkField("DEBE");
            {
                SaldoFin = ((BigDecimal) checkField("HABER")).subtract((BigDecimal) checkField("DEBE"));
            }
        } else {
            if (checkField("NATURALEZA").equals("D")) //SaldoFin=SaldoAnt+ (Double)checkField("DEBE")-(Double)checkField("HABER");
            {
                SaldoFin = SaldoAnt.add(((BigDecimal) checkField("DEBE")).subtract((BigDecimal) checkField("HABER")));
            } else //SaldoFin=SaldoAnt+ (Double)checkField("HABER")-(Double)checkField("DEBE");
            {
                SaldoFin = SaldoAnt.add(((BigDecimal) checkField("HABER")).subtract((BigDecimal) checkField("DEBE")));
            }
        }

        subTotalDebe = subTotalDebe.add((BigDecimal) checkField("DEBE"));
        subTotalHaber = subTotalHaber.add((BigDecimal) checkField("HABER"));
        //this.setVariableValue("SALDO_TOTAL",new Double(SaldoFin));
        this.setVariableValue("SALDO_TOTAL", SaldoFin);
        this.setVariableValue("IMPRIMIR", imprimir);
        this.setVariableValue("SUBTOTALDEBE", subTotalDebe);
        this.setVariableValue("SUBTOTALHABER", subTotalHaber);
        SaldoAnt = SaldoFin;
        if (showPercent) {
            progress();
        }
        //System.out.println((BigDecimal)checkVariable("SALDO_TOTAL"));  
    }

    public void beforeDetailEval() throws JRScriptletException {
    }
}
