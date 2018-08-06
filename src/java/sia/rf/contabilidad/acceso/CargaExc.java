package sia.rf.contabilidad.acceso;

import java.io.File;

import java.sql.*;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import sia.db.dao.DaoFactory;

import sia.rf.contabilidad.registroContableNuevo.bcMaestroOperaciones;
import sia.rf.contabilidad.registroContableNuevo.bcPoliza;
import sia.rf.contabilidad.registroContableNuevo.bcDetallePolizaCarga;
import sia.rf.contabilidad.registroContableNuevo.bcPolizaCarga;
import sia.rf.contabilidad.registroContableNuevo.bcCuentaContable;

import java.text.SimpleDateFormat;
import java.text.ParseException;






public class CargaExc {
    private bcPoliza pbPoliza=null;
    private bcPolizaCarga pbPolizaCarga;
    private bcDetallePolizaCarga pbDetallePolizaCarga;
    private bcCuentaContable pbCuenta=null;
    private bcMaestroOperaciones pbMaestro=null;    

    public final int  COLUMNA_UNIDAD               = 0;
    public final int  COLUMNA_AMBITO               = 1;
    public final int  COLUMNA_NUMERO_POLIZA        = 2;
    public final int  COLUMNA_NUMERO_OPERACION     = 3;
    public final int  COLUMNA_FECHA_APLICACION     = 4;
    public final int  COLUMNA_CONCEPTO             = 5;  
    public final int  COLUMNA_FECHA_ALTA           = 6;  
    public final int  COLUMNA_REFERENCIA_GENERAL   = 7;  
    public final int  COLUMNA_CUENTA_CONTABLE      = 8;  
    public final int  COLUMNA_DEBE_HABER           = 9;  
    public final int  COLUMNA_IMPORTE              = 10;  
    public final int  COLUMNA_REFERENCIA_DETALLE   = 11;  
   

    
    public CargaExc(){
      pbPoliza= new bcPoliza();    
      pbPolizaCarga= new bcPolizaCarga();    
      pbDetallePolizaCarga= new bcDetallePolizaCarga();
      pbCuenta = new bcCuentaContable();  
      pbMaestro = new bcMaestroOperaciones(); 
    }
    
    public String insertaPoliza(Connection conexion, String unidad, String ambito, String numPoli, String numOper, String fechPoli, String concepPoli, String fechAlta, String refGral, String ejercicio, String mes, String estatus, String numEmpleado)throws Exception{
      String lsFecha="";
      try{
        if (fechAlta==null) 
          fechAlta=fechPoli; //Regla aplicada de acuerdo a lo platicado con Carlos Bragdon 18/10/2010
        String lsPolizaId=pbPolizaCarga.select_SEQ_rf_tr_polizas_carga(conexion);
        pbPolizaCarga.setPoliza_id(lsPolizaId);
        pbPolizaCarga.setUnidad(unidad);
        pbPolizaCarga.setAmbito(ambito);
        pbPolizaCarga.setNumPoli(numPoli);
        pbPolizaCarga.setNumOper(numOper);
        lsFecha=fechPoli;
        pbPolizaCarga.setFechPoli(lsFecha);
        pbPolizaCarga.setConcepPoli(concepPoli);
        lsFecha=fechAlta;
        pbPolizaCarga.setFechAlta(lsFecha);
        pbPolizaCarga.setRefGral(refGral);
        pbPolizaCarga.setEjercicio(ejercicio);
        pbPolizaCarga.setMes(mes);
        pbPolizaCarga.setEstatus(estatus);
        pbPolizaCarga.setNumEmpleado(numEmpleado);
        pbPolizaCarga.insert_rf_tr_polizas_carga(conexion);
        return lsPolizaId;
      }catch(Exception e){
         System.out.println("Error en metodo insertaPoliza"); 
         throw e;
      }
    }

