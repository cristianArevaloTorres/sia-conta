<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>


<jsp:useBean id="pbForma" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="request"/>
<%
String mensaje = "";
try  {
  String catCuentaId  = request.getParameter("catCuentaId");
  String uniEje       = request.getParameter("uniEje");
  String entidad      = request.getParameter("entidad");
  String ambito       = request.getParameter("ambito");
  String ejercicio    = request.getParameter("ejercicio");
  String forma        = request.getParameter("forma");
  
  pbForma.validaUniqueForma(forma,uniEje,entidad,ambito,catCuentaId);
  
} catch (Exception ex)  {
  mensaje = !ex.getMessage().equals("") ? ex.getMessage() : "NME - validaUniqueForma";
} 

%>
<BODY OnLoad ="parent.regresaDeCapa('<%=mensaje%>');">

<script language='javascript'>
            
</script>
</BODY>