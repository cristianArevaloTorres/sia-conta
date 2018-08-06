<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentenciasCRS" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsEmpleados" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c715CapaAgregarFirma</title>
  </head>
  <body onload="parent.loadSourceFinish('divAgregar');parent.regresaAgregar()">
  <%
    String numEmpleado = request.getParameter("numEmpleado");
    String mes= request.getParameter("mes");
    boolean empleadoValido = false;
   try{
      crsEmpleados.addParamVal("numEmpleado","and e.num_empleado = :param",numEmpleado);
      crsEmpleados.addParamVal("mes","and fa.mes = :param",mes);
      crsEmpleados.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "firmasAutorizadas.select.RfTcFirmasAutorizadas");
      if(crsEmpleados.next()) 
        empleadoValido = true;
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    if(empleadoValido) {
  %>
   <input type="hidden" id="hprefijo" name="hprefijo" value="<%=crsEmpleados.getString("prefijo")==null?"":crsEmpleados.getString("prefijo")%>">
   <input type="hidden" id="hnombres" name="hnombres" value="<%=crsEmpleados.getString("nombres")==null?"":crsEmpleados.getString("nombres")%>">
   <input type="hidden" id="hapellidoPat" name="hapellidoPat" value="<%=crsEmpleados.getString("apellido_pat")==null?"":crsEmpleados.getString("apellido_pat")%>">
   <input type="hidden" id="hapellidoMat" name="hapellidoMat" value="<%=crsEmpleados.getString("apellido_mat")==null?"":crsEmpleados.getString("apellido_mat")%>">
   <input type="hidden" id="hpuesto" name="hpuesto" value="<%=crsEmpleados.getString("puesto")==null?"":crsEmpleados.getString("puesto")%>">
   <input type="hidden" id="hnumEmpleado" name="hnumEmpleado" value="<%=crsEmpleados.getString("num_empleado")==null?"":crsEmpleados.getString("num_empleado")%>">
  <%}%>
</html>