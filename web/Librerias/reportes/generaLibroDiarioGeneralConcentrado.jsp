<%@page contentType="text/html; charset=iso-8859-1" language="java" import="java.util.*, java.sql.*"%>
<%@ page import="sia.beans.seguridad.*"%>
<%@ page import="sia.libs.archivo.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>
<jsp:useBean id="xZips" class="sia.libs.archivo.Zip" scope="session"/>

<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/barraProgreso.js"></script>
<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/catalogos.js"></script>
<html>
    <head>
        <title>Generación reportes</title>
        <meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
    </head>


    <%!        
        String unidadEjecutora;
        String ambito;
        String numEmpleado;
        Autentifica _autentifica = null;
        Eliminar eliminarLibrosAnteriores = new Eliminar();
    %>

    <%
//        _autentifica = (Autentifica) request.getSession().getAttribute("Autentifica");
//        if (_autentifica == null) {
//            response.sendRedirect("../../Librerias/Funciones/CerrarSesion.jsp");
//        }

        Connection conexion = null;
        String nmonicoLibro = (String) request.getSession().getAttribute("nombreArchivo");
        String formatoSalida = (String) request.getSession().getAttribute("formatoSalida");
        StringBuffer consulta = (StringBuffer) request.getSession().getAttribute("query");
        Map parametros = (Map) request.getSession().getAttribute("parametros");
        parametros.put("REPORT_LOCALE", new java.util.Locale("es", "MX"));
        String strNombreAbrir = null;


        String nombreJasperFuente = (String) request.getSession().getAttribute("rutaArchivo");
        String rutaJasperFuente = nombreJasperFuente.substring(0, nombreJasperFuente.lastIndexOf("/"));
        String archivoPDFDestino = "xSia" + util.formatearFecha(new java.util.Date(), "yyyyMMssddhhmmss") + "_" + nmonicoLibro;
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


                <%  try {
                        conexion = sia.db.dao.DaoFactory.getConnection((Integer) request.getSession().getAttribute("conexion"));
                        eliminarLibrosAnteriores.eliminarNmonico(application.getRealPath("/Reportes" + rutaJasperFuente), nmonicoLibro);

                        parametros.put("SIA_OUT_REPORT", out);
                        parametros.put("SIA_FORM_REPORT", "forma");
                        parametros.put("DEBUG", new Boolean(false));
                        parametros.put("SIA_QUERY_REPORT", consulta.toString());

                        //Parametros paginación
                        parametros.put("PAGINA_INICIO", new Integer(1));
                        parametros.put("PAGINAS_TOTALES", new Integer(999));
                        //Parametros paginación


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
                            out.write(nmonicoLibro.toString());
                            out.write("</td>");
                            out.write("<td width='20%' class='general' align='center' font size='2'>");
                            out.write("<a href='" + util.getContexto(request) + "/Reportes" + rutaJasperFuente + "/" + strNombreAbrir + "'  target='_blank' >");
                            out.write("<img src='" + util.getContexto(request) + "/Librerias/Imagenes/img" + formatoSalida + ".gif' alt='" + archivoPDFDestino + "' border='0'>");
                            out.write("</a>");
                            out.write("</td>");
                            out.write("</tr>");
                        }; // if
                    } //try
                    catch (Exception e) {
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
