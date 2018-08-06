package sia.rf.tesoreria.catalogos.tiposReporte;

public class TipoReporte {
  
  private String descripcion;
  private int valor;
  private String flujo;
  
  public TipoReporte(String descripcion, int valor) {
    setDescripcion(descripcion);
    setValor(valor);
  }
  
  public TipoReporte(String descripcion, String flujo, int valor) {
    setDescripcion(descripcion);
    setFlujo(flujo);
    setValor(valor);
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setValor(int valor) {
    this.valor = valor;
  }

  public int getValor() {
    return valor;
  }

  public void setFlujo(String flujo) {
    this.flujo = flujo;
  }

  public String getFlujo() {
    return flujo;
  }
}
