<%-- 
    Document   : c745Filtro
    Created on : 12/07/2013, 09:42:15 AM
    Author     : EST.JOSE.FLORES
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>c745Filtro</title>
            <script language="JavaScript">
             function validaSeleccion(){    
                    var indice; 
                    var seleccionado = false;  
                    mensaje= '------------------ ALERTA ------------------\n';  
                    mensaje+='Por favor seleccione alguna opción (Género, Grupo o Rubro)';    
                    for (indice=0;indice<document.forms.forma.tipo.length;indice++){ 
                            if (document.forms.forma.tipo[indice].checked){ 
                                seleccionado = true;
                                break;
                            }  
                    }
                    if(seleccionado==false){
                        alert(mensaje);
                        return false;
                    }
                    else document.forms.forma.submit();
             }             
            </script>        
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    </head>
    <body >
      <form id="forma" method="post" action="c745Formulario.jsp" >
        <%util.tituloPagina(out, "Contabilidad", " ","Catálogos Género, Grupo y Rubro" , "Filtro", true);%>
        <br></br>
        <br></br>       
        <table width="40%" align="center">
          <tr class="azulCla">
            <td align="center" >Seleccionar Opción</td>
          </tr>
        </table>
        <table border="0" width="10%" class='sianoborder' align="center">
          <tr align="left">
              <td><input type="radio" name="tipo" value="genero" onclick=""/> Género</td>
          </tr>
          <tr align="left">
              <td><input type="radio" name="tipo" value="grupo" onclick=""/> Grupo</td>
          </tr>
          <tr align="left">
              <td><input type="radio" name="tipo" value="clase" onclick=""/> Rubro</td>
          </tr>
        </table>
        <br></br>
        <br></br>
        <table border='1' align="center" class="resultado" cellPadding="3">
          <tr class="resAzulOscuro">      
              <td colspan="5"><div align="center">Opciones</div></td>
          </tr>
          <tr class="resultado"> 
              <td> <div align="center"><img src="../../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Agregar" onClick="javaScript: if(validaSeleccion()==false)return false;" /> </div></td>
              <td><div align="center"><img src="../../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Modificar" onClick="javaScript: if(validaSeleccion()==false)return false;" />
              <td><div align="center"><img src="../../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Eliminar" onClick="javaScript: if(validaSeleccion()==false)return false;" /> </div></td>
          </tr>     
        </table>
        <br>
            <hr noshade="noshade" width="40%" align="center" style="border-width: 2px;border-style:solid">
        <br>
      </form>
    </body>
</html>