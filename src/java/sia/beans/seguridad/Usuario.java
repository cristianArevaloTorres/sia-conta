 /*
  * Usuario.java
  *
  * Created on 1 de mayo de 2006, 11:52 AM
  *
  * Write by: alejandro.jimenez
  */
package sia.beans.seguridad;

import java.util.ArrayList;

import sia.libs.formato.Fecha;
import java.util.Calendar;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author Alejandro Jim�nez Garcia
 * email: alejandro.jimenez@senado.gob.mx
 *
 */
public class Usuario {
  
  private String sesion;
  private String cuenta;
  private Calendar hora;
  private String nombreCompleto;
  private String unidadEjecutora;
  private String entidad;
  private String ambito;
  private int numEmpleado;
  private int clase;
  private boolean mensaje;
  private boolean invalidado;
  private String texto;
  private String id;
  private String modulos;
  
  private List<UsuariosSitioStack> stackTace;
  
  public Usuario(String sesion, String cuenta, int clase, Autentifica aut) {
    setCuenta(cuenta);
    setHora(Calendar.getInstance());
    setClase(clase);
    setMensaje(getClase()== 0);
    setInvalidado(false);
    setSesion(sesion);
    setTexto(null);
    if(aut!=null) {
      setNombreCompleto(aut.getNombre());
      setUnidadEjecutora(aut.getDesUnidadEjecutora());
      setEntidad(aut.getDesEntidad());
      setAmbito(aut.getDesAmbito());
      setNumEmpleado(aut.getNumeroEmpleado());
    }
    stackTace = new ArrayList();
    modulos = "";
  }
  
  public Usuario(String sesion, String cuenta, int clase) {
    this(sesion, cuenta, clase, null);
  }
  
  public Usuario(String cuenta, int clase) {
    this(null, cuenta, clase);
  }

  public Usuario(String sesion) {
    this(sesion, null, -1);
  }
  
  public java.lang.String getCuenta() {
    return cuenta;
  }
  
  public void setCuenta(java.lang.String nombre) {
    this.cuenta = nombre;
  }
  
  public java.util.Calendar getHora() {
    return hora;
  }
 
  public void setHora(java.util.Calendar hora) {
    this.hora = hora;
  }
  
  public boolean isExclusivo() {
    return getClase()!= 0;
  }
  
  public boolean isMensaje() {
    return mensaje;
  }
  
  public void setMensaje(boolean mensaje) {
    this.mensaje = mensaje;
  }

  public boolean isInvalidado() {
    return invalidado;
  }
  
  public void setInvalidado(boolean invalidado) {
    this.invalidado = invalidado;
  }
  
  public int getClase() {
    return clase;
  }
  
  public void setClase(int clase) {
    this.clase = clase;
  }
  
  public java.lang.String getSesion() {
    return sesion;
  }
  
  public void setSesion(java.lang.String sesion) {
    this.sesion = sesion;
  }

  public String getTexto() {
    return texto;
  }

  public void setTexto(String texto) {
    this.texto = texto;
  }
  
  public String toString() {
    StringBuffer sb= new StringBuffer();
    sb.append(isExclusivo()? "*": "");
    sb.append(getSesion());
    sb.append("[");
    sb.append(getCuenta());
    sb.append(",Clase: ");
    sb.append(getClase());
    sb.append(",Mensaje: ");
    sb.append(isMensaje());
    sb.append(",Hora: ");
    sb.append(Fecha.formatear(Fecha.FECHA_HORA, getHora()));
    sb.append(",");
    sb.append("]");
    return sb.toString();
  }
  
  public void clear() {
    setCuenta(null);
    setTexto(null);
    setClase(-1);
  }
  
  public void finalize() {
    stackTace.clear();
    setStackTace(null);
    setCuenta(null);
    setHora(null);
  }
  
  private String validarURI(String uri) {
    int prim = uri.indexOf("/",1)+1;
    int seg = uri.indexOf("/",prim);
    if(seg != -1 && uri.substring(prim,seg).toUpperCase().equals("FACES")) {
      prim=seg+1;
      seg = uri.indexOf("/",prim);
    }
    if(seg == -1)
      modulos = modulos.concat("seguridad").concat(",");
    if(seg != -1 && modulos.indexOf(uri.substring(prim,seg)) < 0)
      modulos = modulos.concat(uri.substring(prim,seg)).concat(",");
    return seg == -1? "seguridad" : uri.substring(prim,seg);
  }
  
  public String addStack(HttpServletRequest request) {
    stackTace.add(new UsuariosSitioStack(request.getRequestURI()));
    return validarURI(request.getRequestURI());
  }

  public void setStackTace(List<UsuariosSitioStack> stackTace) {
    this.stackTace = stackTace;
  }

  public List<UsuariosSitioStack> getStackTace() {
    return stackTace;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  

  public void setUnidadEjecutora(String unidadEjecutora) {
    this.unidadEjecutora = unidadEjecutora;
  }

  public String getUnidadEjecutora() {
    return unidadEjecutora;
  }

  public void setEntidad(String entidad) {
    this.entidad = entidad;
  }

  public String getEntidad() {
    return entidad;
  }

  public void setAmbito(String ambito) {
    this.ambito = ambito;
  }

  public String getAmbito() {
    return ambito;
  }

  public void setNumEmpleado(int numEmpleado) {
    this.numEmpleado = numEmpleado;
  }

  public int getNumEmpleado() {
    return numEmpleado;
  }

  public void setModulos(String modulos) {
    this.modulos = modulos;
  }

  public String getModulos() {
    return modulos;
  }
  
  public void setNombreCompleto(String nombreCompleto) {
    this.nombreCompleto = nombreCompleto;
  }

  public String getNombreCompleto() {
    return nombreCompleto;
  }
}
