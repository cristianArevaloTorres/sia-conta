<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%    
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }  
                    
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Catálogo de Cuentas</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>

<script language="JavaScript" type="text/javascript">
  
function enviar(){
     document.forms[0].submit();
  }
  </script>
</head>

<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Opciones del Mantenimiento de Pólizas","Filtrar", true);</jsp:scriptlet>
<form id="form1" name="form1" method="post" action="c710AgregarFormulario.jsp">
<table  width="100%" border="0">
    <tr> 
      <td width="390" height="32"> <div align="left"><strong>Fecha actual:</strong> 
      [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%> ]</div></td>
      <td width="625">&nbsp; </td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td colspan="5">&nbsp;</td>
    <tr> 
      <td><div align="right"></div></td>
      <td>&nbsp;</td>
    </tr>
 <tr>
 <tr> 
    <td><div align="right"></div></td>
    <td>&nbsp;</td>
 </tr>
 </table>

<table border='1' align="center" class="resultado" cellPadding="3">
    <tr class="resAzulOscuro">      
      <td colspan="5"><div align="center">Opciones</div></td>
    </tr>
    
    <tr class="resultado">
        <%-- Se agrega restricción para Clave de perfil 16 (Contraloría) Lissette 05062017 --%>
        <td>  <% if (!Autentifica.getPerfilAcceso().equals("16")) { %>  
            <div align="center"><img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16"> 
        <a href="c700PolizaFormulario.jsp?opcion=A">Agregar</a>  
            </div> <% } %> 
        </td>
      <td>  <% if (!Autentifica.getPerfilAcceso().equals("16")) { %>  
          <div align="center"><img src="../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16"> 
          <a href="c700ModificarFiltro.jsp?opcion=M">Modificar</a>
          </div> <% } %>
      </td>
      <td>  <% if (!Autentifica.getPerfilAcceso().equals("16")) { %>  
          <div align="center"><img src="../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16"> 
          <a href="c700ModificarFiltro.jsp?opcion=C">Cancelar</a> 
          </div> <% } %>
      </td>
      <td><div align="center"><img src="../../Librerias/Imagenes/botonConsultar2.gif" width="16" height="16"> 
          <a href="c700ConsultarFiltro.jsp?opcion=Co">Consultar</a> </div></td>
    </tr>     
  </table>
 <br>
  <hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
  <br>
  <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center">
      <td>
        <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('filtroGeneral.jsp?opcion=CapturadorPolizas&idCatalogoCuenta=1','');">
      </td>
    </tr>
  </table>      
<br> 
<br>      
<img src="../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16"><b>Dar clic para agregar pólizas</b><br>
<img src="../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16"><b>Dar clic para modificar pólizas</b><br>
<img src="../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16"><b>Dar clic para cancelar pólizas</b><br>
<img src="../../Librerias/Imagenes/botonConsultar2.gif" width="16" height="16"><b>Dar clic para consultar e imprimir pólizas</b><br>
</form>
</body>
</html>

