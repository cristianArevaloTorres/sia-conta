package sia.libs.formato;



public class Rango {

  private double min;
  private double max;

  public Rango() {
    this(0);
  }
  
  public Rango(double min) {
    this(min, min);
  }

  public Rango(double min, double max) {
    setMin(min);
    setMax(max);
  }

  private void setMin(double min) {
    this.min = min;
  }

  public double getMin() {
    return min;
  }

  private void setMax(double max) {
    this.max = max;
  }

  public double getMax() {
    return max;
  }
  
  public boolean isDentro(double valor) {
    return valor>= getMin() && valor<= getMax();
  }
  
  public boolean isRango() {
    return getMin()!= getMax();  
  }
  
  public String toString() {
    StringBuffer sb= new StringBuffer();
    sb.append("sia.libs.formato.Rango[");
    sb.append(getMin());
    sb.append(",");
    sb.append(getMax());
    sb.append("]");
    return sb.toString();
  }
  
}
