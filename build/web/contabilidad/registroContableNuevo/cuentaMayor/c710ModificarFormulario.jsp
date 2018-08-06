<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbCuentas" class="sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Contabilidad - Cuentas de Mayor - Modificar</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css"
          type='text/css'></link>
    <script language="JavaScript"
            src="../../../Librerias/Javascript/Capturador/funciones.js"
            type="text/javascript"></script>
    <script language="JavaScript"
            src="../../../Librerias/Javascript/Capturador/fcgencValida2.js"
            type="text/javascript"></script>
    <script language="JavaScript"
            src="../../../Librerias/Javascript/Capturador/calendar.js"
            type="text/javascript"></script>
    <script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js"
            type="text/javascript"></script>
    <script src="../../../Librerias/Javascript/Capturador/almacen.js"
            type="text/javascript"></script>
    <script type="text/javascript"
            src="../../../Librerias/Javascript/jquery.js"></script>
    <script type="text/javascript"
            src="../../../Librerias/Javascript/jquery.meio.mask.js"
            charset="utf-8"></script>
    <script language="JavaScript" type="text/javascript">
<%
   Connection conexion=null;
   String ImprimeFecha="";
 try{
    conexion=DaoFactory.getContabilidad();
       
       CachedRowSet crsGenero = null;      
       CachedRowSet crsGrupo = null;
       CachedRowSet crsClase = null;
       
       crsGenero=pbCuentas.selectGenero(conexion);
       crsGrupo =pbCuentas.selectGrupo(conexion);
       //crsClase =pbCuentas.selectClase(conexion);
       
       String lsCveCta = request.getParameter("cveCta");         
       
       pbCuentas.selectCuenta(conexion,lsCveCta);     
       
       String lsConsec= lsCveCta.substring(3,4);   
       
       String lsCuentaID=pbCuentas.getCuenta_mayor_id();  
       String lsCuenta= pbCuentas.getCuenta_mayor();       
       String lsDescripcion= pbCuentas.getDescripcion();  
       String lsNaturaleza= pbCuentas.getNaturaleza();  
       String lsFecha_vig_ini= pbCuentas.getFecha_vig_ini(); 
       String lsFecha_vig_fin = pbCuentas.getFecha_vig_fin();
       String lsId_genero = pbCuentas.getId_genero();  
       String lsId_grupo = pbCuentas.getId_grupo();           
       String lsId_clase = pbCuentas.getId_clase();
       
       crsGrupo =pbCuentas.selectGrupoS(conexion,lsId_genero,lsId_grupo);
       crsClase =pbCuentas.selectClaseS(conexion,lsId_genero,lsId_grupo,lsId_clase);
%>

         
//=====  G A R A N T I Z A R   Q U E   L A   S E S I O N   Q U E D E   C E R R A D A =====
function regresa(){
    var x;
    x=0;
}

function Valida(){
    ok=true;
    mess="AVISO:\n";
    
 if (Filtro.txtNombre.value ==''){
       ok=false; mess=mess+'Capture el Nombre de la Cuenta\n'; 
    }
 if (Filtro.txtFecha01.value !=''){
       if (!ValidarObjeto(Filtro.txtFecha01.value,'F')){
          ok=false; mess=mess+'Fecha de Inicio del Periodo es Incorrecta: '+Filtro.txtFecha01.value+'\n';
       }}

    if (Filtro.txtFecha02.value !=''){
       if (!ValidarObjeto(Filtro.txtFecha02.value,'F')){
          ok=false; mess=mess+'Fecha de Término del Periodo es Incorrecta: '+Filtro.txtFecha02.value+'\n';
       }}
       
    if (Filtro.txtFecha01.value !='' && Filtro.txtFecha02.value == ''){
           ok=false; mess=mess+"Capture la Fecha de Término\n";}
    
     if (Filtro.txtFecha01.value =='' && Filtro.txtFecha02.value != ''){
           ok=false; mess=mess+"Capture la Fecha de Inicio\n";}
           
    if (Filtro.txtFecha01.value !='' && Filtro.txtFecha02.value != ''){
       date01=convierte(Filtro.txtFecha01.value);
       date02=convierte(Filtro.txtFecha02.value);
       if (date02 < date01) {
           ok=false; mess=mess+"La Fecha de Inicio debe ser menor o igual a la Fecha de Término\n";
       }}
       
 if (ok==true)
        return true;
    else{
        alert(mess)
        return false; 
     }        
 }//funcion
function Enviar(){
    if(Valida()){
       Filtro.txtCve.disabled=false;
        //Formulario.txtCurp.disabled=false;
     //   Formulario.btnAceptar[1].disabled=true;
        Filtro.submit();
    }
 }

</script>
  </head>
<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Cuentas de Mayor", "[Modificar]", "Formulario", true);</jsp:scriptlet>
<form name="Filtro" action="c710Control.jsp" method="post" >
<table width="100%" border="0">
<tr><td width="36%"><div align="right">G&eacute;nero:</div></td>
       <td><div align="left"><select name="lstGenero" class="cajaTexto" >
        <%  crsGenero.beforeFirst();      
            while (crsGenero.next()){ 
            if (pbCuentas.getId_genero().equals(crsGenero.getString(1))){             
%>
                <OPTION VALUE="<%=crsGenero.getString(1)%>" selected><%=crsGenero.getString(1)%> <%=crsGenero.getString(2)%></OPTION>
<%          }
            
        }//while
         crsGenero.close();
         crsGenero = null;
%>  
        </select></div></td>
</tr>
        <tr>
  <td><div align="right">Grupo:</div></td>
  <td><div align="left">
      <SELECT NAME="lstGrupo" class='cajaTexto' >
        <%  crsGrupo.beforeFirst();      
            while (crsGrupo.next()){ 
            if (pbCuentas.getId_grupo().equals(crsGrupo.getString(1)) && pbCuentas.getId_genero().equals(crsGrupo.getString(3)))
            {
%>
                <OPTION VALUE="<%=crsGrupo.getString(1)%>" selected><%=crsGrupo.getString(1)%>&nbsp;<%=crsGrupo.getString(2)%></OPTION>
<%          }
            
        }//while
        crsGrupo.close();
        crsGrupo = null;        
%>  
    </select></div></td>
</tr>    
<tr><td><div align="right">Rubro:</div></td>
  <td><div align="left">
      <SELECT NAME="lstClase" class='cajaTexto' >
        <%  crsClase.beforeFirst();      
            while (crsClase.next()){ 
            if (pbCuentas.getId_grupo().equals(crsClase.getString(3)) && pbCuentas.getId_genero().equals(crsClase.getString(2))
               && pbCuentas.getId_clase().equals(crsClase.getString(1)))
            {
%>
                <OPTION VALUE="<%=crsClase.getString(1)%>" selected><%=crsClase.getString(1)%>&nbsp;<%=crsClase.getString(4)%></OPTION>
<%          }
            
        }//while
        crsClase.close();
        crsClase = null;        
%> </select></div></td>
</tr>  
<table width="100%">
        <tr>
          <td width="36%">
            <div align="right">Consecutivo:</div>
          </td>
          <td>
            <div align="left">
              <input type="text" name="txtConse" maxlength="100" size="7"
                     value="<%=lsConsec%>" class="cajaTexto"
                     readonly="readonly"></input>
               
              <!--onChange="javascript:this.value=this.value.toUpperCase();"/-->
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <div align="right">Cuenta:</div>
          </td>
          <td>
            <div align="left">
              <input type="text" name="txtCve" maxlength="100" size="7"
                     value="<%=lsCuenta%>" class="cajaTexto"
                     readonly="readonly"></input>
               
              <!--onChange="javascript:this.value=this.value.toUpperCase();"-->
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <div align="right">Nombre:</div>
          </td>
          <td>
            <div align="left">
              <input type="text" name="txtNombre" maxlength="100" size="60"
                     value="<%=lsDescripcion%>" class="cajaTexto"
                     onchange="javascript:this.value=this.value.toUpperCase();"/>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <div align="right">Naturaleza:</div>
          </td>
          <td>
            <div align="left">
              <%
          if (lsNaturaleza.equals("D")){
%>
               
              <input type="radio" name="btrNatura" value="D" checked="checked"></input>Deudora 
              <%          }else{
%>
               
              <input type="radio" name="btrNatura" value="D"></input>Deudora 
              <%
          }
          if (lsNaturaleza.equals("A")){
%>
               
              <input type="radio" name="btrNatura" value="A" checked="checked"></input>Acreedora 
              <%        }else{
%>
               
              <input type="radio" name="btrNatura" value="A"></input>Acreedora 
              <%        }%>
            </div>
          </td>
        </tr>
        <tr>
          <td>
            <div align="right">Vigencia :</div>
          </td>
          <td>
            Fecha inicial:
            <input name="txtFecha01" type="text" class="cajaTexto" size="15"
                   maxlength="15" value="<%=lsFecha_vig_ini%>"/>
            <a href="javascript: void(0);"
               onmouseover="if (timeoutId) clearTimeout(timeoutId);return true;"
               onclick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')">
              <img src="../../../Librerias/Imagenes/calendar.gif"
                   name="imgCalendar" width="34" height="21" border="0" alt=""></img>
            </a>
            Fecha final:
            <strong>
               
              <!--<input name="txtFecha02" type="text"  class="cajaTexto" size="15" maxlength="15" value="01/01/9999"/>-->
               
              <input name="txtFecha02" type="text" class="cajaTexto" size="15"
                     maxlength="15" value="<%=lsFecha_vig_fin%>"/>
               
              <a href="javascript: void(0);"
                 onmouseover="if (timeoutId) clearTimeout(timeoutId);return true;"
                 onclick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')">
                <img src="../../../Librerias/Imagenes/calendar.gif"
                     name="imgCalendar" width="34" height="21" border="0"
                     alt=""></img>
              </a>
               </strong>
          </td>
        </tr>
        <tr>
          <td>
            <div align="right"></div>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>
            <div align="right"></div>
          </td>
          <td>&nbsp;</td>
        </tr>
        <tr>
          <td>&nbsp;</td>
          <td>&nbsp;</td>
        </tr>
      </table>

      <table align="center">
        <tr>
          <td align="center">&nbsp;</td>
          <td>
            <input type='button' name='btnAceptar' value='Aceptar' class='boton'
                   onclick='Enviar();'></input>
          </td>
          <td>
            <input type='button' name='btnRegresar' value='Regresar'
                   class='boton'
                   onclick="javascript:LlamaPagina('c710ModificarFiltro.jsp','');"></input>
          </td>
        </tr>
      </table>
      
      <input name="hOpcion" type="hidden" value="M"></input>
      <input name="hIdentifica" type="hidden" value="<%=lsCuentaID%>"></input>
      <%
}//try
catch (Exception e){
    System.out.println("no hubo conexion en "+e.getMessage()); 
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
     } //Fin finally
%>
</form></body>
</html>