<%-- 
    Document   : 750Control
    Created on : 3/10/2017, 07:00:27 PM
    Author     : erika.lopezob
--%>

<%@page import="sia.comun.SendMail"%>
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>c750Resultado</title>
<link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
<script language="JavaScript" type="text/javascript">
function window_openFormas(url) {
    window.open(url,'_self');
 }

</script> 

</head>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

<jsp:useBean id="pbCierreMensual" class = "sia.rf.contabilidad.registroContableNuevo.bcCierresMensuales" scope="page" />
<jsp:useBean id="pbPoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page" />
<jsp:setProperty name="pbPoliza" property="poliza_id" param="txtPolizaId" />
<jsp:setProperty name="pbPoliza" property="unidad_ejecutora" param="txtUniEje" />
<jsp:setProperty name="pbPoliza" property="ambito" param="txtAmbito" />
<jsp:setProperty name="pbPoliza" property="pais" param="txtPais" />
<jsp:setProperty name="pbPoliza" property="entidad" param="txtEntidad" />
<jsp:setProperty name="pbPoliza" property="consecutivo" param="txtConsecutivo" />
<jsp:setProperty name="pbPoliza" property="tipo_poliza_id" param="btrTipoPoliza" />
<jsp:setProperty name="pbPoliza" property="ban_cheque" param="txtBanCheque" />
<jsp:setProperty name="pbPoliza" property="maestro_operacion_id" param="txtMaestroOpe" />
<jsp:setProperty name="pbPoliza" property="fecha" param="txtFechaCrea" />
<jsp:setProperty name="pbPoliza" property="concepto" param="txaConcepto"/>
<jsp:setProperty name="pbPoliza" property="referencia" param="txaRefGral" />
<jsp:setProperty name="pbPoliza" property="fecha_afectacion" param="txtFechaAfec" />
<jsp:setProperty name="pbPoliza" property="clasificacion_poliza_id" param="txtClaPol" />
<jsp:setProperty name="pbPoliza" property="poliza_referencia" param="txtPolRef" />
<jsp:setProperty name="pbPoliza" property="num_empleado" param="txtNumEmp" />
<jsp:setProperty name="pbPoliza" property="global" param="txtGlobal" />
<jsp:setProperty name="pbPoliza" property="ejercicio" param="txtEjercicio" />
<jsp:setProperty name="pbPoliza" property="mes" param="txtMes" />
<jsp:setProperty name="pbPoliza" property="origen" param="txtOrigen" />
<jsp:setProperty name="pbPoliza" property="id_catalogo_cuenta" param="txtCatCuenta" />
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbDetallePoliza" class = "sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza" scope="page" />
<jsp:setProperty name="pbDetallePoliza" property="operacion_contable_id"  param="txtOpeConId" />
<jsp:setProperty name="pbDetallePoliza" property="referencia"    param="txtReferencia" />
<jsp:setProperty name="pbDetallePoliza" property="fecha_afectacion" param="txtFechaCrea" />
<jsp:setProperty name="pbDetallePoliza" property="id_detalle" param="txtDetalleId" />
<jsp:setProperty name="pbDetallePoliza" property="importe"   param="txtImporte" />
<jsp:setProperty name="pbDetallePoliza" property="poliza_id" param="txtPolizaId" />
<jsp:setProperty name="pbDetallePoliza" property="cuenta_contable_id" param="txtCuentaConId" />

<jsp:useBean id="pbCheque" class = "sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>

<jsp:useBean id="pbRegistraPoliza" class = "sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas" scope="page" />

<jsp:useBean id="pbCuenta" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>

<jsp:useBean id="pbCuentaCheque" class = "sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>

<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>
<jsp:useBean id="controlRegistro" class= "sia.rf.contabilidad.registroContable.acciones.ControlRegistro" scope="session" />


<!--<body>-->
<FORM Method="post">
 <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cheques cancelados", "Resultado", true);</jsp:scriptlet>    

