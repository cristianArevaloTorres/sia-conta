/*
 * Date of code 5/04/2008
 */

package sia.menu.outlook;

import java.io.InputStream;
import java.util.Properties;
import sia.libs.formato.Error;

/**
 *
 * @author alejandro.jimenez
 */
public class Outlook {

 
	private static Outlook instance;
	private static Object mutex = null;

  static { 
    mutex = new Object(); 
  }
 
  private Outlook() {
  }
  
  public static Outlook getInstance(){
		if (instance== null) {
			synchronized (mutex) {
				if (instance== null)
					instance= new Outlook();
			}
		}
		return instance;
	}  
   
  private Properties getProperties() { 
    InputStream is= getClass().getResourceAsStream("/outlook.properties");
    Properties properties= new Properties();
    try {
      properties.load(is);
      return properties;
    } 
    catch(Exception e) {
      Error.mensaje(e, "SIAFM", "Error: No puede leer el archivo de propiedades.\nEstar seguro que esta en el CLASSPATH outlook.properties");
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
