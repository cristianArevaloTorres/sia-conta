<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<%@include file="/Librerias/Funciones/pool.jspf"%>
<html>
<head>
<title>Cerrar sesión del usuario</title>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1">
</head>
<%
  String nombreCapa= request.getParameter("nombreCapa");
%>
<body onLoad="<%= request.getParameter("nombreCapa")!= null? "parent.loadSourceFinish('"+ nombreCapa+ "');": ""%>">
<%
  if(nombreCapa== null && Autentifica.getLogin().length()> 0) {
    System.out.println("Cerrar sesion ["+ Autentifica.getLogin()+ "]");
    conection.setAutoCommit(false);
    //Autentifica.cerrarSesion(conection);
    conection.commit();
  }; // if  
  session.setAttribute("cerrarSesion",true);
  session.removeAttribute("siaLogin");
  session.removeAttribute("siaPassword");
  session.removeAttribute("siaEntidad");
  session.removeAttribute("reload");
	session.invalidate();
%>
<%@include file="/Librerias/Funciones/libera.jspf"%>
<%
  if(request.getParameter("nombreCapa")== null) {
%>    
  <form name="cerrar" id="cerrar" action="<%=util.getContexto(request)%>/index.jsp" target="_top">
	  <script>document.getElementById('cerrar').submit();</script>
	</form>
<%
  }; // if
%>	
</body>
</html>
