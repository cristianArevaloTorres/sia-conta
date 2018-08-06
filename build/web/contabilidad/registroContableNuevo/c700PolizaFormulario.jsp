<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*, sia.db.sql.Vista, sia.db.sql.Sentencias"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="pbEstadoCat" class="sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<%
 ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro");   

  String lsUniEje=controlReg.getUnidad();
  String lsPais="147";   
  String lsEntidad=String.valueOf(controlReg.getEntidad());
  String lsAmbito=String.valueOf(controlReg.getAmbito());     
  String lsEjercicio=String.valueOf(controlReg.getEjercicio());    
  String lsCatCuenta=String.valueOf(controlReg.getIdCatalogoCuenta());      
  String lsBanCheque="";
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");    
%>

<jsp:useBean id="abOperaciones" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<%
    
  abOperaciones.setTableName("rf_tc_maestro_operaciones");
  String SQL = "select maestro_operacion_id, consecutivo, descripcion from rf_tc_maestro_operaciones where unidad_ejecutora='" + lsUniEje + "' and ambito='"+lsAmbito +
              "' and entidad=" + lsEntidad + " and pais=147 " + " and id_catalogo_cuenta=" + lsCatCuenta +  
              " and  extract(year from fecha_vig_ini) >="+ lsEjercicio+"order by to_number(consecutivo) ";
  abOperaciones.setCommand(SQL);
  Connection con=null;
  try{
  
     con = DaoFactory.getContabilidad();
     abOperaciones.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abOperaciones "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>
<jsp:useBean id="abChequeras" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<%
  abChequeras.setTableName("");
 SQL=" SELECT * FROM (SELECT 0 consecutivo,'CC' abreviatura,'01/01/2013' fecha_vig_ini, "+
"'01/01/2013' fecha_vig_fin,'000' as num_cuenta,'Sin Cuenta' desc_cuenta, "+
"'Sin Estatus' desc_estatus,'0' id_cuenta,'0' maestro_operacion_id,'0' cuenta_cheques_id, "+
"'99' consec_moper,'0'  ultimo_consecutivo,'0' estatus_cheque_id "+
"FROM dual ) ";
      
 //System.out.println(SQL);
  abChequeras.setCommand(SQL);
   con=null;
  try{
     con = DaoFactory.getContabilidad();
     abChequeras.execute(con);
   }
     catch(Exception e){
       System.out.println("Error al cargar abChequera "+SQL);
       System.out.println("Ocurrio un error al accesar el metodo abChequeras "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>




<jsp:useBean id="abOrigenDocto" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abOrigenDocto.setTableName("rf_tc_origen_docto");
      abOrigenDocto.setCommand("select 0 id_origen, 'Sin origen' descripcion, 1 activo, 0 pago_global " +
                               " from dual t");
     abOrigenDocto.execute(conexion);
      conexion.close();
      conexion=null;
   %>
</jsp:useBean> 

<jsp:useBean id="abOperacionCheque" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% String unidadesCxl = lsUniEje+",";
      Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
      List<Vista>registros = new ArrayList<Vista>();
      registros = sentencia.registros("vistas.select.unidadesCxl","numEmpleado="+sbAutentifica.getNumeroEmpleado()+"|");
      if(registros!=null){
        for(Vista registro:registros)
          unidadesCxl += registro.getField("UNIDADES_EJECUTORAS");
      }
      String  unidad[] = unidadesCxl.split(",");
      unidadesCxl = "";
      for(int i=0; i<unidad.length; i++){
        unidadesCxl += "'"+unidad[i]+"',";
      }
      unidadesCxl = unidadesCxl.substring(0,unidadesCxl.length()-1);
      //String condicionUnidad = lsUniEje.equals("100")?" and oc.unidad in('109',"+unidadesCxl+")":" and oc.unidad in ("+unidadesCxl+")";
      String condicionUnidad = " oc.unidad in ("+unidadesCxl+")";
      Connection conexion2=null;
      conexion2=DaoFactory.getContabilidad();
      abOperacionCheque.setTableName("rf_tr_operaciones_cheques");
      SQL =" select * from ( " +
                                   
                                    " select '"+lsEjercicio +"' ejercicio, 'Sin cuenta por liquidar' consecutivo, 'A' tipo_operacion, " +
                                     " 0 origen, 0 importe_neto, 1 vigente, 0 id_operacion, '"+lsUniEje+"' unidad" +
                                    " from dual) oc" +
                                    " where "+ condicionUnidad +
                                      " order by oc.origen ";
      //System.out.println(SQL);
      abOperacionCheque.setCommand(SQL);
      abOperacionCheque.execute(conexion2);
      conexion2.close();
      conexion2=null;
   %>
</jsp:useBean> 

<html>
<head>
<!--<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
-->
<title>Registro de Polizas</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>

<script type="text/javascript">
$(document).ready(
  function() {
   // alert($('#numero').val());
    $.mask.masks.msk= { mask : '99.999,999,999,999', type : 'reverse', defaultValue : '+000' };
    $('#txtImporte').setMask();
    $('#txtImporteDebe').setMask();
    $('#txtImporteHaber').setMask();
    $('#txtTotDebe').setMask();
    $('#txtTotDebebis').setMask();
    $('#txtTotHaber').setMask();
    }
);
</script>

<script language="JavaScript">
 var totCuentas = 0;
 var aPoliza = new Array();//aOrden,aNivel1,aNivel2,aNivel3,aNivel4,aNivel5,aNivel6,aNivel7,aDescrip,aTipoCta,aImporte,aStCuenta); 
function regresa(){
  var x;
  x=1;
}

function recalcula(){
  var tot1 =0;
  var tot2 =0;
  if (totCuentas > 1){
    for(i=0;i<totCuentas;i++){
      if (formulario.txtDebe[i].value != ""){
        tot1 = tot1 + parseFloat(formulario.txtDebe[i].value);
      }
      if (formulario.txtHaber[i].value != ""){
        tot2 = tot2 + parseFloat(formulario.txtHaber[i].value);
      }
     }
  }else {
     if (formulario.txtDebe.value != ""){
        tot1 = tot1 + parseFloat(formulario.txtDebe.value);
     }
     if (formulario.txtHaber.value != ""){
       tot2 = tot2 + parseFloat(formulario.txtHaber.value);
     }
   }
   formulario.txtTotDebe.value =redondear(tot1);;
   formulario.txtTotHaber.value =redondear(tot2);;
}

function cancelaAgrega(){
  trlstNivel2.style.display = 'none';
  trlstNivel3.style.display = 'none';
  trlstNivel4.style.display = 'none';
  trlstNivel5.style.display = 'none';
  trlstNivel6.style.display = 'none';
  trlstNivel7.style.display = 'none';
  trlstNivel8.style.display = 'none';
 <% 
if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
         
 %>  
   trlstNivel9.style.display = 'none';
 <%} %>
 
  formulario.txtImporte.value ="0";
  divImporte.style.display='none';
  acepta.style.display ='none';
  document.getElementById('lstNivel1').selectedIndex=0;
}

function eliminaCta(x){
  aPoliza[x][11] ="1";
  loadSource('capaDetalle',null,'c700CapaPolizaDetalle.jsp?totCtas='+totCuentas,bufferFrameDet);
}

function muestraCapaPlantilla(){
   loadSource('capaDetalle',null,'c700CapaPolizaDetalle.jsp?totCtas='+totCuentas,bufferFrameDet);
}

function compara(a, b) {
  if ( parseInt(a[0]) < parseInt(b[0]) ) return -1;
  if ( parseInt(a[0]) > parseInt(b[0]) ) return 1;
  return 0;
}

function ordenaMov(){
  aPoliza.sort(compara);
  loadSource('capaDetalle',null,'c700CapaPolizaDetalle.jsp?totCtas='+totCuentas,bufferFrameDet);
}

function muestraNivel2(nivelx){
  var ceros = '0000';
  if (document.getElementById('lstNivel1').selectedIndex == -1) {
     n1="";
  }else{
     n1=document.getElementById('lstNivel1')[document.getElementById('lstNivel1').selectedIndex].value;
  }
  if (document.getElementById('lstNivel2').selectedIndex == -1) {
     n2="";
  } else {
      n2="00000".substring(0,1-document.getElementById('lstNivel2')[document.getElementById('lstNivel2').selectedIndex].value.length)+document.getElementById('lstNivel2')[document.getElementById('lstNivel2').selectedIndex].value;
  }
  if (document.getElementById('lstNivel3').selectedIndex == -1) {
      n3="";
  } else {
      n3="00000".substring(0,4-document.getElementById('lstNivel3')[document.getElementById('lstNivel3').selectedIndex].value.length)+document.getElementById('lstNivel3')[document.getElementById('lstNivel3').selectedIndex].value;
  }
  if (document.getElementById('lstNivel4').selectedIndex == -1) {
      n4="";
  } else {
      n4="00000".substring(0,4-document.getElementById('lstNivel4')[document.getElementById('lstNivel4').selectedIndex].value.length)+document.getElementById('lstNivel4')[document.getElementById('lstNivel4').selectedIndex].value;
  }
   if (document.getElementById('lstNivel5').selectedIndex == -1) {
      n5="";
   } else {
      n5="00000".substring(0,4-document.getElementById('lstNivel5')[document.getElementById('lstNivel5').selectedIndex].value.length)+document.getElementById('lstNivel5')[document.getElementById('lstNivel5').selectedIndex].value;
   }
   if (document.getElementById('lstNivel6').selectedIndex == -1) {
      n6="";
   } else {
      n6="00000".substring(0,4-document.getElementById('lstNivel6')[document.getElementById('lstNivel6').selectedIndex].value.length)+document.getElementById('lstNivel6')[document.getElementById('lstNivel6').selectedIndex].value;
   }
   if (document.getElementById('lstNivel7').selectedIndex == -1) {
      n7="";
   } else {
      n7="00000".substring(0,4-document.getElementById('lstNivel7')[document.getElementById('lstNivel7').selectedIndex].value.length)+document.getElementById('lstNivel7')[document.getElementById('lstNivel7').selectedIndex].value;
   }
   if (document.getElementById('lstNivel8').selectedIndex == -1) {
      n8="";
   } else {
      n8="00000".substring(0,4-document.getElementById('lstNivel8')[document.getElementById('lstNivel8').selectedIndex].value.length)+document.getElementById('lstNivel8')[document.getElementById('lstNivel8').selectedIndex].value;
   }   
 <% 
   if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
 %>  
   if (document.getElementById('lstNivel9').selectedIndex == -1) {
      n9="";
   } else {
      n9="00000".substring(0,4-document.getElementById('lstNivel9')[document.getElementById('lstNivel9').selectedIndex].value.length)+document.getElementById('lstNivel9')[document.getElementById('lstNivel9').selectedIndex].value;
   }
  
   vNiv = new Array(n1,n2,n3,n4,n5,n6,n7,n8,n9);
<% }else{ %>
   vNiv = new Array(n1,n2,n3,n4,n5,n6,n7,n8);
<% } %>
   
<% 
 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
   
%>   
   loadSource('capaNivel2',null,'c700CapaPoliza9.jsp?nivRep='+nivelx+'&n1='+n1+'&n2='+n2+'&n3='+n3+'&n4='+n4+'&n5='+n5+'&n6='+n6+'&n7='+n7+'&n8='+n8+'&n9='+n9+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&ejercicio='+<%=lsEjercicio%>,bufferFrame);
<% }else{ %>   
   loadSource('capaNivel2',null,'c700CapaPoliza8.jsp?nivRep='+nivelx+'&n1='+n1+'&n2='+n2+'&n3='+n3+'&n4='+n4+'&n5='+n5+'&n6='+n6+'&n7='+n7+'&n8='+n8+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&ejercicio='+<%=lsEjercicio%>,bufferFrame);
<% } %>
 

}

function OcultaDescPlant()
{
  if (document.forms[0].chbGuardaPlant.checked){
    tdDescPlant.style.display='';
  }else{
    tdDescPlant.style.display='none';
  }
}

</script>

<script languaje="JavaScript">


function cargaPlantilla(nPlan){
  alert("carga plantilla "+nPlan);
  loadSource('capaCargaPlant',null,'c700CapaCargaPlant.jsp?idPlantilla='+nPlan,bufferFramePlant);
}

function muestraDescCuenta(xnivel){
  var i = 0;
  var niv = xnivel +1;
<% 
    if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>   
  for (i == 0; i <=8;i++){
<% }else{ %>
  for (i == 0; i <=7;i++){
<% } %>
    if (xnivel == i){
      divsel[i].style.display ='';
      document.getElementById('lstNivel'+niv).value = document.getElementById('txtCtaNivel'+niv).value;
     }else{
        divsel[i].style.display ='none';
     }
  }
}

function cargaPagina(){
  divImporte.style.display='none';
  divChequera.style.display='none';
  formulario.txtImporte.value ="0";
  acepta.style.display ='none';
  divDescPlant.style.display='none';
  trlstNivel2.style.display='none';
  trlstNivel3.style.display='none';
  trlstNivel4.style.display='none';
  trlstNivel5.style.display='none';
  trlstNivel6.style.display='none';
  trlstNivel7.style.display='none';
  trlstNivel8.style.display='none';
<% 
if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>   
    trlstNivel9.style.display='none';
<% } %>  
  divsel[0].style.display ='none';
  divsel[1].style.display ='none';
  divsel[2].style.display ='none';
  divsel[3].style.display ='none';
  divsel[4].style.display ='none';
  divsel[5].style.display ='none';
  divsel[6].style.display ='none';
  divsel[7].style.display ='none';
<% 
 if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>   
    divsel[8].style.display ='none';
<%} %>  
  var nr = 0;
  document.getElementById('lstNivel1').selectedIndex=0;
  
     muestraNivel2(1);
 }

