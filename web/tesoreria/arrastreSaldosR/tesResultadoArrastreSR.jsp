<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.sql.Connection" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesResultadoArrastreSR</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript"  type="text/javascript">
      function regresa(){
          document.getElementById('form1').action='../tesListadoProgramas.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
      }
    </script>
  </head>
  <%!
    private boolean ejecutaArrastreSaldos(int iProg, String fecha, Connection conn){
        boolean regresa = false;
        CallableStatement st = null;
        StringBuffer sbProceso = new StringBuffer();
        sbProceso.append("begin TSR.RELLENA_DIAS_SIN_SALDOSRES(");
        sbProceso.append(iProg);
        sbProceso.append(", to_date('");
        sbProceso.append(fecha);
        sbProceso.append("','dd/mm/yyyy')); end;");
        try {
            st = conn.prepareCall(sbProceso.toString());
            st.execute();
            regresa = true;
        } catch (Exception e)  { 
            e.printStackTrace();
            regresa = false;
        } 
        return regresa;
    }
  %>
  <%
    String banco = request.getParameter("idProgramaS").toString();  
    Connection conn = null;
    boolean arrastre = false; 
    try  {
      conn = DaoFactory.getTesoreria();
      conn.setAutoCommit(false);   
      if (ejecutaArrastreSaldos(Integer.parseInt(banco),request.getParameter("fechaFinal").toString(),conn)){
            conn.commit();
            arrastre = true;
      }
      else{
            conn.rollback();
            arrastre = false;
      }
    } catch (Exception e)  {
        e.printStackTrace();
        conn.rollback();
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
    <%util.tituloPagina(out,"Tesorería","Saldos de reseva - Bancos","Arrastre de saldos bancarios",true);%>
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
      <table align="center">
        <thead></thead>
         <tbody>
             <tr align="center">
               <td style="font-size:large; color:rgb(0,99,148)">Estatus del arrastre de saldos</td>
             </tr>
             <tr>
               <td></td>
             </tr>
             <tr align="center">
               <td style="font-size:small; font-style:italic; font-weight:normal"><%=arrastre?"El registro del arrastre de saldos se efectuo correctamente":"Error en proceso almacenado de arrastre de saldos"%></td>
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