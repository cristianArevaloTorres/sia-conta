<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="sia.db.dao.*, sia.db.sql.Sentencias"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<jsp:useBean id="pbEstadoCat" class="sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<%  Sentencias sentencias = null;
    Map parametros = null;
    int maximoNivel = -1;

    Connection conexion=null;
    String lsUniEje=controlRegistro.getUnidad();
    String lsEntidad=String.valueOf(controlRegistro.getEntidad());
    String lsAmbito=String.valueOf(controlRegistro.getAmbito());   
    String lsCatCuenta=String.valueOf(controlRegistro.getIdCatalogoCuenta());    
    String lsEjercicio=String.valueOf(controlRegistro.getEjercicio());
    
    boolean lsAdmin=controlRegistro.isUsuarioAdmin();
    boolean lsInter=controlRegistro.isUsuarioIntermedio();
    boolean lsBajo=controlRegistro.isUsuarioBajo();    
    int totNivel = 9;   
    String lsMsg="";
    if(Integer.parseInt(lsEjercicio)<=2010){
        if (!lsCatCuenta.equals("3"))
            totNivel=7; }
    boolean bandera=true;
    //se obtiene la fecha actual
    if ( controlRegistro.getFechaAfectacion()==null){
        fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }
    //se validara que solamente se puede utilizar el ABC para el ejecricio actual y uno  anterior
    String lsFecAct=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion());
    String lsEjeAct=lsFecAct.substring(6);
    if(Integer.parseInt(lsEjeAct) == Integer.parseInt(lsEjercicio) || Integer.parseInt(lsEjeAct) == Integer.parseInt(lsEjercicio)+1){  
    bandera=false;
    }
    try{
         parametros = new HashMap();
         sentencias = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD);
         parametros.put("idCatalogoCuenta", controlRegistro.getIdCatalogoCuenta());
         parametros.put("ejercicio",controlRegistro.getEjercicio());
         maximoNivel = Integer.valueOf(sentencias.consultar("clasificadorCuenta.select.obtenerMaxNivel",parametros).toString()); 
        conexion=DaoFactory.getContabilidad();
        
        String estatus="";
        String descri="";
        pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
        estatus=pbEstadoCat.getEstatus();
        descri=pbEstadoCat.getDescripcion();
        if (!estatus.equals("1")){            
            lsMsg="AVISO: En estos momentos no es posible crear, modificar o eliminar cuentas contables, ya que hay un proceso de "+ descri+" ejecutandose. Favor de intentarlo mas tarde.";
            bandera=true;
        }
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Catálogo de Cuentas Por Cuenta Individual</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>

<script type="text/javascript" language="javascript">
 var nivelReporte = 0;
 var caja='';
 var lsNivel="";
var muestra=false;

function regresa(){
  var x;
  x=1;
}

function cargaPagina(){  
<%
    for(int l=2; l<=totNivel; l++){
%>
        trlstNivel<%=l%>.style.display='none';
<%  }//for

    for(int l=0; l<totNivel; l++){
%>
        divsel[<%=l%>].style.display ='none';
<%  }//for
%>

  var nr = 0;
  formulario.lstNivel1.selectedIndex=0;
    muestraNivel2(1);
<%
    if(lsInter==true || lsBajo== true){
%> <% }%>
}//FUNCION

function VerificaFormato(checado){
  if(checado==1 || checado==3){
     formulario.formato[1].disabled = 'disabled';
     formulario.formato[0].checked = 'checked';
  }
  else{
    formulario.formato[1].disabled = false;
     formulario.formato[0].checked = 'checked';
  }
}

function activaTipoPolizas(){
  if(document.getElementById("tipoUsuario").value == 1)
      document.getElementById("polizas").style.display = "";
}

function desactivaTipoPolizas(){
      document.getElementById("polizas").style.display = "none";
}

function verificaCierre(){
  //activar = false;
  unidad = document.getElementById("unidad").value;
  document.getElementById("cpublica").style.display = "none";
  document.getElementById("polizas").style.display = "none";
  if(unidad != "100"){
    document.getElementById("precierre").style.display = "none";  
  }
}

