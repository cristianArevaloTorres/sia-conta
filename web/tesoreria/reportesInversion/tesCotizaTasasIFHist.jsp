<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsRevisa" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsElabora" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="bcRfTrHistoricoReportes" class="sia.rf.tesoreria.registro.repHistInv.bcRfTrHistoricoReportes" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCotizaTasasIFHist</title>
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
        fecha      = document.getElementById('fechaInicio').value;
        dtosTabla  = document.getElementById('tablah').value;
        loadSource('tasasResultado',null,'tesTablaTasaCapa.jsp?','fecha='+fecha+'&tablaHist='+dtosTabla);
      }
   
       function validarFlotante(event,campo){
         tecla = (document.all) ? event.keyCode : event.which; 
         if(tecla!=46  && tecla !=8 && tecla !=0 && (tecla < 48 || tecla > 57 )) {
           (document.all) ? event.returnValue = false : event.preventDefault();
         }      
         if(tecla == 46){
           var punto = campo.value.indexOf(".",0)
           if (punto != -1){ 
             (document.all) ? event.returnValue = false : event.preventDefault();
           }
         }         
       }
   
   
       function asignaValorRep(boton){
         document.getElementById('tipoReporte').value=boton;
         if (boton=='pdf'){
            document.getElementById('actualizar').value =  confirm('Desea guardar los cambios');
         }
         document.getElementById('form1').action='tesCotizaTasasIFCtrl.jsp';
         document.getElementById('form1').target='_blank';
         document.getElementById('form1').submit();
       }
        
       function regresar(){
           document.getElementById('form1').action='tesListadoRepHist.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
       
           function focoFecha(){
           document.getElementById('fechaInicio').focus();        
       }
       
         function focoCTFecha(){
           document.getElementById('fechaInicio').focus();        
       }
    </script>
  </head>
  <%
    String[] clave = null;
    String fechaReporte = null;
    clave = request.getParameter("clave").split(",");
  
    bcRfTrHistoricoReportes.setReporte(clave[0]);
    bcRfTrHistoricoReportes.setFecha(clave[1]);
    bcRfTrHistoricoReportes.setConsecutivo(clave[2]);
    
    bcRfTrHistoricoReportes.select(DaoFactory.CONEXION_TESORERIA);
     
    crsRevisa.addParam("docto","STFO");
    crsRevisa.addParam("tipo","REV");
    crsRevisa.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsRevisa.liberaParametros();
    
    crsElabora.addParam("docto","STFO");
    crsElabora.addParam("tipo","ELB");
    crsElabora.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsElabora.liberaParametros();
    
  %>
  <body onload="irBuscar()">
  <form id="form1" name="form1" action="tesCotizaTasasIFCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Cotización de tasas de rendimientos por Institución Financiera","Reporte",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" value="<%=request.getParameter("nomReporte")%>" >
      <input type="hidden" id="numReporte" name="numReporte" value="<%=bcRfTrHistoricoReportes.getReporte()%>">
      <input type="hidden" id="fecha" name="fecha" value="<%=bcRfTrHistoricoReportes.getFecha()%>" >
      <input type="hidden" id="consecutivo" name="consecutivo" value="<%=bcRfTrHistoricoReportes.getConsecutivo()%>" >
      <input type="hidden" id="procedencia" name="procedencia" value="historicos" >
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <input type="hidden" id="actualizar" name="actualizar" >
      <input type="hidden" id="tablah" name="tablah" value="<%=bcRfTrHistoricoReportes.getTabla()%>">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
      </IFrame>
      <br>
      <table align="center" cellpadding="3">
      <thead></thead>
        <tbody>
              <tr>
                <td>Fecha</td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fechaInicio" id="fechaInicio" value="<%=bcRfTrHistoricoReportes.getFecha()%>" onfocus="irBuscar()" readonly class="cajaTexto" size="13" >
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio');focoFecha();" >
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt="" ></a></td>
                    </tr>
                    </tbody>
                  </table>
                 </td>
              </tr>
              <tr>
                <td></td>
                <td id="tasasResultado">
                </td>
             </tr>
             <tr>
               <td>Retiro de recursos</td>
               <td>
                  <input type="text" name="retiro" id="retiro" class="cajaTexto" value="<%=bcRfTrHistoricoReportes.getRetiro()%>" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr>
               <td>Fondo de reserva</td>
               <td>
                  <input type="text" name="fondoReserva" id="fondoReserva" class="cajaTexto" value="<%=bcRfTrHistoricoReportes.getFondoReserva()%>" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr>
               <td>Inversión de recursos</td>
               <td>
                  <input type="text" name="inversionRec" id="inversionRec" class="cajaTexto" value="<%=bcRfTrHistoricoReportes.getInversionRec()%>" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr>
                <td>Observaciones</td>
                <td>
                 <textarea cols="80" rows="2" id="observaciones" name="observaciones"  class="cajaTexto"><%=bcRfTrHistoricoReportes.getObservacion()%></textarea>
                </td>
             </tr>
             <tr>
                <td>Cotizó</td>
                <td>
                  <select class="cajaTexto" id="elaboro" name="elaboro" > 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsElabora, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP",bcRfTrHistoricoReportes.getNumEmpleadoE());%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>VoBo</td>
                <td>
                  <select class="cajaTexto" id="reviso" name="reviso"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsRevisa, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP",bcRfTrHistoricoReportes.getNumEmpleadoVB());%>
                  </select>
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
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>