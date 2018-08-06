<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="pbGrupos" class="sia.rf.seguridad.Grupos" scope="page"/>

<%!    //Seccion de declaraciones de variables
    Connection conexion = null;

%>

<%

    try {
        conexion = DaoFactory.getContabilidad();
        conexion.setAutoCommit(false);
        pbGrupos.obtenerSiguienteClave(conexion);
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
        <title>Agregar Grupo</title>
        <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>


        <script language="javascript" type="text/javascript">
            function regresar() {
                document.formaAgregarGrupo.action="c760Resultado.jsp";
                document.formaAgregarGrupo.submit();
                submited = true;
            }
            
            function blancos(componente) {
                for ( i = 0; i < componente.length; i++ ) {
                    if ( componente.charAt(i) != " " ){
                        return true;
                    }
                }
                return false;
            }

            function estaVacio(componente){
                return (  (componente == null) || (componente.lenght == 0)  || (componente == "") || ( blancos(componente) == false) )
            }
            
            function soloLetras(texto) {
                if ( /^[a-zA-Z áéíóúÁÉÍÓÚÑñ]+$/.test(texto) ){
                    return true;
                } 

                return false;
            }
    
            function validar(){
                var listaErrores="";
                var objeto="";

                var nombre= document.getElementById("TxtNombre").value;
                
                if( estaVacio(nombre) ) {
                    listaErrores += "   - Nombre del grupo:\t\tVacío " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtNombre";
                    }
                } else {
                    
                    if( !soloLetras(nombre) ) {
                        listaErrores += "   - Nombre del grupo:\t\tSolamente letras " + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtNombre";
                        }
                    } 
                }                    

                if( !estaVacio(listaErrores) ){
                    listaErrores="\nSe generarón los siguientes errores en la validación:\n" +
                        "---------------------------------------------------------------------\n\n" +
                        listaErrores +
                        "\n----------------------------------------------------------------------\n" +
                        "Por favor presiona aceptar e intenta nuevamente"; 
					 
                    alert(listaErrores);
		
                    (document.getElementById(objeto)).focus();
                    (document.getElementById(objeto)).select();
                    return false;

                } else {
                    return true;
                }
            }
      
            function irGuardar() {
                if( validar() ) {
                    document.formaAgregarGrupo.submit();
                    submited = true;
                }
            }
        </script>


    </head>

    <body>

        <form id="formaAgregarGrupo" name="formaAgregarGrupo"  action="c760Control.jsp?operacion=1&claveGrupo=<%= pbGrupos.getSiguiente()%>" method="post" >
            <%util.tituloPagina(out, "Contabilidad", "Agregar grupo de trabajo", "Modificar", true);%>
            <br/>
            <br/>
            <table align='center' class='general'  cellpadding="3"  border="0">
                <thead>

                </thead>
                <tbody>
                    <tr >
                        <th class="azulObsRt"  width="200">
                            Clave:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtClaveGrupo" id="TxtClaveGrupo"  value="<%= pbGrupos.getSiguiente()%>" size="50" readonly="true" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre:
                        </th>
                        <td>    
                            <input type="text" class="cajaTexto" name="TxtNombre" id="TxtNombre"  size="50" />
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
                        <td>
                            <input type='button' name='btnAceptar' value="Aceptar" class='boton' onclick="irGuardar();">
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