<%!
    public String subCadenaContable(int nivel, String cuentaContable){
      String res="";
       if (nivel==5)
          res=cuentaContable.substring(0,17);           
       else
       if (nivel==6)
          res=cuentaContable.substring(0,21); 
       else    
       if (nivel==7)
          res=cuentaContable.substring(0,25); 
       else
       if (nivel==8)    
          res=cuentaContable.substring(0,29);
       else   
          res=cuentaContable.substring(0);
       return res;              
    }    

   private String letraPoliza(String lsTipoPoliza) { 
     if (lsTipoPoliza.equals("1"))
       lsTipoPoliza="D";
     else  
     if (lsTipoPoliza.equals("2"))
       lsTipoPoliza="E";
     else  
     if (lsTipoPoliza.equals("3"))
       lsTipoPoliza="E";
     else  
     if (lsTipoPoliza.equals("4"))
       lsTipoPoliza="I";      
     return lsTipoPoliza;
   } 
%>

<%
 String opcion= controlRegistro.getPagina();
 sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
 Connection conexion=null;
 ResultSet resultsetAux=null;
 Statement statementAux =null;
 sbAutentifica.setPagina("c750Control");
 String lsMaestroCons=null;
 int liPos=0;
 String lsMaestroId=null;
 String lsOrigen=null;
 List lsTabla=null;
 List ltPolizas=null;
 ltPolizas= new ArrayList();
 List ltPolizasCan=null;
 ltPolizasCan= new ArrayList();
 String lsPolizasCan="";
 String [] lsDatos = new String [10];
 String lsCuentasNoExis="";
 String estatus="0";
 String descripcion="";
 int nivel=0;
 String subCadena="";
 String query="";
 int existenSubcuentas=0;
 String error="0";
 List cierres = null;
