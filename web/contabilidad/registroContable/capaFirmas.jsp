<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista,java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page contentType="text/html;charset=windows-1252"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>capaFirmas</title>
  </head>
  
  <BODY OnLoad ="parent.loadSourceFinish('firmas', parent.bufferCurp);parent.cargarDatos();">  

<%

ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
Map parametros = null;
//String documento = request.getParameter("documento");
String mes = request.getParameter("mes");
List<Vista> registros = new ArrayList<Vista>();
Sentencias sentencia = null;  
try{
  parametros = new HashMap();
  sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  parametros.put("firma","ELB");
  parametros.put("unidad",controlReg.getUnidad());
  parametros.put("entidad",controlReg.getEntidad());
  parametros.put("ambito",controlReg.getAmbito());
  parametros.put("ejercicio",controlReg.getEjercicio());
  //parametros.put("documento", request.getParameter("documento")==null?"BCO":request.getParameter("documento"));
  if(request.getParameter("documento") != null)
      parametros.put("documento", request.getParameter("documento"));
  
  parametros.put("mes", mes);
    
  registros = sentencia.registros("reportes.select.firmas",parametros);
  if(registros!=null){
%>
<table width="30%" align="center">
<tr class="azulCla">
  <td>Firmas</td>
</tr>
</table><br>
<table width="30%" align="center">
<tr>
  <td align="right" class='negrita'>ELABORÓ:</td>
  <td><select id="elaboro" name="elaboro" class="cajaTexto">
<%  for (Vista record : registros) {%>
       <option value="<%=record.getField("NUM_EMPLEADO")%>"><%=record.getField("NOMBRE")%></option>
<%  }%>
      </select></td>
    </tr>
<%
}
  registros = null;
  registros = new ArrayList<Vista>();
  parametros.put("firma","REV");
  registros = sentencia.registros("reportes.select.firmas",parametros);
  if(registros!=null){
%>
<tr>
  <td align="right" class='negrita'>REVISÓ:</td>
  <td><select id="reviso" name="reviso" class="cajaTexto">
<%  for (Vista record : registros) {%>
       <option value="<%=record.getField("NUM_EMPLEADO")%>"><%=record.getField("NOMBRE")%></option>
<%}%>
      </select></td>
    </tr>
<%
}
  registros = null;
  registros = new ArrayList<Vista>();
  parametros.put("firma","AUT");
  registros = sentencia.registros("reportes.select.firmas",parametros);
  if(registros!=null){
%>
<tr>
  <td align="right" class='negrita'>AUTORIZÓ:</td>
  <td><select id="autorizo" name="autorizo" class="cajaTexto">
<%  for (Vista record : registros) {%>
       <option value="<%=record.getField("NUM_EMPLEADO")%>"><%=record.getField("NOMBRE")%></option>
<%}%>
      </select></td>
    </tr>
<%
}
  }//end try
  catch(Exception e){
    System.err.println("Error al consultar las firmas");
  }     
%>
</table>
</body>
</html>