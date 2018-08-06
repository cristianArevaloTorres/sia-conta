<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd"><!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
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
    <title>c714OperacionesTipoResultado</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
    <script language="javascript" type="text/javascript">
      function llamamisma(parametro) {
        
        forma.param.value = parametro
        
        
        forma.action="";
        forma.submit();
      }
      
      function validaEliminar() {
        if(confirm('Realmente quiere eliminar la operacion tipo en cuestión?'))
          return true;
        else
          return false;
      }
    </script>
  </head>
  <body>
<%
  String consecutivo = request.getParameter("hdconsecutivo") != null ? request.getParameter("hdconsecutivo"): "";
  String descripcion = request.getParameter("hddescripcion") != null ? request.getParameter("hddescripcion"): "";
  pbFormas.addParamVal("unidad", "and unidad_ejecutora = ':param'", controlRegistro.getUnidad());
  pbFormas.addParamVal("entidad", "and entidad = :param", controlRegistro.getEntidad());
  pbFormas.addParamVal("ambito", "and ambito = :param", controlRegistro.getAmbito());
  pbFormas.addParamVal("idCatalogo", "and id_catalogo_cuenta = :param", controlRegistro.getIdCatalogoCuenta());
  pbFormas.addParamVal("consecutivo", "and upper(consecutivo) like upper('%:param%')", consecutivo);
  pbFormas.addParamVal("descripcion", "and upper(descripcion) like upper('%:param%')", descripcion);
  pbFormas.addParamVal("fvigencias", "and (to_char(fecha_vig_ini, 'yyyy') = ':param' and to_char(fecha_vig_fin, 'yyyy') = ':param')",  controlRegistro.getEjercicio());
  
  pbFormas.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.operacionesTipoResultado");
  switch(Integer.parseInt(request.getParameter("accion")) ) {
    case 3: // opcion eliminar
      pag.addCampo(new CampoSel("consecutivo","No operación",Campo.AL_CEN,null,"c714OperacionesTipoConsultar.jsp","maestro_operacion_id","accion=3&hdconsecutivo="+consecutivo+"&hddescripcion="+descripcion));
      pag.addImg(new Img("c714OperacionesTipoControl.jsp",Img.IMG_ELIMINAR, "accion=3&hdconsecutivo="+consecutivo+"&hddescripcion="+descripcion, "maestro_operacion_id", false, "Eliminar esta operacion contable", "return validaEliminar()"));
      break;
    case 2: // opcion modificar
      pag.addCampo(new CampoSel("consecutivo","No operación",Campo.AL_CEN,null,"c714OperacionesTipoModificar.jsp","maestro_operacion_id","hdconsecutivo="+consecutivo+"&hddescripcion="+descripcion));
      break;
    case 4: // opcion consultar
      pag.addCampo(new CampoSel("consecutivo","No operación",Campo.AL_CEN,null,"c714OperacionesTipoConsultar.jsp","maestro_operacion_id","hdconsecutivo="+consecutivo+"&hddescripcion="+descripcion));
      break;
    case 5: // opcion imprimir
      pag.addCampo(new CampoSel("consecutivo","No operación",Campo.AL_CEN,null,"c714OperacionesTipoConsultar.jsp","maestro_operacion_id","accion=5&hdconsecutivo="+consecutivo+"&hddescripcion="+descripcion));
      pag.addImg(new Img("c714OperacionesTipoImprimir.jsp",Img.IMG_IMPRIMIR, "", "maestro_operacion_id", true, "Generar reporte de esta operacion contable"));
      break;
  }
  pag.addCampo(new Campo("descripcion","Descripcion",Campo.AL_IZQ,null));
  pag.addCampo(new Campo("registro","registro",Campo.AL_IZQ,null));
  
  int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
%>

    
    <%util.tituloPagina(out,"Contabilidad","Resultado de operaciones tipo","Resultado",true);%>
    <form action="" method="POST" id="forma" name="forma">
      <input type="hidden" name="param" id="param">
      <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
      <input type="hidden" name="hdconsecutivo" id="hdconsecutivo" value="<%=consecutivo%>">
      <input type="hidden" name="hddescripcion" id="hddescripcion" value="<%=descripcion%>">
      
      <br/>
      <br/>
      <br/>
      <%
      pag.seleccionarPagina(pbFormas, 
                               out, 10, 
                               param, "../../../", 
                               "maestro_operacion_id",null,
                               "Operaciones tipo registrados");
      %>
    </form>
  </body>
</html>