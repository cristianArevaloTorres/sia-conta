<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="pbGrupos" class="sia.rf.seguridad.Grupos" scope="page"/>

<%!    //Seccion de declaraciones de variables
    Connection conexion = null;
    int claveGrupo;
%>

<%
    claveGrupo = Integer.parseInt(request.getParameter("clave"));

    Connection conexion = null;
     
    try {
        conexion = DaoFactory.getContabilidad();
        pbGrupos.obtener(conexion, claveGrupo);
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
        if (conexion != null) {
            conexion.close();
            conexion = null;
        }
    }
%>

<html>
    <head>
        <title>Eliminar Grupo</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>


        <script language="javascript" type="text/javascript">
            function regresar() {
                document.formaEliminarGrupo.action="c760Resultado.jsp";
                document.formaEliminarGrupo.submit();
                submited = true;
            }
            
            function confirmar(){
                var nombreGrupo= document.getElementById("TxtNombre").value;
                var mensaje ="\nEsta seguro que desea eliminar el siguiente grupo:\n" +
                    "----------------------------------------------------------\n\n" +
                    "\t" + nombreGrupo +
                    "\n\n-----------------------------------------------------------\n" +
                    "Por favor seleccione una opción"; 


                if( !confirm(mensaje) ){
                    return false;
                } else {
                    return true;
                }
            }
      
            function irEliminar() {
                if( confirmar() ) {
                    document.formaEliminarGrupo.submit();
                    submited = true;
                }
            }
        </script>


    </head>

    <body>

        <%util.tituloPagina(out, "Contabilidad", "Eliminar grupo de trabajo", "Modificar", true);%>
        <form id="formaEliminarGrupo" name="formaEliminarGrupo"  action="c760Control.jsp?operacion=3&claveGrupo=<%= pbGrupos.getClaveGrupo()%>" method="post" >
            <br/>
            <br/>
            <table align='center' class='general'  cellpadding="3" >
                <thead>

                </thead>
                <tbody>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Clave:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtClaveGrupo" id="TxtClaveGrupo"  value="<%= pbGrupos.getClaveGrupo()%>" size="50" readonly="true" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre:
                        </th>
                        <td>    
                            <input type="text" class="cajaTexto" name="TxtNombre" id="TxtNombre"  size="50"  value="<%= pbGrupos.getDescripcion()%>" readonly="true"/>
                        </td>
                    </tr> 
                </tbody>
            </table>

            <br>
            <br>
            <hr class="piePagina">
            <br>

            <table align='center' cellpadding="3">
                <thead></thead>
                <tbody>
                    <tr>
                        <td>
                            <input type='button' name='btnAceptar' value="Eliminar" class='boton' onclick="irEliminar();">
                        </td>
                        <td>
                            <input type='button' name='btnCancelar' value="Regresar" class='boton' onclick="regresar();"  >
                        </td>
                    </tr>
                </tbody>
            </table>
        </form>


    </body>
</html>