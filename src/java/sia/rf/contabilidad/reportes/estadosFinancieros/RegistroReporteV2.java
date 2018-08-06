/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package sia.rf.contabilidad.reportes.estadosFinancieros;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;


public class RegistroReporteV2 {
  
  private String conceptoA    = null;
  private String saldoAnteriorA = null;
  private String saldoActualA = null;
  private String variacionA   = null;
  private String porcentajeA  = null;
  private boolean lineaSaldoA  = false;
  private boolean lineaRenglonA  = false;
  
  private String conceptoB    = null;
  private String saldoAnteriorB = null;
  private String saldoActualB = null;
  private String variacionB   = null;
  private String porcentajeB  = null;
  private boolean lineaSaldoB  = false;
  private boolean lineaRenglonB  = false;
  
  private boolean miles  = false;

  public RegistroReporteV2() {
    this.conceptoA = null;
    this.saldoAnteriorA = null;
    this.saldoActualA = null;
    this.variacionA = null;
    this.porcentajeA = null;
    this.lineaSaldoA = false;
    this.lineaRenglonA = false;
    this.conceptoB = null;
    this.saldoAnteriorB = null;
    this.saldoActualB = null;
    this.variacionB = null;
    this.porcentajeB = null;
    this.lineaSaldoB = false;
    this.lineaRenglonB = false;
    
    this.miles = false;
  }
  
  public RegistroReporteV2(String conceptoA, String saldoActualA, String saldoAnteriorA, String variacionA, String porcentajeA, boolean lineaSaldoA, boolean lineaRenglonA, String conceptoB, String saldoActualB, String saldoAnteriorB, String variacionB, String porcentajeB, boolean lineaSaldoB, boolean lineaRenglonB, boolean aMiles){
    this.setConceptoA(conceptoA);
    this.setSaldoAnteriorA(saldoAnteriorA);
    this.setSaldoActualA(saldoActualA);
    this.setVariacionA(variacionA);
    this.setPorcentajeA(porcentajeA);
    this.setLineaSaldoA(lineaSaldoA);
    this.setLineaRenglonA(lineaRenglonA);
    
    this.setConceptoB(conceptoB);
    this.setSaldoAnteriorB(saldoAnteriorB);
    this.setSaldoActualB(saldoActualB);
    this.setVariacionB(variacionB);
    this.setPorcentajeB(porcentajeB);
    this.setLineaSaldoB(lineaSaldoB);
    this.setLineaRenglonB(lineaRenglonB);
    
    this.setMiles(aMiles);
  }
  
  public String getConceptoA() {
    return conceptoA;
  }

  public void setConceptoA(String conceptoA) {
    this.conceptoA = conceptoA;
  }

  public String getConceptoB() {
    return conceptoB;
  }

  public void setConceptoB(String conceptoB) {
    this.conceptoB = conceptoB;
  }

  public String getPorcentajeA() {
    return porcentajeA;
  }

  public void setPorcentajeA(String porcentajeA) {
    this.porcentajeA = porcentajeA;
  }

  public String getPorcentajeB() {
    return porcentajeB;
  }

  public void setPorcentajeB(String porcentajeB) {
    this.porcentajeB = porcentajeB;
  }

  public String getSaldoActualA() {
    //return saldoActualA;
    return !miles? saldoActualA: (saldoActualA!= null? this.formatearNumero(Double.parseDouble(saldoActualA.replaceAll(",", ""))/1000, "#,##0"): saldoActualA);
  }

  public void setSaldoActualA(String saldoActualA) {
    this.saldoActualA = saldoActualA;
  }

  public String getSaldoActualB() {
    //return saldoActualB;
    return !miles? saldoActualB: (saldoActualB!= null? this.formatearNumero(Double.parseDouble(saldoActualB.replaceAll(",", ""))/1000, "#,##0") : saldoActualB);
  }

  public void setSaldoActualB(String saldoActualB) {
    this.saldoActualB = saldoActualB;
  }

  public String getSaldoAnteriorA() {
    //return saldoAnteriorA;
    return !miles? saldoAnteriorA: (saldoAnteriorA!= null? this.formatearNumero(Double.parseDouble(saldoAnteriorA.replaceAll(",", ""))/1000, "#,##0") : saldoAnteriorA);
  }

  public void setSaldoAnteriorA(String saldoAnteriorA) {
    this.saldoAnteriorA = saldoAnteriorA;
  }

  public String getSaldoAnteriorB() {
    //return saldoAnteriorB;
    return !miles? saldoAnteriorB: (saldoAnteriorB!= null? this.formatearNumero(Double.parseDouble(saldoAnteriorB.replaceAll(",", ""))/1000, "#,##0") : saldoAnteriorB);
  }

  public void setSaldoAnteriorB(String saldoAnteriorB) {
    this.saldoAnteriorB = saldoAnteriorB;
  }

  public String getVariacionA() {
    //return variacionA;
    return !miles? variacionA: (variacionA!= null? this.formatearNumero(Double.parseDouble(variacionA.replaceAll(",", ""))/1000, "#,##0") : variacionA);
  }

  public void setVariacionA(String variacionA) {
    this.variacionA = variacionA;
  }

  public String getVariacionB() {
    //return variacionB;
    return !miles? variacionB: (variacionB!= null? this.formatearNumero(Double.parseDouble(variacionB.replaceAll(",", ""))/1000, "#,##0") : variacionB);
  }

  public void setVariacionB(String variacionB) {
    this.variacionB = variacionB;
  }

  public boolean getLineaSaldoA() {
    return lineaSaldoA;
  }

  public void setLineaSaldoA(boolean lineaSaldoA) {
    this.lineaSaldoA = lineaSaldoA;
  }
  
  public boolean getLineaSaldoB() {
    return lineaSaldoB;
  }

  public void setLineaSaldoB(boolean lineaSaldoB) {
    this.lineaSaldoB = lineaSaldoB;
  }

  public boolean isLineaRenglonA() {
    return lineaRenglonA;
  }

  public void setLineaRenglonA(boolean lineaRenglonA) {
    this.lineaRenglonA = lineaRenglonA;
  }

  public boolean isLineaRenglonB() {
    return lineaRenglonB;
  }

  public void setLineaRenglonB(boolean lineaRenglonB) {
    this.lineaRenglonB = lineaRenglonB;
  }

  public boolean getMiles() {
    return miles;
  }

  public void setMiles(boolean miles) {
    this.miles = miles;
  }  

  //formato numeros....
  public String formatearNumero(double Valor, String Formato){
    NumberFormat Formateo= NumberFormat.getCurrencyInstance(Locale.US);
    if (Formateo instanceof DecimalFormat) {
      ((DecimalFormat) Formateo).setDecimalSeparatorAlwaysShown(true);
      ((DecimalFormat) Formateo).applyPattern(Formato);
    } // if
    return Formateo.format(Valor);
  }
  
}
