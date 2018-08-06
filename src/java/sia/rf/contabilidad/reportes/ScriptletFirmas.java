package sia.rf.contabilidad.reportes;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;

import sia.scriptlets.Firmante;

public class ScriptletFirmas extends Firmas{
  public ScriptletFirmas() {
  }
  
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    Firmante firmante = null;
    try {
      if (checkParameter("ELABORO") != null){
        firmante = getFirma("ELB",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("ELABORO").toString());
        setVariableValue("ELABORO_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
        setVariableValue("ELABORO_AREA", firmante==null?"":firmante.getAreaAdscripcion());
      }
      if (checkParameter("REVISO")!= null){
        firmante = getFirma("REV",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("REVISO").toString());
        setVariableValue("REVISO_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
        setVariableValue("REVISO_AREA", firmante==null?"":firmante.getAreaAdscripcion());
      }
      if (checkParameter("AUTORIZO")!= null){
        firmante = getFirma("AUT",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("AUTORIZO").toString());
        setVariableValue("AUTORIZO_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
        setVariableValue("AUTORIZO_AREA", firmante==null?"":firmante.getAreaAdscripcion());
      }
      setVariableValue("FIRMA_FECHA", getFechaFirma(Integer.valueOf(getParameterValue("ENTIDAD").toString()),
                                       Integer.valueOf(getParameterValue("AMBITO").toString()),
                                       Integer.valueOf(getParameterValue("UNIDAD_EJECUTORA").toString())));
    }//try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    }//catch
  }//afterDetailEval
}
