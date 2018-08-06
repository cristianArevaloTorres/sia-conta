<%@page import="javax.xml.ws.Response"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
    "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=windows-1252"%>
<%@ page import="sia.db.dao.*, java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="abCtasMayor"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<jsp:useBean id="abEjercicios"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=windows-1252"/>
        <title>capaFechas</title>
    </head>

    <body onload="parent.loadSourceFinish('datosFecha');"></body>
    <%
        abEjercicios.setTableName("rf_tc_ejercicios");
        String ejerciciosSQL = "SELECT ejercicio, abierto FROM rf_tc_ejercicios ORDER BY ejercicio";
        abEjercicios.setCommand(ejerciciosSQL);

        int opcion = Integer.parseInt(request.getParameter("opcion"));
        Connection con = null;
    %>
    
    <br/>
    <table border="0" align="center" width="50%">
        <%if (opcion != 0) {%>
        <tr>
            <td width="25%">Fecha inicio: </td>
            <td>
                <input type='text' name='txtFecha01' id="txtFecha01" size='10' maxlength='10' class='cajaTexto' onchange="refrescar();" value='<%=request.getParameter("txtFecha01") == null ? "" : request.getParameter("txtFecha01")%>'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
            </td>
        </tr>
        <tr>
            <td width="25%">Fecha fin: </td>
            <td>
                <input type='text' name='txtFecha02' id="txtFecha02" size='10' maxlength='10' class='cajaTexto' onchange="refrescar();" value='<%=request.getParameter("txtFecha02") == null ? "" : request.getParameter("txtFecha02")%>'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
            </td>
        </tr>
        <%
        } else {

            try {
                con = DaoFactory.getContabilidad();
                abEjercicios.execute(con);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
            }

        %>         
        <tr>
            <td width="25%">
                Año:
            </td>
            <td>
                <select name="anioReporte" id="anioReporte" class="cajaTexto" OnChange="mostrarCuentasMayor()">
                    <option value='null' selected='selected'> - Seleccione - </option>
                    <%
                        String ejercicio = "";
                        abEjercicios.beforeFirst();
                        while (abEjercicios.next()) {
                            ejercicio = abEjercicios.getString(1);
                    %>
                    <option value="<%=ejercicio%>">
                        <%=ejercicio%>
                    </option>
                    <%
                        } // Fin while

                        abEjercicios.close();
                        abEjercicios = null;
                    %>
                </select>
            </td>
        </tr>

        <tr> 
            <td>
                Cuentas de mayor:
            </td>
            <td id="TDCuentasMayor">
                <select name="lstCtasMayor" id="lstCtasMayor" class="cajaTexto" >
                    <option value="null" selected="selected"> - Seleccione - </option>
                </select>
            </td>
        </tr> 


        <%}%>
    </table>
    <br/><br/>
</html>