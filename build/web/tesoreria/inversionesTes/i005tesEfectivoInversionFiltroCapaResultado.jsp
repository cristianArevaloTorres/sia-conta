<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="pbRendimientos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005tesEfectivoInversionFiltroCapaResultado</title>
  </head>
  <body onload="parent.loadSourceFinish('dResultado');">
    <%
      String fecha              =  request.getParameter("fecha");
      String idCuentaInversion  =  request.getParameter("idCuentaInversion");
      try  {
      
      
      pbRendimientos.addParamVal("fecha","and to_char(ei.fecha,'dd/mm/yyyy')=':param'",fecha);
      pbRendimientos.addParamVal("idCuentaInversion","and ei.id_cuenta_inversion=:param",idCuentaInversion);
      
      pbRendimientos.registrosMap(DaoFactory.CONEXION_TESORERIA,"rendimientoEfectivo.select.RfTrEfectivoInversion.inversiones");
      
      pag.addCampo(new Campo("contrato","Contrato",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("fecha","Fecha",Campo.AL_CEN,null));
      pag.addCampo(new Campo("vincimiento","Vencimiento",Campo.AL_CEN,null));
      pag.addCampo(new Campo("importe","Importe",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("rendimiento","Rendimiento",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      
      pag.addImg(new Img("i005tesEfectivoInversionModificarInicio.jsp",Img.IMG_MODIFICAR, "fidCuentaInversion="+idCuentaInversion+"&ffecha="+fecha, 
                 "id_efectivo_inversion", false, "Modificar inicio de deposito",null));
      pag.addImg(new Img("i005tesEfectivoInversionRendimiento.jsp",Img.IMG_MODIFICAR, "fidCuentaInversion="+idCuentaInversion+"&ffecha="+fecha+"&accion=2", 
                 "id_efectivo_inversion", false, "Registrar rendimiento",null,"rendimiento_registrado","0"));
      pag.addImg(new Img("i005tesEfectivoInversionRendimiento.jsp",Img.IMG_MODIFICAR, "fidCuentaInversion="+idCuentaInversion+"&ffecha="+fecha+"&accion=3", 
                 "id_efectivo_inversion", false, "Modificar rendimiento",null,"rendimiento_registrado","1"));
      
      
      
      int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
      
      pag.seleccionarPagina(pbRendimientos, 
                                 out, 20, 
                                 param, "../../", 
                                 "id_efectivo_inversion",null,75,"Listado de rendimientos de efectivo");  
      } catch(Exception e) {
        
      }
    %>
  </body>
</html>