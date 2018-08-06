<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesValidaSaldos</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function llamamisma(pag){
        document.getElementById('pagina').value=pag;
        document.form1.submit();
       }
    
       function regresar(){
           document.getElementById('form1').action='tesFiltroSaldos.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
       
        function asignaPagina(boton){
         document.getElementById('tipoReporte').value=boton;
         document.getElementById('form1').action='tesValidaSaldosCtrl.jsp';
         document.getElementById('form1').target='_blank';
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
   <input type="hidden" value="<%=request.getParameter("fecha")%>" id="fecha" name="fecha">
   <input type="hidden" id="tipoReporte" name="tipoReporte">
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
   <%
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      pbFormas.addParam("fecha",request.getParameter("fecha"));      
      pbFormas.addParam("programa",request.getParameter("idProgramaS"));      
      pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.saldosDiarios.validaSaldos");
      pag.addCampo(new Campo("num_cuenta","Cuenta",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("descripcion","Nombre",Campo.AL_IZQ,null));  
      pag.addCampo(new Campo("saldo_calculado","Saldo calculado",Campo.AL_DER,null));  
      pag.addCampo(new Campo("saldo_banco","Saldo bancario",Campo.AL_DER,null));  
      pag.addCampo(new Campo("diferencia","Direfencia en saldo",Campo.AL_DER,null));  
      pag.seleccionarPagina(pbFormas, out, 25, param, "../", "num_cuenta",null,"Listado de saldos bancarios");
     %>
     <br>
     <input type="hidden" name="pagina" id="pagina" value="<%=param%>"/>
     <input type="hidden" name="fecha" id="fecha" value="<%=request.getParameter("fecha")%>"/>
     <input type="hidden" name="idProgramaS" id="idProgramaS" value="<%=request.getParameter("idProgramaS")%>"/>
     <hr class="piePagina">
     <br>
     <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td><input type="button" class="boton" value="Genera reporte" onclick="asignaPagina('pdf')"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>