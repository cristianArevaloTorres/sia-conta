<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.beans.seguridad.*"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsCuentaBancaria" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsEstatusCheque" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesChequeNominativo</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
    </script>
    <script language="JavaScript"  type="text/javascript">
      function validaNumero(event,campo){
        tecla = (document.all) ? event.keyCode : event.which; 
        if(tecla !=8 && tecla !=0 && (tecla < 48 || tecla > 57 )) {
          (document.all) ? event.returnValue = false : event.preventDefault();
        }      
      }
      
      function verificaFormulario(){
        correcto=true;
        mensaje='------  ALERTA  ------ \n';
        if(document.getElementById('idCuenta').value==''){
          mensaje=mensaje+'Debe seleccionar al menos una cuenta bancaria \n';
          correcto=false;
        }  
        if(document.getElementById('inicioRango').value!='' || document.getElementById('finRango').value!=''){
          if(document.getElementById('inicioRango').value=='' || document.getElementById('finRango').value==''){
            mensaje=mensaje+'Debe indicar el rango de cheques inicio - fin \n';
            correcto=false;
          }
        }
        if(!correcto)
          throw new Error(mensaje);
        return correcto;
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
      
      function validarReporte(){
        if (form1.idReporte.options[form1.idReporte.selectedIndex].value==1){
            divElem =document.getElementById('resChq');
            divElem.style.display='';
            /*cBeneficiario =document.getElementById('beneficiario');
            cBeneficiario.disabled = false;
            cEstatus =document.getElementById('estatus');
            cEstatus.disabled = false;*/
            document.getElementById('renglonBenef').style.display='';
            document.getElementById('renglonEstatus').style.display='';
        }
        else{
            divElem =document.getElementById('resChq');
            divElem.style.display='none';
      /*      cBeneficiario =document.getElementById('beneficiario');
            cBeneficiario.value = '';
            cBeneficiario.disabled = true;
            cEstatus =document.getElementById('estatus');
            for (i=0; i < cEstatus.length; i++){
              cEstatus.options[i].selected = false; 
            }
            cEstatus.disabled = true;*/
            document.getElementById('renglonEstatus').style.display='none';
            document.getElementById('renglonBenef').style.display='none';
        }
       }
      
      function asignaValorRep(boton){
        try{
          document.getElementById('tipoReporte').value=boton;
          document.getElementById('numReporte').value=document.getElementById('idReporte').value;
          if (verificaFormulario() && validarFechas()){
            if (form1.idReporte.options[form1.idReporte.selectedIndex].value==1){
             /* cBeneficiario =document.getElementById('beneficiario');
              cBeneficiario.disabled = false;
              cEstatus =document.getElementById('estatus');
              cEstatus.disabled = false;*/
              document.getElementById('renglonBenef').style.display='';
              document.getElementById('renglonEstatus').style.display='';
            }
            else{
             /* cBeneficiario =document.getElementById('beneficiario');
              cBeneficiario.value = '';
              cBeneficiario.disabled = true;
              cEstatus =document.getElementById('estatus');
              for (i=0; i < cEstatus.length; i++){
                cEstatus.options[i].selected = false; 
              }
              cEstatus.disabled = true;*/
              document.getElementById('renglonBenef').style.display='none';
              document.getElementById('renglonEstatus').style.display='none';
            }
            form1.action='tesChequeNominativoControl.jsp';
            form1.submit();
          }
        } catch(e){
          alert(e.message);
        }
       }
      
        function dispersorasGC() { 
            if (document.form1.elements['selecionaTodo'].checked){
                for (i=0; i<document.form1.elements['idCuenta'].options.length; i++) 
                    document.form1.elements['idCuenta'][i].selected = false;
            }
            if ( document.form1.elements['idCuenta'].selectedIndex == -1) { 
                for (i=0; i<document.form1.elements['idCuenta'].options.length; i++) 
                   document.form1.elements['idCuenta'][i].selected = true;
            } 
            else { 
                for (i=0; i<document.form1.elements['idCuenta'].options.length; i++) 
                   document.form1.elements['idCuenta'][i].selected = false;
                }    
        }
        
        function noSeleccionaDispGC() {
          document.form1.elements['selecionaTodo'].checked = false;
        }
      
    </script>
    <% 
      Autentifica autentifica = (Autentifica)request.getSession().getAttribute("Autentifica"); 
     /*ambito regional=2  and unidad_ejecutora = 121
       ambito estatal =3  and unidad_ejecutora = 121 and entidad = 32 */
      if (request.getParameter("admin").equals("2")){
        if (autentifica.getAmbito().equals("2") || autentifica.getAmbito().equals("1")){
          crsCuentaBancaria.addParamVal("regional"," and unidad_ejecutora = :param",autentifica.getUnidadEjecutora());
        }
        else{
          crsCuentaBancaria.addParamVal("estatal"," and unidad_ejecutora = :param0 and entidad = :param1",autentifica.getUnidadEjecutora(),autentifica.getEntidad());
        }
      }
      
      crsCuentaBancaria.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasBancarias.chqNominativo");
      crsEstatusCheque.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrEstatusCheque.chqNominativo");
    %>
  </head>
  <body>
  <form id="form1" name="form1" action="tesChequeNominativoControl.jsp"  method="POST" target="_blank">
  <%util.tituloPagina(out,"Tesorería","Cheques nominativos","Consulta",true);%>
  <input type="hidden" id="tipoReporte" name="tipoReporte">
  <input type="hidden" id="numReporte" name="numReporte">
   <br>
   <br>
   <table width="80%" align="center" cellpadding="6" >
    <thead></thead>
        <tbody>
           <tr valign="top">
              <td width="35%">Reporte a generar:</td>
              <td>
                <select class="cajaTexto" id="idReporte" name="idReporte" onchange="validarReporte()"> 
                    <option value="1">Cheques nominativos</option>
                    <option value="2">Cobro de cheque sin emisión</option>
                </select>
              </td>
            </tr>
            <tr id="resChq">
              <td>Incluir información del resumen de chequeras:</td>
              <td>
                  <input type="radio" name="resumen" value="1" checked>Si
                  <input type="radio" name="resumen" value="2">No
              </td>
            </tr>
            <tr>
              <td></td>
              <td><input type="checkbox" name="selecionaTodo" id="selecionaTodo" onclick="dispersorasGC()"> Todas las chequeras de gasto corriente y nómina central</td>
            </tr>
           <tr valign="top">
              <td>Unidad responsable:</td>
              <td>
                <select  class="cajaTexto" id="idCuenta" name="idCuenta" size="15" multiple="multiple" onclick="noSeleccionaDispGC()"> 
                  <%CRSComboBox(crsCuentaBancaria, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                </select>
              </td>
            </tr>
            <tr id="renglonBenef">
                <td>Beneficiario: </td>
                <td><input type="text" name="beneficiario" id="beneficiario" class="cajaTexto" size="50"></td>
            </tr>
            <tr id="renglonEstatus">
              <td>Estatus:</td>
              <td>
                <select class="cajaTexto" id="estatus" name="estatus" multiple="multiple" >
                   <%CRSMultipleComboBox(crsEstatusCheque, out,"ID_ESTATUS_CHEQUE","DESCRIPCION","1,2,3");%>
                </select>
              </td>
            </tr>
            <tr>
                <td>Rango de número de cheque: </td>
                <td>
                <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="inicioRango" id="inicioRango" class="cajaTexto" onkeypress="validaNumero(event,this)" size="15"> a: </td>
                     <td><input type="text" name="finRango" id="finRango" class="cajaTexto" onkeypress="validaNumero(event,this)" size="15"> </td>
                    </tr>
                 </tbody>
                </table>
                </td>
             </tr>
             <tr>
                <td>Fecha de expedición: </td>
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
          <td><input type="button" class="boton" value="Generar PDF" onclick="asignaValorRep('PDF')"></td>
          <td><input type="button" class="boton" value="Generar XLS" onclick="asignaValorRep('XLS')"></td>
        </tr>
      </tbody>
    </table>
  </form>
  </body>
</html>