<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesCompCarteraMes</title>
  <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript">
    </script>
    <script language="JavaScript"  type="text/javascript">
   
       function asignaValorRep(boton){
         document.getElementById('tipoReporte').value=boton;
         document.getElementById('form1').action='tesComCarteraMesCtrl.jsp';
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
  <body>
  <form id="form1" name="form1" action="tesComCarteraMesCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Composición de la cartera al cierre de mes","Reporte",true);%>
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
                <td>Mes:</td>
                <td>
                  <select class="cajaTexto" id="mes" name="mes"> 
                    <option value="1">Enero</option>
                    <option value="2">Febrero</option>
                    <option value="3">Marzo</option>
                    <option value="4">Abril</option>
                    <option value="5">Mayo</option>
                    <option value="6">Junio</option>
                    <option value="7">Julio</option>
                    <option value="8">Agosto</option>
                    <option value="9">Septiembre</option>
                    <option value="10">Octubre</option>
                    <option value="11">Noviembre</option>
                    <option value="12">Diciembre</option>
                  </select>
                </td>
                <td>  Año:</td>
                <td>
                  <select class="cajaTexto" id="anio" name="anio"> 
                    <option value="2009">2009</option>
                    <option value="2010">2010</option>
                    <option value="2011">2011</option>
                    <option value="2012">2012</option>
                    <option value="2013">2013</option>
                    <option value="2014">2014</option>
                    <option value="2015">2015</option>
                    <option value="2016">2016</option>
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