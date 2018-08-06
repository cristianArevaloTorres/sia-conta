/**
 * LDAP.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public interface LDAP extends javax.xml.rpc.Service {

/**
 * Autenticación de usuarios en el directorio acitvo
 */
    public java.lang.String getLDAPSoapAddress();

    public sia.ws.consumir.autentifica.directorio.LDAPSoap getLDAPSoap() throws javax.xml.rpc.ServiceException;

    public sia.ws.consumir.autentifica.directorio.LDAPSoap getLDAPSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException;
}
