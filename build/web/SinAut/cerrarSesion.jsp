<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/> 
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
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
  }; // if  
  session.setAttribute("cerrarSesion",true);
  if(Autentifica!=null)
    Autentifica.inicializaUsuario();
	//session.invalidate();
%>
<%
  if(request.getParameter("nombreCapa")== null) {
%>    
  <form name="cerrar" id="cerrar" action="index.jsp" target="_top">
	  <script>document.getElementById('cerrar').submit();</script>
	</form>
<%
  }; // if
%>	
</body>
</html>