function relleCeros(nivel){
  var cancela = 0;
  if (nivel ==0){
    cancela = 1; 
    nivel= 1;
  }
  var nivelInt;
  nivelInt = parseInt(nivel)+1;
   var ceros;
 
   if (nivel ==1){
     ceros=4; //cambio de 5 a 4
   }else 
   if (nivel==2){
     ceros=1;
   }else{
     ceros = 4;
   }
   o="00000".substring(0,ceros-document.getElementById("txtCtaNivel"+nivel).value.length)+document.getElementById("txtCtaNivel"+nivel).value;
   document.getElementById("txtCtaNivel"+nivel).value=o;
   document.getElementById("lstNivel"+nivel).value = document.getElementById("txtCtaNivel"+nivel).value;
   if (document.getElementById("lstNivel"+nivel).selectedIndex == -1){
     divImporte.style.display='none';
     formulario.txtImporte.value ="0";
     formulario.txtReferenciaTem.value="";
     acepta.style.display ='none';
     if (cancela == 0){
       alert ("clave "+o+" no valida");
     }
     document.getElementById("txtCtaNivel"+nivel).value= "";
     document.getElementById("txtCtaNivel"+nivel).focus();
     document.getElementById("lstNivel"+nivel).value = '';
<% 
 if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>   
     for (i=nivelInt;i<=9;i++){
<% }else{ %>
     for (i=nivelInt;i<=8;i++){
<% } %>
       document.getElementById("txtCtaNivel"+i).value='';
       eval("trlstNivel"+i+".style.display='none'");
       document.getElementById("lstNivel"+i).value = '';
     }
       return false;
   }else{
      document.getElementById("lstNivel"+nivel).value = document.getElementById("txtCtaNivel"+nivel).value;
<%  
 if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>         
      if (nivel == 9 ){
<% }else{ %>
      if (nivel == 8 ){
<% } %>     
        divImporte.style.display='';
        acepta.style.display ='';
        formulario.txtImporte.focus();
      }else{
        divImporte.style.display='none';
        formulario.txtImporte.value ="0";
        formulario.txtReferenciaTem.value="";
        acepta.style.display ='none';
      }
        return true;
   }
}

function xx(){
  document.getElementById('txtCtaNivel1').focus();
  }

function deleteComas(valor) {//elimina las comas de una cantidad 
  var numero= "" ;
  for(x=0; x< valor.length; x++) {
    if(valor.charAt(x)!= ",")
      numero= numero+ valor.charAt(x);
  } // for
  
  return numero; 
} // deleteComas 

function validaImporte(o){
  var tempo=o.value;
  o.value=deleteComas(o.value);
  if (!isFloat(o.value) ){
    alert("Importe incorrecto "+ o.value);
    o.value = "";
    return false;
  }else 
  if(o.value=='0.00' || o.value=='0'){
    alert("Importe no puede ser cero "+ o.value);
    return false;  
  }else{
     o.value=tempo;
     return true;
  }
}


