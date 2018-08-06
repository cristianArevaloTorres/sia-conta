<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*, sia.db.sql.Vista, sia.db.sql.Sentencias"%>
<%@ page  import="sia.beans.seguridad.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

<jsp:useBean id="pbPoliza" class="sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page" />
<jsp:useBean id="pbDetallePoliza" class="sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza" scope="page" />
<jsp:useBean id="pbCuenta" class="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>
<jsp:useBean id="pbCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>
<jsp:useBean id="pbCuentaCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>
<jsp:useBean id="pbCuentaBancaria" class="sia.rf.contabilidad.registroContableNuevo.bcCuentasBancarias" scope="page"/>
<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbEstadoCat" class="sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<%
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
   String lsPolizaId=(String) request.getParameter("polizaid");    

   Connection conexion=null;

  List lsTabla = null;   
  String lsUniEje=null;
  String lsPais="147";   
  String lsEntidad=null;
  String lsAmbito=null;
  String lsEjercicio=null;
  String lsCatCuenta=null;
  String lsConsecutivo="";
  String lsGlobal="";
  String [] lsDatos = new String [10];
  sbAutentifica.setPagina("c700ModificarFormulario.jsp");
  String estatus="0";
  String descripcion="";
  String error="0";
      
  try{
  conexion=DaoFactory.getContabilidad();
  

  pbPoliza.select_rf_tr_polizas(conexion,lsPolizaId);
  lsUniEje=pbPoliza.getUnidad_ejecutora();
  lsEntidad=pbPoliza.getEntidad();
  lsAmbito=pbPoliza.getAmbito();
  lsEjercicio=pbPoliza.getEjercicio();
  lsCatCuenta=pbPoliza.getId_catalogo_cuenta();   
  lsConsecutivo=pbPoliza.getConsecutivo();
  lsGlobal=pbPoliza.getGlobal();
    
    
    lsTabla= pbDetallePoliza.select_rf_tr_detalle_poliza_res(conexion,lsPolizaId);

/*    for (int i=0;i<lsTabla.size();i++) {   
      lsDatos=lsTabla.get(i).toString().split("&"); //Separa el registro en los campos
      pbCuenta.select_rf_tr_cuentas_contables(conexion,lsDatos[6]);  //Consulta la cuenta contable de acuerdo al id del catalogo
      lsDatos[7]=pbCuenta.getCuenta_contable();  //Obtiene la cuenta contable y la guarda en la posicion 7 del vector
      lsDatos[8]=pbCuenta.getDescripcion();  //Obtiene la descripcion de la cuenta contable
       System.out.println(i+"***"+lsTabla.get(i).toString()+"***"+lsDatos[0]+lsDatos[1]+lsDatos[2]+lsDatos[6]);
    }  
*/    

 
  // System.out.println(lsPolizaId);
  //String lsPolizaId="";
  
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



<jsp:useBean id="abCuentas" class="sun.jdbc.rowset.CachedRowSet" scope="page" >
<%
/*  abCuentas.setTableName("rtcCuentas");
  abCuentas.setCommand("select idCuenta,nivel1 from rtcCuentas where nivel2 is null order by idCuenta");
  Connection con=DaoFactory.getContabilidad();
  abCuentas.execute(con);
  con.close();
  con=null;
*/  
%>
</jsp:useBean>
<jsp:useBean id="abOrigenDocto" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% 
   /*abOrigenDocto.setTableName("rf_tc_origen_docto");
      abOrigenDocto.setCommand(" select * from " +
                                " (select t.id_origen, t.descripcion, t.activo, t.pago_global " +
                                " from rf_tc_origen_docto  t " +
                                " union " +
                                " select 0 id_origen, 'Sin origen' descripcion, 1 activo, 0 pago_global " +
                                " from dual t) doc " +
                                " order by doc.id_origen ");
      try{
        con = DaoFactory.getContabilidad();
        abOrigenDocto.execute(con);
      }
      catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abOrigenDocto "+e.getMessage());
      } //Fin catch
      finally{
        if (con!=null){
          con.close();
          con=null;
        }
      } //Fin finally
 */
   %>
</jsp:useBean> 

<jsp:useBean id="abOperacionCheque" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% 
   /*
   String unidadesCxl = lsUniEje+",";
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
      String condicionUnidad = lsUniEje.equals("100")?" oc.unidad in('109',"+unidadesCxl+")":" oc.unidad in ("+unidadesCxl+")";
      abOperacionCheque.setTableName("rf_tr_operaciones_cheques");
      abOperacionCheque.setCommand(" select * from ( " +
                                   " select t.ejercicio, t.consecutivo, t.tipo_operacion, t.origen," +
                                    "  t.importe_neto, t.vigente, t.id_operacion, t.unidad" +
                                    " from rf_tr_operaciones_cheques t" +
                                    " union" +
                                    " select '"+lsEjercicio +"' ejercicio, 'Sin cuenta por liquidar' consecutivo, 'A' tipo_operacion, " +
                                    //" select '2012' ejercicio, 'Sin cuenta por liquidar' consecutivo, 'A' tipo_operacion, "+
                                    " 0 origen, 0 importe_neto, 1 vigente, 0 id_operacion, '"+lsUniEje+"' unidad" +
                                    " from dual) oc" +
                                    //" where oc.ejercicio=" +lsEjercicio + condicionUnidad +
                                    " where "+ condicionUnidad +
                                    //" where oc.ejercicio=2012" +
                                    //" and oc.unidad='"+lsUniEje+"'"+
                                    " order by oc.origen ");
      try{
        con = DaoFactory.getContabilidad();
        abOperacionCheque.execute(con);
      }
      catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abOrigenDocto "+e.getMessage());
      } //Fin catch
      finally{
        if (con!=null){
          con.close();
          con=null;
        }
      } //Fin finally
  */
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
   // $('input:text[id*=txtImporteDebe]').setMask();
  }
);
</script>

<script language="JavaScript">
 var totCuentas = 0;
 var aPoliza = new Array();//aOrden,aNivel1,aNivel2,aNivel3,aNivel4,aNivel5,aNivel6,aNivel7,aDescrip,aTipoCta,aImporte,aStCuenta);
<%
   if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){
     pbCheque.select_rf_tr_cheques_poliza(conexion,lsPolizaId);
     pbCheque.setOrigenDocto(pbCheque.getOrigenDocto().equals("")?"0":pbCheque.getOrigenDocto());
     out.println("var importeCheque="+pbCheque.getImporte()+";");
   }
   else
     out.println("var importeCheque=-1;");
%>   
function regresa(){
  var x;
  x=1;
}

