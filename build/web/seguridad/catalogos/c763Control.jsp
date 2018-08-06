<%@page import="java.util.logging.Level"%>
<%@page import="java.util.logging.Logger"%>
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<%@ page  import="sia.rf.seguridad.catalogos.Unidades"%>
<%@ page  import="sia.rf.seguridad.catalogos.Puestos"%>


<html>
<head>
    <meta http-equiv="Content-Language" content="es-mx">
    <meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
    <title></title>
     <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
</head>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<body onLoad="" >
    <%util.tituloPagina(out, "Contabilidad", "Administración de Catálogos ","", "Formulario", true);%>
    
<br><br>

<br>
<FORM Method="post" action="c763Filtro.jsp">

<%
  Connection conexion=null;
try{
    conexion=DaoFactory.getContabilidad();
    String catalogos = request.getParameter("catalogo");
    String operacion = request.getParameter("operacion");
    
    char catalogo='x';    
    catalogo = catalogos.trim().charAt(0);
        int registros           = 0;
    String unidad_ejecutora = "";
    int ambito              = 0;
    String descripcion      = "";
    String desc_puesto      = "";
    Unidades unidades       = null;
    Puestos puestos         = null;
    int id_puesto              = 0;
   
   
         switch(catalogo){
             case 'u'://UNIDADES
                unidades = new Unidades();
                unidad_ejecutora = request.getParameter("txtClave");
                 switch(operacion.trim().charAt(0)){
                    case 'A'://(A)gregar. Inserta un registro a la tabla de Unidades Ejecutoras
                             descripcion    = request.getParameter("txtDesc");
                             ambito         = Integer.valueOf(request.getParameter("lstAmbito"));
                             
                             registros = unidades.insertarUnidad(conexion,unidad_ejecutora,descripcion,ambito);
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'>La Unidad Ejecutora se ha agregado correctamente</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible agregar la Unidad Ejecutora debido a que ya existe.</font></b>
                             <%
                             }
                        break;
                         case 'M'://(M)odfica un registro de la tabla de Unidades Ejecutoras 
                             unidad_ejecutora = request.getParameter("lstUnidadEjecutora");
                             descripcion = request.getParameter("txtDescripcion");
                             ambito      = Integer.valueOf(request.getParameter("lstAmbito"));
                             registros   = unidades.actualizarUnidad(conexion,unidad_ejecutora,descripcion,ambito );
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'> Se  ha actualizado correctamente</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible realizar la actualización.</font></b>
                             <%
                             }
                        break;
                        case 'E' ://(E)limina un registro de la tabla de Unidad Ejecutora
                             unidad_ejecutora = request.getParameter("lstUnidadEjecutora");
                             registros      = unidades.eliminarUnidad(conexion, unidad_ejecutora);
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'>La unidad ejecutora fue eliminada correctamente</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible eliminar la Unidad Ejecutora.</font></b>
                             <%
                          }
                        break;
                        
                    }//Fin switch externo*/
              break;
                case 'p'://PUESTOS
                puestos = new Puestos();
                    switch(operacion.trim().charAt(0)){
                    case 'A'://(A)gregar. Inserta un registro a la tabla de Puestos
                             desc_puesto    = request.getParameter("txtDesc_puesto");
                             id_puesto = Integer.valueOf(request.getParameter("txtIdPuesto"));
                             registros = puestos.insertarPuesto(conexion,id_puesto,desc_puesto);
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'>El puesto se ha agregado correctamente</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible agregar el puesto debido a que ya existe.</font></b>
                             <%
                             }
                        break;
                       case 'M'://(M)odfica un registro de la tabla de Puestos 
                           id_puesto = Integer.valueOf(request.getParameter("lstPuestos"));
                             desc_puesto = request.getParameter("txtDescripcion");
                             registros   = puestos.actualizarPuesto(conexion,id_puesto,desc_puesto);
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'> Se ha actualizado correctamente el puesto</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible realizar la actualización del puesto.</font></b>
                             <%
                             }
                           break;
                          case 'E' ://(E)limina un registro de la tabla de Puestos
                             id_puesto = Integer.valueOf(request.getParameter("lstPuestos"));
                             desc_puesto = request.getParameter("txtDesc_puesto");
                             registros      = puestos.eliminarPuesto(conexion, id_puesto);
                             if( registros >= 1){
                             %>
                             <b><font color='#003399'>El puesto fué eliminado correctamente</font></b>
                             <%
                             }else if( registros == 0){
                             %>
                             <b><font color='#003399'>No es posible eliminar el puesto seleccionado, debido a que esta asociado a un empleado</font></b>
                             <%   
                             }
                        break;
                         }
                    break;
                    }//Fin switch externo*/
    }  catch(Exception E){
            conexion.rollback(); 
   %>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
    }
    finally{
        if (conexion != null){
            conexion.close();
            conexion=null;
        }  
    }
%>

<table width="100%">
 <tr><td width="73%">&nbsp;</td>
     <td width="10%">
         <INPUT TYPE="submit" NAME="btnAceptar" VALUE="Regresar" class=boton >
          </td>
  </tr>
</table>
</FORM>
</body>
</html>