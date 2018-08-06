<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="pcConsultaPerfiles" class="sia.rf.contabilidad.registroContableNuevo.bcConsultaPerfiles" scope="page"/>
<jsp:useBean id="fechaAfectacion" class="sia.rf.contabilidad.registroContable.servicios.FechaAfectacion" scope="page"/>
<jsp:useBean id="controlRegistro" class="sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c700ModificarResultado</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
    <script languaje="JavaScript" type='text/JavaScript'>

function revisa(){ // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
  checksOK = false;
  checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos una póliza'); 
  return checksOK;
}//function

function revisarForm(){
  return revisa() && validaRadio() && validaFecha();
}

function validaFecha(){
  pasa = false;
   fechaCan = document.getElementById('txtFechaCancelacion').value;
  tipoFecha = valorRadio();
  
  if(tipoFecha == "seleccion"){
    if(fechaCan==""){
      alert('AVISO: Favor de seleccionar una fecha de Cancelación.');
      pasa = false;
    }else{
      if(tipoFecha == "seleccion"){
       
        pasa= confirm('¿Está seguro de cancelar las pólizas seleccionadas con fecha de cancelación '+fechaCan+' ?');
      }
      else{
        pasa= confirm('¿Está seguro de cancelar la(s) póliza(s) seleccionada(s)?');
      }
    }
  }else{ pasa = true;}
  if(pasa)
    resultado.btnAceptar.disabled=true;
  return pasa;
}

function valorRadio(){
 var valor="";
  elementos = document.getElementById('resultado').elements;
  longitud = elementos.length;
  for (var i = 0; i < longitud; i++){
     if(elementos[i].name == "selFecha" && elementos[i].type == "radio" && elementos[i].checked == true){
         valor = elementos[i].value;
     }
  }
  return valor;
}

function validaRadio(){
   var avanzar = true;
   if(valorRadio() == ""){
     alert('AVISO: Selecione que fecha requiere para la cancelación.\n');
     avanzar = false;
   }
  return avanzar;
}

function activar(parametro){
  if(parametro == 0)
    document.getElementById('fechaCancelacion').style.display  = "none";
  else
    document.getElementById('fechaCancelacion').style.display  = "";
}

function ocultar(){
  document.getElementById('fechaCancelacion').style.display  = "none";
}
</script>
    
  </head>
  <body onload="ocultar()">

<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Modificar/Cancelar", true);</jsp:scriptlet>    

<br><br>
      <b>Fecha Actual</b> [<%=request.getParameter("txtfechaActual")%>]
<br><br>

<H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=request.getParameter("txtUnidad")%>, Entidad=<%=request.getParameter("txtEntidad")%>, Ambito=<%=request.getParameter("txtAmbito")%> y Ejercicio=<%=request.getParameter("txtEjercicio")%>  </H2>

<br>

<FORM name="resultado" id="resultado" Method="post" action="c700Control.jsp" onsubmit='return revisarForm();' >

<!-- Encabezado de la tabla con resultado del filtro -->
<table width="15%" align="left" class="general">
    <tr>
      <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
    </tr>
 </table>
 <br><br><br>



<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 String lsOpcion= request.getParameter("txtOpcion");
 String lsIdCatalogo= request.getParameter("txtIdCatalogo");
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
 String lstOperaciones=request.getParameter("lstOperaciones");
 String lstpoliza=request.getParameter("lstpoliza");
 String txtConcepto= request.getParameter("txtConcepto");   
 String lsReferencia= request.getParameter("txtReferencia");
 String lsMes= request.getParameter("lstMes"); 
 String lsFecha01= request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02");
 String lsPoliza01= request.getParameter("txtPoliza01");
 String lsPoliza02= request.getParameter("txtPoliza02");
 
 if ( controlRegistro.getFechaAfectacion()==null){
   fechaAfectacion.establecerFechaAfectacion(request,fechaAfectacion.obtenerFechaAfectacion());           
   
   //request.getSession().setAttribute("fechaAfectacion", "20071015");
}  

