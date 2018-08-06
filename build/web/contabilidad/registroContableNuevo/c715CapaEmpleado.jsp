<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentenciasCRS" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsEmpleados" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c715CapaEmpleado</title>
  </head>
  <body onload="parent.loadSourceFinish('divEmpleados');">
  <%
    String nombreCurp = request.getParameter("nombreCurp");
    StringBuffer condicion = null;
    String cads = null;
    StringTokenizer stk = null;
    String val = null;
    try  {
      condicion = new StringBuffer();
      condicion.append("e.curp like upper('%").append(nombreCurp).append("%') or (");
      stk = new StringTokenizer(nombreCurp," ");
      
      while(stk.hasMoreTokens()) {
        val = stk.nextToken();
        condicion.append(" (e.apellido_pat like upper('%").append(val).append("%')");
        condicion.append(" or e.apellido_mat like upper('%").append(val).append("%')");
        condicion.append(" or e.nombres like upper('%").append(val).append("%')) and ");
      }
      val = condicion.toString();
      val = val.substring(0,val.lastIndexOf(" and")).concat(")");
      
      crsEmpleados.addParamVal("condicion","and(:param)",val);
      crsEmpleados.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "firmasAutorizadas.select.RhTrEmpleados-EmpleadosActivos");
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally  {
    }
    
  %>
    <select class="cajaTexto" id="selEmpleado" name="selEmpleado"> 
      <option value=''>- Seleccione -</option>
      <%CRSComboBox(crsEmpleados, out,"num_empleado","apellido_pat,apellido_mat,nombres","");%>
    </select>
  </body>
</html>