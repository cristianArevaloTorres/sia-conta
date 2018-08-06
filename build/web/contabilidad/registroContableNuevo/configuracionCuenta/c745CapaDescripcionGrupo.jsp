<%-- 
    Document   : c745CapaDescripcionGrupo
    Created on : 5/08/2013, 01:19:10 PM
    Author     : EST.JOSE.FLORES
--%>

<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="consultaDescripcionGrupo" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   String idGenero = "";
   String idGrupo  = "";
   idGenero        = request.getParameter("idGenero");
   idGrupo         = request.getParameter("idGrupo");
   consultaDescripcionGrupo.setTableName("rf_tc_grupo_clasf_cta");
   String SQL = "select descripcion from rf_tc_grupo_clasf_cta where  id_genero=".concat(idGenero).
                                                        concat("  and id_grupo=").concat(idGrupo);
   consultaDescripcionGrupo.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     consultaDescripcionGrupo.execute(con);
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

<BODY OnLoad ="parent.loadSourceFinish('capaDescripcionGrupo', parent.bufferFrame8);parent.validaDescripcion();">
<%
 try{
    String descripcion = "";
    String operacion = (String)request.getSession().getAttribute("tipo");
    operacion = operacion.trim();
    consultaDescripcionGrupo.beforeFirst();
    while (consultaDescripcionGrupo.next()){
            descripcion = consultaDescripcionGrupo.getString("descripcion");
    }
    if(operacion.equals("Modificar")){
    %>
        <INPUT TYPE="text" NAME="txtGrupo" SIZE="55"   class="cajaTexto" value='<%=descripcion%>'>
    <%
    }else if(operacion.equals("Eliminar")){
    %>
        <INPUT TYPE="text" NAME="txtGrupo" SIZE="55"   class="cajaTexto" value='<%=descripcion%>' disabled=''>
    <%                   
    }
 }catch(Exception E){
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