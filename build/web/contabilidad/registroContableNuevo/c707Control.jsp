<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*,java.util.Map, java.util.HashMap"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="Autentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="pbCheque" class="sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>  
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");%>
</jsp:useBean> 

<jsp:useBean id="xScriptlet" class="sia.scriptlets.Reporte" scope="page"/>


<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null;
 
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
    
 String lsChequeId = "";
 String lsLstCheques = "";
 String lsMensaje = "";
 
 %>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <title>c707Resultado</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Filtro de cheques", "Reactivación", true);</jsp:scriptlet>    
    
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    
    <br><br>
    
    <br>
    
    <FORM name="control" method="post"  action="c707Filtro.jsp">
 <br><br><br>
 

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<tr>
<td align="center">

<!-- (Inicia codigo JAVA) -->
<%  

 
 try{
    conexion=DaoFactory.getContabilidad();
    
    String[] lstCheques= request.getParameterValues("AMBITOS");
    if (lstCheques == null) return; //se sale.
    
    conexion.setAutoCommit(false);
    
    for(int i=0; i< lstCheques.length; i++ ){
       lsChequeId = lstCheques[i].substring(0,lstCheques[i].length()-1); 
       pbCheque.select_rf_tr_cheques(conexion, lsChequeId);
       if (!pbCheque.getEstatus().equals("1")){ //1 = Impreso
          lsMensaje = lsMensaje + "<br>El cheque " + pbCheque.getConsecutivo() + " no se reactivó."; 
       }
       else {
          pbFechaHoy.execute(conexion);
          pbFechaHoy.first();
          pbCheque.setEstatus("2");
          pbCheque.setFecha_ult_react(pbFechaHoy.getString("fechaActual"));
          pbCheque.update_rf_tr_cheques(conexion,lsChequeId);
          lsMensaje = lsMensaje + "<br>El cheque " + pbCheque.getConsecutivo() + " fue reactivado el " + pbCheque.getFecha_ult_react(); 
       } //if    
    }
    out.println(lsMensaje);
    conexion.commit();
}
catch(Exception E)
   {
   conexion.rollback();
   System.out.println("Error en "+E.getMessage()); 
   
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
</td>
</tr>
</table>
<!-- Termina tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->



<table width='100%'>
 <tr><td width='73%'>&nbsp;</td>
     <td width='10%'>
       <INPUT type="hidden" name="txtfechaActual" value=<%=lsFechaActual%>>
       
       <input type='submit' name='btnAceptar' value='Aceptar' class='boton'>
    </td>
     <td width='80%'>
     <input type='submit' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:history.back();" >
     </td></tr>
 </table>
 </FORM>
  </body>
</html>