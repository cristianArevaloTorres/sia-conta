<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbUsuarios" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>
<jsp:useBean id="pbPerfiles" class="sia.rf.seguridad.Perfil" scope="page"/>

<%! // Seccion de declaracion de variables
    int idPerfil;
    String perfil;
    int idGrupo;
    String grupo;
%>

<%
    // Extraer el contexto actual de la aplicacion.  
    String contexto = request.getContextPath();
    // Se extrae el id del perfil al que perteneces los usuarios a desplegar
    idPerfil = Integer.parseInt(request.getParameter("clave"));

    // Se obtiene el nombre del perfil a que pertenece el usuario que se desea eliminar,
    // asi como el id y el nombre grupo respectivo a que pertenece el perfil actual en el
    // que se estan realizando cambios.
    pbPerfiles.obtenerPerfilGrupo(DaoFactory.getContabilidad(), idPerfil);
    perfil = pbPerfiles.getDescripcion();
    idGrupo = pbPerfiles.getIdGrupo();
    grupo = pbPerfiles.getGrupo();

%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c762Resultado</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>
        <script language="javascript" type="text/javascript">
            // Funcion necesario para poder realizar la paginacion de los resultados del perfil actual
            function llamamisma(noPaginaActual) {
                formaPaginacion.numPaginaActual.value = noPaginaActual;
                formaPaginacion.action="<%=contexto%>/seguridad/usuarios/c762Resultado.jsp";
                formaPaginacion.submit();
            }
            
            function regresar() {
                document.formaRegresarPerfiles.action="<%=contexto%>/seguridad/perfil/c761Resultado.jsp";
                document.formaRegresarPerfiles.submit();
                submited = true;
            }
            
        </script>
    </head>
    <body>

        <%util.tituloPagina(out, "Contabilidad", "Definición de usuarios - Grupo: " + grupo + " / Perfil: " + perfil, "Resultado", true);%>

        <%
            try {

                pbUsuarios.addParam("idPerfil", idPerfil);
                pbUsuarios.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "Seguridad.select.Usuarios-Resultado");

                // Se declaran los campos que se necesitan mostrar en el grid de resultados
                pag.addCampo(new Campo("num_empleado", "Id Usuario", Campo.AL_CEN, null));
                pag.addCampo(new Campo("usuario", "Usuario", Campo.AL_DER, null));
                pag.addCampo(new Campo("curp", "CURP", Campo.AL_CEN, null));
                pag.addCampo(new Campo("nombre", "Nombre", Campo.AL_IZQ, null));
                // Se agregan los enlaces para las operaciones sobre cada unos de los grupos: Actualizar usuario, 
                // eliminar usuario
                pag.addImg(new Img("c762ModificarFormulario.jsp", Img.IMG_MODIFICAR, "idPerfil=" + idPerfil, "", false, "Modificar usuario"));
                pag.addImg(new Img("c762ConsultarFormulario.jsp", Img.IMG_ELIMINAR, "idPerfil=" + idPerfil, "", false, "Eliminar usuario"));

                //Calcular el numero de pagina actual necesarios en el desplegado del grid del catalogo
                int noPaginaActual = request.getParameter("numPaginaActual") != null ? Integer.parseInt(request.getParameter("numPaginaActual")) : 0;
        %>



        <form action="" method="POST" id="formaPaginacion" name="formaPaginacion">
            <input type="hidden" name="numPaginaActual" id="numPaginaActual" value="<%=noPaginaActual%>">
            <input type="hidden" name="clave" id="clave" value="<%=idPerfil%>">
        </form>  

        <table align="center" cellPadding="3">
            <tr class="resultado"> 
                <td> <div align="center">
                        <img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16">
                        <a href="c762AgregarFormulario.jsp?idPerfil=<%=idPerfil%>">Agregar</a> 
                    </div>
                </td>
            </tr>     
        </table>

        <%
                // Grid que muestra el listado paginado de los usuarios del perfil
                pag.seleccionarPagina(pbUsuarios,
                    out, 10,
                    noPaginaActual, contexto + "/",
                    "num_empleado", null, 50, "Usuarios");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }


        %>
        <br><br><br>
        <hr class="piePagina">
        <br>

        <table align='center' border="0">
            <tbody>
                <tr>
                    <td><input type="button" class="boton" value="Regresar a perfil" onclick="regresar()"></td>
                </tr>
            </tbody>
        </table>

        <form action="" method="POST" id="formaRegresarPerfiles" name="formaRegresarPerfiles">
            <input type="hidden" name="clave" id="clave" value="<%=idGrupo%>">
        </form>  


    </body>
</html>