<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
  cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i002tesDepositosRetirosAgregar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      var enviado = false;
      
      function redondear(num, dec) {
          var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
          return result;
      }
    
      function ponerComas(nStr)
      {
        nStr += '';
        x = nStr.split('.');
        x1 = x[0];
        x2 = x.length > 1 ? '.' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
          x1 = x1.replace(rgx, '$1' + ',' + '$2');
        }
        return x1 + x2;
      }
      
      function quitarComas(valor) {    
          var re = /,/g;
          return valor.replace(re,"");        
      }
      
      function formatear(tis) {
        tis.value = quitarComas(tis.value);
        tis.value = ponerComas(redondear(tis.value,2));
      }
    
      function cancelar(pagina) {
        document.getElementById("form1").action = pagina;
        document.getElementById("form1").target = "_self";
        enviar();
      }
      
      function ir(pagina) {
        if(validarFormulario()) {
          document.getElementById("form1").action = pagina;
          document.getElementById("form1").target = "_blnak";
          enviar();
        }
      }
      
      function regresaDeControl(val) {
        if(val==true) {
          document.getElementById("form1").action='i002tesDepositosRetirosAgregar.jsp';
          document.getElementById("form1").target='_self';
          document.getElementById("form1").submit();
        } else 
          enviado = false;
      }
      
      function enviar() {
        if(!enviado) {
          document.getElementById("form1").submit();
          enviado = true;
        }
      }
      
      function validarFormulario() {
        mensaje = '------------- A L E R T A ---------------\n';
        correcto= true;
        if(document.getElementById("fecha").value == '') {
          mensaje  += 'Debe seleccionar una fecha de operacion\n';
          correcto  = false;
        }
        if(document.getElementById("idCuentaInversion").value == '') {
          mensaje  += 'Debe seleccionar una cuenta de inversion\n';
          correcto  = false;
        }
        if(document.getElementById("importe").value == '') {
          mensaje  += 'Debe seleccionar una fecha de operacion\n';
          correcto  = false;
        }
        if(!correcto) 
          alert(mensaje);
        return correcto;
      }
      
      
      
    </script>
  </head>
  <body>
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Dep&oacute;sitos o retiros de efectivo","Agregar",true);%>
      <input type="hidden" id="accion" name="accion" value="1">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha de la operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='idCuentaInversion' name='idCuentaInversion' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO","");%>
            </select></td>
          </tr>
          <tr>
            <td>Tipo operación</td>
            <td nowrap="true"><input type="radio" name="idTipoOperacion" id="idTipoOperacion" value="1" checked>Dep&oacute;sito
                              <input type="radio" name="idTipoOperacion" id="idTipoOperacion" value="2">Retiro</td>
          </tr>
          <tr>
            <td>Valor de la opraci&oacute;n</td>
            <td><div id="divSaldo"><input type="text" class="cajaTexto" name="importe" id="importe" value="" size="15" maxlength="17" style="text-align:right" onchange="formatear(this)"> </div></td>
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
            <td><input type="button" class="boton" value="Aceptar" onclick="ir('i002tesDepositosRetirosControl.jsp')"></td>
            <td><input type="button" class="boton" value="Cencelar" onclick="cancelar('i002tesDepositosRetirosAgregar.jsp')"></td>
          </tr>
        </tbody>
      </table>
      <br>
    </form>
  </body>
</html>