
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>c714OperacionesTipoAgregar</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
        
        
        <script language="javascript" type="text/javascript">
          
          var idCuenta;
          
          function crearCelCheck(fila, nombre, valor, noCelda, alineacionCelda) {
            var celda = fila.insertCell(noCelda);
            celda.className = "chb";
            celda.align = alineacionCelda;
            celda.appendChild(crearCheck(nombre, valor));
            idCuenta = crearHidden("hdIdCuenta", 0);
            celda.appendChild(crearHidden("hdCrea", 0));
            celda.appendChild(idCuenta);
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
            return hdCuenta;
          }
          
          function crearNivelCuenta(fila, nombre, valor, noCelda, alineacionCelda, tamanio, maxLong, soloLectura) {
            var celda = fila.insertCell(noCelda); 
            celda.align = alineacionCelda;
            celda.className = "txt";
            if(valor!="") {
              celda.appendChild(crearText("txt"+nombre+noCelda, valor, tamanio, maxLong, soloLectura));
              idCuenta.value = document.getElementById("hdCtaNivel"+noCelda).value;
            }
          }
          
          function crearText(nombre, valor, tamanio, maxLong, soloLectura) {
            
            var txtCuenta = document.createElement("INPUT");
            txtCuenta.type = "text";
            txtCuenta.value = valor;
            txtCuenta.name = nombre;
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
              var radioHtml = '<input type="radio" name="' + name + '"'+check+readO+'/>';
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
        
      /*    function AgregarCuenta2(){
            var Tabla = document.getElementById("TablaPrueba");
            var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
            var fila = Tabla.insertRow(lRenglones);
            var numCol = 0;
            val = obtUltimo(arreg)
            tamanios = document.getElementById("configura").value.split(",");
            
             crearCelCheck(fila, "chbEliminar", "" + val, numCol++, "center");
              for(i=1; i<=tamanios.length; i++) {
                if(i==1)
                  crearNivelCuenta(fila, "Cta", document.getElementById("txtCtaNivel"+i).value, numCol++, "center", 5, 5, true);
                else+
                  crearNivelCuenta(fila, "Cta", document.getElementById("txtCtaNivel"+i).value, numCol++, "center", 10, 25, true);    
              }
            
            crearRadiosx(fila, "chbTipoOper", "chbTipoOper"+val, numCol++, "center");
          }*/
        </script>
        
        <script language="JavaScript" type="text/javascript">
          /*var correcto = true;
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
          }*/
          
          /*function eliminarElem(elem) {
            tabla = document.getElementById('TablaPrueba');
            if(elem!='') {
              tabla.deleteRow(parseInt(elem)+1);
              arreg.splice(elem,1);
            }
          }*/
        
        /*  function eliminarElementos() {
            elementos = obtElim();
            if(correcto) {
              valores = elementos.split(',');
              for(i=valores.length-1; i >= 0; i--) {
                eliminarElem(valores[i]);
              }
            }
              
          }*/
        </script>
        
        <script language="JavaScript" type="text/javascript">
        
        function regresa() {
          var x;
          x=1;
        }
        
        function relleCeros(nivel){
          nivel2 = "";
          if (nivel ==0){
            nivel= 1;
          }
          var regresa;
          var nivelMax = document.getElementById('nivelesConfigura').value;
          var conf = document.getElementById('configura').value.split(",");
          var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
          var nivelInt;
          nivelInt = parseInt(nivel)+1;
           var ceros;
           ceros = conf[nivel-1];
           vacio = document.getElementById('txtCtaNivel'+nivel).value == '';
           o="00000".substring(0,ceros-eval("formulario.txtCtaNivel"+nivel+".value.length"))+eval("formulario.txtCtaNivel"+nivel+".value");
           eval("formulario.txtCtaNivel"+nivel).value=o;
           eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
           datoCapturado = document.getElementById('txtCtaNivel'+nivel).value;
           rangoPer = document.getElementById("hdRangosPermitidos").value;
           nivel2 =rangoPer.substr(rangoPer.lastIndexOf('|')+1,rangoPer.length);
           if(parseInt(nivel2) == nivel){
              if(rangoPer.indexOf(datoCapturado) == -1){
                  if(nivel== 1)
                    alert ("La cuenta de mayor "+o+" no se encuentra configurada.");
                  else
                    alert ("EL valor de la subcuenta "+o+" en el nivel "+nivel+" no se encuentra configurado. ");
                eval("formulario.txtCtaNivel"+nivel).value= "";
                eval("formulario.txtCtaNivel"+nivel).focus();
                eval("formulario.lstNivel"+nivel+".value = ''");

                for (i = nivelInt; i <= nivelMax; i++){

                  eval("formulario.txtCtaNivel"+i).value='';
                  eval("trlstNivel"+i+".style.display='none'");
                  eval("formulario.lstNivel"+i+".value = ''");
                }
                regresa =  false;
              }
              else{
              eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
              if (eval("formulario.lstNivel"+nivel+".selectedIndex") == -1){
                  document.getElementById("txtDescripcion").focus();
                 regresa = false;
                
              } else{
                  eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
                  regresa = true;
              }
           } 
           }else{
                  eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
                  regresa = true;
              }
           return regresa;
        }
        
        var nivelSel;
        
        function muestraNivel2(nivelx){
          var ceros = '00000';
          params = "";
          for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx; i++)  {
            document.getElementById('txtCtaNivel'+i).value = document.getElementById('lstNivel'+i).value
            params = params + document.getElementById("txtCtaNivel"+i).value;
          } 
          nivelSel = parseInt(nivelx)-1;
          loadSource('capaNivel2',null,'c712CapaCuentasContables.jsp?','nivRep='+nivelx+'&ctas='+params+'&configura='+document.getElementById("configura").value+
          '&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value);
        }
        </script>
        
        <script language="JavaScript" type="text/javascript">
        
        function termino(id,ctaMayorId) {
          //alert(id);
          document.getElementById("hdCtaNivel"+nivelSel).value = id;
          document.getElementById("hdCtaMayorId").value = ctaMayorId;
          //alert("mayor id "+ctaMayorId);
          //alert("id cuenta" + id);
        }
        
        function rangosPermitidos(rangos){
          document.getElementById("hdRangosPermitidos").value = rangos;
          //alert("Rangos : " +rangos);
        }
       
        function muestraDescCuenta(xnivel){
          var i = 0;
          var niv = parseInt(xnivel) +1;
          var c = parseInt(document.getElementById("nivelesConfigura").value)-1;
          divs = document.getElementsByName("divsel");
          
          for (i == 0; i <= c; i++){
            
            if (xnivel == i){
              divs[i].style.display ='';
              document.getElementById('lstNivel'+niv).value = document.getElementById('txtCtaNivel'+niv).value;
             }else{
                divs[i].style.display ='none';
             }
          }
        }
        
       
        </script>
        <script language="javascript" type="text/javascript">
          function validaCambio(num) {
              //alert("num " +num);
              document.getElementById("nivelCta").value=parseInt(num);
              if(relleCeros(num)) {
                muestraNivel2(parseInt(num)+parseInt(1));
              }
            
          }
          
          /*function limpiarCuenta() {
            document.getElementById("txtCtaNivel1").value='';
            validaCambio(1);
          }*/
          
     /*     function validarSiNumero(object)
          {
            div = document.getElementById('dopval');
            div.style.display = 'none';
            if(object.value!='') {
              numero = object.value.substring(object.value.length-1,object.value.length);
              if (!/\d/.test(numero)) {
                object.value = object.value.substring(0,object.value.length-1);
                div.style.display = '';
              }
            }  
          }*/
        </script>
        
        
        <script language="javascript" type="text/javascript">
          /*function enviaMisma() {
            formulario.action='';
            formulario.submit();
          }*/
          
         /* function armaEnvio() {
            var cad="";
            for(i=0; i < arreg.length; i++) {
              cad = cad + arreg[i]+',';
            }
            document.getElementById('registros').value = cad.substring(0,cad.length-1);
          }*/
          
         /* function validaFormulario() {
            msg = "-------------ALERTA -------------\n";
            band = true;
            if (document.getElementById("consecutivo").value=='') {
              msg = msg + "Debe capturar el consecutivo\n";
              band = false;
            }
            if (document.getElementById("descripcion").value=='') {
              msg = msg + "Debe capturar la descripcion\n";
              band = false;
            }
            if (document.getElementById("fechaVigIni").value=='') {
              msg = msg + "Debe capturar la fecha inicial\n";
              band = false;
            }
            if (document.getElementById("fechaVigFin").value=='') {
              msg = msg + "Debe capturar la fecha final\n";
              band = false;
            }
            if (document.getElementById("consecutivo").value=='99') {
              msg = msg + "El sistema no soporta los movimientos sobre la operación 99\n";
              band = false;
            }
            if(arreg.length <= 1) {
              msg = msg + "La operacion tipo debe contar con al menos dos cuentas contables\n";
              band = false;
            }
            if(!band)
              alert(msg);
            else {
              armaEnvio();
              loadSource('capaNivel2',null,'c714CapaValidaUniqueOperacion.jsp?','operacion='+document.getElementById('consecutivo').value+
              '&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+
              '&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value+'&fecha='+document.getElementById('fechaVigIni').value);
            }
            return band;
          }*/
          
          function regresaDeCapa(mensajeError) {
            if(mensajeError == "") {
              formulario.submit();
            } else {
              alert(mensajeError);
            }
            
          }
          
          
//funcion que envia el mensaje una vez que se ha realizado la transccion
//se manda llamar desde la pagina c712CapaControl

  function Fin(accion,msgError){
    if(accion == 'M'){
      if(msgError=='C'){
        alert("La cuenta ya está cerrada para esta Unidad Ejecutora, Entidad y Ambito ");
      }else{
         if(msgError=='EC'){
           alert("En estos momentos no es posible crear, modificar o eliminar cuentas contables, ya que hay un proceso de cierre ejecutándose. \nFavor de intentarlo más tarde.");
         }else{
            alert("El registro se modificó correctamente");
         }//else de EC
      }//else de C
    }//else de M
    
    if(accion == 'A'){
    var lNivel=5;
        if(msgError == ""){
            alert("El registro se agregó correctamente");
        }else{
            if(msgError=='C'){
                alert("La cuenta ya está cerrada para esta Unidad Ejecutora, Entidad y Ambito ");
            }else{
                if(msgError=='CA'){
                    alert("La cuenta que intenta agregar no tiene cuenta padre");
                }else{
                    if(msgError=='FA'){
                       alert("La cuenta que intenta agregar es incorrecta");
                    }else{
                       if(msgError=='EC'){
                          alert("En estos momentos no es posible crear, modificar o eliminar cuentas contables, ya que hay un proceso de cierre ejecutándose.\nFavor de intentarlo más tarde.");
                       }else{
                          alert("El registro ya existe o la subcuenta tiene movimientos");
                       }//else de EC
                    } //else de FA  
                }//else de CA
            }//else de C
        }//else de vacio
    }//if de val
    
    if(accion == 'E'){
        if(msgError==""){
            alert("El registro se eliminó correctamente");
        }else{
            if(msgError=="C"){
                alert("La cuenta ya está cerrada para esta Unidad Ejecutora, Entidad y Ambito ");
            }else{
                if(msgError=="P"){
                    alert("Existen pólizas relacionadas con la subcuenta");
                }else{
                    if(msgError=="EC"){
                        alert("En estos momentos no es posible crear, modificar o eliminar cuentas contables, ya que hay un proceso de cierre ejecutándose.\nFavor de intentarlo más tarde.");
                    }else{
                        alert("Existen subcuentas para el nivel seleccionado");
                    }
                }//else de P
            }//else de C
        }//else de vacio
    }//else de E
}//funcion

        </script>
        <script language="javascript" type="text/javascript">
            //FUNCION QUE REEMPLAZA CARACTERES ESPECIALES
        function Reemplaza(){
          var descrip=formulario.txtDescripcion.value;
          descrip=descrip.replace(/%/gi,"{");//cambia a llave{ el porcentaje%
          descrip=descrip.replace(/&/gi,"|");//cambia a pipe| el ampersand&
          descrip=descrip.replace(/#/gi,"}");//cambia a llave} el gato#
          descrip=descrip.replace(/\+/gi,"[");//cambia a llave} el gato#
          formulario.txtDescripcion.value=descrip;
    }
    
    function regresa(){
      var x;
      x=1;
    }
    
    function tieneCaracteresValidos(texto) {
        //alert("Descripcion: " + texto);
        if ( /^[0-9a-zA-Z áéíóúÑñ .,-\/\(\)\%\{\}\[\&\|\#\+]+$/.test(texto) ){
            return true;
        } 
        
        return false;
    }
//Función llamada al hacer click sobre el boton de INSERTAR
function CapaAgregar(){
        Reemplaza();
        params = "";
        cadenaCeros = "";
       
        nivelx = document.getElementById("nivelCta").value;
        subCtaAgrega = document.getElementById('txtCtaNivel'+nivelx).value;
        //alert("Sucuenta: " + subCtaAgrega);
        niveles = document.getElementById("configura").value;
        // alert("Niveles: " + niveles);
        
        for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx+1; i++)  {
            //alert("cuenta "+ document.getElementById("nivelesConfigura").value);
            //document.getElementById('txtCtaNivel'+i).value = document.getElementById('lstNivel'+i).value
            params = params + document.getElementById("txtCtaNivel"+i).value;
        } 
        nivelSel = parseInt(nivelx)-1;
        // loadSource('capaNivel2',null,'c712CapaCuentasContables.jsp?','nivRep='+nivelx+'&ctas='+params+'&configura='+document.getElementById("configura").value+
        // '&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value);
        
        
        var lsDesc= document.getElementById("txtDescripcion").value;
        if(!tieneCaracteresValidos(lsDesc)) {
            alert("\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
                "------------------------------------------------------------------------\n\n" +
                "La descripci\u00f3n de la Cuenta Contable -  " + lsDesc  + "  - tiene caracteres NO permitidos. " +
                "\n\nPor favor usar solamente los siguientes caracteres:  0-9 a-z A-Z . # % { } , - / )( \n" +
                "\n-------------------------------------------------------------------------\n\n" +
                "Presiona aceptar para corregir e intenta nuevamente.");
            return;
        }
        
        var fechaIni = document.getElementById("txtFecIni").value;
        var fechaFin = document.getElementById("txtFecFin").value;
        var fechaRegistro = document.getElementById("fecRegistro").value;
        var idCuenta = document.getElementById("hdCtaNivel"+nivelSel).value;
        var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
        var nivel = document.getElementById("nivelCta").value;
        var unidadEjecutora = document.getElementById('unidadEjecutora').value;
        var lsEntidad = document.getElementById('entidad').value
        var lsAmbito = document.getElementById('ambito').value;
        var lCtaMayorId = document.getElementById('hdCtaMayorId').value;
        //Carga la capa de la pagina c712CapaControl para el procesamiento de insertar un registro
        
        //alert('c712CapaControl.jsp?ldesc='+lsDesc+'&fec1='+fechaIni+'&fec2='+fechaFin+'&id='+idCuenta+
        //    '&catCuentaId='+idCatCuenta+'&ejercicio='+fechaIni.substr(6,10)+'&opc=A&ctaConta='+params+'&nivelCta='+nivel+'&idCtMay='+lCtaMayorId+'&uniE='+unidadEjecutora+
        //    '&ent='+lsEntidad+'&amb='+lsAmbito+'&fecReg='+fechaRegistro+'&nivelesConf='+niveles+'&ctaVal='+subCtaAgrega+'&pasa=0');
        
        loadSource('capaControl',null,'c712CapaControl.jsp?ldesc='+lsDesc+'&fec1='+fechaIni+'&fec2='+fechaFin+'&id='+idCuenta+
            '&catCuentaId='+idCatCuenta+'&ejercicio='+fechaIni.substr(6,10)+'&opc=A&ctaConta='+params+'&nivelCta='+nivel+'&idCtMay='+lCtaMayorId+'&uniE='+unidadEjecutora+
            '&ent='+lsEntidad+'&amb='+lsAmbito+'&fecReg='+fechaRegistro+'&nivelesConf='+niveles+'&ctaVal='+subCtaAgrega+'&pasa=0');
        
        //formulario.submit();
    }

//Función llamada al hacer click sobre el boton de MODIFICAR
function CapaEditar(){
    Reemplaza();
    params = "";
    niveles = document.getElementById("configura").value;
    nivelx = document.getElementById("nivelCta").value;
    for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx+1; i++)  {
      //alert("cuenta "+ document.getElementById("nivelesConfigura").value);
      //document.getElementById('txtCtaNivel'+i).value = document.getElementById('lstNivel'+i).value
      params = params + document.getElementById("txtCtaNivel"+i).value;
    } 
    nivelSel = parseInt(nivelx)-1;
    var lsDesc= document.getElementById("txtDescripcion").value;
    var fechaIni = document.getElementById("txtFecIni").value;
    var fechaFin = document.getElementById("txtFecFin").value;
    var fechaRegistro = document.getElementById("fecRegistro").value;
    var idCuenta = document.getElementById("hdCtaNivel"+nivelx).value;
    var nivel = document.getElementById("nivelCta").value;
    var unidadEjecutora = document.getElementById('unidadEjecutora').value;
    var lsEntidad = document.getElementById('entidad').value
    var lsAmbito = document.getElementById('ambito').value;
    var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
    var lCtaMayorId = document.getElementById('hdCtaMayorId').value;
    /*if (idCta !="" && lNivel=="")
        lNivel="9";
    var ctaIdMay ="";
    var cta="";*/
        
    if(!tieneCaracteresValidos(lsDesc)) {
        alert("\nSe generar\u00f3n los siguientes errores en la validaci\u00f3n:\n" +
            "------------------------------------------------------------------------\n\n" +
            "La descripci\u00f3n de la Cuenta Contable -  " + lsDesc  + "  - tiene caracteres NO permitidos. " +
            "\n\nPor favor usar solamente los siguientes caracteres:  0-9 a-z A-Z . # % { } , - / )( \n" +
            "\n-------------------------------------------------------------------------\n\n" +
            "Presiona aceptar para corregir e intenta nuevamente.");
        return;
    }

    if (idCuenta != '' ){
      //Carga la capa de la pagina c712CapaControl para el procesamiento de modificar un registro  
      loadSource('capaControl',null,'c712CapaControl.jsp?ldesc='+lsDesc+'&fec1='+fechaIni+'&fec2='+fechaFin+'&id='+idCuenta+'&catCuentaId='+idCatCuenta+
      '&ejercicio='+fechaIni.substr(6,10)+'&opc=M&ctaConta='+params+'&nivelCta='+nivel+
        '&idCtMay='+lCtaMayorId+'&uniE='+unidadEjecutora+'&ent='+lsEntidad+'&amb='+lsAmbito+'&fecReg='+fechaRegistro+'&nivelesConf='+niveles+'paso=0');
    }else{
      alert("Deberá seleccionar un registro para modificarlo");
    }
}

//Función llamada al hacer click sobre el boton de ELIMINAR
function CapaEliminar(){
    var borrar= true;
    nivelx = document.getElementById("nivelCta").value;
    nivelId = 0;
    params = "";
    for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx+1; i++)  {
      if(document.getElementById("txtCtaNivel"+i).value!="")
        nivelId = i;
      params = params + document.getElementById("txtCtaNivel"+i).value;
    }
    if(params != ""){
      borrar = confirm('¿Desea Eliminar el Registro?');
      if(borrar == true){
       niveles = document.getElementById("configura").value;
       var idCuenta = document.getElementById("hdCtaNivel"+nivelx).value;
       var fechaIni = document.getElementById("txtFecIni").value;
       var idCuenta = document.getElementById("hdCtaNivel"+nivelId).value;
       var nivel = document.getElementById("nivelCta").value;
       var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
       //Carga la capa de la pagina c712CapaControl para el procesamiento de eliminar un registro
        loadSource('capaControl',null,'c712CapaControl.jsp?ldesc=0&fec1='+fechaIni+
          '&fec2=0&id='+idCuenta+'&catCuentaId='+idCatCuenta+'&ejercicio='+fechaIni.substr(6,10)+'&opc=E&ctaConta='
          +params+'&nivelCta='+nivel+'&nivelesConf='+niveles+'&idCtMay=-1&uniE=0&ent=0&amb=0&fecReg=0');
      }
    }else{
      alert("Deberá seleccionar un registro para eliminar");
    }
    /*if(borrar == true){
       params = "";
       nivelx = document.getElementById("nivelCta").value;
       for (i = 1; i <= document.getElementById("nivelesConfigura").value && i < nivelx+1; i++)  {
         params = params + document.getElementById("txtCtaNivel"+i).value;
       } 
       var fechaIni = document.getElementById("txtFecIni").value;
       var idCuenta = document.getElementById("hdCtaNivel"+nivelx).value;
       var nivel = document.getElementById("nivelCta").value;
       var idCatCuenta = document.getElementById('idCatalogoCuenta').value;


      if(idCuenta != ''){
        loadSource('capaControl',null,'c712CapaControl.jsp?ldesc=0&fec1='+fechaIni+
          '&fec2=0&id='+idCuenta+'&catCuentaId='+idCatCuenta+'&ejercicio='+fechaIni.substr(6,10)+'&opc=E&ctaConta='
          +params+'&nivelCta='+nivel+'&idCtMay=-1&uniE=0&ent=0&amb=0&fecReg=0');
        muestraNivel2(lNivel-1);
      }else{
        alert("Deberá seleccionar un registro para eliminar");
      }
 }*/
}//funcion

       </script>
        
  </head>
  <%
    //controlRegistro.setIdCatalogoCuenta(2);
    StringBuffer cadenaConfiguracion = new StringBuffer();
    int nivelesConfigura = 8;
    int porcentajeCelda = 60;
    try  {
        crsConfiguraCta.addParam("ejercicio",controlRegistro.getEjercicio());
        controlRegistro.getFechaAfectacion();
        crsConfiguraCta.addParam("idCatalogo",controlRegistro.getIdCatalogoCuenta());
        crsConfiguraCta.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.RfTcDetalleConfCve");
        while(crsConfiguraCta.next()) {
          if(crsConfiguraCta.isLast())
            cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio"));
          else
            cadenaConfiguracion.append(crsConfiguraCta.getString("tamanio")).append(",");  
        }
        nivelesConfigura = crsConfiguraCta.size();
        porcentajeCelda = 60 / nivelesConfigura;

        
  %>
  <body onLoad="muestraNivel2(1);">
      
  
    <%util.tituloPagina(out,"Contabilidad","Agrega formas contables","Agregar",true);controlRegistro.getPais(); %>
    <form method="POST" action="c712CapaControl.jsp" id="formulario">
        
    
      <IFrame style="display:none" name = "bufferCurp" id="bufferCurp">
      </IFrame>
      <input type="hidden" name="accion" value="1"/>
      <input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" id="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" id="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" name="entidad" id="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" name="ambito" id="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" name="pais" id="pais" value="<%=controlRegistro.getPais()%>"/>
      
      <input type="hidden" name="registro" id="registro" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>"/>
      <input type="hidden" id="configura" name="configura" value="<%=cadenaConfiguracion.toString()%>"/>
      <input type="hidden" id="nivelesConfigura" name="nivelesConfigura" value="<%=nivelesConfigura%>"/>
      <input type="hidden" name="registros" id="registros" value=""/>
      <input type="hidden" name="hdCtaNivel0" id="hdCtaNivel0"/>
      <input type="hidden" name="hdRangosPermitidos" id="hdRangosPermitidos" value=""/>
      
      <br>
      <hr class="piePagina">
      <br>
      <%
        //int[] tam = {5,4,4,4,4,4,4,4};
     
      %>
   <!--   <table border="0" cellpadding="5">
        <thead></thead>
        <tr>
          <td>No operación:</td>
          <td><input type="text" id="consecutivo" name="consecutivo" size="3" maxlength="2" class="cajaTexto" onkeyup="validarSiNumero(this)"/>
          <div id="dopval" style="display:none"><font color="red">Solo se permiten numeros</font></div> </td>
          <td rowspan="3" valign="top">Descripción:</td>
          <td rowspan="3"><textarea cols="40" rows="5" id="descripcion" name="descripcion" class="cajaTexto"></textarea></td>
        </tr>
        <tr>
          <td>Fecha de inicio:</td>
          <td><input type="text" id="fechaVigIni" name="fechaVigIni" size="12" maxlength="12" class="cajaTexto"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigIni')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
        <tr>
          <td>Fecha de termino:</td>
          <td><input type="text" id="fechaVigFin" name="fechaVigFin" size="12" maxlength="12" class="cajaTexto"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigFin')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
      </table>-->
      <br>
      <H3>Cuenta</H3>
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
                  %>
                <td><div id="trlstNivel<%=i%>">
                       <input type="hidden" name="hdCtaNivel<%=i%>" id="hdCtaNivel<%=i%>" />
                       <input type='text' name='txtCtaNivel<%=i%>' id='txtCtaNivel<%=i%>' size='<%=crsConfiguraCta.getInt("tamanio")+1%>'  maxlength='<%=crsConfiguraCta.getInt("tamanio")%>' class='cajaTexto' onFocus ="muestraDescCuenta('<%=i-1%>');" value=''
                        tabindex='<%=i%>' onChange="validaCambio('<%=i%>')" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; validaCambio('<%=i%>');} " onkeyup="document.getElementById('lstNivel<%=i%>').value = document.getElementById('txtCtaNivel<%=i%>').value">
                    </div>
                </td>
                <%
                  } else {%>
                <td><div id="trlstNivel<%=i%>" style="display:none">
                      <input type="hidden" name="hdCtaNivel<%=i%>" id="hdCtaNivel<%=i%>"/>
                       <input type='text' name='txtCtaNivel<%=i%>' id='txtCtaNivel<%=i%>' size='<%=crsConfiguraCta.getInt("tamanio")+1%>'  maxlength='<%=crsConfiguraCta.getInt("tamanio")%>' class='cajaTexto' value='' onFocus="muestraDescCuenta('<%=i-1%>');"
                       tabindex='<%=i%>' onChange="validaCambio('<%=i%>')" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; validaCambio('<%=i%>');} " onkeyup="document.getElementById('lstNivel<%=i%>').value = document.getElementById('txtCtaNivel<%=i%>').value">
                    </div>
                </td>
                <%
                  } //if
                } //for
                %>  
              </tr>
            </table >
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <table width="498" id="tNiveles">
              <thead></thead>
              <tr>
                <td width="100">Descripci&oacute;n:</td>
                <td width="386"><%for (int i = 1; i <= nivelesConfigura; i++)  {
                    if(i==1) {
                      %>
                  <div id ='divsel' name="divsel">
                     <select  name='lstNivel<%=i%>' id="lstNivel<%=i%>" class= 'cajaTexto' onChange="muestraNivel2('<%=i+1%>'); txtCtaNivel<%=i%>.value =lstNivel<%=i%>.value; ">
                     </select>
                  </div> <%
                    } else {
                      %>
                  <div id = 'divsel' align="divsel" style="display:none">
                      <select name='lstNivel<%=i%>' id='lstNivel<%=i%>' class= 'cajaTexto' onChange="javascript: muestraNivel2('<%=i+1%>'); txtCtaNivel<%=i%>.value =lstNivel<%=i%>.value; "></select>
                  </div><%
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
          </td>
        </tr>
      </table>
     <!-- <div id="divAgregar" style="display:none">
        <table cellpadding="5">
          <thead></thead>
          <tr>
            <td>
              <input type="button" class="boton" value="Agregar" onclick="AgregarCuenta2()"/>
              <input type="button" class="boton" value="Limpiar" onclick="limpiarCuenta()"/>
            </td>
          </tr>
        </table>
      </div>-->
      
      
      
      
      

     <div id = "capaNivel2"></div>
     <!--
     <span id='detalle'>
  
        <!--<div id="Layer1"  
          style="Z-INDEX: 3; LEFT: 15px; VISIBILITY: visible; OVERFLOW: auto; WIDTH: 100%;  TOP: 1004px; HEIGHT: 250px;"> 
          
          <table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="4%"><strong>Seleccionar</strong></td>
                <td class="azulObs" width="<%//=porcentajeCelda*nivelesConfigura%>%" colspan="<%//=nivelesConfigura%>"><strong>Cuentas y subcuentas</strong></td>
                
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
             </tr>
            </thead>
          </table>-->
        <!--</div>-->
      </span>
      
      <!--<table cellpadding="5">
        <thead></thead>
        <tr>
          <td>
            <input type="button" class="boton" value="Eliminar" onclick="eliminarElementos()"/>
            
          </td>
        </tr>
      </table>-->
      <br></br>
<table width="100%">

    <tr><td width="20%">Descripcion:</td>
        <td><input type="text" class="cajaTexto" name="txtDescripcion" id="txtDescripcion" size="80" maxlength="80"></td>
    </tr>
 <tr>
        <td >Fecha de Inicio de Vigencia:</td>
        <td><INPUT TYPE="text" NAME="txtFecIni" id="txtFecIni" SIZE='12' value="<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%>" maxlength="10" class="cajaTexto">
	 <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="if (formulario.txtFecIni.disabled==false) open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecIni')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a>
	 </td>
</tr>
 <tr>
        <td >Fecha de Fin de Vigencia:</td>
        <td><INPUT TYPE="text" NAME="txtFecFin" id="txtFecFin" value="31/12/<%=controlRegistro.getEjercicio()%>" disabled="disabled" SIZE='12' maxlength="10" class="cajaTexto">
	 <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="if (formulario.txtFecFin.disabled==false) open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecFin')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a>
	 </td>
</tr>
</table>
<div id = "capaControl"></div>
<br>
<br>

<table width='50%' align="center">
<tr><td><div id="divAgr" align="center">
<img  src="../../../Librerias/Imagenes/botonAgregar2.gif" id="imgAgr" width="16" height="16" alt=""  onclick="CapaAgregar();" >
<A href="javascript:void(0);" onclick="CapaAgregar();">Agregar</A>&nbsp;&nbsp; 
 </div></td>
 
  <td>
 <div id="divEdi" align="center">
 <img  src="../../../Librerias/Imagenes/botonEditar2.gif" width="16" height="16" alt="" onclick="CapaEditar();">
 <A href="javascript:void(0);" onclick="CapaEditar();">Modificar</A>&nbsp;&nbsp;
 </div></td>
 
  <td>
 <div id="divEli" align="center" >
 <img  src="../../../Librerias/Imagenes/botonEliminar2.gif" width="16" height="16" alt="" onclick="CapaEliminar();">
 <A href="javascript:void(0);" onclick="CapaEliminar();">Eliminar</A>&nbsp;&nbsp;
 </div></td>
 
  <td><div align="left" >
    <img src="../../../Librerias/Imagenes/botonImpresora.gif" alt="" width="16" height="16" >
     <a href="c712RepCuentasFiltro.jsp" target="_blank" >Reporte</a>
</div></td>
</tr> </table>


 <tr>
        <td >&nbsp;</td>
        <td><INPUT TYPE="hidden" NAME="txtFecReg" SIZE='12' maxlength="10" class="cajaTexto" > </td>
        <td><INPUT TYPE="hidden" NAME="nivelCta" id="nivelCta" value="" class="cajaTexto" > </td>
        <td><INPUT TYPE="hidden" NAME="fecRegistro" id="fecRegistro" value="<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%>" class="cajaTexto" > </td>
        
        <td><INPUT TYPE="hidden" NAME="hdCtaMayorId" id="hdCtaMayorId" value="" class="cajaTexto" > </td>
        <td><INPUT TYPE="hidden" NAME="hRegresaNivel" id="hRegresaNivel" value="0" class="cajaTexto" > </td>
</tr>
</table>
<br></br>
      <hr class="piePagina"/>
     <!-- <table cellpadding="5" align="center">
        <thead></thead>
        <tr>
          <td>
            <input type="button" class="boton" value="Aceptar" onclick="validaFormulario()"/>
            <input type="button" class="boton" value="Regresar" onClick="enviaMisma()"/>
          </td>
        </tr>
      </table>-->
      <%
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
      
      %>
    </form>
  </body>
</html>