<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.db.sql.SentennciasSE"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsFoliosI" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsFoliosF" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesConsolidadoTransf</title>
     <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
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
             document.getElementById('form1').action='tesConsTransfCtrl.jsp';
             document.getElementById('form1').target='_blank';
             document.getElementById('form1').submit();
         }
       }
        
       function regresar(){
           document.getElementById('form1').action='tesListaReportesInv.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
       
       function filtrar(obj,lista) {
      sel = document.getElementById(lista);
      for (i=0; opt = sel.options[i]; i++) {
        txt = opt.text;
        opt.style.display = (txt.indexOf(obj.value) == 0) ? 'block' : 'none';
      }
      // seleccionar primer item visible
      for (i=0; opt = sel.options[i]; i++) 
      if (opt.style.display=='block') {
        sel.selectedIndex=i;
        break;
      }
    } 
    
    </script>
  </head>
  <body>
    <form id="form1" name="form1" action="tesConsTransfCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Consolidación de Transferencias","Reporte",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" value="<%=request.getParameter("nomReporte")%>" >
      <input type="hidden" id="numReporte" name="numReporte" value="<%=request.getParameter("numReporte")%>">
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <br>
      <table align="center">
      <thead></thead>
        <tbody>
            <tr>
              <td>Folios:</td>
              <td>
                <table width="100%" align="center">
                    <thead></thead>
                    <tbody>
                      <tr>
                         <td>
                           <div id="foliosInicial">
                             <input type="text"  onkeyup=filtrar(this,'folioIni'); size="15" name="folioI" id="folioI" class="cajaTexto" >  
                           </div>
                         </td>
                         <td>
                           <div id="foliosFin">
                             <input type="text"  onkeyup=filtrar(this,'folioFin'); size="15" name="folioF" id="folioF" class="cajaTexto" >  
                           </div>
                         </td>
                        </tr>
                    </tbody>
                  </table> 
                </td>         
            </tr>
            <tr>
             <td></td>
             <td>
                <table width="100%" align="center">
                    <thead></thead>
                    <tbody>
                      <tr>
                         <td>
                           <%
                             crsFoliosI.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrHistoricoReportesFolios.reportesInversiones");
                           %>
                           <div id="folioIncialCons">
                             <select class="cajaTexto" id="folioIni" name="folioIni" size="5" onclick="form1.folioI.value=form1.folioIni.options[this.selectedIndex].text"> 
                               <option value=''>- Seleccione -</option>
                               <%CRSComboBox(crsFoliosI, out,"minimo","minimo","");%>
                             </select>  
                           </div>
                         </td>
                         <td>
                          <%
                              crsFoliosF.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrHistoricoReportesFolios.reportesInversiones");
                          %>
                          <div id="folioFinalCons">
                              <select class="cajaTexto" id="folioFin" name="folioFin" size="5" onclick="form1.folioF.value=form1.folioFin.options[this.selectedIndex].text"> 
                              <option value=''>- Seleccione -</option>
                              <%CRSComboBox(crsFoliosF, out,"maximo","maximo","");%>
                              </select>  
                          </div>
                         </td>
                      </tr>
                    </tbody>
                </table> 
             </td>       
            </tr>
            <tr>
                <td>Importe: </td>
                <td>
                <table align='left'>
                  <thead></thead>
                    <tbody>
                    <tr>
                     <td><input type="text" name="inicioImporte" id="inicioImporte" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="18"> a: </td>
                     <td><input type="text" name="finImporte" id="finImporte" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="18"> </td>
                    </tr>
                 </tbody>
                </table>
                </td>
             </tr>
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