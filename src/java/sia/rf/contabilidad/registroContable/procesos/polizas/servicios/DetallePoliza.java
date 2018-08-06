package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

//import oracle.jbo.ViewObject;
import sia.libs.formato.Error;
import sia.libs.formato.Formatos;

import sia.libs.recurso.Contabilidad;

//import sia.configuracion.UtileriasComun;

public class DetallePoliza{
  
  private String referencia;
  private String fechaAfectacion;
  private OperacionContable operacionContable;
  private int cuentaContableId;
  
  public DetallePoliza(String referencia,String  fechaAfectacion,OperacionContable operacionContable,int cuentaContableId) {
    this.setReferencia(referencia);
    this.setFechaAfectacion(fechaAfectacion);
    this.setOperacionContable(operacionContable);    
    this.setCuentaContableId(cuentaContableId);
  }  
  
 /* public int obtenerMaestroOperacion99(String unidadEjecutora, String entidad, String ambito){
    ViewObject voMaestroOperacion=null;
    int maestroOperacion=0;
    UtileriasComun utileria=new UtileriasComun("ModuloPolizasDataControl");
    Formatos formatos = null;
    try{
      utileria.removerVistas("Nombrex");
      formatos = new Formatos(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerMaestroOperacion"),unidadEjecutora,entidad,ambito);
      voMaestroOperacion=utileria.createViewObjectFromSentencia(formatos.getSentencia());
      maestroOperacion=((Number)voMaestroOperacion.first().getAttribute("MAESTRO_OPERACION_ID")).intValue();            
    }catch(Exception e){
      System.err.print(Error.getMensaje(this, "SIAFM", "obtenerMaestroOperacion99", e.getMessage()));       
    }finally{
      utileria.removerVistas("Nombrex");
      utileria=null;
    }
    return maestroOperacion;
  }*/
  
  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setFechaAfectacion(String fechaAfectacion) {
    this.fechaAfectacion = fechaAfectacion;
  }

  public String getFechaAfectacion() {
    return fechaAfectacion;
  }

  public void setCuentaContableId(int cuentaContableId) {
    this.cuentaContableId = cuentaContableId;
  }

  public int getCuentaContableId() {
    return cuentaContableId;
  }

  private void setOperacionContable(OperacionContable operacionContable) {
    this.operacionContable = operacionContable;
  }

  public OperacionContable getOperacionContable() {
    return operacionContable;
  }
}
