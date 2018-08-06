/*
 * Clase: Propiedades.java
 *
 * Creado: 2 de junio de 2007, 12:57 PM
 *
 * Write by: alejandro.jimenez
 */

package sia.libs.recurso;

import java.io.InputStream;
import java.util.Properties;
import sia.libs.formato.Error;

public class Propiedades {
  
	private static Propiedades instance;
	private static Object mutex = null;

  static { 
    mutex = new Object(); 
  }
 
  private Propiedades() {
  }
  
  public static Propiedades getInstance(){
		if (instance== null) {
			synchronized (mutex) {
				if (instance== null)
					instance= new Propiedades();
			}
		}
		return instance;
	}  
   
  private Properties getProperties() { 
    InputStream is= getClass().getResourceAsStream("/sia.properties");
    Properties properties= new Properties();
    try {
      properties.load(is);
      return properties;
    } 
    catch(Exception e) {
      Error.mensaje(e, "SIAFM", "Error: No puede leer el archivo de propiedades.\nEstar seguro que esta en el CLASSPATH sia.properties");
    }    
    return null;
  }
  
  public String getPropiedad(String id) {
    Properties properties= getProperties();
    try {
      return properties.getProperty(id);
    } 
    catch(Exception e) {
      Error.mensaje(e, "SIAFM", "Error: No pudeo leer la propiedad ".concat(id).concat(". !"));
    }    
    return null;
  }
  
  public void finalize() {
    instance.getProperties().clear();
    instance= null;
    mutex   = null;
  }
  
}
