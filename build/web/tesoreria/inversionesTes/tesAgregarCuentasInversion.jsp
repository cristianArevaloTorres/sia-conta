<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Error" %>
<%@ page import="java.sql.*" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="rfTrCuentasInversion" class="sia.rf.tesoreria.registro.cuentasInversion.bcRfTrCuentasInversion" scope="page"/>
<jsp:useBean id="crsBancosInv" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesModificarCuentasInversion</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    
    <script language="javascript" type="text/javascript">

      function validarFormulario() {
        fechaApertura = document.getElementById('fechaApertura');
        idBanco = document.getElementById('idBanco');
        contratoCuenta = document.getElementById('contratoCuenta');
        var mensaje = "------- Alerta -------\n";
        var correcto = true;
        if (fechaApertura.value == ""){
          mensaje = mensaje + "Debe seleccionar una fecha\n";
          correcto = false;
        }
        if (idBanco.value == ""){
          mensaje = mensaje + "Debe seleccionar una Institución bancaria\n";
          correcto = false;
        }
        if (contratoCuenta.value == ""){
          mensaje = mensaje + "Debe capturar un numero de cuenta o contrato\n";
          correcto = false;
        }
        if (!correcto){
          alert(mensaje);
        } else {
          //return correcto;
        }
        return correcto;
      }
      
      function irFiltro() {
        form1.action = "tesFiltroCuentasInversion.jsp";
        envf(form1);
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
  </head>
  <%
    crsBancosInv.registrosMap(DaoFactory.CONEXION_TESORERIA,"cuentasInversion.select.RfTcBancosInversion.inversiones");
  %>
  <body>
    
    <form id="form1" name="form1" action="tesCuentasInversionControl.jsp" method="POST">
    <%util.tituloPagina(out,"Tesorería","Cuentas de inversión","Agregar",true);%>
    <input type="hidden" value="<%=request.getParameter("fBancoInv")%>" id="fBancoInv" name="fBancoInv">
    <input type="hidden" value="1" id="accion" name="accion">
   
 
    <table align="center" cellpadding="5">
      <thead></thead>
      <tbody>
        <tr>
          <td class="negrita">Fecha de apertura</td>
          <td>
            <input type="text" name="fechaApertura" id="fechaApertura" value="" class="cajaTexto">
            <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaApertura')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
        <tr>
          <td class="negrita">Institución bancaria</td>
          <td>
           <select class="cajaTexto" id="idBanco" name="idBanco"> 
              <option value=''>- Seleccione -</option>
              <%CRSComboBox(crsBancosInv, out,"ID_BANCO","NOMBRE_CORTO","");%>
            </select>
          </td>
        </tr>
        <tr>
          <td class="negrita">No. de cuenta o contrato</td>
          <td>
            <input type="text" name="contratoCuenta" id="contratoCuenta" value=""  class="cajaTexto">
          </td>
        </tr>
      </tbody>
    </table>
    <br>
    <hr class="piePagina">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td><input type="submit" class="boton" value="Aceptar" onclick="return validarFormulario()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="irFiltro()"</td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>