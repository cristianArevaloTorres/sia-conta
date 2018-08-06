package sia.rf.contabilidad.sistemas.eliminacion;

import java.sql.Connection;
import java.sql.SQLException;

import java.util.HashMap;


import sia.rf.contabilidad.registroContableEvento.Cadena;


import sun.jdbc.rowset.CachedRowSet;

public class SiafmEliminacion {

public SiafmEliminacion() {
}
       
public String formaAmpliacionReduccion(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra, String pVersion) throws SQLException, Exception {
  HashMap hm = new HashMap();
  CachedRowSet crs = null;
  StringBuffer cadenaTem= new StringBuffer("");
  StringBuffer SQL = new StringBuffer(" select distinct c.cuenta_contable, substr(c.cuenta_contable,1,4) cuentaMayor, substr(c.cuenta_contable, 5, 1) sBcuentaMayor,substr(c.cuenta_contable,6,4) programa," ); 
  SQL.append (" substr(c.cuenta_contable,10,4) unidad, substr(c.cuenta_contable,14,3) entidad,substr(c.cuenta_contable,17,1) ambito," ); 
  SQL.append (" substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) TIGA, substr(c.cuenta_contable,22,4) nivel7," ); 
  SQL.append (" substr(c.cuenta_contable,26,4) nivel8," );

   SQL.append(pMesLetra).append("_cargo * -1 saldo_d, ");
   SQL.append(pMesLetra).append("_abono * -1 saldo_h,");
  SQL.append (" tc.naturaleza ");
  SQL.append (" from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d, rf_tc_clasificador_cuentas tc");
  SQL.append (" where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id and tc.cuenta_mayor_id=c.cuenta_mayor_id");
  SQL.append (" and (     (substr(c.cuenta_contable,1,9)='823010001' or substr(c.cuenta_contable,1,9)='813010001')  ");
  SQL.append ("       or  (substr(c.cuenta_contable,1,9)='823010008' or substr(c.cuenta_contable,1,9)='813010008')   ");
  SQL.append ("       or  (substr(c.cuenta_contable,1,9)='823010009' or substr(c.cuenta_contable,1,9)='813010009') ) ");
  SQL.append (" and c.nivel=6 and extract (year from c.fecha_vig_ini)=").append(pEjercicio).append(" and c.id_catalogo_cuenta = ").append(pCatCuenta); 
  SQL.append (" and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
  SQL.append (" union all ");
  SQL.append (" select distinct c.cuenta, substr(c.cuenta,1,4) cuentaMayor, substr(c.cuenta, 5, 1) sBcuentaMayor,substr(c.cuenta,6,4) programa, ");
  SQL.append (" substr(c.cuenta,10,4) unidad, substr(c.cuenta,14,3) entidad,substr(c.cuenta,17,1) ambito, " ); 
  SQL.append (" substr(c.cuenta,14,4) entAmbito, substr(c.cuenta,18,4) TIGA, substr(c.cuenta,22,4) nivel7, " );   
  SQL.append (" substr(c.cuenta,26,4) nivel8,");
  SQL.append ("     decode(c.operacion_contable_id, 0,c.importe) saldo_d,");
  SQL.append ("     decode(c.operacion_contable_id, 1,c.importe) saldo_h,");
  //SQL.append ("     decode(c.operacion_contable_id,0,'D','A') naturaleza  " ); 
  SQL.append ("     tc.naturaleza  " ); 
  SQL.append (" from rf_tr_bit_polizas_elim c, rf_tc_clasificador_cuentas tc  " ); 
  SQL.append (" where  tc.cuenta_mayor = substr(c.cuenta, 1, 4) and c.ejercicio=").append(pEjercicio).append("and c.tipo_forma='WA' and c.mes=").append(pMes).append(" and c.version=").append(pVersion).append(" and c.importe<>0");
  System.out.println("formaAmpliacionReduccion: "+SQL.toString());
  try {
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(conEliminacion);
    crs.beforeFirst();
    while(crs.next()){
        hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
        hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
        hm.put ("AMBITO",crs.getString("entAmbito"));//imprimeHM(hm);
        hm.put ("TIGA",crs.getString("TIGA"));//imprimeHM(hm);
        hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);
         
         
         hm.put ("IMPORTE_D_"+crs.getString("cuentaMayor"),((crs.getString("saldo_d")==null)?"0":crs.getString("saldo_d")));
         hm.put ("IMPORTE_H_"+crs.getString("cuentaMayor"),((crs.getString("saldo_h")==null)?"0":crs.getString("saldo_h")));
     

      cadenaTem.append(Cadena.construyeCadena(hm));
      cadenaTem.append("~");
      hm.clear();
        
    }
  }catch (Exception e){
     System.out.println ("Ha ocurrido un error en el metodo formaAmpliacionReduccion");
     System.out.println ("formaAmpliacionReduccion:" + SQL.toString());
     crs.close();
     crs=null;
     throw e;
  }finally {
     SQL.setLength(0);
     SQL=null;
     crs=null;
  }
  return cadenaTem.toString();
}
    
