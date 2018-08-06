<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="formaContable" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="page"/>
<jsp:useBean id="pbPoliza" class="sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="maestroOperaciones" class="sia.rf.contabilidad.registroContableNuevo.bcMaestroOperaciones" scope="page"/>
<jsp:useBean id="abChequeras" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<jsp:useBean id="pbCheque" class = "sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>
<jsp:useBean id="pbCuentaCheque" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>
<jsp:useBean id="pbCuentaBancaria" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentasBancarias" scope="page"/>
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<jsp:useBean id="pbDetallePoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza" scope="page" />
<jsp:useBean id="pbCuenta" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>

<%
  String lsUniEje=controlRegistro.getUnidad();
  String lsPais="147";   
  String lsEntidad=String.valueOf(controlRegistro.getEntidad());
  String lsAmbito=String.valueOf(controlRegistro.getAmbito());    
  String lsEjercicio=String.valueOf(controlRegistro.getEjercicio());    
  String lsCatCuenta=String.valueOf(controlRegistro.getIdCatalogoCuenta());  
  //despliegue de talba con valores
  List lsTabla = null;  
  String [] lsDatos2= new String[10];
  String [] lsDatos = new String [10];
  
  
  String idPoliza = request.getParameter("polizaid") != null ? request.getParameter("polizaid") : "";
  String txtFecha01= request.getParameter("txtFecha01");
  String txtFecha02= request.getParameter("txtFecha02");
  String txtPoliza01= request.getParameter("txtPoliza01");
  String txtPoliza02= request.getParameter("txtPoliza02");
  String lstMes= request.getParameter("lstMes");
  String txtforma=request.getParameter("txtforma");
  String consecutivo= null;
  String SQL=null;
  Connection con=null;
  
  String estatus="";
  String  descripcion="";

  
  abChequeras.setTableName("");
