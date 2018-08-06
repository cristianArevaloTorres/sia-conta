<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<%
    
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }
%>
    
<jsp:useBean id="pbCuentas" class = "sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Contabilidad - Cuentas de Mayor - Eliminar</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>
<script language="JavaScript" type="text/javascript">   
          
//=====  G A R A N T I Z A R   Q U E   L A   S E S I O N   Q U E D E   C E R R A D A =====
function regresa(){
    var x;
    x=0;
}

function SeleccionaCuenta(){
    var x=0;
}

//DE ENTRADA CARGA L MUNICIPIO EN SELECCIONE SIN NINGUN VALOR
function CargaCapaMuestra(){
    var pag='E';
    var valor1=""; 
    var valor2=""; 
    var valor3="";
    loadSource('capaMuestra',null,'c710CapaMuestra.jsp?cveGenero='+valor1+'&cveGpo='+valor2+'&cveClase='+valor3, bufferFrame1);
 }

//SE ENCARGA DE HACER EL LLAMADO A LA CAPA 
 function CapaMuestra(val1,val2,val3){  
    var pag='E';
    var valor1=val1; 
    var valor2=val2; 
    var valor3=val3;  
    
    if (valor3 != 'null')  // if (valor1!='')   
       loadSource('capaMuestra',null,'c710CapaMuestra.jsp?cveGenero='+valor1+'&cveGpo='+valor2+'&cveClase='+valor3, bufferFrame1);
    else
        CargaCapaMuestra();
 }
       
function Cuenta(valor)
{ var valores=valor;
  Filtro.txtCve.value=Filtro.lstGenero.value+Filtro.lstGrupo.value+Filtro.lstClase.value+valores;
}

function Valida(){
    ok=true;
    mess="AVISO:\n";
            
    // if (!Filtro.txtFecha01.value ==''){
    //  if (!ValidarObjeto(Filtro.txtFecha01.value,'F'))
    //     { ok=false; mess=mess+'Fecha Inicial Incorrecta: '+Filtro.txtFecha01.value+'\n';}
    //}
    if (!Filtro.txtCve.value ==''){
        if (!isInteger(Filtro.txtCve.value))
            { ok=false; mess=mess+'Número de Cuenta Incorrecta: '+Filtro.txtCve.value+'\n';}
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
       Filtro.txtCve.disabled=false;
       Filtro.submit();
    }
}


</script>
</head>

<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Cuentas de Mayor ", "[Eliminar]", "Filtro", true);</jsp:scriptlet>
<FORM Method="post" name="Filtro" action="c710EliminarResultado.jsp" >
<%
   Connection conexion=null;
 try{
    conexion=DaoFactory.getContabilidad();
       CachedRowSet crsGenero = null;      
       CachedRowSet crsGrupo = null;
       CachedRowSet crsClase = null;
       
       crsGenero =pbCuentas.selectGenero(conexion);
       crsGrupo =pbCuentas.selectGrupo(conexion);
       crsClase =pbCuentas.selectClase(conexion);
%>
 <table width="100%" border="0">
  <tr>
    <td><div align="right"></div></td>
    <td>&nbsp;</td>
 </tr>
 <tr> 
     <td width="36%"><div align="right">G&eacute;nero:</div></td>
     <td><div align="left">
      <SELECT NAME="lstGenero" class='cajaTexto' OnChange='ComponerListaSS(Filtro.lstGenero[selectedIndex].value);'>
       <option value="null" >- Seleccione -
<%  
        int contar1;
        String cat1;         
        while (crsGenero.next()){
        %>
            <OPTION VALUE="<%=crsGenero.getString(1)%>" > <%=crsGenero.getString(1)%>&nbsp;<%=crsGenero.getString(2)%></OPTION>
<%           
        }//while     
        crsGenero.close();
        crsGenero = null;
%>
            </select>
        </div></td>
    </tr>   
<tr>
  <td><div align="right">Grupo:</div></td>
  <td><div align="left">
      <SELECT NAME="lstGrupo" class='cajaTexto' OnChange='ComponerListaClase(Filtro.lstGrupo[selectedIndex].value);'>
  </select></div></td>
</tr>
<script language="JavaScript">
// Comienza rutina para listas dinámicas
function Tuplas ( camp1, camp2 )
{
        this.camp1 = camp1;
        this.camp2 = camp2;
}
var opcionesnull = new Array();
opcionesnull[0]= new Tuplas("- Seleccione -","null")
<%
  // Este código genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
  contar1=1;
  cat1="null";
    while (crsGrupo.next()){
        if (!cat1.equals(crsGrupo.getString("ID_GENERO")))
        {
            contar1=1;
            cat1=crsGrupo.getString("ID_GENERO");
%>
            var opciones<%=cat1%> = new Array();
            opciones<%=cat1%>[0]= new Tuplas("- Seleccione -","null");
<%
        } //fin if
%>
    opciones<%=cat1%>[<%=contar1%>]=new Tuplas("<%=crsGrupo.getString("ID_GRUPO")%> <%=crsGrupo.getString("DESCRIPCION")%>","<%=crsGrupo.getString("ID_GRUPO")%>");
<%
    contar1=contar1+1;
   } // fin while
    crsGrupo.close();
    crsGrupo = null;

%>
var conta;

function ComponerListaSS (arreglo) {
// Compone la lista dependiente a partir del valor de la opción escogida en la lista "padre"
BorrarListaSS();
arreglo = eval("opciones" + arreglo);
  for (conta=0; conta<arreglo.length; conta++){
  //   agrega elementos a nuestro combobox dependiente
    var optionObje = new Option( arreglo[conta].camp1, arreglo[conta].camp2 );
    Filtro.lstGrupo.options[conta] = optionObje;
  } // for
} // ComponerLista


//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaSS() {
   Filtro.lstGrupo.length=0;
   ComponerListaClase("null");
}
</script>

