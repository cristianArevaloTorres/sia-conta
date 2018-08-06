<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsReporte" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesRepInvHist</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
     function guardaNomRep(){
       document.getElementById('nomReporte').value = document.getElementById('reporte').options[document.getElementById('reporte').selectedIndex].text;
     }
        
    function asignaPagina(){
       numReporte=document.getElementById('reporte').options[document.getElementById('reporte').selectedIndex].value;
       document.getElementById('numReporte').value=numReporte;
       document.getElementById('form1').action='tesListadoRepHist.jsp';
       document.getElementById('form1').submit();
    }
    
    </script>
  </head>
  <%
    crsReporte.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTcClaveReportes.reportesInversiones");
    crsReporte.liberaParametros();
  %>
  <body>
  <form id="form1" name="form1" action="tesListadoRepHist.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Reportes de inversión","Históricos",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" >
      <input type="hidden" id="numReporte" name="numReporte">
      <table>
        <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;">Inversiones >> Reportes históricos </td>
          </tr>
        </tbody>
      </table>
      <br>
      <br>
      <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td>Reporte:</td>
          <td>
             <select class="cajaTexto" id="reporte" name="reporte" onchange="guardaNomRep()"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsReporte, out,"REPORTE","DESCRIPCION","");%>
             </select>
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
              <td><input type="button" class="boton" value="Aceptar" onclick="asignaPagina()"></td>
            </tr>
          </tbody>
         </table>
      </form>
  </body>
</html>