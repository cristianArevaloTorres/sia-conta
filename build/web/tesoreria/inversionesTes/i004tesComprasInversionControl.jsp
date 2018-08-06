<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.comprasInversion.bcRfTrComprasInversion" %>
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
    <title>i004tesComprasInversionControl</title>
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
    
  </head>
  <body onload="ajustarVentana()">
    <form id='form1' name='form1' action='' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Inversiones o compras","Control",true);%>
      <%!
        
      
      
        private String insertarOperacion(Connection con, HttpServletRequest request, int i) throws Exception {
          bcRfTrOperacionesInversion operacionInversion = new bcRfTrOperacionesInversion();
          operacionInversion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          operacionInversion.setIdTipoOperacion("3");//compra inversion
          operacionInversion.setFecha(request.getParameter("fecha"));
          operacionInversion.setImporte(request.getParameterValues("importe")[i]);
          operacionInversion.setEstatus("0"); TODO://duda
          operacionInversion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacionInversion.insert(con);
          return operacionInversion.getIdOperacion();
        }
        
        private double insertarDetalleOperacion(Connection con, HttpServletRequest request, String idCompraInversion, int i) throws Exception {
          String opracion = insertarOperacion(con,request,i);
          bcRfTrDetalleCompra detalleCompra = null;
          detalleCompra = new bcRfTrDetalleCompra();
          detalleCompra.setIdCompraInversion(idCompraInversion);
          detalleCompra.setIdOperacion(opracion);
          detalleCompra.setIdTipoValores(request.getParameterValues("idTipoValores")[i]);
          detalleCompra.setTitulosAcciones(request.getParameterValues("titulosAcciones")[i]);
          detalleCompra.setValor(request.getParameterValues("valor")[i]);
          detalleCompra.setImporte(request.getParameterValues("importe")[i]);
          detalleCompra.insert(con);
          return Double.parseDouble(detalleCompra.getImporte());
        }
        
        private double insertarDetallesOperacion(Connection con, HttpServletRequest request, String idCompraInversion) throws Exception {
          double importe = 0.00;
          String[] importes = request.getParameterValues("importe");
          for (int i = 0; i < importes.length; i++)  {
            importe += insertarDetalleOperacion(con,request,idCompraInversion,i);
          }
          return importe;
        }
      
        private bcRfTrComprasInversion insertarCompra(Connection con, HttpServletRequest request) throws Exception {
          double importe;
          bcRfTrComprasInversion comprasInversion = new bcRfTrComprasInversion();
          comprasInversion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          comprasInversion.setIdTipoInversion(request.getParameter("idTipoInversion"));
          
          comprasInversion.setFecha(request.getParameter("fecha"));
          comprasInversion.setPlazo(request.getParameter("plazo"));
          comprasInversion.setTasa(request.getParameter("tasa"));
          comprasInversion.setVencimiento(request.getParameter("vencimiento"));
          comprasInversion.setNumOperacion(request.getParameter("numOperacion"));
          comprasInversion.setEstatus("0");
          comprasInversion.insert(con);
          comprasInversion.select(con);
          importe = insertarDetallesOperacion(con, request, comprasInversion.getIdCompraInversion());
          comprasInversion.setImporteGlobal(String.valueOf(importe));
          comprasInversion.update(con);
          comprasInversion.select(con);
          return comprasInversion;
        }
        
        private void lanzarCalculo(Connection con, bcRfTrComprasInversion compraInversion) throws Exception {
          bcOperacionesInversionCalculo operacion = new bcOperacionesInversionCalculo();
          //compraInversion.select(con);
          operacion.lanzarCalculoDeFechaAnterior(con, compraInversion.getFecha(), compraInversion.getIdCuentaInversion());
        }
      
        private void insertarRegistro(Connection con, HttpServletRequest request) throws Exception {
          lanzarCalculo(con,insertarCompra(con,request));
        }
        
        private void actualizarOperacion(Connection con, HttpServletRequest request, int i,String idOperacion) throws Exception {
          bcRfTrOperacionesInversion operacionInversion = new bcRfTrOperacionesInversion();
          operacionInversion.setIdOperacion(idOperacion);
          operacionInversion.select(con);
          
          //operacionInversion.setIdCuentaInversion(request.getParameter("idCuentaInversion"));
          //operacionInversion.setIdTipoOperacion("3");//compra inversion
          operacionInversion.setFecha(request.getParameter("fecha"));
          operacionInversion.setImporte(request.getParameterValues("importe")[i]);
          //operacionInversion.setEstatus("0"); TODO://duda
          //operacionInversion.setFechaRegistro(Fecha.formatear(Fecha.FECHA_HORA,Fecha.getRegistroDate()));
          operacionInversion.update(con);
        }
        
        private double actualizarDetalleOperacion(Connection con, HttpServletRequest request, String idCompraInversion, int i) throws Exception {
          
          bcRfTrDetalleCompra detalleCompra = null;
          detalleCompra = new bcRfTrDetalleCompra();
          detalleCompra.setIdDetalleCompra(request.getParameterValues("idDetalleCompra")[i]);
          detalleCompra.select(con);
          
          actualizarOperacion(con,request,i,detalleCompra.getIdOperacion());
          //detalleCompra.setIdCompraInversion(idCompraInversion);
          //detalleCompra.setIdOperacion(opracion);
          detalleCompra.setIdTipoValores(request.getParameterValues("idTipoValores")[i]);
          detalleCompra.setTitulosAcciones(request.getParameterValues("titulosAcciones")[i]);
          detalleCompra.setValor(request.getParameterValues("valor")[i]);
          detalleCompra.setImporte(request.getParameterValues("importe")[i]);
          detalleCompra.update(con);
          return Double.parseDouble(detalleCompra.getImporte());
        }
        
        private double actualizarDetallesOperacion(Connection con, HttpServletRequest request, String idCompraInversion) throws Exception {
          double importe = 0.00;
          String[] idDetalle = request.getParameterValues("idDetalleCompra");
          for (int i = 0; i < idDetalle.length; i++)  {
            if(idDetalle[i] != null && !idDetalle[i].equals(""))
              importe += actualizarDetalleOperacion(con,request,idCompraInversion,i);
            else
              importe += insertarDetalleOperacion(con,request,idCompraInversion,i);
          }
          return importe;
        }
        
        private bcRfTrComprasInversion actualizarCompra(Connection con, HttpServletRequest request) throws Exception {
          double importe;
          bcRfTrComprasInversion compraCambio = new bcRfTrComprasInversion();
          compraCambio.setIdCompraInversion(request.getParameter("idCompraInversion"));
          compraCambio.select(con);
          compraCambio.setIdTipoInversion(request.getParameter("idTipoInversion"));
          
          compraCambio.setFecha(request.getParameter("fecha"));
          compraCambio.setPlazo(request.getParameter("plazo"));
          compraCambio.setTasa(request.getParameter("tasa"));
          compraCambio.setVencimiento(request.getParameter("vencimiento"));
          compraCambio.setNumOperacion(request.getParameter("numOperacion"));
          //compraCambio.setEstatus("1");
          compraCambio.update(con);
          compraCambio.select(con);
          importe = actualizarDetallesOperacion(con, request, compraCambio.getIdCompraInversion());
          compraCambio.setImporteGlobal(String.valueOf(importe));
          compraCambio.update(con);
          return compraCambio;
        }
        
        private void eliminarRegistros(Connection con, HttpServletRequest request)  throws Exception {
        String[] acciones = request.getParameterValues("acciondet");
        bcRfTrDetalleCompra detalle = new bcRfTrDetalleCompra();
        bcRfTrOperacionesInversion operacion = new bcRfTrOperacionesInversion();
        for (int i = 0; i < acciones.length; i++)  {
            if(acciones[i].equals("2")){
                detalle.setIdDetalleCompra(request.getParameterValues("idDetalleCompra")[i]);
                detalle.select(con);
                operacion.setIdOperacion(detalle.getIdOperacion());
                detalle.delete(con);
                operacion.delete(con);
            }
        
        }
        
          
          
        }
        
        private void modificarRegistro(Connection con, HttpServletRequest request) throws Exception {
          bcRfTrComprasInversion compraAnterior = new bcRfTrComprasInversion();
          compraAnterior.setIdCompraInversion(request.getParameter("idCompraInversion"));
          compraAnterior.select(con);
          bcRfTrComprasInversion compraModificacion = actualizarCompra(con,request);
          compraModificacion.select(con);
          eliminarRegistros(con,request);
          lanzarCalculo(con,compraModificacion);
          if(!compraModificacion.getFecha().equals(compraAnterior.getFecha())) {
            //compraAnterior.select(con);
            lanzarCalculo(con,compraAnterior);
          }
        }
      %>
      <%
      Connection con = null;
      String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
      String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
      String pagina=null;
      String mensaje = null;
      try  {
        int accion = Integer.parseInt(request.getParameter("accion"));
        con = DaoFactory.getTesoreria();
        con.setAutoCommit(false);
        switch(accion) {
          case 1: // Agregar
            insertarRegistro(con, request);
            mensaje = "Se insertó el registro de manera correcta";
            pagina = "'i004tesComprasInversionAgregar.jsp'";
            break;
          case 2: // Modificar
            modificarRegistro(con, request);
            mensaje = "Se modificó el registro de manera correcta";
            pagina = "'i004tesComprasInversionFiltro.jsp'";
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
      
        
      
      %><br>
      <br>
      
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