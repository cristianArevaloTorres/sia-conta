<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" 
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
   <head>
      <title>c711Clasificador de Cuentas</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
   </head>
   <body>
         <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Clasificador de Cuentas", "Operaciones", true);</jsp:scriptlet>

         <form id="form1" name="form1" method="post" action="c710AgregarFormulario.jsp">
            <table width="100%" border="0">
               <tr>
                  <td colspan="5">&nbsp;</td>
                  <td width="20%">&nbsp;</td>
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td colspan="5">&nbsp;</td>
                  <td>&nbsp;</td>
               </tr>
               <tr>
                  <td>&nbsp;</td>
                  <td colspan="5">&nbsp;</td>
                  <td>&nbsp;</td>
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td colspan="4"><div align="center">Opciones</div></td>
                  <td>&nbsp;</td>
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td width="13%" rowspan="3">
                     <div align="center">
                         <img src="../../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16" alt="Agregar"> 
                         <a href="c711AgregarFiltro.jsp" accesskey="+">Agregar</a>
                     </div>
                  </td>
                  <td width="12%" rowspan="3">
                     <div align="center">
                        <img src="../../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16" alt="Modificar"> 
                        <a href="c711ModificarFiltro.jsp" accesskey="M">Modificar</a>
                     </div>
                  </td>
                  <td width="11%" rowspan="3">
                     <div align="center">
                        <img src="../../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16" alt="Eliminar"> 
                        <a href="c711EliminarFiltro.jsp" accesskey="e">Eliminar</a>
                     </div>
                  </td>
                  <td width="11%" rowspan="3">
                     <div align="center">
                        <img src="../../../Librerias/Imagenes/botonConsultar2.gif" width="16" height="16" alt="Consultar"> 
                        <a href="c711ConsultarFiltro.jsp" accesskey="c">Consultar</a>
                     </div>
                  </td>
                  <td width="12%" rowspan="3">&nbsp;</td>
                  <td>&nbsp;</td>
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td>&nbsp;</td> 
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td>&nbsp;</td>
               </tr>
               <tr> 
                  <td>&nbsp;</td>
                  <td colspan="5">&nbsp;</td>
                   <td>&nbsp;</td>
               </tr>
            </table>   
         </form>
    </body>
</html>
