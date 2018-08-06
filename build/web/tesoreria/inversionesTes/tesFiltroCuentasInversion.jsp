<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsBancosInv" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroCuentasInversion</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/funcres.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script language="javascript" type="text/javascript">
    
      function inicial() {
        arbol = document.getElementById("arbol").value;
        if(arbol=='')
          llamaMismaPagina();
      }

      function llamaMismaPagina() {
        llamamisma(0);
      }
      
      function llamamisma(pag){
        banco = document.getElementById('fBancoInv').options[document.getElementById('fBancoInv').selectedIndex].value;
        loadSource('resultado',null,'tesFiltroCuentasInversionCapa.jsp?','banco='+banco+'&pagina='+pag);
      }
      
      function irAgregar() {
        form1.action = "tesAgregarCuentasInversion.jsp";
        document.getElementById("accion").value = 1;
        form1.submit();
      }
      
      
     </script>
  </head>
  <%
   String fBancoInv = request.getParameter("fBancoInv")!=null?request.getParameter("fBancoInv"):"";
   String arbol = request.getParameter("arbol")!=null?request.getParameter("arbol"):"";
   crsBancosInv.registrosMap(DaoFactory.CONEXION_TESORERIA,"tasasRendimiento.select.RfTcBancosInversion-bancosConInversion.inversiones");
  %>
  <body onload="inicial()">
    <form id="form1" method="POST" action="sigPagina.jsp">
       <%util.tituloPagina(out,"Tesorería","Cuentas de inversión","Filtro",true);%>
    <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
    </IFrame>
    <input type="hidden" name="arbol" id="arbol" value="<%=arbol%>"/>
    <input type="hidden" name="accion" id="accion" value=""/>
    <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td>Banco:</td>
          <td>
            <select class="cajaTexto" id="fBancoInv" name="fBancoInv"> 
              <option value=''>- Seleccione -</option>
              <%CRSComboBox(crsBancosInv, out,"ID_BANCO","NOMBRE_CORTO",fBancoInv);%>
            </select>
          </td>
        </tr>
      </tbody>
    </table>
    <br>
    <hr class="piePagina">
    <br>
    <table align="center" cellpadding="3">
      <thead></thead>
      <tbody>
        <tr>
          <td>
            <input type="button" class="boton" value="Buscar" onclick="llamaMismaPagina()">
          </td>
        </tr>
      </tbody>
    </table>
    <br>
    <div id="resultado">
    </div>
    </form>
  </body>
</html>