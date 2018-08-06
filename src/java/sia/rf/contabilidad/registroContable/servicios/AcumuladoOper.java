package sia.rf.contabilidad.registroContable.servicios;

public class AcumuladoOper {

  private Double debe;
  private Double haber;

  public AcumuladoOper(Double debe, Double haber) 
  {
    this.debe=debe;
    this.haber=haber;
  }

  public void setDebe(Double debe) {
    this.debe = debe;
  }

  public Double getDebe() {
    return debe;
  }

  public void setHaber(Double haber) {
    this.haber = haber;
  }

  public Double getHaber() {
    return haber;
  }
}
