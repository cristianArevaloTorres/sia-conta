<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.db.dao.*,sia.db.sql.Sentencias"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  

<% 
  String pagina = "";
  String opcion = request.getParameter("opcion");
  
  if (opcion.equals("abrirCierre")) {
       pagina = "c742Control.jsp";
  }else if (opcion.equals("Cierre")){
       pagina = "c702Control.jsp";
  }
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
  String unidad = sbAutentifica.getUnidadEjecutora();
  String ambito = sbAutentifica.getAmbito();
  String entidad = sbAutentifica.getEntidad();
  request.getSession().setAttribute("controlRegistro", new ControlRegistro());
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  controlReg.inicializa(sbAutentifica, Integer.parseInt(request.getParameter("idCatalogoCuenta") == null ? "1" :request.getParameter("idCatalogoCuenta")));
  String condicionUnidad = controlReg.isUsuarioAdmin()?" where unidad_ejecutora not in('A00')":" where unidad_ejecutora='"+unidad+"'";
  String condicionAmbito = controlReg.isUsuarioAdmin() || controlReg.isUsuarioIntermedio()?"":" where p.unidad_ejecutora='"+unidad+"' and p.ambito="+ambito;
  String condicionEntidad = controlReg.isUsuarioAdmin()|| controlReg.isUsuarioIntermedio()?"":" where entidad="+entidad+"";
  
  String todasUnidades="";
  String todasAmbitos="";
  String todasEntidades="";
  Connection conexion=null;
  conexion=DaoFactory.getContabilidad(); 
  Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  Map parametros = new HashMap();
  if (controlReg.isUsuarioAdmin()){
      todasUnidades=" union select 1 indice,'0' ambito,'0'  unidadEjecutora, '- Todos -' as descripcionUnidadEjec from dual ";
      todasAmbitos="  union select 1 tipo, '0' as unidadEjecutora, '0' ambito, ' - Todos - ' descripcionAmbito from dual ";
      todasEntidades=" union select '0' unidad_ejecutora, '- Todos -' desunidad, 0 entidad, '- Todos -' descripcionEntidad, '0' ambito,'- Todos -' desc_ambito,1 sede, 1 tipo from dual   ";
  }  
%>
<jsp:useBean id="abProcesos" class="sun.jdbc.rowset.CachedRowSet" scope="page">

   <%
      abProcesos.setTableName("rh_tc_uni_ejecutoras");          
      parametros.put("condicionUnidad", condicionUnidad+todasUnidades);
      abProcesos.setCommand(sentencia.getComando("filtros.select.unidadesEjecutoras", parametros));
      abProcesos.execute(conexion);
      parametros.clear();
   %>
</jsp:useBean> 

<jsp:useBean id="abSubProceso" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abSubProceso.setTableName("RH_TC_ENTIDADES");
      parametros.put("condicionAmbito", condicionAmbito+todasAmbitos);
      abSubProceso.setCommand(sentencia.getComando("filtros.select.ambitos", parametros));
      abSubProceso.execute(conexion);    
      parametros.clear();      
   %>
</jsp:useBean>


<jsp:useBean id="abActividad" class="sun.jdbc.rowset.CachedRowSet" scope="page">
<%
      abActividad.setTableName("rh_tc_reg_entidad");                                     
      parametros.put("condicionEntidad", condicionEntidad+todasEntidades);                                   
      abActividad.setCommand(sentencia.getComando("filtros.select.entidades", parametros));
      abActividad.execute(conexion);     
      parametros.clear();
%>
</jsp:useBean>

<jsp:useBean id="abCierre" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abCierre.setTableName("RF_TC_EJERCICIOS");
      abCierre.setCommand(sentencia.getComando("filtros.select.ejercicios", parametros));
      abCierre.execute(conexion);
      parametros.clear();
   %>
</jsp:useBean> 

<jsp:useBean id="abPrograma" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abPrograma.setTableName("rf_tr_cuentas_contables");
   
      String queryPrograma="";
     if (opcion.equals("Cierre")){   
        queryPrograma=sentencia.getComando("filtros.select.programasTodos", parametros);
     }else{
        queryPrograma=sentencia.getComando("filtros.select.programas", parametros);
     }
      abPrograma.setCommand(queryPrograma);
      abPrograma.execute(conexion);
      parametros.clear();
      queryPrograma=null;
   %>
