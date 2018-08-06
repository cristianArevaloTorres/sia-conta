<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.formas.*" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="formaContable" class="sia.rf.contabilidad.registroContableNuevo.formas.bcRfTcFormasContables" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="maestroOperaciones" class="sia.rf.contabilidad.registroContableNuevo.bcMaestroOperaciones" scope="page"/>
<jsp:useBean id="abChequeras" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<%
  String lsUniEje=request.getParameter("unidadEjecutora");
  String lsPais="147";   
  String lsEntidad=request.getParameter("entidad");
  String lsAmbito=request.getParameter("ambito");    
  String lsEjercicio=request.getParameter("hLsEjercicio");    
  String lsCatCuenta=request.getParameter("idCatalogoCuenta");  
  
  
  String idFormaContable = request.getParameter("ID") != null ? request.getParameter("ID") : "";
  String forma = request.getParameter("hdforma") != null ? request.getParameter("hdforma") : "";
  String concepto = request.getParameter("hdconcepto") != null ? request.getParameter("hdconcepto") : "";
  
  String documentoFuente = request.getParameter("hddocumentoFuente") != null ? request.getParameter("hddocumentoFuente") : "";
  String tipoPolizaId = request.getParameter("hdtipoPolizaId") != null ? request.getParameter("hdtipoPolizaId") : "";
  
  String estatus="0";
  String descripcion="";
  
  String SQL=null;
  Connection con=null;
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
         " AND "+controlRegistro.getEjercicio()+" between extract(year from cc.fecha_vig_ini) and extract(year from cc.fecha_vig_fin)"+
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
String camposVariables=",";
String muestraSigno=null;
Integer maximo=0;
 try{
 // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
    estatus=pbEstadoCat.getEstatus();
    descripcion=pbEstadoCat.getDescripcion();
    if (!estatus.equals("1")){
      throw new Exception(" En estos momentos no es posible crear polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
    } 
    
    maestroOperaciones.select_rf_tc_maestro_operaciones_carga_var(conexion,lsUniEje,lsAmbito,lsEntidad,lsCatCuenta,lsEjercicio,"99");
    formaContable.setFormaContableId(idFormaContable);
    formaContable.select(conexion);        
    crsFormaContable = formaContable.select_formaContable_secuenciaForma_configuraForma(conexion,idFormaContable);
    crsFormaContable.first();
    tipoPolizaId=formaContable.getTipoPolizaId();
    maximo=formaContable.nivelMax(conexion,idFormaContable);
    concepto=formaContable.getConcepto();
    
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c719FormasControl</title>
    
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
   
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script src="../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
    
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
  
  //validación ceros.
  for(var x= 0; x< document.formulario.elements.length; x++) {
    var objeto= document.formulario.elements[x];
    if(objeto.type== "text" && objeto.disabled==false && objeto.name!='txtTotDebe' && objeto.name!='txtTotHaber' && (objeto.value==objeto.title || parseFloat(objeto.value)==0)) {
      alert('Valor inválido en la variable '+objeto.name+' \n');
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
    if(document.formulario.txaRefGral.value=="")
   {
      valida01=false;
      cadenaError=cadenaError+"Es necesario escribir la referencia general \n";
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
     
}


function capaFecha(){
var fechaR=formulario.txtFechaCrea.value;
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
    if(debe!=haber){
      compara=false;
      alert("El debe y el haber no son iguales o ambos valores son igual a cero.");
      document.formulario.btnAceptar.disabled= false;
    }
    return compara;
}

function llamarPagina(){
document.formulario.hdconcepto.value="";
document.formulario.action='c719FormasResultado.jsp';
document.formulario.submit();
}

function rellena(cadena,object){
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
  document.formulario.btnAceptar.disabled= false;         
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
//alert('funcion agrega totales: debe'+debe+'haber: '+haber);   
}//agregaTotales()

function cambiaConsecutivo(cadena, descripcion){
 var arreglo=cadena.split('-');
 var conse=arreglo[1];
 document.formulario.txtFolio.value=conse;
 document.formulario.txaConcepto.value=descripcion;
 }

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
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
        </tr>
      </table>
      <br><br>
    
<table width="100%" border=0>
    <tr>
      <td class="negrita" align="right">Fecha p&oacute;liza generar: </td>
      <td>
          <input type='text' name='txtFechaCrea' id="txtFechaCrea" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCrea')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa
       </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">FORMA:</td>
    <td >
      <div align="left">
        <%=formaContable.getForma()%>
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
    <%
      crsFormaContable.beforeFirst();
      while(crsFormaContable.next()){
      if(camposVariables.indexOf(crsFormaContable.getString("CODIGO")+",")<0)
      {
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
            <td><input type="text" size="10" class="cajaTexto"  id="<%=crsFormaContable.getString("CODIGO")%>"  name="<%=crsFormaContable.getString("CODIGO")%>" onchange="asignaValor(this.value,this.name);"  >  </td>  
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
     crsFormaContable.first();//while(crsFormaContable.next()) 
     if(formaContable.getTipoPolizaId().equals("2")){ %>
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
    <%
    }
    crsFormaContable.first();//while(crsFormaContable.next())
     %>
    <tr> 
    <td class="negrita" align="right">Concepto:</td>
    <td >
      <div align="left">
        <input type='text' name='txaConcepto' id="txaConcepto" size='100'  class='cajaTexto' value="<%=crsFormaContable.getString("CONCEPTO")%>">
      </div>
    </td>
    </tr>
    <tr> 
    <td class="negrita" align="right">Referencia General:</td>
    <td >
      <div align="left">
        <input type='text' name='txaRefGral' id="txaRefGral" size='100'  class='cajaTexto' >
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
               <td class="azulObs" width="10%" align="center" ><strong>Importe</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Referencia</strong></td>
                <td class="azulObs" width="10%" align="center"><strong>Debe / Haber</strong></td>
             </tr>
            </thead>
        <%
            List<Integer> valores = new ArrayList();
            cont=1;
            while(cont<=maximo){
              valores.add(cont);
              cont++;
            }
            valores.add(98);
            valores.add(99);
            List<bcRfTcSecuenciaFormaPojo> secs = formaContable.getDetSec();
            int numSec = 0;
            int subCuenta=0;
           for(bcRfTcSecuenciaFormaPojo sec : secs) {
            %>
              <tr>
            <%
              List<bcRfTcConfiguraFormaPojo> cfgs = sec.getDetConf();
              String valCuenta = ""; 
              String signo = null;
              boolean band = false;
              int x = 0;
              subCuenta=0;
               for(bcRfTcConfiguraFormaPojo cfg : cfgs) {
                band = false;
                while(cfg == null || (valores.get(x) != Integer.parseInt( cfg.getNivel() ) )) {
                  band = true;
                  x++;
                  %><td></td><%
                }
                x++;
                valCuenta = cfg != null && cfg.getCodigo() != null ? cfg.getCodigo() : "";
                signo = cfg != null && cfg.getSigno() != null && signo == null ? cfg.getSigno():signo;
                
                if(cfg.getNivel().equals("1")){
               %>        
                <td align="center">
                  <input type="text" class="cajaTexto" id="txtCuenta" name="txtCuenta" title="<%=valCuenta%>" size="<%=cfg.getNivel().equals("98")?15:10%>" maxlength=""  value="<%=valCuenta%>" disabled onFocus = "window.status='<%=cfg.getNivel()%>'"/> 
                </td>
                <%
                }else{// else del if (cfg.getNivel().equals("1")){
                  if(cfg.getNivel().equals("98") || cfg.getNivel().equals("99")){%>
                  <td align="center">
                  <%if(cfg.getNivel().equals("99")){%>  
                  <input readonly  type="text" class="cajaTexto" name="txtReferencia" id="txtReferencia" size="<%=cfg.getNivel().equals("98")?15:10%>" maxlength=""  value="<%=valCuenta%>" title="<%=valCuenta%>"  onFocus ="window.status='<%=cfg.getNivel()%>'"/> 
                  <%}else{  //if(cfg.getNivel().equals("99")){
                      if(sec.getOperacionContableId().equals("0")){%>
                        <input readonly type="text"  alt="signed-decimal" class="cajaTexto" name="txtImporteDebe" id="txtImporteDebe" size="<%=cfg.getNivel().equals("98")?15:10%>"  value="<%=valCuenta%>" title="<%=valCuenta+(signo.equals("0")?"+":"-")%>"  onFocus ="window.status='<%=cfg.getNivel()%>';miMascara('txtImporteDebe');"/>                     
                        <input type="hidden" name="txtImporteHaber" id="txtImporteHaber" value="0"/> 
                  <%  }else{//if(sec.getOperacionContableId().equals("0")){%>
                        <input  readonly type="text"  alt="signed-decimal" class="cajaTexto"   name="txtImporteHaber" id="txtImporteHaber" size="<%=cfg.getNivel().equals("98")?15:10%>" value="<%=valCuenta%>" title="<%=valCuenta+(signo.equals("0")?"+":"-")%>"  onFocus ="window.status='<%=cfg.getNivel()%>';miMascara('txtImporteHaber');"/>                     
                        <input type="hidden" name="txtImporteDebe" id="txtImporteDebe" value="0"/>                         
                  <%  }
                    } //termina else de //if(cfg.getNivel().equals("99")){%>
                </td><%
                  } else{//else del IF (cfg.getNivel().equals("98") || cfg.getNivel().equals("99"))
                %>
                <td align="center">
                  <input  readonly type="text"  alt="integer" class="cajaTexto"   title="<%=valCuenta%>" name="txtSubCuenta<%=Integer.parseInt(cfg.getNivel())-1%>" id="txtSubCuenta<%=Integer.parseInt(cfg.getNivel())-1%>"  onblur="rellena(this.value, this);" size="<%=cfg.getNivel().equals("98")?15:10%>" value="<%=valCuenta%>"  <%=cfg.getEsvariable().equals("0")?"disabled":""%> onFocus ="window.status='<%=cfg.getNivel()%>';miMascara('txtSubCuenta<%=Integer.parseInt(cfg.getNivel())-1%>');"/> 
                </td>                                                                                                                                                                                                                                                                                                                                              
                <%
                  subCuenta++;
                } //else del ifdentro del else
                } // termina else del if (cfg.getNivel().equals("1")){
              }
               
               if(subCuenta<(maximo-1)){
                 for(int y=subCuenta;y<(maximo-1);y++){%>
                    <input type="hidden"  name="txtSubCuenta<%=y+1%>" id="txtSubCuenta<%=y+1%>" value=""/>
                 <%}
               }
              %>
                <td align="center"> 
                  <input type="radio" name="txtTipoOper<%=numSec%>" id="txtTipoOper<%=numSec%>" disabled  value="1" <%=sec.getOperacionContableId().equals("0")?"checked":""%>/> 
                  <input type="radio" name="txtTipoOper<%=numSec%>" id="txtTipoOper<%=numSec%>" disabled  value="2" <%=sec.getOperacionContableId().equals("1")?"checked":""%>/>
                </td> 
                
                <%
                if(sec.getOperacionContableId().equals("0")){%>
                  <input  type="hidden" name="txtTipoOper"  id="txtTipoOper" value="1" />                 
                <%}else{%>
                  <input type="hidden" name="txtTipoOper" id="txtTipoOper" value="2" />                                   
                <%}  //for(bcRfTcConfiguraFormaPojo cfg : cfgs)              
                %>
              </tr>
         <%       

            numSec++;
            }//for (bcRfTcSecuenciaFormaPojo sec : secs)
%>
   
  </table>
  <table>
  <tr>
               <td align="rigth" width="70%">
                  <p align="right">Total Debe</td>
                <td width="10%" align="right" class=general>
                  <input id="txtTotDebe" name="txtTotDebe"  alt="signed-decimal" value="0.00"  class="cajaTexto" size="15" readonly  ></td>
                  <td align="right" width="30%"> <p align="right">Total Haber</td>
                <td width="10%" align="right" class=general>
                  <input id="txtTotHaber" name="txtTotHaber" alt="signed-decimal" value="0.00"  class="cajaTexto" size="15" readonly ></td>
                  
             
  </tr>
  </table>
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
           <input type="hidden" name="txtUniEje" id="txtUniEje" value="<%=controlRegistro.getUnidad()%>">
           <input type="hidden" name="txtAmbito" id="txtAmbito" value="<%=controlRegistro.getAmbito()%>">
           <input type="hidden" name="txtEntidad" id="txtEntidad" value="<%=controlRegistro.getEntidad()%>">
           <input type="hidden" name="txaConcepto" id="txaConcepto" value="<%=concepto%>">
           <input type="hidden" name="txtOperacion" id="txtOperacion" value="A">
           <input type="hidden" name="txtPais" id="txtPais" value="147">
           <input type="hidden" name="lstOperaciones" id="lstOperaciones" value="<%=maestroOperaciones.getMaestro_operacion_id()+"-"+formaContable.getForma()%>">
           <input type="hidden" name="paginaRegreso" id="paginaRegreso" value="../registroContableNuevo/filtroGeneral.jsp?opcion=irCapturaForma">
           
          
           
           <input type="hidden" name="txtCatCuenta" id="txtCatCuenta" value="<%=lsCatCuenta%>">
           <input type="hidden" name="txtEjercicio" id="txtEjercicio" value="<%=lsEjercicio%>">
           <input type="hidden" name="txtClaPol" id="txtClaPol" value="1">
           <input type="hidden" name="capturaFormas" id="capturaFormas" value="captura"/>
           
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
   <% } //Fin catch
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
