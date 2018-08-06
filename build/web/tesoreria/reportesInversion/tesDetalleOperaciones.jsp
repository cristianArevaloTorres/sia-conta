<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
  cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversion.reportesInversiones");
  String ffechaIni = request.getParameter("fechaInicio")== null ? "" : request.getParameter("fechaInicio");
  String ffechaFin = request.getParameter("fechaFinal")== null ? "" : request.getParameter("fechaFinal");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
  String fproceso = request.getParameter("proceso")== null ? "" : request.getParameter("proceso");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesDetalleOperaciones</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/tabla.js" type="text/javascript"></script>
    <script type="text/javascript" language="javascript">
      function inicia() {
          irBuscar();
      }
     
      function irBuscar() {
          llamamisma(0);
      }
      
      function llamamisma(parametro) {
        cuentaInversion   = document.getElementById('fidCuentaInversion');
        fechaInicial      = document.getElementById('fechaInicio');
        fechaFin          = document.getElementById('fechaFinal');
        procesoId         = document.getElementById('proceso').value;
        loadSource('dResultado',null,'tesDetalleOperacionesCapaResultado.jsp?proceso=',procesoId+'&idCuentaInversion='+cuentaInversion.value+'&fechaIni='+fechaInicial.value+"&fechaFin="+fechaFin.value+'&param='+parametro);
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
             document.getElementById('form1').action='tesDetalleOperacionesCtrl.jsp';
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
  <body>
   <form id="form1" name="form1" action="tesDetalleOperacionesCtrl.jsp" method="post">
      <%@ include file="../../Librerias/Funciones/tablaEncabezado.html"%>
      <%util.tituloPagina(out,"Tesorería","Detalle de Operaciones de Inversión por Institución Financiera","Reporte",true);%>
      <input type="hidden" id="tipoReporte" name="tipoReporte">  
      <input type="hidden" id="proceso" name="proceso" value="<%=request.getParameter("proceso")%>" >
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Cuentas de inversi&oacute;n</td>
            <td><select id='fidCuentaInversion' name='fidCuentaInversion' onchange='' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO",fidCuentaInversion);%>
            </select></td>
          </tr>
            <tr>
                <td>Periodo del: </td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fechaInicio" id="fechaInicio" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" readonly class="cajaTexto" size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a> al: </td>
                     <td><input type="text" name="fechaFinal" id="fechaFinal" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>" readonly class="cajaTexto" size="13">
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
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Buscar" onclick="irBuscar()"></td>
             <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
          </tr>
        </tbody>
      </table>
      <br>
      <table align='center' cellspacing="0" width="100%">
        <thead></thead>
        <tbody>
          <tr>
            <td id='dResultado'>
              
            </td>
          </tr>
        </tbody>
      </table>
      
    <%--  <div id="dResultado">

      </div>
    --%>
    
    
   </form>
  </body>
</html>