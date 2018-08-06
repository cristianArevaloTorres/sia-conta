<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.libs.correo.Envio"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");%>
</jsp:useBean> 
<jsp:useBean id="pbApertura" class="sia.rf.contabilidad.registroContableNuevo.bcAperturaEjercicio" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null; 
 Statement stQuery=null;
 ResultSet rsQuery=null;
  
 String lsEjercicio=request.getParameter("txtEjercicio");
 StringBuffer mensaje= new StringBuffer();
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <title>c706Resultado</title>
  </head>
  <body>
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Apertura Ejercicio", "Apertura Ejercicio", true);</jsp:scriptlet>    
    <br><br>
    <H2>Informaci&oacute;n: Ejercicio=<%=lsEjercicio%> </H2>
    <br>
    <FORM name="control" method="post"  action="">
 <br><br>
 
<!-- (Inicia codigo JAVA) -->
<%  
 try{
    conexion=DaoFactory.getContabilidad();
    conexion.setAutoCommit(false);
    boolean bandera=false;
    String resul="";
    
 %>

<%


    //Verifica que existe el registro del ejercicio a abrir en la tabla de presupuesto
    resul=pbApertura.select_ejercicio(conexion,Integer.parseInt(lsEjercicio));
    //Sino existe lo da de alta
     if (resul.equals("-1")){
       resul="";
       resul=pbApertura.insert_ejercicio(conexion,Integer.parseInt(lsEjercicio));
       if (resul.equals("-1"))
          throw new Exception(" el proceso de insertar el ejercicio ha generado un error,");
       System.out.println("**Se insertó el ejercicio "+lsEjercicio+" correctamente**");
       }
     
     //Duplica el catálogo de cuentas en base al ejercicio anterior       
     resul=pbApertura.select_duplicar_catalogo(conexion,Integer.parseInt(lsEjercicio)-1,Integer.parseInt(lsEjercicio));
     if (resul.equals("-1"))
       throw new Exception(" el proceso de duplicar catalogo ha marcado un error,");
     System.out.println("**Se insertó el catalogo de cuentas "+lsEjercicio+" correctamente**");
     
    //Duplica las operaciones en base al ejercicio anterior       
     resul = pbApertura.select_duplicar_maestro_operacion(conexion,Integer.parseInt(lsEjercicio)-1,Integer.parseInt(lsEjercicio));
     if (resul.equals("-1"))
       throw new Exception(" El proceso de duplicar maestro operacion a marcado un error");
     System.out.println("**Se insertaron las operaciones contables del Ejercicio "+lsEjercicio+" correctamente**");
  
    //Abrir el mes de Enero
    resul=pbApertura.select_registrar_mes_programa(conexion, Integer.parseInt(lsEjercicio));
     if (resul.equals("-1"))
       throw new Exception(" El proceso de Registrar Mes Programa ha marcado un error,");
     System.out.println("**Se abrió el mes de Enero del Ejercicio "+lsEjercicio+" correctamente**");
  
    //Duplicar la cofiguración del catalogo de cuentas
     resul=pbApertura.select_duplicar_configuracion(conexion,Integer.parseInt(lsEjercicio)-1,Integer.parseInt(lsEjercicio));
     if (resul.equals("-1"))
       throw new Exception(" El proceso de duplicar configuracion a marcado un error");
    System.out.println("**Se duplicó la cofiguracion del Catalogo del Ejercicio "+lsEjercicio+" correctamente**");
    
%>
    <p>El proceso de apertura del nuevo ejercicio  ha concluido con éxito</p>

<%
 conexion.commit();

}catch(Exception E){
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
   mensaje.append(" <p>Ha ocurrido un error al accesar la Base de Datos,</p>");
   mensaje.append("<p>favor de reportarlo al Administrador del Sistema.</p>");
   mensaje.append("<p>Gracias.</p>");
  // enviaNotitificacion(asunto,mensaje.toString());   
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   }
   finally{ 
     if (rsQuery != null){
        rsQuery.close();
        rsQuery=null;
     }
     if (stQuery != null){
        stQuery.close();
           stQuery=null;
     }          
    if (conexion!=null){
         conexion.close();
         conexion=null;
    }
 }
%>
<!-- (Termina codigo JAVA) -->
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<br><br>
<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='40%'>
     <input type='botton' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:history.go(-1);" >
     </td></tr>
 </table>
 </FORM>
  </body>
</html>