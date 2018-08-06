<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="crsDetBitacora" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesBitacoraModificar</title>
   <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
    
       function regresa(){
          document.getElementById('form1').action='tesBitacoraAclaracion.jsp';
          document.getElementById('form1').submit();
       }
       
       
      function ir() {
          document.getElementById('form1').action = 'tesBitacoraModificarCtrl.jsp';
          document.getElementById('form1').submit();
      }
    
    </script>
  </head>
  <%!
  
   public String quitaCaracter(String cadena, String caracter){
     int longitud= cadena.length();
     while(cadena.indexOf(caracter)!=-1) {
       int posicion= cadena.indexOf(caracter);
       cadena= cadena.substring(0, posicion).concat(cadena.substring(posicion+1, longitud--));
     }
     return cadena;
    }
    
      private  sia.libs.periodo.Fecha estableceFormato(String fecha){
        sia.libs.periodo.Fecha regresa = null;
        try  {
          regresa = new sia.libs.periodo.Fecha(fecha, "-");
        } catch (Exception ex)  {
           ex.printStackTrace();
        }
        return regresa;
    } 
  %>
  <%
        boolean registraAct = false;
        String[] clave = null;
        clave = request.getParameter("clave").split(",");
 
        crsDetBitacora.addParamVal("idCuenta",clave[0]);
        crsDetBitacora.addParamVal("idSaldoD",clave[1]);                 
        crsDetBitacora.addParamVal("idSeguimiento",request.getParameter("idSeguimiento"));
        crsDetBitacora.registrosMap(DaoFactory.CONEXION_TESORERIA,"criterios.select.modificarActividad.diferenciaSaldo");
        crsDetBitacora.next();
          
       
        
  %>
  <body>
  <form id="form1" name="form1" action="tesRegistraActividades.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Modificar actividades de diferencia en saldos","Diferencia en saldos",true);%>
  <input type="hidden" id="nomProg" name="nomProg" value="<%=request.getParameter("nomProg")%>">
  <input type="hidden" id="opcion" name="opcion" value="<%=request.getParameter("opcion")%>">
  <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=request.getParameter("idProgramaS")%>">
  <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>">
  <input type="hidden" id="estatusDef" name="estatusDef" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="estatusDif" name="estatusDif" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="idEstatus" name="idEstatus" value="<%=request.getParameter("idEstatus")%>">
  <input type="hidden" id="clave" name="clave" value="<%=request.getParameter("clave")%>">
  <input type="hidden" id="mensaje" name="mensaje" value="">
  <input type="hidden" id="idSeguimiento" name="idSeguimiento" value="<%=request.getParameter("idSeguimiento")%>">  
   <br>
      <table>
      <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=request.getParameter("opcion")%> >> <%=request.getParameter("nomProg")%></td>
          </tr>
        </tbody>
      </table>
   <br>
   <table width="80%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4">Modificar aclaración</td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Fecha:</td>
             <td width="3%"></td>
             <td width="59%" align="left"> 
                     <input type="text" name="fecha" id="fecha" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,quitaCaracter(crsDetBitacora.getStr("FECHA"),"-"))%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
                     <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
             </td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="8%" align="left">Acción realizada:</td>
             <td width="3%"></td>
             <td width="59%" align="left"><textarea cols="80" rows="2" id="observaciones" name="observaciones"  class="cajaTexto"><%=crsDetBitacora.getStr("OBSERVACIONES")%></textarea></td>
           </tr>
        </tbody>
   </table> 
   <br>
   <table align="center" <%=!request.getParameter("mensaje").equals("")?"":"style='display:none'"%>>
        <thead></thead>
           <tbody>
             <tr align="center">
               <td  style="font-size:medium;"><%=request.getParameter("mensaje")%></td>
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
          <td><input type="button" class="boton" value="Aceptar" onclick="ir()"></td>
          <td><input type="button" class="boton" value="Cancelar" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>