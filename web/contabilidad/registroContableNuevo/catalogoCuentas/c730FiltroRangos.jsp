<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page import="sia.rf.contabilidad.acceso.CargaExc"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro,sia.beans.seguridad.Autentifica"%>
<%@ page  import="sia.db.sql.Sentencias"%> 
<%@ page  import="sia.db.sql.Vista"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%
  Map parametros = new HashMap();
  request.getSession().setAttribute("controlRegistro", new ControlRegistro());
  ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  Autentifica autentifica = (Autentifica)request.getSession().getAttribute("Autentifica"); 
  
  String lsUniEje=autentifica.getUnidadEjecutora();
  if (lsUniEje.length() == 3)
      lsUniEje = "0"+lsUniEje;  
  String lsPais="147";   
  String lsEntidad=String.valueOf(autentifica.getEntidad());
  String lsAmbito=String.valueOf(autentifica.getAmbito()); 
  if (lsEntidad.length() == 1)
      lsEntidad = "00"+lsEntidad;
  if (lsEntidad.length() == 2)
      lsEntidad = "0"+lsEntidad;
  controlReg.inicializa(autentifica, Integer.parseInt(request.getParameter("idCatalogoCuenta") == null ? "1" :request.getParameter("idCatalogoCuenta")));
  String lsTipoUsuario=String.valueOf(controlReg.getTipoUsuario()); //prueba de subversion
  
   String unidades="";
  Sentencias sentencia = null;
  List<Vista> registros = null;
  String regresa = "";
  sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  registros = new ArrayList<Vista>();
  registros = sentencia.registros("vistas.select.unidadesUsuario","numEmpleado="+autentifica.getNumeroEmpleado()+"|");
  if(registros!=null){
    for(Vista registro:registros){
      regresa = registro.getField("UNIDADES_EJECUTORAS");
    }
  }
  unidades = regresa;
  if(!unidades.equals("")){
    String  unidadVec[] = unidades.split(",");
    unidades = "";
    for(int i=0; i<unidadVec.length; i++){
      unidades += "0"+unidadVec[i]+",";
    }
    unidades = unidades.substring(0,unidades.length()-1);
  }

%>

<jsp:useBean id="abCierre" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abCierre.setTableName("SIA_CATALOGOS.TC_UNI_EJECUTORAS");
      abCierre.setCommand(sentencia.getComando("filtros.select.ejercicios", parametros));
      abCierre.execute(conexion);
      conexion.close();
      conexion=null;
      parametros = null;
   %>
</jsp:useBean> 

<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Filtro por Rangosl</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
 <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
 <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
<script language="JavaScript" type="text/javascript">
<!--
var InterVacio = false;
var mensajes = "AVISO: ";
var vacio=false;
function checkFields() {   
   var num = document.Formulario.elements.length;
   var validFlag = true;
   var vacio=true;
   InterVacio = true;

   for (var i=6; i<24; i++) {  
     if ((document.Formulario.elements[i].value == "" && document.Formulario.elements[i+1].value != "") ||
       (document.Formulario.elements[i].value != "" && document.Formulario.elements[i+1].value == "")){
       validFlag = false;
       mensajes = mensajes+" Los Rangos de Cuentas no pueden ir uno con inFormulariocion y el otro vacío  \n";
//     alert(mensajes);
//     break ;              
      }else{
        if (document.Formulario.elements[i].value > document.Formulario.elements[i+1].value){
	  validFlag = false;
          mensajes = mensajes+" El primer valor del rango "+document.Formulario.elements[i].value+" debe ser menor que el segundo "+document.Formulario.elements[i+1].value+" \n";
//        alert(mensajes);
//        break ;              
	}
    }
    if(i==6){
      if(document.Formulario.elements[i].value=="" && document.Formulario.elements[i].value==""){
      mensajes = mensajes+" Los rangos a nivel de cuenta no pueden ir vacios \n";
//    alert(mensajes);
      validFlag = false;
//    break;
    }
  } 
  i++;
} //for
return validFlag;
}//checkFields
 
//FUNCION QUE RELLENA CON CEROS LOS ELEMENTOS parametros THIS,THIS.VALUE,tam
function RellenaCerosIzq(campo,valor,tam){
 if (campo.value.match(/^\d+$/)){
    cadcero='';
    for(i=0;i<(tam-valor.length);i++){
       cadcero+='0';
    }
    campo.value=cadcero+valor;
 }else{
       campo.value="";}
}//RellenaCerosIzq
 
