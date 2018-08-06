<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<jsp:useBean id="crsCompra" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesIDCompraCICapa</title>
  </head>
  <body onload="parent.loadSourceFinish('socResultado')">
    <%
      String cuentaActivo           =  request.getParameter("idCuenta");
      String compraHist             =  request.getParameter("idCompra")!=null?request.getParameter("idCompra"):"";
      
      if (!cuentaActivo.equals("")){
        crsCompra.addParam("idCuentaInversion",cuentaActivo);      
      }
      crsCompra.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrComprasInversion.reportesInversiones");  
      StringBuffer cdgHTMLList = new StringBuffer();
 
      while (crsCompra.next()){
        
        cdgHTMLList.append("<option value=" + crsCompra.getStr("ID_COMPRA_INVERSION") );
        if (crsCompra.getStr("ID_COMPRA_INVERSION").equals(compraHist))
            cdgHTMLList.append(" selected");
        cdgHTMLList.append(">"+ crsCompra.getStr("FECHA").concat(" ") + crsCompra.getStr("NUM_OPERACION")  + "</option>");
     }
      
    %>
            <select class="cajaTexto" id="idCompra" name="idCompra"  >
              <option value=''>-Seleccione-</option>
             <%=cdgHTMLList%>
            </select>
  </body>
</html>