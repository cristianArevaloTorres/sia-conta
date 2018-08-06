<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ include file="../../../Librerias/Funciones/utilscpv.jsp"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsTiposPoliza" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasFormulario</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <script type="text/javascript" src="../../../Librerias/Javascript/catalogos.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        
        
        <script language="javascript" type="text/javascript">
          
          function soloLetras(e) { // 1
            tecla = (document.all) ? e.keyCode : e.which; // 2
            if (tecla==8) return true; // 3
            patron =/[A-Za-zÑñ\s]/; // 4
            te = String.fromCharCode(tecla); // 5
            if(!patron.test(te)); // 6
              (document.all) ? event.returnValue = false : event.preventDefault();
          } 
          
          function soloNumeros(e) { // 1
            tecla = (document.all) ? e.keyCode : e.which; // 2
            if (tecla==8) return true; // 3
            patron =/^[0-9\t]$/; // 4
            te = String.fromCharCode(tecla); // 5
            if(!(tecla >= 95 && tecla <= 106) && !patron.test(te)) {// 6 no es Numero
                (document.all) ? event.returnValue = false : event.preventDefault();
            }
          } 
          
          function crearCelCheck(fila, nombre, valor, noCelda, alineacionCelda) {
            var celda = fila.insertCell(noCelda);
            celda.className = "chb";
            celda.align = alineacionCelda;
            celda.appendChild(crearCheck(nombre, valor));
            celda.appendChild(crearHidden("hdCrea", 0));
          }
          
          function crearCheck(nombre, valor) {
             
            var chb = document.createElement("INPUT");
            chb.type = "checkbox";
            chb.value = valor;
            chb.name = nombre;
            chb.className = "chb";
            chb.id = chb.name;  
            return chb;
          }
          
          function crearHidden(nombre, valor) {
            var hdCuenta = document.createElement("INPUT");
            hdCuenta.type = "hidden";
            hdCuenta.value = valor;
            hdCuenta.name = nombre;
            hdCuenta.id = nombre;
            return hdCuenta;
          }
          
          function obtNumReg() {
            checks = document.getElementsByName('chbEliminar');
            return checks.length;
          } 
          
          function crearNivelCuenta(fila, nombre, valor, noCelda, alineacionCelda, tamanio, maxLong, soloLectura) {
            var celda = fila.insertCell(noCelda); 
            celda.width="4%";
            celda.align = alineacionCelda;
            celda.className = "txt";
            if(!soloLectura ) {
              validaAgregadoVacioPrimeraLetra(valor);
            } 
            if(valor!="") {
              celda.appendChild(crearText("txt"+nombre+noCelda, valor, tamanio, maxLong, soloLectura));
              celda.appendChild(crearHidden("hd"+nombre+noCelda, soloLectura? 0 : 1));
              celda.appendChild(crearHidden("hd"+"Cfg"+noCelda, 0));
            } else {
              celda.appendChild(crearHidden("txt"+nombre+noCelda, valor, tamanio, maxLong, soloLectura));
              celda.appendChild(crearHidden("hd"+nombre+noCelda, soloLectura? 0 : 1));
              celda.appendChild(crearHidden("hd"+"Cfg"+noCelda, 0));
            }
            
            
          }
          
          function crearText(nombre, valor, tamanio, maxLong, soloLectura) {
            
            var txtCuenta = document.createElement("INPUT");
            txtCuenta.type = "text";
            txtCuenta.value = valor;
            txtCuenta.name = nombre;
            txtCuenta.id = nombre;
            txtCuenta.readOnly = soloLectura;
            txtCuenta.size = tamanio;
            txtCuenta.className = "cajaTexto";  
            return txtCuenta;
          }
          
          function crearRadiosx(fila, nombreOrigen, nombreDestino, noCelda, alineacionCelda) {
            celda = fila.insertCell(noCelda);
            celda.align = alineacionCelda;
            radios = document.getElementsByName(nombreOrigen);
            for(i = 0; i < radios.length; i++) {
              crearRadio(celda, nombreDestino, radios[i].value, true, radios[i].checked);
              creaEspacio(celda);
            }
          }
          
          function creaEspacio(celda) {
            var espacio = document.createElement("span");
            espacio.innerHTML = "&nbsp;";
            celda.appendChild(espacio);
          }
          
          function crearRadio(celda, nombre, valor, soloLectura, checado) {
              check = checado?' checked':'';
              readO = soloLectura?' readOnly':'';
              var radioHtml = '<input type="radio" name="' + nombre + '" value="'+valor+'"'+check+readO+'/>';
              var radioFragment = document.createElement('div');
              radioFragment.innerHTML = radioHtml;         
              celda.appendChild(radioFragment.firstChild);   
          }
          
          var arreg = new Array();
          
          function obtUltimo(vec) {
            regresa = 0;
            if(vec!=null && vec.length>0) {
              regresa = parseInt(vec[vec.length-1])+1;
            }
            arreg[vec.length] = regresa;
            return regresa;
          }
          
          function validaAgregadoVacioPrimeraLetra(valor) {
            if(valor != '') {
              if(!isLetter(valor.charAt(0))) {
                throw new Error("Las variables deben empezar con letra. ------------Favor de verificar------------ ");
                regresa = false;
              }
            } else {
              throw new Error("Las variables de subcuentas, de importe o referencia no deben estar vacias  ----- Favor de verificar -----");
            }
          }
        
          function AgregarCuenta2(){
            var Tabla = document.getElementById("TablaPrueba");
            var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
            var fila = Tabla.insertRow(lRenglones);
            var numCol = 0;
            val = obtUltimo(arreg);
            tamanios = document.getElementById("configura").value.split(",");

            try {
              crearCelCheck(fila, "chbEliminar", "" + val, numCol++, "center");
              for(i=1; i<=tamanios.length; i++) {
                if(i==1)
                  crearNivelCuenta(fila, "Cta", document.getElementById("txtCtaNivel"+i).value, numCol++, "center", 5, 5, true);
                else
                  crearNivelCuenta(fila, "Cta", document.getElementById("txtCtaNivel"+i).value, numCol++, "center", 10, 25, document.getElementById("chbCtaNivel"+i).checked?false:true);    
              }
              
              crearNivelCuenta(fila, "Cta", formulario.txtImporte.value, numCol++, "center", 15, 10, false); 
              crearNivelCuenta(fila, "Cta", formulario.txtReferencia.value, numCol++, "center", 10, 10, false);
              
              crearRadiosx(fila, "chbTipoOper", "chbTipoOper"+val, numCol++, "center");
              crearRadiosx(fila, "chbPosNeg", "chbPosNeg"+val, numCol++, "center");
              
            } catch(e) {
              alert(e.name + ": " + e.message);
              eliminarElem(obtNumReg()-1);
            }
          }
        </script>
        
        <script language="JavaScript" type="text/javascript">
          var correcto = true;
          function obtElim() {
            var elementos='';
            checks = document.getElementsByName('chbEliminar');
            band = false;
            for(i=0; i < checks.length; i++) {  
              if(checks[i].checked) {
                elementos = elementos + i + ',';
                band = true;
              }
            }
            if(!band) {
              alert('Debera seleccionar por lo menos un registro');
              correcto = false;
            } else {
              elementos = elementos.substring(0,elementos.length-1);
            }
            return elementos
          }
          
          function eliminarElem(elem) {
            tabla = document.getElementById('TablaPrueba');
            elem = elem+'';
            try {
            if(elem!='') { 
              tabla.deleteRow(parseInt(elem)+1);
              arreg.splice(elem,1);
            }
            }catch(e){
              alert("Error al borrar"+e.message);
            }
          }
        
          function eliminarElementos() {
            elementos = obtElim();
            if(correcto) {
              valores = elementos.split(',');
              for(i=valores.length-1; i >= 0; i--) {
                eliminarElem(valores[i]);
              }
            }
              
          }
        </script>
        
        <script language="JavaScript" type="text/javascript">
        
        function regresa() {
        }
        
        function relleCeros(nivel){
          var cancela = 0;
          if (nivel ==0){
            cancela = 1; 
            nivel= 1;
          }
          var regresa;
          var nivelMax = document.getElementById("nivelesConfigura").value;
          var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
          var nivelInt;
          var vacio;
          nivelInt = parseInt(nivel)+1;
           var ceros;
          tamanios = document.getElementById("configura").value.split(",");
          ceros = tamanios[nivel-1];
           vacio = document.getElementById('txtCtaNivel'+nivel).value == '';
           o="00000".substring(0,ceros-eval("formulario.txtCtaNivel"+nivel+".value.length"))+eval("formulario.txtCtaNivel"+nivel+".value");
           eval("formulario.txtCtaNivel"+nivel).value=o;
           eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
           if (eval("formulario.lstNivel"+nivel+".selectedIndex") == -1){
             divImporte.style.display='none';
             
             if (cancela == 0){
               if(!vacio)
                alert ("clave "+o+" no valida");
               document.getElementById("divTipoOper").style.display = 'none';
               document.getElementById("divImporte").style.display = 'none';
               document.getElementById("divAgregar").style.display = 'none';
             }
             eval("formulario.txtCtaNivel"+nivel).value= "";
             eval("formulario.txtCtaNivel"+nivel).focus();
             eval("formulario.lstNivel"+nivel+".value = ''");
          
             for (i = nivelInt; i <= nivelMax; i++){
       
               eval("formulario.txtCtaNivel"+i).value='';
               eval("trlstNivel"+i+".style.display='none'");
               eval("formulario.lstNivel"+i+".value = ''");
             }
             
             regresa =  false;
           }else{
              eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
              regresa = true;
           }
           return regresa;
        }
        
        function muestraNivel2(nivelx){
          
          var ceros = '00000';
          params = "";
          if(nivelx != 1 && nivelx <= document.getElementById("nivelesConfigura").value ) {
            desmarcarChecks(nivelx);
            selNivelCta(nivelx);
          }
          for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx; i++)  {
            document.getElementById('txtCtaNivel'+i).value = document.getElementById('lstNivel'+i).value
            params = params + document.getElementById("txtCtaNivel"+i).value;
          }  
          
          loadSource('capaNivel2',null,'c713FormasCapaCuentas.jsp?nivRep='+nivelx+'&ctas='+params+'&configura='+document.getElementById("configura").value+'&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value,bufferFrame);
        }
        
        function asignaValorRegreso(nivelx) {
          var nivelu = parseInt(nivelx)-1;
          if(document.getElementById('hdCtaNivel'+nivelu) != null) {
                document.getElementById('hdCtaNivel'+nivelu).value = document.getElementById('txtCtaNivel'+nivelu).value;
          }
        }
        
        </script>
        
        <script language="JavaScript" type="text/javascript">
        
        
       
        function muestraDescCuenta(xnivel){
          var i = 0;
          var niv = parseInt(xnivel) +1;
          divs = document.getElementsByName("divsel");
          var c = parseInt(document.getElementById("nivelesConfigura").value)-1;
          for (i == 0; i <= c; i++){
         
            if (xnivel == i){
              divs[i].style.display =''; 
              document.getElementById('lstNivel'+niv).value = document.getElementById('txtCtaNivel'+niv).value;
             }else{
                divs[i].style.display ='none';
             }
          }
        }
        
        function selNivelCta(nivel) {
          tamanios = document.getElementById("configura").value.split(",");
          if(document.getElementById('chbCtaNivel'+nivel).checked == true) { 
            document.getElementById('txtCtaNivel'+nivel).size = 10;
            document.getElementById('txtCtaNivel'+nivel).maxLength = 25;
          } else {
            document.getElementById('txtCtaNivel'+nivel).value = document.getElementById('hdCtaNivel'+nivel).value;
            document.getElementById('txtCtaNivel'+nivel).size = parseInt(tamanios[nivel-1])+1;
            document.getElementById('txtCtaNivel'+nivel).maxLength = tamanios[nivel-1];
          }
        }
        
       
        </script>
        <script language="javascript" type="text/javascript">
          
          function validaPrimeraLetra(num) {
            valor = document.getElementById('txtCtaNivel'+num).value;
            if(valor != '') {
              if(!isLetter(valor.charAt(0)))
                alert("La variable debe empezar con letra");
                document.getElementById('txtCtaNivel'+num).focus();
            }
          }
          
          function vSN(num,e) {
            num=parseInt(num);
            if (num==1 || !document.getElementById("chbCtaNivel"+num).checked) {
              soloNumeros(e);
            }
          }
          
          function validaCambio(num) {
            num = parseInt(num);
            
            if (num==1 || !document.getElementById("chbCtaNivel"+num).checked) {
              if(relleCeros(num)) {
                
                muestraNivel2(num+1);
              }
            } else {
              validaPrimeraLetra(num);
            }
          }
          
          function desmarcarChecks(num) {
            hasta = document.getElementById("nivelesConfigura").value;
            for (i = num; i <= hasta && i != 1; i++)  {
              document.getElementById('chbCtaNivel'+i).checked = false;
            }
          }
          
          function limpiarCuenta() {
            document.getElementById("txtImporte").value='IMPORTE';
            document.getElementById("txtReferencia").value='REFERENCIA';
            chbpn = document.getElementsByName("chbPosNeg");
            chbpn[0].checked = true;
            chbpn[1].checked = false;
            chbto = document.getElementsByName("chbTipoOper");
            chbto[0].checked = true;
            chbto[1].checked = false;
            niveles = document.getElementById("nivelesConfigura").value;
            for (i = 1; i <= niveles; i++)  {
              if(i==1)
                document.getElementById("txtCtaNivel"+i).value='';
              else {
                document.getElementById('chbCtaNivel'+i).checked = false; document.getElementById('hdCtaNivel'+i).value=''; selNivelCta(i);}
            }
            validaCambio(1);
            
          }
          
                   
          function validarSiNumero(object)
          {
            div = document.getElementById('dopval');
            div.style.display = 'none';
            if(object.value!='') {
              numero = object.value.substring(object.value.length-1,object.value.length);
              if (!/\D/.test(numero)) {
                object.value = object.value.substring(0,object.value.length-1);
                div.style.display = '';
              }
            }  
          }
        </script>
        
        
        <script language="javascript" type="text/javascript">
          var mensajeError="";
          function enviaMisma() {
            formulario.action='';
            formulario.submit();
          }
          
          function armaEnvio() {
            var cad="";
            for(i=0; i < arreg.length; i++) {
              cad = cad + arreg[i]+',';
            }
            document.getElementById('registros').value = cad.substring(0,cad.length-1);
          }
          
          function validarTxtCta(nombre) {
            ctas = document.getElementsByName('txt'+nombre);
            hds = document.getElementsByName('hd'+nombre);
            if(hds != null) {
              for(i=0; i<hds.length; i++) {
                if(hds[i].value=='1')
                  validaAgregadoVacioPrimeraLetra(ctas[i].value);
              }
            }
          }
          
          function validarSubCuentas() {
            niveles = document.getElementById("nivelesConfigura").value;
            niveles = parseInt(niveles) + 2;
            for (o = 1; o <= niveles; o++)  {
              validarTxtCta('Cta'+o);
            } 
          }
          
          function validaFormulario() {
            msg = "-------------ALERTA -------------\n";
            band = true;
            if (document.getElementById("forma").value=='') {
              msg = msg + "Debe capturar la forma\n";
              band = false;
            }
            if (document.getElementById("concepto").value=='') {
              msg = msg + "Debe capturar el concepto\n";
              band = false;
            }
            if (document.getElementById("documentoFuente").value=='') {
              msg = msg + "Debe capturar el documento fuente\n";
              band = false;
            }
            if(arreg.length <= 1) {
              msg = msg + "La forma debe contar con al menos dos cuentas contables\n";
              band = false;
            }
            try {
              validarSubCuentas();
              
              if(mensajeError!="") {
                alert(mensajeError);
                band = false;
              }
              if(!band)
                alert(msg);
              else {
                armaEnvio();
                loadSource('capaNivel2',null,'c713FormasCapaValidaUniqueForma.jsp?forma='+document.getElementById('forma').value+'&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value,bufferFrame);
              }
            } catch(e) {
              alert(e.name + ": " + e.message);
            }
          }
          
          function regresaDeCapa(mensajeError) {
            if(mensajeError == "") {
               formulario.btnAceptar.disabled=true;
               formulario.submit();
            } else {
              alert(mensajeError);
            }
            
          }
        </script>
        
        
  </head>
  <body onLoad="muestraNivel2(1);">
    <%
        
        crsConfiguraCta.addParam("ejercicio",controlRegistro.getEjercicio());
        crsConfiguraCta.addParam("idCatalogo",controlRegistro.getIdCatalogoCuenta());
        crsConfiguraCta.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.RfTcDetalleConfCve");
        StringBuffer cadenaConfiguracion = new StringBuffer();
        while(crsConfiguraCta.next()) {
          if(crsConfiguraCta.isLast())
            cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio"));
          else
            cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio")).append(",");
          
        }
        crsConfiguraCta.beforeFirst();
        //int[] tam = {5,4,4,4,4,4,4,4};
        crsTiposPoliza.registros(DaoFactory.CONEXION_CONTABILIDAD,"select tipo_poliza_id, descripcion, abreviatura from RF_TC_TIPOS_POLIZAS where tipo_poliza_id in (1,2,3,4) ");
        
        int nivelesConfigura = crsConfiguraCta.size();
        int porcentajeCelda = 60 / nivelesConfigura;
    %>
    <%util.tituloPagina(out,"Contabilidad","Agrega formas contables","Agregar",true);%>
    <form method="POST" action="c713FormasControl.jsp" name="formulario" id="formulario">
    
      <IFrame style="display:none" name = "bufferFrame">
      </IFrame>
      <input type="hidden" name="accion" id="accion" value="1"/>
      <input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" id="hLsEjercicio" name="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" id="unidadEjecutora" name="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" id="entidad" name="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" id="ambito" name="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" id="registro" name="registro" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>"/>
      <input type="hidden" id="esmanual" name="esmanual" value="<%=controlRegistro.getPagina().equals("formasManualesNew")?1:0%>"/>
      <input type="hidden" id="configura" name="configura" value="<%=cadenaConfiguracion.toString()%>"/>
      <input type="hidden" id="nivelesConfigura" name="nivelesConfigura" value="<%=nivelesConfigura%>"/>
      <input type="hidden" id="registros" name="registros" value=""/>
      
      <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
        </tr>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      
      <table border="0" cellpadding="5">
        <thead></thead>
        <tr>
          <td>Forma:</td>
          <td><input type="text" id="forma" name="forma" size="3" maxlength="2" class="cajaTexto" onkeyup="validarSiNumero(this)"/>
          <div id="dopval" style="display:none"><font color="red">Solo se permiten letras</font></div> </td>
          <td rowspan="3" valign="top">Concepto:</td>
          <td rowspan="3"><textarea cols="40" rows="5" id="concepto" name="concepto" class="cajaTexto"></textarea></td>
        </tr>
        <tr>
          <td>Documento fuente:</td>
          <td><input type="text" id="documentoFuente" name="documentoFuente" size="50" maxlength="250" class="cajaTexto"/></td>
        </tr>
        <tr>
          <td>Tipo p&oacute;liza:</td>
          <td>
            <select class="cajaTexto" id="tipoPolizaId" name="tipoPolizaId"> 
              <%CRSComboBox(crsTiposPoliza, out,"tipo_poliza_id","descripcion","");%>
            </select>
          </td>
        </tr>
      </table>
      <br>
      <H3>Detalle de la forma contable</H3>
      <a href="#marca1" id="refmarc" name="nrefmarc"> </a> 
      <table width="100%" class="resultado" cellspacing="3">
        <thead></thead>
        <tr>
          <td>
            <table id="tAgregaCta"> 
              <thead></thead>
              <tr>
                <td width="100">Agregar Cuenta: </td>
                <%for (int i = 1; i <= nivelesConfigura; i++)  {
                  crsConfiguraCta.absolute(i);
                  if(i == 1) {
                  %><td><div id="trlstNivel<%=i%>">
                        <input type='text' name='txtCtaNivel<%=i%>' id='txtCtaNivel<%=i%>' size='<%=crsConfiguraCta.getInt("tamanio")+1%>'  maxlength='<%=crsConfiguraCta.getInt("tamanio")%>' 
                          class='cajaTexto' onFocus ="muestraDescCuenta('<%=i-1%>');" value='' title="capture <%=crsConfiguraCta.getString("descripcion")%>"
                          tabindex='<%=i%>' onChange="validaCambio('<%=i%>')" onKeyDown="if(event.keyCode==13){ event.keyCode=35; validaCambio('<%=i%>');} soloNumeros(event) " 
                          onkeyup="document.getElementById('lstNivel<%=i%>').value = document.getElementById('txtCtaNivel<%=i%>').value">
                      </div>
                    </td>
                  <%
                  } else {%>
                    <td><div id="trlstNivel<%=i%>" style="display:none">
                      <input type="hidden" name="hdCtaNivel<%=i%>" id='hdCtaNivel<%=i%>' />
                      <input type="checkbox" name="chbCtaNivel<%=i%>" id='chbCtaNivel<%=i%>' class="" onClick="selNivelCta(<%=i%>)"/>
                       <input type='text' name='txtCtaNivel<%=i%>' id='txtCtaNivel<%=i%>'  title="capture <%=crsConfiguraCta.getString("descripcion")%>" 
                       size='<%=crsConfiguraCta.getInt("tamanio")+1%>'  maxlength='<%=crsConfiguraCta.getInt("tamanio")%>' class='cajaTexto' value='' 
                       onFocus="muestraDescCuenta('<%=i-1%>');"
                       tabindex='<%=i%>' onChange="validaCambio('<%=i%>')" 
                       onKeyDown=" if(event.keyCode==13){ event.keyCode=35; validaCambio('<%=i%>');} vSN('<%=i%>',event)" 
                       onkeyup="document.getElementById('lstNivel<%=i%>').value = document.getElementById('txtCtaNivel<%=i%>').value">
                    </div>
                </td><%
                  } //if
                } //for
                %>
              </tr>
            </table >
          </td>
          <td class="azulObs">
            <div id="divTipoOper" style="display:none">
              <input type="radio" name="chbTipoOper" id='chbTipoOper' value="0" checked>Debe</input>
              <input type="radio" name="chbTipoOper" id='chbTipoOper' value="1">Haber</input>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <table width="498" id="tNiveles">
              <thead></thead>
              <tr>
                <td width="100">Descripci&oacute;n:</td>
                <td width="386">
                  <%for (int i = 1; i <= nivelesConfigura; i++)  {
                    if(i==1) {
                      %><div id ='divsel' name="divsel">
                           <select  name='lstNivel<%=i%>' id="lstNivel<%=i%>" class= 'cajaTexto' onChange="muestraNivel2(<%=i+1%>); txtCtaNivel<%=i%>.value =lstNivel<%=i%>.value; ">
                           </select>
                        </div>
                      <%
                    } else {
                      %><div id = 'divsel' name="divsel" style="display:none">
                            <select name='lstNivel<%=i%>' id='lstNivel<%=i%>' class= 'cajaTexto' onChange='javascript: muestraNivel2(<%=i+1%>); txtCtaNivel<%=i%>.value =lstNivel<%=i%>.value; '></select>
                        </div>
                      <%
                    }
                        
                  }
                  %>
                </td> 
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td colspan="2">
            <div id="divImporte">
            <table>
              <thead></thead>
              <tr>
                <td valign="top">Importe:</td>
                <td valign="top"><input type="text" id="txtImporte" value="IMPORTE" class= 'cajaTexto'/></td>
                <td valign="top"><input type="radio" id='chbPosNeg' name="chbPosNeg" value="0" checked> Postivo <br>
                                 <input type="radio" id='chbPosNeg' name="chbPosNeg" value="1"> Negativo </td>
                    <td valign="top">Referencia:</td>
                <td valign="top"><input type="text" name='txtReferencia' id="txtReferencia" value="REFERENCIA" class= 'cajaTexto'/></td>
              </tr>
            </table>
            </div>
          </td>
        </tr>
      </table>
      <div id="divAgregar" style="display:none">
        <table cellpadding="5">
          <thead></thead>
          <tr>
            <td>
              <input type="button" class="boton" value="Agregar" onclick="AgregarCuenta2()"/>
              <input type="button" class="boton" value="Limpiar" onclick="limpiarCuenta()"/>
            </td>
          </tr>
        </table>
      </div>
      
      
      
      
      <div id = "capaNivel2"></div>

      <span id='detalle'>
  
        <!--<div id="Layer1"  
          style="Z-INDEX: 3; LEFT: 15px; VISIBILITY: visible; OVERFLOW: auto; WIDTH: 100%;  TOP: 1004px; HEIGHT: 250px;"> -->
          
          <table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="4%"><strong>Sel</strong></td>
                <td class="azulObs" width="<%=porcentajeCelda*nivelesConfigura%>%" colspan="<%=nivelesConfigura%>"><strong>Cuentas y subcuentas</strong></td>
                     
                <td class="azulObs" width="10%" align="center" ><strong>Importe</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Referencia</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Pos / Neg</strong></td>
             </tr>
            </thead>
          </table>
        <!--</div>-->
      </span>
      <table cellpadding="5">
        <thead></thead>
        <tr>
          <td>
            <input type="button" class="boton" value="Eliminar" onclick="eliminarElementos()"/>
            
          </td>
        </tr>
      </table>
      <hr class="piePagina"/>
      <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <input type="button" class="boton" name="btnAceptar" value="Aceptar" onclick="validaFormulario()"/>
            <input type="button" class="boton" value="Regresar" onClick="enviaMisma()"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>