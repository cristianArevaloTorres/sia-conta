package sia.rf.contabilidad.registroContable.formas.servicios;

public class CuentasForma {
  private String cuentaContable;
  private String tipoOperacion;
  private String cuentaContableFormateada;
  private String importe;
  private String referencia;
  private int cuentaContableId;
  private String movible;
  private int idCatalogoCuenta;

  public CuentasForma(String cuentaContable, String tipoOperacion, String importe, String referencia, 
                      String cuentaContableFormateada, int cuentaContableId, String movible, int idCatalogoCuenta) {
    setCuentaContable(cuentaContable);
    setTipoOperacion(tipoOperacion);
    setImporte(importe);
    setReferencia(referencia);
    setCuentaContableFormateada(cuentaContableFormateada);
    setCuentaContableId(cuentaContableId);
    setMovible(movible);
    setIdCatalogoCuenta(idCatalogoCuenta);
  }
  
  public CuentasForma(String cuentaContable, String tipoOperacion, String importe, String referencia, String movible, int idCatalogoCuenta) {
    this(cuentaContable, tipoOperacion, importe, referencia, "", 0, movible, idCatalogoCuenta);
  }
  
  public CuentasForma(String cuentaContable, String tipoOperacion, String importe, String referencia,int cuentaContableId,int idCatalogoCuenta) {
     this(cuentaContable, tipoOperacion, importe, referencia, "",cuentaContableId, "", idCatalogoCuenta);
   }


  public void setCuentaContable(String cuentaContable) {
    this.cuentaContable = cuentaContable;
  }

  public String getCuentaContable() {
    return cuentaContable;
  }

  public void setImporte(String importe) {
    this.importe = importe;
  }

  public String getImporte() {
    return importe;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }

  public void setTipoOperacion(String tipoOperacion) {
    this.tipoOperacion = tipoOperacion;
  }

  public String getTipoOperacion() {
    return tipoOperacion;
  }

  public void setCuentaContableId(int cuentaContableId) {
    this.cuentaContableId = cuentaContableId;
  }

  public int getCuentaContableId() {
    return cuentaContableId;
  }

  public void setCuentaContableFormateada(String cuentaContableFormateada) {
    this.cuentaContableFormateada = cuentaContableFormateada;
  }

  public String getCuentaContableFormateada() {
    return cuentaContableFormateada;
  }

  public void setMovible(String movible) {
    this.movible = movible;
  }

  public String getMovible() {
    return movible;
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }
}
