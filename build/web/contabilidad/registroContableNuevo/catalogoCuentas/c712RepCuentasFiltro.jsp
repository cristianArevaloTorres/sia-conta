<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*,java.io.*" %>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class ="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
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
<%
    Connection conexion=null;
    String lsUniEje=controlRegistro.getUnidad();
    String lsEntidad=String.valueOf(controlRegistro.getEntidad());
    String lsAmbito=String.valueOf(controlRegistro.getAmbito());   
    boolean lsAdmin=controlRegistro.isUsuarioAdmin();
    boolean lsInter=controlRegistro.isUsuarioIntermedio();
    boolean lsBajo=controlRegistro.isUsuarioBajo();
  
    try{
        conexion=DaoFactory.getContabilidad();
%>
function checkFields() {
          var mensajes = "";
          var num = document.form1.elements.length
          var validFlag = true          
          for (var i=0; i<16; i++) {              
              if ((document.form1.elements[i].value == "" && document.form1.elements[i+1].value != "") ||
                   (document.form1.elements[i].value != "" && document.form1.elements[i+1].value == ""))                
              {
                  validFlag = false;
                  mensajes = mensajes+" Los rangos no pueden ir uno con informacion y el otro vacío";
                  alert(mensajes);
                  break ;              
              }
              if(!document.form1.elements[i].value == "")
              {
                  if (!isInteger(document.form1.elements[i].value))
                  {  mensajes = mensajes+" El valor debe ser numérico";
                     alert(mensajes);
                     validFlag = false;
                     break;
                  }
               } 
                     
              if (document.form1.elements[i].value > document.form1.elements[i+1].value)               
              {
                  validFlag = false;
                  mensajes = mensajes+" El primer valor del rango "+document.form1.elements[i].value+" debe ser menor que el segundo "+document.form1.elements[i+1].value;
                  alert(mensajes);
                  break ;              
              }
              
            if(i==0){
                if(document.form1.elements[i].value=="" && document.form1.elements[i].value=="")
                    {mensajes = mensajes+" Los rangos a nivel de cuenta no pueden ir vacios";
                     alert(mensajes);
                     validFlag = false;
                     break;
                     }
              }      
              i++;
          }      
          return validFlag;
}//function checkFields

//FUNCION QUE RELLENA CON CEROS LOS ELEMENTOS parametros THIS,THIS.VALUE,tam
function Rellena(campo,valor,tam){
    cadcero='';
    for(i=0;i<(tam-valor.length);i++){
       cadcero+='0';
    }
    campo.value=cadcero+valor;
  //  if (campo.value == '00000' || campo.value == '0000')
  //    campo.value="";
}

function Enviar(){
    if(checkFields()){
        
        Filtro.txtCta1[4].disabled=false;
        Filtro.txtCta1[5].disabled=false;
        Filtro.txtCta1[6].disabled=false;
        Filtro.txtCta1[7].disabled=false;
        Filtro.submit();
       
    }else{
    
    }
}

function Carga(){
<%
    if(lsInter==true){
%>
        Rellena(Filtro.txtCta1[6],"<%=lsUniEje%>","4");
        Rellena(Filtro.txtCta1[7],"<%=lsUniEje%>","4");
        Filtro.txtCta1[6].disabled=true;
        Filtro.txtCta1[7].disabled=true;
<%  }else{
        if(lsBajo==true){
%>        
            Rellena(Filtro.txtCta1[6],"<%=lsUniEje%>","4");
            Rellena(Filtro.txtCta1[7],"<%=lsUniEje%>","4");
            Filtro.txtCta1[6].disabled=true;
            Filtro.txtCta1[7].disabled=true;
            
            Rellena(Filtro.txtCta1[8],"<%=lsEntidad%><%=lsAmbito%>","4");
            Rellena(Filtro.txtCta1[9],"<%=lsEntidad%><%=lsAmbito%>","4");
            Filtro.txtCta1[8].disabled=true;
            Filtro.txtCta1[9].disabled=true;
        
        
<%        }
    }   
%>
}

</script>
</head>

<body onload="Carga();">
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Catálogo de Cuentas ", "[Reporte]", "Registro", true);</jsp:scriptlet>
<FORM Method="post" name="Filtro" action="c712Reporte.jsp" id="form1"> 

<table width="100%">
<tr> 
      <td>&nbsp;</td>
      <td><div align="center">Rango de Cuenta</div></td>
      <td>&nbsp;</td>
</tr>  
</table>
<br><br>
<table width="100%" >
<tr>
    <td width="37%" align="right">Cuenta: </td>
    <td align="left" width="10%"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right" width="12%">Cuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="1" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,1);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="1" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,1);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
<tr>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
    <td align="right">Subcuenta: </td>
    <td align="left"><input type="text" name="txtCta1" maxlength="4" size="5"  class="cajaTexto" onBlur="Rellena(this,this.value,4);"></input></td>
</tr>
</table>
<br><br>
<table align="center">
 <tr>
     <td align="center">
     <input type='button' name='btnAceptar' value='Aceptar' class='boton' onclick="javascript:Enviar();">
     </td>
 </tr>
</table>
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