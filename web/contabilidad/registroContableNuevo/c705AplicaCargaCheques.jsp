<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaCheque"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page  import="sia.db.dao.DaoFactory"%>
<%@ page  import="sia.libs.correo.Envio"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   

<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Carga de cheques via un archivo de Excel.</title>
<link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type='text/css'>
</head>
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
  <body>
  
<%!
  public void enviaNotitificacion(String asunto,String mensaje, String correoEmpleado){

    StringBuffer sb= new StringBuffer();
    sb.append("<html><title><head></head></title><body>");
    //sb.append("<br><strong>");
    //sb.append(asunto);
    //sb.append("</strong><br>");
    sb.append("<br><strong>");
    sb.append(mensaje);
    sb.append("</strong><br>");
    sb.append("<br>");
    Envio.asuntoMensaje( "siacontabilidad@inegi.org.mx" , correoEmpleado.concat("@inegi.org.mx").concat(",claudia.macariot@inegi.org.mx,jorgeluis.perez@inegi.org.mx,yadhira.ramos@inegi.org.mx"),null  ,asunto,sb.toString(),null,true);
  }
%> 
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Generación de cheques", "Aplicar", true);</jsp:scriptlet>      
<%
  String lsUniEje=request.getParameter("txtUnidad");
  String lsEntidad=request.getParameter("txtEntidad");
  String lsAmbito=request.getParameter("txtAmbito");
  String lsEjercicio=request.getParameter("txtEjercicio");
  
  String lsCatCuenta=(String) request.getAttribute("idCatalogoCuenta");  //Esta fijo en 1 el catalogo de cuenta en la clase bcEnviaPolizaCheque     
  String[] lstCheques= request.getParameterValues("AMBITOS");
  String[] lstChequesRes;
%>
<FORM Method="post" action="" name="formulario" >

  <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%></H2>
<%
  StringBuffer mensajeCheque = null;
  String resultado="";
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
  Connection conexion=null;
  conexion=DaoFactory.getContabilidad();
  conexion.setAutoCommit(false);
  try{
    mensajeCheque = new StringBuffer("");
    bcEnviaPolizaCheque bcEnvia = new bcEnviaPolizaCheque();
    resultado=bcEnvia.procesa(conexion,lstCheques);
    conexion.commit();
%>    
<table width="80%" align="center" class="general">
    <tr>
      <th height="21" colspan="3" align="left" class="general">Los cheques han sido generados exitosamente.</th>
    </tr>
    <tr>
      <td>Numero de cheque</td>
      <td>Poliza cheque asociada</td>
    </tr>
<%
  mensajeCheque.append("<table width='80%' align='cente' class='general'>");
  mensajeCheque.append("<tr><th height='21' colspan='3' align='left' class='general'>Los cheques han sido generados exitosamente.</th></tr>");
  mensajeCheque.append("<tr><td>Numero de cheque</td><td>Poliza cheque asociada</td></tr>");

  lstChequesRes=resultado.split(",");
  for (int x=0; x<lstChequesRes.length-1;x=x+2){    //Ciclo para cada uno de los cheques     
    mensajeCheque.append("<tr><td>").append(lstChequesRes[x]).append("</td><td>").append(lstChequesRes[x+1]).append("</tr>");
%>
   <tr>
     <td><%=lstChequesRes[x]%></td>
     <td><%=lstChequesRes[x+1]%></td>
   </tr>
<%
}
mensajeCheque.append("</table>");
%>  
 </table>    

<br></br>      
<br></br>
<br></br>

      <table align="right" >
   <tr>
     <td>
      <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../registroContableNuevo/filtroGeneral.jsp?opcion=aplicaCheques&idCatalogoCuenta=1','_self');">
     </td>
   </tr>

 </table>    
 
<%
    
    
    enviaNotitificacion("Aplicación de cheques","<p>Ha concluido el proceso de aplicacion de cheques.</p>".concat(mensajeCheque.toString()),"correo");
    System.out.println("Cheques procesados:" + resultado);
  }
  catch(Exception E){
    conexion.rollback();
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
//    sbAutentifica.enviaCorreo();
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
    E.printStackTrace();
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{
     if (conexion!=null){ 
       conexion.close(); 
       conexion=null; 
     }             
     System.out.println("Termino aplicación contable de cheques.");
     }     
    %>
  </form>  
  </body>
</html>