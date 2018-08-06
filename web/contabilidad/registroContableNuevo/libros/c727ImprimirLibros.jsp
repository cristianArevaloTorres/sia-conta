<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Vista,sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.Libros"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.ParseException"%> 
<%@ page import="java.text.DateFormat"%> 
<%@ page import="java.text.SimpleDateFormat"%> 
<%@ page import="sia.db.sql.Sentencias"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c727ImprimirReporte</title>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script type="text/javascript" language="javascript">
            function envia() {
                if(document.getElementById("genera").value=='true')
                    document.getElementById("forma").submit();
            }
        </script>
    </head>

    <body onload="envia()">
        <% util.tituloPagina(out, "Contabilidad", " ", "Resultado", "Generación de libros", true);%>

        <%!            Date fecha_afectacion_izq = null;
            Date fecha_afectacion_der = null;
            String mes = null;
        %>

        <%
            Libros libros = null;
            String pagina = null;
            boolean genera = false;
            String reporte = null;
            String formatoReporte = null;
            List<Vista> registros = new ArrayList<Vista>();
            DatosReportes datosReportes = new DatosReportes();
            String firmas = null;
            StringBuffer query = new StringBuffer();
            Map mapParams = new HashMap();
            //String unidad = null;
            //String entidad = null;
            //String ambito = null;
            String nmonicoLibro = null;


            firmas = request.getParameter("RBFirmas");
            String fecha1 = request.getParameter("txtFecha01");
            String fecha2 = request.getParameter("txtFecha02");



            //modificar fecha para ir generando reporte por mes
            //String fecha1 = "01/04/2012";
            //String fecha2 = "15/04/2012";
            //mes = request.getParameter("mesReporte")==null?fecha2.substring(3,5): request.getParameter("mesReporte");
            String ejercicio = fecha2 != null ? fecha1.substring(6, 10) : request.getParameter("anioReporte");
            try {
                //ControlRegistro controlReg = (ControlRegistro) session.getAttribute("controlRegistro");
                libros = new Libros(fecha1, fecha2, ejercicio);


                switch (Integer.valueOf(request.getParameter("reporte"))) {
                    case 0:
                        nmonicoLibro = "MAY";

                        String datos = request.getParameter("lstCtasMayor");
                        String datos1[] = datos.split("-");
                        String ctaMayor = datos1[0];
                        String cta = datos1[1];
                        String descrip = datos1[2];

                        mapParams.put("CTAMAYOR", ctaMayor);
                        mapParams.put("CTA", cta);
                        mapParams.put("DESCRIP", descrip);

                        pagina = "generaLibroMayor.jsp";
                        //pagina = "generaLibroMayorRecursivo.jsp";
                        reporte = "repLibroMayor" + firmas + "Firmas";
                        break;
                    case 1:
                        nmonicoLibro = "LDC";
                        libros.procesoGenerarLibroDiarioGeneralConcentrado();
                        query.append(libros.sentenciaConcentrado());
                        pagina = "generaLibroDiarioGeneralConcentrado.jsp";
                        reporte = "libroDiarioConcentrado" + firmas + "Firmas";
                        break;
                    case 2:

                        mes = fecha2.substring(3, 5);

                        request.getSession().setAttribute("fecha1", fecha1);
                        request.getSession().setAttribute("fecha2", fecha2);

                        SimpleDateFormat formateador = new SimpleDateFormat("dd/MM/yyyy");
                        fecha_afectacion_izq = formateador.parse(fecha1);
                        fecha_afectacion_der = formateador.parse(fecha2);

                        mapParams.put("FECHA_AFECTACION_IZQ", util.formatearFecha(fecha_afectacion_izq, "dd 'de' MMMM 'de' yyyy").toUpperCase());
                        mapParams.put("FECHA_AFECTACION_DER", util.formatearFecha(fecha_afectacion_der, "dd 'de' MMMM 'de' yyyy").toUpperCase());

                        nmonicoLibro = "LDG";
                        //Este reporte se genera a través de la clase sia.scriptlets.DataSourceApp.java en la clase se va cambiando la fecha de cada mes.
                        pagina = "generaLibroDiarioOperacionesLineas.jsp";

                        if (mes.equals("12")) {
                            // Solamente DICIEMBRE lleva firmas
                            reporte = "libroDiarioPorOperacionLineasDic";
                        } else {
                            // De ENERO a NOVIEMBRE no incluyen firmas
                            reporte = "libroDiarioPorOperacionLineasEneNov";
                        }
                        //reporte = "libroDiarioPorOperacionLineasSinFirmas" + firmas + "Firmas";
                        //reporte = "libroDiarioPorOperacionLineasSinFirmas";

                        break;

                }

                String unidad_ejecutora = "002";
                String entidad = "9";
                String ambito = "1";
                String mes_firmas = "12";

                
                registros = datosReportes.obtenerTitulos(unidad_ejecutora, entidad, ambito);
                mapParams.put("TITULO1", registros.get(0).getField("TITULO1"));
                mapParams.put("TITULO2", registros.get(0).getField("TITULO2"));
                mapParams.put("TITULO3", registros.get(0).getField("TITULO3"));

                mapParams.put("ANIO", request.getParameter("anioReporte"));
                if (Integer.valueOf(request.getParameter("formato")) == 0) {
                    mapParams.put("SIA_QUERY_REPORT", query.toString());
                    mapParams.put("SIA_OUT_REPORT", out);
                    mapParams.put("SIA_FORM_REPORT", "forma");
                    mapParams.put("DEBUG", new Boolean(false));
                    mapParams.put("EJERCICIO", new Integer(ejercicio));
//                    mapParams.put("UNIDAD_EJECUTORA", controlReg.getUnidad());
//                    mapParams.put("ENTIDAD", String.valueOf(controlReg.getEntidad()));
//                    mapParams.put("AMBITO", String.valueOf(controlReg.getAmbito()));
                    mapParams.put("UNIDAD_EJECUTORA", unidad_ejecutora);
                    mapParams.put("ENTIDAD", String.valueOf(entidad));
                    mapParams.put("AMBITO", String.valueOf(ambito));
                    mapParams.put("PAGINA_INICIO", new Integer(0));
                    //mapParams.put("PAGINAS_TOTALES",new Integer(223460));
                    //if(mes!=null)
                    //mapParams.put("MES", new Integer(mes));
                    mapParams.put("MES_FIRMAS", mes_firmas);
                    mapParams.put("DOCUMENTO", nmonicoLibro);
                    mapParams.put("ELABORO", request.getParameter("elaboro"));
                    mapParams.put("REVISO", request.getParameter("reviso"));
                    mapParams.put("AUTORIZO", request.getParameter("autorizo"));
                    mapParams.put("IMG_DIR", request.getSession().getServletContext().getRealPath("/Librerias/Imagenes/").toString());
                    formatoReporte = "pdf";

                    request.getSession().setAttribute("abrirReporte", "abrir");
                    request.getSession().setAttribute("nombreArchivo", nmonicoLibro);
                    request.getSession().setAttribute("rutaArchivo", "/contabilidad/registroContableNuevo/estadosFinancieros/reportes/libros/" + reporte);
                    request.getSession().setAttribute("parametros", mapParams);
                    request.getSession().setAttribute("conexion", DaoFactory.CONEXION_CONTABILIDAD);
                    request.getSession().setAttribute("query", query);
                    request.getSession().setAttribute("formatoSalida", "pdf");
                    request.getSession().setAttribute("accionPagina", "Generacion de reporte de formas contables");
                    request.getSession().setAttribute("modulo", "Contabilidad");

                } else {
                    mapParams = null;
                    formatoReporte = "xls";
                    pagina = "generaXls.jsp";
                    /*repClase.imprimir(request, query, mapParams, reporte,
                     "/contabilidad/estadosFinancieros/reportes/libros/", DaoFactory.CONEXION_CONTABILIDAD, 
                     "", "Generar archivo XLS", "Contabilidad");*/
                }
                genera = true;

            }//end try
            catch (Exception e) {
                System.err.println("Error al generar el reporte");
            } finally {
            }
        %>
        <form id="forma" name="forma" action="../../../Librerias/reportes/<%=pagina%>">
            <br><br><br><br><br>
            <input type="hidden" name="genera" id="genera" value="<%=genera%>">
            <table border="0" width="30%" class='sianoborder' align="center">
                <%
                    if (genera) {
                        out.write("<tr>");
                        out.write("<td>");
                        out.write("<b>Generando el reporte, espere un momento...</b>");
                        out.write("</td>");
                        out.write("</tr>");
                    } else {
                        out.write("<tr>");
                        out.write("<td>");
                        out.write("<b>Hubo un error al momento de generar el reporte, notifíquelo al administrador....</b>");
                        out.write("</td>");
                        out.write("</tr>");
                    }
                %>
            </table>
            <hr noshade size='5' width="50%">

        </form>
    </body>
</html>