function validaSalida(){
  var ok = true; 
  var dia;
  var mess;
  var anio;
  var lsFecha;
  var mensaje = "";
  
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  
  if (!(formulario.btrTipoPoliza[0].checked || formulario.btrTipoPoliza[1].checked  || formulario.btrTipoPoliza[2].checked || formulario.btrTipoPoliza[3].checked)){
    mensaje =mensaje + "- Seleccione el Tipo de Póliza. \n";
    ok = false;
  }
  

if (!isEmpty(formulario.txtFechaCrea.value)){
    lsFecha=formulario.txtFechaCrea.value;
    dia=lsFecha.substring(0,2);
    mess=lsFecha.substring(3);
    mes=mess.substring(0,2);
    anio=mess.substring(3);
    if (!isDate(dia,mes,anio,formulario.txtFechaCrea.value)){
      mensaje=mensaje + '- Fecha de afectación de la Póliza Incorrecto: '+"\n";
      mensaje=mensaje+formulario.txtFechaCrea.value+"  Debe capturar Fecha Válida dd/mm/aaaa \n";
      ok=false;
    }
  }else{
     mensaje = mensaje + "- Capture la Fecha de afectación de la Poliza. \n";
     ok=false;
  }


  if (isEmpty(formulario.txaConcepto.value)){
    mensaje = mensaje + "- Capture el Concepto de la Póliza. \n"
    ok=false;
  }else formulario.txaConcepto.value=validaTxa(formulario.txaConcepto.value,500);
  if (isEmpty(formulario.txaRefGral.value)){
    mensaje = mensaje + "- Capture la referencia general de la póliza. \n"
    ok=false;
  }else formulario.txaRefGral.value=validaTxa(formulario.txaRefGral.value,500)  
  if (!(formulario.txtTotDebe.value == formulario.txtTotHaber.value)){
    mensaje = mensaje + "- Los Totales Debe y Haber deben ser iguales y mayor a cero. \n"
    ok=false;
  }
  if ((formulario.lstOperaciones.value)==''){
    mensaje = mensaje + " No existe niguna operación contable dada de alta. \n"
    ok=false;
  }
  if (document.formulario.btrTipoPoliza[1].checked){
    if (document.formulario.txtFolio.value==''){
      mensaje = mensaje + "- No existe una cuenta bancaria para esta unidad ejecutora. \n"
      ok=false;
    }  
  }
  /*if (document.formulario.btrTipoPoliza[1].checked){
    if (document.formulario.txtBeneficiario.value==''){
      mensaje = mensaje + "- Capture el nombre del beneficiario. \n"
      ok=false;
    }  
  }*/
  
  if (lRenglones < 3){
    mensaje = mensaje + " Se deben agregar al menos 2 cuentas contables al último nivel. \n"
    ok=false;
  } 
  
  /*if (document.formulario.btrTipoPoliza[1].checked){
      document.formulario.txtBanCheque.value='1';
  }
  else{
       document.formulario.txtBanCheque.value='0';
  }*/
  
  document.formulario.txtBanCheque.value='0';
 
<%
%>
  if (ok){
    return ok
  }else{
    alert(mensaje);  
     return ok;
  }
}


function obtieneDescripcion(){
  ultDescrip =document.getElementById('lstNivel1')[document.getElementById('lstNivel1').selectedIndex].text
 
  if (trlstNivel2.style.display == '' && document.getElementById('lstNivel2').value != "")
    ultDescrip = document.getElementById('lstNivel2')[document.getElementById('lstNivel2').selectedIndex].text;
  if (trlstNivel3.style.display == '' && document.getElementById('lstNivel3').value != "")
     ultDescrip = document.getElementById('lstNivel3')[document.getElementById('lstNivel3').selectedIndex].text;
  if (trlstNivel4.style.display == '' && document.getElementById('lstNivel4').value != "")
     ultDescrip = document.getElementById('lstNivel4')[document.getElementById('lstNivel4').selectedIndex].text;
  if (trlstNivel5.style.display == '' && document.getElementById('lstNivel5').value != "")
     ultDescrip = document.getElementById('lstNivel5')[document.getElementById('lstNivel5').selectedIndex].text;
  if (trlstNivel6.style.display == '' && document.getElementById('lstNivel6').value != "")
     ultDescrip = document.getElementById('lstNivel6')[document.getElementById('lstNivel6').selectedIndex].text;
  if (trlstNivel7.style.display == '' && document.getElementById('lstNivel7').value != "")
     ultDescrip = document.getElementById('lstNivel7')[document.getElementById('lstNivel7').selectedIndex].text;   
  if (trlstNivel8.style.display == '' && document.getElementById('lstNivel8').value != "")
     ultDescrip = document.getElementById('lstNivel8')[document.getElementById('lstNivel8').selectedIndex].text;       
<% 
if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){  
%>              
  if (trlstNivel9.style.display == '' && document.getElementById('lstNivel9').value != "")
     ultDescrip = document.getElementById('lstNivel9')[document.getElementById('lstNivel9').selectedIndex].text;
<% } %>    

  return  ultDescrip;  
}


function AgregarCuenta2(){
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  var fila = Tabla.insertRow(lRenglones);
  var celda = fila.insertCell(0);
  
    
  var chb = document.createElement("INPUT");
  chb.type = "checkbox";
  chb.value = "" + lRenglones;
  chb.name = "chbEliminar";
  chb.className = "chb";
  chb.id = chb.name;  
  celda.className = "chb";      
  celda.appendChild(chb);
  celda = fila.insertCell(1);  

  
  var txtCuenta = document.createElement("INPUT");
  txtCuenta.type = "text";
  txtCuenta.value = document.getElementById('lstNivel1').value
  txtCuenta.name = "txtCuenta";
  txtCuenta.id = txtCuenta.name;
  txtCuenta.readOnly = true;
  txtCuenta.size = "4";
  txtCuenta.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtCuenta);  
  celda = fila.insertCell(2);
  
  var txtSubCuenta1 = document.createElement("INPUT");
  txtSubCuenta1.type = "text";
  txtSubCuenta1.value = document.getElementById('lstNivel2').value
  txtSubCuenta1.name = "txtSubCuenta1";
  txtSubCuenta1.id = txtSubCuenta1.name;
  txtSubCuenta1.readOnly = true;
  txtSubCuenta1.size = "3";
  txtSubCuenta1.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta1);  
  celda = fila.insertCell(3);  
  
  var txtSubCuenta2 = document.createElement("INPUT");
  txtSubCuenta2.type = "text";
  txtSubCuenta2.value = document.getElementById('lstNivel3').value
  txtSubCuenta2.name = "txtSubCuenta2";
  txtSubCuenta2.id = txtSubCuenta2.name;
  txtSubCuenta2.readOnly = true;
  txtSubCuenta2.size = "3";
  txtSubCuenta2.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta2);  
  celda = fila.insertCell(4);  
  
  var txtSubCuenta3 = document.createElement("INPUT");
  txtSubCuenta3.type = "text";
  txtSubCuenta3.value = document.getElementById('lstNivel4').value
  txtSubCuenta3.name = "txtSubCuenta3";
  txtSubCuenta3.id = txtSubCuenta3.name;
  txtSubCuenta3.readOnly = true;
  txtSubCuenta3.size = "3";
  txtSubCuenta3.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta3);  
  celda = fila.insertCell(5);  
  
  var txtSubCuenta4 = document.createElement("INPUT");
  txtSubCuenta4.type = "text";
  txtSubCuenta4.value = document.getElementById('lstNivel5').value
  txtSubCuenta4.name = "txtSubCuenta4";
  txtSubCuenta4.id = txtSubCuenta4.name;
  txtSubCuenta4.readOnly = true;
  txtSubCuenta4.size = "3";
  txtSubCuenta4.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta4);  
  celda = fila.insertCell(6);  
  
  var txtSubCuenta5 = document.createElement("INPUT");
  txtSubCuenta5.type = "text";
  txtSubCuenta5.value = document.getElementById('lstNivel6').value
  txtSubCuenta5.name = "txtSubCuenta5";
  txtSubCuenta5.id = txtSubCuenta5.name;
  txtSubCuenta5.readOnly = true;
  txtSubCuenta5.size = "3";
  txtSubCuenta5.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta5);  
  celda = fila.insertCell(7);  
  
  var txtSubCuenta6 = document.createElement("INPUT");
  txtSubCuenta6.type = "text";
  txtSubCuenta6.value = document.getElementById('lstNivel7').value
  txtSubCuenta6.name = "txtSubCuenta6";
  txtSubCuenta6.id = txtSubCuenta6.name;
  txtSubCuenta6.readOnly = true;
  txtSubCuenta6.size = "3";
  txtSubCuenta6.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta6);  
  celda = fila.insertCell(8);
  
  var txtSubCuenta7 = document.createElement("INPUT");
  txtSubCuenta7.type = "text";
  txtSubCuenta7.value = document.getElementById('lstNivel8').value
  txtSubCuenta7.name = "txtSubCuenta7";
  txtSubCuenta7.id = txtSubCuenta7.name;
  txtSubCuenta7.readOnly = true;
  txtSubCuenta7.size = "3";
  txtSubCuenta7.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta7);  
  celda = fila.insertCell(9);
  numCelda = 9;

