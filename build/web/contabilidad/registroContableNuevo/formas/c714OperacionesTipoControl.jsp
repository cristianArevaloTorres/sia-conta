<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.exceptions.contabilidad.formas.OperacionesTipoError" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="maestro" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcMaestroOperaciones" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

 

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c714OperacionesTipoControl</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      function envia(pagina) {
        forma.action = pagina;
        forma.submit();
      }
    </script>
  </head>
  <body>
  <%util.tituloPagina(out,"Contabilidad institucional","Formas contables","Control",true);%>
  <%! 
  
  
    public void crearDetalleOperaciones(Connection con, HttpServletRequest request, bcRfTcMaestroOperaciones maestro, int reg, String[] cuentas) throws Exception {
      bcRfTcDetalleOperaciones det = null;
      try  {
       
        det = new bcRfTcDetalleOperaciones();
        det.setOperacionContableId(request.getParameter("chbTipoOper"+cuentas[reg]));
        if(request.getParameterValues("hdIdCuenta")[reg]==null || request.getParameterValues("hdIdCuenta")[reg].equals(""))
          throw new Exception("No se puede registrar o hacer la modificación ya que el sistema no obtubo de manera correcta el numero de cuenta contable");
        det.setCuentaContableId(request.getParameterValues("hdIdCuenta")[reg]);
        det.setMaestroOperacionId(maestro.getMaestroOperacionId());
        
        det.insert(con);

      } catch (Exception ex)  {
        throw new Exception("Error al generar la secuencia",ex);
      } finally  {
      }
      
    }
    

    
    public void modificarDetalleOperaciones(Connection con, HttpServletRequest request, bcRfTcMaestroOperaciones forma, int reg, String[] cuentas, String detalleOperacionId) throws Exception  {
      bcRfTcDetalleOperaciones det = null;
      try  {
        det = new bcRfTcDetalleOperaciones();
        det.setDetalleOperacionId(detalleOperacionId);
        det.select(con);
        
        det.setOperacionContableId(request.getParameter("chbTipoOper"+cuentas[reg]));
        
        det.update(con);
        
      } catch (Exception ex)  {
        throw new Exception("Error al modificar la secuencia",ex);
      } finally  {
        det = null;
      }
    }
    
    public void crearModificarDetalle(Connection con, HttpServletRequest request, bcRfTcMaestroOperaciones forma) throws Exception {
      String[] cuentas = request.getParameter("registros").split(",");
      String[] creados = request.getParameterValues("hdCrea");
      for (int i = 0; i < cuentas.length; i++)  {
        if(creados[i].equals("1"))
          crearDetalleOperaciones(con, request,forma,i, cuentas);
        else
          modificarDetalleOperaciones(con, request,forma,i, cuentas, creados[i]);
      }
    }
    
    public void eliminarDetalles(Connection con, HttpServletRequest request) throws Exception {
      String[] eliminados = request.getParameter("regEliminar").split(",");
      bcRfTcDetalleOperaciones detalle = new bcRfTcDetalleOperaciones();
      for(String eliminado : eliminados) {
        if(!eliminado.trim().equals("")) {
          detalle.setDetalleOperacionId(eliminado);
          detalle.delete(con);
        }
      }
    }
  %>
  <%
    String pagina  = null;
    boolean error  = false;
    String mensaje = null;
    Connection con = null;
    try  {
    
      con = DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
      con.setAutoCommit(false);
      if(request.getParameter("maestroOperacionId") !=null ){
          maestro.setMaestroOperacionId(request.getParameter("maestroOperacionId"));
          maestro.select(con);  
      }
      String consecutivo = request.getParameter("hdconsecutivo") != null ? request.getParameter("hdconsecutivo"): "";
      String descripcion = request.getParameter("hddescripcion") != null ? request.getParameter("hddescripcion"): "";

%>
    <form id="forma" name="forma">
    <jsp:setProperty name="maestro" property="*" />
    <input type="hidden" name="accion" id="accion" value="<%=request.getParameter("accion")%>">
    <input type="hidden" name="hdconsecutivo" id="hdconsecutivo" value="<%=consecutivo%>">
    <input type="hidden" name="hddescripcion" id="hddescripcion" value="<%=descripcion%>">
    
    <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
        </tr>
      </table>
      <br/>
      
      <br/>
<%   
      
      switch(Integer.parseInt(request.getParameter("accion"))) {
        case 1: pagina="c714OperacionesTipoAgregar.jsp";
                maestro.insert(con); //INSERTA FORMA PADRE
                //INSERTA SECUENCIA FORMA
                String[] cuentas = request.getParameter("registros").split(",");
                for (int i = 0; i < cuentas.length; i++)  {
                  crearDetalleOperaciones(con, request,maestro,i, cuentas);
                }
                mensaje= "Se di&oacute; de alta con éxito la operación contable No. "+maestro.getConsecutivo();
                break;
                
        case 3: pagina = "c714OperacionesTipoResultado.jsp";
                maestro.setMaestroOperacionId(request.getParameter("clave"));
                maestro.borraEnCascada(con);
                mensaje = "Se borr&oacute; con éxito la operación contable No. "+maestro.getConsecutivo();
                break;
        case 2: pagina = "c714OperacionesTipoResultado.jsp";
                eliminarDetalles(con, request);
                maestro.update(con);
                crearModificarDetalle(con, request,maestro);
                mensaje = "Se modific&oacute; con éxito la operación contable "+maestro.getConsecutivo();
                
                break;
      }
      con.commit();
    } catch (Exception ex)  {
      error = true;
      ex.printStackTrace();
      DaoFactory.rollback(con);
      new OperacionesTipoError(ex);
      %><br><br>
      
      
      <%
    } finally  {
      DaoFactory.closeConnection(con);
      con = null;
    }
    
    if(error){
  %>
                  <table align="center">
                    <thead></thead>
                    <tbody>
                      <tr>
                        <td>Ocurrio un error en la base de datos. <br> Favor de comunicarlo al administrador.</td>
                      </tr>
                    </tbody>
                  </table>
                <%}else {%>
                <table width="70%" align="center">
                  <thead></thead>
                  <tr>
                    <td class="azulCla" align="center"><%=mensaje%></td>
                  </tr>
                </table>
  <%}%>
                <br/>
                <hr class="piePagina">
                <table align="center">
                  <thead></thead>
                  <tr>
                    <td><input type="button" class="boton" value="regresar" onclick="envia('<%=pagina%>')"></td>
                  </tr>
                </table>
    
    </form>
  </body>
</html>