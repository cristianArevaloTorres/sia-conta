<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro,sia.rf.contabilidad.registroContable.servicios.CuentaMayor,sia.db.dao.DaoFactory,java.sql.*"%>
<%@ page import="sia.libs.formato.Error"%>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>

<%@ page contentType="text/html;charset=windows-1252"%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>controlRegistro</title>
  </head>
  <body onload="parent.loadSourceFinish('controlRegistro');parent.enviar()"></body>
<%
  Connection conexion = null;
  try{
  conexion = DaoFactory.getContabilidad();
  ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  controlReg.setUnidad(request.getParameter("unidad"));
//  System.out.println("unidad: " + request.getParameter("unidad"));
  if ( controlReg.getFechaAfectacion()==null){
     fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
  }   
  if(request.getParameter("ambito").endsWith("0")){
//      System.out.println(request.getParameter("ambito"));
    controlReg.setAmbito(Integer.valueOf(request.getParameter("ambito")));       }
     else 
    controlReg.setAmbito(Integer.valueOf(request.getParameter("ambito").substring(3)));
  if(request.getParameter("entidad").endsWith("00")){
    controlReg.setEntidad(Integer.valueOf(request.getParameter("entidad")));}
  else
    controlReg.setEntidad(Integer.valueOf(request.getParameter("entidad").substring(3)));
  controlReg.setPais(147);
  controlReg.setEjercicio(Integer.valueOf(request.getParameter("ejercicio")));
  String idCatCta = request.getParameter("idCatalogoCuenta");
  idCatCta = idCatCta==null || idCatCta.isEmpty() || idCatCta.equalsIgnoreCase("null")?"1":idCatCta;
  controlReg.setIdCatalogoCuenta(Integer.valueOf(idCatCta));
  CuentaMayor CM=new CuentaMayor(controlReg.getEjercicio(),conexion);
  controlReg.setUniEjecFormateada(CM.rellenarCadena(controlReg.getUnidad(),conexion));
  controlReg.setAmbEntFormateada(CM.rellenarCadena(String.valueOf(controlReg.getEntidad()),String.valueOf(controlReg.getAmbito()),conexion));
  }
  catch (Exception e){
      e.printStackTrace();
      Error.mensaje(e, "CONTABILIDAD");
  }//end catch
  finally{
    if (conexion!=null){
       conexion.close();
       conexion=null;
    }
  }
%>
</html>