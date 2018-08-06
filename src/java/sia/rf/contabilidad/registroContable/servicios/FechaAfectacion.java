package sia.rf.contabilidad.registroContable.servicios;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import javax.servlet.http.HttpServletRequest;
import sia.libs.formato.Error;
import sia.libs.recurso.Contabilidad;

import sia.rf.contabilidad.registroContable.acciones.ControlRegistro;


import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;


public class FechaAfectacion   {

  public String obtenerFechaAfectacion() {
    Sentencias sentencia = null;
    //UtileriasComun utilerias = new UtileriasComun("sia.rf.contabilidad.registroContable.servicios","ModuloRegistroContable");
    //ViewObject vistaFechaAfectacion = null;
    //StringBuffer parametros = new StringBuffer();   
    String fechaAfectacion = null;
    String parametros = null;
    try {
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      fechaAfectacion = sentencia.consultar(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerFechaAfectacion")).toString();
        //utilerias.removerVistas("Nombrex");
        //vistaFechaAfectacion =  utilerias.createViewObjectFromSentencia(Contabilidad.getInstance().getPropiedad("procesos.polizas.obtenerFechaAfectacion"));
        //fechaAfectacion = vistaFechaAfectacion.last().getAttribute("FECHAAFECTACION").toString();        
    }//try 
    catch (Exception e) {
      System.err.println("error[sia.rf.contabilidad.registroContable.servicios.FechaAfectacion.obtenerFechaAfectacion]");
      e.printStackTrace();
    }//catch 
    finally {
      /*utilerias.removerVistas("Nombrex");
      vistaFechaAfectacion = null;
      parametros = null;*/
      sentencia = null;
    }//finally
    return fechaAfectacion;
  }

  public void establecerFechaAfectacion(HttpServletRequest request, String fechaAfectacion) {
   ControlRegistro controlRegistro=(ControlRegistro)request.getSession().getAttribute("controlRegistro");
   controlRegistro.setFechaAfectacion(fechaAfectacion);
  }
  
  
  
  
  
  public boolean obtenerEstatusMes(String mes, String ejercicio, String unidadEjecutora, String entidad, String ambito, int idCatalogoCuenta){
    boolean regresar = false;
    Sentencias sentencia = null;
    Map parametros = null;
    List<Vista> registros = null;
    try{
      registros = new ArrayList();
      parametros = new HashMap();
      parametros.put("mes",mes);
      parametros.put("ejercicio",ejercicio);
      parametros.put("unidadEjecutora",unidadEjecutora);
      parametros.put("entidad",entidad);
      parametros.put("ambito",ambito);
      parametros.put("idCatalogoCuenta",idCatalogoCuenta);
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      registros = sentencia.registros("webService.select.estatusMes",parametros);
      if(registros != null){
          regresar = true;
          for(Vista registro:registros){
              if(registro.getField("ESTATUS_CIERRE_ID").equals("2"))
                regresar=false;
          }
      }
      
    }
    catch (Exception e){
      Error.mensaje(e,"CONTABILIDAD");    
    }
    finally{
      sentencia = null;
      parametros = null;
      registros = null;
    }
    return regresar;
  }
  
  public boolean obtenerEstatusMes(int ejercicio, int mes, int estatusMes){
    boolean regresar = false;
    Sentencias sentencia = null;
    Map parametros = null;
    List<Vista> registros = null;
    try{
      registros = new ArrayList();
      parametros = new HashMap();
      parametros.put("mes",mes);
      parametros.put("ejercicio",ejercicio);
      parametros.put("estatusCierre",estatusMes);
      sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
      registros = sentencia.registros("banquito.select.estatusCierreMes",parametros);
      if(registros != null){
          regresar = true;
      }
    }
    catch (Exception e){
      Error.mensaje(e,"CONTABILIDAD");    
    }
    finally{
      sentencia = null;
      parametros = null;
      registros = null;
    }
    return regresar;
  }
  
}
