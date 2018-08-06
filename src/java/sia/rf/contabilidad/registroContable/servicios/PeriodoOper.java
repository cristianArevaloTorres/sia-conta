package sia.rf.contabilidad.registroContable.servicios;

public class PeriodoOper {

  private Double debe;
  private Double haber;

  public PeriodoOper(Double debe,Double haber) 
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
