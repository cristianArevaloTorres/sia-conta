<%-- 
  
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<!DOCTYPE html>
<html>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        
        <title>c763Filtro</title>
           <script language="JavaScript">
             function validaSeleccion(){    
                     var indice; 
                    var seleccionado = false;  
                    mensaje= '------------------ ALERTA ------------------\n';  
                    mensaje+='Por favor seleccione alguna opción';    
                    for (indice=0;indice<document.forms.forma.catalogo.length;indice++){ 
                            if (document.forms.forma.catalogo[indice].checked){ 
                                seleccionado = true;
                                break;
                            }  
                    }
                    if(seleccionado==false){
                        alert(mensaje);
                        return false;
                    }
                    else{
                        document.forms.forma.submit();
                    }     
             }             
            </script>       
       <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>
    </head>
    <body >
      <form id="forma" method="post" action="c763AgregarFormulario.jsp">
       <%util.tituloPagina(out, "Contabilidad", "Administración de Catálogos ","", "Formulario", true);%>
       <br></br>
      <br></br>       
      <table width="40%" align="center">
        <tr class="azulCla">
          <td align="center" >Seleccionar Opción</td>
        </tr>
      </table>
     
      <table border="0" width="20%" class='sianoborder' align="center">
        <tr align="left">
  
          <td><input type="radio" name="catalogo" value="unidades" onclick=""/>Unidades Ejecutoras</td>
        </tr>
        <tr align="left">
          <td><input type="radio" name="catalogo" value="puestos" onclick=""/> Puestos</td>
        </tr>
        <tr align="left">
          <td><input type="radio" name="catalogo" value="estructura" onclick=""/> Estructura Funcional</td>
        </tr>
      </table>
      <br></br>
      <br></br>
      
        <table border='1' align="center" class="resultado" cellPadding="3">
            <tr class="resAzulOscuro">      
              <td colspan="5"><div align="center">Opciones</div></td>
            </tr>
            <tr class="resultado"> 
              <td> <div align="center"><img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Agregar" onClick="javaScript: if(validaSeleccion()==false)return false;" /> </div></td>
              <td><div align="center"><img src="../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Modificar" onClick="javaScript: if(validaSeleccion()==false)return false;" />
              <td><div align="center"><img src="../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16"> 
                  <input type="submit" name="operacion" class="boton" value="Eliminar" onClick="javaScript: if(validaSeleccion()==false)return false;" /> </div></td>
            </tr>     
        </table>
         <br>
            <hr noshade="noshade" width="40%" align="center" style="border-width: 2px;border-style:solid">
         <br>
      
      </form>
       </body>
</html>
