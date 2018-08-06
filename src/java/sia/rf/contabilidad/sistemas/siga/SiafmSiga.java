package sia.rf.contabilidad.sistemas.siga;

import java.sql.Connection;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sun.jdbc.rowset.CachedRowSet;

public class SiafmSiga {
  public SiafmSiga() {
  }
  
  private String unidad;
  private String ambito;
  private String entidad;
  
  public void setUnidad(String unidad) {
  this.unidad = unidad;
  }
  
  public String getUnidad() {
  return unidad;
  }
  
  public void setAmbito(String ambito) {
  this.ambito = ambito;
  }
  
  public String getAmbito() {
  return ambito;
  }

  public void setEntidad(String entidad) {
  this.entidad = entidad;
  }

  public String getEntidad() {
  return entidad;
  }  
  
  
public HashMap Forma1 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 1
HashMap hm = new HashMap();
return hm;
  }  
  
public HashMap Forma2 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 2
HashMap hm = new HashMap();
return hm;
}


public HashMap Forma3 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 3
HashMap hm = new HashMap();
return hm;
}

public HashMap Forma4 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 4
HashMap hm = new HashMap();
return hm;
}   

public HashMap Forma5 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 5
HashMap hm = new HashMap();
return hm;
}  

public HashMap Forma6 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 6
HashMap hm = new HashMap();
return hm;
}   

public HashMap Forma7 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 7
HashMap hm = new HashMap();
return hm;
} 

public HashMap Forma8 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 8 Devolucin de bienes
HashMap hm = new HashMap();
return hm;
}

public HashMap Forma9 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 9
HashMap hm = new HashMap();
return hm;
}       
  
public HashMap Forma10 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 10 Venta de desperdicios... y anter
HashMap hm = new HashMap();
return hm;
}        

public HashMap Forma11 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 11
HashMap hm = new HashMap();
return hm;
} 

public HashMap Forma12 (Connection con, String fecha,String unidad, String claveArea, String folio) throws SQLException, Exception {
//Movimiento 12
HashMap hm = new HashMap();
return hm;
}  

