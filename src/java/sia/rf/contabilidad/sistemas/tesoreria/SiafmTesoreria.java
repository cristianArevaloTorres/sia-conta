package sia.rf.contabilidad.sistemas.tesoreria;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

import sia.db.dao.DaoFactory;
import sia.db.sql.Sentencias;
import sia.db.sql.Vista;

import sia.rf.contabilidad.registroContableEvento.Cadena;

import sun.jdbc.rowset.CachedRowSet;

public class SiafmTesoreria {
  
  public SiafmTesoreria() {
  } 
    
  public String registrosContablesHSBC(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
    HashMap hm = new HashMap();
    List<Vista> registros = null;
    Sentencias sentencia = null;
    StringBuffer cadenaTem = new StringBuffer("");
    StringBuffer SQL= new StringBuffer("");
    List<Vista> registroCondicion = null;
    try {
      registros = new ArrayList<Vista>();
      registroCondicion = new ArrayList<Vista>();
      registroCondicion = condicion(numMov,1);
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA); 
      
      SQL.append(" select cb.id_cuenta, cb.num_cuenta, cb.unidad_ejecutora, lpad((cb.entidad || cb.ambito), 4, 0) ambito, cb.nombre_cta, fecha_hora, ");
      SQL.append("       decode(m.clave_trans_recla,null,' ',m.clave_trans_recla) || ' ' || decode(m.descripcion,null,' ',m.descripcion) descripcion, ");
      SQL.append("       nvl(decode(id_tipo_trans,'DR',monto,0),0) cargo, nvl(decode(id_tipo_trans,'CR',monto,0),0) abono, ");
      //SQL.append("       decode(referencia,null,' ',referencia) referencia, ");
        SQL.append("        'MovTesoHSBC: ' || ");
        SQL.append(numMov);
        SQL.append("||' '|| (decode(m.referencia, null, ' ', m.referencia) ||");
        SQL.append("        '   TESORERIA ID_MOV: ' || m.id_movimiento) referencia, ");
      // SQL.append(" (decode(referencia, null, ' ', referencia) ||'   TESORERIA ID_MOV: '||m.id_movimiento) referencia,");
      SQL.append(" ct.id_tipo_aplica tipo_aplica, ABS(M.MONTO) IMPORTE ");
      SQL.append(" from ");
      SQL.append("     RF_TR_CUENTAS_BANCARIAS cb, RF_TR_MOVIMIENTOS_CUENTA m, ");
      SQL.append(" rf_tc_claves_transaccion ct ");
      SQL.append(" where cb.id_banco=1 and cb.id_cuenta = m.id_cuenta(+) ");
      SQL.append(" and m.id_clave_trans=ct.id_clave_trans  ");
      SQL.append(" and cb.num_cuenta = '4042547752' and cb.id_tipo_cta=3 ");
      SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
      SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1  and ");
      //SQL.append(" and  m.fecha_hora >= to_date('" + fechaInicio +"','dd/mm/yyyy')  ");
      //SQL.append(" and  m.fecha_hora < to_date('" + fechaFin +"','dd/mm/yyyy')+1 and ");
      SQL.append(registroCondicion.get(0).getField("CONDICION"));
      SQL.append(" order by monto ");
        //System.out.println("SiafmTesoreria.selectFormaHSBC: "+SQL.toString());
      registros = sentencia.registros(SQL.toString());
      if(registros != null){
        for(Vista registro: registros){
          hm.put ("VAR1",registroCondicion.get(0).getField("VAR1"));
          hm.put ("VAR2",registroCondicion.get(0).getField("VAR2"));
          hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
          hm.put ("IMPORTE",registro.getField("IMPORTE"));
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
        }
      }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo selectFormaHSBC");
      System.out.println ("SiafmTesoreria.registrosContablesHSBC: " + SQL.toString());
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return cadenaTem.toString();
    
  }
  
