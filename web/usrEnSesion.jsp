
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.beans.seguridad.UsuariosSitio"%>
<%@ page import="sia.beans.seguridad.Usuario"%>
<%@ page import="java.util.List"%>

<%@ page import="sia.libs.formato.Fecha"%>
<%@ page import="java.util.Calendar"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<html>
  <head>
    <meta http-equiv="refresh" content="600"/>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Usuarios en sesion</title>
    <link rel="stylesheet" href="Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="Librerias/Javascript/tabla.js" type="text/javascript">
    </script>
  </head>
  <%
    UsuariosSitio usrSitio=null;
    String[] modulos=null;
    boolean adm = request.getParameter("adm")!=null ? request.getParameter("adm").equals("2525") ? true : false : false;
    try  {
      usrSitio = ((UsuariosSitio)request.getSession().getServletContext().getAttribute("UsuariosSitio"));
      modulos = usrSitio.getModulos().split(",");
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    
  %>
  <body>
    <%util.tituloPagina(out,"SIA","Usuarios en sesion","Resultado",true);%>
    <%int i = 1;
      List<Usuario> usuarios = usrSitio.getUsuariosSinModulo();
      if(usuarios!=null && usuarios.size()!=0) {
    %>
    <br><br><table align='center' width="90%">
      <thead></thead>
      <tbody>
        <tr>
          <td> Usuarios </td>
        <tr>
      </tbody>
    </table> <br>
    <table align="center" class="general" cellpadding="3" width="90%">
      <thead>
        <tr>	
          <th class="azulObs">#</th>
          <th class="azulObs">Cuenta</th>
          <th class="azulObs">Fecha</th>
          <th class="azulObs">Hora</th>
          <th class="azulObs">Min en sesion</th>
          <th class="azulObs">Usuario</th>
          <th class="azulObs">Unidad ejecutora</th>
          <th class="azulObs">Entidad</th>
          <th class="azulObs">Ambito</th>
        </tr>
      </thead>
      <tbody>
        <%
          
          for (Usuario usr : usuarios)  { 
          %>
        <tr onmouseover='over(this)' onmouseout='out(this)'>
          <td><%=i++%></td>
          <td><a href="seguridad/Admin/s001PerfilesUsuariosFiltro.jsp?consulta=1&emergente=1&numeroEmpleado=<%=usr.getNumEmpleado()%>&nombreCompleto=<%=usr.getNombreCompleto()%>" target="_blank"><%=usr.getCuenta()%></a></td>
          <td><%=Fecha.formatear(Fecha.FECHA_NOMBRE_DIA,usr.getHora())%></td>
          <td><%=Fecha.formatear(Fecha.HORA_CORTA,usr.getHora())%></td>
          <td align="center"><%=String.valueOf(Fecha.diferenciaMinutos(usr.getHora().getTimeInMillis(), Calendar.getInstance().getTimeInMillis()))%></td>
          <td><%=usr.getNombreCompleto()%></td>
          <td><%=usr.getUnidadEjecutora()%></td>
          <td><%=usr.getEntidad()%></td>
          <td><%=usr.getAmbito()%></td>
          
        </tr>
        <%}%>
      </tbody>
    </table>
    <% }
      for(String modulo : modulos) { 
        if(modulo!=null && !modulo.equals("")) {
          usuarios = usrSitio.getUsuariosModulo(modulo);
          if(usuarios!=null && usuarios.size()!=0) {
    %>
      
    <br><br><table align='center' width="90%">
      <thead></thead>
      <tbody>
        <tr>
          <td><%=modulo.toUpperCase() %></td>
        <tr>
      </tbody>
    </table> <br>
    <table align="center" class="general" cellpadding="3" width="90%">
      <thead>
        <tr>	
          <th class="azulObs">#</th>
          <%if(adm) { %>
          <th class="azulObs">St</th>
          <% } %>
          <th class="azulObs">Cuenta</th>
          <th class="azulObs">Fecha</th>
          <th class="azulObs">Hora</th>
          <th class="azulObs">Min en sesion</th>
          <th class="azulObs">Usuario</th>
          <th class="azulObs">Unidad ejecutora</th>
          <th class="azulObs">Entidad</th>
          <th class="azulObs">Ambito</th>
        </tr>
      </thead>
      <tbody>
        <% 
           i = 1;
          for (Usuario usr : usuarios)  { 
          %>
        <tr onmouseover='over(this)' onmouseout='out(this)'>
          <td><%=i++%></td>
          <%if(adm) {%>
          <td> <a target="_blank" href="usuariosEnSesionStack.jsp?login=<%=usr.getCuenta()%>&idSesion=<%=usr.getSesion()%>"><img src="Librerias/Imagenes/stack.jpeg" width="18px" height="19px" title="stack" border="0"></img></a> </td>
          <%}%>
          <td><a href="seguridad/Admin/s001PerfilesUsuariosFiltro.jsp?consulta=1&emergente=1&numeroEmpleado=<%=usr.getNumEmpleado()%>&nombreCompleto=<%=usr.getNombreCompleto()%>" target="_blank"><%=usr.getCuenta()%></a></td>
          <td><%=Fecha.formatear(Fecha.FECHA_NOMBRE_DIA,usr.getHora())%></td>
          <td><%=Fecha.formatear(Fecha.HORA_CORTA,usr.getHora())%></td>
          <td align="center"><%=String.valueOf(Fecha.diferenciaMinutos(usr.getHora().getTimeInMillis(), Calendar.getInstance().getTimeInMillis()))%></td>
          <td><%=usr.getNombreCompleto()%></td>
          <td><%=usr.getUnidadEjecutora()%></td>
          <td><%=usr.getEntidad()%></td>
          <td><%=usr.getAmbito()%></td>
          
        </tr>
        <%
            }//if
          } //if
        }// for%>
      </tbody>
    </table>
    <% } %>
  </body>
</html>