<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){      
%>              
  var txtSubCuenta8 = document.createElement("INPUT");
  txtSubCuenta8.type = "text";
  txtSubCuenta8.value = document.getElementById('lstNivel9').value
  txtSubCuenta8.name = "txtSubCuenta8";
  txtSubCuenta8.id = txtSubCuenta8.name;
  txtSubCuenta8.readOnly = true;
  txtSubCuenta8.size = "3";
  txtSubCuenta8.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta8);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);

<%} %>  
  
  descripcion=obtieneDescripcion();
  var txtDesc = document.createElement("INPUT");
  txtDesc.type = "text";
  txtDesc.value = descripcion;
  txtDesc.name = "txtDesc";
  txtDesc.id = txtDesc.name;
  txtDesc.readOnly = true;
  txtDesc.size = "20";
  txtDesc.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtDesc);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  
  txtImporte1="";
  txtImporte2="";
  if (formulario.txtTipoCuenta.value=="1")
    txtImporte1=formulario.txtImporte.value;
  else
    txtImporte2=formulario.txtImporte.value;
  
  var txtImporteDebe = document.createElement("INPUT");
  txtImporteDebe.type = "text";
  txtImporteDebe.value =txtImporte1;
  txtImporteDebe.name = "txtImporteDebe";
  txtImporteDebe.id = txtImporteDebe.name;
  //txtImporteDebe.readOnly = false;
  txtImporteDebe.size = "10";
  txtImporteDebe.className = "cajaTextoDer"; 
  txtImporteDebe.attachEvent("onblur", actualizaTotales);
  txtImporteDebe.alt = "msk";
  celda.className = "txt";
  celda.appendChild(txtImporteDebe);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  $('input:text[id*=txtImporteDebe]').setMask();
  
  

  var txtImporteHaber = document.createElement("INPUT");
  txtImporteHaber.type = "text";
  txtImporteHaber.value =txtImporte2;
  txtImporteHaber.name = "txtImporteHaber";
  txtImporteHaber.id = txtImporteHaber.name;
  //txtImporteHaber.readOnly = false;  
  txtImporteHaber.size = "10"; 
  txtImporteHaber.className = "cajaTextoDer";  
  txtImporteHaber.attachEvent("onblur", actualizaTotales);
  txtImporteHaber.alt = "msk";
  celda.className = "txt";
  celda.appendChild(txtImporteHaber);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  $('input:text[id*=txtImporteHaber]').setMask();
  
  if (formulario.txtTipoCuenta.value=="1"){
    txtImporteDebe.readOnly = false;
    txtImporteHaber.readOnly = true;
  }
  else{
    txtImporteDebe.readOnly = true;
    txtImporteHaber.readOnly = false;
  }
  
  var txtReferencia = document.createElement("INPUT");
  txtReferencia.type = "text";
  txtReferencia.value = formulario.txtReferenciaTem.value;
  txtReferencia.name = "txtReferencia";
  txtReferencia.id = txtReferencia.name;
  txtReferencia.readOnly = false;
  txtReferencia.size = "15";
  txtReferencia.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtReferencia);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);

  var txtTipoOper = document.createElement("INPUT");
  txtTipoOper.type = "hidden";
  txtTipoOper.value = formulario.txtTipoCuenta.value;
  txtTipoOper.name = "txtTipoOper";
  txtTipoOper.id = txtTipoOper.name;
  txtTipoOper.readOnly = true;
  txtTipoOper.size = "3";
  txtTipoOper.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtTipoOper);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
}

function cargaDetalleOperacion(cuenta, subCuenta1, subCuenta2, subCuenta3, subCuenta4, subCuenta5, subCuenta6, subCuenta7, subCuenta8, descripcion, tipoCuenta, debe, haber, referencia,nivel){
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  var fila = Tabla.insertRow(lRenglones);
  var celda = fila.insertCell(0);
  var chb = document.createElement("INPUT");
  chb.type = "checkbox";
  chb.value = "" + lRenglones;
  chb.name = "chbEliminar";
  chb.className = "chb";
  chb.id = chb.name;  
  celda.className = "chb";      
  celda.appendChild(chb);
  celda = fila.insertCell(1);  

  
  var txtCuenta = document.createElement("INPUT");
  txtCuenta.type = "text";
  txtCuenta.value = cuenta;
  txtCuenta.name = "txtCuenta";
  txtCuenta.id = txtCuenta.name;
  txtCuenta.readOnly = true;
  txtCuenta.size = "4";
  txtCuenta.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtCuenta);  
  celda = fila.insertCell(2);
  
  var txtSubCuenta1 = document.createElement("INPUT");
  txtSubCuenta1.type = "text";
  txtSubCuenta1.value = subCuenta1;
  txtSubCuenta1.name = "txtSubCuenta1";
  txtSubCuenta1.id = txtSubCuenta1.name;
  txtSubCuenta1.readOnly = true;
  txtSubCuenta1.size = "3";
  txtSubCuenta1.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta1);  
  celda = fila.insertCell(3);  
  
  var txtSubCuenta2 = document.createElement("INPUT");
  txtSubCuenta2.type = "text";
  txtSubCuenta2.value = subCuenta2;
  txtSubCuenta2.name = "txtSubCuenta2";
  txtSubCuenta2.id = txtSubCuenta2.name;
  txtSubCuenta2.readOnly = true;
  txtSubCuenta2.size = "3";
  txtSubCuenta2.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta2);  
  celda = fila.insertCell(4);  
  
  var txtSubCuenta3 = document.createElement("INPUT");
  txtSubCuenta3.type = "text";
  txtSubCuenta3.value = subCuenta3;
  txtSubCuenta3.name = "txtSubCuenta3";
  txtSubCuenta3.id = txtSubCuenta3.name;
  txtSubCuenta3.readOnly = true;
  txtSubCuenta3.size = "3";
  txtSubCuenta3.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta3);  
  celda = fila.insertCell(5);  
  
  var txtSubCuenta4 = document.createElement("INPUT");
  txtSubCuenta4.type = "text";
  txtSubCuenta4.value = subCuenta4;
  txtSubCuenta4.name = "txtSubCuenta4";
  txtSubCuenta4.id = txtSubCuenta4.name;
  txtSubCuenta4.readOnly = true;
  txtSubCuenta4.size = "3";
  txtSubCuenta4.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta4);  
  celda = fila.insertCell(6);  
  
  var txtSubCuenta5 = document.createElement("INPUT");
  txtSubCuenta5.type = "text";
  txtSubCuenta5.value = subCuenta5;
  txtSubCuenta5.name = "txtSubCuenta5";
  txtSubCuenta5.id = txtSubCuenta5.name;
  if (6>nivel)
    txtSubCuenta5.readOnly = false;
  else  
    txtSubCuenta5.readOnly = true;
  txtSubCuenta5.size = "3";
  txtSubCuenta5.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta5);  
  celda = fila.insertCell(7);  
  
  var txtSubCuenta6 = document.createElement("INPUT");
  txtSubCuenta6.type = "text";
  txtSubCuenta6.value = subCuenta6;
  txtSubCuenta6.name = "txtSubCuenta6";
  txtSubCuenta6.id = txtSubCuenta6.name;
  if (7>nivel)
    txtSubCuenta6.readOnly = false;
  else  
    txtSubCuenta6.readOnly = true;
  txtSubCuenta6.size = "3";
  txtSubCuenta6.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta6);  
  celda = fila.insertCell(8);  
  
  var txtSubCuenta7 = document.createElement("INPUT");
  txtSubCuenta7.type = "text";
  txtSubCuenta7.value = subCuenta7;
  txtSubCuenta7.name = "txtSubCuenta7";
  txtSubCuenta7.id = txtSubCuenta7.name;
  if (8>nivel)
    txtSubCuenta7.readOnly = false;
  else  
    txtSubCuenta7.readOnly = true;
  txtSubCuenta7.size = "3";
  txtSubCuenta7.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta7);  
  celda = fila.insertCell(9);
  
  numCelda = 9;

