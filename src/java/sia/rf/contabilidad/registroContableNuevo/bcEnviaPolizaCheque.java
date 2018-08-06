package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import sia.db.dao.DaoFactory;
import sia.rf.contabilidad.registroContableNuevo.bcPoliza;
import sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
import sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques;
import sia.rf.contabilidad.registroContableNuevo.bcCheque;
import sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo;
import sun.jdbc.rowset.CachedRowSet;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;

public class bcEnviaPolizaCheque {
  private bcPoliza pbPoliza=null;
  private bcDetallePoliza pbDetallePoliza=null;
  private bcMaestroOperaciones pbMaestroOperaciones=null;
  private Polizas pbRegistraPoliza=null;
  private bcCargaCheque pbCargaCheque=null;
  private bcCuentasCheques pbCuentasCheques=null;
  private bcCheque pbCheque=null;
  private bcEstadoCatalogo bcEstadoCat=null;
  private CachedRowSet crsDetalleOperaciones=null;  
  private ResultSet crsDetalleCuentasCheques=null;
  private bcCuentaContable pbCuenta=null;
    
  public bcEnviaPolizaCheque() {
    pbPoliza = new bcPoliza();
    pbDetallePoliza = new bcDetallePoliza();
    pbMaestroOperaciones = new bcMaestroOperaciones();
    pbRegistraPoliza = new Polizas();
    pbCargaCheque= new bcCargaCheque();
    pbCuentasCheques = new bcCuentasCheques();
    pbCheque= new bcCheque();
    bcEstadoCat = new bcEstadoCatalogo();
    pbCuenta = new bcCuentaContable(); 
  }

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
    
