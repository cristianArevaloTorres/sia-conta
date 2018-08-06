/*
 * Clase: SessionListener.java
 *
 * Creado: 23 de mayo de 2007, 02:23 PM
 *
 * Write by: alejandro.jimenez
 */

package sia.filters;

import java.sql.Connection;



import sia.libs.formato.Fecha;
import sia.beans.seguridad.UsuariosSitio;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import sia.beans.seguridad.Autentifica;
import sia.beans.seguridad.ConsumirAcceso;
import sia.db.dao.DaoFactory;
import sia.libs.formato.Error;
import sia.libs.recurso.Propiedades;

public class SessionListener implements HttpSessionListener {
  
  private int totalSessionCount  = 0;
  private int currentSessionCount= 0;
  private int maxSessionCount    = 0;
  private ServletContext context = null;
  private String attibutosMantenibles = "entrada|contenedor|error";
  
  public void sessionCreated(HttpSessionEvent event) {
    totalSessionCount++;
    currentSessionCount++;
    if(currentSessionCount> maxSessionCount) {
      maxSessionCount= currentSessionCount;
    }
    if(context == null) 
      storeInServletContext(event);
    HttpSession session= event.getSession();
    context.log("[+SESSION] ".concat(session.getId()));    
    UsuariosSitio usuarios= (UsuariosSitio)context.getAttribute("UsuariosSitio");
    usuarios.add(session.getId());
    context.log("[+REGISTRO DE CUENTAS] "+ session.getId().concat(" ").concat(Fecha.formatear(Fecha.FECHA_HORA, usuarios.getHora())));    
  }

  public void sessionDestroyed(HttpSessionEvent event) {
    HttpSession session= event.getSession();
    if(context == null) 
      storeInServletContext(event);
    UsuariosSitio usuarios= (UsuariosSitio)context.getAttribute("UsuariosSitio");
    Autentifica autentifica= (Autentifica)session.getAttribute("Autentifica");
    //if(Propiedades.getInstance().getPropiedad("sistema.sia.produccion").equals("1"))
      //deleteEmpleado(autentifica.getIp(),session.getServletContext().getAttribute("systemName").toString(),session.getId(),autentifica.getNumeroEmpleado(), session.getAttribute("cerrarSesion")==null?false:true);    
    session.removeAttribute("cerrarSesion");
    context.log("[-REGISTRO DE CUENTAS] "+ session.getId().concat(" ").concat(Fecha.formatear(Fecha.FECHA_HORA, usuarios.getHora())));    
    usuarios.delete(session.getId());
    context.log("[-SESSION] ".concat(session.getId()));   
   // Connection con=null;
    try{
    //  con = DaoFactory.getConnection();
//      autentifica.cerrarSesion(con);
          autentifica.cerrarSesion();
    }
    catch(Exception e){
       Error.mensaje(e,"SIAFM");
    }
    finally {
    //  DaoFactory.closeConnection(con);
      borrarAtributosSesion(session);
    }
    currentSessionCount--;
  }
  
  private void borrarAtributosSesion(HttpSession session) {
    String elemento = null;
    //Map noEliminados = new HashMap();
    while(session.getAttributeNames().hasMoreElements()) {
      elemento = (String)session.getAttributeNames().nextElement();
      //if(attibutosMantenibles.indexOf(elemento) < 0) {
        session.removeAttribute(elemento);
      //} else {
        //noEliminados.put(elemento,session.getAttribute(elemento));
        //session.removeAttribute(elemento);
      //}
      //System.out.println(elemento);
    }
    /*Iterator it = noEliminados.entrySet().iterator();
    Object e;
    while(it.hasNext()) {
        e = it.next();
        session.setAttribute(elemento,noEliminados.get(elemento));
    }*/
  }
  
  
   private void deleteEmpleado(String ip, String nombreAplicacion, String session, int numEmpleado, boolean cerrarSesion){
     ConsumirAcceso consumirAcceso=null;
     int tipoMensaje=0;
     try {
       consumirAcceso=new ConsumirAcceso(ip,nombreAplicacion,session,numEmpleado);       
       tipoMensaje=consumirAcceso.deleteAccesoEmpleado(cerrarSesion);
         if(tipoMensaje==ConsumirAcceso.ACCESO_ELIMINADO){
           System.out.println("Acceso eliminado wsAutenticacion ".concat(session) );
         }else{
             System.out.println("Error de comunicacion con el wsAutenticacion ip ".concat(ip).concat("numempleado ").concat(String.valueOf(numEmpleado)));
         }
           
     }//end try
      catch (Exception e) {
        Error.mensaje("SIAFM",e);
      }
      finally{
         consumirAcceso=null;
      }
   }
  
  // Register self in the servlet context so that
  // servlets and JSP pages can access the session
  // counts.
  private void storeInServletContext(HttpSessionEvent event) {
    HttpSession session = event.getSession();
    context = session.getServletContext();
    context.setAttribute("sessionCounter", this);
  }


    
}
