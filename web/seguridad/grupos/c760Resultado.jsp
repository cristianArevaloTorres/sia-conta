<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbGrupos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pag" class="sia.libs.fun.tabla.PaginacionSE" scope="page"/>

<%
    // Extraer el contexto actual de la aplicacion.
    String contexto = request.getContextPath();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c760Resultado</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>
        <script language="javascript" type="text/javascript">
                // Funcion necesario para poder realizar la paginacion de los resultados del catalogo de grupos
            function llamamisma(noPaginaActual) {
                formaPaginacion.numPaginaActual.value = noPaginaActual;
                formaPaginacion.action="<%=contexto%>/seguridad/grupos/c760Resultado.jsp";
                formaPaginacion.submit();
            }
        </script>
    </head>
    <body>
        
        <%util.tituloPagina(out, "Contabilidad", "Definición de grupos de trabajo", "Resultado", true);%>

        <%
            try {
                // Se manda traer el total de grupos existente en el catalogo
                pbGrupos.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "Seguridad.select.Grupos-Resultado");

                // Se declaran los campos que se necesitan mostrar en el grid de resultados
                pag.addCampo(new Campo("cve_grupo", "Id Grupo", Campo.AL_CEN, null));
                pag.addCampo(new Campo("descripcion", "Descripción", Campo.AL_IZQ, null));
                pag.addCampo(new Campo("perfiles", "Perfiles", Campo.AL_CEN, null));
                // Se agregan los enlaces para las operaciones sobre cada unos de los grupos: Actualizas grupo, 
                // eliminar grupo y ver perfiles contenidos en grupo.
                pag.addImg(new Img("c760ModificarFormulario.jsp", Img.IMG_MODIFICAR, "", "", false, "Modificar grupo"));
                pag.addImg(new Img("c760ConsultarFormulario.jsp", Img.IMG_ELIMINAR, "", "", false, "Eliminar grupo"));
                pag.addImg(new Img(contexto + "/seguridad/perfil/c761Resultado.jsp", Img.IMG_SEG_CONSULTAR, "", "", false, "Consultar perfiles"));

                //Calcular el numero de pagina actual necesarios en el desplegado del grid del catalogo
                int noPaginaActual = request.getParameter("numPaginaActual") != null ? Integer.parseInt(request.getParameter("numPaginaActual")) : 0;
        %>



        <form action="" method="POST" id="formaPaginacion" name="formaPaginacion">
            <input type="hidden" name="numPaginaActual" id="numPaginaActual" value="<%=noPaginaActual%>">
        </form>  
        
        <table align="center" cellPadding="3">
            <tr class="resultado"> 
                <td> <div align="center">
                        <img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16">
                        <a href="c760AgregarFormulario.jsp">Agregar</a> 
                    </div>
                </td>
            </tr>     
        </table>

        <%
                // Grid que muestra el catagolo de grupos de usuarios
                pag.seleccionarPagina(pbGrupos,
                        out, 10,
                        noPaginaActual, contexto + "/",
                        "cve_grupo", null, 50, "Grupos");
            } catch (Exception ex) {
                ex.printStackTrace();
            } finally {
            }


        %>
        <br><br><br>
        <hr class="piePagina">
        <br>


    </body>
</html>