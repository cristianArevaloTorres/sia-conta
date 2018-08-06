package sia.rf.contabilidad.reportes;

import java.util.HashMap;
import java.util.Map;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.scriptlets.BarraProgreso;

public class ScriptletFormascontables extends BarraProgreso{
    public ScriptletFormascontables() {
    }
    
    public void beforeDetailEval() throws JRScriptletException {
      super.beforeDetailEval();
      int nivel = Integer.parseInt(this.getFieldValue("NIVEL").toString());
      Map parametros = (HashMap)this.getParameterValue("VALORES");
      int operacionContableId = Integer.parseInt(this.getFieldValue("OPERACION_CONTABLE_ID").toString());
      if(nivel == 98){
        if(operacionContableId == 0){
          setVariableValue("operacion", operacionContableId);
          parametros.put("IMPORTE_DEBE", this.getFieldValue("CODIGO").toString());
        }
        else{
          setVariableValue("operacion", operacionContableId);
          parametros.put("IMPORTE_HABER", this.getFieldValue("CODIGO").toString());
        }
      }
      if(nivel == 99){
        parametros.put("REFERENCIA",this.getFieldValue("CODIGO").toString());
      }
    }
}
