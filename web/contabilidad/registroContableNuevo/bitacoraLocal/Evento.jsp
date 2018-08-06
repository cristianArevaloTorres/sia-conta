<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="evento" class="sia.rf.contabilidad.registroContableEvento.EventoContable" scope="session"/>

<%
Connection conexion=null;
CachedRowSet crsEvento = null;
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    String idModulo= request.getParameter("modulo");
    crsEvento  = evento.select_rf_tr_eventoContable_cacheRowSet(conexion,idModulo);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Modulo</title>
  </head>
  <body onload="parent.loadSourceFinish('capaEvento');">
  
  
  <SELECT NAME="lstEvento" class="cajaTexto" OnChange="">
  <option value="">-Seleccione-</option>
  <%  
    while (crsEvento.next()){
  %>
      <OPTION VALUE="<%=crsEvento.getString(1)%>"><%=crsEvento.getString(3)%></OPTION>
  <% 
    }//while     
    
  %>
  </select>
  
  </body>
  
 <%
  }catch(Exception e){
      System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    }
    finally{
    crsEvento.close();
    crsEvento = null;
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally
%>
</html>