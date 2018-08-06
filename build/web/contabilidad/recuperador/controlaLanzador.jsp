<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<jsp:useBean id="pbLanzador" class="sia.rf.contabilidad.registroContableEvento.Lanzador" scope="application"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta http-equiv="refresh" content="60;URL=<%=request.getRequestURL()%>"/>
    <title>controlaLanzador</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
  </head>
  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Proceso de Pólizas Automáticas", "Demonio", true);</jsp:scriptlet>  
   <%
   pbLanzador.setTiempo(10);// segundos
   String accion = request.getParameter("Accion");
   String activo = "false";
   if (accion == null){
   }else{
      if(accion.equals("1")) pbLanzador.iniciar();
      if(accion.equals("0")) pbLanzador.detener();
   }
  %>
  <br>
  <table border="1">
  <tr><th class=general width="20%">
  Proceso  
  </th>
  <th class=general width="10%">
  Fecha de Inicio
  </th>
  <th class=general width="10%">
  Periodo de Ejecución
  (Segs)
  </th>
  <th class=general width="5%">
  Ejecuciones
  </th>
  <th class=general width="5%">
  Errores
  </th>
  <th class=general width="5%">
  Activo
  </th>
  <th colspan="3" class=general >
  Acciones
  </th>
  </tr>
  <tr>
  <td>Sistema de Recuperación Contable</td><td><%=pbLanzador.getInicio()%></td>
  <td><%=pbLanzador.getTiempo()%></td><td><%=String.format("%,.0f",pbLanzador.getEjecuciones())%></td><td><%=String.format("%,.0f",pbLanzador.getErrores())%></td>
  <td><%=pbLanzador.isActivo()%></td>
  <td>
  <a href='controlaLanzador.jsp'>Actualizar</a>
  </td>
  <td>
  <a href='controlaLanzador.jsp?Accion=1'>Iniciar</a>
  </td><td>
  <a href='controlaLanzador.jsp?Accion=0'>Detener</a>
  </td></tr>
  </table>
  </body>
</html>