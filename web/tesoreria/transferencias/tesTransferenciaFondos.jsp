<%@ page contentType="text/html;charset=ISO-8859-1"%>
<% response.setHeader("Pragma","no-cache");%>
<% response.setHeader("Cache-Control","no-store");%>
<% response.setDateHeader("Expires",-1);%> 
<%@ page import="sia.db.dao.DaoFactory"%>
<%@ include file="../../Librerias/Funciones/utilscpv.jsp"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="crsDestinatario" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsCuentaCA" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsAutoriza" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsRevisa" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="crsElabora" class="sia.db.sql.SentenciasCRS" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>tesTransferenciaFondos</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
   <script language="javascript" src="../../Librerias/Javascript/crearElementos.js" type="text/javascript">
    </script>
    <script language="JavaScript" src="../../Librerias/Javascript/Calendario/Calendar/calendar.js" type="text/javascript"></script>
    <script language="JavaScript"  type="text/javascript">
     
    var numTransaccion = 1; 
    
    function validarFlotante(event,campo){
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
    
      function agregarEvento(componente,evento,funcion,parametros) {
        if (componente.addEventListener){
          componente.addEventListener(evento,function() {funcion(parametros);},false)
        } else if(componente.attachEvent){
          componente.attachEvent('on'+evento, function() {funcion.call(parametros, parametros);});
        }
      }
    
    function agregarTransaccion(nomTabla){
      var tabla = document.getElementById(nomTabla);
      imAgr = document.getElementById("himgAgregar");
      tabla.style.display="";
      var lRen = tabla.rows.length;
      var fila = tabla.insertRow(lRen);
      numTransaccion++;
      fila.className='azulCla';
      try {
        var col = fila.insertCell(0);
            im = imAgr.cloneNode(true); im.name = 'img'+numTransaccion; im.id = 'img'+numTransaccion;
        //var img = crearImg('img'+numTransaccion,'img'+numTransaccion,'../../Librerias/Imagenes/botonAgregar2.gif');
            //img.onclick = function() {crearRow('movimientos','afecta','TSR',this)};  
            //img.onclick = agregarEvento(img,'click',crearRow,img); 
            col.appendChild(crearHidden('hdNumTransaccion','hdNumTransaccion',numTransaccion));
            col.appendChild(im);  
        var col  = fila.insertCell(1);
            col.appendChild(document.createTextNode('Referencia'));
        var it = crearInput('referencia','referencia','');
            it.className='cajaTexto';
            it.size='40';
            col.appendChild(it);  
            col.colSpan='3';
        
       } catch(e) {
        alert(e.message);
       }
    }
    
    function obtenerRenglonValido(numTransaccion){
      encontro = false;
      finalizo = false;
      elementos = document.getElementsByName('hdNumTransaccion');
      if (elementos.length!=0){
          var num;
          for (i=0;i<elementos.length;i++){
            if (elementos[i].value==numTransaccion){
                encontro = true;
            }
            if (encontro && elementos[i].value!=numTransaccion){
                finalizo = true;
                
                break;
            }
            num = i;
          }//for 
          numRenglon = elementos[num].parentNode.parentNode.rowIndex;
          if (finalizo){
            numRenglon = numRenglon;
          }
          numRenglon++;
        }
        else
            numRenglon = document.getElementById('movimientos').rows.length;
      return numRenglon;
    }
    
    function valorChildren(obj,nombreChildren){
        regresa=0;
        children = obj.childNodes;
        for (i=0;i<children.length;i++){
            if (children[i].id==nombreChildren){
                regresa = children[i].value;
            }
        } //for 
      return regresa;
    }
    
    function colocarRadioAfecta(valor){
        if(valor=='Cargo')
            document.getElementsByName('afecta')[0].checked=true;
        else
            document.getElementsByName('afecta')[1].checked=true;
    }
    
    function colocarSelectCuenta(valor,valorTipoTrans){
        if (valorTipoTrans=='1'){
          document.getElementsByName('TSR')[0].checked=true;
          actualizarSelect(document.getElementsByName('TSR')[0]);
          colocarCtaSelect(valor,document.getElementById('cuentaCA'),document.getElementById('ctaCargoAbono'));
        }
        if (valorTipoTrans=='2'){
          document.getElementsByName('TSR')[1].checked=true;
          actualizarSelect(document.getElementsByName('TSR')[1]);
          colocarCtaSelect(valor,document.getElementById('cuentaCASpei'),document.getElementById('ctaCargoAbonoSpei'));
        }
        if (valorTipoTrans=='3'){
          document.getElementsByName('TSR')[2].checked=true;
          actualizarSelect(document.getElementsByName('TSR')[2]);
          colocarCtaSelect(valor,document.getElementById('cuentaCADR'),document.getElementById('ctaCargoAbonoDR'));
        }
        
    }
    
    function colocarCtaSelect(valor,objCaja,objSelect){
      opciones = objSelect.childNodes;
      for (i=0;i<opciones.length;i++){
        if (opciones[i].value==valor){
            objCaja.value=opciones[i].text;
            opciones[i].selected=true;
            filtrar(objCaja,objSelect.name);
            break;
        }
      }// for
    }
    
    
    function editar(renglonEditar){
        renglon = renglonEditar;
        renglonEditar = renglonEditar.parentNode.parentNode;
        var children = renglonEditar.childNodes;
        x = valorChildren(children[1],'hdCA');
        colocarRadioAfecta(x);
        colocarSelectCuenta(valorChildren(children[2],'hdCuentaCA'),valorChildren(children[3],'hdTipoTransferencia'));
        document.getElementById('importeCA').value=valorChildren(children[3],'hdImporteCA');
        del(renglon);
    }
    
    
    function verificaDatosTabla(){
       correcto=true;
       mensaje='------  ALERTA  ------  \n';
       if(document.getElementById('importeCA').value==''){
         mensaje=mensaje+'Debe capturar el importe de la transferencia \n';
         correcto=false;
       } 
       valTipoTrans = radiovalue('TSR');   
       if(valTipoTrans==1){
         if(document.getElementById('ctaCargoAbono').selectedIndex==-1){
            mensaje=mensaje+'Debe seleccionar la cuenta de afectacion de la transferencia \n';
            correcto=false;
         } 
       }
       if(valTipoTrans==2){
         if(document.getElementById('ctaCargoAbonoSpei').selectedIndex==-1){
            mensaje=mensaje+'Debe seleccionar la cuenta de afectacion de la transferencia \n';
            correcto=false;
         } 
       }
       if(valTipoTrans==3){
         if(document.getElementById('ctaCargoAbonoDR').selectedIndex==-1){
            mensaje=mensaje+'Debe seleccionar la cuenta de afectacion de la transferencia \n';
            correcto=false;
         } 
       }
       if(!correcto)
        throw new Error(mensaje);
    }
    
    function crearRow(imagen){
      try {
          verificaDatosTabla();
          var tabla = document.getElementById('movimientos');
          imElim = document.getElementById("himgEliminar");
          imModi = document.getElementById("himgModificar");
          tabla.style.display="";
          var lRen = tabla.rows.length;
          numTrans = imagen.id.substring(3,imagen.id.length);
          var fila = tabla.insertRow(obtenerRenglonValido(numTrans));
          try {
            var col = fila.insertCell(0);
                im = imElim.cloneNode(true); im.id="imgEliminar"; im.name="imgEliminar";
                col.appendChild(crearHidden('hdNumTransaccion','hdNumTransaccion',numTrans)); 
            //var img = crearImg('imgEliminar','imgEliminar','../../Librerias/Imagenes/botonEliminar2.gif');
                //img.onclick = function() {del(this.parentNode.parentNode.rowIndex)};    
                //img.onclick = agregarEvento(img,'click',del,img);
                col.appendChild(im);  
                col.appendChild(document.createTextNode(' '));
                im = imModi.cloneNode(true); im.id="imgModificar"; im.name="imgModificar";
                //img = crearImg('imgModificar','imgModificar','../../Librerias/Imagenes/botonEditar2.gif');
                //img.onclick = function() {editar(this.parentNode.parentNode)};    
                //img.onclick = agregarEvento(img,'click',editar,img);   
                col.appendChild(im);
            var col  = fila.insertCell(1);
            var valRadio = radiovalue('afecta');
                col.appendChild(document.createTextNode(valRadio));
                col.appendChild(crearHidden('hdCA','hdCA',valRadio));  
                col.align='center';
       
            var col  = fila.insertCell(2);
            var valTipoTrans = radiovalue('TSR');   
                if(valTipoTrans==1){
                    col.appendChild(document.createTextNode(form1.ctaCargoAbono.options[form1.ctaCargoAbono.selectedIndex].text));
                    col.appendChild(crearHidden('hdCuentaCA','hdCuentaCA',document.getElementById('ctaCargoAbono').value)); 
                }
                if(valTipoTrans==2){
                    col.appendChild(document.createTextNode(form1.ctaCargoAbonoSpei.options[form1.ctaCargoAbonoSpei.selectedIndex].text));
                    col.appendChild(crearHidden('hdCuentaCA','hdCuentaCA',document.getElementById('ctaCargoAbonoSpei').value)); 
                }
                if(valTipoTrans==3){
                    col.appendChild(document.createTextNode(form1.ctaCargoAbonoDR.options[form1.ctaCargoAbonoDR.selectedIndex].text));
                    col.appendChild(crearHidden('hdCuentaCA','hdCuentaCA',document.getElementById('ctaCargoAbonoDR').value)); 
                }
            var col  = fila.insertCell(3);
                col.appendChild(document.createTextNode(document.getElementById('importeCA').value));
                col.appendChild(crearHidden('hdImporteCA','hdImporteCA',document.getElementById('importeCA').value)); 
                col.align='right';
                col.appendChild(crearHidden('hdTipoTransferencia','hdTipoTransferencia',radiovalue('TSR'))); 
           } catch(e) {
              alert(e.message);
           }
      }
      catch(e) {
        alert(e.message);
      }
    
     
    }
    
    function del(obj){
      obj = obj.parentNode.parentNode;
      document.getElementById('movimientos').deleteRow(obj.rowIndex);
    }
    
    
    function radiovalue(grupo) {
      var radios = document.getElementsByName(grupo);
      for (i = 0; radio = radios[i]; i++) {
        if (radio.checked) {
          return radio.value;
        }
      }
    }
    
    function filtrar(obj,lista) {
      sel = document.getElementById(lista);
      for (i=0; opt = sel.options[i]; i++) {
        txt = opt.text;
        opt.style.display = (txt.indexOf(obj.value) == 0) ? 'block' : 'none';
      }
      // seleccionar primer item visible
      for (i=0; opt = sel.options[i]; i++) 
      if (opt.style.display=='block') {
        sel.selectedIndex=i;
        break;
      }
    } 
    
    function getRadioButtonSelectedValue(elemento)
    {
    for(i=0;i<elemento.length;i++)
        if(elemento[i].checked) return elemento[i].value;
    }

    function verificarMontosCA(){
      montosCorrectos = true;
      ren = 0;
      col = 0;
      sumaCargo=0;
      sumaAbono=0;
      importeCapturado=0;
      transacciones =document.getElementsByName('hdNumTransaccion');
      cargoAbono = document.getElementsByName('hdCA');
      numTransaccion = 0;
      if (getRadioButtonSelectedValue(document.getElementsByName('valida'))=="1"){
         for (i=0;i<transacciones.length;i++){
            if(transacciones[i].value==numTransaccion){  
                if( document.getElementsByName('hdCA')[col].value=='Cargo'){
                    sumaCargo = sumaCargo + parseFloat(document.getElementsByName('hdImporteCA')[col].value);
                }
                else{
                    sumaAbono = sumaAbono + parseFloat(document.getElementsByName('hdImporteCA')[col].value);
                }
                col++;
            }
            else{
                sumaCargo = sumaCargo.toFixed(2);
                sumaAbono = sumaAbono.toFixed(2);
                 if(sumaCargo!=0 || sumaAbono!=0){
                    importeCapturado = parseFloat(document.getElementById('importe').value);
                    importeCapturado = importeCapturado.toFixed(2);
                    if(sumaCargo!=importeCapturado || sumaAbono!=importeCapturado ) {
                        montosCorrectos = false;
                        throw new Error('La sumatoria de cargos y/o abonos, no puede ser diferente al importe capturado para la transferencia, verifique la tranferencia número: '+ numTransaccion );
                    }
                }
              sumaCargo=0;
              sumaAbono=0;
            }
            numTransaccion=transacciones[i].value;
          }// for
          sumaCargo = sumaCargo.toFixed(2);
          sumaAbono = sumaAbono.toFixed(2);
          importeCapturado = parseFloat(document.getElementById('importe').value);
          importeCapturado = importeCapturado.toFixed(2);
          if(sumaCargo!=0 || sumaAbono!=0){
            if(sumaCargo!=importeCapturado || sumaAbono!=importeCapturado ){
              montosCorrectos = false;
              throw new Error('La sumatoria de cargos y/o abonos, no puede ser diferente al importe capturado para la transferencia, verifique la tranferencia número: '+ numTransaccion );
             } 
         }
      }  
      else{ /// valida cargos y abonos en general 
       for (i=0;i<cargoAbono.length;i++){
          if(document.getElementsByName('hdCA')[col].value=='Cargo'){
             sumaCargo = sumaCargo + parseFloat(document.getElementsByName('hdImporteCA')[col].value);
          }
          else{
             sumaAbono = sumaAbono + parseFloat(document.getElementsByName('hdImporteCA')[col].value);
          }
          col++;
       }// for
          sumaCargo = sumaCargo.toFixed(2);
          sumaAbono = sumaAbono.toFixed(2);
          importeCapturado = parseFloat(document.getElementById('importe').value);
          importeCapturado = importeCapturado.toFixed(2);
          if(sumaCargo!=0 || sumaAbono!=0){
            if(sumaCargo!=importeCapturado || sumaAbono!=importeCapturado ){
              montosCorrectos = false;
              throw new Error('La sumatoria de cargos y/o abonos, no puede ser diferente al importe capturado para la transferencia, verifique sus movimientos');
             } 
         }
      }// else
      return montosCorrectos;
    }
    
    function verificaFormulario(){
       correcto=true;
       mensaje='------  ALERTA  ------ \n';
       if(document.getElementById('fechaGenera').value==''){
         mensaje=mensaje+'Debe seleccionar una fecha \n';
         correcto=false;
       }  
       if(document.getElementById('importe').value==''){
         mensaje=mensaje+'Debe capturar el monto de la transferencia de fondos  \n';
         correcto=fals
       }  
       if(document.getElementById('autoriza').selectedIndex==0){
         mensaje=mensaje+'Debe seleccionar la firma de autorización \n';
         correcto=false;
       } 
       if(document.getElementById('reviso').selectedIndex==0){
         mensaje=mensaje+'Debe seleccionar la firma de revisión \n';
         correcto=false;
       } 
       if(document.getElementById('elaboro').selectedIndex==0){
         mensaje=mensaje+'Debe seleccionar la firma de elaboración \n';
         correcto=false;
       }
       if(!correcto)
        throw new Error(mensaje);
       return correcto;
    }
    
    function asignaValorRep(boton){
      try{
        document.getElementById('tipoReporte').value=boton;
        if (verificaFormulario() &&  verificarMontosCA() ){
          form1.action='tesTransferenciaFondosControl.jsp';
          form1.submit();
        }
        
        ///verificaFormulario();
        ///verificarMontosCA();
      } catch(e){
        alert(e.message);
      }
    }
    
    
    
    function actualizarSelect(tis){
      document.getElementById('transferenciaCuenta').style.display='none';
      document.getElementById('transferenciaSpei').style.display='none';
      document.getElementById('transferenciaDR').style.display='none';
      document.getElementById('transferenciaCuentaCaja').style.display='none';
      document.getElementById('transferenciaSpeiCaja').style.display='none';
      document.getElementById('transferenciaDRCaja').style.display='none';
      if (tis.value=='1'){
        document.getElementById('transferenciaCuenta').style.display='';
        document.getElementById('transferenciaCuentaCaja').style.display='';
      }
      if (tis.value=='2'){
        document.getElementById('transferenciaSpei').style.display='';
        document.getElementById('transferenciaSpeiCaja').style.display='';
      }
      if (tis.value=='3'){
        document.getElementById('transferenciaDR').style.display='';
        document.getElementById('transferenciaDRCaja').style.display='';
      }
    }
       
</script>
  </head>
  <% 
    boolean mostrar = false;
    mostrar = request.getParameter("area").equals("2")?true:false;

    crsDestinatario.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.destinatariosTF");

    crsAutoriza.addParam("docto","STFO");
    crsAutoriza.addParam("tipo","AUT");
    crsAutoriza.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsAutoriza.liberaParametros();
    
    crsRevisa.addParam("docto","STFO");
    crsRevisa.addParam("tipo","REV");
    crsRevisa.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsRevisa.liberaParametros();
    
    crsElabora.addParam("docto","STFO");
    crsElabora.addParam("tipo","ELB");
    crsElabora.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.RhTrFirmasDocsTesoreriaTransferencia");
    crsElabora.liberaParametros();
    
  %>
  <body>
    <form id="form1" name="form1" action="tesTransferenciaFondosControl.jsp" method="POST" target="_blank">
        <%util.tituloPagina(out,"Tesorería","Reporte","Transferencia de fondos",true);%>
        <div style="display:none">
          <img src="../../Librerias/Imagenes/botonAgregar2.gif" name="himgAgregar" id="himgAgregar" 
                            onclick="crearRow(this)">
          <img src="../../Librerias/Imagenes/botonEliminar2.gif" name="himgEliminar" id="himgEliminar" 
                            onclick="del(this)">
          <img src="../../Librerias/Imagenes/botonEditar2.gif" name="himgModificar" id="himgModificar" 
                            onclick="editar(this)">
        </div>
        <input type="hidden" value="<%=request.getParameter("area")%>" id="area" name="area">
        <input type="hidden" id="tipoReporte" name="tipoReporte">
        <br>
        <br>
        <table align="center" cellpadding="6">
          <thead></thead>
            <tbody>
              <tr>
                <td>Fecha: </td>
                <td>
                  <input type="text" name="fechaGenera" id="fechaGenera" value="" class="cajaTexto" readonly>
                    <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
                    onClick="open_calendar('../../Librerias/Javascript/Calendario/Calendar/calendar.html','document.forms[0].fechaGenera')">
                    <img src="../../Librerias/Javascript/Calendario/Calendar/cal.gif" name="imgCalendar" border="0" alt=""></a>
                </td>
              </tr>
              <tr>
                <td>Importe:</td>
                <td>
                  <input type="text" name="importe" id="importe" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25" >
                </td>
              </tr>
              <tr <%=mostrar?"style='display:none'":""%>>
                <td>Tipo de transferencia:</td>
                <td >
                  <input type="radio" name="tipo" value="INV" checked> Inversiones<br>
                  <input type="radio" name="tipo" value="ING"> Ingresos<br>
                  <input type="radio" name="tipo" value="REA"> Reintegros de ejercicios anteriores<br>
                </td>
              </tr>
              <tr>
                <td>Dirigido a:</td>
                <td>
                    <select class="cajaTexto" id="dirigidoA" name="dirigidoA"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsDestinatario, out,"ID_DESTINATARIO","PUESTO","");%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>Concepto:</td>
                <td>
                  <textarea cols="80" rows="2" id="concepto" name="concepto"  class="cajaTexto"></textarea>
                </td>
              </tr>
              <tr>
                <td>Validacion de cargos/abonos:</td>
                <td>
                  <input type="radio" name="valida" value="1" checked>Por transferencia<br>
                  <input type="radio" name="valida" value="2"> Grupo de transferencia<br>
                </td>
              </tr>
              <tr>
                <td >Afectaci&oacute;n:</td>  
                <td>
                  <hr>
                </td>
              </tr>
              <tr>
                <td></td>
                <td>
                  <table width="100%" align="center">
                    <thead></thead>
                    <tbody>
                      <tr>
                        <td></td>
                        <td>Tipo de movimiento </td>
                      </tr>
                      <tr>
                       <td>
                          <input type="radio" name="TSR" id="TSR" value="1" onclick="actualizarSelect(this)" checked>Cuentas mismo banco
                          <input type="radio" name="TSR" id="TSR" value="2" onclick="actualizarSelect(this)">SPEI
                          <input type="radio" name="TSR" id="TSR" value="3" onclick="actualizarSelect(this)">Depositos referenciados
                        </td>
                        <td>
                          <input type="radio" name="afecta" id="afecta" value="Cargo" checked>Cargo
                          <input type="radio" name="afecta" id="afecta" value="Abono">Abono 
                        </td>
                      </tr>
                    <tr>
                     <td>Cuenta:</td>
                      <td>Importe:</td>
                    </tr>
                    <tr>
                     
                      <td>
                        <div id="transferenciaCuentaCaja">
                          <input type="text"  onkeyup=filtrar(this,'ctaCargoAbono'); size="62" name="cuentaCA" id="cuentaCA" class="cajaTexto" >  
                        </div>
                        <div id="transferenciaSpeiCaja" style="display:none">
                          <input type="text"  onkeyup=filtrar(this,'ctaCargoAbonoSpei'); size="62" name="cuentaCASpei" id="cuentaCASpei" class="cajaTexto" >  
                        </div>
                        <div id="transferenciaDRCaja" style="display:none">
                          <input type="text"  onkeyup=filtrar(this,'ctaCargoAbonoDR'); size="62" name="cuentaCADR" id="cuentaCADR" class="cajaTexto" >  
                        </div>
                      </td>
                       <td>
                        <input type="text" name="importeCA" id="importeCA" class="cajaTexto" onkeypress="validarFlotante(event,this)" size="25">
                      </td>
                    </tr>
                    <tr>
                     
                      <td>
                      <%
                          crsCuentaCA.addParamVal("tipo","and tipo=:param","1");
                          crsCuentaCA.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.afectacion");
                          crsCuentaCA.liberaParametros(); 
                      %>
                      <div id="transferenciaCuenta">
                          <select class="cajaTexto" id="ctaCargoAbono" name="ctaCargoAbono" size="5" onclick="form1.cuentaCA.value=form1.ctaCargoAbono.options[this.selectedIndex].text"> 
                          <option value=''>- Seleccione -</option>
                          <%CRSComboBox(crsCuentaCA, out,"idCuenta","cuenta,beneficiario","");%>
                          </select>  
                      </div>
                      <%
                          crsCuentaCA.addParamVal("tipo","and tipo=:param","2");
                          crsCuentaCA.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.afectacion");
                          crsCuentaCA.liberaParametros(); 
                      %>
                      <div id="transferenciaSpei" style="display:none">
                          <select class="cajaTexto" id="ctaCargoAbonoSpei" name="ctaCargoAbonoSpei" size="5" onclick="form1.cuentaCASpei.value=form1.ctaCargoAbonoSpei.options[this.selectedIndex].text"> 
                          <option value=''>- Seleccione -</option>
                          <%CRSComboBox(crsCuentaCA, out,"idCuenta","cuenta,beneficiario","");%>
                          </select>  
                      </div>
                      <%
                          crsCuentaCA.addParamVal("tipo","and tipo=:param","3");
                          crsCuentaCA.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.afectacion");
                          crsCuentaCA.liberaParametros(); 
                      %>
                      <div id="transferenciaDR" style="display:none">
                          <select class="cajaTexto" id="ctaCargoAbonoDR" name="ctaCargoAbonoDR" size="5" onclick="form1.cuentaCADR.value=form1.ctaCargoAbonoDR.options[this.selectedIndex].text"> 
                          <option value=''>- Seleccione -</option>
                          <%CRSComboBox(crsCuentaCA, out,"idCuenta","cuenta,beneficiario","");%>
                          </select>  
                      </div>
                      </td>
                       <td></td>
                    </tr>
                    </tbody>
                  </table> 
                </td>               
              </tr>
              <tr>
                <td></td>
                <td>
                  <table id="movimientos" width="100%" align="center">
                    <thead>
                      <tr>
                        <th class="azulObs" width="10%">Opciones</th>
                        <th class="azulObs" width="15%" >Afectaci&oacute;n</th>
                        <th class="azulObs" width="60%">Cuenta</th>
                        <th class="azulObs" width="15%">Monto</th>
                      </tr>
                      <tr class="azulCla">
                        <th width="10%" align="left"><img src="../../Librerias/Imagenes/botonAgregar2.gif" name="img1" id="img1" 
                            onclick="crearRow(this)"><input type="hidden" name="hdNumTransaccion" id="hdNumTransaccion" value="1"></th>
                        <th colspan="3" class="azulCla">Referencia<input type="text" class="cajaTexto" name="referencia" id="referencia" size="40"></th>
                      </tr>
                    </thead>
                    <tbody>
                    </tbody>
                   </table> 
                </td>
              </tr>
              <tr>
                <td></td>  
                <td align="center">
                 <input type="button" class="boton" value="Agregar nueva transferencia" onclick="agregarTransaccion('movimientos')">
                </td>
              </tr>
              <tr>
                <td></td>  
                <td>
                  <hr>
                </td>
              </tr>
              <tr>
                <td>Observaciones::</td>
                <td>
                 <textarea cols="80" rows="2" id="observaciones" name="observaciones"  class="cajaTexto"></textarea>
                </td>
              </tr>
              <tr>
                <td>Elaboro:</td>
                <td>
                  <select class="cajaTexto" id="elaboro" name="elaboro"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsElabora, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>Reviso:</td>
                <td>
                  <select class="cajaTexto" id="reviso" name="reviso"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsRevisa, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
                  </select>
                </td>
              </tr>
              <tr>
                <td>Autorizo:</td>
                <td>
                  <select class="cajaTexto" id="autoriza" name="autoriza"> 
                    <option value=''>- Seleccione -</option>
                    <%CRSComboBox(crsAutoriza, out,"NUM_EMPLEADO","NOMBRE,PUESTO_ESP","");%>
                  </select>
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
          <td><input type="button" class="boton" value="Generar PDF" onclick="asignaValorRep('PDF')"></td>
        </tr>
      </tbody>
    </table>
    </form>
  </body>
</html>