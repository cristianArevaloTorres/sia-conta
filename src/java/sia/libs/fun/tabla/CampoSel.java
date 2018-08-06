package sia.libs.fun.tabla;

public class CampoSel extends Campo {
  
  private String ruta;
  private String key;
  private String parametros;
  
  public CampoSel(int tamanoCol, String campo, String nombreCol, int alineacion, String formato, String ruta, String key, String parametros) {
    super(tamanoCol, campo, nombreCol, alineacion, formato);
    setRuta(ruta);
    setKey(key);
    setParametros(parametros);
  }
  
  public CampoSel(String campo, String nombreCol, int alineacion, String formato, String ruta, String key, String parametros) {
    this(-1, campo, nombreCol, alineacion, formato, ruta, key, parametros);
  }

  public void setRuta(String ruta) {
    this.ruta = ruta;
  }

  public String getRuta() {
    return ruta;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getKey() {
    return key;
  }

  public void setParametros(String parametros) {
    this.parametros = parametros;
  }

  public String getParametros() {
    return parametros;
  }
}
