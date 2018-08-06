<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i003tesTasasRendimientoCapaResultado</title>
  </head>
  <body onload="parent.loadSourceFinish('dResultado');">
  <%
    String fecha              =  request.getParameter("fecha");
    String idBanco  =  request.getParameter("idBanco");
    try  {
    
    
    pbDocumentos.addParamVal("fecha","and to_char(tr.fecha,'dd/mm/yyyy') = ':param'",fecha);
    pbDocumentos.addParamVal("idBanco","and tr.id_banco = :param",idBanco);
   
    
    pbDocumentos.registrosMap(DaoFactory.CONEXION_TESORERIA,"tasasRendimiento.select.RfTrTasasRendimiento-resultado.inversiones");
    
    pag.addCampo(new Campo("nombre_corto","Institución financiera",Campo.AL_IZQ,null));
    pag.addCampo(new Campo("fecha","Fecha",Campo.AL_CEN,null));
    
    pag.addImg(new Img("i003tesTasasRendimientoModificar.jsp",Img.IMG_MODIFICAR, "fidBanco="+idBanco+"&ffecha="+fecha, "id_rendimiento", false, "Modificar compras"));
    
    
    
    int param = request.getParameter("param") != null ? Integer.parseInt(request.getParameter("param")) : 0;
    
    pag.seleccionarPagina(pbDocumentos, 
                               out, 20, 
                               param, "../../", 
                               "id_rendimiento",null,50,"Listado de tasas");  
    } catch(Exception e) {
      
    }
  %>
  </body>
</html>