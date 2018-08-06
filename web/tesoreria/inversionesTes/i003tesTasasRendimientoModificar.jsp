
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.db.dao.DaoFactory" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="bancosInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tasasDetalle" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tasasRendimiento" class="sia.rf.tesoreria.registro.tasasRendimiento.bcRfTrTasasRendimiento" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i003tesTasasRendimientoModificar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script type="text/javascript" language="javascript">
      
      function cambiaCuenta(tis) {
        if (tis.value=='498'){
           document.getElementById('banxico').style.display='';
           document.getElementById('otros').style.display='none';
        }
        else{
            document.getElementById('banxico').style.display='none';
            document.getElementById('otros').style.display='';
        }
      }      
      
      function validarFormulario() {
        correcto = true;
        mensaje  = "----------- A L E R T A -------------\n" 
        if(document.getElementById("fecha").value=='') {
          correcto = false;
          mensaje += "Debe seleccionar una fecha\n";
        }
        if(document.getElementById("idBanco").value=='') {
          correcto = false;
          mensaje += "Debe seleccionar una institución bancaria\n";
        }
        if (document.getElementById('idBanco').value=='498')
            tasas = document.getElementsByName("tasab");
        else
            tasas = document.getElementsByName("tasa");
        for (i = 0; i < tasas.length; i++)  {
          if(tasas[i].value=='') {
            correcto = false;
            mensaje += "Son requeridas todas las tasas\n";
            break;
          }
        }
        
        if(!correcto)
          alert(mensaje);
         else
          aceptar();
        
      }
      
      function aceptar() {
        envf(form1);
      }
      
      var jsenv = false;
      function envf(frm) {
        if(!jsenv) {
          frm.submit();
          jsenv = true;
        }
        else
          alert('Cuide su información, evite dar doble click en la página');
      }
      
      function cancelar() {
        form1.action='i003tesTasasRendimientoFiltro.jsp';
        form1.submit();
      }
      
      function validarFlotante(event,campo){
        //alert(event);
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
      
    </script>
  </head>
  <%
    bancosInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"tasasRendimiento.select.RfTcBancosInversion-bancosConInversion.inversiones");
    tasasDetalle.addParamVal("idRendimiento","and id_rendimiento=:param",request.getParameter("clave"));
    tasasDetalle.registrosMap(DaoFactory.CONEXION_TESORERIA,"tasasRendimiento.select.RfTrTasasRendDetalle.inversiones");
    tasasRendimiento.setIdRendimiento(request.getParameter("clave"));
    tasasRendimiento.select(DaoFactory.CONEXION_TESORERIA);
    String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
    String fidBanco = request.getParameter("fidBanco")== null ? "" : request.getParameter("fidBanco");
    boolean isBanxico = tasasRendimiento.getIdBanco().equals("498");
    String[] titBan = {"CETES","TIIE"};
    String[] titOtr = {"1","7","14","21","28"};
    
  %>
  <body>
    <form id='form1' name='form1' action='i003tesTasasRendimientoControl.jsp' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Tasas de rendimiento","Modificar",true);%>
      <input type="hidden" id="idRendimiento" name="idRendimiento" value="<%=request.getParameter("clave")%>">
      <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
      <input type="hidden" id="fidBanco" name="fidBanco" value="<%=fidBanco%>">
      <input type="hidden" id="accion" name="accion" value="2">
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td>Fecha de operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="<%=tasasRendimiento.getFecha()%>" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td>Instituci&oacute;n financiera</td>
            <td><select id='idBanco' name='idBanco' onchange='cambiaCuenta(this)' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(bancosInversion, out,"id_banco","NOMBRE_CORTO",tasasRendimiento.getIdBanco());%>
            </select></td>
          </tr>
        </tbody>
      </table>
      <br>
      <table align='center'>
        <thead></thead>
        <tbody id="<%=isBanxico ?"banxico":"otros"%>">
          <tr>
            <td><%=isBanxico ?"Documentos":"Plazos"%></td>
            <%int cont=0;
              boolean reg = true;
              while(tasasDetalle.next() && reg) { %>
              <td align="center"><%=isBanxico?titBan[cont]:titOtr[cont]%><input type="hidden" id="<%=isBanxico ? "plazob":"plazo"%>" name="<%=isBanxico ? "plazob":"plazo"%>" value="<%=tasasDetalle.getString("plazo")%>"></td>
            <%cont++;
              if (isBanxico && cont==2)
                reg = false;
              else 
                reg = true;
                }%>
          </tr>
          <tr>
            <td>Tasas</td>
            <%int contp=0;
              boolean regp = true;
              tasasDetalle.beforeFirst();
              while(tasasDetalle.next() && regp) {%>
              <td><input type="text" class="cajaTexto" name="<%=isBanxico?"tasab":"tasa"%>" id="<%=isBanxico?"tasab":"tasa"%>" value="<%=tasasDetalle.getString("tasa")%>" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <%contp++; 
              if (isBanxico && contp==2)
                regp = false;
              else 
                regp = true;}%>
          </tr>
        </tbody>
         <tbody id="<%=!isBanxico ?"banxico":"otros"%>" style="display:none">
          <tr>
            <td><%=!isBanxico ?"Documentos":"Plazos"%></td>
            <%for (int i = 0; i < (isBanxico?5:2); i++)  {%>
              <td align="center"><%=isBanxico?titOtr[i]:titBan[i] %><input type="hidden" id="<%=!isBanxico ? "plazob":"plazo"%>" name="<%=!isBanxico ? "plazob":"plazo"%>" value=""></td>
            <%}%>
          </tr>
          <tr>
            <td>Tasas</td>
            <%for (int i = 0; i < (isBanxico?5:2); i++)  {%>
              <td><input type="text" class="cajaTexto" name="<%=!isBanxico?"tasab":"tasa"%>" id="<%=!isBanxico?"tasab":"tasa"%>" value="" size="7" maxlength="7" style="text-align:right" onkeypress="validarFlotante(event,this)">%</td>
            <%}%>
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
            <td><input type="button" class="boton" value="Guardar" onclick="validarFormulario()"></td>
            <td><input type="button" class="boton" value="Cancelar" onclick="cancelar()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>