<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>


<%
   ControlRegistro controlReg = (ControlRegistro)session.getAttribute("controlRegistro");
   String lsUnidad      = controlReg.getUnidad();
   String lsAmbito      = "" + controlReg.getAmbito();
   String lsEntidad     = "" + controlReg.getEntidad();
   String lsEjercicio   = "" + controlReg.getEjercicio();
   String lsFechaActual = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlReg.getFechaAfectacion());
   
   
   
%>

<jsp:useBean id="pbCuentasBancarias" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>
<%
    
  pbCuentasBancarias.setTableName("rf_tesoreria.rf_tr_cuentas_bancarias");
  String SQL = "select cb.id_cuenta, cb.num_cuenta ||'     '|| cb.nombre_cta nombre_cta " + 
     "from rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc " +
      "where cb.id_cuenta = cc.id_cuenta and cc.unidad_ejecutora = "+ lsUnidad +" and cc.ambito = "+lsAmbito+" and cc.entidad = "+lsEntidad+" and cb.id_banco=2 and cb.id_tipo_cta in(1,2)"+
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
    <title>c706FiltroReporte</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    
   
    <script language="JavaScript" type="text/javascript">
function revisaFormulario() {
   var valida01=true, valida02=true, valida03=true;
   var cadenaError='';
   var date01,date02;


   
  if ((document.filtro.txtFecha01.value=="") && (!document.filtro.txtFecha02.value=="")){
      valida01=false;
       cadenaError=cadenaError+"Es necesario escribir la fecha de inicio del Periodo\n";
   }
   
   
   if ((!document.filtro.txtFecha01.value=="") && (document.filtro.txtFecha02.value=="")){
       valida01=false;
       cadenaError=cadenaError+"Es necesario escribir la fecha final del Periodo\n";
   }
   
   if (valida01){
    if (!(document.filtro.txtFecha01.value=="" && document.filtro.txtFecha02.value=="")) {      
      if (!(document.filtro.txtFecha01.value=="")) {
          valida02=ValidarObjeto(document.filtro.txtFecha01.value,"F");
          if (!(valida02)) {
              cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha01.value+"\n";
              valida01=false; 
          }
          else date01=convierte(document.filtro.txtFecha01.value);
      }//txtFecha01
      
    
      if (!(document.filtro.txtFecha02.value=="")) {
          valida02=ValidarObjeto(document.filtro.txtFecha02.value,"F");
	        if (!(valida02)) {
             cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha02.value+"\n";
             valida01=false; 
          }
          else date02=convierte(document.filtro.txtFecha02.value);            
	    }//txtFecha02
      
  
  	  if (valida02) {
  	      if (!(date01<=date02)) {
             valida01=false;
             cadenaError=cadenaError+"La fecha inicial debe de ser menor o igual a la fecha final,por favor verifique sus datos\n"; 
          }
      }//valida02
      }//txtFecha01 y txtFEcha02
   }//valida01   

   if(!(valida01))
       alert(cadenaError);
   else        
       document.filtro.submit();
}
</script>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de reporte de cheques", "Reporte", true);</jsp:scriptlet>    
    
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    
    <br><br>
    
    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    
    <br>
    
    <FORM name="filtro" method="post"  action="c706ResultadoReporte.jsp">
              <input type='hidden' name='txtUnidad' value='<%=lsUnidad%>'>
              <input type='hidden' name='txtAmbito' value='<%=lsAmbito%>'>
              <input type='hidden' name='txtEntidad' value='<%=lsEntidad%>'>
              <input type='hidden' name='txtEjercicio' value='<%=lsEjercicio%>'> 
              <input type='hidden' name='txtfechaActual' value='<%=lsFechaActual%>'> 
    
    <table align="center">
       <tr> <td colspan="2" class= "negrita" align="center"> B&uacute;squeda por:</td></tr>
       <tr><td  class="negrita" align="right">Cuenta bancaria: </td>
       <td >
       <select name='lstCuentaBancaria' class= 'cajaTexto'  >
<%
     pbCuentasBancarias.beforeFirst();
     while (pbCuentasBancarias.next()) {        
%>        
            <option value='<%=pbCuentasBancarias.getString("id_cuenta")%>'><%=pbCuentasBancarias.getString("nombre_cta")%></option>
<%      
     }%>   
      </select>
       </td>
       </tr>
       <tr><td  class="negrita" align="right">N&uacute;mero de cheque: </td>
       <td ><INPUT TYPE="text" NAME="txtNoCheque" SIZE=12  class=cajaTexto > </td>
       </tr>
       <tr><td class="negrita" align="right">Periodo del: </td>
          <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td  class="negrita" align="right">Importe: </td>
       <td ><INPUT TYPE="text" NAME="txtImporte" SIZE=20  class=cajaTexto > </td>
       </tr>
       <tr><td  class="negrita" align="right">Concepto: </td>
       <td ><INPUT TYPE="text" NAME="txtConcepto" SIZE=75  class=cajaTexto > </td>
       </tr>
       <tr><td  class="negrita" align="right">P&oacute;liza asociada: </td>
       <td ><INPUT TYPE="text" NAME="txtPolizaAsoc" SIZE=20  class=cajaTexto > Ejemplo: C10</td>
       </tr>
       <tr><td  class="negrita" align="right">Beneficiario: </td>
       <td ><INPUT TYPE="text" NAME="txtBeneficiario" SIZE=75  class=cajaTexto > </td>
       </tr>
       <tr><td  class="negrita" align="right">Estatus: </td>
       <td ><select name="lstEstatus" class=cajaTexto>
       <option value=""> - Seleccione -</option>
       <option value="0"> GENERADO</option>
       <option value="1"> IMPRESO</option>
       <option value="2"> REACTIVADO</option>
       <option value="3"> CANCELADO</option>
       
       </select>
       </td>
       </tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
          
          <td>
          <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('filtroGeneral.jsp?opcion=<%=controlReg.getPagina()%>&idCatalogoCuenta=<%=controlReg.getIdCatalogoCuenta()%>','');"></td>
      </tr>
    </table>            
    </FORM>
  </body>
</html>