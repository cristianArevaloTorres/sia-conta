<%-- 
    Document   : generaReporteDataSource
    Created on : 2/07/2013, 04:12:43 PM
    Author     : claudia.macariot
--%>
<%@ page contentType="text/html;charset=windows-1252" import="net.sf.jasperreports.engine.JasperExportManager,net.sf.jasperreports.engine.JasperFillManager,net.sf.jasperreports.engine.JasperPrint,java.util.*,java.io.*,java.sql.Connection"%>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.SituacionFinancieraComparativo"%>
<%@ page import="net.sf.jasperreports.engine.data.JRBeanCollectionDataSource" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>
<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/barraProgreso.js"></script>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
    <title>generaReporteESF</title>
  </head>
  
<%
  //EstadoFinanciero edoFinanciero = null;
  //EstadoFinancieroAcumulado edoFinancieroAcum = null;
  //SaldosEstadoFinanciero edoFinanciero = null;
  SituacionFinancieraComparativo edoFinanciero = null;
  //SaldosEstadoFinancieroAcumulado edoFinancieroAcum = null;
  Connection con= null;
  
  JasperPrint jasperPrint = null;     
  try{
  
    String nombreArchivo = null;
    String rutaArchivo = null;   
    //String strRutaImagen = null;
    //Boolean acumulado = false;
    Map parametros = (Map)request.getSession().getAttribute("parametros");     
    //acumulado = (Boolean)request.getSession().getAttribute("acumulado");
   // if (!acumulado) {
      //edoFinanciero  = (EstadoFinanciero)request.getSession().getAttribute("edoFinanciero");
      edoFinanciero  = (SituacionFinancieraComparativo)request.getSession().getAttribute("edoFinancieroCom");
   /* }
    else {
      //edoFinancieroAcum = (EstadoFinancieroAcumulado)request.getSession().getAttribute("edoFinancieroAcum");
      edoFinancieroAcum = (SaldosEstadoFinancieroAcumulado)request.getSession().getAttribute("edoFinancieroAcum");
    }*/
    nombreArchivo  =(String)request.getSession().getAttribute("nombreArchivo");
    rutaArchivo    =(String)request.getSession().getAttribute("rutaArchivo");   
    
    String rutaSinArchivo = rutaArchivo.substring(0,rutaArchivo.lastIndexOf("/"));
    String rutaSinArchivoSinReportes = rutaSinArchivo.substring(0,rutaSinArchivo.lastIndexOf("/"));
    util.eliminarTemporalesDias(application.getRealPath("/Reportes/"+rutaSinArchivoSinReportes), 1);
    String nom_archivo= "xSia"+ util.formatearFecha(new java.util.Date(), "yyyyMMddhhmmss")+ "_"+nombreArchivo;
    String  strNombreAbrir = nom_archivo+".pdf";      
%>
       
  <body>
    <%util.getHojaEstilo(out, request);%>
    <form action="" method="post" name="forma" id="forma">
      <%util.tituloPagina(out, (String)request.getSession().getAttribute("modulo"), " ",(String)request.getSession().getAttribute("accionPagina"), "Reporte", true);%>  
    <table id= "avancePorcentaje" width="100%" border="0" align="center">
  <tr>
   <td align="left" width="33%"><font color="#003366" size="1"><strong>0%</strong></font></td>
   <td align="center" width="34%"><input type="text" name="proceso" id="proceso" value="0%" size="3" onFocus="this.blur();" class='cajaTexto'></td>
     <td align="right" width="33%"><font color="#003366" size="1"><strong>100%</strong></font></td>
  </tr>
 </table> 
   <table id= "graficoPorcentaje" width="100%" border="0" align="center">
     <tr class="general">
        <td class="general" id="llevaProcesado" width='0%' bgcolor="#65C42D">&nbsp;</td>
        <td class="general" id="faltaProcesar" width='100%' bgcolor="#FF3300">&nbsp;</td>
     </tr>
   </table>
    <br>
    <%out.println("<br><b><center>Espere por favor, exportando archivo. !</center></b><br>");%>  
    </form>
    <table width="90%" class="general" align="center">
     <tr class="general">
       <th colspan="3" class="resultado" align="left">Archivos generados</th>
     </tr>
    <tr class="general">
       <th width="60%" class="general">Nombre del archivo</th>
       <th width="20%" class="general">Descargar</th> 
   </tr>
   <%
     con = sia.db.dao.DaoFactory.getConnection((Integer)request.getSession().getAttribute("conexion"));
     xScriptlet.setSource(application.getRealPath(rutaArchivo.split("\\.")[0]));
     xScriptlet.setTarget(application.getRealPath("Reportes"+rutaSinArchivoSinReportes+"/"+nom_archivo));
     xScriptlet.setConnection(con);  
     parametros.put("SIA_OUT_REPORT", out);
     parametros.put("SIA_FORM_REPORT", "forma");
     parametros.put("DEBUG", new Boolean(true));
     parametros.put("REPORT_CONNECTION",con);
    /// if (!acumulado) {
        parametros.put("SIA_RECORD_COUNT",edoFinanciero.getCount());
    /* }
     else {
        parametros.put("SIA_RECORD_COUNT",edoFinancieroAcum.getCount());
     }*/
     xScriptlet.setParameter(parametros);
     File archivo=new File(application.getRealPath("/Reportes"+rutaSinArchivoSinReportes+"/"));
     if (!(archivo.exists())) 
          archivo.mkdirs();
     //if (!acumulado) {
        jasperPrint   = JasperFillManager.fillReport(application.getRealPath(rutaArchivo)+".jasper", parametros, new JRBeanCollectionDataSource(edoFinanciero.getListaFinal()));   
     /*}
     else {
        jasperPrint   = JasperFillManager.fillReport(application.getRealPath(rutaArchivo)+".jasper", parametros, edoFinancieroAcum);   
     }  */   
     JasperExportManager.exportReportToPdfFile(jasperPrint,application.getRealPath("//Reportes"+rutaSinArchivoSinReportes+"//"+strNombreAbrir));            
     
   %>
       <tr class="general">
          <td width="60%" class="general"><%=nombreArchivo%></td>
          <td width="20%" class="general" align="center" font size="2"><a href=<%=util.getContexto(request)+"/Reportes"+rutaSinArchivoSinReportes+"/"+strNombreAbrir%>>
           <img src="<%=util.getContexto(request)%>/Librerias/Imagenes/imgpdf.gif" alt="<%=nom_archivo%>" border="0"> </a>
         </td> 
       </tr>
   </table>
  </body>  
  <%
  }catch (Exception e){
     System.out.println("Error en la generacion del reporte");
     e.printStackTrace();
   }finally{
     request.getSession().removeAttribute("parametros");
     request.getSession().removeAttribute("edoFinanciero");
     request.getSession().removeAttribute("nombreArchivo");
     request.getSession().removeAttribute("rutaArchivo"); 
     request.getSession().removeAttribute("modulo");
     request.getSession().removeAttribute("accionPagina");
     request.getSession().removeAttribute("conexion");    
     request.getSession().removeAttribute("acumulado");
     edoFinanciero=null;
     jasperPrint=null;   
     if (con !=null)
      con.close();
     con = null; 
      
      
   }  
  %>
</html>