package sia.libs.fun.tabla;

public class CampoSelOrdena extends CampoSel {

  public CampoSelOrdena(int tamanoCol, String campo, String nombreCol, int alineacion, String formato, String ruta, String key, String parametros) {
    super(tamanoCol, campo, nombreCol, alineacion, formato, ruta, key, parametros);
    ordenado=true;
  }
  
  public CampoSelOrdena(String campo, String nombreCol, int alineacion, String formato, String ruta, String key, String parametros) {
    this(-1, campo, nombreCol, alineacion, formato, ruta, key, parametros);
  }
}
