 /*
  * UsuariosSitio.java
  *
  * Created on 1 de mayo de 2006, 11:52 AM
  *
  * Write by: alejandro.jimenez
  */
package sia.beans.seguridad;

import java.util.ArrayList;

import sia.libs.recurso.Propiedades;
import java.util.Calendar;

import java.util.List;

import javax.servlet.http.HttpServletRequest;


import org.josql.Query;

import org.josql.QueryResults;

import sia.libs.formato.Error;

public class UsuariosSitio {
  
  /** Creates a new instance of UsuariosStito */
  private Calendar hora;
  private boolean multiSesion;
  private String modulos;
  private Query query;
  private List lCuentas;
  
  public UsuariosSitio() throws Exception {
    setHora(Calendar.getInstance());
    String propiedad= Propiedades.getInstance().getPropiedad("sistema.sia.control");
    setMultiSesion(propiedad==null || propiedad.equals("2"));
    System.out.println("inicio proceso de REGISTRO ["+ getHora().getTime().toString()+ "]");
    query = new Query();
    lCuentas = new ArrayList();
    modulos="";
  };
  
  private void removeUsuario(Usuario usr) {
    usr.finalize();
    lCuentas.remove(usr);
  }
  
  public void delete(String sesion) {
      List<Usuario> usrs = getUsuarios(sesion);
      for(Usuario usr : usrs) {
        System.out.println("remove usuario: "+ usr.toString());
        removeUsuario(usr);
      }
  } //  delete

  public void deleteCuenta(String sesion, String cuenta) {
        removeUsuario(getUsuario(sesion,cuenta));
  } //  delete
  
  public void add(String sesion) {
    try {
        //cuentas.put(sesion, new HashMap());
        System.out.println("add sesion: "+ sesion);
    }
    catch(Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "add", "Error: ".concat(e.getMessage())));
    }
  } // add
  
  public void addCuenta(String sesion, String cuenta) {
    addCuenta(sesion, cuenta, null);
  }

  public void addCuenta(String sesion, String cuenta, Autentifica aut) {
    try {
          Usuario usuario= new Usuario(sesion, cuenta, -1, aut);
          lCuentas.add(usuario);
          System.out.println("add usuario: "+ usuario.toString());
    }
    catch(Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "addCuenta", "Error: ".concat(e.getMessage())));
    }
  } // addCuenta
  
   public void addCuenta(String sesion, Usuario usuario) {
     addCuenta(sesion, usuario, null);
   }

  public void addCuenta(String sesion, Usuario usuario, Autentifica aut) {
    try {
          usuario.setNombreCompleto(aut.getNombre());
          usuario.setUnidadEjecutora(aut.getDesSiglasUE());
          usuario.setEntidad(aut.getDesEntidad());
          usuario.setAmbito(aut.getDesAmbito());
          usuario.setNumEmpleado(aut.getNumeroEmpleado());
          //cuentas.put(usuario.getCuenta(), usuario);
          lCuentas.add(usuario);
          System.out.println("add usuario: "+ usuario.toString());
    }
    catch(Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", "addCuenta", "Error: ".concat(e.getMessage())));
    }
  } // addCuenta
  
  
  public Calendar getHora() {
    return hora;
  }
  
  public Calendar getHora(Calendar hora) {
    return hora;
  }

  public void setHora(Calendar hora) {
    this.hora = hora;
  }


  public long getComenzo() {
    return hora.getTimeInMillis();
  } // getComenzo
  

  public void finalize() {
    System.out.println("termino proceso de REGISTRO ["+ Calendar.getInstance().getTime().toString()+ "]");
    getLCuentas().clear();
    setLCuentas(null);
    setHora(null);
  } // finalize

  public boolean isMultiSesion() {
    return multiSesion;
  }

  public void setMultiSesion(boolean multiSesion) {
    this.multiSesion = multiSesion;
  }
  
  public void addStack(HttpServletRequest request) {
    Usuario usuario = getUsuario(request.getSession().getId(),((Autentifica)request.getSession().getAttribute("Autentifica")).getLogin());
    validarModulo(usuario.addStack(request));
  }
  
  private void validarModulo(String modulo) {
    if(modulos.indexOf(modulo) < 0)
      modulos = modulos.concat(modulo).concat(",");
  }
  
  public Usuario getUsuario(String id, String cuenta) {
    String sen = "select * from sia.beans.seguridad.Usuario where cuenta = '"+cuenta+"' and sesion = '"+id+"'";
    QueryResults qr;
    List<Usuario> res;
    Usuario usr = null; 
    try {
      query.parse(sen);
      qr = query.execute(lCuentas);
      res = qr.getResults();
      usr = res.get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return usr;
  }
  
  public Usuario getSesion(String id) {
    String sen = "select * from sia.beans.seguridad.Usuario where sesion = '"+id+"'";
    QueryResults qr;
    List<Usuario> res;
    Usuario usr = null; 
    try {
      query.parse(sen);
      qr = query.execute(lCuentas);
      res = qr.getResults();
      usr = res.get(0);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return usr;
  }
  
  public List<Usuario> getUsuarios(String id) {
    String sen = "select * from sia.beans.seguridad.Usuario where sesion = '"+id+"'";
    QueryResults qr;
    List<Usuario> res = null;
    try {
      query.parse(sen);
      qr = query.execute(lCuentas);
      res = qr.getResults();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }
  
  public List<Usuario> getUsuariosModulo(String modulo) {
    String sen = "select * from sia.beans.seguridad.Usuario where modulos like '%"+modulo+"%'";
    QueryResults qr;
    List<Usuario> res = null;
    try {
      query.parse(sen);
      qr = query.execute(lCuentas);
      res = qr.getResults();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }
  
  public List<Usuario> getUsuariosSinModulo() {
    String sen = "select * from sia.beans.seguridad.Usuario where length(modulos) = 0";
    QueryResults qr;
    List<Usuario> res = null;
    try {
      query.parse(sen);
      qr = query.execute(lCuentas);
      res = qr.getResults();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return res;
  }

  public void setModulos(String modulos) {
    this.modulos = modulos;
  }

  public String getModulos() {
    return modulos;
  }

  public void setLCuentas(List lCuentas) {
    this.lCuentas = lCuentas;
  }

  public List getLCuentas() {
    return lCuentas;
  }
}
