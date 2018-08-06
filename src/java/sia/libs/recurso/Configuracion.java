package sia.libs.recurso;


import java.io.InputStream;

import java.util.Properties;

import sia.libs.formato.Error;

public class Configuracion {

  private static final String NOMBRE = "configuracion";

  private static Configuracion instance;
  private static Object mutex = null;
  private Properties properties;

  static {
    mutex=new Object();
  }

  private Configuracion() {
    setProperties(loadProperties());
  }

  public static Configuracion getInstance() {
    if (instance == null) {
      synchronized (mutex) {
        if (instance == null)
          instance=new Configuracion();
      }
    }
    return instance;
  }

  private Properties loadProperties() {
    InputStream is = this.getClass().getResourceAsStream("/".concat(NOMBRE).concat(".properties"));
    Properties properties = new Properties();
    try {
      properties.load(is);
      return properties;
    }
    catch (Exception e) {
      Error.mensaje(e,"Estar seguro que esta en el CLASSPATH ".concat(NOMBRE).concat(".properties"));
    }
    return null;
  }

  public static String getPropiedad(String id) {
    String regresar = null;
    try {
      regresar=getInstance().getProperties().getProperty(id);
    }
    catch (Exception e) {
      Error.mensaje(e,"Error al obtener la propiedad");
      regresar="";
    }
    return regresar;
  }

  public static int getPropiedadInt(String id) {
    int regresar = 0;
    String value = null;
    try {
      value=getInstance().getProperties().getProperty(id);
      regresar=Integer.parseInt(value);
    }
    catch (Exception e) {
      Error.mensaje(e,"Error al obtener la propiedad");
      regresar=0;
    }
    return regresar;
  }

  public static byte getPropiedadByte(String id) {
    byte regresar = 0;
    String value = null;
    try {
      value=getInstance().getProperties().getProperty(id);
      regresar=(byte)value.toCharArray()[0];
    }
    catch (Exception e) {
      Error.mensaje(e,"Error al obtener la propiedad");
      regresar=0;
    }
    return regresar;
  }

  public static double getPropiedadDouble(String id) {
    double regresar = 0;
    String value = null;
    try {
      value=getInstance().getProperties().getProperty(id);
      regresar=Double.parseDouble(value);
    }
    catch (Exception e) {
      Error.mensaje(e,"Error al obtener la propiedad");
    }
    return regresar;
  }

  public static String getPropiedad(String secion, String id) {
    return getPropiedad(secion.concat(".").concat(id));
  }

  public void finalize() {
    instance.getProperties().clear();
    instance=null;
    mutex=null;
  }

  private void setProperties(Properties properties) {
    this.properties=properties;
  }

  private Properties getProperties() {
    return properties;
  }

  public String getPropiedadServidor(String id) {
    try {
      String servidor = getProperties().getProperty("sistema.servidor");
      id=id.concat(".").concat(servidor);
      return getProperties().getProperty(id);
    }
    catch (Exception e) {
      Error.mensaje(e, "No pudeo leer la propiedad ".concat(id).concat(". !"));
    } // try
    return null;
  }

  public static void main(String... args) {
    String dbName = Configuracion.getInstance().getPropiedad("sistema.bd.type");
    System.out.println(Configuracion.getInstance().getPropiedad("sistema.bd.".concat(dbName).concat(".jndi")));
    System.out.println(Configuracion.getInstance().getPropiedadServidor("sistema.bd.type"));
  }

}
