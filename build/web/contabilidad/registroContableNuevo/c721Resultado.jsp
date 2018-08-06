<%-- 
    Document   : c721Resultado
    Created on : 9/02/2017, 02:21:39 PM
    Author     : Alvaro Vargas
--%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
    
 String lsCuentaBancaria = request.getParameter("lstCuentaBancaria");
 String lsNoCheque = request.getParameter("txtNoCheque");
 String lsFecha01= request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02");
 String lsImporte = request.getParameter("txtImporte");
 String lsConcepto = request.getParameter("txtConcepto");
 String lsPolizaAsoc = request.getParameter("txtPolizaAsoc");
 String lsBeneficiario = request.getParameter("txtBeneficiario");
 String lsEstatus = request.getParameter("lstEstatus");
 String tipoFormato = request.getParameter("tipoFormato");
 String pocicion = request.getParameter("pocicion");
 %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <meta http-equiv="Cache-Control" content ="no-cache">
    <meta http-equiv="Pragma" content="no-cache"> 
    <meta http-equiv="expires" content="0" >



    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <script language="JavaScript" type='text/JavaScript'>
    
function imprimir(banImprimir){
   resultado.txtOperacion.value = banImprimir; //0 = vista preliminar y 1 = Imprimir
   if (revisa())
      resultado.submit();
}

function revisa()
{ // Valida que al menos un check box est� seleccionado y si es as�, confirmar su Revisi�n
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
   else        //Ning�n check box esta seleccionado
   {
    avanza=false;
   }
  if (avanza) resultado.btnAceptar.disabled=true;
 return avanza;
}//function
</script>
    <title>c721Resultado</title>
  </head>
  <body>
  
     <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques", "Cheque manual", true);</jsp:scriptlet>    
    
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    
    <br><br>
    
<!--    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>-->
    
    <br>
    
    <FORM name="resultado" id="resultado" method="post"  action="c721Control.jsp">
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
 <td width="100%" bgcolor="#B6D0F1"> <input type="radio" name="ambitoRegional" value="0AMBITOS" onClick="seleccionarOpcion(document.resultado, this);">
   Marcar todos &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
   <input name="ambitoRegional" type="radio" value="1AMBITOS" onClick="seleccionarOpcion(document.resultado, this);" checked>
   Des-marcar todos </td>
</tr>
<tr>
<td>

