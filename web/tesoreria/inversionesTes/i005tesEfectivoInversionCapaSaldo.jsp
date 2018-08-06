<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Numero" %>
<jsp:useBean id="bcRfTrOperacionesInversion" class="sia.rf.tesoreria.registro.operacionesInversion.bcOperacionesInversionCalculo" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005tesEfectivoInversionCapaSaldo</title>
  </head>
  <%
    String idCompraInversion = request.getParameter("idCuentaInversion");
    String fecha = request.getParameter("fecha");
    double monto = bcRfTrOperacionesInversion.getSaldoAnteriorMismaFechaOAnterior(idCompraInversion,fecha);
    boolean isSaldo = monto!=0.00;
  %>
  <body onload="parent.loadSourceFinish('divSaldo'); parent.regresaDeSaldo();">
  
    <input type="text" style="text-align:right" class="cajaTexto" name="importe" id="importe" value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,monto)%>"> 
  </body>
</html>