<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i006tesVencimientoInversionFiltroCapaResultado</title>
  </head>
  <body onload="parent.loadSourceFinish('dResultado');">
    <%
      String fecha              =  request.getParameter("fecha");
      String idCuentaInversion  =  request.getParameter("idCuentaInversion");
      String estatus            =  request.getParameter("estatus");
      try  {
      
      
      pbDocumentos.addParamVal("fecha","and to_char(coi.fecha,'dd/mm/yyyy')=':param'",fecha);
      pbDocumentos.addParamVal("idCuentaInversion","and coi.id_cuenta_inversion=:param",idCuentaInversion);
      pbDocumentos.addParamVal("estatus","and coi.estatus=:param",estatus);
      
      pbDocumentos.registrosMap(DaoFactory.CONEXION_TESORERIA,"vencimiento.select.RfTrComprasInversion-Vencimiento.inversiones");
      
      pag.addCampo(new Campo("contrato","Contrato",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("importe_global","Importe global",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      pag.addCampo(new Campo("vencimiento","Fecha vencimiento",Campo.AL_CEN,null));
      
      pag.addImg(new Img("i006tesVencimientoInversionModificar.jsp",Img.IMG_MODIFICAR, "fidCuentaInversion="+idCuentaInversion+"&ffecha="+fecha, 
                 "id_compra_inversion", false, "Registrar vencimiento",null,"vencido","1"));
      pag.addImg(new Img("",Img.PALOMA_GRIS, "", 
                 "id_compra_inversion", false, "",null,"estatus","1"));
      
      
      
      int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
      
      pag.seleccionarPagina(pbDocumentos, 
                                 out, 20, 
                                 param, "../../", 
                                 "id_compra_inversion",null,50,"Listado de vencimientos");  
      } catch(Exception e) {
        
      }
    %>
  </body>
</html>