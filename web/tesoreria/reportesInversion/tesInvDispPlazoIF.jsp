<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsBanco" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCuenta" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesInvDispPlazoIF</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript">
    </script>
    
      <script language="JavaScript"  type="text/javascript">
   
        function irBuscar() {
          llamamisma(0);
        }
      
        function llamamisma(parametro) {
        idBanco      = document.getElementById('banco').value;
        loadSource('socResultado',null,'tesIDPlazoIFCapa.jsp?banco=',idBanco);
      }
     
       function validarFechas(){  
       correcto=true; 
       mensaje='------  ALERTA  ------ \n';
       fInicio=document.getElementById('fechaInicio');
       ffinal=document.getElementById('fechaFinal');
       correcto = validaFIniFFin(fInicio,ffinal);
       if (correcto == false){
         mensaje=mensaje+'Verifique el periodo de fechas \n';
         correcto=false;
       }
       if(!correcto)
         throw new Error(mensaje);
       return correcto;
     }
     
       function asignaValorRep(boton){
         document.getElementById('tipoReporte').value=boton;
         if (validarFechas()){
             document.getElementById('form1').action='tesInvDispPlazoIFCtrl.jsp';
             document.getElementById('form1').target='_blank';
             document.getElementById('form1').submit();
         }
       }
        
       function regresar(){
           document.getElementById('form1').action='tesListaReportesInv.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
    </script>
  </head>
  <%
    crsBanco.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTcBancosInversion.reportesInversiones");
    crsBanco.liberaParametros();
  %>
  <body>
  <form id="form1" name="form1" action="tesInvDispPlazoIFCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Inversión de las Disponibilidades por Plazo e Institución Financiera","Reporte",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" value="<%=request.getParameter("nomReporte")%>" >
      <input type="hidden" id="numReporte" name="numReporte" value="<%=request.getParameter("numReporte")%>">
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
      </IFrame>
      <br>
      <table align="center" cellpadding="3">
      <thead></thead>
        <tbody>
              <tr>
                <td>Institución bancaria:</td>
                <td>
                  <select class="cajaTexto" id="banco" name="banco"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsBanco, out,"ID_BANCO","NOMBRE_CORTO","");%>
                  </select>
                </td>
              </tr>
            <%--  
                <select class="cajaTexto" id="banco" name="banco" onchange="irBuscar()"> 
              <tr>
                <td>Contrato/Cuenta:</td>
                <td id="socResultado">
                  <select class="cajaTexto" id="cuenta" name="cuenta" > 
                    <option value=''>- Seleccione -</option>
                  </select>
                </td>
             </tr>
            --%>
             <tr>
                <td>Periodo del: </td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fechaInicio" id="fechaInicio" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> al: </td>
                     <td><input type="text" name="fechaFinal" id="fechaFinal" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" class="cajaTexto" readonly size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaFinal')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
                    </tr>
                    </tbody>
                  </table>
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
          <td><input type="button" class="boton" value="Generar PDF" onclick="asignaValorRep('pdf')"></td>
          <td><input type="button" class="boton" value="Generar XLS" onclick="asignaValorRep('xls')"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>