  public String procesa(Connection conexion, String chequesId [])  throws Exception,SQLException{
   String lsCatCuenta="1";  //Ver que Luz me lo pase como parametro
   String lsPolizaId="";
   String lsEntidad="";
   String lsDetalleId;
   String lsCuentaContableId="";
   String lsOperacionContableId="";
   String consecutivoCheque="";    //Separa cheques a aplicar poliza
   String ejercicio="";
   String mes="";
   String lsSeqChequeId="";
   String resultado="";
   String lsDigVerificador="-1";
   String estatus="";
   String descripcion="";
   double ldImporteDebe=0.0;
   double ldImporteHaber=0.0;
   //Connection conexion=null;
   String error="0";
   int nivel=0;
   String subCadena="";
   int existenSubcuentas=0;
   String query="";
   
   // conexion=DaoFactory.getContabilidad();
   // conexion.setAutoCommit(false);
   try{ System.out.println("bcEnviaPolizaCheque.procesa");
     for (int x=0; x<chequesId.length;x++)
     {    //Ciclo para cada uno de los cheques     
     try{  
     
       // Verifica si no hay cierre definitivo justamente antes de hacer el commit a cada cheque
       bcEstadoCat.select_rf_tc_estado_catalogo(conexion);
       estatus=bcEstadoCat.getEstatus();
       descripcion=bcEstadoCat.getDescripcion();
       if (!estatus.equals("1")){
         if (!estatus.equals("5")){ 
           error="C";
           throw new Exception(" En estos momentos no es posible aplicar cheques ya que hay un proceso de "+ descripcion+" ejecutandose. Favor de intentarlo mas tarde.");
         }  
       }        
       consecutivoCheque=chequesId[x].substring(0,chequesId[x].length()-1);
       pbCargaCheque.select_RF_TR_CHEQUES_CARGA(conexion,consecutivoCheque); //Lee cada uno de los cheques de la carga

       lsEntidad=pbCargaCheque.getAmbito().substring(0,2);
       if (lsEntidad.substring(0,1).equals("0")) {
             lsEntidad=lsEntidad.substring(1);
         }    
       ejercicio=pbCargaCheque.getFechacheque().substring(6,10);
       mes=pbCargaCheque.getFechacheque().substring(3,5);
                
       //Obtiene datos de cuenta bancaria como consecutivo, abreviatura necesarios para la generacion del cheque 
       pbCuentasCheques.setUnidad_ejecutora(pbCargaCheque.getUnidad()); 
       pbCuentasCheques.setAmbito(pbCargaCheque.getAmbito().substring(2));
       pbCuentasCheques.setEntidad(lsEntidad);
       crsDetalleCuentasCheques=null;
       String origen = "nomina";
       crsDetalleCuentasCheques=pbCuentasCheques.select_rf_tr_detalle_Cuentas_Cheques(conexion,ejercicio,pbCargaCheque.getCuentabancaria(),origen);
       // System.out.println("size "+  crsDetalleCuentasCheques.size());
       if (crsDetalleCuentasCheques==null){
         error="B";
         throw new Exception("Los datos de la cuenta bancaria no pueden ser leidos."); 
       }
       //crsDetalleCuentasCheques.beforeFirst();
       //crsDetalleCuentasCheques.next();
               
       
       pbPoliza.setUnidad_ejecutora(pbCargaCheque.getUnidad());
       pbPoliza.setAmbito(pbCargaCheque.getAmbito().substring(2));
       pbPoliza.setPais("147");
       pbPoliza.setEntidad(lsEntidad);
       pbPoliza.setTipo_poliza_id("3"); //tipo cheque (generando poliza de egreso)  
       pbPoliza.setBan_cheque("1"); //Esta opcion siempre genera cheque
       pbMaestroOperaciones.setMaestro_operacion_id(null);
       pbMaestroOperaciones.select_rf_tc_maestro_operaciones_carga_var(conexion,pbCargaCheque.getUnidad(),pbCargaCheque.getAmbito().substring(2),lsEntidad,lsCatCuenta,ejercicio,pbCargaCheque.getOperaciontipo());
       if (pbMaestroOperaciones.getMaestro_operacion_id()==null){
         error="B";
         throw new Exception("La operacion tipo "+pbCargaCheque.getOperaciontipo()+" especificada en el archivo de carga no existe.");
       }
       pbPoliza.setMaestro_operacion_id(pbMaestroOperaciones.getMaestro_operacion_id());      
       pbPoliza.setFecha(pbCargaCheque.getFechacheque());
       pbPoliza.setConcepto(pbCargaCheque.getConcepto());
       pbPoliza.setReferencia("CHEQUE NO. "+crsDetalleCuentasCheques.getString("ultimo_consecutivo")); 
       //pbPoliza.setReferencia(pbCargaCheque.getCuentabancaria()+" "+crsDetalleCuentasCheques.getString("ultimo_consecutivo")); 
       //pbPoliza.setFecha_afectacion(rsQuery.getString("fechAlta"));  //Fecha del dia por default
       pbPoliza.setClasificacion_poliza_id("1"); //Abierta
       //pbPoliza.setPoliza_referencia("");  //null como si fuera manual ya se checo con Chava 
       pbPoliza.setNum_empleado(pbCargaCheque.getNumempleado());
       pbPoliza.setEjercicio(ejercicio);
       pbPoliza.setMes(mes);
       pbPoliza.setOrigen(pbCargaCheque.getOperaciontipo());
       pbPoliza.setId_catalogo_cuenta(lsCatCuenta);
       lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
       pbPoliza.setPoliza_id(lsPolizaId);
       pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
       pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));                    
       if (!pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id<>2","0001")){  //Esta fijo OJO
          throw new Exception("El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza.");
       }  
       if (pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id=2","0001")){  //Esta fijo OJO
          throw new Exception("El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza.");
       }                  
          
       error="0";   
   
       
       pbPoliza.insert_rf_tr_polizas(conexion);  //Inserta poliza
       System.out.println("bcEnviaPolizaCheque.procesa - insertó poliza");
       pbMaestroOperaciones.setUnidad_ejecutora(pbCargaCheque.getUnidad());
       pbMaestroOperaciones.setAmbito(pbCargaCheque.getAmbito().substring(2));
       pbMaestroOperaciones.setEntidad(lsEntidad);
       pbMaestroOperaciones.setId_catalogo_cuenta(lsCatCuenta);
       pbMaestroOperaciones.setConsecutivo(pbCargaCheque.getOperaciontipo());
       crsDetalleOperaciones=pbMaestroOperaciones.select_rf_tr_detalle_operaciones(ejercicio);
        
       ldImporteDebe=0.0;
       ldImporteHaber=0.0;
       crsDetalleOperaciones.beforeFirst();
       while (crsDetalleOperaciones.next()){  
         lsDetalleId=pbDetallePoliza.select_SEQ_rf_tr_detalle_polizas(conexion);  //obtiene consecutivo del detallle
         pbDetallePoliza.setId_detalle(lsDetalleId);
         lsCuentaContableId=crsDetalleOperaciones.getString("cuenta_contable_id");    
         //Valida si la cuenta es de ultimo nivel
         pbCuenta.select_rf_tr_cuentas_contables(conexion,lsCuentaContableId); //lsCuentaContable lleva cuentaContableId
         nivel=Integer.parseInt(pbCuenta.getNivel());
         subCadena=subCadenaContable(nivel,pbCuenta.getCuenta_contable());  //lsCuenta lleva cuentaContable
         System.out.println("bcEnviaPolizaCheque.procesa query:" + query + "*");
         query="select count(*) registros from rf_tr_cuentas_contables where cuenta_contable like '"+subCadena+"%'"+" and id_catalogo_cuenta = '"+lsCatCuenta+"' and extract(year from fecha_vig_ini) = " + pbPoliza.getEjercicio();     
         existenSubcuentas = pbCuenta.selectCountCuenta(conexion,query);     
         System.out.println("bcEnviaPolizaCheque.procesa pbCuenta.selectCountCuenta");
         if (existenSubcuentas>1){
           error="B";
           throw new Exception("Se intento cargar polizas con una cuenta contable que no es de ultimo nivel: "+pbCuenta.getCuenta_contable());
         }       
         pbDetallePoliza.setCuenta_contable_id(lsCuentaContableId);
         lsOperacionContableId=crsDetalleOperaciones.getString("operacion_contable_id");
         pbDetallePoliza.setOperacion_contable_id(lsOperacionContableId);
         pbDetallePoliza.setReferencia(pbCargaCheque.getBeneficiario());   
         //pbDetallePoliza.setReferencia(crsDetalleCuentasCheques.getString("ultimo_consecutivo")); 
         pbDetallePoliza.setFecha_afectacion(pbCargaCheque.getFechacheque());
         pbDetallePoliza.setImporte(pbCargaCheque.getImporte());
         pbDetallePoliza.setPoliza_id(lsPolizaId);
         if (lsOperacionContableId.equals("0")) {
               ldImporteDebe=ldImporteDebe+Double.valueOf(pbCargaCheque.getImporte());
           }
         else {
               ldImporteHaber=ldImporteHaber+Double.valueOf(pbCargaCheque.getImporte());
           }
         pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);  //inserta el detalle de la poliza
         
         pbRegistraPoliza.setIdEvento("0");  //actualizarImporteCuenta
         pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsCuentaContableId),Double.valueOf(pbCargaCheque.getImporte()),lsOperacionContableId,pbCargaCheque.getFechacheque(),"+");  //Actualiza los saldos         
       } // Fin de While
       
        if (ldImporteDebe!=ldImporteHaber){  //Esta fijo OJO
           throw new Exception("La poliza no puede ser generada porque el debe y el haber son diferentes.");
        }
       lsSeqChequeId=pbCheque.select_SEQ_rf_tr_cheques(conexion);
       lsDigVerificador=pbCheque.select_digito_verificador(conexion,crsDetalleCuentasCheques.getString("ultimo_consecutivo"));
       pbCheque.setCheque_id(lsSeqChequeId);
       pbCheque.setCuenta_cheques_id(crsDetalleCuentasCheques.getString("cuenta_cheques_id"));
       pbCheque.setConsecutivo(crsDetalleCuentasCheques.getString("ultimo_consecutivo"));
       pbCheque.setAbreviatura(crsDetalleCuentasCheques.getString("abreviatura"));
       pbCheque.setImporte(pbCargaCheque.getImporte());
       pbCheque.setReferencia(pbCargaCheque.getConcepto()); //El campo se llama referencia aunque guardara el concepto igual que la poliza
       pbCheque.setBeneficiario(pbCargaCheque.getBeneficiario());
       pbCheque.setNumEmpleado(pbCargaCheque.getNumempleado());
       pbCheque.setDigitoVerificador(lsDigVerificador);
       pbCheque.setPoliza_id(lsPolizaId);
       pbCheque.setFechaCheque(pbCargaCheque.getFechacheque());
       pbCheque.setOrigenDocto(pbCargaCheque.getOrigen_operacion());
       pbCheque.setCxl(pbCargaCheque.getOperacion_pago());
       pbCheque.setCxlSup(pbCargaCheque.getOperacion_pago_sup());
       System.out.println("bcEnviaPolizaCheque.procesa antes insertar cheques");
       pbCheque.insert_rf_tr_cheques(conexion);
       System.out.println("bcEnviaPolizaCheque.procesa insertó cheques");
       pbCuentasCheques.setConsecutivo(crsDetalleCuentasCheques.getString("ultimo_consecutivo"));
       pbCuentasCheques.update_rf_tc_cuentas_consecutivo(conexion,crsDetalleCuentasCheques.getString("cuenta_cheques_id"));
       
       pbCargaCheque.update_RF_TR_CHEQUES_CARGA_FECHA(conexion,pbCargaCheque.getConsecutivocheques());
       // conexion.commit();      
       resultado=resultado+crsDetalleCuentasCheques.getString("ultimo_consecutivo")+",";
       resultado=resultado+pbPoliza.getConsecutivo()+",";
       System.out.println("resLstChqs: "+resultado);
      }catch(Exception e){
          // conexion.rollback();      
          System.out.println("Error al generar cheque: "+ pbCargaCheque.getConsecutivocheques()+" "+e.getMessage());
          //resultado=resultado+crsDetalleCuentasCheques.getString("ultimo_consecutivo")+",";
          resultado=resultado+"0"+",";
          if (error.equals("C"))
            resultado=resultado+e.getMessage()+",";
          else  
          if (error.equals("B"))
              resultado=resultado+e.getMessage()+" Favor de comunicarse con el administrador del sistema.,";
          else
            resultado=resultado+" Error al generarse el cheque. Favor de comunicarse con el administrador del sistema.,";  
          throw e;  
      } 
     } // Fin de For
   }catch(Exception e){
      System.out.println("Error grave en metodo procesa de envia cheques: "+e.getMessage());
      throw e;
   }finally{
      System.out.println("Termino aplicacion contable de cheques en metodo procesa.");  
    //  if (conexion!=null){ 
    //    conexion.close(); 
    //    conexion=null; 
    //  }          
   } 
 return resultado;  
 }
}   