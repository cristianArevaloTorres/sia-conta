<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@ page import="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaCheque"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.beans.seguridad.*,sia.db.dao.DaoFactory"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>  
<jsp:useBean id="Autentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />
<jsp:useBean id="pbCargaCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCargaCheque" scope="page"/>
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>Eliminación de carga de cheques</title>
<link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
</head>

  <body>
  <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Generación de cheques", "Aplicar", true);</jsp:scriptlet>      
<%
  
  String lsUniEje=request.getParameter("txtUnidad");
  String lsEntidad=request.getParameter("txtEntidad");
  String lsAmbito=request.getParameter("txtAmbito");
  String lsEjercicio=request.getParameter("txtEjercicio");
  String lsEntAmb="";
   if(lsEntidad.length()==1)
   lsEntAmb="0"+lsEntidad+lsAmbito;
  else
   lsEntAmb=lsEntidad+lsAmbito;
  
  String lsCatCuenta=(String) request.getAttribute("idCatalogoCuenta");  //Esta fijo en 1 el catalogo de cuenta en la clase bcEnviaPolizaCheque     
  String[] lstCheques= request.getParameterValues("AMBITOS");
  Connection conexion = null;
  String condicion = "";

%>
<FORM Method="post" action="" name="formulario" >
 <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%></H2>
<%
   String resultado="";
  try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    for (int x=0; x<lstCheques.length;x++){
      condicion = condicion.concat(lstCheques[x].substring(0,lstCheques[x].length()-1));
       if(x < lstCheques.length-1)
         condicion = condicion.concat(",");
    }
    condicion = "where a.consecutivocheques in(".concat(condicion).concat(") and a.unidad = '").concat(lsUniEje).concat("' and a.ambito = '").concat(lsEntAmb).concat("' and extract(year from a.fechacarga) = ").concat(lsEjercicio).concat(" and a.numempleado = ").concat(String.valueOf(Autentifica.getNumeroEmpleado()));
    pbCargaCheque.delete_RF_TR_CHEQUES_CARGA(conexion,condicion);
    //conexion.commit();
%>

<p>Se han eliminado los registro seleccionados.</p>
<%
    //System.out.println("Cheques procesados:" + resultado);
  }
  catch(Exception E){
    conexion.rollback();
    Autentifica.setError("Error en pagina en "+Autentifica.getPagina()+" "+E.getMessage());  
    Autentifica.enviaCorreo();
    System.out.println("Error en pagina en "+Autentifica.getPagina()+" "+E.getMessage()); 
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{
     System.out.println("Termino la eliminación de cheques.");
     if (conexion!=null){
       conexion.close();
       conexion=null;
     }
   }     
    %>
</form>
  </body>
</html>