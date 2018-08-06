<%@ page contentType="text/html;charset=ISO-8859-1"%>
<%@ page import="sun.jdbc.rowset.CachedRowSet"%>
<%@ page import="sia.db.dao.*" %>
<%@ page import="sia.db.dao.DaoFactory" %>

<%@page import="java.util.*" %>
<%@page import="java.sql.*"%>


<jsp:useBean id="pbCatalogo" class="sia.rf.contabilidad.registroContableEvento.Catalogo" scope="application"/>
<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>

<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
 <!--   <meta http-equiv="refresh" content="60;URL=< % = request.getRequestURL() % >"/>-->
    <title>controlaCatalogo</title>
    <link rel="stylesheet" href="../../Librerias/Estilos/siastyle.css" type='text/css'>
    <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
  </head>
  <body>
<%util.tituloPagina(out,"Contabilidad institucional","Control del Catálogo de cuentas","Resultado",true);%>
<%
String estadoActual ="";
String estadoActualDescr="";
try{
   Connection con = DaoFactory.getContabilidad();
   ResultSet crsEstadosCatalogo=null;
   pbCatalogo.select_estado_actual(con);
   estadoActual=pbCatalogo.getEstatus();
   estadoActualDescr=pbCatalogo.getDescripcion();
   crsEstadosCatalogo=pbCatalogo.select_estados(con);
   if(con !=null){
        con.close();
        con = null;   
       }
  %>    

<script language="javascript">
function cambiaEstado(){
estadoActual=<%=estadoActual%>;
estadoNuevo=document.filtro.lstEstadosCatalogo.value;
if(estadoNuevo!=null && estadoNuevo!=estadoActual){
    document.filtro.btnAceptar.disabled=true;
    document.filtro.submit();
}
else{
 alert("Selección inválida. Estado Actual: "+"<%=estadoActual%>"+" Estado Nuevo: "+estadoNuevo);
 document.filtro.btnAceptar.disabled=false;
}
}
</script>
<jsp:directive.include file="../registroContableNuevo/encabezadoFechaActual.jspf"/>  
<FORM name="filtro" method="post"  action="c732Control.jsp">  
  <br>
  <br>
  <br>
  <br>  
  
  <table width="100%" >
  <tr></tr>
  <tr><td class="negrita" align="right">Estado actual del catálogo: </td> <td><%=estadoActualDescr%></td></tr>
  <tr></tr>
  <tr></tr>
  <tr></tr>
  <tr><td class="negrita" align="right">Cambiar estado a: </td>
          <td><select class="cajaTexto" id="lstEestadosCatalogo" name="lstEstadosCatalogo">
          <%                              
            crsEstadosCatalogo.beforeFirst();
            while (crsEstadosCatalogo.next()) {
                if(estadoActual!=null && estadoActual.compareTo(crsEstadosCatalogo.getString("estatus").trim()) == 0)
                   out.println("<option value="+crsEstadosCatalogo.getString("estatus")+" selected>"+  crsEstadosCatalogo.getString("descripcion")+"</option>");
                else
                   out.println("<option value="+crsEstadosCatalogo.getString("estatus")+">"+ crsEstadosCatalogo.getString("descripcion")+"</option>");
             }
    %>
          </select></td>
   </tr>
  </table>
<%}
    catch (Exception e) {
      System.out.println("Error en CRSComboBox: "+ e.getMessage());
    }
    finally{
       
    }
%>
    <br>
    <br>
    <br>
    <br>
    <br>    
      <table width="100%" >
        <tr>     
          <td width="80%" align="right"><input type='button' name='btnAceptar' value='Aceptar' class='boton' onClick="this.disabled=true;javascript:cambiaEstado();"></td>
          <td width="20%"><input type='button' name='btnCancelar' value='Cancelar' class='boton' onClick="javascript:LlamaPagina('c732controlaCatalogo.jsp','');"></td>
        </tr>
      </table> 
</form>      
</body>
</html>