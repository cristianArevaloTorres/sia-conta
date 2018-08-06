<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="dtsProceso" class="sia.rf.tesoreria.VariablesSession" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>registraSReserva</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript"  type="text/javascript">

      function regresa(){
            document.getElementById('form1').action='seleccArchSReserva.jsp';
            document.getElementById('form1').submit();
      }


      function importarArchivoS(){
          document.getElementById('form1').action='tesRegistraSReservaCtrl.jsp';
          document.getElementById('form1').submit();
      }

    </script>
    <%!
    private String getUltimaFechaCarga(String siglasBanco){
        String regresa = null;
        Map parametros = new HashMap(); 
        List <Vista> registro= null;
        parametros.put("siglas",siglasBanco); 
        SentennciasSE sentenciasSE = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
        try  {
          registro = sentenciasSE.registros("criterios.select.ultimaCarga.saldoReserva",parametros);
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
 //       boolean habilitar = false;
        boolean habilitar = true;
        boolean capturaFecha = false;
        String ultimaFechaBD = null;
      //  String strTipo = request.getAttribute("contenido").toString();
        /// Se evito la validación del tipo de archivo.
      /*  if ((strTipo.equals("text/plain")) || (strTipo.equals("application/octet-stream")) || (strTipo.equals("text/html")) )
            habilitar = true;
        else
            habilitar = false;
  */
        String banco =  dtsProceso.getIdProgramaS();
        
        if (banco.equals("6") || banco.equals("8") || banco.equals("11") || banco.equals("12") ){
            capturaFecha = true;
            if (banco.equals("6"))
                ultimaFechaBD = getUltimaFechaCarga("HBMX");
            else if (banco.equals("8"))
                    ultimaFechaBD = getUltimaFechaCarga("BBVA");
                 else if (banco.equals("12"))
                        ultimaFechaBD = getUltimaFechaCarga("MULTIVA");
                      else  
                         ultimaFechaBD = getUltimaFechaCarga("BAJIO");
        }    
        else
            capturaFecha = false;
        
       
    %>
  </head>
  <body>
  <form id="form1" name="form1" action="tesRegistraSReservaCtrl.jsp"  method="POST">
  <%util.tituloPagina(out,"Tesorería","Información de archivo cargado","Importación de saldos bancarios",true);%>
  <input type="hidden" id="nombre" name="nombre" value="<%=(String)session.getAttribute("nombre")%>">
  <input type="hidden" id="nomProg" name="nomProg" value="<%=dtsProceso.getNomProg()%>">
  <input type="hidden" id="opcion" name="opcion" value="<%=dtsProceso.getOpcion()%>">
  <input type="hidden" id="idProgramaS" name="idProgramaS" value="<%=dtsProceso.getIdProgramaS()%>">
  <input type="hidden" id="proceso" name="proceso" value="<%=dtsProceso.getProceso()%>">
  <table>
    <thead></thead>
        <tbody>
           <tr align="left">
              <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;"><%=dtsProceso.getOpcion()%> >> <%=dtsProceso.getNomProg()%></td>
           </tr>
        </tbody>
   </table>
   <br>
   <br>
   <table width="50%" align="center" class="general" cellpadding="3">
   <thead></thead>
        <tbody>
           <tr class="azulCla">
             <td colspan="4" >Informaci&oacute;n del archivo</td>
           </tr>
            <tr>
             <td width="6%"></td>
             <td width="15%">Nombre:</td>
             <td width="3%"></td>
             <td width="20%"><%=(String)session.getAttribute("nombre")%></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="15%">Tamaño:</td>
             <td width="3%"></td>
             <td width="20%"><%=session.getAttribute("tamanio")%> bytes (
             <%
               float floatBytes =Float.valueOf(session.getAttribute("tamanio").toString()).floatValue();
               // Float.valueOf(session.getAttribute("tamanio")).floatValue();
               float floatKBytes = floatBytes/1024;
               double redondeo = Math.round(floatKBytes*100)/100.0;
             %>
             <%=redondeo%> kb)</td>  
           </tr>
           <%--  <tr>
             <td width="6%"></td>
             <td width="15%">Tipo:</td>
             <td width="3%"></td>
             <td width="20%"><%=request.getAttribute("contenido")%></td>
           </tr>
           --%>
        </tbody>
   </table> 
   <br>
   <table width="50%" align="center" cellpadding="3" <%=capturaFecha?"":"style='display:none'"%>>
   <thead></thead>
        <tbody>
           <tr>
             <td width="6%"></td>
             <td width="15%">Ultimo saldo registrado en BD es del día:</td>
             <td width="3%"></td>
             <td width="20%" align="left"><%=ultimaFechaBD==null?" ":ultimaFechaBD%></td>
           </tr>
           <tr>
             <td width="6%"></td>
             <td width="15%">Fecha de registro:</td>
             <td width="3%"></td>
             <td width="20%" align="left">
             <input type="text" name="fechaCarga" id="fechaCarga" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                     <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                     onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaCarga')">
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
          <td <%=habilitar?"":"style='display:none'"%>><input type="button" class="boton" value="Importar a BD" onclick="importarArchivoS()"></td>
          <td><input type="button" class="boton" value="Seleccionar otro" onclick="regresa()"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>