<%-- 
    Document   : c761ModificarFormulario
    Created on : 12/08/2013, 03:15:18 PM
    Author     : EST.JOSE.FLORES
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.logging.Logger"%>
<%@page import="java.util.logging.Level"%>
<%@page import="java.util.*,java.sql.*"%>
<%@page import="sia.db.dao.*"%>

<jsp:useBean id="util" class="sia.libs.pagina.Funciones" scope="session"/>
<jsp:useBean id="consultaPerfil" class="sun.jdbc.rowset.CachedRowSet" scope="page">
  <%
   consultaPerfil.setTableName("sg_tc_perfil");
   String SQL = "SELECT cve_perfil as clave, descripcion FROM sg_tc_perfil where cve_perfil = "+request.getParameter("clave");
   consultaPerfil.setCommand(SQL);
   Connection con=null;
   try{
     con = DaoFactory.getContabilidad();
     consultaPerfil.execute(con);
   } catch(Exception e){
        System.out.println("Ocurrio un error al accesar el metodo consultaPerfil "+e.getMessage());
   } finally{
      if (con!=null){
          con.close();
          con=null;
      }
   } //Fin finally
 %>
</jsp:useBean>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>c761FModificarFormulario</title>
      <link rel="stylesheet" href="../../Librerias/Estilos/siaexplorer.css" type="text/css">
      <script language="JavaScript" src="../../Librerias/Javascript/Capturador/funciones.js" type="text/javascript"></script>
            <script language="JavaScript">
                function regresar(){ 
                    window.location.href="../perfil/c761Resultado.jsp?clave=<%=session.getAttribute("claveGrupo")%>";
                } 
                function validar(){
                    mensaje= '------------------ ALERTA ------------------\n';  
                    var aprobado = true;
                    var valorCampo = document.forms.formaModificarPerfil.txtNombrePerfil.value;
                    if(valorCampo=='' || valorCampo==' ' || valorCampo=='  ' || valorCampo=='   ' ){
                       mensaje+='Por favor escriba en el campo Nombre Nuevo';
                       alert(mensaje);
                       mensaje='';
                       aprobado = false;
                    }
                    return aprobado;
                }
            </script>
    </head>
    <body onLoad="">
    <%util.tituloPagina(out, "Seguridad", "Modificar Perfil","", "Formulario", true);%>
    <br></br> 
        <form id="formaModificarPerfil" method="post" action="c761Control.jsp" >    
            <table align='center' class='general'  cellpadding="3" >
                <thead></thead>
                <tbody>
                   <%
                       consultaPerfil.beforeFirst(); 
                       while (consultaPerfil.next()){
                   %>   
                        <th class="azulObs" align="right">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Clave:
                        </th>
                        <td>
                          <input type="text"   name="claveAnteriorPerfil" size="50"   class="cajaTexto" value="<%=consultaPerfil.getString("clave")%>" disabled="">
                          <input type="hidden" name="clavePerfil" value="<%=consultaPerfil.getString("clave")%>" />
                        </td>
                    </tr>
                    <tr>
                        <th class="azulObs" align="right">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Nombre Anterior:
                        </th>
                        <td>    
                            <input type="text" NAME="nombreAnteriorPerfil" SIZE="50"   class="cajaTexto" value="<%=consultaPerfil.getString("descripcion")%>" disabled="">
                        </td>
                    </tr> 

                    <tr>
                        <th class="azulObs" align="right">
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                            Nombre Nuevo:
                        </th>
                        <td>    
                            <input TYPE="text" NAME="txtNombrePerfil" SIZE="50"   class="cajaTexto" value="<%=consultaPerfil.getString("descripcion")%>">
                        </td>
                    </tr>                     
                    <% }//Fin de while   %>  
                </tbody>
            </table>
            <br></br>
            <table align="center">
                <tr>
                  <td>
                        <input type="submit" class="boton" value="Aceptar" onClick="javaScript: if( validar() == false) return false; "/>
                        <input type="hidden" name="operacion" value="Modificar" />
                  </td>
                  <td>
                        <input type="button" class="boton" value="Regresar" onclick="regresar();" >
                  </td>
                </tr>
            </table>                         
       </form>
    </body>
</html>