<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbPolizaCarga" class="sia.rf.contabilidad.registroContableNuevo.bcPolizaCarga" scope="page"/>  
<jsp:useBean id="pbFechaHoy" class="sun.jdbc.rowset.CachedRowSet" scope="page">
    <% 
        // Se obtiene la fecha actual del servidor de la base de datos
        pbFechaHoy.setCommand("select to_Char(sysdate,'dd/mm/yyyy hh24:mi:ss') fechaActual from dual");
    %>
</jsp:useBean> 

<!-- (Inicia codigo JAVA) -->
<%

    
    Connection conexion = null;
    
    // Recuperacion de los parametros pasando mediante POST: 
    //         Unidad Ejecutora, Ambito, Entidad, Ejercicio, Fecha Actual
    //         y Catalogo de Cuentas
    String lsUnidad = request.getParameter("txtUnidad");
    String lsAmbito = request.getParameter("txtAmbito");
    String lsEntidad = request.getParameter("txtEntidad");
    String lsEjercicio = request.getParameter("txtEjercicio");
    String lsFechaActual = request.getParameter("txtfechaActual");
    String lsCatCuenta = request.getParameter("txtCatCuenta");
    String lsPolizaId = "";
%>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
    
    <title>c725Control</title>
  </head>
  <body>
  
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", " ", "Control de polizas", "Eliminar", true);</jsp:scriptlet>    
    
    <br><br>
      <b>Fecha Actual</b> [<%=lsFechaActual%>]
    
    <br><br>
    
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

 
 try {
         // Conexion a la bd de Contabilidad
         conexion = DaoFactory.getContabilidad();
         
         // Recuperacion del arreglo de ambitos seleccionados
         String[] lstPolizas = request.getParameterValues("AMBITOS");
         conexion.setAutoCommit(false);
         conexion = DaoFactory.getContabilidad();
          // Recorrido por cada unos de los elementos del arreglo de ambitos
         for (int i = 0; i < lstPolizas.length; i++) {
             // Extraccion del ID de la poliza
             lsPolizaId = lstPolizas[i].substring(0, lstPolizas[i].length() - 1);
             //Recupera y carga la poliza en el objeto en base al Id de la poliza 
             //proporcionado como parametros
             pbPolizaCarga.select_rf_tr_polizas_carga(conexion, lsPolizaId);
             //Actualiza los campos necesarios para establecer la poliza como eliminada,
             //destacando que el borrado es logico, es decir, la informacion se conserva
             //pero el registro ya no aparece en los listados del sistema
             pbPolizaCarga.update_rf_tr_polizas_carga_estatus_can(conexion, lsPolizaId, "9", "Cancelado por el empleado: " + pbPolizaCarga.getNumEmpleado() + " el dia: ");

%>
     <p align="left">La poliza <b><%=pbPolizaCarga.getNumPoli()%></b> con fecha de aplicacion contable del <b><%=pbPolizaCarga.getFechPoli()%></b></p>
<%
       }
         //Realizar los cambios exitosamente
         conexion.commit(); 
%>       
  <p>   </p>
  <p>   </p>
  <p><b>Las polizas han sido eliminadas correctamente.</b></p>
<%         
        } catch (Exception E) {
            //En caso de cualquier error la base de datos realiza un rollback de todos los cambios efectuados 
            //hasta el momento del fallo
            conexion.rollback();
            System.out.println("Error en " + E.getMessage());
   
%>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
   } finally {
           if (conexion != null) {
               //Cerrar la conexion con la BD
               conexion.close();
               conexion = null;
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
     <input type='botton' name='btnRegresar' value='Regresar' class='boton' onClick="window.open('../filtroGeneral.jsp?opcion=eliminarCargaExcel&idCatalogoCuenta=1','_self');" >
     </td></tr>
 </table>
 </FORM>
  </body>
</html>