package sia.rf.contabilidad.sistemas.cierreAnual;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import java.util.HashMap;

import sia.rf.contabilidad.registroContableEvento.Cadena;

public class SiafmPolizasTraspaso {
    public SiafmPolizasTraspaso() {
    }

    private String referenciaGral;     

    public void setReferenciaGral(String referenciaGral) {
        this.referenciaGral = referenciaGral;
    }

    public String getReferenciaGral() {
        return referenciaGral;
    }
    
    
  public String formaOATraspaso8140_a_8120(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
      HashMap hm = new HashMap(); 
      StringBuffer SQL = new StringBuffer("");
      StringBuffer cadenaTem= new StringBuffer("");
      Statement stQuery=null;
      ResultSet rsQuery= null;
      try{
          SQL.append("SELECT "); 
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
          SQL.append("'ORIGEN' AS tipo, ");
          if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }
          SQL.append("end) saldo_actual "); 
          SQL.append("FROM rf_tr_cuentas_contables cc "); 
          SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
          SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8140 ");  
          SQL.append("and cc.nivel=6  ");  
          SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
          SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
          if(ptipoCampo.equals("_pub")){
          SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
          SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }else{
              SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
              SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }
          SQL.append("end) <>0  ");
          SQL.append(" UNION ALL ");
          SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual ");
          SQL.append("FROM( "); 
          SQL.append("SELECT ");  
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
          SQL.append("'DESTINO' as tipo, ");
          if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }
          SQL.append("end) saldo_actual "); 
          SQL.append("FROM rf_tr_cuentas_contables cc "); 
          SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
          SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8140  ");  
          SQL.append("and cc.nivel=6 ");  
          SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
          SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
          if(ptipoCampo.equals("_pub")){
              SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
              SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }else{
              SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
              SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
          }
          SQL.append("end) <>0 "); 
          SQL.append(") sub "); 
          SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo ");  
          stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
          rsQuery = stQuery.executeQuery(SQL.toString());         
          while (rsQuery.next()){
              if (rsQuery.getString("tipo").equals("ORIGEN")){
                  hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                  hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                  hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                  hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                  hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                  hm.put ("IMPORTE8140",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                   hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ01":"PAJ01_CENTRAL");//impri
                  cadenaTem.append(Cadena.construyeCadena(hm));
                  cadenaTem.append("~");
                  hm.clear();
              }else{
                  hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                  hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                  hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                  hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                   hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                  hm.put ("IMPORTE8120",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                  hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ01":"PAJ01_CENTRAL");//impri
                  cadenaTem.append(Cadena.construyeCadena(hm));
                  cadenaTem.append("~");
                  hm.clear();                    
              }
          } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ01":"PAJ01_CENTRAL";
      }
      catch(Exception e){
        System.out.println("Ocurrio un error al accesar al metodo  formaOATraspaso8140_a_8120 "+e.getMessage());
        System.out.println("SQL  formaOATraspaso8140_a_8120: "+SQL.toString());
        throw e;
      } //Fin catch
      finally{
        if (rsQuery!=null){
          rsQuery.close();
          rsQuery=null;
        }
        if (stQuery!=null){
          stQuery.close();
          stQuery=null;
        }
      } //Fin finally       
      return cadenaTem.toString();
  }
    
    
    public String formaOBTraspaso8130_a_8120(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");   
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, "); 
            SQL.append("'ORIGEN' AS tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(cc.nov_abono_acum + cc.dic_abono ").append(") saldo_actual "); 
            }else{
                  SQL.append("(cc.nov_abono_acum_eli + cc.dic_abono_eli ").append(") saldo_actual "); 
            }
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla  on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8130 ");            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (cc.nov_abono_acum + cc.dic_abono ").append(" )<>0 ");  
             }else{
                SQL.append(" and (cc.nov_abono_acum_eli + cc.dic_abono_eli ").append(" )<>0 ");  
            }
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( ");
            SQL.append("SELECT ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append(" (case when substr(cc.cuenta_contable,18,4) in ('0001','0003') then '0001' else '0002' end) as tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(cc.nov_abono_acum + cc.dic_abono ").append(") saldo_actual"); 
            }else{
                  SQL.append("(cc.nov_abono_acum_eli + cc.dic_abono_eli").append(") saldo_actual"); 
            }
            SQL.append(" FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN  ");
            SQL.append("rf_tc_clasificador_cuentas cla  ");
            SQL.append("on cc.cuenta_mayor_id = cla.cuenta_mayor_id  ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8130  ");
            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
            SQL.append("and cc.id_catalogo_cuenta=1 ");
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (cc.nov_abono_acum + cc.dic_abono ").append(" )<>0 ");  
             }else{
                SQL.append(" and (cc.nov_abono_acum_eli + cc.dic_abono_eli ").append(" )<>0 ");  
            }
            SQL.append(") sub ");
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo ");
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8130",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ02":"PAJ02_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8120",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ02":"PAJ02_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ02":"PAJ02_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOBTraspaso8130_a_8120 "+e.getMessage());
          System.out.println("SQL formaOBTraspaso8130_a_8120: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
    public String formaOCTraspaso8120_a_8130(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo ) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{               
            SQL.append("SELECT  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");   
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append(" (cc.nov_cargo_acum + dic_cargo").append(") saldo_actual "); 
            }else{
                  SQL.append(" (cc.nov_cargo_acum_eli + dic_cargo_eli").append(") saldo_actual "); 
             }
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla  on cc.cuenta_mayor_id = cla.cuenta_mayor_id "); 
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8130 "); 
            SQL.append("and cc.nivel=6  "); 
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append("and (cc.nov_cargo_acum + dic_cargo ").append(")<>0 ");  
            }else{
                SQL.append("and (cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") <>0 ");  
                        }
            SQL.append(" UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  "); 
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor,");     
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa,"); 
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad,"); 
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append(" (case when substr(cc.cuenta_contable,18,4) in ('0001','0003') then '0001' else '0002' end) as tiga, ");
            SQL.append(" 'DESTINO' as tipo, ");
           if(ptipoCampo.equals("_pub")){
                  SQL.append("(cc.nov_cargo_acum + dic_cargo ").append(") saldo_actual "); 
          }else{
                  SQL.append("(cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") saldo_actual "); 
           }
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id   "); 
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8130 "); 
            SQL.append("and cc.nivel=6 "); 
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (cc.nov_cargo_acum + dic_cargo ").append(") <>0 ");  
            }else{
                SQL.append(" and (cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") <>0");  
            }
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo "); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));  
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8130",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ03":"PAJ03_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));  
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8120",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ03":"PAJ03_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
            referenciaGral=ptipoCampo.equals("_pub")?"PAJ03":"PAJ03_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOCTraspaso8120_a_8130 "+e.getMessage());
          System.out.println("SQL formaQCTraspaso8120_a_8130: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
    public String formaODTraspaso8120_a_8110(Connection con, String pCatCuenta, String pEjercicio,String ptipoCampo  ) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{   
            SQL.append("SELECT  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,"); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN  rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  "); 
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8120 "); 
            SQL.append("and cc.nivel=6  "); 
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append("UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'DESTINO' as tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id "); 
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8120  "); 
            SQL.append("and cc.nivel=6 "); 
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo "); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                    if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));  
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8120",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ04":"PAJ04_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));  
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8110",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ04":"PAJ04_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ04":"PAJ04_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaODTraspaso8120_a_8110 "+e.getMessage());
          System.out.println("SQL formaODTraspaso8120_a_8110: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
   
    
     public String formaOETraspaso8220_a_8210(Connection con, String pCatCuenta, String pEjercicio,String ptipoCampo  ) throws SQLException, Exception {
         HashMap hm = new HashMap(); 
         StringBuffer SQL = new StringBuffer("");
         StringBuffer cadenaTem= new StringBuffer("");
         Statement stQuery=null;
         ResultSet rsQuery= null;
         try{   
             SQL.append("SELECT  "); 
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
             SQL.append("'ORIGEN' AS tipo, "); 
             if(ptipoCampo.equals("_pub")){
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc "); 
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  "); 
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8220  "); 
             SQL.append("and cc.nivel=6  "); 
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
             SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0 "); 
             SQL.append("UNION ALL "); 
             SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
             SQL.append("FROM( "); 
             SQL.append("SELECT  "); 
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
             SQL.append("'DESTINO' AS tipo, "); 
             if(ptipoCampo.equals("_pub")){
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc "); 
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  "); 
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8220 "); 
             SQL.append("and cc.nivel=6  "); 
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio); 
             SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0 "); 
             SQL.append(") sub "); 
             SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo "); 
             stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rsQuery = stQuery.executeQuery(SQL.toString());         
             while (rsQuery.next()){
                 if (rsQuery.getString("tipo").equals("ORIGEN")){
                     hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                     hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                     hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                     hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                     hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                     hm.put ("IMPORTE8220",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ05":"PAJ05_CENTRAL");//impri
                     cadenaTem.append(Cadena.construyeCadena(hm));
                     cadenaTem.append("~");
                     hm.clear();
                 }else{
                     hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                     hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                     hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                     hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                     hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                       
                     hm.put ("IMPORTE8210",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ05":"PAJ05_CENTRAL");//impri
                     cadenaTem.append(Cadena.construyeCadena(hm));
                     cadenaTem.append("~");
                     hm.clear();                    
                 }
             } //del while
            referenciaGral=ptipoCampo.equals("_pub")?"PAJ05":"PAJ05_CENTRAL";
         }
         catch(Exception e){
           System.out.println("Ocurrio un error al accesar al metodo formaOETraspaso8220_a_8210 "+e.getMessage());
           System.out.println("SQL formaOETraspaso8220_a_8210: "+SQL.toString());
           throw e;
         } //Fin catch
         finally{
           if (rsQuery!=null){
             rsQuery.close();
             rsQuery=null;
           }
           if (stQuery!=null){
             stQuery.close();
             stQuery=null;
           }
         } //Fin finally       
         return cadenaTem.toString();
     }
    //--------------------------
    public String formaOFTraspaso8240_a_8220(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");   
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8240  ");
            SQL.append("and cc.nivel=7  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append("UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,nivel7,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1000') )then '0001' ");
            SQL.append("else( ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0002','2000','3000','4000','5000','6000')) then '0002' ");
            SQL.append("else '0005' end ");
            SQL.append(") end tiga, ");
            SQL.append("'0000' as nivel7, ");
            SQL.append("'DESTINO' as tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc  ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8240  ");
            SQL.append("and cc.nivel=7  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,nivel7,tipo "); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));//imprimeHM(hm);  
                    hm.put ("IMPORTE8240",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ06":"PAJ06_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));//imprimeHM(hm);  
                    hm.put ("IMPORTE8220",rsQuery.getString("saldo_actual"));//imprimeHM(hm)
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ06":"PAJ06_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                } 
                
            } //del while

             referenciaGral=ptipoCampo.equals("_pub")?"PAJ06":"PAJ06_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOFTraspaso8240_a_8220 "+e.getMessage());
          System.out.println("SQL formaOFTraspaso8240_a_8220: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
    public String formaOGTraspasoCargos8230_a_8220(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append(" (cc.nov_cargo_acum + dic_cargo ").append(") saldo_actual "); 
            }else{
                  SQL.append(" (cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") saldo_actual "); 
             }
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8230 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (cc.nov_cargo_acum + dic_cargo ").append(") <>0 ");  
             }else{
                SQL.append(" and (cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") <>0 ");  
            }
            SQL.append(" UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  ");
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','0003') )then '0001' ");
            SQL.append("else( ");
            SQL.append(" case when (substr(cc.cuenta_contable,18,4) in('0002','0004')) then '0002' ");
            SQL.append("end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(cc.nov_cargo_acum + dic_cargo ").append(") saldo_actual "); 
            }else{
                  SQL.append("(cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") saldo_actual "); 
            }
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8230 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and(cc.nov_cargo_acum + dic_cargo ").append(") <>0 ");  
            }else{
                SQL.append(" and(cc.nov_cargo_acum_eli + dic_cargo_eli ").append(") <>0 ");  
             }
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo"); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
               if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8230",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ07":"PAJ07_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8220",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ07":"PAJ07_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
           } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ07":"PAJ07_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOGTraspasoCargos8230_a_8220 "+e.getMessage());
          System.out.println("SQL formaOGTraspaso8230_a_8220: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
    
    public String formaOHAbonosTraspasos8230_a_8220(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append(" (cc.nov_abono_acum + dic_abono ").append(") saldo_actual "); 
            }else{
                  SQL.append(" (cc.nov_abono_acum_eli + dic_abono_eli ").append(") saldo_actual "); 
             }
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8230 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (cc.nov_abono_acum + dic_abono ").append(") <>0 ");  
             }else{
                SQL.append(" and (cc.nov_abono_acum_eli + dic_abono_eli ").append(") <>0 ");  
            }
            SQL.append("UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  ");
            SQL.append(" substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','0003') )then '0001' ");
            SQL.append("else( ");
            SQL.append(" case when (substr(cc.cuenta_contable,18,4) in('0002','0004')) then '0002' ");
            SQL.append("end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(cc.nov_abono_acum + dic_abono ").append(") saldo_actual "); 
            }else{
                  SQL.append("(cc.nov_abono_acum_eli + dic_abono_eli ").append(") saldo_actual "); 
            }
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8230 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and(cc.nov_abono_acum + dic_abono ").append(") <>0");  
            }else{
                SQL.append(" and(cc.nov_abono_acum_eli + dic_abono_eli ").append(") <>0");  
             }
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo"); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
               if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8230",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ08":"PAJ08_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE8220",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ08":"PAJ08_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
           } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ08":"PAJ08_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOHAbonosTraspaso8230_a_8220 "+e.getMessage());
          System.out.println("SQL formaOHAbonosTraspaso8230_a_8220: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
   }
        
    public String formaOITraspasos8250_8260_a_9300(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8250 ");
            SQL.append("or substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8260 ) ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append(" end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo ");
            SQL.append("UNION ALL "); 
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("'9300' as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1000') )then '0001' ");
            SQL.append("else( ");
            SQL.append(" case when (substr(cc.cuenta_contable,18,4) in('0002','2000','3000','4000','5000','6000','7000')) then '0002'  end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8250 ");
            SQL.append("or substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8260 )");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
            SQL.append("end) <>0 "); 
            SQL.append(" ) sub2 "); 
            SQL.append("GROUP BY  mayor,submayor,programa, unidad, ambito,tiga,tipo"); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
               if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ09":"PAJ09_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE9300",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ09":"PAJ09_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
           } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ09":"PAJ09_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOITraspasos8250_8260_a_9300 "+e.getMessage());
          System.out.println("SQL formaOITraspasos8250_8260_a_9300: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
    
    public String formaOJTraspasos8150_8270_9300_a_9100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            //1
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8150 ");
            SQL.append("OR substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=9300 ");
            SQL.append("OR substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8270) ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo ");
            
            // DESTINO 2
            SQL.append("UNION ALL "); 
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM( ");
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("'9100' as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'DESTINO' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8150 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo ");
            //3 DESTINO
             SQL.append("UNION ALL "); 
             SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
             SQL.append("SELECT  ");
             SQL.append("'9100' as mayor, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
             SQL.append("'DESTINO' AS tipo,  "); 
             if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end)*-1 saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc ");
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=9300 ");
             SQL.append("and cc.nivel=6 ");
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
             SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
             SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
             SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0  ");
             SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo ");
            //4 DESTINO
            SQL.append("UNION ALL "); 
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("'9100' as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1000') )then '0001' ");
            SQL.append("else( ");
            SQL.append(" case when (substr(cc.cuenta_contable,18,4) in('0002','2000','3000','4000','5000','6000','7000')) then '0002'  end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end)*-1 saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8270 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" ) sub2 "); 
            SQL.append("GROUP BY  mayor,submayor,programa, unidad, ambito,tiga,tipo");             
            SQL.append(" ) destino "); 
            SQL.append("GROUP BY  mayor,submayor,programa, unidad, ambito,tiga,tipo"); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
               if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ10":"PAJ10_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));//imprimeHM(hm);                      
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ10":"PAJ10_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
           } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ10":"PAJ10_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOITraspasos8250_8260_a_9300 "+e.getMessage());
          System.out.println("SQL formaOITraspasos8150_8270_9300_a_9100: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    } 

    public String formaOKTraspasos8110_8210_a_9100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            //1        
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8110 ");
            SQL.append("or substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8210 )");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            //2
            SQL.append(" UNION ALL "); 
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM ( ");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'DESTINO' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8110 ");
            SQL.append("and cc.nivel=6 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo ");
                //3
                SQL.append("UNION ALL "); 
                SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual FROM( ");
                SQL.append("SELECT  ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
                SQL.append("'DESTINO' AS tipo,  "); 
                if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end)*-1 saldo_actual "); 
                SQL.append("FROM rf_tr_cuentas_contables cc ");
                SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=8210 ");
                SQL.append("and cc.nivel=6 ");
                SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                    SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                    SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) <>0  ");
                SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,tipo");
       
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
            if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ11":"PAJ11_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));  
                    hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ11":"PAJ11_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
            } //del while
            referenciaGral=ptipoCampo.equals("_pub")?"PAJ11":"PAJ11_CENTRAL";
            }
            catch(Exception e){
              System.out.println("Ocurrio un error al accesar al metodo formaOKTraspasos8110_8210_a_9100 "+e.getMessage());
              System.out.println("SQL formaOKTraspasos8110_8210_a_9100: "+SQL.toString());
            throw e;
            } //Fin catch
            finally{
              if (rsQuery!=null){
                rsQuery.close();
                rsQuery=null;
                }
                if (stQuery!=null){
                stQuery.close();
                stQuery=null;
                }
            } //Fin finally
        return cadenaTem.toString();
}   

    public String formaOLTraspasos4221_4399_4319_a_6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
        ////1
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
         if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
            SQL.append("end) saldo_actual, 1 as tipoSaldo "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0100' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0011' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0003' ");
            SQL.append("and cc.nivel=8 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
         if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
         }else{
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
         }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo, tipoSaldo");
            //2
            SQL.append(" UNION ALL ");
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
            SQL.append("end) saldo_actual, 2 as tipoSaldo "); 
            SQL.append("FROM ");
            SQL.append("(select cuenta_contable,nivel_max ");
            SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
            SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
            SQL.append(" from rf_tr_cuentas_contables cc ");
            SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4221_000_%' ");
            SQL.append("   and cc.nivel >= 5 ");
            SQL.append(")where   nivel= nivel_max ) nn,");
            SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0100' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0011' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0003' ");
            SQL.append("and cc.nivel=nn.nivel_max ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
            SQL.append("end) <>0  ");
            
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
    //3 y 31 SON DOS QUERYS PORQUE EL VALOR DEL NIVEL DEPENDIENDO DEL VALOR DEL NIVEL6
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("'0000' as nivel7, ");
         SQL.append("'0000' as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 3 as tipoSaldo "); 
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0002') ");
         SQL.append("and cc.nivel=6 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append(" end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo ");
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
         SQL.append("'0000' as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 31 as tipoSaldo "); 
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))in ('0003','0004') ");
         SQL.append("and cc.nivel=7 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
         //4
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 4 as tipoSaldo "); 
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0009' ");
         SQL.append("and cc.nivel=7 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");       
        //5
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 5 as tipoSaldo "); 
         SQL.append("FROM ");
         SQL.append("(select cuenta_contable,nivel_max ");
         SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
         SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
        SQL.append(" from rf_tr_cuentas_contables cc ");
        SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4399_000_%' ");
        SQL.append("   and cc.nivel >= 5 ");
        SQL.append(")where   nivel= nivel_max ) nn,");
        SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
        SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4399 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>5 ");         
         SQL.append("and cc.nivel=nn.nivel_max ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
        //6
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 6 as tipoSaldo "); 
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4399 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5 ");         
         SQL.append("and cc.nivel=7 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
        //7
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
         SQL.append("'ORIGEN' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 7 as tipoSaldo "); 
         SQL.append("FROM ");
         SQL.append("(select cuenta_contable,nivel_max ");
         SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
         SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
         SQL.append(" from rf_tr_cuentas_contables cc ");
         SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4319_000_%' ");
         SQL.append("   and cc.nivel >= 5 ");
         SQL.append(")where   nivel= nivel_max ) nn,");
         SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4319 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0008', '0009', '0001') ");
         SQL.append("and cc.nivel=nn.nivel_max ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");   
         ///////////////SEGUNDA PARTE DE QUERYS----------------------QUERYS CUENTA DESTINO////////////////////////////////////////////////////////////////////////////////
          //1 DESTINO       
              SQL.append(" UNION ALL ");
              SQL.append(" SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
              SQL.append(" SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
              SQL.append("SELECT  ");
              SQL.append(" '6100' as mayor, ");
              SQL.append(" '2' as submayor, ");  
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
              SQL.append("case when (substr(cc.cuenta_contable,22,4) in('0001','1000') )then '0001'  else( case when (substr(cc.cuenta_contable,22,4) in('0002','2000','3000','4000','5000','6000')) then '0002' else  substr(cc.cuenta_contable,22,4) end) end tiga, ");
              SQL.append("'0000' as nivel7, ");
              SQL.append("'0000' as nivel8, ");
              SQL.append("'DESTINO' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
              SQL.append("end) saldo_actual, 1 as tipoSaldo "); 
              SQL.append("FROM rf_tr_cuentas_contables cc ");
              SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
              SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
              SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0100' ");
              SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0011' ");
              SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0003' ");
              SQL.append("and cc.nivel=8 ");
              SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
              SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
              SQL.append("end) <>0  ");
              SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
              //2 destino
              SQL.append(" UNION ALL ");
              SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
              SQL.append("SELECT  ");
              SQL.append(" '6100' as mayor, ");
              SQL.append(" '2' as submayor, ");  
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
              SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1001','1003','2007') )then '0001'  else(case when (substr(cc.cuenta_contable,18,4) in('0002','0004','0102','0009')) then '0002' else  substr(cc.cuenta_contable,22,4) end) end tiga, ");
              SQL.append("'0000' as nivel7, ");
              SQL.append("'0000' as nivel8, ");
              SQL.append("'DESTINO' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
           SQL.append("end) saldo_actual, 2 as tipoSaldo "); 
           SQL.append("FROM ");
           SQL.append("(select cuenta_contable,nivel_max ");
           SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
           SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
          SQL.append(" from rf_tr_cuentas_contables cc ");
          SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4221_000_%' ");
          SQL.append("   and cc.nivel >= 5 ");
          SQL.append(")where   nivel= nivel_max ) nn,");
          SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
          SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
          SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0100' ");
              SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0011' ");
      SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0003' ");
              SQL.append("and cc.nivel=nn.nivel_max ");
              SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
              SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
              SQL.append("end) <>0  ");
              SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo ");
         //3 y 31 DESTINO
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append(" '6100' as mayor, ");
         SQL.append(" '2' as submayor, ");  
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("'0000' as nivel7, ");
         SQL.append("'0000' as nivel8, ");
         SQL.append("'DESTINO' AS tipo,  "); 
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 3 as tipoSaldo "); 
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0002') ");
         SQL.append("and cc.nivel=6 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }  
         SQL.append(" end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo ");
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append(" '6100' as mayor, ");
         SQL.append("'2' as submayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
         SQL.append("'0000' as nivel7, ");
         SQL.append("'0000' as nivel8, ");
         SQL.append("'DESTINO' AS tipo,  ");
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 31 as tipoSaldo ");
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))in ('0003','0004') ");
         SQL.append("and cc.nivel=7 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }  
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo ");
         //4
         SQL.append(" UNION ALL ");
         SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
         SQL.append("SELECT  ");
         SQL.append("'6100' as mayor, ");
         SQL.append("'2' as submayor, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
         SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
         SQL.append("case when (substr(cc.cuenta_contable,22,4) in('0001','1000') )then '0001'  else '0002' end tiga, ");
         SQL.append("'0000' as nivel7, ");
         SQL.append("'0000' as nivel8, ");
         SQL.append("'DESTINO' AS tipo,  ");
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) saldo_actual, 4 as tipoSaldo ");
         SQL.append("FROM rf_tr_cuentas_contables cc ");
         SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
         SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4221 ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0100' ");
         SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0009' ");
         SQL.append("and cc.nivel=7 ");
         SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
         SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
         SQL.append("end) <>0  ");
         SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
         //5
          SQL.append(" UNION ALL ");
          SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
          SQL.append("SELECT  ");
          SQL.append("'6100' as mayor, ");
          SQL.append(" '2' as submayor, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
          SQL.append("'0002' tiga, ");
          SQL.append("'0000' as nivel7, ");
          SQL.append("'0000' as nivel8, ");
          SQL.append("'DESTINO' AS tipo,  ");
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
          SQL.append("end) saldo_actual, 5 as tipoSaldo ");
      SQL.append("FROM ");
      SQL.append("(select cuenta_contable,nivel_max ");
      SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
      SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
      SQL.append(" from rf_tr_cuentas_contables cc ");
      SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4399_000_%' ");
      SQL.append("   and cc.nivel >= 5 ");
      SQL.append(")where   nivel= nivel_max ) nn,");
      SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
      SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4399 ");
          SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>5 ");         
          SQL.append("and cc.nivel=nn.nivel_max ");
          SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
          SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
          SQL.append("end) <>0  ");
          SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
          //6
          SQL.append(" UNION ALL ");
          SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
          SQL.append("SELECT  ");
          SQL.append("'6100' as mayor, ");
          SQL.append("'2' as submayor,  ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
          SQL.append("'0000' as nivel7, ");
          SQL.append("'0000' as nivel8, ");
          SQL.append("'DESTINO' AS tipo,  ");
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
          SQL.append("end) saldo_actual, 6 as tipoSaldo ");
          SQL.append("FROM rf_tr_cuentas_contables cc ");
          SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
          SQL.append(" WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4399 ");
          SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5 ");          
          SQL.append("and cc.nivel=7 ");
          SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
          SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
          SQL.append("end) <>0  ");
          SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
          //7
          SQL.append(" UNION ALL ");
          SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga, nivel7, nivel8, tipo,sum(saldo_actual) as saldo_actual, tipoSaldo FROM(");
          SQL.append("SELECT  ");
          SQL.append("'6100' as mayor, ");
          SQL.append("'2' as submayor, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
          SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
          SQL.append(" '0002' tiga, ");
          SQL.append("'0000' as nivel7, ");
          SQL.append("'0000' as nivel8, ");
          SQL.append("'DESTINO' AS tipo,  ");
        if(ptipoCampo.equals("_pub")){
            SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
            SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
           SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        } 
          SQL.append("end) saldo_actual, 7 as tipoSaldo ");
          SQL.append("FROM ");
          SQL.append("(select cuenta_contable,nivel_max ");
          SQL.append("from (select cc.cuenta_contable,cc.nivel,(select max(cc2.nivel) from rf_tr_cuentas_contables cc2 where cc2.id_catalogo_cuenta = ").append(pCatCuenta).append(" and to_char(cc2.fecha_vig_ini,'yyyy') =").append(pEjercicio);
          SQL.append("      and cc2.cuenta_contable like substr(cc.cuenta_contable, 1, ((cc.nivel-1)*4)+1)||'%'  ) as nivel_max ");
          SQL.append(" from rf_tr_cuentas_contables cc ");
          SQL.append("  where cc.id_catalogo_cuenta =").append(pCatCuenta).append(" and to_char(cc.fecha_vig_ini,'yyyy') =").append(pEjercicio).append(" and cc.cuenta_contable like '4319_000_%' ");
          SQL.append("   and cc.nivel >= 5 ");
          SQL.append(")where   nivel= nivel_max ) nn,");
          SQL.append("rf_tr_cuentas_contables cc  INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
          SQL.append(" WHERE  nn.cuenta_contable=cc.cuenta_contable and substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4319 ");
          SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0008', '0009', '0001') ");
          SQL.append("and cc.nivel=nn.nivel_max ");
          SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
          SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
        if(ptipoCampo.equals("_pub")){
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }else{
           SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
           SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
        }
          SQL.append("end) <>0  ");
          SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
          SQL.append(") destino GROUP BY mayor, submayor,programa, unidad, ambito,tiga, nivel7, nivel8, tipo, tipoSaldo");
          
        stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        rsQuery = stQuery.executeQuery(SQL.toString());         
        while (rsQuery.next()){
            if (rsQuery.getString("tipo").equals("ORIGEN")){
              hm.put("MAYOR", rsQuery.getString("mayor"));
              hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
              hm.put ("PROGRAMA",rsQuery.getString("programa"));            
              hm.put ("UNIDAD",rsQuery.getString("unidad"));                
              hm.put ("AMBITO",rsQuery.getString("ambito"));
              hm.put ("TIGA",rsQuery.getString("tiga"));
              hm.put ("NIVEL7",rsQuery.getString("nivel7"));
              hm.put ("NIVEL8",rsQuery.getString("nivel8"));
              hm.put ("IMPORTE".concat(rsQuery.getString("tipoSaldo")),rsQuery.getString("saldo_actual"));
              hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ12":"PAJ12_CENTRAL");//impri
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();
            }else{
              hm.put("MAYOR", rsQuery.getString("mayor"));
              hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
              hm.put ("PROGRAMA",rsQuery.getString("programa"));            
              hm.put ("UNIDAD",rsQuery.getString("unidad"));                
              hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
              hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);
              hm.put ("NIVEL7",rsQuery.getString("nivel7"));
              hm.put ("NIVEL8",rsQuery.getString("nivel8"));
              hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));
              hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ12":"PAJ12_CENTRAL");//impri
              cadenaTem.append(Cadena.construyeCadena(hm));
              cadenaTem.append("~");
              hm.clear();
        }
    } //del while
    referenciaGral=ptipoCampo.equals("_pub")?"PAJ12":"PAJ12_CENTRAL";
    }
    catch(Exception e){
      System.out.println("Ocurrio un error al accesar al metodo formaOLTraspasos4221_4399_4319_a_6100 "+e.getMessage());
      System.out.println("SQL formaOLTraspasos4221_4399_4319_a_6100: "+SQL.toString());
    throw e;
    } //Fin catch
    finally{
      if (rsQuery!=null){
        rsQuery.close();
        rsQuery=null;
        }
        if (stQuery!=null){
        stQuery.close();
        stQuery=null;
        }
    } //Fin finally
    return cadenaTem.toString();
    }
    
    public String formaOMTraspaso4322_a_6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");  
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4322 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=7  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append("UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga, nivel7, tipo,sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("'1' as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'0000' as nivel7, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4322 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=7 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));
                    hm.put ("IMPORTE4322",rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ13":"PAJ13_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));
                    hm.put ("IMPORTE6100",rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ13":"PAJ13_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ13":"PAJ13_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOMTraspaso4322_a_6100 "+e.getMessage());
          System.out.println("SQL  formaOMTraspaso4322_a_6100: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
       return cadenaTem.toString();
    }
    public String formaONTraspasos4321_4326_a_6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            //1
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
           }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4321 ");
            SQL.append("or substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4326 )");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in (1,2) ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in('0001','0002','0003','0004','0007') ");
            SQL.append("and cc.nivel=7 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,tipo ");
            SQL.append(" UNION ALL ");
          // DESTINO2
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append(" '1' as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'0000' as nivel7, ");
            SQL.append("'DESTINO' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE (substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4321 ");
            SQL.append("or substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=4326 )");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in (1,2) ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in('0001','0002','0003','0004','0007') ");
            SQL.append("and cc.nivel=7 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,tipo");
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
               if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));
                    hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ14":"PAJ14_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put("MAYOR", rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put ("NIVEL7",rsQuery.getString("nivel7"));
                    hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ14":"PAJ14_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }
           } //del while
           referenciaGral=ptipoCampo.equals("_pub")?"PAJ14":"PAJ14_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaONTraspasos4321_4326_a_6100 "+e.getMessage());
          System.out.println("SQL formaONTraspasos4321_4326_a_6100: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }          
    
     public String formaOOTraspasos51_55_a_6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
         HashMap hm = new HashMap(); 
         StringBuffer SQL = new StringBuffer("");
         StringBuffer cadenaTem= new StringBuffer("");
         Statement stQuery=null;
         ResultSet rsQuery= null;
         try{
             //1
             SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
             SQL.append("SELECT  ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
             SQL.append("'0000' as nivel7, ");
             SQL.append("'0000' as nivel8, ");
             SQL.append("'ORIGEN' AS tipo,  "); 
             if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
             SQL.append("end) saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc ");
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) ");
             SQL.append("in(5111,5112,5113,5114,5115,5116,5117,5121,5122,5123,5124,5125,5126,5127,5129,5131,5132,5133,5134,5135,5136,5137,5138,5139,5241,5243,5291)  ");
             SQL.append("and cc.nivel=6 ");
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
             SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
             SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
             SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0  ");
             SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo ");
           // ORIGEN2
            SQL.append(" UNION ALL ");
            SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'0000' as nivel8, ");
            SQL.append("'ORIGEN' AS tipo,  "); 
            if(ptipoCampo.equals("_pub")){
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
               SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
               SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5535  ");
            SQL.append("and cc.nivel=7 ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo ");
           //ORIGEN3
             SQL.append(" UNION ALL ");
             SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
             SQL.append("SELECT  ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))||'B' as mayor, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
             SQL.append("'ORIGEN' AS tipo,  "); 
             if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc ");
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
             SQL.append("and cc.nivel=8 ");
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
             SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
             SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
             SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0  ");
             SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
                //ORIGEN3A EXCEPCIONES EJERCICIO 2012
                  SQL.append(" UNION ALL ");
                  SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
                  SQL.append("SELECT  ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))||'B' as mayor, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
                  SQL.append("'ORIGEN' AS tipo,  "); 
                  if(ptipoCampo.equals("_pub")){
                     SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                     SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }else{
                     SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                     SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }
                  SQL.append("end) saldo_actual "); 
                  SQL.append("FROM rf_tr_cuentas_contables cc ");
                  SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                  SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
                  SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) not in(3,5) ");
                  SQL.append(" and ((substr(cc.cuenta_contable,1,5)='55997' and cc.nivel=6) or (substr(cc.cuenta_contable,1,5)<>'55997' and cc.nivel=7)) ");
                  SQL.append(" and substr(cc.cuenta_contable,1,25) not in (select substr(t.cuenta_contable,1,25) from rf_tr_cuentas_contables t where t.cuenta_contable like '5599%' and "); 
                  SQL.append("t.id_catalogo_cuenta=").append(pCatCuenta).append(" and to_char(t.fecha_vig_ini,'yyyy')=").append(pEjercicio).append(" and t.nivel=8) ");
                  SQL.append(" and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                  SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                  if(ptipoCampo.equals("_pub")){
                  SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }else{
                      SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                      SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }
                  SQL.append("end) <>0  ");
                  SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
             // ORIGEN4
              SQL.append(" UNION ALL ");
              SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
              SQL.append("SELECT  ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))||'A' as mayor, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
              SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
              SQL.append("'ORIGEN' AS tipo,  "); 
              if(ptipoCampo.equals("_pub")){
                 SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                 SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
              }else{
                 SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                 SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
              }
              SQL.append("end) saldo_actual "); 
              SQL.append("FROM rf_tr_cuentas_contables cc ");
              SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
              SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
              SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in (3,5) ");
              SQL.append("and cc.nivel=7 ");
              SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
              SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
              if(ptipoCampo.equals("_pub")){
              SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
              SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
              }else{
                  SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
              }
              SQL.append("end) <>0  ");
              SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
              //ORIGEN 5
               SQL.append(" UNION ALL ");
               SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
               SQL.append("SELECT  ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))||'B' as mayor, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");  
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(8,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel8, ");
               SQL.append("'ORIGEN' AS tipo,  "); 
               if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) saldo_actual "); 
               SQL.append("FROM rf_tr_cuentas_contables cc ");
               SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
               SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
               SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=2 ");
               SQL.append("and cc.nivel=5 ");
               SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
               SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
               if(ptipoCampo.equals("_pub")){
               SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
               SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                   SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                   SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) <>0  ");
               SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
              ////////////////////////////QUERYSDESTINO///////////////
               // DESTINO 1
               SQL.append(" UNION ALL ");
               SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
               SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
               SQL.append("SELECT  ");
               SQL.append("'6100' as mayor, ");
               SQL.append("'2' as submayor, ");  
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
               SQL.append("'0001' as tiga, ");
               SQL.append("'0000' as nivel7, ");
               SQL.append("'0000' as nivel8, ");
               SQL.append("'DESTINO' AS tipo,  "); 
               if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) saldo_actual "); 
               SQL.append("FROM rf_tr_cuentas_contables cc ");
               SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
               SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in(5111,5112,5113,5114,5115,5116,5117) ");
               SQL.append("and cc.nivel=6 ");
               SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
               SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
               if(ptipoCampo.equals("_pub")){
               SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
               SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                   SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                   SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) <>0  ");
               SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo ");
               SQL.append(" UNION ALL ");
              SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
             SQL.append("SELECT  ");
             SQL.append("'6100' as mayor, ");
             SQL.append("'2' as submayor, ");  
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
             SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
             SQL.append("'0002' as tiga, ");
             SQL.append("'0000' as nivel7, ");
             SQL.append("'0000' as nivel8, ");
             SQL.append("'DESTINO' AS tipo,  "); 
             if(ptipoCampo.equals("_pub")){
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) saldo_actual "); 
             SQL.append("FROM rf_tr_cuentas_contables cc ");
             SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
             SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) ");
             SQL.append("in (5121,5122,5123,5124,5125,5126,5127,5129,5131,5132,5133,5134,5135,5136,5137,5138,5139,5241,5243,5291) ");
             SQL.append("and cc.nivel=6 ");
             SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
             SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
             if(ptipoCampo.equals("_pub")){
             SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
             SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }else{
                 SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                 SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
             }
             SQL.append("end) <>0  ");
             SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo ");
               // DESTINO 2
                SQL.append(" UNION ALL ");
                SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
                SQL.append("SELECT  ");
                SQL.append("'6100' as mayor, ");
                SQL.append("'2' as submayor, ");  
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                SQL.append("'0002' as tiga, ");
                SQL.append("'0000' as nivel7, ");
                SQL.append("'0000' as nivel8, ");
                SQL.append("'DESTINO' AS tipo,  "); 
                if(ptipoCampo.equals("_pub")){
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) saldo_actual "); 
                SQL.append("FROM rf_tr_cuentas_contables cc ");
                SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5535  ");
                SQL.append("and cc.nivel=7 ");
                SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                    SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                    SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) <>0  ");
                SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
               //DESTINO 3
               SQL.append(" UNION ALL ");
               SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
               
               SQL.append("SELECT  ");
               SQL.append("'6100' as mayor, ");
               SQL.append("'2' as submayor, ");  
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
               SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
               SQL.append("'0002' as tiga, ");
               SQL.append("'0000' as nivel7, ");
               SQL.append("'0000' as nivel8, ");
               SQL.append("'DESTINO' AS tipo,  "); 
               if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) saldo_actual "); 
               SQL.append("FROM rf_tr_cuentas_contables cc ");
               SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
               SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
               SQL.append("and cc.nivel=8 ");
               SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
               SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
               if(ptipoCampo.equals("_pub")){
               SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
               SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }else{
                   SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                   SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
               }
               SQL.append("end) <>0  ");
               //   DESTINO QUERY3A excepcion
               SQL.append(" UNION ALL ");
               SQL.append("SELECT  ");
                SQL.append("'6100' as mayor, ");
                SQL.append("'2' as submayor, ");  
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                SQL.append("'0002' as tiga, ");
                SQL.append("'0000' as nivel7, ");
                SQL.append("'0000' as nivel8, ");
                SQL.append("'DESTINO' AS tipo,  "); 
                if(ptipoCampo.equals("_pub")){
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) saldo_actual "); 
                SQL.append("FROM rf_tr_cuentas_contables cc ");
                SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
                SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) not in(3,5) ");
                SQL.append(" and ((substr(cc.cuenta_contable,1,5)='55997' and cc.nivel=6) or (substr(cc.cuenta_contable,1,5)<>'55997' and cc.nivel=7)) ");
                SQL.append(" and substr(cc.cuenta_contable,1,25) not in (select substr(t.cuenta_contable,1,25) from rf_tr_cuentas_contables t where t.cuenta_contable like '5599%' and "); 
                SQL.append("t.id_catalogo_cuenta=").append(pCatCuenta).append(" and to_char(t.fecha_vig_ini,'yyyy')=").append(pEjercicio).append(" and t.nivel=8) ");
                SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                    SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                    SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) <>0  ");
               
               SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
               // DESTINO 4
                SQL.append(" UNION ALL ");
                SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
                SQL.append("SELECT  ");
                SQL.append("'6100' as mayor, ");
                SQL.append("'2' as submayor, ");  
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                SQL.append("case when( substr(cc.cuenta_contable,5,1)='3') then  '0002' else substr(cc.cuenta_contable,22,4) end tiga, ");
                SQL.append("'0000' as nivel7, ");
                SQL.append("'0000' as nivel8, ");
                SQL.append("'DESTINO' AS tipo,  "); 
                if(ptipoCampo.equals("_pub")){
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                   SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                   SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) saldo_actual "); 
                SQL.append("FROM rf_tr_cuentas_contables cc ");
                SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
                SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in (3,5) ");
                SQL.append("and cc.nivel=7 ");
                SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }else{
                    SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                    SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                }
                SQL.append("end) <>0  ");
                SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo ");
                //DESTINO 5
                  SQL.append(" UNION ALL ");
                  SQL.append("SELECT mayor, submayor,programa, unidad, ambito, tiga,nivel7,nivel8,tipo,sum(saldo_actual) as saldo_actual FROM(");
                  SQL.append("SELECT  ");
                  SQL.append("'6100' as mayor, ");
                  SQL.append("'2' as submayor, ");  
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
                  SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
                  SQL.append("'0002' as tiga, ");
                  SQL.append("'0000' as nivel7, ");
                  SQL.append("'0000' as nivel8, ");
                  SQL.append("'DESTINO' AS tipo,  "); 
                  if(ptipoCampo.equals("_pub")){
                     SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                     SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }else{
                     SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                     SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }
                  SQL.append("end) saldo_actual "); 
                  SQL.append("FROM rf_tr_cuentas_contables cc ");
                  SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
                  SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=5599 ");
                  SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=2 ");
                  SQL.append("and cc.nivel=5 ");
                  SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
                  SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
                  if(ptipoCampo.equals("_pub")){
                  SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }else{
                      SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                      SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
                  }
                  SQL.append("end) <>0  ");
                  SQL.append(") sub1 GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
                  SQL.append(") destino GROUP BY mayor, submayor,programa, unidad, ambito,tiga,nivel7,nivel8,tipo");
             stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
             rsQuery = stQuery.executeQuery(SQL.toString());         
             while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                     hm.put("MAYOR", rsQuery.getString("mayor").substring(0,4));
                     hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                     hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                     hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                     hm.put ("AMBITO",rsQuery.getString("ambito"));
                     hm.put ("TIGA",rsQuery.getString("tiga"));
                     hm.put("NIVEL7", rsQuery.getString("nivel7") );
                     hm.put("NIVEL8", rsQuery.getString("nivel8") );
                     hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ15":"PAJ15_CENTRAL");//impri
                     cadenaTem.append(Cadena.construyeCadena(hm));
                     cadenaTem.append("~");
                     hm.clear();
                 }else{
                     hm.put("MAYOR", rsQuery.getString("mayor").substring(0,4));
                     hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                     hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                     hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                     hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                     hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm); 
                     hm.put("NIVEL7", rsQuery.getString("nivel7") );
                     hm.put("NIVEL8", rsQuery.getString("nivel8") );
                     hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));//imprimeHM(hm);                         
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ15":"PAJ15_CENTRAL");//impri
                     cadenaTem.append(Cadena.construyeCadena(hm));
                     cadenaTem.append("~");
                     hm.clear();
                 }
            } //del while
            referenciaGral=ptipoCampo.equals("_pub")?"PAJ15":"PAJ15_CENTRAL";
            }
         catch(Exception e){
           System.out.println("Ocurrio un error al accesar al metodo formaOOTraspasos51_55_a_6100 "+e.getMessage());
           System.out.println("SQL formaOOTraspasos51_55_a_6100: "+SQL.toString());
           throw e;
         } //Fin catch
         finally{
           if (rsQuery!=null){
             rsQuery.close();
             rsQuery=null;
           }
           if (stQuery!=null){
             stQuery.close();
             stQuery=null;
           }
         } //Fin finally       
         return cadenaTem.toString();
     }       
     
    public String formaOPTraspaso5532_5531_5536_5592_a_6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as mayor, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");  
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in(5532,5531,5536,5592) ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=7  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT mayor,submayor,programa, unidad, ambito, tiga,nivel7,tipo,sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("'6100' as mayor, ");
            SQL.append("'1' as submayor, ");    
            SQL.append("'0005' as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'0000'as nivel7, ");  
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in(5532,5531,5536,5592) ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=7 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  mayor,submayor,programa, unidad, ambito,tiga,nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put("MAYOR",rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE".concat(rsQuery.getString("mayor")),rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ16":"PAJ16_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put("MAYOR",rsQuery.getString("mayor"));
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));
                    hm.put ("TIGA",rsQuery.getString("tiga"));
                    hm.put("NIVEL7", rsQuery.getString("nivel7"));
                    hm.put ("IMPORTE",rsQuery.getString("saldo_actual"));
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ16":"PAJ16_CENTRAL");
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ16":"PAJ16_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOPTraspaso5532_5531_5536_5592_a_6100 "+e.getMessage());
          System.out.println("SQL  formaOPTraspaso5532_5531_5536_5592_a_6100: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }    
     
     
    public String formaOQTraspaso6100_a_6200(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=2 ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100  ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=2 ");
            SQL.append("and cc.nivel=6 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE6100",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ17":"PAJ17_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                     hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE6200",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ17":"PAJ17_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ17":"PAJ17_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOQTraspaso6100_a_6200 "+e.getMessage());
          System.out.println("SQL  formaOQTraspaso6100_a_6200: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }    
    
    public String formaORTraspaso6100_a_6200(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga, nivel7, tipo, sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga, nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm); 
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6100",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ18":"PAJ18_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6200",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ18":"PAJ18_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ18":"PAJ18_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaORTraspaso6100_a_6200 "+e.getMessage());
          System.out.println("SQL  formaORTraspaso6100_a_6200: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    } 
    
    public String formaOSTraspaso6100_a_6300(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end)*-1 saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0009') ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end)*-1 saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100  ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0009') ");
            SQL.append("and cc.nivel=6 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE6100",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ19":"PAJ19_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                     hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put ("IMPORTE6300",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ19":"PAJ19_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ19":"PAJ19_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOSTraspaso6100_a_6300 "+e.getMessage());
          System.out.println("SQL  formaOSTraspaso6100_a_6300: "+SQL.toString());
          throw e;
        } //Fin catch
        
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }    
    
    public String formaOTTraspaso6100_a_6300(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end)*-1 saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga, nivel7, tipo, sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end)*-1 saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga, nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6100",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ20":"PAJ20_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6300",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ20":"PAJ20_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ20":"PAJ20_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOTTraspaso6100_a_6300 "+e.getMessage());
          System.out.println("SQL  formaOTTraspaso6100_a_6300: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    } 

  
    public String formaOUTraspaso6200_a_3210(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");   
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6200  ");
            SQL.append("and   substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0005'  ");               
            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append(" UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1000') )then '0001' ");
            SQL.append("else( ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0002','2000','5000')) then '0002' ");
            SQL.append("else '0005' end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' as tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc  ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6200 ");
            SQL.append("and   substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))<>'0005' ");               
            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo "); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("IMPORTE6200",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ21":"PAJ21_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("IMPORTE3210",rsQuery.getString("saldo_actual"));//imprimeHM(hm)
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ21":"PAJ21_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                } 
                
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ21":"PAJ21_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOUTraspaso6200_a_3210 "+e.getMessage());
          System.out.println("SQL formaOUTraspaso6200_a_3210: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
  
    public String formaOVTraspaso6200_a_3210(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6200 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga, nivel7, tipo, sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6200 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga, nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6200",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ22":"PAJ22_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") ); 
                    hm.put ("IMPORTE3210",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ22":"PAJ22_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ22":"PAJ22_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOTVraspaso6200_a_3210 "+e.getMessage());
          System.out.println("SQL  formaOTVraspaso6200_a_3210: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
  
    
    public String formaOWTraspaso6300_a_3210(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");   
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("'ORIGEN' AS tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6300  ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0009') ");
            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append("UNION ALL "); 
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga,tipo,sum(saldo_actual) as saldo_actual "); 
            SQL.append("FROM( "); 
            SQL.append("SELECT  ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");     
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0001','1000') )then '0001' ");
            SQL.append("else( ");
            SQL.append("case when (substr(cc.cuenta_contable,18,4) in('0002','2000','5000')) then '0002' ");
            SQL.append("else '0005' end ");
            SQL.append(") end tiga, ");
            SQL.append("'DESTINO' as tipo, "); 
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual ");
            SQL.append("FROM rf_tr_cuentas_contables cc  ");
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id  ");
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6300  ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) in ('0001','0009') ");
            SQL.append("and cc.nivel=6  ");
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 ");
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga,tipo "); 
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("IMPORTE6300",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ23":"PAJ23_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor")); 
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);  
                    hm.put ("IMPORTE3210",rsQuery.getString("saldo_actual"));//imprimeHM(hm)
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ23":"PAJ23_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                } 
                
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ23":"PAJ23_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo formaOWTraspaso6300_a_3210 "+e.getMessage());
          System.out.println("SQL formaOWTraspaso6300_a_3210: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }
        
    public String formaOXTraspaso6300_a_3210(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        HashMap hm = new HashMap(); 
        StringBuffer SQL = new StringBuffer("");
        StringBuffer cadenaTem= new StringBuffer("");
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito,  "); 
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as TIGA, ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'ORIGEN' AS tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6300 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
            SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
            SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0  ");
            SQL.append(" UNION ALL ");
            SQL.append("SELECT submayor,programa, unidad, ambito, tiga, nivel7, tipo, sum(saldo_actual) as saldo_actual ");
            SQL.append("FROM( "); 
            SQL.append("SELECT ");  
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as submayor, ");    
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as programa, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('unidad',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as unidad, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('ambito',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as ambito, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(6,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as tiga, ");
            SQL.append("substr(cc.cuenta_contable,F_POSICION_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')), F_LONGITUD_NIVEL(7,cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy'))) as nivel7, ");
            SQL.append("'DESTINO' as tipo, ");
            if(ptipoCampo.equals("_pub")){
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                  SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
                  SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6300 ");  
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('submayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=1 ");
            SQL.append("and substr(cc.cuenta_contable,F_POSICION_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('programa',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))='0005' ");
            SQL.append("and cc.nivel=6 ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append("and cc.id_catalogo_cuenta=").append(pCatCuenta);
            if(ptipoCampo.equals("_pub")){
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
                SQL.append(" and (case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" )) ");  
                SQL.append(" else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) <>0 "); 
            SQL.append(") sub "); 
            SQL.append("GROUP BY  submayor,programa, unidad, ambito,tiga, nivel7,tipo ");  
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
                if (rsQuery.getString("tipo").equals("ORIGEN")){
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));            
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE6300",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                     hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ24":"PAJ24_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();
                }else{
                    hm.put ("SUBMAYOR",rsQuery.getString("submayor"));                           
                    hm.put ("PROGRAMA",rsQuery.getString("programa"));            
                    hm.put ("UNIDAD",rsQuery.getString("unidad"));                
                    hm.put ("AMBITO",rsQuery.getString("ambito"));//imprimeHM(hm);
                    hm.put ("TIGA",rsQuery.getString("tiga"));//imprimeHM(hm);                
                    hm.put("NIVEL7", rsQuery.getString("nivel7") );
                    hm.put ("IMPORTE3210",rsQuery.getString("saldo_actual"));//imprimeHM(hm);
                    hm.put ("REFERENCIA",ptipoCampo.equals("_pub")?"PAJ24":"PAJ24_CENTRAL");//impri
                    cadenaTem.append(Cadena.construyeCadena(hm));
                    cadenaTem.append("~");
                    hm.clear();                    
                }
            } //del while
             referenciaGral=ptipoCampo.equals("_pub")?"PAJ24":"PAJ24_CENTRAL";
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  formaOXTraspaso6300_a_3210 "+e.getMessage());
          System.out.println("SQL  formaOXTraspaso6300_a_3210: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return cadenaTem.toString();
    }       
    
    
    public Boolean VerificaSaldo6100(Connection con, String pCatCuenta, String pEjercicio, String ptipoCampo) throws SQLException, Exception {
        StringBuffer SQL = new StringBuffer("");
        Boolean Positivo= false;
        Statement stQuery=null;
        ResultSet rsQuery= null;
        try{
            SQL.append("SELECT "); 
            if(ptipoCampo.equals("_pub")){
              SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(")) "); 
              SQL.append("else ((cc.nov_abono_acum + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }else{
              SQL.append("(case cla.naturaleza when 'D' then ((cc.nov_cargo_acum_eli + dic_cargo").append(ptipoCampo).append(") - (cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(")) "); 
              SQL.append("else ((cc.nov_abono_acum_eli + cc.dic_abono").append(ptipoCampo).append(" ) - (cc.nov_cargo_acum_eli + cc.dic_cargo").append(ptipoCampo).append(" )) ");  
            }
            SQL.append("end) saldo_actual "); 
            SQL.append("FROM rf_tr_cuentas_contables cc "); 
            SQL.append("INNER JOIN rf_tc_clasificador_cuentas cla on cc.cuenta_mayor_id = cla.cuenta_mayor_id ");  
            SQL.append("WHERE substr(cc.cuenta_contable,F_POSICION_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')),F_LONGITUD_NIVEL('mayor',cc.cuenta_mayor_id,to_char(cc.fecha_vig_ini,'yyyy')))=6100 ");  
            SQL.append("and cc.nivel=1  ");  
            SQL.append("and to_char(cc.fecha_vig_ini,'yyyy')=").append(pEjercicio);
            SQL.append(" and cc.id_catalogo_cuenta=").append(pCatCuenta);
            stQuery = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            rsQuery = stQuery.executeQuery(SQL.toString());         
            while (rsQuery.next()){
            System.out.println("Saldo de la cuenta 6100******: "+rsQuery.getDouble("saldo_actual"));
              if(rsQuery.getDouble("saldo_actual")>0)
                Positivo=true;
            }
        }
        catch(Exception e){
          System.out.println("Ocurrio un error al accesar al metodo  VerificaSaldo6100 "+e.getMessage());
          System.out.println("SQL  VerificaSaldo6100: "+SQL.toString());
          throw e;
        } //Fin catch
        finally{
          if (rsQuery!=null){
            rsQuery.close();
            rsQuery=null;
          }
          if (stQuery!=null){
            stQuery.close();
            stQuery=null;
          }
        } //Fin finally       
        return Positivo;
    }
    
}//fin clase


