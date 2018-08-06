/**
 * AdminSoap.java
 *
 */

package sia.ws.consumir.autentifica.directorio;

public interface AdminSoap extends java.rmi.Remote {
  
  /**
   * Crea un usuario dentro del DA
   */
  
  public java.lang.String activeDirectory(String claveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado, String objPrueba, String cveAplicacion) throws java.rmi.RemoteException;  
    
}
