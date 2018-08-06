<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesRegistraActividades</title>
   <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='tesBitacoraAclaracion.jsp';
          document.getElementById('form1').submit();
       }
       
       
      function ir() {
          document.getElementById('form1').action = 'tesRegistraActividadCtrl.jsp';
          document.getElementById('form1').submit();
      }
    
    </script>
  </head>
  <body>
  <form id="form1" name="form1" action="tesRegistraActividadCtrl.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Reporte de registro de saldos con diferencia","Diferencia en saldos",true);%>
  <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
  <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
  <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
  <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
  <input type="hidden" id="estatusDef" name="estatusDef" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="estatusDif" name="estatusDif" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="idEstatus" name="idEstatus" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
   <br>
      <table>
      <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
          </tr>
        </tbody>
      </table>
   <br>
   <table width="80%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4">Agregar aclaración</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Fecha:</td>
             <td width="3%"></td>
             <td width="59%" align="left">  
                     <input type="text" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
             </td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Acción realizada:</td>
             <td width="3%"></td>
             <td width="59%" align="left"><textarea cols="80" rows="2" id="observaciones" name="observaciones"  class="cajaTexto"></textarea></td>
           </tr>
        </tbody>
   </table> 
   <br>
   <table align="center" <%=!request.getParameter("mensaje").equals("")?"":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td  style="font-size:medium;"><%=request.getParameter("mensaje")%></td>
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
          <td><input type="button" class="boton" value="Aceptar" onclick="ir()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>