<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Formatos"%>
<%@ page import="java.util.*"%>
<%//@ page import="sia.comun.Reporte"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.xml.Dml"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.db.sql.Vista"%>

<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasImprimir</title>
    <script type="text/javascript" language="javascript">
      function envia() {
        if(document.getElementById("genera").value=='true')
          forma.submit();
      }
    </script>
  </head>
  
  <body onload="envia()">
    <form id="forma" name="forma" action="../../../Librerias/reportes/generaReporte.jsp">
  <%
         // HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest(); 
    Map mapParams = new HashMap();
    Dml dml = null;
  //  Reporte repClase = new Reporte();
    sia.libs.formato.Fecha fecha = null;
    StringBuffer sentencia = new StringBuffer();
    int reporte = 0;
    String condicion = null;
    boolean genera = false;
    List<Vista> registros          = null; 
    try{
       DatosReportes datosReportes = new DatosReportes();           
       registros = new ArrayList<Vista>();
       reporte = 1;
       dml = new Dml(Dml.CONTABILIDAD);
      mapParams.put("idCatalogoCuenta", controlRegistro.getIdCatalogoCuenta());
      mapParams.put("ambito",controlRegistro.getAmbito());
      mapParams.put("entidad",controlRegistro.getEntidad());
      mapParams.put("uniEjecutora",controlRegistro.getUnidad());
      if(reporte == 1){
        condicion = "and fc.forma_contable_id = ".concat(request.getParameter("clave"));
        mapParams.put("condicion",condicion);
      }
      else{
        mapParams.put("condicion"," ");
      }
      sentencia.append(dml.getSelect("formasContables","reporteFormas",mapParams));

      registros = datosReportes.obtenerTitulos(controlRegistro.getUnidad(),String.valueOf(controlRegistro.getEntidad()),String.valueOf(controlRegistro.getAmbito()));      
      mapParams.clear();
      mapParams.put("FECHA",fecha.formatear(6,controlRegistro.getFechaAfectacion()).toUpperCase());
      mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
      mapParams.put("TITULO1",registros==null?"T1":registros.get(0).getField("TITULO1")); 
      mapParams.put("TITULO2",registros==null?"T2":registros.get(0).getField("TITULO2")); 
      mapParams.put("TITULO3",registros==null?"T3":registros.get(0).getField("TITULO3")); 
  
      
      
      //    repClase.imprimir(sentencia, mapParams, "FormasContables", "/contabilidad/registroContable/formas/reportes/formasContables", 
    //  DaoFactory.CONEXION_CONTABILIDAD, "pdf", "Generacion de reporte de formas contables", "Contabilidad");
      request.getSession().setAttribute("abrirReporte", "abrir");
      request.getSession().setAttribute("nombreArchivo", "formasContables");
      request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/formas/reportes/formasContables");
      request.getSession().setAttribute("parametros", mapParams);
      request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
      request.getSession().setAttribute("query", sentencia);
      request.getSession().setAttribute("formatoSalida", "pdf");
      request.getSession().setAttribute("accionPagina", "Generacion de reporte de formas contables");
      request.getSession().setAttribute("modulo", "Contabilidad");

      genera = true;
    }
    catch(Exception e){
      System.err.println("Error al generar el reporte de formas contables");
    }
  
  %>
    <input type="hidden" name="genera" id="genera" value="<%=genera%>">
    </form>
  </body>
</html>