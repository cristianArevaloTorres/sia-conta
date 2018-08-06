/**
 * ObtenerUsrAtributoResponse.java
 */

package sia.ws.consumir.autentifica.directorio;

public class ObtenerUsrAtributoResponse implements java.io.Serializable {
  
  private java.lang.String obtenerUsrAtributoResult;
  
  public ObtenerUsrAtributoResponse() {
  }
  
  public ObtenerUsrAtributoResponse(String obtenerUsrAtributoResult) {
    this.obtenerUsrAtributoResult = obtenerUsrAtributoResult;
  }
  
  public java.lang.String getObtenerUsrAtributoResult() {
    return obtenerUsrAtributoResult;
  }
  
  public void setObtenerUsrAtributoResult(java.lang.String obtenerUsrAtributoResult) {
    this.obtenerUsrAtributoResult = obtenerUsrAtributoResult;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ObtenerUsrAtributoResponse))
      return false;
    ObtenerUsrAtributoResponse other = (ObtenerUsrAtributoResponse) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.obtenerUsrAtributoResult==null && other.getObtenerUsrAtributoResult()==null) || (this.obtenerUsrAtributoResult!=null && this.obtenerUsrAtributoResult.equals(other.getObtenerUsrAtributoResult())));
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
    if (getObtenerUsrAtributoResult() != null) {
      _hashCode += getObtenerUsrAtributoResult().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ObtenerUsrAtributoResponse.class, true);
  
  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrAtributoResponse"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("obtenerUsrAtributoResult");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrAtributoResult"));
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
