<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1" import="sia.libs.archivo.Patron"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c727DescargaReportes</title>
    </head>
  <body>
  <%util.getHojaEstilo(out, request);%>
  <form id="forma" name="forma" method="post" action="c727ImprimirLibros.jsp" target="_blank">
    <%
      util.tituloPagina(out, "Contabilidad", " ","Descarga de reportes de libros contables" , "Reportes", true);%>
    <hr class="pie">
    <br>
    <%
    String[] archivos=Patron.listaArchivosSeleccionados(application.getRealPath("/Reportes/contabilidad/estadosFinancieros/reportes/"),"xSia?repLibroMayor.pdf");
    if(archivos.length>0){
    %>
    <table align="center">
      <tr class="general">
        <th colspan='3' align="center" class="general">
          Lista de archivos a descargar
        </th>
      </tr>
      <tr class="general">
        <th class="general">
          Fecha
        </th>
        <th class="general">
          Hora
        </th>
        <th class="general">
          Descarga
        </th>
      </tr>
    <%for(int x=0; x<archivos.length;x++){
        if(archivos[x].endsWith("repLibroMayor.pdf")){
        String[] datos=archivos[x].split("_");
        %>
      <tr class="general">
        <td class="general">
          <%=util.despliegaFecha(datos[0].substring(4, 12))%>
        </td>
        <td class="general">
          <%=datos[0].substring(12).substring(0, 2)+":"+datos[0].substring(12).substring(2, 4)%>
        </td>
        <td class="general" align="center">
          <a href=<%=util.getContexto(request)+"/Reportes/contabilidad/estadosFinancieros/reportes/"+archivos[x]%>>
          <img src="<%=util.getContexto(request)%>/Librerias/Imagenes/imgpdf.gif" alt="<%=archivos[x]%>" border="0"> </a>
        </td>
      </tr>
      <%}
      }%>
    </table>
    <%}%>
    </form>
  </body>
</html>