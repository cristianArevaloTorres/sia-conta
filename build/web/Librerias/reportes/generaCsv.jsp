<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page language="java" import="java.util.*, java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="xScriptlet" class="sia.libs.archivo.Csv" scope="page"/>
<jsp:useBean id="xZips" class="sia.libs.archivo.Zip" scope="session"/>
<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/barraProgreso.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/catalogos.js"></script>
<html>
<head>
<title>Generación reportes</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>

<%
    Connection con = null;
    String rutaArchivo= (String)request.getSession().getAttribute("rutaArchivo");
    Map parametros= (Map)request.getSession().getAttribute("parametros"); 
    parametros = parametros == null ? new HashMap() : parametros;
    parametros.put("REPORT_LOCALE",new java.util.Locale("es","MX"));
    String formatoSalida= (String)request.getSession().getAttribute("formatoSalida");
    StringBuffer consulta= (StringBuffer)request.getSession().getAttribute("query");
    String strNombreAbrir = null;
    String nombreArchivo = (String)request.getSession().getAttribute("nombreArchivo");  //rutaArchivo.split("\\/")[rutaArchivo.split("\\/").length-1].split("\\.")[0];
    String rutaSinArchivo = rutaArchivo.substring(0,rutaArchivo.lastIndexOf("/"));
    String rutaSinArchivoSinReportes = rutaSinArchivo.substring(0,rutaSinArchivo.lastIndexOf("/"));
try {
    con = sia.db.dao.DaoFactory.getConnection((Integer)request.getSession().getAttribute("conexion"));
    util.eliminarTemporalesDias(application.getRealPath("/Reportes"+rutaSinArchivoSinReportes), 1);
    //util.eliminarTemporalesHoras(application.getRealPath("/MetasInst/Reportes/Pdfs/"),2);
    String nom_archivo= nombreArchivo;
    out.println(consulta.toString());
%>

<body onload="ajustarVentanaJsp();">
<%util.getHojaEstilo(out, request);%>

<form id="forma" name="forma" method="post" action="">
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
<table width="90%" class="general" align="center">
   <tr class="general">
    <th colspan="3" class="resultado" align="left">Archivos generados</th>
   </tr>
   <tr class="general">
       <th width="60%" class="general">Nombre del archivo</th>
       <th width="20%" class="general">Descargar</th> 
   </tr>


<%  try {
   
       //String ruta, String query, String nombreReporte, 
         //                          int conn, HttpServletRequest request, Writer out   
      
       if (xScriptlet.procesar("Reportes"+rutaSinArchivoSinReportes, consulta.toString(), nombreArchivo, 
          (Integer)request.getSession().getAttribute("conexion"), request, out)) {        
          
        }
        else
            strNombreAbrir = nom_archivo+"."+formatoSalida;
       
%>
            <tr class="general">
                            <td width="60%" class="general"><%=nombreArchivo%></td>
                            <td width="20%" class="general" align="center" font size="2"><a href=<%=xScriptlet.getRutaCompleta()%>>
                                            <img src="<%=util.getContexto(request)%>/Librerias/Imagenes/img<%=formatoSalida%>.gif" alt="<%=nom_archivo%>" border="0"> </a>
                            </td> 
   </tr>     
<%  
      
    } //try
    catch (Exception e) {
     System.out.println("[Error al generar el reporte] "+e.getMessage());
    } //catch
%>
 </table>
  <br>
  <center>
    <hr class="piePagina">
   </center>
</form> 
</body>
<%} //try
  catch (Exception e) {
    util.msgError(out, e.getMessage());
 }
 finally { 
      request.getSession().removeAttribute("rutaArchivo"); 
      request.getSession().removeAttribute("abrirReporte"); 
      request.getSession().removeAttribute("nombreArchivo"); 
      request.getSession().removeAttribute("parametros"); 
      request.getSession().removeAttribute("conexion"); 
      request.getSession().removeAttribute("formatoSalida"); 
      request.getSession().removeAttribute("query");
      request.getSession().removeAttribute("accionPagina");
      request.getSession().removeAttribute("modulo");
      if(con!= null)
        con.close();
      con = null;
    } 
%>
</html>