  private List<Vista> condicion(String movimiento,int id_banco) throws SQLException, Exception {
    List<Vista> registros = null;
    Sentencias sentencia = null;
    StringBuffer SQL= new StringBuffer("");
    SQL.append("select * from RF_TR_HSBC_REG_SIMPLIF where mov=" + movimiento+ " and id_banco="+id_banco);
    try{
      registros = new ArrayList<Vista>();
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA); 
      registros = sentencia.registros(SQL.toString());
       
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo Lista");
      System.out.println ("SiafmTesoreria.Lista: ");
      System.out.println ("SiafmTesoreria.registrosContablesBancoNo.condicion: "+id_banco+" "+ SQL.toString());
      throw e;
    } 
    finally {
      SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return registros;
  }  
  
  public String registrosContablesBanamex(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
      HashMap hm = new HashMap();
      List<Vista> registros = null;
      List<Vista> registroCondicion = null;
      boolean queryGeneral = true;
      Sentencias sentencia = null;
      StringBuffer cadenaTem = new StringBuffer("");
      StringBuffer SQL= new StringBuffer("");
   
      try {
        registros = new ArrayList<Vista>();
        registroCondicion = new ArrayList<Vista>();
        registroCondicion = condicion(numMov,2);
        sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA);
        int c=0;
   //query general
   if(numMov.equals("59"))
     queryGeneral=false;
   if(numMov.equals("60"))
     queryGeneral=false;
   if(queryGeneral){
          SQL.append("    select cb.id_cuenta id_cuenta, ");
          SQL.append("        cb.num_cuenta num_cuenta,cb.nombre_cta nombre_cta,tc.id_tipo_cta id_tipo_cta, ");
          SQL.append("        tc.descripcion descCta, ");
          SQL.append(registroCondicion.get(0).getField("COND_VARIABLES"));
          SQL.append("        abs(m.monto) importe,cb.unidad_ejecutora unidad_ejecutora,lpad((cb.entidad || cb.ambito), 4, 0) ambito, ");
          SQL.append("        m.fecha_hora fecha_hora, ");
          SQL.append("        decode(m.clave_trans_recla, null, ' ', m.clave_trans_recla) || ' ' || ");
          SQL.append("        decode(m.descripcion, null, ' ', m.descripcion) descripcion, ");
          SQL.append("        m.id_tipo_trans id_tipo_trans, ");
          SQL.append("        'MovTesoBMX: ' || ");
          SQL.append(numMov);
          SQL.append("||' '|| (decode(m.referencia, null, ' ', m.referencia) ||");
          SQL.append("        '   TESORERIA ID_MOV: ' || m.id_movimiento) referencia, ");
          SQL.append("        m.id_cuenta mid_cuenta, m.id_movimiento id_movimiento \n");
          SQL.append("    \nfrom RF_TR_CUENTAS_BANCARIAS  cb, RF_TR_MOVIMIENTOS_CUENTA m, ");
          SQL.append("        rf_tc_tipo_cuenta        tc, rf_tc_claves_transaccion ct ");
          SQL.append("    \nwhere ");
          SQL.append("        m.id_clave_trans = ct.id_clave_trans ");
          SQL.append("        and cb.id_cuenta = m.id_cuenta ");
          SQL.append("        and cb.id_tipo_cta = tc.id_tipo_cta  ");
       
      SQL.append(registroCondicion.get(0).getField("CONDICION"));
      SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
      SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1 ");
      SQL.append(" and ( ( cb.num_cuenta <> '43710149845'  \n" +
      "  and M.CLAVE_TRANS = '403' \n" +
      "  and (m.referencia like ('%/MAF%') or  \n" + 
     //a partir de 1/05/2013 se simplifican las siguientes referencias y se cambian por la referencia MIN - realizo cambio CLHL
      "         m.referencia like ('%/MDR%') or m.referencia like ('%/MDV%') or  m.referencia like ('%/MPC%') or"+
      "         m.referencia like ('%/MIN%') or \n" + 
      "         m.referencia like ('%/MFR%') or \n" + 
      "         m.referencia like ('%/MRT%') or \n" + 
      "         m.referencia like ('%/MSG%') or m.referencia like ('%/MPZ%') or\n" +
      "         m.referencia like ('%MLCB%') or m.referencia like ('%MPZB%') or\n" +
      "         m.referencia like ('%/MSV%') or m.referencia like ('%/MLC%'))" +
      "  and nvl(decode(id_tipo_trans,'D',monto,0),0) = 0 ) \n" +
      "  or \n" +
      "  ( cb.num_cuenta <> '43710149845' \n" +
      "  and M.CLAVE_TRANS = '322' \n" +
      "  and substr(m.referencia, 1, 3) in ('RCG','RDR','RDV','RFR','RAF') \n" +
      "  and substr(m.referencia, length(m.referencia)-5, length(m.referencia)) <> '029106' \n" +
      "  and nvl(decode(id_tipo_trans,'C',monto,0),0) = 0 ) \n" +
      "  or \n" +
      "  ( cb.num_cuenta = '43710149845' \n" +
      "    and (    (M.CLAVE_TRANS = '849' and (m.referencia like '%DUPL%' or m.referencia like '%MSG%' or m.referencia like '%MPZ%' or m.referencia like ('%MLCB%') or m.referencia like ('%MPZB%') or m.referencia like '%MEX%'))\n" +
      "          or (M.CLAVE_TRANS = '425' and (substr(referencia, 1,3) in ('RZO', 'INV', 'DIS') or (m.referencia LIKE ('%11206%')) or (m.referencia LIKE ('%RECAA%')) ))\n" +
      "          or (M.CLAVE_TRANS in ('439', '364', '322', '053', '035') )" +
      "          or m.referencia like '%29076%' "+
      "          or m.referencia like '%'||to_char(to_char(sysdate,'yyyy'))||'SEG%' "+
      "          or (M.CLAVE_TRANS = '342')) )\n" +
      ")");
      SQL.append("\norder by monto");
        registros = sentencia.registros(SQL.toString());
        //registros = null;
      } else{
        registros = sentencia.registros(ctaBnx29106(fForma,fechaInicio, fechaFin,numMov).toString());
        
      }
      //System.out.println("SiafmTesoreria.registrosContablesBanamex: \n"+SQL.toString());
      c=0;
        //registros = sentencia.registros(SQL.toString());
        if(registros != null){
          c=0;
          for(Vista registro: registros){
            hm.put ("VAR1",registro.getField("VAR1"));
            hm.put ("VAR2",registro.getField("VAR2"));
            hm.put ("VAR3",registro.getField("VAR3"));
            if(registro.getField("REFERENCIA").contains("/"))
              hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA").substring(0, registro.getField("REFERENCIA").indexOf("/") - 1));
            else
              hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
            hm.put ("IMPORTE",registro.getField("IMPORTE"));
            cadenaTem.append(Cadena.construyeCadena(hm));
            cadenaTem.append("~");
            hm.clear();
            c=c+1;
          }          //System.out.println("SiafmTesoreria.registrosContablesBanamexNuevo: "+SQL.toString()+"Registros: "+c);
        }
      }
      catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo registrosContablesBanamex");
        System.out.println ("SiafmTesoreria.registroscontablesBanamex: " + SQL.toString());
        throw e;
      }
      finally {
        SQL.setLength(0);
        SQL=null;
        sentencia = null;
      }
      return cadenaTem.toString();
      }
   

