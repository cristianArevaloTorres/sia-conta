<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="java.sql.*"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="bcRfTrMovimientosCuenta" class="sia.rf.tesoreria.registro.movimientos.bcRfTrMovimientosCuenta" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesModificaIdClave</title>
    <script language="JavaScript"  type="text/javascript">
     
       function ir(){
           document.getElementById('form1').action='tesListaTransacciones.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       }

    </script>
  </head>
  <%!
    public String getClaveMod(String idClave){
        String regresa = null;
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        Map parametros = new HashMap();
        List<Vista> registros = null;
        try  {
            parametros.put("idClaveMod",idClave);
            registros = sentenciasSE.registros("criterios.select.obtenerClaveTrans.modClaveTrans",parametros);
            if (registros!=null){
                regresa = registros.get(0).getField("CLAVE_TRANS");
            }
        } catch (Exception e)  {
            e.printStackTrace();
            regresa = "''"; 
        } finally  {
            sentenciasSE = null;
            registros = null;
        }
        return regresa; 
    }
  %>
  <%
    Connection conn = null;
    String[] clave = null;
    String idClaveMod = null;
    String claveMod = null;
    
    try  { 
        conn = DaoFactory.getTesoreria();
        conn.setAutoCommit(false);
                
        clave = request.getParameter("clave").split(",");
        bcRfTrMovimientosCuenta.setIdMovimiento(clave[0]);
        bcRfTrMovimientosCuenta.setIdCuenta(clave[1]);
        bcRfTrMovimientosCuenta.select(DaoFactory.CONEXION_TESORERIA);
        
        idClaveMod = request.getParameter("nuevaClave");
        claveMod = getClaveMod(idClaveMod);
        bcRfTrMovimientosCuenta.setIdClaveTransRecla(idClaveMod);
        bcRfTrMovimientosCuenta.setClaveTransRecla(claveMod);
        bcRfTrMovimientosCuenta.update(conn);
        conn.commit();
        
        
    } catch (Exception ex)  {
        ex.printStackTrace();
        conn.rollback();
    } finally  {
        DaoFactory.closeConnection(conn);
    }
    
  %>
   <body onload="ir()">
   <form id="form1" name="form1">
    <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
    <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
    <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
    <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
    <input type="hidden" name="fechaInicio" id="fechaInicio" value="<%=request.getParameter("fechaInicio")%>"/>
    <input type="hidden" name="fechaFinal" id="fechaFinal" value="<%=request.getParameter("fechaFinal")%>"/>
    <input type="hidden" id="ctaSelec" name="ctaSelec" value="<%=request.getParameter("ctaSelec")%>"/>
   </form>
  </body>
</html>