<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.fun.tabla.*" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesArchivoIncorrecto</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='seleccionarArchivo.jsp';
          document.getElementById('form1').submit();
       }
    
    </script>
  </head>
  <body>
    <form id="form1" name="form1" action="seleccionarArchivo.jsp">
    <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Problema para realizar la carga de movimientos bancarios",true);%>  
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <table>
        <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
           </tr>
        </tbody>
   </table>
   <br>
   <br>
   <table align="center">
        <thead></thead>
           <tbody>
             <tr align="center">
               <td style="color:rgb(198,0,0); font-size:large;">Error</td>
             </tr>
           </tbody>
   </table> 
   <br>
   <table align="center">
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
          <td><input type="button" class="boton" value="Aceptar" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>