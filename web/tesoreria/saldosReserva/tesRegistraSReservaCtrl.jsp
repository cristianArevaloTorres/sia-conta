<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.sql.Connection" %>
<%@ page import=" sia.rf.tesoreria.saldoReserva.acciones.LeeArchivoSaldosR" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesRegistraSReservaCtrl</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
     <script language="JavaScript"  type="text/javascript">
      function regresa(){
          document.getElementById('form1').action='../tesListadoProgramas.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
      }

    </script>
  </head>
   <%
    String banco = request.getParameter("idProgramaS").toString();  
    String fechaACargar = null;
    boolean cargaOK = false;
    LeeArchivoSaldosR leeArchivoSaldosR = new LeeArchivoSaldosR((String)session.getAttribute("ruta"),(String)session.getAttribute("nombre"));
    Connection conn = null;
    try  {
      conn = DaoFactory.getTesoreria();
      conn.setAutoCommit(false);
      if (banco.equals("10")){
        if (leeArchivoSaldosR.leeArchivoStdr(conn)){
            conn.commit();
            cargaOK = true;
        }
        else{
            conn.rollback();
            cargaOK = false;
        }
      }
      else{
        if (banco.equals("6") || banco.equals("8") || banco.equals("11") || banco.equals("12"))
            fechaACargar = request.getParameter("fechaCarga").toString();   
        if (leeArchivoSaldosR.leeDivideArchivo(conn,fechaACargar,banco)){
            conn.commit();
            cargaOK = true;
        }
        else{
            conn.rollback();
            cargaOK = false;
        }
      }
    } catch (Exception e)  {
        e.printStackTrace();
        conn.rollback();
        cargaOK = false;
    } finally  {
      try  {
        if (conn != null)
          conn.close();
        conn = null;  
      } catch (Exception e)  {
            e.printStackTrace();
      }
    }
   %>
  <body>
     <form id="form1" name="form1" action="../tesListadoProgramas.jsp" method="POST">
     <%util.tituloPagina(out,"Tesorería","Carga de saldos reserva","Resultado de la carga de saldos",true);%>
     <input type="hidden" id="nombre" name="nombre" value="<%=(String)session.getAttribute("nombre")%>">
     <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
     <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
     <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
     <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
     <table>
     <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
           </tr>
        </tbody>
     </table>
         <br>
         <br>
         <table align="center">
           <thead></thead>
           <tbody>
             <tr align="center">
               <td style="font-size:large; color:rgb(0,99,148)">Estatus de la importación</td>
             </tr>
             <tr>
               <td></td>
             </tr>
             <tr align="center">
               <td style="font-size:small; font-style:italic; font-weight:normal"><%=cargaOK?"Todos los registros del archivo se han importado correctamente.":"Error al hacer el registro de saldos, favor de revisar la información del archivo"%></td>
             </tr>
           </tbody>
         </table> 
         <br>
         <hr class="piePagina">
         <br>
         <table align="center">
           <thead></thead>
           <tbody>
             <tr>
               <td><input type="button" class="boton" value="Aceptar" onclick="regresa()"></td>
             </tr>
           </tbody>
         </table>
     </form>
  </body>
</html>