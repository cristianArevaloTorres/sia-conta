<%@page contentType="text/html; charset=windows-1252" language="java" import="java.util.*, java.sql.*"%>
<%@ page import="sia.beans.seguridad.*"%>
<%@ page import="sia.scriptlets.DSA"%>
<%@ page import="sia.libs.archivo.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>

<script language="JavaScript" type="text/javascript" src="<%=util.getContexto(request)%>/Librerias/Javascript/barraProgreso.js"></script>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html;  charset=windows-1252"/>
        <title>Generación Libro Diario Operaciones Lineas</title>
    </head>


    <%!        Autentifica _autentifica = null;
        Eliminar eliminarLibrosAnteriores = new Eliminar();
    %>

    <%

//        _autentifica = (Autentifica) request.getSession().getAttribute("Autentifica");
//        if (_autentifica == null) {
//            response.sendRedirect("../../Librerias/Funciones/CerrarSesion.jsp");
//        }

        DSA dsa = new DSA();
        Map parametros = (Map) request.getSession().getAttribute("parametros");

        String formatoSalida = (String) request.getSession().getAttribute("formatoSalida");
        String nmonicoLibro = (String) request.getSession().getAttribute("nombreArchivo");
        String archivoJasperFuente = (String) request.getSession().getAttribute("rutaArchivo");
        String rutaJasperFuente = archivoJasperFuente.substring(0, archivoJasperFuente.lastIndexOf("/"));
        String archivoPDFDestino = "xSia" + util.formatearFecha(new java.util.Date(), "yyyyMMssddhhmmss") + "_" + nmonicoLibro;

        String fuente = application.getRealPath(archivoJasperFuente.split("\\.")[0]);
        String destino = application.getRealPath("/Reportes" + rutaJasperFuente + "/" + archivoPDFDestino);

        String fechaAfectacionIzq = (String) request.getSession().getAttribute("fecha1");
        String fechaAfectacionDer = (String) request.getSession().getAttribute("fecha2");

        dsa.setDestino(destino);
        dsa.setFuente(fuente);
        dsa.setFechaAfectacionDer(fechaAfectacionDer);
        dsa.setFechaAfectacionIzq(fechaAfectacionIzq);

    %>

    <body>
        <%util.getHojaEstilo(out, request);%>

        <form action="" method="post" name="forma" id="forma">
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
                        eliminarLibrosAnteriores.eliminarNmonico(application.getRealPath("/Reportes" + rutaJasperFuente), nmonicoLibro);
                        dsa.fill(parametros);
                        dsa.pdf();
                        
                        out.write("<tr class='general'>");
                        out.write("<td width='60%' class='general'>" + nmonicoLibro + "</td>");
                        out.write("<td width='20%' class='general' align='center'>");
                        out.write("<a href='" + util.getContexto(request) + "/Reportes" + rutaJasperFuente + "/" + archivoPDFDestino + ".pdf' target='_blank'>");
                        out.write("<img src='" + util.getContexto(request) + "/Librerias/Imagenes/img" + formatoSalida + ".gif'  alt='" + archivoPDFDestino + "' border='0'>");
                        out.write("</a>");
                        out.write("</td>");
                        out.write("</tr>");
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
                    }

                %>
            </table>
        </form> 
    </body>
</html>

