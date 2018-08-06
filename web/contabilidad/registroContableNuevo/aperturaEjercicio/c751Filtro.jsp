<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page  import="java.util.*,java.sql.*"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="pbAperturaEjercicio" class = "sia.rf.contabilidad.registroContableNuevo.bcAperturaEjercicio" scope="page"/> 
<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
    <title>c751Filtro</title>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" >
      function Valida(){
         pasa= confirm('¿Está seguro abrir el ejercicio indicado?');
         if (pasa){
          filtro.btnAceptar.disabled=true;
          filtro.submit();
         }
      }
    </script>
</head>
<body>
    <jsp:scriptlet>util.tituloPagina(out, "Contabilidad", "Apertura Ejercicio","Filtrar", true);</jsp:scriptlet>    
    <jsp:directive.include file="../../registroContableNuevo/encabezadoFechaActual.jspf"/>

    <br><br>
        <H2>Proceso de Apertura de Ejercicio. </H2>
   
<%   
   String pagina ="";
   String proceso="";
   String fechas  = sia.libs.formato.Fecha.formatear(sia.libs.formato.Fecha.FECHA_CORTA,controlRegistro.getFechaAfectacion());
   System.out.println(fechas);
   Connection conexion=null;
    try{
     conexion=DaoFactory.getContabilidad();
     pbAperturaEjercicio.select_ultimo_ejercicio(conexion);
          
 %>  
   
    <br>
    <FORM name="filtro" method="post"  action="c751Control.jsp">
    <table align="center">
       <tr><td  class="negrita" align="right">Ejercicio: </td>
          <td>
            <input type='text' class=cajaTexto name='txtEjercicio' value=<%=pbAperturaEjercicio.getProceso()%> readonly >  
          </td>
      </tr>
      <tr><td><br></td>
          <td>
                <input type='hidden' name='txtFechaActual' value=<%=fechas%>>             
          </td>
      </tr>
      <tr>
          <td  align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="Valida();" ></td>
      </tr>
      </table>    
<%
}
catch(Exception E){
    conexion.rollback(); 
 %>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
<%
}
finally{
   if (conexion != null){
       conexion.close();
       conexion=null;
   }  
}

%>
  </FORM>
  </body>
</html>