<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesModTransaccionCtrl</title>
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
   
     public boolean updateDtos(String idCveSelc, HttpServletRequest request,Connection conn){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"update RF_TC_CLAVES_TRANSACCION set descripcion='".concat(request.getParameter("descripcion")).concat("',ID_CLAVE_TB=").concat(request.getParameter("claveTb")).concat(",VIGENCIA_INI=to_date('").concat(request.getParameter("fecha")).concat("','dd/mm/yyyy') where ID_CLAVE_TRANS=").concat(request.getParameter("clave")));
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
    
    
     public boolean updateCosto(String idCveSelc, String nvoCosto,Connection conn){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"update RF_TC_COSTOS_TRANSACCION set COSTO=".concat(nvoCosto).concat(" where ID_CLAVE_TRANS=").concat(idCveSelc));
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
        String pagina     = "tesListaTransacciones.jsp";
        String mensaje = null;
        int ejecuto = -1;
        String clave = null;
        clave = request.getParameter("clave");
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);  
          if (request.getParameter("tipoTrans").equals("C")){
            if (updateCosto(clave,request.getParameter("costo"),con)) {
                if (updateDtos(clave,request,con)){
                    con.commit();
                    mensaje = "";
                    pagina = "tesListaTransacciones.jsp"; 
                }
                else{
                    if(con!=null)
                        con.rollback();
                    mensaje = "Error al actualizar los datos de la clave de transacción";
                    pagina = "tesModTransaccion.jsp"; 
                }
            }
            else {
                if(con!=null)
                    con.rollback();
                mensaje = "Error actualizar el costo de la transacción seleccionada ";
                pagina = "tesModTransaccion.jsp"; 
            }
          }
          else{
              if (updateDtos(clave,request,con)){
                 con.commit();
                 mensaje = "";
                 pagina = "tesListaTransacciones.jsp"; 
              }
              else{
                 if(con!=null)
                    con.rollback();
                 mensaje = "Error al cerrar la bitácora de aclaraciones";
                 pagina = "tesModTransaccion.jsp"; 
              }
            }
        } finally  {
          DaoFactory.closeConnection(con);
          con = null;
        }
        
  %>  
  <body onload="ir('<%=pagina%>')">
     <form id="form1" name="form1"  method="POST">
        <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
        <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
        <input type="hidden" id="param" name="param" value="<%=request.getParameter("param")%>">  
     </form>
  </body>
</html>