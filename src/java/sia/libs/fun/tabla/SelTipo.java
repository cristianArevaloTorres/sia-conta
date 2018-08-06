package sia.libs.fun.tabla;

public class SelTipo {
  
  private String tipo;
  private String value;
  private String name;
  private String campoCondicion;
  private String valorCondicion;
  
  public SelTipo(String tipo, String value, String name, String campo, String valorValido) {
    setTipo(tipo);
    setValue(value);
    setName(name);
    setCampoCondicion(campo);
    setValorCondicion(valorValido);
  }
  
  public SelTipo(String tipo, String value, String name) {
    this(tipo,value,name,null,null);
  }

  public void setTipo(String tipo) {
    this.tipo = tipo;
  }

  public String getTipo() {
    return tipo;
  }

  public void setValue(String value) {
    this.value = value;
  }

  public String getValue() {
    return value;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }


  public void setCampoCondicion(String campoCondicion) {
    this.campoCondicion = campoCondicion;
  }

  public String getCampoCondicion() {
    return campoCondicion;
  }

  public void setValorCondicion(String valorCondicion) {
    this.valorCondicion = valorCondicion;
  }

  public String getValorCondicion() {
    return valorCondicion;
  }
}
