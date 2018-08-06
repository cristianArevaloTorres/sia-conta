<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.servicios.Condicion"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="pcConsultaPerfiles" class="sia.rf.contabilidad.registroContableNuevo.bcConsultaPerfiles" scope="page"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c733ResultadoPolizasAuto</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
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
    var borrar= confirm('¿Está seguro de ABRIR las pólizas seleccionadas ?');
    if (borrar == false)
      {
       avanza = false;
      }
   }
   else        //Ningún check box esta seleccionado
   {
    avanza=false;
   }
  if (avanza) resultado.btnAceptar.disabled=true;
 return avanza;
}//function
</script>
    
  </head>
  <body>
<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 String lsOpcion= "C";
 String lsIdCatalogo= request.getParameter("idCatalogoCuenta");
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito").substring(3,4);
 String lsEntidad= request.getParameter("txtEntidad").substring(3,(request.getParameter("txtEntidad").length()));
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
    
 String lsReferencia= request.getParameter("txtReferencia");
 String lsMes= request.getParameter("txtMes"); 
 String lsFecha01= request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02");
 String lsPoliza01= request.getParameter("txtPoliza01");
 

%>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de pólizas","Abrir Póliza", true);</jsp:scriptlet>    

<H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=request.getParameter("txtUnidad")%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=request.getParameter("txtEjercicio")%>  </H2>

<br>

<FORM name="resultado" id="resultado" Method="post" action="c733ControlPolizasAuto.jsp" onsubmit='return revisa();'>

<!-- Encabezado de la tabla con resultado del filtro -->
<table width="15%" align="left" class="general">
    <tr>
      <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
    </tr>
 </table>
 <br><br><br>





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
        Condicion condicionRango = new Condicion();
        condicionRango.setCriterios(lsPoliza01.toUpperCase());
        lsCondicion=lsCondicion+ " and "+ condicionRango.getSentencia("b.abreviatura", "a.consecutivo ");
    }
    Query="select ROWNUM CONSFILA, sqlresul.* from "+
                 "(SELECT distinct a.poliza_id POLIZAID, b.abreviatura||a.consecutivo as consecutivo,to_char(a.fecha,'dd/mm/yyyy') fecha,a.referencia, a.concepto,a.origen "+
                 "FROM RF_TR_POLIZAS a,  RF_TC_TIPOS_POLIZAS b, RF_TR_DETALLE_POLIZA d, RF_TR_CUENTAS_CONTABLES c  "+
                 " where a.poliza_id=d.poliza_id and d.cuenta_contable_id=c.cuenta_contable_id and a.tipo_poliza_id=b.tipo_poliza_id and a.id_catalogo_cuenta=1 and a.unidad_ejecutora='"+lsUnidad+"' and a.ambito="+lsAmbito+" and a.entidad="+lsEntidad+lsCondicion+
                 " and  ascii(substr(a.origen,1,1)) >=48 and ascii(substr(a.origen,1,1))<=57 "+
                 " and a.origen<> '99' and to_number(to_char(a.fecha,'mm')) not in ( select distinct  t.mes from rf_tr_cierres_mensuales t where t.unidad_ejecutora='"+lsUnidad+"' and t.ambito='"+lsAmbito+"' and t.entidad='"+
                                                                 lsEntidad+"' and t.ejercicio='"+lsEjercicio+"' and t.id_catalogo_cuenta=1  and t.programa=substr(c.cuenta_contable,6,4) and t.estatus_cierre_id='2') "+                 
                 "order by polizaid asc) sqlresul ";

      System.out.println(Query);      
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"POLIZAID-,CONSECUTIVO-,FECHA-,REFERENCIA-,CONCEPTO-,-ORIGEN","10%,10%,10%,30%,30%,10%","Id. Poliza,Consecutivo,Fecha,Referencia,Concepto,Origen","4,4,4,4,4,4"};
      String [] DefInput={"POLIZAID","CHECKBOX","ambitos","LEFT"};
      
      xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", "", true);
      
}
catch(Exception E)
   {
   System.out.println("Error en "+E.getMessage()); 
   System.out.println("Query Abrir Pólizas : "+Query);
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
        <% if (lsOpcion.equals("C")){ %>
            <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
        <%}%>    
    </td>
     <td width='80%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c733FiltroPolizasAuto.jsp','');" >
     </td></tr>
 </table>
 
 <input type='hidden' name='txtIdCatalogo' value=<%=lsIdCatalogo%>>
 <input type='hidden' name='txtUnidad' value=<%=lsUnidad%>>
 <input type='hidden' name='txtAmbito' value=<%=lsAmbito%>>
 <input type='hidden' name='txtEntidad' value=<%=lsEntidad%>>
 <input type='hidden' name='txtEjercicio' value=<%=lsEjercicio%>>              
 <input type='hidden' name='txtOpcion' value=<%=lsOpcion%>>
<input type='hidden' name='txtfechaActual' value='<%=lsFechaActual%>'> 

 <input type='hidden' name='txtReferencia' value=<%=lsReferencia%>>
 <input type='hidden' name='txtMes' value=<%=lsMes%>>
 <input type='hidden' name='txtFecha01' value=<%=lsAmbito%>>
 <input type='hidden' name='txtFecha02' value=<%=lsFecha02%>>
 <input type='hidden' name='txtPoliza01' value=<%=lsPoliza01%>>              
  </FORM>
  </body>
</html>