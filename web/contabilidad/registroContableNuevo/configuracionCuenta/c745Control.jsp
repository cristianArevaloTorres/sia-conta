<%-- 
    Document   : c745Control
    Created on : 23/07/2013, 02:27:53 PM
    Author     : EST.JOSE.FLORES
--%>
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="java.util.logging.Level"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.configuracionCuenta.Genero"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.configuracionCuenta.Grupo"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.configuracionCuenta.Clase"%>

<html>
<head>
    <meta http-equiv="Content-Language" content="es-mx">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>Abrir Cierre Definitivo</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
</head>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<body onLoad="aplicaValidaciones();" >
    <%util.tituloPagina(out, "Contabilidad", " ","Catálogos Género, Grupo y Rubro" , "Control", true);%>
<br><br><br>

<FORM Method="post" action="c745Filtro.jsp">
<%
 //sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
 Connection conexion=null;
 //sbAutentifica.setPagina("c745Control");
 String mensaje      = ""; 
try{
    //Varible que obtiene la conexion a la base de datos de contabilidad
    conexion            = DaoFactory.getContabilidad();
    //Variable que obtiene el catalogo seleccionado por el usuario (Genero, Grupo o Clase)
    String operacion    = ((String)request.getSession().getAttribute("tipo")).trim();
    //Variable que almacena el tipo de operacion (Altas, Bajas o Cambios)
    String tipo         = ((String)request.getSession().getAttribute("operacion")).trim();
    
    char tipos = 'x';
    if(tipo != null && tipo.trim().equals("grupo") ){
        tipos = 'u';
    } else if(tipo != null)
              tipos = tipo.charAt(0);
    
    int registros       = 0;
    int idGenero        = 0;
    int idGrupo         = 0; 
    int idClase         = 0;
    Genero genero       = null;
    Grupo  grupo        = null;
    Clase  clase        = null;
    String descripcion  = ""; 
         switch(tipos){
            case 'g'://El usuario ha seleccionado genero y los operaciones seran apliacadas a la tabla RF_TC_GENERO_CLASF_CTA
                    genero          = new Genero();
                    idGenero        = Integer.valueOf(request.getParameter("lstGenero")); 
                    switch(operacion.charAt(0)){
                        case 'A'://(A)gregar
                             descripcion= request.getParameter("txtGenero");
                             //Inserta un registro a la tabla
                             registros  = genero.insertar(conexion,idGenero,descripcion);
                             if( registros >= 1){
                                mensaje = "<b>El género se ha agregado correctamente</b>";
                             }else if( registros == 0)
                                mensaje = "<b>No es posible agregar el género debido a que ya existe.</b>";
                        break;
                        case 'M'://(M)odfica un registro de la tabla de catalogo de Genero 
                             descripcion= request.getParameter("txtGenero");
                             registros  = genero.actualizar(conexion,idGenero,descripcion );
                             if( registros >= 1){
                                mensaje = "<b>El género fue actualizado correctamente.</b>";
                             }else
                                mensaje = "<b>No se pudo realizar la actualización.</b>";
                        break;
                        case 'E'://(E)limina un registro de la tabla de genero
                             registros  = genero.eliminar(conexion,idGenero);
                             if( registros >= 1){
                                mensaje = "<b>El género fue eliminado correctamente.</b>";
                             }else if( registros == -1)
                                mensaje = "<b>No es posible eliminar el género seleccionado, debido a que tiene grupos asociados</b>";
                        break;
                    }//End Switch operacion.charAt(0)
            break;
            case 'u' ://El usuario ha seleccionado grupo y las operaciones seran apliacadas a la tabla RF_TC_GRUPO_CLASF_CTA
                    grupo           = new Grupo();
                    char operaciones= 'x';
                    operaciones     = operacion.charAt(0);
                    idGenero        = Integer.valueOf(request.getParameter("lstGenero2"));   
                    idGrupo         = Integer.valueOf(request.getParameter("lstGrupo2"));
                    switch(operaciones){
                        case 'A' ://(A)gregar 
                             descripcion    = request.getParameter("txtGrupo");
                             //Inserta un registro a la tabla de Grupos
                             registros      = grupo.insertar(conexion,idGrupo,idGenero,descripcion);
                             if( registros >= 1){
                                mensaje = "<b>El grupo se ha agregado correctamente.</b>";
                             }else if( registros == 0)
                                mensaje = "<b>No es posible agregar el grupo debido a que ya existe.</b>";
                        break;
                        case 'M' ://(M)odificar 
                             descripcion = request.getParameter("txtGrupo");
                             //Actualiza un registro de la tabla de Grupo
                             registros   = grupo.actualizar(conexion,idGrupo,idGenero,descripcion );
                             if( registros >= 1){
                                mensaje = "<b>El grupo fue actualizado correctamente.</b>";
                             }else
                                mensaje = "<b>No se pudo realizar la actualización.</b>";
                        break;
                        case 'E' ://(E)limina un registro de la tabla de grupos
                             registros   = grupo.eliminar(conexion,idGrupo,idGenero);
                             if( registros >= 1){
                                mensaje = "<b>El grupo fue eliminado correctamente.</b>";
                             }else if( registros == -1) {
                                mensaje = "<b>No es posible eliminar un grupo seleccionado, debido a que tienen rubros asociados.</b>";
                             }else if(registros == 0) 
                                mensaje = "<b>No se puede eliminar un grupo que no existe.</b>";
                        break;
                    }//End Switch TipoFormulario
            break;
            case 'c' ://El usuario ha seleccionado la clase y las operaciones seran apliacadas a la tabla RF_TC_CLASE_CLASIF_CTA
                    clase           = new Clase();
                    idGenero        = Integer.valueOf(request.getParameter("lstGenero3"));   
                    idGrupo         = Integer.valueOf(request.getParameter("lstGrupo3"));
                    idClase         = Integer.valueOf(request.getParameter("lstClase"));
                    switch(operacion.charAt(0)){
                        case 'A'://(A)gregar 
                             descripcion    = request.getParameter("txtClase");
                             //Inserta un registro a la tabla 
                             registros      = clase.insertar(conexion,idClase,idGrupo,idGenero,descripcion);
                             if( registros >= 1){
                                mensaje = "<b>El rubro se ha agregado correctamente.</b>";
                             }else if( registros == 0)
                                mensaje = "<b>No es posible agregar rubro debido a que ya existe.</b>";
                        break;
                        case 'M'://(M)odificar  
                             descripcion    = request.getParameter("txtClase");
                             //Actualiza un registro de la tabla 
                             registros      = clase.actualizar(conexion,idClase,idGrupo,idGenero,descripcion );
                             if( registros >= 1){
                                mensaje = "<b>El rubro se ha actualizado correctamente.</b>";
                             }else
                                mensaje = "<b>No se pudo realizar la actualización del rubro.</b>";
                        break;
                        case 'E'://(E)limina un registro de la tabla
                             registros      = clase.eliminar(conexion,idClase,idGrupo,idGenero);
                             if( registros >= 1){
                                mensaje = "<b>El rubro fue eliminado correctamente.</b>";
                             }else if( registros == -1){
                                mensaje = "<b>No es posible eliminar un rubro que no existe.</b>";
                             }else if( registros == 0)
                                mensaje = "<b>No es posible eliminar el rubro debido a que tiene cuentas de mayor asociadas.</b>";
                        break;
                    }//End Switch TipoFormulario
            break;
        }//Fin switch externo*/
    } catch(Exception E){
            conexion.rollback(); 
            /*sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
            sbAutentifica.enviaCorreo();
            System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); */
%>   <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
    } finally {
        if (conexion != null){
            conexion.close();
            conexion=null;
        }  
    }
%>
    <br><br>
       <p><font color='#003399'><%= mensaje%></font></p>  
    <table width="100%">
        <tr><td width="73%">&nbsp;</td>
            <td width="10%">
                <input type="submit" class="boton" value="Regresar" name="btnAceptar">
            </td>
        </tr>
    </table>
</FORM>
</body>
</html>