<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){           
%>   
  var txtSubCuenta8 = document.createElement("INPUT");
  txtSubCuenta8.type = "text";
  txtSubCuenta8.value = subCuenta8;
  txtSubCuenta8.name = "txtSubCuenta8";
  txtSubCuenta8.id = txtSubCuenta8.name;
  if (9>nivel)
    txtSubCuenta8.readOnly = false;
  else  
    txtSubCuenta8.readOnly = true;  
  txtSubCuenta8.readOnly = true;
  txtSubCuenta8.size = "3";
  txtSubCuenta8.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta8);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  
<%} %> 
  
 
  var txtDesc = document.createElement("INPUT");
  txtDesc.type = "text";
  txtDesc.value = descripcion;
  txtDesc.name = "txtDesc";
  txtDesc.id = txtDesc.name;
  txtDesc.readOnly = false;
  txtDesc.size = "20";
  txtDesc.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtDesc);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  
  
  var txtImporteDebe = document.createElement("INPUT");
  txtImporteDebe.type = "text";
  txtImporteDebe.value =debe;
  txtImporteDebe.name = "txtImporteDebe";
  txtImporteDebe.id = txtImporteDebe.name;
  txtImporteDebe.size = "10";
  txtImporteDebe.className = "cajaTextoDer"; 
  txtImporteDebe.attachEvent("onblur", actualizaTotales);
  txtImporteDebe.alt = "msk";
  celda.className = "txt";
  celda.appendChild(txtImporteDebe);
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  $('input:text[id*=txtImporteDebe]').setMask();
  
  var txtImporteHaber = document.createElement("INPUT");
  txtImporteHaber.type = "text";
  txtImporteHaber.value =haber;
  txtImporteHaber.name = "txtImporteHaber";
  txtImporteHaber.id = txtImporteHaber.name;
  txtImporteHaber.size = "10";
  txtImporteHaber.className = "cajaTextoDer";  
  txtImporteHaber.attachEvent("onblur", actualizaTotales);
  txtImporteHaber.alt = "msk";
  celda.className = "txt";
  celda.appendChild(txtImporteHaber);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);
  $('input:text[id*=txtImporteHaber]').setMask();
  
  if (tipoCuenta=='0'){
    txtImporteDebe.readOnly = false;
    txtImporteHaber.readOnly = true;
    valor="1";
  }
  else{
    txtImporteDebe.readOnly = true;
    txtImporteHaber.readOnly = false;
    valor="2";
  }
  
  var txtReferencia = document.createElement("INPUT");
  txtReferencia.type = "text";
  txtReferencia.value = referencia;
  txtReferencia.name = "txtReferencia";
  txtReferencia.id = txtReferencia.name;
  txtReferencia.readOnly = false;
  txtReferencia.size = "15";
  txtReferencia.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtReferencia);  
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);

  var txtTipoOper = document.createElement("INPUT");
  txtTipoOper.type = "hidden";
  txtTipoOper.value = valor;
  txtTipoOper.name = "txtTipoOper";
  txtTipoOper.id = txtTipoOper.name;
  txtTipoOper.readOnly = true;
  txtTipoOper.size = "3";
  txtTipoOper.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtTipoOper); 
  numCelda=numCelda + 1;
  celda = fila.insertCell(numCelda);  
 
}


function Borra(i){
  var Tabla = document.getElementById("TablaPrueba");
  Tabla.deleteRow(i+1);
}

function Elimina(){
  var i=0,j=0;
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  
  
  
  if (lRenglones < 2){} 
  else{
    if (lRenglones == 2 ){ // en caso de que solo tenga 1 registro el table no lo toma el chbEliminar como un vector
        if(formulario.chbEliminar.checked){
           var Tabla = document.getElementById("TablaPrueba");
           Tabla.deleteRow(1);
            actualizaTotales();
        } 
    }
    else{ //en caso de que si se tenga mas de 1 registro
     i=0;
      while (i < formulario.txtCuenta.length){  
        if(formulario.chbEliminar[i].checked){
           Borra(i);
           actualizaTotales();           
           if (typeof(formulario.txtCuenta) == 'undefined')break;
           continue; 
        } else {i++;}
      }//while
    }//else de if (lRenglones = 2 ){}
   }//else
}

function formatoCeros(valor){
  var ceros='';
  var localiza=valor;
  var posicion=localiza.indexOf('.');
  var longitud=localiza.length;
 // alert('posiioc '+posicion);
  if (posicion!=-1){
    var decimales=localiza.substring(posicion+1,longitud);
    if (decimales.length==1){
      ceros='0';
    } 
  }
  else
    ceros='.00';
  return ceros;
}

function agregaTotales(){
var totDebe=0;
  if (formulario.txtTipoCuenta.value=='1'){ 
     tot=parseFloat(deleteComas(formulario.txtTotDebe.value))+parseFloat(deleteComas(formulario.txtImporte.value))
     formulario.txtTotDebe.value=redondear(tot);
     formulario.txtTotDebe.value=formulario.txtTotDebe.value+formatoCeros(formulario.txtTotDebe.value);  
     $('#txtTotDebe').setMask();

  }   
  else{   
     tot=parseFloat(deleteComas(formulario.txtTotHaber.value))+parseFloat(deleteComas(formulario.txtImporte.value))
     formulario.txtTotHaber.value=redondear(tot);
     formulario.txtTotHaber.value=formulario.txtTotHaber.value+formatoCeros(formulario.txtTotHaber.value);     
     $('#txtTotHaber').setMask();
  }   
}


function actualizaTotales(){
  var totDebe=0;
  var totHaber=0;
  var  i=0;
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  

  if (lRenglones < 2){} 
  else{
    if (lRenglones == 2 ){ // en caso de que solo tenga 1 registro el table no lo toma el chbEliminar como un vector

      if (formulario.txtImporteDebe.value!=""&&formulario.txtImporteDebe.value!=null){

         totDebe=totDebe+parseFloat(deleteComas(formulario.txtImporteDebe.value));
      }   
    if (formulario.txtImporteHaber.value!=""&&formulario.txtImporteHaber.value!=null){           

       totHaber=totHaber+parseFloat(deleteComas(formulario.txtImporteHaber.value));
    }   
    }  
    else{

      while (i < formulario.txtCuenta.length){  

         if (formulario.txtImporteDebe[i].value!=""&&formulario.txtImporteDebe[i].value!=null){
           totDebe=totDebe+parseFloat(deleteComas(formulario.txtImporteDebe[i].value));
         }  
         if (formulario.txtImporteHaber[i].value!=""&&formulario.txtImporteHaber[i].value!=null){
             totHaber=totHaber+parseFloat(deleteComas(formulario.txtImporteHaber[i].value));
         }  
         i++;
       }//while
    }
  }  
  formulario.txtTotDebe.value=redondear(totDebe);
  formulario.txtTotHaber.value=redondear(totHaber);
  
  formulario.txtTotDebe.value=formulario.txtTotDebe.value+formatoCeros(formulario.txtTotDebe.value);
  formulario.txtTotHaber.value=formulario.txtTotHaber.value+formatoCeros(formulario.txtTotHaber.value);
  
  
 /* var localiza=formulario.txtTotDebe.value;
  var posicion=localiza.indexOf('.');
  var longitud=localiza.length;
 // alert('posiioc '+posicion);
  if (posicion!=-1){
    var decimales=localiza.substring(posicion+1,longitud);
    if (decimales.length==1){
      formulario.txtTotDebe.value=formulario.txtTotDebe.value+'0';
    } 
 //   alert('debe '+formulario.txtTotDebe.value+' haber '+formulario.txtTotHaber.value+' posic '+posicion+  ' lon '+ longitud+ ' deci '+ decimales); 
  }
  else
    formulario.txtTotDebe.value=formulario.txtTotDebe.value+'.00';

  localiza=formulario.txtTotHaber.value;
  posicion=localiza.indexOf('.');
  longitud=localiza.length;
 // alert('posiioc '+posicion);
  if (posicion!=-1){
    decimales=localiza.substring(posicion+1,longitud);
    if (decimales.length==1){
      formulario.txtTotHaber.value=formulario.txtTotHaber.value+'0';
    } 
 //   alert('debe '+formulario.txtTotDebe.value+' haber '+formulario.txtTotHaber.value+' posic '+posicion+  ' lon '+ longitud+ ' deci '+ decimales); 
  }
  else
    formulario.txtTotHaber.value=formulario.txtTotHaber.value+'.00';
 */   
  
  $('#txtTotDebe').setMask();
  $('#txtTotHaber').setMask();
}