SQL="SELECT * FROM (SELECT cc.consecutivo,cc.abreviatura,to_char(cc.fecha_vig_ini,'dd/mm/yyyy') fecha_vig_ini,"+
         "to_char(cc.fecha_vig_fin,'dd/mm/yyyy') fecha_vig_fin,"+
         "cb.num_cuenta,"+
         "primeramay(cb.nombre_cta) desc_cuenta,"+
         "primeramay(ec.descripcion) desc_estatus,"+
         "cc.id_cuenta,"+
         "cc.maestro_operacion_id,"+
         "cc.cuenta_cheques_id,"+
         "moper.consecutivo consec_moper,"+
         "(cc.consecutivo + 1) ultimo_consecutivo,"+
         "cc.estatus_cheque_id"+
         " FROM rf_tc_cuentas_cheques cc"+
         " INNER JOIN rf_tc_maestro_operaciones moper ON moper.maestro_operacion_id = cc.maestro_operacion_id"+
         " AND cc.unidad_ejecutora ="+controlRegistro.getUnidad()+
         " AND cc.ambito ="+controlRegistro.getAmbito()+
         " AND cc.entidad ="+controlRegistro.getEntidad()+
         " AND cc.pais =147"+
         " AND "+controlRegistro.getEjercicio()+"between extract(year from cc.fecha_vig_ini) and extract(year from cc.fecha_vig_fin)"+
         //" AND to_char(cc.fecha_vig_ini,'yyyymmdd') <= "+"20090720"+ //CAMBIAR POR LA FECHA ACTUAL          IGUAL QUE EN BCCUENTASCHEQUES
         //" AND to_char(cc.fecha_vig_fin,'yyyymmdd') >= "+"20090720"+
         " INNER JOIN rf_tesoreria.rf_tr_cuentas_bancarias cb ON cc.id_cuenta = cb.id_cuenta"+
         //" AND cc.unidad_ejecutora = cb.unidad_ejecutora"+
         //" AND cc.ambito = cb.ambito"+
         //" AND cc.entidad = cb.entidad"+
         " INNER JOIN rf_tc_estatus_chequeras ec ON cc.estatus_cheque_id = ec.estatus_cheque_id) qrslt WHERE(estatus_cheque_id = 1)";
         //System.out.println("chequera "+SQL);
         
         
         
  abChequeras.setCommand(SQL);
   con=null;
  try{
     con = DaoFactory.getContabilidad();
     abChequeras.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo abChequeras "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally

  

Connection conexion=null;
CachedRowSet crsFormaContable = null;      
String cadConexion = null;
String referenciaGeneral = null;
String tipoPolizaId=null;
String concepto= null;
String referencia= null;
String fecha=null;
String mes=null;
String forma=null;
String NumPoliza=null;
String documentoFuente=null;
String camposVariables=",";
String muestraSigno=null;
Integer maximo=0;

 try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
     estatus=pbEstadoCat.getEstatus();
     descripcion=pbEstadoCat.getDescripcion();
     if (!estatus.equals("1")){
        throw new Exception(" En estos momentos no es posible crear, modificar o cancelar polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
     }    
    lsTabla= pbDetallePoliza.select_rf_tr_detalle_poliza_res(conexion,idPoliza);
    lsDatos2= lsTabla.get(0).toString().split("&");     
    maestroOperaciones.select_rf_tc_maestro_operaciones_carga_var(conexion,lsUniEje,lsAmbito,lsEntidad,lsCatCuenta,lsEjercicio,"99");
    pbPoliza.select_rf_tr_polizas(conexion, idPoliza);        
    consecutivo= pbPoliza.selectConsecutivo(conexion,idPoliza);
    crsFormaContable = formaContable.select_formaContable_secuenciaForma_configuraFormaPoliza(conexion,idPoliza);
    crsFormaContable.first();
    tipoPolizaId=pbPoliza.getTipo_poliza_id();
    referenciaGeneral= pbPoliza.getReferencia();
    concepto=pbPoliza.getConcepto();
    referencia=lsDatos2[1];
    fecha=lsDatos2[2];
    mes=fecha.substring(3,5);
    forma= pbPoliza.getOrigen();
    formaContable.setFormaContableId(crsFormaContable.getString("FORMA_CONTABLE_ID"));
    formaContable.select(conexion);        
    maximo=formaContable.nivelMax(conexion,crsFormaContable.getString("FORMA_CONTABLE_ID"));
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c723ControlModificar</title>
    
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
   
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>   
   <script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8" ></script>
   <script type="text/javascript" src="../../../Librerias/Javascript/mascaras.js" charset="utf-8" ></script>

   
<script language="javascript" type="text/javascript">

if (window.history) {
        
        function noBack(){window.history.forward()}
        noBack();
        window.onload=noBack;
        window.onpageshow=function(evt){if(evt.persisted)noBack()}
        window.onunload=function(){void(0)}
}

var ban=false; 

function validaFechaCapa()
{

 ban=true;
   if (document.formulario.txtFechaCrea.value.substring(3,5)!=document.formulario.mes.value){
    alert('La fecha de la póliza no corresponde al mes con la que se afectó. \n');
    document.formulario.btnAceptar.disabled= false;
    ban = false; 
  }  else
   if (document.formulario.txtValFecha.value=='2'){
    alert('La fecha especificada para la poliza no puede ser mayor al dia de hoy. \n');
    document.formulario.btnAceptar.disabled= false;
    ban = false; 
  }  
  else
  if (document.formulario.txtValFecha.value=='3'){
    alert('El año especificado para la poliza no es igual al ejercicio seleccionado. \n');
    document.formulario.btnAceptar.disabled= false;
    ban = false;  
  }  
  else
  if (document.formulario.txtValFecha.value=='4'){
    alert('El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza \n');
    document.formulario.btnAceptar.disabled= false;
    ban=false; 
  }  
  else
  if (document.formulario.txtValFecha.value=='5'){
    alert('El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza \n');
    document.formulario.btnAceptar.disabled= false;
    ban=false; 
  }  

  for(var x= 0; x< document.formulario.elements.length; x++) {
    var objeto= document.formulario.elements[x];
    if(objeto.type== "text"  && objeto.name=='txtReferencia' && objeto.value=='REFERENCIA') {
      alert('Debe asignar una referencia \n');
      document.formulario.btnAceptar.disabled= false;
      ban=false; 
      x=document.formulario.elements.length;
    }
  }
  
  //validación ceros.
  for(var x= 0; x< document.formulario.elements.length; x++) {
    var objeto= document.formulario.elements[x];
    if(objeto.type== "text" && objeto.value.length>0 && objeto.disabled==false && objeto.name!='txtTotDebe' && objeto.name!='txtTotHaber' && (objeto.value==objeto.title || parseFloat(objeto.value)==0)) {
      alert('Debe asignar un valor valido en '+objeto.name+' valor '+objeto.value+' \n');
      document.formulario.btnAceptar.disabled= false;
      ban=false; 
      x=document.formulario.elements.length;
    }
  }

  valida=validaDH();
       if(ban && valida){
          activar(document.formulario);
          document.formulario.action='../c700Control.jsp?';  
          document.formulario.submit();
        }  
  
}

function revisaFormulario() 
{
  document.formulario.btnAceptar.disabled= true;
   var valida01=true;
   var cadenaError='';
   var date01;
  
   
   if(document.formulario.txtFechaCrea.value=="")
   {
      valida01=false;
      cadenaError=cadenaError+"Es necesario escribir la fecha póliza a generar \n";
      document.formulario.btnAceptar.disabled= false;
   }
     
   if (valida01)
   {
     if (!(document.formulario.txtFechaCrea.value=="") )
     {      
        
        valida01=ValidarObjeto(document.formulario.txtFechaCrea.value,"F");
        
        if (!(valida01)) {
            cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.formulario.txtFechaCrea.value+"\n";
            document.formulario.btnAceptar.disabled= false;
           valida01=false; 
        }
        else date01=convierte(document.formulario.txtFechaCrea.value);
       
     }//txtFecha01
   }//valida01   

   if(!(valida01))
      alert(cadenaError);
   else
       capaFecha();
       //valida=validaDH();
       //alert('ban'+ban+' validaDH');
       //if(ban && valida){
         // activar(document.formulario);
          //document.formulario.action='../c700Control.jsp?';  
          //document.formulario.submit();
        //}  
}


function capaFecha(){
//alert(document.formulario.txtFechaCrea.value);
//alert(document.formulario.txtSubCuenta2[0].value);
var fechaR=document.formulario.txtFechaCrea.value;
var programa=formulario.txtSubCuenta2[0].value;
loadSource('capaFecha',null,'../c700CapaFecha.jsp?ejercicio='+<%=lsEjercicio%>+'&catCuentaId='+<%=lsCatCuenta%>+'&uniEje=<%=lsUniEje%>&entidad='+<%=lsEntidad%>+'&ambito='+<%=lsAmbito%>+'&fecha='+fechaR+'&programa='+programa,bufferFrame8);
}

function activar(formulario) {
  for(var x= 0; x< formulario.elements.length; x++) {
    var objeto= formulario.elements[x];
    if((objeto.type== "text"  || objeto.type== "radio") && objeto.disabled==true) {
      objeto.disabled=false;
    }
  }
}

function validaDH(){
    var valor=0;
    var debe=0;
    var haber=0;
    var compara=true;
    for(x=0; x< document.formulario.length;x++) {
      var objeto= document.formulario.elements[x];
      if(objeto.name== "txtImporteDebe")
                debe=debe+ parseFloat(stripCharsInBag(objeto.value, ', '))*100;
      if(objeto.name== "txtImporteHaber")
                haber=haber+ parseFloat(stripCharsInBag(objeto.value, ', '))*100;
     } // for x
   // alert("debe"+ debe+ " haber "+haber); 
    if(debe!=haber){
      compara=false;
      alert("El debe y el haber no son iguales");
      document.formulario.btnAceptar.disabled= false;
    }
    return compara;
}

function llamarPagina(){
document.formulario.hdconcepto.value="";
if(document.formulario.txtReferencia.length>1)
  document.formulario.txtReferencia[0].value="";
else
  document.formulario.txtReferencia.value="";

document.formulario.txtforma.value='<%=txtforma%>';
document.formulario.action='c723ResultadoModificar.jsp';
document.formulario.submit();
}

function rellena(cadena,object){
//alert(cadena);
var cadenaTem=object.value;
var lonRellena=4-cadena.length;
var x=1;
  while (x<=lonRellena){
    cadenaTem='0'+cadenaTem;
    x=x+1;
  }
  object.value=cadenaTem;
return cadenaTem;
}

function eliminaSigno(objeto){
  objeto.value=objeto.value.replace('-','');
}

function asignaValor(valor,nombre){
 var signo='';
 var titulo='';
 //alert('valor= '+valor+' nombre= '+nombre);
 
   for(z=0; z< document.formulario.length;z++) {
       var objeto= document.formulario.elements[z];
       signo=objeto.title.substring(objeto.title.length-1);
       if(signo=='+' || signo=='-')     
         titulo=objeto.title.substring(0,objeto.title.length-1)
       else 
          titulo=objeto.title;   
       if(objeto.type== "text" && titulo==nombre)
       //alert('objeto.type='+objeto.type+' objeto.name= '+nombre);
         if(signo=='-'){
           if(parseFloat(valor)<0)
             objeto.value=valor.substring(1,valor.length);
           else
             objeto.value='-'+valor;
         }
         else{
           objeto.value=valor;
         }
   } // for x
 }//

function agregaTotales(){
  var debe=0;
  var haber=0;
    for(x=0; x< document.formulario.length;x++) {
      var objeto= document.formulario.elements[x];
      if(objeto.name== "txtImporteDebe")
                debe=debe+ parseFloat(stripCharsInBag(objeto.value, ', '))*100;
      if(objeto.name== "txtImporteHaber")
                haber=haber+ parseFloat(stripCharsInBag(objeto.value, ', '))*100;
     } // for x
     document.formulario.txtTotDebe.value=debe;
     miMascara("txtTotDebe");
     document.formulario.txtTotHaber.value=haber;
     miMascara("txtTotHaber");   
}//agregaTotales()

</script>
  </head>
  <body>
     <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Formas Contables", "Aplicar", true);</jsp:scriptlet>    
     <br>
  <FORM name="formulario" method="post"  action="">
  
     <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
        </tr>
        <tr>
         <td align="center"><%=lsUniEje%></td>
         <td align="center"><%=lsEntidad%></td>
         <td align="center"><%=lsAmbito%></td>
        </tr>
      </table>
      <br><br>
    
<table width="100%" border="0">
    <tr>
      <td class="negrita" align="right">Fecha p&oacute;liza modificar: </td>
      <td>
          <input type='text' value='<%=fecha%>'  name='txtFechaCrea' id="txtFechaCrea" size='10' maxlength='10' class='cajaTexto'>
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCrea')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
       </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">FORMA:</td>
    <td >
      <div align="left">
        <%=pbPoliza.getOrigen()%>
      </div>
    </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">TIPO:</td>
    <td >
      <div align="left">
        <%=crsFormaContable.getString("DESCRIPCION")%>
      </div>
    </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">CONSECUTIVO:</td>
    <td >
      <div align="left">
        <%=consecutivo%>
      </div>
    </td>
    </tr>
    <%
      crsFormaContable.beforeFirst();
      while(crsFormaContable.next()){
      if(camposVariables.indexOf(crsFormaContable.getString("CODIGO")+",")<0){
        camposVariables=camposVariables+(crsFormaContable.getString("CODIGO")+",");
         if(crsFormaContable.getString("SIGNO")!=null){
          if(crsFormaContable.getString("SIGNO").equals("0"))
            muestraSigno="(+)";
          else
            muestraSigno="(-)";
        }else muestraSigno="";
    %>
       <tr>
        <td class="negrita" align="right"><%=crsFormaContable.getString("CODIGO").toUpperCase()+muestraSigno%></td>
        <%
          if(crsFormaContable.getString("NIVEL").equals("98")||crsFormaContable.getString("NIVEL").equals("99")){
           if(crsFormaContable.getString("NIVEL").equals("99")){%>
            <td><input type="text" size="10"  class="cajaTexto"  id="<%=crsFormaContable.getString("CODIGO")%>"  value="<%=referencia%>" name="<%=crsFormaContable.getString("CODIGO")%>" onchange="asignaValor(this.value,this.name);"  >  </td>  
        <%}else{  //if(crsFormaContable.getString("NIVEL").equals("99"))%>
            <td><input type="text" size="15"  alt="signed-decimal" class="cajaTexto"  id="<%=crsFormaContable.getString("CODIGO")%>"  name="<%=crsFormaContable.getString("CODIGO")%>" onchange="eliminaSigno(this);asignaValor(this.value,this.name);agregaTotales();" onFocus="miMascara('<%=crsFormaContable.getString("CODIGO")%>')";  >  </td>  
        <%}
         }else{
          if(crsFormaContable.getString("NIVEL").equals("2")){
            %>
              <td><input type="text" size="1" maxlength="1" alt="signed-decimal-1" class="cajaTexto"  id="<%=crsFormaContable.getString("CODIGO")%>"  name="<%=crsFormaContable.getString("CODIGO")%>" onchange="eliminaSigno(this);asignaValor(this.value,this.name);agregaTotales();" onFocus="miMascara('<%=crsFormaContable.getString("CODIGO")%>')";  >  </td>             
              <%}
           if(!crsFormaContable.getString("NIVEL").equals("2")){ %>
              <td><input type="text" alt="integer" class="cajaTexto" size="10" id="<%=crsFormaContable.getString("CODIGO")%>" name="<%=crsFormaContable.getString("CODIGO")%>" onblur="rellena(this.value, this);" onchange="asignaValor(this.value,this.name);" onFocus="miMascara('<%=crsFormaContable.getString("CODIGO")%>')"; >  </td>
              <%}
     }// if(crsFormaContable.getString("NIVEL").equals("98")||crsFormaContable.getString("NIVEL").equals("99")){
     %>
     </tr>
     <%
      }
     }// while(crsFormaContable.next())/if(crsFormaContable.getString("NIVEL").equals("98")||crsFormaContable.getString("NIVEL").equals("99")){
     crsFormaContable.beforeFirst();//while(crsFormaContable.next()) 
     if(pbPoliza.getTipo_poliza_id().equals("2")){
       pbCheque.select_rf_tr_cheques_poliza(conexion,idPoliza);
       pbCuentaCheque.select_rf_tc_cuentas_cheques(conexion,pbCheque.getCuenta_cheques_id());
       pbCuentaBancaria.select_rf_tesoreria_rf_tr_cuentas_bancarias(conexion,pbCuentaCheque.getId_cuenta());
     %>
    <tr>
      <td>Cuentas de Cheques:</td>
      <td>
        <select disabled="true" name='lstCuentas'  tabindex='9' class= 'cajaTexto' onChange='cambiaConsecutivo(document.formulario.lstCuentas[selectedIndex].value,document.formulario.lstCuentas.options[document.formulario.lstCuentas.selectedIndex].text);'>
          <option value=<%=pbCheque.getCuenta_cheques_id()%>-<%=pbCheque.getConsecutivo()%>-<%=pbCuentaCheque.getAbreviatura()%>><%=pbCuentaBancaria.getNum_cuenta()%> - <%=pbCuentaBancaria.getNombre_cta()%></option>
        </select>
      </td>
    </tr>
    <tr>
       <td>Folio: </td>   
       <td><input type='text' name='txtFolio' size='10'  maxlength='10' class='cajaTexto' VALUE='<%=pbCheque.getConsecutivo()%>' disabled></td>
    </tr>
    <tr>
       <td>Beneficiario: </td>   
       <td>
       <input type='text' name='txtBeneficiario' size='50'  maxlength='50' class='cajaTexto' VALUE='<%=pbCheque.getBeneficiario()%>'>
       </td>
    </tr>
  <%
    }//if cheques
  %>
    
    <tr> 
    <td class="negrita" align="right">Concepto:</td>
    <td >
      <div align="left">
        <input type='text' name='txaConcepto' id="txaConcepto" size='100'  class='cajaTexto' value="<%=concepto%>" >
      </div>
    </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">Referencia General:</td>
    <td >
      <div align="left">
        <input type='text' name='txaRefGral' id="txaRefGral" size='100'  class='cajaTexto' value="<%=referenciaGeneral%>" >
      </div>
    </td>
    </tr>
</table>

<br><br>
<table id='TablaPrueba' border="1" class="general" width="100%">
            <thead class='tabla01'>
             <tr>
                <td class="azulObs" width="4%"><strong>Cuenta</strong></td>
                <%
                 int cont=2;                 
                while(cont<=maximo){ %>             
                <td class="azulObs" width="4%" align="center"><strong><%="Nivel"+cont%></strong></td>
                <%cont++;} %>
            
        <%//} %>       
                <td class="azulObs" width="10%" align="center" ><strong>Importe</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Referencia</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
                
             </tr>
            </thead>
        <%
           //despliegue de talba con valores
            String debe="";
            String haber="";
            String nivel8="";
            String ceros="";
            String cerosDebe="";
            String cerosHaber="";
            String localiza="";
            int posicion=0;
            int longitud=0;
            String decimales="";
            String etiDebe="";
            String etiHaber="";   
            crsFormaContable.beforeFirst();          
            boolean[] niveles= new boolean[maximo+2]; //{false, false, false, false, false, false,false, false, false, false};
            String[] nivelesCampo= new String[maximo+2];//{"", "", "", "", "", "","", "", "", ""};
            int t=0;
            while(t<=maximo+1){
              nivelesCampo[t]="";
              t++;
            }
            
      List<bcRfTcSecuenciaFormaPojo> secs = formaContable.getDetSec();      
      for (int i=0;i<lsTabla.size();i++) {   
      
      bcRfTcSecuenciaFormaPojo sec=secs.get(i);
      List<bcRfTcConfiguraFormaPojo> cfgs = sec.getDetConf();
      for(bcRfTcConfiguraFormaPojo cfg : cfgs) {
        if(Integer.parseInt(cfg.getNivel())<=maximo){
            niveles[Integer.parseInt(cfg.getNivel())-1]=!cfg.getEsvariable().equals("0");
            nivelesCampo[Integer.parseInt(cfg.getNivel())-1]=cfg.getCodigo() != null ? cfg.getCodigo() : "";
        }else{
            if(Integer.parseInt(cfg.getNivel())==98){
                niveles[maximo]=!cfg.getEsvariable().equals("0");
                nivelesCampo[maximo]=cfg.getCodigo() != null ? cfg.getCodigo() : "";
            }else{
                niveles[maximo+1]=!cfg.getEsvariable().equals("0");
                nivelesCampo[maximo+1]=cfg.getCodigo() != null ? cfg.getCodigo() : "";
            }
        }        
      }
      
      
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
<td class=txt><input type = "text" class = "cajaTexto" size = '4' value='<%=lsDatos[7].substring(0,4)%>' name = "txtCuenta" id ="txtCuenta" readonly <%=niveles[0]?"":"disabled"%> <%=nivelesCampo[0].length()>0 && niveles[0]?"title='"+nivelesCampo[0]+"'":""%> > </td>
<td class=txt><input type = "text" class = "cajaTexto" size = '1' value='<%=lsDatos[7].substring(4,5)%>' name = "txtSubCuenta1" id ="txtSubCuenta1" readonly <%=niveles[1]?"":"disabled"%> <%=nivelesCampo[1].length()>0 && niveles[1]?"title='"+nivelesCampo[1]+"'":""%> > </td>
<%
  cont=2;
  int cont2=5;
  int cont3=cont2+4;
  while (cont<maximo){%>
    <td class=txt><input type = "text" class = "cajaTexto" size = '3' value='<%=lsDatos[7].substring(cont2,cont3)%>' name = "txtSubCuenta<%=cont%>" id ="txtSubCuenta<%=cont%>" readonly <%=niveles[cont]?"":"disabled"%> <%=nivelesCampo[cont].length()>0 && niveles[cont]?"title='"+nivelesCampo[cont]+"'":""%> > </td>  
  <%
   cont++;
   cont2=cont3;
   cont3=cont3+4;
  }%>
<td class=txt>
<%if(lsDatos[0].equals("0")){%>
  <input readonly type="text"  alt="signed-decimal" class="cajaTexto" name="txtImporteDebe" id="txtImporteDebe" size="15"  value="<%=lsDatos[4]%>" <%=nivelesCampo[maximo].length()>0?"title='"+(nivelesCampo[maximo]+(Double.parseDouble(lsDatos[4])<0?"-":"+"))+"'":""%> />
  <input type="hidden" name="txtImporteHaber" id="txtImporteHaber" value="0"/> 
<%  }else{// if(lsDatos[0].equals("0")){%{%>
  <input readonly type="text"  alt="signed-decimal" class="cajaTexto"   name="txtImporteHaber" id="txtImporteHaber" size="15" value="<%=lsDatos[4]%>" <%=nivelesCampo[maximo].length()>0?"title='"+(nivelesCampo[maximo]+(Double.parseDouble(lsDatos[4])<0?"-":"+"))+"'":""%> />                     
  <input type="hidden" name="txtImporteDebe" id="txtImporteDebe" value="0"/>                         
<%  }%>
</td>

                  
<td class=txt><input readonly type = "text" class = "cajaTexto" size = '15' value='<%=lsDatos[1]%>' name = "txtReferencia" id ="txtReferencia" <%=nivelesCampo[maximo+1].length()>0?"title='"+nivelesCampo[maximo+1]+"'":""%> > </td>



<td align="center"> 
  <input type="radio" name="txtTipoOper<%=i%>" id="txtTipoOper<%=i%>" disabled  value="1" <%=lsDatos[0].equals("0")?"checked":""%>/> 
  <input type="radio" name="txtTipoOper<%=i%>" id="txtTipoOper<%=i%>" disabled  value="2" <%=lsDatos[0].equals("1")?"checked":""%>/>

<%if(lsDatos[0].equals("0")){%>
      <input  type="hidden" name="txtTipoOper"  id="txtTipoOper" value="1" />                 
 <%}else{%>
      <input type="hidden" name="txtTipoOper" id="txtTipoOper" value="2" />                                   
  <%}%>
</td>  
  <%
  for(int limpia=0;limpia<niveles.length;limpia++){
    niveles[limpia]=false;
    nivelesCampo[limpia]="";
  }
  }//for (int i=0;i<lsTabla.size();i++) {   
%>            
                


   
  </table>

      <table>
  <tr>
               <td align="rigth" width="70%">
                  <p align="right">Total Debe</td>
                <td width="10%" align="right" class=general>
                  <input id="txtTotDebe" name="txtTotDebe"  alt="signed-decimal"   class="cajaTexto" size="15" readonly  ></td>
                  <td align="right" width="30%"> <p align="right">Total Haber</td>
                <td width="10%" align="right" class=general>
                  <input id="txtTotHaber" name="txtTotHaber" alt="signed-decimal"   class="cajaTexto" size="15" readonly ></td>
                  
             
  </tr>
  </table>
  <SCRIPT>agregaTotales()</SCRIPT>
<table>
    <tr>
      <td  align="right">
          <input type='button' name='btnAceptar' value='Aceptar' class='boton' onclick="revisaFormulario();" >
      </td>
      <td>
          <input type="button" name='btnRegresar'  value='Regresar' class='boton' onclick="llamarPagina()">
           <input type="hidden" name="hdforma" id="hdforma" value="<%=forma%>">
           <input type="hidden" name="hdconcepto" id="hdconcepto" value="<%=concepto%>">
           <input type="hidden" name="hddocumentoFuente" id="hddocumentoFuente" value="<%=documentoFuente%>">
           
           
           <input type="hidden" name="btrTipoPoliza" id="btrTipoPoliza" value="<%=tipoPolizaId%>">
           <input type="hidden" name="txtPolizaId" id="txtPolizaId" value="<%=idPoliza%>">
           <input type="hidden" name="txtOrigen" id="txtOrigen" value="<%=pbPoliza.getOrigen()%>">
           <input type="hidden" name="txtConsecutivo" id="txtConsecutivo" value="<%=pbPoliza.getConsecutivo()%>">
           <input type="hidden" name="txtPolRef" id="txtPolRef" value="<%=pbPoliza.getPoliza_referencia()%>">
           <input type="hidden" name="txtGlobal" id="txtGlobal" value="<%=pbPoliza.getGlobal()%>">
           <input type="hidden" name="txtforma" id="txtforma" value="<%=pbPoliza.getOrigen()%>">
           <input type="hidden" name="txtIdCatalogo" id="txtIdCatalogo" value="<%=lsCatCuenta%>">
           
           
           <input type="hidden" name="txtUniEje" id="txtUniEje" value="<%=controlRegistro.getUnidad()%>">
           <input type="hidden" name="txtAmbito" id="txtAmbito" value="<%=controlRegistro.getAmbito()%>">
           <input type="hidden" name="txtEntidad" id="txtEntidad" value="<%=controlRegistro.getEntidad()%>">
           <input type="hidden" name="txaConcepto" id="txaConcepto" value="<%=concepto%>">
           <input type="hidden" name="txtOperacion" id="txtOperacion" value="M">
           <input type="hidden" name="txtPais" id="txtPais" value="147">
           <input type="hidden" name="lstOperaciones" id="txtOperacion" value="<%=maestroOperaciones.getMaestro_operacion_id()+"-"+pbPoliza.getOrigen()%>">
                                
           <input type="hidden" name="txtCatCuenta" id="txtCatCuenta" value="<%=lsCatCuenta%>">
           <input type="hidden" name="txtEjercicio" id="txtEjercicio" value="<%=lsEjercicio%>">
           <input type="hidden" name="txtClaPol" id="txtClaPol" value="1">
           <input type="hidden" name="paginaRegreso" id="paginaRegreso" value="../registroContableNuevo/filtroGeneral.jsp?opcion=irModificaPolizaForma">

           <input type="hidden" name="txtFecha01" id="txtFecha01" value="<%=txtFecha01%>">
           <input type="hidden" name="txtFecha02" id="txtFecha02" value="<%=txtFecha02%>">
           <input type="hidden" name="mes" id="mes" value="<%=mes%>">
           <input type="hidden" name="txtPoliza01" id="txtPoliza01" value="<%=txtPoliza01%>">
           <input type="hidden" name="txtPoliza02" id="txtPoliza02" value="<%=txtPoliza02%>">
           <input type="hidden" name="lstMes" id="lstMes" value="<%=lstMes%>">
           <input type="hidden" name="capturaFormas" id="capturaFormas" value="modifica"/>
                                 
      </td>
     </tr>
  </table>
 <!-- Capa Fecha -->
 <IFrame style="display:none" name = "bufferFrame8">
 </IFrame>
 <div id = "capaFecha">
</div>

</FORM>
  </body>
</html>

 
<%
  }catch(Exception e){System.out.println("Ocurrio un error al accesar  "+e.getMessage());
   %>
      <p>Ha ocurrido un error al accesar la Base de Datos,</p>
      <p>favor de reportarlo al Administrador del Sistema.</p>
      <p>Gracias.</p>
   <% 
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
       if(crsFormaContable!=null){
          crsFormaContable.close();
          crsFormaContable=null;
       }
     } //Fin finally
%>
