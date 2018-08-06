<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sia.rf.tesoreria.registro.comprasInversion.bcRfTrComprasInversion" %>
<%@ page import="sia.db.sql.SentenciasCRS"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Numero" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<%
  SentenciasCRS compras  = null;
  SentenciasCRS detalleCompras    = null;
  boolean isSociedadInversion     = false;
  String ffecha             = request.getParameter("ffecha")==null ? "" : request.getParameter("ffecha");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")==null ? "" : request.getParameter("fidCuentaInversion");
  String festatus           = request.getParameter("festatus")==null ? "" : request.getParameter("festatus");
  try  {
    compras = new SentenciasCRS();
    compras.addParamVal("idCompraInversion","and coi.id_compra_inversion=:param",request.getParameter("clave"));
    compras.registrosMap(DaoFactory.CONEXION_TESORERIA, "vencimiento.select.RfTrComprasInversion-formularioCompra.inversiones");
    if(compras.next())
      isSociedadInversion = compras.getString("id_tipo_inversion").equals("3");
      
    detalleCompras = new SentenciasCRS();
    detalleCompras.addParamVal("idCompraInversion","and id_compra_inversion=:param",request.getParameter("clave"));
    detalleCompras.registrosMap(DaoFactory.CONEXION_TESORERIA, "compraInversion.select.RfTrDetalleCompra.inversiones");
    
    
    
  } catch (Exception ex)  {
    ex.printStackTrace();
  } finally  {
    if(detalleCompras!=null) {
      detalleCompras.liberaParametros();
    }
  }
  
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i006tesVencimientoInversionModificar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script type="text/javascript" language="javascript">
      
      function ponerComas(nStr)
      {
        nStr += '';
        x = nStr.split('.');
        x1 = x[0];
        x2 = x.length > 1 ? '.' + x[1] : '';
        var rgx = /(\d+)(\d{3})/;
        while (rgx.test(x1)) {
          x1 = x1.replace(rgx, '$1' + ',' + '$2');
        }
        return x1 + x2;
      }
      
      function quitarComas(valor) {    
          var re = /,/g;
          return valor.replace(re,"");        
      }
      
      function formatear(tis) {
        tis.value = quitarComas(tis.value);
        tis.value = ponerComas(redondear(tis.value,2));
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
      
      function redondear(num, dec) {
          var result = Math.round(num*Math.pow(10,dec))/Math.pow(10,dec);
          return result;
      }
      
      function calcularRendimientos() {
        if(document.getElementById("hisSociedad").value=='true')
          calcularRendimientoSI();
        else
          calcularRendimientoMD();
      }
      
      function calcularRendimientoMD() {
        rendimientos = document.getElementsByName("rendimiento");
        importes = document.getElementsByName("importe");
        tasa = document.getElementById("tasa");
        plazo = document.getElementById("plazo");
        for (i = 0; i < rendimientos.length; i++)  {
          rendimientos[i].value = importes[i].value*(tasa.value/100/360)*plazo.value;
          formatear(rendimientos[i]);
        }
        
      }
      
      function calcularRendimientoSI() {
        rendimientos        = document.getElementsByName("rendimiento");
        titulosAccion       = document.getElementsByName("titulosAcciones");
        valores             = document.getElementsByName("valor");
        valoresAccionVenta  = document.getElementsByName("valorAccionVenta");
        for (i = 0; i < rendimientos.length; i++)  {
          if(valoresAccionVenta[i].value != '') {
            rendimientos[i].value = (quitarComas(valoresAccionVenta[i].value)-quitarComas(valores[i].value)) * titulosAccion[i].value;
            formatear(rendimientos[i]);
            formatear(valoresAccionVenta[i]);
          } else
            alert("Requiere el valor de la accion al momento de vender para calcular el rendimiento de la fila " + (i+1));
        }
      }
    </script>
    <script type="text/javascript" language="javascript"> 
      var jsenv = false;
      function envf(frm) {
        if(!jsenv) {
          frm.submit();
          jsenv = true;
        }
        else
          alert('Cuide su información, evite dar doble click en la página');
      }
      
      function preparaEnvio() {
        if(document.getElementById("hisSociedad").value=='true') {
          valoresAccionVenta  = document.getElementsByName("valorAccionVenta");
          for (i = 0; i < valoresAccionVenta.length; i++)  {
            valoresAccionVenta[i].value = quitarComas(valoresAccionVenta[i].value);
          }
        }
        rendimientos  = document.getElementsByName("rendimiento");
        for (i = 0; i < rendimientos.length; i++)  {
          rendimientos[i].value = quitarComas(rendimientos[i].value);
        }
        document.getElementById("form1").action='i006tesVencimientoInversionControl.jsp';
        envf(document.getElementById("form1"));
      }
    
      function validarFormulario() {
        correcto = true;
        mensaje='------------- A L E R T A --------------\n';
        if(document.getElementById("hisSociedad").value=='true') {
          valoresAccionVenta  = document.getElementsByName("valorAccionVenta");
          for (i = 0; i < valoresAccionVenta.length; i++)  {
            if(valoresAccionVenta[i].value=='') {
              mensaje += "Requiere capturar todos los valores de venta de las acciones\n";
              correcto = false;
              break;
            }
          }
        }
        rendimientos  = document.getElementsByName("rendimiento");
        for (i = 0; i < rendimientos.length; i++)  {
          if(rendimientos[i].value=='') {
            mensaje += "Requiere calcular todos los rendimientos de venta de las acciones\n";
            correcto = false;
            break;
          }
        }
        if(!correcto) {
          alert(mensaje);
        } else
          preparaEnvio();
      }
    </script>
  </head>
  <body>
    <form id='form1' name='form1' action='i006tesVencimientoInversionFiltro.jsp' method='post'>
      <%util.tituloPagina(out,"Tesorería central","Vencimiento de inversiones o compras","Modificar",true);%>
      <input type="hidden" id="idCompraInversion" name="idCompraInversion" value="<%=compras.getString("id_compra_inversion")%>">
      <input type="hidden" id="idCuentaInversion" name="idCuentaInversion" value="<%=compras.getString("id_cuenta_inversion")%>">
      <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
      <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>">
      <input type="hidden" id="festatus" name="festatus" value="<%=festatus%>">
      <input type="hidden" id="plazo" name="plazo" value="<%=compras.getString("plazo")%>">
      <input type="hidden" id="tasa" name="tasa" value="<%=compras.getString("tasa")%>">
      <input type="hidden" id="estatus" name="estatus" value="<%=compras.getString("estatus")%>">
      <input type="hidden" id="hisSociedad" name="hisSociedad" value="<%=isSociedadInversion%>">
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td class="negrita" align="right">Cuenta de inversi&oacute;n</td>
            <td><%=compras.getString("cuenta_inversion")%></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tipo inversi&oacute;n</td>
            <td><%=compras.getString("tipo_inversion")%></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de inversi&oacute;n</td>
            <td><%=compras.getString("fecha")%></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Plazo</td>
            <td><%=compras.getString("plazo")%></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tasa</td>
            <td><%=compras.getString("tasa")%></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de vencimiento</td>
            <td><%=compras.getString("vencimiento")%>
                <input type="hidden" id="vencimiento" name="vencimiento" value="<%=compras.getString("vencimiento")%>"></td>
          </tr>
          <tr>
            <td class="negrita" align="right">N&uacute;mero de operaci&oacute;n</td>
            <td><%=compras.getString("num_operacion")%></td>
          </tr>
        </tbody>
      </table>
      <br><br>
      <table align='center' class='general' id="tbDocumentos">
        <thead>
          <tr class="azulCla">
            <td colspan="6">Listado de documentos de la compra</td>
            
          </tr>
          <tr>
            <th class='azulObs'>Tipo valor</th>
            <th class='azulObs'>No de titulos o acciones</th>
            <th class='azulObs'>Valor del t&iacute;tulo o acci&oacute;n</th>
            <th class='azulObs'>Importe</th>
            <%if(isSociedadInversion) {%>
              <th class='azulObs'>Valor accion venta</th>
            <%}%>
            <th class='azulObs'>Rendimiento</th>
          </tr>
        </thead>
        <tbody>
          <% try  {
              while(detalleCompras.next()) {%>
                <tr>
              <td><%=detalleCompras.getString("tipo_valor")%>
              <input type="hidden" id="idDetalleCompra" name="idDetalleCompra" value="<%=detalleCompras.getString("id_detalle_compra")%>">
              </td>
              <td align="right"><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompras.getDouble("titulos_acciones"))%>
                <input type="hidden" id="titulosAcciones" name="titulosAcciones" value="<%=detalleCompras.getDouble("titulos_acciones")%>"></td>
              <td align="right"><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompras.getDouble("valor"))%>
                <input type="hidden" id="valor" name="valor" value="<%=detalleCompras.getDouble("valor")%>"></td>
              <td align="right"><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompras.getDouble("importe"))%>
                <input type="hidden" id="importe" name="importe" value="<%=detalleCompras.getDouble("importe")%>"></td>
              <%if(isSociedadInversion) {%>
                <td align="right"><input type="text" style="text-align:right" class="cajaTexto" name="valorAccionVenta" id="valorAccionVenta" 
                    value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompras.getDouble("valor_accion_venta"))%>" size="10" maxlength="18" onkeypress="validarFlotante(event,this)"></td>
              <%}%>
              <td align="right"><input type="text" style="text-align:right" class="cajaTexto" name="rendimiento" id="rendimiento" 
                  value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompras.getDouble("rendimiento"))%>" size="10" maxlength="17" onkeypress="validarFlotante(event,this)"></td>
            </tr> <%
              }
            } catch (Exception ex)  {
              ex.printStackTrace();
            } finally  {
            }
          %>
        </tbody>
      </table>
      <br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Aceptar" onclick="validarFormulario()"></td>
            <td><input type="submit" class="boton" value="Cancelar" onclick=""></td>
            <td><input type="button" class="boton" value="Calcular" onclick="calcularRendimientos()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>