/**
 * LDAPSoap.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.3 Oct 05, 2005 (05:23:37 EDT) WSDL2Java emitter.
 */

package sia.ws.consumir.autentifica.directorio;

public interface LDAPSoap extends java.rmi.Remote {

    /**
     * Autentica al usuario según parámetros, regresa DataSet con
     * atributos del usuario
     */
    public sia.ws.consumir.autentifica.directorio.AutenticarResponseAutenticarResult autenticar(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String dominio, java.lang.String grupo) throws java.rmi.RemoteException;    

    /**
     * Autentica al usuario, regresa DataSet con atributos del usuario
     * y atributo bandera si pertenece ('True') o no ('False') al grupo
     */
    public sia.ws.consumir.autentifica.directorio.AutenticarGrupoResponseAutenticarGrupoResult autenticarGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException;

    /**
     * Autentica al usuario, regresa string con resultado obtenido
     * (vacío, autenticado o curp)
     */
    public java.lang.String obtenerCURP(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException;

    /**
     * Autentica al usuario y obtiene No.Empleado, regresa string
     * con resultado obtenido (vacío, autenticado o no.empleado)
     */
    public java.lang.String obtenerNumEmpleado(java.lang.String loginUsr, java.lang.String passUsr) throws java.rmi.RemoteException;

    /**
     * Autentica al usuario y valida si pertenece al grupo dado, regresa
     * string indicando si pertenece ('True') o no ('False') al grupo
     */
    public java.lang.String perteneceGrupo(java.lang.String loginUsr, java.lang.String passUsr, java.lang.String grupo) throws java.rmi.RemoteException;

    //wakko...    
    /**
     * Obtiene la cuenta del usuario a partir del No.Empleado, regresa string     
     */
    public java.lang.String obtenerUsrCuenta(java.lang.String numEmpleado, java.lang.String cveAplicacion) throws java.rmi.RemoteException;

    /**
     * Obtiene el atributo solicitado del usuario, regresa string
     */
    public java.lang.String obtenerUsrAtributo(String opcion, String loginUsr, String passUsr, String atributoObtener, String atributoFiltro, String valorFiltro, String cveAplicacion) throws java.rmi.RemoteException;
    
}
