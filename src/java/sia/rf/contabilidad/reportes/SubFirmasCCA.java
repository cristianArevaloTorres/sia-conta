package sia.rf.contabilidad.reportes;

import java.sql.SQLException;

import net.sf.jasperreports.engine.JRScriptletException;

import sia.libs.formato.Error;

import sia.scriptlets.Firmante;

public class SubFirmasCCA extends Firmas {
  
  public void afterDetailEval() throws JRScriptletException{
    super.afterDetailEval();
    Firmante firmante = null;
    try {
      //if (checkParameter("AUTORIZO") != null && checkParameter("REVISO") != null &&
      //    checkParameter("ELABORO") != null) {
       if (checkParameter("SRF") != null){
        firmante = getFirma("SRF",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("SRF").toString());
        setVariableValue("SRF_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
        setVariableValue("SRF_AREA", firmante==null?"":firmante.getAreaAdscripcion());
       }
      if (checkParameter("JRF")!= null){
        firmante = getFirma("JRF",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("JRF").toString());
        setVariableValue("JRF_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
        setVariableValue("JRF_AREA", firmante==null?"":firmante.getAreaAdscripcion());
      }
        if (checkParameter("SRM") != null){
         firmante = getFirma("SRM",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("SRM").toString());
         setVariableValue("SRM_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
         setVariableValue("SRM_AREA", firmante==null?"":firmante.getAreaAdscripcion());
        }
        if (checkParameter("ARM")!= null){
         firmante = getFirma("ARM",getParameterValue("UNIDAD_EJECUTORA").toString(),getParameterValue("ENTIDAD").toString(),getParameterValue("AMBITO").toString(),getParameterValue("EJERCICIO").toString(),getParameterValue("MES").toString(),getParameterValue("DOCUMENTO").toString(),getParameterValue("ARM").toString());
         setVariableValue("ARM_EMPLEADO", firmante==null?"":firmante.getNombreEmpleado());
         setVariableValue("ARM_AREA", firmante==null?"":firmante.getAreaAdscripcion());
        }

     //} //if
       setVariableValue("FIRMA_FECHA", getFechaFirma(Integer.valueOf(getParameterValue("ENTIDAD").toString()),
                                        Integer.valueOf(getParameterValue("AMBITO").toString()),
                                        Integer.valueOf(getParameterValue("UNIDAD_EJECUTORA").toString())));
       
    } //try
    catch (JRScriptletException e) {
      Error.mensaje(e, "SIAFM");
    } //catch
  } //afterDetailEval
  
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
