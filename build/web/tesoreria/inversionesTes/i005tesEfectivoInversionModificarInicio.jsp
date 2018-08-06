<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.rf.tesoreria.registro.efectivoInversion.bcRfTrEfectivoInversion" %>
<%@ page import="sia.libs.formato.Numero" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="compras" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
  bcRfTrEfectivoInversion efectivo=null;
  String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
  try  {
    efectivo = new bcRfTrEfectivoInversion();
    efectivo.setIdEfectivoInversion(request.getParameter("clave"));
    efectivo.select(DaoFactory.CONEXION_TESORERIA);
  
    cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
    
    String arbol = request.getParameter("arbol") == null ? "" : request.getParameter("arbol");  
    
    compras.addParamVal("idCuentaInversion","and ci.id_cuenta_inversion = :param", efectivo.getIdCuentaInversion());
    compras.addParamVal("fecha","and to_char(ci.fecha,'dd/mm/yyyy') = ':param'",efectivo.getFecha());
    compras.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrComprasInversion-AsociaEfectivo.inversiones");

  } catch (Exception ex)  {
    ex.printStackTrace();
  } finally  {
  }
  
  
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005tesEfectivoInversionModificarInicio</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      
      function llamaSaldo() {
        cuentaInversion = document.getElementById("idCuentaInversion");
        fechaInversion = document.getElementById("fecha");
        if(cuentaInversion.value!='' && fechaInversion.value!='')  
            loadSource('dResultado',null,'i005tesEfectivoInversionCapaSaldo.jsp?','idCuentaInversion='+cuentaInversion.value+'&fecha='+fechaInversion.value);
      }
      
      function ir(pagina) {
        document.getElementById("form1").action = pagina;
        envf(document.getElementById("form1"));
      }
      
      var jsenv = false;
      function envf(frm) {
        if(!jsenv) {
          frm.submit();
          jsenv = true;
        }
        else
          alert('Cuide su información, evite dar doble click en la página');
      }
      
      function regresaDeSaldo() {
        llamaCompras();
      }
      
      function llamaCompras() {
        cuentaInversion = document.getElementById("idCuentaInversion");
        fechaInversion = document.getElementById("fecha");
        if(cuentaInversion.value!='' && fechaInversion.value!='')  
            loadSource('dCompras',null,'i005EfectivoInversionCapaCompras.jsp?','idCuentaInversion='+cuentaInversion.value+'&fecha='+fechaInversion.value);
      }
      
      function regresaDeCompras() {
        
      }
    </script>
  </head>
  <body>
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversion de efectivo - modificar inicio de dep&oacute;sito","Modificar",true);%>
      <input type="hidden" id="idEfectivoInversion" name="idEfectivoInversion" value="<%=request.getParameter("clave")%>">
      <input type="hidden" id="accion" name="accion" value="1">
      <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
      <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha inicio de dep&oacute;sito</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="<%=efectivo.getFecha()%>" readonly size="13" onchange="llamaSaldo()"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='idCuentaInversion' name='idCuentaInversion' onchange='llamaSaldo()' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO",efectivo.getIdCuentaInversion());%>
            </select></td>
          </tr>
          <tr>
            <td>Compra asociada</td>
            <td><div id="dCompras">
              <select id="idCompraInversion" name="idCompraInversion" class="cajaTexto">
                <%CRSComboBox(compras, out,"ID_COMPRA_INVERSION","NUM_OPERACION",efectivo.getIdComprasInversion());%>
              </select>
            </div></td>
          </tr>
          <tr>
            <td>Saldo de efectivo</td>
            <td><div id="divSaldo"><input type="text" style="text-align:right" class="cajaTexto" name="importe" id="importe" value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,Double.parseDouble(efectivo.getImporte()))%>" size="15" maxlength="17"> </div></td>
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
            <td><input type="button" class="boton" value="Aceptar" onclick="ir('i005tesEfectivoInversionControl.jsp')"></td>
            <td><input type="button" class="boton" value="Cencelar" onclick="ir('i005tesEfectivoInversionFiltro.jsp')"></td>
          </tr>
        </tbody>
      </table>
      <br>
    </form>
  </body>
</html>