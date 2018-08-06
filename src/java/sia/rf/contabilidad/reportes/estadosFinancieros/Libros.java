package sia.rf.contabilidad.reportes.estadosFinancieros;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import sia.db.dao.DaoFactory;

import sia.libs.formato.Formatos;
import sia.libs.periodo.Fecha;

public class Libros {
String fecha1=null;
String fecha2=null;
String ejercicio=null;
private Connection conexion = null;
  
  public Libros(String fecha1, String fecha2,String ejercicio) {
  setFecha1(fecha1);
  setFecha2(fecha2);  
  setEjercicio(ejercicio);
  }
  
  public StringBuffer sentenciaMayor(String fechaini, String fechafin, String mes,String nombreMes, String ctaMayor, String anio) {
     StringBuffer sentencia = new StringBuffer();
     sentencia.append("select to_date('").append(fechaini).append("','dd/mm/yyyy') as fecha_afectacion, 0 as poliza_id, null as num_evento,null as num_asiento, ");
     sentencia.append("'0' as documento,cla.cuenta_mayor,cla.naturaleza, 'SALDO ANTERIOR' as descripcion, ");
     if(nombreMes.equals("ENERO")){
       sentencia.append("decode(t.").append(mes).append("_cargo_ini_eli,0,0,t.").append(mes).append("_cargo_ini_eli) Debe, ");
       sentencia.append("decode(t.").append(mes).append("_abono_ini_eli,0,0,t.").append(mes).append("_abono_ini_eli) Haber, ");
     }else{ 
         sentencia.append("decode(t.").append(mes).append("_cargo_acum_eli,0,0,t.").append(mes).append("_cargo_acum_eli) Debe, ");
         sentencia.append("decode(t.").append(mes).append("_abono_acum_eli,0,0,t.").append(mes).append("_abono_acum_eli) Haber, ");
     }
     sentencia.append("1 as orden ");
     sentencia.append("from rf_tr_cuentas_contables t, rf_tc_clasificador_cuentas cla ");
     sentencia.append("where t.nivel=1  and t.cuenta_mayor_id= cla.cuenta_mayor_id and t.id_catalogo_cuenta=1 ");
     sentencia.append("and substr(t.cuenta_contable,0,4)='").append(ctaMayor).append("' ");
     sentencia.append("and to_char(t.fecha_vig_ini, 'yyyy')='").append(anio).append("' ");
     sentencia.append("UNION ALL ");
     sentencia.append("select polizas.fecha_afectacion, polizas.poliza_id, polizas.num_evento, polizas.num_asiento, polizas.documento, ");
     sentencia.append("polizas.cuenta_mayor, polizas.naturaleza, polizas.descripcion, polizas.debe, polizas.haber, polizas.orden ");
     sentencia.append("from( select dp.fecha_afectacion, p.poliza_id, p.origen num_evento, p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0') num_asiento, ");
     sentencia.append("p.referencia documento, cla.cuenta_mayor, cla.naturaleza, cla.descripcion despcripcion_cuenta, p.concepto descripcion, ");
     sentencia.append("decode(dp.operacion_contable_id,'0',dp.importe,0) Debe, decode(dp.operacion_contable_id,'1',dp.importe,0) Haber, 2 as orden ");
     sentencia.append("from rf_tr_detalle_poliza dp, Rf_Tr_Polizas p, rf_tr_cuentas_contables cc, rf_tc_clasificador_cuentas cla, rf_tc_tipos_polizas tp ");
     sentencia.append("where tp.tipo_poliza_id=p.tipo_poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id and cc.cuenta_mayor_id=cla.cuenta_mayor_id ");
     sentencia.append("and cc.id_catalogo_cuenta=1 and substr(cc.cuenta_contable,0,4)='").append(ctaMayor).append("' ");
     sentencia.append("and to_date(to_char(dp.fecha_afectacion,'dd/MM/yyyy'),'dd/MM/yyyy') >= to_date('").append(fechaini).append("','dd/MM,yyyy') ");
     sentencia.append("AND to_date(to_char(dp.fecha_afectacion,'dd/MM/yyyy'),'dd/MM/yyyy') <= to_date('").append(fechafin).append("','dd/MM/yyyy') ");
     sentencia.append("and to_char(p.fecha, 'dd/MM/yyyy')=to_char(dp.fecha_afectacion,'dd/MM/yyyy') and p.id_catalogo_cuenta=1 and p.id_catalogo_cuenta=cc.id_catalogo_cuenta ");
     sentencia.append("and p.poliza_id=dp.poliza_id and dp.cuenta_contable_id=cc.cuenta_contable_id ");
     sentencia.append("and (p.idevento not in(301) or p.idevento is null ) ");
     sentencia.append(") polizas ");
     sentencia.append("order by fecha_afectacion, orden, poliza_id");
     return sentencia;
     } //formarSentenciaMayor()
 
  
   public StringBuffer sentenciaConcentrado() {     
     StringBuffer sentencia = new StringBuffer();
     sentencia.append("select c.cuenta,c.cuenta_mayor_id, c.descripcion, c.fechaA fecha_afectacion, decode(p.debe,null,0,p.debe) debe,decode(p.haber,null,0,p.haber) haber ");
     sentencia.append("from( ");
     sentencia.append("select fecha_afectacion, cuenta,cuenta_mayor_id, ");
     sentencia.append("sum(decode(haber,null,0,haber)) haber,sum(decode(debe,null,0,debe)) debe,descripcion ");
     sentencia.append("from( ");     
     sentencia.append("select dp1.fecha_afectacion,  substr(cc1.cuenta_contable, 1, 4) cuenta,cc1.cuenta_mayor_id, cl.descripcion, ");                       
     sentencia.append("(case when dp1.operacion_contable_id=0 then sum(importe) else 0 end) debe, ");
     sentencia.append("(case when dp1.operacion_contable_id=1 then sum(importe) else 0 end) haber ");
     sentencia.append("from rf_tr_cuentas_contables       cc1, ");
     sentencia.append("RF_TR_DETALLE_POLIZA_TEMP_LIB dp1, ");
     sentencia.append("RF_TR_POLIZAS_TEMP_LIB        pol, ");
     sentencia.append("rf_tc_clasificador_cuentas cl ");
     sentencia.append("where   cc1.cuenta_contable_id = dp1.cuenta_contable_id ");
     sentencia.append("and pol.poliza_id = dp1.poliza_id ");
     sentencia.append("and cc1.cuenta_mayor_id=cl.cuenta_mayor_id ");
     sentencia.append("and dp1.fecha_afectacion >= '").append(fecha1);
     sentencia.append("' and dp1.fecha_afectacion <= '").append(fecha2);                        
     sentencia.append("' and cc1.id_catalogo_cuenta = 1 ");
     sentencia.append("and extract(year from cc1.fecha_vig_ini) = ").append(ejercicio);
     sentencia.append("and pol.tipo_poliza_id <> 6 ");
     sentencia.append("and (pol.idevento is null or pol.idevento <> 301) ");
     sentencia.append("group by dp1.fecha_afectacion,substr(cc1.cuenta_contable, 1, 4), cc1.cuenta_mayor_id, ");
     sentencia.append("dp1.operacion_contable_id, cl.descripcion ");
     sentencia.append(")tabla ");
     sentencia.append("group by tabla.cuenta, tabla.fecha_afectacion,tabla.cuenta_mayor_id, tabla.descripcion ");
     sentencia.append("order by tabla.fecha_afectacion ");
     sentencia.append(")p, ");
     sentencia.append("(select cuenta_mayor_id, substr(cuenta_contable,1,4) cuenta, descripcion, fecha.fechaA ");
      sentencia.append("from rf_tr_cuentas_contables,(select  distinct(fecha_afectacion) fechaA from RF_TR_DETALLE_POLIZA_TEMP_LIB ");
     sentencia.append("where (fecha_afectacion>='").append(fecha1).append("' and fecha_afectacion<='").append(fecha2).append("')) fecha ");                    
     sentencia.append(" where extract(year from fecha_vig_ini)=").append(ejercicio);
     sentencia.append(" and id_catalogo_cuenta=1 ");
     sentencia.append("and nivel=1)c ");
     sentencia.append("where c.cuenta=p.cuenta(+) and c.fechaA=p.fecha_afectacion(+) ");
     sentencia.append("order by c.fechaA,c.cuenta ");
     return sentencia;
   } //formarSentenciaConcentrado()
   
