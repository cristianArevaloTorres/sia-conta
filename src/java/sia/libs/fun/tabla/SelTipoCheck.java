package sia.libs.fun.tabla;

public class SelTipoCheck extends SelTipo {
  
  public SelTipoCheck(String value, String name) {
    this(value,name,null,null);
  }
  
  public SelTipoCheck(String value, String name, String campoCondicion, String valorCondicion) {
    super("checkbox",value,name, campoCondicion,valorCondicion);
  }
}
