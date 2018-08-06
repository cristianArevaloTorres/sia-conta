<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbCuentasBancarias" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<jsp:useBean id="pbChequera" class="sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>  
<jsp:useBean id="Autentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />

<%
   ControlRegistro controlReg = (ControlRegistro)session.getAttribute("controlRegistro");
   String lsUnidad      = controlReg.getUnidad();
   String lsAmbito      = "" + controlReg.getAmbito();
   String lsEntidad     = "" + controlReg.getEntidad();
   String lsEjercicio   = "" + controlReg.getEjercicio();
   String lsFechaActual = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlReg.getFechaAfectacion());
   pbChequera.setUnidad_ejecutora(lsUnidad);
   pbChequera.setEntidad(lsEntidad);
   pbChequera.setAmbito(lsAmbito);
   int numEmpleado = Autentifica.getNumeroEmpleado();
    
   
  pbCuentasBancarias.setTableName("rf_tesoreria.rf_tr_cuentas_bancarias");
  String SQL = "select cc.cuenta_cheques_id, cb.id_cuenta, cb.num_cuenta ||'     '|| cb.nombre_cta nombre_cta, cc.consecutivo " +  
     "from rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc " +
     "where cb.id_cuenta = cc.id_cuenta and cc.unidad_ejecutora = "+ lsUnidad +" and cc.ambito = "+lsAmbito+" and cc.entidad = "+lsEntidad+"  and cb.id_banco=2 and cb.id_tipo_cta in(1,2)" +
     "order by cb.nombre_cta";
  pbCuentasBancarias.setCommand(SQL);
  Connection con=null;
  try{
     con = DaoFactory.getContabilidad();
     pbCuentasBancarias.execute(con);
   }
     catch(Exception e){
       System.out.println("Ocurrio un error al accesar el metodo pbCuentasBancarias "+e.getMessage());
     } //Fin catch
     finally{
       if (con!=null){
         con.close();
         con=null;
       }
     } //Fin finally
%>



<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c737Resultado</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/javascript">
function revisaFormulario() {
   var valida01=true, valida02=true, valida03=true;
   var cadenaError='';
   var date01,date02;
   var  nuevo,anterior=0;
   nuevo = parseInt(document.filtro.txtConsecutivo.value,10);
   anterior = parseInt(document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title,10)
   valida01=(document.filtro.txtMotivo.value.length>0 );  
   valida02=(nuevo > anterior);
    if((!valida01 || !valida02)){
      if(!(valida01))        
            alert("Debe de capturar el valor del campo Motivo");
       else
            alert("El consecutivo que capture debe ser mayor a: "+document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title);
   }
   else
   {      
       document.filtro.txtcuentaBancaria.value = document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].value;   
       document.filtro.txtConsecutivoAnt.value= document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title
       document.filtro.submit();
   }
}

  function asignaConsecutivo(){
   document.filtro.txtConsecutivo.value=document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title;
  }
  
  function Verificar (valor){
   if (valor.value<(document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title).value)
    {
        alert("El valor que capture debe ser mayor a:"+document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title);
        document.filtro.txtConsecutivo.value=document.filtro.lstCuentaBancaria.options[document.filtro.lstCuentaBancaria.selectedIndex].title
    }
    //document.filtro.txtConsecutivo.focus();
  }
</script>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado de cheques", "Actualizar consecutivo", true);</jsp:scriptlet>    
        
    <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    
    <br>
    
    <FORM name="filtro" method="post"  action="c737Control.jsp">
              <input type='hidden' name='txtUnidad' value='<%=lsUnidad%>'>
              <input type='hidden' name='txtAmbito' value='<%=lsAmbito%>'>
              <input type='hidden' name='txtEntidad' value='<%=lsEntidad%>'>
              <input type='hidden' name='txtEjercicio' value='<%=lsEjercicio%>'> 
              <input type='hidden' name='txtfechaActual' value='<%=lsFechaActual%>'> 
              <input type='hidden' name='txtNumEmpleado' value='<%=numEmpleado%>'> 
    <table align="center">
       <tr> <td colspan="2" class= "negrita" align="center"> B&uacute;squeda por:</td></tr>
       <tr><td  class="negrita" align="right">Cuenta bancaria: </td>
       <td >
       <select name='lstCuentaBancaria' class= 'cajaTexto' onchange='asignaConsecutivo();' >
<%
     pbCuentasBancarias.beforeFirst();
     while (pbCuentasBancarias.next()) {        
%>        
            <option value='<%=pbCuentasBancarias.getString("cuenta_cheques_id")%>' title='<%=pbCuentasBancarias.getString("consecutivo")%>' ><%=pbCuentasBancarias.getString("nombre_cta")%></option>
<%      
     
     }
     
     %>   
      </select>
       </td>
       </tr>
       <tr><td  class="negrita" align="right">Consecutivo: </td>
       <td ><INPUT TYPE="text" NAME="txtConsecutivo" SIZE=5  class=cajaTexto onchange="Verificar(document.filtro.txtConsecutivo.value)" > </td>
       </tr>
       <tr><td  class="negrita" align="right">Motivo: </td>
       <td ><INPUT TYPE="text" NAME="txtMotivo" SIZE=50  class=cajaTexto  > </td>
       </tr>      
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
          <td>
          <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('../../../registroContableNuevo/filtroGeneral.jsp?opcion=<%=controlReg.getPagina()%>&idCatalogoCuenta=<%=controlReg.getIdCatalogoCuenta()%>','');">
          <input type='hidden' name='txtcuentaBancaria'>
          <input type='hidden' name='txtConsecutivoAnt'>
       </td>
          
      </tr>
    </table>
    </FORM>
    <script>
      asignaConsecutivo();
    </script>
  </body>
</html>