<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="pbGrupos" class="sia.rf.seguridad.Grupos" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c760Control</title>
        <link rel="stylesheet"href="../../Librerias/Estilos/siaexplorer.css" type='text/css' >
    </head>

    <body>

        <%!    //Seccion de declaraciones de variables
            Connection conexion = null;
            String mensaje;
            String nombreGrupo = "";
            int claveGrupo = 0;
            int operacion = 0;
        %>

        <%
            Connection conexion = null;
            try {
                conexion = DaoFactory.getContabilidad();
                conexion.setAutoCommit(false);

                
                pbGrupos.setClaveGrupo(Integer.parseInt(request.getParameter("claveGrupo")));
                pbGrupos.setDescripcion(request.getParameter("TxtNombre").toUpperCase());
                
                operacion = Integer.parseInt(request.getParameter("operacion"));
                switch (operacion) {
                    // Agregar grupo
                    case 1:
                        pbGrupos.insertar(conexion);
                        mensaje = "El grupo <b>" + pbGrupos.getDescripcion() + "</b> has sido dado de alta satisfactoriamente.";
                        break;
                    // Modificar grupo
                    case 2:
                        pbGrupos.actualizar(conexion);
                        mensaje = "El grupo <b>" +  pbGrupos.getDescripcion() + "</b> has sido modificado satisfactoriamente.";
                        break;
                    // Eliminar grupo
                    case 3:
                        if (!pbGrupos.tienePerfiles(conexion)) {
                            pbGrupos.eliminar(conexion);
                            mensaje = "El grupo <b>" +  pbGrupos.getDescripcion() + "</b> has sido eliminado satisfactoriamente.";
                        } else {
                            mensaje = "El grupo <b>" +  pbGrupos.getDescripcion() + "</b> no fué eliminado debido a que tiene perfiles asociados.";
                        }
                        break;
                }

                conexion.commit();
            } catch (Exception E) {
                conexion.rollback();
                mensaje= "Ha ocurrido un error al accesar la Base de Datos,</p><p>favor de reportarlo al Administrador del Sistema.</p><p>Gracias.</p>";
            } finally {
                if (conexion != null) {
                    conexion.close();
                    conexion = null;
                }
            }

        %>

        <%util.tituloPagina(out, "Contabilidad", "Definición de grupos de trabajo", "Resultado", true);%>
        <br><br>
        <table align='center' class='general'>
            <thead>
                <tr>
                    <th class='azulObs'><b>      </b></th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td align="center" >
                        <p> <font color='#003399'><%= mensaje%></font></p>
                    </td>
                </tr>
            </tbody>
        </table>

        <br><br>
        <hr class="piePagina">
        <br>

        <form method="post" action="c760Resultado.jsp" id="formaRegresar">
            <table width="100%" align="center">
                <tr>
                    <td width="100%" align="center">
                        <input type="submit" name="btnAceptar" value="Aceptar" class="boton" >
                    </td>
                </tr>
            </table>
        </form>

    </body>
</html>