//FUNCION QUE rellena CON nueves LOS ELEMENTOS parametros THIS,THIS.VALUE,tam
function RellenaNuevesDer(campo,valor,tam){
 if (campo.value.match(/^\d+$/)){
    cadnueve='';
    for(i=valor.length;i<tam;i++){
       cadnueve+='9';
    }
    campo.value=valor+cadnueve;
 }else
       campo.value="";						
} //RellenaNuevesDer
 
function chkInterRanVacio(){
  validFlag = true;
  for(var i=6; i<24; i=i+2) {
     if(document.Formulario.elements[i].value == "" && document.Formulario.elements[i+1].value == ""){
       if (i < 24){
	 vacio=true;
	 for (var j=i+6; j<24; j++){              
           if (document.Formulario.elements[j].value != "" && document.Formulario.elements[j+1].value != ""){
             vacio=false;
           }
	 }//for
         if(!vacio){
           validFlag = false;
           mensajes = mensajes+" Los rangos intermedios no pueden ir vacíos \n";
         }
      }
      i=22;
    }
  } //for
  return validFlag; 
}//ckRanVacio

function Valida(){
    mensajes = "AVISO: ";
    for (var i=6; i<24; i++){              
      if (document.Formulario.elements[i].value != "" && document.Formulario.elements[i+1].value != ""){
        vacio=false;
      }
     }//for
     if(vacio){
       mensajes = mensajes+" Los Rangos de Cuentas no deben ir vacíos "+" \n";
       return false;
     }else{
       if(checkFields()) {
         if(chkInterRanVacio()){
           return true;
	 }
       	 alert(mensajes);
         return false;
       }else {
	 alert(mensajes);
         return false;
        }
      }
}//Valida
 
function Enviar(){
    if(Valida()){
        //Formulario.submit();
     alert("Listo para enviar"); 
    }
}//Enviar

function cargaCta(){
var tipoUsr = <%=lsTipoUsuario%>;
//var tipoUsr = 3;   // para probar OJO
if ( tipoUsr == 2 ) {
  document.Formulario.txtCtaIni[3].value = '<%=lsUniEje%>';
  document.Formulario.txtCtaFin[3].value = '<%=lsUniEje%>'; 
  document.Formulario.txtCtaIni[4].value = '<%=lsEntidad%><%=lsAmbito%>';
  document.Formulario.txtCtaFin[4].value = '<%=lsEntidad%><%=lsAmbito%>';
  if(<%=unidades.length()==0%>){
  document.Formulario.txtCtaIni[3].disabled="1";
  document.Formulario.txtCtaFin[3].disabled="1";
  }
}
if ( tipoUsr == 3) {
  document.Formulario.txtCtaIni[3].value = '<%=lsUniEje%>';
  document.Formulario.txtCtaFin[3].value = '<%=lsUniEje%>'; 
  document.Formulario.txtCtaIni[4].value = '<%=lsEntidad%><%=lsAmbito%>';
  document.Formulario.txtCtaFin[4].value = '<%=lsEntidad%><%=lsAmbito%>';
  document.Formulario.txtCtaIni[3].disabled="1";
  document.Formulario.txtCtaFin[3].disabled="1"; 
  document.Formulario.txtCtaIni[4].disabled ="1";
  document.Formulario.txtCtaFin[4].disabled="1";
} // if(tipoUsr == 3)
} // cargaCta()

function asignaValor(){
  var tipoUsr = <%=lsTipoUsuario%>;
  if(document.getElementById('cbxSaldoCero').checked)
    document.getElementById('saldosCero').value=true;
  else
    document.getElementById('saldosCero').value=false;
    
  if ( tipoUsr == 2 && <%=unidades.length()==0%>) {
    document.Formulario.txtCtaIni[3].disabled=false;
    document.Formulario.txtCtaFin[3].disabled=false;
  }
  if ( tipoUsr == 3) {
    document.Formulario.txtCtaIni[3].disabled=false;
    document.Formulario.txtCtaFin[3].disabled=false;
    document.Formulario.txtCtaIni[4].disabled=false;
    document.Formulario.txtCtaFin[4].disabled=false;
  }
  if(Valida()){
      Formulario.submit();
  }
  if ( tipoUsr == 2 && <%=unidades.length()==0%>) {
    document.Formulario.txtCtaIni[3].disabled="1";
    document.Formulario.txtCtaFin[3].disabled="1";
  }
  if ( tipoUsr == 3) {
    document.Formulario.txtCtaIni[3].disabled="1";
    document.Formulario.txtCtaFin[3].disabled="1";
    document.Formulario.txtCtaIni[4].disabled ="1";
    document.Formulario.txtCtaFin[4].disabled="1";
  }
  return true;
}

