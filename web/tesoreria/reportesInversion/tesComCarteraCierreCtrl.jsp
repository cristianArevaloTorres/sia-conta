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
    <title>tesComCarteraCierreCtrl</title>
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
    
     private String getContrato(String cuenta, String banco){
       SentenciasCRS sen = null;
          String regresa = null;
          try  {
            sen = new SentenciasCRS();    
            sen.addParam("idBanco"," and cb.id_banco=".concat(banco).concat(" and id_cuenta_inversion =").concat(cuenta));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("CONTRATO_CUENTA");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sen = null;
        }
        return regresa; 
    }
    
    private String getImporteCartera(HttpServletRequest request,String idCuentaInv){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            sen.addParam("idBanco",request.getParameter("banco"));      
            sen.addParam("idCuentaInversion",idCuentaInv);  
            sen.addParam("fechaInicial",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat("01"));
            sen.addParam("fechaFinal",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"consultas.select.importeCarteraInstFinCierre.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("IMPORTE");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
   }
    
    
    private String getQueryReporte(HttpServletRequest request, String idCuentaInv){
        String regresa = null;
        SentennciasSE sentencia = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {      
            sentencia.addParam("fechaInicial",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat("01"));
            sentencia.addParam("fechaFinal",request.getParameter("anio").concat("-").concat(Cadena.rellenar(request.getParameter("mes"),2,'0',true)).concat("-").concat(String.valueOf(Fecha.getDiasEnElMes(Integer.parseInt(request.getParameter("anio")), Integer.parseInt(request.getParameter("mes"))-1))));
            sentencia.addParam("idBanco",request.getParameter("banco"));
            sentencia.addParam("idCuentaInversion",idCuentaInv);
            regresa = sentencia.getCommand("consultas.select.carteraInstFinCierre.reportesInversiones");
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
              sentencia = null;
        }
        return regresa; 
    }
    
   private String getBancoCuenta(HttpServletRequest request){
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            sen.addParam("idBanco"," and bi.id_banco =".concat(request.getParameter("banco")));      
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");
            if(sen.next())
              regresa = sen.getString("NOMBRE_CORTO").concat(",").concat(sen.getString("CONTRATO_CUENTA")).concat("|").concat(sen.getString("ID_CUENTA_INVERSION"));
        } catch (Exception e)  {
           e.printStackTrace();
        }  finally  {
            sen = null;
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
    String idCuentaSel = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO",Fecha.getNombreMes(Integer.parseInt(request.getParameter("mes"))-1).concat(" ").concat(request.getParameter("anio")) );
        parametros.put("FECHA_GENERACION",getFechaGeneracion());
        bancoCuenta = getBancoCuenta(request);
        idCuentaSel = bancoCuenta.substring(bancoCuenta.indexOf("|")+1,bancoCuenta.length());
        parametros.put("INSTBANCARIA",bancoCuenta.substring(0,bancoCuenta.indexOf(",")));
        parametros.put("CUENTAINVERSION",bancoCuenta.substring(bancoCuenta.indexOf(",")+1,bancoCuenta.indexOf("|")));
        parametros.put("TOTALIMPORTE",Double.parseDouble(getImporteCartera(request,idCuentaSel)));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/comCarteraCierre");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/comCarteraCierreXls");
        request.getSession().setAttribute("nombreArchivo","carteraCierreMes");
        resultado = getQueryReporte(request,idCuentaSel);
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