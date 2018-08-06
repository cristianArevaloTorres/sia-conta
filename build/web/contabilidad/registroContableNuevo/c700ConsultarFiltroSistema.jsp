<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>


<%   
    if ( controlRegistro.getFechaAfectacion()==null){
       fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
    }  
   ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
   String lsUniEje=controlReg.getUnidad();
   String lsEntidad=String.valueOf(controlReg.getEntidad());
   String lsAmbito=String.valueOf(controlReg.getAmbito()); 
   String lsEjercicio=String.valueOf(controlReg.getEjercicio());  
   String lsCatCuenta=String.valueOf(controlReg.getIdCatalogoCuenta());
   System.out.println(lsUniEje);
%>


<jsp:useBean id="abOperaciones" class="sun.jdbc.rowset.CachedRowSet" scope="page">     
 <%
   abOperaciones.setTableName("rf_tc_maestro_operaciones");
              /* String SQL = "select maestro_operacion_id, consecutivo, descripcion from rf_tc_maestro_operaciones where  unidad_ejecutora='" + lsUniEje+ "' and ambito='"+lsAmbito +
               "' and entidad=" + lsEntidad + " and pais=147 " + " and id_catalogo_cuenta=" + lsCatCuenta + " and  extract(year from fecha_vig_ini) ="+ lsEjercicio;  */
               
               String SQL = "select maestro_operacion_id, consecutivo, descripcion from rf_tc_maestro_operaciones where  unidad_ejecutora= 103 and ambito=1 "+
               " and entidad=1 and pais=147 " + " and id_catalogo_cuenta=1 and  extract(year from fecha_vig_ini) =2013";  
                abOperaciones.setCommand(SQL);
    Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
      abOperaciones.execute(con);
    }
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo abOperaciones "+e.getMessage());
      } //Fin catch
 
     finally{
        if (con!=null){
          con.close();
          con=null;
        }
      } //Fin finally
 %>
</jsp:useBean> 

<jsp:useBean id="abpoliza" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   abpoliza.setTableName("rf_tc_tipos_polizas");
   String SQL2 = ("select tipo_poliza_id,descripcion,registro,abreviatura " + 
                  " from rf_tc_tipos_polizas" +
                  " order by tipo_poliza_id" ); 
    abpoliza.setCommand(SQL2);
    Connection con2=null;
    try{
     con2 = DaoFactory.getContabilidad();
     abpoliza.execute(con2);
    }
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo abpoliza "+e.getMessage());
      } //Fin catch
 
     finally{
        if (con2!=null){
          con2.close();
          con2=null;
        }
      } //Fin finally
 %>
</jsp:useBean> 

<jsp:useBean id="absistema" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   absistema.setTableName("rf_tr_sistema");
   String SQL3 = ("select s.idsistema, m.idmodulo, m.descripcion "+
                  " from rf_tr_sistema s, rf_tr_modulo m" +
                  " where s.idsistema=m.idsistema " +
                  " order by m.idmodulo" ); 
    absistema.setCommand(SQL3);
    Connection con3=null;
      try{
       con3 = DaoFactory.getContabilidad();
       absistema.execute(con3);
      }
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo abpoliza "+e.getMessage());
      } //Fin catch
      finally{
        if (con3!=null){
          con3.close();
          con3=null;
        }
      } //Fin finally
 %>
</jsp:useBean> 

<jsp:useBean id="abevento" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   abevento.setTableName("rf_tr_eventocontable");
   String SQL4 = ("select idevento, idmodulo, descripcion " +
                  " from rf_tr_eventocontable " +
                  " order by idevento" ); 
    abevento.setCommand(SQL4);
    Connection con4=null;
     try{
       con4 = DaoFactory.getContabilidad();
       abevento.execute(con4);
     }
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo abpoliza "+e.getMessage());
      } //Fin catch
      finally{
        if (con4!=null){
          con4.close();
          con4=null;
        }
      } //Fin finally
 %>
