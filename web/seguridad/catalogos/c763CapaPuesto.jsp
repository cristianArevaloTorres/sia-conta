<%-- 
 
--%>

<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="consultaPuestos2" class="sun.jdbc.rowset.CachedRowSet" scope="page" >
    <%
    
   int id_puesto = 0;
   id_puesto         = Integer.valueOf(request.getParameter("id_puesto"));
   consultaPuestos2.setTableName("rh_tr_puestos");
   String SQL4 = "select id_puesto,desc_puesto from rh_tr_puestos where id_puesto="+request.getParameter("id_puesto");
   consultaPuestos2.setCommand(SQL4);
   Connection con4=null;
   try{
     con4 = DaoFactory.getContabilidad();
     consultaPuestos2.execute(con4);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaPuestos2 "+e.getMessage());
   } finally{
      if (con4!=null){
          con4.close();
          con4=null;
      }
   } //Fin finally
   System.out.println("consultaPuestos2 " + SQL4);
 %>
 
</jsp:useBean>


<head>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
</head>
    <BODY OnLoad ="parent.loadSourceFinish('capaPuestos', parent.bufferFrame8);parent.validaDescripcion();">
<%
 try{
    String descripcion = "";
    String operacion = request.getParameter("operacion");
    operacion = operacion.trim();
    consultaPuestos2.beforeFirst();
    while (consultaPuestos2.next()){
           descripcion = consultaPuestos2.getString("desc_puesto");
         }
    
    if(operacion.equals("Modificar")){
    %>
      <INPUT TYPE="text" NAME="txtDescripcion" SIZE="55"   class="cajaTexto" onChange="javascript:this.value=this.value.toUpperCase();" value='<%=descripcion%>'>
           
      <%
    }else if(operacion.equals("Eliminar")){
    %>
        <INPUT TYPE="text" NAME="txtDescripcion" SIZE="55"   class="cajaTexto" value='<%=descripcion%>' disabled>
    <%                   
    }
 }catch(Exception E){
    System.out.println("Error en pagina en "+" "+E.getMessage()); 
  %>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
  <%
 }finally{
  //sbAutentifica.cerrarConexion(conexion);  
 } 
 %>   
    </body>
