package sia.rf.contabilidad.registroContable.formas.servicios;

public class ElementoFormaContable {
  private String valor;
  private int nivel;

  public ElementoFormaContable(String valor, int nivel) {
    setValor(valor);
    setNivel(nivel);
  }

  public void setValor(String valor) {
    this.valor = valor;
  }

  public String getValor() {
    return valor;
  }

  public void setNivel(int nivel) {
    this.nivel = nivel;
  }

  public int getNivel() {
    return nivel;
  }
}
