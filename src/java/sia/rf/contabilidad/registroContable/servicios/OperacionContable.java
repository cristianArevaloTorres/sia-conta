package sia.rf.contabilidad.registroContable.servicios;

public class OperacionContable {

  private PeriodoOper periodoOper;
  private AcumuladoOper acumuladoOper;
  private int mes;

  public OperacionContable(PeriodoOper periodoOper,AcumuladoOper acumuladoOper,int mes) 
  {
    this.periodoOper=periodoOper;
    this.acumuladoOper=acumuladoOper;
    this.mes=mes;
  }

  public void setPeriodoOper(PeriodoOper periodoOper) {
    this.periodoOper = periodoOper;
  }

  public PeriodoOper getPeriodoOper() {
    return periodoOper;
  }

  public void setAcumuladoOper(AcumuladoOper acumuladoOper) {
    this.acumuladoOper = acumuladoOper;
  }

  public AcumuladoOper getAcumuladoOper() {
    return acumuladoOper;
  }

  public void setMes(int mes) {
    this.mes = mes;
  }

  public int getMes() {
    return mes;
  }
}
