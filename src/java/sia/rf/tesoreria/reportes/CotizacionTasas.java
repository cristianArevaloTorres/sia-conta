package sia.rf.tesoreria.reportes;


public class CotizacionTasas {

  private Double dias14;
  private Double dia1;
  private Double dias28;
  private Double dias21;
  private Double dias7;
  private String institucionFinanciera;
  private String plazo1;
  private String plazoSemana;
  private String plazoMes;
  private String plazoRango;
 
  
  public CotizacionTasas(String institucionFinanciera, Double dia1, Double dias7, Double dias14,  Double dias21, Double dias28,  String plazo1, String plazoSemana, String plazoMes, String plazoRango) {
    this.institucionFinanciera = institucionFinanciera;
    this.dia1 = dia1;
    this.dias7 = dias7;
    this.dias14 = dias14;
    this.dias21 = dias21;
    this.dias28 = dias28;
    this.plazo1 = plazo1;
    this.plazoSemana = plazoSemana;
    this.plazoMes = plazoMes;
    this.plazoRango = plazoRango;
  }
  
  public CotizacionTasas() {
  }

  public void setDias14(Double dias14) {
    this.dias14 = dias14;
  }

  public Double getDias14() {
    return dias14;
  }

  public void setDia1(Double dia1) {
    this.dia1 = dia1;
  }

  public Double getDia1() {
    return dia1;
  }

  public void setDias28(Double dias28) {
    this.dias28 = dias28;
  }

  public Double getDias28() {
    return dias28;
  }

  public void setDias21(Double dias21) {
    this.dias21 = dias21;
  }

  public Double getDias21() {
    return dias21;
  }

  public void setDias7(Double dias7) {
    this.dias7 = dias7;
  }

  public Double getDias7() {
    return dias7;
  }

  public void setInstitucionFinanciera(String institucionFinanciera) {
    this.institucionFinanciera = institucionFinanciera;
  }

  public String getInstitucionFinanciera() {
    return institucionFinanciera;
  }

  public void setPlazo1(String plazo1) {
    this.plazo1 = plazo1;
  }

  public String getPlazo1() {
    return plazo1;
  }

  public void setPlazoMes(String plazoMes) {
    this.plazoMes = plazoMes;
  }

  public String getPlazoMes() {
    return plazoMes;
  }

  public void setPlazoRango(String plazoRango) {
    this.plazoRango = plazoRango;
  }

  public String getPlazoRango() {
    return plazoRango;
  }


  public void setPlazoSemana(String plazoSemana) {
    this.plazoSemana = plazoSemana;
  }

  public String getPlazoSemana() {
    return plazoSemana;
  }
}
