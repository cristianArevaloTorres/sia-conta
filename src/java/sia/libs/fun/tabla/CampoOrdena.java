package sia.libs.fun.tabla;

public class CampoOrdena extends Campo {
  public CampoOrdena(String campo, String nombreCol, int alineacion) {
    this(campo, nombreCol, alineacion, null);
  }
  
  public CampoOrdena(String campo, String nombreCol) {
    this(campo, nombreCol, -1, null);
  }
  
  public CampoOrdena(String campo, String nombreCol, String formato) {
    this(campo, nombreCol, -1, formato);
  }
  
  public CampoOrdena(String campo, String nombreCol, int alineacion, String formato) {
    this(-1, campo, nombreCol, alineacion, formato);
  }
  
  public CampoOrdena(int tamanoCol, String campo, String nombreCol, int alineacion, String formato) {
    super(tamanoCol, campo, nombreCol, alineacion, formato);
    ordenado=true;
  }
}