    public void insertaDetallePoliza(Connection conexion, String unidad, String ambito, String numPold, String cuentPold, String debeHaber, String impoPold, String referencia, String polizaId)throws Exception{
      try{
        String lsDetalleId=pbDetallePolizaCarga.select_SEQ_rf_tr_detalle_polizas_carga(conexion);
          pbDetallePolizaCarga.setId_detalle(lsDetalleId);
          pbDetallePolizaCarga.setUnidad(unidad);
          pbDetallePolizaCarga.setAmbito(ambito);
          pbDetallePolizaCarga.setNumPold(numPold);
          pbDetallePolizaCarga.setCuentPold(cuentPold);
          pbDetallePolizaCarga.setDebeHaber(debeHaber);
          pbDetallePolizaCarga.setImpoPold(impoPold);
          pbDetallePolizaCarga.setReferencia(referencia);
          pbDetallePolizaCarga.setPoliza_id(polizaId);
          pbDetallePolizaCarga.insert_rf_tr_detalle_poliza_carga(conexion);
          
      }catch(Exception e){
         System.out.println("Error en metodo insertaDetallePoliza"); 
         throw e;
      }
    }
    
    public String subCadenaContable(int nivel, String cuentaContable){
      String res="";
       if (nivel==5)  // se renumeran antes empezaba de 4, al agregarse un nivel empiezan de 5, las posiciones no cambian
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
    
    private boolean validarFecha(String fecha) {
      if (fecha == null)
        return false;
      SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy"); //anio-mes-dia
      if (fecha.trim().length() != dateFormat.toPattern().length())
        return false;
      dateFormat.setLenient(false);
      try{
       dateFormat.parse(fecha.trim());
      }
      catch (ParseException pe) {
        return false;
      }
      return true;
    }
    
    protected double red(double monto, int dec) {
      return Math.round(monto*Math.pow(10,dec))/Math.pow(10,dec);
    }
    
    public String procesa(Connection conexion,String numEmpleado, String lsUnidad, String lsAmbito, String lsRuta, String pEjercicio, String pCatCuenta, String psPrograma, String psTipoUsuario) throws Exception,SQLException{
      //String SQL="";
      //String archivo="C:/datos/dbf/chequeras/abril/1301005";
      //String nombreArchivo="POLIZAS.DBF";
      String unidad="";
      String ambito="";
      String numPoliza="";
      String dbUnidad="";
      String dbAmbito="";
      String dbNumPoli="";
      String dbNumOper="";
      String dbFechPoli;
      String dbConcepPoli="";
      String dbFechAlta="";
      String dbRefGral="";
      String dbCuentPold="";
      String dbDebeHaber="";
      String dbImpoPold="";
      String dbReferencia="";
      int totalPolizas=0;
      String ejercicio="";
      String mes="";
      String error="";
      String lsPolizaId="";
      String resultado="";
      double ldImporteDebe=0.0;
      double ldImporteHaber=0.0;
      String lsCuentaContableId="";
      int nivel=0;
      String subCadena="";
      String query="";
      int existenSubcuentas=0;
      String lsUnidadTem="";
      String lsEntAmb="";
      
      ejercicio=pEjercicio;
      
      //SQL="select polizas.unidad, polizas.ambito, numPoli, numOper, fechPoli, concepPoli, fechAlta, refGral, numPold, cuentPold, debeHaber, impoPold, referencia ";
      //SQL = SQL + " from polizas, detmov ";
      //SQL = SQL + " where polizas.unidad = detmov.unidad and polizas.ambito=detmov.ambito and numpoli=numPold ";
      
      
      File archivo = null;
      String nombre = null;
      long tamanio = 0;
      String archivoXls = lsRuta;   // "c:/datos/dbf/cheques.xls";
      Workbook woorkBook              = null;
      Sheet    sheet                  = null;
      int totalFilas = 0;
      int totalColumnas;
      Cell [] filax                   = null;
      int fila =1;
      String lsEntidad="";
      String lsAmbitoTem="";
      String estatusEmp="";
    
          
      try{
        archivo = new File(archivoXls);     
        if (!archivo.exists()) {
          error="El archivo "+ archivoXls+ " no existe.";
        }
        nombre = archivo.getName();         
        tamanio = archivo.length();   
        woorkBook             = Workbook.getWorkbook(archivo);
        sheet                 = woorkBook.getSheet(0);
        totalFilas = sheet.getRows();   
        totalColumnas =  sheet.getColumns();
        if(totalFilas>0)
          System.out.println("El archivo " + nombre + "contiene "+ totalFilas + "registros.");   
          
        estatusEmp=pbPolizaCarga.select_SEQ_rf_tc_modifica_ambitos(conexion,numEmpleado);  
        while(fila < totalFilas){
          filax  = sheet.getRow(fila);
            
          dbUnidad=filax[COLUMNA_UNIDAD].getContents().trim();
          if (dbUnidad.toUpperCase().trim().equals("FIN")) // Si viene un registro vacio indica fin de archivo 
             break;            
          dbAmbito=filax[COLUMNA_AMBITO].getContents().trim();
          dbNumPoli=filax[COLUMNA_NUMERO_POLIZA].getContents().trim();
          dbNumOper=filax[COLUMNA_NUMERO_OPERACION].getContents().trim();
          dbFechPoli=filax[COLUMNA_FECHA_APLICACION].getContents().trim();
          dbConcepPoli=filax[COLUMNA_CONCEPTO].getContents().trim();
          dbFechAlta=filax[COLUMNA_FECHA_ALTA].getContents().trim();
          dbRefGral=filax[COLUMNA_REFERENCIA_GENERAL].getContents().trim();
          dbCuentPold=filax[COLUMNA_CUENTA_CONTABLE].getContents().trim();
          dbDebeHaber=filax[COLUMNA_DEBE_HABER].getContents().trim();
          dbImpoPold=filax[COLUMNA_IMPORTE].getContents().trim();
          dbReferencia=filax[COLUMNA_REFERENCIA_DETALLE].getContents().trim();
          
          if ((dbAmbito.equals("")) || (dbNumPoli.equals("")) || (dbNumOper.equals("")) || (dbFechPoli.equals("")) || (dbConcepPoli.equals("")) || (dbFechAlta.equals("") || (dbRefGral.equals("")) || (dbCuentPold.equals("")) || (dbDebeHaber.equals("")) || (dbImpoPold.equals("")) || (dbReferencia.equals("")))){
            error="Hay uno o varios campos sin informacion en el archivo de carga, todos son obligatorios. ";
            break;
          } 
          
          if (dbNumPoli.length()>6){
            error="Se intento cargar polizas con un numero de poliza mayor a 6 caracteres: "+dbNumPoli;
            break;
          }    
            
          if (validarFecha(dbFechPoli)==false){
            error="Se intento cargar polizas con fecha contable con un formato diferente a dd/mm/aaaa: "+dbFechPoli;
            break;
          }      
          
          if (validarFecha(dbFechAlta)==false){
            error="Se intento cargar polizas con fecha de alta con un formato diferente a dd/mm/aaaa: "+dbFechAlta;
            break;
          }   
            
          if (dbConcepPoli!=null)
            dbConcepPoli=dbConcepPoli.replaceAll("'"," ");
          if (dbConcepPoli.length()>1000){
            error="Se intento cargar polizas con un concepto mayor a 1000 caracteres: "+dbConcepPoli;
            break;
          } 
          
          if (dbRefGral!=null)
            dbRefGral=dbRefGral.replaceAll("'"," "); 
          if (dbRefGral.length()>500){
            error="Se intento cargar polizas con una referencia general mayor a 500 caracteres: "+dbRefGral;
            break;
          }             
            
          if (dbReferencia!=null)
            dbReferencia=dbReferencia.replaceAll("'"," ");          
           if (dbReferencia.length()>255){
              error="Se intento cargar polizas con una referencia de detalle mayor a 255 caracteres: "+dbReferencia;
              break;
            }             
                          
          // System.out.println("uni eje "+dbUnidad );
                    
          lsCuentaContableId= pbCuenta.selectCuentaContableId(conexion,dbCuentPold,pCatCuenta,pEjercicio);
          if (lsCuentaContableId.equals("")){
            error="Se intento cargar polizas con una cuenta contable inexistente: "+dbCuentPold;
            break;
          }
          
          lsUnidadTem=dbCuentPold.substring(9,13);
          lsEntAmb=dbCuentPold.substring(13,17);
          
          if (psTipoUsuario.equals("3")){
            if (!("0"+lsUnidad+"0"+lsAmbito).equals(lsUnidadTem+lsEntAmb)){
              error="Se intento cargar polizas con una cuenta contable de la cual no tiene permisos para aplicar el registro: "+dbCuentPold;
              break;            
            }  
          }          
          
          if (psTipoUsuario.equals("2")){
            if (estatusEmp.equals("0")){
               if (!("0"+lsUnidad+"0"+lsAmbito).equals(lsUnidadTem+lsEntAmb)){
                 error="Se intento cargar polizas con una cuenta contable de la cual no tiene permisos para aplicar el registro: "+dbCuentPold;
                 break;            
               } 
            }   
            else   
              if (!("0"+lsUnidad).equals(lsUnidadTem)){
                error="Se intento cargar polizas con una cuenta contable de la cual no tiene permisos para aplicar el registro: "+dbCuentPold;
                break;            
              }  
          }          
          
          pbCuenta.select_rf_tr_cuentas_contables(conexion,lsCuentaContableId);
          nivel=Integer.parseInt(pbCuenta.getNivel());
          subCadena=subCadenaContable(nivel,dbCuentPold);
          System.out.println("query "+query);
          query="select count(*) registros from rf_tr_cuentas_contables where cuenta_contable like '"+subCadena+"%'"+" and id_catalogo_cuenta = '"+pCatCuenta+"' and extract(year from fecha_vig_ini) = " + ejercicio;     
          existenSubcuentas=pbCuenta.selectCountCuenta(conexion,query);     
          if (existenSubcuentas>1){
            error="Se intento cargar polizas con una cuenta contable que no es de ultimo nivel: "+dbCuentPold;
            break;
          }
          
          if (!ejercicio.equals(dbFechPoli.substring(6))){
            error="Se intento cargar polizas de un ejercicio diferente al que se especifico en el filtro principal, ";
            break;
          }  
          mes=dbFechPoli.substring(3,5);
           
          
          if (!dbUnidad.equals(lsUnidad)){
             error="Se intento cargar polizas de una unidad diferente a la seleccionada en el filtro principal. ";
             break; 
          }
          if (!dbAmbito.equals(lsAmbito)){
             error="Se intento cargar polizas de un ambito diferente al seleccionado en el filtro principal. ";
             break; 
          }         
          
          if (Double.parseDouble(dbImpoPold)==0){
              error="Se intento cargar polizas con un importe cero en alguno de los movimientos. ";
              break;               
          }
          
          if ((!dbDebeHaber.toUpperCase().equals("D"))&&(!dbDebeHaber.toUpperCase().equals("H"))){
            error="Se intento cargar polizas con un valor diferente de D o H en alguno de los movimientos del campo DEBE_HABER: "+dbDebeHaber;
            break;
          } 
          
         if ((!dbNumPoli.substring(0,1).toUpperCase().equals("D"))&&(!dbNumPoli.substring(0,1).toUpperCase().equals("C"))&&(!dbNumPoli.substring(0,1).toUpperCase().equals("E"))&&(!dbNumPoli.substring(0,1).toUpperCase().equals("I"))){
            error="Se intento cargar polizas con un valor diferente del primer caracter de D, C, E o I: "+dbNumPoli;
            break;
          }           
                         
            pbPoliza.setUnidad_ejecutora(dbUnidad);
            lsAmbitoTem=dbAmbito.substring(2);
            lsEntidad=dbAmbito.substring(0,2);
            if (lsEntidad.substring(0,1).equals("0"))
              lsEntidad=lsEntidad.substring(1);
            
            pbMaestro.select_rf_tc_maestro_operaciones_carga_var(conexion,lsUnidad,lsAmbitoTem,lsEntidad,pCatCuenta,pEjercicio,dbNumOper);              
            if(pbMaestro.getMaestro_operacion_id()==null){
              error="Se intento cargar polizas con una operacion inexistente en el catalogo de operaciones. Ejemplo de operacion correcta 05, 12, 99";
              break;                 
            }
            if(dbNumOper.length()!=2){
               error="Se intento cargar polizas con una operacion de longitud diferente a 2 caracteres. Ejemplo de operacion correcta 05, 12, 99";
               break;                 
            }
            
        
            psPrograma= dbCuentPold.substring(5,9);
            pbPoliza.setAmbito(lsAmbitoTem);
            pbPoliza.setEntidad(lsEntidad);
            pbPoliza.setEjercicio(ejercicio);
            pbPoliza.setMes(mes);
            pbPoliza.setId_catalogo_cuenta(pCatCuenta);
        
            if (!pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id<>2",psPrograma)){
                error="El mes actual no se ha abierto o no existe un mes con estatus de preliminar para la fecha especificada de la poliza.";
                break;
            }  
            if (pbPoliza.selectVerificaEstatusMes(conexion," and estatus_cierre_id=2",psPrograma)){
                error="El mes ya se encuentra cerrado definitivamente para la fecha especificada de la poliza.";
                break;
            }                 
           
            
          if (!unidad.equals(dbUnidad) || !ambito.equals(dbAmbito) || !numPoliza.equals(dbNumPoli)){
            if (unidad.equals("")){  //Primer registro
               System.out.println(" Inserta Poliza por primer registro");
               lsPolizaId=insertaPoliza(conexion, dbUnidad,  dbAmbito,  dbNumPoli, dbNumOper,  dbFechPoli,  dbConcepPoli,  dbFechAlta,  dbRefGral, ejercicio,  mes, "0", numEmpleado);
               System.out.println(" Inserta detalle por primer registro");
               insertaDetallePoliza(conexion,dbUnidad,dbAmbito,dbNumPoli,dbCuentPold,dbDebeHaber,dbImpoPold,dbReferencia,lsPolizaId);
               unidad=dbUnidad;
               ambito=dbAmbito;
               numPoliza=dbNumPoli;
               totalPolizas+=1;

               if (dbDebeHaber.toUpperCase().equals("D"))
                 ldImporteDebe=Double.valueOf(dbImpoPold);
               else    
                 ldImporteHaber=Double.valueOf(dbImpoPold);               
            }else{
               System.out.println("Termina poliza");
               System.out.println(" Inserta Poliza medio");
               ldImporteDebe=red(ldImporteDebe,2);
               ldImporteHaber=red(ldImporteHaber,2);
               if (ldImporteDebe!=ldImporteHaber){  //Esta fijo OJO
                  error="Se intento cargar polizas donde el debe y el haber son diferentes.";
                  break;
               }    
               lsPolizaId=insertaPoliza(conexion, dbUnidad,  dbAmbito,  dbNumPoli, dbNumOper,  dbFechPoli,  dbConcepPoli,  dbFechAlta,  dbRefGral, ejercicio,  mes, "0",numEmpleado);
               System.out.println(" Inserta detalle medio");
               insertaDetallePoliza(conexion,dbUnidad,dbAmbito,dbNumPoli,dbCuentPold,dbDebeHaber,dbImpoPold,dbReferencia,lsPolizaId);
               unidad=dbUnidad;
               ambito=dbAmbito;
               numPoliza=dbNumPoli;
               totalPolizas+=1;
               ldImporteDebe=0.0;
               ldImporteHaber=0.0;
               if (dbDebeHaber.toUpperCase().equals("D"))
                 ldImporteDebe=Double.valueOf(dbImpoPold);
               else    
                 ldImporteHaber=Double.valueOf(dbImpoPold);                 
            }
          }else{  
             System.out.println(" Inserta detalle solo");               
             insertaDetallePoliza(conexion,dbUnidad,dbAmbito,dbNumPoli,dbCuentPold,dbDebeHaber,dbImpoPold,dbReferencia,lsPolizaId);
             if (dbDebeHaber.toUpperCase().equals("D"))
              ldImporteDebe=red(ldImporteDebe,2)+red(Double.valueOf(dbImpoPold),2);
             else    
              ldImporteHaber=red(ldImporteHaber,2)+red(Double.valueOf(dbImpoPold),2);              
          }  
           // System.out.println("unidad "+rsQuery.getString(dbUnidad)+" ambito "+rsQuery.getString(dbAmbito) +"  numPoli "+rsQuery.getString(dbNumPoli)+" dbNumPold "+rsQuery.getString(dbNumPold));
          // System.out.println("poliza "+rsQuery.getString("numPoli"))
          fila++;
        } //Fin WHILE

        if (error.equals("")){
          ldImporteDebe=red(ldImporteDebe,2);
          ldImporteHaber=red(ldImporteHaber,2);
          if (ldImporteDebe!=ldImporteHaber){  //Esta fijo OJO
             error="Se intento cargar polizas donde el debe y el haber son diferentes.";
          }    
        }  
        
        if (!error.equals("")){ 
           totalPolizas=0;
           throw new Exception(error);
        }
        resultado="1,La carga de polizas ha sido exitosa. Utilice la opcion aplicar polizas para que se aplique el registro contable. Total de polizas grabadas: "+totalPolizas;
        System.out.println("Total de polizas procesadas: "+totalPolizas);
      }catch(Exception e){
        resultado="0,La carga de polizas ha fallado. Causa: "+e.getMessage()+" Favor de reportarlo al administrador del sistema. ";
        System.out.println("Error en metodo procesa "+e.getMessage());
        if (error.equals("")){ 
            throw new Exception("error en algun campo,");
        } 
        else
          throw e;
      }  
      finally{
      }
    return resultado;  
    }  
}
