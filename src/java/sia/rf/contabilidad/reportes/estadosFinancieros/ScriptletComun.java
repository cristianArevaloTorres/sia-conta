package sia.rf.contabilidad.reportes.estadosFinancieros;

import java.util.ArrayList;

import java.util.List;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.db.sql.Vista;

import sia.libs.periodo.Fecha;
import sia.libs.formato.Error;

import sia.scriptlets.Firmante;
import sia.scriptlets.ScriptletFirmas;
import sia.rf.contabilidad.reportes.Firmas;
import sia.rf.contabilidad.reportes.Entidad;

public class ScriptletComun extends Firmas {

  public void afterReportInit() throws JRScriptletException {
    super.afterReportInit();
    try {
      if (checkParameter("FECHA_CONSOLIDACION") != null) {
        Fecha fechaPeriodo = new Fecha(getParameterValue("FECHA_CONSOLIDACION").toString(), "/");
        String mes = sia.libs.formato.Fecha.getNombreMes(fechaPeriodo.getMes() - 1).toUpperCase();
        fechaPeriodo.getDiasEnElMes();
        String fecha = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
        fechaPeriodo.addMeses(-1);
        String mes_ant = sia.libs.formato.Fecha.getNombreMes(fechaPeriodo.getMes() - 1).toUpperCase();
        setVariableValue("MES", mes);
        setVariableValue("MES_ANT", mes_ant);
        setVariableValue("FECHA_MAY_CONSOLIDACION", fecha);
      }//if
      
      if (checkParameter("FECHA_MES_EJERCICIO_COMPARA") != null) {
        Fecha fechaPeriodo = new Fecha(getParameterValue("FECHA_CONSOLIDACION").toString(), "/");
        fechaPeriodo.getDiasEnElMes();
        String fecha = sia.libs.formato.Fecha.formatear(6, fechaPeriodo.getTerminoMes().toString()).toUpperCase();
        Fecha fechaPeriodoCompara = new Fecha(getParameterValue("FECHA_MES_EJERCICIO_COMPARA").toString(), "/");
        //fechaPeriodo.addMeses(-1);
        String fechaAnt = sia.libs.formato.Fecha.formatear(6, fechaPeriodoCompara.getTerminoMes().toString()).toUpperCase();
        setVariableValue("FECHA_MAY_CONSOLIDACION", fecha);
        setVariableValue("FECHA_CONSOLIDACION_ANT", fechaAnt);
        fecha = fecha.concat(" Y ").concat(fechaAnt);
        setVariableValue("PERIODO", fecha);
      } //if
    }//try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    }//catch
  }//afterReportInit

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
  
  public String getFechaFirma(int iEntidad, int ambito, int unidadEjecutora){
    Entidad entidad = null;
    String regresa = null;
    try{
      entidad = new Entidad();
      regresa = entidad.getEntidad(iEntidad,ambito,unidadEjecutora).concat(" a ").concat(getFecha());
    }
    catch(Exception e){
      e.printStackTrace();
    }
    return regresa;
  }
}