public String registrosContablesBBVA(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
  HashMap hm = new HashMap();
  List<Vista> registros = null;
  Sentencias sentencia = null;
  StringBuffer cadenaTem = new StringBuffer("");
  StringBuffer SQL= new StringBuffer("");
  List<Vista> registroCondicion = null;
  try {
    /*registros = new ArrayList<Vista>();
    registroCondicion = new ArrayList<Vista>();
    registroCondicion = condicion(numMov,3);
    sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA); 
    
    SQL.append(" select cb.id_cuenta, cb.num_cuenta, cb.unidad_ejecutora, lpad((cb.entidad || cb.ambito), 4, 0) ambito, cb.nombre_cta, fecha_hora, ");
    SQL.append("       decode(m.clave_trans_recla,null,' ',m.clave_trans_recla) || ' ' || decode(m.descripcion,null,' ',m.descripcion) descripcion, ");
    SQL.append("       nvl(decode(id_tipo_trans,'DR',monto,0),0) cargo, nvl(decode(id_tipo_trans,'CR',monto,0),0) abono, ");
    //SQL.append("       decode(referencia,null,' ',referencia) referencia, ");
      SQL.append("        'MovTesoBBVA: ' || ");
      SQL.append(numMov);
      SQL.append("||' '|| (decode(m.referencia, null, ' ', m.referencia) ||");
      SQL.append("        '   TESORERIA ID_MOV: ' || m.id_movimiento) referencia, ");
    
SQL.append(registroCondicion.get(0).getField("COND_VARIABLES"));
    SQL.append(" (decode(referencia, null, ' ', referencia) ||'   TESORERIA ID_MOV: '||m.id_movimiento) referencia,");
    SQL.append(" ct.id_tipo_aplica tipo_aplica, ABS(M.MONTO) IMPORTE ");
    SQL.append(" from ");
    SQL.append("     RF_TR_CUENTAS_BANCARIAS cb, RF_TR_MOVIMIENTOS_CUENTA m, ");
    SQL.append(" rf_tc_claves_transaccion ct ");
    SQL.append(" where cb.id_banco=3 and cb.id_cuenta = m.id_cuenta(+) ");
    SQL.append(" and m.id_clave_trans=ct.id_clave_trans  ");
    SQL.append(" and cb.num_cuenta = '0161763024' ");
    SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
    SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1  ");

    SQL.append(registroCondicion.get(0).getField("CONDICION")==null?" ":registroCondicion.get(0).getField("CONDICION"));
    SQL.append(" order by monto ");
    System.out.println("SiafmTesoreria.selectFormaBBVA: "+SQL.toString());
    registros = sentencia.registros(SQL.toString());
    if(registros != null){
      for(Vista registro: registros){
        hm.put ("VAR1",registroCondicion.get(0).getField("VAR1"));
        hm.put ("VAR2",registroCondicion.get(0).getField("VAR2"));
        hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
        hm.put ("IMPORTE",registro.getField("IMPORTE"));
        cadenaTem.append(Cadena.construyeCadena(hm));
        cadenaTem.append("~");
        hm.clear();
      }
    }*/
  }
  catch (Exception e){
    System.out.println ("Ha ocurrido un error en el metodo selectFormaBBVA");
    System.out.println ("SiafmTesoreria.registrosContablesBBVA: " + SQL.toString());
    throw e;
  } 
  finally {
    SQL.setLength(0);
    SQL=null;
    sentencia = null;
  }
  return cadenaTem.toString();
  
}