function capaOperacion(){
  var oper=formulario.lstOperaciones.value;
  var posicion=oper.indexOf('-')+1;
  oper=oper.substring(posicion,oper.length);
   // alert(oper);
  if (oper=='99'){
    formulario.btnAgregar.disabled=false;
    formulario.btnElimina.disabled=false;
  }    
  else{
    formulario.btnAgregar.disabled=true;
    formulario.btnElimina.disabled=true;
  }
  // var pregunta= confirm('Al cambiar de operacion contable se eliminaran todos los renglones del detalle, ¿Continuar?');
  // if (pregunta == true){
    var Tabla = document.getElementById("TablaPrueba");
    var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
    // alert(lRenglones);
    if (lRenglones>1){ 
        // formulario.chbEliminar[1].checked=true
        // formulario.chbEliminar[2].checked=true
        i=0;
        //alert(formulario.txtCuenta.length);
        while (i < formulario.chbEliminar.length){  
          // alert('i'+i);
          // alert(formulario.chbEliminar.length);
          formulario.chbEliminar[i].checked=true;
          i++;
        }   
        Elimina(); 
    }
    var fechaRe=formulario.txtFechaCrea.value;
    loadSource('capaOperacion',null,'c700CapaOperacion.jsp?ejercicio='+<%=lsEjercicio%>+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&fecha='+fechaRe+'&operacionTipo='+formulario.lstOperaciones.value,bufferFrame9);  
  //}  
}


function capaFecha(){
//alert('en capa');
var fechaR=formulario.txtFechaCrea.value;
var programa=formulario.txtSubCuenta2[0].value;
loadSource('capaFecha',null,'c700CapaFecha.jsp?ejercicio='+<%=lsEjercicio%>+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&fecha='+fechaR+'&programa='+programa,bufferFrame8);
}

function rellena(cadena, longitud){
var cadenaTem=cadena;
var lonRellena=longitud-cadena.length;
var x=1;
  while (x<=lonRellena){
    cadenaTem=cadenaTem+'0';
    x=x+1;
  }
return cadenaTem;
}

function capaCuenta(){
//alert('en capa');
var i=0;
var Tabla = document.getElementById("TablaPrueba");
var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
var cuentaCon='';
var cuentaConTem='';
i=0;
  while (i < formulario.txtCuenta.length){  
<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>              
    cuentaConTem=formulario.txtCuenta[i].value+formulario.txtSubCuenta1[i].value+formulario.txtSubCuenta2[i].value+formulario.txtSubCuenta3[i].value+formulario.txtSubCuenta4[i].value+formulario.txtSubCuenta5[i].value+formulario.txtSubCuenta6[i].value+formulario.txtSubCuenta7[i].value+formulario.txtSubCuenta8[i].value; 
    cuentaConTem=rellena(cuentaConTem,33);
<%} else {%>
    cuentaConTem=formulario.txtCuenta[i].value+formulario.txtSubCuenta1[i].value+formulario.txtSubCuenta2[i].value+formulario.txtSubCuenta3[i].value+formulario.txtSubCuenta4[i].value+formulario.txtSubCuenta5[i].value+formulario.txtSubCuenta6[i].value+formulario.txtSubCuenta7[i].value; 
    cuentaConTem=rellena(cuentaConTem,29);
<% }%>
   // if (i<30)
      cuentaCon=cuentaCon+"'"+cuentaConTem+"',";
    i++;
    // if (typeof(formulario.txtCuenta) == 'undefined')break;
   // continue; 
  }//while
// alert('cuentaCon '+cuentaCon)
loadSource('capaCuenta',null,'c700CapaCuenta.jsp?ejercicio='+<%=lsEjercicio%>+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&cuentas='+cuentaCon,bufferFrame10);
}


function validaFechaCapa(){
ban=true;
//alert('en ');
//alert(formulario.txtValFecha.value);
  if (formulario.txtValFecha.value=='2'){
    alert('La fecha especificada para la poliza no puede ser mayor al dia de hoy. ');
    ban = false; 
  }  
  else
  if (formulario.txtValFecha.value=='3'){
    alert('El año especificado para la poliza no es igual al ejercicio seleccionado. ');
    ban = false; 
  }  
  else
  if (formulario.txtValFecha.value=='4'){
    alert('El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza');
    ban=false; 
  }  
  else
  if (formulario.txtValFecha.value=='5'){
    alert('El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza ');
    ban=false; 
  }  
  
  if (ban){ 
    formulario.btnAceptar.disabled=true;
    formulario.txtFolio.disabled=false;
    formulario.submit();
  }  
}

function validaCuenta(){
//alert('en ');
//alert(formulario.txtValCuenta.value);

  if (formulario.txtValCuenta.value=='0'){
    //  alert('Todas las cuentas existen. ');
    // capaFecha();
  }  
  else{
    alert('Las siguientes cuentas contables no existen en el catalogo de cuentas:  ' + formulario.txtValCuenta.value);
  }  
}

function muestraDivChequera(){
  /*if (document.formulario.btrTipoPoliza[1].checked){
    divChequera.style.display='';
    if (document.formulario.txtFolio.value=='')
      alert('No existe una cuenta bancaria para esta unidad ejecutora');
  }  
  else
    divChequera.style.display='none';*/
}


function cambiaConsecutivo(cadena, descripcion){
 var arreglo=cadena.split('-');
 var conse=arreglo[1];
 formulario.txtFolio.value=conse;
 formulario.txaConcepto.value=descripcion;
}
</script>

</head>
<body onLoad=cargaPagina();>

<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Agregar pólizas", "Agregar", true);</jsp:scriptlet>    

<FORM Method="post" action="c700Control.jsp" name="formulario" >

<%

 
 
 Connection conexion=null;
 sbAutentifica.setPagina("c700PolizaFormulario.jsp");
 String estatus="0";
 String descripcion="";
 String error="0";
 
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(true);
      
   // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
   pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
   estatus=pbEstadoCat.getEstatus();
   descripcion=pbEstadoCat.getDescripcion();
   if (!estatus.equals("1")){
     error="C";
     throw new Exception(" En estos momentos no es posible crear, modificar o cancelar polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
   }
     
%>

  <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%></H2>
<table>
  <tr>
     <td height="26">Tipo de P&oacute;liza:</td>
     <td>
        <p>
          <input type="radio" name="btrTipoPoliza" value=1 onClick='muestraDivChequera();' checked>Diario
         <!-- <input type="radio" name="btrTipoPoliza" value=3 onClick='muestraDivChequera();' >Cheques (Generando poliza de Egreso)--->
          <input type="radio" name="btrTipoPoliza" value=3 onClick='muestraDivChequera();' >Egreso
          <input type="radio" name="btrTipoPoliza" value=4 onClick='muestraDivChequera();' >Ingreso
        </p><!-- termina DynAPI -->

     </td>
  </tr>

  <tr>
    <td>Fecha de Afectaci&oacute;n:</td>
    <td><input type='text' name='txtFechaCrea' size='10' maxlength='10' class='cajaTexto' VALUE='' onChange=''><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;"  onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCrea');"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a></td>
  </tr>
</table>

<div id="divChequera">
  <table>
    <tr>
      <td>Cuentas de Cheques:</td>
      <td><select name='lstCuentas'  tabindex='9' class= 'cajaTexto' onChange='cambiaConsecutivo(document.formulario.lstCuentas[selectedIndex].value,document.formulario.lstCuentas.options[document.formulario.lstCuentas.selectedIndex].text);'>
<%
     abChequeras.beforeFirst();
     int contador=1;
     String valor="";
     while (abChequeras.next()) { 
       if (contador==1)  
         valor=abChequeras.getString("ultimo_consecutivo");
       contador++;  
%>        
           <option value=<%=abChequeras.getString("cuenta_cheques_id")%>-<%=abChequeras.getString("ultimo_consecutivo")%>-<%=abChequeras.getString("abreviatura")%>><%=abChequeras.getString("num_cuenta")%> - <%=abChequeras.getString("desc_cuenta")%></option>
<%   } %>
        </select></td>
    </tr>
    <tr>
       <td>Folio: </td>   
       <td><input type='text' name='txtFolio' size='10'  maxlength='10' class='cajaTexto' VALUE='<%=valor%>' disabled></td>
    </tr>
    <tr>
       <td>Beneficiario: </td>   
       <td><input type='text' name='txtBeneficiario' size='50'  maxlength='50' class='cajaTexto' VALUE='' ></td>
    </tr>
<tr><td>Origen:</td>
  <td><SELECT id='origenDocto' class='cajaTexto' NAME='lstOrigenDocto' onchange="ComponerListaOperacionCheque(document.formulario.lstOrigenDocto[selectedIndex].value);formulario.lstOperacionCheque.options[1].selected=true;">

         <%
          int cuenta;
          String cat;
          try{
            abOrigenDocto.beforeFirst(); 
            while (abOrigenDocto.next()){
          %>
            <OPTION VALUE="<%=abOrigenDocto.getString("id_origen")%>"><%=abOrigenDocto.getString("descripcion")%></OPTION>
          <%
           } //Fin while
         }catch (Exception e) {
           System.out.println("Error en lista de cierre: "+ e.getMessage());
         }         
         %>
      </SELECT></td></tr>  
<tr>
<td >CXL:</td>
  <td>
     <SELECT id="lstOperacionCheque" class="cajaTexto" NAME="lstOperacionCheque">
     </SELECT>

<script language="JavaScript">
// Comienza rutina para listas dinamicas
function Tupla ( campo1, campo2 )
{
        this.campo1 = campo1;
        this.campo2 = campo2;
}
//var opciones0 = new Array();
//opciones0[0]= new Tupla("Sin cuenta por liquidar","Sin cuenta por liquidar");

<%
  // Este cÓdigo genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
  cuenta=1;
  cat="null";
  try{
  abOperacionCheque.beforeFirst();
  while (abOperacionCheque.next()){
    if (!cat.equals(abOperacionCheque.getString("origen")))
    {
      cuenta=1;
      cat=abOperacionCheque.getString("origen");
%>
      var opciones<%=cat%> = new Array();
      //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
<%
     } //fin if
%>
  opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=abOperacionCheque.getString("consecutivo")%>","<%=abOperacionCheque.getString("consecutivo")%>");
<%
  cuenta=cuenta+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en programa: "+ e.getMessage());
  }         
         
%>
var contador;

function ComponerListaOperacionCheque ( array ) {
// Compone la lista dependiente a partir
// del valor de la opcion escogida en la lista "padre"
//alert("pasa "+ array);
BorrarListaOperacionCheque();
  array = eval("opciones" + array);

  for (contador=1; contador<array.length; contador++)
  {
    //Añade elementos a nuestro combobox dependiente
    var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
    formulario.lstOperacionCheque.options[contador] = optionObj;
  } // for
  formulario.lstOperacionCheque.options[1].selected=true;
} // ComponerLista

 
//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaOperacionCheque() {
    formulario.lstOperacionCheque.length=0;
}

//Inicializamos enviando la clave del proceso padre para este caso
ComponerListaOperacionCheque ("0");
</script>
</td></tr>
  </table>
  <br>
</div>




<!-- Capa Fecha -->
 <IFrame style="display:none" name = "bufferFrame8">
 </IFrame>
 <div id = "capaFecha">
</div>

<!-- Capa Operacion -->
 <IFrame style="display:none" name = "bufferFrame9">
 </IFrame>
 <div id = "capaOperacion">
</div>

<!-- Capa Cuenta -->
 <IFrame style="display:none" name = "bufferFrame10">
 </IFrame>
 <div id = "capaCuenta">
</div>


<br><br>

<hr noshade size='3'>
<!-------------------->
<H3>Detalle de la P&oacute;liza</H3>
<a href="#marca1" id="refmarc" name="nrefmarc"> </a> 

<table id="tAgregaCta">
  <tr>
    <td width="100">Agregar Cuenta: </td>
    <td><div id="trlstNivel1">
           <input type='text' name='txtCtaNivel1' id='txtCtaNivel1' size='10'  maxlength='10' class='cajaTexto' onFocus ='muestraDescCuenta(0);'value=''
            tabindex=1 onChange="if (relleCeros(1)) muestraNivel2(2);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(1)) muestraNivel2(2);} " onkeyup="document.getElementById('lstNivel1').value = document.getElementById('txtCtaNivel1').value">
        </div>
    </td>
    <td><div id="trlstNivel2">
           <input type='text' name='txtCtaNivel2' id='txtCtaNivel2' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(1);"
           tabindex=2 onChange="if (relleCeros(2)) muestraNivel2(3);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(2)) muestraNivel2(3);} " onkeyup="document.getElementById('lstNivel2').value = document.getElementById('txtCtaNivel2').value">
        </div>
    </td>
    <td><div id="trlstNivel3">
           <input type='text' name='txtCtaNivel3' id='txtCtaNivel3' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(2);"
            tabindex=3 onChange="if (relleCeros(3)) muestraNivel2(4);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(3)) muestraNivel2(4);} " onkeyup=" document.getElementById('lstNivel3').value = document.getElementById('txtCtaNivel3').value">
        </div>
    </td>
    <td><div id="trlstNivel4">
           <input type='text' name='txtCtaNivel4' id='txtCtaNivel4' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(3);"
            tabindex=4 onChange="if (relleCeros(4))  muestraNivel2(5);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(4)) muestraNivel2(5);} " onkeyup="document.getElementById('lstNivel4').value = document.getElementById('txtCtaNivel4').value">
          </div>
    </td>
    <td><div id="trlstNivel5">
           <input type='text' name='txtCtaNivel5' id='txtCtaNivel5' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(4);"
            tabindex=5 onChange="if (relleCeros(5))  muestraNivel2(6);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(5)) muestraNivel2(6);} " onkeyup="document.getElementById('lstNivel5').value = document.getElementById('txtCtaNivel5').value">
        </div>
    </td>
    <td><div id="trlstNivel6">
           <input type='text' name='txtCtaNivel6' id='txtCtaNivel6' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(5);"
            tabindex=6 onChange="if (relleCeros(6))  muestraNivel2(7);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(6)) muestraNivel2(7);} " onkeyup="document.getElementById('lstNivel6').value = document.getElementById('txtCtaNivel6').value">
        </div>
    </td>
    <td><div id="trlstNivel7">
           <input type='text' name='txtCtaNivel7' id='txtCtaNivel7' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(6);"
            tabindex=7 onChange="if (relleCeros(7))  muestraNivel2(8);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(7)) muestraNivel2(8);} " onkeyup="document.getElementById('lstNivel7').value = document.getElementById('txtCtaNivel7').value">
        </div>
    </td>    
<% 
 if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){          
 
%>              
    <td><div id="trlstNivel8">
           <input type='text' name='txtCtaNivel8' id='txtCtaNivel8' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(7);"
            tabindex=8 onChange="if (relleCeros(8))  muestraNivel2(9);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(8)) muestraNivel2(9);} " onkeyup="document.getElementById('lstNivel8').value = document.getElementById('txtCtaNivel8').value">
        </div>
    </td>
 <%} else {%>
     <td><div id="trlstNivel8">
           <input type='text' name='txtCtaNivel8' id='txtCtaNivel8' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(7);"
            tabindex=8 onChange="relleCeros(8);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; relleCeros(8);} " onkeyup="document.getElementById('lstNivel8').value = document.getElementById('txtCtaNivel8').value">
        </div>
    </td>
 <%}%>
<% 
   if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>              
    
    <td><div id="trlstNivel9">
           <input type='text' name='txtCtaNivel9' id='txtCtaNivel9' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(8);"
            tabindex=9 onChange="relleCeros(9);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; relleCeros(9);} " onkeyup="document.getElementById('lstNivel9').value = document.getElementById('txtCtaNivel9').value">
        </div>
    </td>    
<% } %>    
  </tr>
</table >

<table width="498" id="tNiveles">
  <tr>
    <td width="100">Descripci&oacute;n:</td>
    <td width="386">
      <div id ='divsel'>
         <select  name='lstNivel1'  id='lstNivel1' class= 'cajaTexto' onChange="muestraNivel2(2); txtCtaNivel1.value =lstNivel1.value; ">
         <option value="" selected></option>
         </select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel2' id='lstNivel2' class= 'cajaTexto' onChange='javascript: muestraNivel2(3); txtCtaNivel2.value =lstNivel2.value; '></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel3' id='lstNivel3' class= 'cajaTexto' onChange='javascript: muestraNivel2(4); txtCtaNivel3.value =lstNivel3.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel4' id='lstNivel4' class= 'cajaTexto' onChange='javascript: muestraNivel2(5); txtCtaNivel4.value =lstNivel4.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel5' id='lstNivel5' class= 'cajaTexto'  onChange='javascript: muestraNivel2(6); txtCtaNivel5.value =lstNivel5.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel6' id='lstNivel6' class= 'cajaTexto'  onChange='javascript: muestraNivel2(7); txtCtaNivel6.value =lstNivel6.value' ></select>
      </div>      
      <div id = 'divsel'>
          <select name='lstNivel7' id='lstNivel7' class= 'cajaTexto'  onChange='javascript: muestraNivel2(8); txtCtaNivel7.value =lstNivel7.value' ></select>
      </div>            
<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){     
%>                    
      <div id = 'divsel'>
          <select name='lstNivel8' id='lstNivel8' class= 'cajaTexto'  onChange='javascript: muestraNivel2(9); txtCtaNivel8.value =lstNivel8.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel9' id='lstNivel9' class= 'cajaTexto' onChange='javascript: txtCtaNivel9.value =lstNivel9.value;  divImporte.style.display="";  acepta.style.display =""; formulario.txtImporte.focus();'></select>
      </div>
<% }else{%>
      <div id = 'divsel'>
          <select name='lstNivel8' id='lstNivel8' class= 'cajaTexto' onChange='javascript: txtCtaNivel8.value =lstNivel8.value;  divImporte.style.display="";  acepta.style.display =""; formulario.txtImporte.focus();'></select>
      </div>
<% }%>
    </td> 
  </tr>
