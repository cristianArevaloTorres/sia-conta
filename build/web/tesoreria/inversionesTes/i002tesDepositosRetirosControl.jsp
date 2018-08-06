
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.operacionesInversion.*" %>

<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="java.sql.*" %>
<%@ page import="sia.db.sql.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i002tesDepositosRetirosControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
     function regresarAPagina(pagina) {
        //alert(pagina);
        correcto = pagina==null?false:true;
        //alert(correcto)
        window.opener.regresaDeControl(correcto);
        cerrar();
        
      }
      
      function cerrar() {
        window.open('','_parent','');
        window.close();
      }
      
      var width     = screen.width;
      var height    = screen.height;
      function ajustarVentana(){
        window.resizeTo(width/2,height/2);
        window.moveTo(width/4, height/4);
      }
    </script>
  </head >
  <body onload='ajustarVentana()'>
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Dep&oacute;sitos o retiros de efectivo","Control",true);%>
      <%!
      
        private void lanzarCalculo(Connection con, String fecha, String idCuentaInversion) throws Exception {
          bcOperacionesInversionCalculo operacion = new bcOperacionesInversionCalculo();
          //compraInversion.select(con);
          operacion.lanzarCalculoDeFechaAnterior(con, fecha, idCuentaInversion);
        }
      
        private void insertarRegistro(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
          operacion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacion.setIdTipoOperacion(request.getParameter("idTipoOperacion"));
          operacion.setFecha(request.getParameter("fecha"));
          operacion.setImporte(request.getParameter("importe"));
          operacion.setEstatus("0");
          operacion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacion.insert(con);
          operacion.select(con);
          lanzarCalculo(con,operacion.getFecha(),operacion.getIdCuentaInversion());
          
          
        }
        
        private void modificarRegistro(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrOperacionesInversion operacionAnterior = new bcRfTrOperacionesInversion();
          operacionAnterior.setIdOperacion(request.getParameter("idOperacion"));
          operacionAnterior.select(con);
          bcRfTrOperacionesInversion operacionModificar = new bcRfTrOperacionesInversion();
          operacionModificar.setIdOperacion(request.getParameter("idOperacion"));
          operacionModificar.select(con);
          operacionModificar.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacionModificar.setIdTipoOperacion(request.getParameter("idTipoOperacion"));
          operacionModificar.setFecha(request.getParameter("fecha"));
          operacionModificar.setImporte(request.getParameter("importe"));
          //operacion.setEstatus("0");
          //operacion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacionModificar.update(con);
          operacionModificar.select(con);
          lanzarCalculo(con,operacionModificar.getFecha(),operacionModificar.getIdCuentaInversion());
          if(!operacionAnterior.getFecha().equals(operacionModificar.getFecha()) && !operacionAnterior.getIdOperacion().equals(operacionModificar.getIdOperacion()))
            lanzarCalculo(con,operacionAnterior.getFecha(),operacionAnterior.getIdCuentaInversion());
          
        }
      %>
      <%
      Connection con = null;
      String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
      String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
      String pagina = null;
      String mensaje= null;
      try  {
        int accion = Integer.parseInt(request.getParameter("accion"));
        con = DaoFactory.getTesoreria();
        con.setAutoCommit(false);
        switch(accion) {
          case 1: // Agregar
            insertarRegistro(con, request);
            mensaje = "Se insertó el registro de manera correcta";
            pagina = "'i002tesDepositosRetirosAgregar.jsp'";
            break;
          case 2: // Modificar
            modificarRegistro(con, request);
            mensaje = "Se modificó el registro de manera correcta";
            pagina = "'i002tesDepositosRetirosFiltro.jsp'";
            break;
        }
        con.commit();
        
      } catch (Exception ex)  {
        con.rollback();
        ex.printStackTrace();
        
        %>Ocurrio el siguiente error <br> <%=ex%><%
      } finally  {
        try  {
          if(con!=null)
            con.close();
          con = null;
        } catch (Exception ex)  {
          ex.printStackTrace();
        } 
      }
      
        
      
      %>
      <br>
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
            <%if(pagina!=null){%>
            <script type="text/javascript" language="javascript"> 
              regresarAPagina(<%=pagina%>);
            </script>
            <%} else {%>
            <td align="center"><input type="button" class="boton" value="Aceptar" onclick="regresarAPagina(<%=pagina%>)">
            <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
            <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>"></td>
            <%}%>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>