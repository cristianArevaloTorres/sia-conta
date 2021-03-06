<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ page import="sia.db.sql.Vista"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesInversionTipoInstCtrl</title>
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
    
    private String getQueryReporte(HttpServletRequest request, String idCuentaS){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
            sentencia.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));
//            sentencia.addParam("idCuentaInversion",request.getParameter("cuenta"));
            sentencia.addParam("idCuentaInversion",idCuentaS);
            sentencia.addParam("idBanco",request.getParameter("banco"));
            regresa = sentencia.getCommand("consultas.select.invTipoInstrumento.reportesInversiones");
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
  
  
    private String getBancoCuenta(HttpServletRequest request){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            //sen.addParam("idBanco"," and bi.id_banco =".concat(request.getParameter("banco")).concat(" and cb.id_cuenta_inversion =").concat(request.getParameter("cuenta")));      
            sen.addParam("idBanco"," and bi.id_banco =".concat(request.getParameter("banco")));      
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("NOMBRE_CORTO").concat(",").concat(sen.getString("CONTRATO_CUENTA")).concat(",").concat(sen.getString("ID_CUENTA_INVERSION"));
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
    }
   
  
    private String getImporteCartera(HttpServletRequest request, String idCuentaS){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();    
            sen.addParam("fechaInicial",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaInicio")).toString()));
            sen.addParam("fechaFinal",Fecha.formatear(Fecha.FECHA_CORTA_GUION,estableceFormato(request.getParameter("fechaFinal")).toString()));      
            sen.addParam("idBanco",request.getParameter("banco"));      
            //sen.addParam("idCuentaInversion",request.getParameter("cuenta"));
            sen.addParam("idCuentaInversion",idCuentaS);
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.importeInvTipoInstrumento.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("IMPORTE");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
    }
  
    
  %>
  <%
    StringBuffer queryReporte = new StringBuffer();
    String resultado  = null;
 //   String bancoCuenta = null;
    String[] datosCtaInv = null;
    Map parametros = new HashMap();
    try  { 
    
        datosCtaInv = getBancoCuenta(request).split(",");
        parametros.put("BANCO",datosCtaInv[0]);
        parametros.put("CUENTA",datosCtaInv[1]);
        
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
        parametros.put("FECHA_GENERACION",getFechaGeneracion());
        parametros.put("TOTALIMPORTE",Double.parseDouble(getImporteCartera(request,datosCtaInv[2])));
      
      //  bancoCuenta = getBancoCuenta(request);
     //   parametros.put("BANCO",bancoCuenta.substring(0,bancoCuenta.indexOf(",")));
      //  parametros.put("CUENTA",bancoCuenta.substring(bancoCuenta.indexOf(",")+1,bancoCuenta.length()));
      
        datosCtaInv = getBancoCuenta(request).split(",");
        parametros.put("BANCO",datosCtaInv[0]);
        parametros.put("CUENTA",datosCtaInv[1]);
        
      
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/inversionTipoInst");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/inversionTipoInstXls");
        request.getSession().setAttribute("nombreArchivo","inversionTipoInst");
        resultado = getQueryReporte(request, datosCtaInv[2]);
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