try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    //String[] vsTipoOper= request.getParameterValues("txtTipoOper");
    //String lsBeneficiario=request.getParameter("txtBeneficiario");
    //String lsOrigenDocto=request.getParameter("lstOrigenDocto");
    //String lsCxl=request.getParameter("lstOperacionCheque");
   // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
   pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
   estatus=pbEstadoCat.getEstatus();
   descripcion=pbEstadoCat.getDescripcion();
   if (!estatus.equals("1")){
     error="C";
     throw new Exception(" En estos momentos no es posible cancelar polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
   }
   ///////////////////////////CANCELAR/////////////
   cierres = new ArrayList();
     String fechaCancelacion=request.getParameter("txtFechaCancelacion");
     String fechaOriginal = "";
     String fechaCancela = "";
     String lsPolizaSeleccionada=null;
     String lsIdChequeConta=null;
     String lsDetalleId=null;
     String mensajeCorreo="";
     String [] polizasSeleccionadas = request.getParameterValues("AMBITOS");
     String [] cheq=new String[polizasSeleccionadas.length];
        ArrayList chequesCancelados=new ArrayList();
        ArrayList chequesCanceladosDat=new ArrayList();
     for(int z=0; z < polizasSeleccionadas.length; z++){      
        lsPolizaSeleccionada=(polizasSeleccionadas[z]).substring(0,polizasSeleccionadas[z].indexOf('-'));
        lsIdChequeConta=(polizasSeleccionadas[z]).substring(polizasSeleccionadas[z].indexOf('-')+1,polizasSeleccionadas[z].length()-1);  
        System.out.println("chequeid: " + lsIdChequeConta);
        //cheq[z]=lsIdChequeConta;
        chequesCancelados.add(pbCheque.select_consecutivo_ARE(conexion, lsIdChequeConta));
        //cheq[z]=pbCheque.select_consecutivo_ARE(conexion, lsIdChequeConta);
 //       chequesCancelados.add(lsIdChequeConta);
        pbCheque.update_rf_tr_chequesARE_estatus(conexion, lsIdChequeConta, "3"); // Se cambia el estatus del cheque a cancelado via cheque_id
        pbCheque.select_rf_tr_cheques(conexion, lsIdChequeConta);
        //System.out.println("beneficiario:"+pbCheque.getBeneficiario().toString());
        mensajeCorreo=mensajeCorreo+"No. Cheque: "+chequesCancelados.get(z).toString()+", Importe: $" +pbCheque.getImporte().toString()+" Beneficiario: "+pbCheque.getBeneficiario().toString()+", Fecha de Cheque: "+pbCheque.getFechaCheque().toString()+" Referencia: " +pbCheque.getReferencia().toString()+". \n \n";
        System.out.println("mensaje Correo " + mensajeCorreo);
        }//for(int z=0; z < polizasSeleccionadas.length; z++)
   conexion.commit();  
   /*Número de cheque, banco, nombre, monto y fecha. Y si es posible la cxl con el que está relacionado*/
   
   /*en calidad y produccion*/
   SendMail mail2 = new SendMail();
   mail2.dePara("", "jaime.ramos@senado.gob.mx");
   mail2.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);  
   SendMail mail3 = new SendMail();
   mail3.dePara("", "atenogenes.yuen@senado.gob.mx");
   mail3.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
   /*solamente en calidad */
   SendMail mail4 = new SendMail();
   mail4.dePara("", "elia.valencia@senado.gob.mx");
   mail4.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
 
   /* solamente para caso de producción: 
   SendMail mail = new SendMail();
   mail.dePara("", "jose.cortes@senado.gob.mx");
   mail.asuntoMensaje("Cancelación de cheque por ARE", "AVISO. Se realizó la cancelación del (los) cheque(s): \n \n "+mensajeCorreo);
   SendMail mail5 = new SendMail();
   mail5.dePara("", "samanta.lira@senado.gob.mx");
   mail5.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
   SendMail mail6 = new SendMail();
   mail6.dePara("", "etna.martinez@senado.gob.mx");
   mail6.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
   SendMail mail7 = new SendMail();
   mail7.dePara("", "claudia.luna@senado.gob.mx");
   mail7.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
   SendMail mail8 = new SendMail();
   mail8.dePara("", "santiago.ardavin@senado.gob.mx");
   mail8.asuntoMensaje("Cancelación de cheque por ARE", "Se realizó la cancelación del (los) cheque(s): "+mensajeCorreo);
  */
   
   for (int i=0;i<chequesCancelados.size();i++) {
%>
<p>Se canceló el cheque número <b><font color='#003399'> <%=chequesCancelados.get(i).toString()%></font></b> </p>
<br><br>
<%
    }    
  
}
catch(Exception E){
    conexion.rollback(); 
/*    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
    sbAutentifica.enviaCorreo();*/
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
    if (!error.equals("C")){
%>
     <p><%=lsCuentasNoExis%></p>
    <p>Ha ocurrido un error:</p>
    <p><b><%=E.getMessage()%></b>

<p>Si es necesario reportelo al Administrador del Sistema.</p>
    <!-- <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>avor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>-->
<%
   }else{
%>
     <p>Ha ocurrido un error.</p>
     <p><%=E.getMessage()%></p>
<%
   }
   }
   finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }  
     if (resultsetAux != null){
        resultsetAux.close();
        resultsetAux=null;
     }
     if (statementAux != null){
        statementAux.close();
           statementAux=null;
      }           
    }
%>
 <!--<table width="100%">
 <tr><td width="73%">&nbsp;</td>
     <td width="10%">
    <INPUT TYPE="button" NAME="btnAceptar" VALUE="Aceptar" class=boton onClick="javascript:<%=request.getParameter("paginaRegreso")==null?"LlamaPagina('c700opcionesPolizas.jspx','');"  :  "window_openFormas('"+request.getParameter("paginaRegreso")+"');" %> ">
   </td>
  </tr>
</table>
-->
</FORM>
</body>
</html>


