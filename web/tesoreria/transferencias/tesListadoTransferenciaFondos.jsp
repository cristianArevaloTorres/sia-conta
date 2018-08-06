<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListadoTransferenciaFondos</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
       function llamamisma(pag){
        document.getElementById('pagina').value=pag;
        document.form1.submit();
       }
    
    </script>
  </head>
   <body >
   <form id="form1" name="form1" action="tesListadoTransferenciaFondos.jsp" method="POST">
    <%util.tituloPagina(out,"Tesorería","Listado de reporte","Transferencia de fondos",true);%>
    <%
      String folios = request.getParameter("folio");
      int param = request.getParameter("pagina") != null ? Integer.parseInt(request.getParameter("pagina")) : 0;
      if (folios.equals("SEG"))
        pbFormas.addParam("folios","'SEG'");
      else
        pbFormas.addParam("folios","'INV','REA','ING','DIIF','DCIB'");
      pbFormas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.listadoReportes");
      pag.addCampo(new Campo("fecha","Fecha",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("consecutivo","Folio",Campo.AL_CEN,null));  
      pag.addCampo(new Campo("descripcion","Reporte",Campo.AL_CEN,null));  
      pag.addImg(new Img("tesTransferenciaFondosHistorico.jsp",Img.IMG_MODIFICAR,"","reporte,fecha,consecutivo",false,"Modificar"));
      pag.seleccionarPagina(pbFormas, out, 10, param, "../../", "reporte,fecha,consecutivo",null,"Listado de reportes transferencia de fondos historicos");
    %>
    <input type="hidden" name="pagina" id="pagina" value="<%=param%>"/>
    <input type="hidden" name="folio" id="folio" value="<%=request.getParameter("folio")%>"/>
    </form>
  </body>
</html>