    public StringBuffer sentenciaConcentradoOriginal() {
      StringBuffer sentencia = new StringBuffer();
      sentencia.append("select c.cuenta,c.cuenta_mayor_id, c.descripcion, c.fechaA fecha_afectacion, decode(p.debe,null,0,p.debe) debe,decode(p.haber,null,0,p.haber) haber ");
      sentencia.append("from( ");
      sentencia.append("select fecha_afectacion, cuenta,cuenta_mayor_id, ");
      sentencia.append("sum(decode(haber,null,0,haber)) haber,sum(decode(debe,null,0,debe)) debe,descripcion ");
      sentencia.append("from( ");
      sentencia.append("select dp.fecha_afectacion, substr(cc.cuenta_contable,1,4) cuenta, ");
      sentencia.append("cc.cuenta_mayor_id,cc.cuenta_contable_id, ");
      sentencia.append("( ");
      sentencia.append("select sum(dp1.importe) haber  ");
      //sentencia.append("from rf_tr_cuentas_contables cc1, rf_tr_detalle_poliza dp1, Rf_Tr_Polizas pol  ");
      sentencia.append("from rf_tr_cuentas_contables cc1, RF_TR_DETALLE_POLIZA_TEMP_LIB dp1, RF_TR_POLIZAS_TEMP_LIB pol  ");
      sentencia.append("where dp1.fecha_afectacion>='").append(fecha1).append("' and dp1.fecha_afectacion<='").append(fecha2).append("'");
      sentencia.append(" and cc1.cuenta_contable_id=dp1.cuenta_contable_id ");
      sentencia.append("and dp1.operacion_contable_id=1 ");
      sentencia.append("and cc1.id_catalogo_cuenta=1 ");
      sentencia.append("and extract(year from cc1.fecha_vig_ini)=").append(ejercicio);
      sentencia.append(" and cc.cuenta_mayor_id=cc1.cuenta_mayor_id ");
      sentencia.append("and dp.fecha_afectacion=dp1.fecha_afectacion and pol.poliza_id= dp1.poliza_id and pol.tipo_poliza_id<>6 and ( pol.idevento is null or pol.idevento<>301)");
      sentencia.append(")haber, ");
      sentencia.append("( ");
      sentencia.append("select sum(dp1.importe) haber  ");
      //sentencia.append("from rf_tr_cuentas_contables cc1, rf_tr_detalle_poliza dp1, Rf_Tr_Polizas pol  ");
      sentencia.append("from rf_tr_cuentas_contables cc1, RF_TR_DETALLE_POLIZA_TEMP_LIB dp1, RF_TR_POLIZAS_TEMP_LIB pol  ");
      sentencia.append("where dp1.fecha_afectacion>='").append(fecha1).append("' and dp1.fecha_afectacion<='").append(fecha2).append("'");
      sentencia.append(" and cc1.cuenta_contable_id=dp1.cuenta_contable_id ");
      sentencia.append("and dp1.operacion_contable_id=0 ");
      sentencia.append("and cc1.id_catalogo_cuenta=1 ");
      sentencia.append("and extract(year from cc1.fecha_vig_ini)=").append(ejercicio);
      sentencia.append(" and cc.cuenta_mayor_id=cc1.cuenta_mayor_id ");
      sentencia.append("and dp.fecha_afectacion=dp1.fecha_afectacion and pol.poliza_id= dp1.poliza_id  and pol.tipo_poliza_id<>6 and ( pol.idevento is null or pol.idevento<>301)");
      sentencia.append(")debe, ");
      sentencia.append("( ");
      sentencia.append("select descripcion ");
      sentencia.append("from rf_tc_clasificador_cuentas  ");
      sentencia.append("where cuenta_mayor_id=cc.cuenta_mayor_id) descripcion ");
      //sentencia.append("from rf_tr_cuentas_contables cc, rf_tr_detalle_poliza dp ");
      sentencia.append("from rf_tr_cuentas_contables cc, RF_TR_DETALLE_POLIZA_TEMP_LIB dp ");
      sentencia.append("where (dp.fecha_afectacion>='").append(fecha1).append("' and dp.fecha_afectacion<='").append(fecha2).append("')");
      sentencia.append(" and cc.cuenta_contable_id=dp.cuenta_contable_id ");
      sentencia.append("and  cc.id_catalogo_cuenta=1 ");
      sentencia.append("and extract(year from cc.fecha_vig_ini)=").append(ejercicio);
      sentencia.append(")tabla ");
      sentencia.append("group by tabla.cuenta, tabla.fecha_afectacion,tabla.cuenta_mayor_id, tabla.descripcion ");
      sentencia.append("order by tabla.fecha_afectacion ");
      sentencia.append(")p, ");
      sentencia.append("(select cuenta_mayor_id, substr(cuenta_contable,1,4) cuenta, descripcion, fecha.fechaA ");
      //sentencia.append("from rf_tr_cuentas_contables,(select  distinct(fecha_afectacion) fechaA from rf_tr_detalle_poliza ");
       sentencia.append("from rf_tr_cuentas_contables,(select  distinct(fecha_afectacion) fechaA from RF_TR_DETALLE_POLIZA_TEMP_LIB ");
      sentencia.append("where (fecha_afectacion>='").append(fecha1).append("' and fecha_afectacion<='").append(fecha2).append("')) fecha ");                    
      sentencia.append(" where extract(year from fecha_vig_ini)=").append(ejercicio);
      sentencia.append(" and id_catalogo_cuenta=1 ");
      sentencia.append("and nivel=1)c ");
      sentencia.append("where c.cuenta=p.cuenta(+) and c.fechaA=p.fecha_afectacion(+) ");
      sentencia.append("order by c.fechaA,c.cuenta ");
      return sentencia;
    } //formarSentenciaConcentrado()
   
