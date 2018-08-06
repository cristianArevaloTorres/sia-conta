/**
 * ActiveDirectory.java
 */

package sia.ws.consumir.autentifica.directorio;

public class ActiveDirectory implements java.io.Serializable {
  
  private String claveMov;
  private String nombre;
  private String apellido;
  private String curp;
  private String cargo;
  private String clavePuesto;
  private String nivelPuesto;
  private String puesto;
  private String numEmpleado;
  private String objPrueba;
  private String cveAplicacion;
  
  
  public ActiveDirectory() {
  }
  
  public ActiveDirectory(String claveMov, String nombre, String apellido, String curp, String cargo, String clavePuesto, String nivelPuesto, String puesto, String numEmpleado, String objPrueba, String cveAplicacion) {
    this.claveMov = claveMov;
    this.nombre = nombre;
    this.apellido = apellido;
    this.curp = curp;
    this.cargo = cargo;
    this.clavePuesto = clavePuesto;
    this.nivelPuesto = nivelPuesto;
    this.puesto = puesto;    
    this.numEmpleado = numEmpleado;
    this.objPrueba = objPrueba;
    this.cveAplicacion = cveAplicacion;
  }
  
  
  public java.lang.String getClaveMov() {
    return claveMov;
  }
  
  public void setClaveMov(java.lang.String claveMov) {
    this.claveMov = claveMov;
  }
  
  public java.lang.String getNumEmpleado() {
    return numEmpleado;
  }
  
  public void setNumEmpleado(java.lang.String numEmpleado) {
    this.numEmpleado = numEmpleado;
  }
  
  public java.lang.String getCveAplicacion() {
    return cveAplicacion;
  }
  
  public void setCveAplicacion(java.lang.String cveAplicacion) {
    this.cveAplicacion = cveAplicacion;
  }
  
  public java.lang.String getNombre() {
    return nombre;
  }
  
  public void setNombre(java.lang.String nombre) {
    this.nombre = nombre;
  }
  
  public java.lang.String getApellido() {
    return apellido;
  }
  
  public void setApellido(java.lang.String apellido) {
    this.apellido = apellido;
  }
  
  public java.lang.String getCurp() {
    return curp;
  }
  
  public void setCurp(java.lang.String curp) {
    this.curp = curp;
  }
  
  public java.lang.String getCargo() {
    return cargo;
  }
  
  public void setCargo(java.lang.String cargo) {
    this.cargo = cargo;
  }
  
  public java.lang.String getClavePuesto() {
    return clavePuesto;
  }
  
  public void setClavePuesto(java.lang.String clavePuesto) {
    this.clavePuesto = clavePuesto;
  }
  
  public java.lang.String getNivelPuesto() {
    return nivelPuesto;
  }
  
  public void setNivelPuesto(java.lang.String nivelPuesto) {
    this.nivelPuesto = nivelPuesto;
  }
  
  public java.lang.String getPuesto() {
    return puesto;
  }
  
  public void setPuesto(java.lang.String puesto) {
    this.puesto = puesto;
  }
  
  public java.lang.String getObjPrueba() {
    return objPrueba;
  }
  
  public void setObjPrueba(java.lang.String objPrueba) {
    this.objPrueba = objPrueba;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ActiveDirectory))
      return false;
    ActiveDirectory other = (ActiveDirectory) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null)
      return (__equalsCalc == obj);
    
    __equalsCalc = obj;
    boolean _equals;
    
    _equals = true
    && ((this.claveMov==null && other.getClaveMov()==null) || (this.claveMov!=null && this.claveMov.equals(other.getClaveMov())))
    && ((this.nombre==null && other.getNombre()==null) || (this.nombre!=null && this.nombre.equals(other.getNombre())))
    && ((this.apellido==null && other.getApellido()==null) || (this.apellido!=null && this.apellido.equals(other.getApellido())))
    && ((this.curp==null && other.getCurp()==null) || (this.curp!=null && this.curp.equals(other.getCurp())))
    && ((this.cargo==null && other.getCargo()==null) || (this.cargo!=null && this.cargo.equals(other.getCargo())))
    && ((this.clavePuesto==null && other.getClavePuesto()==null) || (this.clavePuesto!=null && this.clavePuesto.equals(other.getClavePuesto())))
    && ((this.nivelPuesto==null && other.getNivelPuesto()==null) || (this.nivelPuesto!=null && this.nivelPuesto.equals(other.getNivelPuesto())))
    && ((this.puesto==null && other.getPuesto()==null) || (this.puesto!=null && this.puesto.equals(other.getPuesto())))
    && ((this.numEmpleado==null && other.getNumEmpleado()==null) || (this.numEmpleado!=null && this.numEmpleado.equals(other.getNumEmpleado())))
    && ((this.objPrueba==null && other.getObjPrueba()==null) || (this.objPrueba!=null && this.objPrueba.equals(other.getObjPrueba())))
    && ((this.cveAplicacion==null && other.getCveAplicacion()==null) || (this.cveAplicacion!=null && this.cveAplicacion.equals(other.getCveAplicacion())))
    ;
    __equalsCalc = null;
    return _equals;
  }
  
  private boolean __hashCodeCalc = false;
  
  public synchronized int hashCode() {
    if (__hashCodeCalc)
      return 0;

    __hashCodeCalc = true;
    int _hashCode = 1;
    
    if (getClaveMov() != null)
      _hashCode += getClaveMov().hashCode();
    if (getNombre() != null)
      _hashCode += getNombre().hashCode();
    if (getApellido() != null)
      _hashCode += getApellido().hashCode();
    if (getCurp() != null)
      _hashCode += getCurp().hashCode();
    if (getCargo() != null)
      _hashCode += getCargo().hashCode();
    if (getClavePuesto() != null)
      _hashCode += getClavePuesto().hashCode();
    if (getNivelPuesto() != null)
      _hashCode += getNivelPuesto().hashCode();
    if (getPuesto() != null)
      _hashCode += getPuesto().hashCode();
    
    if (getNumEmpleado() != null)
      _hashCode += getNumEmpleado().hashCode();
    if (getObjPrueba() != null)
      _hashCode += getObjPrueba().hashCode();    
    if (getCveAplicacion() != null)
      _hashCode += getCveAplicacion().hashCode();
    
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ActiveDirectory.class, true);
  
  static {
    //http://intranetp.wapp.senado.gob.mx/ws/adminda
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", ">ActiveDirectory"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    //parametros
    elemField.setFieldName("claveMov");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "claveMov"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("nombre");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "nombre"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("apellido");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "apellido"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("curp");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "curp"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("cargo");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "cargo"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("clavePuesto");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "clavePuesto"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("nivelPuesto");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "nivelPuesto"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("puesto");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "puesto"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("numEmpleado");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "numEmpleado"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("objPrueba");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "objPrueba"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);    
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("cveAplicacion");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "cveAplicacion"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }
  
  /**
   * Return type metadata object
   */
  public static org.apache.axis.description.TypeDesc getTypeDesc() {
    return typeDesc;
  }
  
  /**
   * Get Custom Serializer
   */
  public static org.apache.axis.encoding.Serializer getSerializer(String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
  }
  
  /**
   * Get Custom Deserializer
   */
  public static org.apache.axis.encoding.Deserializer getDeserializer(String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
  }
    
}
