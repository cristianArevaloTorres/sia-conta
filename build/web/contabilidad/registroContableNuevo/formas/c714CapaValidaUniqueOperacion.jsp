<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>


<jsp:useBean id="pbForma" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcMaestroOperaciones" scope="request"/>
<%
String mensaje = "";
try  {
  String catCuentaId  = request.getParameter("catCuentaId");
  String uniEje       = request.getParameter("uniEje");
  String entidad      = request.getParameter("entidad");
  String ambito       = request.getParameter("ambito");
  String fecha        = request.getParameter("fecha");
  String forma        = request.getParameter("operacion");
  
  pbForma.validaUniqueOperacion(forma,uniEje,entidad,ambito,catCuentaId,fecha);
  
} catch (Exception ex)  {
  mensaje = !ex.getMessage().equals("") ? ex.getMessage() : "NME - validaUniqueOperacion";
} 

%>
<BODY OnLoad ="parent.regresaDeCapa('<%=mensaje%>');">

<script language='javascript'>
            
</script>
</BODY>