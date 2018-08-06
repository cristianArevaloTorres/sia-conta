<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="bancosInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i003tesTasasRendimientoAgregar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script type="text/javascript" language="javascript">
       
      function cambiaCuenta(tis) {
        if (tis.value=='498'){
           document.getElementById('banxico').style.display='';
           document.getElementById('otros').style.display='none';
        }
        else{
            document.getElementById('banxico').style.display='none';
            document.getElementById('otros').style.display='';
        }
      }           
       
      
      function validarFormulario() {
        correcto = true;
        mensaje  = "----------- A L E R T A -------------\n" 
        if(document.getElementById("fecha").value=='') {
          correcto = false;
          mensaje += "Debe seleccionar una fecha\n";
        }
        if(document.getElementById("idBanco").value=='') {
          correcto = false;
          mensaje += "Debe seleccionar una institución bancaria\n";
        }
        if (document.getElementById('idBanco').value=='498')
            tasas = document.getElementsByName("tasab");
        else
            tasas = document.getElementsByName("tasa");
        for (i = 0; i < tasas.length; i++)  {
          if(tasas[i].value=='') {
            correcto = false;
            mensaje += "Son requeridas todas las tasas\n";
            break;
          }
        }
        
        if(!correcto)
          alert(mensaje);
         else
          aceptar();
        
      }
      
      function aceptar() {
        envf(document.getElementById('form1'));
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
      
      function validarFlotante(event,campo){
        //alert(event);
        tecla = (document.all) ? event.keyCode : event.which; 
        if(tecla!=46  && tecla !=8 && tecla !=0 && (tecla < 48 || tecla > 57 )) {
          (document.all) ? event.returnValue = false : event.preventDefault();
        }      
        if(tecla == 46){
          var punto = campo.value.indexOf(".",0)
          if (punto != -1){ 
            (document.all) ? event.returnValue = false : event.preventDefault();
          }
        }         
      }
      
    </script>
  </head>
  <%
    bancosInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"tasasRendimiento.select.RfTcBancosInversion-bancosConInversion.inversiones");
  %>
  <body>
    <form id='form1' name='form1' action='i003tesTasasRendimientoControl.jsp' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Tasas de rendimiento","Agregar",true);%>
      <input type="hidden" id="accion" name="accion" value="1">
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha de operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Instituci&oacute;n financiera</td>
            <td><select id='idBanco' name='idBanco' onchange='cambiaCuenta(this)' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(bancosInversion, out,"id_banco","NOMBRE_CORTO","");%>
            </select></td>
          </tr>
        </tbody>
      </table>
      <br>
      <table align='center'>
        <thead></thead>
        <tbody id="otros">
          <tr>
            <td>Plazos</td>
            <td align="center">1<input type="hidden" id="plazo" name="plazo" value="1"></td>
            <td align="center">7<input type="hidden" id="plazo" name="plazo" value="7"></td>
            <td align="center">14<input type="hidden" id="plazo" name="plazo" value="14"></td>
            <td align="center">21<input type="hidden" id="plazo" name="plazo" value="21"></td>
            <td align="center">28<input type="hidden" id="plazo" name="plazo" value="28"></td>
          </tr>
          <tr>
            <td>Tasas</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
          </tr>
        </tbody>
         <tbody id="banxico" style="display:none">
          <tr>
            <td>Documentos</td>
            <td align="center">Cetes<input type="hidden" id="plazob" name="plazob" value="1"></td>
            <td align="center">TIIE<input type="hidden" id="plazob" name="plazob" value="7"></td>
          </tr>
          <tr>
            <td>Tasas</td>
            <td><input type="text" class="cajaTexto" name="tasab" id="tasab" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <td><input type="text" class="cajaTexto" name="tasab" id="tasab" value="0.0" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
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
            <td><input type="button" class="boton" value="Guardar" onclick="validarFormulario()"></td>
            <td><input type="reset" class="boton" value="Cancelar" onclick=""></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>