/**
 * ActiveDirectoryResponse.java
 */

package sia.ws.consumir.autentifica.directorio;

public class ActiveDirectoryResponse implements java.io.Serializable {
  
  private java.lang.String activeDirectoryResult;
  
  public ActiveDirectoryResponse() {
  }
  
  public ActiveDirectoryResponse(String activeDirectoryResult) {
    this.activeDirectoryResult = activeDirectoryResult;
  }
  
  public java.lang.String getActiveDirectoryResult() {
    return activeDirectoryResult;
  }
  
  public void setActiveDirectoryResult(java.lang.String activeDirectoryResult) {
    this.activeDirectoryResult = activeDirectoryResult;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ActiveDirectoryResponse))
      return false;
    ActiveDirectoryResponse other = (ActiveDirectoryResponse) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.activeDirectoryResult==null && other.getActiveDirectoryResult()==null) || (this.activeDirectoryResult!=null && this.activeDirectoryResult.equals(other.getActiveDirectoryResult())));
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
    if (getActiveDirectoryResult() != null) {
      _hashCode += getActiveDirectoryResult().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ActiveDirectoryResponse.class, true);
  
  static {
    //http://intranetp.wapp.senado.gob.mx/ws/adminda
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", ">ActiveDirectoryResponse"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("activeDirectoryResult");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/ws/adminda", "ActiveDirectoryResult"));
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
