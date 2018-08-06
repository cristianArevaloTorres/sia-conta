/**
 * ObtenerUsrAtributo.java 
 */

package sia.ws.consumir.autentifica.directorio;

public class ObtenerUsrAtributo implements java.io.Serializable {
  
  private java.lang.String numEmpleado;  
  private java.lang.String cveAplicacion;
  
  public ObtenerUsrAtributo() {
  }
  
  public ObtenerUsrAtributo(java.lang.String numEmpleado, java.lang.String cveAplicacion) {
    this.numEmpleado = numEmpleado;
    this.cveAplicacion = cveAplicacion;
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
  
  private java.lang.Object __equalsCalc = null;
  
  public synchronized boolean equals(java.lang.Object obj) {
    if (!(obj instanceof ObtenerUsrAtributo))
      return false;
    ObtenerUsrAtributo other = (ObtenerUsrAtributo) obj;
    if (obj == null)
      return false;
    if (this == obj)
      return true;
    if (__equalsCalc != null) {
      return (__equalsCalc == obj);
    }
    __equalsCalc = obj;
    boolean _equals;
    _equals = true && ((this.numEmpleado==null && other.getNumEmpleado()==null) || (this.numEmpleado!=null && this.numEmpleado.equals(other.getNumEmpleado()))) && ((this.cveAplicacion==null && other.getCveAplicacion()==null) || (this.cveAplicacion!=null && this.cveAplicacion.equals(other.getCveAplicacion())));
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
    if (getNumEmpleado() != null) {
      _hashCode += getNumEmpleado().hashCode();
    }
    if (getCveAplicacion() != null) {
      _hashCode += getCveAplicacion().hashCode();
    }
    __hashCodeCalc = false;
    return _hashCode;
  }
  
  // Type metadata
  private static org.apache.axis.description.TypeDesc typeDesc = new org.apache.axis.description.TypeDesc(ObtenerUsrAtributo.class, true);
  
  static {
    typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">ObtenerUsrAtributo"));
    org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("numEmpleado");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "numEmpleado"));
    elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new org.apache.axis.description.ElementDesc();
    elemField.setFieldName("cveAplicacion");
    elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "cveAplicacion"));
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
