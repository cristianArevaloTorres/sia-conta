<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
  cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
  String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
  String arbol = request.getParameter("arbol") == null ? "" : request.getParameter("arbol");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i004tesFiltroComprasInversion</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/funcres.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      function inicia() {
        if(document.getElementById("arbol").value=='')
          irBuscar();
      }
     
      function irBuscar() {
          llamamisma(0);
      }
      
      function llamamisma(parametro) {
        cuentaInversion = document.getElementById("fidCuentaInversion");
        fechaInversion = document.getElementById("ffecha");
          
            loadSource('dResultado',null,'i004tesComprasInversionFiltroCapaResultado.jsp?','idCuentaInversion='+cuentaInversion.value+'&fecha='+fechaInversion.value+'&param='+parametro);
      }
    </script>
  </head>
  <body onload="inicia()">
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversiones o compras","Filtro",true);%>
      <input type="hidden" id="arbol" name='arbol' value="<%=arbol%>"> 
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha de operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="ffecha" id="ffecha" value="<%=ffecha%>" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].ffecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='fidCuentaInversion' name='fidCuentaInversion' onchange='cambiaCuenta()' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO",fidCuentaInversion);%>
            </select></td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Buscar" onclick="irBuscar()"></td>
          </tr>
        </tbody>
      </table>
      <br>
      <div id="dResultado">
      
      </div>
    </form>
  </body>
</html>