<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=windows-1252">
<title>Agregar polizas manuales.</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>
<script language="JavaScript" type="text/javascript">
 function window_openFormas(url) {
    window.open(url,'_self');
 }
 
 </script> 

</head>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/calendar.js" type="text/javascript"></script>
<script src="../../Librerias/Javascript/Capturador/dynapi/src/dynapi.js" type="text/javascript"></script>
<script src="../../Librerias/Javascript/Capturador/almacen.js" type="text/javascript"></script>
<script language="JavaScript" src="../../Librerias/Javascript/Capturador/fcgencValida2.js" type="text/javascript"></script>

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


<body topmargin=1 leftmargin=10>
<FORM Method="post">
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Pólizas", "Resultado", true);</jsp:scriptlet>    

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
// System.out.println("tiooffffpo"+pbPoliza.getTipo_poliza_id());
 sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
 // System.out.println(sbAutentifica.getNumeroEmpleado());
 
 Connection conexion=null;
 ResultSet resultsetAux=null;
 Statement statementAux =null;
 sbAutentifica.setPagina("c700Control");
 String operacion=request.getParameter("txtOperacion");
 
 String lsMaestroCons=null;
 int liPos=0;
 String lsMaestroId=null;
 String lsOrigen=null;

 if (!operacion.equals("C")){ //Si es cancelar se leen estos datos de la poliza a cancelar
   lsMaestroCons=request.getParameter("lstOperaciones");
   liPos=lsMaestroCons.lastIndexOf("-");
   lsMaestroId=lsMaestroCons.substring(0,liPos);
   lsOrigen=lsMaestroCons.substring(liPos+1);
 }
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
      
   String[] vsCuenta= request.getParameterValues("txtCuenta");
   String[] vsSubCuenta1= request.getParameterValues("txtSubCuenta1");
   String[] vsSubCuenta2= request.getParameterValues("txtSubCuenta2");
   String[] vsSubCuenta3= request.getParameterValues("txtSubCuenta3");
   String[] vsSubCuenta4= request.getParameterValues("txtSubCuenta4");
   String[] vsSubCuenta5= request.getParameterValues("txtSubCuenta5");
   String[] vsSubCuenta6= request.getParameterValues("txtSubCuenta6");
   String[] vsSubCuenta7= request.getParameterValues("txtSubCuenta7");   
   String[] vsSubCuenta8= request.getParameterValues("txtSubCuenta8");      
   String[] vsImporteDebe= request.getParameterValues("txtImporteDebe");
   String[] vsImporteHaber= request.getParameterValues("txtImporteHaber");
   String[] vsReferencia= request.getParameterValues("txtReferencia");
   String[] vsTipoOper= request.getParameterValues("txtTipoOper");
   
   String lsBeneficiario=request.getParameter("txtBeneficiario");

   String lsOrigenDocto=request.getParameter("lstOrigenDocto");
   String lsCxl=request.getParameter("lstOperacionCheque");