public String registrosContablesBmx29076(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
    HashMap hm = new HashMap();
    List<Vista> registros = null;
    List<Vista> registroCondicion = null;
   
    Sentencias sentencia = null;
    StringBuffer cadenaTem = new StringBuffer("");
    StringBuffer SQL= new StringBuffer("");
 
    try {
      registros = new ArrayList<Vista>();
      registroCondicion = new ArrayList<Vista>();
      registroCondicion = condicion(numMov,2);
      sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA);
      int c=0;
 //query general
        SQL.append("    select cb.id_cuenta id_cuenta, ");
        SQL.append("        cb.num_cuenta num_cuenta,cb.nombre_cta nombre_cta,tc.id_tipo_cta id_tipo_cta, ");
        SQL.append("        tc.descripcion descCta, ");
        SQL.append(registroCondicion.get(0).getField("COND_VARIABLES"));
        SQL.append("        abs(m.monto) importe,cb.unidad_ejecutora unidad_ejecutora,lpad((cb.entidad || cb.ambito), 4, 0) ambito, ");
        SQL.append("        m.fecha_hora fecha_hora, ");
        SQL.append("        decode(m.clave_trans_recla, null, ' ', m.clave_trans_recla) || ' ' || ");
        SQL.append("        decode(m.descripcion, null, ' ', m.descripcion) descripcion, ");
        SQL.append("        m.id_tipo_trans id_tipo_trans, ");
        SQL.append("        'MovTesoBMX29076: ' || ");
        SQL.append(numMov);
        SQL.append("||' '|| (decode(m.referencia, null, ' ', m.referencia) ||");
        SQL.append("        '   TESORERIA ID_MOV: ' || m.id_movimiento) referencia, ");
        SQL.append("        m.id_cuenta mid_cuenta, m.id_movimiento id_movimiento \n");
        SQL.append("    \nfrom RF_TR_CUENTAS_BANCARIAS  cb, RF_TR_MOVIMIENTOS_CUENTA m, ");
        SQL.append("        rf_tc_tipo_cuenta        tc, rf_tc_claves_transaccion ct ");
        SQL.append("    \nwhere ");
        SQL.append("        m.id_clave_trans = ct.id_clave_trans ");
        SQL.append("        and cb.id_cuenta = m.id_cuenta ");
        SQL.append("        and cb.id_tipo_cta = tc.id_tipo_cta  ");
     
    SQL.append(registroCondicion.get(0).getField("CONDICION"));
    SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
    SQL.append(" and to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1 ");
    SQL.append(" and ( ( cb.num_cuenta <> '08400029076'  \n" +
    "  and M.CLAVE_TRANS = '403' \n" +
    "  and (m.referencia like ('%/MAF%') or  \n" + 
    //a partir de 1/05/2013 se simplifican las siguientes referencias y se cambian por la referencia MIN - realizo cambio CLHL
    "         m.referencia like ('%/MDR%') or m.referencia like ('%/MDV%') or m.referencia like ('%/MPC%') or \n" + 
    "         m.referencia like ('%/MIN%') or \n" + 
    "         m.referencia like ('%/MFR%') or \n" + 
    "         m.referencia like ('%/MRT%') or \n" + 
    "         m.referencia like ('%/MSG%') or m.referencia like ('%/MPZ%') or\n" +
    "         m.referencia like ('%MLCB%') or m.referencia like ('%MPZB%') or\n" +
    "         m.referencia like ('%/MSV%') or m.referencia like ('%/MLC%'))" +
    "  and nvl(decode(id_tipo_trans,'D',monto,0),0) = 0 ) \n" +
    "  or \n" +
    "  ( cb.num_cuenta <> '08400029076' \n" +
    "  and M.CLAVE_TRANS = '322' \n" +
    "  and substr(m.referencia, 1, 3) in ('RCG','RDR','RDV','RFR','RAF') \n" +
    "  and substr(m.referencia, length(m.referencia)-5, length(m.referencia)) <> '029106' \n" +
    "  and nvl(decode(id_tipo_trans,'C',monto,0),0) = 0 ) \n" +
    "  or \n" +
    "  ( cb.num_cuenta = '08400029076' \n" +
    "    and (    (M.CLAVE_TRANS = '849' and (m.referencia like '%DUPL%' or m.referencia like '%MSG%' or referencia like '%MPZ%' or m.referencia like '%MEX%'))\n" +
    "          or (M.CLAVE_TRANS = '425' and (substr(referencia, 1,3) in ('RZO', 'INV', 'DIS') or (m.referencia LIKE ('%11206%')) or (m.referencia LIKE ('%RECAA%')) ))\n" +
    "          or (M.CLAVE_TRANS in ('439', '364', '322', '053', '035') )" +
    "          or (M.CLAVE_TRANS = '342')) )\n" +
    ")");
    SQL.append("\norder by monto");
    //System.out.println("SiafmTesoreria.registrosContablesBmx29076: \n"+SQL.toString());
    c=0;
      registros = sentencia.registros(SQL.toString());
      //registros = null;
      if(registros != null){
        c=0;
        for(Vista registro: registros){
          hm.put ("VAR1",registro.getField("VAR1"));
          hm.put ("VAR2",registro.getField("VAR2"));
          hm.put ("VAR3",registro.getField("VAR3"));
          hm.put ("REFERENCIA",registro.getField("REFERENCIA")==null?" ":registro.getField("REFERENCIA"));
          hm.put ("IMPORTE",registro.getField("IMPORTE"));
          cadenaTem.append(Cadena.construyeCadena(hm));
          cadenaTem.append("~");
          hm.clear();
          c=c+1;
        }          //System.out.println("SiafmTesoreria.registrosContablesBmx29076: "+SQL.toString()+"Registros: "+c);
      }
    }
    catch (Exception e){
      System.out.println ("Ha ocurrido un error en el metodo registrosContablesBmx29076");
      System.out.println ("SiafmTesoreria.registroscontablesBmx29076: " + SQL.toString());
      throw e;
    }
    finally {
      SQL.setLength(0);
      SQL=null;
      sentencia = null;
    }
    return cadenaTem.toString();
    }    

  public StringBuffer ctaBnx29106(String fForma,String fechaInicio, String fechaFin,String numMov) throws SQLException, Exception {
      List<Vista> registroCondicion = null;
      Sentencias sentencia = null;
      StringBuffer SQL= new StringBuffer("");
   
      try {
        registroCondicion = new ArrayList<Vista>();
        registroCondicion = condicion(numMov,2);
        sentencia = new Sentencias(DaoFactory.CONEXION_TESORERIA);
        
        SQL.append("    select cb.id_cuenta id_cuenta, ");
        SQL.append("        cb.num_cuenta num_cuenta,cb.nombre_cta nombre_cta,tc.id_tipo_cta id_tipo_cta, ");
        SQL.append("        tc.descripcion descCta, ");
        SQL.append(registroCondicion.get(0).getField("COND_VARIABLES"));
        SQL.append("        abs(m.monto) importe,cb.unidad_ejecutora unidad_ejecutora,lpad((cb.entidad || cb.ambito), 4, 0) ambito, ");
        SQL.append("        m.fecha_hora fecha_hora, ");
        SQL.append("        decode(m.clave_trans_recla, null, ' ', m.clave_trans_recla) || ' ' || ");
        SQL.append("        decode(m.descripcion, null, ' ', m.descripcion) descripcion, ");
        SQL.append("        m.id_tipo_trans id_tipo_trans, ");
        //SQL.append("        'MovTesoBMX29106: ' || ");
        //SQL.append(numMov);
        //SQL.append("||' '|| (decode(m.referencia, null, ' ', m.referencia) ||");
        SQL.append("decode(m.referencia, null, ' ', m.referencia) referencia, ");
        //SQL.append("        '   TESORERIA ID_MOV: ' || m.id_movimiento) referencia, ");
        SQL.append("        m.id_cuenta mid_cuenta, m.id_movimiento id_movimiento \n");
        SQL.append("    \nfrom RF_TR_CUENTAS_BANCARIAS  cb, RF_TR_MOVIMIENTOS_CUENTA m, ");
        SQL.append("        rf_tc_tipo_cuenta        tc, rf_tc_claves_transaccion ct ");
        SQL.append("    \nwhere ");
        SQL.append("        m.id_clave_trans = ct.id_clave_trans ");
        SQL.append("        and cb.id_cuenta = m.id_cuenta ");
        SQL.append("        and cb.id_tipo_cta = tc.id_tipo_cta  ");
        SQL.append(" \nand cb.num_cuenta = 08400029106");  
        SQL.append("\n").append(registroCondicion.get(0).getField("CONDICION"));
        SQL.append(" \nand to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') >= to_date('"+fechaInicio+"', 'dd/mm/yyyy')  ");
        SQL.append(" \nand to_date(to_char(m.fecha_hora,'dd/mm/yyyy'),'dd/mm/yyyy') < to_date('"+fechaFin+"', 'dd/mm/yyyy') + 1 ");
        SQL.append("\norder by monto");
        //System.out.println("SiafmTesoreria.registrosContablesBanamex: \n"+SQL.toString());
      }
      catch (Exception e){
        System.out.println ("Ha ocurrido un error en el metodo registrosContablesBanamex");
        System.out.println ("SiafmTesoreria.ctaBnx29016: " + SQL.toString());
        throw e;
      }
      finally {
        sentencia = null;
        registroCondicion = null;
      }
      return SQL;
      }
}


