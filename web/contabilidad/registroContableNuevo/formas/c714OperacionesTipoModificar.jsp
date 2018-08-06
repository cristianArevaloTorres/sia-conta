<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.formato.Fecha"%>
<%@ page import="java.sql.Connection" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="java.util.*" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcConfiguracionGeneral" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcConfiguracionRenglon" %>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="maestroOper" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcMaestroOperaciones" scope="page"/>
<jsp:useBean id="detalle" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsConfiguraCta" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c714OperacionesTipoModificar</title>
        <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
        <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
        <script language="JavaScript" src="../../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
        <script language="JavaScript" src="../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
        
        
        <script language="javascript" type="text/javascript">
          
          var idCuenta;
          var arregElimina = new Array();
          
          function crearCelCheck(fila, nombre, valor, noCelda, alineacionCelda) {
            var celda = fila.insertCell(noCelda);
            celda.className = "chb";
            celda.align = alineacionCelda;
            celda.appendChild(crearCheck(nombre, valor));
            idCuenta = crearHidden("hdIdCuenta", 0);
            celda.appendChild(crearHidden("hdCrea", 1));
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
              
              if(document.getElementsByName('hdCrea')[elem]!=0)
                arregElimina.push(document.getElementsByName('hdCrea')[elem].value);
              arreg.splice(elem,1);
              tabla.deleteRow(parseInt(elem)+1);
              
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
          var regresa;
          var nivelMax = document.getElementById('nivelesConfigura').value;
          var conf = document.getElementById('configura').value.split(",");
          var idCatCuenta = document.getElementById('idCatalogoCuenta').value;
          var nivelInt;
          nivelInt = parseInt(nivel)+1;
          ceros = conf[nivel-1];
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
          loadSource('capaNivel2',null,'c714OperacionesTipoCapaCuentas.jsp?','nivRep='+nivelx+'&ctas='+params+'&configura='+document.getElementById("configura").value+'&catCuentaId='+document.getElementById('idCatalogoCuenta').value+'&uniEje='+document.getElementById('unidadEjecutora').value+'&entidad='+document.getElementById('entidad').value+'&ambito='+document.getElementById('ambito').value+'&ejercicio='+document.getElementById('hLsEjercicio').value);        
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
            formulario.submit();
          }
          
          function regresaAResultado() {
            document.getElementById("formulario").action='c714OperacionesTipoResultado.jsp';
            document.getElementById("formulario").submit();
          }
          
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
          
          function validaFormulario(ejercicio) {
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
            } else {
                var fecha_vig_ini = document.getElementById("fechaVigIni").value;
                var ejercicio_fecha = fecha_vig_ini.substring(6, 10);
                //alert("Ejercicio actual: " + ejercicio  + "Ejercicio sel: " + ejercicio_fecha);
                if(ejercicio_fecha != ejercicio ){ 
                    msg = msg + "La fecha de Vigencia Inicial " + fecha_vig_ini + " no corresponde al ejercicio " + ejercicio + "\n";
                    band = false;
                }
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
            else
              armaEnvio();
            return band;
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
        
        <%
         StringBuffer cadenaConfiguracion = new StringBuffer();
          int nivelesConfigura = 8;
          int porcentajeCelda = 60;
        
        try  {
          
          maestroOper.setMaestroOperacionId(request.getParameter("key"));
          maestroOper.select(DaoFactory.CONEXION_CONTABILIDAD);   
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
          String consecutivo = request.getParameter("hdconsecutivo") != null ? request.getParameter("hdconsecutivo"): "";
          String descripcion = request.getParameter("hddescripcion") != null ? request.getParameter("hddescripcion"): "";
  %>
        
  </head>
  <body onLoad="muestraNivel2(1);iniciaChecks();">
    <%util.tituloPagina(out,"Contabilidad","Consulta de formas contables","Consulta",true);%>
    <form method="POST" action="c714OperacionesTipoControl.jsp" id="formulario">
    
      <IFrame style="display:none" name = "bufferCurp" id="bufferCurp">
      </IFrame>
      <input type="hidden" name="accion" value="2"/>
      
      <input type="hidden" name="hdconsecutivo" id="hdconsecutivo" value="<%=consecutivo%>">
      <input type="hidden" name="hddescripcion" id="hddescripcion" value="<%=descripcion%>">
      
      <input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=controlRegistro.getIdCatalogoCuenta()%>"/>
      <input type="hidden" name="hLsEjercicio" id="hLsEjercicio" value="<%=controlRegistro.getEjercicio()%>"/>
      <input type="hidden" name="unidadEjecutora" id="unidadEjecutora" value="<%=controlRegistro.getUnidad()%>"/>
      <input type="hidden" name="entidad" id="entidad" value="<%=controlRegistro.getEntidad()%>"/>
      <input type="hidden" name="ambito" id="ambito" value="<%=controlRegistro.getAmbito()%>"/>
      <input type="hidden" name="pais" id="pais" value="<%=controlRegistro.getPais()%>"/>
       
      <input type="hidden" name="registro" id="registro" value="<%=Fecha.formatear(Fecha.FECHA_CORTA,Fecha.getRegistro())%>"/>
      <input type="hidden" name="maestroOperacionId" id="maestroOperacionId" value="<%=maestroOper.getMaestroOperacionId()%>"/>
      <input type="hidden" id="configura" name="configura" value="<%=cadenaConfiguracion.toString()%>"/>
      <input type="hidden" id="nivelesConfigura" name="nivelesConfigura" value="<%=nivelesConfigura%>"/>

      <input type="hidden" name="esmanual" id="esmanual" value="1"/>
      <input type="hidden" name="registros" id="registros" value=""/>
      <input type="hidden" name="regEliminar" id="regEliminar" value=""/>
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
          <td><input type="text" id="consecutivo" name="consecutivo" size="3" maxlength="2" class="cajaTexto" value="<%=maestroOper.getConsecutivo()%>" onclick="validarSiNumero(this)"/>
          <div id="dopval" style="display:none"><font color="red">Solo se permiten numeros</font></div> </td>
          <td rowspan="3" valign="top">Descripción:</td>
          <td rowspan="3"><textarea cols="40" rows="5" id="descripcion" name="descripcion" class="cajaTexto"><%=maestroOper.getDescripcion()%></textarea></td>
        </tr>
        <tr>
          <td>Fecha de inicio:</td>
          <td><input type="text" id="fechaVigIni" name="fechaVigIni" size="12" maxlength="12" class="cajaTexto" value="<%=maestroOper.getFechaVigIni()%>"/>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigIni')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
          </td>
        </tr>
        <tr>
          <td>Fecha de termino:</td>
          <td>
              <input type="text" id="fechaVigFin" name="fechaVigFin" size="12" maxlength="12" class="cajaTexto" value="<%=maestroOper.getFechaVigFin()%>" disabled />
              
         <!-- <input type="text" id="fechaVigFin" name="fechaVigFin" size="12" maxlength="12" class="cajaTexto" value="<%= "31/12/" + controlRegistro.getEjercicio() %>"/>-->
         <!--<a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaVigFin')">
          <img src="../../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt="" ></a>-->
          </td>
        </tr>
      </table>      
      
      
          
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

      <br>      
          <table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="10%"> Seleccione </td>
                <td class="azulObs" width="<%=porcentajeCelda*nivelesConfigura%>%" colspan="<%=nivelesConfigura%>"><strong>Cuentas y subcuentas</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
             </tr>
            </thead>
            
        <%
          try {
            detalle.addParam("maestro",maestroOper.getMaestroOperacionId());
            detalle.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"formasContables.select.detalleFormulario");
            
            List<String> cuenta = null;
            for(int x=0; detalle.next(); x++) {
              cuenta = bcConfiguracionGeneral.separarClave(cadenaConfiguracion.toString(), detalle.getString("cuenta_contable"), detalle.getInt("nivel"));
            %> 
              <tr>
                
                <td align="center"> 
                  <input type="checkbox" value="<%=x%>" name="chbEliminar"/>
                  <input type="hidden" name="hdIdCuenta" value="<%=detalle.getString("cuenta_contable_id")%>"/>
                  <input type="hidden" name="hdCrea" value="<%=detalle.getString("detalle_operacion_id")%>"/>
                </td>
                <%
                  for (int i = 0; i < nivelesConfigura; i++)  {
                    if(cuenta.size() > i && cuenta.get(i) != null) {
                      %><td width="10%" align="center"><input type="text" class="cajaTexto" value="<%=cuenta.get(i)%>" size="5" readonly></td><%
                    } else {
                      %><td width="10%"></td><%
                    }
                  }
                  
                %>
                <td align="center"> 
                  <input type="radio" name="chbTipoOper<%=x%>" value="0" <%=detalle.getString("operacion_contable_id").equals("0") ? "checked" : ""%>/> 
                  <input type="radio" name="chbTipoOper<%=x%>" value="1" <%=detalle.getString("operacion_contable_id").equals("1") ? "checked" : ""%>/>
                </td> 
                
              </tr>
            <%
            }
            %>
                
                
            
                
          <%
            
            
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
            <input type="submit" class="boton" value="Aceptar" onclick="return validaFormulario(<%= controlRegistro.getEjercicio()  %> )"/>
            <input type="button" class="boton" value="Regresar" onClick="regresaAResultado()"/>
          </td>
        </tr>
      </table>
      <%} catch (Exception ex)  {
          ex.printStackTrace();
        } finally  {
        }
        
        String consecutivo = request.getParameter("hdconsecutivo") != null ? request.getParameter("hdconsecutivo"): "";
        String descripcion = request.getParameter("hddescripcion") != null ? request.getParameter("hddescripcion"): "";
          
        %>
    </form>
  </body>
</html>