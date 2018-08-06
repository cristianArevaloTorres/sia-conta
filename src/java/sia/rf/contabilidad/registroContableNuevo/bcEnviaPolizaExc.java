package sia.rf.contabilidad.registroContableNuevo;

import java.sql.*;

import sia.rf.contabilidad.registroContableNuevo.bcPoliza;
import sia.rf.contabilidad.registroContableNuevo.bcDetallePoliza;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;
import sia.rf.contabilidad.registroContable.procesos.polizas.servicios.Polizas;
import sia.rf.contabilidad.registroContableNuevo.bcPolizaCarga;
import sia.rf.contabilidad.registroContableNuevo.bcDetallePolizaCarga;
import sia.ws.publicar.contabilidad.Registro;


public class bcEnviaPolizaExc {
  private bcPoliza pbPoliza=null;
  private bcPolizaCarga pbPolizaCarga=null;
  private bcDetallePoliza pbDetallePoliza=null;
  private bcDetallePolizaCarga pbDetallePolizaCarga=null;
  private bcMaestroOperaciones pbMaestroOperaciones=null;
  private bcCuentaContable pbCuenta=null;
  private Polizas pbRegistraPoliza=null;
  private Registro registro=null;
  
  public bcEnviaPolizaExc() {
    pbPoliza = new bcPoliza();
    pbPolizaCarga = new bcPolizaCarga();
    pbDetallePoliza = new bcDetallePoliza();
    pbDetallePolizaCarga = new bcDetallePolizaCarga();
    pbMaestroOperaciones = new bcMaestroOperaciones();
    pbCuenta = new bcCuentaContable();    
    pbRegistraPoliza = new Polizas();
    registro = new Registro();
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
  
  public String procesa(Connection conexion, String psPolizaId, String psCatCuenta, String psPrograma)  throws Exception,SQLException{
   Statement stQuery=null;
   ResultSet rsQuery=null;
   String lsTipoPoliza="";
   String lsTipoPolizaTem="";
   String lsTipoPolizaId="";
   String lsCatCuenta=psCatCuenta;
   String lsPolizaId="";
   String lsEntidad="";
   String lsDetalleId;
   String lsCuentaContableId="";
   String lsOperacionContableId="";
   String lsPolizaIdCarga="";
   int totalPolizas=0;
   String  lsTemCuentPold9="";   
   String  lsTemCuentPold8="";   
   String  lsTemCuentPold7="";
   String  lsTemCuentPold6="";
   String  lsTemCuentPold5="";
   String  lsTemCuentPold4="";
   String  lsTemCuentPold3="";
   String  lsTemCuentPold2="";
   String  lsTemCuentPold1="";

   
   String lsNivel="0";
   String lsCuentaMayorId="";
   String resultado="";
   int nivel=0;
   String subCadena="";
   int existenSubcuentas=0;
   String query="";
     
   StringBuffer SQL=new StringBuffer("SELECT a.poliza_id,a.unidad,a.ambito,a.numPoli,a.numOper,to_char(a.fechPoli,'dd/mm/yyyy') fechPoli,a.concepPoli,to_char(a.fechAlta,'dd/mm/yyyy') fechAlta,a.refGral,a.ejercicio,a.mes, a.estatus, a.numEmpleado, a.fechaDia, a.fechaCarga, a.mensaje, d.impopold, d.debehaber, d.referencia, d.cuentpold "); 
   SQL.append(" FROM rf_tr_polizas_carga a, rf_tr_detalle_poliza_carga d ");
   SQL.append(" WHERE a.poliza_id="+psPolizaId+" and a.poliza_id=d.poliza_id and a.estatus=0 ORDER BY a.poliza_id ");
   System.out.println(SQL.toString());
   try{
     stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
     rsQuery=stQuery.executeQuery(SQL.toString());
     while (rsQuery.next()){
       pbPoliza.setUnidad_ejecutora(rsQuery.getString("unidad"));
       pbPoliza.setAmbito(rsQuery.getString("ambito").substring(2));
       pbPoliza.setPais("147");
      lsEntidad=rsQuery.getString("ambito").substring(0,2);
       if (lsEntidad.substring(0,1).equals("0"))
         lsEntidad=lsEntidad.substring(1);
       pbPoliza.setEntidad(lsEntidad);
       lsTipoPoliza=rsQuery.getString("numPoli").substring(0,1);
       if (lsTipoPoliza.equals("D"))
         lsTipoPolizaId="1";
       else if (lsTipoPoliza.equals("C"))
         lsTipoPolizaId="2";
       else if (lsTipoPoliza.equals("E"))
         lsTipoPolizaId="3";
       else 
       lsTipoPolizaId="4";
       // lsTipoPolizaId="5"; // DE APERTURA (DA), QUITAR ESTA FIJO 
       pbPoliza.setTipo_poliza_id(lsTipoPolizaId);    
       pbMaestroOperaciones.select_rf_tc_maestro_operaciones_carga(conexion,rsQuery.getString("unidad"),rsQuery.getString("ambito").substring(2),lsEntidad,lsCatCuenta,rsQuery.getString("ejercicio"));
       pbPoliza.setMaestro_operacion_id(pbMaestroOperaciones.getMaestro_operacion_id());      
       pbPoliza.setFecha(rsQuery.getString("fechPoli"));
       pbPoliza.setConcepto(rsQuery.getString("concepPoli"));
       pbPoliza.setReferencia(rsQuery.getString("refGral"));
       pbPoliza.setFecha_afectacion(rsQuery.getString("fechAlta"));  
       pbPoliza.setClasificacion_poliza_id("1"); //Abierta
       pbPoliza.setPoliza_referencia("null");  //Poliza manual, verificarlo con Chava
       pbPoliza.setNum_empleado(rsQuery.getString("numEmpleado"));
       pbPoliza.setEjercicio(rsQuery.getString("ejercicio"));
       pbPoliza.setMes(rsQuery.getString("mes"));
       pbPoliza.setOrigen(rsQuery.getString("numOper"));
       pbPoliza.setId_catalogo_cuenta(lsCatCuenta);
   
 
       lsDetalleId=pbDetallePoliza.select_SEQ_rf_tr_detalle_polizas(conexion);  //obtiene consecutivo del detallle
       pbDetallePoliza.setId_detalle(lsDetalleId);
       lsCuentaContableId= pbCuenta.selectCuentaContableId(conexion,rsQuery.getString("cuentPold"),lsCatCuenta,rsQuery.getString("ejercicio")); // Pasa la cuenmta contable , el id catalogo de cuenta y el ejercico para determinar el id de la cuenta contable
  /*     if (lsCuentaContableId.equals("")){
           String tempo=null;
           if (tempo.equals("")){
             System.out.println("Trono por null");
           }
           throw new Exception("La cuenta contable: "+rsQuery.getString("cuentPold")+" de la poliza "+rsQuery.getString("numPoli")+" no existe");
       } 
   */
       if (lsCuentaContableId.equals("")){
          lsTemCuentPold1=rsQuery.getString("cuentPold").substring(0,4); 
          lsTemCuentPold2=rsQuery.getString("cuentPold").substring(4,5);
          lsTemCuentPold3=rsQuery.getString("cuentPold").substring(5,9);
          lsTemCuentPold4=rsQuery.getString("cuentPold").substring(9,13);
          lsTemCuentPold5=rsQuery.getString("cuentPold").substring(13,17);
          lsTemCuentPold8=rsQuery.getString("cuentPold").substring(25,29);
          lsTemCuentPold9=rsQuery.getString("cuentPold").substring(29);
          lsTemCuentPold7=rsQuery.getString("cuentPold").substring(21,25);
          lsTemCuentPold6=rsQuery.getString("cuentPold").substring(17,21);
          lsNivel="0";
          lsCuentaMayorId="";
          if (!lsTemCuentPold8.equals("0000"))
            lsNivel="8";
          else            
          if (!lsTemCuentPold7.equals("0000"))
            lsNivel="7";
          else  
            if (!lsTemCuentPold6.equals("0000"))
              lsNivel="6";
            else
              if (!lsTemCuentPold5.equals("0000"))
                lsNivel="5";
          if(lsNivel.equals("0"))
            throw new Exception("La cuenta contable: "+rsQuery.getString("cuentPold")+" de la poliza "+rsQuery.getString("numPoli")+" no existe y no puede ser creada");
          else{
            System.out.println("La cuenta contable: "+rsQuery.getString("cuentPold")+" de la poliza "+rsQuery.getString("numPoli")+" no existe y ha sido creada");
            registro.AgregaCuentaContable(lsTemCuentPold1,lsTemCuentPold2,lsTemCuentPold3,lsTemCuentPold4,lsTemCuentPold5,lsTemCuentPold6,lsTemCuentPold7,lsTemCuentPold8,lsTemCuentPold9,rsQuery.getString("fechPoli"),"Pendiente",lsNivel,lsCatCuenta);
            //lsCuentaMayorId= pbCuenta.selectCuentaMayorId(conexion,rsQuery.getString("cuentPold").substring(0,17)+"000000000000",lsCatCuenta,rsQuery.getString("ejercicio")); 
            //pbCuenta.insert_rf_tr_cuentas_contables_no_existe(conexion,lsCuentaMayorId,rsQuery.getString("cuentPold"),lsNivel,"Pendiente","01/01/2010","31/12/2099","99999","26/12/2010","1");
            lsCuentaContableId= pbCuenta.selectCuentaContableId(conexion,rsQuery.getString("cuentPold"),lsCatCuenta,rsQuery.getString("ejercicio")); // Pasa la cuenmta contable , el id catalogo de cuenta y el ejercico para determinar el id de la cuenta contable
          }
         // System.out.println("nivel "+ lsNivel+"cuenta mayor id "+lsCuentaMayorId);
         // throw new Exception("La cuenta contable: "+rsQuery.getString("cuentPold")+" de la poliza "+rsQuery.getString("numPoli")+" no existe");
       }   
    
       pbDetallePoliza.setCuenta_contable_id(lsCuentaContableId);
       if (rsQuery.getString("debeHaber").equals("D"))
         lsOperacionContableId="0";
       else
         lsOperacionContableId="1";
       pbDetallePoliza.setOperacion_contable_id(lsOperacionContableId);
       pbDetallePoliza.setReferencia(rsQuery.getString("referencia"));
       pbDetallePoliza.setFecha_afectacion(rsQuery.getString("fechPoli"));
       pbDetallePoliza.setImporte(rsQuery.getString("impoPold"));
       
       
       //Valida si la cuenta es de ultimo nivel
       pbCuenta.select_rf_tr_cuentas_contables(conexion,lsCuentaContableId); //lsCuentaContable lleva cuentaContableId
       nivel=Integer.parseInt(pbCuenta.getNivel());
       subCadena=subCadenaContable(nivel,rsQuery.getString("cuentPold"));  //lsCuenta lleva cuentaContable
       System.out.println("query "+query);
       query="select count(*) registros from rf_tr_cuentas_contables where cuenta_contable like '"+subCadena+"%'"+" and id_catalogo_cuenta = '"+lsCatCuenta+"' and extract(year from fecha_vig_ini) = " + pbPoliza.getEjercicio();     
       existenSubcuentas=pbCuenta.selectCountCuenta(conexion,query);     
       if (existenSubcuentas>1){
         throw new Exception("Se intento cargar polizas con una cuenta contable que no es de ultimo nivel: "+rsQuery.getString("cuentPold"));
       }           
       
       pbRegistraPoliza.setIdEvento("0");
       pbRegistraPoliza.registrarImporteCuentaWs(conexion,Integer.valueOf(lsCuentaContableId),Double.valueOf(rsQuery.getString("impoPold")),lsOperacionContableId,rsQuery.getString("fechPoli"),"+");  //Actualiza los saldos
       
       psPrograma = rsQuery.getString("cuentpold").substring(5,9);
       if (!pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id<>2",psPrograma)){
            throw new Exception("El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza.");
       }  
       
       if (pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id=2",psPrograma)){
            throw new Exception("El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza.");
       }                 

       if (!lsPolizaIdCarga.equals(rsQuery.getString("poliza_id"))){
         if(lsPolizaIdCarga.equals("")){ //Primer registro
           //System.out.println(" Inserta Poliza por primer registro");
           lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
           pbPoliza.setPoliza_id(lsPolizaId);
           pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
           pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));                 
           pbPoliza.insert_rf_tr_polizas(conexion);  //Inserta poliza
           //System.out.println(" Inserta detalle por primer registro");
           pbDetallePoliza.setPoliza_id(lsPolizaId);
           pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);  //inserta el detalle de la poliza
           lsPolizaIdCarga=rsQuery.getString("poliza_id"); 
           lsTipoPolizaTem=lsTipoPoliza;
           totalPolizas+=1;
         }else{
               //System.out.println("Poliza intermedia:"+lsPolizaId);
               //psPrograma = rsQuery.getString("cuentpold").substring(5,9);
               //if (!pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id<>2",psPrograma)){
               //    throw new Exception("El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza.");
               //}  
               //if (pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id=2",psPrograma)){
               //    throw new Exception("El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza.");
               //}                 
             pbPolizaCarga.update_rf_tr_polizas_carga_estatus(conexion,lsPolizaIdCarga,"1",lsTipoPolizaTem+pbPoliza.getConsecutivo());
             // System.out.println(" Inserta Poliza por cambio registro");
             lsPolizaId=pbPoliza.select_SEQ_rf_tr_polizas(conexion);
             pbPoliza.setPoliza_id(lsPolizaId);             
             pbPoliza.setConsecutivo(pbPoliza.selectConsecutivoPolizas(conexion));
             pbPoliza.setGlobal(pbPoliza.selectConsecutivoGlobalPolizas(conexion));                              
             pbPoliza.insert_rf_tr_polizas(conexion);  //Inserta poliza
             // System.out.println(" Inserta detalle por cambio registro");
             pbDetallePoliza.setPoliza_id(lsPolizaId);
             pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);  //inserta el detalle de la poliza
             lsPolizaIdCarga=rsQuery.getString("poliza_id"); 
             lsTipoPolizaTem=lsTipoPoliza;
             totalPolizas+=1;             
         }
       }else{
           // System.out.println(" Inserta detalle solo");                          
           pbDetallePoliza.insert_rf_tr_detalle_poliza(conexion);  //inserta el detalle de la poliza
       }//Fin de if 
     } // Fin de While
       // System.out.println("Ultima poliza: "+lsPolizaId);
     
       //psPrograma = rsQuery.getString("cuentpold").substring(5,9);
       //if (!pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id<>2",psPrograma)){
       //    throw new Exception("El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza.");
       //}  
       //if (pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id=2",psPrograma)){
       //    throw new Exception("El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza.");
       //}                 
     
       pbPolizaCarga.update_rf_tr_polizas_carga_estatus(conexion,lsPolizaIdCarga,"1",lsTipoPolizaTem+pbPoliza.getConsecutivo());
       resultado=lsTipoPoliza+pbPoliza.getConsecutivo();
   }catch(Exception e){
       System.out.println("Error grave en metodo procesa: "+e.getMessage());
       resultado="";
       throw e;       
   }finally{
       System.out.println("Termino aplicacion contable");  
       if (rsQuery!=null){
         rsQuery.close();
         rsQuery=null;
       }
       if (stQuery!=null){
         stQuery.close();
         stQuery=null;
       }       
   } 
   return resultado; 
 }
}   