</jsp:useBean> 


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c700consultarFiltro</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
    
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
      filtro.btnAceptar.disabled=true;
      document.filtro.submit();
}
</script>
  </head>
  <body>
  
     <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Modificar/Cancelar", true);</jsp:scriptlet>    
      <br><br>
      <b>Fecha Actual</b> [ <%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%> ]
    
     <br><br>
    
     <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    
    <br>
    
    <FORM name="filtro" method="post"  action="c700ConsultarResultado.jsp">        
     <table align="center">
            <tr><td align="right"><input type="checkbox" name="" value="0"></td>
            <td class="negrita">Sólo pólizas de la unidad</td></tr>
            <tr><td  class="negrita" align="right">Operación Contable </td>
            <td><select id='lstOperaciones' name='lstOperaciones' class='cajaTexto' onchange='capaOperacion();'>
                <option value="">-Seleccione-</option>
             <% 
              abOperaciones.beforeFirst();
              while (abOperaciones.next()){
          %>
            <option value='<%=abOperaciones.getString("maestro_operacion_id")%>-<%=abOperaciones.getString("consecutivo")%>'><%=abOperaciones.getString("consecutivo")%>-<%=abOperaciones.getString("descripcion")%></option>
                       
          <%       
          }
         %>
           </td>      
         </tr>  
                  
      <tr><td  class="negrita" align="right">Tipo de Póliza: </td>
          <td ><select id="lstpoliza" name="lstpoliza" class="cajaTexto">
                <option value="">-Seleccione-</option>
                                  
            <%
          abpoliza.beforeFirst(); 
          while (abpoliza.next()){
          %>
            <option value="<%=abpoliza.getString("tipo_poliza_id")%>"><%=abpoliza.getString("descripcion")%></option>
                       
          <%       
          }
         %>           
        </select> 
          </td>      
      </tr>     
                 
      <tr><td class="negrita" align="right">Sistema:</td>
          <td><select name="lstsistema" class="cajaTexto" onchange="ComponerListaPro (document.forms.filtro.lstsistema[selectedIndex].value);filtro.lstevento.options[0].selected=true;" > 
                  <option value="">-Seleccione-</option>
         <%
          //Este código genera la lista padre dinamicamente
          int cuenta;
          String cat;
          //cuenta=1;
          absistema.beforeFirst(); 
          while (absistema.next()){
          %>
            <option value="<%=absistema.getString("idmodulo")%>"><%=absistema.getString("descripcion")%></option>
          <%       
          }
         %>         
          </select>
          </td>      
      </tr>     
    
      <tr><td  class="negrita" align="right">Evento</td>
          <td ><select name="lstevento" class="cajaTexto">
                  <option value="">-Seleccione-</option>
               <%
          abevento.beforeFirst(); 
            while (abevento.next()){
          %>
            <option value="<%=abevento.getString("idevento")%>"><%=abevento.getString("descripcion")%></option>
          <%       
           }
          %>     
           </select>

 <script language="JavaScript">
       // Comienza rutina para listas dinamicas
          function Tupla ( campo1, campo2 )
            {
             this.campo1 = campo1;
             this.campo2 = campo2;
            }
            var opcionesnull = new Array();
           // opcionesnull[0]= new Tupla("- Seleccione -","null");
         <%
             // Este cÓdigo genera un arreglo para cada proceso padre y almacena en
             //dichos arreglos cada una de los subprocesos hijos que le corresponden
             cuenta=1; 
             cat="null";
            try{
             abevento.beforeFirst();
           while (abevento.next()){
               if (!cat.equals(abevento.getString("idmodulo")))
            {
             cuenta=1;
             cat=abevento.getString("idmodulo");
         %>
            var opciones<%=cat%>[0] = new Array();
           //opciones<%=cat%>[0]= new Tupla("- Seleccione -","null");
           
         <%
           } //fin if
         %>
              opciones<%=cat%>[<%=cuenta%>]=new Tupla("<%=abevento.getString("idevento")+" "+abevento.getString("descripcion")%>","<%=abevento.getString("idevento")%>");
           //opciones<%=cat%>[<%=cuenta%>]=new Tupla("- Seleccione -","null")","<%=abevento.getString("idevento")%>","<%=abevento.getString("descripcion")%>","<%=abevento.getString("idevento")%>");
          //opcionesnull<%=cat%>[<%=cuenta%>]= new Tupla("- Seleccione -","null")+" "+abevento.getString("idevento")+" "+abevento.getString("descripcion")%>","<%=abevento.getString("idevento")%>");
        <%
         cuenta=cuenta+1;
       } // fin while
   }catch (Exception e) {
       System.out.println("Error en programa: "+ e.getMessage());
   }         
  %>
    var contador;
     function ComponerListaPro ( array ) {
   // Compone la lista dependiente a partir
 // del valor de la opcion escogida en la lista "padre"
 BorrarListaPro();
   array = eval("opciones" + array);
   for (contador=1; contador<array.length; contador++)
   {
     //Añade elementos a nuestro combobox dependiente
     var optionObj = new Option( array[contador].campo1, array[contador].campo2 );
     filtro.lstevento.options[contador] = optionObj;
   } // for
 } // ComponerLista
 
