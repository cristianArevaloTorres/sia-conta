<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="modulo" class="sia.rf.contabilidad.registroContableEvento.Modulo" scope="session"/>

<%
Connection conexion=null;
CachedRowSet crsModulo = null;
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    String sistema= request.getParameter("sistema");
    crsModulo  = modulo.select_rf_tr_modulo_cacheRowSet(conexion,sistema);
    String paramMod="1";
    if(crsModulo.first())
      paramMod=crsModulo.getString(1);
    crsModulo.beforeFirst();   
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Modulo</title>
  </head>
  <body onload="parent.loadSourceFinish('capaModulo');parent.loadSource('capaEvento',null,'Evento.jsp','?modulo=<%=paramMod%>');">
  <SELECT NAME="lstModulo" class="cajaTexto" OnChange="loadSource('capaEvento',null,'Evento.jsp','?modulo='+document.Filtro.lstModulo[selectedIndex].value);">
  <%  
    while (crsModulo.next()){
  %>
      <OPTION VALUE="<%=crsModulo.getString(1)%>"><%=crsModulo.getString(3)%></OPTION>
  <% 
    }//while     
    crsModulo.close();
    crsModulo = null;
  %>
  </select>
  
  </body>
  
 <%
  }catch(Exception e){
      System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    }
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally
%>
</html>