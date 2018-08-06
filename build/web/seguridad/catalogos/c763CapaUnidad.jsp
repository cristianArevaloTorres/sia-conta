<%-- 
 
--%>

<%@ page contentType="text/html;charset=WINDOWS-1252" import="java.util.*,java.sql.*,java.io.*"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page  import="sia.db.dao.*"%>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/> 
<jsp:useBean id="consultaUnidadEjecutora" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   String unidad_ejecutora = "";
   unidad_ejecutora     = request.getParameter("unidad_ejecutora");
   consultaUnidadEjecutora.setTableName("rh_tc_uni_ejecutoras");
   String SQL = "select unidad_ejecutora, ambito, descripcion from rh_tc_uni_ejecutoras  where unidad_ejecutora='"+unidad_ejecutora+"'";
   
   consultaUnidadEjecutora.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     consultaUnidadEjecutora.execute(con);
   }
   catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaUnidadEjecutora "+e.getMessage());
   } //Fin catch
   finally{
     if (con!=null){
         con.close();
         con=null;
     }
   } //Fin finally 
   
  System.out.println("consultaUnidadEjecutora: " + SQL);
 
 %>
</jsp:useBean>

<jsp:useBean id="consultaAmbito" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaAmbito.setTableName("rh_tc_ambitos");
   String SQL2 = "select ambito, descripcion from rh_tc_ambitos ORDER by ambito ASC ";
   consultaAmbito.setCommand(SQL2);
   Connection con2=null;
   try{
     con2 = DaoFactory.getContabilidad();
     consultaAmbito.execute(con2);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaAmbito "+e.getMessage());
   } finally{
      if (con2!=null){
          con2.close();
          con2=null;
      }
   } //Fin finally
 %>
</jsp:useBean>


<head>
    <link rel="stylesheet" href="../../../Librerias/Estilos/siaexplorer.css" type="text/css">
    <script language="JavaScript" src="../../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
</head>
    <BODY OnLoad ="parent.loadSourceFinish('capaUnidad', parent.bufferFrame7);parent.validaDescripcion();">
<%
 try{
    String descripcion = "";
    String ambito = "";
    String operacion = request.getParameter("operacion");
    operacion = operacion.trim();
    consultaUnidadEjecutora.beforeFirst();
    while (consultaUnidadEjecutora.next()){
           descripcion = consultaUnidadEjecutora.getString("descripcion");
          ambito = consultaUnidadEjecutora.getString("ambito");
    }
    
    if(operacion.equals("Modificar")){
    %>
      <INPUT TYPE="text" NAME="txtDescripcion" SIZE="55" onChange="javascript:this.value=this.value.toUpperCase();"  class="cajaTexto" value='<%=descripcion%>' />
      <select NAME="lstAmbito" class='cajaTexto' OnChange='' >  <!--CapaCuenta(Filtro.lstGenero.value,Filtro.lstGrupo.value,Filtro.lstClase.value)-->         
           <%
                    consultaAmbito.beforeFirst(); 
                    while (consultaAmbito.next()){
                     if (consultaAmbito.getString("ambito").equals(ambito)){      
                  %>
                    <option selected value="<%=consultaAmbito.getString("ambito")%>"><%=consultaAmbito.getString("descripcion")%></option>
                  <%
                     }else{
                  %>
                    <option value="<%=consultaAmbito.getString("ambito")%>"><%=consultaAmbito.getString("descripcion")%></option>
                   <%                         
                     }
                  }
            %>
         </select>
      
      <%
    }else if(operacion.equals("Eliminar")){
    %>
        <INPUT TYPE="text" NAME="txtDescripcion" SIZE="55"   class="cajaTexto" value='<%=descripcion%>' disabled=''>
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
