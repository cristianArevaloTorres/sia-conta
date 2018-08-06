package sia.libs.fun.tabla;

public class SelTipoRadio extends SelTipo {
  
  public SelTipoRadio(String value, String name) {
    this(value,name,null,null);
  }
  
  public SelTipoRadio(String value, String name, String campoCondicion, String valorCondicion) {
    super("radio",value,name, campoCondicion,valorCondicion);
  }
}
