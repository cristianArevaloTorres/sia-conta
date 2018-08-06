package sia.rf.contabilidad.registroContable.procesos.polizas.servicios;

public class OperacionContable {
  private int id;
  private double importe;
  private String campo;
  private String parametro;
  private String referencia;

  public OperacionContable(int id, double importe, String campo, String parametro,String referencia) {
    setId(id);
    setImporte(importe);
    setCampo(campo);
    setParametro(parametro);
    setReferencia(referencia);
  }

  public void setImporte(double importe) {
    this.importe = importe;
  }

  public double getImporte() {
    return importe;
  }

  public void setCampo(String campo) {
    this.campo = campo;
  }

  public String getCampo() {
    return campo;
  }

  public String toString() {
    StringBuffer sb = new StringBuffer();
    sb.append("OperacionContable");
    sb.append("[");
    sb.append(getImporte());
    sb.append(",");
    sb.append(getCampo());
    sb.append("]");
    return sb.toString();
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getId() {
    return id;
  }
  
  public void setParametro(String parametro) {
    this.parametro = parametro;
  }

  public String getParametro() {
    return parametro;
  }

  public void setReferencia(String referencia) {
    this.referencia = referencia;
  }

  public String getReferencia() {
    return referencia;
  }
  
  public OperacionContable getNewInstance() {
    return new OperacionContable(getId(), getImporte(), getCampo(), getParametro(), getReferencia());
  }

}
