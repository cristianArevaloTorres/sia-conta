<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page import="sia.db.dao.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>

<%@page import="java.util.*" %>
<%@page import="java.sql.*"%>
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbCatalogo" class="sia.rf.contabilidad.registroContableEvento.Catalogo" scope="application"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>controlaCatalogo</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
</head>
<body>
    <%util.tituloPagina(out,"Contabilidad institucional","Control del Catalogo de cuentas","Control",true);%>
    <br><br>
    <jsp:directive.include file="../registroContableNuevo/encabezadoFechaActual.jspf"/>
    <br>
<form name="control" method="post"  action="">       
<%    
Connection conexion=null;
sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");

String lsEstadoNuevo=request.getParameter("lstEstadosCatalogo");

try{
   conexion=DaoFactory.getContabilidad();
   
   conexion.setAutoCommit(false); 
   String lsResultado="";
   
   pbCatalogo.setEstadoNuevo(lsEstadoNuevo);
   pbCatalogo.cambiaEstado(conexion);
   
   conexion.commit();
   pbCatalogo.select_estado_actual(conexion);
   lsResultado=pbCatalogo.getDescripcion();
   
%>
<p class='texto02'>El Catálogo de cuentas ha cambiado al estado de:  </p>
<br>
<p class='texto02'>  <b><font color='#003399'><%=lsResultado%></font></b> </p>
<br><br><br>
<%
  }//del try
catch(Exception E){
    conexion.rollback(); 
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }  
}
%>
    
    <table align="center">
      <tr><td><br></td>
          <td>
              <input type='hidden' name='txtIdCatalogo' value=<%=request.getAttribute("idCatalogoCuenta")%>>
              <input type='hidden' name='txtUnidad' value=<%=request.getAttribute("unidadEjecutora")%>>
              <input type='hidden' name='txtAmbito' value=<%=request.getAttribute("ambito")%>>
              <input type='hidden' name='txtEntidad' value=<%=request.getAttribute("entidad")%>>
              <input type='hidden' name='txtEjercicio' value=<%=request.getAttribute("ejercicio")%>>              
              <input type='hidden' name='txtOpcion' value=<%=request.getAttribute("opcion")%>>
              <input type='hidden' name='txtfechaActual' value=<%=request.getAttribute("fechaActual")%>>              
          </td>
      </tr>
      <tr>
          <td  align="right"></td>
          <td><input type='button' name='btnCancelar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c732controlaCatalogo.jsp','');"></td>
      </tr>
      </table>            
  </FORM>
  </body>
</html>
