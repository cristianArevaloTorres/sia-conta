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
<jsp:useBean id="pbEstado" class="sia.rf.contabilidad.registroContableEvento.Catalogo" scope="page"/>
<jsp:useBean id="pbProcesos" class="sia.rf.contabilidad.registroContableNuevo.bcProcesosCierre" scope="page"/>

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
 Statement stQuery=null;
 ResultSet rsQuery=null;
  
 String lsEjercicio=request.getParameter("lstEjercicio");
 String lsMes=request.getParameter("lstMes"); 
 String numMes=lsMes.substring(0,2);
 String abrMes=lsMes.substring(3);
 String[] meses = {"Enero","Febrero","Marzo","Abril","Mayo","Junio","Julio","Agosto","Septiembre","Octubre","Noviembre","Diciembre" };
 String nombreMes=meses[Integer.valueOf(numMes)-1]; 
 String asunto="Estatus del proceso de verificacion del mes de "+nombreMes+" para el ejercicio "+ lsEjercicio;
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
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Proceso de verificacion.", "Verificacion", true);</jsp:scriptlet>    
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
  <p>El proceso de verificación no puede ser lanzado ya que NO existe un proceso de cierre definitivo activo.</p>
 <%
    } else{
%>
<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<p>Las siguientes cuentas contables se han dado de alta en el catalogo de cuentas de manera automatica.</p>
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos" border=3>

<tr>
<td align="center">Cuenta contable</td>
<td align="center">Descripción</td>
<td align="center">Nivel</td>
<td align="center">Fecha de vigencia de inicio</td>
<td align="center">Fecha de vigencia de Fin</td>
</tr>

<%
     mensaje.append("<p>Las siguientes cuentas contables se han dado de alta en el catalogo de cuentas de manera automatica.</p>");
     mensaje.append("<table width='100%' align='center' class='general' name='tAmbitos' id='tAmbitos' border=3>");
     mensaje.append("<tr>");
     mensaje.append("<td align='center'>Cuenta contable</td>"); 
     mensaje.append("<td align='center'>Descripción</td>"); 
     mensaje.append("<td align='center'>Nivel</td>"); 
     mensaje.append("<td align='center'>Fecha de vigencia de inicio</td>"); 
     mensaje.append("<td align='center'>Fecha de vigencia de Fin</td>"); 
     mensaje.append("<tr>");
     
     resul=pbProcesos.select_proceso_verificacion(conexion,numMes,lsEjercicio);
     if (resul.equals("-1")){
       throw new Exception(" el proceso de verificacion a marcado un error,");
     }
     pbProcesos.select_rf_tr_procesos_cierre(conexion,lsEjercicio,numMes);
     if (pbProcesos.getEjercicio()==null){
       pbProcesos.setEjercicio(lsEjercicio);
       pbProcesos.setMes(numMes);
       pbProcesos.setVerificacion("1");     
       pbProcesos.setReafectacion("0");
       pbProcesos.setCongruencia("0");
       pbProcesos.setCierre_definitivo("0");
       pbProcesos.setEliminacion("0");
       pbProcesos.insert_rf_tr_procesos_cierre(conexion);
     }
     else{
       pbProcesos.update_rf_tr_procesos_cierre_estatus(conexion,lsEjercicio,numMes,"1","1");
     }     
     
     StringBuffer SQL=new StringBuffer("select cuenta_contable, cuenta_mayor_id, nivel, descripcion, to_char(fecha_vig_ini,'dd/mm/yyyy') fecha_vig_ini,  "); 
     SQL.append("  to_char(fecha_vig_fin,'dd/mm/yyyy') fecha_vig_fin , codigo_registro, registro, id_catalogo_cuenta ");
     SQL.append(" from rf_tr_cuentas_contables_pv ");    
     stQuery=conexion.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);     
     rsQuery=stQuery.executeQuery(SQL.toString());     
     while (rsQuery.next()){
        bandera=true;
        mensaje.append("<tr>");
        mensaje.append("<td  align='center'>").append(rsQuery.getString("cuenta_Contable")).append("</td>");
        mensaje.append("<td  align='center'>").append(rsQuery.getString("descripcion")).append("</td>");
        mensaje.append("<td  align='center'>").append(rsQuery.getString("nivel")).append("</td>");
        mensaje.append("<td  align='center'>").append(rsQuery.getString("fecha_vig_ini")).append("</td>");
        mensaje.append("<td  align='center'>").append(rsQuery.getString("fecha_vig_fin")).append("</td>");
        mensaje.append("</tr>");
%>   
       <tr>  
       <td  align="center"><%=rsQuery.getString("cuenta_Contable")%></td>
       <td  align="center"><%=rsQuery.getString("descripcion")%></td>
       <td align="center"><%=rsQuery.getString("nivel")%></td>
       <td  align="center"><%=rsQuery.getString("fecha_vig_ini")%></td>
       <td align="center"><%=rsQuery.getString("fecha_vig_fin")%></td>
       </tr>
<%     }  
       mensaje.append(" </table>");
       if (bandera==false){
       mensaje.append(" <p>El proceso de verificacion ha concluido, <b>no encontrandose ninguna cuenta contable que agregar.</b></p>");
%>
     </table>
   <p>El proceso de verificacion ha concluido, <b>no encontrandose ninguna cuenta contable que agregar.</b></p>

<%
       }else{
 %>
 </table>
<%       
       }
       conexion.commit();
      // enviaNotitificacion(asunto,mensaje.toString());
     }  //else principal
}catch(Exception E){
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
   mensaje.append(" <p>Ha ocurrido un error al accesar la Base de Datos,</p>");
   mensaje.append("<p>").append(E.getMessage()).append("</p>");
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