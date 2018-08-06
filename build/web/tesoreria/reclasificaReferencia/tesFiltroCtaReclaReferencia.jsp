<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.libs.formato.Fecha" %>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsCuentaBancaria" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <title>tesFiltroCtaReclaReferencia</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
    </script>
    <script language="JavaScript"  type="text/javascript">
      
      function verificaFormulario(){
        correcto=true;
        mensaje='------  ALERTA  ------ \n';
        if(document.getElementById('cuentas').value==''){
          mensaje=mensaje+'Debe seleccionar al menos una cuenta bancaria \n';
          correcto=false;
        }  
        if(!correcto)
          throw new Error(mensaje);
        return correcto;
      }
      
      function validarFechas(){
        correcto=true;
        mensaje='------  ALERTA  ------ \n';
        fInicio=document.getElementById('fechaInicio');
        ffinal=document.getElementById('fechaFinal');
        correcto = validaFIniFFin(fInicio,ffinal);
        if (correcto == false){
          mensaje=mensaje+'Verifique el periodo de fechas \n';
          correcto=false;
        }
        if(!correcto)
          throw new Error(mensaje);
        return correcto;
      }
      
      function asignaPagina(){
        try{
          if (verificaFormulario() && validarFechas()){
            form1.action='tesListadoMovReclaReferencia.jsp';
            form1.submit();
          }
        } catch(e){
          alert(e.message);
        }
       }
      
    </script>
    <% 
      crsCuentaBancaria.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.reclaReferencia");
    %>
  </head>
  <body>
   <form id="form1" name="form1" method="POST" >
   <%util.tituloPagina(out,"Tesorería","Reclasificación de referencia bancaria","Filtro",true);%>
   <br>
   <br>
   <table width="50%" align="center" cellpadding="6">
    <thead></thead>
        <tbody>
           <tr valign="top">
              <td>Cuenta bancaria:</td>
              <td>
                <select class="cajaTexto" id="cuentas" name="cuentas"> 
                  <%CRSComboBox(crsCuentaBancaria, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                </select>
              </td>
            </tr>
            <tr>
                <td>Periodo: </td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fechaInicio" id="fechaInicio" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> al: </td>
                     <td><input type="text" name="fechaFinal" id="fechaFinal" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13" >
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaFinal')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
                    </tr>
                    </tbody>
                  </table>
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
          <td><input type="button" class="boton" value="Ver transacciones" onclick="asignaPagina()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>