<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbUsuarios" class="sia.rf.seguridad.Usuarios" scope="page"/>
<jsp:useBean id="pbPerfiles" class="sia.rf.seguridad.Perfil" scope="page"/>

<%!    //Seccion de declaraciones de variables
    int numEmpleado;
    int idPerfil;
    String contexto;
    String perfil;
    int idGrupo;
    String grupo;
%>

<%
    // Extraer el contexto actual de la aplicacion.
    contexto = request.getContextPath();
    numEmpleado = Integer.parseInt(request.getParameter("clave"));
    idPerfil = Integer.parseInt(request.getParameter("idPerfil"));

    // Se obtiene el nombre del perfil a que pertenece el usuario que se desea eliminar,
    // asi como el id y el nombre grupo respectivo a que pertenece el perfil actual en el
    // que se estan realizando cambios.
    pbPerfiles.obtenerPerfilGrupo(DaoFactory.getContabilidad(), idPerfil);
    perfil = pbPerfiles.getDescripcion();
    idGrupo = pbPerfiles.getIdGrupo();
    grupo = pbPerfiles.getGrupo();

    Connection conexion = null;

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c762Eliminar</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>

        <script language="javascript" type="text/javascript">
            // Funcion que efectua la operacion de regresar al listado de usuarios del perfil
            function regresar() {
                document.formaEliminarUsuario.action="c762Resultado.jsp";
                document.formaEliminarUsuario.submit();
                submited = true;
            }
            
            // Despliega la confirmacion de si realmente desea eliminar el usuario
            function confirmar(){
                var nombre = document.getElementById("TxtNombre").value;
                var paterno = document.getElementById("TxtPaterno").value;
                var materno = document.getElementById("TxtMaterno").value;
                
                
                var mensaje ="\n\u00BFEsta seguro que desea eliminar el siguiente usuario\u003F\n" +
                    "----------------------------------------------------------\n\n" +
                    "     " + nombre  + " " + paterno + " " + materno +
                    "\n\n-----------------------------------------------------------\n" +
                    "Por favor seleccione una opci\u00F3n"; 


                if( !confirm(mensaje) ){
                    return false;
                } else {
                    return true;
                }
            }
      
            // Si el usuario confirma como verdadera la operacion del eliminar esta funcion mandar llamar
            // a la opcion necesaria pasando como parametro el id del usuario que se desea borrar.
            function eliminar() {
                if( confirmar() ) {
                    document.formaEliminarUsuario.submit();
                    submited = true;
                }
            }
        </script>

    </head>
    <body>

        <%
            try {

                conexion = DaoFactory.getContabilidad();

                // Se obtiene de la bd la informacion del usuarios que se desea eliminar
                // para poder mostrarlo en la pantall
                pbUsuarios.obtener(conexion, numEmpleado);

        %>

        <form id="formaEliminarUsuario" name="formaEliminarUsuario" method="POST" action="c762Control.jsp?operacion=3">
            <%util.tituloPagina(out, "Contabilidad", "Eliminaci&oacute;n de usuario - Grupo: " + grupo + " / Perfil: " + perfil, "Eliminar", true);%>

            <br/>
            <br/>
            <table align='center' class='general' cellpadding="3">
                <thead>

                </thead>
                <tbody>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Número de Empleado:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtNumEmpleado" id="TxtNumEmpleado"  value="<%= pbUsuarios.getNumEmpleado()%>" size="40" readonly="true" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            CURP:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtCURP" id="TxtCURP"  value="<%= pbUsuarios.getCURP()%>" size="40" readonly="true" />
                        </td>
                    </tr>
                    <tr>
                        <th  class="azulObsRt"  width="200">
                            Apellido Paterno:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtPaterno" id="TxtPaterno"  value="<%= pbUsuarios.getPaterno()%>" size="40" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <th  class="azulObsRt"  width="200">
                            Apellido Materno:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtMaterno" id="TxtMaterno"  value="<%= pbUsuarios.getMaterno()%>" size="40" readonly="true"/>
                        </td>
                    </tr>

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtNombre" id="TxtNombre"  value="<%= pbUsuarios.getNombre()%>" size="40" readonly="true"/>
                        </td>
                    </tr>                    
                    <tr>
                        <th  class="azulObsRt"  width="200">
                            Puesto:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoPuesto(conexion, true).toString()%>
                    </tr>                    
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Unidad:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoUnidadesEjecutoras(conexion, true).toString()%>
                        </td>
                    </tr>                    


                    <tr>
                        <th class="azulObsRt"  width="200">
                            Entidad:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoEntidades(conexion, true).toString()%>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Ambito:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoAmbitos(conexion, true).toString()%>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Plaza:
                        </th>
                        <td id="TDPlazas">
                            <!--
                            <select id="DLPlazas" name="DLPlazas" class="cajaTexto">
                                <option value="0">Ninguna plaza para la Unidad Ejecutora, Entidad y Ambito</option>
                            </select>
                            -->

                            <%= pbUsuarios.obtenerCatalogoPlazas(conexion, pbUsuarios.getIdUnidad(), pbUsuarios.getIdEntidad(), pbUsuarios.getIdAmbito(), true).toString()%>
                        </td>
                    </tr>

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre de Usuario:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtUsuario" id="TxtUsuario"  value="<%= pbUsuarios.getUsuario()%>" size="40" readonly="true"/>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Contrasena:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtContrasena" id="TxtContrasena"  value="<%= pbUsuarios.getContrasena()%>" size="40" readonly="true"/>
                        </td>
                    </tr>     


                    <tr>
                        <th>
                        </th>
                        <td>
                            <input type="hidden" name="clave" id="clave" value="<%=idPerfil%>">
                        </td>
                    </tr>                      


                </tbody>
            </table>

            <br><br><br>
            <hr class="piePagina">
            <br>
            <table align='center' cellpadding="3">
                <thead></thead>
                <tbody>
                    <tr>
                        <td><input type="button" class="boton" value="Aceptar " onclick="eliminar()"></td>
                        <td><input type="button" class="boton" value="Regresar " onclick="regresar()" ></td>
                    </tr>
                </tbody>
            </table>
        </form>

        <%
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
                if (conexion != null) {
                    conexion.close();
                    conexion = null;
                }
            }

        %>

    </body>
</html>

