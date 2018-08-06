
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.libs.formato.Numero" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tiposInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tiposValores" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="detalleCompra" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="compraInversion" class="sia.rf.tesoreria.registro.comprasInversion.bcRfTrComprasInversion" scope="page"/>
<jsp:useBean id="bcRfTrOperacionesInversion" class="sia.rf.tesoreria.registro.operacionesInversion.bcOperacionesInversionCalculo" scope="page"/>
<%
  cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
  tiposInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTcTiposInversion.inversiones");
  tiposValores.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTcTipoValores.inversiones");
  
  String idCompraInversion = request.getParameter("clave");
  
  detalleCompra.addParamVal("idCompraInversion","and id_compra_inversion=:param",idCompraInversion);
  detalleCompra.registrosMap(DaoFactory.CONEXION_TESORERIA, "compraInversion.select.RfTrDetalleCompra.inversiones");
  
  compraInversion.setIdCompraInversion(idCompraInversion);
  compraInversion.select(DaoFactory.CONEXION_TESORERIA);
  
  
  double monto = bcRfTrOperacionesInversion.getSaldoAnteriorMismaFechaOAnterior(compraInversion.getIdCuentaInversion(),compraInversion.getFecha());
  String ffecha = request.getParameter("ffecha")== null ? "" : request.getParameter("ffecha");
  String fidCuentaInversion = request.getParameter("fidCuentaInversion")== null ? "" : request.getParameter("fidCuentaInversion");
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i004tesComprasInversionModificar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" src="../../Librerias/Javascript/crearElementos.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script language="JavaScript" src="../../Librerias/Javascript/tabla.js" type="text/javascript">
    </script>
    
    <script type="text/javascript" language="javascript">
      function inicia() {
        //alert(document.getElementById('ihsaldo').value+' - '+sumarImportes());
        document.getElementById('ihsaldo').value = parseFloat(document.getElementById('ihsaldo').value) + parseFloat(sumarImportes());
        //alert(document.getElementById('ihsaldo').value);
        document.getElementById('ihSaldoInicial').value = document.getElementById('ihsaldo').value;
        sumarSaldos();
      }
    </script>
    <script type="text/javascript" language="javascript">
      var enviado = false;
      var validaSal = true;
      
      var tamanio= new Array();
      var padre = new Array();
      var ind=-1;
      
      function getElemento(objPadre,nombreHijo) {
        ind++;
        padre[ind] = objPadre; 
        nodos = padre[ind].childNodes;
        var nodo = objPadre;
        for(tamanio[ind]=0; tamanio[ind]<nodos.length && nodo.name!=nombreHijo; tamanio[ind]++) {
          if(nodos[tamanio[ind]].name==nombreHijo)
            nodo = nodos[tamanio[ind]];
          else {
            nodo = getElemento(nodos[tamanio[ind]],nombreHijo);
            nodos = padre[ind].childNodes;
          }
        }
        ind--;
        return nodo;
      }
      
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
      
      function agregarEvento(componente,evento,funcion,parametros) {
        if (componente.addEventListener){
          componente.addEventListener(evento,function() {funcion(parametros);},false);
        } else if(componente.attachEvent){
          componente.attachEvent('on'+evento, function() {funcion.call(parametros, parametros);});
      	}
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
      
      function sumarImportes() {
        saldo = 0.00;
        ipts = document.getElementsByName('importe');
        for(x=0; x<ipts.length; x++) {
            if(gpn(ipts[x],"trDetalle").syle.display=='') {
              saldo += parseFloat(quitarComas(ipts[x].value));
            }
        }
        return saldo;
      }
      
      function sumarSaldos() {
        saldo = document.getElementById('ihsaldo').value;
        
        divm = document.getElementById('divMonto');
        //alert(saldo + ' - ' + sumarImportes());
        saldo = saldo - sumarImportes();
        
        //document.getElementById('ihsaldo').value = redondear(saldo,2);
        //alert(saldo);
        divm.innerHTML = ponerComas(redondear(saldo,2));
        //alert(document.getElementById('ihsaldo').value);
      }
      
      function calcularSaldo(tis) {
        ren = tis.parentNode.parentNode;
        titacc = getElemento(ren,'titulosAcciones');
        valacc = getElemento(ren,'valor');
        impacc = getElemento(ren,'importe');
        if(valacc.value != '' && titacc.value != '') {
          
          titacc.value = quitarComas(titacc.value);
          valacc.value = quitarComas(valacc.value);        
          
            impacc.value = ponerComas(redondear((titacc.value * valacc.value),2));
          sumarSaldos();  
          titacc.value = ponerComas(titacc.value);
          valacc.value = ponerComas(valacc.value);
        } else {
          impacc.value = '';
        }
      }
      
      function cambiaCuenta() {
        validaSal = false;
      }
      
      function createImg(ide,name,src) {
        var elemento  = document.createElement("img");
        elemento.src= src;
        elemento.name = name;
        elemento.id   = ide;
        return elemento;
      }
      
      function validarSaldo() {
        
          cuentaInversion = document.getElementById("idCuentaInversion");
          fechaInversion = document.getElementById("fecha");
          validaSal = true;
          //alert("entra ini");
          if(cuentaInversion.value!='' && fechaInversion.value!='') {
            //alert("entra");
            loadSource('capaSaldo',null,'i004tesComprasInversionCapaSaldo.jsp?','idCuentaInversion='+cuentaInversion.value+'&fecha='+fechaInversion.value+'&nombreCuenta='+cuentaInversion.options[cuentaInversion.selectedIndex].text);
            
          } else
            alert('Debe seleccionar una cuenta y una fecha para poder asignar documentos');
        
      }
      
      function regresaValidarMonto(isMonto) {
        //if(isMonto) 
        //  aregarRegistro();
        if(document.getElementById("fecha").value == document.getElementById("ihFechaInversion").value) {
          document.getElementById('ihsaldo').value = document.getElementById('ihSaldoInicial').value;
        }
        sumarSaldos();
      }
    
      function aregarRegistro() {
       
          tipoValores = document.getElementById("hIdTipoValores");
          itexPat = document.getElementById("itpat");
          var tabla = document.getElementById('tbDocumentos');
          var lRen = tabla.rows.length;
          var fila = tabla.insertRow(lRen);
          fila.id = 'trDetalle';
          var tipoFirma;
          
           try {
              var inpt;
              var col  = fila.insertCell(0);
                tipoValor = tipoValores.cloneNode(true);
                tipoValor.id   = "idTipoValores";
                tipoValor.name = "idTipoValores";
                col.appendChild(tipoValor);  
                col.appendChild(crearHidden('idDetalleCompra','idDetalleCompra','')); 
                col.appendChild(crearHidden('acciondet','acciondet','0')); 
              col = fila.insertCell(1); fila.align='center';
                titacc = itexPat.cloneNode(true); 
                titacc.id="titulosAcciones"; titacc.name="titulosAcciones"; titacc.size=12; titacc.maxlen=12; titacc.style.textAlign='right';
                //inpt = crearInput('titulosAcciones','titulosAcciones',''); inpt.className = "cajaTexto"; inpt.size=12;inpt.maxlen=12; inpt.style.textAlign='right';
                //agregarEvento(inpt,"keypress",validaNumero,inpt);
                col.appendChild(titacc);
              col = fila.insertCell(2);
                titacc = itexPat.cloneNode(true); 
                titacc.id="valor"; titacc.name="valor"; titacc.size=17; titacc.maxlen=17; titacc.style.textAlign='right';
                //inpt = crearInput('valor','valor',''); inpt.className = "cajaTexto"; inpt.size=17;inpt.maxlen=17; inpt.style.textAlign='right';
                //agregarEvento(inpt,"keypress",validaNumero,inpt);
                col.appendChild(titacc);
              col = fila.insertCell(3);
                inpt = crearInput('importe','importe',''); inpt.className = "cajaTexto"; inpt.size=17;inpt.maxlen=17; inpt.style.textAlign='right'; inpt.readOnly='true';
                col.appendChild(inpt);
              col = fila.insertCell(4);
                img = createImg("imgs","imgs","../../Librerias/Imagenes/botonEliminar2.gif");
                //alert(img.id);alert(img.name);
                agregarEvento(img,"click",eliminarReg,img);
                col.appendChild(img);
          } catch(e) {
              alert(e.message);
          }
       
      }
      
      var elimina = new Array();
      
      function eliminarReg(tis) {
        getElemento(gpn(tis,"trDetalle"),"acciondet").value="2";
        gpn(tis,"trDetalle").style.display="none";
        /*tb = document.getElementById("tbDocumentos");
        saldo = document.getElementById('ihsaldo').value;
        ren = tis.parentNode.parentNode;
        if(getElemento(ren,'idDetalleCompra').value != '') {
         elimina[elimina.length] = getElemento(ren,'idDetalleCompra').value;
         //document.getElementById('ihsaldo').value =parseFloat(saldo) + parseFloat(getElemento(ren,'importe').value);
        }
        tb.deleteRow(ren.rowIndex);
        armaEliminados();*/
        sumarSaldos();
      }
    </script>
    <script type="text/javascript" language="javascript">
      function validarFormulario() {
        mensaje='-------- A L E R T A ----------\n';
        correcto = true;
        if(document.getElementById("idCuentaInversion").value=='') {
          mensaje += 'Debe seleccionar una cuenta de inversión\n';
          correcto = false;
        }
        if(document.getElementById("idTipoInversion").value=='') {
          mensaje += 'Debe seleccionar un tipo de inversión\n';
          correcto = false;
        }
        if(document.getElementById("idTipoInversion").value=='') {
          mensaje += 'Debe seleccionar la fecha de inversión\n';
          correcto = false;
        }
        if(document.getElementById("plazo").value=='') {
          mensaje += 'Debe capturar un plazo\n';
          correcto = false;
        }
        if(document.getElementById("tasa").value=='') {
          mensaje += 'Debe capturar la tasa\n';
          correcto = false;
        }
        if(document.getElementById("vencimiento").value=='') {
          mensaje += 'Debe seleccionar fecha de vencimiento\n';
          correcto = false;
        }
        if(document.getElementById("numOperacion").value=='') {
          mensaje += 'Debe capturar un numero de operacion\n';
          correcto = false;
        }
        tiaccs = document.getElementsByName("titulosAcciones");
        if(tiaccs.length > 0) {
          for (i = 0; i < tiaccs.length; i++)  {
            if(document.getElementsByName("titulosAcciones")[i].value=='' || document.getElementsByName("valor")[i].value=='') {
              mensaje += 'El número de documento y el valor del documento deben contener una cantidad\n';
              correcto = false;
              break;
            }
          }
          
        } else {
          mensaje += 'Debe agregar documentos a la inversión\n';
          correcto = false;
        } 
        /*if(parseDouble(document.getElementById("divMonto").innerHTML)<0) {
          mensaje += 'No se puede realizar la inversión ya que no se cuenta con el recurso suficiente\n';
          correcto = false;
        }*/
        
        if(!correcto) {
          alert(mensaje);
        } else 
          validarCompra();
      }
      
      function armaEliminados() {
        for(x=0; x<elimina.length; x++) {
          eliminados.value += elimina[x] + ',';
        }
        eliminados.value = eliminados.value.substring(0,eliminados.value.length-1);
        //alert(eliminados.value);
      }
      
      function validarCompra() {
          fechaInversion = document.getElementById("fecha");
          plazoInversion = document.getElementById("plazo");
          venceInversion = document.getElementById("vencimiento");
      
            loadSource('capaSaldo',null,'i004tesComprasInversionCapaValidarCompra.jsp?','fecha='+fechaInversion.value+'&plazo='+plazoInversion.value+'&vencimiento='+venceInversion.value);
      }
      
      function regresaValidaCompra(msg) {
        if(msg!='')
          alert(msg);
        else
          ir();
      }
      
      function ir() {
        document.getElementById('idCuentaInversion').disabled = false;
        document.getElementById("form1").action='i004tesComprasInversionControl.jsp';
        document.getElementById("form1").target="_blank";
        enviar();
      }
      
      function cancelar() {
        document.getElementById("form1").action='i004tesComprasInversionFiltro.jsp';
        document.getElementById("form1").target="_self";
        enviar();
      }
      
      function regresaDeControl(val) {
        if(val==true) {
          document.getElementById("form1").action='i004tesComprasInversionFiltro.jsp';
          document.getElementById("form1").target='_self';
          document.getElementById("form1").submit();
        } else
          enviado = false;
          
      }
      
      function enviar() {
        if(!enviado) {
          document.getElementById("form1").submit();
          enviado = true;
        }
      }
    </script>
  </head>
  <body onload="inicia()">
    <form action="i004tesComprasInversionFiltro.jsp" method="POST" id="form1" name="form1">
      <%util.tituloPagina(out,"Tesorería central","Inversiones o compras","Modificar",true);%>
      
      <input type="hidden" id="accion" name="accion" value="2">
      <input type="hidden" id="eliminados" name="eliminados" value="2">
      <input type="hidden" id="idCompraInversion" name="idCompraInversion" value="<%=compraInversion.getIdCompraInversion()%>">
      <input type="hidden" id="ffecha" name="ffecha" value="<%=ffecha%>">
      <input type="hidden" id="fidCuentaInversion" name="fidCuentaInversion" value="<%=fidCuentaInversion%>">
      <input type="hidden" id="ihSaldoInicial" name="ihSaldoInicial" value="">
      <input type="hidden" id="ihFechaInversion" name="ihFechaInversion" value="<%=compraInversion.getFecha()%>">
      <IFrame style="display:none" name="bufferCurp" id="bufferCurp">
        </IFrame>
      <div style="display:none">
        <select id='hIdTipoValores' name='hIdTipoValores' onchange='' class="cajaTexto">
        <%CRSComboBox(tiposValores, out,"ID_TIPO_VALORES","DESCRIPCION","");%>  
        </select>
        <input type="text" class="cajaTexto" name="itpat" id="itpat" value="" onkeypress="validarFlotante(event,this)" onchange="calcularSaldo(this)">
      </div>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td class="negrita" align="right">Cuenta de inversi&oacute;n</td>
            <td><select id='idCuentaInversion' name='idCuentaInversion' onchange='cambiaCuenta()' class='cajaTexto' disabled>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO",compraInversion.getIdCuentaInversion());%>
            </select>
               </td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tipo inversi&oacute;n</td>
            <td><select id='idTipoInversion' name='idTipoInversion' onchange='' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(tiposInversion, out,"ID_TIPO_INVERSION","DESCRIPCION",compraInversion.getIdTipoInversion());%>
            </select></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de inversi&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="<%=compraInversion.getFecha()%>" onblur="validarSaldo()" readonly size="13"> &nbsp; <a  
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Plazo</td>
            <td><input type="text" class="cajaTexto" name="plazo" id="plazo" value="<%=compraInversion.getPlazo()%>" size="3" maxlength="3"></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tasa</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="<%=compraInversion.getTasa()%>" size="9" maxlength="9" onkeypress="validarFlotante(event,this)">%</td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de vencimiento</td>
            <td><input type="text" class="cajaTexto" name="vencimiento" id="vencimiento" value="<%=compraInversion.getVencimiento()%>" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].vencimiento')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td class="negrita" align="right">N&uacute;mero de operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="numOperacion" id="numOperacion" value="<%=compraInversion.getNumOperacion()%>" size="22" maxlength="21"></td>
          </tr>
        </tbody>
      </table>
      <br><br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td nowrap="false"><div id="capaSaldo">El saldo de la cuenta seleccionada es: 
               <div id="divMonto"><%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,monto)%>
               </div> 
               <input type="hidden" id="ihsaldo" name="ihsaldo" value="<%=monto%>">
              </div> 
            </td>
          </tr>
        </tbody>
      </table>
      <table align='center' class='general' id="tbDocumentos">
        <thead>
          <tr class="azulCla">
            <td colspan="4">Listado de documentos de la compra</td>
            <td align="right"><img src='../../Librerias/Imagenes/botonAgregar2.gif' id='' name='' alt="Agregar documento" onclick="aregarRegistro()"> </td>
          </tr>
          <tr>
            <th class='azulObs'>Tipo valor</th>
            <th class='azulObs'>No de titulos o acciones</th>
            <th class='azulObs'>Valor del t&iacute;tulo o acci&oacute;n</th>
            <th class='azulObs'>Importe</th>
            <th class='azulObs'>Operaciones</th>
          </tr>
        </thead>
        <tbody>
          <% try  {
              while(detalleCompra.next()) {%>
                <tr id="trDetalle">
              <td><select id='idTipoValores' name='idTipoValores' onchange='' class="cajaTexto">
                <%CRSComboBox(tiposValores, out,"ID_TIPO_VALORES","DESCRIPCION",detalleCompra.getString("Id_Tipo_Valores")) ;%>  
              </select>
              <input type="hidden" id="idDetalleCompra" name="idDetalleCompra" value="<%=detalleCompra.getString("id_detalle_compra")%>">
              <input type="hidden" id="acciondet" name="acciondet" value="0">
              </td>
              <td align="center"><input style="text-align:right" type="text" class="cajaTexto" size="12" maxlength="12"  onkeypress="validarFlotante(event,this)" onchange="calcularSaldo(this)" 
               name="titulosAcciones" id="titulosAcciones" value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompra.getDouble("titulos_acciones"))%>"></td>
              <td align="center"><input style="text-align:right" type="text" class="cajaTexto" size="17" maxlength="17"  onkeypress="validarFlotante(event,this)" onchange="calcularSaldo(this)" 
               name="valor" id="valor" value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompra.getDouble("valor"))%>"></td>
              <td align="center"><input style="text-align:right" type="text" class="cajaTexto" size="17" maxlength="17" name="importe" id="importe" readonly 
               value="<%=Numero.formatear(Numero.NUMERO_MILES_CON_DECIMALES,detalleCompra.getDouble("importe"))%>"></td>
              <td align="center"><img src='../../Librerias/Imagenes/botonEliminar2.gif' id='imgs' name='imgs' alt="Eliminar documento" onclick="eliminarReg(this)"></td>
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
            <td><input type="button" class="boton" value="Cancelar" onclick="cancelar()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>