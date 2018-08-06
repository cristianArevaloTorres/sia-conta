<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<!-- (Inicia codigo JAVA) -->
<%

 ControlRegistro controlReg = (ControlRegistro)session.getAttribute("controlRegistro");
   String lsUnidad      = controlReg.getUnidad();
   String lsAmbito      = "" + controlReg.getAmbito();
   String lsEntidad     = "" + controlReg.getEntidad();
   String lsEjercicio   = "" + controlReg.getEjercicio();
   String lsFechaActual = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlReg.getFechaAfectacion());
   
 Connection conexion=null;
  
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta http-equiv="Cache-Control" content ="no-cache">
    <meta http-equiv="Pragma" content="no-cache"> 
    <meta http-equiv="expires" content="0" >



    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" type='text/JavaScript'>

function abrirReporte(){
    document.resultado.action="../polizas/c736Imprimir.jsp";
    document.resultado.submit();
  //var parametros = '../registroContableNuevo/polizas/c736Imprimir.jsp';
  //window.open(parametros,'_blank');
}
function btnRegresar1(){
    document.resultado.action="../filtroGeneral.jsp?opcion=ReporteNoPolizas&idCatalogoCuenta=1"
    document.resultado.submit();
  //var parametros = '../registroContableNuevo/polizas/c736Imprimir.jsp';
  //window.open(parametros,'_blank');
}

function revisa()
{ // Valida que al menos un check box está seleccionado y si es así, confirmar su Revisión
 var avanza = true;
 var checksOK = false;
 var tipoChek = typeof(document.resultado.AMBITOS)
 
 checksOK = verificaChecksSinSubmit(document.resultado, 'AMBITOS', 'Debe seleccionar al menos un cheque');
 if (checksOK)  //Al menos un check box esta seleccionado
   {
    if(  tipoChek === 'undefined' ) {
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
    <title>c736Resultado</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado de polizas", "Reporte", true);</jsp:scriptlet>    
    
    <H2>Informaci&oacute;n de la P&oacute;lizas: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    
    <br>
    
    <FORM name="resultado" method="post" >
    <!-- Encabezado de la tabla con resultado del filtro -->

    
<table width="15%" align="left" class="general">
    <tr>
      <th height="21" colspan="3" align="left" class="general">Resultado del filtro</th>
    </tr>
 </table>
 <br><br><br>
 

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
    Query="select ROWNUM CONSFILA, sqlresul.* from "+
           "(select t.mes,  m.descripcion, t.unidad_ejecutora, t.entidad||t.ambito ambito,sum(decode(t.tipo_poliza_id,1,1,0)) Diario, sum(decode(t.tipo_poliza_id,2,1,0)) Cheque,"+
           " sum(decode(t.tipo_poliza_id,3,1,0)) Egreso, sum(decode(t.tipo_poliza_id,4,1,0)) Ingreso"+
           " from rf_tr_polizas t, tc_meses m where t.id_catalogo_cuenta=1"+
           " and t.ejercicio="+lsEjercicio+"  and t.unidad_ejecutora="+lsUnidad+" and t.tipo_poliza_id in(1,2,3,4)" +
           " and t.entidad="+lsEntidad+"  and t.ambito="+lsAmbito+
           " and t.mes=m.id_mes group by  t.mes, m.descripcion, t.unidad_ejecutora, t.entidad||t.ambito order by t.unidad_ejecutora, t.entidad||t.ambito, t.mes) sqlresul order by CONSFILA";
    //System.out.println("Poliza asociada "+Query);
    String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"CONSFILA-,DESCRIPCION-,UNIDAD_EJECUTORA-,AMBITO-,DIARIO-,CHEQUE-,EGRESO-,INGRESO-","12%,13%,12%,13%,12%,13%,12%,13%","No.,Mes,Unidad Ejecutora,Ambito,Diario,Cheque,Egreso,Ingreso","4,4,4,4,4"};
      String [] DefInput={"POLIZA_ID","CHECKBOX","ambitos","LEFT"};

      xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", "", false);
}
catch(Exception E)
   {
   System.out.println("Error en "+E.getMessage()); 
   System.out.println("Query: "+Query);
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
<INPUT type="hidden" name="ejercicio" value="<%=lsEjercicio%>" >
<INPUT type="hidden" name="unidad" value="<%=lsUnidad%>" >
<INPUT type="hidden" name="ambito" value="<%=lsAmbito%>" >
<INPUT type="hidden" name="entidad" value="<%=lsEntidad%>" >

<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->

<table width='100%'>
  <tr>
     <td width='100%' align="right">
       <input type='button' name='btnReporte' value='Reporte Listado' class='boton' onClick="abrirReporte();" >
    </td>
     <td width='20%'>
       <input type="button" name='btnRegresar' value='Regresar' class='boton' onClick="btnRegresar1();" >
                                                                                    
     </td></tr>
 </table>
 </FORM>
  </body>
</html>