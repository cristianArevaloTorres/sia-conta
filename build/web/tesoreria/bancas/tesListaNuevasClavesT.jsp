<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsClavesTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaNuevasClavesT</title>
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
    <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Listado de claves de transaccion no existentes",true);%>  
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
               <td style="color:rgb(214,107,0); font-size:large;">Nuevas claves de transacción encontradas</td>
             </tr>
             <tr align="center">
               <td>Se han encontrado nueva(s) clave(s) de transación(es) en el archivo</td>
             </tr>
             <tr>
               <td></td>
             </tr>
             <tr align="center">
               <td>Para continuar con la carga del archivo, deberá primero dar de alta</td>
             </tr>
             <tr align="center">
               <td>la(s) siguiente(s) clave(s) de transacción(es)</td>
             </tr>
           </tbody>
   </table> 
   <br>
   <br>
   <%
     crsClavesTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.nuevasClaveTrans.bancas");
   %>
    <table align="center" class="resultado" width="50%">
       <thead>
        <tr>
           <th  class="azulCla" colspan="2">Listado de nuevas claves de transacción</th>
         </tr>
         <tr>
           <th class="azulObs">Clave</th>
           <th class="azulObs">Descripción</th>
         </tr>
       </thead>
       <tbody>
       <%
       while(crsClavesTrans.next()) {%>
         <tr>
           <td align="center"><%=crsClavesTrans.getStr("clave")%></td>
           <td>&nbsp;<%=crsClavesTrans.getStr("descripcion")%></td>
         </tr>
        <% } %>
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