<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsCuenta" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsAutoriza" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsRevisa" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsElabora" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesAutorizacionInvDF</title>
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
        idCuenta      = document.getElementById('idCuenta').value;
        loadSource('socResultado',null,'tesIDCompraCICapa.jsp?','idCuenta='+idCuenta);
      }
     
     
       function asignaValorRep(boton){
         document.getElementById('tipoReporte').value=boton;
         document.getElementById('form1').action='tesAutInversionDFCtrl.jsp';
         document.getElementById('form1').target='_blank';
         document.getElementById('form1').submit();
       }
        
       function regresar(){
           document.getElementById('form1').action='tesListaReportesInv.jsp';
           document.getElementById('form1').target='';
           document.getElementById('form1').submit();
       } 
    </script>
  </head>
  <%
    crsCuenta.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.RfTrCuentasInversionExisten.reportesInversiones");
    crsCuenta.liberaParametros();
    
    crsAutoriza.addParam("docto","DRIC");
    crsAutoriza.addParam("tipo","AUT");
    crsAutoriza.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsAutoriza.liberaParametros();
    
    crsRevisa.addParam("docto","DRIC");
    crsRevisa.addParam("tipo","VB");
    crsRevisa.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsRevisa.liberaParametros();
    
    crsElabora.addParam("docto","DRIC");
    crsElabora.addParam("tipo","ELB");
    crsElabora.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsElabora.liberaParametros();
    
  %>
  <body>
  <form id="form1" name="form1" action="tesAutInversionDFCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Autorización de Inversión de Disponibilidades Financieras","Reporte",true);%>
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
                <td>Contrato/Cuenta:</td>
                <td>
                  <select class="cajaTexto" id="idCuenta" name="idCuenta" onchange="irBuscar()"> 
                    <option value=''>- Seleccione -</option>
                     <%CRSComboBox(crsCuenta, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA","");%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>Compra de inversión:</td>
                <td><div id="socResultado">
                  <select class="cajaTexto" id="idCompra" name="idCompra" > 
                    <option value=''>- Seleccione -</option>
                  </select>
                </td>
             </tr>
              <tr>
                <td>Observaciones:</td>
                <td>
                 <textarea cols="80" rows="2" id="observaciones" name="observaciones"  class="cajaTexto"></textarea>
                </td>
             </tr>
             <tr>
                <td></td>
                <td></td>
                <td>Artículo 53</td>
             </tr>
             <tr>
                <td>Autorizo:</td>
                <td>
                  <select class="cajaTexto" id="autoriza" name="autoriza" > 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsAutoriza, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
                  </select>
                </td>
                <td>
                   <input type="checkbox" name="leyenda" id="leyenda" value="1"><br>
                   <input type="checkbox" name="leyenda" id="leyenda" value="2">
                </td>
              </tr>
              <tr>
                <td>Vo. Bo.</td>
                <td>
                  <select class="cajaTexto" id="reviso" name="reviso"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsRevisa, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
                  </select>
                </td>
              </tr>
              <tr>
              <td>Elaboro:</td>
                <td>
                  <select class="cajaTexto" id="elaboro" name="elaboro"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsElabora, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
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
          <td><input type="button" class="boton" value="Generar XLS" onclick="asignaValorRep('xls')"></td>
          <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>