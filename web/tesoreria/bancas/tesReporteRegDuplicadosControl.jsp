<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="sia.rf.tesoreria.bancas.acciones.Criterios"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesReporteRegDuplicadosControl</title>
     <script type="text/javascript" language="javascript">
       function ir(){
            document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
            document.getElementById('form1').submit();
       }

    </script>
  </head>
    <%!
     
    private String getQueryReporte(){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            regresa = sentencia.getCommand("criterios.select.registrosDuplicados.bancas");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
           }
        return regresa; 
    }
  %>
  <%
    String resultado  = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        request.getSession().setAttribute("formatoSalida","pdf");
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        request.getSession().setAttribute("rutaArchivo","/tesoreria/bancas/Reportes/repRegistrosDuplicados");
        request.getSession().setAttribute("nombreArchivo","repRegistrosDuplicados");
        resultado = getQueryReporte();
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
    } catch (Exception ex)  {
        ex.printStackTrace();
    } finally  {
    }
    
  %>
  <body onload="ir()">
    <form name="form1" id="form1" action="../../Librerias/reportes/generaReporte.jsp">
    </form>
  </body>
</html>