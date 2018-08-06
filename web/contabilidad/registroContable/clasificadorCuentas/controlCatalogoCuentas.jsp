<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.contabilidad.registroContable.clasificadorCuentas.servicios.ProcesoCatalogoCuentas,sia.beans.seguridad.Autentifica"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="sbAutentifica" class ="sia.beans.seguridad.Autentifica" scope="session" />
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>controlCatalogoCuentas</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
  </head>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Procesar archivo", "Proceso", true);</jsp:scriptlet>    
    <body>
    <form id="form" method="POST" action="catalogoCuentas.jsp">
<%
String rutaArchivo=request.getParameter("rutaArchivo");
String nombreArchivo=request.getParameter("nombreArchivo");
String fechaIni=request.getParameter("txtFechaIni");
String fechaFin=request.getParameter("txtFechaFin");
String fechaRegistro=request.getParameter("txtFechaRegistro");
String idCatalogoCuenta=request.getParameter("cboIdCatalogoCuenta");
//String nivelMaximo=request.getParameter("cboNivel");
String nivelMaximo="9";
String accion=request.getParameter("accion");
String tipoCarga=request.getParameter("tipoCarga");
String ejercicio=null;
String mes = null;
StringBuffer mensaje = new StringBuffer();
ProcesoCatalogoCuentas proceso = null;

try{
  nombreArchivo = "/"+nombreArchivo.substring(nombreArchivo.lastIndexOf("\\")+1,nombreArchivo.length());
         
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
  if(!accion.equals("1")){
    ejercicio = request.getParameter("lstEjercicio");
    proceso = new ProcesoCatalogoCuentas(idCatalogoCuenta,accion,ejercicio,nivelMaximo,sbAutentifica.getNumeroEmpleado());
    if(accion.equals("2")){
      mes = request.getParameter("mes");
      proceso.setMes(mes);
    }
  }
  else{
    proceso = new ProcesoCatalogoCuentas(fechaIni,fechaFin,fechaRegistro,idCatalogoCuenta,nivelMaximo,accion,sbAutentifica.getNumeroEmpleado());
  }
  proceso.setTipoApertura(tipoCarga);
  System.out.println(rutaArchivo+nombreArchivo);
  mensaje = proceso.procesarArchivo(rutaArchivo+nombreArchivo);
%>
<p> <font color='#003399'><%=mensaje%></font></p>

<br>

<%

}
catch(Exception e){ 
         e.printStackTrace();
%>
     <p>Ha ocurrido un error en el proceso,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p> <font color='#003399'><%=mensaje%></font></p>
     <p>Gracias.</p>
<%
       } 
       finally{
        proceso=null;
        mensaje=null;
        
       }
%>
<br/>
<INPUT TYPE="submit" NAME="btnRegresar" VALUE="Regresar" class="boton">
</form>
</body>
</html>