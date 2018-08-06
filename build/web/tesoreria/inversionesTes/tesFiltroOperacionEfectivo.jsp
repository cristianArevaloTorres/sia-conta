<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsBancosInv" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroOperacionEfectivo</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
  </head>
  <body>
    <form id="form1" method="POST" action="sigpafina.jsp">
       <%util.tituloPagina(out,"Tesorería","Operación - Efectivo","Filtro",true);%>
    <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
    </IFrame>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>