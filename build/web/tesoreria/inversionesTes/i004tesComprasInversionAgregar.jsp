
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<%@ page import="sia.db.dao.DaoFactory" %>

<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="cuentasInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tiposInversion" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="tiposValores" class="sia.db.sql.SentenciasCRS" scope="page"/>
<%
  cuentasInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrCuentasInversion.inversiones");
  tiposInversion.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTcTiposInversion.inversiones");
  tiposValores.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTcTipoValores.inversiones");

%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>i004tesComprasInversionAgregar</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="javascript" src="../../Librerias/Javascript/crearElementos.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript">
    </script>
    <script type="text/javascript" language="javascript">
      var enviado = false;
      var validaSal = false;
      
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
      
      function sumarSaldos(tis) {
        saldo = document.getElementById('ihsaldo').value;
        ipts = document.getElementsByName('importe');
        divm = document.getElementById('divMonto');
        for(x=0; x<ipts.length; x++) {
          saldo -= parseFloat(quitarComas(ipts[x].value));
        }
        divm.innerHTML = ponerComas(redondear(saldo,2));
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
          sumarSaldos(tis);  
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
          if(cuentaInversion.value!='' && fechaInversion.value!='') {
            loadSource('capaSaldo',null,'i004tesComprasInversionCapaSaldo.jsp?','idCuentaInversion='+cuentaInversion.value+'&fecha='+fechaInversion.value+'&nombreCuenta='+cuentaInversion.options[cuentaInversion.selectedIndex].text);
            
          } 
    
        
      }
      
      function regresaValidarMonto(isMonto) {
        
        
      }
    
      function aregarRegistro() {
        
          tipoValores = document.getElementById("hIdTipoValores");
          itexPat = document.getElementById("itpat");
          cuentaInversion = document.getElementById("idCuentaInversion");
          fechaInversion = document.getElementById("fecha");
          var tabla = document.getElementById('tbDocumentos');
          var lRen = tabla.rows.length;
          var fila = tabla.insertRow(lRen);
          var tipoFirma;
          
          try {
            if(cuentaInversion.value!='' && fechaInversion.value!='') {
           
              var inpt;
              var col  = fila.insertCell(0);
                tipoValor = tipoValores.cloneNode(true);
                tipoValor.id   = "idTipoValores";
                tipoValor.name = "idTipoValores";
                col.appendChild(tipoValor);          
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
            } else
              alert('Debe seleccionar una cuenta y una fecha para poder asignar documentos');
          
          } catch(e) {
              alert(e.message);
          }
       
      }
      
      
      
      function eliminarReg(tis) {
        tb = document.getElementById("tbDocumentos");
        ren = tis.parentNode.parentNode;
        tb.deleteRow(ren.rowIndex);
        sumarSaldos(tis);
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
        document.getElementById("form1").action='i004tesComprasInversionControl.jsp';
        document.getElementById("form1").target="_blank";
        enviar();
      }
      
      function cancelar() {
        document.getElementById("form1").action='i004tesComprasInversionAgregar.jsp';
        document.getElementById("form1").target="_self";
        enviar();
      }
      
      function enviar() {
        if(!enviado) {
          document.getElementById("form1").submit();
          enviado = true;
        }
      }
      
      function regresaDeControl(val) {
        if(val==true) {
          form1.action='i004tesComprasInversionAgregar.jsp';
          form1.target='_self';
          form1.submit();
        } else
          enviado = false;
          
      }
    </script>
  </head>
  <body>
    <form action="i004tesComprasInversionAgregar.jsp" method="POST" id="form1" name="form1">
      <%util.tituloPagina(out,"Tesorería central","Inversiones o compras","Agregar",true);%>
      <input type="hidden" id="accion" name="accion" value="1">
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
            <td><select id='idCuentaInversion' name='idCuentaInversion' onchange='' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(cuentasInversion, out,"ID_CUENTA_INVERSION","CONTRATO_CUENTA,NOMBRE_CORTO","");%>
            </select></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tipo inversi&oacute;n</td>
            <td><select id='idTipoInversion' name='idTipoInversion' onchange='' class='cajaTexto'>
              <option value=''>-Seleccione-</option>
              <%CRSComboBox(tiposInversion, out,"ID_TIPO_INVERSION","DESCRIPCION","");%>
            </select></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de inversi&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="fecha" id="fecha" value="" onblur="validarSaldo()" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fecha')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Plazo</td>
            <td><input type="text" class="cajaTexto" name="plazo" id="plazo" value="" size="3" maxlength="3"></td>
          </tr>
          <tr>
            <td class="negrita" align="right">Tasa</td>
            <td><input type="text" class="cajaTexto" name="tasa" id="tasa" value="" size="9" maxlength="9" onkeypress="validarFlotante(event,this)">%</td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha de vencimiento</td>
            <td><input type="text" class="cajaTexto" name="vencimiento" id="vencimiento" value="" readonly size="13"> &nbsp; <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
            onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].vencimiento')">
           <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a></td>
          </tr>
          <tr>
            <td class="negrita" align="right">N&uacute;mero de operaci&oacute;n</td>
            <td><input type="text" class="cajaTexto" name="numOperacion" id="numOperacion" value="" size="22" maxlength="21"></td>
          </tr>
        </tbody>
      </table>
      <br><br>
      <table align='center'>
        <thead></thead>
        <tbody>
          <tr>
            <td nowrap="false"><div id="capaSaldo"></div> </td>
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