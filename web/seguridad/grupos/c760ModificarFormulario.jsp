<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbGrupos" class="sia.rf.seguridad.Grupos" scope="page"/>

<%!   //Seccion de declaraciones de variables 
    int claveGrupo;
    String contexto;
%>

<%
    // Extraer el contexto actual de la aplicacion.
    contexto = request.getContextPath();
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
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c760Modificar</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>

        <script language="javascript" type="text/javascript">
            function regresar() {
                document.formaModificarGrupo.action="c760Resultado.jsp";
                document.formaModificarGrupo.submit();
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

                var nombreNuevo= document.getElementById("TxtNombre").value;
                
                //alert("Descripcion: " + nombreNuevo);
                if( estaVacio(nombreNuevo) ) {
                    listaErrores += "   - Nombre del grupo:\tVacío " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtNombre";
                    }
                } else {
                    
                    if( !soloLetras(nombreNuevo) ) {
                        listaErrores += "   - Nombre del grupo:\tSolamente letras " + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtNombre";
                        }
                    } 
                }                    


                //alert("Errores: " + listaErrores);
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
                    document.formaModificarGrupo.submit();
                    submited = true;
                }
            }
        </script>

    </head>
    <body>
        <form id="formaModificarGrupo" name="formaModificarGrupo" method="POST" action="c760Control.jsp?operacion=2&claveGrupo=<%= pbGrupos.getClaveGrupo()%>">
            <%util.tituloPagina(out, "Contabilidad", "Modificaci&oacute;n de grupo de trabajo", "Modificar", true);%>

            <br/>
            <br/>
            <table align='center' class='general' cellpadding="3">
                <thead>

                </thead>
                <tbody>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Clave:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtClaveGrupo" id="TxtClaveGrupo"  value="<%= pbGrupos.getClaveGrupo()%>" size="40" readonly="true" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre anterior:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtNombreAnterior" id="TxtNombreAnterior"  value="<%= pbGrupos.getDescripcion()%>" size="40" readonly="true"/>
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre nuevo:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtNombre" id="TxtNombre"  value="<%= pbGrupos.getDescripcion()%>" size="40"/>
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
                        <td><input type="button" class="boton" value="Aceptar" onclick="irGuardar()"></td>
                        <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
                    </tr>
                </tbody>
            </table>
        </form>
    </body>
</html>