<!-- (Inicia codigo JAVA) -->
<%  

 String Query="";
 try{
    conexion=DaoFactory.getContabilidad();
    String lsCondicion=""; 
/*    if (!lsCuentaBancaria.equals("")) {
        lsCondicion = lsCondicion + " and cb.id_cuenta = " + lsCuentaBancaria +" ";
    }
    if (!lsNoCheque.equals("")) {
       lsCondicion = lsCondicion + " and ch.consecutivo="+lsNoCheque+" ";
    }  */
    /*ya estaba comentado
    if (!lsEjercicio.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(c.FECHA,'yyyy'),'yyyy')=to_date('"+lsEjercicio+"','yyyy')";
    } ya estaba comentado 
    */
 /*   if (!lsFecha01.equals("")) {
        lsCondicion=lsCondicion+ " and to_date(to_char(ch.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+lsFecha01+"','dd/MM/yyyy') and to_date(to_char(ch.fecha,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+lsFecha02+"','dd/MM/yyyy') ";
    }
    if (!lsImporte.equals("")) {
        lsCondicion=lsCondicion+ " and ch.importe ="+lsImporte + " ";       
    }
    if (!lsConcepto.equals("")) {
        lsCondicion = lsCondicion + " and upper(ch.referencia) like  upper('%" + lsConcepto +"%') ";
    }
    if (!lsPolizaAsoc.equals("")) {
        lsCondicion = lsCondicion + " and 'C' || p.consecutivo =upper('" + lsPolizaAsoc + "') ";
    }    
    if (!lsBeneficiario.equals("")) {
        lsCondicion = lsCondicion + " and upper(ch.beneficiario) like  upper('%" + lsBeneficiario +"%') ";
    }
    if (!lsEstatus.equals("")) {
        lsCondicion = lsCondicion + " and ch.estatus = " + lsEstatus + " ";
    }
*/    
    /* Pendiente
    if(!Autentifica.getPerfilAcceso().equals("1051")){
      lsCondicion=lsCondicion+ " and a.poliza_referencia is null " +
                 " and a.poliza_id not in " +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) ";
    }*/
/*     Query="select ROWNUM CONSFILA, sqlresul.* from "+
           "(select ch.cheque_id, cb.num_cuenta ||'   '||cb.nombre_cta nombre_cta, ch.consecutivo, to_char(ch.fechaCheque, 'dd/mm/yyyy') fechaCheque, "+
           "ch.importe, ch.poliza_id, ch.referencia, ch.beneficiario, decode(ch.estatus,0,'GENERADO',1,'IMPRESO',2,'REACTIVADO',3,'CANCELADO') estatus, tp.abreviatura || p.consecutivo consPoliza "+
           "from rf_tr_cheques ch, rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc, rf_tr_polizas p, rf_tc_tipos_polizas tp "+
           "where ch.cuenta_cheques_id = cc.cuenta_cheques_id and tp.tipo_poliza_id = p.tipo_poliza_id and cb.id_cuenta = cc.id_cuenta and ch.estatus in(0,1,2)  and ch.poliza_id=p.poliza_id " +
           " and cc.unidad_ejecutora="+lsUnidad+" and cc.entidad="+lsEntidad+" and cc.ambito="+lsAmbito+ " and p.ejercicio="+lsEjercicio+
           lsCondicion + " ) sqlresul order by sqlresul.consecutivo";  */

     Query="select ROWNUM CONSFILA, sqlresul.* from "+
           "(select (select max(cheque_id) + 1 from rf_tr_cheques) cheque_id,"+
           "(SELECT NOMBRE_CTA FROM RF_TC_CUENTAS_CHEQUES WHERE ID_CUENTA = " + lsCuentaBancaria + " AND CUENTA_CHEQUES_ID != 115) NOMBRE_CTA,"+
           "(SELECT MAX (CONSECUTIVO) + 1 FROM RF_TC_CUENTAS_CHEQUES WHERE ID_CUENTA = " + lsCuentaBancaria + " AND CUENTA_CHEQUES_ID != 115) CONSECUTIVO,"+
           "TO_CHAR(TO_DATE('" + lsFecha01 + "', 'dd/mm/yyyy'),'dd/mm/yyyy') fechacheque, " +
            lsImporte + " importe, 0 poliza_id, '" + lsConcepto + "' referencia, '" + lsBeneficiario + "' beneficiario, 'por generar' estatus, 0 conspoliza" +
           " from dual) sqlresul order by sqlresul.consecutivo";

              // System.out.println("Poliza asociada"+Query);
   
 //    out.println("Qry c721Resultado: " + Query);
 
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"NOMBRE_CTA-,CONSECUTIVO-,FECHACHEQUE-,IMPORTE-,REFERENCIA-,BENEFICIARIO-,ESTATUS-","17%,10%,10%,10%,17%,17%,10%","Nombre de Cuenta,Cheque,Fecha,Importe,Concepto,Beneficiario,Estatus","4,4,4,4,4"};
      String [] DefInput={"CHEQUE_ID","CHECKBOX","ambitos","LEFT"};

      xPaginacion.SeleccionarHorizontalQuery(conexion, DefQuery, DefAlias, DefInput, out, 0, "","", "", true);
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
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
       <INPUT type="hidden" name="txtfechaCheque" value=<%=lsFecha01%>>
       <INPUT type="hidden" name="txtEjercicio" value=<%=lsEjercicio%>>
       <INPUT type="hidden" name="txtOperacion" value="0">
       <INPUT type="hidden" name="hTipoFormato" value=<%=tipoFormato%>>
       <INPUT type="hidden" name="hPocicion" value=<%=pocicion%>>
       <INPUT type="hidden" name="lsCuentaBancaria" value=<%=lsCuentaBancaria%>>
       <INPUT type="hidden" name="lsImporte" value=<%=lsImporte%>>
       <INPUT type="hidden" name="lsConcepto" value='<%=lsConcepto%>'>
       <INPUT type="hidden" name="lsBeneficiario" value='<%=lsBeneficiario%>'>
<table width='100%'>
 <tr>
     <td width='100%' align="right">

    <!---   <input type='button' name='btnPreview' value='Vista Preliminar' class='boton' onclick="imprimir(0);">--->
       <input type='button' name='btnAceptar' value='Generar' class='boton' onclick="imprimir(1);">
    </td>
     <td width='10%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c721Filtro.jsp?','');">
     </td></tr>
 </table>
 </FORM>
  </body>
</html>