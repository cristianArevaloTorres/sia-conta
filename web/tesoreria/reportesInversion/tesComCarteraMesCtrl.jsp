<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.libs.formato.Cadena" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesComCarteraMesCtrl</title>
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
            sen.addParam("fechaInicial",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            sen.addParam("fechaFinal",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.importeCarteraCierreMes.reportesInversiones");
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
            sentencia.addParam("fechaInicial",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            sentencia.addParam("fechaFinal",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            regresa = sentencia.getCommand("consultas.select.carteraCierreMes.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return regresa; 
    }
    
  
       
  private String getFechaGeneracion(){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        List<Vista> registros = null;
        try  {      
            registros = sentencia.registros("select to_char(sysdate,'dd/mm/yy HH24:mm:ss') fechaOrigen from dual");
        if (registros!=null)
            regresa = registros.get(0).getField("FECHAORIGEN");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
              registros = null;
        }
        return regresa; 
  }
  
 
    
  %>
  <%
    StringBuffer queryReporte = new StringBuffer();
    String resultado  = null;
    String bancoCuenta = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO",Fecha.getNombreMes(Integer.parseInt(request.getParameter("mes"))-1).concat(" ").concat(request.getParameter("anio")) );
        parametros.put("FECHA_GENERACION",getFechaGeneracion());
        parametros.put("TOTALIMPORTE",Double.parseDouble(getImporteCartera(request)));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/comCarteraMes");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/comCarteraMesXls");
        request.getSession().setAttribute("nombreArchivo","compCarteraCierreMes");
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