<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.db.dao.*,sia.db.sql.Sentencias"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  

<%

    HttpSession sesionActual = request.getSession();
    if (sesionActual.isNew() || sesionActual == null) {
        String paginaCerrarSesion = "../../Librerias/Funciones/CerrarSesion.jsp";
        response.sendRedirect(response.encodeRedirectURL(paginaCerrarSesion));
        return;
    }


  Sentencias sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  Map parametros = new HashMap();
  String opcion = request.getParameter("opcion");
  String pagina = null;
  if(opcion.equals("conciliacion")){
    pagina = "../registroContableNuevo/conciliacion/c726FiltroReporte.jsp";
  }
  else {
    if(opcion.equals("edosFinancieros"))
      pagina = "../registroContableNuevo/estadosFinancieros/c731Filtro.jsp";
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
%>

<jsp:useBean id="abProcesos" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abProcesos.setTableName("SIA_CATALOGOS.TC_UNI_EJECUTORAS");
      parametros.put("condicionUnidad", condicionUnidad);
      abProcesos.setCommand(sentencia.getComando("filtros.select.unidadesEjecutoras", parametros));
      abProcesos.execute(conexion);
      conexion.close();
      conexion=null;
      parametros.clear();
   %>
</jsp:useBean> 
<jsp:useBean id="abSubProceso" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abSubProceso.setTableName("GTCMANOBRCOT");
      parametros.put("condicionAmbito", condicionAmbito);
      abSubProceso.setCommand(sentencia.getComando("filtros.select.ambitos", parametros));
      abSubProceso.execute(conexion);
      conexion.close();
      conexion=null;   
      parametros.clear();        
   %>
</jsp:useBean>


<jsp:useBean id="abActividad" class="sun.jdbc.rowset.CachedRowSet" scope="page">
<%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abActividad.setTableName("sia_admin.rh_tc_reg_entidad");   
      parametros.put("condicionEntidad", condicionEntidad);                                   
      abActividad.setCommand(sentencia.getComando("filtros.select.entidades", parametros));
      abActividad.execute(conexion);
      conexion.close();
      conexion=null;       
%>
</jsp:useBean>

<jsp:useBean id="abCierre" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion = DaoFactory.getContabilidad();
      abCierre.setTableName("SIA_CATALOGOS.TC_UNI_EJECUTORAS");
      abCierre.setCommand(sentencia.getComando("filtros.select.ejercicios", parametros));
      abCierre.execute(conexion);
      conexion.close();
      conexion=null;
   %>
</jsp:useBean> 

<jsp:useBean id="abPrograma" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      Connection conexion=null;
      conexion=DaoFactory.getContabilidad();
      abPrograma.setTableName("rf_tr_cuentas_contables");
      abPrograma.setCommand(sentencia.getComando("filtros.select.programas", parametros));
      abPrograma.execute(conexion);
      conexion.close();
      conexion=null;
   %>
</jsp:useBean> 

<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Filtro general.</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
<script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
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
  loadSource('controlRegistro',null,'controlRegistro.jsp?','unidad='+document.getElementById('unidad').value+'&ambito='+document.getElementById('ambito').value+'&entidad='+document.getElementById('entidad').value+'&ejercicio='+document.getElementById('ejercicio').value+'&idCatalogoCuenta='+document.getElementById('idCatalogoCuenta').value);
}

function enviar(){
   document.getElementById('form').submit();
}
</script>
</head>
<body  onLoad=inicializa();>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro", "Filtrar", true);</jsp:scriptlet>      
<FORM id="form"  Method="post" action="<%=pagina%>" name="Formulario" >
<%
   String numEmpleado=Integer.toString(sbAutentifica.getNumeroEmpleado());
   String resultado="";
   Connection conexion=null;
   try{   
%>


<table align="center" cellpadding="5">
 <tr><td class='negrita'>Unidad Ejecutora:</td>
  <td>
      <!-- Lista de unidad ejecutora-->
      <SELECT id='unidad' class= 'cajaTexto' NAME="lstProceso" class='lst' OnChange='ComponerLista (document.forms.Formulario.lstProceso[selectedIndex].value);BorrarListaAct();Formulario.lstSubProceso.options[1].selected=true;ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);Formulario.lstActividad.options[1].selected=true;'>
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
     <SELECT id="lstPrograma" class="cajaTexto" NAME="lstPrograma" class="lst" multiple="multiple">
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
     <table width='100%' align="right">
       <tr  align="right">
         <td width='10%'>
            <br/>
            <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../registroContable/filtroGenerarRegistroContableRep.jspx?pagina=cargarPolizas&idCatalogoCuenta=1','_self');" >
         </td></tr>
     </table>
<%
   }
   finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }  
    }      
%>
<input type="hidden" name="programa" id="programa" value="">
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
  <table cellpadding="5"  border="0" width="15%" class="sianoborder" align="center">
    <tr align="center">
      <td>
        <input type="button" class="boton" value="Aceptar" onclick="cargarDatos()"/>
      </td>
    </tr>
  </table>
  </form>  
  </body>
</html>