package sia.rf.contabilidad.reportes;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;
import sia.libs.periodo.Fecha;

import sia.scriptlets.BarraProgreso;

public class ScriptletComun extends BarraProgreso {
  public void afterReportInit() throws JRScriptletException {
    super.afterReportInit();
    try {
      if (checkParameter("FECHA_CONSOLIDACION") != null) {
        Fecha fechaPeriodo = new Fecha(getParameterValue("FECHA_CONSOLIDACION").toString(), "/");
        fechaPeriodo.getDiasEnElMes();
        String fecha = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
        setVariableValue("FECHA_MAY_CONSOLIDACION", fecha);
      } //if
       
    } //try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    } //catch
  } //afterReportInit
}
