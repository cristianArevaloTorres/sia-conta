<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.util.*"%>
<%@ page import="sia.db.sql.*"%>
<%@ page import="sia.rf.tesoreria.bancas.acciones.Criterios"%>
<jsp:useBean id="crsIdMovimientos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="rfTrMovimientosCuentaTmp" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuentaTmp" scope="page"/>
<%@include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCapaElinimaDup</title>
  </head>
 
  <%
    boolean correcto = true;
    Connection con    = null;
    try  {
      con = DaoFactory.getTesoreria();  
      con.setAutoCommit(false);
      rfTrMovimientosCuentaTmp.eliminaRegDuplicados(con);
      con.commit();
    } catch (Exception ex)  {
        correcto = false;
        if(con!=null)
            con.rollback();
        ex.printStackTrace();
    } finally{
        DaoFactory.closeConnection(con);
        con = null; 
    }
    
  %>
   <body onLoad="parent.loadSourceFinish('divEliminaDup'); parent.returnElimina('<%=correcto%>');">
  </body >
</html>