package sia.rf.contabilidad.registroContable.servicios;

public class Rango extends sia.libs.formato.Rango {

  private String tipo;

  public Rango() {
    this(""); 
  }

  public Rango(double min) {
    this("", min); 
  }
  
  public Rango(String tipo) {
    this(tipo, 0D);
  }

  public Rango(String tipo, double min) {
    this(tipo, min, min);
  }

  public Rango(double min, double max) {
    this("", min, max);
  }
  
  public Rango(String tipo, double min, double max) {
    super(min, max);
    setTipo(tipo);
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }
  
  public boolean isDentro(String tipo, double valor) {
    return tipo.equalsIgnoreCase(tipo) && super.isDentro(valor);
  }

  public boolean isDentro(double valor) {
    return isDentro("", valor);
  }
  
}
