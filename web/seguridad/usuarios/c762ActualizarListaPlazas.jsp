<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="pbUsuarios" class="sia.rf.seguridad.Usuarios" scope="page"/>


<%!    
    String id_unidad;
    int id_entidad;
    String id_ambito;
    Connection conexion;
    String dlPlazas;
%>

<%

    id_unidad = request.getParameter("idUnidad");
    id_entidad = Integer.parseInt(request.getParameter("idEntidad"));
    id_ambito = request.getParameter("idAmbito");

    conexion = null;

    try {
        conexion = DaoFactory.getContabilidad();
        dlPlazas = pbUsuarios.obtenerCatalogoPlazas(conexion, id_unidad, id_entidad, id_ambito, false).toString();
        out.write(dlPlazas);
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        if (conexion != null) {
            conexion.close();
            conexion = null;
        }
    }

%>
