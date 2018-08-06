/**
 * PerteneceGrupoResponse.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class PerteneceGrupoResponse  implements java.io.Serializable {
    private java.lang.String perteneceGrupoResult;

    public PerteneceGrupoResponse() {
    }

    public PerteneceGrupoResponse(
           java.lang.String perteneceGrupoResult) {
           this.perteneceGrupoResult = perteneceGrupoResult;
    }


    /**
     * Gets the perteneceGrupoResult value for this PerteneceGrupoResponse.
     * 
     * @return perteneceGrupoResult
     */
    public java.lang.String getPerteneceGrupoResult() {
        return perteneceGrupoResult;
    }


    /**
     * Sets the perteneceGrupoResult value for this PerteneceGrupoResponse.
     * 
     * @param perteneceGrupoResult
     */
    public void setPerteneceGrupoResult(java.lang.String perteneceGrupoResult) {
        this.perteneceGrupoResult = perteneceGrupoResult;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PerteneceGrupoResponse)) return false;
        PerteneceGrupoResponse other = (PerteneceGrupoResponse) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.perteneceGrupoResult==null && other.getPerteneceGrupoResult()==null) || 
             (this.perteneceGrupoResult!=null &&
              this.perteneceGrupoResult.equals(other.getPerteneceGrupoResult())));
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
        if (getPerteneceGrupoResult() != null) {
            _hashCode += getPerteneceGrupoResult().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PerteneceGrupoResponse.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">PerteneceGrupoResponse"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("perteneceGrupoResult");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "PerteneceGrupoResult"));
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
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
