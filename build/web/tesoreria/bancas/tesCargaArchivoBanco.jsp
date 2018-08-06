<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCargaArchivoBanco</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript"  type="text/javascript">

      function regresa(){
          document.getElementById('form1').action='seleccionarArchivo.jsp';
          document.getElementById('form1').submit();
      }


      function importarArchivoS(){
          document.getElementById('form1').action='tesCarArchBanControl.jsp';
          document.getElementById('form1').submit();
      }


    </script>
    <%
    boolean habilitar = true;
     /* boolean habilitar = false;
      String strTipo = request.getAttribute("contenido").toString();
      if ((strTipo.equals("text/plain")) || (strTipo.equals("application/octet-stream")) || (strTipo.equals("text/html")) )
        habilitar = true;
      else
        habilitar = false;
 * */
    %>
  </head>
  <body>
  <form id="form1" name="form1" action="tesCarArchBanControl.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Información del archivo de transacciones",true);%>
  <input type="hidden" id="nombre" name="nombre" value="<%=(String)session.getAttribute("nombre")%>">
  <input type="hidden" id="nomProg" name="nomProg" value="<%=session.getAttribute("nomProg")%>">
  <input type="hidden" id="opcion" name="opcion" value="<%=session.getAttribute("opcion")%>">
  <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=session.getAttribute("idProgramaS")%>">
  <input type="hidden" id="proceso" name="proceso" value="<%=session.getAttribute("proceso")%>">
  <table>
    <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getAttribute("opcion")%> >> <%=request.getAttribute("nomProg")%></td>
           </tr>
        </tbody>
   </table>
   <br>
   <br>
   <table width="50%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4" >Informaci&oacute;n del archivo</td>
           </tr>
            <tr>
             <td width="6%"></td>
             <td width="10%">Nombre:</td>
             <td width="3%"></td>
             <td width="25%"><%=(String)session.getAttribute("nombre")%></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="10%">Tamaño:</td>
             <td width="3%"></td>
             <td width="25%"><%=session.getAttribute("tamanio")%> bytes (
             <%
               //float floatBytes = Float.valueOf(session.getAttribute("tamanio").toString()).floatValue();
               float floatBytes =Float.valueOf(session.getAttribute("tamanio").toString()).floatValue();
               float floatKBytes = floatBytes/1024;
               double redondeo = Math.round(floatKBytes*100)/100.0;
             %>
             <%=redondeo%> kb)</td>  
           </tr>
           <%--      <tr>
             <td width="6%"></td>
             <td width="10%">Tipo:</td>
             <td width="3%"></td>
             <td width="25%"><%=request.getAttribute("contenido")%></td>
           </tr>
           --%>
        </tbody>
   </table> 
   <br>
   <hr class="piePagina">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td <%=habilitar?"":"style='display:none'"%>><input type="button" class="boton" value="Importar a BD" onclick="importarArchivoS()"></td>
          <td><input type="button" class="boton" value="Seleccionar otro" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>