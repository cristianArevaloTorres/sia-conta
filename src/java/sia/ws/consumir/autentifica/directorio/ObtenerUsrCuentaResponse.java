/**
 * ObtenerUsrCuentaResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class ObtenerUsrCuentaResponse implements java.io.Serializable {
  
  private java.lang.String obtenerUsrCuentaResult;
  
  public ObtenerUsrCuentaResponse() {
  }
  
  public ObtenerUsrCuentaResponse(String obtenerUsrCuentaResult) {
    this.obtenerUsrCuentaResult = obtenerUsrCuentaResult;
  }
  
  public java.lang.String getObtenerUsrCuentaResult() {
    return obtenerUsrCuentaResult;
  }
  
  public void setObtenerUsrCuentaResult(java.lang.String obtenerUsrCuentaResult) {
    this.obtenerUsrCuentaResult = obtenerUsrCuentaResult;
  }
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ObtenerUsrCuentaResponse))
      return false;
    ObtenerUsrCuentaResponse other = (ObtenerUsrCuentaResponse) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.obtenerUsrCuentaResult==null && other.getObtenerUsrCuentaResult()==null) || (this.obtenerUsrCuentaResult!=null && this.obtenerUsrCuentaResult.equals(other.getObtenerUsrCuentaResult())));
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
    if (getObtenerUsrCuentaResult() != null) {
      _hashCode += getObtenerUsrCuentaResult().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ObtenerUsrCuentaResponse.class, true);
  
  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrCuentaResponse"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("obtenerUsrCuentaResult");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "ObtenerUsrCuentaResult"));
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
