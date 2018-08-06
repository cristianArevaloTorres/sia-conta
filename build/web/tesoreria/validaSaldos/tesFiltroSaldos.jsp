<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroSaldos</title>
     <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript">
    </script>
    <script language="JavaScript"  type="text/javascript">
     
       function asignaPagina(){
         document.getElementById('form1').action='tesValidaSaldos.jsp';
         document.getElementById('form1').submit();
       }
       
       
       function regresar(){
           document.getElementById('form1').action='../tesListadoProgramas.jsp';
           document.getElementById('form1').submit();
       } 
        
    </script>
  </head>
  <body>
  <form id="form1" name="form1" action="tesValidaSaldos.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Validación de saldos","Filtro",true);%>
      <input type="hidden" value="<%=request.getParameter("opcion")%>" id="opcion" name="opcion">
      <input type="hidden" value="<%=request.getParameter("nomProg")%>" id="nomProg" name="nomProg">
      <input type="hidden" value="<%=request.getParameter("idProgramaS")%>" id="idProgramaS" name="idProgramaS">
      <input type="hidden" value="<%=request.getParameter("proceso")%>" id="proceso" name="proceso">
      <input type="hidden" value="<%=request.getParameter("administrador")%>" id="administrador" name="administrador">
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
      <table align="center" cellpadding="3">
      <thead></thead>
        <tbody>
             <tr>
                <td>Fecha de validación: </td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> 
                     </td>
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
          <td><input type="button" class="boton" value="Tabla comparativa" onclick="asignaPagina()"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>