<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="java.sql.*" %>
<%@ page import="sia.db.sql.*" %>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i004tesComprasInversionCapaValidarCompra</title>
  </head>
  <%!
        public static String obtenDiaSiguienteHabil(String fecha, Integer plazo) {
          SentenciasCRS sen = null;
          String regresa = null;
          try  {
            sen = new SentenciasCRS();
            sen.addParam("fecha",fecha);
            sen.addParam("plazo",plazo);
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.ObtenDiaSiguienteHabil.inversiones");
            if(sen.next())
              regresa = sen.getString("campo1");
          } catch (Exception ex)  {
            ex.printStackTrace();
          } finally  {
          }
          return regresa;
        }
        
        public static void validaFechaHabil(String fecha, Integer plazo,String vencimiento) throws Exception {
          String fechaHabil = obtenDiaSiguienteHabil(fecha, plazo);
          if( fechaHabil != null) {
            if(!fechaHabil.equals(vencimiento))
              throw new Exception("La fecha habil, no coincide con la fecha de vencimiento. \\n Fecha sugerida: ".concat(fechaHabil));
          }
          else
            throw new Exception("Error al obtener dia siguiente habil");
        }
  %>
  <%
    String fecha        = request.getParameter("fecha");
    String plazo        = request.getParameter("plazo");
    String vencimiento  = request.getParameter("vencimiento");
    String mensaje = ""; 
    try  {
      validaFechaHabil(fecha,Integer.parseInt(plazo),vencimiento);
    } catch (Exception ex)  {
      mensaje = ex.getMessage();
      ex.printStackTrace();
      
    } finally  {
    }
    
  %>
  <body onload="parent.regresaValidaCompra('<%=mensaje%>')">
  
  
  </body>
</html>