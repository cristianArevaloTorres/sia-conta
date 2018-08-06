<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="pbUsuarios" class="sia.rf.seguridad.Usuarios" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c762Control</title>
        <link rel="stylesheet"href="../../Librerias/Estilos/siaexplorer.css" type='text/css' >
    </head>

    <body>

        <%!            //Seccion de declaraciones de variables
            Connection conexion = null;
            String mensaje;
            int idPerfil;
            int operacion = 0;
        %>

        <%
            try {
                // Se obtiene la conexion para DS de Contabilidad
                conexion = DaoFactory.getContabilidad();
                conexion.setAutoCommit(false);

                // Obtener el id del perfil de los usuarios
                idPerfil = Integer.parseInt(request.getParameter("clave"));
                // Operaciones a realizar: insertar, actualizar y eliminar
                operacion = Integer.parseInt(request.getParameter("operacion"));

                //Obtener los valores del formulario para el usuario actual
                pbUsuarios.setCURP(request.getParameter("TxtCURP").toUpperCase());
                pbUsuarios.setPaterno(request.getParameter("TxtPaterno").toUpperCase());
                pbUsuarios.setMaterno(request.getParameter("TxtMaterno").toUpperCase());
                pbUsuarios.setNombre(request.getParameter("TxtNombre").toUpperCase());
                pbUsuarios.setUsuario(request.getParameter("TxtUsuario"));
                pbUsuarios.setContrasena(request.getParameter("TxtContrasena"));

                switch (operacion) {
                    // Agregar usuario
                    case 1:
                        //Establecer el puesto del usuario y la plaza a donde pertenece
                        pbUsuarios.setIdPuesto(Integer.parseInt(request.getParameter("DLPuestos")));
                        pbUsuarios.setIdPlaza(Integer.parseInt(request.getParameter("DLPlazas")));

                        if (pbUsuarios.existeUsuario(conexion)) {
                            mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué dado de alta debido <br/> a que actualmente el nombre usuario <b>"
                                + pbUsuarios.getUsuario() + "</b> existe en el sistema.";

                        } else {
                            //Realiza operacion CRUD para crear un usuario nuevo
                            pbUsuarios.insertar(conexion, idPerfil);
                            mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> has sido dado de alta satisfactoriamente";
                        }
                        break;
                    // Modificar usuario
                    case 2:
                        // Establecer el numero del empleado en el cual se desean realizar cambios
                        pbUsuarios.setNumEmpleado(Integer.parseInt(request.getParameter("TxtNumEmpleado")));

                        //Establecer el puesto del usuario y la plaza a donde pertenece
                        pbUsuarios.setIdPuesto(Integer.parseInt(request.getParameter("DLPuestos")));
                        pbUsuarios.setIdPlaza(Integer.parseInt(request.getParameter("DLPlazas")));

                        if (pbUsuarios.existeUsuario(conexion)) {
                            mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué modificado debido <br/> a que actualmente el nombre de usuario <b>"
                                + pbUsuarios.getUsuario() + "</b> existe en el sistema.";
                        } else {


                            //Realiza operacion CRUD para actualizar un usuario
                            pbUsuarios.actualizar(conexion);
                            mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> ha sido modificado satisfactoriamente";
                        }
                        break;
                    // Eliminar usuario
                    case 3:
                        pbUsuarios.setNumEmpleado(Integer.parseInt(request.getParameter("TxtNumEmpleado")));

                        // Validaciones necesarias antes de poder realizar una operacion de borrado de un empleado
                        // en caso de que alguna de ellas se cumpla el empleado no podra ser borrado.
                        if (pbUsuarios.tienePolizas(conexion)) {
                            mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué eliminado debido a que tiene <b>Polizas</b> asociadas.";
                        } else {
                            if (pbUsuarios.tienePolizasCargaExcel(conexion)) {
                                mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué eliminado debido a que tiene <b>Polizas cargadas desde excel</b>.";
                            } else {
                                if (pbUsuarios.tieneDocumentosContables(conexion)) {
                                    mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué eliminado debido a que tiene <b>Documentos Contables</b> asociados.";
                                } else {
                                    if (pbUsuarios.tieneFirmasAutorizadas(conexion)) {
                                        mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> no fué eliminado debido a que es un <b>Firmantes Autorizador</b>.";
                                    } else {
                                        pbUsuarios.eliminar(conexion);
                                        mensaje = "El empleado <b>" + pbUsuarios.getNombreCompleto() + "</b> fué eliminado satisfactoriamentes.";
                                    }
                                }
                            }

                        }
                        break;
                }

                // En caso de que no exista ninguna exception se realiza la operacion en la base de datos
                conexion.commit();
            } catch (Exception E) {
                // Se realiza un rollback si surge algun problema al momento de intentar efecuar alguna de la 
                // operaciones anteriores, esta instruccion deshace cualquier cambio hecho en la bd.
                conexion.rollback();
                mensaje = "Ha ocurrido un error al accesar la Base de Datos,</p><p>favor de reportarlo al Administrador del Sistema.</p><p>Gracias.</p>";
            } finally {
                if (conexion != null) {
                    conexion.close();
                    conexion = null;
                }
            }

        %>

        <%util.tituloPagina(out, "Contabilidad", "Definición de usuarios", "Resultado", true);%>
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

        <form method="post" action="c762Resultado.jsp?clave=<%= idPerfil%>" id="formaRegresar">
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