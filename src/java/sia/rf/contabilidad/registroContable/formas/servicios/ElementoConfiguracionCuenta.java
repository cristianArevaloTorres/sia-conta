package sia.rf.contabilidad.registroContable.formas.servicios;

public class ElementoConfiguracionCuenta {
  private int orden;
  private int tamanio;
  private String caracter;

  public ElementoConfiguracionCuenta(int orden, int tamanio, String caracter) {
    setOrden(orden);
    setTamanio(tamanio);
    setCaracter(caracter);
  }

  public void setOrden(int orden) {
    this.orden = orden;
  }

  public int getOrden() {
    return orden;
  }

  public void setTamanio(int tamanio) {
    this.tamanio = tamanio;
  }

  public int getTamanio() {
    return tamanio;
  }

  public void setCaracter(String caracter) {
    this.caracter = caracter;
  }

  public String getCaracter() {
    return caracter;
  }
}
