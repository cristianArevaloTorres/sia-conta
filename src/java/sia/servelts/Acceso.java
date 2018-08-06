package sia.servelts;

import com.coolservlets.beans.TreeBean;

import java.io.IOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspFactory;
import javax.servlet.jsp.PageContext;
import sia.beans.seguridad.Autentifica;
import sia.beans.seguridad.ConsumirAcceso;
import sia.beans.seguridad.Usuario;
import sia.beans.seguridad.UsuariosSitio;

import sia.exceptions.acceso.AccesoError;

import sia.db.dao.DaoFactory;

import sia.db.sql.SentenciasCRS;

import sia.libs.formato.Fecha;
import sia.libs.formato.Error;
import sia.libs.recurso.Propiedades;

public class Acceso extends HttpServlet {

  private static final int ACCESO_BLOQUEADO=-1;
  private static final int NO_AUTENTIFICADO=0;
  private boolean isUsuario = false;
  

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
  }
  
  private void validarContrasenia(HttpServletRequest request, 
                                  String pagina, String sistema,ServletContext context) throws Exception {
    Connection conection       = null;
    Statement stm_             = null;
    String cuenta     = request.getParameter("cuentaSia");
    String contrasenia= request.getParameter("contraseniaSia");
    Autentifica Autentifica   = request.getSession().getAttribute("Autentifica")==null?new Autentifica(sistema,request.getRemoteAddr()):(Autentifica)request.getSession().getAttribute("Autentifica");
    
    try {
      conection= DaoFactory.getConnection();
      //conection = null;
      if(conection == null)
       throw new Exception("Error al obtener la conexion");
      stm_     = conection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
      conection.setAutoCommit(false);
     // isUsuario= Autentifica.tieneAccesoBD(conection, stm_, cuenta, contrasenia);             
       isUsuario= Autentifica.tieneAccesoBD(cuenta, contrasenia);             
      if(Propiedades.getInstance().getPropiedad("sistema.sia.produccion").equals("1")){ 
        if(Autentifica.getNumeroEmpleado()!=0){
          int numEmpleadoWs=Autentifica.getNumeroEmpleado();
          //numEmpleadoWs = consumirAccesoWS(request,Autentifica,numEmpleadoWs,conection,context); //TODO: Comentado por autentificacion critica WS
          if(Autentifica.getNumeroEmpleado()!=numEmpleadoWs){
           // isUsuario=Autentifica.tieneAccesoBD(conection,stm_,numEmpleadoWs);
           isUsuario=Autentifica.tieneAccesoBD(numEmpleadoWs);
          }
        } //end if(Autentifica.getNumeroEmpleado()!=0)
      }//end if
        conection.commit();
    } 
    catch (Exception e) {
      new AccesoError(e,cuenta,sistema,request.getRemoteAddr());
      try  {
         if(conection != null)
           conection.rollback();
      } catch (Exception ex)  {
        new AccesoError(ex,cuenta,sistema,request.getRemoteAddr());
        System.err.println("Error al realizar el rollback");
      } finally  {
      }
      String error = Autentifica.getError()!=null? Autentifica.getError(): e.getMessage()!=null? e.getMessage() : "Error en la comunicacion hacia la base de datos"; 
      pagina= "/Librerias/Funciones/ErrorPage.jsp?error="+error+"&loginError=".concat(cuenta);
      System.err.println(Error.getMensaje(this, "SIAFM", e.getMessage()));
      throw new Exception(Autentifica!=null && Autentifica.getError()!=null? Autentifica.getError() : e.getMessage()!=null?e.getMessage():"Error en la comunicacion hacia la base de datos");
      //irError(pagina,request,response,());
      //e.printStackTrace();
    } 
    finally {
    try  {
      if(stm_!= null)
        stm_.close();
      stm_= null;
      if (conection!=null)
        conection.close();
      conection= null;
    } catch (Exception ex)  {
      new AccesoError(ex,cuenta,sistema,request.getRemoteAddr());
      System.err.println("Error al cerrar la conexion");
    } 
    
      
    }
  }
  
  private int consumirAccesoWS(HttpServletRequest request, Autentifica aut, int numEmpleadoWs, Connection con,ServletContext context) {
    
      ConsumirAcceso consumirAcceso=null;
      try  {
        consumirAcceso=new ConsumirAcceso (request.getRemoteAddr(),
                                         request.getSession().getServletContext().getAttribute("systemName").toString(),
                                         request.getSession().getId(), aut.getNumeroEmpleado());
        numEmpleadoWs=consumirAcceso.addAccesoEmpleado();
        switch ( numEmpleadoWs){
         case ACCESO_BLOQUEADO : 
           //aut.cerrarSesion(con);
           aut.cerrarSesion();
           aut.setError("Acceso bloqueado, cominuquese con al administrador del sistema");
           isUsuario = false;
           request.getSession().removeAttribute("Autentifica");
         break;
         case NO_AUTENTIFICADO :
           request.getSession().removeAttribute("Autentifica");
         break;
         case ConsumirAcceso.ERROR_COMUNICACION:
            context.log("[wsAutenticacion]".concat("Error al consumir el servicio de Autenticacion") );
            numEmpleadoWs= aut.getNumeroEmpleado();
                  
        }//end switch
      } catch (Exception ex)  {
        ex.printStackTrace();
      } finally  {
      }
      return numEmpleadoWs;
                  
  }
  
  private void agregarUsario(ServletContext context,PageContext pageContext, Autentifica Autentifica, String pagina) throws Exception {
    UsuariosSitio usuarios = (UsuariosSitio)context.getAttribute("UsuariosSitio");
    HttpSession sesion     = pageContext.getSession();
    Usuario usuario= null; //usuarios.getCuenta(sesion.getId(), cuenta);
    
    if(isUsuario) {
      usuario= new Usuario(sesion.getId(), Autentifica.getLogin(), Autentifica.getTipo()); 
      usuarios.addCuenta(sesion.getId(), usuario, Autentifica);
      context.log("[+AUTENTICACION DE USUARIO] [".concat(Autentifica.getLogin()).concat("] ").concat(Fecha.formatear(Fecha.FECHA_HORA)));
      synchronized (sesion) {
        pageContext.setAttribute("Autentifica", Autentifica, pageContext.SESSION_SCOPE);
        TreeBean tree= new TreeBean(); 
        tree.setReload(true);
        pageContext.setAttribute("tree", tree, pageContext.SESSION_SCOPE);
      }  // synchronized
    }    
    else {
      synchronized (sesion) {
        pageContext.removeAttribute("Autentifica", pageContext.SESSION_SCOPE);
      }  // synchronized
      pagina= "/Librerias/Funciones/ErrorPage.jsp?error="+ Autentifica.getError().replace('\n',' ')+ "&loginError="+ Autentifica.getLogin();
      throw new Exception(Autentifica.getError());
    } // if
  }
  

  public void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    String pagina     = "/contenedor.jsp";
    String cuenta     = request.getParameter("cuentaSia");
    String contrasenia= request.getParameter("contraseniaSia");
    String sistema = request.getContextPath().substring(1,request.getContextPath().length());
    Autentifica Autentifica   = request.getSession().getAttribute("Autentifica")==null?new Autentifica(sistema,request.getRemoteAddr()):(Autentifica)request.getSession().getAttribute("Autentifica");
    
    request.getSession().setAttribute("Autentifica",Autentifica);
         try  {
           if(cuenta!= null && contrasenia!= null) {
             JspFactory jspFactory  = JspFactory.getDefaultFactory();
             PageContext pageContext= jspFactory.getPageContext(this, request, response, "", true, 8192, true);
             ServletContext context = pageContext.getServletContext();
              
             validarContrasenia(request,pagina,sistema,context); //
             pagina = validarIr(pagina,request,response); 
             agregarUsario(context,pageContext,Autentifica,pagina);
             //cargarDatosDePresupuesto(request);  // carga datos necesarios para el sistema de presupuesto;
           }
           else
            throw new Exception("Error en la comunicacion hacia la base de datos");
           despachar(pagina, request, response);
         } catch (Exception ex)  {
           //new AccesoError(ex,cuenta,sistema,request.getRemoteAddr());
           irError("",request,response,ex.getMessage()!=null ? ex.getMessage() : "Ocurrio un error al tratar de ingresar");
         } 
  }
  
  private String gp(HttpServletRequest request, String parametro) {
    return request.getParameter(parametro)!=null?request.getParameter(parametro):"";
  }
  
  private void irError(String pagina, HttpServletRequest request, HttpServletResponse response, String error)  {
    try  {
      boolean isForo  = request.getParameter("idForo")!=null;
      boolean isMetasSeg  = !isForo && request.getParameter("entrada") !=null ? request.getParameter("entrada").equals("metasSeguimiento") : false;
      pagina = pagina != null && !pagina.equals("")? pagina : "/Librerias/Funciones/ErrorPage.jsp?error="+error;
      HttpSession sesion = request.getSession();
      if(isForo) {
        pagina = "/SinAutentifica/MetasInst/Foros/ErrorPage.jsp?error="+error+"&idForo="+gp(request,"idForo")+"&idTema="+gp(request,"idTema")+"&cForo="+gp(request,"cForo")+"&cuenta="+gp(request,"cuentaSia");
        sesion.setAttribute("error","SinAutentifica/MetasInst/Foros/ErrorPage.jsp?idForo="+gp(request,"idForo")+"&idTema="+gp(request,"idTema")+"&cForo="+gp(request,"cForo")+"&cuenta="+gp(request,"cuentaSia"));
      }
      if(isMetasSeg) {
        pagina = "/SinAutentifica/MetasInst/Seguimiento/ErrorPage.jsp?error="+error+"&cuenta="+gp(request,"cuentaSia");
        sesion.setAttribute("error","SinAutentifica/MetasInst/Seguimiento/ErrorPage.jsp?cuenta="+gp(request,"cuentaSia"));
      }
      despachar(pagina, request, response);    
    } catch (Exception ex)  {
      System.err.println("Error al enviar la pagina de error");
    } finally  {
      
    }
    
  }
  
  
  private String validarIr(String pagina, HttpServletRequest request, HttpServletResponse response) throws ServletException, 
                                                              IOException,
                                                              Exception {
//    Se redireccionaba a una pagina especial para la junta de gobierno, ahora con la nueva solicitud, actuará como los demas usuarios. 
    boolean isError     = pagina.indexOf("Error") != -1;
    boolean isForo      = request.getParameter("idForo")!=null && !isError;
    boolean isMetasSeg  = request.getParameter("entrada") !=null && !isForo && !isError ? request.getParameter("entrada").equals("metasSeguimiento") : false;
    boolean isPEst      = isProyectosEstrategicos(request) && !isForo && !isError;
    boolean isMetGen    = isMetasGeneral(request) && !isForo && !isError;
    HttpSession sesion = request.getSession();
    sesion.setAttribute("entrada","/index.jsp");
    sesion.setAttribute("contenedor","/contenedor.jsp"); 
    if(isForo) {
      if(!isError) {
        pagina = "/presupuesto/metasInst/Foros/p002EditarComentarios.jsp";
        sesion.setAttribute("contenedor","/p002EditarComentarios.jsp");
      }
    }
    if(isMetasSeg) {
      if(!isError && isMetGen) {
        pagina = "/presupuesto/metasInst/Seguimiento/p001Index.jsp";
        sesion.setAttribute("contenedor",request.getContextPath().concat("/presupuesto/metasInst/Seguimiento/p001Index.jsp?contenedor=1") );
      } else
        throw new Exception("No tiene privilegios para entrar a este sitio");
    } else {
      if(isPEst || isMetGen) {
          pagina="/contenedorMetasGenral.jsp";
          sesion.setAttribute("contenedor",request.getContextPath().concat("/contenedorMetasGenral.jsp"));
      }
    }
    return pagina;
    
    
  }
  
  private void despachar(String pagina, HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                              IOException {
    RequestDispatcher dispatcher= getServletContext().getRequestDispatcher(pagina);
    dispatcher.forward(request, response);
  }
  
  
  private boolean isProyectosEstrategicos(HttpServletRequest request) {
    boolean regresa = false;
    Autentifica aut = (Autentifica)request.getSession().getAttribute("Autentifica");
    if(Propiedades.getInstance().getPropiedad("sistema.sia.produccion").equals("1")) {
      if(aut.isPerfil(Propiedades.getInstance().getPropiedad("sistema.sia.proyectosEstrategicosProd")))
        regresa = true;
    }
    else {
      if( aut.isPerfil(Propiedades.getInstance().getPropiedad("sistema.sia.proyectosEstrategicos")))
        regresa = true;
    }
    return regresa;
  }
  
  private boolean isMetasGeneral(HttpServletRequest request) {
    boolean regresa = false;
    Autentifica aut = (Autentifica)request.getSession().getAttribute("Autentifica");
   /* if(Propiedades.getInstance().getPropiedad("sistema.sia.produccion").equals("1")) {
      if(aut.isPerfiles(Propiedades.getInstance().getPropiedad("sistema.sia.perfil.outlook.pro")))
        regresa = true;
    }
    else {
      if( aut.isPerfiles(Propiedades.getInstance().getPropiedad("sistema.sia.perfil.outlook.pru")))
        regresa = true;
    }*/
    return regresa;
  }
  

}
