package sia.rf.contabilidad.registroContableNuevo;

import java.util.ArrayList;
import java.util.List;

public class bcConfiguracionGeneral {
  
  private int idCatalogoCuenta;
  private List<bcConfiguracionRenglon> configuracion;
  private int ejercicio;
  
  public bcConfiguracionGeneral(int idCatalogoCuenta, int ejercicio) {
    setIdCatalogoCuenta(idCatalogoCuenta);
    setEjercicio(ejercicio);
    inicializa();
  }
  
  private void inicializa() {
    configuracion = new ArrayList();
    configuracion.add(new bcConfiguracionRenglon(1,5,0));
    configuracion.add(new bcConfiguracionRenglon(2,4,5));
    configuracion.add(new bcConfiguracionRenglon(3,4,9));
    configuracion.add(new bcConfiguracionRenglon(4,4,13));
    configuracion.add(new bcConfiguracionRenglon(5,4,17));
    configuracion.add(new bcConfiguracionRenglon(6,4,21));
    configuracion.add(new bcConfiguracionRenglon(7,4,25));
    if(idCatalogoCuenta == 3 || ejercicio > 2010)
      configuracion.add(new bcConfiguracionRenglon(8,4,29));
  }
  
  public List<bcConfiguracionRenglon> getConfiguracion() {
    return configuracion;
  }

  public void setIdCatalogoCuenta(int idCatalogoCuenta) {
    this.idCatalogoCuenta = idCatalogoCuenta;
  }

  public int getIdCatalogoCuenta() {
    return idCatalogoCuenta;
  }
  
  public static synchronized List<String> separarClave(List<bcConfiguracionRenglon> conf, String clave, int nivel) {
    List<String> regresa = null;
    try  {
      
      regresa = new ArrayList();
      
      for (int i = 0; i < nivel; i++)  {
        regresa.add(clave.substring(conf.get(i).getPosicion() , conf.get(i).getPosicion() + conf.get(i).getLongitud()));
      }
      
    } catch (Exception ex)  {
      System.err.println("Error al separar la cuenta contable");
    } 
    
    return regresa;
  }
  
  public static synchronized List<String> separarClave(String configura, String clave, int nivel) {
    List<String> regresa = null;
    String[] conf = configura.split(",");
    int tamIni=0;
    try  {
      
      regresa = new ArrayList();
      
      for (int i = 0; i < nivel; i++)  {
        regresa.add(clave.substring(tamIni, Integer.parseInt(conf[i]) + tamIni ));
        tamIni = tamIni + Integer.parseInt(conf[i]);
      }
      
    } catch (Exception ex)  {
      System.err.println("Error al separar la cuenta contable");
    } 
    
    return regresa;
  }

  public void setEjercicio(int ejercicio) {
    this.ejercicio = ejercicio;
  }

  public int getEjercicio() {
    return ejercicio;
  }
}
