<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbEnvia" class="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaExc" scope="page"/> 
<jsp:useBean id="pbChequera"  class="sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>  
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>

<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null; 
 String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsFolioFinal=request.getParameter("txtFolioFinal");
 String lsCtaBancaria=request.getParameter("txtcuentaBancaria");    

%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <title>c735Control</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cambiar folio final", "Aplicar", true);</jsp:scriptlet>    
    
    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    <br>
     <FORM name="control" method="post"  action="../../../registroContable/filtroGenerarRegistroContableHB.jspx?pagina=folioFinal&idCatalogoCuenta=1">
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
       pbChequera.update_rf_tc_cuentas_folioFinal(conexion,lsCtaBancaria,lsFolioFinal);
       try{
       // Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
       pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
       estatus=pbEstadoCat.getEstatus();
       descripcion=pbEstadoCat.getDescripcion();
        if (estatus.equals("1")){      
          //resultado=pbEnvia.procesa(conexion,lsPolizaId, lsCatCuenta,"0001");
          resultado=lsCtaBancaria;
          conexion.commit();
        }  
        else{
         resultado="C";
         //conexion.commit();  // Para terminar la transaccion en falso
        } 
       } 
       catch(Exception E){
        resultado=""; 
        conexion.rollback(); 
       } 
       
       if(resultado.equals("C")){     
%>       
     <p>No se ha podido actualizar el folio final:<b><%=lsFolioFinal%></b>, ya que hay un proceso de <b><%=descripcion%></b> en SIA CONTABILIDAD</p>       
<%             
       }else
       if (!resultado.equals("")){

%>
     <p>El folio final:<b><%=lsFolioFinal%></b> ha sido actualizado coreectamente en SIA CONTABILIDAD</p>
<%
       }else{
%>
     <p><b>NO fue actualizado el folio final </b> vuelva a intentarlo, si el problema persiste comuniquese con el administrador.</p>
<%
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
       
    </td>
     <td width='80%'>
     <input type="submit" name='btnRegresar' value='Regresar' class='boton' onclick="javascript:LlamaPagina('c735Resultado.jsp?','');">
     </td></tr>
 </table>
 </FORM>
  </body>
</html>