<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<%@ page  import="sia.rf.contabilidad.registroContable.acciones.ControlRegistro"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbEnvia" class="sia.rf.contabilidad.registroContableNuevo.bcEnviaPolizaExc" scope="page"/> 
<jsp:useBean id="pbCuentaCheque"  class="sia.rf.contabilidad.registroContableNuevo.bcCuentasCheques" scope="page"/>  
<jsp:useBean id="pbCheque" class = "sia.rf.contabilidad.registroContableNuevo.bcCheque" scope="page"/>
<jsp:useBean id="pbEstadoCat" class = "sia.rf.contabilidad.registroContableEvento.bcEstadoCatalogo" scope="page"/>


<!-- (Inicia codigo JAVA) -->
<%
 Connection conexion=null; 
  String lsUnidad= request.getParameter("txtUnidad");
 String lsAmbito= request.getParameter("txtAmbito");
 String lsEntidad= request.getParameter("txtEntidad");
 String lsEjercicio= request.getParameter("txtEjercicio");
 String lsConsecutivo=request.getParameter("txtConsecutivo");
 String numEmpleado= request.getParameter("txtNumEmpleado");
 int consecutivo= Integer.parseInt(lsConsecutivo);
 String lsConsecutivoAnt= request.getParameter("txtConsecutivoAnt");
 int consecutivoAnt= Integer.parseInt(lsConsecutivoAnt)+1;
 String lsMotivo=request.getParameter("txtMotivo");
 String lsCtaBancaria=request.getParameter("txtcuentaBancaria");    
 ControlRegistro controlReg = (ControlRegistro)session.getAttribute("controlRegistro");
 String lsFechaActual = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlReg.getFechaAfectacion());
 

%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <title>c737Control</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Cambiar consecutivo", "Aplicar", true);</jsp:scriptlet>    
    
    <H2>Informaci&oacute;n de la P&oacute;liza: Unidad ejecutora=<%=lsUnidad%>, Entidad=<%=lsEntidad%>, Ambito=<%=lsAmbito%> y Ejercicio=<%=lsEjercicio%> </H2>
    <br>
     <FORM name="control" method="post"  action="../../../registroContable/filtroGenerarRegistroContableHB.jspx?pagina=consecutivo&idCatalogoCuenta=1">
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
    String estatus="";
    String descripcion="";
    
       try{
        for(consecutivoAnt=consecutivoAnt;consecutivoAnt<=consecutivo;consecutivoAnt++){ 
          String lsChequeId=pbCheque.select_SEQ_rf_tr_cheques(conexion);
          pbCheque.setCheque_id(lsChequeId);
          pbCheque.setCuenta_cheques_id(lsCtaBancaria);
          pbCheque.setConsecutivo(Integer.toString(consecutivoAnt));          
          pbCheque.setImporte("0"); 
          pbCheque.setReferencia(lsMotivo); 
          pbCheque.setPoliza_id("-1");          
          pbCheque.setAbreviatura("CC");
          pbCheque.setBeneficiario("NO aplica");
          pbCheque.setNumEmpleado(numEmpleado);
          pbCheque.setDigitoVerificador("0");
          pbCheque.setFechaCheque(lsFechaActual);
          pbCheque.insert_rf_tr_cheques(conexion);
          pbCheque.setEstatus("5");   
          pbCheque.setFecha(lsFechaActual);
          pbCheque.setFecha_ult_react(lsFechaActual);
          pbCheque.setFecha_impresion(lsFechaActual);
          pbCheque.setFechaCheque(lsFechaActual);
          pbCheque.update_rf_tr_cheques(conexion, lsChequeId);
        }//fin del ciclo para insertar en rf_tr_cheques
        
        pbCuentaCheque.setConsecutivo(String.valueOf(consecutivoAnt-1));
        pbCuentaCheque.update_rf_tc_cuentas_consecutivo(conexion,lsCtaBancaria);
        //Verifica si no hay cierre definitivo justamente antes de hacer el commit a la poliza
      pbEstadoCat.select_rf_tc_estado_catalogo(conexion);
      estatus=pbEstadoCat.getEstatus();
      descripcion=pbEstadoCat.getDescripcion();
        if (estatus.equals("1")){      
          //resultado=pbEnvia.procesa(conexion,lsPolizaId, lsCatCuenta,"0001");fue
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
     <p>No se ha podido actualizar el consecutivo:<b><%=lsConsecutivo%></b>, ya que hay un proceso de <b><%=descripcion%></b> en SIA CONTABILIDAD</p>       
<%             
       }else
       if (!resultado.equals("")){

%>
     <p>El consecutivo:<b><%=lsConsecutivo%></b> ha sido actualizado coreectamente en SIA CONTABILIDAD</p>
<%
       }else{
%>
     <p><b>NO fue actualizado el consecutivo </b> vuelva a intentarlo, si el problema persiste comuniquese con el administrador.</p>
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
     <input type="submit" name='btnRegresar' value='Regresar' class='boton' onclick="javascript:LlamaPagina('c737Resultado.jsp?','');">
     </td></tr>
 </table>
 </FORM>
  </body>
</html>