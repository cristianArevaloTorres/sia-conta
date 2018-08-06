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
    <title>c719FormasResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
    <script language="javascript" type="text/javascript">
      function llamamisma(parametro) {
       forma.param.value = parametro
       forma.action="";
       forma.submit();
      }
      
      function FiltroGeneral() {
       forma.action="c719FormasFiltro.jsp";
       forma.submit();
       //window.open('../../registroContable/filtroGenerarRegistroContableHB.jspx?pagina=irCapturaForma','_self');
      }
      function validaEliminar() {
        if(confirm('Realmente quiere eliminar la forma en cuestión?'))
          return true;
        else
          return false;
      }
      
      function validar(){
        var boleano = false;
        for(i=0; i<(document.forma.elements.length); i++){
          nombre = document.forma.elements[i].name;
          tipo = document.forma.elements[i].type;
          if (tipo == "radio" && !boleano){
           boleano = document.forma.elements[i].checked;
          }//if
        }//for
        if(!boleano)
          alert("Al menos debes seleccionar una opción");
        else
          document.forma.submit();
      }
    </script>
  </head>
  <body>
<%
  String forma = request.getParameter("hdforma") != null ? request.getParameter("hdforma") : "";
  String concepto = request.getParameter("hdconcepto") != null ? request.getParameter("hdconcepto") : "";
  String documentoFuente = request.getParameter("hddocumentoFuente") != null ? request.getParameter("hddocumentoFuente") : "";
  String tipoPolizaId = request.getParameter("hdtipoPolizaId") != null ? request.getParameter("hdtipoPolizaId") : "";
  pbFormas.addParam("esmanual",1);
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
  pag.addCampo(new CampoSel("forma","Forma",Campo.AL_IZQ,null,"c719FormasConsultar.jsp","forma_contable_id",null));
  pag.addImg(new Img("c713FormasImprimir.jsp",Img.IMG_IMPRIMIR, "hdforma="+forma+"&hdconcepto="+concepto+"&hddocumentoFuente="+documentoFuente+"&tipoPolizaId="+tipoPolizaId, "forma_contable_id", true, "Generar reporte de esta forma contable"));
  
  pag.addCampo(new Campo("concepto","Concepto",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("documento_fuente","Documento fuente",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("poliza","Poliza",Campo.AL_IZQ,null));
  
  int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
%>

    
    <%util.tituloPagina(out,"Contabilidad","Resultado de formas contables","Resultado",true);%>
    <form action="c719FormaControl.jsp" method="POST" id="forma" name="forma">
     
      
      <br/>
      <br/>
      <br/>
      <%
      pag.seleccionarPagina(pbFormas, 
                               out, 10, 
                               param, "../../../", 
                               "forma_contable_id",new SelTipoRadio(null,"ID"));
      %>
      <table width="100%">
      <tr> 
      <td align="right">
      <input type="button" value="Capturar" class='boton' onclick="validar()">      
      <input type="button" value="Regresar" class='boton' onclick="FiltroGeneral()">
      <input type="hidden" name="param" id="param">
      <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
      <input type="hidden" name="hdforma" id="hdforma" value="<%=forma%>">
      <input type="hidden" name="hdconcepto" id="hdconcepto" value="<%=concepto%>">
      <input type="hidden" name="hddocumentoFuente" id="hddocumentoFuente" value="<%=documentoFuente%>">
      <input type="hidden" name="hdtipoPolizaId" id="hdtipoPolizaId" value="<%=tipoPolizaId%>">
      <input type="hidden" name="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" name="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" name="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" name="pagina" value="irCapturaForma"/>      
      </td>
      </tr>
      </table>
    </form>
  </body>
</html>