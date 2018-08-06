package sia.ws.consumir.autentifica.directorio;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

//import mx.gob.inegi.wapp.intranet.intranet.ws.autenticacion.LDAPSoapProxy;
import sia.ws.consumir.autentifica.directorio.LDAPSoapProxy;
//import INEGI.WebService.*;

/**
 * Servlet implementation class for Servlet: Autenticacion
 *
 */
 public class Autenticacion extends javax.servlet.http.HttpServlet implements javax.servlet.Servlet 
 {
	private static final String CONTET_TYPE = "text/html; charset=windows-1252";

	public Autenticacion() 
	{
		super();
	}   	
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		this.doPost(request, response);
	}  	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
	{
		response.setContentType(CONTET_TYPE);
		PrintWriter out = response.getWriter();
		String loginUsr = "";
		String passUsr = "";
		String CURP = "";
		String NumEmpleado = "";		

		try
		{
			loginUsr = request.getParameter("user");
			passUsr = request.getParameter("password");
			
			Autentica obj;
			obj = new Autentica(loginUsr, passUsr);
      
			if(obj.ObtenerCURP())
			{
				CURP = obj.getCURP();
			}
			
			if(obj.ObtenerNumeroEmpleado())
			{
				NumEmpleado = obj.getNumeroEmpleado();
			}

      //if (obj.ObtenerUsrCuenta())
     
			out.println("<html>");
			out.println("<head>INEGI - Autenticacion</head>");
			out.println("<body>");
			out.println("<p>N&uacute;mero de empleado: " + NumEmpleado + "</p>");
			out.println("<p>CURP: " + CURP + "</p>");
			out.println("</body>");
			out.println("</html>");
			out.close();
		}
		catch(Exception ex)
		{
			ex.printStackTrace(out);
		}
	}   	  	    
}