package sia.rf.contabilidad.reportes;

import java.util.Iterator;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.rf.contabilidad.registroContable.servicios.Cuenta;
import sia.rf.contabilidad.registroContable.servicios.SubCuenta;
import sia.scriptlets.BarraProgreso;

public class PolizasCheque extends BarraProgreso {

  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();
    //ModuloRegistroContableImpl am = null;   
    try  {
      String poliza = "";
     /* am = (ModuloRegistroContableImpl)Configuration.createRootApplicationModule("sia.rf.contabilidad.registroContable.servicios.ModuloRegistroContable", 
        "ModuloRegistroContableLocal");      */
      Integer cuenta_contable_id = (Integer)checkField("CUENTA_CONTABLE_ID", new Integer(1));
      int tipoPolizaId = Integer.valueOf(getFieldValue("CLASIFICACION_POLIZA_ID").toString()); 
      
      Cuenta cuenta = new Cuenta(cuenta_contable_id, getConnection());
      DatosReportes datosReportes = new DatosReportes();
      StringBuffer sb = new StringBuffer();
      StringBuffer sbCuenta = new StringBuffer();
      StringBuffer sbSubCuenta = new StringBuffer();
      
      Iterator iterator = cuenta.getSubcuentas().iterator();    
       int i = 1;
      while (iterator.hasNext()) {
        SubCuenta subCuenta = (SubCuenta)iterator.next();
        
        if (i > 1){        
           sb.append("  ");              
           sbCuenta.append("  \n");
           sbSubCuenta.append(subCuenta.getCuentaCorta()== null?"":subCuenta.getCuentaCorta() + "\n");
        }
        else{
           sbCuenta.append(subCuenta.getCuentaCorta() == null?"":subCuenta.getCuentaCorta() + "\n");
           sbSubCuenta.append(" \n");
        }
        sb.append(subCuenta.getCuentaPadre().getCuentaContable());      
        sb.append("  ");
        sb.append("\n");
        i++;
      } // while
      if(tipoPolizaId == 1){
        this.setVariableValue("CLASIFICACION_POLIZA","");
      }
      else{
        if(checkField("POLIZA_REFERENCIA")!= null){
          poliza = datosReportes.getPolizaCancelada(getFieldValue("POLIZA_REFERENCIA").toString(),"consecutivo");//El segundo parametro es para indicar que campo se quiere del query
          this.setVariableValue("CLASIFICACION_POLIZA","De Cancelación a la póliza "+getFieldValue("UNIDAD_EJECUTORA").toString()+" "+getFieldValue("ENTAMB").toString()+poliza);
        }
        else{
          poliza="";
          this.setVariableValue("CLASIFICACION_POLIZA","Póliza Cancelada");
        }
      }
      this.setVariableValue("CUENTA", sbCuenta.toString());
      this.setVariableValue("SUBCUENTA",sbSubCuenta.toString());
      this.setVariableValue("DESCRIPCION", sb.toString());
      Integer tipoOperacion = (Integer)checkField("DEBE_HABER", new Integer(5));
      Double Importe = (Double)checkField("IMPORTE", 0.00);
      if (tipoOperacion == 0) {
          this.setVariableValue("DEBE", Importe.doubleValue());
          this.setVariableValue("HABER", 0.00);
      }
      else {
          this.setVariableValue("HABER", Importe.doubleValue());
          this.setVariableValue("DEBE", 0.00);
      } //if
    } 
    catch (Exception ex)  {
      ex.printStackTrace();
    }
    finally  {
     /* if(am !=null){
        Configuration.releaseRootApplicationModule(am, true);
      }
      am = null;      */          
    }
  }
  
 /* public String getPolizaCancelada(String polizaId){
    Sentencias sentencia = null;
    Map parametros = null;
    List<Vista> registros = null;
    String desCiudad = null;
    String regresa = null;
    try{
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      parametros = new HashMap();
      registros = new ArrayList<Vista>();
      parametros.put("polizaId",polizaId);
      registros = sentencia.registros("reportes.select.obtenerPolizaCancelada",parametros);
      if(registros!=null){
        for(Vista registro:registros){ 
          regresa = registro.getField("POLIZA");
        }
        
      }
    }
    catch(Exception e){
      e.printStackTrace();
    }
    finally{
      sentencia = null;
      registros = null;
      parametros = null;
    }
    return regresa;
  }*/
}