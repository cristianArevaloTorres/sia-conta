<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet" %>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="sistema" class="sia.rf.contabilidad.registroContableEvento.Sistema" scope="session"/>
<jsp:useBean id="modulo" class="sia.rf.contabilidad.registroContableEvento.Modulo" scope="session"/>
<jsp:useBean id="evento" class="sia.rf.contabilidad.registroContableEvento.EventoContable" scope="session"/>

<script language="JavaScript" type="text/javascript">
ns4 = (document.layers)? true:false
ie4 = (document.all)? true:false
function loadSource(id,nestref, url, parametros) {
  url= url+ parametros;
//	alert("pagina:"+ url)
	if (ns4) {
		var lyr = (nestref)? eval('document.'+nestref+'.document.'+id) : document.layers[id]
		lyr.load(url,lyr.clip.width)
	}
	else 
	  if (ie4) {
		  bufferCurp.document.location = url
  	}; // if
}

function loadSourceFinish(id) {
	if (ie4) document.all[id].innerHTML = bufferCurp.document.body.innerHTML
}


</script>

<%
Connection conexion=null;
CachedRowSet crsSistema = null;      
String cadConexion = null;
 try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    crsSistema = sistema.select_rf_tr_sistema_enCache2(conexion);
%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c720Filtro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
   
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
   
    <script language="JavaScript" type="text/javascript">
    
