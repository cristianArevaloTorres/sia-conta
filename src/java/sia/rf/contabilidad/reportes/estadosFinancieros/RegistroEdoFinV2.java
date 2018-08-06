/**
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.contabilidad.reportes.estadosFinancieros;

public class RegistroEdoFinV2 {
  
  private String concepto      = null;
  private String saldoAnterior = null;
  private String saldoActual   = null;
  private String variacion     = null;
  private String porcentaje    = null;
  private boolean lineaSaldo   = false;
  private boolean lineaRenglon = false;

  public RegistroEdoFinV2() {
    this.concepto = null;
    this.saldoAnterior = null;
    this.saldoActual = null;
    this.variacion = null;
    this.porcentaje = null;
    this.lineaSaldo = false;
    this.lineaRenglon = false;
  }  
  
  public RegistroEdoFinV2(String concepto, String saldoActual, String saldoAnterior, String variacion, String porcentaje, boolean lineaSaldo, boolean lineaRenglon){
    this.setConcepto(concepto);
    this.setSaldoAnterior(saldoAnterior);
    this.setSaldoActual(saldoActual);
    this.setVariacion(variacion);
    this.setPorcentaje(porcentaje);
    this.setLineaSaldo(lineaSaldo);
    this.setLineaRenglon(lineaRenglon);
  }

  public String getConcepto() {
    return concepto;
  }

  public void setConcepto(String concepto) {
    this.concepto = concepto;
  }

  public boolean isLineaSaldo() {
    return lineaSaldo;
  }

  public void setLineaSaldo(boolean lineaSaldo) {
    this.lineaSaldo = lineaSaldo;
  }

  public String getPorcentaje() {
    return porcentaje;
  }

  public void setPorcentaje(String porcentaje) {
    this.porcentaje = porcentaje;
  }

  public String getSaldoActual() {
    return saldoActual;
  }

  public void setSaldoActual(String saldoActual) {
    this.saldoActual = saldoActual;
  }

  public String getSaldoAnterior() {
    return saldoAnterior;
  }

  public void setSaldoAnterior(String saldoAnterior) {
    this.saldoAnterior = saldoAnterior;
  }

  public String getVariacion() {
    return variacion;
  }

  public void setVariacion(String variacion) {
    this.variacion = variacion;
  }

  public boolean isLineaRenglon() {
    return lineaRenglon;
  }

  public void setLineaRenglon(boolean lineaRenglon) {
    this.lineaRenglon = lineaRenglon;
  }
  
}
