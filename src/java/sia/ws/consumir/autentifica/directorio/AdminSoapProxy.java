package sia.ws.consumir.autentifica.directorio;

public class AdminSoapProxy implements sia.ws.consumir.autentifica.directorio.AdminSoap {
  private String _endpoint = null;
  private sia.ws.consumir.autentifica.directorio.AdminSoap adminSoap = null;
  
  public AdminSoapProxy() {
    _initAdminSoapProxy();
  }
  
  private void _initAdminSoapProxy() {
    try {
      adminSoap = (new sia.ws.consumir.autentifica.directorio.AdminLocator()).getAdminSoap();
      if (adminSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)adminSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)adminSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (adminSoap != null)
      ((javax.xml.rpc.Stub)adminSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public sia.ws.consumir.autentifica.directorio.AdminSoap getAdminSoap() {
    if (adminSoap == null)
      _initAdminSoapProxy();
    return adminSoap;
  }
  
  //metodos...    
  public java.lang.String activeDirectory(String claveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado, String objPrueba, String cveAplicacion) throws java.rmi.RemoteException{
    if (adminSoap == null)
      _initAdminSoapProxy();
    return adminSoap.activeDirectory(claveMov, nombre, apellido, curp, cargo, clavePuesto, nivelPuesto, puesto, numEmpleado, objPrueba, cveAplicacion);
  }
  
}