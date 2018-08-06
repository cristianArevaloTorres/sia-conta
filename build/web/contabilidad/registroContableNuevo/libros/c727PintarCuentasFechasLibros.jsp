<%@page import="javax.xml.ws.Response"%>

<%@ page import="sia.db.dao.*, java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<jsp:useBean id="abCtasMayor"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<jsp:useBean id="abEjercicios"  class="sun.jdbc.rowset.CachedRowSet" scope="page"/>


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
            <input type='text' name='txtFecha01' id="txtFecha01" size='10' maxlength='10' class='cajaTexto' onchange="refrescar();" onblur="validadLlenadoFechas();"  >
            <a href="javascript: void(0);" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01');">
                <img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
        </td>
    </tr>
    <tr>
        <td width="25%">Fecha fin: </td>
        <td>
            <input type='text' name='txtFecha02' id="txtFecha02" size='10' maxlength='10' class='cajaTexto' onchange="refrescar();"  onblur="validadLlenadoFechas();">
            <a href="javascript: void(0);" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02');">
                <img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
        </td>
    </tr>
    <% } else {

        try {
            con = DaoFactory.getContabilidad();
            abEjercicios.execute(con);


    %>         
    <tr>
        <td width="25%">
            Año:
        </td>
        <td>
            <select name="anioReporte" id="anioReporte" class="cajaTexto" OnChange="mostrarCuentasMayor()">
                <option value='Seleccionar' selected='selected'> - Seleccionar - </option>
                <%
                    String ejercicio = "";
                    abEjercicios.beforeFirst();
                    while (abEjercicios.next()) {
                        ejercicio = abEjercicios.getString(1);
                        out.write("<option value='" + ejercicio + "'>");
                        out.write(ejercicio);
                        out.write("</option>");
                    } // Fin while


                %>
            </select>
        </td>
    </tr>

    <tr> 
        <td>
            <div id="DIVNombreCuentasMayor">
                Cuenta:
            </div>
        </td>

        <td>
            <div id="DIVListaCuentasMayor">
                <select name="lstCtasMayor" id="lstCtasMayor" class="cajaTexto">
                    <option value="Seleccionar" selected="selected"> - Seleccionar - </option>
                </select>
            </div>
        </td>
    </tr> 

    <%


            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {

                abEjercicios.close();
                abEjercicios = null;

                con.close();
                con = null;
            }


        }

    %>
</table>
<br/><br/>
