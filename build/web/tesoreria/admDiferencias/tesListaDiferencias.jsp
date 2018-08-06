<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsEstatus" class="sia.db.sql.SentenciasCRS" scope="page"/>
 <%
    String estDef = request.getParameter("estatusDef")== null ? "" : request.getParameter("estatusDef");
    crsEstatus.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTcEstatusDiferencia.diferenciaSaldo");
    
  %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaDiferencias</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
     
      function inicia() {
          irBuscar();
      }
     
      function irBuscar() {
          llamamisma(0);
          
      }
      
      function llamamisma(parametro) {
        estatusVal = document.getElementById("estatusDif");
        idProg = document.getElementById("idProgramaS");
        nomProgS = document.getElementById("nomProg");
        opcionS = document.getElementById("opcion");
        procesoS = document.getElementById("proceso");
        loadSource('dResultado',null,'tesListaDiferenciasCapa.jsp?','idEstatus='+estatusVal.value+'&idProgramaS='+idProg.value+'&nomProg='+nomProgS.value+'&opcion='+opcionS.value+'&proceso='+procesoS.value+'&param='+parametro);
      }
    
    </script>
  </head>
 
  <body onload="inicia()">
  <form id="form1" name="form1" action="tesBitacoraAclaracion.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Filtro por estatus de diferencia en saldos","Diferencia en saldos",true);%>
      <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
      <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
      <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
      <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
      </IFrame>
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
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Estatus:</td>
            <td><select id='estatusDif' name='estatusDif' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(crsEstatus, out,"ID_ESTATUS_DIF","DESCRIPCION",estDef);%>
            </select></td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
       <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Buscar" onclick="irBuscar()"></td>
          </tr>
        </tbody>
      </table>
      <br>
      <div id="dResultado">
      
      </div>
      </form>
  </body>
</html>