function validaCxl() {

   var pasa = true;
   var valorRadio = 0;
   var radios = document.getElementsByName('btrTipoPoliza');
    for(i=0; i < radios.length; i++) {
      if(radios[i].type == 'radio')
        if(radios[i].checked)
         valorRadio = radios[i].value;
    }
  if(valorRadio == 2){
   cxl = document.getElementById("lstOperacionCheque").value;
   cxlSup = document.getElementById("lstOperacionChequeFin").value;
   if(cxl == "" || cxlSup == ""){
     alert("No se puede modificar la póliza de cheque sin seleccionar una CXL inicial y final.");
     pasa = false;
   }
   if(cxl.substr(9,15) > cxlSup.substr(9,15)){
     alert("La CXL inicial no debe de ser mayor a la CXL final.");
     pasa = false;
   }
  }
   return pasa; 
 }
 

function recalcula(){
  var tot1 =0;
  var tot2 =0;
  if (totCuentas > 1){
    for(i=0;i<totCuentas;i++){
      if (formulario.txtDebe[i].value != ""){
        //alert("val1 ="+formulario.txtDebe[i].value);
        tot1 = tot1 + parseFloat(formulario.txtDebe[i].value);
      }
      if (formulario.txtHaber[i].value != ""){
        //alert("val2 ="+formulario.txtHaber[i].value);
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
  formulario.lstNivel1.selectedIndex=0;
  // alert('salio de cancelaAgrega');
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
  if (formulario.lstNivel1.selectedIndex == -1) {
     n1="";
  }else{
     n1=formulario.lstNivel1[formulario.lstNivel1.selectedIndex].value;
  }
  if (formulario.lstNivel2.selectedIndex == -1) {
     n2="";
  } else {
      n2="00000".substring(0,1-formulario.lstNivel2[formulario.lstNivel2.selectedIndex].value.length)+formulario.lstNivel2[formulario.lstNivel2.selectedIndex].value;
  }
  if (formulario.lstNivel3.selectedIndex == -1) {
      n3="";
  } else {
      n3="00000".substring(0,4-formulario.lstNivel3[formulario.lstNivel3.selectedIndex].value.length)+formulario.lstNivel3[formulario.lstNivel3.selectedIndex].value;
  }
  if (formulario.lstNivel4.selectedIndex == -1) {
      n4="";
  } else {
      n4="00000".substring(0,4-formulario.lstNivel4[formulario.lstNivel4.selectedIndex].value.length)+formulario.lstNivel4[formulario.lstNivel4.selectedIndex].value;
  }
   if (formulario.lstNivel5.selectedIndex == -1) {
      n5="";
   } else {
      n5="00000".substring(0,4-formulario.lstNivel5[formulario.lstNivel5.selectedIndex].value.length)+formulario.lstNivel5[formulario.lstNivel5.selectedIndex].value;
   }
   if (formulario.lstNivel6.selectedIndex == -1) {
      n6="";
   } else {
      n6="00000".substring(0,4-formulario.lstNivel6[formulario.lstNivel6.selectedIndex].value.length)+formulario.lstNivel6[formulario.lstNivel6.selectedIndex].value;
   }
   if (formulario.lstNivel7.selectedIndex == -1) {
      n7="";
   } else {
      n7="00000".substring(0,4-formulario.lstNivel7[formulario.lstNivel7.selectedIndex].value.length)+formulario.lstNivel7[formulario.lstNivel7.selectedIndex].value;
   }
   if (formulario.lstNivel8.selectedIndex == -1) {
      n8="";
   } else {
      n8="00000".substring(0,4-formulario.lstNivel8[formulario.lstNivel8.selectedIndex].value.length)+formulario.lstNivel8[formulario.lstNivel8.selectedIndex].value;
   }         
 <% 
   
   if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){    
 %>  
   if (formulario.lstNivel9.selectedIndex == -1) {
      n9="";
   } else {
      n9="00000".substring(0,4-formulario.lstNivel9[formulario.lstNivel9.selectedIndex].value.length)+formulario.lstNivel9[formulario.lstNivel9.selectedIndex].value;
   }
   // alert( ' paso 1111');
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
  // alert( ' paso 2222');

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
      eval("formulario.lstNivel"+niv+".value = formulario.txtCtaNivel"+niv+".value");
     }else{
        divsel[i].style.display ='none';
     }
  }
// alert('salio de muestraDescCuenta');  
}


function cargaPagina(){
   
// alert('entro a carga pagina');
<%
// System.out.println("entro a carga");
  String debe="";
  String haber="";
  String nivel9="";
  
  out.println("var oper=formulario.lstOperaciones.value;");
  out.println("var posicion=oper.indexOf('-')+1;");
  out.println("oper=oper.substring(posicion,oper.length);");
  out.println("if (oper=='99'){");
  out.println("  formulario.btnAgregar.disabled=false;");
  out.println("  formulario.btnElimina.disabled=false;");
  out.println("}");    
  out.println("else{");
  out.println("  formulario.btnAgregar.disabled=true;");
  out.println("  formulario.btnElimina.disabled=true;");
  out.println("}");
  out.println("$('input:text[id*=txtImporteDebe]').setMask();");
  out.println("$('input:text[id*=txtImporteHaber]').setMask();");
  out.println("actualizaTotales();");


   /*   out.println("if (oper=='99') ");
      out.println("cargaDetalle("+"'"+lsDatos[7].substring(0,5)+"',"+"'"+lsDatos[7].substring(5,9)+"',"+"'"+lsDatos[7].substring(9,13)+"',"+"'"
      +lsDatos[7].substring(13,17)+"',"+"'"+lsDatos[7].substring(17,21)+"',"+"'"+lsDatos[7].substring(21,25)+"',"+"'"+lsDatos[7].substring(25,29)+"',"
      +nivel8+"'"+lsDatos[8]+"',"+"'"+lsDatos[0]+ "',"+"'"+debe+"' + formatoCeros('" + debe + "')," + "'"+ haber+"' + formatoCeros('" + haber + "'),"+"'"+lsDatos[1]+"');");
      out.println(" else {");
      out.println("cargaDetalleOperacion("+"'"+lsDatos[7].substring(0,5)+"',"+"'"+lsDatos[7].substring(5,9)+"',"+"'"+lsDatos[7].substring(9,13)+"',"+"'"
      +lsDatos[7].substring(13,17)+"',"+"'"+lsDatos[7].substring(17,21)+"',"+"'"+lsDatos[7].substring(21,25)+"',"+"'"+lsDatos[7].substring(25,29)+"',"
      +nivel8+"'"+lsDatos[8]+"',"+"'"+lsDatos[0]+ "',"+"'"+debe+"' + formatoCeros('" + debe + "')," + "'"+ haber+"' + formatoCeros('" + haber + "'),"+"'"+lsDatos[1]+"',"+pbCuenta.getNivel()+");");
      out.println("}");
      out.println("actualizaTotales();");
   */

%>
// alert('entro2 a carga pagina');
  divImporte.style.display='none';
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
  formulario.lstNivel1.selectedIndex=0;
  
     muestraNivel2(1);
     muestraDivChequera();
//     formulario.lstNivel1.selectedIndex=-1;

 var lsOrigenDocto = "0";
<% if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1") ){  %>
    lsOrigenDocto = <%=pbCheque.getOrigenDocto()%>;
    ComponerListaOperacionCheque (document.formulario.lstOrigenDocto[lsOrigenDocto].value);
    ComponerListaOperacionChequeFin (document.formulario.lstOrigenDocto[lsOrigenDocto].value);
<%  } %>
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
  //  alert('paso 3333a');
   if (nivel ==1){
     ceros=4; // cambio de 5 a 4
   }else 
   if (nivel==2){
     ceros=1;
   }else{
     ceros = 4;
   }

   o="00000".substring(0,ceros-eval("formulario.txtCtaNivel"+nivel+".value.length"))+eval("formulario.txtCtaNivel"+nivel+".value");
   eval("formulario.txtCtaNivel"+nivel).value=o;
   eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
   if (eval("formulario.lstNivel"+nivel+".selectedIndex") == -1){
     divImporte.style.display='none';
     formulario.txtImporte.value ="0";
     formulario.txtReferenciaTem.value="";
     acepta.style.display ='none';
     if (cancela == 0){
       alert ("clave "+o+" no valida");
     }
     eval("formulario.txtCtaNivel"+nivel).value= "";
     eval("formulario.txtCtaNivel"+nivel).focus();
     eval("formulario.lstNivel"+nivel+".value = ''");
<% 
 if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>   
     for (i=nivelInt;i<=9;i++){
<% }else{ %>
     for (i=nivelInt;i<=8;i++){
<% } %>
       eval("formulario.txtCtaNivel"+i).value='';
       eval("trlstNivel"+i+".style.display='none'");
       eval("formulario.lstNivel"+i+".value = ''");
     }
     // alert('paso 4444');
     return false;
   }else{
      eval("formulario.lstNivel"+nivel+".value = formulario.txtCtaNivel"+nivel+".value");
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
  eval("formulario.txtCtaNivel1.focus()");
  // alert ("ooo");
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
  
 // if (!(formulario.btrTipoPoliza[0].checked || formulario.btrTipoPoliza[1].checked  || formulario.btrTipoPoliza[2].checked ||formulario.btrTipoPoliza[3].checked)){
 if (!(formulario.btrTipoPoliza[0].checked || formulario.btrTipoPoliza[1].checked  || formulario.btrTipoPoliza[2].checked)){
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
      mensaje=mensaje + '- Fecha de Creación de la Póliza Incorrecto: '+"\n";
      mensaje=mensaje+formulario.txtFechaCrea.value+"  Debe capturar Fecha Válida dd/mm/aaaa \n";
      ok=false;
    }else{
      mesPoliza = formulario.txtMesPoliza.value;
      if(!(mesPoliza == mes)){
         mensaje = mensaje + "- La fecha de la póliza no se puede cambiar a otro mes. \n";
         ok=false;
      }
    }
  }else{
     mensaje = mensaje + "- Capture la Fecha de Creación de la Póliza. \n"
     ok=false;
  }


   if (isEmpty(formulario.txaConcepto.value)){
    mensaje = mensaje + "- Capture el Concepto de la Póliza. \n"
    ok=false;
  }formulario.txaConcepto.value=validaTxa(formulario.txaConcepto.value,500);
  if (isEmpty(formulario.txaRefGral.value)){
    mensaje = mensaje + "- Capture la referencia general de la póliza. \n"
    ok=false;
  }else formulario.txaRefGral.value=validaTxa(formulario.txaRefGral.value,500)  
  
  if (!(formulario.txtTotDebe.value == formulario.txtTotHaber.value)){
    mensaje = mensaje + "- Los Totales Debe y Haber deben ser iguales y mayor a cero. \n"
    ok=false;
  }
  if ((formulario.lstOperaciones.value)=='000-00'){
    mensaje = mensaje + " Seleccione una operacion contable. \n"
    ok=false;
  }
  
 // if (!(formulario.txtTotDebe.value == importeCheque || importeCheque==-1 )){
 //   mensaje = mensaje + "- El importe del cheque de esta poliza es por: "+importeCheque+", no puede ser diferente al nuevo importe total. \n";
 //   ok=false;
 // }  
  
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
  /*
  if (document.formulario.btrTipoPoliza[1].checked){
  document.formulario.txtBanCheque.value='1';
  }
  else{
    document.formulario.txtBanCheque.value='0';
  }  */
  
  document.formulario.txtBanCheque.value='0';
  if (ok){
     return ok; 
  }else{
     alert(mensaje);
     return ok;
  }
}