</jsp:useBean> 
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Filtro de cierre definitivo</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
<script language="JavaScript" src="../../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
<script language="JavaScript">
function inicializa(){
  ComponerLista (document.forms.Formulario.lstProceso[0].value);BorrarListaAct();
  ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);
  Formulario.lstProceso.options[0].selected=true;
  Formulario.lstSubProceso.options[1].selected=true;
  Formulario.lstActividad.options[1].selected=true;
  ComponerListaPro (document.forms.Formulario.lstCierre[0].value);
  Formulario.lstPrograma.options[1].selected=true;
  if(document.getElementById('usuarioAdmin').value == "true"){
    document.getElementById('unidad').disabled = false;
    document.getElementById('ambito').disabled = false;
    document.getElementById('entidad').disabled = false;
  }else{
    if(document.getElementById('usuarioInter').value == "true"){
      document.getElementById('unidad').disabled = true;
      document.getElementById('unidad').style.color="#C0C0C0"; 
      document.getElementById('ambito').disabled = false;
      document.getElementById('entidad').disabled = false;
    }else{
      document.getElementById('unidad').disabled = true;
      document.getElementById('unidad').style.color="#C0C0C0"; 
      document.getElementById('ambito').disabled = true;
      document.getElementById('ambito').style.color="#C0C0C0"; 
      document.getElementById('entidad').disabled = true;
      document.getElementById('entidad').style.color="#C0C0C0"; 
    }
  }
}

function seleccionados(){
 ListaSeleccion = "";
 programas = document.getElementById('lstPrograma');
 for(i=0;i<programas.length;i++){  
    //compruebo si la opción actual esta seleccionada por el usuario
    if(programas.options[i].selected == true){
      //Lo añado a un variable...
      if(ListaSeleccion == "")
        ListaSeleccion += programas.options[i].value
      else
        ListaSeleccion += ","+programas.options[i].value; 
    }
  }
  document.getElementById('programa').value = ListaSeleccion;
}

function cargarDatos(){
  seleccionados();
  loadSource('controlRegistro',null,'../../registroContable/controlRegistro.jsp?','unidad='+document.getElementById('unidad').value+'&ambito='+document.getElementById('ambito').value+'&entidad='+document.getElementById('entidad').value+'&ejercicio='+document.getElementById('ejercicio').value+'&idCatalogoCuenta='+document.getElementById('idCatalogoCuenta').value);
}

function enviar(){
  var ambito=document.getElementById('ambito').value.substring(document.getElementById('ambito').value.length-1, document.getElementById('ambito').value.length);
  var mensajeambito='';
  
  if ((Formulario.lstPrograma.value=='0')&&(Formulario.lstProceso.value!='0')){
      alert("Es necesario que selecione el Programa a Cerrar");
  } else {
  switch (ambito) { 
    case '0':
        if (document.getElementById('hopcion').value=="abrirCierre")
           mensajeambito=' para el ámbito 0, él cual abre todas las unidades, incluyendo ámbito central, regional y estatal ';
        else       
          mensajeambito=' para el ámbito 0 - Todos, él cual cerrará todas las unidades, incluyendo ámbito central, regional y estatal ';  
      break;
    case '2':
        if (document.getElementById('hopcion').value=="abrirCierre")
           mensajeambito=' para el ámbito 2, él cual abre los ámbitos regional y estatal de la unidad seleccionada ';
        else
           mensajeambito=' para el ámbito 2 - Regional,  él cual cerrará los ámbitos regional y estatal de la unidad seleccionada '; 
      break;
    case '1':
        if (document.getElementById('hopcion').value=="abrirCierre")
           mensajeambito=' para el ámbito 1, él cual abre  sólo la unidad, ámbito y entidad seleccionada';
        else
           mensajeambito=' para el ámbito 1, él cual cerrará  sólo la unidad, ámbito y entidad seleccionada';
      break;
    case '3':
        if (document.getElementById('hopcion').value=="abrirCierre")
           mensajeambito=' para el ámbito 3, él cual abre  sólo la unidad, estatal y entidad seleccionada';
        else
           mensajeambito=' para el ámbito 3, él cual cerrará  sólo la unidad, estatal y entidad seleccionada';
      break;      
  }

  var menConfirma='';
  if (document.getElementById('hopcion').value=="abrirCierre")
      menConfirma='¿Está seguro de Abrir el Cierre Definitivo de ';
  else
      menConfirma='¿Está seguro de realizar el Cierre Definitivo de ';
  
   var confirma= confirm(menConfirma+form.lstMes.options[form.lstMes.selectedIndex].text+' del ejercicio '+
       form.lstCierre.value+mensajeambito+' del programa '+document.getElementById('lstPrograma').value+'?');
   if (confirma){
      Formulario.Aceptar.disabled=true; 
      Formulario.lstProceso.disabled=false;
      Formulario.lstSubProceso.disabled=false;
      Formulario.lstActividad.disabled=false;      
      document.getElementById('form').submit();
   }
  }
  }
