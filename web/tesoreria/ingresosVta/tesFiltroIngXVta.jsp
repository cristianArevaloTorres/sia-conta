<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ page import="sia.libs.formato.Fecha" %>
<%@ page import="sia.db.sql.Vista" %>
<%@ page import="sia.db.sql.SentennciasSE" %>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsCtasIXV" class="sia.db.sql.SentenciasCRS" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesFiltroIngXVta</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/validacionesFecha.js" type="text/javascript" >
    </script>
    <script language="JavaScript"  type="text/javascript">

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
     
        function verificaSeleccionCta(){
          correcto=true; 
          mensaje='------  ALERTA  ------ \n';
          if ( (document.getElementById("ingXVta")==null || document.form1.elements['ingXVta'].selectedIndex == -1) )
                 correcto = false; 
          if (correcto == false){
            mensaje=mensaje+'Debe seleccionar al menos una cuenta bancaria \n';
            correcto=false;
          }
          if(!correcto)
            throw new Error(mensaje);
          return correcto;
        }
        
        
        function asignaValorRep(boton){
          try{
            document.getElementById('tipoReporte').value= boton;
            if (validarFechas() && verificaSeleccionCta()){
                document.getElementById('form1').action='tesFiltroIngXVtaCtrl.jsp';
                document.getElementById('form1').target='_blank';
                document.getElementById('form1').submit();
             }
          } catch(e){
            alert(e.message);
          }
        }
        
        
    </script>
  </head>
<%!
  
    public String fomatearFecha(String fecha)  {
    String regresa = null;
    if (fecha!=null)
      regresa = Fecha.formatear(2,fecha.replace("-",""));
    return regresa;
  }
%>
<%

    crsCtasIXV.addParamVal("cuentas","957,1035");
    crsCtasIXV.registrosMap(DaoFactory.CONEXION_TESORERIA,"catalogos.select.rfTrCuentasBancarias.ingresosVtas");

 
%>
  <body >
    <form id="form1" name="form1" action="tesFiltroIngXVtaCtrl.jsp" method="POST">
      <%util.tituloPagina(out,"Tesorería","Ingresos por ventas","Generador de reportes",true);%>
      <input type="hidden" id="tipoReporte" name="tipoReporte">
      <input type="hidden" id="tesAdm" name="tesAdm"  value="<%=request.getParameter("tesAdm")%>"> 
      <br>
      <br>
       <table  width="80%" align="center" >
      <thead></thead>
      <tbody>
        <tr>
          <td align="left">Información a consultar</td>
        </tr>
        <tr>
          <td align="left">
            <input name="referenciaRep" id="referenciaRep" type="radio" value="1" checked="checked"/>Referencias válidas
            <input name="referenciaRep" id="referenciaRep" type="radio" value="2"/>Referencias inválidas
          </td>
        </tr>
      </tbody>
      </table>
      <br>
      <br>
      <table width="80%" align="center" >
      <thead></thead>
      <tbody>
        <tr>
          <td align="left">Cuentas a consultar</td>
        </tr>
      </tbody>
      </table>
      <table width="80%" style="background-color:transparent; border-color:rgb(0,132,198); border-style:solid; border-width:2.0px;" align="center">
      <thead></thead>
      <tbody>
        <tr> 
          <td> 
            <table>
            <thead></thead>
            <tbody>
                <tr>
                  <td></td><td></td><td></td>
                </tr>
                <tr>
                  <td></td>
                  <td><select  class="cajaTexto" id="ingXVta" name="ingXVta" multiple="multiple" > 
                        <%CRSComboBox(crsCtasIXV, out,"ID_CUENTA","NUM_CUENTA,NOMBRE_CTA","");%>
                      </select>
                  </td><td></td>
                </tr>
            </tbody>
            </table>
          </td>
        </tr>
      </tbody>
      </table>
      <br>
      <br>
      <table align="center">
        <thead></thead>
        <tbody>
          <tr>
            <td align="right">Periodo del: </td>
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
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>