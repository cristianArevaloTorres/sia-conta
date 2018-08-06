/*
 * Clase: ContextListener.java
 *
 * Creado: 23 de mayo de 2007, 02:33 PM
 *
 * Write by: alejandro.jimenez
 */

package sia.filters;

import java.sql.Connection;

import sia.libs.formato.Fecha;
import sia.libs.formato.Error;
import sia.beans.seguridad.UsuariosSitio;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;


public class ContextListener implements ServletContextListener {
  
  private static final String NOMBRE_APLICACION="FINANCIEROS";
  
  public void contextInitialized(ServletContextEvent event) {
    ServletContext context = event.getServletContext();
    setInitialAttribute(context,"systemName",NOMBRE_APLICACION);    
    context.log("[+SISTEMA] ".concat((String)context.getAttribute("systemName")));    
    try {
      UsuariosSitio usuarios= new UsuariosSitio();
      context.setAttribute("UsuariosSitio", usuarios);
      context.log("[+CONTROL DE USUARIOS] ".concat(Fecha.formatear(Fecha.FECHA_HORA, usuarios.getHora())));    
      //cargaGraficaMetas(context);
    } catch (Exception e) {
      System.err.println(Error.getMensaje(this, "SIAFM", e.getMessage()));      
      e.printStackTrace();
    }
  }
  
  public void contextDestroyed(ServletContextEvent event) {
    ServletContext context = event.getServletContext();
    UsuariosSitio usuarios= (UsuariosSitio)context.getAttribute("UsuariosSitio");
    context.log("[-CONTROL DE USUARIOS] ".concat("\n\n").concat(usuarios.toString()));    
    context.log("[-SISTEMA]".concat((String)context.getAttribute("systemName")));    
    context.removeAttribute("UsuariosSitio");
  }

  private void setInitialAttribute(ServletContext context, String initParamName, String defaultValue) {
    String initialValue= context.getInitParameter(initParamName);
    if(initialValue!= null)
      context.setAttribute(initParamName, initialValue);
    else 
      context.setAttribute(initParamName, defaultValue);
  }
  
  
  
}
