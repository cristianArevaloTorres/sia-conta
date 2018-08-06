package sia.rf.contabilidad.registroContableNuevo;

public class bcConfiguracionRenglon {
  
  private int nivel;
  private int longitud;
  private int posicion;
  
  public bcConfiguracionRenglon(int nivel, int longitud, int posicion) {
    setNivel(nivel);
    setLongitud(longitud);
    setPosicion(posicion);
  }

  public void setNivel(int nivel) {
    this.nivel = nivel;
  }

  public int getNivel() {
    return nivel;
  }

  public void setLongitud(int longitud) {
    this.longitud = longitud;
  }

  public int getLongitud() {
    return longitud;
  }

  public void setPosicion(int posicion) {
    this.posicion = posicion;
  }

  public int getPosicion() {
    return posicion;
  }
}
