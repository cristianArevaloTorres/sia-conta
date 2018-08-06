<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="pcConsultaPerfiles" class="sia.rf.contabilidad.registroContableNuevo.bcConsultaPerfiles" scope="page"/>


<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c723ResultadoModificar</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
    
    <script languaje="JavaScript" type='text/JavaScript'>

function revisa()
{ // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
 var avanza = true;
 var checksOK = false;
 checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos una póliza');
 if (checksOK)  //Al menos un check box esta seleccionado
   {
    var borrar= confirm('¿Está seguro de cancelar las pólizas seleccionadas ?');
    if (borrar == false)
      {
       avanza = false;
      }
   }
   else        //Ningún check box esta seleccionado
   {
    alert("aqui");
    avanza=false;
   }
  if (avanza) resultado.btnAceptar.disabled=true;
 return avanza;
}//function

function Regresar() {
       resultado.action="../filtroGeneral.jsp?opcion=irModificaPolizaForma";
       resultado.submit();
       //javascript:LlamaPagina('c700opcionesPolizas.jspx','');
      }

</script>
    
  </head>
  <body>

<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas", "Modificar/Cancelar", true);</jsp:scriptlet>    
<jsp:directive.include file="../encabezadoFechaActual.jspf" />


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

<FORM name="resultado" id="resultado" Method="post" action="">

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
 String lsForma= request.getParameter("txtforma");
 String lsOpcion= "M";
 String lsIdCatalogo= request.getParameter("txtIdCatalogo");
 String lsUnidad= controlRegistro.getUnidad();
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
    
 String lsReferencia= request.getParameter("txtReferencia");
 String lsMes= request.getParameter("lstMes")==null?"":request.getParameter("lstMes"); 
 String lsFecha01= request.getParameter("txtFecha01")==null?"":request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02")==null?"":request.getParameter("txtFecha02");
 String lsPoliza01= request.getParameter("txtPoliza01")==null?"": request.getParameter("txtPoliza01");
 String lsPoliza02= request.getParameter("txtPoliza02")==null?"":request.getParameter("txtPoliza02");

%>

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<tr>
<td>
<!-- (Inicia codigo JAVA) -->
<%  
 String Query="";
 try{
    conexion=DaoFactory.getContabilidad();
    String lsCondicion="";
        
    if (!lsReferencia.equals("")) {
        lsCondicion = lsCondicion + " and upper(a.referencia) like  upper('%" + lsReferencia +"%') ";
    }
    if (!lsMes.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(a.FECHA,'MM'),'MM')=to_date('"+lsMes+"','MM')";
    }  
    if (!lsEjercicio.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(a.FECHA,'yyyy'),'yyyy')=to_date('"+lsEjercicio+"','yyyy')";
    }  
    if (!lsForma.equals("Todas")) {
       lsCondicion = lsCondicion + " and upper(a.origen)= upper('"+ lsForma + "')";
    }else {
        lsCondicion = lsCondicion + " and not (ascii(substr(a.origen,1,1)) >=48 and ascii(substr(a.origen,1,1))<=57) ";        
     }
    if (!lsFecha01.equals("")) {
        lsCondicion=lsCondicion+ " and to_date(to_char(a.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+lsFecha01+"','dd/MM/yyyy') and to_date(to_char(a.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+lsFecha02+"','dd/MM/yyyy') ";
    }
    if (!lsPoliza01.equals("")) {
        lsCondicion=lsCondicion+ " and upper(b.abreviatura)=upper('"+lsPoliza01.substring(0,1)+"') and a.consecutivo >="+lsPoliza01.substring(1)+" and a.consecutivo<="+lsPoliza02.substring(1);          
    }
    
    pcConsultaPerfiles.select_rf_tc_pers_modifica_polizas(conexion,String.valueOf(Autentifica.getNumeroEmpleado()));
    
    if(pcConsultaPerfiles.getNumEmpleado().equals("")){
      lsCondicion=lsCondicion+ " and a.poliza_referencia is null " +
                 " and a.poliza_id not in " +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) ";
    }else{
      lsCondicion=lsCondicion+ " and ((a.poliza_referencia is null " +
                 " and a.poliza_id not in "  +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) )"+
                 " or a.idEvento in " +  pcConsultaPerfiles.getEventos()+")";
    }

     Query="select ROWNUM CONSFILA, sqlresul.* from "+
                 "(SELECT distinct a.poliza_id POLIZAID, b.abreviatura||a.consecutivo as consecutivo,to_char(a.fecha,'dd/mm/yyyy') fecha,a.referencia, a.concepto "+
                 " FROM RF_TR_POLIZAS a,  RF_TC_TIPOS_POLIZAS b, RF_TR_DETALLE_POLIZA d, RF_TR_CUENTAS_CONTABLES c  "+
                 " where a.poliza_id=d.poliza_id and d.cuenta_contable_id=c.cuenta_contable_id and a.tipo_poliza_id=b.tipo_poliza_id and a.id_catalogo_cuenta="+lsIdCatalogo+" and a.unidad_ejecutora='"+lsUnidad+"' and a.ambito="+lsAmbito+" and a.entidad="+lsEntidad+lsCondicion+ 
                 //" and a.poliza_referencia is null "+                 
                 " and to_number(to_char(a.fecha,'mm')) not in ( select distinct  t.mes from rf_tr_cierres_mensuales t where t.unidad_ejecutora='"+lsUnidad+"' and t.ambito='"+lsAmbito+"' and t.entidad='"+
                                                                 lsEntidad+"' and t.ejercicio='"+lsEjercicio+"' and t.id_catalogo_cuenta='"+lsIdCatalogo+"'  and t.programa=substr(c.cuenta_contable,6,4) and t.estatus_cierre_id='2') "+                 
                 //" and a.poliza_id not in " +
                 //" (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) "+
                 " order by a.poliza_id asc) sqlresul ";

  // System.out.println(Query);
      
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"POLIZAID-,CONSECUTIVO-,FECHA-,REFERENCIA-,CONCEPTO-","10%,10%,10%,35%,35%","Id. Poliza,Consecutivo,Fecha,Referencia,Concepto","4,4,4,4,4"};
      String [] DefInput={"POLIZAID","CHECKBOX","ambitos","LEFT"};
      
      //if (lsOpcion.equals("M")){
          xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 1, "<a href=c723ControlModificar.jsp","?txtFecha01="+lsFecha01+"&txtFecha02="+lsFecha02+"&txtPoliza01="+lsPoliza01+"&txtPoliza02="+lsPoliza02+"&lstMes="+lsMes+"&txtforma="+lsForma+"&consecutivo="+"&polizaid=", ">", false);     
      //}
      //else{
        //  xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", "", true);
     // }
}
catch(Exception E)
   {
   System.out.println("Error en "+E.getMessage()); 
   //System.out.println("Query: "+Query);
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



<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'>
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
        <INPUT type="hidden" name="txtOperacion" value=<%=lsOpcion%>>
        <input type="hidden" name="pagina" value="irModificaPolizaForma"/>      
     </td>
     <td width='80%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="Regresar()" >
     </td></tr>
 </table>

  </FORM>
  </body>
</html>