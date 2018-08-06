<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<%
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c728EventosFormasFiltro</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
  </head>
  <body>
    <form name="form1" id="form1" action="c728EventosFormasResultado.jsp" method="post">
      <%util.tituloPagina(out,"Contabilidad","Formas eventos y funciones","Filtro",true);%>
      <table cellpadding="5" align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td>Evento</td>
            <td><input type="text" class="cajaTexto" name="fevento" id="fevento" value="" size="30">
            </td>
          </tr>
          <tr>
            <td>Forma autom&aacute;tica</td>
            <td><input type="text" class="cajaTexto" name="fforma" id="fforma" value="" size="2" maxlength="2">
            </td>
          </tr>
          <tr>
            <td>Nombre de la variable</td>
            <td><input type="text" class="cajaTexto" name="fnombreVariable" id="fnombreVariable" value="" size="30">
            </td>
          </tr>
          <tr>
            <td>Tipo de la variable</td>
            <td><input type="text" class="cajaTexto" name="ftipo" id="ftipo" value="" size="30">
            </td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="submit" class="boton" value="Aceptar"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>