function revisaFormulario() {
   var valida01=true, valida02=true, valida03=true;
   var cadenaError='';
   var date01,date02;
   var descSistema,descModulo,descEvento;
   
   if(document.Filtro.sinPoliza.checked)
          document.Filtro.sinPoliza.value="1"
       else
          document.Filtro.sinPoliza.value="0"       
          
          
   
   if ((document.Filtro.txtFecha01.value=="") && (!document.Filtro.txtFecha02.value==""))
   {
      valida01=false;
      cadenaError=cadenaError+"Es necesario escribir la fecha de inicio del Periodo\n";
   }
   if ((!document.Filtro.txtFecha01.value=="") && (document.Filtro.txtFecha02.value==""))
   {
       valida01=false;
       cadenaError=cadenaError+"Es necesario escribir la fecha final del Periodo\n";
   }
   
   if (valida01)
   {
     if (!(document.Filtro.txtFecha01.value=="" && document.Filtro.txtFecha02.value=="")) 
     {      
      if (!(document.Filtro.txtFecha01.value=="")) 
      {
          valida02=ValidarObjeto(document.Filtro.txtFecha01.value,"F");
          if (!(valida02)) 
          {
              cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.Filtro.txtFecha01.value+"\n";
              valida01=false; 
          }
          else date01=convierte(document.Filtro.txtFecha01.value);
      }//txtFecha01
      
    
      if (!(document.Filtro.txtFecha02.value=="")) 
      {
          valida02=ValidarObjeto(document.Filtro.txtFecha02.value,"F");
	        if (!(valida02)) 
          {
             cadenaError=cadenaError+"Error en el formato de la fecha, verifique que sea correcto(dd/mm/aaaa): "+document.Filtro.txtFecha02.value+"\n";
             valida01=false; 
          }
          else date02=convierte(document.Filtro.txtFecha02.value);            
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
        {  
       document.Filtro.descSistema.value=document.Filtro.lstSistema.options[document.Filtro.lstSistema.selectedIndex].text;                              
       document.Filtro.descModulo.value=document.Filtro.lstModulo.options[document.Filtro.lstModulo.selectedIndex].text;                              
       document.Filtro.descEvento.value=document.Filtro.lstEvento.options[document.Filtro.lstEvento.selectedIndex].text;      
       document.Filtro.cadConexion.value=document.Filtro.lstConexion.options[document.Filtro.lstSistema.selectedIndex].text;
       //document.Filtro.cadConexion.;
       //alert(document.Filtro.cadConexion.value);
       document.Filtro.submit();
       }//else    
       
       
}


</script>
  </head>
  <body onload="loadSource('capaModulo',null,'Modulo.jsp','?sistema=1');">
     <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de bitácora local", "Aplicar", true);</jsp:scriptlet>    
     <br>
  <FORM name="Filtro" method="post"  action="c720Resultado.jsp">
     <IFRAME STYLE="display:none" NAME="bufferCurp"></IFRAME>  
      <table width="100%" border=0>
          <tr>
            <td><div align="right"></div></td>
            <td>&nbsp;</td>
          </tr>
          <tr> 
            <td width='300'><div align="right">Sistema:</div></td>
            <td>
              <div align="left">                                   
                  <SELECT NAME="lstSistema" class="cajaTexto" OnChange="loadSource('capaModulo',null,'Modulo.jsp','?sistema='+document.Filtro.lstSistema[selectedIndex].value);">
                  
                    <%  
                      int contar1;
                      String cat1;         
                       while (crsSistema.next()){
                    %>
                      <OPTION VALUE="<%=crsSistema.getString(1)%>"><%=crsSistema.getString(2)%></OPTION>
                    <% 
                        cadConexion=crsSistema.getString("CADCONEXION");                   
                       }//while     
                    %>
                </SELECT>
                <SELECT NAME="lstConexion" class="cajaTexto" style="display:none">
                  
                    <%  
                      crsSistema.beforeFirst();
                       while (crsSistema.next()){
                    %>
                      <OPTION VALUE="<%=crsSistema.getString(4)%>"><%=crsSistema.getString(4)%></OPTION>
                    <% 
                       }//while     
                       crsSistema.close();
                       crsSistema = null;
                    %>
                </SELECT>
              </div>
            </td>
          </tr>   
          <tr>
            <td>
              <div align="right">M&oacute;dulo:</div>
            </td>
            <td >
              <div align="left" id='capaModulo'>
               
              </div>
            </td>
          </tr>
          <tr>  
              <td>  
                  <div align="right">Evento:</div>
             </td>
             <td >
                  <div align="left" id='capaEvento'>
                  </div>
             </td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha Solicitud Del: </td>
            <td><input type='text' name='txtFecha01' id="txtFecha01" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
          </tr>
          <tr>
            <td class="negrita" align="right">Al:</td>
            <td><input type='text' name='txtFecha02' id="txtFecha02" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
          </tr>
          <tr>
            <td class="negrita" align="right">Fecha Registro Del: </td>
            <td><input type='text' name='txtFecha03' id="txtFecha03" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha03')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
          </tr>
          <tr>
            <td class="negrita" align="right">Al:</td>
            <td><input type='text' name='txtFecha04' id="txtFecha04" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha04')"><img src="../../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
          </tr>      
          <tr>
            <td>
              <div align="right">Estatus:</div>
            </td>
            <td >
              <div align="left">
                <SELECT NAME="lstEstatus" class='cajaTexto'>
                 <option value="0"> Pendiente de aplicaci&oacute;n contable</option>
                 <option value="1"> Aplicado contablemente</option>
                </select>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div align="right">Referencia:</div>
            </td>
            <td >
              <div align="left">
                <input type='text' name='txtreferencia' id="txtreferencia" size='20'  class='cajaTexto'>
              </div>
            </td>
          </tr>
          <tr>
            <td>
              <div align="right">Registros sin PÓLIZA:</div>
            </td>
            <td >
              <div align="left">
                <input type="checkbox" name='sinPoliza' id="sinPoliza" size='20' value="0">
              </div>
            </td>
          </tr>
          <tr>
            <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
            <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick=""></td>
          </tr>
</table>
   <input type="hidden" id="descSistema" name="descSistema" >
   <input type="hidden" id="descModulo" name="descModulo" >
   <input type="hidden" id="descEvento" name="descEvento" >
   <input type="hidden" id="cadConexion" name="cadConexion" value="<%=cadConexion%>">   
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