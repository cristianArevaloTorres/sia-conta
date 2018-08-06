package sia.rf.tesoreria.registro.operacionesInversion;

import java.sql.Connection;

import java.util.List;

import sia.configuracion.BeanBase;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentenciasCRS;

import sia.db.sql.SentennciasSE;

import sia.db.sql.Vista;

import sia.libs.formato.Fecha;

public class bcOperacionesInversionCalculo extends BeanBase {
  
  public bcOperacionesInversionCalculo() {
  }
  
  public double getSaldoAnteriorMismaFechaOAnterior(String idCuentaInversion, String fecha) throws Exception {
    SentenciasCRS sen = getSaldosAnteriorMismaFechaOAnterior(idCuentaInversion,fecha);
    double regresa = 0.00;
    if(sen.next())
     regresa =  sen.getDouble("saldo_actual");
    return regresa;
  }
  
  public SentenciasCRS getSaldosAnteriorMismaFechaOAnterior(String idCuentaInversion, String fecha) throws Exception {
    SentenciasCRS sen = null;
    try  {
      sen = new SentenciasCRS();
      sen.addParam("idCuentaInversion",idCuentaInversion);
      sen.addParam("fecha",fecha);
      sen.registrosMap(DaoFactory.CONEXION_TESORERIA,"compraInversion.select.RfTrOperacionesInversion-SaldoAnteriorMismaFechaOAnterior.inversiones");
    } catch (Exception ex)  {
      throw new Exception("ha ocurrido un error al obtener los saldos del dia anterior",ex);
    } 
    return sen;
  }
  
  private List<Vista> getSaldosDeFechaAnterior(Connection con, String idCuentaInversion, String fecha) throws Exception {
    SentennciasSE sen = null;
    List<Vista> regresa = null;
    try  {
      sen = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
      sen.addParam("idCuentaInversion",idCuentaInversion);
      sen.addParam("fecha",fecha);
      regresa = sen.registros(con,"compraInversion.select.RfTrOperacionesInversion-SaldoDeFechaAnterior.inversiones");
    } catch (Exception ex)  {
      throw new Exception("ha ocurrido un error al obtener los saldos del dia anterior",ex);
    } 
    return regresa;
  }
  
  private List<Vista> getOperacionesACalcular(Connection con, String idCuentaInversion, String fecha) throws Exception {
    SentennciasSE sen = null;
    List<Vista> regresa;
    try  {
      sen = new SentennciasSE(DaoFactory.CONEXION_TESORERIA);
      sen.addParam("idCuentaInversion",idCuentaInversion);
      sen.addParam("fechax",Fecha.getFormatoValidoFromDer(fecha));
      regresa = sen.registros(con,"compraInversion.select.RfTrOperacionesInversion-OrdenadoMovimientosSiguientes.inversiones");
    } catch (Exception ex)  {
      throw new Exception("ha ocurrido un error al obtener las operaciones a calcular",ex);
    } finally {
      sen = null;
    }
    return regresa;
  }
  
  public void lanzarCalculoDeFechaAnterior(Connection con, String fecha, String idCuentaInversion) throws Exception {
    List<Vista> senSaldo = getSaldosDeFechaAnterior(con,idCuentaInversion,fecha); //SALDO ANTERIOR PRIMER REGISTRO
    List<Vista> senOpera=null;
    //if(senSaldo != null && senSaldo.size() > 0)
    senOpera = getOperacionesACalcular(con,idCuentaInversion,fecha); //OPERACIONES A CALCULAR
    bcRfTrOperacionesInversion operacion = null;
    double saldoActual = 0.00;
    double saldoReal = 0.00;
    if(senSaldo != null && senSaldo.size() > 0) {
      saldoActual = senSaldo.get(0).getDouble("saldo_actual");
      saldoReal = senSaldo.get(0).getDouble("saldo_real");
    }
    if(senOpera!=null) {
      for(Vista opera : senOpera) {
        operacion = new bcRfTrOperacionesInversion(); 
        operacion.setIdOperacion(opera.getField("id_operacion"));
        operacion.setIdCuentaInversion(opera.getField("id_cuenta_inversion"));
        operacion.setIdTipoOperacion(opera.getField("id_tipo_operacion"));
        operacion.setFecha(opera.getField("fecha"));
        operacion.setImporte(opera.getDouble("importe"));
        operacion.setEstatus(opera.getField("estatus"));
        operacion.setSaldoAnterior(saldoActual);
        operacion.setFechaRegistro(opera.getField("fecha_registro"));
        operacion.setAfectacion(opera.getField("afectacion"));
        operacion.setAfectaSaldoReal(opera.getInt("afecta_saldo_real"));
        
        saldoReal  = operacion.addImporte(saldoReal);
        saldoActual= Double.parseDouble(operacion.getSaldoActual());
        operacion.update(con);
      }
    }
  }
}
