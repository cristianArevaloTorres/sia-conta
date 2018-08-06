package sia.rf.contabilidad.reportes;

public class DetallePolizaValidacion {

  private String fecha;
  private String tipoPoliza;
  private Double monto;
  private int tipoOperacion; 

  public DetallePolizaValidacion() {
    this(null,null,null,0);
  }

  public DetallePolizaValidacion(String fecha, String tipoPoliza, Double monto, int tipoOperacion) {
     setFecha(fecha);
     setTipoPoliza(tipoPoliza);
     setMonto(monto);
     setTipoOperacion(tipoOperacion);
  }
  
  public void setFecha(String fecha) {
    this.fecha = fecha;
  }

  public String getFecha() {
    return fecha;
  }

  public void setTipoPoliza(String tipoPoliza) {
    this.tipoPoliza = tipoPoliza;
  }

  public String getTipoPoliza() {
    return tipoPoliza;
  }

  public void setMonto(Double monto) {
    this.monto = monto;
  }

  public Double getMonto() {
    return monto;
  }

  public void setTipoOperacion(int tipoOperacion) {
    this.tipoOperacion = tipoOperacion;
  }

  public int getTipoOperacion() {
    return tipoOperacion;
  }
  
}
