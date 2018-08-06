package sia.scriptlets;

public class Firmante {

  private String nombreEmpleado;
  private String areaAdscripcion;
  
  public Firmante(String nombreEmpleado, String areaAdscripcion) {
    setNombreEmpleado(nombreEmpleado);
    setAreaAdscripcion(areaAdscripcion);
  }

  public void setNombreEmpleado(String nombreEmpleado) {
    this.nombreEmpleado = nombreEmpleado;
  }

  public String getNombreEmpleado() {
    return nombreEmpleado;
  }

  public void setAreaAdscripcion(String areaAdscripcion) {
    this.areaAdscripcion = areaAdscripcion;
  }

  public String getAreaAdscripcion() {
    return areaAdscripcion;
  }

}
