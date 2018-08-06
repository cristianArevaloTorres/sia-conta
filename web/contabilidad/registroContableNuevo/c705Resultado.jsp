<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>
<%@ page import="sia.db.sql.Sentencias,sia.db.dao.DaoFactory,sia.db.sql.Vista"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="xPaginacion" class="sia.utilerias.generador.hpaginacion" scope="page"/>
<jsp:useBean id="pbCargaCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCargaCheque" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsEntAmb="";
 if(lsEntidad.length()==1)
   lsEntAmb="0"+lsEntidad+lsAmbito;
 else
   lsEntAmb=lsEntidad+lsAmbito;
 String lsFechaActual=request.getParameter("txtfechaActual");
    
 String lsCuentaBancaria = request.getParameter("lstCuentaBancaria");
 String lsFecha01= request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02");
 String lsFecha03= request.getParameter("txtFecha03");
 String lsFecha04= request.getParameter("txtFecha04");
 String lsImporte = request.getParameter("txtImporte");
 String lsConcepto = request.getParameter("txtConcepto");
 String lsOperacionTipo = request.getParameter("txtOperacionTipo");
 String lsBeneficiario = request.getParameter("txtBeneficiario");
 Sentencias sentencia = null;
List<Vista> registrosCheque = null;
int consecutivoSigCheque = -1;
Map parametros = null;
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
   if (revisa()){
      document.getElementById('btnAceptar').disabled='true';
      resultado.submit();
   }
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
    <title>c705Resultado</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Resultado", "Aplicar", true);</jsp:scriptlet>    
    
    <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    
    <br>
    
    <FORM name="resultado" method="post"  action="c705AplicaCargaCheques.jsp">
    <!-- Encabezado de la tabla con resultado del filtro -->
<%
 String Query="";
 try{
    ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
    sentencia = new Sentencias(DaoFactory.CONEXION_CONTABILIDAD,Sentencias.XML);
    parametros = new HashMap();
    parametros.put("idCuenta",lsCuentaBancaria);
    parametros.put("ejercicio",controlReg.getEjercicio());     
    parametros.put("unidad", controlReg.getUnidad());
    parametros.put("ambito", controlReg.getAmbito());
    parametros.put("entidad", controlReg.getEntidad());
    registrosCheque = sentencia.registros("reportes.select.consecutivoSigChequeN", parametros);
    System.out.println(sentencia.getComando("reportes.select.consecutivoSigChequeN",parametros));
    for(Vista registroCheque:registrosCheque ){
      consecutivoSigCheque = registroCheque.getInt("ULTIMO_CONSECUTIVO");
    }
    registrosCheque = null;
    conexion=DaoFactory.getContabilidad();
    String lsCondicion="";
%>
     <br>
    <table width="100%">
    <tr><td  class="negrita"><font color="Red">ADVERTENCIA. El folio consecutivo puede variar si otros usuarios se encuentran afectando la misma chequera de manera simultánea.</font></td></tr>
    </table>
    <br>
    <table width="100%">
    <tr><td width="3%" class="negrita">Folio: </td>
       <td ><INPUT TYPE="text" NAME="txtNoCheque" SIZE='12' readonly="readonly" class='cajaTexto' value='<%=consecutivoSigCheque%>'> </td>
       </tr>
    </table>
    <br>
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


        
    if (!lsCuentaBancaria.equals("")) {
        lsCondicion = lsCondicion + " and cb.id_cuenta = " + lsCuentaBancaria +" ";
    }
    /*
    if (!lsEjercicio.equals("")) {
       lsCondicion = lsCondicion + " and to_date(to_char(c.FECHA,'yyyy'),'yyyy')=to_date('"+lsEjercicio+"','yyyy')";
    }  
    */
    if (!lsFecha01.equals("")) {
        lsCondicion=lsCondicion+ " and to_date(to_char(ch.fechaCheque,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+lsFecha01+"','dd/MM/yyyy') and to_date(to_char(ch.fechaCheque,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+lsFecha02+"','dd/MM/yyyy') ";
    }
    if (!lsFecha03.equals("")) {
        lsCondicion=lsCondicion+ " and to_date(to_char(ch.fechaCarga,'dd/MM/yyyy'),'dd/MM/yyyy')>=to_date('"+lsFecha03+"','dd/MM/yyyy') and to_date(to_char(ch.fechaCarga,'dd/MM/yyyy'),'dd/MM/yyyy')<=to_date('"+lsFecha04+"','dd/MM/yyyy') ";
    }    
    if (!lsImporte.equals("")) {
        lsCondicion=lsCondicion+ " and ch.importe ="+lsImporte + " ";          
    }
    if (!lsConcepto.equals("")) {
        lsCondicion = lsCondicion + " and upper(ch.concepto) like  upper('%" + lsConcepto +"%') ";
    }
    if (!lsOperacionTipo.equals("")) {
        lsCondicion = lsCondicion + " and ch.operacionTipo =" + lsOperacionTipo + " ";
    }
    if (!lsBeneficiario.equals("")) {
        lsCondicion = lsCondicion + " and upper(ch.beneficiario) like  upper('%" + lsBeneficiario +"%') ";
    }
        
    /* Pendiente
    if(!Autentifica.getPerfilAcceso().equals("1051")){
      lsCondicion=lsCondicion+ " and a.poliza_referencia is null " +
                 " and a.poliza_id not in " +
                 " (select distinct p.poliza_referencia from rf_tr_polizas p where p.id_catalogo_cuenta='"+lsIdCatalogo+"' and p.unidad_ejecutora='"+lsUnidad+"' and p.ambito="+lsAmbito+" and p.entidad="+lsEntidad+" and p.poliza_referencia is not null ) ";
    }*/

     Query="select ROWNUM CONSFILA, sqlresul.* from "+
           "(select ch.consecutivoCheques,cb.num_cuenta ||'   '||cb.nombre_cta nombre_cta, to_char(ch.fechaCheque, 'dd/mm/yyyy') fechaCheque,to_char(ch.fechaCarga, 'dd/mm/yyyy') || "+
           "(select ' ' || t.nombres || ' ' || t.apellido_pat || ' ' || t.apellido_mat from sia_admin.rh_tr_empleados t where t.num_empleado=ch.numEmpleado) fechaCarga, "+
           "ch.importe,  ch.concepto, ch.beneficiario, ch.operacionTipo,decode(ch.estatus,0,'GENERADO',1,'APLICADO') estatus, ch.operacion_pago || '-' || ch.operacion_pago_sup cxl "+
           "from rf_tr_cheques_carga ch, rf_tesoreria.rf_tr_cuentas_bancarias cb, rf_tc_cuentas_cheques cc "+
           "where cb.num_Cuenta LIKE '%'||ch.cuentaBancaria and cb.id_cuenta = cc.id_cuenta and ch.estatus=0 "+
           //"and cb.unidad_ejecutora=cc.unidad_ejecutora " +
           "and ch.unidad="+lsUnidad + " and ch.ambito="+lsEntAmb+ 
           "and cc.cuenta_cheques_id=   (SELECT CUENTA_CHEQUES_ID FROM rf_tc_cuentas_cheques WHERE TIPO_FORMATO = 'U' AND " + controlReg.getEjercicio() +
           " between extract(year from fecha_vig_ini) and extract(year from fecha_vig_fin) AND id_cuenta = cc.id_cuenta )  "+
           " and to_char(CH.FECHACHEQUE,'yyyy') = " + controlReg.getEjercicio() + " " +
           // " and cb.id_banco=2 " + 
