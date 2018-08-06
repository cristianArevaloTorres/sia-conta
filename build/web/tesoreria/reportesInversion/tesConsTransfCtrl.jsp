<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesConsTransfCtrl</title>
    <script language="JavaScript"  type="text/javascript">
     
       function ir(){
            document.getElementById('form1').action='../../Librerias/reportes/generaReporte.jsp';
            document.getElementById('form1').submit();
       }

    </script>
  </head>
 <%!
    private  sia.libs.periodo.Fecha estableceFormato(String fecha){
        sia.libs.periodo.Fecha regresa = null;
        try  {
          regresa = new sia.libs.periodo.Fecha(fecha, "/");
        } catch (Exception ex)  {
           ex.printStackTrace();
        }
        return regresa;
    }
    
    private String getQueryReporte(HttpServletRequest request){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fechaInicio",request.getParameter("fechaInicio"));
            sentencia.addParam("fechaFin",request.getParameter("fechaFinal"));
        //    if (!request.getParameter("inicioRango").equals(""))
          //      sentencia.addParam("folios","and (consecutivo between ('".concat(request.getParameter("inicioRango")).concat("') and ('").concat(request.getParameter("finRango")).concat("'))"  ));
        
            if (!request.getParameter("folioI").equals("") && !request.getParameter("folioF").equals("") )
                sentencia.addParam("folios","and (consecutivo between ('".concat(request.getParameter("folioI").trim()).concat("') and ('").concat(request.getParameter("folioF").trim()).concat("'))"  ));
        
            if (!request.getParameter("inicioImporte").equals(""))
                sentencia.addParam("importe"," and substr(INFORMACION,12,instr(INFORMACION,'~',12)-12) between ".concat(request.getParameter("inicioImporte")).concat(" and ").concat(request.getParameter("finImporte")));
            regresa = sentencia.getCommand("consultas.select.consolidadoTransf.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
            }
        return regresa; 
    }
  %>
  <%
    StringBuffer queryReporte = new StringBuffer();
    String resultado  = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/consolidadoTransf");
        request.getSession().setAttribute("nombreArchivo","consolidadoTransf");
        resultado = getQueryReporte(request);
        request.getSession().setAttribute("query",new StringBuffer(resultado)); 
        request.getSession().setAttribute("parametros",parametros);
    } catch (Exception ex)  {
        ex.printStackTrace();
    } finally  {
    }
    
  %>
  <body onload="ir()">
    <form id="form1" name="form1">
     <input type="hidden" id="tipoReporte" name="tipoReporte" value="<%=request.getParameter("tipoReporte")%>">
    </form>
  </body>
</html>