/*   
      for(int x= 0; x< vsCuenta.length; x++) {
        System.out.println("*"+vsCuenta[x]+"*"+vsSubCuenta1[x]+"*"+vsSubCuenta2[x]+"*"+vsSubCuenta3[x]+"*"+vsSubCuenta4[x]+"*"+vsSubCuenta5[x]+"*"+vsSubCuenta6[x]+"*"+vsImporteDebe[x]+"*"+vsImporteHaber[x]);
      }
*/
   
   // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
   pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
   estatus=pbEstadoCat.getEstatus();
   descripcion=pbEstadoCat.getDescripcion();
   
   if (!estatus.equals("1")){
     error="C";
     throw new Exception(" En estos momentos no es posible crear, modificar o cancelar polizas, ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
   }
     
   if (operacion.equals("A")){
      // Graba Poliza
      String lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
      pbPoliza.setPoliza_id(lsPolizaId);
      pbPoliza.setMaestro_operacion_id(lsMaestroId);
      pbPoliza.setOrigen(lsOrigen);;
      
      pbPoliza.setNum_empleado(Integer.toString(sbAutentifica.getNumeroEmpleado()));
      String lsMes=pbPoliza.getFecha().substring(3,5);
      pbPoliza.setMes(lsMes);
      
      pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
      pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));
      
      pbPoliza.insert_rf_tr_polizas(conexion);
      
     
      //Verifica si es una poliza de cheque
      if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){
          String lsChequeId=pbCheque.select_SEQ_rf_tr_cheques(conexion);
          String lsCuentas=request.getParameter("lstCuentas");
          String lsImporte=request.getParameter("txtTotDebe");
          String [] lsParametrosCuenta = new String [4];
          Double ldImporteModD=0.0; //13-feb-2011
          Double ldImporteModH=0.0;
          lsParametrosCuenta=lsCuentas.split("-");
          String lsDigitoVer=pbCheque.select_digito_verificador(conexion,lsParametrosCuenta[1]);
    //      System.out.println("dd"+lsParametrosCuenta[0]+lsParametrosCuenta[1]+lsParametrosCuenta[2]+"*");
          pbCheque.setCheque_id(lsChequeId);
          pbCheque.setCuenta_cheques_id(lsParametrosCuenta[0]);
          pbCheque.setConsecutivo(lsParametrosCuenta[1]);
          pbCheque.setAbreviatura(lsParametrosCuenta[2]);
          for(int x= 0; x< vsCuenta.length; x++) {
            if (vsTipoOper[x].equals("2")) {
              lsImporte=vsImporteHaber[x].replaceAll(",","");
              if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("2002") ) 
                ldImporteModH+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza  
              else  
              if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("3001") ) 
                ldImporteModH+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza                  
            }  
            else{
              lsImporte=vsImporteDebe[x].replaceAll(",","");
              if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("2002") ) 
                ldImporteModD+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza     
              else  
              if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("3001") ) 
                ldImporteModD+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza                  
            }  
          }
          if (ldImporteModH>0.0D)  
            pbCheque.setImporte(Double.toString(ldImporteModH)); 
          else
            if (ldImporteModD>0.0D) 
               pbCheque.setImporte(Double.toString(ldImporteModD));
            else
              pbCheque.setImporte(Double.toString(ldImporteModD*-1));                      
          pbCheque.setReferencia(pbPoliza.getConcepto()); //11042011 se cambio por concepto en vez de referencia
          pbCheque.setBeneficiario(lsBeneficiario);
          pbCheque.setNumEmpleado(Integer.toString(sbAutentifica.getNumeroEmpleado()));
          pbCheque.setPoliza_id(lsPolizaId);
          pbCheque.setDigitoVerificador(lsDigitoVer);
          pbCheque.setFechaCheque(pbPoliza.getFecha());
          pbCheque.setOrigenDocto(lsOrigenDocto);
          pbCheque.setCxl(lsCxl.equals("Sin cuenta por liquidar")?"Sin operacion":lsCxl);
          pbCheque.setCxlSup(lsCxl.equals("Sin cuenta por liquidar")?"Sin operacion":lsCxl);
          pbCheque.insert_rf_tr_cheques(conexion);
          pbCuentaCheque.setConsecutivo(lsParametrosCuenta[1]);
          pbCuentaCheque.update_rf_tc_cuentas_consecutivo(conexion,lsParametrosCuenta[0]);
      }
      
      //Graba detalle Poliza
      String lsDetalleId=null;
      String lsImporte=null;
      String lsCuentaId=null;
      String lsCuentaContable=null;
      String lsCatCuenta=pbPoliza.getId_catalogo_cuenta();
      String lsCuenta=null;
      for(int x= 0; x< vsCuenta.length; x++) {
        if (vsTipoOper[x].equals("2")) {
          pbDetallePoliza.setOperacion_contable_id("1");
          lsImporte=vsImporteHaber[x].replaceAll(",","");
        }  
        else{
          pbDetallePoliza.setOperacion_contable_id("0");
          lsImporte=vsImporteDebe[x].replaceAll(",","");
        }  
        pbDetallePoliza.setReferencia(vsReferencia[x]);  
        lsDetalleId=pbDetallePoliza.select_SEQ_rf_tr_detalle_polizas(conexion);
        pbDetallePoliza.setId_detalle(lsDetalleId);
        pbDetallePoliza.setImporte(lsImporte);
        pbDetallePoliza.setPoliza_id(lsPolizaId);
        lsCuenta="";
        if (vsCuenta[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsCuenta[x];
        if (vsSubCuenta1[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta1[x];
        if (vsSubCuenta2 == null || vsSubCuenta2[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta2[x];
        if (vsSubCuenta3 == null || vsSubCuenta3[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta3[x];
        if (vsSubCuenta4 == null || vsSubCuenta4[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta4[x];
        if (vsSubCuenta5 == null || vsSubCuenta5[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta5[x];
        if (vsSubCuenta6 == null || vsSubCuenta6[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta6[x];
        if (vsSubCuenta7 == null || vsSubCuenta7[x].equals("") ) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta7[x];
        //if ((pbPoliza.getEjercicio().equals("2012") && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))))
          if (vsSubCuenta8 == null || vsSubCuenta8[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta8[x];
        lsCuentaContable= pbCuenta.selectCuentaContableId(conexion,lsCuenta,lsCatCuenta,pbPoliza.getEjercicio()); 
        if (lsCuentaContable.equals(""))
          lsCuentasNoExis=lsCuentasNoExis+",";
        pbDetallePoliza.setCuenta_contable_id(lsCuentaContable);
        
        //Valida si la cuenta es de ultimo nivel
        pbCuenta.select_rf_tr_cuentas_contables(conexion,lsCuentaContable); //lsCuentaContable lleva cuentaContableId
        nivel=Integer.parseInt(pbCuenta.getNivel());

        if (nivel<5){
          error="C";
          throw new Exception("Se intento cargar polizas con una cuenta contable cuyo nivel es menor al ambito(el nivel minimo requerido es 5): "+lsCuenta);
        }        
        subCadena=subCadenaContable(nivel,lsCuenta);  //lsCuenta lleva cuentaContable
        // System.out.println("query "+query);
        query="select count(*) registros from rf_tr_cuentas_contables where cuenta_contable like '"+subCadena+"%'"+" and id_catalogo_cuenta = '"+lsCatCuenta+"' and extract(year from fecha_vig_ini) = " + pbPoliza.getEjercicio();     
        existenSubcuentas=pbCuenta.selectCountCuenta(conexion,query);     
        if (existenSubcuentas>1){
          error="C";
          throw new Exception("Se intento cargar polizas con una cuenta contable que no es de ultimo nivel: "+lsCuenta);
        }
        
        if(Double.valueOf(lsImporte)==0.0D){
          error="C";
          throw new Exception("Se intento cargar polizas con un importe en 0.0 en el detalle de la cuenta: "+lsCuenta);
        }
        
        //En esta instrucción realiza el llamado del metodo para realizar el insert en el detalle de la póliza
        pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion); 
        pbRegistraPoliza.setIdEvento("0");
        pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsCuentaContable),Double.valueOf(lsImporte),pbDetallePoliza.getOperacion_contable_id(),pbDetallePoliza.getFecha_afectacion(),"+");
        
      //  pbCuenta.update_rf_tr_cuentas_contables(conexion,lsCuentaContable,lsCatCuenta,lsMes,pbDetallePoliza.getOperacion_contable_id());
        
      }
   }   
   if (operacion.equals("M")){ //Si es Modificar realiza las siguientes instrucciones para actualizar la poliza
   
     String lsCxlFin = request.getParameter("lstOperacionChequeFin");
     lsTabla=pbDetallePoliza.select_rf_tr_detalle_poliza_res(conexion, pbPoliza.getPoliza_id());   
     for (int i=0;i<lsTabla.size();i++) {   
        lsDatos=lsTabla.get(i).toString().split("&"); //Separa el registro en los campos
        pbRegistraPoliza.setIdEvento("0");
        pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsDatos[6]),Double.valueOf(lsDatos[4]),lsDatos[0],lsDatos[2],"-");        
     }    
      if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){
        pbCheque.select_rf_tr_cheques_poliza(conexion, pbPoliza.getPoliza_id());
        pbCheque.delete_rf_tr_cheques(conexion, pbCheque.getPoliza_id());
        pbCheque.setBeneficiario(lsBeneficiario);  //domingo 23
        pbCheque.setReferencia(pbPoliza.getConcepto()); //11042011 se cambio por concepto en vez de referencia
        // Verificar lo de la fecha cheque hasta cuando se permite modificar una poliza cheque de acuerdo al estatus del cheque 
        pbCheque.setOrigenDocto(lsOrigenDocto);
        pbCheque.setCxl(lsCxl.equals("Sin cuenta por liquidar")?"Sin operacion":lsCxl);
        pbCheque.setCxlSup(lsCxlFin.equals("Sin cuenta por liquidar")?"Sin operacion":lsCxlFin);
      //String lsOrigenDocto=request.getParameter("lstOrigenDocto");
      //String lsCxl=request.getParameter("lstOperacionCheque");
      }  
     //En esta instrucción realiza el llamado del metodo para realizar el borrado del detalle de la póliza
     pbDetallePoliza.delete_rf_tr_detalle_poliza(conexion, pbPoliza.getPoliza_id());
     pbPoliza.delete_rf_tr_polizas(conexion,pbPoliza.getPoliza_id());    
       
      // Graba Poliza
      //String lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
     // pbPoliza.setPoliza_id(lsPolizaId);
      pbPoliza.setMaestro_operacion_id(lsMaestroId);
      pbPoliza.setOrigen(lsOrigen);;
      
      pbPoliza.setNum_empleado(Integer.toString(sbAutentifica.getNumeroEmpleado()));
      String lsMes=pbPoliza.getFecha().substring(3,5);
      pbPoliza.setMes(lsMes);
      
      //pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
      //pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));
      //System.out.println("tioopo"+pbPoliza.getTipo_poliza_id());
      pbPoliza.insert_rf_tr_polizas(conexion);
      // if (pbPoliza.getTipo_poliza_id().equals("2")) Lo movi mas abajo para obtener el nuevo posible importe 13-feb-2011
      //   pbCheque.insert_rf_tr_cheques(conexion);    Lo movi mas abajo para obtener el nuevo posible importe 13-feb-2011 
      Double ldImporteModD=0.0; //13-feb-2011
      Double ldImporteModH=0.0;
       
      //Graba detalle Poliza
      String lsDetalleId=null;
      String lsImporte=null;
      String lsCuentaId=null;
      String lsCuentaContable=null;
      String lsCatCuenta=pbPoliza.getId_catalogo_cuenta();
      String lsCuenta=null;
      for(int x= 0; x< vsCuenta.length; x++) {
        if (vsTipoOper[x].equals("2")) {
          pbDetallePoliza.setOperacion_contable_id("1");
          lsImporte=vsImporteHaber[x].replaceAll(",","");
          if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("2002") ) 
            ldImporteModH+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza  
          else  
          if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("3001") ) 
            ldImporteModH+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza              
        }  
        else{
          pbDetallePoliza.setOperacion_contable_id("0");
          lsImporte=vsImporteDebe[x].replaceAll(",","");
          if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("2002") ) 
            ldImporteModD+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza     
          else  
          if (vsCuenta[x].equals("1112") && vsSubCuenta1[x].equals("2") && vsSubCuenta5[x].equals("3001") ) 
            ldImporteModD+=Double.valueOf(lsImporte); //13-feb-2011 es el importe total de la poliza               
        }  
        pbDetallePoliza.setReferencia(vsReferencia[x]);  
        pbDetallePoliza.setFecha_afectacion(pbPoliza.getFecha());// CHL 18-08-2010 linea para que actualice en el detalle, la nueva fecha de la poliza capturada 
        lsDetalleId=pbDetallePoliza.select_SEQ_rf_tr_detalle_polizas(conexion);
        pbDetallePoliza.setId_detalle(lsDetalleId);
        pbDetallePoliza.setImporte(lsImporte);
        // pbDetallePoliza.setPoliza_id(lsPolizaId);
        lsCuenta="";
        if (vsCuenta[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsCuenta[x];
        if (vsSubCuenta1[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta1[x];
        if (vsSubCuenta2 ==null || vsSubCuenta2[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta2[x];
        if (vsSubCuenta3 ==null || vsSubCuenta3[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta3[x];
        if (vsSubCuenta4 ==null || vsSubCuenta4[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta4[x];
        if (vsSubCuenta5 ==null || vsSubCuenta5[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta5[x];
        if (vsSubCuenta6 ==null || vsSubCuenta6[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta6[x];
        if (vsSubCuenta7 ==null || vsSubCuenta7[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta7[x];
        // if ((pbPoliza.getEjercicio().equals("2012") && (lsCatCuenta.equals("3")||lsCatCuenta.equals("1")||lsCatCuenta.equals("2"))))
          if (vsSubCuenta8==null || vsSubCuenta8[x].equals("")) lsCuenta+="0000"; else lsCuenta+=vsSubCuenta8[x];        
        lsCuentaContable= pbCuenta.selectCuentaContableId(conexion,lsCuenta,lsCatCuenta, pbPoliza.getEjercicio()); 
        if (lsCuentaContable.equals(""))
          lsCuentasNoExis=lsCuentasNoExis+",";        
        pbDetallePoliza.setCuenta_contable_id(lsCuentaContable);
        
        //Valida si la cuenta es de ultimo nivel
        pbCuenta.select_rf_tr_cuentas_contables(conexion,lsCuentaContable); //lsCuentaContable lleva cuentaContableId
        nivel=Integer.parseInt(pbCuenta.getNivel());
        subCadena=subCadenaContable(nivel,lsCuenta);  //lsCuenta lleva cuentaContable
        //System.out.println("query "+query);
        query="select count(*) registros from rf_tr_cuentas_contables where cuenta_contable like '"+subCadena+"%'"+" and id_catalogo_cuenta = '"+lsCatCuenta+"' and extract(year from fecha_vig_ini) = " + pbPoliza.getEjercicio();     
        existenSubcuentas=pbCuenta.selectCountCuenta(conexion,query);     
        if (existenSubcuentas>1){
          throw new Exception("Se intento cargar polizas con una cuenta contable que no es de ultimo nivel: "+lsCuenta);
        }        

        if(Double.valueOf(lsImporte)==0.0D){
          error="C";
          throw new Exception("Se intento cargar polizas con un importe en 0.0 en el detalle de la cuenta: "+lsCuenta);
        }
        
        pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);
        pbRegistraPoliza.setIdEvento("0");
        pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsCuentaContable),Double.valueOf(lsImporte),pbDetallePoliza.getOperacion_contable_id(),pbPoliza.getFecha(),"+");
        
      //  pbCuenta.update_rf_tr_cuentas_contables(conexion,lsCuentaContable,lsCatCuenta,lsMes,pbDetallePoliza.getOperacion_contable_id());
        
      }

      if (pbPoliza.getTipo_poliza_id().equals("3") && pbPoliza.getBan_cheque().equals("1")){
        if (ldImporteModH>0.0D)  
          pbCheque.setImporte(Double.toString(ldImporteModH)); //es el if que se movio y se agrego el setImporte
        else
         if (ldImporteModD>0.0D) 
          pbCheque.setImporte(Double.toString(ldImporteModD));
         else
           pbCheque.setImporte(Double.toString(ldImporteModD*-1));      
        pbCheque.setFechaCheque(pbPoliza.getFecha());
        pbCheque.insert_rf_tr_cheques(conexion);            //el cheque se borra pero se inserta con el mismo id
      }  
      
   }   
   if (operacion.equals("C")){ //Si es Cancelar realiza las siguientes instrucciones para anular la póliza
   cierres = new ArrayList();
     String fechaCancelacion=request.getParameter("txtFechaCancelacion");
     String fechaOriginal = "";
     String fechaCancela = "";
     String lsPolizaSeleccionada=null;
     String lsDetalleId=null;
     String [] polizasSeleccionadas = request.getParameterValues("AMBITOS");
     for(int z=0; z < polizasSeleccionadas.length; z++){
        lsPolizaSeleccionada=polizasSeleccionadas[z].substring(0,polizasSeleccionadas[z].length()-1); 
        //pbDetallePoliza.delete_rf_tr_detalle_poliza(conexion, pbPoliza.getPoliza_id());
        //pbPoliza.delete_rf_tr_polizas(conexion,pbPoliza.getPoliza_id());    
       
        // Graba Poliza de cancelacion
        
        // pbPoliza.setMaestro_operacion_id(lsMaestroId);
        // pbPoliza.setOrigen(lsOrigen);;
      
        // pbPoliza.setNum_empleado(Integer.toString(sbAutentifica.getNumeroEmpleado()));
        //  String lsMes=pbPoliza.getFecha().substring(3,5);
        //
        //pbPoliza.setMes(lsMes);

         //En esta instrucción realiza el llamado del metodo para leer la información de bcPoliza
        //Metodo que lee la informacion de bcPoliza
        pbPoliza.select_rf_tr_polizas(conexion, lsPolizaSeleccionada);
        String lsTempo=letraPoliza(pbPoliza.getTipo_poliza_id());
        ltPolizas.add(lsTempo+pbPoliza.getConsecutivo()+" de "+ sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(pbPoliza.getMes())-1));
        fechaOriginal = pbPoliza.getFecha().substring(6,10)+ pbPoliza.getFecha().substring(3,5)+pbPoliza.getFecha().substring(0,2); // se pone la fecha original en formato yyyyddmm para poder compararla
        if(request.getParameter("selFecha").equals("original")){
           fechaCancelacion = pbPoliza.getFecha();
        }else{
           pbPoliza.setMes(fechaCancelacion.substring(3,5));
           pbPoliza.setFecha(fechaCancelacion);
           
           // se incluyo que mande el ejercicio
           pbPoliza.setEjercicio(fechaCancelacion.substring(6,10));  

           
        }
        fechaCancela = fechaCancelacion.substring(6,10)+fechaCancelacion.substring(3,5)+fechaCancelacion.substring(0,2); // se pone la fecha cancelacion en formato yyyyddmm para poder compararla
        if(Long.valueOf(fechaCancela) >= Long.valueOf(fechaOriginal)){
          //if (Integer.valueOf(pbPoliza.getMes())>=Integer.valueOf(fechaCancelacion.substring(3,5))){
             //if(Integer.valueOf(pbPoliza.getFecha().substring(0,2))>= Integer.valueOf(fechaCancelacion.substring(0,2))){
               pbPoliza.update_rf_tr_polizas_estatus(conexion,lsPolizaSeleccionada,"2");
               pbCheque.update_rf_tr_cheques_estatus(conexion, lsPolizaSeleccionada, "3"); // Se cambia el estatus del cheque a cancelado via el poliza_id de la poliza original
               
                String lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
                pbPoliza.setPoliza_id(lsPolizaId);      
                pbPoliza.setTipo_poliza_id("1"); //Preguntar al usuario si siempre que se cancela una poliza se hace una de diario, SI siempre es de diario
                lsTempo=letraPoliza(pbPoliza.getTipo_poliza_id());
                /*if(request.getParameter("selFecha").equals("seleccion")){
                  pbPoliza.setMes(fechaCancelacion.substring(3,5));
                  pbPoliza.setFecha(fechaCancelacion);
                }*/
                
                //En esta instrucción realiza el llamado del metodo para seleccionar el consecutivo de la poliza en la clase bcPoliza.java
                pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
                ltPolizasCan.add(lsTempo+pbPoliza.getConsecutivo()+" de "+ sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(pbPoliza.getMes())-1));
                pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));
                pbPoliza.setClasificacion_poliza_id("3"); //checarlo con el usuario      
                pbPoliza.setPoliza_referencia(lsPolizaSeleccionada);     
                pbPoliza.setNum_empleado(String.valueOf(sbAutentifica.getNumeroEmpleado()));
                //En esta instrucción realiza el llamado del metodo para realizar el insert en la póliza
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

                  //pbCuenta.getCuenta_contable().substring(6,10);
                  //select_cierre_mensual(Connection con, pbPoliza.getUnidad_ejecutora(),String pAmbito,String pEntidad,String pEjecicio,String pIdCatalogoCuenta,String pMes, String pPrograma)
                  cierres = pbCierreMensual.select_cierre_mensual(conexion, pbPoliza.getUnidad_ejecutora(),pbPoliza.getAmbito(),pbPoliza.getEntidad(),fechaCancelacion.substring(6,10),pbCuenta.getId_catalogo_cuenta(),fechaCancelacion.substring(3,5),pbCuenta.getCuenta_contable().substring(5,9));
                  if(cierres.contains("0") || cierres.contains("1")){
                    if(cierres.contains("2")){
                      throw new Exception(" El mes de "+sia.libs.formato.Fecha.getNombreMes(Integer.valueOf(fechaCancelacion.substring(3,5))-1) +" que se requiere afectar ya se encuentra cerrado definitivamente.");
                    }else{
                      
                      pbRegistraPoliza.setIdEvento("0");
                      //Verificar si el mes está abierto por cuenta contable
                      pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsDatos[6]),(Double.valueOf(lsDatos[4])*-1),lsDatos[0],fechaCancelacion,"+");        
                      //pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsDatos[6]),(Double.valueOf(lsDatos[4])*-1),lsDatos[0],lsDatos[2],"+");   
                      pbDetallePoliza.setCuenta_contable_id(lsDatos[6]);
                                            
                      if(request.getParameter("selFecha").equals("seleccion"))
                        pbDetallePoliza.setFecha_afectacion(fechaCancelacion);
                      else
                        pbDetallePoliza.setFecha_afectacion(lsDatos[2]);
                     // pbDetallePoliza.setId_detalle(lsDatos[3]);
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
            /* }else{
               throw new Exception("La fecha de cancelación de la póliza  es menor al de la original");
             }
          }else{
            throw new Exception("La fecha de cancelación de la póliza  es menor al de la original");
          }*/
        }else{
           throw new Exception("La fecha de cancelación de alguna  póliza  es menor a la original");
        }
                    
      }
   }   
      
   if (!lsCuentasNoExis.equals(""))
     throw new Exception("Las cuentas contables " + lsCuentasNoExis + " no existen, favor de darlas de altas. ");
   conexion.commit();  
   if (operacion.equals("A")){
    String lsTipoPoliza=letraPoliza(pbPoliza.getTipo_poliza_id());
    
    
%>
<p class='texto02'>La póliza se generó correctamente en la: </p>
<p class='texto02'>&nbsp;&nbsp;&nbsp;Unidad Ejecutora: <b><font color='#003399'><%=pbPoliza.getUnidad_ejecutora()%></font></b> </p>
<p class='texto02'>&nbsp;&nbsp;&nbsp;Entidad: <b><font color='#003399'><%=pbPoliza.getEntidad()%></font></b> </p>
<p class='texto02'>&nbsp;&nbsp;&nbsp;Ambito: <b><font color='#003399'><%=pbPoliza.getAmbito()%></font></b> </p>
<p class='texto02'>&nbsp;&nbsp;&nbsp;Consecutivo: <b><font color='#003399'><%=lsTipoPoliza.toUpperCase()+pbPoliza.getConsecutivo()%></font></b> </p>
<br><br><br>

<%
   }
   if (operacion.equals("M")){
     String lsTipoPoliza=letraPoliza(pbPoliza.getTipo_poliza_id());
%>
<p>La póliza se modificó correctamente en la: </p>
<p>&nbsp;&nbsp;&nbsp;Unidad Ejecutora: <b><font color='#003399'><%=pbPoliza.getUnidad_ejecutora()%></font></b> </p>
<p>&nbsp;&nbsp;&nbsp;Entidad: <b><font color='#003399'><%=pbPoliza.getEntidad()%></font></b> </p>
<p>&nbsp;&nbsp;&nbsp;Ambito: <b><font color='#003399'><%=pbPoliza.getAmbito()%></font></b> </p>
<p>&nbsp;&nbsp;&nbsp;Consecutivo: <b><font color='#003399'><%=lsTipoPoliza.toUpperCase()+pbPoliza.getConsecutivo()%></font></b> </p>
<br><br><br>

<%
  }
  if (operacion.equals("C")){
    for (int i=0;i<ltPolizas.size();i++) {   
%>
<p>La póliza <b><font color='#003399'><%=ltPolizas.get(i).toString()%></font></b> se canceló y generó una póliza de cancelación con número de consecutivo <b><font color='#003399'> <%=ltPolizasCan.get(i).toString()%></font></b> para la: </p>
<br><br>
<%
    }    
     
%>
<p class='texto02'>Unidad Ejecutora: <b><font color='#003399'><%=pbPoliza.getUnidad_ejecutora()%></font></b> </p>
<p class='texto02'>Entidad: <b><font color='#003399'><%=pbPoliza.getEntidad()%></font></b> </p>
<p class='texto02'>Ambito: <b><font color='#003399'><%=pbPoliza.getAmbito()%></font></b> </p>

<%
  }
  
}
catch(Exception E){E.printStackTrace();
    conexion.rollback(); 
    //sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
    //sbAutentifica.enviaCorreo();
    //System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
    if (!error.equals("C")){
%>
     <p><%=lsCuentasNoExis%></p>
    <p>Ha ocurrido un error:</p>
<%
  if(E.getMessage().indexOf("ORA-00001")!=-1){
%>
<p><b>Se ha asignado el mismo consecutivo que otra póliza lo cual no es permitido, por lo tanto es necesario volver a capturar la póliza. </b>
<%
  }else{
   if(E.getMessage().indexOf("ORA-00060")!=-1){
%>
    <p><b>Se ha presentando un interbloqueo al querer afectar a la misma cuenta al mismo tiempo, por lo tanto es necesario volver a capturar la póliza.</b>
<%
  }else
%>
    <p><b><%=E.getMessage()%></b>
<%}%>
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

<table width="100%">
 <tr><td width="73%">&nbsp;</td>
     <td width="10%">
         <%
         
         String boton=request.getParameter("capturaFormas")==null?"capturador":request.getParameter("capturaFormas");
          if(boton.equals("captura")){%>
                <input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:LlamaPagina('filtroGeneral.jsp?opcion=irCapturaForma&idCatalogoCuenta=1','');" >       
         <%}else if(boton.equals("modifica")){%>
                <input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:LlamaPagina('filtroGeneral.jsp?opcion=irModificaPolizaForma&idCatalogoCuenta=1','');" >
         <%}else {%>
               <input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="javascript:LlamaPagina('c700OpcionesPolizas.jsp','');" >
         <%
          }
         %>
   </td>
  </tr>
</table>
</FORM>
</body>
</html>
