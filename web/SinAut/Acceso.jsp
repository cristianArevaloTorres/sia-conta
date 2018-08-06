<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="java.io.IOException"%>
<%@ page import="sia.beans.seguridad.Autentifica"%>
<%@ page import="sia.exceptions.acceso.AccesoError"%>
<%@ page import="sia.libs.formato.Fecha"%>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Acceso</title>
  </head>
  <%!
      private String gp(HttpServletRequest request, String parametro) {
        return request.getParameter(parametro) != null ? request.getParameter(parametro) : "";
      }
      
      private void irError(String pagina, HttpServletRequest request, HttpServletResponse response, String error)  {
        try  {
          despachar(pagina, request, response);    
        } catch (Exception ex)  {
          System.err.println("Error al enviar la pagina de error");
        } finally  {
        }
      }
      
      private void validarIr(String pagina, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        despachar(pagina, request, response);
      }
      
      private ServletContext getServletContext(HttpServletRequest request) {
        return request.getSession().getServletContext();
      }
      
      private void despachar(String pagina, HttpServletRequest request, HttpServletResponse response) throws ServletException,
                                                                  IOException {
        RequestDispatcher dispatcher= getServletContext(request).getRequestDispatcher(pagina);
        dispatcher.forward(request, response);
      }
  %>
  <%
    String pagina     = "/contenedor.jsp";
    String cuenta     = request.getParameter("cuentaSia")==null?"":request.getParameter("cuentaSia").toLowerCase().trim();
    String contrasenia= request.getParameter("contraseniaSia");
    String sistema = request.getContextPath().substring(1,request.getContextPath().length());
    //cuenta = cuenta.toLowerCase();
    Autentifica Autentifica = null;
    if(cuenta!= null && contrasenia!= null) {
       ServletContext context = pageContext.getServletContext();
       HttpSession sesion     = pageContext.getSession();
         Autentifica   = request.getSession().getAttribute("Autentifica")==null?new Autentifica(sistema,request.getRemoteAddr()):(Autentifica)request.getSession().getAttribute("Autentifica");
         Autentifica.inicializaUsuario();
         boolean isUsuario          = false;
         try  {
           isUsuario = Autentifica.tieneAccesoBD(cuenta, contrasenia);             
         } catch (Exception ex)  {
           pagina= "/SinAut/ErrorPage.jsp?error="+ ex.getMessage()!=null?ex.getMessage():"NME"+ "&loginError="+ Autentifica.getLogin();
           ex.printStackTrace();
         } finally  {
         }
         if(isUsuario) {
           context.log("[+AUTENTICACION DE USUARIO] [".concat(Autentifica.getLogin()).concat("] ").concat(Fecha.formatear(Fecha.FECHA_HORA)));
           synchronized (sesion) {
             pageContext.setAttribute("Autentifica", Autentifica, pageContext.SESSION_SCOPE);
             sesion.setMaxInactiveInterval(60*60);
           }  // synchronized
         }    
         else {
           //synchronized (sesion) {
             //pageContext.removeAttribute("Autentifica", pageContext.SESSION_SCOPE);
           //}  // synchronized
           pagina= "/SinAut/ErrorPage.jsp?error="+ Autentifica.getError().replace('\n',' ')+ "&loginError="+ Autentifica.getLogin();
         } // if
     }
     else
       try  {
         throw new Exception("Error no tipificado del autentifica");  
       } catch (Exception ex)  {
         pagina= "/SinAut/ErrorPage.jsp?error=Error en la comunicacion hacia la base de datos&loginError=".concat(cuenta);
         new AccesoError(ex,cuenta,sistema,request.getRemoteAddr());
       } finally  {
       }
    if(Autentifica != null && pagina.indexOf("ErrorPage") >= 0) 
      irError(pagina,request,response,Autentifica.getError());
    else
      validarIr(pagina,request,response);
  %>
  <body></body>
</html>