  public String formaEliminacionTipo2(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra) throws SQLException, Exception {
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer cadenaTem= new StringBuffer("");
    StringBuffer SQL = new StringBuffer(" select distinct c.cuenta_contable, substr(c.cuenta_contable,1,4) cuentaMayor, substr(c.cuenta_contable, 5, 1) sBcuentaMayor, substr(c.cuenta_contable,6,4) programa," ); 
    SQL.append (" substr(c.cuenta_contable,10,4) unidad, substr(c.cuenta_contable,14,3) entidad,substr(c.cuenta_contable,17,1) ambito," ); 
    SQL.append (" substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, substr(c.cuenta_contable,22,4) nivel7," ); 
    SQL.append (" substr(c.cuenta_contable,26,4) nivel8," );
    SQL.append (" case when tc.naturaleza ='D'" );
    //SQL.append ("    then ((oct_cargo_acum+nov_cargo) - (oct_abono_acum+nov_abono)) * -1 " );
    //SQL.append ("    else ((oct_abono_acum+nov_abono) - (oct_cargo_acum+nov_cargo)) * -1 " );
    SQL.append ("    then ").append(pMesLetra).append("_cargo * -1 ");
    SQL.append ("    else ").append(pMesLetra).append("_abono * -1 ");
    SQL.append (" end saldo, " ); 
    SQL.append (" tc.naturaleza ");
    SQL.append (" from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d, rf_tc_clasificador_cuentas tc");
    SQL.append (" where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id and tc.cuenta_mayor_id=c.cuenta_mayor_id");
    SQL.append (" and (c.cuenta_contable like'55992____________%' or c.cuenta_contable like'43993____________%')  ");
    SQL.append (" and c.nivel=5 and extract (year from c.fecha_vig_ini)=").append(pEjercicio).append(" and c.id_catalogo_cuenta = ").append(pCatCuenta); 
    SQL.append (" and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
    System.out.println("formaEliminacionTipo2: "+SQL.toString());
    try {
      crs = new CachedRowSet();
      crs.setCommand(SQL.toString());
      crs.execute(conEliminacion);
      crs.beforeFirst();
      while(crs.next()){
          hm.put ("PROGRAMA",crs.getString("PROGRAMA"));//imprimeHM(hm);
          hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
          hm.put ("AMBITO",crs.getString("entAmbito"));//imprimeHM(hm);
          hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);
      
        if (crs.getString("naturaleza").equals("D")){ 
         /* hm.put ("UNIDAD41201",crs.getString("unidad"));//imprimeHM(hm);
          hm.put ("AMBITO41201",crs.getString("entAmbito"));//imprimeHM(hm);
          hm.put ("NIVEL541201",crs.getString("nivel5"));//imprimeHM(hm);
          hm.put ("NIVEL641201",crs.getString("nivel6"));//imprimeHM(hm);
          hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);*/
          hm.put ("IMPORTE_INCREMENTOS",crs.getString("saldo"));//imprimeHM(hm);
          
        }else{
           /*hm.put ("UNIDAD41202",crs.getString("unidad"));//imprimeHM(hm);
           hm.put ("AMBITO41202",crs.getString("entAmbito"));//imprimeHM(hm);
           hm.put ("NIVEL541202",crs.getString("nivel5"));//imprimeHM(hm);
           hm.put ("NIVEL641202",crs.getString("nivel6"));//imprimeHM(hm);
           
           hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);*/
           hm.put ("IMPORTE_DECREMENTOS",crs.getString("saldo"));//imprimeHM(hm);
        }
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
          
      }
    }catch (Exception e){
       System.out.println ("Ha ocurrido un error en el metodo formaEliminacionTipo2");
       System.out.println ("formaEliminacionTipo2:" + SQL.toString());
       crs.close();
       crs=null;
       throw e;
    }finally {
       SQL.setLength(0);
       SQL=null;
       crs=null;
    }
    return cadenaTem.toString();
  }    
  
