package sia.libs.archivo;

import java.io.File;
import java.util.Calendar;
import sia.libs.formato.Fecha;

public final class Patron {
  private Patron() {
  }

  public static void eliminarTemporalesDias(String ruta, int dias) {
    Eliminar.eliminarTemporales(ruta, getPatronDias(dias));
  } //eliminarTemporalesDias

  public static String getPatronDias(int dias) {
    Calendar elimiarDesde = Calendar.getInstance();
    elimiarDesde.set(Calendar.DAY_OF_MONTH, elimiarDesde.get(Calendar.DAY_OF_MONTH) - dias);
    return "xSia" + Fecha.formatear("yyyyMMdd", elimiarDesde.getTime());
  } //getPatronDias

  public static String[] listaArchivosSeleccionados(String ruta, String patron) {
    File directorio = new File(ruta);
    String[] list = directorio.list(new FileNameFiltro(patron));
    return list;
  } // listaArchivosSeleccionados
}
