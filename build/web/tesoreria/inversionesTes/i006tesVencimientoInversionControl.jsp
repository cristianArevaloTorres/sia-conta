
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.operacionesInversion.*" %>
<%@ page import="sia.rf.tesoreria.registro.comprasInversion.bcRfTrComprasInversion" %>
<%@ page import="sia.rf.tesoreria.registro.detalleCompra.bcRfTrDetalleCompra" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="java.sql.*" %>
<%@ page import="sia.db.sql.*" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i006tesVencimientoInversionControl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
     function ir(pagina) {
       if(pagina!='' && pagina!='null') {
          form1.action = pagina
          form1.submit();
       }
     }
    </script>
  </head>
  <%!
      
        private String insertarOperacion(Connection con, HttpServletRequest request, int tipoOperacion, int i) throws Exception {
          bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
          operacion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacion.setIdTipoOperacion(String.valueOf(tipoOperacion));
          operacion.setFecha(request.getParameter("vencimiento"));
          operacion.setImporte(request.getParameterValues(tipoOperacion==4?"importe":"rendimiento")[i]);
          operacion.setEstatus("0");
          operacion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacion.insert(con);
          return operacion.getIdOperacion();
        }
        
        private String modificarOperacion(Connection con, HttpServletRequest request, int tipoOperacion, String idOperacion, int i) throws Exception {
          bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
          operacion.setIdOperacion(idOperacion);
          operacion.select(con);
          
          operacion.setFecha(request.getParameter("vencimiento"));
          operacion.setImporte(request.getParameterValues(tipoOperacion==4?"importe":"rendimiento")[i]);
          operacion.update(con);
          return operacion.getIdOperacion();
        }
        
        private void lanzarCalculo(Connection con, HttpServletRequest request) throws Exception {
          
          bcOperacionesInversionCalculo operacion = new bcOperacionesInversionCalculo();
          //compraInversion.select(con);
          operacion.lanzarCalculoDeFechaAnterior(con, request.getParameter("vencimiento"), request.getParameter("idCuentaInversion"));
        }
        
        private void cambiaEstatus(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrComprasInversion compra = new bcRfTrComprasInversion();
          compra.setIdCompraInversion(request.getParameter("idCompraInversion"));
          compra.select(con);
          compra.setEstatus("1");
          compra.update(con);
        }
      
        private void insertarRegistro(Connection con, HttpServletRequest request) throws Exception {
          String[] idDetalleCompra = request.getParameterValues("idDetalleCompra");
          bcRfTrDetalleCompra detalleCompra = new bcRfTrDetalleCompra();
          for (int i = 0; i < idDetalleCompra.length; i++)  {
            detalleCompra.setIdDetalleCompra(idDetalleCompra[i]);
            detalleCompra.select(con);
            if(request.getParameter("hisSociedad").equals("true"))
              detalleCompra.setValorAccionVenta(request.getParameterValues("valorAccionVenta")[i]);
            detalleCompra.setRendimiento(request.getParameterValues("rendimiento")[i]);
            detalleCompra.setIdOperacionRecupera(insertarOperacion(con, request, 4, i));
            detalleCompra.setIdOperacionRend(insertarOperacion(con, request, 7, i));
            detalleCompra.update(con);
          }
          cambiaEstatus(con,request);
          lanzarCalculo(con,request);
        }
        
        
        private void modificarRegistro(Connection con, HttpServletRequest request) throws Exception {
          String[] idDetalleCompra = request.getParameterValues("idDetalleCompra");
          bcRfTrDetalleCompra detalleCompra = new bcRfTrDetalleCompra();
          for (int i = 0; i < idDetalleCompra.length; i++)  {
            detalleCompra.setIdDetalleCompra(idDetalleCompra[i]);
            detalleCompra.select(con);
            if(request.getParameter("hisSociedad").equals("true"))
              detalleCompra.setValorAccionVenta(request.getParameterValues("valorAccionVenta")[i]);
            detalleCompra.setRendimiento(request.getParameterValues("rendimiento")[i]);
            detalleCompra.setIdOperacionRecupera(modificarOperacion(con, request, 4, detalleCompra.getIdOperacionRecupera(), i));
            detalleCompra.setIdOperacionRend(modificarOperacion(con, request, 7, detalleCompra.getIdOperacionRend(), i));
            detalleCompra.update(con);
          }
          //cambiaEstatus(con,request);
          lanzarCalculo(con,request);
        }
      %>
      <%
      Connection con            = null;
      String ffecha             = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
      String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
      String festatus           = request.getParameter("festatus")==null ? "" : request.getParameter("festatus");
      String estatus            = request.getParameter("estatus")==null ? "" : request.getParameter("estatus");
      String pagina=null;
      String mensaje=null;
      try  {
        con = DaoFactory.getTesoreria();
        con.setAutoCommit(false);
        switch(Integer.parseInt(estatus)) {
          case 0: // Agregar
            insertarRegistro(con, request);
            pagina = "'i006tesVencimientoInversionFiltro.jsp'";
            mensaje="Se realizó el vencimiento de la compra de manera correcta";
            break;
          case 1: // Modificar
            modificarRegistro(con, request);
            pagina = "'i006tesVencimientoInversionFiltro.jsp'";
            mensaje="Se realizó la modificacion del vencimiento de la compra de manera correcta";
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
      <%util.tituloPagina(out,"Tesorería central","Vencimiento de inversiones o compras","Control",true);%>
      
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
            <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>">
            <input type="hidden" id="festatus" name="festatus" value="<%=festatus%>"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>