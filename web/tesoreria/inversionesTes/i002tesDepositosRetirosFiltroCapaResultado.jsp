<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i002tesDepositosRetirosFiltroCapaResultado</title>
  </head>
  <body onload="parent.loadSourceFinish('dResultado');">
    <%
      String fecha              =  request.getParameter("fecha");
      String idCuentaInversion  =  request.getParameter("idCuentaInversion");
      
      try  {
      
      
      pbDocumentos.addParamVal("fecha","and to_char(oi.fecha,'dd/mm/yyyy')=':param'",fecha);
      pbDocumentos.addParamVal("idCuentaInversion","and oi.id_cuenta_inversion=:param",idCuentaInversion);
      
      pbDocumentos.registrosMap(DaoFactory.CONEXION_TESORERIA,"depositosRetiros.select.RfTrOperacionesInversion-resultado.inversiones");
      
      pag.addCampo(new Campo("fecha","Fecha vencimiento",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("contrato","Cuentas de inversión",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("tipo_operacion","Tipo de operación",Campo.AL_IZQ,null));
      pag.addCampo(new Campo("importe","Valor de la operación",Campo.AL_DER,Campo.FORMATO_DOS_DEC));
      
      
      pag.addImg(new Img("i002tesDepositosRetirosModificar.jsp",Img.IMG_MODIFICAR, "fidCuentaInversion="+idCuentaInversion+"&ffecha="+fecha, 
                 "id_operacion", false, "Modificar compras",null));
      
      
      
      int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
      
      pag.seleccionarPagina(pbDocumentos, 
                                 out, 20, 
                                 param, "../../", 
                                 "id_operacion",null,50,"Listado de depóstios y retiros");  
      } catch(Exception e) {
        
      }
    %>
  </body>
</html>