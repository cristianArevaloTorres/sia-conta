<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="java.util.*" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ include file="../../../Librerias/Funciones/utilscpv.jsp"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="crsTiposPoliza" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="formaContable" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c713FormasModificar</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <script type="text/javascript" src="../../../Librerias/Javascript/catalogos.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
        <script language="javascript" type="text/javascript">
          
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
            hdCuenta.id = hdCuenta.name; 
            return hdCuenta;
          }
          
          function crearNivelCuenta(fila, nombre, valor, noCelda, alineacionCelda, tamanio, maxLong, soloLectura) {
            celda = fila.insertCell(noCelda); 
            celda.width ="4%";
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
              celda.appendChild(crearHidden("txt"+nombre+noCelda, valor));
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
            txtCuenta.readonly = soloLectura;
            txtCuenta.size = tamanio;
            txtCuenta.className = "cajaTexto";  
            return txtCuenta;
          }
          
          function obtNumReg() {
            checks = document.getElementsByName('chbEliminar');
            return checks.length;
          } 
          
          function crearRadiosx(fila, nombreOrigen, nombreDestino, noCelda, alineacionCelda) {
              alert(1.1);
            celda = fila.insertCell(noCelda);
              alert(1.2);
            celda.align = alineacionCelda;
              alert(1.3);
            radios = document.getElementsByName(nombreOrigen);
              alert(1.4);
            for(i = 0; i < radios.length; i++) {
              alert(i);
              crearRadio(celda, nombreDestino, radios[i].value, true, radios[i].checked);
              creaEspacio(celda);
            }
              alert(1.5);
          }
          
          function creaEspacio(celda) {
            var espacio = document.createElement("span");
            espacio.innerHTML = "&nbsp;";
            celda.appendChild(espacio);
          }
          
          function crearRadio(celda, nombre, valor, soloLectura, checado) {
              check = checado?' checked':'';
              readO = soloLectura?' readOnly':'';
              var radioHtml = '<input type="radio" name="' + name + '"'+check+readO+'/>';
              var radioFragment = document.createElement('div');
              radioFragment.innerHTML = radioHtml;         
              celda.appendChild(radioFragment.firstChild);   
          }
          
          var arreg = new Array();
          var arregElimina = new Array();
          
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
              crearNivelCuenta(fila, "Cta", document.getElementById("txtImporte").value, numCol++, "center", 15, 10, false); 
              crearNivelCuenta(fila, "Cta", document.getElementById("txtReferencia").value, numCol++, "center", 10, 10, false);
              alert(0);
              crearRadiosx(fila, "chbTipoOper", "chbTipoOper"+val, numCol++, "center");
              alert(1);
              crearRadiosx(fila, "chbPosNeg", "chbPosNeg"+val, numCol++, "center");
              alert();
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
              if(document.getElementsByName('hdCrea')[elem]!=0)
                arregElimina.push(document.getElementsByName('hdCrea')[elem].value);
              arreg.splice(elem,1);
              tabla.deleteRow(parseInt(elem)+1);
            }
            }catch(e){
              alert("Error al borrar");
            }
          }
          
        
          function eliminarElementos() {
            elementos = obtElim();
            if(correcto) {
              valores = elementos.split(',');
              for(i=valores.length-1; i >= 0; i--) {
                eliminarElem(valores[i]);
              }
              armaEnvio();
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
          var nivelMax = document.getElementById("nivelesConfigura").value;
          var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
          var nivelInt;
          nivelInt = parseInt(nivel)+1;
           var ceros;
           tamanios = document.getElementById("configura").value.split(",");
           ceros = tamanios[nivel-1];
           vacio = document.getElementById('txtCtaNivel'+nivel).value == '';
           o="00000".substring(0,ceros-document.getElementById('txtCtaNivel'+nivel).value.length)+document.getElementById('txtCtaNivel'+nivel).value;
           document.getElementById('txtCtaNivel'+nivel).value=o;
           document.getElementById('lstNivel'+nivel).value = document.getElementById('txtCtaNivel'+nivel).value;
           if (document.getElementById('lstNivel'+nivel).selectedIndex == -1){
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
             return false;
           }else{
              eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
              return true;
           }
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
                document.getElementById('chbCtaNivel'+i).checked = false; document.getElementById('hdCtaNivel'+i).value=''; selNivelCta(i); }
            }
            validaCambio(1);
          }
        
       
        </script>
        <script language="javascript" type="text/javascript">
          function validaPrimeraLetra(num) {
            valor = document.getElementById('txtCtaNivel'+num).value;
            if(valor != '')
              if(!isLetter(valor.charAt(0))) {
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
            if (num == 1 || !document.getElementById("chbCtaNivel"+num).checked) {
              if(relleCeros(num)) {
                muestraNivel2(num+1);
              }
            }  else {
              validaPrimeraLetra(num);
            }
          }
          
          function desmarcarChecks(num) {
            hasta = document.getElementById("nivelesConfigura").value;
            for (i = num; i <= hasta; i++)  {
              document.getElementById('chbCtaNivel'+i).checked = false;
            }
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
          function armaEnvio() {
            var cad = "";
            for(i=0; i < arreg.length; i++) {
              cad = cad + arreg[i]+',';
            }
            document.getElementById('registros').value = cad.substring(0,cad.length-1);
            cad = "";
            for(i=0; i < arregElimina.length; i++) {
              cad = cad + arregElimina[i]+',';
            }
            document.getElementById('regEliminar').value = cad.substring(0,cad.length-1);
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
            
              if(!band)
                alert(msg);
              else
                armaEnvio();
            } catch(e) {
              band=false;
              alert(e.name + ": " + e.message);
              
            }
            return band;
          }
          
          function regresarAResultado() {
            formulario.action="c713FormasResultado.jsp";
            formulario.submit();
          }
          
        </script>
        
        <script language="javascript" type="text/javascript">
          function iniciaChecks() {
            elementos = document.getElementsByName('chbEliminar');
            for(i=0; i<elementos.length; i++) {
              arreg.push(elementos[i].value);
            }
            armaEnvio();
          }
        </script>
        
        
  </head>
  <body onLoad="muestraNivel2(1); iniciaChecks();">
  <%
        Connection con = null;
        StringBuffer cadenaConfiguracion = new StringBuffer();
        try  {
          con = DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
          formaContable.setFormaContableId(request.getParameter("key"));
          formaContable.select(con); 
          
          crsConfiguraCta.addParam("ejercicio",controlRegistro.getEjercicio());
          crsConfiguraCta.addParam("idCatalogo",controlRegistro.getIdCatalogoCuenta());
          crsConfiguraCta.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.RfTcDetalleConfCve");
          while(crsConfiguraCta.next()) {
            if(crsConfiguraCta.isLast())
              cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio"));
            else
              cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio")).append(",");
          
        }
        crsConfiguraCta.beforeFirst();
        //int[] tam = {5,4,4,4,4,4,4,4};

        } catch (Exception ex)  {
          ex.printStackTrace();
        } finally  {
            if(con!=null){
              con.close();
              con = null;
            }
        }
        String forma = request.getParameter("hdforma") != null ? request.getParameter("hdforma") : "";
        String concepto = request.getParameter("hdconcepto") != null ? request.getParameter("hdconcepto") : "";
        String documentoFuente = request.getParameter("hddocumentoFuente") != null ? request.getParameter("hddocumentoFuente") : "";
        String tipoPolizaId = request.getParameter("hdtipoPolizaId") != null ? request.getParameter("hdtipoPolizaId") : "";
        int nivelesConfigura = crsConfiguraCta.size();
        int porcentajeCelda = 60 / nivelesConfigura;
        crsTiposPoliza.registros(DaoFactory.CONEXION_CONTABILIDAD,"select tipo_poliza_id, descripcion, abreviatura from RF_TC_TIPOS_POLIZAS where tipo_poliza_id in (1,2,3,4)");
        
  %>
  <%util.tituloPagina(out,"Contabilidad","Modificación de formas contables","Modificar",true);%>
    <form method="POST" action="c713FormasControl.jsp" id="formulario">
    
      <IFrame style="display:none" name = "bufferFrame">
      </IFrame>
      <input type="hidden" name="accion" value="2"/>
      
      <input type="hidden" name="hdforma" id="hdforma" value="<%=forma%>">
      <input type="hidden" name="hdconcepto" id="hdconcepto" value="<%=concepto%>">
      <input type="hidden" name="hddocumentoFuente" id="hddocumentoFuente" value="<%=documentoFuente%>">
      <input type="hidden" name="hdtipoPolizaId" id="hdtipoPolizaId" value="<%=tipoPolizaId%>">
      
      <input type="hidden" name="formaContableId" id="formaContableId" value="<%=formaContable.getFormaContableId()%>" />
      <input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=formaContable.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" id="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" id="unidadEjecutora" value="<%=formaContable.getUnidadEjecutora()%>"/>
      <input type="hidden" name="entidad" id="entidad" value="<%=formaContable.getEntidad()%>"/>
      <input type="hidden" name="ambito" id="ambito" value="<%=formaContable.getAmbito()%>"/>
      <input type="hidden" name="registro" id="registro" value="<%=formaContable.getRegistro()%>"/>
      <input type="hidden" name="esmanual" id="esmanual" value="<%=formaContable.getEsmanual()%>"/>
      <input type="hidden" id="configura" name="configura" value="<%=cadenaConfiguracion.toString()%>"/>
      <input type="hidden" id="nivelesConfigura" name="nivelesConfigura" value="<%=nivelesConfigura%>"/>
      <input type="hidden" name="registros" id="registros" value=""/>
      <input type="hidden" name="regEliminar" id="regEliminar" value=""/>
      
      <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=formaContable.getUnidadEjecutora()%></td>
         <td align="center"><%=formaContable.getEntidad()%></td>
         <td align="center"><%=formaContable.getAmbito()%></td>
        </tr>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table border="0" cellpadding="5">
        <thead></thead>
        <tr>
          <td>Forma:</td>
          <td><input type="text" disabled size="3" maxlength="2" readonly class="cajaTexto" value="<%=formaContable.getForma()%>"  onkeyup="validarSiNumero(this)"/>
          <input type="hidden" id="forma" name="forma" value="<%=formaContable.getForma()%>"/>
          <div id="dopval" style="display:none"><font color="red">Solo se permiten letras</font></div> </td>
          <td rowspan="3" valign="top">Concepto:</td>
          <td rowspan="3"><textarea cols="40" rows="5" id="concepto" name="concepto" class="cajaTexto"><%=formaContable.getConcepto()%></textarea></td>
        </tr>
        <tr>
          <td>Documento fuente:</td>
          <td><input type="text" id="documentoFuente" name="documentoFuente" size="50" maxlength="250" class="cajaTexto" value="<%=formaContable.getDocumentoFuente()%>"/></td>
        </tr>
        <tr>
          <td>Tipo p&oacute;liza:</td>
          <td>
            <select class="cajaTexto" id="tipoPolizaId" name="tipoPolizaId"> 
              <%CRSComboBox(crsTiposPoliza, out,"tipo_poliza_id","descripcion",formaContable.getTipoPolizaId());%>
              
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
              <input type="radio" name="chbTipoOper" value="0" checked>Debe</input>
              <input type="radio" name="chbTipoOper" value="1">Haber</input>
            </div>
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <table width="498" id="tNiveles">
              <thead></thead>
              <tr>
                <td width="100">Descripci&oacute;n:</td><TD>
                <%for (int i = 1; i <= nivelesConfigura; i++)  {
                    if(i==1) {
                      %><div id ='divsel'>
                           <select  name='lstNivel<%=i%>' id="lstNivel<%=i%>" class= 'cajaTexto' onChange="muestraNivel2(<%=i+1%>); txtCtaNivel<%=i%>.value =lstNivel<%=i%>.value; ">
                           </select>
                        </div>
                      <%
                    } else {
                      %><div id = 'divsel' style="display:none">
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
                <td valign="top"><input type="radio" name="chbPosNeg" value="0" checked> Postivo <br>
                                 <input type="radio" name="chbPosNeg" value="1"> Negativo </td>
                    <td valign="top">Referencia:</td>
                <td valign="top"><input type="text" id="txtReferencia" value="REFERENCIA" class= 'cajaTexto'/></td>
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
             <%
          try {
            List<Integer> valores = new ArrayList();
            for (int i = 1; i <= nivelesConfigura; i++)  {
              valores.add(i);
            }
            valores.add(98);
            valores.add(99);
            int maxSubCuentas = nivelesConfigura+2;
            
            
            List<bcRfTcSecuenciaFormaPojo> secs = formaContable.getDetSec();
            int numSec = 0;
            for(bcRfTcSecuenciaFormaPojo sec : secs) {
            %>
              <tr>
                <td align="center">
                  <input type="checkbox" name="chbEliminar" value="<%=numSec%>"/>
                  <input type="hidden" name="hdCrea" value="<%=sec.getSecuenciaFormaId()%>"/>
                </td>
            <%
              List<bcRfTcConfiguraFormaPojo> cfgs = sec.getDetConf();
              String valCuenta = ""; 
              String signo = null;
              boolean band = false;
              int x = 0;
              for(bcRfTcConfiguraFormaPojo cfg : cfgs) {
                band = false;
                while(cfg == null || (valores.get(x) != Integer.parseInt( cfg.getNivel() ) )) {
                  band = true;
                  x++;
                  %><td width="4%">
                    <input type="hidden" name="txtCta<%=x%>" /> 
                    <input type="hidden" name="hdCta<%=x%>"  /> 
                    <input type="hidden" name="hdCfg<%=x%>"  /> 
                  </td><%
                }
                //x = band ? x : x + 1; 
                x++;
                valCuenta = cfg != null && cfg.getCodigo() != null ? cfg.getCodigo() : "";
                signo = cfg != null && cfg.getSigno() != null && signo == null ? cfg.getSigno():signo; 
            %>
                
                <td align="center">
                  <input type="text" class="cajaTexto" name="txtCta<%=x%>" size="<%=cfg.getNivel().equals("98")?15:cfg.getNivel().equals("1")?5:10%>" maxlength="<%=cfg.getNivel().equals("1")?5:25%>" <%=cfg.getEsvariable().equals("1")?"":"readonly"%> value="<%=valCuenta%>"/> 
                  <input type="hidden" name="hdCta<%=x%>"  value="<%=cfg.getEsvariable()%>" /> 
                  <input type="hidden" name="hdCfg<%=x%>"  value="<%=cfg.getConfiguraFormaId()%>" /> 
                </td>
            <%}%>
                <td align="center"> 
                  <input type="radio" name="chbTipoOper<%=numSec%>" value="0" <%=sec.getOperacionContableId().equals("0")?"checked":""%>/> 
                  <input type="radio" name="chbTipoOper<%=numSec%>" value="1" <%=sec.getOperacionContableId().equals("1")?"checked":""%>/>
                </td> 
                <td align="center"> 
                  <input type="radio" name="chbPosNeg<%=numSec%>" value="0" <%=signo.equals("0")?"checked":""%>/> 
                  <input type="radio" name="chbPosNeg<%=numSec%>" value="1" <%=signo.equals("1")?"checked":""%>/>
                </td> 
              </tr>
          <%
            numSec++;
            }
          } catch(Exception e) {
            out.print(e.getMessage()); 
          }
        %>
          </table>
        <!--</div>-->
     
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
            <input type="submit" class="boton" value="Aceptar" onclick="return validaFormulario()"/>
            <input type="button" class="boton" value="Regresar" onclick="regresarAResultado()"/>
          </td>
        </tr>
      </table>
    </form>
  </body>
</html>