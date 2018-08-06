<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<jsp:useBean id="pbFormas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
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
        document.getElementById("forma").action='c713FormasFiltro.jsp';
        document.getElementById("forma").submit();
      }
    </script>
  </head>
  <body>
<%
  String forma = request.getParameter("hdforma") != null ? request.getParameter("hdforma") : "";
  String concepto = request.getParameter("hdconcepto") != null ? request.getParameter("hdconcepto") : "";
  String documentoFuente = request.getParameter("hddocumentoFuente") != null ? request.getParameter("hddocumentoFuente") : "";
  String tipoPolizaId = request.getParameter("hdtipoPolizaId") != null ? request.getParameter("hdtipoPolizaId") : "";
  pbFormas.addParam("esmanual",controlRegistro.getPagina().equals("formasManualesNew")?1:0);
  pbFormas.addParamVal("unidad", "and unidad_ejecutora = ':param'", controlRegistro.getUnidad());
  pbFormas.addParamVal("entidad", "and entidad = :param", controlRegistro.getEntidad());
  pbFormas.addParamVal("ambito", "and ambito = :param", controlRegistro.getAmbito());
  pbFormas.addParamVal("esManual", "and esmanual = :param", null);
  pbFormas.addParamVal("idCatalogo", "and id_catalogo_cuenta = :param", controlRegistro.getIdCatalogoCuenta());
  pbFormas.addParamVal("forma", "and upper(forma) like upper('%:param%')", forma);
  pbFormas.addParamVal("concepto", "and upper(concepto) like upper('%:param%')", concepto);
  pbFormas.addParamVal("documentoFuente", "and upper(documento_Fuente) like upper('%:param%')", documentoFuente);
  pbFormas.addParamVal("tipoPolizaId", "and RfTcFormasContables.tipo_Poliza_Id = :param", tipoPolizaId);
  
  pbFormas.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.formasContables");
  switch(Integer.parseInt(request.getParameter("accion")) ) {
    case 3: // opcion eliminar
      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=3&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
      pag.addImg(new Img("c713FormasControl.jsp",Img.IMG_ELIMINAR, "accion=3&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId, "forma_contable_id", false, "Eliminar esta forma contable", "return validaEliminar()"));
      break;
    case 2: // opcion modificar
      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasModificar.jsp","forma_contable_id","hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
      break;
    case 4: // opcion consultar
      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=4&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
      break;
    case 5: // opcion imprimir
      pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c713FormasConsultar.jsp","forma_contable_id","accion=5&hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId));
      pag.addImg(new Img("c713FormasImprimir.jsp",Img.IMG_IMPRIMIR, "hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId, "forma_contable_id", true, "Generar reporte de esta forma contable"));
      break;
  }
  pag.addCampo(new Campo("concepto","Concepto",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("documento_fuente","Documento fuente",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("poliza","P&oacute;liza",Campo.AL_IZQ,null));
  
  int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
%>

    
    <%util.tituloPagina(out,"Contabilidad","Resultado de formas contables","Resultado",true);%>
    <form action="" method="POST" id="forma" name="forma">
      <input type="hidden" name="param" id="param">
      <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
      <input type="hidden" name="hdforma" id="hdforma" value="<%=forma%>">
      <input type="hidden" name="hdconcepto" id="hdconcepto" value="<%=concepto%>">
      <input type="hidden" name="hddocumentoFuente" id="hddocumentoFuente" value="<%=documentoFuente%>">
      <input type="hidden" name="hdtipoPolizaId" id="hdtipoPolizaId" value="<%=tipoPolizaId%>">
      <input type="hidden" name="hdPagina" id="hdPagina" value="<%=controlRegistro.getPagina()%>">
      <input type="hidden" name="hdidCatalogoCuenta" id="hdidCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>">
      <br/>
      <br/>
      <br/>
      <%
      pag.seleccionarPagina(pbFormas, 
                               out, 10, 
                               param, "../../../", 
                               "forma_contable_id",null);
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