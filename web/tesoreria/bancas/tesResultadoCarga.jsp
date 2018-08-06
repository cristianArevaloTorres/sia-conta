<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsSobregiro" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsDiferencia" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesResultadoCarga</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='../tesListadoProgramas.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
       }
       
       function generaReporte(){
          document.getElementById('form1').action='tesReporteResultado.jsp';
          document.getElementById('form1').target='_blank';
          document.getElementById('form1').submit();
       }
    
    </script>
  </head>
  <%!
  %>
  <%
    boolean habSobregiro = false;
    boolean habDiferencia = false;
  
    crsSobregiro.addParam("fechaCarga",request.getParameter("fechaCarga"));
    crsSobregiro.addParam("prog",request.getParameter("idProgramaS"));
    crsSobregiro.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.cuentasSobregiradas.bancas");
    if(crsSobregiro.size()>0)
      habSobregiro = true;
    else
      habSobregiro = false;
   
    crsDiferencia.addParam("fechaCarga",request.getParameter("fechaCarga"));
    crsDiferencia.addParam("prog",request.getParameter("idProgramaS"));
    crsDiferencia.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.registraronDiferencias.bancas");
    if(crsDiferencia.size()>0)
      habDiferencia = true;
    else
      habDiferencia = false;  
  
  %>
  <body>
     <form id="form1" name="form1"  method="POST">
     <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Resultado de la carga de transaccionalidad bancaria",true);%>  
     <input type="hidden" id="nombre" name="nombre" value="<%=request.getParameter("nombre")%>">
     <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
     <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
     <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
     <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
     <input type="hidden" id="mensaje" name="mensaje" value="<%=request.getParameter("mensaje")%>">
     <input type="hidden" id="fechaCarga" name="fechaCarga" value="<%=request.getParameter("fechaCarga")%>">
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
               <td style="font-size:large; color:rgb(0,99,148);">Estatus de la importación</td>
             </tr>
             <tr align="center">
               <td>Todos los registros del archivo se han importado correctamente</td>
             </tr>
           </tbody>
     </table> 
     <br>
     <table align="center" <%=habDiferencia?"":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td style="color:rgb(255,0,0); font-size:small;">Se detecto alguna diferencia en saldo</td>
             </tr>
           </tbody>
     </table> 
     <br>
     <table align="center" <%=habSobregiro?"":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td  style="color:rgb(255,0,0); font-size:small;">Existencia de cuenta(s) sobregirada(s)</td>
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
           <td><input type="button" class="boton" value="Generar PDF" onclick="generaReporte()"></td>
           <td><input type="button" class="boton" value="Aceptar" onclick="regresa()"></td>
         </tr>
       </tbody>
     </table>
     </form>
  </body>
</html>