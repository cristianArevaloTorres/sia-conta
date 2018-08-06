<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<jsp:useBean id="compras" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005EfectivoInversionCapaCompras</title>
  </head>
  <%
    String idCuentaInversion = request.getParameter("idCuentaInversion");
    String fecha = request.getParameter("fecha");
    String idCompraInversion = request.getParameter("idCompraInversion") == null ? "" : request.getParameter("idCompraInversion");
    compras.addParamVal("idCuentaInversion","and ci.id_cuenta_inversion = :param",idCuentaInversion);
    compras.addParamVal("fecha","and to_char(ci.fecha,'dd/mm/yyyy') = ':param'",fecha);
    compras.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrComprasInversion-AsociaEfectivo.inversiones");
  %>
  <body onload="parent.loadSourceFinish('dCompras'); parent.regresaDeCompras();">
    <select id="idCompraInversion" name="idCompraInversion" class="cajaTexto">
      <%CRSComboBox(compras, out,"ID_COMPRA_INVERSION","NUM_OPERACION",idCompraInversion);%>
    </select>
  </body>
</html>