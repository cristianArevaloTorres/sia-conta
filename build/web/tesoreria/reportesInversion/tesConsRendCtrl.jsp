<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<jsp:useBean id="dtosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesConsRendCtrl</title>
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
            regresa = sentencia.getCommand("consultas.select.consolidadoRendimientos.reportesInversiones");
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
        
        parametros.put("IMG_DIR",request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/"));
        parametros.put("PERIODO", "del ".concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaInicio")).toString())).concat(" al ").concat( Fecha.formatear(Fecha.FECHA_LARGA,estableceFormato(request.getParameter("fechaFinal")).toString())));
        request.getSession().setAttribute("formatoSalida",request.getParameter("tipoReporte"));
        request.getSession().setAttribute("conexion",DaoFactory.CONEXION_TESORERIA);
        request.getSession().setAttribute("rutaArchivo","/tesoreria/reportesInversion/Reportes/consolidadoRendimientos");
        request.getSession().setAttribute("nombreArchivo","consolidadoRendimientos");
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