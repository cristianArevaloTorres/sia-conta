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
  String accion = request.getParameter("accion");
  String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
  try  {
    efectivo = new bcRfTrEfectivoInversion();
    efectivo.setIdEfectivoInversion(request.getParameter("clave"));
    efectivo.select(DaoFactory.CONEXION_TESORERIA);
  
    cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
    compras.addParamVal("idCuentaInversion","and ci.id_cuenta_inversion = :param", efectivo.getIdCuentaInversion());
    compras.addParamVal("fecha","and to_char(ci.fecha,'dd/mm/yyyy') = ':param'",efectivo.getFecha());
    compras.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrComprasInversion-AsociaEfectivo.inversiones");
    String arbol = request.getParameter("arbol") == null ? "" : request.getParameter("arbol");  
  } catch (Exception ex)  {
    ex.printStackTrace();
  } finally  {
  }
  
  
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005tesEfectivoInversionRendimiento</title>
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
        //alert(document.getElementById('accion').value);
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
    </script>
    <script type="text/javascript" language="javascript">
      
      function validarFormulario() {
        mensaje = "------------ ALERTA ---------------\n";
        correcto= true;
        if(document.getElementById("vincimiento").value == '') {
          mensaje += "Debe seleccionar la fecha de vencimiento\n";
          correcto = false;
        }
        if(document.getElementById("rendimiento").value == '') {
          mensaje += "Debe capturar el rendimiento obtenido\n";
          correcto = false;
        }
        if(!correcto)
          alert(mensaje);
        else
          ir('i005tesEfectivoInversionControl.jsp');
      }
    </script>
  </head>
  <body>
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversion de efectivo - registro de rendimiento","Registar / Modificar",true);%>
      <input type="hidden" id="idEfectivoInversion" name="idEfectivoInversion" value="<%=request.getParameter("clave")%>">
      <input type="hidden" id="accion" name="accion" value="<%=request.getParameter("accion")%>">
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
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="<%=efectivo.getFecha()%>" disabled size="13" onchange="llamaSaldo()"> &nbsp; 
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></td>
          </tr>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='' name='' onchange='llamaSaldo()' class='cajaTexto' disabled>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO",efectivo.getIdCuentaInversion());%>
              </select>
              <input type="hidden" id="idCuentaInversion" name="idCuentaInversion" value="<%=efectivo.getIdCuentaInversion()%>">
            </td>
          </tr>
          <tr>
            <td>Compra asociada</td>
            <td>
              <select id="idCompraInversion" name="idCompraInversion" class="cajaTexto" disabled>
                <%CRSComboBox(compras, out,"ID_COMPRA_INVERSION","NUM_OPERACION",efectivo.getIdComprasInversion());%>
              </select>
            </td>
          </tr>
          <tr>
            <td>Saldo de efectivo</td>
            <td><div id="divSaldo"><input type="text" style="text-align:right" class="cajaTexto" name="" id="" disabled value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,Double.parseDouble(efectivo.getImporte()))%>" readonly size="15" maxlength="17"> 
                <input type="hidden" id="importe" name="importe" value="<%=efectivo.getImporte()%>">  </div></td>
          </tr>
          <tr>
            <td>Fecha de vencimiento inicio de dep&oacute;sito</td>
            <td><input type="text" class="cajaTexto" name="vincimiento" id="vincimiento" value="<%=efectivo.getVincimiento()%>" readonly size="13" onchange="llamaSaldo()"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].vincimiento')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Rendimiento de dep&oacute;sito efectivo</td>
            <td><div id="divSaldo"><input type="text" style="text-align:right" class="cajaTexto" name="rendimiento" id="rendimiento" value="<%=efectivo.getRendimiento().equals("")?"":Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,Double.parseDouble(efectivo.getRendimiento()))%>" size="15" maxlength="17"> </div></td>
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
            <td><input type="button" class="boton" value="Aceptar" onclick="validarFormulario()"></td>
            <td><input type="button" class="boton" value="Cencelar" onclick="ir('i005tesEfectivoInversionFiltro.jsp')"></td>
          </tr>
        </tbody>
      </table>
      <br>
    </form>
  </body>
</html>