</script>
</head>
<body  onLoad=inicializa();>
<jsp:scriptlet>util.tituloPagina(out,"Contabilidad"," ","Filtro Cierre Definitivo","Filtrar",true);</jsp:scriptlet>      
<br><br>
      <jsp:directive.include file="../encabezadoFechaActual.jspf"/>
<br><br>
<FORM id="form"  Method="post" action="<%=pagina%>" name="Formulario" >
<%
   String numEmpleado=Integer.toString(sbAutentifica.getNumeroEmpleado());
   String resultado="";
   //Connection conexion=null;
   try{   
%>


<table align="center" cellpadding="5">
 <tr><td class='negrita'>Unidad Ejecutora:</td>
  <td>
      <!-- Lista de unidad ejecutora-->
      <SELECT id='unidad' class= 'cajaTexto' NAME="lstProceso" class='lst' OnChange='ComponerLista (document.forms.Formulario.lstProceso[selectedIndex].value);BorrarListaAct();Formulario.lstSubProceso.options[1].selected=true;ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);Formulario.lstActividad.options[1].selected=true;seleccionaPrograma();'>
         <%
          //Este código genera la lista padre dinamicamente
          int cuenta;
          String cat;
          //cuenta=1;
          try{
          abProcesos.beforeFirst(); 
          while (abProcesos.next()){
          %>
            <OPTION VALUE="<%=abProcesos.getString("unidadEjecutora")%>"><%=abProcesos.getString("unidadEjecutora")+" "+abProcesos.getString("descripcionUnidadEjec")%></OPTION>
          <%
          // cuenta=cuenta+1;
         } //Fin while
         }catch (Exception e) {
           System.out.println("Error en unidad ejecutora: "+ e.getMessage());
         }         
         
         %>
      </SELECT>
  </td>
</tr>  
<tr>
<td class='negrita'>Ambito:</td>
  <td>
     <!--Lista Ambito-->
     <SELECT id='ambito' class="cajaTexto" NAME="lstSubProceso" class='lst' onchange="ComponerListaAct(document.forms.Formulario.lstSubProceso[selectedIndex].value);Formulario.lstActividad.options[1].selected=true;">
     </SELECT>

<script language="JavaScript">
// Comienza rutina para listas dinamicas
function Tupla ( campo1, campo2 )
{
        this.campo1 = campo1;
        this.campo2 = campo2;
}
var opcionesnull = new Array();
//opcionesnull[0]= new Tupla("- Seleccione -","null");

<%
  // Este cÓdigo genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
  cuenta=1;
  cat="null";
  try{
  abSubProceso.beforeFirst();
  while (abSubProceso.next()){
    if (!cat.equals(abSubProceso.getString("unidadEjecutora")))
    {
      cuenta=1;
      cat=abSubProceso.getString("unidadEjecutora");
%>
      var opciones<%=cat%> = new Array();
      //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
<%
     } //fin if
%>
  opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=abSubProceso.getString("ambito")+" "+abSubProceso.getString("descripcionAmbito")%>","<%=abSubProceso.getString("unidadEjecutora")+abSubProceso.getString("ambito")%>");
<%
  cuenta=cuenta+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en ambito: "+ e.getMessage());
  }         
         
%>
var contador;

function ComponerLista ( array ) {
// Compone la lista dependiente a partir
// del valor de la opcion escogida en la lista "padre"

BorrarListaSub();
  array = eval("opciones" + array);

  for (contador=1; contador<array.length; contador++)
  {
    //Añade elementos a nuestro combobox dependiente
    var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
    Formulario.lstSubProceso.options[contador] = optionObj;
  } // for
 
} // ComponerLista
//Formulario.lstSubProceso.options[0].selected=true;
 
 function seleccionaPrograma(){
   if (document.getElementById('hopcion').value=="Cierre"){ 
     if (Formulario.lstProceso.options[Formulario.lstProceso.selectedIndex].value=='0'){  
       Formulario.lstPrograma.options[1].selected = true; 
     }else{
       Formulario.lstPrograma.options[2].selected = true; 
      }    
   } 
 }
//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaSub() {
    Formulario.lstSubProceso.length=0;
}

//Inicializamos enviando la clave del proceso padre para este caso
ComponerLista ("null");
</script>
</td></tr>

<tr>
  <td class='negrita'>Entidad:</td>
  <td>
     <!-- Lista Entidad-->
     <SELECT id='entidad' class="cajaTexto" NAME="lstActividad" class='lst'>
     </SELECT>

<script language="JavaScript">
// Comienza rutina para listas dinámicas
function Tupla ( campoact1, campoact2 )
{
        this.campoact1 = campoact1;
        this.campoact2 = campoact2;
}
var opcionactnull = new Array();
//opcionactnull[0]= new Tupla("- Seleccione -","null");

<%
  // Este código genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
  int cuentaact;
  String catact;
  cuentaact=1;
  catact="null";
  try{
  abActividad.beforeFirst();
  while (abActividad.next()){
    if (!catact.equals(abActividad.getString("unidadEjecutora")+abActividad.getString("ambito")))
    {
      cuentaact=1;
      catact=abActividad.getString("unidadEjecutora")+abActividad.getString("ambito");
%>
      var opcionact<%=catact%> = new Array();
      //opcionact<%=catact%>[0]= new Tupla("- Seleccione -","null");
<%
     } //fin if
%>
  opcionact<%=catact%>[<%=cuentaact%>]=new Tupla("<%=abActividad.getString("entidad")+" "+abActividad.getString("descripcionEntidad")%>","<%=abActividad.getString("unidadEjecutora")+abActividad.getString("entidad")%>");
<%
  cuentaact=cuentaact+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en entidad: "+ e.getMessage());
  }         
           
%>
var contador;

function ComponerListaAct ( arrayact ) {
// Compone la lista dependiente a partir
// del valor de la opción escogida en la lista "padre"

BorrarListaAct();
  arrayact = eval("opcionact" + arrayact);

  for (contador=1; contador<arrayact.length; contador++)
  {
    //Añade elementos a nuestro combobox dependiente
    var optionObj = new Option( arrayact[contador].campoact1, arrayact[contador].campoact2 );
    Formulario.lstActividad.options[contador] = optionObj;
  } // for
} // ComponerLista

//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaAct() {
    Formulario.lstActividad.length=0;
}
//Inicializamos enviando la cve del proceso padre para este caso
ComponerListaAct ("null");
</script>

</td></tr>
 <tr><td class='negrita'>Ejercicio:</td>
  <td>
      <SELECT id="ejercicio" class="cajaTexto" NAME="lstCierre" class='lst' onchange="ComponerListaPro (document.forms.Formulario.lstCierre[selectedIndex].value);Formulario.lstPrograma.options[1].selected=true;" >
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
<td class='negrita'>Programa:</td>
  <td>
     <SELECT id="lstPrograma" class="cajaTexto" NAME="lstPrograma" class="lst">
     </SELECT>

<script language="JavaScript">
// Comienza rutina para listas dinamicas
function Tupla ( campo1, campo2 )
{
        this.campo1 = campo1;
        this.campo2 = campo2;
}
var opcionesnull = new Array();
//opcionesnull[0]= new Tupla("- Seleccione -","null");

<%
  // Este cÓdigo genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
  cuenta=1;
  cat="null";
  try{
  abPrograma.beforeFirst();
  while (abPrograma.next()){
    if (!cat.equals(abPrograma.getString("periodo")))
    {
      cuenta=1;
      cat=abPrograma.getString("periodo");
%>
      var opciones<%=cat%> = new Array();
      //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
<%
     } //fin if
%>
  opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=abPrograma.getString("programa")+" "+abPrograma.getString("descripcion")%>","<%=abPrograma.getString("programa")%>");
<%
  cuenta=cuenta+1;
  } // fin while
  }catch (Exception e) {
      System.out.println("Error en programa: "+ e.getMessage());
  }         
         
%>
var contador;

function ComponerListaPro ( array ) {
// Compone la lista dependiente a partir
// del valor de la opcion escogida en la lista "padre"

BorrarListaPro();
  array = eval("opciones" + array);

  for (contador=1; contador<array.length; contador++)
  {
    //Añade elementos a nuestro combobox dependiente
    var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
    Formulario.lstPrograma.options[contador] = optionObj;
  } // for
} // ComponerLista
//Formulario.lstSubProceso.options[0].selected=true;
 
//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaPro() {
    Formulario.lstPrograma.length=0;
}

