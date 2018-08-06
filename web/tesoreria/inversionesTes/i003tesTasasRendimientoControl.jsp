<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.tasasRendimiento.bcRfTrTasasRendimiento" %>
<%@ page import="sia.rf.tesoreria.registro.tasasRendimiento.bcRfTrTasasRendDetalle" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="java.sql.*" %>
<%@ page import="sia.db.sql.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i003TasasRendimientoControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">  
    <script type="text/javascript" language="javascript">
      function ir(pagina) {
        document.getElementById('form1').action = pagina;
        document.getElementById('form1').submit();
      }
      
      function envia(pagina) {
        //alert(pagina);
        if(pagina!='' && 
        pagina!='null') {
          document.getElementById("form1").action=pagina;
          document.getElementById("form1").submit();
        }
      }
    </script>
  </head>
  <%!
        private void registrarTasasRendimientoDetalle(Connection con, HttpServletRequest request, String idRendimiento) throws Exception {
          bcRfTrTasasRendDetalle rendimientoDetalle = null;
          String[] plazos = request.getParameterValues(request.getParameter("idBanco").equals("498")?"plazob":"plazo");
          for (int i = 0; i < plazos.length; i++)  {
            rendimientoDetalle = new bcRfTrTasasRendDetalle();
            rendimientoDetalle.setIdRendimiento(idRendimiento);
            rendimientoDetalle.setPlazo(plazos[i]);
            rendimientoDetalle.setTasa(request.getParameterValues(request.getParameter("idBanco").equals("498")?"tasab":"tasa")[i]);
            rendimientoDetalle.insert(con);
          }
        }
      
        private void registrarTasasRendimiento(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrTasasRendimiento tasasRendimiento = new bcRfTrTasasRendimiento();
          tasasRendimiento.setFecha(request.getParameter("fecha"));
          tasasRendimiento.setIdBanco(request.getParameter("idBanco"));
          tasasRendimiento.insert(con);
          registrarTasasRendimientoDetalle(con,request, tasasRendimiento.getIdRendimiento());
        }
        
        private void actualizarTasasRendimientoDetalle(Connection con, HttpServletRequest request, String idRendimiento) throws Exception {
          bcRfTrTasasRendDetalle rendimientoDetalle = null;
          String[] plazos = request.getParameterValues(request.getParameter("idBanco").equals("498")?"plazob":"plazo");
          for (int i = 0; i < plazos.length; i++)  {
            rendimientoDetalle = new bcRfTrTasasRendDetalle();
            rendimientoDetalle.setIdRendimiento(idRendimiento);
            rendimientoDetalle.setPlazo(plazos[i]);
            rendimientoDetalle.setTasa(request.getParameterValues(request.getParameter("idBanco").equals("498")?"tasab":"tasa")[i]);
            rendimientoDetalle.update(con);
          }
        }
        
        private void modificarTasasRendimiento(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrTasasRendimiento tasasRendimiento = new bcRfTrTasasRendimiento();
          tasasRendimiento.setIdRendimiento(request.getParameter("idRendimiento"));
          tasasRendimiento.select(con);
          
          tasasRendimiento.setFecha(request.getParameter("fecha"));
          tasasRendimiento.setIdBanco(request.getParameter("idBanco"));
          tasasRendimiento.update(con);
          actualizarTasasRendimientoDetalle(con,request, tasasRendimiento.getIdRendimiento());
        }
      %>
      <%
        int accion = Integer.parseInt(request.getParameter("accion"));
        String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
        String fidBanco = request.getParameter("fidBanco")== null ? "" : request.getParameter("fidBanco");
        Connection con = null;
        String pagina = null;
        String mensaje="";
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          switch(accion) {
          case 1: // Agregar
            registrarTasasRendimiento(con, request);
            pagina="'i003tesTasasRendimientoAgregar.jsp'";
            mensaje = "Se realizó la inserción de manera correcta";
            break;
          case 2: // Modificar
            modificarTasasRendimiento(con, request);
            pagina="'i003tesTasasRendimientoFiltro.jsp'";
            mensaje = "Se realizó la modificación de manera correcta";
            break;
        }
          con.commit();
        } catch (Exception ex)  {
          con.rollback();
          mensaje = ex.getMessage();
          ex.printStackTrace();
        } finally  {
          DaoFactory.closeConnection(con);
        }
        
      %>
  <body onload="envia(<%=pagina%>)">
    <form id='form1' name='form1' action='' method='post'>
      <%=pagina%>
      <%util.tituloPagina(out,"Tesorería central","Tasas de rendimiento","Control",true);%>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td align="center"><%=mensaje%></td>
          </tr>
          <tr>
            <td><hr class="piePagina"></td>
          </tr>
          <tr>
            <td align="center"><input type="button" class="boton" value="Aceptar" onclick="ir(<%=pagina%>)">
            <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
            <input type="hidden" id="fidBanco" name="fidBanco" value="<%=fidBanco%>"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>