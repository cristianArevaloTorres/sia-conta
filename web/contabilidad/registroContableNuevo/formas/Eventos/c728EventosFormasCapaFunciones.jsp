
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="crsVariables" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c728EventosFormasCapaFunciones</title>
  </head>
  <%
    String idFuncion = request.getParameter("idfuncion");
    try  {
      crsVariables.addParamVal("idFuncion","and f.idFuncion = :param",idFuncion);
      crsVariables.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"eventos.select.RfTcFunciones.Formas");
      if(crsVariables.size()>0)
        crsVariables.next();
      //System.out.println(crsVariables.getString("TIPO")+" - "+crsVariables.getString("REGLA_CONTABLE")+" - "+crsVariables.getString("VARIABLE_NOMBRE_VARIABLE"));
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    
  %>
  <script type="text/javascript" languaje="javascript">
        parent.document.getElementById('tipo').value = "<%=crsVariables.getString("TIPO")==null?"":crsVariables.getString("TIPO")%>";
        parent.document.getElementById('reglaContable').innerHTML = "<%=crsVariables.getString("REGLA_CONTABLE")==null?"":crsVariables.getString("REGLA_CONTABLE")%>";
        parent.document.getElementById('nombreVariable').value = "<%=crsVariables.getString("VARIABLE_NOMBRE_VARIABLE")==null?"":crsVariables.getString("VARIABLE_NOMBRE_VARIABLE")%>";
  </script>
  <body onload="parent.loadSourceFinish('dfuncion');"></body>
</html>