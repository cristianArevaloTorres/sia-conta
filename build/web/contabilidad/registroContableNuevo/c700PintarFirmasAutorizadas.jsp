<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista,java.util.*,sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Vista,sia.rf.contabilidad.reportes.DatosReportes"%>
<%@ page import="sia.libs.recurso.Contabilidad"%>
<%@ page import="sia.rf.contabilidad.reportes.estadosFinancieros.Libros"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="java.text.DateFormat"%>
<%@ page import="java.text.ParseException"%> 
<%@ page import="sia.db.sql.Sentencias"%>
<%@ page import="sia.db.dao.*, java.sql.*"%>

<%
    ControlRegistro controlReg = (ControlRegistro) request.getSession().getAttribute("controlRegistro");
    Map parametros = null;
    parametros = new HashMap();

    boolean existeFirmantes = false;


    List<Vista> registros = new ArrayList<Vista>();
    Sentencias sentencia = null;
    try {

        sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD, Sentencias.XML);

        String mes = request.getParameter("idMes");
        parametros.put("mes", mes);

        String documento = request.getParameter("idDocumento");
        parametros.put("documento", documento);

        int ejercicio = Integer.parseInt(request.getParameter("idEjercicio"));
        String unidad = request.getParameter("idUnidad");
        int ambito = Integer.parseInt(request.getParameter("idAmbito"));
        int entidad = Integer.parseInt(request.getParameter("idEntidad"));

        parametros.put("unidad", unidad);
        parametros.put("entidad", entidad);
        parametros.put("ambito", ambito);
        parametros.put("ejercicio", ejercicio);
        parametros.put("firma", "ELB");

        registros = sentencia.registros("reportes.select.firmas", parametros);

        out.write("<table border='0' width='100%' align='center'>");
        if (registros != null) {
            out.write("<tr>");
            out.write("<td align='right'>");
            out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            out.write("ELABORÓ:");
            out.write("</td>");
            out.write("<td>");
            out.write("<select id='elaboro' name='elaboro' class='cajaTexto'>");

            for (Vista record : registros) {
                out.write("<option value='" + record.getField("NUM_EMPLEADO") + "'>" + record.getField("NOMBRE") + "</option>");
            }

            out.write("</select>");
            out.write("</td>");
            out.write("</tr>");

            existeFirmantes = true;
        }

        registros = null;
        registros = new ArrayList<Vista>();
        parametros.put("firma", "REV");
        registros = sentencia.registros("reportes.select.firmas", parametros);

        if (registros != null) {
            out.write("<tr>");
            out.write("<td align='right'>");
            out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            out.write("REVISÓ:");
            out.write("</td>");
            out.write("<td>");
            out.write("<select id='reviso' name='reviso' class='cajaTexto'>");

            for (Vista record : registros) {
                out.write("<option value='" + record.getField("NUM_EMPLEADO") + "'>" + record.getField("NOMBRE") + "</option>");
            }

            out.write("</select>");
            out.write("</td>");
            out.write("</tr>");

            existeFirmantes = true;
        }


        registros = null;
        registros = new ArrayList<Vista>();
        parametros.put("firma", "AUT");
        registros = sentencia.registros("reportes.select.firmas", parametros);
        if (registros != null) {
            out.write("<tr>");
            out.write("<td align='right'>");
            out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            out.write("AUTORIZÓ:");
            out.write("</td>");
            out.write("<td>");
            out.write("<select id='autorizo' name='autorizo' class='cajaTexto'>");

            for (Vista record : registros) {
                out.write("<option value='" + record.getField("NUM_EMPLEADO") + "'>" + record.getField("NOMBRE") + "</option>");
            }

            out.write("</select>");
            out.write("</td>");
            out.write("</tr>");

            existeFirmantes = true;
        }

        if (!existeFirmantes) {
            out.write("<tr>");
            out.write("<td align='right'>");
            out.write("&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;");
            out.write("<b>No existen firmantes autorizadaos para el mes seleccionado.</b>");
            out.write("</td>");
            out.write("</tr>");
        }

        out.write("</table>");



    } catch (Exception e) {
        System.err.println("Error al consultar las firmas");
    } finally {
    }
%>
</table>
</body>
</html>