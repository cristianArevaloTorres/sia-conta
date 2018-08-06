package sia.rf.contabilidad.reportes;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;
import sia.libs.periodo.Fecha;

import sia.scriptlets.BarraProgreso;

public class ScriptletMovimientosNoIdentificados extends BarraProgreso {
  public ScriptletMovimientosNoIdentificados() {
  }
  
  
  public void afterReportInit() throws JRScriptletException {
    super.afterReportInit();
    //sia.rf.contabilidad.reportes.ScriptletMovimientosNoIdentificados
    try {
      if (checkParameter("FECHA_CONSOLIDACION") != null){
        Fecha fechaPeriodo = new Fecha(getParameterValue("FECHA_CONSOLIDACION").toString().substring(0,8), "/");
        String fecha=String.valueOf(fechaPeriodo.getDia()).concat(" DE ").concat(sia.libs.formato.Fecha.getNombreMes(fechaPeriodo.getMes()-1).toUpperCase().concat(" DE ").concat(String.valueOf(fechaPeriodo.getAnio())));          
        setVariableValue("FECHA_MAY_CONSOLIDACION", fecha);
      } //if
    } //try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    } //catch}
  } //afterReportInit

}