//Inicializamos enviando la clave del proceso padre para este caso
ComponerListaPro ("null");
</script>
</td></tr>
<tr>
<td class='negrita'>Mes:</td>
  <td>
     <SELECT id="lstMes" class="cajaTexto" NAME="lstMes" class="lst">
         <option  SELECTED Value="01">ENERO</option>
         <option Value="02">FEBRERO</option>
         <option Value="03">MARZO</option>
         <option Value="04">ABRIL</option>
         <option Value="05">MAYO</option>
         <option Value="06">JUNIO</option>
         <option Value="07">JULIO</option>
         <option Value="08">AGOSTO</option>
         <option Value="09">SEPTIEMBRE</option>
         <option Value="10">OCTUBRE</option>
         <option Value="11">NOVIEMBRE</option>
         <option Value="12">DICIEMBRE</option>
     </SELECT>
  </td>   
</tr>
</table>



<%
     System.out.println(resultado);
  }
  catch(Exception E){
    System.out.println(resultado);
    conexion.rollback(); 
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p><%=E.getMessage()%></p>
     <p>verifique la informacion del archivo de carga, </p>
     <p>si el problema persiste favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }  
     sentencia=null;
     parametros=null;
    }      
%>
<input type="hidden" name="programa" id="programa" value="">
<input type="hidden" name="hopcion" id="hopcion" value="<%=request.getParameter("opcion")%>">
<input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=request.getParameter("idCatalogoCuenta")%>">
<input type="hidden" name="usuarioAdmin" id="usuarioAdmin" value="<%=controlReg.isUsuarioAdmin()%>">
<input type="hidden" name="usuarioInter" id="usuarioInter" value="<%=controlReg.isUsuarioIntermedio()%>">
<input type="hidden" name="usuarioBajo" id="usuarioBajo" value="<%=controlReg.isUsuarioBajo()%>">
    <IFrame style="display:none" name = "bufferCurp" id='bufferCurp'>
    </IFrame>
  <div id='controlRegistro'></div>
  <br>
  <hr noshade="noshade" width="40%" align="center" style="border-width: 1px;border-style:solid">
  <br>
<%
   if (opcion.equals("abrirCierre")) {
%>
  <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center">
      <td>
        <input type="button" name="Aceptar" class="boton" value="Aceptar" onclick='Formulario.action="c742Control.jsp";cargarDatos()'/>
      </td>
    </tr>
  </table>
<%
  }else if (opcion.equals("Cierre")){
%>
    <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center">
      <td>
        <input type="button" name="Aceptar" class="boton" value="Aplicar Cierre" onclick='Formulario.action="c702Control.jsp";cargarDatos();'/>
      </td>
            <td>
        <input type="button" name="Regresar" class="boton" value="Consultar Cierres" onclick='Formulario.action="c702ConsultarResultado.jsp";Formulario.Regresar.disabled=true;Formulario.submit();'/>
      </td>
    </tr>
  </table>
<%  
  }
%>  

  
  
  
  

</form>  
  </body>
</html>