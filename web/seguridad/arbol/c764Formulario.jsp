<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.fun.tabla.*" %>

<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbPerfiles" class="sia.rf.seguridad.Perfil" scope="page"/>
<jsp:useBean id="pbArbol" class="sia.rf.seguridad.Arbol" scope="page"/>

<%! // Seccion de declaracion de variables
    int idPerfil;
    String perfil;
    int idGrupo;
    String grupo;
    Connection conexion = null;
%>

<%
    // Extraer el contexto actual de la aplicacion.  
    String contexto = request.getContextPath();
    // Se extrae el id del perfil al que perteneces los usuarios a desplegar
    idPerfil = Integer.parseInt(request.getParameter("clave"));


    conexion = DaoFactory.getContabilidad();

    pbPerfiles.obtenerPerfilGrupo(conexion, idPerfil);
    perfil = pbPerfiles.getDescripcion();
    idGrupo = pbPerfiles.getIdGrupo();
    grupo = pbPerfiles.getGrupo();
%>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
        <title>c762Resultado</title>
        <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>

        <style type="text/css">
            TR.cerrar {  
                display:none;
            }

            TR.abrir {  
                display:inline;
            }
        </style>


        <script language="javascript" type="text/javascript">
            function regresar() {
                document.formaRegresar.action="<%=contexto%>/seguridad/perfil/c761Resultado.jsp";
                document.formaRegresar.submit();
                submited = true;
            }
            
            
            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Esta funcion se asegura que el PADRE tienes AL MENOS 1 hijo seleccionado
             * @author: Juan Jose Galindo
             * @version: 26/08/2013/A
             */

            function tieneHijosSeleccionados(padre_id) {
                var hijos = document.getElementsByName(padre_id);
                var tiene_hijos_seleccionados= false;
                    
                if(hijos.length > 0) {
                    for(var indice=0; indice < hijos.length; indice++) {
                        hijo_id = hijos[indice].value;
                        if (hijos[indice].checked == true){ 
                            tiene_hijos_seleccionados= true;
                            break;
                        }
                    }
                } 
                
                return tiene_hijos_seleccionados;
            }


            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Funcion recursiva encargada de pintar los checkbox PADRES del checkbox HIJO 
             *           seleccionado por el usuario.
             * @author: Juan Jose Galindo
             * @version: 26/08/2013/A
             */
            function pintarPadre(hijo, seleccionar) {
                if(hijo != "") {
                    var objetos = document.forms[0].elements;
                
                    if(objetos.length > 0) {
                        var objeto;
                        for(var indice=0; indice < objetos.length; indice++) {
                            objeto = objetos[indice];
                            if (objeto.type == "checkbox" && objeto.value == hijo ) {
                                var tiene_hijos_seleccionados = tieneHijosSeleccionados(objeto.value);
                                if(tiene_hijos_seleccionados) {
                                    if(!objeto.checked){
                                        objeto.checked = true;
                                    }
                                } else {
                                    objeto.checked = seleccionar;
                                }
                                    
                                break;
                            } 
                        }
                        
                        pintarPadre(objeto.id, seleccionar);
                    } 
                } else {
                    return;
                }
            }

            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Funcion recursiva encargada de pintar los CheckBox HIJOS correspondientes al CheckBox 
             *           seleccionado por el usuario.
             * @author: Juan Jose Galindo
             * @version: 26/08/2013/A
             */
            function pintarHijos(padre_id, seleccionar) {
                var hijos = document.getElementsByName(padre_id);
                if(hijos.length > 0) {
                    for(var indice=0; indice < hijos.length; indice++) {
                        hijo_id = hijos[indice].value;
                        hijos[indice].checked = seleccionar;
                        pintarHijos(hijo_id, seleccionar);
                    }
                } else {
                    return;
                }
            }

            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Funcion que se encarga de SELECCIONAR/DESELECCIONAR todos los checkbox disponibles en la pantalla.
             *           
             * @author: Juan Jose Galindo
             * @version: 27/08/2013/A
             */

            function pintarTodos(valor) {
                var objetos = document.forms[0].elements;
                if(objetos.length > 0) {
                    for(var indice=0; indice < objetos.length; indice++) {
                        objeto = objetos[indice];
                        if (objeto.type == "checkbox") {
                            objeto.checked = valor;
                        } 
                    }
                } 
            }


            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Funcion que se encarga de mostrar/ocultar los elementos del nivel inferior inmediato de acuerdo a la posicion
             *           donde haya presionado el usuarios (icono del libro abierto/cerrado). 
             *           
             *           En el caso de que el elemento (hijo) sea una carpeta (icono libro) se preguntara si esta CERRADA, si se cumple 
             *           esta condicion SE BRINCA el elemento, es decir, no tendra ninguna afectacion es su estado y el de sus hijos.
             *           
             * @author: Juan Jose Galindo
             * @version: 28/08/2013/A
             */

            function mostrar(padre_id, clase, abrir_id, cerrar_id, nivel) {
                var hijos = document.getElementsByName(padre_id);
                if(hijos.length > 0) {
                    for(var indice=0; indice < hijos.length; indice++) {
                        var hijo_id = hijos[indice].name;

                        if(nivel != 1) {
                            var div_nombre = "diva" + padre_id.substring(2, padre_id.length);
                            var objeto_div = document.getElementById(div_nombre);
                            if( objeto_div.style.display == "none") {
                                continue;
                            }
                        }
                
                        hijos[indice].className= clase;
                        mostrar(hijo_id, clase, abrir_id, cerrar_id, nivel + 1);
                    }
                } else {
                    var objeto_div = document.getElementById(abrir_id);
                    objeto_div.style.display = "inline";

                    var objeto_div = document.getElementById(cerrar_id);
                    objeto_div.style.display = "none";

                    return;
                }
            }


            /**
             * Modulo: Seguridad
             * Submodulo: Arbol/Menu Perfiles
             * Objetivo: Recuperar los ID's de los checkbox seleccionados por el usuario con el fin 
             *           crear una unica cadena concatenada que posteriormente sera transmitidas mediante 
             *           POST al metodo que se encargara de realizar las modificaciones en el arbol en la BD.
             *           
             * @author: Juan Jose Galindo
             * @version: 28/08/2013/A
             */

            function obtenerOpcionesSeleccionadas() {
                var objetos = document.forms[0].elements;
                var opciones = "";
                
                if(objetos.length > 0) {
                    for(var indice=0; indice < objetos.length; indice++) {
                        objeto = objetos[indice];
                        if (objeto.type == "checkbox" && objeto.checked == true) {
                            opcion = objeto.value;
                            opciones += opcion + ", ";
                        } 
                    }
                } 
                
                return opciones;
            }
            
            
            function confirmar(grupo, perfil){
                var mensaje ="\n\u00BF Estas seguro que deseas eliminar todas las opciones de \u003F\n" +
                    "-------------------------------------------------------------\n\n" +
                    "   Grupo: " + grupo + "  /  Perfil: " + perfil +
                    "\n\n-------------------------------------------------------------\n" +
                    "Por favor selecciona una opci\u00F3n."; 

                if( !confirm(mensaje) ){
                    return false;
                } else {
                    return true;
                }
            }


            function guardar(grupo, perfil) {
                var opciones = obtenerOpcionesSeleccionadas();
                var hacerEnvio = true;
                
                if( opciones == "" ) {
                    hacerEnvio = confirmar(grupo, perfil);
                } 
                
                if(hacerEnvio) {
                    document.ArbolMenuOpciones.action += "&opciones=" + opciones;
                    document.ArbolMenuOpciones.submit();
                    submited = true;
                }
            } 
            
         
        </script>
    </head>
    <body>

        <%util.tituloPagina(out, "Contabilidad", "Definición de Árbol de Menú - Grupo: " + grupo + " / Perfil: " + perfil, "Resultado", true);%>

        <br/>
        <br/>
        <table border='0' align="center" >
            <tr > 
                <td> 
                    <img src="../../Librerias/Imagenes/botonCorrectoDesac.gif">
                    <a onclick="pintarTodos(true)">Elegir a todos</a>
                </td>
                <td> 
                    &nbsp;&nbsp;&nbsp;
                </td>
                <td>
                    <img src="../../Librerias/Imagenes/botonEliminar2.gif">
                    <a onclick="pintarTodos(false)">Quitar a todos</a>
                </td>
            </tr>     
        </table>
        <hr noshade="noshade" width="60%" align="center" style="border-width: 1px;border-style:solid">
        <br/>
        <form id="ArbolMenuOpciones" name="ArbolMenuOpciones" method="POST" action="c764Control.jsp?operacion=1&idPerfil=<%=idPerfil%>&perfil=<%=perfil%>&idGrupo=<%=idGrupo%>&grupo=<%=grupo%>">
            <%= pbArbol.obtenerArbol(conexion, idPerfil).toString()%>       
        </form>


        <form action="" method="POST" id="formaRegresar" name="formaRegresar">
            <input type="hidden" name="clave" id="clave" value="<%=idGrupo%>">
        </form>  


        <br><br>
        <hr class="piePagina">
        <br>
        <table align='center' cellpadding="3">
            <thead></thead>
            <tbody>
                <tr>
                    <td><input type="button" class="boton" value="Aceptar" onclick="guardar('<%=grupo%>', '<%=perfil%>')"></td>
                    <td><input type="button" class="boton" value="Regresar a perfiles" onclick="regresar()"></td>
                </tr>
            </tbody>
        </table>

        <br/><br/>
        <br/><br/>
    </body>
</html>