<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.db.dao.*,sia.db.sql.Sentencias"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<jsp:useBean id="ueEmpleado" class="sia.rf.contabilidad.registroContableNuevo.bcUnidadesEjec" scope="page"/>


<%
  String opcion = request.getParameter("opcion");
  String pagina = null;
  String numEmpleado="-1";
  String resultado="";
  Connection conexion=null;
  String unidad = null;
  String ambito = null;
  String entidad = null;
  Sentencias sentencia = null;
  ControlRegistro controlReg = null;
  String condicionUnidad = null;
  String condicionAmbito = null;
  String condicionEntidad = null;
  try{  
  if(opcion != null){
    if(opcion.equals("cuenta")){
      pagina = "catalogoCuentas/c729FiltroCuenta.jsp";
    }
    else if (opcion.equals("rangos")){
        pagina = "catalogoCuentas/c730FiltroRangos.jsp";
    } else if (opcion.equals("validacion")){
        pagina = "validacion/c741FiltroReporte.jsp";
    } else if(opcion.equals("eliminarCarga")){
      pagina = "cheques/eliminacion/743Filtro.jsp";
    } else if(opcion.equals("chequeNominativo")){
      pagina = "cheques/nominativos/c746Filtro.jsp?opcion=chequeNominativo";
    }else if (opcion.equals("reexpedir")){
        pagina = "polizas/reexpedir/c748Filtro.jsp?opcion=reexpedir";
    }else if (opcion.equals("cancelar")){
        pagina = "polizas/reexpedir/c748Filtro.jsp?opcion=cancelar";
    }else if(opcion.equals("aplicaCheques")){
       pagina = "c705Filtro.jsp";
    }else if(opcion.equals("imprimeCheques")){
       pagina = "c706Filtro.jsp";
    }else if(opcion.equals("reactivaCheques")){
       pagina = "c707Filtro.jsp";
    }else if(opcion.equals("reactivaCheques")){
       pagina = "c707Filtro.jsp";
    }else if(opcion.equals("reporte")){
       pagina = "c706FiltroReporte.jsp";
    }else if(opcion.equals("folioFinal")){
       pagina = "cheques/folioFinal/c735Resultado.jsp";
    }else if(opcion.equals("consecutivo")){
       pagina = "cheques/consecutivo/c737Resultado.jsp";
    }else if(opcion.equals("ReporteNoPolizas")){
       pagina = "polizas/c736Resultado.jsp";
    }else if(opcion.equals("formasManualesNew")){
       pagina = "formas/c713FormasIndex.jsp?opcion=formasManualesNew";
    }else if(opcion.equals("formasContablesNew")){
       pagina = "formas/c713FormasIndex.jsp?opcion=formasContablesNew";
    }else if(opcion.equals("operacionesTipoNew")){
       pagina = "formas/c714OperacionesTipoIndex.jsp?opcion=operacionesTipoNew";
    }else if(opcion.equals("catalogoCuentas")){
       pagina = "catalogoCuentas/c712ActualizarFormulario.jsp";
    }else if(opcion.equals("cargarPolizas")){
       pagina = "polizas/carga/c721ArchivoPoliza.jsp";
    }else if(opcion.equals("cargaCheques")){
       pagina = "cheques/carga/c704ArchivoCheque.jsp";
    }else if(opcion.equals("aplicarCarga")){
       pagina = "cargaDBF/c722Filtro.jsp";
    }else if(opcion.equals("eliminarCargaPolizas")){
       pagina = "cargaDBF/c725Filtro.jsp";
    }else if(opcion.equals("irCapturaForma")){
       pagina = "formas/c719FormasFiltro.jsp";
    }else if(opcion.equals("irModificaPolizaForma")){
       pagina = "formas/c723FiltroModificar.jsp";
    }else if(opcion.equals("irOpcionesPolizas")){
       pagina = "c700OpcionesPolizas.jsp";
    }else if(opcion.equals("irConsultarPolizas")){
       pagina = "c700ConsultarFiltro.jsp";//Falta migrar esta pagina
    }else if(opcion.equals("")){
       pagina = "";//Falta migrar esta pagina
    }
  }
  //System.out.println("filtroGeneral.jsp Autentifica: "+sbAutentifica);
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
  //System.out.println("Cuenta de usuario: "+sbAutentifica.getCuenta());
  numEmpleado=Integer.toString(sbAutentifica.getNumeroEmpleado());
  unidad = "='"+sbAutentifica.getUnidadEjecutora().concat("'");
  ambito = sbAutentifica.getAmbito();
  entidad = sbAutentifica.getEntidad();
  sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
  controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  if(controlReg == null){
    request.getSession().setAttribute("controlRegistro", new ControlRegistro());
    controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  }
  //controlReg.setEjercicio(Integer.valueOf(fechaAfectacion.obtenerFechaAfectacion().substring(0,4)));
  controlReg.inicializa(sbAutentifica, request.getParameter("idCatalogoCuenta") == null ? controlReg.getIdCatalogoCuenta():Integer.valueOf(request.getParameter("idCatalogoCuenta")));
  System.out.println("filtroGeneral.jsp controlRegisgtro.unidad: "+controlReg.getUnidad()+", entidad: "+controlReg.getEntidad()+", ambito: "+controlReg.getAmbito());
  conexion=DaoFactory.getContabilidad();
  ueEmpleado.select_UnidadesEjecporEmpleado(conexion,sbAutentifica.getNumeroEmpleado());  
  String  unidades[] = null;
  if(ueEmpleado.getUnidadEjecutora()!= null){
    controlReg.setAmbito(1);
    controlReg.isUsuarioIntermedio();
    //controlReg.obtenerTipoUsuario("",1);
    unidades=(ueEmpleado.getUnidadEjecutora()).split(",");
    unidad=" in (";
    for(int i=0; i<unidades.length; i++){
          unidad += "'"+unidades[i]+"',";
        }
    unidad= unidad.substring(0,unidad.length()-1).concat(")");
  }
  condicionUnidad = controlReg.isUsuarioAdmin() && ueEmpleado.getUnidadEjecutora()== null?" where unidad_ejecutora not in('A00')":" where unidad_ejecutora"+unidad;
  condicionAmbito = controlReg.isUsuarioAdmin() || controlReg.isUsuarioIntermedio()?"":" where p.unidad_ejecutora"+unidad+" and p.ambito="+ambito;
  condicionEntidad = controlReg.isUsuarioAdmin()|| controlReg.isUsuarioIntermedio()?"":" where entidad="+entidad+"";
%>
<jsp:useBean id="abProcesos" class="sun.jdbc.rowset.CachedRowSet" scope="page">

   <%
      abProcesos.setTableName("SIA_CATALOGOS.TC_UNI_EJECUTORAS");
      abProcesos.setCommand("select 1 indice, ambito, unidad_ejecutora as unidadEjecutora, siafm.primeramay(descripcion) descripcionUnidadEjec  "+
                            " from SIA_CATALOGOS.TC_UNI_EJECUTORAS "+ condicionUnidad +
                            " order by indice, ambito,unidad_ejecutora ");
      abProcesos.execute(conexion);
   %>
</jsp:useBean> 

<jsp:useBean id="abSubProceso" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abSubProceso.setTableName("GTCMANOBRCOT");
      abSubProceso.setCommand("select 1 tipo, p.unidad_ejecutora as unidadEjecutora, p.ambito, a.descripcion as descripcionAmbito from  "+
                            " ( "+
                            "   select u.unidad_ejecutora, 2 ambito "+
                            "   from sia_admin.rh_tc_reg_entidad ue "+
                            "   inner join sia_admin.rh_tc_coord_estatales e on e.coord_estatal=ue.entidad "+
                            "   inner join SIA_CATALOGOS.TC_UNI_EJECUTORAS u on u.unidad_ejecutora=ue.unidad_ejecutora"+
                            "   and e.pais=147 "+
                            "   and u.ambito=2 and ue.sede=1"+
                            "   union "+
                            "   select u.unidad_ejecutora, decode(u.ambito,2,decode(ue.sede,0,3,1,3),1) ambito "+
                            "   from sia_admin.rh_tc_reg_entidad ue "+
                            "   inner join sia_admin.rh_tc_coord_estatales e on e.coord_estatal=ue.entidad "+
                            "   inner join SIA_CATALOGOS.TC_UNI_EJECUTORAS u on u.unidad_ejecutora=ue.unidad_ejecutora "+
                            "   and e.pais=147 "+
                            "   union "+
                            "   select '109' unidad_ejecutora, 3 ambito from dual "+
                            "   union "+
                            "   select '107' unidad_ejecutora, 2 ambito from dual"+
                            " ) p "+
                            " inner join sia_admin.rh_tc_ambitos a on a.ambito=p.ambito "+ condicionAmbito +
                            " order by 1 " );
      abSubProceso.execute(conexion);   
   %>
</jsp:useBean>


<jsp:useBean id="abActividad" class="sun.jdbc.rowset.CachedRowSet" scope="page">
<%
      abActividad.setTableName("sia_admin.rh_tc_reg_entidad");                 
      abActividad.setCommand(" select unidad_ejecutora unidadEjecutora, desunidad, entidad, descripcion descripcionEntidad, ambito, desc_ambito,sede,pais, tipo from "+
      " ( "+
      " select "+
      " u.unidad_ejecutora, siafm.primeramay(u.descripcion) desunidad, e.coord_estatal entidad, e.descripcion, 2 ambito, 'Regional' desc_ambito,ue.sede,e.pais, 1 tipo "+
      " from "+
      " sia_admin.rh_tc_reg_entidad ue "+
      " inner join sia_admin.rh_tc_coord_estatales e on e.coord_estatal=ue.entidad  "+
      " inner join SIA_CATALOGOS.TC_UNI_EJECUTORAS u on u.unidad_ejecutora=ue.unidad_ejecutora  "+
      " and e.pais=147 "+
      " and u.ambito=2 and ue.sede=1 "+
      " union "+
      " select "+
      " u.unidad_ejecutora, siafm.primeramay(u.descripcion) desunidad, e.coord_estatal entidad, e.descripcion, decode(u.ambito,2,decode(ue.sede,0,3,1,3),1) ambito, decode(u.ambito,2,decode(ue.sede,0,'Estatal',1,'Estatal'),'Central') desc_ambito,ue.sede,e.pais, 1 tipo "+
      " from "+
      " sia_admin.rh_tc_reg_entidad ue "+
      " inner join sia_admin.rh_tc_coord_estatales e on e.coord_estatal=ue.entidad  "+
      " inner join SIA_CATALOGOS.TC_UNI_EJECUTORAS u on u.unidad_ejecutora=ue.unidad_ejecutora  "+
      " and e.pais=147 "+
      " union "+
      " select "+
      " '109' unidad_ejecutora, 'Direccion general de administracion' desunidad, 11 entidad,'Guanajuato' descripcion, 3 ambito, 'Estatal' desc_ambito, 0 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " union "+
      " select "+
      " '129' unidad_ejecutora, 'Direccion regional centro sur' desunidad, 35 entidad,'DF Norte' descripcion, 3 ambito, 'Estatal' desc_ambito, 0 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " union "+
      " select "+
      " '129' unidad_ejecutora, 'Direccion regional centro sur' desunidad, 36 entidad,'DF Sur' descripcion, 3 ambito, 'Estatal' desc_ambito, 0 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " union "+
      " select "+
      " '130' unidad_ejecutora, 'Direccion regional centro' desunidad, 33 entidad,'Mexico Oriente' descripcion, 3 ambito, 'Estatal' desc_ambito, 0 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " union "+
      " select "+
      " '130' unidad_ejecutora, 'Direccion regional centro sur' desunidad, 34 entidad,'Mexico Poniente' descripcion, 3 ambito, 'Estatal' desc_ambito, 0 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " union "+
      " select "+
      " '107' unidad_ejecutora, 'Direccion general de geografia y medio ambiente' desunidad, 1 entidad, 'Aguascalientes' descripcion, 2 ambito, 'Regional' desc_ambito, 1 sede, '147' pais, 1 tipo "+
      " from "+
      " dual "+
      " ) "+ condicionEntidad +
      " order by unidadEjecutora,ambito,entidad " );                                    
      abActividad.execute(conexion);      
%>
</jsp:useBean>

<jsp:useBean id="abCierre" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abCierre.setTableName("RF_TC_EJERCICIOS");
      abCierre.setCommand(sentencia.getComando("catalogos.select.EjercicioHB", ""));
      abCierre.execute(conexion);
      sentencia=null;
   %>
</jsp:useBean> 

<jsp:useBean id="abPrograma" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <%
      abPrograma.setTableName("rf_tr_cuentas_contables");
      abPrograma.setCommand(" select rownum llave, substr(a.cuenta_contable,6,4) programa,upper(a.descripcion) as descripcion, a.id_catalogo_cuenta,to_char(a.fecha_vig_ini,'yyyy') as periodo    " +
                         " from rf_tr_cuentas_contables a,  "+
                         " (select min(t.cuenta_contable_id) cuenta_contable_id,substr(t.cuenta_contable,6,4),t.id_catalogo_cuenta, to_char(t.fecha_vig_ini,'yyyy') fecha_vig_ini  from rf_tr_cuentas_contables t  "+
                         " where t.nivel='2' group by substr(t.cuenta_contable,6,4),t.id_catalogo_cuenta, to_char(t.fecha_vig_ini,'yyyy')) b  "+                         
                         " where a.cuenta_contable_id=b.cuenta_contable_id and a.id_catalogo_cuenta=1  "+
                         " order by a.id_catalogo_cuenta,to_char(a.fecha_vig_ini,'yyyy'),substr(a.cuenta_contable,6,4)  ");
      abPrograma.execute(conexion);
   %>
</jsp:useBean> 


     
      



<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>filtroGeneral</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
<script language="JavaScript" src="../../Librerias/Javascript/refrescarCapa.js" type="text/javascript"></script>
<script language="JavaScript">
function inicializa(){
  ComponerLista (document.forms.Formulario.lstProceso[0].value);BorrarListaAct();
  ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);
  Formulario.lstProceso.options[0].selected=true;
  Formulario.lstSubProceso.options[1].selected=true;
  Formulario.lstActividad.options[1].selected=true;
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

function cargarDatos(){
  loadSource('controlRegistro',null,'../registroContable/controlRegistro.jsp?','unidad='+document.getElementById('unidad').value+
                                                                               '&ambito='+document.getElementById('ambito').value+
                                                                               '&entidad='+document.getElementById('entidad').value+
                                                                               '&ejercicio='+document.getElementById('ejercicio').value+
                                                                               '&idCatalogoCuenta='+document.getElementById('idCatalogoCuenta').value+
                                                                               '&opcion='+document.getElementById('opcion').value);
}

function enviar(){
   //document.getElementById('descEntidad').value = document.getElementById('lstActividad').options[document.getElementById('lstActividad').selectedIndex].text;
   document.getElementById('form').submit();
}
</script>
</head>
<body  onLoad=inicializa();>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro", "Filtrar", true);</jsp:scriptlet>      
<FORM id="form"  Method="post" action="<%=pagina%>" name="Formulario" >



<table align="center" cellpadding="5">
 <tr><td class='negrita'>Unidad Ejecutora:</td>
  <td>
      <!-- Lista de unidad ejecutora-->
      <SELECT id='unidad' class= 'cajaTexto' NAME="lstProceso" OnChange='ComponerLista (document.forms.Formulario.lstProceso[selectedIndex].value);BorrarListaAct();Formulario.lstSubProceso.options[1].selected=true;ComponerListaAct (document.forms.Formulario.lstSubProceso[1].value);Formulario.lstActividad.options[1].selected=true;'>
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
     <SELECT id='ambito' class="cajaTexto" NAME="lstSubProceso" onchange="ComponerListaAct(document.forms.Formulario.lstSubProceso[selectedIndex].value);Formulario.lstActividad.options[1].selected=true;">
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
     <SELECT id='entidad' class="cajaTexto" NAME="lstActividad">
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
      <SELECT id="ejercicio" class="cajaTexto" NAME="lstCierre">
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
</table>



<%
     //System.out.println(resultado);
  }
  catch(Exception E){
    System.out.println(resultado);
    if(sbAutentifica != null){
      sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
      sbAutentifica.enviaCorreo();
      System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
    }
    E.printStackTrace();
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
<input type="hidden" name="descEntidad" id="descEntidad" value="">
<input type="hidden" name="idCatalogoCuenta" id="idCatalogoCuenta" value="<%=controlReg.getIdCatalogoCuenta()%>">
<input type="hidden" name="opcion" id="opcion" value="<%=request.getParameter("opcion")%>">
<%if(ueEmpleado.getUnidadEjecutora()!= null)%>
  <input type="hidden" name="usuarioAdmin" id="usuarioAdmin" value="true">
<%else%>
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
        <input type="button" class="boton" id="btnAceptar" value="Aceptar" onclick="document.getElementById('btnAceptar').disabled='true';cargarDatos()"/>
      </td>
    </tr>
  </table>
  </form>  
  </body>
</html>