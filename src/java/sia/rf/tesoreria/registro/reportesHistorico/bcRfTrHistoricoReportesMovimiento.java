package sia.rf.tesoreria.registro.reportesHistorico;

import sia.db.dao.DaoFactory;
import sia.db.sql.SentenciasCRS;

public class bcRfTrHistoricoReportesMovimiento {

    private String numTransferencia;
    private String tipoMovimiento;
    private String idCuentaBancaria;
    private String monto;
    private String referencia;
    private String tipoCuenta;
    private String noCuentaNombre;
    

    public bcRfTrHistoricoReportesMovimiento() {
    }

    public void setNumTransferencia(String numTransferencia) {
        this.numTransferencia = numTransferencia;
    }

    public String getNumTransferencia() {
        return numTransferencia;
    }

    public void setTipoMovimiento(String tipoMovimiento) {
        this.tipoMovimiento = tipoMovimiento;
    }

    public String getTipoMovimiento() {
        return tipoMovimiento;
    }

    public void setIdCuentaBancaria(String idCuentaBancaria) {
        this.idCuentaBancaria = idCuentaBancaria;
    }

    public String getIdCuentaBancaria() {
        return idCuentaBancaria;
    }

    public void setMonto(String monto) {
        this.monto = monto;
    }

    public String getMonto() {
        return monto;
    }

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public String getReferencia() {
        return referencia;
    }

  private void getNumeroNombreCta(){
    SentenciasCRS crsCuenta = null;
    try  {
      crsCuenta = new SentenciasCRS();
      crsCuenta.addParamVal("tipo","and tipo=:param",getTipoCuenta());
      crsCuenta.addParamVal("idCuenta","and idCuenta=:param",getIdCuentaBancaria());
      crsCuenta.registrosMap(DaoFactory.CONEXION_TESORERIA,"transferenciaFondos.select.afectacion");
      if(crsCuenta.next()){
         setNoCuentaNombre(crsCuenta.getString("cuenta").concat(" ").concat(crsCuenta.getString("beneficiario")));
      }
      
    } catch (Exception ex)  {
      ex.printStackTrace();
    } finally {
      if(crsCuenta!=null){
        crsCuenta.liberaParametros();
        crsCuenta.cerrar();
      }
    }
  }

  public void setNoCuentaNombre(String noCuentaNombre) {
    this.noCuentaNombre = noCuentaNombre;
    
  }

  public String getNoCuentaNombre() {
    getNumeroNombreCta();
    return noCuentaNombre;
  }

  public void setTipoCuenta(String tipoCuenta) {
    this.tipoCuenta = tipoCuenta;
  }

  public String getTipoCuenta() {
    return tipoCuenta;
  }
}
