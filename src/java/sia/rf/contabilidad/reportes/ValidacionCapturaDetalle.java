package sia.rf.contabilidad.reportes;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import net.sf.jasperreports.engine.JRScriptletException;
import sia.scriptlets.BarraProgreso;
import sia.libs.formato.Error;

public class ValidacionCapturaDetalle extends BarraProgreso {
 
  ArrayList detalle;
  
  public void beforeReportInit() throws JRScriptletException {
    super.beforeReportInit();
    String querySubreporte = null;
    try {
      querySubreporte = getParameterValue("QUERY_SUBREPORTE").toString();      
      detalle = vaciarResultados(querySubreporte);      
    } catch (Exception e) {
       Error.mensaje("SIAFM",e);
    }
  }
 
  public void afterDetailEval() throws JRScriptletException {
    super.afterDetailEval();     
    String fecha = null;
    String tipoPoliza = null;
    Double importe = null;    
    int operacionContable = 0;
    boolean imprime = true;
    String poliza = "";
    try  {      
      DatosReportes datosReportes = new DatosReportes();
      fecha = getFieldValue("FECHA_AFECTACION").toString();
      tipoPoliza = getFieldValue("ABREVIATURA").toString();
      importe = (Double)getFieldValue("IMPORTE");      
      operacionContable = ((Integer)getFieldValue("OPERACION_CONTABLE_ID")).intValue();
      int tipoPolizaId = Integer.valueOf(getFieldValue("CLASIFICACION_POLIZA_ID").toString());
      /*DetallePolizaValidacion det = new DetallePolizaValidacion();
      for (int i = 0; i < detalle.size(); i++)  {        
        det = (DetallePolizaValidacion)detalle.get(i);
        if ( (det.getFecha().equals(fecha)) && (importe.equals(det.getMonto())) ) {
          if ( (det.getTipoOperacion() == 0 && operacionContable == 1 ) || (det.getTipoOperacion() == 1 && operacionContable == 0) )        
            if ( (det.getTipoPoliza().equals("D") && tipoPoliza.equals("E")) || ( det.getTipoPoliza().equals("D") && tipoPoliza.equals("I")) ||
                 (det.getTipoPoliza().equals("E") && tipoPoliza.equals("D")) || ( det.getTipoPoliza().equals("I") && tipoPoliza.equals("D")) ) {
              imprime = false;
              detalle.remove(i);
            } // if final
        } // if principal    
      } //for      **/
      setVariableValue("IMPRIME",imprime);
      
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
    catch (Exception e)  {
      Error.mensaje("SIAFM",e);
    }         
  }    
  
  public ArrayList vaciarResultados (String queryDiferencias) throws Exception{
    Statement stm        = null;
    ResultSet rst = null;
    ArrayList detalle = new ArrayList();
    String fecha = null;
    String tipoPoliza = null;
    Double importe = null;
    int operacionContable = 0;
    try  {      
      stm= getConnection().createStatement();
      rst= stm.executeQuery(queryDiferencias);
      while (rst.next()) {
        fecha = rst.getString("FECHA_AFECTACION");        
        tipoPoliza = rst.getString("ABREVIATURA");
        importe = rst.getDouble("IMPORTE");
        operacionContable = rst.getInt("OPERACION_CONTABLE_ID");
        detalle.add(new DetallePolizaValidacion(fecha,tipoPoliza,importe,operacionContable));
      }      
    } 
    catch (Exception e)  {
      Error.mensaje("SIAFM",e);
    } 
    finally  {
      if (rst!= null) 
        rst.close();
      rst= null;        
      if (stm!= null)
        stm.close();
      stm= null;      
    }    
    return detalle;
  }
  
}
