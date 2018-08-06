
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>
<%@ page import="sia.db.sql.SentenciasCRS" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbDocumentosFirmas" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="pbDocumentos" class="sia.db.sql.SentenciasCRS" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />

<%
  String contexto = request.getContextPath();
  String documentoFirmaId = request.getParameter("clave");
  try  {
    pbDocumentosFirmas.addParamVal("documentoContableId","and df.documento_contable_id=:param",documentoFirmaId);
    pbDocumentosFirmas.addParamVal("ejercicio","and fa.ejercicio=:param",controlRegistro.getEjercicio());
    pbDocumentosFirmas.addParamVal("mes","and fa.mes=:param",session.getAttribute("mes"));
    pbDocumentosFirmas.registrosMap(DaoFactory.CONEXION_CONTABILIDAD, "firmasAutorizadas.select.RfTrDocumentosFirmas-PorDocumento");
    
    pbDocumentos.addParamVal("documentoContableId","and documento_contable_id=:param",documentoFirmaId);
    pbDocumentos.addParam("ejercicio",controlRegistro.getEjercicio());
    pbDocumentos.registrosMap(DaoFactory.CONEXION_CONTABILIDAD,"firmasAutorizadas.select.RfTrDocumentosContables-Resultado");
    pbDocumentos.next();
  } catch (Exception ex)  {
    ex.printStackTrace();
  } finally  {
  }
  
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c715Modificar</title>
    <link rel="stylesheet" href="<%=contexto%>/Librerias/Estilos/siaexplorer.css" type='text/css'>
    <script language="JavaScript" src="<%=contexto%>/Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
    <script language="javascript" src="<%=contexto%>/Librerias/Javascript/crearElementos.js" type="text/javascript"></script>
    <script language="javascript" type="text/javascript">
      function validaTxt(event,tipo,elemento,longi){
        var res=true;
        //alert('Codigo de tecla pulsada: '+event.keyCode);
        if (tipo==1) {//textArea
            if (event.keyCode == 39 || event.keyCode == 34 || event.keyCode ==13 ||
               (limitaTxa(elemento.value,longi-1)==false)) { 
                alert('Caracter no válido');
                event.returnValue=false;
                }
        };
        return res;
        }
        function limitaTxa(texto,longMax){
        if(texto.length>longMax){ 
            alert("Cadena de texto demasiado larga, \n"+texto+" Longitud actual: "+texto.length+"\n Máximo "+longMax+" caracteres de extensión");
            texto=texto.substring(0,longMax);
            //alert("Nueva Cadena de texto, \n"+texto+" Longitud nueva: "+texto.length);
            return false;
        }
        else 
           return true;
        }
      
      function llamaEmpleados() {
        fnombre = document.getElementById('fnombre');
        loadSource('divTipoMeta',null,'c715CapaEmpleado.jsp?','nombreCurp='+fnombre.value);
      }
      
      function llamaAgregarFirma() {
        if(document.getElementById('selEmpleado').selectedIndex!=0) {
          selEmpleado = document.getElementById('selEmpleado');
          //alert(selEmpleado.selectedIndex);
          loadSource('divAgregar',null,'c715CapaAgregarFirma.jsp?','numEmpleado='+selEmpleado.value);
        } else {
          alert("Debe seleccionar un empleado");
        }
      }
      
      function agregarEvento(componente,evento,funcion,parametros) {
        if (componente.addEventListener){
          componente.addEventListener(evento,function() {funcion(parametros);},false);
        } else if(componente.attachEvent){
          componente.attachEvent('on'+evento, function() {funcion.call(parametros, parametros);});
      	}
      }
      
      function regresaAgregar() {
        hprefijo = document.getElementById("hprefijo");
        hnombres = document.getElementById("hnombres");
        hapellidoPat = document.getElementById("hapellidoPat");
        hapellidoMat = document.getElementById("hapellidoMat");
        hpuesto = document.getElementById("hpuesto");
        hnumEmpleado = document.getElementById("hnumEmpleado");
        ftipoFirma = document.getElementById("ftipoFirma");
        
        var tabla = document.getElementById('tbFirmantes');
        var lRen = tabla.rows.length;
        var fila = tabla.insertRow(lRen);
        var tipoFirma;
        
         try {
            var inpt;
            var col  = fila.insertCell(0);
              inpt = crearInput('prefijo','prefijo',hprefijo.value); inpt.className = "cajaTexto"; inpt.size=10;inpt.maxlen=10;
              col.appendChild(inpt);                
            col = fila.insertCell(1); fila.align='center';
              inpt = crearInput('nombres','nombres',hnombres.value); inpt.className = "cajaTexto"; inpt.size=20;inpt.maxlen=20;
              col.appendChild(inpt);
            col = fila.insertCell(2);
              inpt = crearInput('apellidoPat','apellidoPat',hapellidoPat.value); inpt.className = "cajaTexto"; inpt.size=15;inpt.maxlen=15;
              col.appendChild(inpt);
            col = fila.insertCell(3);
              inpt = crearInput('apellidoMat','apellidoMat',hapellidoMat.value); inpt.className = "cajaTexto"; inpt.size=15;inpt.maxlen=15;
              col.appendChild(inpt);
            col = fila.insertCell(4);
              inpt = crearInput('puestoFirma','puestoFirma',hpuesto.value); inpt.className = "cajaTexto"; inpt.size=40;inpt.maxlen=40;
              col.appendChild(inpt);
            col = fila.insertCell(5);
              tipoFirma = ftipoFirma.cloneNode(true);
              tipoFirma.id   = "tipoFirma";
              tipoFirma.name = "tipoFirma";
              tipoFirma.selectedIndex = ftipoFirma.selectedIndex;
              col.appendChild(tipoFirma);
            col = fila.insertCell(6);
              img = crearImg("img"+lRen,"imgs","../../Librerias/Imagenes/botonEliminar2.gif");
              //alert(img.id);alert(img.name);
              agregarEvento(img,"click",eliminarReg,img);
              col.appendChild(img);
              col.appendChild(crearHidden('numEmpleado', 'numEmpleado', hnumEmpleado.value));
              col.appendChild(crearHidden('documentoFirmaId', 'documentoFirmaId', ""));
        } catch(e) {
            alert(e.message);
        }
      }
      
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
      
      function eliminarReg(reg) {
        reg=reg.parentNode.parentNode;
        docFirma = getElemento(reg,"documentoFirmaId");
        eliminado= document.getElementById("eliminados");
        //alert(docFirma.value);
        if(docFirma.value!='')
          eliminado.value = eliminado.value + docFirma.value + ',';
        document.getElementById('tbFirmantes').deleteRow(reg.rowIndex);
        //alert(eliminado.value);
      }
      
      
      function regresar() {
        document.form1.action="c715Resultado.jsp";
        ir();
      }
    </script>
    <script language="javascript" type="text/javascript">
      function validaNoDuplicados() {
        var tabla = document.getElementById('tbFirmantes');
        var lRen = tabla.rows.length;
        numEmpleados = document.getElementsByName("numEmpleado");
        tiposFirmas = document.getElementsByName("tipoFirma");
        correcto = true;
        try {
          for(i=0; i<lRen-2; i++) {
            for(x=i+1; x<lRen-1; x++) {
              if(numEmpleados[i].value == numEmpleados[x].value && tiposFirmas[i].value == tiposFirmas[x].value)  
                correcto = false;
            }
          }
        } catch(e) {
          alert(e.message);
        }
        if(!correcto)
          alert("No puede existir una persona duplicada con el mismo tipo de firma");
        return correcto;
      }
      
      function irGuardar() {
        if(validaNoDuplicados())
          ir();
      }
      
      var submited=false;
      function ir() {
        if(!submited) {
          document.form1.submit();
          submited = true;
        }
      }
    </script>
    
  </head>
  <body>
    <form id="form1" name="form1" method="POST" action="c715Control.jsp">
    <%util.tituloPagina(out,"Contabilidad","Modificaci&oacute;n de firmas autorizadas","Modificar",true);%>
    <input type="hidden" id="documentoContableId" name="documentoContableId" value="<%=pbDocumentos.getString("documento_contable_id")%>">
    <input type="hidden" id="eliminados" name="eliminados" value="">
    <input type="hidden" id="documento" name="documento" value="<%=pbDocumentos.getString("documento")%>">
    <IFrame style="display:none" name = "bufferCurp" id='bufferCurp'>
      </IFrame>
    <br>
    <br>
      <table align='center' class='general' width="40%" cellpadding="0" cellspacing="0">
        <thead></thead>
        <tbody>
          <tr>
            <td>
              <table align='center' class='general' width="100%">
                <thead>
                  <tr>
                    <th class='azulObs'>Unidad</th>
                    <th class='azulObs'>Entidad</th>
                    <th class='azulObs'>Ambito</th>
                  </tr>
                  
                </thead>
                <tbody>
                  <tr>
                    <td align="center"><%=controlRegistro.getUnidad()%></td>
                    <td align="center"><%=controlRegistro.getEntidad()%></td>
                    <td align="center"><%=controlRegistro.getAmbito()%></td>
                    
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
          <tr>
            <td>
              <table align='center' class='general' width="100%">
                <thead>
                  <tr>
                    <th class='azulObs'>Ejercicio</th>
                    <th class='azulObs'>Mes</th>
                    <th class='azulObs'>Documento</th>
                  </tr>
                  
                </thead>
                <tbody>
                  <tr>
                    <td align="center"><%=controlRegistro.getEjercicio()%></td>
                    <td align="center"><%=session.getAttribute("mes")%></td>
                    <td align="center"><%=pbDocumentos.getString("documento")%></td>
                    
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
      
      
      <br/>
      <br/>
    <br>
    
      <table align='center' class='general'>
        <thead></thead>
        <tbody>
          <tr>
            <td>
              <table align='center' class='general' cellpadding="3">
                <thead>
                  
                </thead>
                <tbody>
                  <tr>
                    <th class='azulObs' align="right">Nombre o curp:</th>
                    <td><input type="text" class="cajaTexto" name="fnombre" id="fnombre" size="40"/>
                        <input type="button" class="boton" value="Buscar" onclick="llamaEmpleados()">
                    </td>
                  </tr>
                  <tr>
                    <th class='azulObs' align="right">Encontrados:</th>
                    <td><div id="divEmpleados"><select name="selEmpleado" id="selEmpleado" class="cajaTexto">
                      <option>-Seleccione-</option>
                    </select> </div></td>
                  </tr>
                  <tr>
                    <th class='azulObs' align="right">Tipo:</th>
                    <td>
                      <select class="cajaTexto" id="ftipoFirma" name="ftipoFirma">
                        <option value="ELB">Elaboro</option>
                        <option value="REV">Reviso</option>
                        <option value="AUT">Autorizo</option>
                      </select> 
                    </td>
                  </tr>
                </tbody>
              </table>
            </td>
          </tr>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <div id="divAgregar"></div>
      <table align='center' class='general'>
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Agregar firma" onclick="llamaAgregarFirma()"></td>
          </tr>
        </tbody>
      </table>
      <br>
      <table align='center' class='general' width="80%" id="tbFirmantes">
        <thead>
          <tr>
            <th class='azulObs'>Prefijo</th>
            <th class='azulObs'>Nombre(s)</th>
            <th class='azulObs'>Apellido paterno</th>
            <th class='azulObs'>Apellido materno</th>
            <th class='azulObs'>Puesto</th>
            <th class='azulObs'>Tipo firma</th>
            <th class='azulObs' width="10%">Opciones</th>
          </tr>
        </thead>
        <tbody>
  
          <% int cont=1;
          while(pbDocumentosFirmas.next()) {%>
          <tr>
            
            <td align="center"><input type="text" class="cajaTexto" name="prefijo" id="prefijo" size="10" maxlength="10" 
                onkeypress="if(validaTxt(event,1,this,this.maxlength)==false) event.returnValue=false;"
                onblur= "this.value=this.value.toUpperCase();"
                value="<%=pbDocumentosFirmas.getString("prefijo")==null?"":pbDocumentosFirmas.getString("prefijo")%>"/></td>
            <td align="center"><input type="text" class="cajaTexto" name="nombres" id="nombres" size="20" maxlength="50"
                onkeypress="if(validaTxt(event,1,this,this.maxlength)==false) event.returnValue=false;"
                onblur= "this.value=this.value.toUpperCase();"
                value="<%=pbDocumentosFirmas.getString("nombres")==null?"":pbDocumentosFirmas.getString("nombres")%>"/></td>
            <td align="center"><input type="text" class="cajaTexto" name="apellidoPat" id="apellidoPat" size="15" maxlength="30" 
                onkeypress="if(validaTxt(event,1,this,this.maxlength)==false) event.returnValue=false;"
                onblur= "this.value=this.value.toUpperCase();"
                value="<%=pbDocumentosFirmas.getString("apellido_pat")==null?"":pbDocumentosFirmas.getString("apellido_pat")%>"/></td>
            <td align="center"><input type="text" class="cajaTexto" name="apellidoMat" id="apellidoMat" size="15" maxlength="30" 
                onkeypress="if(validaTxt(event,1,this,this.maxlength)==false) event.returnValue=false;"
                onblur= "this.value=this.value.toUpperCase();"
                value="<%=pbDocumentosFirmas.getString("apellido_mat")==null?"":pbDocumentosFirmas.getString("apellido_mat")%>"/></td>
            <td align="center"><input type="text" class="cajaTexto" name="puestoFirma" id="puestoFirma" size="40" maxlength="30" 
                onkeypress="if(validaTxt(event,1,this,this.maxlength)==false) event.returnValue=false;"
                onblur= "this.value=this.value.toUpperCase();"
                value="<%=pbDocumentosFirmas.getString("puesto")==null?"":pbDocumentosFirmas.getString("puesto")%>"/></td>
            <td align="center"><select class="cajaTexto" id="tipoFirma" name="tipoFirma">
                        <option value="ELB" <%=pbDocumentosFirmas.getString("firma").equals("ELB")?"selected":""%>>Elaboro</option>
                        <option value="REV" <%=pbDocumentosFirmas.getString("firma").equals("REV")?"selected":""%>>Reviso</option>
                        <option value="AUT" <%=pbDocumentosFirmas.getString("firma").equals("AUT")?"selected":""%>>Autorizo</option>
                      </select> </td>
            <td align="center"><img src="../../Librerias/Imagenes/botonEliminar2.gif"  name="imgs" id="img<%=cont++%>" onclick="eliminarReg(this)">
            <input type="hidden" id="numEmpleado" name="numEmpleado" value="<%=pbDocumentosFirmas.getString("num_empleado")%>">
            <input type="hidden" id="documentoFirmaId" name="documentoFirmaId" value="<%=pbDocumentosFirmas.getString("documento_firma_id")%>">
             <input type="hidden" name="mes" id="mes" value="<%=session.getAttribute("mes")%>"></td>
          </tr>
          <%}%>
        </tbody>
      </table>
      <br>
      <hr class="piePagina">
      <br>
      <table align='center' cellpadding="3">
        <thead></thead>
        <tbody>
          <tr>
            <td><input type="button" class="boton" value="Aceptar" onclick="irGuardar()"></td>
            <td><input type="button" class="boton" value="Regresar" onclick="regresar()"></td>
          </tr>
        </tbody>
      </table>
    </form>
  </body>
</html>