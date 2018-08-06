<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="pbCuentas" class = "sia.rf.contabilidad.registroContableNuevo.bcClasificadorCuentas" scope="page"/>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<title>Contabilidad - Cuentas de Mayor - Consultar</title>
<link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.js"></script>
<script type="text/javascript" src="../../../Librerias/Javascript/jquery.meio.mask.js" charset="utf-8"></script>
<script language="JavaScript" type="text/javascript">
<%
   Connection conexion=null;
 try{
    conexion=DaoFactory.getContabilidad();
       
       CachedRowSet crsGenero = null;      
       CachedRowSet crsGrupo = null;
       CachedRowSet crsClase = null;
       
       crsGenero =pbCuentas.selectGenero(conexion);
       crsGrupo =pbCuentas.selectGrupo(conexion);
       //crsClase =pbCuentas.selectClase(conexion);
           
       String opcion="";
      // String valGenero="";
       String lsCveCta = request.getParameter("cveCta");  
       
       pbCuentas.selectCuenta(conexion,lsCveCta);     
       
       String lsCuenta= pbCuentas.getCuenta_mayor();       
       String lsDescripcion= pbCuentas.getDescripcion();  
       String lsNaturaleza= pbCuentas.getNaturaleza();  
       String lsFecha_vig_ini= pbCuentas.getFecha_vig_ini(); 
       String lsFecha_vig_fin = pbCuentas.getFecha_vig_fin();
       String lsId_genero = pbCuentas.getId_genero();  
       String lsId_grupo = pbCuentas.getId_grupo();    
       crsGrupo =pbCuentas.selectGrupoS(conexion,lsId_genero,lsId_grupo);
       
       String lsId_clase = pbCuentas.getId_clase();
       crsClase =pbCuentas.selectClaseS(conexion,lsId_genero,lsId_grupo,lsId_clase);
%>

function cerrar() {
var ventana = window.self;
ventana.opener = window.self;
ventana.close();
}


function Valida(){
    ok=true;
    mess="AVISO:\n";
     
    if (ok==true) 
        return true;
    else{
        alert(mess);
        return false;
     }        
 }//funcion valida
 
function Enviar(){
    if(Valida()){
       Filtro.submit();
    }
     
} // Termina la funcion Enviar   


</script>
</head>
<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Cuentas de Mayor", "[Consultar]", "Formulario", true);</jsp:scriptlet>
<FORM Method="post" name="Filtro" >
<table width="100%" >

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
<tr>
  <td width="36%"><div align="right">N&uacute;mero de la cuenta:</div></td>
      <td><div align="left">
        <INPUT TYPE="text" Class="cajaTexto" NAME="txtCve" SIZE=13 value="<%=lsCveCta%>" maxlength="20" readonly >
        </div></td>
</tr>
<tr>  <td><div align="right">Nombre:</div></td>
      <td><div align="left"><INPUT TYPE="text" Class="cajaTexto" NAME="txtNombre" SIZE=100 value="<%=lsDescripcion%>" maxlength="20" readonly>
  </div></td>
</tr>
<tr>      
      <td><div align="right">Naturaleza:</div></td>
      <% if (lsNaturaleza.equals("D")){%>
                <td><div align="left"><input type="radio" name="btrNatura" value="D" checked >Deudora 
                <input type="radio" name="btrNatura" value="A" disabled>Acreedora</div></td> <%}
         else {%>
                <td><div align="left"><input type="radio" name="btrNatura" value="D" disabled>Deudora 
                <input type="radio" name="btrNatura" value="A" checked >Acreedora</div></td> <%}%>
 </tr> 
 <tr>
     <td><div align="right">Vigencia:</div></td>
      <td>Fecha inicial:
      <INPUT TYPE="text" Class="cajaTexto" NAME="txtFecha" SIZE=13 value="<%=lsFecha_vig_ini%>" maxlength="20" readonly>
      Fecha final:<strong> 
        <input name="txtFecha02" type="text"  class="cajaTexto" size="15" maxlength="15" value="<%=lsFecha_vig_fin%>" readonly>
        </strong></td>
 </tr>
</table>
<br><br>
<br>
<br>
<br>
<br>
<br><br>
<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'></td>
     <td width='80%'>         
         <INPUT TYPE="button" NAME="btnRegresar" VALUE="Regresar" class=boton onClick="window.close();">
     </td>
 </tr>
</table>
</FORM>
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
     } //Fin finally
%>