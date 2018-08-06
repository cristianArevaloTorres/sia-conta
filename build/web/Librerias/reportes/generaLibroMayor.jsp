
<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*, java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>
<jsp:useBean id="xZips" class="sia.libs.archivo.Zip" scope="session"/>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.Libros"%>
<%@ page import="sia.beans.seguridad.*"%>
<%@ page import="sia.scriptlets.DSA"%>
<%@ page import="sia.libs.archivo.*"%>

<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/barraProgreso.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/catalogos.js"></script>
<html>
    <head>
        <title>Generación reportes</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    </head>


    <%!        
        Autentifica _autentifica = null;
        Eliminar eliminarLibrosAnteriores = new Eliminar();
        String rutaRealJasperFuente = null;
    %>

    <%

        //_autentifica = (Autentifica) request.getSession().getAttribute("Autentifica");
        //if (_autentifica == null) {
        //  response.sendRedirect("../../Librerias/Funciones/CerrarSesion.jsp");
        //}
    
        //Codigo para generar un mes.....
        //String meses[] = {"SEPTIEMBRE"};
        //String mes[] = {"ago"};
        //String fechaIni[] = {"01/09/2013"};
        //String fechaFin[] = {"30/09/2013"};
        //Integer pagInicio[]={52251,54155};
        //termina código para generar un mes....*/

        Map parametros = (Map) request.getSession().getAttribute("parametros");
  
        //String fecha1 = (String) parametros.get("FECHA1");
        //String fecha2 = (String) parametros.get("FECHA2");
        String anio = (String) parametros.get("ANIO");
        String ctaMayor = (String) parametros.get("CTAMAYOR");
        String cta = (String) parametros.get("CTA");
        String descrip = (String) parametros.get("DESCRIP");
        //String ejercicio = fecha2 != null ? fecha1.substring(6, 10) : request.getParameter("ANIO");;
        
        
        Libros libros = new Libros(null, null, null);
        StringBuffer consulta = new StringBuffer();              
        
        String meses[] = {"ENERO", "FEBRERO", "MARZO", "ABRIL", "MAYO", "JUNIO", "JULIO", "AGOSTO", "SEPTIEMBRE", "OCTUBRE", "NOVIEMBRE", "DICIEMBRE"};
        String mes[] = {"ene", "ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov"};
        String fechaIni[] = {"01/01/" + anio, "01/02/" + anio, "01/03/" + anio, "01/04/" + anio, "01/05/" + anio, "01/06/" + anio, "01/07/" + anio, "01/08/" + anio, "01/09/" + anio, "01/10/" + anio, "01/11/" + anio, "01/12/" + anio};
        String fechaFin[] = {"31/01/" + anio, "28/02/" + anio, "31/03/" + anio, "30/04/" + anio, "31/05/" + anio, "30/06/" + anio, "31/07/" + anio, "31/08/" + anio, "30/09/" + anio, "31/10/" + anio, "30/11/" + anio, "31/12/" + anio};
        Integer pagInicio[] = {0, 100, 200, 300, 400, 500, 600, 700, 800, 900, 1000, 1100};

        Connection conexion = null;
        String nombreJasperFuente = (String) request.getSession().getAttribute("rutaArchivo");
        String rutaJasperFuente = nombreJasperFuente.substring(0, nombreJasperFuente.lastIndexOf("/"));
        String nmonicoLibro = (String) request.getSession().getAttribute("nombreArchivo");

        parametros.put("REPORT_LOCALE", new java.util.Locale("es", "MX"));

        String formatoSalida = (String) request.getSession().getAttribute("formatoSalida");
    %>

    <body onload="ajustarVentanaJsp();">
        <%util.getHojaEstilo(out, request);%>
        <form id="forma" name="forma" method="post" action="">
            <%util.tituloPagina(out, (String) request.getSession().getAttribute("modulo"), " ", (String) request.getSession().getAttribute("accionPagina"), "Reporte", true);%>
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
                <%
                    try {
                        conexion = sia.db.dao.DaoFactory.getConnection((Integer) request.getSession().getAttribute("conexion"));
                        eliminarLibrosAnteriores.eliminarNmonico(application.getRealPath("/Reportes" + rutaJasperFuente), nmonicoLibro);

                        for (int indice = 0; indice < meses.length; indice++) {
                            consulta.append(libros.sentenciaMayor(fechaIni[indice], fechaFin[indice], mes[indice], meses[indice], ctaMayor, anio));
                            parametros.put("CTA", cta);
                            parametros.put("DESCRIP", descrip);

                            //Parametros paginación
                            parametros.put("PAGINA_INICIO", pagInicio[indice]);
                            parametros.put("PAGINAS_TOTALES", new Integer(2000));
                            //parametros.put("PAGINA_INICIO", new Integer(0));

                            //Parametros paginación

                            String strNombreAbrir = null;
                            String archivoPDFDestino = "xSia" + util.formatearFecha(new java.util.Date(), "yyyyMMssddhhmmss") + "_" + meses[indice] + "_" + ctaMayor + "_" + nmonicoLibro;

                            parametros.put("NOMBREMES", meses[indice]);
                            parametros.put("SIA_OUT_REPORT", out);
                            parametros.put("SIA_FORM_REPORT", "forma");
                            parametros.put("DEBUG", new Boolean(false));
                            parametros.put("SIA_QUERY_REPORT", consulta.toString());
                            xScriptlet.setSource(application.getRealPath(nombreJasperFuente.split("\\.")[0]));
                            xScriptlet.setTarget(application.getRealPath("Reportes" + rutaJasperFuente + "/" + archivoPDFDestino));
                            xScriptlet.setConnection(conexion);
                            xScriptlet.setParameter(parametros);
                            
                            if (xScriptlet.procesar(formatoSalida, false)) {
                                if (formatoSalida.equals("xls") || (formatoSalida.equals("csv"))) {
                                    xZips.setDebug(false);
                                    xZips.setEliminar(true);
                                    xZips.compactar(application.getRealPath("Reportes" + rutaJasperFuente) + "/" + archivoPDFDestino + ".zip", application.getRealPath("Reportes" + rutaJasperFuente), "/" + archivoPDFDestino + "." + formatoSalida);
                                    strNombreAbrir = archivoPDFDestino + ".zip";
                                } else {
                                    strNombreAbrir = archivoPDFDestino + "." + formatoSalida;
                                }

                                out.write("<tr class='general'>");
                                out.write("<td width='60%' class='general'>");
                                out.write(meses[indice].toString());
                                out.write("</td>");
                                out.write("<td width='20%' class='general' align='center' font size='2'>");
                                out.write("<a href='" + util.getContexto(request) + "/Reportes" + rutaJasperFuente + "/" + strNombreAbrir + "' target='_blank'>");
                                out.write("<img src='" + util.getContexto(request) + "/Librerias/Imagenes/img" + formatoSalida + ".gif' alt='" + archivoPDFDestino + "' border='0'>");
                                out.write("</a>");
                                out.write("</td>");
                                out.write("</tr>");

                                consulta.delete(0, consulta.length());
                            } // if
                        } //  fin for
                    } catch (Exception e) {
                        System.out.println("[Error al generar el reporte] " + e.getMessage());
                    } finally {
                        request.getSession().removeAttribute("rutaArchivo");
                        request.getSession().removeAttribute("abrirReporte");
                        request.getSession().removeAttribute("nombreArchivo");
                        request.getSession().removeAttribute("parametros");
                        request.getSession().removeAttribute("conexion");
                        request.getSession().removeAttribute("formatoSalida");
                        request.getSession().removeAttribute("query");
                        request.getSession().removeAttribute("accionPagina");
                        request.getSession().removeAttribute("modulo");

                        if (conexion != null) {
                            conexion.close();
                        }

                        conexion = null;
                    }
                %>
            </table>
            <br>
            <center>
                <hr class="piePagina">
            </center>
        </form> 
    </body>

</html>
