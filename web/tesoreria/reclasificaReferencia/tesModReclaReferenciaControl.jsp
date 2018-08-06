<%@ page contentType="text/html;charset=ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.sql.*" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="rfTrMovimientosCuenta" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuenta" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesModReclaReferenciaControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
      function ir(pagina) {
        if(pagina!='' && pagina!='null') {
          document.getElementById("form1").action = pagina;
          document.getElementById("form1").submit();
        }
      }
      
    </script>
  </head>
  <%!
    private String getCause(Throwable e) {
    String reg="<br><br> - ";
    if(e.getCause()!=null) {
      //System.out.println(e.getCause());
      reg = reg.concat(e.getCause().toString());
      reg = reg.concat(getCause(e.getCause()));
    } else {reg="";}
    return reg;
    
  }
  %>
   <%
   
        Connection con    = null;
        boolean correcto  = false;
        String pagina     = null;
        String mensaje    = null; 
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          
              rfTrMovimientosCuenta.setIdMovimiento(request.getParameter("idMovimientoCta"));
              rfTrMovimientosCuenta.setIdCuenta(request.getParameter("idCuentaSelec"));
              rfTrMovimientosCuenta.select(con);
              %>
              <jsp:setProperty name="rfTrMovimientosCuenta" property="*"/>
              <%
              rfTrMovimientosCuenta.setReferencia(request.getParameter("referenciaV"));
              rfTrMovimientosCuenta.update(con);
              pagina = "'tesListadoMovReclaReferencia.jsp'";
              mensaje = "Se realizó la modificación de manera correcta";
           
          
          con.commit();
          correcto = true;
        } catch (Exception ex)  {
          ex.printStackTrace();
          System.out.println(getCause(ex));
           %> Ocurrio un error al realizar la operacion<br> Comuniquelo al administrador. <br> <%=ex.getMessage()%><%
        
          if(con!=null)
            con.rollback();
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
      %>
  
  <body onload="ir(<%=pagina%>)">
    <form id="form1" name="form1" action="tesFiltroCtaReclaReferencia.jsp" method="post">
     <input type="hidden" name="pagina" id="pagina" value="<%=request.getParameter("pagina")%>"/>
     <input type="hidden" name="cuentas" id="cuentas" value="<%=request.getParameter("cuentas")%>"/>
     <input type="hidden" name="fechaInicio" id="fechaInicio" value="<%=request.getParameter("fechaInicio")%>"/>
     <input type="hidden" name="fechaFinal" id="fechaFinal" value="<%=request.getParameter("fechaFinal")%>"/>
    </form>
  </body>
</html>