/**
 * Admin.java
 *
 */

package sia.ws.consumir.autentifica.directorio;

public interface Admin extends javax.xml.rpc.Service {
  
  public java.lang.String getAdminSoapAddress();
  
  public sia.ws.consumir.autentifica.directorio.AdminSoap getAdminSoap() throws javax.xml.rpc.ServiceException;
  
  public sia.ws.consumir.autentifica.directorio.AdminSoap getAdminSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
