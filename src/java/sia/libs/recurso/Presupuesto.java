package sia.libs.recurso;

import java.io.InputStream;
import java.util.Properties;
import sia.libs.formato.Error;

public class Presupuesto {
  private static final String NOMBRE= "presupuesto";
  
  private static Presupuesto instance; 
  private static Object mutex = null;
  private Properties properties;

  static { 
    mutex = new Object(); 
  }
  
  private Presupuesto() {
    setProperties(loadProperties());
  }
  
  public static Presupuesto getInstance(){
    if (instance== null) {
      synchronized (mutex) {
        if (instance== null)
          instance= new Presupuesto();
      }
    }
    return instance;
  }  
   
  private Properties loadProperties() { 
    InputStream is= this.getClass().getResourceAsStream("/".concat(NOMBRE).concat(".properties"));
    Properties properties= new Properties();
    try {
      properties.load(is);
      return properties;
    } 
    catch(Exception e) {
      Error.mensaje(e, "SIAFM", "Error: No puede leer el archivo de propiedades.\nEstar seguro que esta en el CLASSPATH ".concat(NOMBRE).concat(".properties"));
    }    
    return null;
  }
  
  public static String getPropiedad(String id) {
    String regresar= null;
    try {
      regresar= getInstance().getProperties().getProperty(id);
    } 
    catch(Exception e) {
      Error.mensaje(e, "SIAFM", "Error: No pudeo leer la propiedad ".concat(id).concat(". !"));
      regresar= "";
    }    
    return regresar;
  }

  public static String getPropiedad(String secion, String id) {
    return getPropiedad(secion.concat(".").concat(id)); 
  }
  
  public void finalize() {
    instance.getProperties().clear();
    instance= null;
    mutex   = null;
  }

  private void setProperties(Properties properties) {
    this.properties = properties;
  }

  private Properties getProperties() {
    return properties;
  }
  
  
}
