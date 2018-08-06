<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="pbCuenta" class="sia.rf.contabilidad.registroContableNuevo.bcCuentaContable" scope="page"/>



<BODY OnLoad ="parent.loadSourceFinish('capaCuenta', parent.bufferFrame10);parent.validaCuenta();">

<%
 String param1="";
 boolean banderaFecha=false;
 String catCuentaId=request.getParameter("catCuentaId");
 String uniEje= request.getParameter("uniEje").length()==1?("00").concat(request.getParameter("uniEje")):request.getParameter("uniEje");
 String entidad=request.getParameter("entidad");
 String ambito=request.getParameter("ambito");
 String  ejercicio=request.getParameter("ejercicio");
 String lsCuentas = request.getParameter("cuentas");
 lsCuentas=lsCuentas.substring(0,lsCuentas.length()-1);
 // System.out.println("ejer "+ejercicio);
 String lsResultado="";
 
/* 
 String[] vsCuenta= request.getParameterValues("txtCuenta");
 String[] vsSubCuenta1= request.getParameterValues("txtSubCuenta1");
 String[] vsSubCuenta2= request.getParameterValues("txtSubCuenta2");
 String[] vsSubCuenta3= request.getParameterValues("txtSubCuenta3");
 String[] vsSubCuenta4= request.getParameterValues("txtSubCuenta4");
 String[] vsSubCuenta5= request.getParameterValues("txtSubCuenta5");
 String[] vsSubCuenta6= request.getParameterValues("txtSubCuenta6");
 String[] vsSubCuenta7= request.getParameterValues("txtSubCuenta7");   
 
 System.out.println("cuenta "+vsCuenta.length);

 */
 Connection conexion=null;
 try{
  conexion=DaoFactory.getContabilidad();
  //  sbAutentifica.setPagina(request.getRequestURI());
      
  lsResultado= pbCuenta.selectCuentaContableNoExisten(conexion,lsCuentas,catCuentaId,ejercicio);
  if (lsResultado.equals(""))
    param1="0";
  else
    param1=lsResultado;
  //System.out.println("param en capa cuenta "+param1);
}
catch(Exception E)
{
    System.out.println("Error en pagina en "+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}
finally{
  //sbAutentifica.cerrarConexion(conexion);  
  if (conexion != null){
    conexion.close();
    conexion=null;
  }   
  
}
   %>
<input name="txtValCuenta" type="hidden" value="<%=param1%>">
</body>

