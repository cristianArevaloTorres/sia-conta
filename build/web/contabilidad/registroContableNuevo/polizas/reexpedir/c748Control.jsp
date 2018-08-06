<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>c748Resultado</title>
<link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
<script language="JavaScript" type="text/javascript">
function window_openFormas(url) {
    window.open(url,'_self');
 }

</script> 

</head>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

bcCierresMensuales
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
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cheques reexpedir", "Resultado", true);</jsp:scriptlet>    

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
 sbAutentifica.setPagina("c748Control");
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
     String [] polizasSeleccionadas = request.getParameterValues("AMBITOS");
     ArrayList chequesCancelados=new ArrayList();
     for(int z=0; z < polizasSeleccionadas.length; z++){      
        lsPolizaSeleccionada=(polizasSeleccionadas[z]).substring(0,polizasSeleccionadas[z].indexOf('-'));
        lsIdChequeConta=(polizasSeleccionadas[z]).substring(polizasSeleccionadas[z].indexOf('-')+1,polizasSeleccionadas[z].length()-1);  
        pbPoliza.select_rf_tr_polizas(conexion, lsPolizaSeleccionada);
        pbCheque.select_rf_tr_cheques(conexion,lsIdChequeConta);
        chequesCancelados.add(pbCheque.getConsecutivo());
        String lsTempo=letraPoliza(pbPoliza.getTipo_poliza_id());
        ltPolizas.add(lsTempo+pbPoliza.getConsecutivo()+" de "+ sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(pbPoliza.getMes())-1));
        fechaOriginal = pbPoliza.getFecha().substring(6,10)+ pbPoliza.getFecha().substring(3,5)+pbPoliza.getFecha().substring(0,2); // se pone la fecha original en formato yyyyddmm para poder compararla
        if(request.getParameter("selFecha").equals("original")){
           fechaCancelacion = pbPoliza.getFecha();
        }else{
           pbPoliza.setMes(fechaCancelacion.substring(3,5));
           pbPoliza.setFecha(fechaCancelacion);
           pbPoliza.setEjercicio(fechaCancelacion.substring(6,10));           
        }
        fechaCancela = fechaCancelacion.substring(6,10)+fechaCancelacion.substring(3,5)+fechaCancelacion.substring(0,2); // se pone la fecha cancelacion en formato yyyyddmm para poder compararla
        if(Long.valueOf(fechaCancela) >= Long.valueOf(fechaOriginal)){
               pbPoliza.update_rf_tr_polizas_estatus(conexion,lsPolizaSeleccionada,"2");
               pbCheque.update_rf_tr_cheques_estatus(conexion, lsPolizaSeleccionada, "3"); // Se cambia el estatus del cheque a cancelado via el poliza_id de la poliza original
// REEXPEDIR---
               if(opcion.equals("reexpedir")) {
                   System.out.println("uno");
                 pbCheque.update_rf_tr_cheques_reexpedir(conexion,lsPolizaSeleccionada);
                   System.out.println("dos");
                 pbCheque.update_rf_presupuesto_s2_rf_tr_cheque_nominativo(conexion,lsIdChequeConta);
                   System.out.println("tres");
                 pbCheque.update_rf_tr_operaciones_cheques(conexion,lsIdChequeConta);
                   System.out.println("cuatro");
               }  
               //reexpedir
               String lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
               pbPoliza.setPoliza_id(lsPolizaId);      
               pbPoliza.setTipo_poliza_id("1"); //Preguntar al usuario si siempre que se cancela una poliza se hace una de diario, SI siempre es de diario
               lsTempo=letraPoliza(pbPoliza.getTipo_poliza_id());
               pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
               ltPolizasCan.add(lsTempo+pbPoliza.getConsecutivo()+" de "+ sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(pbPoliza.getMes())-1));
               pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));
               pbPoliza.setClasificacion_poliza_id("3"); //checarlo con el usuario      
               pbPoliza.setPoliza_referencia(lsPolizaSeleccionada);     
               pbPoliza.setNum_empleado(String.valueOf(sbAutentifica.getNumeroEmpleado()));
               pbPoliza.insert_rf_tr_polizas(conexion);
               //Graba detalle Poliza
               lsTabla=pbDetallePoliza.select_rf_tr_detalle_poliza_res(conexion, lsPolizaSeleccionada);   
               for (int i=0;i<lsTabla.size();i++) {   
               lsDatos=lsTabla.get(i).toString().split("&"); //Separa el registro en los campos                
               pbCuenta.select_rf_tr_cuentas_contables(conexion,lsDatos[6]);
               //Cuando el ejercicio de la fecha de cancelacion es diferente al ejercicio de la fecha original de la poliza
               //Debera buscar el id de la cuenta contable del ejercicio de la fecha de cancelacion
                if (!fechaOriginal.substring(0,4).equals(fechaCancelacion.substring(6,10))){
                    lsDatos[6]=pbCuenta.selectCuentaContableId(pbCuenta.getCuenta_contable(), pbCuenta.getId_catalogo_cuenta(), fechaCancelacion.substring(6,10), pbCuenta.getNivel());
                    pbCuenta.select_rf_tr_cuentas_contables(conexion,lsDatos[6]);
                }
                cierres = pbCierreMensual.select_cierre_mensual(conexion, pbPoliza.getUnidad_ejecutora(),pbPoliza.getAmbito(),pbPoliza.getEntidad(),fechaCancelacion.substring(6,10),pbCuenta.getId_catalogo_cuenta(),fechaCancelacion.substring(3,5),pbCuenta.getCuenta_contable().substring(5,9));
                if(cierres.contains("0") || cierres.contains("1")){
                  if(cierres.contains("2")){
                    throw new Exception(" El mes de "+sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(fechaCancelacion.substring(3,5))-1) +" que se requiere afectar ya se encuentra cerrado definitivamente.");
                  }else{
                      
                    pbRegistraPoliza.setIdEvento("0");
                    //Verificar si el mes está abierto por cuenta contable
                    pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsDatos[6]),(Double.valueOf(lsDatos[4])*-1),lsDatos[0],fechaCancelacion,"+");        
                    pbDetallePoliza.setCuenta_contable_id(lsDatos[6]);
                    if(request.getParameter("selFecha").equals("seleccion"))
                      pbDetallePoliza.setFecha_afectacion(fechaCancelacion);
                    else
                      pbDetallePoliza.setFecha_afectacion(lsDatos[2]);
                    pbDetallePoliza.setImporte(Double.toString(Double.valueOf(lsDatos[4])*-1));
                    pbDetallePoliza.setOperacion_contable_id(lsDatos[0]);
                    pbDetallePoliza.setPoliza_id(lsPolizaId);
                    pbDetallePoliza.setReferencia(lsDatos[1]);
                    lsDetalleId=pbDetallePoliza.select_SEQ_rf_tr_detalle_polizas(conexion);
                    pbDetallePoliza.setId_detalle(lsDetalleId);       
                    pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);
                  }
                 }
                 else{
                    throw new Exception(" El mes de "+sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(fechaCancelacion.substring(3,5))-1) +" que se requiere afectar no se encuentra abierto.");
                 }
                  
                }  
        }else{
           throw new Exception("La fecha de cancelación de algun  cheque  es menor a la original");
        }//if(Long.valueOf(fechaCancela) >= Long.valueOf(fechaOriginal))
                    
      }//for(int z=0; z < polizasSeleccionadas.length; z++)
   //////////////////////FIN CANCELAR  
   if (!lsCuentasNoExis.equals(""))
     throw new Exception("Las cuentas contables " + lsCuentasNoExis + " no existen, favor de darlas de altas. ");
   conexion.commit();  
   for (int i=0;i<ltPolizas.size();i++) {   
%>
<p>La póliza <b><font color='#003399'><%=ltPolizas.get(i).toString()%></font></b> se canceló y generó una póliza de cancelación con número de consecutivo <b><font color='#003399'> <%=ltPolizasCan.get(i).toString()%></font></b> </p>
<p>Se canceló el cheque número <b><font color='#003399'> <%=chequesCancelados.get(i).toString()%></font></b> </p>

<br><br>
<%
    }    
     
%>
<p> Para la </p>
<p class='texto02' style='color:#003399'>Unidad Ejecutora: <b><%=pbPoliza.getUnidad_ejecutora()%></b> </p>
<p class='texto02'>Entidad: <b><font color='#003399'><%=pbPoliza.getEntidad()%></font></b> </p>
<p class='texto02'>Ambito: <b><font color='#003399'><%=pbPoliza.getAmbito()%></font></b> </p>

<%

  
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
<% 
  if(E.getMessage().indexOf("ORA-00001")!=-1){
%>
<p><b>Se ha asignado el mismo consecutivo que otra póliza lo cual no es permitido, por lo tanto es necesario volver a capturar la póliza. </b>
<%}
  else{
    if(E.getMessage().indexOf("ORA-00060")!=-1){
%>
    <p><b>Se ha presentando un interbloqueo al querer afectar a la misma cuenta al mismo tiempo, por lo tanto es necesario volver a capturar la póliza.</b>
<%}
    else{
%>
    <p><b><%=E.getMessage()%></b>
<%} }%>
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

