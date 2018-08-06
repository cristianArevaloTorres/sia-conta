<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="pbArbol" class="sia.rf.seguridad.Arbol" scope="page"/>
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
            int idGrupo;
            String perfil;
            String grupo;
            int operacion = 0;
            String opciones;
        %>

        <%

            idPerfil = Integer.parseInt(request.getParameter("idPerfil"));
            idGrupo = Integer.parseInt(request.getParameter("idGrupo"));
            perfil = request.getParameter("perfil");
            grupo = request.getParameter("grupo");
            
            try {
                // Se obtiene la conexion para DS de Contabilidad
                conexion = DaoFactory.getContabilidad();
                conexion.setAutoCommit(false);

                operacion = Integer.parseInt(request.getParameter("operacion"));
                opciones = request.getParameter("opciones");

                switch (operacion) {
                    // Agregar usuario
                    case 1:
                        opciones = !opciones.equals("") ? opciones.substring(0, opciones.length() - 1) : "";
                        
                        pbArbol.actualizarOpcionesArbolMenu(conexion, opciones, idPerfil);
                        mensaje = "El arbol del grupo: <b>" + grupo + "</b> y perfil: <b>" + perfil + "</b> fué correctamente modificado.";
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

        <%util.tituloPagina(out, "Contabilidad", "Definición de Árbol de Menú - Grupo: " + grupo + " / Perfil: " + perfil, "Resultado", true);%>
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

        <form method="post" action="../perfil/c761Resultado.jsp?clave=<%=idGrupo%>" id="formaRegresar">
            <table width="100%" align="center">
                <tr>
                    <td width="100%" align="center">
                        <input type="submit" name="btnAceptar" value="Regresar a perfiles de <%=grupo%>" class="boton" >
                    </td>
                </tr>
            </table>
        </form>

    </body>
</html>