</table>


<div id="divImporte">
  <table>
    <tr>
      <td>Importe:</td>
      <td><input id='txtImporte' type='text' name='txtImporte' alt='msk' size='10'  maxlength='10' class='cajaTexto' VALUE='' tabindex=8 onKeyDown=" if(event.keyCode==13) event.keyCode=9;" style="text-align: right; float:right"></td>
      <td>Tipo de Importe:</td>
      <td><select name='txtTipoCuenta'  tabindex='9' class= 'cajaTexto' onKeyDown= "if(event.keyCode==13) event.keyCode=9;">
             <option value='1' >Debe</option>
             <option value='2' >Haber</option>
          </select></td>
       <td>Referencia:</td>   
       <td><input type='text' name='txtReferenciaTem' size='30'  maxlength='50' class='cajaTexto' VALUE='' ></td>
    </tr>
  </table>
  <br>
</div>

<table width="36%">
  <tr>
     <td  id ="acepta" width="37%"><INPUT type='button' name='btnAgregar' VALUE="Agregar" class='boton' tabindex=10
             onClick ='if (validaImporte(formulario.txtImporte)){AgregarCuenta2(); nrefmarc.click();agregaTotales();} else {formulario.txtImporte.focus();}'>
     </td>
     <td width="63%"> <INPUT type='button' name='btnCancelar' onblur="document.getElementById('txtCtaNivel1').focus();" onClick="document.getElementById('txtCtaNivel1').value ='';relleCeros(0);" VALUE="Cancelar" class='boton' tabindex=11></td>
  </tr>
