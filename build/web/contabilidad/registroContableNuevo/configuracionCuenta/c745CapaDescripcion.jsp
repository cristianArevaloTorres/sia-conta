<%-- 
    Document   : c745CapaDescripcion
    Created on : 1/08/2013, 02:16:21 PM
    Author     : EST.JOSE.FLORES
--%>

<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>

<jsp:useBean id="consultaDescripcionClase" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   String idGenero= "";
   String idGrupo = "";
   String idClase = ""; 
   idGenero        = request.getParameter("idGenero");
   idGrupo         = request.getParameter("idGrupo");
   idClase         = request.getParameter("idClase");
   consultaDescripcionClase.setTableName("rf_tc_clase_clasif_cta");
   String SQL = " select descripcion from rf_tc_clase_clasif_cta ".
           concat(" where id_genero=").concat(idGenero).concat(" and " ).
           concat(" id_grupo=").concat(idGrupo).concat(" and ").
           concat(" id_clase=").concat(idClase);
   consultaDescripcionClase.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     consultaDescripcionClase.execute(con);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaGenero "+e.getMessage());
   } finally{
     if (con!=null){
         con.close();
         con=null;
     }
   } //Fin finally
 %>
</jsp:useBean>

<head>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
</head>

<BODY OnLoad ="parent.loadSourceFinish('capaDescripcion', parent.bufferFrame9);parent.validaDescripcion();">
<%
 try{
    String descripcion = "";
    String operacion = (String)request.getSession().getAttribute("tipo");
    operacion = operacion.trim();
    consultaDescripcionClase.beforeFirst();
    while (consultaDescripcionClase.next()){
            descripcion = consultaDescripcionClase.getString("descripcion");
    }
    if(operacion.equals("Modificar")){
    %>
        <INPUT TYPE="text" NAME="txtClase" SIZE="55"   class="cajaTexto" value='<%=descripcion%>'>
    <%
    }else if(operacion.equals("Eliminar")){
    %>
        <INPUT TYPE="text" NAME="txtClase" SIZE="55"   class="cajaTexto" value='<%=descripcion%>' disabled=''>
    <%                   
    }//Fin de  if(operacion.equals("Modificar"))
 } catch(Exception E){
    System.out.println("Error en pagina en "+" "+E.getMessage()); 
  %>
     <p>Ha ocurrido un error al accesar la Base de Datos,</p>
     <p>favor de reportarlo al Administrador del Sistema.</p>
     <p>Gracias.</p>
  <%
 } finally{
  //sbAutentifica.cerrarConexion(conexion);  
 } 
 %>   
</body>