    public StringBuffer sentenciaGeneral() {
      StringBuffer sentencia = new StringBuffer();
      sentencia.append("select p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||'-'||LPAD(p.consecutivo,5,'0') num_Poliza, ");
      sentencia.append("p.referencia documento,dp.fecha_afectacion, p.concepto descripcion,cla.cuenta_mayor,cla.descripcion despcripcion_cuenta, ");
      sentencia.append("decode(dp.operacion_contable_id,'0',dp.importe,0) Debe, ");
      sentencia.append("decode(dp.operacion_contable_id,'1',dp.importe,0) Haber,dp.cuenta_contable_id ");
      sentencia.append("from rf_tr_detalle_poliza dp, "); 
      sentencia.append("Rf_Tr_Polizas p, "); 
      sentencia.append("rf_tr_cuentas_contables cc, "); 
      sentencia.append("rf_tc_clasificador_cuentas cla, ");
      sentencia.append("rf_tc_tipos_polizas tp ");
      sentencia.append("where tp.tipo_poliza_id=p.tipo_poliza_id ");
      sentencia.append("and dp.cuenta_contable_id=cc.cuenta_contable_id ");
      sentencia.append("and cc.cuenta_mayor_id=cla.cuenta_mayor_id ");
      sentencia.append("and cc.id_catalogo_cuenta=1 ");
      sentencia.append("and dp.fecha_afectacion>='").append(fecha1).append("' AND  dp.fecha_afectacion<='").append(fecha2).append("'");
      sentencia.append("and to_char(p.fecha, 'dd/MM/yyyy')=to_char(dp.fecha_afectacion,'dd/MM/yyyy') ");
      sentencia.append("and p.id_catalogo_cuenta=1 ");
      sentencia.append("and p.id_catalogo_cuenta=cc.id_catalogo_cuenta ");
      sentencia.append("and p.poliza_id=dp.poliza_id ");
      sentencia.append("and dp.cuenta_contable_id=cc.cuenta_contable_id ");
      sentencia.append("order by dp.fecha_afectacion,p.unidad_ejecutora||'-'||LPAD(p.entidad,2,'0')||p.ambito||'-'||tp.abreviatura||LPAD(p.consecutivo,5,'0'), ");
      sentencia.append("dp.operacion_contable_id, cla.cuenta_mayor, dp.cuenta_contable_id ");
      
          return sentencia;
    } //formarSentenciaMayor()

