<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.util.*,java.sql.*"%>
<%@ page import="sia.db.dao.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>


<%
        // Recuperacion de los parametros pasando mediante POST: 
        //         Unidad Ejecutora, Ambito, Entidad, Ejercicio, Fecha Actual
        //         y Catalogo de Cuentas       
        ControlRegistro controlReg = (ControlRegistro) session.getAttribute("controlRegistro");
        String lsUnidad = controlReg.getUnidad();
        String lsAmbito = "" + controlReg.getAmbito();
        String lsEntidad = "" + controlReg.getEntidad();
        String lsEjercicio = "" + controlReg.getEjercicio();
        String lsFechaActual = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA, controlReg.getFechaAfectacion());
        String lsCatCuenta = String.valueOf(controlReg.getIdCatalogoCuenta());

%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c706Filtro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" type="text/javascript">
        //Funcion para validar la información que el usuario esta capturando 
        //en el formultario
        function revisaFormulario() {
            var valida01=true, valida02=true;
            var cadenaError='';
            var date01,date02;
            
            // Valida que la fecha inicial del periodo no esta vacia
            if ((document.filtro.txtFecha01.value=="") && (!document.filtro.txtFecha02.value=="")){
                valida01=false;
                cadenaError=cadenaError+"Es necesario escribir la fecha de inicio del Periodo\n";
            }
            
            // Valida que la fecha final del periodo no esta vacia
            if ((!document.filtro.txtFecha01.value=="") && (document.filtro.txtFecha02.value=="")){
                valida01=false;
                cadenaError=cadenaError+"Es necesario escribir la fecha final del Periodo\n";
            }
            
            
            if (valida01){
                if (!(document.filtro.txtFecha01.value=="" && document.filtro.txtFecha02.value=="")) {      
                    if (!(document.filtro.txtFecha01.value=="")) {
                        // Valida que la fecha inicial del periodo tenga el formato correcto de dd/mm/aaaa                        
                        valida02=ValidarObjeto(document.filtro.txtFecha01.value,"F");
                        if (!(valida02)) {
                            cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha01.value+"\n";
                            valida01=false; 
                        }
                        else date01=convierte(document.filtro.txtFecha01.value);
                    }//txtFecha01
                    
                    // Valida que la fecha inicial del periodo tenga el formato correcto de dd/mm/aaaa
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
                }//txtFecha01 y txtFecha02
            }//valida01   
            
            
            
            if (valida01){
                cadenaError='';
                valida01=true;
                valida02=true;
                
                // Valida que la fecha inicial del periodo no esta vacia
                if ((document.filtro.txtFecha03.value=="") && (!document.filtro.txtFecha04.value=="")){
                    valida01=false;
                    cadenaError=cadenaError+"Es necesario escribir la fecha de inicio del Periodo\n";
                }
                
                
                // Valida que la fecha final del periodo no esta vacia
                if ((!document.filtro.txtFecha03.value=="") && (document.filtro.txtFecha04.value=="")){
                    valida01=false;
                    cadenaError=cadenaError+"Es necesario escribir la fecha final del Periodo\n";
                }
                
                if (valida01){
                    if (!(document.filtro.txtFecha03.value=="" && document.filtro.txtFecha04.value=="")) {      
                        // Valida que la fecha inicial del periodo tenga el formato correcto de dd/mm/aaaa
                        if (!(document.filtro.txtFecha03.value=="")) {
                            valida02=ValidarObjeto(document.filtro.txtFecha03.value,"F");
                            if (!(valida02)) {
                                cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha03.value+"\n";
                                valida01=false; 
                            }
                            else date01=convierte(document.filtro.txtFecha03.value);
                        }//txtFecha03
                        
                        // Valida que la fecha final del periodo tenga el formato correcto de dd/mm/aaaa
                        if (!(document.filtro.txtFecha04.value=="")) {
                            valida02=ValidarObjeto(document.filtro.txtFecha04.value,"F");
                            if (!(valida02)) {
                                cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.filtro.txtFecha04.value+"\n";
                                valida01=false; 
                            }
                            else date02=convierte(document.filtro.txtFecha04.value)           
                        }//txtFecha04
                        
                        // Valida que la fecha inicial debe de ser menor o igual a la fecha final
                        if (valida02) {
                            if (!(date01<=date02)) {
                                valida01=false;
                                cadenaError=cadenaError+"La fecha inicial debe de ser menor o igual a la fecha final,por favor verifique sus datos\n"; 
                            }
                        }//valida02
                    }//txtFecha03 y txtFecha04
                }//valida01   
            }
            
            
            if(!(valida01))
                alert(cadenaError); // Muestra mensaje de alerta con los errores encontrados en la validacion
            else        
                document.filtro.submit(); // Realiza un envio del formulario para su procesamiento
        }
        
        // Funcion encargada de realizar el envio del formulario capturado, toda vez
        // que ha sido correctamente validado.
        function enviaFiltro() {
            filtro.action="../filtroGeneral.jsp?opcion=eliminarCargaExcel&idCatalogoCuenta=1";
            filtro.submit();
        }
    </script>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de polizas", "Eliminar", true);</jsp:scriptlet>    
    
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    
    <br><br>
    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    <br>
    
    <FORM name="filtro" method="post"  action="c725Resultado.jsp">
        <input type='hidden' name='txtUnidad' value='<%=lsUnidad%>'>
        <input type='hidden' name='txtAmbito' value='<%=lsAmbito%>'>
        <input type='hidden' name='txtEntidad' value='<%=lsEntidad%>'>
        <input type='hidden' name='txtEjercicio' value='<%=lsEjercicio%>'> 
        <input type='hidden' name='txtfechaActual' value='<%=lsFechaActual%>'> 
        <input type='hidden' name='txtCatCuenta' value='<%=lsCatCuenta%>'> 
        <input type='hidden' name='pagina' value='eliminarCarga'>
        <input type='hidden' name='idCatalogoCuenta' value='1'>
                  
        <table align="center">
            <tr> <td colspan="2" class= "negrita" align="center"> B&uacute;squeda por:</td></tr>
            <tr><td  class="negrita" align="right">N&uacute;mero de Poliza(numPoli): </td>
                <td ><INPUT TYPE="text" NAME="txtNumPoli" SIZE=12  class=cajaTexto > </td>
            </tr>
            <tr><td  class="negrita" align="right">N&uacute;mero de Operación: </td>
                <td ><INPUT TYPE="text" NAME="txtOperacion" SIZE=6  class=cajaTexto > </td>
            </tr>
            <tr><td  class="negrita" align="right">Concepto: </td>
                <td ><INPUT TYPE="text" NAME="txtConcepto" SIZE=30  class=cajaTexto > </td>
            </tr>       
            <tr><td  class="negrita" align="right">Referencia General: </td>
                <td ><INPUT TYPE="text" NAME="txtRefGral" SIZE=30  class=cajaTexto > </td>
            </tr>               
            <tr><td class="negrita" align="right">Fecha de aplicación contable del: </td>
                <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
            </tr>
            <tr><td class="negrita" align="right">Al:</td>
                <td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
            </tr>
            <tr><td class="negrita" align="right">Fecha de alta del: </td>
                <td><input type='text' name='txtFecha03' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha03')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
            </tr>
            <tr><td class="negrita" align="right">Al:</td>
                <td><input type='text' name='txtFecha04' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha04')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
            </tr>      
            <tr>
                <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
                <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="enviaFiltro()"></td>
            </tr>
        </table>            
    </FORM>
  </body>
</html>