function desactivar(reporte){
  if(reporte == '0'){
   document.getElementById('xls').disabled = false;
   document.getElementById('pdf').checked = "checked";
   //document.getElementById('cbxSaldoCero').disabled = false;
  }
  else{
   document.getElementById('xls').disabled = "disabled";
   document.getElementById('pdf').checked = "checked";
   document.getElementById('xls').checked = false;
   //document.getElementById('cbxSaldoCero').disabled = "disabled";
  }
}

function verificaCierre(){
  //activar = false;
  unidad = document.getElementById("unidad").value;
  document.getElementById("cpublica").style.display = "none";
  if(unidad != "100"){
    document.getElementById("precierre").style.display = "none";  
  }
}

function verificaCPublica(){
  mes = document.getElementById("cboMes").value;
  if(mes=="12"){
    document.getElementById("cpublica").style.display = "";
  }
  else{
    document.getElementById("cpublica").style.display = "none";
  }
}

function oculta(){
  document.getElementById("generaReporte").style.display = "none";
}

//función que verifica las unidades ejecutoras que puede consultar un usuario tipo=2
function validaUnidadEjecutora(unidades,orden,tipoUsr){
    //alert('parametro:'+unidades);
    //alert('valor: '+document.Formulario.txtCtaIni[2].value);
    if(orden==1 && tipoUsr==2){
      if(unidades.indexOf(document.Formulario.txtCtaIni[3].value)<0){
        alert("Datos validos son : "+unidades);
        document.Formulario.txtCtaIni[3].value='<%=lsUniEje%>';
        document.Formulario.txtCtaIni[3].focus();
       }
      }
      if(orden==2 && tipoUsr==2){
       if(unidades.indexOf(document.Formulario.txtCtaFin[3].value)<0){
         alert("Datos validos son : "+unidades);
         document.Formulario.txtCtaFin[3].value='<%=lsUniEje%>';
         document.Formulario.txtCtaFin[3].focus();
      
       }
      }
  }
  
  function validarRangos(){
   unidadIni =  document.Formulario.txtCtaIni[3].value;
   unidadFin =  document.Formulario.txtCtaFin[3].value;
   
   if(unidadIni=='0002' && unidadFin=='0002')
     document.getElementById("generaReporte").style.display = "";
   else 
     document.getElementById("generaReporte").style.display = "none";
  }

//-->
</script>
<body  topmargin=1 leftmargin=10 onload="javascript:cargaCta();javascript:verificaCierre();javascript:oculta()">
<!--<body>-->
<%util.tituloPagina(out, "Contabilidad", " ","Generacion de reportes del catálogo de cuentas" , "Reportes", true);%>
<form name="Formulario" id="Formulario"   action="c730ImprimirReporte.jsp" method="post" target="_blank" >
<table border="0" align="center">
<thead>
<tr>
<td align="right" class='negrita'>Mes:</td>
<td>		
<select name="cboMes" class="cajaTexto" id="cboMes" onchange="verificaCPublica()">
<option value="01">Enero</option>
<option value="02">Febrero</option>
<option value="03">Marzo</option>
<option value="04">Abril</option>
<option value="05">Mayo</option>
<option value="06">Junio</option>
<option value="07">Julio</option>
<option value="08">Agosto</option>
<option value="09">Septiembre</option>
<option value="10">Octubre</option>
<option value="11">Noviembre</option>
<option value="12">Diciembre</option>
</select>
 </td>
</tr>
<tr><td align="right" class='negrita'>Ejercicio:</td>
  <td>
      <SELECT id="ejercicio" class="cajaTexto" NAME="lstEjercicio" class='lst'>
         <%

          try{
          abCierre.beforeFirst(); 
          while (abCierre.next()){
          %>
            <OPTION VALUE="<%=abCierre.getString("ejercicio")%>"><%=abCierre.getString("ejercicio")%></OPTION>
          <%
         } //Fin while
         }catch (Exception e) {
           System.out.println("Error en lista de cierre: "+ e.getMessage());
         }         
         
         %>
      </SELECT> 
  </td>
</tr>  
<tr>
<td align="right" class='negrita'>Título del programa:</td>
<td><input type="text" id="txtTitulo" name="txtTitulo" size="35" class="cajaTexto" /></td>
</tr>
</thead>
</table>
</br>
<table width="40%" align="center">
 <tr class="azulCla"><td>Tipo de cierre</td></tr>
</table>
<table border="0" width="11%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="cierre" value="1" checked="checked"/>Normal</td></tr>
    <tr id="precierre" ><td><input type="radio" name="cierre" value="2"/>Eliminación</td></tr>
    <tr id="cpublica"><td><input type="radio" name="cierre"  value="3"/>Cuenta Pública</td></tr>
  </table><br>
