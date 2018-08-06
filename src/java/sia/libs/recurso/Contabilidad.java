/*
 * Clase: Contabilidad.java
 *
 * Creado: 10 de diciembre de 2007, 14:17 PM
 *
 * Write by: alejandro.jimenez
 */

package sia.libs.recurso;

import java.io.InputStream;
import java.util.Properties;

import sia.libs.formato.Error;

public class Contabilidad {
  
  private static final String NOMBRE= "contabilidad";
  
	private static Contabilidad instance; 
	private static Object mutex = null;
  private Properties properties;

  static { 
    mutex = new Object(); 
  }
 
  private Contabilidad() {
    setProperties(loadProperties());
  }
  
  public static Contabilidad getInstance(){
		if (instance== null) {
			synchronized (mutex) {
				if (instance== null)
					instance= new Contabilidad();
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
      Error.mensaje(e, "SIAFM", "Error: No puede leer el archivo de propiedades.\nEstar seguro que esta en el CLASSPATH ".concat("contabilidad").concat(".properties"));
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
