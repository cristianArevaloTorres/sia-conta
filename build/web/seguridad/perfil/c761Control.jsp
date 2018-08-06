<%-- 
    Document   : c761Control
    Created on : 13/08/2013, 12:30:56 PM
    Author     : EST.JOSE.FLORES
--%>

<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="java.util.logging.Level"%>
<%@ page import="java.util.logging.Logger"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sia.beans.seguridad.Autentifica"%>
<%@ page import="sia.rf.seguridad.Perfil"%>

<html>
<head>
    <meta http-equiv="Content-Language" content="es-mx">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title>c761Control.jsp</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
            <script language="JavaScript">
                function regresar(){ 
                    window.location.href="../perfil/c761Resultado.jsp?clave=<%=session.getAttribute("claveGrupo")%>"; 
                } 
            </script>
</head>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<body>
    <%util.tituloPagina(out, "Seguridad", " ","Control de Perfiles" , "Control", true);%>
<br><br><br>
<FORM Method="post" action="">
<%
    /*sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
    sbAutentifica.setPagina("c761Control");*/
    Connection conexion = null;
    int idPerfil        = 0;
    int registros       = 0;    
    conexion            = DaoFactory.getContabilidad();
    String tipo         = request.getParameter("operacion");
    String mensaje      = "";
    String descripcion  = "";
    Perfil perfil       = null;
    try{
        perfil          = new Perfil();
        idPerfil        = Integer.parseInt((String)request.getParameter("clavePerfil"));
        descripcion     = request.getParameter("txtNombrePerfil");
        switch(tipo.charAt(0)){
               case 'A'://(A)grega un nuevo perfil a la tabla de Perfiles (SG_TC_PERFIL)
                        registros   = perfil.insertar(conexion, idPerfil, descripcion, Integer.parseInt((String)session.getAttribute("claveGrupo")));
                        if( registros >= 1){
                            mensaje = "<b>El perfil se ha agregado correctamente.</b>";
                        }else if( registros == 0)
                            mensaje = "<b>No es posible insartar perfil debido a que ya existe.</b>";
               break;
               case 'M'://(M)odifica un registro de la tabla de Perfiles (SG_TC_PERFIL)
                        registros   = perfil.actualizar(conexion,idPerfil, descripcion );
                        if( registros >= 1){
                            mensaje = "<b>El perfil se ha actualizado correctamente.</b>";
                        }else if (descripcion.length()>=20)
                            mensaje = "<b>No se permite actualizar una descripcion mayor de 20 caracteres.</b>";
               break;
               case 'E'://(E)limina un perfil siempre y cuando no existan usuarios asociados a ese perfil
                        registros   = perfil.eliminar(conexion, idPerfil);
                        if( registros >= 1){
                            mensaje = "<b>El perfil fue eliminado correctamente.</b>";
                        } else if( registros == -1){
                            mensaje = "<b>No es posible eliminar el perfil debido a que tiene usuarios asociados.</b>";
                        } else if( registros == -2)
                            mensaje = "<b>No es posible eliminar el perfil debido a que tiene modulos asociados.</b>";
               break;
        }//End Switch TipoFormulario
    } catch(Exception E){
            conexion.rollback(); 
            //sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());
            //sbAutentifica.enviaCorreo();
            //System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
    } finally{
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
                <input type="button" class="boton" value="Regresar" onclick="regresar();" >
            </td>
        </tr>
    </table>
</FORM>
</body>
</html>