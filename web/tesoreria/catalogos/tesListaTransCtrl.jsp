<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsDetalleTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsMovConTrans" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsPeriodoAnt" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsPeriodoDes" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsMaxFechaAnt" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsMaxFechaAntSP" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaTransCtrl</title>
       <script language="javascript" type="text/javascript">
    
      function ir(paginaLis) {
        if(paginaLis!='' && paginaLis!='null') {
          document.getElementById('form1').action = paginaLis;
          document.getElementById('form1').submit();
        }
      }
      
    </script>
  </head>
    <%!
  
    public boolean ejecutaSentencia(String sentenciaEjecutar,Connection conn){
      boolean regresa = false;
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,sentenciaEjecutar);
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
        String mensaje = null;
        boolean ejecuto = false;
        String clave = null;
        String numPAgina = null;
        try  {
          clave = request.getParameter("clave");
          numPAgina = request.getParameter("param");
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);
          crsDetalleTrans.addParamVal("idClaveTrans",clave);
          crsDetalleTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.detalleTransaccion.catTransacciones");
          crsDetalleTrans.next();
          
          crsMovConTrans.addParamVal("idClaveTrans",clave);
          crsMovConTrans.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.existenMov.catTransacciones");
          crsMovConTrans.next();
          if(crsMovConTrans.getStr("cuantos").equals("0")){
            if(crsDetalleTrans.getStr("id_tipo_aplica").equals("C")){
                ejecuto = ejecutaSentencia("delete from RF_TC_COSTOS_TRANSACCION where id_clave_trans = ".concat(clave), con);
                if (ejecuto){
                    ejecuto = ejecutaSentencia("delete from RF_TC_CLAVES_TRANSACCION where id_clave_trans = ".concat(clave), con);
                    if (ejecuto){ 
                       con.commit();
                        mensaje = "";
                    }
                    else{
                        if(con!=null)
                            con.rollback();
                        mensaje = "Error al eliminar la clave de transacción seleccionada";
                    }
                }
                else{
                    if(con!=null)
                        con.rollback();
                    mensaje = "Error al eliminar costos de la comisión a borrar";
                }
            }
            else{
              ejecuto = ejecutaSentencia("delete from RF_TC_CLAVES_TRANSACCION where id_clave_trans = ".concat(clave), con);
                if (ejecuto){ 
                    con.commit();
                    mensaje = "";
                }
                else{
                    if(con!=null)
                        con.rollback();
                    mensaje = "Error al eliminar la clave de transacción seleccionada";
                }
            }
          }
          else{
            if(con!=null)
                con.rollback();
            mensaje = "No se puede eliminar la transacción debido a que tiene movimientos bancarios relacionados";
          }
        } catch (Exception ex)  {
            ex.printStackTrace();
        } finally  {
            DaoFactory.closeConnection(con);
            con = null;
        }
        
  %>  
  <body onload="ir('tesListaTransacciones.jsp')">
     <form id="form1" name="form1"  method="POST">
       <input type="hidden" name="param" id="param" value="<%=request.getParameter("param")%>"/>  
       <input type="hidden" id="mensaje" name="mensaje" value="<%=mensaje%>">
     </form>
  </body>
</html>