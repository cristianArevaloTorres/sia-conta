<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c728EventosFormasResultado</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
      function llamamisma(parametro) {
        
        document.forma.param.value = parametro;
        
        
        document.forma.action="";
        document.forma.submit();
      }
      
      function validaEliminar() {
        if(confirm('Realmente quiere eliminar la forma en cuestión?'))
          return true;
        else
          return false;
      }
      
      function regresar() {
        document.getElementById("forma").action='c728EventosFormasFiltro.jsp';
        document.getElementById("forma").submit();
      }
    </script>
  </head>
  <body>
<%
  String evento = request.getParameter("fevento") != null ? request.getParameter("fevento") : "";
  String forma = request.getParameter("fforma") != null ? request.getParameter("fforma") : "";
  String nombreVariable = request.getParameter("fnombreVariable") != null ? request.getParameter("fnombreVariable") : "";
  String tipo = request.getParameter("ftipo") != null ? request.getParameter("ftipo") : "";
  pbFormas.addParamVal("evento", "and e.idevento like ':param%'", evento);
  pbFormas.addParamVal("forma", "and upper(f.FORMA) like upper('%:param%')", forma);
  pbFormas.addParamVal("nombre", "and upper(fu.NOMBREVARIABLE) like upper('%:param%')", nombreVariable);
  pbFormas.addParamVal("tipo", "and upper(vt.NOMBREVARIABLE) like upper('%:param%')", tipo);
  
  pbFormas.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"eventos.select.RfTrFormacontable-Resultado.Formas");
//  switch(Integer.parseInt(request.getParameter("accion")) ) {
//    case 3: // opcion eliminar
//      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=3&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
//      pag.addImg(new Img("c713FormasControl.jsp",Img.IMG_ELIMINAR, "accion=3&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId, "forma_contable_id", false, "Eliminar esta forma contable", "return validaEliminar()"));
//      break;
//    case 2: // opcion modificar
//      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasModificar.jsp","forma_contable_id","hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
//      break;
//    case 4: // opcion consultar
//      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=4&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
//      break;
//    case 5: // opcion imprimir
//      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=5&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
//      pag.addImg(new Img("c713FormasImprimir.jsp",Img.IMG_IMPRIMIR, "hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId, "forma_contable_id", true, "Generar reporte de esta forma contable"));
//      break;
//  }
  pag.addCampo(new Campo("forma","Forma",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("idevento","Id evento",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("descripcion","Evento",Campo.AL_IZQ,null));
  pag.addImg(new Img("c728EventosFormasModificar.jsp",Img.IMG_MODIFICAR, 
  "accion=2&fevento="+evento+"&fforma="+forma+"&fnombreVariable="+nombreVariable+"&ftipo="+tipo, "idforma", false, "Modificar", ""));
  
  int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
%>

    
    <%util.tituloPagina(out,"Contabilidad","Resultado de formas contables","Resultado",true);%>
    <form action="" method="POST" id="forma" name="forma">
      <input type="hidden" name="param" id="param">
      <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
      <input type="hidden" name="fevento" id="fevento" value="<%=evento%>">
      <input type="hidden" name="fforma" id="fforma" value="<%=forma%>">
      <input type="hidden" name="fnombreVariable" id="fnombreVariable" value="<%=nombreVariable%>">
      <input type="hidden" name="ftipo" id="ftipo" value="<%=tipo%>">
      <br/>
      <br/>
      <br/>
      <%
      pag.seleccionarPagina(pbFormas, 
                               out, 20, 
                               param, "../../../../", 
                               "idforma",null);
      %>
      <br>
      <br>
      <hr class="piePagina">
      <br>
      <table align="center">
        <tr>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </table>  
    </form>
  </body>
</html>