package sia.xml;

public class Modulo {
  
  private String descripcion;
  private String ruta;
  
  public Modulo(String descripcion, String ruta) {
    setDescripcion(descripcion);
    setRuta(ruta);
  }

  public void setDescripcion(String descripcion) {
    this.descripcion = descripcion;
  }

  public String getDescripcion() {
    return descripcion;
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }
}
