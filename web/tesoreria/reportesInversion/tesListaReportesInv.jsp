<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.catalogos.tiposReporte.ListaReportesInversion"%>
<%@ page import="sia.rf.tesoreria.catalogos.tiposReporte.TipoReporte"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesListaReportesInv</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="javascript" type="text/javascript">
    
     function guardaNomRep(){
       document.getElementById('nomReporte').value = document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].text;
     }
        
    function asignaPagina(){
       numReporte=document.getElementById('listaReporte').options[document.getElementById('listaReporte').selectedIndex].value;
       document.getElementById('numReporte').value=numReporte;
       switch (numReporte){
        case '1':
                 document.getElementById('form1').action='tesCotizaTasasIF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '3':
                 document.getElementById('form1').action='tesDisponibilidadF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '6':
                 document.getElementById('form1').action='tesRendimientosIF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '7':
                 document.getElementById('form1').action='tesResumenTasaPlazos.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '8':
                 document.getElementById('form1').action='tesResumenTasaIF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '9':
                 document.getElementById('form1').action='tesInvDispPlazo.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '10':
                 document.getElementById('form1').action='tesInvDispPlazoIF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '11':
                 document.getElementById('form1').action='tesComCarteraIFF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '12':
                 document.getElementById('form1').action='tesComCarteraCierre.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '13':
                 document.getElementById('form1').action='tesReservaMDP.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '14':
                 document.getElementById('form1').action='tesCompCarteraMes.jsp';
                 document.getElementById('form1').submit();
                 break;         
        case '15':
                 document.getElementById('form1').action='tesInversionTipoInst.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '16': 
                 document.getElementById('form1').action='tesAutorizacionInvDF.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '17': 
                 document.getElementById('form1').action='tesDetalleOperaciones.jsp?proceso=1';
                 document.getElementById('form1').submit();
                 break;
        case '18': 
                 document.getElementById('form1').action='tesInversionTipoInstGlobal.jsp?';
                 document.getElementById('form1').submit();
                 break;
        case '19': 
                 document.getElementById('form1').action='tesConsolidadoTransf.jsp';
                 document.getElementById('form1').submit();      
                 break;
        case '20': 
                 document.getElementById('form1').action='tesConsolidadoRendimientos.jsp';
                 document.getElementById('form1').submit();
                 break;
        case '21': 
                 document.getElementById('form1').action='tesTasasPactadasIDF.jsp';
                 document.getElementById('form1').submit();
                 break;
       }
    }
    
    </script>
  </head>
  <%
 StringBuffer cdgHTMLList = new StringBuffer();
 ListaReportesInversion listaReportesInversion = new ListaReportesInversion();
 listaReportesInversion.iniciar(Autentifica);
 
 List<TipoReporte> nombreReporte = listaReportesInversion.getTipoReporte();
 Iterator iter = nombreReporte.iterator();
 while (iter.hasNext()){
   TipoReporte tipoReporte = (TipoReporte) iter.next();
   cdgHTMLList.append("<option value=" + tipoReporte.getValor() );
   cdgHTMLList.append(">"+ tipoReporte.getDescripcion()   + "</option>");
 }
  %>
  <body>
  <form id="form1" name="form1" method="POST">
      <%util.tituloPagina(out,"Tesorería","Listado de reportes de inversión","Generador de reportes",true);%>
      <input type="hidden" id="nomReporte" name="nomReporte" >
      <input type="hidden" id="numReporte" name="numReporte">
      <table>
        <thead></thead>
        <tbody>
          <tr align="left">
            <td style="color:rgb(0,99,148); font-style:italic; font-weight:bold;">Inversiones >> Consultas y reportes </td>
          </tr>
        </tbody>
      </table>
      <br>
      <br>
      <table align="center">
      <thead></thead>
      <tbody>
        <tr>
          <td>Información a consultar:</td>
          <td>
            <select class="cajaTexto" id="listaReporte" name="listaReporte" onchange="guardaNomRep()">
             <%=cdgHTMLList%>
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
              <td><input type="button" class="boton" value="Aceptar" onclick="asignaPagina()"></td>
            </tr>
          </tbody>
         </table>
      </form>
  </body>
</html>