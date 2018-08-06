<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c714OperacionesTipoFiltro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
  </head>
  <body>
    <%util.tituloPagina(out,"Contabilidad institucional","Formas contables","Filtro",true);%>
    <jsp:directive.include file="../encabezadoFechaActual.jspf" />
    <form action="c714OperacionesTipoResultado.jsp" method="POST" id="forma">
    
      <input type="hidden" name="accion" value="<%=request.getParameter("accion")%>"/>
    
      <table border="0" cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>No operacion:</td>
          <td><input type="text" id="hdconsecutivo" name="hdconsecutivo" size="3" maxlength="2" class="cajaTexto"/></td>
          
        </tr>
        <tr>
          <td valign="top">Descripcion:</td>
          <td><textarea cols="40" rows="5" id="hddescripcion" name="hddescripcion" class="cajaTexto"></textarea></td>
        </tr>
      </table>
      <hr class="piePagina"/>
      <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <input type="submit" class="boton" value="Aceptar" onclick="validaFormulario()"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>