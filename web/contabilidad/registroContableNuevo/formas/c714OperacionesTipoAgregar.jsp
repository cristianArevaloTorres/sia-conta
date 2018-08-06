<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
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
              var radioHtml = '<input type="radio" name="' + nombre + '" id="' + nombre + '" value="'+valor+'"'+check+readO+'/>';
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
        
          function AgregarCuenta2(){
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
            if(elem!='') {
              tabla.deleteRow(parseInt(elem)+1);
              arreg.splice(elem,1);
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
          var nivelMax = document.getElementById('nivelesConfigura').value;
          var conf = document.getElementById('configura').value.split(",");
          var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
          var nivelInt;
          nivelInt = parseInt(nivel)+1;
           var ceros;
           ceros = conf[nivel-1];
//           if (nivel ==1){
//             ceros=5;
//           }else{
//             ceros = 4;
//           }
           vacio = document.getElementById('txtCtaNivel'+nivel).value == '';
           o="00000".substring(0,ceros-eval("formulario.txtCtaNivel"+nivel+".value.length"))+eval("formulario.txtCtaNivel"+nivel+".value");
           eval("formulario.txtCtaNivel"+nivel).value=o;
           eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
           if (eval("formulario.lstNivel"+nivel+".selectedIndex") == -1){
             if (cancela == 0){
               if(!vacio)
                alert ("clave "+o+" no valida");
             }
             eval("formulario.txtCtaNivel"+nivel).value= "";
             eval("formulario.txtCtaNivel"+nivel).focus();
             eval("formulario.lstNivel"+nivel+".value = ''");
          
             for (i = nivelInt; i <= nivelMax; i++){
       
               eval("formulario.txtCtaNivel"+i).value='';
               eval("trlstNivel"+i+".style.display='none'");
               eval("formulario.lstNivel"+i+".value = ''");
             }
             if(nivelInt <= 5) {
              document.getElementById("divTipoOper").style.display = 'none';
              document.getElementById("divAgregar").style.display = 'none';
             }
             
             regresa =  false;
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
          loadSource('capaNivel2',null,'c714OperacionesTipoCapaCuentas.jsp?','nivRep='+nivelx+'&ctas='+params+'&configura='+document.getElementById("configura").value+
          '&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value);
        }
        </script>
        
        <script language="JavaScript" type="text/javascript">
        
        function termino(id) {
          document.getElementById("hdCtaNivel"+nivelSel).value = id;
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
              if(relleCeros(num)) {
                muestraNivel2(parseInt(num)+parseInt(1));
              }
            
          }
          
          function limpiarCuenta() {
            document.getElementById("txtCtaNivel1").value='';
            validaCambio(1);
          }
          
          function validarSiNumero(object)
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
          }
        </script>
        
        
        <script language="javascript" type="text/javascript">
          function enviaMisma() {
            formulario.action='';
            formulario.fechaVigFin.disabled=true;
            formulario.submit();
          }
          
          function armaEnvio() {
            var cad="";
            for(i=0; i < arreg.length; i++) {
              cad = cad + arreg[i]+',';
            }
            document.getElementById('registros').value = cad.substring(0,cad.length-1);
          }
          
          function validaFormulario() {
            msg  = "-------------ALERTA -------------\n";
            band = true;
            
            var fechaVigIni  = document.getElementById("fechaVigIni").value;
            var fechaVigFin  = document.getElementById("fechaVigFin").value;
            var añoIni       = fechaVigIni.substring(6,10);
            var añoFin       = fechaVigFin.substring(6,10);
            //msg              = msg +" "+añoIni+" "+añoFin+ "añoss\n";
            
            if( añoIni == añoFin ){
                band = true;
                formulario.fechaVigFin.disabled=false;
            }
            else {
              msg  = msg + "El año de la Fecha de Inicio y la Fecha de termino no son iguales, debe seleccionar el mismo Ejercico \n";
              band = false;
            }
            
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
          }
          
          function regresaDeCapa(mensajeError) {
            if(mensajeError == "") {
              formulario.submit();
            } else {
              alert(mensajeError);
            }
            
          }
        </script>
        
        
  </head>
  <%
    //controlRegistro.setIdCatalogoCuenta(2);
    StringBuffer cadenaConfiguracion = new StringBuffer();
    int nivelesConfigura = 8;
    int porcentajeCelda = 60;
    try  {
        crsConfiguraCta.addParam("ejercicio",controlRegistro.getEjercicio());
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
    <form method="POST" action="c714OperacionesTipoControl.jsp" id="formulario">
    
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
      <%
        int[] tam = {5,4,4,4,4,4,4,4};
      %>
      <table border="0" cellpadding="5">
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
          <% 
          String anioEjercicio= Integer.toString(controlRegistro.getEjercicio());
          
          String mesActual = (String)Integer.toString(sia.libs.formato.Fecha.getMesActual());
          if(mesActual.length()==1)
              mesActual= "0"+mesActual;
          
          String diaActual = (String)Integer.toString(sia.libs.formato.Fecha.getDiaActual());
          if(diaActual.length()==1)
              diaActual= "0"+diaActual;
          
          String fecha = diaActual.concat("/").concat(mesActual).concat("/").concat(anioEjercicio);
          //sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, anioEjercicio.concat(controlRegistro.getFechaAfectacion()).substring(4).trim()
         
          %>
         <!--<div id="Layer1"  
         <td><input type="text" id="fechaVigIni" name="fechaVigIni" value="<%=fecha%>" size="12" maxlength="12" class="cajaTexto"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigIni')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
        <tr>
          
          <td>Fecha de termino:</td>
          <td><input type="text" id="fechaVigFin" name="fechaVigFin" size="12" maxlength="12" value="31/12/<%=controlRegistro.getEjercicio()%>" disabled="disabled" class="cajaTexto"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigFin')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>          
         </tr>
         -->

        <tr>
          <td>Fecha de inicio:</td>
          <td><input type="text" id="fechaVigIni" name="fechaVigIni" value="<%=fecha%>" size="12" maxlength="12" class="cajaTexto"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigIni')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
        <tr>
          <td>Fecha de termino:</td>
          <td><input type="text" id="fechaVigFin" name="fechaVigFin" size="12" maxlength="12" class="cajaTexto" value="31/12/<%=controlRegistro.getEjercicio()%>" disabled />
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigFin')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>

          
       
      </table>
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
                <td class="azulObs" width="4%"><strong>Seleccionar</strong></td>
                <td class="azulObs" width="<%=porcentajeCelda*nivelesConfigura%>%" colspan="<%=nivelesConfigura%>"><strong>Cuentas y subcuentas</strong></td>
                
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
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
            <input type="button" class="boton" value="Aceptar" onclick="validaFormulario()"/>
            <input type="button" class="boton" value="Regresar" onClick="enviaMisma()"/>
          </td>
        </tr>
      </table>
      <%
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
      
      %>
    </form>
  </body>
</html>