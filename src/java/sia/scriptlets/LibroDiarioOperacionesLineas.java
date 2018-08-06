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

public class LibroDiarioOperacionesLineas extends SubFirmas {

    public LibroDiarioOperacionesLineas() {
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

    }
}