</br>
<table width="100%">
<thead>
<tr>
<td colspan="4" align="center" class='negrita'>
<table width="40%" align="center">
 <tr class="azulCla"><td>Rango de cuentas</td></tr>
</table>
</td>
</tr>	  
</thead>
</table>
<table Border="0" id="tabCtas" width="25%" align="center">
<thead>
<tr>
<td width="15%" align="right" class='negrita'>Cuenta:</td>
<td width="10%"><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);"/></td>
<td width="15%" align="right" class='negrita'>Cuenta:</td>
<td width="10%"><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);"/></td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td width="10%"><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="1" class="cajaTexto"/></td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td width="10%"><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="1" class="cajaTexto" /></td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>                                          
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="<%=unidades.length()==0?"":"validaUnidadEjecutora('"+unidades+"',1,'"+lsTipoUsuario+"');"%>RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="<%=unidades.length()==0?"":"validaUnidadEjecutora('"+unidades+"',2,'"+lsTipoUsuario+"');"%>RellenaCerosIzq(this,this.value,4);validarRangos();" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" size="5" name="txtRango" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaNuevesDer(this,this.value,4);" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaNuevesDer(this,this.value,4);" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaNuevesDer(this,this.value,4);" /> </td>
</tr>
<tr>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaIni" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaCerosIzq(this,this.value,4);" /> </td>
<td width="15%" align="right" class='negrita'>Subcuenta:</td>
<td><input type="text" id="txtCtaFin" name="txtRango" size="5" maxlength="4" class="cajaTexto" onBlur="RellenaNuevesDer(this,this.value,4);" /> </td>
</tr>
</thead>
</table>
</br>
</br>
<table border="0" width="40%" align="center">
<thead>
<tr>
<td align="left"  class='negrita'><input type="checkbox" id="cbxSaldoCero" />Solo cuentas con movimientos(diferente de 0)</td>
</tr>
</thead>
</table>
</br>
</br>

<div id="generaReporte">
<table width="40%" align="center">
 <tr class="azulCla"><td>Seleccione</td></tr>
</table>
<table border="0" width="30%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="rangoUnidad" value="rangoUnidad" checked="checked"/>Generar reporte solo de la unidad</td></tr>
    <tr><td><input type="radio" name="rangoUnidad" value="rangoTodos"/>Generar el reporte de todas las unidades</td></tr>
  </table><br>
</br>
</div>

<table border="0" width="40%" align="center">
<thead>
<tr>
<td width="10%"></td>
<td width="30%">
<table width="70%" align="left">
 <tr class="azulCla"><td>Reporte</td></tr>
</table>
</td>
<td width="5%"></td>
<td width="30%">
<table width="70%" align="left">
 <tr class="azulCla"><td>Formato</td></tr>
</table>
</td>
</tr>

<tr>
<td width="10%"></td>
<td align="left" class='negrita'><input type="radio" id="rbtRepte" name="rbtRepte" value="0" checked="checked" onclick="desactivar(0)"/>Catálogo de Cuentas</td>
<td width="5%"></td>
<td align="left" class='negrita'><input type="radio" id="pdf" name="rbtFormato" value="0" checked="checked" />PDF</td>
</tr>
<tr>
<td width="10%"></td>
<td align="left" class='negrita'><input type="radio" id="rbtRepte" name="rbtRepte" value="1" onclick="desactivar(1)" />Cuentas colectivas</td>
<td width="5%"></td>
 <% if (!Autentifica.getPerfilAcceso().equals("16")) { %> 
<td align="left" class='negrita'><input type="radio" id="xls" name="rbtFormato" value="1" />Excel</td>
<% } %>
</tr>
</thead>
</table>
<br>
<table width="50%" align="center">
<thead>
<tr>
<td><hr noshade="noshade" width="80%" align="center" style="border-width: 1px;border-style:solid"></td>
</tr>
</thead>
</table>
<table cellpadding="5" border="0" width="15%" class="sianoborder" align="center">
<thead>
<tr align="center">
<td><input type="button" name="btnAceptar"  class="boton" value="Generar Reporte" onclick="return asignaValor()" /></td>
</tr>
</thead>
</table>	
<input type="hidden" name="saldosCero" id="saldosCero" value="false">
<input type="hidden" name="formato" id="formato" value="pdf">
<input type="hidden" name="unidad" id="unidad" value="<%=autentifica.getUnidadEjecutora()%>">
</form>
</body>
</html>