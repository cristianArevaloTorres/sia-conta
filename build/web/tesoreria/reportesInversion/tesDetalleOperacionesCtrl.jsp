<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="sia.db.sql.Vista" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesDetalleOperacionesCtrl</title>
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
        String idCuentaInversion     =  request.getParameter("fidCuentaInversion");
        String fechaInicio           =  request.getParameter("fechaInicio");
        String fechaFinal            =  request.getParameter("fechaFinal");

        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            if (!idCuentaInversion.equals(""))
              sentencia.addParam("idCuentaInversion","and id_cuenta_inversion=".concat(idCuentaInversion));
            sentencia.addParam("periodo","and (trunc(fecha) >= to_date('".concat(fechaInicio).concat("','dd/mm/yyyy') and  trunc(fecha) <= to_date('").concat(fechaFinal).concat("','dd/mm/yyyy'))") );

            regresa = sentencia.getCommand("consultas.select.detalleOperaciones.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
           }
           finally  {
              sentencia = null;
            }
        return regresa; 
    }
    
      private String getInstitucion(String idCuentaInversion) {
          SentenciasCRS sen = null;
          String regresa = null;
          try  {
            sen = new SentenciasCRS();
            sen.addParam("idBanco"," and id_cuenta_inversion=".concat(idCuentaInversion));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("NOMBRE_CORTO");
          } catch (Exception ex)  {
            ex.printStackTrace();
          } finally  {
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
        parametros.put("BANCO",getInstitucion(request.getParameter("fidCuentaInversion")));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/detalleOperaciones");
        request.getSession().setAttribute("nombreArchivo","detalleOperaciones");
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