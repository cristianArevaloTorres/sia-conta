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
    <title>i005tesEfectivoInversionAgregar</title>
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
      
      function cancelar(pagina) {
        document.getElementById("form1").action = pagina;
        document.getElementById("form1").submit();
      }
      
      function ir(pagina) {
        if(validarFormulario()) {
          document.getElementById("form1").action = pagina;
          envf(document.getElementById("form1"));
        }
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
      
      function validarFormulario() {
        mensaje = '-------------- A L E R T --------------------\n';
        correcto= true;
        if(document.getElementById("fecha").value=='') {
          mensaje += 'Debe seleccionar una fecha\n';
          correcto = false;
        }
        if(document.getElementById("idCuentaInversion").value=='') {
          mensaje += 'Debe seleccionar una cuenta de inversion\n';
          correcto = false;
        }
        if(document.getElementById("importe").value=='') {
          mensaje += 'Debe capturar el saldo de efectivo\n';
          correcto = false;
        }
        if(!correcto)
          alert(mensaje);
        return correcto;
      }
    </script>
  </head>
  <body>
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversion de efectivo - registrar inicio de dep&oacute;sito","Agregar",true);%>
      <input type="hidden" id="arbol" name='arbol' value="<%=arbol%>"> 
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha inicio de dep&oacute;sito</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="" readonly size="13" onblur="llamaSaldo()"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='idCuentaInversion' name='idCuentaInversion' onchange='llamaSaldo()' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO","");%>
            </select></td>
          </tr>
          <tr>
            <td>Compra asociada</td>
            
            <td><div id="dCompras">
              <select name='idCompraInversion' id='idCompraInversion' class='cajaTexto'>
                <option value=''>-Seleccione-</option>
              </select>
            </div></td>
          </tr>
          <tr>
            <td>Saldo de efectivo</td>
            <td><div id="divSaldo"><input type="text" class="cajaTexto" name="importe" id="importe" value="" readonly size="15" maxlength="17"> </div></td>
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
            <td><input type="button" class="boton" value="Cancelar" onclick="cancelar('i005tesEfectivoInversionAgregar.jsp')"></td>
          </tr>
        </tbody>
      </table>
      <br>
    </form>
  </body>
</html>