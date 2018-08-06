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
<jsp:useBean id="pbEstado" class = "sia.rf.contabilidad.registroContableEvento.Catalogo" scope="page"/>
<jsp:useBean id="pbProcesos" class = "sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%!
  public void enviaNotitificacion(String asunto,String mensaje){
    StringBuffer sb= new StringBuffer();
    sb.append("<html><title><head></head></title><body>");
    //sb.append("<br><strong>");
    //sb.append(asunto);
    //sb.append("</strong><br>");
    sb.append("<br><strong>");
    sb.append(mensaje);
    sb.append("</strong><br>");
    sb.append("<br>");
    Envio.asuntoMensaje( "siacontabilidad@senado.gob.mx" ,  "siacontabilidad@senado.gob.mx" ,null  ,asunto,sb.toString(),null,true);
  }
%>  
<%
 Connection conexion=null; 

  
 String lsEjercicio=request.getParameter("lstEjercicio");
 String lsMes=request.getParameter("lstMes"); 
 String numMes=lsMes.substring(0,2);
 String abrMes=lsMes.substring(3);
 String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" };
 String nombreMes=meses[Integer.valueOf(numMes)-1]; 
 String asunto="Estatus del proceso de reafectación del mes de "+nombreMes+" para el ejercicio "+ lsEjercicio;
 StringBuffer mensaje= new StringBuffer();
 //mensaje.append(asunto+"<br>");   
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    <title>c706Resultado</title>
  </head>
  <body>
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Proceso de reafectación.","Reafectación", true);</jsp:scriptlet>    
    <br><br>
    <H2>Informaci&oacute;n: Mes=<%=nombreMes%>, Ejercicio=<%=lsEjercicio%> </H2>
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
    
    pbEstado.select_estado_actual(conexion);
    if (!pbEstado.getEstatus().equals("2")){
 %>
  <p>El proceso de reafectacion no puede ser lanzado ya que NO existe un proceso de cierre definitivo activo.</p>
 <%
    }else{
     pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjercicio,numMes);
     if (pbProcesos.getEjercicio()==null){   
 %>
  <p>El proceso de reafectacion no puede ser lanzado, ya que NO existe un proceso de Verificacion con anteriodidad.</p>
 <%
     }
     else{
%>
    <p>Ya existe un proceso de Verificación aplicado el dia: <%=pbProcesos.getFecha_verificacion()%>,</p>
    <p>por lo que el proceso de Reafectación ha sido lanzado.</p>
<%
     mensaje.append("<p>Ya existe un proceso de Verificación aplicado el dia: "+pbProcesos.getFecha_verificacion()+",</p>");
     mensaje.append("<p>por lo que el proceso de Reafectación ha sido lanzado.</p>");
     
     resul=pbProcesos.select_proceso_reafectacion(conexion,numMes,abrMes,lsEjercicio);
     if (resul.equals("-1"))
       throw new Exception(" el proceso de reafectación ha marcado un error,");
       
     pbProcesos.update_rf_tr_procesos_cierre_estatus(conexion,lsEjercicio,numMes,"2","1");  
     
     mensaje.append("<p>El proceso de reafectación ha concluido, se han reafectado "+resul+" cuentas.</p>");
%>
   <p>El proceso de reafectacion ha concluido, se han reafectado <%=resul%> cuentas.</p>
<%       
       conexion.commit();
     //  enviaNotitificacion(asunto,mensaje.toString());
     }  
   }  //else principal
}catch(Exception E){
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
   mensaje.append(" <p>Ha ocurrido un error al accesar la Base de Datos,</p>");
   mensaje.append("<p>").append(E.getMessage()).append("</p>");
   mensaje.append("<p>favor de reportarlo al Administrador del Sistema.</p>");
   mensaje.append("<p>Gracias.</p>");
 //  enviaNotitificacion(asunto,mensaje.toString());   
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