<tr>
  <td><div align="right">Rubro:</div></td>
  <td><div align="left">
      <SELECT NAME="lstClase" class='cajaTexto' onChange="javascript:CapaMuestra(Filtro.lstGenero.value,Filtro.lstGrupo.value,Filtro.lstClase.value);">
      </SELECT>
      
<script language="JavaScript">
// Comienza rutina para listas dinámicas
function Tuplas ( camp1, camp2 )
{
        this.camp1 = camp1;
        this.camp2 = camp2;
}
var opcionessnull = new Array();
opcionessnull[0]= new Tuplas("- Seleccione -","null")
<%
  // Este código genera un arreglo para cada proceso padre y almacena en
  //dichos arreglos cada una de los subprocesos hijos que le corresponden
 int contar2=1;
 String cat2="null";
    while (crsClase.next()){
        if (!cat2.equals(crsClase.getString("GRUPO")))
        {
            contar2=1;
            cat2=crsClase.getString("GRUPO");
%>
            var opcioness<%=cat2%> = new Array();
            opcioness<%=cat2%>[0]= new Tuplas("- Seleccione -","null");
<%
        } //fin if
%>
    opcioness<%=cat2%>[<%=contar2%>]=new Tuplas("<%=crsClase.getString("ID_CLASE")%> <%=crsClase.getString("DESCRIPCION")%>","<%=crsClase.getString("ID_CLASE")%>");
<%
    contar2=contar2+1;
   } // fin while   
    crsClase.close();
    crsClase = null;
%>

var contaR;

function ComponerListaClase(arreglos) {
//Compone la lista dependiente a partir del valor de la opcion escogida en la lista "padre"
if (arreglos != "null"){
    arreglos = Filtro.lstGenero.value+arreglos;}
BorrarListaC();
arreglos = eval("opcioness" + arreglos);
  for (contaR=0; contaR<arreglos.length; contaR++){
  //   agrega elementos a nuestro combobox dependiente
    var optionObje = new Option( arreglos[contaR].camp1, arreglos[contaR].camp2 );
    Filtro.lstClase.options[contaR] = optionObje;
  } // for
  
} // ComponerListaClase


//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaC() {
    Filtro.lstClase.length=0;
}
</script>
</div></td></tr>
</table>
<!--  Capa que devuelve las cuentas -->
 <IFrame style="display:none" name = "bufferFrame1">
 </IFrame>
 <div id = "capaMuestra">
</div>

<table width="100%" >  
  <tr>
  <td width="36%"><div align="right">Nombre:</div></td>
  <td><div align="left"><input type="text" name="txtNombre" maxlength="100" size="60"  class="cajaTexto" onChange="javascript:this.value=this.value.toUpperCase();"/>
  </div></td>
  </tr>
 
  <tr>      
      <td><div align="right">Naturaleza:</div></td>
      <td><div align="left"><input type="radio" name="btrNatura" value="D" >Deudor 
        <input type="radio" name="btrNatura" value="A" >Acreedor
        <input type="hidden" name="btrNatura" value="C" checked>
        </div></td>
    </tr>
 <!--  <tr> 
      <td><div align="right">Vigencia :</div></td>
      <td> Fecha inicial: 
          <input name="txtFecha01" type="text"  class="cajaTexto" size="15" maxlength="15" /> 
          <a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" 
          onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')">
          <img  src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""> 
        </a>Fecha final:<strong> 
        <input name="txtFecha02" type="text"  class="cajaTexto" size="15" maxlength="15" value="01/01/9999"/>         
        </strong></td>
   </tr>-->
   <tr> 
      <td><div align="right"></div></td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td><div align="right"></div></td>
      <td>&nbsp;</td>
    </tr>
    <tr> 
      <td>&nbsp;</td>
      <td>&nbsp;</td>
    </tr>
  </table> 


<script language="javascript" type="text/javascript">
//Inicializamos enviando la clave del proceso padre para este caso
    ComponerListaSS("null");
    ComponerListaClase("null");
    CargaCapaMuestra();
</script>  

<br>
<table align="center">
 <tr>
     <td align="center">
     <input type='button' name='btnAceptar' value='Aceptar' class='boton' onclick="javascript:Enviar();">
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c710Menu.jsp','');">
		 </td>
 </tr>
</table>
  <input type='hidden' name='txtOpcion' value='E'>
</form>
</BODY>
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
     } //Fin finally
%>