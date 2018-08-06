<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%    
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }        
    Connection conexion=null;
    try{
    conexion=DaoFactory.getContabilidad();        
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Catálogo de Cuentas</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>

<script language="JavaScript" type="text/javascript">
  
function enviar(){
     document.forms[0].submit();
  }
  </script>
</head>

<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cuentas de Mayor", "Operaciones", true);</jsp:scriptlet>
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

<table width='100%' border='0'>
    <tr> 
      <td>&nbsp;</td>
      <td colspan="5"><div align="center">Opciones</div></td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td width="13%" rowspan="3"> <div align="center"><img src="../../../Librerias/Imagenes/botonAgregar2.gif" width="16" height="16"> 
          <a href="c710AgregarFormulario.jsp" accesskey="+">Agregar</a> </div></td>
      <td width="12%" rowspan="3"><div align="center"><img src="../../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16"> 
          <a href="c710ModificarFiltro.jsp" accesskey="M">Modificar</a></div></td>
      <td width="11%" rowspan="3"><div align="center"><img src="../../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16"> 
          <a href="c710EliminarFiltro.jsp" accesskey="e">Eliminar</a> </div></td>
      <td width="11%" rowspan="3"><div align="center"><img src="../../../Librerias/Imagenes/botonConsultar2.gif" width="16" height="16"> 
          <a href="c710ConsultarFiltro.jsp" accesskey="c">Consultar</a> </div></td>
      <td width="12%" rowspan="3"><div align="center"><img src="../../../Librerias/Imagenes/botonImpresora.gif" width="16" height="16"> 
          <a href="c710RepFiltro.jsp" accesskey="c">Imprimir</a> </div></td>
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
<%
    }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally

%>
