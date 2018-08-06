<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsAutoriza" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsRevisa" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsElabora" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="bcRfTrHistoricoReportes" class="sia.rf.tesoreria.registro.repHistInv.bcRfTrHistoricoReportes" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesDisponibilidadFHist</title>
  <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
   <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
   <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript">
   </script>
   <script language="JavaScript"  type="text/javascript">
   
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
         document.getElementById('form1').action='tesDisponibilidadCtrl.jsp';
         document.getElementById('form1').target='_blank';
         document.getElementById('form1').submit();
       }
        
       function regresar(){
           document.getElementById('form1').action='tesListadoRepHist.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
    </script>
  </head>
  <%
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
    boolean mostrar = false;
    String[] clave = null;
    String fechaReporte = null;
    clave = request.getParameter("clave").split(",");
      
    bcRfTrHistoricoReportes.setReporte(clave[0]);
    bcRfTrHistoricoReportes.setFecha(clave[1]);
    bcRfTrHistoricoReportes.setConsecutivo(clave[2]);
      
    bcRfTrHistoricoReportes.select(DaoFactory.CONEXION_TESORERIA);
    
    if ( sdf.parse(clave[1]).after(sdf.parse("23/05/2012")))
        mostrar = true;
    else 
        mostrar = false; 
    
    
    crsAutoriza.addParam("docto","DFIN");
    crsAutoriza.addParam("tipo","AUT");
    crsAutoriza.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsAutoriza.liberaParametros();
    
    crsRevisa.addParam("docto","DFIN");
    crsRevisa.addParam("tipo","REV");
    crsRevisa.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsRevisa.liberaParametros();
    
    crsElabora.addParam("docto","DFIN");
    crsElabora.addParam("tipo","ELB");
    crsElabora.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsElabora.liberaParametros();
    
  %>
  <body>
  <form id="form1" name="form1" action="tesDisponibilidadCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorer�a","Disponibilidades financieras","Reporte",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" value="<%=request.getParameter("nomReporte")%>" >
      <input type="hidden" id="numReporte" name="numReporte" value="<%=bcRfTrHistoricoReportes.getReporte()%>">
      <input type="hidden" id="fecha" name="fecha" value="<%=bcRfTrHistoricoReportes.getFecha()%>" >
      <input type="hidden" id="consecutivo" name="consecutivo" value="<%=bcRfTrHistoricoReportes.getConsecutivo()%>" >
      <input type="hidden" id="procedencia" name="procedencia" value="historicos" >
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <input type="hidden" id="actualizar" name="actualizar" >
      <input type="hidden" id="banco" name="banco" value="2" >
      <br>
      <table align="center" cellpadding="3">
      <thead></thead>
        <tbody>
              <tr>
                <td>Fecha: </td>
                 <td>
                  <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="fechaInicio" id="fechaInicio" value="<%=bcRfTrHistoricoReportes.getFecha()%>" class="cajaTexto" readonly size="13">
                         <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                         onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaInicio')">
                         <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
                    </tr>
                    </tbody>
                  </table>
                 </td>
              </tr>
              <%-- 
              <tr>
                <td>Instituci�n bancaria:</td>
                <td>
                  <input type="radio" name="banco" id="banco" value="1" <%=bcRfTrHistoricoReportes.getValorBanco().equals("1")?"checked":""%> >Nafinsa
                  <input type="radio" name="banco" id="banco" value="2" <%=bcRfTrHistoricoReportes.getValorBanco().equals("2")?"checked":""%> >Otro banco
                </td>
             </tr>
             --%>
             <tr>
               <td>Compromisos de pago</td>
               <td></td>
             </tr>
             <tr>
               <td>Concentradora egresos:</td>
               <td>
                  <input type="text" name="conEgresosCP" id="conEgresosCP" value="<%=bcRfTrHistoricoReportes.getConEgreCP()%>" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr  <%=mostrar?"style='display:none'":""%>>
               <td>Vencimiento del d�a:</td>
               <td>
                  <input type="text" name="vencimientoCP" id="vencimientoCP" value="<%=bcRfTrHistoricoReportes.getVencCP()%>" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
              <tr>
               <td>Montos a invertir</td>
               <td></td>
             </tr>
              <tr>
               <td>Concentradora ingresos:</td>
               <td>
                  <input type="text" name="conIngresoInv" id="conIngresoInv" value="<%=bcRfTrHistoricoReportes.getConIngInv()%>" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr>
               <td>Concentradora egresos:</td>
               <td>
                  <input type="text" name="conEgresosInv" id="conEgresosInv" value="<%=bcRfTrHistoricoReportes.getConEgeInv()%>" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr  <%=mostrar?"style='display:none'":""%>>
               <td>Vencimiento del d�a:</td>
               <td>
                  <input type="text" name="vencimientoInv" id="vencimientoInv" value="<%=bcRfTrHistoricoReportes.getVencInv()%>" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
             </tr>
             <tr>
                <td>Observaciones:</td>
                <td>
                 <textarea cols="80" rows="2" id="observaciones"  name="observaciones" class="cajaTexto"><%=bcRfTrHistoricoReportes.getObservacion()%></textarea>
                </td>
             </tr>
             <tr>
                <td>Autoriz�:</td>
                <td>
                  <select class="cajaTexto" id="autoriza" name="autoriza" > 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsAutoriza, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP",bcRfTrHistoricoReportes.getNumEmpleadoA());%>
                  </select>
                </td>
              
              </tr>
              <tr>
                <td>Revis�:</td>
                <td>
                  <select class="cajaTexto" id="reviso" name="reviso"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsRevisa, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP",bcRfTrHistoricoReportes.getNumEmpleadoVB());%>
                  </select>
                </td>
              </tr>
              <tr>
              <td>Elabor�:</td>
                <td>
                  <select class="cajaTexto" id="elaboro" name="elaboro"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsElabora, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP",bcRfTrHistoricoReportes.getNumEmpleadoE());%>
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