  public void setFecha1(String fecha1) {
    this.fecha1 = fecha1;
  }

  public String getFecha1() {
    return fecha1;
  }

  public void setFecha2(String fecha2) {
    this.fecha2 = fecha2;
  }

  public String getFecha2() {
    return fecha2;
  }

  public void setEjercicio(String ejercicio) {
    this.ejercicio = ejercicio;
  }

  public String getEjercicio() {
    return ejercicio;
  }
  
   
    private void limpiarTablaPolizasTemporal() throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM rf_tr_polizas_temp_lib");

            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Libros.limpiarTablaPolizasTemporal()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally     }
    }

    private void limpiarTablaDetallesPolizasTemporal() throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("DELETE FROM rf_tr_detalle_poliza_temp_lib");
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoUsuariosPerfiles()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    }

    private void llenarTablaPolizasTemporal() throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO rf_tr_polizas_temp_lib  \n");
            SQL.append("    SELECT * \n");
            SQL.append("    FROM rf_tr_polizas \n");
            SQL.append("    WHERE id_catalogo_cuenta = 1 \n");
            SQL.append("          AND TO_DATE(TO_CHAR(fecha,'dd/mm/yyyy'),'dd/mm/yyyy') >= '").append(fecha1).append("' \n");
            SQL.append("          AND TO_DATE(TO_CHAR(fecha,'dd/mm/yyyy'),'dd/mm/yyyy') <= '").append(fecha2).append("' \n");
            SQL.append("          AND ejercicio = ").append(ejercicio);

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Libros.llenarTablaPolizasTemporal()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally 
    }

    private void llenarTablaDetallesPolizasTemporal() throws SQLException, Exception {
        Statement stQuery = null;
        try {
            stQuery = conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
            StringBuffer SQL = new StringBuffer("INSERT INTO rf_tr_detalle_poliza_temp_lib  \n");
            SQL.append("    SELECT * \n");
            SQL.append("    FROM rf_tr_detalle_poliza \n");
            SQL.append("    WHERE poliza_id IN (SELECT  \n");
            SQL.append("                            poliza_id  \n");
            SQL.append("                        FROM  \n");
            SQL.append("                            rf_tr_polizas \n");
            SQL.append("                        WHERE id_catalogo_cuenta = 1 \n");
            SQL.append("                              AND TO_DATE(TO_CHAR(fecha,'dd/mm/yyyy'),'dd/mm/yyyy') >= '").append(fecha1).append("' \n");
            SQL.append("                              AND TO_DATE(TO_CHAR(fecha,'dd/mm/yyyy'),'dd/mm/yyyy') <= '").append(fecha2).append("' \n");
            SQL.append("                              AND ejercicio = ").append(ejercicio).append(" )");

            //System.out.println(SQL.toString()); 
            int rs = -1;
            rs = stQuery.executeUpdate(SQL.toString());
            
        } //Fin try 
        catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Usuarios.eliminarCatalogoUsuariosPerfiles()" + e.getMessage());
            throw e;
        } //Fin catch 
        finally {
            if (stQuery != null) {
                stQuery.close();
                stQuery = null;
            }
        } //Fin finally                                                                                                                                                                                                                                                                                                                      
    }

    public boolean procesoGenerarLibroDiarioGeneralConcentrado() throws SQLException, Exception {
        boolean correcto = false;

        try {
            conexion = sia.db.dao.DaoFactory.getConnection(DaoFactory.CONEXION_CONTABILIDAD);
            limpiarTablaPolizasTemporal();
            limpiarTablaDetallesPolizasTemporal();
            llenarTablaPolizasTemporal();
            llenarTablaDetallesPolizasTemporal();
            conexion.commit();
            correcto = true;
        } catch (Exception e) {
            System.out.println("Ocurrio un error al accesar al metodo Libros.procesoGenerarLibroDiarioGeneralConcentrado()" + e.getMessage());
            conexion.rollback();
            throw e;
        } finally {
            conexion.close();
            conexion = null;
        }

        return correcto;
    }
}
