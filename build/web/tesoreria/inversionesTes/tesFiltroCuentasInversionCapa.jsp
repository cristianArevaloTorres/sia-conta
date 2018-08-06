<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroCuentasInversionCapa</title>
  </head>
  <body onLoad="parent.loadSourceFinish('resultado');">
    <%
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      String idBanco = request.getParameter("banco");
      pbFormas.addParamVal("idBanco","and bi.id_banco=:param",idBanco);
      pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"inversionesTes.select.RfTrCuentasInversion");
      pag.addCampo(new Campo("nombre_corto","Institución bancaria",Campo.AL_IZQ,null));  
      pag.addCampo(new Campo("CONTRATO_CUENTA","Contrato o cuenta",Campo.AL_IZQ,null));  
      pag.addCampo(new Campo("FECHA_APERTURA","Fecha de apertura",Campo.AL_CEN,null));  
      pag.addImg(new Img("tesModificarCuentasInversion.jsp",Img.IMG_MODIFICAR,"fBancoInv="+idBanco,"ID_CUENTA_INVERSION",false,"Modificar"));
      pag.seleccionarPagina(pbFormas, out, 10, param, "../../", "ID_CUENTA_INVERSION",null,70,"Listado de cuentas de inversión");
    %>
  </body>
</html>