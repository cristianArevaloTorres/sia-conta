package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

import sia.libs.formato.Error;
import sia.libs.periodo.Fecha;

public class Campo {
  private String tipo;
  private String fecha;
  
  public Campo(String tipo, String fecha) {
    this.setTipo(tipo);
    this.setFecha(fecha);
  }
  
  public String construirMes(){
    return this.construirParcial(this.getTipo(), this.getFecha());
  }
  
  public String construirMesEliminacion(){
    return this.construirMesesEliminacion(this.getTipo(), this.getFecha());
  }
  
  public String construirAcum(){
    return this.construirParcial(this.getTipo(), this.getFecha())+"_acum";
  }
  
  public String construirParcial(String tipo, String fechaAfectacion){
    try{
      Fecha fecha = new Fecha(fechaAfectacion, "-");
      String descMes = sia.libs.formato.Fecha.getNombreMesCorto(fecha.getMes()-1);
      
      return descMes+"_"+tipo;
    }//end try
    catch(Exception e){
      System.err.println(Error.getMensaje(this, "SIAFM", "construirParcial", e.getMessage()));
      return null;
      //Error.mensaje(this, e, "registrarImporteCuenta")
    }//end catch(Exception e)
  }//end public String construirAtributo  
  
   public String construirMesesEliminacion(String tipo, String fechaAfectacion){
     try{
       Fecha fecha = new Fecha(fechaAfectacion, "-");
       String descMes = sia.libs.formato.Fecha.getNombreMesCorto(fecha.getMes()-1);
       return descMes+"_"+tipo+"_eli";
     }//end try
     catch(Exception e){
       System.err.println(Error.getMensaje(this, "SIAFM", "construirParcial", e.getMessage()));
       return null;
       //Error.mensaje(this, e, "registrarImporteCuenta")
     }//end catch(Exception e)
   }//end public String construirAtributo  

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }
}
