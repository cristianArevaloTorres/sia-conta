<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesInvTipoInstGlobalCtrl</title>
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
    
    private String getImporteCartera(HttpServletRequest request){
       SentenciasCRS sen = null;
       String regresa = null;
          try  {
            sen = new SentenciasCRS();    
            sen.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
            sen.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.importeInvTipoInstrumentoGlobal.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("IMPORTE");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
             sen = null;
        }
      return regresa; 
    }
    
    private String getQueryReporte(HttpServletRequest request){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
            sentencia.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));
            regresa = sentencia.getCommand("consultas.select.invTipoInstrumentoGlobal.reportesInversiones");
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
        parametros.put("TOTALIMPORTE",Double.parseDouble(getImporteCartera(request)));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/inversionTipoInstGlobal");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/invTipoInstGlobalXls");
        request.getSession().setAttribute("nombreArchivo","invTipoInstGlobal");
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
   </form>
  </body>
</html>