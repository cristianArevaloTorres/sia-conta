<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>


<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="pbCuentasBancarias" class="sun.jdbc.rowset.CachedRowSet" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c748Filtro</title>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
    
    <script language="JavaScript">
    
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
   if ((!document.filtro.txtPoliza01.value=="") && (document.filtro.txtPoliza02.value=="")){
       valida01=false;
       cadenaError=cadenaError+"Es necesario escribir el rango de pólizas completo\n";
   }
   if ((document.filtro.txtPoliza01.value=="") && (!document.filtro.txtPoliza02.value=="")){
       valida01=false;
       cadenaError=cadenaError+"Es necesario escribir el rango de pólizas completo\n";
   }
   
   if(!(valida01))
       alert(cadenaError);
   else
    document.filtro.submit();
    
}
</script>
  </head>
  <body>
  <%
  //String opcion= request.getParameter("opcion");
  ControlRegistro controlReg= (ControlRegistro)request.getSession().getAttribute("controlRegistro");      
  //String pagina= controlReg.getPagina();
     //request.setAttribute("unidadEjecutora",String.valueOf(controlReg.getUnidad()));
     //request.setAttribute("ambito",String.valueOf(controlReg.getAmbito()));
     //request.setAttribute("entidad",String.valueOf(controlReg.getEntidad()));
     //request.setAttribute("ejercicio",String.valueOf(controlReg.getEjercicio()));
     //request.setAttribute("idCatalogoCuenta",String.valueOf(controlReg.getIdCatalogoCuenta()));    
     //request.setAttribute("fechaActual",controlReg.getFechaEstablecida());  
     
     pbCuentasBancarias.setTableName("rf_tesoreria.rf_tr_cuentas_bancarias");
     String SQL = "select cb.id_cuenta, cb.num_cuenta ||'     '|| cc.nombre_cta nombre_cta " + 
     "from rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc " +
     "where cb.id_cuenta = cc.id_cuenta and cc.unidad_ejecutora = "+ controlReg.getUnidad() +" and cc.ambito = "+controlReg.getAmbito()+" and cc.entidad = "+controlReg.getEntidad()+
     //" and cb.id_tipo_cta in(1,2) " +
     "and cc.cuenta_cheques_id not in(9999) "+
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
        controlReg.setPagina(request.getParameter("opcion"));
     if(controlReg.getPagina().equals("reexpedir")){
%>
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques a reexpedir", "Cancelar/Reexpedir", true);</jsp:scriptlet>    
<%
  } else{      
%>    
   <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques a cancelar", "Cancelar", true);</jsp:scriptlet>    
<%}; %>   
    
<br><br>
      <jsp:directive.include file="../../encabezadoFechaActual.jspf"/>
<br><br>
    <H2>Informaci&oacute;n del Cheque: Unidad ejecutora=<%=controlReg.getUnidad()%>, Entidad=<%=controlReg.getEntidad()%>, Ambito=<%=controlReg.getAmbito()%> y Ejercicio=<%=controlReg.getEjercicio()%> </H2>
    
    <br>
    
    <FORM name="filtro" method="post"  action="c748Resultado.jsp">        
    <table align="center">
    
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
    
    
       <tr><td  class="negrita" align="right">Mes de Afectación: </td>
          <td ><select name="lstMes" class=cajaTexto>
                <option SELECTED Value="">- Seleccione -</option>
                <option Value="01">Enero</option>
                <option Value="02">Febrero</option>
                <option Value="03">Marzo</option>
                <option Value="04">Abril</option>
                <option Value="05">Mayo</option>
                <option Value="06">Junio</option>
                <option Value="07">Julio</option>
                <option Value="08">Agosto</option>
                <option Value="09">Septiembre</option>
                <option Value="10">Octubre</option>
                <option Value="11">Noviembre</option>
                <option Value="12">Diciembre</option>
  </select>
          
          </td>      
      </tr>      
      <tr><td class="negrita" align="right">Período del: </td>
          <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td class="negrita" align="right">Rango de cheques de: </td>
          <td><input type='text' name='txtPoliza01' size='10' maxlength='10' class='cajaTexto'> ejem. 1</td>
      </tr>
      <tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtPoliza02' size='10' maxlength='10' class='cajaTexto'> ejem. 10, mostrará los Cheques del 1 a 10</td>
      </tr>
      <tr><td><br></td>
          <td>
              <!--<input type='hidden' name='txtIdCatalogo' value=<%=request.getAttribute("idCatalogoCuenta")%>>
              <input type='hidden' name='txtUnidad' value=<%=request.getAttribute("unidadEjecutora")%>>
              <input type='hidden' name='txtAmbito' value=<%=request.getAttribute("ambito")%>>
              <input type='hidden' name='txtEntidad' value=<%=request.getAttribute("entidad")%>>
              <input type='hidden' name='txtEjercicio' value=<%=request.getAttribute("ejercicio")%>>              
              <input type='hidden' name='txtOpcion' value=<%=request.getAttribute("opcion")%>>
              <input type='hidden' name='txtfechaActual' value=<%=request.getAttribute("fechaActual")%>>   
              <input type='hidden' name='pagina' value=''>
              <input type='hidden' name='opcion' value=<%=controlReg.getPagina()%>>-->
              
              
          </td>
      </tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
          <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('../../filtroGeneral.jsp?opcion=<%=controlReg.getPagina()%>&idCatalogoCuenta=<%=controlReg.getIdCatalogoCuenta()%>','');"></td>
      </tr>
      </table>            
      
  </FORM>
  </body>
</html>