public String general(Connection con, String fechaIni,String fechaFin,String movimiento, String unidad, String entidad, String ambito, String folio_siga) throws SQLException, Exception {
    //Con folio SIGA y formas de cancelaciones 2013
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer SQL= new StringBuffer("");
    String partida ="";
    StringBuffer cadenaTem= new StringBuffer("");
    SQL.append(" select ");
    SQL.append(" lpad(substr(t.unidad_admin, 0, 3), 4, 0) unidad_ejecutora, ");
    SQL.append("        t.entidad, ");
    SQL.append("        t.ambito, ");
    SQL.append("        t.fecha_inicial, ");
    SQL.append("        t.folio_siga, ");
    SQL.append("        t.tipo_mov_contable_id, ");
    SQL.append("        '1' capitulo, ");
    SQL.append("        rpad(substr(t.partida, 0, 2), 4, 0)partida2Digitos, ");
    SQL.append("        rpad(substr(t.partida, 0, 3), 4, 0)partida3digitos, ");
    SQL.append("        rpad(decode(substr(t.partida, 0, 2),'59','34',substr(partida, 0, 3)), 4, 0) partida3, ");
    SQL.append("        t.partida partidaOriginal, ");
    SQL.append("           substr(t.partida, 2, 1) partida, ");
    SQL.append("           substr(t.partida, 3, 1) partida_generica, ");
    SQL.append("       SUBSTR(t.unidad_admin, 0, 3) || t.entidad || t.ambito || t.partida referencia, ");
    SQL.append("           case ");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.importe_sin_iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.importe_sin_iva*-1");
    SQL.append("   end importe_sin_iva,");
       
    SQL.append("   case");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.iva*-1");
    SQL.append("   end iva,           ");
    SQL.append("   case");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.importe_con_iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.importe_con_iva*-1");
    SQL.append("   end importe_con_iva,");
    SQL.append("   importe_con_iva importe_canc_con_iva,");
    SQL.append("        t.estatus_mov_contable,       t.unidad_admin, ");
    SQL.append("        lpad(t.entidad || t.ambito, 4, 0) ent_amb ");
      //SQL.append(" from sia_almacen.zrm_tr_movs_con_det2@sia_tra  t");
    //SQL.append(" from sia_almacen.aaprueba_zrm_tr_movs_con_det@sia_tra  t"); 06-06-2013
    SQL.append(" from sia_almacen.zrm_tr_movs_con_det  t");
    SQL.append(" WHERE ");
    SQL.append(" t.unidad_admin='"+unidad+"' and t.entidad="+entidad+" and t.ambito=" +ambito);
    SQL.append(" and to_date(to_char(t.fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')>=to_date('"+fechaIni+"','dd/mm/yyyy') AND to_date(to_char(t.fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')<=to_date('"+fechaFin+"','dd/mm/yyyy')");
    SQL.append(" and t.folio_siga=" + folio_siga);
    SQL.append(" and t.tipo_mov_contable_id=" + movimiento);          
      SQL.append(" and t.partida not in (9900,0000) ");
      SQL.append(" and rpad(substr(t.partida, 0, 1), 4, 0)='2000' ");
      
   //   System.out.println("SiafmSiga.general: "+SQL.toString());
      
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(con); 
        crs.beforeFirst();
        int partida1=0;
        while(crs.next()){
         partida=crs.getString("PARTIDA");
         partida1=Integer.valueOf(partida);
         partida1=((partida1<3)?partida1:(partida1==3)?9:(partida1>3 && partida1<=9)?partida1-1:'0');
         //antes bien
         //partida1=((partida1>2 && partida1<=9)?partida1-1:partida1);//
         
         partida=Integer.toString(partida1);
         //((crs.getInt("PARTIDA")>="3" and crs.getInt("PARTIDA")<=4)?crs.getInt("PARTIDA")-1:crs.getString("PARTIDA"));
          hm.put ("CAPITULO",crs.getString("CAPITULO"));//1
          hm.put ("PARTIDA",partida);//1
          hm.put ("PARTIDA_GENERICA",crs.getString("PARTIDA_GENERICA"));//6
          hm.put ("UNIDAD",crs.getString("UNIDAD_EJECUTORA"));//102
          hm.put ("AMBITO",crs.getString("ENT_AMB"));//11
          
          hm.put ("IMPORTE_ANTES_IVA",crs.getString("IMPORTE_SIN_IVA"));
          if(movimiento.equals("1")||movimiento.equals("13")){
              hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_IVA",crs.getString("IVA"));
              if(movimiento.equals("1")){//entrada
                 hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_DESP_IVA",crs.getString("IMPORTE_CON_IVA"));
              }
              else{//cancelacion de entrada
                 hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_CANC_DESP_IVA",crs.getString("IMPORTE_CANC_CON_IVA"));
                 }
          }
          else{
            hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_ANTES_IVA",crs.getString("IMPORTE_SIN_IVA"));
            if(movimiento.equals("6")||movimiento.equals("10")||movimiento.equals("11")||
               movimiento.equals("18")||movimiento.equals("22")||movimiento.equals("23")){
                    hm.put ("CONC_PARTIDA","2"+crs.getString("PARTIDA")+"00");
                }
            else{}
          }
          
          hm.put ("REFERENCIA",crs.getString("REFERENCIA"));                
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el m�todo general");
      System.out.println ("SiafmSiga.general: " + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
    }
return cadenaTem.toString();
}  
    
public String generalAnt(Connection con, String fechaIni,String fechaFin,String movimiento, String unidad, String entidad, String ambito) throws SQLException, Exception {
    //2012 Sin Folio SIGA
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer SQL= new StringBuffer("");
    String partida ="";
    StringBuffer cadenaTem= new StringBuffer("");
      SQL.append(" select entidad, ambito, lpad(entidad || ambito,4,0) ent_amb, ");
      SQL.append(" SUBSTR(unidad_admin,   0,   3) || entidad || ambito || partida referencia, ");
      SQL.append(" lpad(substr(unidad_admin,0,3),4,0) unidad_ejecutora, ");
      SQL.append(" tipo_mov_contable_id, ");
      SQL.append(" substr(partida, 2, 1) partida, " );
      SQL.append(" substr(partida, 3, 1) partida_generica, ");
      SQL.append(" importe_sin_iva, iva, importe_con_iva, ");
      SQL.append(" decode(substr(partida, 0, 2),'59','34',substr(partida, 0, 2)) partida,");
      SQL.append(" fecha_inicial, estatus_mov_contable ");
   
   //   SQL.append(" from sia_almacen.zrm_tr_movs_con_det@sia_tra ");--06-06-2013
      SQL.append(" from sia_almacen.zrm_tr_movs_con_det ");
      SQL.append(" WHERE tipo_mov_contable_id=" + movimiento);
    SQL.append(" and to_date(to_char(fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')>=to_date('"+fechaIni+"','dd/mm/yyyy') AND to_date(to_char(fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')<=to_date('"+fechaFin+"','dd/mm/yyyy')");
      
      SQL.append(" and unidad_admin='"+unidad+"' and entidad="+entidad+" and ambito=" +ambito);
      SQL.append(" and partida not in (9900,0000) ");
      
      System.out.println("SiafmSiga.generalAnt: "+SQL.toString());
      
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(con); 
        crs.beforeFirst();
        int partida1=0;
        while(crs.next()){
         partida=crs.getString("PARTIDA");
         partida1=Integer.valueOf(partida);
         partida1=((partida1<3)?partida1:(partida1==3)?9:(partida1>3 && partida1<=9)?partida1-1:'0');
         //antes bien
         //partida1=((partida1>2 && partida1<=9)?partida1-1:partida1);//
         
         partida=Integer.toString(partida1);
         //((crs.getInt("PARTIDA")>="3" and crs.getInt("PARTIDA")<=4)?crs.getInt("PARTIDA")-1:crs.getString("PARTIDA"));
          hm.put ("PARTIDA",partida);//1
          hm.put ("PARTIDA_GENERICA",crs.getString("PARTIDA_GENERICA"));//6
          hm.put ("UNIDAD",crs.getString("UNIDAD_EJECUTORA"));//102
          hm.put ("AMBITO",crs.getString("ENT_AMB"));//11
          
          hm.put ("IMPORTE_ANTES_IVA",crs.getString("IMPORTE_SIN_IVA"));
          if(movimiento.equals("1")){
            hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_DESP_IVA",crs.getString("IMPORTE_CON_IVA"));
            hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_IVA",crs.getString("IVA"));
          }
          else{
            hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_ANTES_IVA",crs.getString("IMPORTE_SIN_IVA"));
            if(movimiento.equals("6")||movimiento.equals("10")||movimiento.equals("11")){
                    hm.put ("CONC_PARTIDA","2"+crs.getString("PARTIDA")+"00");
                }
            else{}
          }
          hm.put ("REFERENCIA",crs.getString("REFERENCIA"));                

          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el método general");
      System.out.println ("SiafmSiga.general: " + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
    }
    return cadenaTem.toString();
}

public String general2013(Connection con, String fechaIni,String fechaFin,String movimiento, String unidad, String entidad, String ambito, String folio_siga) throws SQLException, Exception {
    //Con folio SIGA y formas de cancelaciones 2013  
    //Para los movimientos de cancelacion (mov>12), los importes se multipicaran por -1
    //Para el ejercicio 2013, todas las entradas y salidas se registraran con iva incluido
    HashMap hm = new HashMap();
    CachedRowSet crs = null;
    StringBuffer SQL= new StringBuffer("");
    String partida ="";
    StringBuffer cadenaTem= new StringBuffer("");
    SQL.append(" select ");
    SQL.append(" lpad(substr(t.unidad_admin, 0, 3), 4, 0) unidad_ejecutora, ");
    SQL.append("        t.entidad, ");
    SQL.append("        t.ambito, ");
    SQL.append("        t.fecha_inicial, ");
    SQL.append("        t.folio_siga, ");
    SQL.append("        t.tipo_mov_contable_id, ");
    SQL.append("        '1' capitulo, ");
    SQL.append("        rpad(substr(t.partida, 0, 2), 4, 0)partida2Digitos, ");
    SQL.append("        rpad(substr(t.partida, 0, 3), 4, 0)partida3digitos, ");
    SQL.append("        rpad(decode(substr(t.partida, 0, 2),'59','34',substr(partida, 0, 3)), 4, 0) partida3, ");
    SQL.append("        t.partida partidaOriginal, ");
    SQL.append("           substr(t.partida, 2, 1) partida, ");
    SQL.append("           substr(t.partida, 3, 1) partida_generica, ");
    SQL.append("       SUBSTR(t.unidad_admin, 0, 3) || t.entidad || t.ambito || t.partida referencia, ");
    SQL.append("   case ");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.importe_sin_iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.importe_sin_iva*-1");
    SQL.append("   end importe_sin_iva,");
       
    SQL.append("   case");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.iva*-1");
    SQL.append("   end iva,           ");
    
    SQL.append("   case");
    SQL.append("     when t.tipo_mov_contable_id>0 and t.tipo_mov_contable_id<13 then  t.importe_con_iva");
    SQL.append("     when t.tipo_mov_contable_id>12                              then  t.importe_con_iva*-1");
    SQL.append("   end importe_con_iva,");
    
    SQL.append("   importe_con_iva importe_canc_con_iva,");
    SQL.append("        t.estatus_mov_contable,       t.unidad_admin, ");
    SQL.append("        lpad(t.entidad || t.ambito, 4, 0) ent_amb ");
      //SQL.append(" from sia_almacen.zrm_tr_movs_con_det2@sia_tra  t");
    //SQL.append(" from sia_almacen.aaprueba_zrm_tr_movs_con_det@sia_tra  t");--06-06-2013
    SQL.append(" from sia_almacen.zrm_tr_movs_con_det  t");
    SQL.append(" WHERE ");
    SQL.append(" t.unidad_admin='"+unidad+"' and t.entidad="+entidad+" and t.ambito=" +ambito);
    SQL.append(" and to_date(to_char(t.fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')>=to_date('"+fechaIni+"','dd/mm/yyyy') AND to_date(to_char(t.fecha_inicial,'dd/mm/yyyy'),'dd/mm/yyyy')<=to_date('"+fechaFin+"','dd/mm/yyyy')");
    SQL.append(" and t.folio_siga=" + folio_siga);
    SQL.append(" and t.tipo_mov_contable_id=" + movimiento);          
    SQL.append(" and t.partida not in (9900,0000)  ");
    SQL.append(" and rpad(substr(t.partida, 0, 1), 4, 0)='2000' ");
      
 //   System.out.println("SiafmSiga.general2013: "+SQL.toString());
      
      try {
        crs = new CachedRowSet();
        crs.setCommand(SQL.toString());
        crs.execute(con); 
        crs.beforeFirst();
        int partida1=0;
        while(crs.next()){
         if (crs.getString("IMPORTE_CON_IVA")!=null && Double.valueOf(crs.getString("IMPORTE_CON_IVA"))!=0){
            partida=crs.getString("PARTIDA");
            partida1=Integer.valueOf(partida);
            partida1=((partida1<3)?partida1:(partida1==3)?9:(partida1>3 && partida1<=9)?partida1-1:'0');
      
            partida=Integer.toString(partida1);
            hm.put ("CAPITULO",crs.getString("CAPITULO"));//1
            hm.put ("PARTIDA",partida);//1
            hm.put ("PARTIDA_GENERICA",crs.getString("PARTIDA_GENERICA"));//6
            hm.put ("UNIDAD",crs.getString("UNIDAD_EJECUTORA"));//102
            hm.put ("AMBITO",crs.getString("ENT_AMB"));//11
         
            hm.put ("IMPORTE_DESP_IVA",crs.getString("IMPORTE_CON_IVA"));         
         
            if(movimiento.equals("2")||movimiento.equals("14")||movimiento.equals("8")||movimiento.equals("20")){
              hm.put ("IMPORTE2"+crs.getString("PARTIDA")+"_DESP_IVA",crs.getString("IMPORTE_CON_IVA"));
            }//Fin MOVIMIENTOS PORMENORIZADOS 

            if(movimiento.equals("6")||movimiento.equals("10")||movimiento.equals("11")||
               movimiento.equals("18")||movimiento.equals("22")||movimiento.equals("23")){
                    hm.put ("CONC_PARTIDA","2"+crs.getString("PARTIDA")+"00");
             } //FIN MOVIMIENTOS APLICAN CONCEPTO PARTIDA
           
             hm.put ("REFERENCIA",crs.getString("REFERENCIA"));                
             cadenaTem.append(Cadena.construyeCadena(hm));
             cadenaTem.append("~");
             hm.clear();
         }     
        }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo general");
      System.out.println ("SiafmSiga.general: " + SQL.toString());
      crs.close();
      crs=null;
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      crs=null;
    }
return cadenaTem.toString();
}  

public CachedRowSet getUnidadEntidadAmbito(Connection con,String fechaIni, String fechaFin) throws SQLException, Exception {
  List<Vista> registros = null;
  Sentencias sentencia = null;
  CachedRowSet crs = null;
  StringBuffer SQL= new StringBuffer("");
  SQL.append("select distinct SUBSTR(unidad_admin,   0,   3) unidad_ejecutora, unidad_admin unidad, entidad, ambito ");
  
  SQL.append("from sia_almacen.zrm_tr_movs_con_det_arm ");
  //SQL.append("WHERE to_char(fecha_inicial,'dd/mm/yyyy') BETWEEN '"+fechaIni+"' AND '"+fechaFin+"' and SUBSTR(unidad_admin, 0, 3)<>'122'");
  SQL.append("WHERE to_date(to_char(fecha_inicial, 'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaIni+"','dd/mm/yyyy') AND to_date(to_char(fecha_inicial, 'dd/mm/yyyy'),'dd/mm/yyyy')<= to_date('"+fechaFin+"','dd/mm/yyyy')");
  System.out.println("SiafmSiga.getUnidadEntidadAmbito: "+ SQL.toString());
  
  try{
    crs = new CachedRowSet();
    crs.setCommand(SQL.toString());
    crs.execute(con); 
  }
  catch (Exception e){
    System.out.println ("Ha ocurrido un error en el metodo getUnidadEntidadAmbito");
    System.out.println ("SiafmTesoreria.getUnidadEntidadAmbito: ");
    throw e;
  } 
  finally {
    SQL.setLength(0);
    SQL=null;
    sentencia = null;
  }
  return crs;
}

}





