<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesReporteResultado</title>
     <script type="text/javascript" language="javascript">
       function ir(){
            document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
            document.getElementById('form1').submit();
       }

    </script>
  </head>
  <%!
    private String getQueryReporte(HttpServletRequest request){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fecha",request.getParameter("fechaCarga"));
            sentencia.addParam("idPrograma",request.getParameter("idProgramaS"));
            regresa = sentencia.getCommand("criterios.select.repSaldoDiario.bancas");
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
    Date fechaDate = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        fechaDate = sdf.parse(request.getParameter("fechaCarga"));
        parametros.put("fecha",fechaDate);
        request.getSession().setAttribute("formatoSalida","pdf");
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        request.getSession().setAttribute("rutaArchivo","/tesoreria/bancas/Reportes/repSaldosDiarios");
        request.getSession().setAttribute("nombreArchivo","repSaldosDiarios");
        resultado = getQueryReporte(request);
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
    } catch (Exception ex)  {
        ex.printStackTrace();
    } finally  {
    }
    
  %>
  <body onload="ir()">
     <form name="form1" id="form1" action="../../Librerias/reportes/generaReporte.jsp">
     <input type="hidden" id="nombre" name="nombre" value="<%=request.getParameter("nombre")%>">
     <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
     <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
     <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
     <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
     <input type="hidden" id="mensaje" name="mensaje" value="<%=request.getParameter("mensaje")%>">
     <input type="hidden" id="fechaCarga" name="fechaCarga" value="<%=request.getParameter("fechaCarga")%>">
    </form>
  </body>
</html>