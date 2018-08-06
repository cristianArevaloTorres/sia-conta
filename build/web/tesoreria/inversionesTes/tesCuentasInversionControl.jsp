<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Error" %>
<%@ page import="java.sql.*" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="rfTrCuentasInversion" class="sia.rf.tesoreria.registro.cuentasInversion.bcRfTrCuentasInversion" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCuentasInversionControl</title>
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
        <%
        Connection con    = null;
        boolean correcto  = false;
        String pagina     = null;
        String mensaje    = null; 
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          int accion = Integer.parseInt(request.getParameter("accion"));
          
          switch(accion) {
            case 1: //
              %>
              <jsp:setProperty name="rfTrCuentasInversion" property="*"/>
              <%
              rfTrCuentasInversion.insert(con);
              pagina = "'tesAgregarCuentasInversion.jsp'";
              mensaje = "Se realizó la inserción de manera correcta";
              break;
            case 2: // modificacion
              rfTrCuentasInversion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
              rfTrCuentasInversion.select(con);
              %>
              <jsp:setProperty name="rfTrCuentasInversion" property="*"/>
              <%
              rfTrCuentasInversion.update(con);
              pagina = "'tesFiltroCuentasInversion.jsp'";
              mensaje = "Se realizó la modificación de manera correcta";
              break;
          }
          con.commit();
          correcto = true;
        } catch (Exception ex)  {
        %> Ocurrio un error al realizar la operacion<br> Comuniquelo al administrador. <br> <%=ex.getMessage()%><%
          if(con!=null)
            con.rollback();
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
        
      %>
  <body onload="ir(<%=pagina%>)">
    <form id="form1" name="form1" action="tesFiltroCuentasInversion.jsp" method="post">
      <%util.tituloPagina(out,"Tesorería","Cuentas de inversión","Control",true);%>
      <input type="hidden" value="<%=request.getParameter("fBancoInv")%>" id="fBancoInv" name="fBancoInv">

        <br>
        
        <input type="hidden" value="<%=correcto%>" id="correcto" name="correcto">
        <table align="center">
          <thead></thead>
          <tbody>
            <tr>
              <td align="center"><%=mensaje%></td>
            </tr>
            <tr>
              <td align="center"><hr class="piePagina"></td>
            </tr>
            
            <tr>
              <td align="center"><input type="button" value="Aceptar" class="boton" onclick="ir(<%=pagina%>)"></td>
            </tr>
          </tbody>
        </table>
    </form>
  </body>
</html>