</table>

<table width="822">
    <tr>
      <td width="517" align="center"> </td>
      <td width="293" valign="middle"><strong>Operaciones Contables</strong>
        <select name='lstOperaciones' class= 'cajaTexto' onChange='capaOperacion();' >
<%
     abOperaciones.beforeFirst();
     while (abOperaciones.next()) {        
        if (abOperaciones.getString("consecutivo").equals("99")){
%>        
            <option selected value='<%=abOperaciones.getString("maestro_operacion_id")%>-<%=abOperaciones.getString("consecutivo")%>'><%=abOperaciones.getString("consecutivo")%>-<%=abOperaciones.getString("descripcion")%></option>
<%      }else{ %>            
           <option value='<%=abOperaciones.getString("maestro_operacion_id")%>-<%=abOperaciones.getString("consecutivo")%>'><%=abOperaciones.getString("consecutivo")%>-<%=abOperaciones.getString("descripcion")%></option>           
<%      }
     }%>   
      </select>
      </td>
    </tr>
</table>

<IFrame style="display:none" name = "bufferFrameDet"></IFrame>
<div id = "capaDetalle"></div>
<br><br>

<span id='detalle'>
  
<div id=Layer1  
  style="Z-INDEX: 3; LEFT: 15px; VISIBILITY: visible; OVERFLOW: auto; WIDTH: 750px;  TOP: 1004px; HEIGHT: 250px; background-color: #74A7A7;  border: 2px #336666;"> 
  
  <table id='TablaPrueba' border=1 class=general>
    <thead class='tabla01'>
     <tr>
        <td class=general width="4%"><strong>Seleccionar</strong></td>
        <td class=general width="4%"><strong>Cuenta</strong></td>
        <td class=general width="4%" align="center"><strong>SUB</strong></td>
        <td class=general width="4%" align="center" ><strong>S-Sub<br></strong></td>
        <td class=general width="4%" align="center" ><strong>S-S-Sub<br></strong></td>
        <td class=general width="4%" align="center" ><strong>S-S-S-Sub<br></strong></td>
        <td class=general width="4%" align="center" ><strong>S-S-S-S-Sub<br></strong></td>
        <td class=general width="4%" align="center" ><strong>S-S-S-S-S-Sub<br></strong></td>
        <td class=general width="4%" align="center" ><strong>S-S-S-S-S-S-Sub<br></strong></td>
<% 
    if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){      
%>              
        <td class=general width="4%" align="center" ><strong>S-S-S-S-S-S-S-Sub<br></strong></td>
<%} %>        
        <td class=general width="20%" align="center" ><strong>Descripci&oacute;n</strong></td>
        <td class=general width="10%" align="center"><strong>Debe</strong></td>
        <td class=general width="10%" align="center"><strong>Haber</strong></td>
        <td class=general width="10%" align="center"><strong>Referencia</strong></td>
     </tr>
    </thead>
  </table>
</div>
</span>
<br>

<table width="750" class=general>
  <tr id="trtotcta">
     <td><input type='button'  class='boton' name='btnElimina' value='Eliminar' onClick='Elimina();'></td>    
    <td class=general align="rigth" width="78%" style="border-left-style: none; border-left-width: medium;">
     	<p align="right">Totales</td>
    <td width="10%" align="right" class=general>
       <input id="txtTotDebe" name="txtTotDebe"  alt='msk'  value="0.00"  class="cajaTexto" size="15" readonly style="text-align: right; float:right" ></td>
    <td width="10%" align="right" class=general>
      	<input id="txtTotHaber" name="txtTotHaber" alt='msk'  value="0.00"  class="cajaTexto" size="15" readonly style="text-align: right; float:right"></td>
  </tr>
</table>

<br><br>
<hr noshade size='3'>
<br><br>

<!-- Capa cargaPlantilla -->
<IFrame style="display:none" name = "bufferFramePlant"></IFrame>
<div id = "capaCargaPlant"></div>

<!-- Capa MuestraNivel2 -->
<IFrame style="display:none" name = "bufferFrame"></IFrame>
<div id = "capaNivel2"></div>

<table>
  <tr>
    <td align="left">Concepto:</td>
    <td><textarea name='txaConcepto' rows='3' cols='80' class='cajaTexto' maxlength=20 ></textarea></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr>
    <td>Referencia General:</td>
    <td><textarea name='txaRefGral' rows='3' cols='80' class='cajaTexto' maxlength=20 ></textarea></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>    
  </tr>  
  <tr>
      <td  width="48%" valign="middle"><div id= "divDescPlant">
        Descripcion: <textarea  name="txaDescPlant" rows="2" cols="50" class='cajaTexto'></textarea></div></td>
  </tr>
</table>

<%
  }catch(Exception E){
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());
%>
     <p><%=E.getMessage()%></p>
<%
    if (!error.equals("C")){
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   error="1";
   }
   finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }             
    }
%>

<table width="100%">
  <tr>
<%
   if (error.equals("0")){
%>   
    <td  width="48%"></td>
    <td width="18%" align="right">
       <INPUT type='button' name='btnAceptar' value="Aceptar" class='boton' onclick="if (validaSalida()){ capaFecha(); }"></td>
<% } %>      
    <td width="14%" align="right">
        <INPUT type='button' name='btnCancelar' VALUE="Regresar" class='boton' onClick="javascript:LlamaPagina('c700OpcionesPolizas.jsp','');" ></td>               
  </tr>
</table>


  
  <input name="txtUniEje" type="hidden" value=<%=lsUniEje%> >
  <input name="txtPais" type="hidden" value=<%=lsPais%> >
  <input name="txtEntidad" type="hidden" value=<%=lsEntidad%> >
  <input name="txtAmbito" type="hidden" value=<%=lsAmbito%> >
  <input name="txtEjercicio" type="hidden" value=<%=lsEjercicio%> >
  <input name="txtCatCuenta" type="hidden" value=<%=lsCatCuenta%> >
  
<INPUT type="hidden" name="txtClaPol" value="1">
<INPUT type="hidden" name="txtOperacion" value="A">
<INPUT type="hidden" name="txtBanCheque" value="0">


</form>
</body>
</html>