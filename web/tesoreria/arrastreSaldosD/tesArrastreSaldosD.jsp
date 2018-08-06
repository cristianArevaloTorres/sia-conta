<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesArrastreSaldosD</title>
   <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
     <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
      <script language="JavaScript"  type="text/javascript">
      function regresa(){
          document.getElementById('form1').action='../tesListadoProgramas.jsp';
          document.getElementById('form1').target='';
          document.getElementById('form1').submit();
      }
      
      function arrastraSaldos(){
          document.getElementById('form1').action='tesResultadoArrastreSD.jsp';
          document.getElementById('form1').submit();
      }
    </script>
     <%!
        private String getUltimaFechaCarga(String banco){
            String regresa = null;
            Map parametros = new HashMap(); 
            List <Vista> registro= null;
            parametros.put("idProg",banco); 
            SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
            try  {
              registro = sentenciasSE.registros("criterios.select.fechaUltimoSaldo.saldosDiarios",parametros);
              if (registro != null)
                regresa = registro.get(0).getField("FECHA");
              else
                regresa = null;
            }
            catch (Exception e)  {
              e.printStackTrace();
              regresa = null;
            } finally  {
              sentenciasSE = null;
            }
            return regresa;
         }
     %>
     <%
        String ultimaFechaBD = null;
        String banco = request.getParameter("idProgramaS").toString(); 
        if (!banco.equals("7"))
            ultimaFechaBD = getUltimaFechaCarga(banco);
        else 
            ultimaFechaBD = getUltimaFechaCarga("7,9");
    %>
  </head>
  <body>
    <form id="form1" name="form1" action="tesResultadoArrastreSD.jsp" method="POST">
    <%util.tituloPagina(out,"Tesorer�a","Saldos diarios - Bancos","Arrastre de saldos bancarios",true);%>
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
      <table width="50%" align="center" cellpadding="3" class="general">
        <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4" >Saldos bancarios</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="20%">Los �ltimos saldos registrados en la BD son del d�a: </td>
             <td width="3%"></td>
             <td width="15%"><%=ultimaFechaBD%></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="20%">Fecha �ltima para arrastre de saldos: </td>
             <td width="3%"></td>
             <td width="15%%">
             <input type="text" name="fechaFinal" id="fechaFinal" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaFinal')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
             </td>  
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
          <td><input type="button" class="boton" value="Arrastre de saldos" onclick="arrastraSaldos()"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresa()"></td>
        </tr>
        </tbody>
      </table>
    </form>  
  </body>
</html>