//Formulario.lstSubProceso.options[0].selected=true;
//Elimina las opciones de nuestro vector dependiente cada que se requiera
function BorrarListaPro() {
    filtro.lstevento.length=0;
}
//Inicializamos enviando la clave del proceso padre para este caso
ComponerListaPro ("null");
</script>
    
          
           </td>      
      </tr>     
      
       <tr>
          <td  class="negrita" align="right">Referencia General: </td>
          <td><INPUT TYPE="text" NAME="txtReferencia" SIZE=20  class=cajaTexto > ejem. KA101</td></tr>
       <tr>
          <td  class="negrita" align="right">Concepto: </td>
          <td ><INPUT TYPE="text" NAME="txtReferencia" SIZE=20  class=cajaTexto ></td></tr>
       <tr>
          <td class="negrita" align="right">Mes de Afectación: </td>
          <td><select name="lstMes" class='cajaTexto'>
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
          <td><input type='text' name='txtFecha01' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha01')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td></tr><tr><td class="negrita" align="right">Al:</td>
          <td><input type='text' name='txtFecha02' size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFecha02')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td></tr><tr><td class="negrita" align="right">Rango de pólizas: </td>
          <td><input type='text' name='txtPoliza01' SIZE=20 maxlength='10' class='cajaTexto'> ejem. E1,I8,D5,D8</td></tr><tr><td class="negrita" align="right">Importes:</td>
          <td><input type='text' name='txtPoliza02' SIZE=20 maxlength='10' class='cajaTexto'></td></tr>
          <tr><td><br></td>
          <td>
              <input type='hidden' name='txtIdCatalogo' value=<%=lsCatCuenta%>>
              <input type='hidden' name='txtUnidad' value=<%=lsUniEje%>>
              <input type='hidden' name='txtAmbito' value=<%=lsAmbito%>>
              <input type='hidden' name='txtEntidad' value=<%=lsEntidad%>>
              <input type='hidden' name='txtEjercicio' value=<%=lsEjercicio%>>              
              <input type='hidden' name='txtOpcion' value=<%=request.getParameter("opcion")%>>
              <input type='hidden' name='txtfechaActual' value=<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%>>              
          </td>
      </tr>
      
      <tr>
        <td rowspan="3"  class="negrita" align="right">Estatus de pólizas:</td>
        <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Todas las pólizas </td></tr>
      <tr>
        <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Pólizas activas</td></tr>
      <tr>
         <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Pólizas canceladas</td></tr>
      <tr><td><br></td><td><br></td></tr>
      <tr>
        <td rowspan="5"  class="negrita" align="right">Tipo de ordenamiento:</td>
        <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Unidad</td></tr>
      <tr>
        <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Entidad y ámbito</td></tr>
      <tr>
         <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Tipo de póliza</td></tr>
      <tr>
         <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Consecutivo</td></tr>
      <tr>
         <td><input id="" type="radio" name="reporte" value="0" onclick=""/>Debe-Haber y cuenta contable</td></tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:revisaFormulario();"></td>
          <td><input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c700OpcionesPolizas.jsp','');"></td></tr>
   </table>            
  </FORM>
  </body>
</html>