//           "and cb.id_tipo_cta in(1,2)" +
           lsCondicion + " order by ch.consecutivoCheques) sqlresul ";
           //lsCondicion + " ) sqlresul ";
//            out.println("Query: " + Query);

   
      System.out.println("Qry: " + Query);
      String [] DefQuery={Query,"100%","LEFT","2",""};
      String [] DefAlias={"CONSFILA-,NOMBRE_CTA-,FECHACHEQUE-,FECHACARGA,IMPORTE-,CONCEPTO-,BENEFICIARIO-,OPERACIONTIPO-,ESTATUS-,CXL-","5%,15%,7%,15%,8%,15%,15%,5%,5%,10%","No.,Nombre de Cuenta,Fecha cheque,Nombre y Fecha de quien realizo la carga,Importe,Concepto,Beneficiario,Operacion tipo,Estatus, Documento","4,4,4,4,4"};
      String [] DefInput={"CONSECUTIVOCHEQUES","CHECKBOX","ambitos","LEFT"};
      

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



<table width='100%'>
 <tr><td width='60%'>&nbsp;</td>
     <td width='30%'>
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
       <INPUT type="hidden" name="txtEjercicio" value=<%=lsEjercicio%>>
       <INPUT type="hidden" name="txtOperacion" value="0">
        <input type='button' id='btnAceptar' value='Aplicar' class='boton' onclick="imprimir(1);">
    </td>
     <td width='10%'>
     <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c705Filtro.jsp','');" >
     </td></tr>
 </table>
  <input type='hidden' name='txtUnidad' value='<%=lsUnidad%>'>
  <input type='hidden' name='txtAmbito' value='<%=lsAmbito%>'>
  <input type='hidden' name='txtEntidad' value='<%=lsEntidad%>'>
  <input type='hidden' name='txtEjercicio' value='<%=lsEjercicio%>'> 
    
 </FORM>
  </body>
</html>