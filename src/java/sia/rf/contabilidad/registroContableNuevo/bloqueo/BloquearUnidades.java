package sia.rf.contabilidad.registroContableNuevo.bloqueo;

public class BloquearUnidades {

  private String unidadEjecutora; 
  private String ambito;
  private String entidad; 
  private String programa; 
  
  public BloquearUnidades(String unidadEjecutora, String ambito, String entidad, String programa) {
    setUnidadEjecutora(unidadEjecutora);
    setAmbito(ambito);
    setEntidad(entidad);
    setPrograma(programa);
  }

  public void setUnidadEjecutora(String unidadEjecutora) {
    this.unidadEjecutora = unidadEjecutora;
  }

  public String getUnidadEjecutora() {
    return unidadEjecutora;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setPrograma(String programa) {
    this.programa = programa;
  }

  public String getPrograma() {
    return programa;
  }
}
