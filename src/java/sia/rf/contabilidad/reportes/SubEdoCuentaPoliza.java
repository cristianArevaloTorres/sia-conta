package sia.rf.contabilidad.reportes;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.scriptlets.BarraProgreso;

public class SubEdoCuentaPoliza extends BarraProgreso {

  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    DatosReportes datosReportes = new DatosReportes();
    String poliza = "";
    int tipoPolizaId = Integer.valueOf(getFieldValue("CLASIFICACION_POLIZA_ID").toString());
    if(tipoPolizaId == 1){
      this.setVariableValue("CLASIFICACION_POLIZA","");
    }
    else{
      if(checkField("POLIZA_REFERENCIA")!= null){
        poliza = datosReportes.getPolizaCancelada(getFieldValue("POLIZA_REFERENCIA").toString(),"consecutivo");//El segundo parametro es para indicar que campo se quiere del query
        this.setVariableValue("CLASIFICACION_POLIZA","De Cancelacion a la póliza "+poliza);
      }
      else{
        poliza="";
        this.setVariableValue("CLASIFICACION_POLIZA","Póliza Cancelada");
      }
    }
  }
}
