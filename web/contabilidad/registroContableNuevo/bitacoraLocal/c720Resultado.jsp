<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ page import="sia.db.dao.DaoFactory"%>

<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="bitacoraLocal" class="sia.rf.contabilidad.registroContableEvento.BitacoraLocal" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c720Resultado</title>
     <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
  </head>
  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Bitacora Local", "Resultado", true);</jsp:scriptlet>
  <FORM name="Resultado" id="Resultado" Method="post" action="c720Filtro.jsp" >
  
 
  
  <!-- (Inicia codigo JAVA) -->
<%
   String Query="";
   Connection conexion=null;
   StringBuffer SQL=null;
   String lsOpcion= request.getParameter("txtOpcion");
   String sistema = request.getParameter("lstSistema");
   String modulo = request.getParameter("lstModulo");
   String evento = request.getParameter("lstEvento");
   String referencia= (request.getParameter("txtreferencia")).toUpperCase();
   String fecha01 = request.getParameter("txtFecha01")==null?"":request.getParameter("txtFecha01");
   String fecha02 = request.getParameter("txtFecha02")==null?"":request.getParameter("txtFecha02");
   String fecha03 = request.getParameter("txtFecha03")==null?"":request.getParameter("txtFecha03");
   String fecha04 = request.getParameter("txtFecha04")==null?"":request.getParameter("txtFecha04");
   String estatus = request.getParameter("lstEstatus"); 
   String cadConexion = request.getParameter("cadConexion");
   String descSistema = request.getParameter("descSistema");
   String descModulo = request.getParameter("descModulo");
   String descEvento = evento.equals("")?"Varios":request.getParameter("descEvento");
   String sinPoliza = request.getParameter("sinPoliza")==null?"0":request.getParameter("sinPoliza");
 try{
       //conexion=DaoFactory.getContabilidad();
       conexion=DaoFactory.getConnection(cadConexion);
       Query= bitacoraLocal.select_BITACORALOCAL_Query(evento,fecha01,fecha02,fecha03,fecha04,estatus,referencia,sinPoliza);       
       %>
       
         <table width='80%'>
        <thead>
            <tr><td >&nbsp;</td></tr>
            <tr>
            <td><div align="center">
                Sistema: <%=descSistema%>
                </div>
            </td>
            <td><div align="center">
                Modulo: <%=descModulo%>
                </div>
            </td>
            <td><div align="center">
                Evento: <%=descEvento%>
                </div>
            </td>
           </tr>
        </thead>     
        </table>       
        <!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
        <table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
        <tr>
          <td width="100%" bgcolor="#B6D0F1"> 
     <%
       String [] DefQuery={Query,"100%","LEFT","2",""};
       String [] DefAlias={" NUMREGISTRO-,IDEVENTO-,FECHASOLICITUD-,FECHAREGISTRO-,PARAMETROS-,ESTATUS_ARM-,POLIZAS_ARM-","5%,5%,15%,15%,35%,10%,15%","Num. registro,Evento,Fecha solicitud,Fecha registro,Parametros,Estatus,Polizas","4,4,4,4,4,4"};
       String [] DefInput={"IDEVENTO","CHECKBOX","ambitos","LEFT"};
       xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", ">", false);
       }    
       catch(Exception e){
        System.out.println("Error en pagina en "+" "+e.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
      }
      finally{ 
        if (conexion!=null){
         conexion.close();
         conexion=null;
        }
      }
%>
<!-- (Termina codigo JAVA) -->
</td>
</tr>
</table>

<table width='100%'>
  <thead>
    <tr><td >&nbsp;</td></tr>
    <tr>
       <td><div align="center">
            <INPUT TYPE="submit" NAME="btnRegresar" VALUE="Regresar" class='boton' >           
            </div>
        </td>
    </tr>
  </thead>     
</table>

<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
</FORM>
  </body>
</html>