function verificaCPublica(){
  mes = document.getElementById("lstMes").value;
  if(mes=="12"){
    document.getElementById("cpublica").style.display = "";
  }
  else{
    document.getElementById("cpublica").style.display = "none";
    document.getElementById("polizas").style.display = "none";
  }
}

function DespliegaCta(){
  alert("cuenta "+formulario.txtCtaNivel1.value);
}  

//FUNCION QUE SE UTILIZA AL MOMENTO DE SALIR DE LA CAJA DE TEXTO DE LA CUENTA CONTABLE
function muestraDescCuenta(xnivel){
  var i = 0;
  var niv = xnivel +1;

for (i == 0; i <= <%=totNivel-1%>; i++){
    if (xnivel == i){
      divsel[i].style.display ='';
      eval("formulario.lstNivel"+niv+".value = formulario.txtCtaNivel"+niv+".value");       
     }else{
        divsel[i].style.display ='none';
     }
  }      
}//funcion

var conti=true;
 
function relleCeros(nivel){
   conti=true;
   var ceros;
   var  usuarioAdmin = <%=controlRegistro.isUsuarioAdmin()%>;
   if (nivel ==2){  
     ceros=0;  
   }else{
     ceros = 4;
   }
   
   o="00000".substring(0,ceros-eval("formulario.txtCtaNivel"+nivel+".value.length"))+eval("formulario.txtCtaNivel"+nivel+".value");
   eval("formulario.txtCtaNivel"+nivel).value=o;
   eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
 <%       
    for (int l=1; l<=totNivel; l++){
%>
        if( parseInt(<%=(l)%>) == (nivel) ){//nivel recibido
            if(nivel== 4 && !usuarioAdmin){ //(nivel== 3)
                if(parseInt(eval("formulario.txtCtaNivel"+nivel).value,10) != <%=lsUniEje%>){
                    alert("Es un rango no permitido");
                    eval("formulario.txtCtaNivel"+nivel).value="";
                    conti=false;
                }else{
                    //muestra los botones
<%                  if(lsInter == true){        %>  
<%                  }                           %>
                }//else
            }//if de nivel 4
            
            if(nivel==5 && !usuarioAdmin){  
            
                if(parseInt(eval("formulario.txtCtaNivel"+nivel).value,10) != parseInt(<%=lsEntidad%><%=lsAmbito%>,10)){
                    alert("Es un rango no permitido");
                    eval("formulario.txtCtaNivel"+nivel).value="";
                    conti=false;
                }else{
<%                  if(lsBajo == true){        %>  
                        
<%                  }                           %>
                }//else
            }//if de nivel 5
                        
            if(conti==true){
            if(formulario.lstNivel<%=l%>.length >=1){//si la lista de descripcion tiene mas elementos
                if(formulario.lstNivel<%=l%>.value != ''){//si la lista de descripcion tiene un elemento seleccionado
                    return true;
                }else{//la lista de descripcion no tiene elemento seleccionado
                    if (ValidaRango()){//es valido el dato de la caja
                        formulario.hNivel.value=nivel;
                        formulario.txtDescripcion.value=''; 
                        formulario.txtDescripcion.focus();  
                    }else{//no es valido el dato de la caja
                      //  alert("El dato no se encuentra en el rango permitido");
<% 
%>                        
                   if(formulario.hOrden.value == ''){
                                formulario.hOrden.value= nivel;
                                alert("Este nivel no está permitido o configurado ");   
                        }else{
                      alert("El dato no se encuentra en el rango permitido");
                    }
<%
                        for (int k=1; k<=totNivel; k++){//se limpia el dato de la caja porque el valor no es valido                     
%>                        
                            if( parseInt(<%=(k)%>) == parseInt(formulario.hOrden.value)){//limpiar
                                formulario.txtCtaNivel<%=k%>.value="";    
                                formulario.txtCtaNivel<%=k%>.focus();    
                            }//if de limpiar
<%
                        }//for de limpiar
%>                
                    }//else de no es valido el dato de la caja
                    return false;                     
                    }//la lista de descripcion no tiene elementos seleccionados
            }else{//la lista de descripcion no tiene elementos seleccionados
                if (ValidaRango()){
                    formulario.hNivel.value=nivel;
                    formulario.txtDescripcion.value='';  
                    formulario.txtDescripcion.focus();    
                }else{
                  //  alert("El dato no se encuentra en el rango permitido");
<% 
%>                                          
                    if(formulario.hOrden.value == ''){
                        formulario.hOrden.value= nivel;
                        alert("Este nivel no está permitido o configurado "); 
                    }else{
                      alert("El dato no se encuentra en el rango permitido");
                    }                    
<%                        
                    for (int k=1; k<=totNivel; k++){%>
                        if( parseInt(<%=(k)%>) == parseInt(formulario.hOrden.value)){
                            formulario.txtCtaNivel<%=k%>.value="";    
                            formulario.txtCtaNivel<%=k%>.focus();    
                        }
<%
                    }//for
%>                
                }
                return false;
            }//la lista de descripcion no tiene elementos seleccionados
            }//if de conti            
        } //if de nivel recibido
<%
    }//for
%>
}//funcion

