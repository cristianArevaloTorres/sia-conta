<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ page import="sia.db.sql.Vista"%>
<jsp:useBean id="dtosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesRendimientosIFCtrl</title>
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
            //sentencia.addParam("idCuentaInversion",request.getParameter("cuenta"));
            sentencia.addParam("idCuentaInversion",idCuentaS);
            regresa = sentencia.getCommand("consultas.select.rendimientosIF.reportesInversiones");
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
            ///sen.addParam("idBanco"," and bi.id_banco =".concat(request.getParameter("banco")).concat(" and cb.id_cuenta_inversion =").concat(request.getParameter("cuenta")));      
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
   // String bancoCuenta = null;
    String[] datosCtaInv = null;
    Map parametros = new HashMap();
    try  { 
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
        parametros.put("FECHA_GENERACION",getFechaGeneracion());
        //bancoCuenta = getBancoCuenta(request);
        //parametros.put("INSTBANCARIA",bancoCuenta.substring(0,bancoCuenta.indexOf(",")));
        //parametros.put("CONTRATO",bancoCuenta.substring(bancoCuenta.indexOf(",")+1,bancoCuenta.length()));

        datosCtaInv = getBancoCuenta(request).split(",");
        parametros.put("INSTBANCARIA",datosCtaInv[0]);
        parametros.put("CONTRATO",datosCtaInv[1]);
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("autoriza"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","AUT");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
           parametros.put("NOM_VOBO",dtosFirmas.getString("NOMBRE"));
           parametros.put("PUESTO_VOBO",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("reviso"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","REV");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("NOM_REVISO",dtosFirmas.getString("NOMBRE"));
            parametros.put("PUESTO_REVISO",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();
        
        dtosFirmas.addParamVal("numEmpleado"," and NUM_EMPLEADO=:param ",request.getParameter("elaboro"));
        dtosFirmas.addParam("docto","DFIN");
        dtosFirmas.addParam("tipo","ELB");
        dtosFirmas.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
        if(dtosFirmas.next()){
            parametros.put("NOM_ELABORO",dtosFirmas.getString("NOMBRE"));
            parametros.put("PUESTO_ELABORO",dtosFirmas.getString("PUESTO_ESP"));
        }
        dtosFirmas.liberaParametros();  
        
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        if (request.getParameter("tipoReporte").equals("pdf"))
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/rendimientosIF");
        else
            request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/rendimientosIFXls");
        request.getSession().setAttribute("nombreArchivo","carteraInsFinFecha");
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