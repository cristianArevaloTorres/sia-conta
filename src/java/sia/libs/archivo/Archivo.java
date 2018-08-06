package sia.libs.archivo;

import java.io.File;
import sia.libs.formato.Error;
 
public final class Archivo {
  
  public static void verificaRuta(String ruta){
    File dir = new File(ruta.substring(0,ruta.lastIndexOf(File.separator)));
    boolean exito = false;
    if (!(dir.exists())) {
      exito = dir.mkdirs();
    }
    if (!exito && !dir.exists()) {
      try {
        throw new Exception("No se pudo crear la ruta ".concat(ruta));
      }
      catch (Exception e){
        Error.mensaje(e, "SIAFM");  
      }
    }      
  }
}