
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.efectivoInversion.*" %>
<%@ page import="sia.rf.tesoreria.registro.operacionesInversion.*" %>
<%@ page import="sia.rf.tesoreria.registro.detalleCompra.bcRfTrDetalleCompra" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="java.sql.*" %>
<%@ page import="sia.db.sql.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i005tesEfectivoInversionControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      function ir(pagina) {
        if(pagina!='' && pagina!='null') {
          document.getElementById("form1").action=pagina;
          document.getElementById("form1").submit();
        } 
      }
    </script>
  </head>
        <%!
      
        private String insertarOperacion(Connection con, HttpServletRequest request, String idTipoOperacion,String fecha, String importe) throws Exception {
          bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
          operacion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacion.setIdTipoOperacion(idTipoOperacion);//Inversion de efectivo
          operacion.setFecha(fecha);
          operacion.setImporte(importe);
          operacion.setEstatus("0");
          operacion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacion.insert(con);
          return operacion.getIdOperacion();
          
        }
      
        private void insertarRegistro(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrEfectivoInversion efectivo = new bcRfTrEfectivoInversion();
          efectivo.setFecha(request.getParameter("fecha"));
          efectivo.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          efectivo.setImporte(request.getParameter("importe"));
          efectivo.setIdComprasInversion(request.getParameter("idCompraInversion"));
          efectivo.setIdOperacionDeposito(insertarOperacion(con,request,"5",request.getParameter("fecha"),request.getParameter("importe")));
          efectivo.insert(con);
          efectivo.select(con);
          lanzarCalculo(con,efectivo.getIdCuentaInversion(),efectivo.getFecha());
        }
        
        private void lanzarCalculo(Connection con, String idCuentaInversion, String fecha) throws Exception {
          
          bcOperacionesInversionCalculo operacion = new bcOperacionesInversionCalculo();
          //compraInversion.select(con);
          operacion.lanzarCalculoDeFechaAnterior(con, fecha, idCuentaInversion);
        }
        
        private void actualizarOperacion(Connection con, HttpServletRequest request,String idOperacion,String fecha,String importe) throws Exception {
          bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
          operacion.setIdOperacion(idOperacion);
          operacion.select(con);
          operacion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacion.setFecha(fecha);
          operacion.setImporte(importe);
          operacion.update(con);
        }
        
        private void modificarRegistro(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrEfectivoInversion efectivoAnterior = new bcRfTrEfectivoInversion();
          efectivoAnterior.setIdEfectivoInversion(request.getParameter("idEfectivoInversion"));
          efectivoAnterior.select(con);
          bcRfTrEfectivoInversion efectivoModificada = new bcRfTrEfectivoInversion();
          efectivoModificada.setIdEfectivoInversion(request.getParameter("idEfectivoInversion"));
          efectivoModificada.select(con);
          efectivoModificada.setFecha(request.getParameter("fecha"));
          efectivoModificada.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          efectivoModificada.setImporte(request.getParameter("importe"));
          efectivoModificada.setIdComprasInversion(request.getParameter("idCompraInversion"));
          actualizarOperacion(con,request,efectivoModificada.getIdOperacionDeposito(),efectivoModificada.getFecha(),efectivoModificada.getImporte());
          efectivoModificada.update(con);
          efectivoModificada.select(con);
          lanzarCalculo(con,efectivoModificada.getIdCuentaInversion(),efectivoModificada.getFecha());
          if(!efectivoModificada.getFecha().equals(efectivoAnterior.getFecha()))
            lanzarCalculo(con,efectivoAnterior.getIdCuentaInversion(),efectivoAnterior.getFecha());
        }
        
        private void registrarRendimiento(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrEfectivoInversion efectivo = new bcRfTrEfectivoInversion();
          efectivo.setIdEfectivoInversion(request.getParameter("idEfectivoInversion"));
          efectivo.select(con);
          efectivo.setVincimiento(request.getParameter("vincimiento"));
          efectivo.setRendimiento(request.getParameter("rendimiento"));
          efectivo.setIdOperacionRecupera(insertarOperacion(con,request,"6",request.getParameter("vincimiento"),request.getParameter("importe")));
          efectivo.setIdOperacion(insertarOperacion(con,request,"8",request.getParameter("vincimiento"),request.getParameter("rendimiento")));
          efectivo.update(con);
          efectivo.select(con);
          lanzarCalculo(con,efectivo.getIdCuentaInversion(),efectivo.getFecha());
        }
        
        private void modificarRendimiento(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrEfectivoInversion efectivoAnterior = new bcRfTrEfectivoInversion();
          efectivoAnterior.setIdEfectivoInversion(request.getParameter("idEfectivoInversion"));
          efectivoAnterior.select(con);
          bcRfTrEfectivoInversion efectivoModificada = new bcRfTrEfectivoInversion();
          efectivoModificada.setIdEfectivoInversion(request.getParameter("idEfectivoInversion"));
          efectivoModificada.select(con);
          efectivoModificada.setVincimiento(request.getParameter("vincimiento"));
          efectivoModificada.setRendimiento(request.getParameter("rendimiento"));
          actualizarOperacion(con,request,efectivoModificada.getIdOperacionRecupera(),request.getParameter("vincimiento"),request.getParameter("importe"));
          actualizarOperacion(con,request,efectivoModificada.getIdOperacion(),request.getParameter("vincimiento"),request.getParameter("rendimiento"));
          efectivoModificada.update(con);
          efectivoModificada.select(con);
          lanzarCalculo(con,efectivoModificada.getIdCuentaInversion(),efectivoModificada.getFecha());
          if(!efectivoModificada.getFecha().equals(efectivoAnterior.getFecha()))
            lanzarCalculo(con,efectivoAnterior.getIdCuentaInversion(),efectivoAnterior.getFecha());
        }
      %>
      <%
      Connection con            = null;
      String ffecha             = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
      String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
      Integer accion            = request.getParameter("accion")==null ? 0 : Integer.parseInt(request.getParameter("accion"));
      String pagina=null;
      String mensaje=null;
      try  {
        con = DaoFactory.getTesoreria();
        con.setAutoCommit(false);
        switch((accion)) {
          case 0: // Agregar inicio de deposito
            insertarRegistro(con, request);
            pagina = "'i005tesEfectivoInversionAgregar.jsp'";
            mensaje="Se realizó la inversion de efectivo de manera correcta";
            break;
          case 1: // Modificar inicio de deposito
            modificarRegistro(con, request);
            pagina = "'i005tesEfectivoInversionFiltro.jsp'";
            mensaje="Se realizó la modificación de manera correcta";
            break;
          case 2: // Registrar rendimiento de efectivo
            registrarRendimiento(con, request);
            pagina = "'i005tesEfectivoInversionFiltro.jsp'";
            mensaje="Se realizó el vencimiento de manera correcta";
            break;
          case 3: // Modificar rendimiento de efectivo
            modificarRendimiento(con, request);
            pagina = "'i005tesEfectivoInversionFiltro.jsp'";
            mensaje="Se realizó la modificación del vencimiento de manera correcta";
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
  <body onload="ir(<%=pagina%>)">
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversion de efectivo","Control",true);%>

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
            <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>