<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbUsuarios" class="sia.rf.seguridad.Usuarios" scope="page"/>
<jsp:useBean id="pbPerfiles" class="sia.rf.seguridad.Perfil" scope="page"/>

<%!//Seccion de declaraciones de variables
    int numEmpleado;
    int idPerfil;
    String contexto;
    String perfil;
    int idGrupo;
    String grupo;
%>

<%
    contexto = request.getContextPath();

    // Se extrae el id del perfil al pertenecera el el usuario que se esta dando de altas
    idPerfil = Integer.parseInt(request.getParameter("idPerfil"));

    // Obtiene el nombre del perfil y grupo para poder desplegarlo en el encabezado
    // de la pagina 
    pbPerfiles.obtenerPerfilGrupo(DaoFactory.getContabilidad(), idPerfil);
    perfil = pbPerfiles.getDescripcion();
    idGrupo = pbPerfiles.getIdGrupo();
    grupo = pbPerfiles.getGrupo();

    Connection conexion = null;

%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c762AgregarFormulario</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>

        <script language="javascript" type="text/javascript">
            // Regresa al grid de resultado de los usuario del perfil
            function regresar() {
                document.formaAgregarUsuario.action="c762Resultado.jsp";
                document.formaAgregarUsuario.submit();
                submited = true;
            }

            /**
             * Funcion auxiliar que ayuda a la funcion estaVacio() a deterpara determinar si algun componente 
             *      del formulario esta vacio.
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */

            function blancos(componente) {
                for ( i = 0; i < componente.length; i++ ) {
                    if ( componente.charAt(i) != " " ){
                        return true;
                    }
                }
                return false;
            }

            /**
             * Funcion para determinar si algun componente del formulario esta vacio.
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */
            function estaVacio(componente){
                return (  (componente == null) || (componente.lenght == 0)  || (componente == "") )
                //return (  (componente == null) || (componente.lenght == 0)  || (componente == "") || ( blancos(componente) == false) )
            }

            /**
             * Funcion para saber si lo que el usuario tecleo en el campo de texto corresponde solamente a letras con o sin acento.
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */
            function soloLetras(texto) {
                if ( /^[a-zA-Z áéíóúÁÉÍÓÚÑñ]+$/.test(texto) ){
                    return true;
                } 

                return false;
            }

            /**
             * Funcion para saber si lo que el usuario tecleo corresponde solamente a letras con o sin acento
             *      y ademas los digitos numericos del 0 al 9
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */
            function soloLetrasNumeros(texto) {
                if ( /^[0-9a-zA-Z. ]+$/.test(texto) ){
                    return true;
                } 

                return false;
            }
            
            function soloLetrasPunto(texto) {
                if ( /^[a-zA-Z. áéíóúÁÉÍÓÚÑñ]+$/.test(texto) ){
                    return true;
                } 
                return false;
            }
            
            function tieneLongitudMenor(menor, objeto) {
                var longitud = objeto.length;
                
                if(longitud < menor)  {
                    return true;
                }
        
                return false;
            }
            
            function tieneLongitudMayor(mayor, objeto) {
                var longitud = objeto.length;
                
                if(longitud > mayor) {
                    return true;
                }
                    
                return false;
            }



            /**
             * Funcion que permite la validacion de la informacion capturada en el formulario para la opciones
             *      de agregar y modificar un usuario del modulo de seguridad
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */
            function validarFormularioUsuario(){
                var listaErrores="";
                var objeto="";
    
                // Obtiene los valores de cada uno de los elementos que pertecen al formulario
                var curp= document.getElementById("TxtCURP").value;
                var paterno= document.getElementById("TxtPaterno").value;
                var materno= document.getElementById("TxtMaterno").value;
                var nombre= document.getElementById("TxtNombre").value;
                var puesto= document.getElementById("DLPuestos").value;
                var plaza= document.getElementById("DLPlazas").value;
                var usuario= document.getElementById("TxtUsuario").value;
                var contrasena= document.getElementById("TxtContrasena").value;
                
                if( estaVacio(curp) ) {
                    listaErrores += "     -  CURP:   Vac\u00edo " + "\n";
            
                    if( estaVacio(objeto) ) {
                        objeto= "TxtCURP";
                    }
                } else {
                    if(tieneLongitudMayor(18, curp)) {
                        listaErrores += "     -  CURP:   No mayor de 18 digitos. " + "\n";
                        
                        if( estaVacio(objeto) ) {
                            objeto= "TxtCURP";
                        }
                    } else {
                        
                        if(tieneLongitudMenor(18, curp)) {
                            listaErrores += "     -  CURP:   No menor de 18 digitos. " + "\n";
                        
                            if( estaVacio(objeto) ) {
                                objeto= "TxtCURP";
                            }
                        } else {
                        
                            if( !soloLetrasNumeros(curp) ) {
                                listaErrores += "     -  CURP:   Solamente letras y numeros. " + "\n";
                
                                if( estaVacio(objeto) ) {
                                    objeto= "TxtCURP";
                                }
                            } 
                        }     
                    }     
                }
                               
                
                if( estaVacio(paterno) ) {
                    listaErrores += "     -  Apellido Paterno:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtPaterno";
                    }
                } else {
                    if( !soloLetras(paterno) ) {
                        listaErrores += "     -  Apellido Paterno:   Solamente letras " + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtPaterno";
                        }
                    } 
                }                    
                
                if( estaVacio(materno) ) {
                    listaErrores += "     -  Apellido Materno:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtMaterno";
                    }
                } else {
                    if( !soloLetras(materno) ) {
                        listaErrores += "     -  Apellido Materno:   Solamente letras " + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtMaterno";
                        }
                    } 
                }                    
                
                
                if( estaVacio(nombre) ) {
                    listaErrores += "     -  Nombre:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtNombre";
                    }
                } else {
                    if( !soloLetrasPunto(nombre) ) {
                        listaErrores += "     -  Nombre:   Solamente letras " + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtNombre";
                        }
                    } 
                }  

                if( estaVacio(puesto) ) {
                    listaErrores += "     -  Puesto:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "DLPuestos";
                    }
                }

                if( estaVacio(plaza) || plaza == 0) {
                    listaErrores += "     -  Plaza:  Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "DLPlazas";
                    }
                }
                
                if( estaVacio(usuario) ) {
                    listaErrores += "     -  Nombre de Usuario:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtUsuario";
                    }
                } else {
                    if( !soloLetras(usuario) ) {
                        listaErrores += "     -  Nombre de usuario:   Solamente letras y punto" + "\n";
		
                        if( estaVacio(objeto) ) {
                            objeto= "TxtUsuario";
                        }
                    } 
                }                  
                
                if( estaVacio(contrasena) ) {
                    listaErrores += "     - Contrase\u00f1a:   Vac\u00edo " + "\n";
       
                    if( estaVacio(objeto) ) {
                        objeto= "TxtContrasena";
                    }
                }
              
                
                if( !estaVacio(listaErrores) ){
                    listaErrores="\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                        "-------------------------------------------------------------------\n\n" +
                        listaErrores +
                        "\n--------------------------------------------------------------------\n" +
                        "Por favor presiona aceptar e intenta nuevamente."; 
					 
                    alert(listaErrores);
		
                    (document.getElementById(objeto)).focus();
                    (document.getElementById(objeto)).select();
                    return false;

                } else {
                    return true;
                }
            }
      

            /// Ajax
            /**
             * Funcion que permite la validacion de la informacion capturada en el formulario para la opciones
             *      de agregar y modificar un usuario del modulo de seguridad
             * @author: Juan Jose Galindo
             * @version: 21/08/2013/A
             */

            var xmlHttp
            function mostrarPlazas(){
                xmlHttp = new XMLHttpRequest();
                if (xmlHttp == null){
                    alert ("Tu navegador no soporta AJAX!");
                    return;
                }
	
                hoy = new Date();
                id_refrescar = Math.abs(Math.sin(hoy.getTime()));
    
                var id_unidad;
                var id_entidad;
                var id_ambito;

                id_unidad= document.getElementById("DLUnidades").value;    
                id_entidad = document.getElementById("DLEntidades").value;
                id_ambito = document.getElementById("DLAmbitos").value;

                var url = "c762ActualizarListaPlazas.jsp";
                url += "?idUnidad=" + id_unidad;
                url += "&idEntidad=" + id_entidad;
                url += "&idAmbito=" + id_ambito;
                url += "&id=" + id_refrescar;
                
                //alert("URL:" + url);
    
                xmlHttp.onreadystatechange = mostrarPlazasResultado;
                xmlHttp.open("GET", url, true);
                xmlHttp.send(null);
            }

            function mostrarPlazasResultado(){
                if (xmlHttp.readyState == 4){
                    document.getElementById("TDPlazas").innerHTML = xmlHttp.responseText;
                }
            }
            
            //Fin Ajax

            // Funcion que manda llamar a la operacion de insertar en la bd un nuevo usuario
            function guardar() {
                if( validarFormularioUsuario() ) {
                    document.formaAgregarUsuario.submit();
                    submited = true;
                }
            }           
        </script>

    </head>
    <body>

        <%
            try {
                conexion = DaoFactory.getContabilidad();
        %>

        <form id="formaAgregarUsuario" name="formaAgregarUsuario" method="POST" action="c762Control.jsp?operacion=1">
            <%util.tituloPagina(out, "Contabilidad", "Agregar de usuario - Grupo: " + grupo + " / Perfil: " + perfil, "Agregar", true);%>

            <br/>
            <br/>
            <table align='center' class='general' cellpadding="3">
                <thead>

                </thead>
                <tbody>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            CURP:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtCURP" id="TxtCURP" size="40"  maxlength="18" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Apellido Paterno:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtPaterno" id="TxtPaterno"  size="40"/>
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Apellido Materno:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtMaterno" id="TxtMaterno"  size="40"/>
                        </td>
                    </tr>

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtNombre" id="TxtNombre" size="40"/>
                        </td>
                    </tr>                    
                    <tr>
                        <th class="azulObsRt"  width="200">
                            Puesto:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoPuesto(conexion, false).toString()%>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Unidad:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoUnidadesEjecutoras(conexion, false).toString()%>
                        </td>
                    </tr>                    


                    <tr>
                        <th class="azulObsRt"  width="200">
                            Entidad:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoEntidades(conexion, false).toString()%>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Ambito:
                        </th>
                        <td>
                            <%= pbUsuarios.obtenerCatalogoAmbitos(conexion, false).toString()%>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Plaza:
                        </th>
                        <td id="TDPlazas">
                            <!-- 
                            <select id="DLPlazas" name="DLPlazas">
                            </select>
                            -->

                            <%= pbUsuarios.obtenerCatalogoPlazas(conexion, "000", 0, "0", false).toString()%>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Nombre de Usuario:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtUsuario" id="TxtUsuario"  size="40"/>
                        </td>
                    </tr>                    

                    <tr>
                        <th class="azulObsRt"  width="200">
                            Contrase&ntilde;a:
                        </th>
                        <td>
                            <input type="text" class="cajaTexto" name="TxtContrasena" id="TxtContrasena"  size="40"/>
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
                <tbody>
                    <tr>
                        <td><input type="button" class="boton" value="Aceptar" onclick="guardar();"></td>
                        <td><input type="button" class="boton" value="Regresar" onclick="regresar();"></td>
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

