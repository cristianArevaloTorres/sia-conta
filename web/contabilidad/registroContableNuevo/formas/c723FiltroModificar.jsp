<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />
<jsp:useBean id="formas" class="sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="session"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c723FiltroModificar</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
    
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
   
   if(!(valida01)){
       alert(valida01);
   }
   else {
       document.filtro.txtforma.value=document.filtro.lstForma.options[document.filtro.lstForma.selectedIndex].text;                              
       document.filtro.action="c723ResultadoModificar.jsp";
       document.filtro.submit();
       
       }
}

function FiltroGeneral() {
       document.filtro.action="../filtroGeneral.jsp?opcion=irModificaPolizaForma";
       document.filtro.submit();
       }
</script>

<%
Connection conexion=null;
CachedRowSet crsFormas = null;      
String cadConexion = null;
 try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    crsFormas = formas.select_rf_tr_formasManualesPolizas(conexion,controlRegistro.getUnidad(),controlRegistro.getAmbito(),controlRegistro.getEntidad());
%>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Modificar a través de Formas", true);</jsp:scriptlet>    
    <table align="center" width="50%" class="general">
        <thead></thead>
        <tr>
         <td class="azulObs">Unidad ejecutora</td>
         <td class="azulObs">Entidad</td>
         <td class="azulObs">Ambito</td>
         <td class="azulObs">Ejercicio</td>
        </tr>
        <tr>
         <td align="center"><%=controlRegistro.getUnidad()%></td>
         <td align="center"><%=controlRegistro.getEntidad()%></td>
         <td align="center"><%=controlRegistro.getAmbito()%></td>
         <td align="center"><%=controlRegistro.getEjercicio()%></td>
        </tr>
      </table>
      <br><br>
    <FORM name="filtro" method="post"  ">        
   <%
   if(crsFormas.next()){
   %>
   

    <table align="center">
       <tr><td  class="negrita" align="right">Referencia General: </td>
            <td ><INPUT TYPE="text" NAME="txtReferencia" SIZE=12  class=cajaTexto > ejem. KA101</td>
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
          <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      </tr>
      <tr><td class="negrita" align="right">Rango de pólizas de: </td>
          <td><input type='text' name='txtPoliza01' size='10' maxlength='10' class='cajaTexto'> ejem. D1</td>
      </tr>
      <tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtPoliza02' size='10' maxlength='10' class='cajaTexto'> ejem. D10, mostrará las pólizas de Diario de 1 a 10</td>
      </tr>
      <tr>
        <td class="negrita" align="right">Forma:</td>
        <td>
           <div align="left">                                   
             <SELECT NAME="lstForma" id="lstForma" class="cajaTexto">
             <option value="TODAS">Todas
              <%  
              crsFormas.beforeFirst();
               while (crsFormas.next()){
              %>
            <OPTION VALUE="<%=crsFormas.getString("ORIGEN")%>"><%=crsFormas.getString("ORIGEN")%></OPTION>
              <% 
                }//while     
              %>
            </SELECT>
            <%    
              crsFormas.close();
              crsFormas = null;
            %>
          </div> 
          </td>
      </tr>
      
      <tr>
        <td>
         <br>
        </td>
        <td>
          <input type='hidden' name='txtIdCatalogo' value=<%=controlRegistro.getIdCatalogoCuenta()%>>
          <input type='hidden' name='txtUnidad' value=<%=controlRegistro.getUnidad()%>>
          <input type='hidden' name='txtAmbito' value=<%=controlRegistro.getAmbito()%>>
          <input type='hidden' name='txtEntidad' value=<%=controlRegistro.getEntidad()%>>
          <input type='hidden' name='txtEjercicio' value=<%=controlRegistro.getEjercicio()%>>              
          <input type='hidden' name='txtforma' value="">
          <input type='hidden' name='pagina' value='irModificaPolizaForma'>              
         </td>
      </tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
          <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="FiltroGeneral();"></td>
      </tr>
      </table>            
      <%
      }else{%>
      <table>
        <tr>
          <td>
         <h3> No existen pólizas capturadas a través de formas manuales con los datos seleccionados.</h3>
          </td>
        </tr>
        <tr>
          <td>
          <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="FiltroGeneral();">
          </td>
        </tr>
      </table>
      <%}//fin del else%>
  </FORM>
  </body>
</html>
<%
  }catch(Exception e){System.out.println("Ocurrio un error al accesar  "+e.getMessage());
    } //Fin catch
    finally{
       if (conexion!=null){
         conexion.close();
         conexion=null;
       }
     } //Fin finally
%>