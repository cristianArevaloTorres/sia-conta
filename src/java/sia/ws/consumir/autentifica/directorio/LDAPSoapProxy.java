package sia.ws.consumir.autentifica.directorio;

public class LDAPSoapProxy implements sia.ws.consumir.autentifica.directorio.LDAPSoap {
  private String _endpoint = null;
  private sia.ws.consumir.autentifica.directorio.LDAPSoap lDAPSoap = null;
  
  public LDAPSoapProxy() {
    _initLDAPSoapProxy();
  }
  
  private void _initLDAPSoapProxy() {
    try {
      lDAPSoap = (new sia.ws.consumir.autentifica.directorio.LDAPLocator()).getLDAPSoap();
      if (lDAPSoap != null) {
        if (_endpoint != null)
          ((javax.xml.rpc.Stub)lDAPSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
        else
          _endpoint = (String)((javax.xml.rpc.Stub)lDAPSoap)._getProperty("javax.xml.rpc.service.endpoint.address");
      }
      
    }
    catch (javax.xml.rpc.ServiceException serviceException) {}
  }
  
  public String getEndpoint() {
    return _endpoint;
  }
  
  public void setEndpoint(String endpoint) {
    _endpoint = endpoint;
    if (lDAPSoap != null)
      ((javax.xml.rpc.Stub)lDAPSoap)._setProperty("javax.xml.rpc.service.endpoint.address", _endpoint);
    
  }
  
  public sia.ws.consumir.autentifica.directorio.LDAPSoap getLDAPSoap() {    
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap;
  }
  
  public sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult autenticar(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String dominio, java.lang.String grupo) throws java.rmi.RemoteException{    
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.autenticar(loginUsr, passUsr, dominio, grupo);
  }
  
  public sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult autenticarGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.autenticarGrupo(loginUsr, passUsr, grupo);    
  }

  
  public java.lang.String obtenerCURP(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.obtenerCURP(loginUsr, passUsr);
  }
  
  public java.lang.String obtenerNumEmpleado(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.obtenerNumEmpleado(loginUsr, passUsr);
  }
  
  public java.lang.String perteneceGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.perteneceGrupo(loginUsr, passUsr, grupo);
  }

  //wakko...
  public java.lang.String obtenerUsrCuenta(java.lang.String numEmpleado, java.lang.String cveAplicacion) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.obtenerUsrCuenta(numEmpleado, cveAplicacion);    
  }

  public java.lang.String obtenerUsrAtributo(String opcion, String loginUsr, String passUsr, String atributoObtener, String atributoFiltro, String valorFiltro, String cveAplicacion) throws java.rmi.RemoteException{
    if (lDAPSoap == null)
      _initLDAPSoapProxy();
    return lDAPSoap.obtenerUsrAtributo(opcion, loginUsr, passUsr, atributoObtener, atributoFiltro, valorFiltro, cveAplicacion);
  }
  
}