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
<%@ page import="sia.db.sql.Sentencias"%>
<%@ page import="sia.db.dao.*, java.sql.*"%>

<jsp:useBean id="abCtasMayor" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>


<%!    String ejercicio;
    Connection conexion;

%>


<%

    ejercicio = request.getParameter("idEjercicio");
    //ejercicio = "2013";

    abCtasMayor.setTableName("rf_tr_cuentas_contables");
    String SQL = "SELECT SUBSTR(t.cuenta_contable,1,4)cuenta_mayor,replace(to_char((substr(t.cuenta_contable,1,4)),'9,9,9,9'),',','.') cuenta, UPPER(t.descripcion) descripcion "
        + " FROM rf_tr_cuentas_contables t where t.id_catalogo_cuenta=1 and to_char(t.fecha_vig_ini,'yyyy')= '" + ejercicio + "' AND t.nivel=1 ORDER BY cuenta_mayor ";
    abCtasMayor.setCommand(SQL);

    conexion = null;

    try {

        conexion = DaoFactory.getContabilidad();
        abCtasMayor.execute(conexion);

        String cuentaMayor;
        String cuenta;
        String descripcion;

        //StringBuffer selector = new StringBuffer("<select id='lstCtasMayor' name='lstCtasMayor' class='cajaTexto' OnChange='mostrarListaTipoReportes(this.value)'>");
        StringBuffer selector = new StringBuffer("<select id='lstCtasMayor' name='lstCtasMayor' class='cajaTexto'>");
        selector.append("<option value='Seleccionar' selected='selected'> - Seleccionar - </option>");

        abCtasMayor.beforeFirst();
        while (abCtasMayor.next()) {
            cuentaMayor = abCtasMayor.getString(1);
            cuenta = abCtasMayor.getString(2);
            descripcion = abCtasMayor.getString(3);

            selector.append("<option value='").append(cuentaMayor).append("-");
            selector.append(cuenta).append("-");
            selector.append(descripcion);
            selector.append("'");
            selector.append(">");
            selector.append(cuentaMayor + " - " + descripcion);
            selector.append("</option>");
        }
        selector.append("</select>");

        out.write(selector.toString());
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        if (conexion != null) {
            conexion.close();
            conexion = null;
        }

        abCtasMayor.close();
        abCtasMayor = null;
    }

%>               