function obtieneDescripcion(){
  ultDescrip =formulario.lstNivel1[formulario.lstNivel1.selectedIndex].text
  if (trlstNivel2.style.display == '' && formulario.lstNivel2.value != "")
    ultDescrip = formulario.lstNivel2[formulario.lstNivel2.selectedIndex].text;
  if (trlstNivel3.style.display == '' && formulario.lstNivel3.value != "")
     ultDescrip = formulario.lstNivel3[formulario.lstNivel3.selectedIndex].text;
  if (trlstNivel4.style.display == '' && formulario.lstNivel4.value != "")
     ultDescrip = formulario.lstNivel4[formulario.lstNivel4.selectedIndex].text;
  if (trlstNivel5.style.display == '' && formulario.lstNivel5.value != "")
     ultDescrip = formulario.lstNivel5[formulario.lstNivel5.selectedIndex].text;
  if (trlstNivel6.style.display == '' && formulario.lstNivel6.value != "")
     ultDescrip = formulario.lstNivel6[formulario.lstNivel6.selectedIndex].text;
  if (trlstNivel7.style.display == '' && formulario.lstNivel7.value != "")
     ultDescrip = formulario.lstNivel7[formulario.lstNivel7.selectedIndex].text;
  if (trlstNivel8.style.display == '' && formulario.lstNivel8.value != "")
     ultDescrip = formulario.lstNivel8[formulario.lstNivel8.selectedIndex].text;
     
<% 
   if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>              
  if (trlstNivel9.style.display == '' && formulario.lstNivel9.value != "")
     ultDescrip = formulario.lstNivel9[formulario.lstNivel9.selectedIndex].text;
<% } %>         
// alert('salio de obtieneDescripcion');
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
  txtCuenta.value = formulario.lstNivel1.value
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
  txtSubCuenta1.value = formulario.lstNivel2.value
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
  txtSubCuenta2.value = formulario.lstNivel3.value
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
  txtSubCuenta3.value = formulario.lstNivel4.value
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
  txtSubCuenta4.value = formulario.lstNivel5.value
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
  txtSubCuenta5.value = formulario.lstNivel6.value
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
  txtSubCuenta6.value = formulario.lstNivel7.value
  txtSubCuenta6.name = "txtSubCuenta6";
  txtSubCuenta6.id = txtSubCuenta6.name;
  txtSubCuenta6.readOnly = true;
  txtSubCuenta6.size = "3";
  txtSubCuenta6.className = "cajaTexto";  
  celda.className = "txt";
  celda.appendChild(txtSubCuenta6);  
  celda = fila.insertCell(8);
  numCelda = 8;

  var txtSubCuenta7 = document.createElement("INPUT");
  txtSubCuenta7.type = "text";
  txtSubCuenta7.value = formulario.lstNivel8.value
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
  txtSubCuenta8.value = formulario.lstNivel9.value
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
  
  //var c =  document.getElementById(txtImporteDebe.name);
  //c.attachEvent("onclick", actualizaTotales2);  

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
  // alert('salioagregacuenta');
}

