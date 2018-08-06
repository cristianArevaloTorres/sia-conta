<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaRegistrosDuplicados</title>
      <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
       <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='seleccionarArchivo.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
       }
       
       function generaReporte(){
          document.getElementById('form1').action='tesReporteRegDuplicadosControl.jsp';
          document.getElementById('form1').target='_blank';
          document.getElementById('form1').submit();
       }
       
       function llamaEliminaDuplicados() {
          loadSource('divEliminaDup',null,'tesCapaElinimaDup.jsp','');
       }
       
       function returnElimina(correcto) {
         if(correcto){
           document.getElementById('bElimina').style.display='none';
           document.getElementById('bContinua').style.display='';
           document.getElementById('bReporte').style.display='none';
         }
         else{
           document.getElementById('bElimina').style.display='';
           document.getElementById('bContinua').style.display='none';
           document.getElementById('bReporte').style.display='';
         }
       }
       
       function continuar(){
          document.getElementById('form1').action='tesImportarTransControl.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
       }
    
    </script>
  </head>
  <body>
   <form id="form1" name="form1">
    <%util.tituloPagina(out,"Tesorería","Transaccionalidad bancaria","Registros duplicados",true);%>  
    <IFrame style="display:none" name="bufferCurp" id="bufferCurp"></IFrame>
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <input type="hidden" id="nombre" name="nombre" value="<%=request.getParameter("nombre")%>">
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
               <td style="color:rgb(148,0,0); font-size:large;">Existen registros duplicados</td>
             </tr>
             <tr align="center">
               <td>Se han encontrado movimientos bancarios duplicados en el proceso de información</td>
             </tr>
           </tbody>
   </table>
    <div id="divEliminaDup"></div>
   <br>
   <br>
   <hr class="piePagina">
    <br>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td><input id="bReporte" name="bReporte"  type="button" class="boton" value="Ver reporte" onclick="generaReporte()"></td>
          <td><input id="bElimina" name="bElimina"  type="button" class="boton" value="Eliminar" onclick="llamaEliminaDuplicados()"></td>
          <td ><input style='display:none' id="bContinua" name="bContinua" type="button" class="boton" value="Continuar" onclick="continuar()"></td>
          <td><input type="button" class="boton" value="Seleccionar otro archivo" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>