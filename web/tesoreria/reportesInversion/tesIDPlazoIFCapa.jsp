<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<jsp:useBean id="crsCuenta" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesIDPlazoIFCapa</title>
  </head>
  <body onload="parent.loadSourceFinish('socResultado')">
    <%
      String bancoActivo           =  request.getParameter("banco");
      
      if (!bancoActivo.equals("")){
        crsCuenta.addParam("idBanco","and bi.id_banco = '".concat(bancoActivo).concat("'"));      
      }
      crsCuenta.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");  
      StringBuffer cdgHTMLList = new StringBuffer();
 
 
      while (crsCuenta.next()){
        cdgHTMLList.append("<option value=" + crsCuenta.getStr("ID_CUENTA_INVERSION") );
        cdgHTMLList.append(">"+ crsCuenta.getStr("CONTRATO_CUENTA")   + "</option>");
     }
      
    %>
            <select class="cajaTexto" id="cuenta" name="cuenta" >
              <option value=''>- Seleccione -</option>
             <%=cdgHTMLList%>
            </select>
  </body>
</html>