function muestraNivel2(nivelx){
    lsNivel=nivelx;
  var ceros = '00000'; 
<%
    for(int k=1; k<=totNivel; k++){
%>    
        if (formulario.lstNivel<%=k%>.selectedIndex == -1) {
            n<%=k%>="";
        }else{
            n<%=k%>=formulario.lstNivel<%=k%>[formulario.lstNivel<%=k%>.selectedIndex].value;
        }
<%
    }//for
%>
 <% 
 if (totNivel > 7){
%>   
        loadSource('capaNivel2',null,'../catalogoCuentas/c712CapaCuentas.jsp?nivRep='+nivelx+'&n1='+n1+'&n2='+n2+'&n3='+n3+'&n4='+n4+'&n5='+n5+'&n6='+n6+'&n7='+n7+'&n8='+n8+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&ejercicio='+<%=lsEjercicio%>+'&totN=<%=totNivel%>',bufferFrame);
        
<% }else{ %>   

       loadSource('capaNivel2',null,'../catalogoCuentas/c712CapaCuentas.jsp?nivRep='+nivelx+'&n1='+n1+'&n2='+n2+'&n3='+n3+'&n4='+n4+'&n5='+n5+'&n6='+n6+'&n7='+n7+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&ejercicio='+<%=lsEjercicio%>+'&totN=<%=totNivel%>',bufferFrame);
<% } %>
}
//funcion que valida el rango de valores
function ValidaRango(){
<% 
 for (int k=1; k<=totNivel; k++){
%>
       if( parseInt(<%=(k)%>) == parseInt(formulario.hOrden.value)){
            if(formulario.txtCtaNivel<%=k%>.length==1){
                formulario.txtCtaNivel<%=k%>.value='000'+formulario.txtCtaNivel<%=k%>;
            }else{
                if(formulario.txtCtaNivel<%=k%>.length==2){
                    formulario.txtCtaNivel<%=k%>.value='00'+formulario.txtCtaNivel<%=k%>;
                }else{
                    if(formulario.txtCtaNivel<%=k%>.length==3){
                        formulario.txtCtaNivel<%=k%>.value='0'+formulario.txtCtaNivel<%=k%>;
                    }//else
                }//else
            }//else
                
            caja=formulario.txtCtaNivel<%=k%>.value;    
        }
<%
    }//for
%>   
    var codigo=formulario.hCodigo.value;
    var tam = formulario.hTamanio.value;
    var tempo = "";
    var tempo2="";
    var conta=1;                                                                                                                                                                                                                                                                                                                                                                                                
    var ind=0;
    var arreglo=new Array();
    var ope="";
    for (x=0; x<=codigo.length; x++){
        if(x==parseInt(tam*conta,10)){
            if(conta==1){
                tempo=codigo.substr(tam*(conta-1),(tam*conta));
                ope=codigo.substr((tam*conta),(tam*(conta-1)+1));
                if(ope == '.'){
                    conta=conta+1;
                    tempo2=codigo.substr((tam*(conta-1)+1),(tam*conta));
                    conta=conta-1;
                    if(parseInt(caja,10) >= parseInt(tempo,10) && parseInt(caja,10) <= parseInt(tempo2,10) ){
                        arreglo[ind]=1;               
                    }else{
                        arreglo[ind]=0;               
                    }
                }else{
                    conta=conta+1;
                    tempo2=codigo.substr((tam*(conta-1)+1),(tam*conta));
                    conta=conta-1;
                    if(parseInt(caja,10) == parseInt(tempo,10) || parseInt(caja,10) == parseInt(tempo2,10) ){
                       arreglo[ind]=1;               
                    }else{
                        arreglo[ind]=0;               
                    }
                }
            }else{
                tempo=codigo.substr((parseInt(tam,10)*(parseInt(conta,10))+conta),parseInt(tam,10));
                ope=codigo.substr((parseInt(tam,10)*(parseInt(conta,10))+(conta-1)),parseInt(1) );
                if(ope == '.'){
                    conta=conta-1;
                    tempo2=codigo.substr((parseInt(tam,10)*(parseInt(conta,10))+conta),parseInt(tam,10));
    
                    conta=conta+1;
                    if(parseInt(caja,10) >= parseInt(tempo2,10) && parseInt(caja,10) <= parseInt(tempo,10)){
                        arreglo[ind]=1;               
                    }else{
                        arreglo[ind]=0;               
                    }
                }else{
                    if(parseInt(caja,10) == parseInt(tempo,10)){
                        arreglo[ind]=1;               
                    }else{
                        arreglo[ind]=0;               
                    }
                }
            }//else de conta +
            conta=conta+1;
            ind=ind+1;
        }//if
    }//for
    var band=false;
    
    for(y=0; y<arreglo.length; y++){
        if(arreglo[y]==1){
            band=true;    
        }
    }//for de y
    
    if(band== true)
        return true;
    else
        return false;
}

function Validar(){
    ok=true;
    mess="AVISO:\n";           
 
    if (formulario.txtDescripcion.value == ""){
        ok=false; mess=mess+'Capture la descripción de la cuenta\n'; 
    } 
    
    if (ok==true)
        return true;
    else{
        alert(mess)
        return false;
     }            
}

var tempoCta="";

function Fin2(){
    var tempolNiv=0;
    if(tempoCta != ""){
        eval("formulario.txtCtaNivel"+lsNivel).value=tempoCta;
        for (dd=0; dd < eval("formulario.lstNivel"+lsNivel).length; dd++){
            if (eval("formulario.lstNivel"+lsNivel)[dd].value == tempoCta){
                eval("formulario.lstNivel"+lsNivel)[dd].selected=true;
            }
        }
        tempolNiv=parseInt(lsNivel,10)+1;
        tempoCta="";
    } 
}
function Limpiar(){
    var niv=0;
<%
    for(int l=1; l<= totNivel; l++){
%>
        if(<%=l%>==parseInt(lsNivel)){       
           niv=formulario.lstNivel<%=l%>.length;
           if(niv==0){
               formulario.txtDescripcion.value='';  
           }
        }
<%  
    }//for
%>     
}
function Valida(){
    ok=true;
    mess="AVISO:\n";
    
   if (formulario.txtCtaNivel1.value == ''){
      ok=false; mess=mess+'Seleccione una cuenta\n'; 
     }            
     if (ok==true)
        return true;
     else{
        alert(mess)
        return false;
     }        
}
function Enviar(){
        if(Valida()){            
            formulario.submit();
        }
}

function recuperaNiveles(){
  cuenta = "";
  maximoNivel = document.getElementById("maximoNivel").value;
  nivel ="";
  for(k=1; k <= maximoNivel; k++){
    nivel = document.getElementById("txtCtaNivel"+k).value;
    
    if(nivel!="")
     nivelReporte = k;
    if(k == 2)
      nivel = nivel==""?"0":nivel;
    else
      nivel = nivel==""?"0000":nivel;
    cuenta += nivel;
  }
  document.getElementById("cuentaContable").value = cuenta;
}

function validarUsuario(){
  avanza = true;
  usuarioBajo = <%=controlRegistro.isUsuarioBajo()%>;
  usuarioIntermedio = <%=controlRegistro.isUsuarioIntermedio()%>;
  usuarioAdmin = <%=controlRegistro.isUsuarioAdmin()%>;
  if(usuarioAdmin)
    avanza = true;
  else{
    if(usuarioIntermedio && nivelReporte<4){
      alert('Al menos debe de seleccionar la unidad');
      avanza = false;
    }
    else{
      if(usuarioBajo && nivelReporte < 5){
        alert('Al menos debe seleccionar una unidad y ambito');
        avanza = false;
      }
    }
  }
  return avanza;
}

</script>
    
  </head>
  <body onLoad="verificaCierre();cargaPagina();VerificaFormato(1);">
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Por Cuenta Individual", "Filtro", true);</jsp:scriptlet>    
  <form name="formulario" method="post" action="c729ImprimirReporte.jsp" target="_blank"> 
   <input type="hidden" id="maximoNivel" value="<%=maximoNivel%>"></input>
  <table  width="100%">
    <tr><td class='negrita'>Informaci&oacute;n de la Cuenta: Unidad Ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%></td>
    <td><p class="AzulN"><%=lsMsg%></p></td></tr>
  </table>
  </br>

<table  id="tAgregaCta">
 <tr><td align="left" class='negrita'>Cuenta: </td>
<%  

for(int i=1; i <= totNivel; i++){
   if(i!=totNivel){
%>
  <td align="left"><div align="left" id="trlstNivel<%=i%>" > 
      <input type='text' id='txtCtaNivel<%=i%>' name='txtCtaNivel<%=i%>' size='10'  maxlength='10' class='cajaTexto' onFocus='muestraDescCuenta(<%=(i-1)%>);' 
      value=''  tabindex='<%=i%>' onChange= "nivel=1;if (relleCeros(<%=i%>)) muestraNivel2(<%=i+1%>);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(<%=i%>)) muestraNivel2(<%=(i+1)%>);}" 
      onkeyup="formulario.lstNivel<%=i%>.value = formulario.txtCtaNivel<%=i%>.value; " ></div>  
    </td>
    
<%  }
else{ 
%>    
 <td><div id="trlstNivel<%=i%>" >
           <input type='text' name='txtCtaNivel<%=i%>' size='10'  maxlength='10' class='cajaTexto' onFocus='muestraDescCuenta(<%=(i-1)%>);' 
           value='' tabindex='<%=i%>' onChange="nivel=2;if (relleCeros(<%=i%>)) muestraNivel2(<%=i+1%>);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(<%=i%>)) muestraNivel2(<%=(i+1)%>);}" 
           onkeyup="formulario.lstNivel<%=i%>.value = formulario.txtCtaNivel<%=i%>.value" >
            </div>
        </td>
    
<%    }%>
<%
}%></tr>
</table >

<table width="498" id="tNiveles">
  <tr>
    <td width="100">Descripci&oacute;n:</td>
    <td width="386"> 
<%
    for(int j=1; j<=totNivel; j++ ){
%>  
        <div id='divsel' align="left">
<%
            if(j!=totNivel){
%>
                <select  name='lstNivel<%=j%>' class='cajaTexto' onChange="javascript: muestraNivel2(<%=(j+1)%>); txtCtaNivel<%=j%>.value =lstNivel<%=j%>.value; ">     
<%
            }else{  //se pinta el ultimo nivel, ya no se llama a la funcion muestraNivel2
%>
                <select  name='lstNivel<%=j%>' class='cajaTexto' onChange="txtCtaNivel<%=j%>.value =lstNivel<%=j%>.value;  ">
<%
            }//else
            if(j==1){
%>         
                <option value="" selected></option></select>
<%
            }else{
%>
                </select>
<%          }
%>
        </div>
<%    
    }//for
%>
</td>
    </tr>
</table>
</br>
    <table  width="50%
    " align="center">
    <tr align="center"><td width="29%" align="right" class='negrita'>Mes:</td>
      <td width="50%" align="left"><select id="lstMes" name="lstMes"  class= 'cajaTexto' onchange="verificaCPublica()">
      <option value="01"> Enero </option>
      <option value="02"> Febrero </option>
      <option value="03"> Marzo </option>
      <option value="04"> Abril </option>
      <option value="05"> Mayo </option>
      <option value="06"> Junio </option>
      <option value="07"> Julio </option>
      <option value="08"> Agosto </option>
      <option value="09"> Septiembre </option>
      <option value="10"> Octubre </option>
      <option value="11"> Noviembre </option>
      <option value="12"> Diciembre </option>
      </select></td>
    <tr><td align="right" class="negrita">Título del programa:</td>
    <td><input id='txtTitulo' type='text' name='txtTitulo' class='cajaTexto' VALUE=' ' size="40"/></td></tr>
  </table>
<table width="100%">
</table><br><br>
<table width="30%" align="center">
 <tr class="azulCla"><td>Tipo de cierre</td></tr>
</table>
<table border="0" width="11%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="cierre" value="1" checked="checked" onclick="desactivaTipoPolizas()"/>Normal</td></tr>
    <tr id="precierre"><td><input type="radio" name="cierre" value="2" onclick="desactivaTipoPolizas()"/>Eliminación</td></tr>
    <tr id="cpublica"><td><input type="radio" name="cierre" value="3" onclick="activaTipoPolizas()"/>Cuenta Pública</td></tr>
  </table><br>
</br>
<div id="polizas">
<table width="30%" align="center">
 <tr class="azulCla"><td>Tipo de pólizas asociadas</td></tr>
</table>
<table border="0" width="11%" class='sianoborder' align="center">
    <tr><td><input type="radio" name="tipoPolizas" value="1" checked="checked"/>Central</td></tr>
    <tr><td><input type="radio" name="tipoPolizas" value="2" />Unidades</td></tr>
  </table></div>
</br>
<table cellspacing="5" width="30%" align="center">
 <tr align="center" class="azulCla">
 <td width="60%">Reporte</td>
 <td width="60%">Formato</td>
 </tr>
 <tr>
 <td width="20%" class='negrita'><input type="radio" name="btrReporte"  class="btr" value="1" checked onClick='VerificaFormato(1);' >Con Saldos </td>
 <td width="200%" class='negrita'><input type="radio" name="formato" class="btr" value="0" checked="checked">PDF</td>
 </tr>
 <tr>
 <td width="20%" class='negrita'><input type="radio" name="btrReporte"  class="btr" value="2" onClick='VerificaFormato(2);' >Estado de Cuenta </td>
 <% if (!Autentifica.getPerfilAcceso().equals("16")) { %> 
 <td width="20%" class='negrita'><input type="radio" name="formato" class="btr" value="1">Excel</td>
 <% } %>
 </tr>
 <tr>
 <td width="20%" class='negrita'><input type="radio" name="btrReporte"  class="btr" value="3" onClick='VerificaFormato(3);'>Validación de Captura </td>
 </tr> </table><br>
<table width="50%" align="center">
<tr><td><hr noshade="noshade" width="80%" align="center" style="border-width: 1px;border-style:solid"></td></tr>
</table>
<table width='50%' align="center">
<tr><td colspan="4"><div align="center"> <input type="button" name='btnAceptar' value='Aceptar' class="boton" onClick="javascript:recuperaNiveles();if(validarUsuario()){javascript:Enviar();}">
<input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c729FiltroCuenta.jsp','');">
 </div></td>
 </tr>
</table>

<!-- Capa MuestraNivel2 -->
<IFrame style="display:none" name = "bufferFrame"></IFrame>
<div id = "capaNivel2"></div>

<input type="hidden" name="hNivel" value=""></input>
<input type="hidden" name="hOrden" value=""></input>
<input type="hidden" name="hTamanio" value=""></input>
<input type="hidden" name="hCodigo" value=""></input>
<input type="hidden" name="cuentaContable" value=""></input>
<input type="hidden" id="unidad" name="unidad" value="<%=lsUniEje%>"></input>
<input type="hidden" id="tipoUsuario" name="tipoUsuario" value="<%=controlRegistro.getTipoUsuario()%>"></input>

  </form>
  </body>
</html>
<%
    }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
       sentencias = null;
       parametros = null;
     } //Fin finally
%>

  