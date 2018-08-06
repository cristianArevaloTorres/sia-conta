<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListadoRepHist</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function llamamisma(pag){
        document.getElementById('pagina').value=pag;
        document.form1.submit();
       }
    
    </script>
  </head>
   <body >
   <form id="form1" name="form1" action="tesListadoRepHist.jsp"   method="POST">
    <%util.tituloPagina(out,"Tesorería","Listado de reporte","Historicos ".concat(request.getParameter("nomReporte")),true);%>
    <%
      String nomPagina = null;
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      pbFormas.addParam("idReporte",request.getParameter("numReporte"));
      pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"listados.select.listaRepHist.reportesInversiones");
      pag.addCampo(new Campo("fecha","Fecha",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("consecutivo","Folio",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("descripcion","Reporte",Campo.AL_CEN,null));  
      if(request.getParameter("numReporte").equals("4"))
        nomPagina = "tesAutorizacionInvDFHist.jsp";
      if(request.getParameter("numReporte").equals("3"))
        nomPagina = "tesDisponibilidadFHist.jsp";
      if(request.getParameter("numReporte").equals("1"))
        nomPagina = "tesCotizaTasasIFHist.jsp";
      pag.addImg(new Img(nomPagina,Img.IMG_MODIFICAR,"","reporte,fecha,consecutivo",false,"Modificar"));
      pag.seleccionarPagina(pbFormas, out, 10, param, "../../", "reporte,fecha,consecutivo",null,"Listado de reportes ".concat(request.getParameter("nomReporte")));
    %>
    <input type="hidden" name="pagina" id="pagina" value="<%=param%>"/>
    <input type="hidden" name="numReporte" id="numReporte" value="<%=request.getParameter("numReporte")%>"/>
    <input type="hidden" name="nomReporte" id="nomReporte" value="<%=request.getParameter("nomReporte")%>"/>
    </form>
  </body>
</html>