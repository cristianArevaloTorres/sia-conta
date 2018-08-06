/**
 * PerteneceGrupo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public class PerteneceGrupo  implements java.io.Serializable {
    private java.lang.String loginUsr;

    private java.lang.String passUsr;

    private java.lang.String grupo;

    public PerteneceGrupo() {
    }

    public PerteneceGrupo(
           java.lang.String loginUsr,
           java.lang.String passUsr,
           java.lang.String grupo) {
           this.loginUsr = loginUsr;
           this.passUsr = passUsr;
           this.grupo = grupo;
    }


    /**
     * Gets the loginUsr value for this PerteneceGrupo.
     * 
     * @return loginUsr
     */
    public java.lang.String getLoginUsr() {
        return loginUsr;
    }


    /**
     * Sets the loginUsr value for this PerteneceGrupo.
     * 
     * @param loginUsr
     */
    public void setLoginUsr(java.lang.String loginUsr) {
        this.loginUsr = loginUsr;
    }


    /**
     * Gets the passUsr value for this PerteneceGrupo.
     * 
     * @return passUsr
     */
    public java.lang.String getPassUsr() {
        return passUsr;
    }


    /**
     * Sets the passUsr value for this PerteneceGrupo.
     * 
     * @param passUsr
     */
    public void setPassUsr(java.lang.String passUsr) {
        this.passUsr = passUsr;
    }


    /**
     * Gets the grupo value for this PerteneceGrupo.
     * 
     * @return grupo
     */
    public java.lang.String getGrupo() {
        return grupo;
    }


    /**
     * Sets the grupo value for this PerteneceGrupo.
     * 
     * @param grupo
     */
    public void setGrupo(java.lang.String grupo) {
        this.grupo = grupo;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof PerteneceGrupo)) return false;
        PerteneceGrupo other = (PerteneceGrupo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.loginUsr==null && other.getLoginUsr()==null) || 
             (this.loginUsr!=null &&
              this.loginUsr.equals(other.getLoginUsr()))) &&
            ((this.passUsr==null && other.getPassUsr()==null) || 
             (this.passUsr!=null &&
              this.passUsr.equals(other.getPassUsr()))) &&
            ((this.grupo==null && other.getGrupo()==null) || 
             (this.grupo!=null &&
              this.grupo.equals(other.getGrupo())));
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
        if (getGrupo() != null) {
            _hashCode += getGrupo().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(PerteneceGrupo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", ">PerteneceGrupo"));
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
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("grupo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://intranet.wapp.senado.gob.mx/intranet/ws/autenticacion", "grupo"));
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
