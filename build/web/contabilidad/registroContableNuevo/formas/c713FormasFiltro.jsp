<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ include file="../../../Librerias/Funciones/utilscpv.jsp"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsTiposPoliza" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasFiltro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
  </head>
  <body>
    <%util.tituloPagina(out,"Contabilidad institucional","Formas contables","Filtro",true);
    crsTiposPoliza.registros(DaoFactory.CONEXION_CONTABILIDAD,"select tipo_poliza_id, descripcion, abreviatura from RF_TC_TIPOS_POLIZAS where tipo_poliza_id in (1,2,3,4)");%>
    <jsp:directive.include file="../encabezadoFechaActual.jspf" />
    <form action="c713FormasResultado.jsp" method="POST" id="forma">
    
      <input type="hidden" name="accion" value="<%=request.getParameter("accion")%>"/>
    
      <table border="0" cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>Forma:</td>
          <td><input type="text" id="hdforma" name="hdforma" size="3" maxlength="2" class="cajaTexto"/></td>
          
        </tr>
        <tr>
          <td valign="top">Concepto:</td>
          <td><textarea cols="40" rows="5" id="hdconcepto" name="hdconcepto" class="cajaTexto"></textarea></td>
        </tr>
        <tr>
          <td>Documento fuente:</td>
          <td><input type="text" id="hddocumentoFuente" name="hddocumentoFuente" size="50" maxlength="250" class="cajaTexto"/></td>
        </tr>
        <tr>
          <td>Tipo p&oacute;liza:</td>
          <td>
            <select class="cajaTexto" id="hdtipoPolizaId" name="hdtipoPolizaId"> 
              <option value="" selected>- Seleccione -</option>
              <%CRSComboBox(crsTiposPoliza, out,"tipo_poliza_id","descripcion","");%>
              
            </select>
          </td>
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