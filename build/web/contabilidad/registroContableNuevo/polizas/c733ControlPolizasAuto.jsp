<%@page import="sia.beans.seguridad.Autentifica"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.beans.seguridad.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbEnvia" class="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaExc" scope="page"/> 
<jsp:useBean id="pbPoliza" class="sia.rf.contabilidad.registroContableNuevo.bcPoliza" scope="page"/>  
<jsp:useBean id="pbMaestroOperaciones" class="sia.rf.contabilidad.registroContableNuevo.bcMaestroOperaciones" scope="page"/>
<jsp:useBean id="sbAutentifica" class="sia.beans.seguridad.Autentifica" scope="session"/>  
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
   <% pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");%>
</jsp:useBean> 
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
  sbAutentifica = (Autentifica)request.getSession().getAttribute("Autentifica");
  int num_empleado = sbAutentifica.getNumeroEmpleado();
  
 Connection conexion=null; 
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFechaActual=request.getParameter("txtfechaActual");
 String lsCatCuenta=request.getParameter("txtIdCatalogo");    

String[] lstPolizas= request.getParameterValues("AMBITOS");

 String lsReferencia= request.getParameter("txtReferencia");
 String lsMes= request.getParameter("txtMes");
 String lsFecha01= request.getParameter("txtFecha01");
 String lsFecha02= request.getParameter("txtFecha02");
 String lsPoliza01=request.getParameter("txtPoliza01");
 String lsPoliza02=request.getParameter("txtPoliza02");    
 String lsPolizaId="";
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <title>c733ControlPolizasAuto</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Abrir pólizas automatizadas", "Aplicar", true);</jsp:scriptlet>    
    
    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    <br>
     <FORM name="control" method="post"  action="">
    <br><br><br>
 

<!-- Inicia tabla para desplegar resultado del filtro utilizando el bean xPaginacion -->
<table width="100%" align="center" class="general" name="tAmbitos" id="tAmbitos">
<tr>
<td align="center">

<!-- (Inicia codigo JAVA) -->
<%  

 
 try{
    conexion=DaoFactory.getContabilidad();
    String resultado="";
    conexion.setAutoCommit(false);
    conexion=DaoFactory.getContabilidad();
    
    String estatus="";
    String descripcion="";
    
    for(int i=0; i< lstPolizas.length; i++ ){
       pbMaestroOperaciones.select_rf_tc_maestro_operaciones_carga(conexion, lsUnidad, lsAmbito, lsEntidad, lsCatCuenta, lsEjercicio);
       lsPolizaId = lstPolizas[i].substring(0,lstPolizas[i].length()-1); 
       pbPoliza.update_rf_tr_polizas_automaticas(conexion,lsPolizaId, pbMaestroOperaciones.getMaestro_operacion_id(), num_empleado);
       try{
       // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
       pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
       estatus=pbEstadoCat.getEstatus();
       descripcion=pbEstadoCat.getDescripcion();
        if (estatus.equals("1")){      
          //resultado=pbEnvia.procesa(conexion,lsPolizaId, lsCatCuenta,"0001");
          resultado=lsPolizaId;
          conexion.commit();
        }  
        else{
         resultado="C";
         conexion.rollback();
         //conexion.commit();  // Para terminar la transaccion en falso
        } 
       } 
       catch(Exception E){
        resultado=""; 
        conexion.rollback(); 
       } 
       
       if(resultado.equals("C")){     
%>       
     <p>La poliza con idPoliza=<b><%=lsPolizaId%></b>,  NO fue abierta, ya que hay un proceso de <b><%=descripcion%></b> en SIA CONTABILIDAD</p>       
<%             
       }else
       if (!resultado.equals("")){

%>
     <p>La poliza con idPoliza=<b><%=lsPolizaId%></b>,  fue abierta correctamente en SIA CONTABILIDAD</p>
<%
       }else{
%>
     <p>La poliza con idPoliza=<b><%=lsPolizaId%> NO fue abierta</b> vuelva a intentarlo, si el problema persiste comuniquese con el administrador.</p>
<%
       }
    }      
}catch(Exception E){
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
    </td>
     <td width='80%'>
     <input type='botton' name='btnRegresar' value='Regresar' class='boton' onClick="javascript:LlamaPagina('c733FiltroPolizasAuto.jsp','');" >
     </td></tr>
 </table>
 </FORM>
  </body>
</html>