function cargaDetalleOperacion(cuenta, subCuenta1, subCuenta2, subCuenta3, subCuenta4, subCuenta5, subCuenta6, subCuenta7, subCuenta8, descripcion, tipoCuenta, debe, haber, referencia,nivel){
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  var fila = Tabla.insertRow(lRenglones);
  var celda = fila.insertCell(0);
  // alert('entro a carga detalle');  
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
  if (5>nivel)
    txtSubCuenta4.readOnly = false;
  else  
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
 // alert('antes de subcuenta7 ');
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
  
  // alert('despue de subcuenta7 ');
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
  
  //var c =  document.getElementById(txtImporteDebe.name);
  //c.attachEvent("onclick", actualizaTotales2);  

  var txtImporteHaber = document.createElement("INPUT");
  txtImporteHaber.type = "text";
  txtImporteHaber.value =haber;
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
  // alert('paso carga detalle');    
}

function cargaDetalle(cuenta, subCuenta1, subCuenta2, subCuenta3, subCuenta4, subCuenta5, subCuenta6, subCuenta7, descripcion, tipoCuenta, debe, haber, referencia){
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  var fila = Tabla.insertRow(lRenglones);
  var celda = fila.insertCell(0);
  // alert('entro a carga detalle');  
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
 // alert('antes de subcuenta7 ');
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
  
  // alert('despue de subcuenta7 ');
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
  
  
  var txtImporteDebe = document.createElement("INPUT");
  txtImporteDebe.type = "text";
  txtImporteDebe.value =debe;
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
  
  //var c =  document.getElementById(txtImporteDebe.name);
  //c.attachEvent("onclick", actualizaTotales2);  

  var txtImporteHaber = document.createElement("INPUT");
  txtImporteHaber.type = "text";
  txtImporteHaber.value =haber;
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
  // alert('paso carga detalle');    
}


function Borra(i){
  var Tabla = document.getElementById("TablaPrueba");
  Tabla.deleteRow(i+1);
}