    public String formaEliminacionActivoFijo(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer("");
      StringBuffer SQL = new StringBuffer(" select distinct c.cuenta_contable, substr(c.cuenta_contable,1,4) cuentaMayor, substr(c.cuenta_contable, 5, 1) sBcuentaMayor, substr(c.cuenta_contable,6,4) programa," ); 
      SQL.append (" substr(c.cuenta_contable,10,4) unidad, substr(c.cuenta_contable,14,3) entidad,substr(c.cuenta_contable,17,1) ambito," ); 
      SQL.append (" substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, substr(c.cuenta_contable,22,4) nivel7," ); 
      SQL.append (" substr(c.cuenta_contable,26,4) nivel8," );
       SQL.append ("     decode(d.operacion_contable_id, 0,").append(pMesLetra).append("_cargo * -1 ) saldo_d,");
       SQL.append ("     decode(d.operacion_contable_id, 1,").append(pMesLetra).append("_abono * -1 ) saldo_h,"); 
      SQL.append (" tc.naturaleza ");
      SQL.append (" from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d, rf_tc_clasificador_cuentas tc");
      SQL.append (" where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id and tc.cuenta_mayor_id=c.cuenta_mayor_id");
      SQL.append (" and (c.cuenta_contable like'559930001________________%' or c.cuenta_contable like'439940001________________%')  ");
      SQL.append (" and c.nivel=7 and extract (year from c.fecha_vig_ini)=").append(pEjercicio).append(" and c.id_catalogo_cuenta = ").append(pCatCuenta); 
      SQL.append (" and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
      System.out.println("formaEliminacionActivoFijo: "+SQL.toString());
            
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(conEliminacion);
        crs.beforeFirst();
        
        while(crs.next()){
            hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
            hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
            hm.put ("AMBITO",crs.getString("entAmbito"));//imprimeHM(hm);
            hm.put ("PARTIDA",crs.getString("nivel6"));//imprimeHM(hm);
            hm.put ("EJERCICIO",crs.getString("nivel7"));//imprimeHM(hm);             
            hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);
          
            hm.put ("IMPORTE_D",((crs.getString("saldo_d")==null)?"0":crs.getString("saldo_d")));
            hm.put ("IMPORTE_H",((crs.getString("saldo_h")==null)?"0":crs.getString("saldo_h")));          
         
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
            
        }
      }catch (Exception e){
         System.out.println ("Ha ocurrido un error en el metodo formaEliminacionActivoFijo");
         System.out.println ("formaEliminacionActivoFijo:" + SQL.toString());
         crs.close();
         crs=null;
         throw e;
      }finally {
         SQL.setLength(0);
         SQL=null;
         crs=null;
      }
      return cadenaTem.toString();
    }      
    

        public String formaEliminacionPagosPresupuestarios(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra) throws SQLException, Exception {
          HashMap hm = new HashMap();
          CachedRowSet crs = null;    
          StringBuffer cadenaTem= new StringBuffer("");
          StringBuffer SQL = new StringBuffer(" select distinct c.cuenta_contable, substr(c.cuenta_contable,1,4) cuentaMayor, substr(c.cuenta_contable, 5, 1) sBcuentaMayor, substr(c.cuenta_contable,6,4) programa," ); 
          SQL.append (" substr(c.cuenta_contable,10,4) unidad, substr(c.cuenta_contable,14,3) entidad,substr(c.cuenta_contable,17,1) ambito," ); 
          SQL.append (" substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, substr(c.cuenta_contable,22,4) nivel7," ); 
          SQL.append (" substr(c.cuenta_contable,26,4) nivel8," );
          /*SQL.append (" case when tc.naturaleza ='D'" );
          //SQL.append ("    then ((oct_cargo_acum+nov_cargo) - (oct_abono_acum+nov_abono)) * -1 " );
          //SQL.append ("    else ((oct_abono_acum+nov_abono) - (oct_cargo_acum+nov_cargo)) * -1 " );
          SQL.append ("    then ").append(pMesLetra).append("_cargo * -1 ");
          SQL.append ("    else ").append(pMesLetra).append("_abono * -1 ");
          SQL.append (" end saldo, " ); */
           SQL.append ("     decode(d.operacion_contable_id, 0,").append(pMesLetra).append("_cargo * -1 ) saldo_d,");
           SQL.append ("     decode(d.operacion_contable_id, 1,").append(pMesLetra).append("_abono * -1 ) saldo_h,"); 
            SQL.append (" d.operacion_contable_id operacion_contable_id,");
          SQL.append (" tc.naturaleza ");
          SQL.append (" from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d, rf_tc_clasificador_cuentas tc");
          SQL.append (" where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id and tc.cuenta_mayor_id=c.cuenta_mayor_id and ");
          SQL.append(" ( ");
          SQL.append(" substr(c.cuenta_contable,1,4)='4221'  and substr(c.cuenta_contable,5,1)='1' \n ");
          SQL.append(" and ((substr(c.cuenta_contable,6,4) in ('0001','0000') and  substr(c.cuenta_contable,10,12) not in ('010000110003','010000110008')) or \n");
         SQL.append("       (substr(c.cuenta_contable,6,4) in ('0008','0009') and substr(c.cuenta_contable, 10,12) not in ('010000110003','010000110008'))"   );
//        SQL.append(" and ((substr(c.cuenta_contable,6,4) in ('0001','0000') and  substr(c.cuenta_contable,10,12) not in ('010000110003','010000110008')) or  (substr(c.cuenta_contable,6,4)='0008' and  substr(c.cuenta_contable,18,4) not in ('0003','0008') ))");
          SQL.append(" )  ");
          SQL.append(" )  ");
          SQL.append (" and extract (year from c.fecha_vig_ini)=").append(pEjercicio).append(" and c.id_catalogo_cuenta = ").append(pCatCuenta); 
          SQL.append (" and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
          System.out.println("formaEliminacionPagosPresupuestarios: "+SQL.toString());
                
          try {
            crs = new CachedRowSet();
            crs.setCommand(SQL.toString());
            crs.execute(conEliminacion);
            crs.beforeFirst();
            while(crs.next()){ 
                
                if (crs.getString("operacion_contable_id").equals("1")){ 
                 //if (crs.getString("naturaleza").equals("A")){ 
                   hm.put ("PROGRAMA",crs.getString("programa"));//imprimeHM(hm);
                   hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
                   hm.put ("AMBITO",crs.getString("entAmbito"));//imprimeHM(hm);
                   hm.put ("NIVEL6",crs.getString("nivel6"));//imprimeHM(hm);
                   hm.put ("NIVEL7",crs.getString("nivel7"));//imprimeHM(hm);
                   hm.put ("NIVEL8",crs.getString("nivel8"));//imprimeHM(hm);            
                   hm.put ("IMPORTE",crs.getString("saldo_h"));//imprimeHM(hm);
                   hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);
                 }              
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();
                
            }
          }catch (Exception e){
             System.out.println ("Ha ocurrido un error en el metodo formaEliminacionPagosPresupuestarios");
             System.out.println ("formaEliminacionPagosPresupuestarios:" + SQL.toString());
             crs.close();
             crs=null;
             throw e;
          }finally {
             SQL.setLength(0);
             SQL=null;
             crs=null;
          }
          return cadenaTem.toString();
        }          
   
    public String formaCancMinistracionesAUE(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra, String pVersion) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer("");
      StringBuffer SQL = new StringBuffer(" select distinct c.cuenta_contable, substr(c.cuenta_contable,1,4) cuentaMayor, substr(c.cuenta_contable, 5, 1) sBcuentaMayor,substr(c.cuenta_contable,6,4) programa," ); 
      SQL.append (" substr(c.cuenta_contable,10,4) unidad, substr(c.cuenta_contable,14,3) entidad,substr(c.cuenta_contable,17,1) ambito," ); 
      SQL.append (" substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) TIGA, substr(c.cuenta_contable,22,4) nivel7," ); 
      SQL.append (" substr(c.cuenta_contable,26,4) nivel8," );
      SQL.append(pMesLetra).append("_cargo * -1 saldo_d, ");
      SQL.append(pMesLetra).append("_abono * -1 saldo_h,");
      SQL.append (" tc.naturaleza ");
      SQL.append (" from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d, rf_tc_clasificador_cuentas tc");
      SQL.append (" where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id and tc.cuenta_mayor_id=c.cuenta_mayor_id");
      SQL.append (" and ( (substr(c.cuenta_contable, 1, 4) = '8120' or ");
      SQL.append ("        substr(c.cuenta_contable, 1, 4) = '8140' or ");
      SQL.append ("        substr(c.cuenta_contable, 1, 4) = '8150') ");
      //Se modifico para que no considere programa 8 y 9 y cualquier dato adicional se capture en pantalla
      SQL.append ("   and  substr(c.cuenta_contable,6,4) not in ('0008','0009')  ");
      
      SQL.append ("    and substr(c.cuenta_contable, 10, 4) >100 )  ");
      SQL.append (" and c.nivel=6 and extract (year from c.fecha_vig_ini)=").append(pEjercicio).append(" and c.id_catalogo_cuenta = ").append(pCatCuenta); 
      SQL.append (" and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
      //Se agrega una union para que lea los datos faltantes en la tabla de rf_tr_bit_polizas_elim
      SQL.append (" union all ");
      SQL.append ("  select distinct c.cuenta, substr(c.cuenta,1,4) cuentaMayor, substr(c.cuenta, 5, 1) sBcuentaMayor,substr(c.cuenta,6,4) programa, ");
      SQL.append ("  substr(c.cuenta,10,4) unidad, substr(c.cuenta,14,3) entidad,substr(c.cuenta,17,1) ambito, ");
      SQL.append ("  substr(c.cuenta,14,4) entAmbito, substr(c.cuenta,18,4) TIGA, substr(c.cuenta,22,4) nivel7, ");
      SQL.append ("  substr(c.cuenta,26,4) nivel8, ");
      SQL.append ("      decode(c.operacion_contable_id, 0,c.importe,0) saldo_d, ");
      SQL.append ("      decode(c.operacion_contable_id, 1,c.importe,0) saldo_h, ");
      SQL.append ("      tc.naturaleza ");
      SQL.append ("  from rf_tr_bit_polizas_elim c, rf_tc_clasificador_cuentas tc  ");
      SQL.append ("  where tc.cuenta_mayor = substr(c.cuenta, 1, 4) and c.tipo_forma='WF' and c.ejercicio=").append(pEjercicio).append(" and c.mes=").append(pMes).append(" and c.version=").append(pVersion).append(" and c.importe<>0 ");
      System.out.println("formaCancMinistracionesAUE: "+SQL.toString());
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(conEliminacion);
        crs.beforeFirst();
        while(crs.next()){
            hm.put ("PROGRAMA",crs.getString("PROGRAMA"));//imprimeHM(hm);
            hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
            hm.put ("AMBITO",crs.getString("entAmbito"));//imprimeHM(hm);
            hm.put ("TIGA",crs.getString("TIGA"));//imprimeHM(hm);
            hm.put ("REFERENCIA",crs.getString("cuenta_contable"));//imprimeHM(hm);
             
            if (crs.getString("cuentaMayor").equals("4221")){
               hm.put ("IMPORTE_H_"+crs.getString("cuentaMayor")+"_"+crs.getString("programa").substring(3)+"_"+crs.getString("nivel7").substring(0,1),((crs.getString("saldo_h")==null)?"0":crs.getString("saldo_h")));
            }else{             
               hm.put ("IMPORTE_D_"+crs.getString("cuentaMayor"),((crs.getString("saldo_d")==null)?"0":crs.getString("saldo_d")));
               hm.put ("IMPORTE_H_"+crs.getString("cuentaMayor"),((crs.getString("saldo_h")==null)?"0":crs.getString("saldo_h")));
            }

          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
            
        }
      }catch (Exception e){
         System.out.println ("Ha ocurrido un error en el metodo formaCancMinistracionesAUE");
         System.out.println ("formaCancMinistracionesAUE:" + SQL.toString());
         crs.close();
         crs=null;
         throw e;
      }finally {
         SQL.setLength(0);
         SQL=null;
         crs=null;
      }
      return cadenaTem.toString();
    }        
    
    public String entradasSalidasActivos(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer();
      StringBuffer SQL = new StringBuffer();      
      SQL.append ("  select p.poliza_id, substr(c.cuenta_contable,1,4) cuentaMayor, ");
      SQL.append ("         substr(c.cuenta_contable, 5, 1) subctamayor,substr(c.cuenta_contable,6,4) programa,");
      SQL.append ("         substr(c.cuenta_contable,10,4) unidad,");
      SQL.append ("         substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, d.referencia, d.operacion_contable_id,");
      SQL.append ("         decode(d.operacion_contable_id,0,d.importe,0) cargo, decode(d.operacion_contable_id,1,d.importe,0) abono");
      SQL.append ("         from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d");
      SQL.append ("         where c.cuenta_contable_id=d.cuenta_contable_id and p.poliza_id=d.poliza_id");
      SQL.append ("         and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.clasificacion_poliza_id=2 and  p.idevento=355 and p.mes=").append(pMes);
      SQL.append ("         and p.poliza_id = (select max(pi.poliza_id) poliza_id from rf_tr_polizas pi");
      SQL.append ("              where pi.id_catalogo_cuenta=").append(pCatCuenta).append(" and pi.idevento=355 and pi.mes=").append(pMes).append(" and pi.ejercicio=").append(pEjercicio).append(")");
      SQL.append ("         order by substr(c.cuenta_contable,1,4),substr(c.cuenta_contable, 5, 1)");
      System.out.println("entradasSalidasActivos: "+SQL.toString());
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(conEliminacion);
        crs.beforeFirst();
        while(crs.next()){
            hm.put ("PARTIDA",crs.getString("subctamayor"));//imprimeHM(hm);
            //hm.put ("PARTIDA",crs.getString("sBcuentaMayor"));//imprimeHM(hm);
            hm.put ("REFERENCIA",crs.getString("referencia"));//imprimeHM(hm);
            if(crs.getInt("operacion_contable_id")==0 && crs.getDouble("cargo")!=0.00) 
              hm.put ("IMPORTE_D_"+crs.getString("cuentaMayor"),crs.getString("cargo"));
            if(crs.getInt("operacion_contable_id")==1 && crs.getDouble("abono")!=0.00) 
              hm.put ("IMPORTE_H_"+crs.getString("cuentaMayor"),crs.getString("abono"));

          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
            
        }
      }catch (Exception e){
         System.out.println ("Ha ocurrido un error en el metodo entradasSalidasActivos");
         System.out.println ("entradasSalidasActivos:" + SQL.toString());
         crs.close();
         crs=null;
         throw e;
      }finally {
         SQL.setLength(0);
         SQL=null;
         crs=null;
      }
      return cadenaTem.toString();
    }  
    
    public String elimacionRemesas(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer();
      StringBuffer SQL = new StringBuffer();      
      SQL.append ("  select p.poliza_id, substr(c.cuenta_contable,1,4) cuentaMayor, ");
      SQL.append ("    substr(c.cuenta_contable, 5, 1) subctamayor,substr(c.cuenta_contable,6,4) programa,");
      SQL.append ("    substr(c.cuenta_contable,10,4) unidad,");
      SQL.append ("    substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, d.referencia, d.operacion_contable_id,");
      SQL.append ("    decode(d.operacion_contable_id,0,d.importe,0) cargo,decode(d.operacion_contable_id,1,d.importe,0) abono");
      SQL.append ("  from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d");
      SQL.append ("  where p.poliza_id=d.poliza_id and c.cuenta_contable_id=d.cuenta_contable_id and p.clasificacion_poliza_id=2 and p.origen='UH'");
      SQL.append ("    and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
      SQL.append ("    and p.referencia = 'ELIMINACION_REMESAS_").append(pMes).append("_").append(pEjercicio).append("'");
      SQL.append ("  order by substr(c.cuenta_contable,1,4),substr(c.cuenta_contable, 5, 1), d.operacion_contable_id");
      System.out.println("elimacionRemesas: "+SQL.toString());
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(conEliminacion);
        crs.beforeFirst();
        while(crs.next()){
            hm.put ("SUBCTA",crs.getString("subctamayor"));//imprimeHM(hm);
            hm.put ("REFERENCIA",crs.getString("referencia"));//imprimeHM(hm);
            if(crs.getInt("operacion_contable_id")==0 && crs.getDouble("cargo")!=0.00) 
              hm.put ("IMPORTE_CARGO",crs.getString("cargo"));
            if(crs.getInt("operacion_contable_id")==1 && crs.getDouble("abono")!=0.00) 
              hm.put ("IMPORTE_ABONO",crs.getString("abono"));

          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
            
        }
      }catch (Exception e){
         System.out.println ("Ha ocurrido un error en el metodo elimacionRemesas");
         System.out.println ("elimacionRemesas:" + SQL.toString());
         crs.close();
         crs=null;
         throw e;
      }finally {
         SQL.setLength(0);
         SQL=null;
         crs=null;
      }
      return cadenaTem.toString();
    }  
    
    public String elimacionEntradasAlmacen(Connection conEliminacion, String pCatCuenta, String pEjercicio , String pMes, String pMesLetra) throws SQLException, Exception {
      HashMap hm = new HashMap();
      CachedRowSet crs = null;
      StringBuffer cadenaTem= new StringBuffer();
      StringBuffer SQL = new StringBuffer();      
      /*SQL.append ("  select p.poliza_id, substr(c.cuenta_contable,1,4) cuentaMayor, ");
      SQL.append ("    substr(c.cuenta_contable, 5, 1) subctamayor,substr(c.cuenta_contable,6,4) programa,");
      SQL.append ("    substr(c.cuenta_contable,10,4) unidad,");
      SQL.append ("    substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, substr(c.cuenta_contable,22,4) nivel7, d.referencia, d.operacion_contable_id,");
      SQL.append ("    decode(d.operacion_contable_id,0,d.importe,0) cargo,decode(d.operacion_contable_id,1,d.importe,0) abono");
      SQL.append ("  from rf_tr_cuentas_contables c,rf_tr_polizas p, rf_tr_detalle_poliza d");
      SQL.append ("  where p.poliza_id=d.poliza_id and c.cuenta_contable_id=d.cuenta_contable_id");
      SQL.append ("    and p.id_catalogo_cuenta=").append(pCatCuenta).append(" and p.ejercicio=").append(pEjercicio).append(" and p.mes=").append(pMes);
      SQL.append ("    and p.referencia = 'ELIMINACION_ENTRADAS_ALMACEN_").append(pMes).append("_").append(pEjercicio).append("'");
      SQL.append ("\n  order by substr(c.cuenta_contable,1,4),substr(c.cuenta_contable, 5, 1), d.operacion_contable_id");*/
      try {
          if ((Integer.valueOf(pMes) >= 6 && Integer.valueOf(pEjercicio)==2013) || Integer.valueOf(pEjercicio)>2013) {
            SQL.append ("\n  select substr(c.cuenta_contable,1,4) cuentaMayor,");
            SQL.append ("\n     substr(c.cuenta_contable, 5, 1) subctamayor,substr(c.cuenta_contable,6,4) programa,");
            SQL.append ("\n     substr(c.cuenta_contable,10,4) unidad,");
            SQL.append ("\n     substr(c.cuenta_contable,14,4) entAmbito, substr(c.cuenta_contable,18,4) nivel6, substr(c.cuenta_contable,22,4) nivel7, c.cuenta_contable as referencia,");
            //SQL.append ("\n d.operacion_contable_id ");
            //Esta condicion solo aplico una vez, pero se considero por las pruebas que se requirieron hacer o por si vuelven a abrir dicho mes
            if(pMes.equals("06") && pEjercicio.equals("2013"))
              SQL.append ("\n    (c.").append(pMesLetra).append("_cargo_acum*-1) cargo, (c.").append(pMesLetra).append("_abono_acum*-1) abono");
            //Esta condicion siempre debe de aplicar para despues de junio del 2013 en adelante
            if ((Integer.valueOf(pMes) > 6 && Integer.valueOf(pEjercicio) == 2013) || Integer.valueOf(pEjercicio)>2013)
              SQL.append ("\n    (c.").append(pMesLetra).append("_cargo*-1) cargo, (c.").append(pMesLetra).append("_abono*-1) abono");
            SQL.append ("\n  from rf_tr_cuentas_contables c");
            SQL.append ("\n  where c.id_catalogo_cuenta=").append(pCatCuenta).append(" and extract(year from c.fecha_vig_ini)=").append(pEjercicio);
            SQL.append ("\n    and (c.cuenta_contable like '5121_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5122_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5123_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5124_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5125_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5126_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5127_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5128_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '5129_0001________0004%' and c.nivel=6 or");
            SQL.append ("\n    c.cuenta_contable like '439920001________00050004%' and c.nivel=7)");
            if(pMes.equals("06") && pEjercicio.equals("2013"))
              SQL.append ("\n    and (c.").append(pMesLetra).append("_cargo_acum<>0 or c.").append(pMesLetra).append("_abono_acum<>0)");
          //Esta condicion siempre debe de aplicar para despues de junio del 2013 en adelante
            if ((Integer.valueOf(pMes) > 6 && Integer.valueOf(pEjercicio) == 2013) || Integer.valueOf(pEjercicio)>2013)
              SQL.append ("\n    and (c.").append(pMesLetra).append("_cargo<>0 or c.").append(pMesLetra).append("_abono<>0)");
            SQL.append ("\n  order by substr(c.cuenta_contable,1,4),substr(c.cuenta_contable, 5, 1)");
            
            System.out.println("elimacionEntradasAlmacen: "+SQL.toString());
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(conEliminacion);
        crs.beforeFirst();
        while(crs.next()){
            hm.put ("SUBCTA",crs.getString("subctamayor"));//imprimeHM(hm);
            hm.put ("UNIDAD",crs.getString("unidad"));//imprimeHM(hm);
            hm.put ("AMBITO",crs.getString("entambito"));//imprimeHM(hm);
            hm.put ("REFERENCIA",crs.getString("referencia"));//imprimeHM(hm);
            if(crs.getDouble("cargo")!=0.00) 
              hm.put ("IMPORTE_"+crs.getString("cuentaMayor"),crs.getString("cargo"));
            if(crs.getDouble("abono")!=0.00) 
              hm.put ("IMPORTE_"+crs.getString("cuentaMayor"),crs.getString("abono"));

          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
            
        }
      }
      }catch (Exception e){
         System.out.println ("Ha ocurrido un error en el metodo elimacionEntradasAlmacen");
         System.out.println ("elimacionEntradasAlmacen:" + SQL.toString());
         crs.close();
         crs=null;
         throw e;
      }finally {
         SQL.setLength(0);
         SQL=null;
         crs=null;
      }
      return cadenaTem.toString();
    }  
}