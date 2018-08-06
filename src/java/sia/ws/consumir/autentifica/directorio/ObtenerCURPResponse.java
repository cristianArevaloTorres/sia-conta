/**
 * ObtenerCURPResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class ObtenerCURPResponse  implements java.io.Serializable {
  
  private java.lang.String obtenerCURPResult;
  
  public ObtenerCURPResponse() {
  }
  
  public ObtenerCURPResponse(java.lang.String obtenerCURPResult) {
    this.obtenerCURPResult = obtenerCURPResult;
  }
  
  /**
   * Gets the obtenerCURPResult value for this ObtenerCURPResponse.
   *
   * @return obtenerCURPResult
   */
  public java.lang.String getObtenerCURPResult() {
    return obtenerCURPResult;
  }  
  
  /**
   * Sets the obtenerCURPResult value for this ObtenerCURPResponse.
   *
   * @param obtenerCURPResult
   */
  public void setObtenerCURPResult(java.lang.String obtenerCURPResult) {
    this.obtenerCURPResult = obtenerCURPResult;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ObtenerCURPResponse)) 
      return false;
    ObtenerCURPResponse other = (ObtenerCURPResponse) obj;
    if (obj == null) 
      return false;
    if (this == obj) 
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.obtenerCURPResult==null && other.getObtenerCURPResult()==null) || (this.obtenerCURPResult!=null && this.obtenerCURPResult.equals(other.getObtenerCURPResult())));
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
    if (getObtenerCURPResult() != null) {
      _hashCode += getObtenerCURPResult().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc =
  new org.apache.axis.description.TypeDesc(ObtenerCURPResponse.class, true);
  
  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerCURPResponse"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("obtenerCURPResult");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerCURPResult"));
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
    return new org.apache.axis.encoding.ser.BeanSerializer(_javaType, _xmlType, typeDesc);
  }
  
  /**
   * Get Custom Deserializer
   */
  public static org.apache.axis.encoding.Deserializer getDeserializer(java.lang.String mechType, java.lang.Class _javaType, javax.xml.namespace.QName _xmlType) {
    return new org.apache.axis.encoding.ser.BeanDeserializer(_javaType, _xmlType, typeDesc);
  }
  
}
