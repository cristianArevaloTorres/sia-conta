<%@ page contentType="text/html; charset=ISO-8859-1"%>
<%@ page  import="sia.beans.seguridad.*"%>
<%@ page import="sia.rf.contabilidad.acceso.CargaChequeDBF"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="java.util.*,java.sql.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>   
<%@ page import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<html>
<head>
<meta http-equiv="Content-Language" content="es-mx">
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Agregar polizas manuales.</title>
<link rel="stylesheet" href="../../../../Librerias/Estilos/siaexplorer.css" type='text/css'>
</head>
<jsp:useBean id="sbAutentifica" class = "sia.beans.seguridad.Autentifica" scope="session" />  

<%
  ControlRegistro controlReg = (ControlRegistro)request.getSession().getAttribute("controlRegistro"); 
  String lsUniEje=controlReg.getUnidad();
  String lsPais="147";   
  String lsEntidad=String.valueOf(controlReg.getEntidad());
  String lsAmbito=String.valueOf(controlReg.getAmbito()); 
 // String lsEjercicio="2009";
  String lsEjercicio=String.valueOf(controlReg.getEjercicio());  
  String lsCatCuenta=String.valueOf(controlReg.getIdCatalogoCuenta());      
  String lsRuta=(String) session.getAttribute("ruta");     
%>
<body>
<jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Carga de cheques", "Cargar", true);</jsp:scriptlet>      
<FORM Method="post" action="" name="formulario" >
  <H2>Informaci&oacute;n: Unidad ejecutora=<%=lsUniEje%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%></H2>
<%
   sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
   String numEmpleado=Integer.toString(sbAutentifica.getNumeroEmpleado());
   String resultado="";
   Connection conexion=null;
   if(lsEntidad.length()==1)
     lsEntidad="0"+lsEntidad;
   try{   
     conexion=DaoFactory.getContabilidad();
     conexion.setAutoCommit(false);
     CargaChequeDBF cargaChequeDBF = new CargaChequeDBF();
     resultado=cargaChequeDBF.procesa(conexion, numEmpleado, lsUniEje, lsEntidad+ lsAmbito, lsRuta,controlReg.getEjercicio());
     conexion.commit();
     //conexion.rollback();
%>
<H2>La carga de cheques ha sido exitosa.</H2>
<H2><%=resultado%></H2>

<%
    // System.out.println(resultado);
  }
  catch(Exception E){
    System.out.println(resultado);
    conexion.rollback(); 
    sbAutentifica.setError("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage());  
  //  sbAutentifica.enviaCorreo();
    System.out.println("Error en pagina en "+sbAutentifica.getPagina()+" "+E.getMessage()); 
%>
     <H3><font color="red">Ha ocurrido un error.</font></h3>
     <p><b><%=E.getMessage()%></b></p>
     <p>Favor de corregir lo necesario o en su caso reportarlo al administrador del sistema.</p>
     <table width='100%' align="right">
       <tr  align="right">
         <td width='10%'>
            <br/>
            <input type='button' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../../filtroGeneral.jsp?opcion=cargaCheques&idCatalogoCuenta=1','_self');" >
         </td></tr>
     </table>
<%
   }
   finally{
     if (conexion != null){
       conexion.close();
       conexion=null;
     }  
    }      
%>
  </form>  
  </body>
</html>