function Elimina(){
  var i=0,j=0;
  var Tabla = document.getElementById("TablaPrueba");
  var lRenglones = Tabla.rows.length; //Obtiene los renglones de la Tabla
  
  //alert("renglones en elimina "+lRenglones);
  
  if (lRenglones < 2){} 
  else{
    if (lRenglones == 2 ){ // en caso de que solo tenga 1 registro el table no lo toma el chbEliminar como un vector
        if(formulario.chbEliminar.checked){
/*        
           // Este if es para actualizar los totales
           if (formulario.txtImporteDebe.value==""){
             tot=parseFloat(formulario.txtTotHaber.value)-parseFloat(formulario.txtImporteHaber.value);
             formulario.txtTotHaber.value=redondear(tot)
           }   
           else{
             tot=parseFloat(formulario.txtTotDebe.value)-parseFloat(formulario.txtImporteDebe.value);
             formulario.txtTotDebe.value=redondear(tot)
           }  
*/
           var Tabla = document.getElementById("TablaPrueba");
           Tabla.deleteRow(1);
            actualizaTotales();
        } 
    }
    else{ //en caso de que si se tenga mas de 1 registro
     i=0;
      while (i < formulario.txtCuenta.length){  
        if(formulario.chbEliminar[i].checked){
        //  alert(formulario.txtImporteDebe[i].value+'*'+formulario.txtImporteHaber[i].value);
          // Este if es para actualizar los totales
/*          
           if (formulario.txtImporteDebe[i].value==""){
             tot=parseFloat(formulario.txtTotHaber.value)-parseFloat(formulario.txtImporteHaber[i].value);
             formulario.txtTotHaber.value=redondear(tot)
           }   
           else{
             tot=parseFloat(formulario.txtTotDebe.value)-parseFloat(formulario.txtImporteDebe[i].value);
             formulario.txtTotDebe.value=redondear(tot)
           }        
*/           
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
 //   alert('debe '+formulario.txtTotDebe.value+' haber '+formulario.txtTotHaber.value+' posic '+posicion+  ' lon '+ longitud+ ' deci '+ decimales); 
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
  
  //alert("renglones en actualiza "+lRenglones);
  
//alert('actualiza'+i+' '+formulario.txtCuenta.length+'&');  
  if (lRenglones < 2){} 
  else{
    if (lRenglones == 2 ){ // en caso de que solo tenga 1 registro el table no lo toma el chbEliminar como un vector
     // alert('*'+formulario.txtImporteDebe.value+'*'+formulario.txtImporteHaber.value+'*');
      if (formulario.txtImporteDebe.value!=""&&formulario.txtImporteDebe.value!=null){
    //     alert('1');
         totDebe=totDebe+parseFloat(deleteComas(formulario.txtImporteDebe.value));

      }   
    if (formulario.txtImporteHaber.value!=""&&formulario.txtImporteHaber.value!=null){           
    //    alert('2');
       totHaber=totHaber+parseFloat(deleteComas(formulario.txtImporteHaber.value));
    }   
    }  
    else{
      //alert("length"+formulario.txtCuenta.length);
      while (i < formulario.txtCuenta.length){  
      //    alert('/'+formulario.txtImporteDebe[i].value+'/'+formulario.txtImporteHaber[i].value+'/');
         if (formulario.txtImporteDebe[i].value!=""&&formulario.txtImporteDebe[i].value!=null){
        // alert('3');
           totDebe=totDebe+parseFloat(deleteComas(formulario.txtImporteDebe[i].value));
         }  
         if (formulario.txtImporteHaber[i].value!=""&&formulario.txtImporteHaber[i].value!=null){
          //   alert('4');
           totHaber=totHaber+parseFloat(deleteComas(formulario.txtImporteHaber[i].value));
         }  
         i++;
        // alert(i);
      }//while
    }
  }  
  formulario.txtTotDebe.value=redondear(totDebe);
  formulario.txtTotHaber.value=redondear(totHaber);
  
  formulario.txtTotDebe.value=formulario.txtTotDebe.value+formatoCeros(formulario.txtTotDebe.value);
  formulario.txtTotHaber.value=formulario.txtTotHaber.value+formatoCeros(formulario.txtTotHaber.value);
  
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
    cuentaCon=cuentaCon+"'"+cuentaConTem+"',";
    i++;
    // if (typeof(formulario.txtCuenta) == 'undefined')break;
   // continue; 
  }//while
// alert('cuentaCon '+cuentaCon)
loadSource('capaCuenta',null,'c700CapaCuenta.jsp?ejercicio='+<%=lsEjercicio%>+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje='+<%=lsUniEje%>+'&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&cuentas='+cuentaCon,bufferFrame10);
}

function validaFechaCapa(){
//alert(formulario.txtValFecha.value);
ban=true;
  if (formulario.txtValFecha.value=='2'){
    alert('La fecha especificada para la poliza no puede ser mayor al dia de hoy. ');
    ban=false; 
  }  
  else
  if (formulario.txtValFecha.value=='3'){
    alert('El año especificado para la poliza no es igual al ejercicio seleccionado. ');
    ban=false; 
  }  
  else
  if (formulario.txtValFecha.value=='4'){
    alert('El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especifica de la poliza');
    ban=false; 
  }  
  // alert('reg'+formulario.lstOperaciones.disabled);  
  if (ban){ 
    formulario.lstOperaciones.disabled=false;
    formulario.btrTipoPoliza[0].disabled=false;
    formulario.btrTipoPoliza[1].disabled=false;
    formulario.btrTipoPoliza[2].disabled=false;
    //formulario.btrTipoPoliza[3].disabled=false;
    formulario.btnAceptar.disabled=true;
    // alert(formulario.lstOperaciones.disabled);
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
  /*if (document.formulario.btrTipoPoliza[1].checked)
    divChequera.style.display='';
  else
    divChequera.style.display='none';*/
}
</script>

</head>


 
<body onLoad=cargaPagina(); >
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Modificar pólizas", "Modificar", true);</jsp:scriptlet>    

<FORM Method="post" action="c700Control.jsp" name="formulario" >

<%
   // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
   pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
   estatus=pbEstadoCat.getEstatus();
   descripcion=pbEstadoCat.getDescripcion();
   if (!estatus.equals("1")){
     error="C";
     throw new Exception(" En estos momentos no es posible crear, modificar o cancelar polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
   }
%>

  <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Consecutivo=<%=lsConsecutivo%></H2>
  
<table>
  <tr>
     <td height="26">Tipo de P&oacute;liza:</td>
     <td>
        <p>
<% if (pbPoliza.getTipo_poliza_id().equals("1")){ %>
          <input type="radio" name="btrTipoPoliza" value=1 checked disabled>Diario
<% }else{%>
          <input type="radio" name="btrTipoPoliza" value=1d disabled>Diario
 <%}%>   
   
<%// if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){%>
         <!-- <input type="radio" name="btrTipoPoliza" value=3 checked disabled>Cheques (Modificando poliza de Egreso)-->
<% //}else{%>
         <!-- <input type="radio" name="btrTipoPoliza" value=1d disabled>Cheques (Generando poliza de Egreso)-->
 <%//}%>   
          
<% if (pbPoliza.getTipo_poliza_id().equals("3")&& (pbPoliza.getBan_cheque().equals("0") ||  pbPoliza.getBan_cheque().equals(""))){  %>
          <input type="radio" name="btrTipoPoliza" value=3 checked disabled>Egreso
<% }else{%>
          <input type="radio" name="btrTipoPoliza" value=1d disabled>Egreso
 <%}%>   
          
<% if (pbPoliza.getTipo_poliza_id().equals("4")){ %>
          <input type="radio" name="btrTipoPoliza" value=4 checked disabled>Ingreso
<% }else{%>
          <input type="radio" name="btrTipoPoliza" value=1d disabled>Ingreso
 <%}%>   
          
        </p><!-- termina DynAPI -->

     </td>
  </tr>

  <tr>
    <td>Fecha de Afectaci&oacute;n:</td>
    <td><input type='text' name='txtFechaCrea' size='10' maxlength='10' class='cajaTexto' VALUE=<%=pbPoliza.getFecha()%> onchange=''><a href="javascript: void(0);" onmouseover="if (timeoutId) clearTimeout(timeoutId);return true;" onclick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCrea')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a></td>
    <td class=txt type= "hidden"><input type= "hidden" name="txtMesPoliza" size='3' display='none' id="txtMesPoliza" value =<%=pbPoliza.getFecha().substring(3,5)%> readOnly></td>

  </tr>
</table>

<div id="divChequera">
<%    
 if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){
   pbCheque.select_rf_tr_cheques_poliza(conexion,lsPolizaId);
   pbCuentaCheque.select_rf_tc_cuentas_cheques(conexion,pbCheque.getCuenta_cheques_id());
   pbCuentaBancaria.select_rf_tesoreria_rf_tr_cuentas_bancarias(conexion,pbCuentaCheque.getId_cuenta());
%>   
  <table>
    <tr>
      <td>Cuentas de Cheques:</td>
      <td><select disabled name='lstCuentas'  tabindex='9' class= 'cajaTexto' onChange='cambiaConsecutivo(document.formulario.lstCuentas[selectedIndex].value,document.formulario.lstCuentas.options[document.formulario.lstCuentas.selectedIndex].text);'>
           <option value=<%=pbCheque.getCuenta_cheques_id()%>-<%=pbCheque.getConsecutivo()%>-<%=pbCuentaCheque.getAbreviatura()%>><%=pbCuentaBancaria.getNum_cuenta()%> - <%=pbCuentaBancaria.getNombre_cta()%></option>
        </select></td>
    </tr>
    <tr><td>Folio: </td> 
       <td><input type='text' name='txtFolio' size='10'  maxlength='10' class='cajaTexto' VALUE='<%=pbCheque.getConsecutivo()%>' disabled></td>
    </tr>
    <tr><td>Beneficiario: </td>   
       <td><input type='text' name='txtBeneficiario' size='50'  maxlength='50' class='cajaTexto' VALUE='<%=pbCheque.getBeneficiario()%>'  ></td>
    </tr>    
    <tr>
       <td>Importe original del Cheque: </td>   
       <td><input type='text' name='txtCheque' size='15'  maxlength='15' class='cajaTexto' VALUE='<%=pbCheque.getImporte()%>' disabled style="text-align: right; float:right"></td>
    </tr>  
<tr><td>Origen:</td>
      <!--<SELECT id='unidad'      class='cajaTexto' NAME="lstProceso"     OnChange='ComponerListaOperacionCheque(document.formulario.lstOrigenDocto[selectedIndex].value);BorrarListaAct();Formulario.lstSubProceso.options[1].selected=true;ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);Formulario.lstOperacionChequeFin.options[1].selected=true;'>-->
  <!--<td><SELECT id='lstOrigenDocto' class='cajaTexto' NAME='lstOrigenDocto' onchange="ComponerListaOperacionCheque(document.formulario.lstOrigenDocto[selectedIndex].value);BorrarListaOperacionCheque();formulario.lstOperacionCheque.options[1].selected=true;ComponerListaOperacionChequeFin(document.formulario.lstOrigenDocto[selectedIndex].value);formulario.lstOperacionChequeFin.options[1].selected=true;">-->
  <td><SELECT id='lstOrigenDocto' class='cajaTexto' NAME='lstOrigenDocto' onchange="ComponerListaOperacionCheque(document.formulario.lstOrigenDocto[selectedIndex].value);ComponerListaOperacionChequeFin(document.formulario.lstOrigenDocto[selectedIndex].value)">

         <%
          int cuenta;
          String cat;
          int cuentaFin;
          String catFin;
          try{
            abOrigenDocto.beforeFirst(); 
            while (abOrigenDocto.next()){
            
            
              if (abOrigenDocto.getString("id_origen").equals(pbCheque.getOrigenDocto())){
%>        
                <option selected value='<%=abOrigenDocto.getString("id_origen")%>'><%=abOrigenDocto.getString("descripcion")%></option>
<%            }else{ %>            
                <OPTION VALUE="<%=abOrigenDocto.getString("id_origen")%>"><%=abOrigenDocto.getString("descripcion")%></OPTION>
<%            }    
           } //Fin while
         }catch (Exception e) {
           System.out.println("Error en lista de cierre: "+ e.getMessage());
         }         
         %>
      </SELECT></td></tr>  
<tr>
<td >CXL inicial:</td>
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
var opciones = new Array();
var arrayOpciones = "|";
var arrayOpcionesSup = "|";
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
      //opciones.concat(cat);
      arrayOpciones = arrayOpciones + "opciones"+<%=cat%>+"|";
      //alert(arrayOpciones);
      //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
<%
     } //fin if
%>
  opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=abOperacionCheque.getString("consecutivo")%>","<%=abOperacionCheque.getString("consecutivo")%>");
<%
  cuenta=cuenta+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en operacion cheque: "+ e.getMessage());
  }         
         
%>
var contador;
function ComponerListaOperacionCheque ( array ) {
  pocicion = 1;
  lsCxlCheque = document.getElementById('cxlCheque').value;
  cxl = "";
// Compone la lista dependiente a partir
// del valor de la opcion escogida en la lista "padre"
BorrarListaOperacionCheque();
//alert(arrayOpciones);
//alert(arrayOpciones.indexOf("opciones"+array));
  if (arrayOpciones.indexOf("opciones"+array) != -1){
      array = eval("opciones" + array);
      for (contador=1; contador<array.length; contador++){
        //Añade elementos a nuestro combobox dependiente
        var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
        formulario.lstOperacionCheque.options[contador] = optionObj;
        cxl = formulario.lstOperacionCheque.options[contador].value;
        if(cxl == lsCxlCheque)
          pocicion = contador;
      } // for
      // posiciona la siguiente lista en su valor a modificar
      formulario.lstOperacionCheque.options[pocicion].selected=true;
  }// if
} // ComponerLista

 
//Elimina las opciones de nuestro vector dependiente cada que se requiera

function BorrarListaOperacionCheque() {
    formulario.lstOperacionCheque.length=0;
}

//Inicializamos enviando la clave del proceso padre para este caso
ComponerListaOperacionCheque('0');
</script>
</td></tr>
<tr><td >CXL final:</td>
<td><SELECT id="lstOperacionChequeFin" class="cajaTexto" NAME="lstOperacionChequeFin">
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
  cuentaFin=1;
  catFin="null";
  try{
  abOperacionCheque.beforeFirst();
  while (abOperacionCheque.next()){
    if (!catFin.equals(abOperacionCheque.getString("origen")))
    {
      cuentaFin=1;
      catFin=abOperacionCheque.getString("origen");
%>
      var opciones<%=catFin%> = new Array();
      //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
      arrayOpcionesSup = arrayOpcionesSup + "opcionesSup"+<%=catFin%>+"|";
<%    
     } //fin if
%>
  opciones<%=catFin%>[<%=cuentaFin%>]=new Tupla("<%=abOperacionCheque.getString("consecutivo")%>","<%=abOperacionCheque.getString("consecutivo")%>");
<%
  cuentaFin=cuentaFin+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en operacion cheque: "+ e.getMessage());
  }         
         
%>
 contador=0;
 
function ComponerListaOperacionChequeFin ( array ) {
  //alert(opciones);
  pocicion = 1;
  lsCxlChequeFin = document.getElementById('cxlChequeFin').value;
  cxlFin = "";
// Compone la lista dependiente a partir
// del valor de la opcion escogida en la lista "padre"
BorrarListaOperacionChequeFin();
  if (arrayOpcionesSup.indexOf("opcionesSup"+array) != -1){
      if( eval("opciones" + array).length!=0)
      array = eval("opciones" + array);
      for (contador=1; contador<array.length; contador++){
        //Añade elementos a nuestro combobox dependiente
        var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
        formulario.lstOperacionChequeFin.options[contador] = optionObj;
        cxlFin = formulario.lstOperacionChequeFin.options[contador].value;
        if(cxlFin == lsCxlChequeFin)
          pocicion = contador;
      } // for
      // posiciona la siguiente lista en su valor a modificar
      formulario.lstOperacionChequeFin.options[pocicion].selected=true;
  }//if
} // ComponerLista

 
//Elimina las opciones de nuestro vector dependiente cada que se requiera

function BorrarListaOperacionChequeFin() {
    formulario.lstOperacionChequeFin.length=0;
}

//Inicializamos enviando la clave del proceso padre para este caso
ComponerListaOperacionChequeFin('0');
</script>
</td></tr>
  </table>
<%
 }
%>
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
<H3>Detalle de la P&oacute;liza</H3>
<a href="#marca1" id="refmarc" name="nrefmarc"> </a> 

<table id="tAgregaCta">
  <tr>
    <td width="100">Agregar Cuenta: </td>
    <td><div id="trlstNivel1">
           <input type='text' name='txtCtaNivel1' size='10'  maxlength='10' class='cajaTexto' onFocus ='muestraDescCuenta(0);'value=''
            tabindex=1 onChange="if (relleCeros(1)) muestraNivel2(2);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(1)) muestraNivel2(2);} " onkeyup="formulario.lstNivel1.value = formulario.txtCtaNivel1.value">
        </div>
    </td>
    <td><div id="trlstNivel2">
           <input type='text' name='txtCtaNivel2' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(1);"
           tabindex=2 onChange="if (relleCeros(2)) muestraNivel2(3);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(2)) muestraNivel2(3);} " onkeyup="formulario.lstNivel2.value = formulario.txtCtaNivel2.value">
        </div>
    </td>
    <td><div id="trlstNivel3">
           <input type='text' name='txtCtaNivel3' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(2);"
            tabindex=3 onChange="if (relleCeros(3)) muestraNivel2(4);" onKeyDown=" if(event.keyCode==13){ event.keyCode=35; if (relleCeros(3)) muestraNivel2(4);} " onkeyup=" formulario.lstNivel3.value = formulario.txtCtaNivel3.value">
        </div>
    </td>
    <td><div id="trlstNivel4">
           <input type='text' name='txtCtaNivel4' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(3);"
            tabindex=4 onChange="if (relleCeros(4))  muestraNivel2(5);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(4)) muestraNivel2(5);} " onkeyup="formulario.lstNivel4.value = formulario.txtCtaNivel4.value">
          </div>
    </td>
    <td><div id="trlstNivel5">
           <input type='text' name='txtCtaNivel5' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(4);"
            tabindex=5 onChange="if (relleCeros(5))  muestraNivel2(6);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(5)) muestraNivel2(6);} " onkeyup="formulario.lstNivel5.value = formulario.txtCtaNivel5.value">
        </div>
    </td>
    <td><div id="trlstNivel6">
           <input type='text' name='txtCtaNivel6' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(5);"
            tabindex=6 onChange="if (relleCeros(6))  muestraNivel2(7);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(6)) muestraNivel2(7);} " onkeyup="formulario.lstNivel6.value = formulario.txtCtaNivel6.value">
        </div>
    </td>
    <td><div id="trlstNivel7">
           <input type='text' name='txtCtaNivel7' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(6);"
            tabindex=7 onChange="if (relleCeros(7))  muestraNivel2(8);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(7)) muestraNivel2(8);} " onkeyup="formulario.lstNivel7.value = formulario.txtCtaNivel7.value">
        </div>
    </td>    
<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>              
    <td><div id="trlstNivel8">
           <input type='text' name='txtCtaNivel8' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(7);"
            tabindex=8 onChange="if (relleCeros(8))  muestraNivel2(9);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; if (relleCeros(8)) muestraNivel2(9);} " onkeyup="formulario.lstNivel8.value = formulario.txtCtaNivel8.value">
        </div>
    </td>
 <%} else {%>
     <td><div id="trlstNivel8">
           <input type='text' name='txtCtaNivel8' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(7);"
            tabindex=8 onChange="relleCeros(8);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; relleCeros(8);} " onkeyup="formulario.lstNivel8.value = formulario.txtCtaNivel8.value">
        </div>
    </td>
 <%}%>
<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>              
    
    <td><div id="trlstNivel9">
           <input type='text' name='txtCtaNivel9' size='10'  maxlength='10' class='cajaTexto' value='' onFocus="muestraDescCuenta(8);"
            tabindex=9 onChange="relleCeros(9);" onKeyDown="if(event.keyCode==13){ event.keyCode=35; relleCeros(9);} " onkeyup="formulario.lstNivel9.value = formulario.txtCtaNivel9.value">
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
         <select  name='lstNivel1' class= 'cajaTexto' onChange="muestraNivel2(2); txtCtaNivel1.value =lstNivel1.value; "  >
         <option value="" selected></option>
         </select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel2' class= 'cajaTexto' onChange='javascript: muestraNivel2(3); txtCtaNivel2.value =lstNivel2.value; '></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel3' class= 'cajaTexto' onChange='javascript: muestraNivel2(4); txtCtaNivel3.value =lstNivel3.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel4' class= 'cajaTexto' onChange='javascript: muestraNivel2(5); txtCtaNivel4.value =lstNivel4.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel5' class= 'cajaTexto'  onChange='javascript: muestraNivel2(6); txtCtaNivel5.value =lstNivel5.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel6' class= 'cajaTexto'  onChange='javascript: muestraNivel2(7); txtCtaNivel6.value =lstNivel6.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel7' class= 'cajaTexto'  onChange='javascript: muestraNivel2(8); txtCtaNivel7.value =lstNivel7.value' ></select>
      </div>            
<% 
  if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>                    
      <div id = 'divsel'>
          <select name='lstNivel8' class= 'cajaTexto'  onChange='javascript: muestraNivel2(9); txtCtaNivel8.value =lstNivel8.value' ></select>
      </div>
      <div id = 'divsel'>
          <select name='lstNivel9' class= 'cajaTexto' onChange='javascript: txtCtaNivel9.value =lstNivel9.value;  divImporte.style.display="";  acepta.style.display =""; formulario.txtImporte.focus();'></select>
      </div>
<% }else{%>
      <div id = 'divsel'>
          <select name='lstNivel8' class= 'cajaTexto' onChange='javascript: txtCtaNivel8.value =lstNivel8.value;  divImporte.style.display="";  acepta.style.display =""; formulario.txtImporte.focus();'></select>
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
     <td width="63%"> <INPUT type='button' name='btnCancelar' onblur="formulario.txtCtaNivel1.focus();" onClick="formulario.txtCtaNivel1.value ='';relleCeros(0);" VALUE="Cancelar" class='boton' tabindex=11></td>
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
        if (abOperaciones.getString("maestro_operacion_id").equals(pbPoliza.getMaestro_operacion_id())){
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
<%




  String ceros="";
  String cerosDebe="";
  String cerosHaber="";
  String localiza="";
  int posicion=0;
  int longitud=0;
  String decimales="";
  String etiDebe="";
  String etiHaber="";


  for (int i=0;i<lsTabla.size();i++) {   
      lsDatos=lsTabla.get(i).toString().split("&"); //Separa el registro en los campos
      pbCuenta.select_rf_tr_cuentas_contables(conexion,lsDatos[6]);  //Consulta la cuenta contable de acuerdo al id del catalogo
      lsDatos[7]=pbCuenta.getCuenta_contable();  //Obtiene la cuenta contable y la guarda en la posicion 7 del vector
      lsDatos[8]=pbCuenta.getDescripcion();  //Obtiene la descripcion de la cuenta contable
      if (lsDatos[0].equals("0")){
        debe=lsDatos[4];
        haber="";
      }  
      else{
        debe="";
        haber=lsDatos[4];      
      }
      if (debe.equals(""))
        debe="0";
      if (haber.equals(""))
        haber="0";
      nivel9="'',";  
     if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
        nivel9="'"+lsDatos[7].substring(29,33)+"',";
      }  
      
      ceros="";
      localiza=debe;  
      posicion=localiza.lastIndexOf('.');
      longitud=localiza.length();
      decimales="";
      if (posicion!=-1){
        decimales=localiza.substring(posicion+1,longitud);
        if (decimales.length()==1){
          ceros="0";
        } 
      }
      else
       ceros=".00";
      cerosDebe=ceros;
      
      ceros="";
      localiza=haber;  
      posicion=localiza.lastIndexOf('.');
      longitud=localiza.length();
      decimales="";
      if (posicion!=-1){
        decimales=localiza.substring(posicion+1,longitud);
        if (decimales.length()==1){
          ceros="0";
        } 
      }
      else
       ceros=".00";
      cerosHaber=ceros;
      
      if (lsDatos[0].equals("0")){
       etiDebe="";
       etiHaber="readOnly";
      }
      else{
        etiDebe="readOnly";
        etiHaber="";
      }

      
%>
<tr class=general>
<td class=chb><input type = "checkbox" class = "chb" size='3' name = "chbEliminar" id = "chbEliminar" > </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '4' value='<%=lsDatos[7].substring(0,4)%>' name = "txtCuenta" id ="txtCuenta" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(4,5)%>' name = "txtSubCuenta1" id ="txtSubCuenta1" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(5,9)%>' name = "txtSubCuenta2" id ="txtSubCuenta2" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(9,13)%>' name = "txtSubCuenta3" id ="txtSubCuenta3" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(13,17)%>' name = "txtSubCuenta4" id ="txtSubCuenta4" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(17,21)%>' name = "txtSubCuenta5" id ="txtSubCuenta5" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(21,25)%>' name = "txtSubCuenta6" id ="txtSubCuenta6" readonly> </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(25,29)%>' name = "txtSubCuenta7" id ="txtSubCuenta7" readonly> </td>

<%
   if ((Integer.parseInt(lsEjercicio)>=2012) && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))){
%>
  <td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(29,33)%>' name = "txtSubCuenta8" id ="txtSubCuenta8" readonly> </td>
<%}%>  

<td class=txt><input type = "text" class = "cajaTexto" size = '20' value='<%=lsDatos[8]%>' name = "txtDesc" id ="txtDesc" readOnly > </td>

<td class=txt><input type = "text" class = "cajaTexto" size = '10' value='<%=debe+cerosDebe%>' name = "txtImporteDebe" id ="txtImporteDebe"  alt='msk' onblur='actualizaTotales();' <%=etiDebe%>> </td>
<%
//out.println("$('input:text[id*=txtImporteDebe]').setMask();");
%>

<td class=txt><input type = "text" class = "cajaTexto" size = '10' value='<%=haber+cerosHaber%>' name = "txtImporteHaber" id ="txtImporteHaber"  alt='msk' onblur='actualizaTotales();' <%=etiHaber%>> </td>
<%
//out.println("$('input:text[id*=txtImporteHaber]').setMask();");
%>
<td class=txt><input type = "text" class = "cajaTexto" size = '15' value='<%=lsDatos[1]%>' name = "txtReferencia" id ="txtReferencia" > </td>
<%
if (lsDatos[0].equals("0")){
%>
 <td class=txt type= "hidden"><input type= "hidden" name="txtTipoOper" size='3' display='none' id="txtTipoOper" value = '1' readOnly></td>
<% 
}else{
%>
 <td class=txt type= "hidden"><input type= "hidden" name="txtTipoOper" size='3' display='none' id="txtTipoOper" value = '2' readOnly></td>
 </tr>
 <%
   }     
  }
%>
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
       <input id="txtTotDebe" name="txtTotDebe"  alt='msk'  value="0.00"  class="cajaTexto" size="15" readonly  style="text-align: right; float:right" ></td>
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
    <td><textarea name='txaConcepto' rows='3' cols='80' class='cajaTexto' ><%=pbPoliza.getConcepto()%></textarea></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>
  <tr>
    <td>Referencia General:</td>
    <td><textarea name='txaRefGral' rows='3' cols='80' class='cajaTexto' ><%=pbPoliza.getReferencia()%></textarea></td>
    <td>&nbsp;</td>
    <td>&nbsp; </td>
  </tr>  
  <tr>
    <td  width="48%" valign="middle"><div id= "divDescPlant">
        Descripcion: <textarea  name="txaDescPlant" rows="2" cols="50" class='cajaTexto'></textarea></div></td>
  </tr>
</table>
<% if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){%>
  <INPUT type="hidden" id="cxlCheque" name="cxlCheque" value="<%=pbCheque.getCxl().equals("")?"0":pbCheque.getCxl()%>">
  <INPUT type="hidden" id="cxlChequeFin" name="cxlChequeFin" value="<%=pbCheque.getCxlSup().equals("")?"0":pbCheque.getCxlSup()%>">

<%
 }
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
       <INPUT type='button' name='btnAceptar' value="Aceptar" class='boton' onclick="if (validaSalida()){if(validaCxl()){ capaFecha();}}"></td>
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
  <input name="txtPolizaId" type="hidden" value=<%=lsPolizaId%> >
  <input name="txtConsecutivo" type="hidden" value=<%=lsConsecutivo%> >
  <input name="txtGlobal" type="hidden" value=<%=lsGlobal%> >
  
  <INPUT type="hidden" name="txtClaPol" value="1">
  <INPUT type="hidden" name="txtOperacion" value="M">
  <INPUT type="hidden" name="txtBanCheque" value="0">

</form>
</body>
</html>