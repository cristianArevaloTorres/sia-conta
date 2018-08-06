/**
 * LDAPSoapStub.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class LDAPSoapStub extends org.apache.axis.client.Stub implements sia.ws.consumir.autentifica.directorio.LDAPSoap {  
  private java.util.Vector cachedSerClasses = new java.util.Vector();
  private java.util.Vector cachedSerQNames = new java.util.Vector();
  private java.util.Vector cachedSerFactories = new java.util.Vector();
  private java.util.Vector cachedDeserFactories = new java.util.Vector();
  
  static org.apache.axis.description.OperationDesc [] _operations;
  
  static {
    _operations = new org.apache.axis.description.OperationDesc[8]; //nums Metodo
    _initOperationDesc1();
  }
  
  private static void _initOperationDesc1(){
    org.apache.axis.description.OperationDesc oper;
    org.apache.axis.description.ParameterDesc param;
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("Autenticar");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "dominio"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "grupo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">>AutenticarResponse>AutenticarResult"));
    oper.setReturnClass(sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "AutenticarResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[0] = oper;
    
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("AutenticarGrupo");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "grupo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">>AutenticarGrupoResponse>AutenticarGrupoResult"));
    oper.setReturnClass(sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "AutenticarGrupoResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[1] = oper;
    
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("ObtenerCURP");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerCURPResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[2] = oper;
    
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("ObtenerNumEmpleado");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerNumEmpleadoResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[3] = oper;
    
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("PerteneceGrupo");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "grupo"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "PerteneceGrupoResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[4] = oper;
    
    //wakko...
    
    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("ObtenerUsrCuenta");
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "numEmpleado"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "cveAplicacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrCuentaResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[5] = oper;

    oper = new org.apache.axis.description.OperationDesc();
    oper.setName("ObtenerUsrAtributo");
    //parametros
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "opcion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "atributoObtener"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "atributoFiltro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "valorFiltro"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    param = new org.apache.axis.description.ParameterDesc(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "cveAplicacion"), org.apache.axis.description.ParameterDesc.IN, new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"), java.lang.String.class, false, false);
    param.setOmittable(true);
    oper.addParameter(param);
    //Return type
    oper.setReturnType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    oper.setReturnClass(java.lang.String.class);
    oper.setReturnQName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrAtributoResult"));
    oper.setStyle(org.apache.axis.constants.Style.WRAPPED);
    oper.setUse(org.apache.axis.constants.Use.LITERAL);
    _operations[6] = oper;   
    
  }
  
  public LDAPSoapStub() throws org.apache.axis.AxisFault {
    this(null);
  }
  
  public LDAPSoapStub(java.net.URL endpointURL, javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
    this(service);
    super.cachedEndpoint = endpointURL;
  }
  
  public LDAPSoapStub(javax.xml.rpc.Service service) throws org.apache.axis.AxisFault {
    if (service == null) {
      super.service = new org.apache.axis.client.Service();
    } else {
      super.service = service;
    }
    ((org.apache.axis.client.Service)super.service).setTypeMappingVersion("1.2");
    java.lang.Class cls;
    javax.xml.namespace.QName qName;
    javax.xml.namespace.QName qName2;
    java.lang.Class beansf = org.apache.axis.encoding.ser.BeanSerializerFactory.class;
    java.lang.Class beandf = org.apache.axis.encoding.ser.BeanDeserializerFactory.class;
    java.lang.Class enumsf = org.apache.axis.encoding.ser.EnumSerializerFactory.class;
    java.lang.Class enumdf = org.apache.axis.encoding.ser.EnumDeserializerFactory.class;
    java.lang.Class arraysf = org.apache.axis.encoding.ser.ArraySerializerFactory.class;
    java.lang.Class arraydf = org.apache.axis.encoding.ser.ArrayDeserializerFactory.class;
    java.lang.Class simplesf = org.apache.axis.encoding.ser.SimpleSerializerFactory.class;
    java.lang.Class simpledf = org.apache.axis.encoding.ser.SimpleDeserializerFactory.class;
    java.lang.Class simplelistsf = org.apache.axis.encoding.ser.SimpleListSerializerFactory.class;
    
    java.lang.Class simplelistdf = org.apache.axis.encoding.ser.SimpleListDeserializerFactory.class;
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">>AutenticarGrupoResponse>AutenticarGrupoResult");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">>AutenticarResponse>AutenticarResult");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">AutenticarGrupo");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.AutenticarGrupo.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">AutenticarGrupoResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponse.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerCURP");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerCURP.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerCURPResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerCURPResponse.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerNumEmpleado");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerNumEmpleado.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerNumEmpleadoResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerNumEmpleadoResponse.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">PerteneceGrupo");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.PerteneceGrupo.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">PerteneceGrupoResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.PerteneceGrupoResponse.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    //wakko...    
    
    //ObtenerUsrCuenta
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrCuenta");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerUsrCuenta.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrCuentaResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerUsrCuentaResponse.class;
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    //ObtenerUsrAtributo
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrAtributo");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerUsrCuenta.class; //mod
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);
    
    qName = new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrAtributoResponse");
    cachedSerQNames.add(qName);
    cls = sia.ws.consumir.autentifica.directorio.ObtenerUsrCuentaResponse.class; //mod
    cachedSerClasses.add(cls);
    cachedSerFactories.add(beansf);
    cachedDeserFactories.add(beandf);    
  }
  
  protected org.apache.axis.client.Call createCall() throws java.rmi.RemoteException {
    try {
      org.apache.axis.client.Call _call = super._createCall();
      if (super.maintainSessionSet) {
        _call.setMaintainSession(super.maintainSession);
      }
      if (super.cachedUsername != null) {
        _call.setUsername(super.cachedUsername);
      }
      if (super.cachedPassword != null) {
        _call.setPassword(super.cachedPassword);
      }
      if (super.cachedEndpoint != null) {
        _call.setTargetEndpointAddress(super.cachedEndpoint);
      }
      if (super.cachedTimeout != null) {
        _call.setTimeout(super.cachedTimeout);
      }
      if (super.cachedPortName != null) {
        _call.setPortName(super.cachedPortName);
      }
      java.util.Enumeration keys = super.cachedProperties.keys();
      while (keys.hasMoreElements()) {
        java.lang.String key = (java.lang.String) keys.nextElement();
        _call.setProperty(key, super.cachedProperties.get(key));
      }
      // All the type mapping information is registered
      // when the first call is made.
      // The type mapping information is actually registered in
      // the TypeMappingRegistry of the service, which
      // is the reason why registration is only needed for the first call.
      synchronized (this) {
        if (firstCall()) {
          // must set encoding style before registering serializers
          _call.setEncodingStyle(null);
          for (int i = 0; i < cachedSerFactories.size(); ++i) {
            java.lang.Class cls = (java.lang.Class) cachedSerClasses.get(i);
            javax.xml.namespace.QName qName =
            (javax.xml.namespace.QName) cachedSerQNames.get(i);
            java.lang.Object x = cachedSerFactories.get(i);
            if (x instanceof Class) {
              java.lang.Class sf = (java.lang.Class)
              cachedSerFactories.get(i);
              java.lang.Class df = (java.lang.Class)
              cachedDeserFactories.get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            }
            else if (x instanceof javax.xml.rpc.encoding.SerializerFactory) {
              org.apache.axis.encoding.SerializerFactory sf = (org.apache.axis.encoding.SerializerFactory)
              cachedSerFactories.get(i);
              org.apache.axis.encoding.DeserializerFactory df = (org.apache.axis.encoding.DeserializerFactory)
              cachedDeserFactories.get(i);
              _call.registerTypeMapping(cls, qName, sf, df, false);
            }
          }
        }
      }
      return _call;
    }
    catch (java.lang.Throwable _t) {
      throw new org.apache.axis.AxisFault("Failure trying to get the Call object", _t);
    }
  }
  
  public sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult autenticar(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String dominio, java.lang.String grupo) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[0]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/Autenticar");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "Autenticar"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {        java.lang.Object _resp = _call.invoke(new java.lang.Object[] {loginUsr, passUsr, dominio, grupo});
    
    if (_resp instanceof java.rmi.RemoteException) {
      throw (java.rmi.RemoteException)_resp;
    }
    else {
      extractAttachments(_call);
      try {
        return (sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult) _resp;
      } catch (java.lang.Exception _exception) {
        return (sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult) org.apache.axis.utils.JavaUtils.convert(_resp, sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult.class);
      }
    }
    } catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
  public sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult autenticarGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[1]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/AutenticarGrupo");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "AutenticarGrupo"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {loginUsr, passUsr, grupo});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult) org.apache.axis.utils.JavaUtils.convert(_resp, sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
  public java.lang.String obtenerCURP(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[2]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ObtenerCURP");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerCURP"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {loginUsr, passUsr});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
  public java.lang.String obtenerNumEmpleado(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[3]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ObtenerNumEmpleado");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerNumEmpleado"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {loginUsr, passUsr});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
  public java.lang.String perteneceGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[4]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/PerteneceGrupo");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "PerteneceGrupo"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {loginUsr, passUsr, grupo});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
  //wakko...
  public java.lang.String obtenerUsrCuenta(java.lang.String numEmpleado, java.lang.String cveAplicacion) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[5]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ObtenerUsrCuenta");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrCuenta"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {numEmpleado, cveAplicacion});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }

  public java.lang.String obtenerUsrAtributo(String opcion, String loginUsr, String passUsr, String atributoObtener, String atributoFiltro, String valorFiltro, String cveAplicacion) throws java.rmi.RemoteException {
    if (super.cachedEndpoint == null) {
      throw new org.apache.axis.NoEndPointException();
    }
    org.apache.axis.client.Call _call = createCall();
    _call.setOperation(_operations[6]);
    _call.setUseSOAPAction(true);
    _call.setSOAPActionURI("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion/ObtenerUsrAtributo");
    _call.setEncodingStyle(null);
    _call.setProperty(org.apache.axis.client.Call.SEND_TYPE_ATTR, Boolean.FALSE);
    _call.setProperty(org.apache.axis.AxisEngine.PROP_DOMULTIREFS, Boolean.FALSE);
    _call.setSOAPVersion(org.apache.axis.soap.SOAPConstants.SOAP11_CONSTANTS);
    _call.setOperationName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrAtributo"));
    
    setRequestHeaders(_call);
    setAttachments(_call);
    try {
      java.lang.Object _resp = _call.invoke(new java.lang.Object[] {opcion, loginUsr, passUsr, atributoObtener, atributoFiltro, valorFiltro, cveAplicacion});
      
      if (_resp instanceof java.rmi.RemoteException) {
        throw (java.rmi.RemoteException)_resp;
      }
      else {
        extractAttachments(_call);
        try {
          return (java.lang.String) _resp;
        }
        catch (java.lang.Exception _exception) {
          return (java.lang.String) org.apache.axis.utils.JavaUtils.convert(_resp, java.lang.String.class);
        }
      }
    }
    catch (org.apache.axis.AxisFault axisFaultException) {
      throw axisFaultException;
    }
  }
  
}
