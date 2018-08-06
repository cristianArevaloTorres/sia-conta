<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.beans.seguridad.Autentifica" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesRegistraActividadCtrl</title>
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
   
     public boolean insertaDatos(StringBuffer listaCampos, StringBuffer listaValores, Connection conn, String tabla){
      boolean regresa = false; 
      int ejecuto = -1;
      SentennciasSE sentenciasSE = null;
      try  {
        sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        ejecuto = sentenciasSE.ejecutar(conn,"insert into ".concat(tabla).concat(" (").concat(listaCampos.toString()).concat(") values (").concat(listaValores.toString()).concat(")"));
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
   
    public String getIdSeguimiento(String idCta, String idSdoD){  
       SentenciasCRS sen = null;
       String regresa = null;
       try  {
            sen = new SentenciasCRS();
            sen.addParam("idCuenta",idCta);      
            sen.addParam("idSaldoD",idSdoD);      
            sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.idSeguimientoAct.diferenciaSaldo");
            if(sen.next())
              regresa = sen.getString("ID_SEGUIMIENTO");
        } catch (Exception e)  {
            e.printStackTrace();
        }  finally  {
            sen = null;
        }
        return regresa;
    }
   
    public boolean registraAclaracion(Connection conn, HttpServletRequest request){
      boolean regresa = false;
      String mensaje = " ";
      String[] clave = null;
      StringBuffer listaValores = new StringBuffer();
      StringBuffer listaCampos = new StringBuffer();
      Autentifica aut = (Autentifica)request.getSession().getAttribute("Autentifica");
      clave = request.getParameter("clave").split(",");
      try  {
       
        listaValores.append(clave[0].concat(","));
        listaCampos.append("id_cuenta,");
        listaValores.append(clave[1].concat(","));
        listaCampos.append("id_saldo_diario,");
        listaValores.append(getIdSeguimiento(clave[0],clave[1]).concat(","));
        listaCampos.append("id_seguimiento,");
        listaValores.append("'".concat(request.getParameter("observaciones")).concat("',"));
        listaCampos.append("observaciones,");
        listaValores.append("to_date('".concat(request.getParameter("fecha")).concat("','dd/mm/yyyy'),"));
        listaCampos.append("fecha,");
        listaValores.append(aut.getNumeroEmpleado());
        listaValores.append(",");
        listaCampos.append("num_empleado,");
        listaValores.append(request.getParameter("idEstatus"));
        listaCampos.append("id_estatus_dif");
        regresa = insertaDatos(listaCampos,listaValores, conn, "RF_TR_SEGUIMIENTO_DIFERENCIAS");
      } catch (Exception ex)  {
          ex.printStackTrace();
          regresa = false; 
      }
      return regresa; 
    }
  %>
  <%
        Connection con    = null;
        String pagina     = "tesBitacoraAclaracion.jsp";
        String mensaje = null;
        int ejecuto = -1;
        try  {
          con = DaoFactory.getTesoreria();
          con.setAutoCommit(false);  
          if(registraAclaracion(con, request)){
               con.commit();
               pagina = "tesBitacoraAclaracion.jsp"; 
            }
         else{
               pagina     = "tesRegistraActividades.jsp";
               if(con!=null)
                    con.rollback();
               mensaje = "Error al registrar en la bitacora de actividades para la diferencia en saldo";
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
     </form>
  </body>
</html>