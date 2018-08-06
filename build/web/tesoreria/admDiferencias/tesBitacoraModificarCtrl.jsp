<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.libs.formato.Fecha"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesBitacoraModificarCtrl</title>
       <script language="javascript" type="text/javascript">
    
      function ir(pagina) {
        if(pagina!='' && pagina!='null') {
          document.getElementById('form1').action = pagina;
          document.getElementById('form1').submit();
        }
      }
      
    </script>
  </head>
    <%!
    
  
     public boolean updateDtos(HttpServletRequest request,String idCtaSelc,String idSaldoSelec,Connection conn){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"update RF_TR_SEGUIMIENTO_DIFERENCIAS set OBSERVACIONES='".concat(request.getParameter("observaciones")).concat("', FECHA=to_date('").concat(request.getParameter("fecha")).concat("','dd/mm/yyyy') where id_cuenta=").concat(idCtaSelc).concat(" and id_saldo_diario=").concat(idSaldoSelec).concat(" and ID_SEGUIMIENTO=").concat(request.getParameter("idSeguimiento")));
        if (ejecuto!=-1)
          regresa = true;
        else 
          regresa =false;
      } catch (Exception e)  {
            e.printStackTrace();
            regresa = false; 
        } finally  {
            sentenciasSE = null;
          }
      return regresa; 
    }
   
    
  %>
  <%
        Connection con    = null;
        String pagina     = "tesBitacoraAclaracion.jsp";
        String mensaje = null;
        int ejecuto = -1;
        String[] clave = null;
        clave = request.getParameter("clave").split(",");
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);  
          if (updateDtos(request,clave[0],clave[1],con)){
               con.commit();
               pagina = "tesBitacoraAclaracion.jsp"; 
          }
          else{
             if(con!=null)
                    con.rollback();
              mensaje = "Error al modificar la bitácora de aclaraciones";
          }
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
  %>  
  <body onload="ir('<%=pagina%>')">
     <form id="form1" name="form1"  method="POST">
        <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
        <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
        <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
        <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
        <input type="hidden" id="estatusDef" name="estatusDef" value="<%=request.getParameter("idEstatus")%>">
        <input type="hidden" id="estatusDif" name="estatusDif" value="<%=request.getParameter("idEstatus")%>">
        <input type="hidden" id="idEstatus" name="idEstatus" value="<%=request.getParameter("idEstatus")%>">
        <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
        <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
        <input type="hidden" id="idSeguimiento" name="idSeguimiento" value="<%=request.getParameter("idSeguimiento")%>">  
     </form>
  </body>
</html>