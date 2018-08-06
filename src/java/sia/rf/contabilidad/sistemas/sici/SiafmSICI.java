/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.contabilidad.sistemas.sici;

import java.util.HashMap;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import sia.rf.contabilidad.registroContableEvento.Cadena;

import java.sql.SQLException;

/**
 *
 * @author Carlos.Bragdon
 */
public class SiafmSICI {
  
  public String getReferencia() {
    return referencia;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public SiafmSICI() {
  }
  
  private String referencia;
  private String concepto;

  /**
   * Evento 350. Registro diario de las transferencias de ingresos
   * @param conSICI
   * @param idIngresoAuto
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento350(Connection conSICI, String idIngresoAuto) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    HashMap hm = null;
    StringBuffer query = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      hm = new HashMap();
      query = new StringBuffer();

      query.append("select");
      query.append(" id_ingreso_auto||'-'||num_traspaso as referencia, ");
      query.append(" substr(id_rubro, 1, 1) as nivel_2, ");
      query.append(" '0'||substr(id_rubro, 1, 3) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as nivel_8, ");
      query.append(" (importe+ (iva- retencion_iva)) as importe_bancos, ");
      query.append(" importe as importe_acreedores, ");
      query.append(" (iva- retencion_iva) as importe_entidades, ");
      query.append(" case when tasa_iva= 0 then '0001' else case when tasa_iva= 11 then '0002' else '0003' end end as tasa_iva ");
      query.append("from");
      query.append(" ct_tr_ingresos_auto ");
      query.append("where");
      query.append(" id_ingreso_auto = ").append(idIngresoAuto).append(" ");
      //si importe_entidades = 0; no registre en la 23115->2117
      rst = stm.executeQuery(query.toString());
      if (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_BANCOS", rst.getString("IMPORTE_BANCOS"));//total
        hm.put("IMPORTE_ACREEDORES", rst.getString("IMPORTE_ACREEDORES"));//importe sin iva
        hm.put("IMPORTE_ENTIDADES", rst.getString("IMPORTE_ENTIDADES"));//iva- retencion_iva
        hm.put("NIVEL2", rst.getString("NIVEL_2"));
        hm.put("RUBRO1", rst.getString("RUBRO").substring(2, 4));//2191
        hm.put("RUBRO2", rst.getString("RUBRO"));//2117
        hm.put("NIVEL8", rst.getString("NIVEL_8"));
        hm.put("NIVEL9", rst.getString("NIVEL_8"));
        hm.put("TASA", rst.getString("TASA_IVA"));

        this.setReferencia(rst.getString("REFERENCIA"));
      }
      rst.close();
      query.delete(0, query.length());
      query.append(Cadena.construyeCadena(hm));
      query.append("~");
      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento350]: " + e.getMessage());
      throw e;
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }
      rst = null;
      stm = null;
    }

    return query.toString();
  }

  /**
   * Evento 351. Cancelacion del registro de las transferencias de ingresos
   * @param conSICI
   * @param idIngresoAuto
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento351(Connection conSICI, String idIngresoAuto) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    HashMap hm = null;
    StringBuffer query = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      hm = new HashMap();
      query = new StringBuffer();

      query.append("select");
      query.append(" id_ingreso_auto||'-'||num_traspaso as referencia, ");
      query.append(" substr(id_rubro, 1, 1) as nivel_2, ");
      query.append(" '0'||substr(id_rubro, 1, 3) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as nivel_8, ");
      query.append(" (importe+ (iva- retencion_iva)) as importe_bancos, ");
      query.append(" importe as importe_acreedores, ");
      query.append(" (iva- retencion_iva) as importe_entidades, ");
      query.append(" case when tasa_iva= 0 then '0001' else case when tasa_iva= 11 then '0002' else '0003' end end as tasa_iva ");
      query.append("from");
      query.append(" ct_tr_ingresos_auto ");
      query.append("where");
      query.append(" id_ingreso_auto = ").append(idIngresoAuto).append(" ");
      //si importe_entidades = 0; no registre en la 23115/2117
      rst = stm.executeQuery(query.toString());
      if (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_BANCOS", rst.getString("IMPORTE_BANCOS"));//total
        hm.put("IMPORTE_ACREEDORES", rst.getString("IMPORTE_ACREEDORES"));//importe sin iva
        hm.put("IMPORTE_ENTIDADES", rst.getString("IMPORTE_ENTIDADES"));//iva- retencion_iva
        hm.put("NIVEL2", rst.getString("NIVEL_2"));
        hm.put("RUBRO1", rst.getString("RUBRO").substring(2, 4));//2191
        hm.put("RUBRO2", rst.getString("RUBRO"));//2117
        hm.put("NIVEL8", rst.getString("NIVEL_8"));
        hm.put("NIVEL9", rst.getString("NIVEL_8"));
        hm.put("TASA", rst.getString("TASA_IVA"));
        this.setReferencia(rst.getString("REFERENCIA"));
      }
      rst.close();
      query.delete(0, query.length());
      query.append(Cadena.construyeCadena(hm));
      query.append("~");
      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento351]: " + e.getMessage());
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }

      rst = null;
      stm = null;
    }

    return query.toString();
  }

  /**
   * Evento 352. Devolucion de las transferencias de ingresos
   * @param conSICI
   * @param idIngresoAuto
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento352(Connection conSICI, String idIngresoAuto) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    HashMap hm = null;
    StringBuffer query = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

      hm = new HashMap();
      query = new StringBuffer();

      query.append("select");
      query.append(" id_ingreso_auto||'-'||num_traspaso as referencia, ");
      query.append(" substr(id_rubro, 1, 1) as nivel_2, ");
      query.append(" '0'||substr(id_rubro, 1, 3) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as nivel_8, ");
      query.append(" (importe+ (iva- retencion_iva)) as importe_bancos, ");
      query.append(" importe as importe_acreedores, ");
      query.append(" (iva- retencion_iva) as importe_entidades, ");
      query.append(" case when tasa_iva= 0 then '0001' else case when tasa_iva= 11 then '0002' else '0003' end end as tasa_iva ");
      query.append("from");
      query.append(" ct_tr_ingresos_auto ");
      query.append("where");
      query.append(" id_ingreso_auto = ").append(idIngresoAuto).append(" ");
      //si importe_entidades = 0; no registre en la 23115 -> 2117
      rst = stm.executeQuery(query.toString());
      if (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_BANCOS", rst.getString("IMPORTE_BANCOS"));//total
        hm.put("IMPORTE_ACREEDORES", rst.getString("IMPORTE_ACREEDORES"));//importe sin iva
        hm.put("IMPORTE_ENTIDADES", rst.getString("IMPORTE_ENTIDADES"));//iva- retencion_iva
        hm.put("NIVEL2", rst.getString("NIVEL_2"));
        hm.put("RUBRO1", rst.getString("RUBRO").substring(2, 4));//2191
        hm.put("RUBRO2", rst.getString("RUBRO"));//2117
        hm.put("NIVEL8", rst.getString("NIVEL_8"));
        hm.put("NIVEL9", rst.getString("NIVEL_8"));
        hm.put("TASA", rst.getString("TASA_IVA"));
        this.setReferencia(rst.getString("REFERENCIA"));
      }
      rst.close();
      query.delete(0, query.length());
      query.append(Cadena.construyeCadena(hm));
      query.append("~");
      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento352]: " + e.getMessage());
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }

      rst = null;
      stm = null;
    }

    return query.toString();
  }

  /**
   * Evento 353A. Transferencia de recursos a la cuenta concentradora de ingresos
   * @param conSICI
   * @param idTransferencia
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento353A(Connection conSICI, String idTransferencia) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    double importeAcumulado = 0.0f;
    HashMap hm = null;
    StringBuffer query = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      hm = new HashMap();
      query = new StringBuffer();

      query.append("select");
      query.append(" id_transferencia as referencia, ");
      query.append(" substr(id_rubro, 1, 1) as nivel_2, ");
      query.append(" '00'||substr(id_rubro, 2, 2) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as nivel_8, ");
      query.append(" observaciones_estatus as concepto, ");
      query.append(" sum(importe) as importe ");
      query.append("from");
      query.append(" ct_tr_ingresos_auto ");
      query.append("where");
      query.append(" id_transferencia = '").append(idTransferencia).append("' ");
      query.append(" and estatus_ingrs_auto = 4 ");//corresponde devolucion
      query.append("group by");
      query.append(" id_transferencia, ");
      query.append(" substr(id_rubro, 1, 1), ");
      query.append(" '00'||substr(id_rubro, 2, 2), ");
      query.append(" '000'||substr(id_rubro, 4, 1), ");
      query.append(" observaciones_estatus ");
      query.append("order by");
      query.append(" nivel_2, ");
      query.append(" rubro, ");
      query.append(" nivel_8 ");
      rst = stm.executeQuery(query.toString());
      query.delete(0, query.length());
      //llenado 21205
      while (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_ACREEDORES", rst.getString("IMPORTE"));//total
        //hm.put("UNIDAD", rst.getString("UNIDAD_EJECUTORA"));
        hm.put("NIVEL2", rst.getString("NIVEL_2"));
        hm.put("RUBRO", rst.getString("RUBRO"));
        hm.put("NIVEL8", rst.getString("NIVEL_8"));
        hm.put("CONCEPTO", rst.getString("CONCEPTO"));
        this.setReferencia(rst.getString("REFERENCIA"));
        
        importeAcumulado += rst.getDouble("IMPORTE");

        query.append(Cadena.construyeCadena(hm));
        query.append("~");
      }
      rst.close();
      //llenado 11203 -> 1112
      hm.clear();
      hm.put("REFERENCIA", this.getReferencia());
      hm.put("IMPORTE_BANCOS", importeAcumulado);//total
      //System.out.println("acumulado final: " + importeAcumulado);

      query.append(Cadena.construyeCadena(hm));
      query.append("~");

      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento353]: " + e.getMessage());
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }

      rst = null;
      stm = null;
    }

    return query.toString();
  }

    /**
   * Evento 353B. Transferencia de recursos de la cuenta de ingresos autogenerados
   * @param conSICI
   * @param idTransferencia
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento353B(Connection conSICI, String idTransferencia) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    double importeAcumulado = 0.0f;
    HashMap hm = null;
    StringBuffer query = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      hm = new HashMap();
      query = new StringBuffer();

      query.append("select");
      query.append(" id_transferencia as referencia, ");
      query.append(" '00'||substr(id_rubro, 1, 2) as nivel_7, ");
      query.append(" '000'||substr(id_rubro, 3, 1) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as nivel_9, ");
      query.append(" observaciones_estatus as concepto, ");
      query.append(" sum(importe) as importe ");
      query.append("from");
      query.append(" ct_tr_ingresos_auto ");
      query.append("where");
      query.append(" id_transferencia = '").append(idTransferencia).append("' ");
      query.append(" and estatus_ingrs_auto = 4 ");//corresponde devolucion
      query.append("group by");
      query.append(" id_transferencia, ");
      query.append(" '00'||substr(id_rubro, 1, 2), ");
      query.append(" '000'||substr(id_rubro, 3, 1), ");
      query.append(" '000'||substr(id_rubro, 4, 1), ");
      query.append(" observaciones_estatus ");
      query.append("order by");
      query.append(" nivel_7, ");
      query.append(" rubro, ");
      query.append(" nivel_9 ");
      rst = stm.executeQuery(query.toString());
      query.delete(0, query.length());
      //llenado 23101->1112
      while (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_DEPOSITOS", rst.getString("IMPORTE"));//total
        hm.put("NIVEL7", rst.getString("NIVEL_7"));
        hm.put("RUBRO", rst.getString("RUBRO"));
        hm.put("NIVEL9", rst.getString("NIVEL_9"));
        hm.put("CONCEPTO", rst.getString("CONCEPTO"));
        this.setReferencia(rst.getString("REFERENCIA"));
        
        importeAcumulado += rst.getDouble("IMPORTE");

        query.append(Cadena.construyeCadena(hm));
        query.append("~");
      }
      rst.close();
      hm.clear();

      //llenado 11203->2199
      hm.put("REFERENCIA", this.getReferencia());
      hm.put("IMPORTE_BANCOS", importeAcumulado);//total
      query.append(Cadena.construyeCadena(hm));
      query.append("~");

      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento353]: " + e.getMessage());
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }

      rst = null;
      stm = null;
    }

    return query.toString();
  }

  /**
   * Evento 354. Traslado IVA
   * @param conSICI
   * @param idTrasladoIva
   * @return
   * @throws SQLException
   * @throws Exception
   */
  public String evento354(Connection conSICI, String idTrasladoIva) throws SQLException, Exception {
    Statement stm = null;
    ResultSet rst = null;
    //double importeAcumulado = 0.0f;
    HashMap hm = null;
    StringBuffer query = null;
    StringBuffer cadena = null;
    try {
      stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      hm = new HashMap();
      query = new StringBuffer();
      cadena = new StringBuffer();

      query.append("select");
      query.append("  id_traslado_iva as referencia, ");
      query.append(" '0'||substr(id_rubro, 1, 3) as rubro, ");
      query.append(" '000'||substr(id_rubro, 4, 1) as analitico, ");
      query.append("  case when tasa_iva= 0 then '0001' else case when tasa_iva= 11 then '0002' else '0003' end end as tasa_iva, ");
      query.append("  sum(iva)-sum(retencion_iva) as importe_iva ");
      query.append("from");
      query.append("  ct_tr_ingresos_auto ");
      query.append("where");
      query.append("  id_traslado_iva = '").append(idTrasladoIva).append("' ");
      query.append("  and (estatus_ingrs_auto = 1 or estatus_ingrs_auto = 4) ");//solo estatus 1 o 4
      query.append("group by");
      query.append("  id_traslado_iva, ");
      query.append(" '0'||substr(id_rubro, 1, 3), ");
      query.append(" '000'||substr(id_rubro, 4, 1), ");
      query.append("  case when tasa_iva= 0 then '0001' else case when tasa_iva= 11 then '0002' else '0003' end end ");
      query.append("order by");
      query.append("  rubro, ");
      query.append("  analitico ");
      rst = stm.executeQuery(query.toString());
      query.delete(0, query.length());
      //llenado 23115
      while (rst.next()) {
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("RUBRO", rst.getString("RUBRO"));
        hm.put("ANALITICO", rst.getString("ANALITICO"));
        hm.put("IMPORTE_ENTIDADES", rst.getString("IMPORTE_IVA"));
        hm.put("TASA", rst.getString("TASA_IVA"));
        //importeAcumulado += rst.getDouble("IMPORTE_IVA");

        cadena.append(Cadena.construyeCadena(hm));
        cadena.append("~");
      }
      rst.close();
      //hm.clear();
      query.delete(0, query.length());
      query.append("select");
      query.append("  id_traslado_iva as referencia, ");
      query.append("  diferencia_redondeo, ");
      query.append("  diferencia_redondeo+ (select sum(iva)- sum(retencion_iva) from ct_tr_ingresos_auto where id_traslado_iva = '").append(idTrasladoIva).append("' ) as total_importe_iva, ");
      query.append("  observaciones as concepto ");
      query.append("from");
      query.append("  ct_tr_traslado_iva ");
      query.append("where");
      query.append("  id_traslado_iva = '").append(idTrasladoIva).append("' ");
      rst = stm.executeQuery(query.toString());
      if (rst.next()) {
        //llenado 23115-> 2117 en caso de diferencia por redondeo
        if (rst.getDouble("DIFERENCIA_REDONDEO")!= 0){
          hm.put("REFERENCIA", rst.getString("REFERENCIA"));
          hm.put("RUBRO", "0000");
          hm.put("ANALITICO", "0000");
          hm.put("IMPORTE_ENTIDADES", rst.getString("DIFERENCIA_REDONDEO"));
          hm.put("TASA", "4");//UR 100

          cadena.append(Cadena.construyeCadena(hm));
          cadena.append("~");
        }
        hm.clear();
        //llenado 11203
        hm.put("REFERENCIA", rst.getString("REFERENCIA"));
        hm.put("IMPORTE_BANCOS", rst.getString("TOTAL_IMPORTE_IVA"));//sumar diferencia_redondeo
        hm.put("CONCEPTO", rst.getString("CONCEPTO"));
        this.setReferencia(rst.getString("REFERENCIA"));
        cadena.append(Cadena.construyeCadena(hm));
        cadena.append("~");
      }
      rst.close();
      hm.clear();
    } catch (Exception e) {
      System.out.println("Error.[SiafmSICI.evento354]: " + e.getMessage());
    } finally {
      if (rst != null) {
        rst.close();
      }
      if (stm != null) {
        stm.close();
      }

      rst = null;
      stm = null;
    }

    return cadena.toString();
  }
  

     //public String evento1AF(Connection conSICI, String mesEjercicio) throws SQLException, Exception {
      /**
        * Evento 1AF. Poliza de eliminacion de OT - Activos Fijos
        * @param conSICI
        * @param mesEjercicio MM/yyyy
        * @return
        * @throws SQLException
        * @throws Exception 
        */
     //public String evento1AF(Connection conSICI, String mesEjercicio) throws SQLException, Exception { 
       public String evento355(Connection conSICI, String mesEjercicio) throws SQLException, Exception {
        Statement stm = null;
         ResultSet rst = null;
         HashMap hm = null;
         StringBuffer query = null;
         StringBuffer cadena = null;         
         String meses[] = new String[] {"pendiente", "ene", "feb", "mar", "abr", "may", "jun", "jul", "ago", "sep", "oct", "nov", "dic"}; 
         try {
           stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
           hm = new HashMap();
           query = new StringBuffer();
           cadena = new StringBuffer();
           query.append("select \n");
           query.append("  nivel1, nivel2, (cargo_periodo*-1) as debe, (abono_periodo*-1) as haber \n");
           query.append("from ( \n");
           query.append("  select \n");
           query.append("    decode(substr(cc.cuenta_contable, 18, 2), '52', '1242', \n");
           query.append("      decode(substr(cc.cuenta_contable, 18, 2), '53', '1243', \n");
           query.append("        decode(substr(cc.cuenta_contable, 18, 2), '54', '1244', \n");
           query.append("          decode(substr(cc.cuenta_contable, 18, 2), '56', '1246', \n");
           query.append("            decode(substr(cc.cuenta_contable, 18, 3), '513', '1247', \n");
           query.append("              decode(substr(cc.cuenta_contable, 18, 3), '514', '1247', '1241') \n");
           query.append("            ) \n");
           query.append("          ) \n");
           query.append("        ) \n");
           query.append("      ) \n");
           query.append("    ) as nivel1, \n");
           query.append("    decode(substr(cc.cuenta_contable, 18, 3), '515', 3, \n");
           query.append("      decode(substr(cc.cuenta_contable, 18, 3), '514', 2, substr(cc.cuenta_contable, 20, 1)) \n");
           query.append("    ) as nivel2, \n");
           query.append("    substr(cc.cuenta_contable, 1, 4) cuenta_mayor, \n");
           query.append("    substr(cc.cuenta_contable, 18, 4) partida_generica, \n"); 
           query.append("    sum(cc.").append(meses[Integer.parseInt(mesEjercicio.substring(0, 2))]).append("_cargo) as cargo_periodo, \n");
           query.append("    sum(cc.").append(meses[Integer.parseInt(mesEjercicio.substring(0, 2))]).append("_abono) as abono_periodo, \n");
           query.append("    cl.naturaleza \n");
           query.append("  from \n");
           query.append("    rf_contabilidad_arm.rf_tr_cuentas_contables cc, \n");
           query.append("    rf_contabilidad_arm.rf_tc_clasificador_cuentas cl \n");
           query.append("  where \n");
           query.append("    cc.cuenta_mayor_id = cl.cuenta_mayor_id \n");
           query.append("    and cc.id_catalogo_cuenta=1 \n");
           query.append("    and to_char(cc.fecha_vig_ini, 'yyyy') = '").append(mesEjercicio.substring(3, 7)).append("' \n");
           query.append("    and cc.cuenta_contable like '114560001____________0000%' \n");
           query.append("    and substr(cc.cuenta_contable, 18, 4)!= '0000' \n");
           query.append("  group by \n");
           query.append("    substr(cc.cuenta_contable, 1, 4), \n");
           query.append("    substr(cc.cuenta_contable, 18, 4), \n");
           query.append("    cl.naturaleza, \n");
           query.append("    decode(substr(cc.cuenta_contable, 18, 2), '52', '1242', \n");
           query.append("      decode(substr(cc.cuenta_contable, 18, 2), '53', '1243', \n");
           query.append("        decode(substr(cc.cuenta_contable, 18, 2), '54', '1244', \n");
           query.append("          decode(substr(cc.cuenta_contable, 18, 2), '56', '1246', \n");
           query.append("            decode(substr(cc.cuenta_contable, 18, 3), '513', '1247', \n");
           query.append("              decode(substr(cc.cuenta_contable, 18, 3), '514', '1247', '1241') \n");
           query.append("            ) \n");
           query.append("          ) \n");
           query.append("        ) \n");
           query.append("      ) \n");
           query.append("    ), \n");
           query.append("    decode(substr(cc.cuenta_contable, 18, 3), '515', 3, \n");
           query.append("      decode(substr(cc.cuenta_contable, 18, 3), '514', 2, substr(cc.cuenta_contable, 20, 1)) \n");
           query.append("    ) \n");
           query.append(") \n");
           query.append("where (cargo_periodo!= 0 and abono_periodo != 0) \n");
           query.append("order by nivel1, nivel2 "); 
           rst = stm.executeQuery(query.toString());
           query.delete(0, query.length());
           //llenado parte1
           while (rst.next()) {
             hm.put("REFERENCIA","Eliminación OT "+mesEjercicio);
             hm.put("NIVEL".concat(rst.getString("NIVEL1")), rst.getString("NIVEL2"));
             hm.put("IMPORTE".concat(rst.getString("NIVEL1")), rst.getString("DEBE"));
             cadena.append(Cadena.construyeCadena(hm));
             cadena.append("~");
             hm.clear();
           }
           this.setReferencia("Eliminación OT "+mesEjercicio);
           rst.close();
           
         } catch (Exception e) {
           System.out.println("Error.[SiafmSICI.evento1AF]: " + e.getMessage());
         } finally {
           if (rst != null) {
             rst.close();
           }
           if (stm != null) {
             stm.close();
           }
           rst = null;
           stm = null;
         }
         return cadena.toString();
       }
       
  /**
     * Evento 1AF. Poliza de eliminacion de Remesas - Activos Fijos
     * @param conSICI
     * @param mesEjercicio MM/yyyy
     * @return
     * @throws SQLException
     * @throws Exception 
     */
    public String evento356(Connection conSICI, String mesEjercicio) throws SQLException, Exception {
      Statement stm = null;
      ResultSet rst = null;
      HashMap hm = null;
      StringBuffer query = null;
      StringBuffer cadena = null;
      
      try {
        stm = conSICI.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        hm = new HashMap();
        query = new StringBuffer();
        cadena = new StringBuffer();
        
        query.append(" select \n");
        query.append(" partida,  \n");
        query.append(" nivel2,  \n");
        query.append(" sum(ot_entrada)*-1 as debe,  \n");
        query.append(" sum(ot_salida)*-1 as haber  \n");
        query.append(" from (  \n");
        query.append(" select  \n");
        query.append(" substr(clv_art,1,2)||'00' as partida,  \n");
        query.append(" decode(substr(substr(clv_art,1,2), 2, 1), '1', 1, '2', 2, '3', 9, '4', 3, '5', 4, '6', 5, '7', 6, '8', 7, '9', 8, 0) as nivel2,  \n");
        query.append(" case when (mg.clv_tmov = 5 or mg.clv_tmov= 6) then round((md.cantidad * md.precio) + md.iva, 2) else 0 end as ot_entrada,  \n");
        query.append(" case when (mg.clv_tmov = 11 or mg.clv_tmov= 12 or mg.clv_tmov= 13) then round((md.cantidad * md.precio) + md.iva, 2) else 0 end as ot_salida  \n");
        query.append(" from  \n");
        query.append(" sia_almacen.rm_tr_movimientosg mg  \n");
        query.append(" left outer join sia_almacen.rm_tr_movimientosd md on mg.id = md.id_origen  \n");
        query.append(" where  \n");
        query.append(" to_char(mg.fecha, 'MM/yyyy') = '").append(mesEjercicio).append("' \n");
        query.append(" and (  \n");
        query.append(" mg.clv_tmov = 5 or  \n");
        query.append(" mg.clv_tmov = 6  \n");
        query.append(" or  \n");
        query.append(" mg.clv_tmov = 11 or  \n");
        query.append(" mg.clv_tmov = 12 or  \n");
        query.append(" mg.clv_tmov = 13  \n");
        query.append(" )  \n");
        query.append(" and nvl((select to_char(mx.fecha, 'MM/yyyy') from sia_almacen.rm_tr_movimientosg mx where mx.id = mg.ot_destino_id), to_char(mg.fecha, 'MM/yyyy')) = '").append(mesEjercicio).append("' \n");
        query.append(" and nvl((select to_char(mx.fecha, 'MM/yyyy') from sia_almacen.rm_tr_movimientosg mx where mx.ot_destino_id = mg.id), to_char(mg.fecha, 'MM/yyyy')) = '").append(mesEjercicio).append("' \n");
        query.append(" )  \n");
        query.append(" group by  \n");
        query.append(" partida, nivel2 \n");
        query.append(" order by  \n");
        query.append(" partida  \n");
        rst = stm.executeQuery(query.toString());
        query.delete(0, query.length());
        while (rst.next()) {
          hm.put("SUB_MAYOR", rst.getString("NIVEL2"));
          hm.put("IMPORTE", rst.getString("DEBE"));
          hm.put("REFERENCIA", rst.getString("PARTIDA"));
          cadena.append(Cadena.construyeCadena(hm));
          cadena.append("~");
        }
        hm.clear();
        rst.close();
        
      } catch (Exception e) {
        System.out.println("Error.[SiafmSICI.evento2AF]: " + e.getMessage());
      } finally {
        if (rst != null) {
          rst.close();
        }
        if (stm != null) {
          stm.close();
        }

        rst = null;
        stm = null;
      }

      return cadena.toString();
    }
       

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public String getConcepto() {
    return concepto;
  }
}
