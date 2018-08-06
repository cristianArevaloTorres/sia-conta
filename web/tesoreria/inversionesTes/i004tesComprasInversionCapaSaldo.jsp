
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Numero" %>
<jsp:useBean id="bcRfTrOperacionesInversion" class="sia.rf.tesoreria.registro.operacionesInversion.bcOperacionesInversionCalculo" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i004tesComprasInversionCapaSaldo</title>
  </head>
  <%
    String idCompraInversion = request.getParameter("idCuentaInversion");
    String fecha = request.getParameter("fecha");
    String nombreCuenta = request.getParameter("nombreCuenta");
    double monto = bcRfTrOperacionesInversion.getSaldoAnteriorMismaFechaOAnterior(idCompraInversion,fecha);
    boolean isSaldo = monto!=0.00;
  %>
  <body onload="parent.loadSourceFinish('capaSaldo'); parent.regresaValidarMonto(<%=isSaldo%>)">
  
    El saldo disponible en la cuenta <%=nombreCuenta%> es <div id="divMonto"><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,monto)%></div> 
    <input type="hidden" id="ihsaldo" name="ihsaldo" value="<%=monto%>">
  </body>
</html>