<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Formatos"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.db.sql.Vista"%>

<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c714OperacionesTipoImprimir</title>
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
      Formatos formatos = null;
      boolean genera = false;
      List<Vista> registros          = null;    
      try{
          DatosReportes datosReportes = new DatosReportes();           
          registros = new ArrayList<Vista>();
          int MaestroOperacionId = Integer.parseInt(request.getParameter("clave"));
          StringBuffer sbQuery2 = new StringBuffer();
          Map mapParams = new HashMap();
          //HttpServletRequest request = (HttpServletRequest)FacesContext.getCurrentInstance().getExternalContext().getRequest();            

          registros = datosReportes.obtenerTitulos(controlRegistro.getUnidad(),String.valueOf(controlRegistro.getEntidad()),String.valueOf(controlRegistro.getAmbito()));      
          formatos = new Formatos(Contabilidad.getInstance().getPropiedad("operacionesContables.query.imprimirOperacionContable"),String.valueOf(MaestroOperacionId));
          sbQuery2.append(formatos.getSentencia());
          mapParams.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
          mapParams.put("TITULO1",registros==null?"T1":registros.get(0).getField("TITULO1")); 
          mapParams.put("TITULO2",registros==null?"T2":registros.get(0).getField("TITULO2")); 
          mapParams.put("TITULO3",registros==null?"T3":registros.get(0).getField("TITULO3")); 

          request.getSession().setAttribute("abrirReporte", "abrir");
          request.getSession().setAttribute("nombreArchivo", "OperacionesContables");
          request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContable/procesos/polizas/reportes/listadoOpContables");
          request.getSession().setAttribute("parametros", mapParams);
          request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
          request.getSession().setAttribute("query", sbQuery2);
          request.getSession().setAttribute("formatoSalida", "pdf");
          request.getSession().setAttribute("accionPagina", "Generación de reporte operaciones contables");
          request.getSession().setAttribute("modulo", "Contabilidad");
          genera = true;
      }//end try
      catch(Exception e){
          System.err.println("Error al generar el reporte");
          
      }                   
  
  %>
    <input type="hidden" name="genera" id="genera" value="<%=genera%>">
    </form>
  </body>
</html>