%>

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<!-- Mensaje "Marcar todos" -->
 <% if (lsOpcion.equals("C")){ %>
<tr>
 <td width="100%" bgcolor="#B6D0F1"> <input type="radio" name="ambitoRegional" value="0AMBITOS" onClick="seleccionarOpcion(document.resultado, this);">
   Marcar todos &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input name="ambitoRegional" type="radio" value="1AMBITOS" onClick="seleccionarOpcion(document.resultado, this);" checked>
   Des-marcar todos </td>
</tr>
<%}%>   
<tr>
<td>

<!-- (Inicia codigo JAVA) -->
<%  

 String Query="";
 String lsCondicion="";
 String lsCondModificar="";
    
 try{
    conexion=DaoFactory.getContabilidad();
        
    if (!lsReferencia.equals("")) {
        lsCondicion = lsCondicion + " and upper(a.referencia) like  upper('%" + lsReferencia +"%') ";
    }
    if (!lsMes.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(a.FECHA,'MM'),'MM')=to_date('"+lsMes+"','MM')";
           }  
    if (!lsEjercicio.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(a.FECHA,'yyyy'),'yyyy')=to_date('"+lsEjercicio+"','yyyy')";
    }  
    
    if (!lsFecha01.equals("")) {
        lsCondicion=lsCondicion+ " and to_date(to_char(a.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+lsFecha01+"','dd/MM/yyyy') and to_date(to_char(a.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+lsFecha02+"','dd/MM/yyyy') ";
    }
    if (!lsPoliza01.equals("")) {
        lsCondicion=lsCondicion+ " and upper(b.abreviatura)=upper('"+lsPoliza01.substring(0,1)+"') and a.consecutivo >="+lsPoliza01.substring(1)+" and a.consecutivo<="+lsPoliza02.substring(1);          
    }
        
    if (!lstpoliza.equals("")){
          lsCondicion = lsCondicion + " and a.tipo_poliza_id="+lstpoliza;
        
    }    
    if (!lstOperaciones.equals("")){
        lsCondicion = lsCondicion + " and  a.maestro_operacion_id="+lstOperaciones;
    }
    
    if (!txtConcepto.equals("")){
           lsCondicion = lsCondicion + " and upper(a.concepto) like  upper('%" + txtConcepto +"%') ";
    }
    
    
    pcConsultaPerfiles.select_rf_tc_pers_modifica_polizas(conexion,String.valueOf(Autentifica.getNumeroEmpleado()));
    if (lsOpcion.equals("M")){     
        lsCondModificar="  and ascii(substr(a.origen,1,1)) >=48 and ascii(substr(a.origen,1,1))<=57 ";          
    }
    if(pcConsultaPerfiles.getNumEmpleado().equals("")){
      lsCondicion=lsCondicion+" and (a.poliza_referencia IS NULL or a.poliza_referencia=0) " + lsCondModificar+ //CONDICION PARA OCULTAR LAS PÓLIZAS AUTOMÁTICAS
                 " and a.poliza_id not in " +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) ";
    }else{
      lsCondicion=lsCondicion+ " and (( a.poliza_referencia is null "+lsCondModificar+
                 " and a.poliza_id not in "  +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) )"+
                 " or (a.idEvento in " +  pcConsultaPerfiles.getEventos()+" and a.clasificacion_poliza_id=1 ))";
    }

     Query="select ROWNUM CONSFILA, sqlresul.* from "+
                 "(SELECT distinct a.poliza_id POLIZAID, b.abreviatura||a.consecutivo as consecutivo,to_char(a.fecha,'dd/mm/yyyy') fecha,a.referencia, a.concepto,a.origen, decode(a.ban_cheque,1,'Cheque',decode(a.tipo_poliza_id,1,'Diario',3,'Egreso','Ingreso')) poliza "+
                 "FROM RF_TR_POLIZAS a,  RF_TC_TIPOS_POLIZAS b, RF_TR_DETALLE_POLIZA d, RF_TR_CUENTAS_CONTABLES c  "+
                 " where a.poliza_id=d.poliza_id and d.cuenta_contable_id=c.cuenta_contable_id and a.tipo_poliza_id=b.tipo_poliza_id and a.id_catalogo_cuenta="+lsIdCatalogo+" and a.unidad_ejecutora='"+lsUnidad+"' and a.ambito="+lsAmbito+" and a.entidad="+lsEntidad+lsCondicion+ 
                 //" and a.poliza_referencia is null "+                 
                 " and to_number(to_char(a.fecha,'mm')) not in ( select distinct  t.mes from rf_tr_cierres_mensuales t where t.unidad_ejecutora='"+lsUnidad+"' and t.ambito='"+lsAmbito+"' and t.entidad='"+
                                                                lsEntidad+"' and t.ejercicio='"+lsEjercicio+"' and t.id_catalogo_cuenta='"+lsIdCatalogo+"'  and t.programa=substr(c.cuenta_contable,6,4) and t.estatus_cierre_id='2') "+                 
                                "order by polizaid asc) sqlresul ";
      
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"POLIZAID-,CONSECUTIVO-,FECHA-,REFERENCIA-,CONCEPTO-,-ORIGEN, POLIZA-","7%,7%,7%,30%,30%,4%,6%","Id. Poliza,Consecutivo,Fecha,Referencia,Concepto,Origen,Poliza","4,4,4,4,4,4,4"};
      String [] DefInput={"POLIZAID","CHECKBOX","ambitos","LEFT"};
      //out.println(Query);
      if (lsOpcion.equals("M")){
          xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 1, "<a href=c700ModificarFormulario.jsp","?polizaid=", ">", false);     
      }
      else{
          xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", "", true);
      }
}
catch(Exception E)
   {
   System.out.println("Error en "+E.getMessage()); 
   System.out.println("Query Modifica/Cancela Pólizas : "+Query);
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{ 
     if (conexion!=null){
         conexion.close();
         conexion=null;
     }

   }
%>
<!-- (Termina codigo JAVA) -->
</td>
</tr>
</table>
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
 <% if (lsOpcion.equals("C")){ %>
 <table width="30%" class="azulCla">
        <tr > <td>Selección de fecha de cancelación</td></tr>
      </table>
      <table border="0" width="30%" class='sianoborder'>
        <tr><td >
            <input type="radio" name="selFecha" value="original" onclick="return activar(0);"/> Fecha original de la póliza
          </td></tr>
        <tr><td >
            <input type="radio" name="selFecha" value="seleccion" onclick="return activar(1);"/> Seleccionar fecha de cancelación
          </td></tr>
      </table>
<div id="fechaCancelacion">
</br>
    <font color="red"><b>**NOTA:</b>  La fecha que se seleccione es la que aplicará contablemente para la cancelacion de todas las pólizas seleccionadas sin importar el tipo que sea.</font>
    </br>
<table>
</br>
  <tr><td class="negrita" align="right">Fecha cancelacion: </td>
      <td><input type='text' name='txtFechaCancelacion' id="txtFechaCancelacion" size='10' maxlength='10' class='cajaTexto'><a href="javascript: void(0);" onMouseOver="if (timeoutId) clearTimeout(timeoutId);return true;" onClick="open_calendar('../../Librerias/Javascript/Capturador/calendar.html','document.forms[0].txtFechaCancelacion')"><img src="../../Librerias/Imagenes/calendar.gif" name="imgCalendar" width="34" height="21" border="0" alt=""></a> dd/mm/aaaa</td>
      <!--value="<%=sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion())%>"-->
  </tr>
</table>
</div>
<%}%>


<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'>
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
        <INPUT type="hidden" name="txtOperacion" value=<%=lsOpcion%>>
        <% if (lsOpcion.equals("C")){ %>
            <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
        <%}%>    
    </td>
     <td width='80%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c700OpcionesPolizas.jsp','');" >
     </td></tr>
 </table>

  </FORM>
  </body>
</html>