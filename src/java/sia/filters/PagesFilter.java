package sia.filters;
//import com.coolservlets.beans.TreeBean;

import com.evermind.server.http.EvermindHttpServletRequest;

import sia.beans.seguridad.Autentifica;
import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import sia.beans.seguridad.ConsumirAcceso;

import sia.db.dao.DaoFactory;

//import sia.menu.outlook.MenuSection;
import sia.libs.formato.Error;
//import sia.libs.recurso.IJurid;

import sia.libs.recurso.Propiedades;

public class PagesFilter extends HttpServlet implements Filter {

  private FilterConfig filterConfig= null;
  private String paginaIndice      = null;
  private String paginaExclusion   = null; 
  private String paginaSesion      = null;
  private String nombreAplicacion  = null;
  private ServletContext context   = null;
  
  private static final String FORMATOS= ".jpg|.gif|.png|.bmp|.jpeg|.css|.js|.txt|.pdf|.xls|.dbf";
  
  public PagesFilter()  {
    paginaIndice= null;
  }

  public void init(FilterConfig filterConfig) {
    this.filterConfig= filterConfig;
    paginaIndice = filterConfig.getInitParameter("paginaIndice");
    if(paginaIndice== null)
      paginaIndice= "/index.jsp";
    paginaExclusion= filterConfig.getInitParameter("paginaExclusion");
    if(paginaExclusion== null)
      paginaExclusion= "/excluir.jsp";
    paginaSesion= filterConfig.getInitParameter("paginaSesion");
    if(paginaSesion== null)
      paginaSesion= "/termino.jsp";
  }
  
  private void verificarAutentificacion(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException,
                                                                        ServletException {
    String contexto        = null;
    String pagina          = null;
    String exclusion       = null;
    String extension       = null;
    Autentifica autentifica= null;
    HttpSession session    = null;     
    
    if(request instanceof HttpServletRequest || request instanceof EvermindHttpServletRequest) {
      //session= ((HttpServletRequest)request).getSession(false);
      session = ((HttpServletRequest)request).getSession();
    }
    if(session!= null) {
      autentifica = (Autentifica)session.getAttribute("Autentifica");
      if(autentifica==null) {
        session.setAttribute("Autentifica",new Autentifica());
        autentifica = (Autentifica)session.getAttribute("Autentifica");
      }
      context    = session.getServletContext();
    } // if
    contexto= ((HttpServletRequest)request).getContextPath();
    pagina  = ((HttpServletRequest)request).getRequestURI();
    if(pagina.indexOf("ErrorPage.jsp")!=-1) {
    filterChain.doFilter(request, response);
    }
    else {
    if(pagina!= null && pagina.lastIndexOf(".")>= 0)
    extension= pagina.substring(pagina.lastIndexOf(".")).toLowerCase();
    exclusion= this.paginaExclusion;
    if(session== null)
    exclusion= this.paginaSesion; 
    if (!isAutenticado((HttpServletRequest)request,autentifica)  )     {
        ((Autentifica)session.getAttribute("Autentifica")).setError("Tiene que autenticarse para poder entrar al sitio.");
        mensaje("~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~");
        mensaje("Autentifica: "+ autentifica);
        mensaje("Session: "+ session);
        mensaje("Contexto: ".concat(contexto));
        mensaje("Pagina: ".concat(pagina));
        mensaje("Extension: ".concat(extension==null?pagina:extension));
        mensaje("Ir pagina: ".concat(contexto.concat(exclusion)));
        redireccionar((HttpServletResponse)response, contexto.concat(exclusion));
    }  
    else{
          if(response instanceof HttpServletResponse) {
              ((HttpServletResponse)response).setHeader("Pragma","no-cache");
              ((HttpServletResponse)response).setHeader("Cache-Control","no-store");
              ((HttpServletResponse)response).setDateHeader("Expires",-1); 
    }
    filterChain.doFilter(request, response);
    autentifica.setEnviado(true);  
    }
    }
  }
  
  private boolean isEXcluida(ServletRequest request) throws IOException,
                                                             ServletException {
    String pagina  = ((HttpServletRequest)request).getRequestURI();
    return (pagina.indexOf("SinAut")>=0||pagina.indexOf("ContaProcu/index.jsp")>=0||pagina.indexOf("ContaPA/index.jsp")>=0);
  }

  public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
    if(isEXcluida(request))
      filterChain.doFilter(request, response);
    else
      verificarAutentificacion(request, response, filterChain);
  }
  
  private boolean isAutenticado(HttpServletRequest req, Autentifica autentifica) throws IOException, ServletException {
    int numEmpleado=0;
    HttpSession session=null;
    try  {
      session=req.getSession();
      nombreAplicacion =session.getServletContext().getAttribute("systemName").toString();
      if ((autentifica== null || !autentifica.isFirmado()) ) {
        numEmpleado=0;   
        session.setAttribute("Autentifica",new Autentifica());
        autentifica=(Autentifica)session.getAttribute("Autentifica");
      }else{
        numEmpleado=autentifica.getNumeroEmpleado();
      }
    } 
    catch (Exception ex)  {
      Error.mensaje(ex,"SIAFM");      
    } finally  {
    }
    return numEmpleado!=0;  
  }

  private void redireccionar(HttpServletResponse response, String pagina) throws IOException {
    if(filterConfig!= null) {
      if(pagina!= null && !"".equals(pagina)) 
        response.sendRedirect(pagina);
    }; // if
  }; // redireccionar

  public void destroy() {
    this.filterConfig= null;
  }

  private void mensaje(String texto) {
    if(context!= null)
      context.log(texto);
    else
      System.err.println(texto);
  } // mensaje
   
  private void setAutentica(Autentifica autentifica, int numEmpleado) {
    Connection con;
    con=null;
    try {
      con = DaoFactory.getConnection();
      Statement stm = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      con.setAutoCommit(false);
      autentifica.tieneAccesoBD(numEmpleado);
      autentifica.setEnviado(true);
      con.commit();
    }
    catch (Exception e) {
      Error.getMensaje(e,"SIAFM");
    }
    finally {
      DaoFactory.closeConnection(con);
    }
    
  }
   
  
  
}
