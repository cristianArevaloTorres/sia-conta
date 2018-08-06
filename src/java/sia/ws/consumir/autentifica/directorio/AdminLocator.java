/**
 * AdminLocator.java
 *
 */

package sia.ws.consumir.autentifica.directorio;

public class AdminLocator extends org.apache.axis.client.Service implements sia.ws.consumir.autentifica.directorio.Admin {
  
  
  public AdminLocator() {
  }
    
  public AdminLocator(org.apache.axis.EngineConfiguration config) {
    super(config);
  }
  
  public AdminLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
    super(wsdlLoc, sName);
  }
  
  // Use to get a proxy class for AdminSoap
  private java.lang.String AdminSoap_address = "http://intranet.wapp.senado.gob.mx/ws/adminda/admin.asmx";
  
  public java.lang.String getAdminSoapAddress() {
    return AdminSoap_address;
  }
  
  // The WSDD service name defaults to the port name.
  private java.lang.String AdminSoapWSDDServiceName = "AdminSoap";

  
  public java.lang.String getAdminSoapWSDDServiceName() {
    return AdminSoapWSDDServiceName;
  }
  
  public void setAdminSoapWSDDServiceName(java.lang.String name) {
    AdminSoapWSDDServiceName = name;
  }
  
  public sia.ws.consumir.autentifica.directorio.AdminSoap getAdminSoap() throws javax.xml.rpc.ServiceException {
    java.net.URL endpoint;
    try {
      endpoint = new java.net.URL(AdminSoap_address);
    }
    catch (java.net.MalformedURLException e) {
      throw new javax.xml.rpc.ServiceException(e);
    }
    return getAdminSoap(endpoint);
  }
  
  public sia.ws.consumir.autentifica.directorio.AdminSoap getAdminSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
    try {
      sia.ws.consumir.autentifica.directorio.AdminSoapStub _stub = new sia.ws.consumir.autentifica.directorio.AdminSoapStub(portAddress, this);
      _stub.setPortName(getAdminSoapWSDDServiceName());
      return _stub;
    }
    catch (org.apache.axis.AxisFault e) {
      return null;
    }
  }
   
  public void setAdminSoapEndpointAddress(java.lang.String address) {
    AdminSoap_address = address;
  }
  
  /**
   * For the given interface, get the stub implementation.
   * If this service has no port for the given interface,
   * then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
    try {
      if (sia.ws.consumir.autentifica.directorio.AdminSoap.class.isAssignableFrom(serviceEndpointInterface)) {
        sia.ws.consumir.autentifica.directorio.AdminSoapStub _stub = new sia.ws.consumir.autentifica.directorio.AdminSoapStub(new java.net.URL(AdminSoap_address), this);
        _stub.setPortName(getAdminSoapWSDDServiceName());
        return _stub;
      }
    }
    catch (java.lang.Throwable t) {
      throw new javax.xml.rpc.ServiceException(t);
    }
    throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
  }
  
  /**
   * For the given interface, get the stub implementation.
   * If this service has no port for the given interface,
   * then ServiceException is thrown.
   */
  public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
    if (portName == null) {
      return getPort(serviceEndpointInterface);
    }
    java.lang.String inputPortName = portName.getLocalPart();
    if ("AdminSoap".equals(inputPortName)) {
      return getAdminSoap();
    }
    else  {
      java.rmi.Remote _stub = getPort(serviceEndpointInterface);
      ((org.apache.axis.client.Stub) _stub).setPortName(portName);
      return _stub;
    }
  }
  
  public javax.xml.namespace.QName getServiceName() {
    //http://intranetp.wapp.senado.gob.mx/ws/adminda/admin.asmx
    return new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/AdminDA", "Admin");
  }
  
  private java.util.HashSet ports = null;
  
  public java.util.Iterator getPorts() {
    if (ports == null) {
      ports = new java.util.HashSet();
      ports.add(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/AdminDA", "AdminSoap"));
    }
    return ports.iterator();
  }
  
  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {    
    if ("AdminSoap".equals(portName)) {
      setAdminSoapEndpointAddress(address);
    }
    else { // Unknown Port Name
      throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
    }
  }
  
  /**
   * Set the endpoint address for the specified port name.
   */
  public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
    setEndpointAddress(portName.getLocalPart(), address);
  }
  
}
