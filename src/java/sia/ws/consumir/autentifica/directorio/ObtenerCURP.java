/**
 * ObtenerCURP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class ObtenerCURP  implements java.io.Serializable {
  
  private java.lang.String loginUsr;  
  private java.lang.String passUsr;
  
  public ObtenerCURP() {
  }
  
  public ObtenerCURP(java.lang.String loginUsr, java.lang.String passUsr) {
    this.loginUsr = loginUsr;
    this.passUsr = passUsr;
  }  
  
  /**
   * Gets the loginUsr value for this ObtenerCURP.
   *
   * @return loginUsr
   */
  public java.lang.String getLoginUsr() {
    return loginUsr;
  }
  
  
  /**
   * Sets the loginUsr value for this ObtenerCURP.
   *
   * @param loginUsr
   */
  public void setLoginUsr(java.lang.String loginUsr) {
    this.loginUsr = loginUsr;
  }
  
  
  /**
   * Gets the passUsr value for this ObtenerCURP.
   *
   * @return passUsr
   */
  public java.lang.String getPassUsr() {
    return passUsr;
  }
  
  
  /**
   * Sets the passUsr value for this ObtenerCURP.
   *
   * @param passUsr
   */
  public void setPassUsr(java.lang.String passUsr) {
    this.passUsr = passUsr;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ObtenerCURP)) 
      return false;
    ObtenerCURP other = (ObtenerCURP) obj;
    if (obj == null) 
      return false;
    if (this == obj) 
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.loginUsr==null && other.getLoginUsr()==null) || (this.loginUsr!=null && this.loginUsr.equals(other.getLoginUsr()))) && ((this.passUsr==null && other.getPassUsr()==null) || (this.passUsr!=null && this.passUsr.equals(other.getPassUsr())));
    __equalsCalc = null;
    return _equals;
  }
  
  private boolean __hashCodeCalc = false;
  
  public synchronized int hashCode() {
    if (__hashCodeCalc) {
      return 0;
    }
    __hashCodeCalc = true;
    int _hashCode = 1;
    if (getLoginUsr() != null) {
      _hashCode += getLoginUsr().hashCode();
    }
    if (getPassUsr() != null) {
      _hashCode += getPassUsr().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc =
  new org.apache.axis.description.TypeDesc(ObtenerCURP.class, true);
  
  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerCURP"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("loginUsr");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "loginUsr"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("passUsr");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "passUsr"));
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
  public static org.apache.axis.encoding.Serializer getSerializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanSerializer( _javaType, _xmlType, typeDesc);
  }
  
  /**
   * Get Custom Deserializer
   */
  public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanDeserializer( _javaType, _xmlType, typeDesc);
  }
  
}
